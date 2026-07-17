package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.solr.document.GroupPayrunSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.GroupPayrunSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipTable;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_GROUP_PAYRUN_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:28.
 */
@Component
public class GroupPayrunSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(GroupPayrunSolrComponent.class);

    @Autowired
    GroupPayrunSolrDocRepository groupPayrunSolrDocRepository;
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("payrollService")
    private PayrollServiceLocal payrollService;
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
    public void index(EdsPayslipTable payslipTable) throws InterruptedException {
        this.indexes(Arrays.asList(payslipTable));
    }

    @Transactional
    public void indexes(List<EdsPayslipTable> payslipTable) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(payslipTable)) {
            List<GroupPayrunSolrDoc> groupPayrunSolrDocs = new ArrayList<>();
            for (EdsPayslipTable edsPayslipItem : payslipTable) {
                if (Objects.nonNull(edsPayslipItem)) {
                    try {
                        groupPayrunSolrDocs.add(createGroupPayrunDocument(edsPayslipItem.getRPC(), companyId));
                        log.info("Indexed GroupPayrun Core CID - {}, objId - {}", companyId, edsPayslipItem.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* ERROR occurred while creating solr doc GroupPayrun = {} **********************", edsPayslipItem.getName());
                        throw e;
                    }
                }
            }

            if (!groupPayrunSolrDocs.isEmpty()) {
                log.info("========= Create GroupPayrun solr docs for company {} with size {} =========", companyId, groupPayrunSolrDocs.size());
                groupPayrunSolrDocRepository.saveAll(groupPayrunSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsPayslipTable> payslipTable) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(payslipTable)) {
            ConcurrentLinkedQueue<GroupPayrunSolrDoc> groupPayrunSolrDocs = new ConcurrentLinkedQueue<>();

            String database = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();

            for (EdsPayslipTable edsPayslipItem : payslipTable) {
                if (Objects.nonNull(edsPayslipItem)) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(database);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            PayslipTable rpc = edsPayslipItem.getRPC();
                            sync.execute(getSynchronizedKey(rpc),
                                    () -> {
                                        groupPayrunSolrDocs.add(createGroupPayrunDocument(rpc, companyId));
                                        log.info("Indexed GroupPayrun Core CID - {}, objId - {}", companId, edsPayslipItem.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* ERROR occurred while creating solr doc GroupPayrun = {} **********************", edsPayslipItem.getName());
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> invokedTasks = executor.invokeAll(tasks);
                for (Future<Void> invokedTask : invokedTasks) {
                    try {
                        invokedTask.get();
                    } catch (ExecutionException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Leave Request list", e);
            }

            if (!groupPayrunSolrDocs.isEmpty()) {
                log.info("========= Create GroupPayrun solr docs for company {} with size {} =========", companyId, groupPayrunSolrDocs.size());
                try {
                    groupPayrunSolrDocRepository.saveAll(groupPayrunSolrDocs);
                } catch (Exception e) {
                    log.error("Error indexing payslip table solr docs", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(PayslipTable rpc) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + rpc.getId();
    }

    private GroupPayrunSolrDoc createGroupPayrunDocument(PayslipTable payslipTable, Integer companyId) {
        GroupPayrunSolrDoc groupPayrunSolrDoc = new GroupPayrunSolrDoc();

        groupPayrunSolrDoc.setOid(SolrUtils.generatedOId(companyId, payslipTable.getId()));
        groupPayrunSolrDoc.setCompanyId(companyId);
        groupPayrunSolrDoc.setGroupPayrunId(payslipTable.getId());
        groupPayrunSolrDoc.setCreationDate(payslipTable.getCreationDate());

        if (payslipTable.getPreparer() != null) {
            groupPayrunSolrDoc.setPreparerId(payslipTable.getPreparer().getId());
            groupPayrunSolrDoc.setPreparerName(payslipTable.getPreparer().getName());
            groupPayrunSolrDoc.setPreparerIdName(SolrUtils.getIdName(payslipTable.getPreparer().getId(), payslipTable.getPreparer().getName()));
        }
        if (payslipTable.getStatus() != null) {
            groupPayrunSolrDoc.setStatusId(payslipTable.getStatus().getObjectID());
            groupPayrunSolrDoc.setStatusCode(payslipTable.getStatus().getCode());
            groupPayrunSolrDoc.setStatusName(payslipTable.getStatus().getName());
            groupPayrunSolrDoc.setStatusIdName(SolrUtils.getIdName(payslipTable.getStatus().getObjectID(), payslipTable.getStatus().getName()));
        }
        if (payslipTable.getApprover() != null) {
            groupPayrunSolrDoc.setApproverId(payslipTable.getApprover().getId());
            groupPayrunSolrDoc.setApproverName(payslipTable.getApprover().getName());
            groupPayrunSolrDoc.setApproverIdName(SolrUtils.getIdName(payslipTable.getApprover().getId(), payslipTable.getApprover().getName()));
        }

        groupPayrunSolrDoc.setLastUpdate(payslipTable.getLastUpdateTime());
        groupPayrunSolrDoc.setApprovedDate(payslipTable.getApprovedDate());
        groupPayrunSolrDoc.setProcessDate(payslipTable.getProcessDate());
        groupPayrunSolrDoc.setMonthId(payslipTable.getMonth().getId());
        groupPayrunSolrDoc.setMonthName(payslipTable.getMonth().getName());
        groupPayrunSolrDoc.setMonthIdName(SolrUtils.getIdName(payslipTable.getMonth().getId(), payslipTable.getMonth().getName()));
        groupPayrunSolrDoc.setYear(payslipTable.getYear());
        groupPayrunSolrDoc.setTotal(payslipTable.getTotalAmount().doubleValue());
        groupPayrunSolrDoc.setTotalInBase(payslipTable.getTotalInBase().doubleValue());

        if (payslipTable.getPayrollBatch() != null) {
            groupPayrunSolrDoc.setPayrollBatchId(payslipTable.getPayrollBatch().getId());
            groupPayrunSolrDoc.setPayrollBatchName(payslipTable.getPayrollBatch().getName());
        }
        if (payslipTable.getPaymentMethod() != null) {
            groupPayrunSolrDoc.setPaymentMethodName(payslipTable.getPaymentMethod().getName());
        }
        if (payslipTable.getCurrency() != null) {
            groupPayrunSolrDoc.setForeignCurrencyName(payslipTable.getCurrency().getName());
        }
        if (payslipTable.getProject() != null) {
            groupPayrunSolrDoc.setProjectId(payslipTable.getProject().getId());
            groupPayrunSolrDoc.setProjectName(payslipTable.getProject().getName());
        }
        if (payslipTable.getLocation() != null) {
            groupPayrunSolrDoc.setLocationId(payslipTable.getLocation().getID());
            groupPayrunSolrDoc.setLocationName(payslipTable.getLocation().getName());
        }
        groupPayrunSolrDoc.setDeleted(payslipTable.getDeleted());
        groupPayrunSolrDoc.setFrequency(payslipTable.getFrequency());
        groupPayrunSolrDoc.setPension(payslipTable.getPension() != null ? payslipTable.getPension().doubleValue() : 0d);
        groupPayrunSolrDoc.setDeduction(payslipTable.getDeduction() != null ? payslipTable.getDeduction().doubleValue() : 0d);
        groupPayrunSolrDoc.setExpense(payslipTable.getExpense() != null ? payslipTable.getExpense().doubleValue() : 0d);
        return groupPayrunSolrDoc;
    }

    public FacetFilterRpc getGroupPayrunFacetFilterData(FacetFilterRpc groupPayrunFacet) {

        if (!groupPayrunFacet.isFilterChanges()) {
            groupPayrunFacet = commonServiceLocal.getUserFacetFilter(groupPayrunFacet);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(groupPayrunFacet.getSearchKey());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getGroupPayrunSolrQuery(fp));
        solrQuery.append(payrollService.generatePermissionQuery(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(groupPayrunFacet, company, null, null));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_GROUP_PAYRUN_CORE, solrQuery.toString(), groupPayrunFacet, GroupPayrunSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, groupPayrunFacet);
        return groupPayrunFacet;
    }

    public Page<GroupPayrunSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrGroupPayrunRepresenter.FIELD_PROCESS_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case GroupPayrunData.PERIOD ->
                            solrSort = Sort.by(Arrays.asList(new Sort.Order(order, SolrGroupPayrunRepresenter.FIELD_YEAR), new Sort.Order(order, SolrGroupPayrunRepresenter.FIELD_MONTH_ID)));
                    case GroupPayrunData.APPROVER ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.SORTABLE_APPROVER_NAME);
                    case GroupPayrunData.PREPARER ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.SORTABLE_PREPARER_NAME);
                    case GroupPayrunData.STATUS ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_STATUS_NAME);
                    case GroupPayrunData.TOTAL_AMOUNT ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_TOTAL);
                    case GroupPayrunData.TOTAL_IN_BASE ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_TOTAL_IN_BASE);
                    case GroupPayrunData.CURRENCY_NAME ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_FOREIGN_CURRENCY_NAME);
                    case GroupPayrunData.PAYMENT_METHOD ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_PAYMENT_METHOD_NAME);
                    case GroupPayrunData.PROCESS_DATE ->
                            solrSort = Sort.by(order, SolrGroupPayrunRepresenter.FIELD_PROCESS_DATE);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_GROUP_PAYRUN_CORE, query, GroupPayrunSolrDoc.class);
    }
}
