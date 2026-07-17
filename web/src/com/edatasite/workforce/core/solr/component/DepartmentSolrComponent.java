package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsDepartmentCustomFields;
import com.edatasite.workforce.core.solr.document.DepartmentSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.DepartmentSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.team.client.rpc.TeamSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_DEPARTMENT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

@Component
public class DepartmentSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(DepartmentSolrComponent.class);

    @Autowired
    private DepartmentSolrDocRepository repository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    EmployeeManager employeeManager;
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
    public void index(EdsDepartment department) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Collections.singletonList(department));
    }

    @Transactional
    public void indexes(List<EdsDepartment> departments) throws SolrServerException, IOException, InterruptedException {

        if (!CollectionUtils.isEmpty(departments)) {
            List<DepartmentSolrDoc> departmentSolrDocs = new ArrayList<>();

            String companyId = ServerSecurityContext.getInstance().getCompanyId();
            for (EdsDepartment department : departments) {
                if (department != null) {
                    try {
                        SelectItem item = departmentTreeManager.getParentItemByChildId(department.getObjectID());
                        EdsReferenceLocale locale = null;
                        if (item != null && item.getId() != null) {
                            locale = departmentManager.getDeparmentLocalization(item.getId());
                        }
                        if (department != null) {
                            departmentSolrDocs.add(createDepartmentDocument(department.getSolrRPC(), Integer.valueOf(companyId), employeeManager.getEmployeesCountByDepartment(department), item, locale, department.getCustomFields()));
                            log.info("Indexed Department Core CID - {}, objId - {}", companyId, department.getObjectID());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on Department with id {}, and error message {} **********************", department.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!departmentSolrDocs.isEmpty()) {
                log.info("========= Create Department solr docs for company {} with size {} =========", companyId, departmentSolrDocs.size());
                repository.saveAll(departmentSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsDepartment> departments) throws SolrServerException, IOException, InterruptedException {
        if (!CollectionUtils.isEmpty(departments)) {
            ConcurrentLinkedQueue<DepartmentSolrDoc> departmentSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsDepartment department : departments) {
                if (department != null) {
                    TeamSolrItem solrRPC = department.getSolrRPC();
                    EdsDepartmentCustomFields customFields = department.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        SelectItem item = departmentTreeManager.getParentItemByChildId(department.getObjectID());
                                        EdsReferenceLocale locale = null;
                                        if (item != null && item.getId() != null) {
                                            locale = departmentManager.getDeparmentLocalization(item.getId());
                                        }
                                        if (department != null) {
                                            departmentSolrDocs.add(createDepartmentDocument(solrRPC, Integer.valueOf(companyId), employeeManager.getEmployeesCountByDepartment(department), item, locale, customFields));
                                            log.info("Indexed Department Core CID - {}, objId - {}", companyId, department.getObjectID());
                                        }
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Department with id {}, and error message {} **********************", department.getObjectID(), e.getMessage());
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
                log.error("Error on loading Department list", e);
            }

            if (!departmentSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Department solr docs for company {} with size {} =========", companyId, departmentSolrDocs.size());
                    repository.saveAll(departmentSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Department list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(TeamSolrItem department) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + department.getObjectId();
    }


    private DepartmentSolrDoc createDepartmentDocument(TeamSolrItem department, Integer companyId, Long headCount, SelectItem parentDepartment, EdsReferenceLocale parentLocale, EdsCustomFields customFields) {
        DepartmentSolrDoc departmentSolrDoc = new DepartmentSolrDoc();
        departmentSolrDoc.setOid(SolrUtils.generatedOId(companyId, department.getObjectId()));
        departmentSolrDoc.setCompanyId(companyId);
        departmentSolrDoc.setDepartmentId(department.getObjectId());
        departmentSolrDoc.setName(department.getName());
        departmentSolrDoc.setNumber(department.getNumber());
        if (department.getName() != null) {
            departmentSolrDoc.setNameEn(department.getNameEn() != null ? department.getNameEn() : department.getName());
            departmentSolrDoc.setNameRu(department.getNameRu() != null ? department.getNameRu() : department.getName());
            departmentSolrDoc.setNameAr(department.getNameAr() != null ? department.getNameAr() : department.getName());
            departmentSolrDoc.setNameUz(department.getNameUz() != null ? department.getNameUz() : department.getName());
        }
        departmentSolrDoc.setStartDate(department.getStartDate());
        departmentSolrDoc.setEncryptedId(EncryptionHelper.encryptURL("department/" + department.getObjectId().toString()));
        departmentSolrDoc.setHeadCount(headCount.toString());
        departmentSolrDoc.setStatusName(department.getStatusName());

        if (parentDepartment != null) {
            departmentSolrDoc.setParentDepartmentId(parentDepartment.getId());
            departmentSolrDoc.setParentDepartmentName(parentDepartment.getName());
            departmentSolrDoc.setParentDepartmentIdName(parentDepartment.getId() + SolrDepartmentRepresenter.SPLIT + (parentDepartment.getCode() + "->" + parentDepartment.getName()));
            if (parentLocale != null) {
                departmentSolrDoc.setParentDepartmentNameEn(parentLocale.getEnglish() != null ? parentLocale.getEnglish() : parentDepartment.getName());
                departmentSolrDoc.setParentDepartmentNameRu(parentLocale.getRussian() != null ? parentLocale.getRussian() : parentDepartment.getName());
                departmentSolrDoc.setParentDepartmentNameAr(parentLocale.getArabic() != null ? parentLocale.getArabic() : parentDepartment.getName());
                departmentSolrDoc.setParentDepartmentNameUz(parentLocale.getUzbek() != null ? parentLocale.getUzbek() : parentDepartment.getName());
            }
        }

        if (department.getLocation() != null) {
            departmentSolrDoc.setLocationId(department.getLocation().getId());
            departmentSolrDoc.setLocationName(department.getLocation().getName());
            departmentSolrDoc.setLocationIdName(department.getLocation().getId() + SolrDepartmentRepresenter.SPLIT + (department.getLocation().getCode() + "->" + department.getLocation().getName()));
        }

        if (department.getLeader() != null) {
            departmentSolrDoc.setLeaderId(department.getLeader().getId());
            departmentSolrDoc.setLeaderName(department.getLeader().getName());
            departmentSolrDoc.setLeaderIdName(department.getLeader().getId() + SolrDepartmentRepresenter.SPLIT + department.getLeader().getName());
        }
        departmentSolrDoc.setLeaderIsVacant(department.getLeaderIsVacant());

        if (department.getCreatedBy() != null) {
            departmentSolrDoc.setCreatedDate(department.getCreatedDate());
            departmentSolrDoc.setCreatedById(department.getCreatedBy().getId());
            departmentSolrDoc.setCreatedByName(department.getCreatedBy().getName());
            departmentSolrDoc.setCreatedByIdName(department.getCreatedBy().getId() + SolrPositionRepresenter.SPLIT + department.getCreatedBy().getName());
        }

        if (department.getModifiedBy() != null) {
            departmentSolrDoc.setModifiedDate(department.getModifiedDate());
            departmentSolrDoc.setModifiedById(department.getModifiedBy().getId());
            departmentSolrDoc.setModifiedByName(department.getModifiedBy().getName());
            departmentSolrDoc.setModifiedByIdName(department.getModifiedBy().getId() + SolrPositionRepresenter.SPLIT + department.getModifiedBy().getName());
        }
        CustomFieldsUtils.setSolrDocDynamicFields(departmentSolrDoc, customFields);


        return departmentSolrDoc;
    }


    public Page<DepartmentSolrDoc> getList(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(QueryBuilderForSolr.getDepartmentListCore(fp, user, company)));

        FacetFilterRpc departmentFacetFilter = fp.getFacetFilter();
        if (departmentFacetFilter != null && !departmentFacetFilter.isFilterChanges()) {
            departmentFacetFilter = commonServiceLocal.getUserFacetFilter(departmentFacetFilter);
        }
        if (fp.getLocationId() == null) {
            fp.setLocationId(user.getLocation() != null ? user.getLocation().getObjectID() : null);
        }
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID);

        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case "name" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_NAME);
                    case "code" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_NUMBER);
                    case "status" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_STATUS_NAME);
                    case "locationId" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_LOCATION_NAME);
                    case "createdDate" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_CREATED_DATE);
                    case "createdBy" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_CREATED_BY_NAME);
                    case "modifiedDate" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_MODIFIED_DATE);
                    case "modifiedBy" -> solrSort = Sort.by(order, SolrDepartmentRepresenter.SORTABLE_MODIFIED_BY_NAME);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_DEPARTMENT_CORE, query, DepartmentSolrDoc.class);
    }

    public FacetFilterRpc getDepartmentFacetFilterData(FacetFilterRpc facetFilterRpc) {
        EdsUser user = userManager.getUser();
        if (!facetFilterRpc.isFilterChanges()) {
            facetFilterRpc = commonServiceLocal.getUserFacetFilter(facetFilterRpc);
        }
        EdsCompany company = user.getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(facetFilterRpc.getSearchKey());
        fp.setViewType(facetFilterRpc.getName());

        String solrQuery = getDepartmentFacetQuery(fp, user.getCompany().getObjectID()) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilterRpc, company, null, null);
        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_DEPARTMENT_CORE, solrQuery, facetFilterRpc, DepartmentSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, facetFilterRpc);
        return facetFilterRpc;
    }


    public String getDepartmentFacetQuery(final ListingFilterParameter fp, final Integer companyId) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrDepartmentRepresenter.FIELD_COMPANY_ID).append(":").append(companyId);

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ").append(SolrDepartmentRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            if (!fp.isLookUp()) {
                final SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getDepartmentSearchFields(), fp.getSearchKey());
            }
            sql.append(")");
        }
        return sql.toString();
    }

    private static Map<String, Double> getDepartmentSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrDepartmentRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_LOCATION_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_CREATED_BY_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrDepartmentRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }


}
