package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.solr.document.CaseSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.CaseSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CASE_CORE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
@Component
public class CaseSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CaseSolrComponent.class);

    @Autowired
    private CaseSolrDocRepository caseSolrDocRepository;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCase edsCase) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsCase));
    }

    @Transactional
    public void indexes(List<EdsCase> edsCaseList) throws InterruptedException {

        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCaseList)) {
            List<CaseSolrDoc> caseSolrDocs = new ArrayList<>();

            for (EdsCase edsCase : edsCaseList) {
                if (edsCase != null && !edsCase.getDeleted()) {
                    try {
                        caseSolrDocs.add(createCaseDocument(edsCase.getSolrRPC(), companyID, edsCase.getCustomFields()));
                        log.info("Indexed Case Core CID - {}, objId - {}", companyID, edsCase.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* CASE = {}, {} **********************", edsCase.getCaseNumberString(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!caseSolrDocs.isEmpty()) {
                log.info("========= Created CASE solr docs for company {} with size {} =========", companyID, caseSolrDocs.size());
                caseSolrDocRepository.saveAll(caseSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCase> edsCaseList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCaseList)) {
            ConcurrentLinkedQueue<CaseSolrDoc> caseSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCase edsCase : edsCaseList) {
                if (edsCase != null && !edsCase.getDeleted()) {
                    Hibernate.initialize(edsCase.getTracker());
                    Hibernate.initialize(edsCase.getAssignee());
                    Hibernate.initialize(edsCase.getDepartment());
                    Hibernate.initialize(edsCase.getCaseOrigion());
                    Hibernate.initialize(edsCase.getType());
                    Hibernate.initialize(edsCase.getCaseReason());
                    Hibernate.initialize(edsCase.getPriority());
                    Hibernate.initialize(edsCase.getStatus());
                    Hibernate.initialize(edsCase.getResolver());
                    Hibernate.initialize(edsCase.getLead());
                    Hibernate.initialize(edsCase.getCrmAccount());
                    Hibernate.initialize(edsCase.getCrmContact());
                    Hibernate.initialize(edsCase.getInternalStatus());
                    Hibernate.initialize(edsCase.getCustomFields());
                    CaseSolrItem rpc = edsCase.getSolrRPC();
                    EdsCrmCustomFields customFields = edsCase.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyID);
                            sync.execute(getSynchronizedKey(rpc), () -> {
                                caseSolrDocs.add(createCaseDocument(rpc, companyID, customFields));
                                log.info("Indexed Case Core CID - {}, objId - {}", companyID, edsCase.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* CASE = {}, {} **********************", edsCase.getCaseNumberString(), e.getMessage());
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
                log.error("Error on loading CASE list", e);
            }

            if (!caseSolrDocs.isEmpty()) {
                try {
                    log.info("========= Created CASE solr docs for company {} with size {} =========", companyID, caseSolrDocs.size());
                    caseSolrDocRepository.saveAll(caseSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving CASE list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(CaseSolrItem edsCase) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + edsCase.getObjectID();
    }

    private CaseSolrDoc createCaseDocument(CaseSolrItem item, Integer companyID, EdsCustomFields customFields) {
        CaseSolrDoc caseSolrDoc = new CaseSolrDoc();

        caseSolrDoc.setOid(SolrUtils.generatedOId(companyID, item.getObjectID()));
        caseSolrDoc.setCompanyId(companyID);
        caseSolrDoc.setCaseId(item.getObjectID());
        if (item.getCaseTrackerId() != null) {
            caseSolrDoc.setCaseTrackerId(item.getCaseTrackerId());
        }
        caseSolrDoc.setCaseEmailId(item.getCaseEmailId());
        caseSolrDoc.setCaseEmail(item.getCaseEmail());
        caseSolrDoc.setCasePhone(item.getCasePhone());
        caseSolrDoc.setCaseSubject(item.getCaseSubject());
        caseSolrDoc.setCaseNumber(item.getCaseNumber());
        if (item.getCaseAssignee() != null) {
            caseSolrDoc.setCaseAssigneeId(item.getCaseAssignee().getId());
            caseSolrDoc.setCaseAssignee(item.getCaseAssignee().getName());
            caseSolrDoc.setCaseAssigneeIdName(SolrUtils.getIdName(item.getCaseAssignee().getId(), item.getCaseAssignee().getName()));
        }
        if (item.getCaseDepartment() != null) {
            caseSolrDoc.setCaseDepartmentId(item.getCaseDepartment().getId());
            caseSolrDoc.setCaseDepartment(item.getCaseDepartment().getName());
            caseSolrDoc.setCaseDepartmentIdName(SolrUtils.getIdName(item.getCaseDepartment().getId(), item.getCaseDepartment().getName()));
        }
        if (item.getCaseOrigin() != null) {
            caseSolrDoc.setCaseOriginId(item.getCaseOrigin().getId());
            caseSolrDoc.setCaseOriginName(item.getCaseOrigin().getName());
            caseSolrDoc.setCaseOriginIdName(SolrUtils.getIdName(item.getCaseOrigin().getId(), item.getCaseOrigin().getName()));
            caseSolrDoc.setCaseOriginCode(item.getCaseOrigin().getCode());
            caseSolrDoc.setCaseOriginIdCodeName(SolrUtils.getIdCodeName(item.getCaseOrigin().getId(), item.getCaseOrigin().getCode(), item.getCaseOrigin().getName()));
        }
        if (item.getCaseType() != null) {
            caseSolrDoc.setCaseTypeId(item.getCaseType().getId());
            caseSolrDoc.setCaseTypeName(item.getCaseType().getName());
            caseSolrDoc.setCaseTypeIdName(SolrUtils.getIdName(item.getCaseType().getId(), item.getCaseType().getName()));
            caseSolrDoc.setCaseTypeCode(item.getCaseType().getCode());
            caseSolrDoc.setCaseTypeIdCodeName(SolrUtils.getIdCodeName(item.getCaseType().getId(), item.getCaseType().getCode(), item.getCaseType().getName()));
        }
        if (item.getCaseReason() != null) {
            caseSolrDoc.setCaseReasonId(item.getCaseReason().getId());
            caseSolrDoc.setCaseReasonName(item.getCaseReason().getName());
            caseSolrDoc.setCaseReasonIdName(SolrUtils.getIdName(item.getCaseReason().getId(), item.getCaseReason().getName()));
        }
        if (item.getPriority() != null) {
            caseSolrDoc.setPriorityId(item.getPriority().getId());
            caseSolrDoc.setPriorityName(item.getPriority().getName());
            caseSolrDoc.setPriorityIdName(SolrUtils.getIdName(item.getPriority().getId(), item.getPriority().getName()));
            caseSolrDoc.setPriorityCode(item.getPriority().getCode());
            caseSolrDoc.setPriorityIdCodeName(SolrUtils.getIdCodeName(item.getPriority().getId(), item.getPriority().getCode(), item.getPriority().getCode()));
            caseSolrDoc.setPriorityColor(item.getPriority().getColorName());
            caseSolrDoc.setPrioritySorder(item.getPriority().getOrderId());
        }
        if (item.getStatus() != null) {
            caseSolrDoc.setStatusId(item.getStatus().getObjectID());
            caseSolrDoc.setStatusName(item.getStatus().getName());
            caseSolrDoc.setStatusIdName(SolrUtils.getIdName(item.getStatus().getObjectID(), item.getStatus().getName()));
            caseSolrDoc.setStatusCode(item.getStatus().getCode());
            caseSolrDoc.setStatusIdCodeName(SolrUtils.getIdCodeName(item.getStatus().getObjectID(), item.getStatus().getCode(), item.getStatus().getName()));
            caseSolrDoc.setStatusSorder(item.getStatus().getOrder());
        }
        if (item.getResolver() != null) {
            caseSolrDoc.setResolverId(item.getResolver().getId());
            caseSolrDoc.setResolverName(item.getResolver().getName());
            caseSolrDoc.setResolverIdName(SolrUtils.getIdName(item.getResolver().getId(), item.getResolver().getName()));
        }
        if (item.getEntityId() != null) {
            caseSolrDoc.setEntityId(item.getEntityId());
        }
        if (item.getLeadId() != null) {
            caseSolrDoc.setLeadId(item.getLeadId());
        }
        if (item.getAccountId() != null) {
            caseSolrDoc.setAccountId(item.getAccountId());
        }
        if (item.getRelatedToId() != null) {
            caseSolrDoc.setRelatedToId(item.getRelatedToId());
        }
        caseSolrDoc.setInTrash(item.getInTrash());
        if (StringUtils.isNotBlank(item.getReportedBy())) {
            caseSolrDoc.setReportedBy(item.getReportedBy());
        } else if (StringUtils.isNotBlank(item.getCaseEmail())) {
            caseSolrDoc.setReportedBy(item.getCaseEmailId());
        }
        if (item.getCreateDate() != null) {
            caseSolrDoc.setCreateDate(item.getCreateDate());
            caseSolrDoc.setLastUpdatedDate(item.getCreateDate());
        }
        caseSolrDoc.setInternalUpdatedDate(item.getInternalUpdatedDate());
        if (item.getInternalStatus() != null) {
            caseSolrDoc.setInternalStatusId(item.getInternalStatus().getId());
            caseSolrDoc.setInternalStatusName(item.getInternalStatus().getName());
            caseSolrDoc.setInternalStatusIdName(SolrUtils.getIdName(item.getInternalStatus().getId(), item.getInternalStatus().getName()));
            caseSolrDoc.setInternalStatusSorder(item.getInternalStatus().getOrder());
        }
        caseSolrDoc.setKanbanOrder(item.getKanbanOrder());

        List<EdsRelation> relationList = relationManager.getAllRelations(EdsRelation.TYPE_CASE, item.getObjectID());
        SolrRelationUtils.addToRelationBaseSolrDoc(caseSolrDoc, relationList, EdsRelation.TYPE_CASE);
        CustomFieldsUtils.setSolrDocDynamicFields(caseSolrDoc, customFields);

        return caseSolrDoc;
    }

    @Transactional
    public Page<CaseSolrDoc> getList(ListingFilterParameter fp, String solrQuery, boolean isGroup) {
        StringBuilder caseSolrQuery = new StringBuilder(solrQuery);
        EdsUser user = caseManager.getUser();
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CASES_LIST) && !user.isClientContact()) {
            caseSolrQuery.append(" AND (");
            caseSolrQuery.append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":").append(user.getObjectID());
            caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.RESOLVER_ID).append(":").append(user.getObjectID());
            EdsEmployee employee = employeeManager.get(user.getObjectID());
            if (employee != null && employee.getTeam() != null) {
                caseSolrQuery.append(" OR (").append(SolrCaseRepresenter.CASE_DEPARTMENT_ID).append(":").append(employee.getTeam().getObjectID());
                caseSolrQuery.append(" AND (-").append(SolrCaseRepresenter.CASE_ASSIGNEE_ID).append(":[* TO *] AND *:*))");
            }
            caseSolrQuery.append(" )");
        }
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(caseSolrQuery.toString()));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCaseRepresenter.LAST_UPDATE_DATE);
        if (!fp.isSearchButton()) {
            if (StringUtils.isNotEmpty(fp.getSortField())) {

                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                Sort searchSort = switch (fp.getSortField()) {
                    case CaseItem.CASE_ID -> Sort.by(order, SolrCaseRepresenter.CASE_ID);
                    case CaseItem.SUBJECT -> Sort.by(order, SolrCaseRepresenter.SORTABLE_CASE_SUBJECT);
                    case CaseItem.PRIORITY -> Sort.by(order, SolrCaseRepresenter.PRIORITY_SORDER);
                    case CaseItem.REPORTED_BY -> Sort.by(order, SolrCaseRepresenter.SORTABLE_REPORTED_BY);
                    case CaseItem.CREATED_DATE -> Sort.by(order, SolrCaseRepresenter.CREATE_DATE);
                    case CaseItem.LAST_UPDATED_DATE -> Sort.by(order, SolrCaseRepresenter.LAST_UPDATE_DATE);
                    case CaseItem.ASSIGNED_TO -> Sort.by(order, SolrCaseRepresenter.SORTABLE_CASE_ASSIGNEE);
                    case CaseItem.STATUS -> Sort.by(order, SolrCaseRepresenter.STATUS_SORDER);
                    case CaseItem.INTERNAL_STATUS -> Sort.by(order, SolrCaseRepresenter.INTERNAL_STATUS_SORDER);
                    case CaseItem.INTERNAL_UPDATED_DATE -> Sort.by(order, SolrCaseRepresenter.INTERNAL_UPDATED_DATE);
                    case CaseItem.KANBAN_ORDER -> Sort.by(order, SolrCaseRepresenter.KANBAN_ORDER);
                    default -> null;
                };

                if (searchSort == null) {
                    solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), true);
                } else {
                    solrSort = searchSort;
                }
            }
        }

        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), fp.getLimit(), solrSort));

        return solrTemplate.query(SOLR_CASE_CORE, query, CaseSolrDoc.class);
    }

    public FacetFilterRpc getCaseFacetFilterData(FacetFilterRpc caseFacetData) {
        if (!caseFacetData.isFilterChanges()) {
            caseFacetData = commonServiceLocal.getUserFacetFilter(caseFacetData);
        }

        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();

        fp.setSearchKey(caseFacetData.getSearchKey());
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(this.commonServiceLocal.getCrmCaseSolrQuery(fp, company, caseFacetData));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(caseFacetData, company, null, null));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CASE_CORE, solrQuery.toString(), caseFacetData, CaseSolrDoc.class);

        return SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, caseFacetData);
    }

    public ArrayList<SelectItem> getCrmLookNamesForCase(Page<CaseSolrDoc> caseSolrDocPage, ListingFilterParameter filterParametrs) throws SolrServerException, IOException {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (caseSolrDocPage != null && caseSolrDocPage.getContent() != null && caseSolrDocPage.getContent().size() > 0) {
            for (int i = 0, resultsSize = caseSolrDocPage.getContent().size(); i < resultsSize; i++) {
                CaseSolrDoc caseSolrDoc = caseSolrDocPage.getContent().get(i);
                if (filterParametrs.isWithCode()) {
                    String number = caseSolrDoc.getCaseNumber();
                    String name = (!"".equals(number) ? number + " -> " + caseSolrDoc.getCaseSubject() : "");
                    SelectItem item = new SelectItem(caseSolrDoc.getCaseId(), name, number);
                    item.setReferenceCode(number);
                    item.setCode(number);
                    selectItems.add(item);
                } else {
                    selectItems.add(new SelectItem(caseSolrDoc.getCaseId(), caseSolrDoc.getCaseSubject()));
                }
            }
        }
        return selectItems;
    }
}
