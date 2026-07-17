package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.solr.document.PositionSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.PositionSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.PositionSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_POSITION_CORE;

@Component
public class PositionSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(PositionSolrComponent.class);

    @Autowired
    private PositionSolrDocRepository repository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;


    @Transactional
    public void index(EdsPosition position) throws InterruptedException {
        this.indexes(Collections.singletonList(position));

    }

    @Transactional
    public void indexes(List<EdsPosition> positions) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(positions)) {
            List<PositionSolrDoc> positionSolrDocs = new ArrayList<>();

            for (EdsPosition position : positions) {
                if (position != null) {
                    try {
                        Long positionEmployeeCount = employeeManager.getPositionEmployeeCount(position.getObjectID());
                        positionSolrDocs.add(createPositionDocument(position.getSolrRPC(), companyId, positionEmployeeCount));
                        log.info("Indexed Position Core CID - {}, objId - {}", companyId, position.getObjectID());
                    } catch (Exception e) {
                        log.info("********************* Position = {}, {} **********************", position.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!positionSolrDocs.isEmpty()) {
                log.info("========= Create Position solr docs for company {} with size {} =========", companyId, positionSolrDocs.size());
                repository.saveAll(positionSolrDocs);
            }

        }
    }

    @Transactional
    public void indexConcurrently(List<EdsPosition> positions) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(positions)) {
            ConcurrentLinkedQueue<PositionSolrDoc> positionSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsPosition position : positions) {
                if (position != null) {
                    PositionSolrItem solrRPC = position.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(position), () -> {
                                Long positionEmployeeCount = employeeManager.getPositionEmployeeCount(position.getObjectID());
                                positionSolrDocs.add(createPositionDocument(solrRPC, companyId, positionEmployeeCount));
                                        log.info("Indexed Position Core CID - {}, objId - {}", companyId, position.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* EdsPosition = {} **********************", position.getObjectID());
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Position list", e);
            }

            if (!positionSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Position solr docs for company {} with size {} =========", companyId, positionSolrDocs.size());
                    repository.saveAll(positionSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Position list", e);
                }
            }

        }
    }

    protected String getSynchronizedKey(EdsPosition position) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + position.getObjectID();
    }

    private PositionSolrDoc createPositionDocument(PositionSolrItem position, Integer companyId, Long positionEmployeeCount) {
        PositionSolrDoc positionSolrDoc = new PositionSolrDoc();
        positionSolrDoc.setOid(SolrUtils.generatedOId(companyId, position.getObjectId()));
        positionSolrDoc.setPositionId(position.getObjectId());
        positionSolrDoc.setCompanyId(companyId);
        positionSolrDoc.setName(position.getName());
        positionSolrDoc.setNumber(position.getNumber());
        positionSolrDoc.setEmployeeCount(String.valueOf(positionEmployeeCount));
        positionSolrDoc.setVacantCount(position.getVacantCount() != null ? position.getVacantCount() : 0);
        if (position.getCreatedBy() != null) {
            positionSolrDoc.setCreatedDate(position.getCreatedDate());
            positionSolrDoc.setCreatedById(position.getCreatedBy().getId());
            positionSolrDoc.setCreatedByName(position.getCreatedBy().getName());
            positionSolrDoc.setCreatedByIdName(position.getCreatedBy().getId() + SolrPositionRepresenter.SPLIT + position.getCreatedBy().getName());
        }

        positionSolrDoc.setModifiedDate(position.getModifiedDate());
        if (position.getModifiedBy() != null) {
            positionSolrDoc.setModifiedById(position.getModifiedBy().getId());
            positionSolrDoc.setModifiedByName(position.getModifiedBy().getName());
            positionSolrDoc.setModifiedByIdName(position.getModifiedBy().getId() + SolrPositionRepresenter.SPLIT + position.getModifiedBy().getName());
        }

        if (position.getNameLocale() != null) {
            ReferenceLocale locale = position.getNameLocale();
            positionSolrDoc.setNameEn(locale.getEnglish() != null ? locale.getEnglish() : position.getName());
            positionSolrDoc.setNameRu(locale.getRussian() != null ? locale.getRussian() : position.getName());
            positionSolrDoc.setNameUz(locale.getUzbek() != null ? locale.getUzbek() : position.getName());
            positionSolrDoc.setNameAr(locale.getArabic() != null ? locale.getArabic() : position.getName());
        }

        if (position.getDepartment() != null) {
            positionSolrDoc.setDepartmentId(position.getDepartment().getId());
            positionSolrDoc.setDepartmentName(position.getDepartment().getName());
            positionSolrDoc.setDepartmentIdName(position.getDepartment().getId() + SolrPositionRepresenter.SPLIT + position.getDepartment().getName());
            if (position.getDepartmentLocale() != null) {
                ReferenceLocale locale = position.getDepartmentLocale();
                String deptName = position.getDepartment().getName();
                positionSolrDoc.setDepartmentNameEn(locale.getEnglish() != null ? locale.getEnglish() : deptName);
                positionSolrDoc.setDepartmentNameRu(locale.getRussian() != null ? locale.getRussian() : deptName);
                positionSolrDoc.setDepartmentNameAr(locale.getArabic() != null ? locale.getArabic() : deptName);
                positionSolrDoc.setDepartmentNameUz(locale.getUzbek() != null ? locale.getUzbek() : deptName);
            }
        }

        if (position.getStatus() != null) {
            SelectItem status = position.getStatus();
            positionSolrDoc.setStatusId(status.getId());
            positionSolrDoc.setStatusName(status.getName());
            positionSolrDoc.setStatusCode(status.getCode());
            positionSolrDoc.setStatusIdName(status.getId() + SolrPositionRepresenter.SPLIT + status.getName());
            if (position.getStatusLocale() != null) {
                ReferenceLocale locale = position.getStatusLocale();
                positionSolrDoc.setStatusEn(locale.getEnglish() != null ? locale.getEnglish() : status.getName());
                positionSolrDoc.setStatusRu(locale.getRussian() != null ? locale.getRussian() : status.getName());
                positionSolrDoc.setStatusUz(locale.getUzbek() != null ? locale.getUzbek() : status.getName());
                positionSolrDoc.setStatusAr(locale.getArabic() != null ? locale.getArabic() : status.getName());
            }
        }

        if (position.getType() != null) {
            SelectItem type = position.getType();
            positionSolrDoc.setTypeId(type.getId());
            positionSolrDoc.setTypeName(type.getName());
            positionSolrDoc.setTypeCode(type.getCode());
            positionSolrDoc.setTypeIdName(type.getId() + SolrPositionRepresenter.SPLIT + type.getName());
            if (position.getTypeLocale() != null) {
                ReferenceLocale locale = position.getTypeLocale();
                positionSolrDoc.setTypeNameEn(locale.getEnglish() != null ? locale.getEnglish() : type.getName());
                positionSolrDoc.setTypeNameRu(locale.getRussian() != null ? locale.getRussian() : type.getName());
                positionSolrDoc.setTypeNameUz(locale.getUzbek() != null ? locale.getUzbek() : type.getName());
                positionSolrDoc.setTypeNameAr(locale.getArabic() != null ? locale.getArabic() : type.getName());
            }
        }

        if (position.getLocation() != null) {
            SelectItem location = position.getLocation();
            positionSolrDoc.setLocationId(location.getId());
            positionSolrDoc.setLocationName(location.getCode() != null ? location.getCode() + "->" + location.getName() : location.getName());
            positionSolrDoc.setLocationIdName(location.getId() + SolrPositionRepresenter.SPLIT + location.getName());
        }
        return positionSolrDoc;
    }

    public Page<PositionSolrDoc> getList(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();

        FacetFilterRpc positionFacetFilter = fp.getFacetFilter();
        if (positionFacetFilter != null && !positionFacetFilter.isFilterChanges()) {
            positionFacetFilter = commonServiceLocal.getUserFacetFilter(positionFacetFilter);
        }

        fp.setLocationId(user.getLocation() != null ? user.getLocation().getObjectID() : null);
        String solrQuery = null;
        if (user != null && user.getCompany().getObjectID() != null) {
            solrQuery = QueryBuilderForSolr.getPositionListCore(fp, user, company) +
                    SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(positionFacetFilter, company, null, null);
        }
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrPositionRepresenter.FIELD_POSITION_ID);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case "name" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_NAME);
                    case "code" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_NUMBER);
                    case "status" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_STATUS_NAME);
                    case "locationId" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_LOCATION_NAME);
                    case "createdDate" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_CREATED_DATE);
                    case "createdBy" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_CREATED_BY_NAME);
                    case "modifiedDate" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_MODIFIED_DATE);
                    case "modifiedBy" -> solrSort = Sort.by(order, SolrPositionRepresenter.SORTABLE_MODIFIED_BY_NAME);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_POSITION_CORE, query, PositionSolrDoc.class);
    }

    public FacetFilterRpc getPositionFacetFilterData(FacetFilterRpc facetFilterRpc) {
        EdsUser user = userManager.getUser();
        if (!facetFilterRpc.isFilterChanges()) {
            facetFilterRpc = commonServiceLocal.getUserFacetFilter(facetFilterRpc);
        }
        EdsCompany company = user.getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(facetFilterRpc.getSearchKey());
        fp.setViewType(facetFilterRpc.getName());
        fp.setDepartmentId(facetFilterRpc.getDepartmentId());

        String solrQuery = getPositionFacetQuery(fp, user) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilterRpc, company, null, null);
        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_POSITION_CORE, solrQuery, facetFilterRpc, PositionSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, facetFilterRpc, facetFilterRpc.getSolrFieldMapCodeList());

        return facetFilterRpc;
    }


    public String getPositionFacetQuery(final ListingFilterParameter fp, final EdsUser user) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrPositionRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ").append(SolrPositionRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            if (!fp.isLookUp()) {
                final SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getPositionSearchFields(), fp.getSearchKey());
            }
            sql.append(")");
        }
        return sql.toString();
    }

    private static Map<String, Double> getPositionSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPositionRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_LOCATION_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_CREATED_BY_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_UZ, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_RU, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_AR, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_EN, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_STATUS_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }


}
