package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.solr.document.CashAdvanceSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.CashAdvanceSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceSolrItem;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CASH_ADVANCE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
@Component
public class CashAdvanceSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CashAdvanceSolrComponent.class);

    @Autowired
    private CashAdvanceSolrDocRepository cashAdvanceSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    @Qualifier("payrollService")
    private PayrollServiceLocal payrollService;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCashAdvance edsCashAdvance) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsCashAdvance));
    }

    @Transactional
    public void indexes(List<EdsCashAdvance> edsCashAdvanceList) throws SolrServerException, IOException, InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCashAdvanceList)) {
            List<CashAdvanceSolrDoc> cashAdvanceSolrDocs = new ArrayList<>();

            for (EdsCashAdvance edsCashAdvance : edsCashAdvanceList) {
                if (Objects.nonNull(edsCashAdvance) && !Boolean.TRUE.equals(edsCashAdvance.getDeleted())) {
                    try {
                        cashAdvanceSolrDocs.add(createCashAdvanceDocument(edsCashAdvance.getSolrRPC(), companyId));
                        log.info("Indexed CashAdvance Core CID - {}, objId - {}", companyId, edsCashAdvance.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on Cash Advance with number = {} **********************, error: {}", edsCashAdvance.getNumber(), e);
                        throw e;
                    }
                }
            }
            if (!cashAdvanceSolrDocs.isEmpty()) {
                log.info("========= Create Cash Advance solr docs for company {} with size {} =========", companyId, cashAdvanceSolrDocs.size());
                cashAdvanceSolrDocRepository.saveAll(cashAdvanceSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCashAdvance> edsCashAdvanceList) throws SolrServerException, IOException, InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCashAdvanceList)) {
            ConcurrentLinkedQueue<CashAdvanceSolrDoc> cashAdvanceSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCashAdvance edsCashAdvance : edsCashAdvanceList) {
                if (Objects.nonNull(edsCashAdvance) && !Boolean.TRUE.equals(edsCashAdvance.getDeleted())) {
                    CashAdvanceSolrItem solrRPC = edsCashAdvance.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(edsCashAdvance), () -> {
                                cashAdvanceSolrDocs.add(createCashAdvanceDocument(solrRPC, companyId));
                                        log.info("Indexed CashAdvance Core CID - {}, objId - {}", companId, edsCashAdvance.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Cash Advance with number = {}, companyID: {}, **********************, error: {}", edsCashAdvance.getNumber(), companyId, e);
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
                log.error("Error on loading Cash Advance list", e);
            }

            if (!cashAdvanceSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Cash Advance solr docs for company {} with size {} =========", companyId, cashAdvanceSolrDocs.size());
                    cashAdvanceSolrDocRepository.saveAll(cashAdvanceSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Cash Advance list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(EdsCashAdvance cashAdvance) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + cashAdvance.getObjectID();
    }

    private CashAdvanceSolrDoc createCashAdvanceDocument(CashAdvanceSolrItem cashAdvance, Integer companyId) {
        CashAdvanceSolrDoc cashAdvanceSolrDoc = new CashAdvanceSolrDoc();

        cashAdvanceSolrDoc.setOid(SolrUtils.generatedOId(companyId, cashAdvance.getObjectId()));
        cashAdvanceSolrDoc.setCompanyId(companyId);
        cashAdvanceSolrDoc.setCashAdvanceId(cashAdvance.getObjectId());

        if (cashAdvance.getEmployee() != null && cashAdvance.getEmployeeProfile() != null) {
            SelectItem employee = cashAdvance.getEmployee();
            SelectItem employeeProfile = cashAdvance.getEmployeeProfile();
            cashAdvanceSolrDoc.setEmployeeId(employee.getId());
            cashAdvanceSolrDoc.setEmployeeName(employee.getName());
            cashAdvanceSolrDoc.setEmployeeIdName(SolrUtils.getIdName(employee.getId(), employee.getName()));
            cashAdvanceSolrDoc.setEmployeeCode(employeeProfile.getCode());
            if (cashAdvance.getDriverNumber() != null) {
                cashAdvanceSolrDoc.setDriverId(cashAdvance.getDriverNumber());
            }
        }

        if (cashAdvance.getCurrentApprover() != null && cashAdvance.getCurrentApprover().getExactEmployee() != null) {
            SelectItem exactEmployee = cashAdvance.getCurrentApprover().getExactEmployee();
            cashAdvanceSolrDoc.setApproverId(exactEmployee.getId());
            cashAdvanceSolrDoc.setApproverName(exactEmployee.getName());
            cashAdvanceSolrDoc.setApproverIdName(SolrUtils.getIdName(exactEmployee.getId(), exactEmployee.getName()));
        }

        if (cashAdvance.getStatus() != null) {
            SelectItem status = cashAdvance.getStatus();
            cashAdvanceSolrDoc.setStatusId(status.getId());
            cashAdvanceSolrDoc.setStatusName(status.getName());
            cashAdvanceSolrDoc.setStatusCode(status.getDescription());
            cashAdvanceSolrDoc.setStatusIdName(SolrUtils.getIdName(status.getId(), status.getName()));
        }

        if (cashAdvance.getPaymentMethod() != null) {
            SelectItem paymentMethod = cashAdvance.getPaymentMethod();
            cashAdvanceSolrDoc.setPaymentMethodId(paymentMethod.getId());
            cashAdvanceSolrDoc.setPaymentMethodName(paymentMethod.getName());
            cashAdvanceSolrDoc.setPaymentMethodCode(paymentMethod.getCode());
        }

        cashAdvanceSolrDoc.setTotalAmount(cashAdvance.getTotalAmount() != null ? cashAdvance.getTotalAmount().doubleValue() : 0d);
        cashAdvanceSolrDoc.setPaymentAmount(cashAdvance.getPaymentAmount() != null ? cashAdvance.getPaymentAmount().doubleValue() : 0d);
        cashAdvanceSolrDoc.setPercent(cashAdvance.getPercent());
        cashAdvanceSolrDoc.setRequestDate(cashAdvance.getRequestDate());
        if (cashAdvance.getApprovedDate() != null) {
            cashAdvanceSolrDoc.setApprovedDate(cashAdvance.getApprovedDate());
        }
        cashAdvanceSolrDoc.setLastUpdate(cashAdvance.getLastUpdate());
        cashAdvanceSolrDoc.setType(cashAdvance.getType());
        cashAdvanceSolrDoc.setPurpose(cashAdvance.getPurpose());

        if (cashAdvance.getPayrollBatches() != null && !cashAdvance.getPayrollBatches().isEmpty()) {
            cashAdvanceSolrDoc.getPayrollBatchId().addAll(cashAdvance.getPayrollBatches());
        }

        cashAdvanceSolrDoc.setNumber(cashAdvance.getNumber());
        BigDecimal cashAdvanceRemainingAmount = cashAdvanceManager.getCashAdvanceRemainingAmount(cashAdvance.getObjectId());
        cashAdvanceSolrDoc.setRemainingAmount(cashAdvanceRemainingAmount != null ? cashAdvanceRemainingAmount.doubleValue() : 0d);

        if (cashAdvance.getCurrency() != null) {
            SelectItem currency = cashAdvance.getCurrency();
            cashAdvanceSolrDoc.setCurrencyId(currency.getId());
            cashAdvanceSolrDoc.setCurrencyName(currency.getName());
        }

        if (cashAdvance.getPreviousApprover() != null) {
            ApproverItem previousApprover = cashAdvance.getPreviousApprover();
            cashAdvanceSolrDoc.setPreviousApproverId(previousApprover.getObjectID());
            cashAdvanceSolrDoc.setPreviousApproverName(previousApprover.getName());
            cashAdvanceSolrDoc.setPreviousApproverIdName(SolrUtils.getIdName(previousApprover.getObjectID(), previousApprover.getName()));

            Integer statusId = previousApprover.getStatus() != null ? previousApprover.getStatus().getObjectID() : null;
            String statusCode = previousApprover.getStatus() != null ? previousApprover.getStatus().getCode() : "";
            cashAdvanceSolrDoc.setPreviousApproverStatusId(statusId);
            cashAdvanceSolrDoc.setPreviousApproverStatusCode(statusCode);

            Integer employeeId = previousApprover.getExactEmployee() != null ? previousApprover.getExactEmployee().getId() : null;
            String employeeName = previousApprover.getExactEmployee() != null ? previousApprover.getExactEmployee().getName() : "";
            cashAdvanceSolrDoc.setPreviousApproverExactEmployeeId(employeeId);
            cashAdvanceSolrDoc.setPreviousApproverExactEmployeeName(employeeName);
        }

        if (cashAdvance.getCurrentApprover() != null) {
            ApproverItem currentApprover = cashAdvance.getCurrentApprover();
            cashAdvanceSolrDoc.setCurrentApproverId(currentApprover.getObjectID());
            cashAdvanceSolrDoc.setCurrentApproverName(currentApprover.getName());
            cashAdvanceSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(currentApprover.getObjectID(), currentApprover.getName()));

            Integer statusId = currentApprover.getStatus() != null ? currentApprover.getStatus().getObjectID() : null;
            String statusCode = currentApprover.getStatus() != null ? currentApprover.getStatus().getCode() : "";
            cashAdvanceSolrDoc.setCurrentApproverStatusId(statusId);
            cashAdvanceSolrDoc.setCurrentApproverStatusCode(statusCode);

            Integer employeeId = currentApprover.getExactEmployee() != null ? currentApprover.getExactEmployee().getId() : null;
            String employeeName = currentApprover.getExactEmployee() != null ? currentApprover.getExactEmployee().getName() : "";
            cashAdvanceSolrDoc.setCurrentApproverExactEmployeeId(employeeId);
            cashAdvanceSolrDoc.setCurrentApproverExactEmployeeName(employeeName);
        }

        if (cashAdvance.getOverallStatus() != null) {
            SelectItem status = cashAdvance.getOverallStatus();
            cashAdvanceSolrDoc.setOverallStatusId(status.getId());
            cashAdvanceSolrDoc.setOverallStatusName(status.getName());
            cashAdvanceSolrDoc.setOverallStatusCode(status.getCode());
        }

        return cashAdvanceSolrDoc;
    }

    @Transactional
    public Page<CashAdvanceSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery cashAdvanceQuery = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = null;
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            boolean desc = !fp.isAscending();
            Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
            if ("employee".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.SORTABLE_EMPLOYEE_NAME);
            } else if ("approver".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.SORTABLE_APPROVER_NAME);
            } else if ("date".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE);
            } else if ("amount".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.FIELD_TOTAL_AMOUNT);
            } else if ("status".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.FIELD_STATUS_NAME);
            } else if ("number".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.FIELD_NUMBER);
            } else if ("remainingAmount".equals(fp.getSortField())) {
                solrSort = Sort.by(sortDirection, SolrCashAdvanceRepresenter.FIELD_REMAINING_AMOUNT);
            } else {
                solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), desc, true);
            }
        } else {
            solrSort = Sort.by(Sort.Direction.DESC, SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID);
        }

        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        cashAdvanceQuery.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_CASH_ADVANCE_CORE, cashAdvanceQuery, CashAdvanceSolrDoc.class);
    }

    public FacetFilterRpc getCashAdvanceFacetFilterData(FacetFilterRpc cashAdvanceFacet) {
        if (!cashAdvanceFacet.isFilterChanges()) {
            cashAdvanceFacet = commonServiceLocal.getUserFacetFilter(cashAdvanceFacet);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(cashAdvanceFacet.getSearchKey());
        fp.setStartDate(cashAdvanceFacet.getStartDate());
        fp.setEndDate(cashAdvanceFacet.getEndDate());
        fp.setEmployeeId(cashAdvanceFacet.getUserID());
        fp.setHRMS(cashAdvanceFacet.isHRMS());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCashAdvanceSolrQuery(fp, userManager.getUser()));
        solrQuery.append(fp.getEmployeeId() == null ? payrollService.generatePermissionQuery(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST) : "");
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(cashAdvanceFacet, FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(cashAdvanceFacet, company,
                SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE,
                SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE,
                FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CASH_ADVANCE_CORE, solrQuery.toString(), cashAdvanceFacet, CashAdvanceSolrDoc.class);

        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, cashAdvanceFacet);

        if (cashAdvanceFacet.getFacetContentMap().containsKey(FacetContentType.CashAdvanceFacetFilter.getContentCode()[2])) {
            getCashAdvanceFacetResultFromSolr(facetPage, cashAdvanceFacet);
        }
        return cashAdvanceFacet;
    }

    private FacetFilterRpc getCashAdvanceFacetResultFromSolr(QueryResponse facetPage, FacetFilterRpc cashAdvanceFacet) {
        FacetField amount = facetPage.getFacetField(SolrCashAdvanceRepresenter.FIELD_TOTAL_AMOUNT);
        if (amount != null && amount.getValues() != null) {
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amount.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] paymentAmount = new SelectItem[5];
            paymentAmount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            paymentAmount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            paymentAmount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            paymentAmount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            paymentAmount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            paymentAmount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            paymentAmount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            paymentAmount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            paymentAmount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            paymentAmount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            cashAdvanceFacet.getFacetContentMap().get(FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]).setFacetItems(paymentAmount);
        } else {
            cashAdvanceFacet.getFacetContentMap().get(FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]).setFacetItems(new SelectItem[0]);
        }
        return cashAdvanceFacet;
    }
}
