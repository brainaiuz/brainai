package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.solr.document.ExpenseReportClaimsSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ExpenseReportClaimsSolrDocRepository;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.EventSolrItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsSolrItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:27.
 */
@Component
public class ExpenseReportClaimsSolrComponent {
    private static final Logger log = LoggerFactory.getLogger(ExpenseReportClaimsSolrComponent.class);

    @Autowired
    private ExpenseReportClaimsSolrDocRepository reportClaimsSolrDocRepository;
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
    private ProjectManager projectManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsExpenseReport edsExpenseReport) throws InterruptedException {
        this.indexes(Arrays.asList(edsExpenseReport));
    }

    @Transactional
    public void indexes(List<EdsExpenseReport> expenseReports) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(expenseReports)) {
            List<ExpenseReportClaimsSolrDoc> expenseReportClaimsSolrDocs = new ArrayList<>();
            for (EdsExpenseReport expenseReport : expenseReports) {
                if (Objects.nonNull(expenseReport)) {
                    try {
                        expenseReportClaimsSolrDocs.add(createExpenseReportClaimDataDocument(expenseReport.getSolrRPC(), companyId, expenseReport.getCustomFields()));
                        log.info("Indexed ExpenseReport Core CID - {}, objId - {}", companyId, expenseReport.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on Expence Report with id = {} **********************", expenseReport.getObjectID());
                        throw e;
                    }
                }
            }
            if (!expenseReportClaimsSolrDocs.isEmpty()) {
                log.info("========= Create Expence Report solr docs for company {} with size {} =========", companyId, expenseReportClaimsSolrDocs.size());
                reportClaimsSolrDocRepository.saveAll(expenseReportClaimsSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsExpenseReport> expenseReports) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(expenseReports)) {
            ConcurrentLinkedQueue<ExpenseReportClaimsSolrDoc> expenseReportClaimsSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsExpenseReport expenseReport : expenseReports) {
                if (Objects.nonNull(expenseReport)) {
                    ExpenseReportsSolrItem solrRPC = expenseReport.getSolrRPC();
                    EdsInvoiceCustomFields customFields = expenseReport.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        expenseReportClaimsSolrDocs.add(createExpenseReportClaimDataDocument(solrRPC, companyId, customFields));
                                        log.info("Indexed ExpenseReport Core CID - {}, objId - {}", companId, expenseReport.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Expence Report with id = {} **********************", expenseReport.getObjectID());
                            log.error("ERROR: ", e);
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
                log.error("Error on loading Expence Report list", e);
            }

            if (!expenseReportClaimsSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Expence Report solr docs for company {} with size {} =========", companyId, expenseReportClaimsSolrDocs.size());
                    reportClaimsSolrDocRepository.saveAll(expenseReportClaimsSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Expence Report list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(ExpenseReportsSolrItem expenseReport) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + expenseReport.getObjectID();
    }

    private ExpenseReportClaimsSolrDoc createExpenseReportClaimDataDocument(ExpenseReportsSolrItem expenseReport, Integer companyId, EdsCustomFields customFields) {
        ExpenseReportClaimsSolrDoc reportClaimsSolrDoc = new ExpenseReportClaimsSolrDoc();
        reportClaimsSolrDoc.setCompanyId(companyId);
        reportClaimsSolrDoc.setOid(SolrUtils.generatedOId(companyId, expenseReport.getObjectID()));
        reportClaimsSolrDoc.setReportId(expenseReport.getObjectID());
        reportClaimsSolrDoc.setTitle(expenseReport.getTitle() == null ? "" : expenseReport.getTitle());
        reportClaimsSolrDoc.setStartDate(expenseReport.getStartDate());

        if (expenseReport.getNumbering() != null && !"".equals(expenseReport.getNumbering())) {
            reportClaimsSolrDoc.setNumbering(expenseReport.getNumbering());
        }

        if (expenseReport.getRelatedProject() != null) {
            reportClaimsSolrDoc.setRelatedProjectId(expenseReport.getRelatedProject().getId());
            reportClaimsSolrDoc.setRelatedProjectName(expenseReport.getRelatedProject().getName());
            reportClaimsSolrDoc.setRelatedProjectNumber(expenseReport.getRelatedProject().getNumber());
            reportClaimsSolrDoc.setRelatedProjectNumberName(expenseReport.getRelatedProject().getNumber() + SolrExpenseReportRepresenter.ARROW + expenseReport.getRelatedProject().getName());
            reportClaimsSolrDoc.setRelatedProjectIdName(SolrUtils.getIdName(expenseReport.getRelatedProject().getId(), expenseReport.getRelatedProject().getCode() + " - " + expenseReport.getRelatedProject().getName()));
        }

        if (!expenseReport.getMultiProject().isEmpty()) {
            expenseReport.getMultiProject().forEach(edsProject -> {
                reportClaimsSolrDoc.getMultiProjectId().add(edsProject.getId());
                reportClaimsSolrDoc.getMultiProjectName().add(edsProject.getName());
                reportClaimsSolrDoc.getMultiProjectNumber().add(edsProject.getCode());
                reportClaimsSolrDoc.getMultiProjectIdName().add(SolrUtils.getIdName(edsProject.getId(), edsProject.getName()));
                reportClaimsSolrDoc.getMultiProjectNumberName().add(edsProject.getCode() + SolrExpenseReportRepresenter.ARROW + edsProject.getName());
            });
        }

        if (expenseReport.getReporter() != null) {
            reportClaimsSolrDoc.setReporterId(expenseReport.getReporter().getId());
            reportClaimsSolrDoc.setReporterName(expenseReport.getReporter().getName());
            reportClaimsSolrDoc.setReporterIdName(SolrUtils.getIdName(expenseReport.getReporter().getId(), expenseReport.getReporter().getName()));
        }

        if (expenseReport.getCurrentApprover() != null) {
            reportClaimsSolrDoc.setApproverId(expenseReport.getCurrentApprover().getId());
            reportClaimsSolrDoc.setApproverName(expenseReport.getCurrentApprover().getName());
            reportClaimsSolrDoc.setApproverIdName(SolrUtils.getIdName(expenseReport.getCurrentApprover().getId(), expenseReport.getCurrentApprover().getName()));
        }

        if (expenseReport.getStatus() != null) {
            reportClaimsSolrDoc.setStatusId(expenseReport.getStatus().getId());
            reportClaimsSolrDoc.setStatusCode(expenseReport.getStatus().getCode());
            reportClaimsSolrDoc.setStatusName(expenseReport.getStatus().getName());
            reportClaimsSolrDoc.setStatusIdName(SolrUtils.getIdName(expenseReport.getStatus().getId(), expenseReport.getStatus().getName()));
        }

        if (expenseReport.getOrginalAmount() != null) {
            reportClaimsSolrDoc.setOrginalAmount(expenseReport.getOrginalAmount());
        }

        if (expenseReport.getCurrency() != null) {
            reportClaimsSolrDoc.setCurrencyId(expenseReport.getCurrency().getId());
            reportClaimsSolrDoc.setCurrencyName(expenseReport.getCurrency().getName());
            reportClaimsSolrDoc.setCurrencyIdName(SolrUtils.getIdName(expenseReport.getCurrency().getId(), expenseReport.getCurrency().getName()));
        }

        if (expenseReport.getSupplier() != null) {
            reportClaimsSolrDoc.setSupplierId(expenseReport.getSupplier().getId());
            reportClaimsSolrDoc.setSupplierName(expenseReport.getSupplier().getName());
            reportClaimsSolrDoc.setSupplierIdName(SolrUtils.getIdName(expenseReport.getSupplier().getId(), expenseReport.getSupplier().getName()));
            if (!expenseReport.getSupplierOwnerIds().isEmpty()) {
                reportClaimsSolrDoc.getSupplierOwnerId().addAll(expenseReport.getSupplierOwnerIds());
            }
        }

        reportClaimsSolrDoc.setPaidAmount(expenseReport.getPaidAmount());
        reportClaimsSolrDoc.setDueAmount(expenseReport.getDueAmount());
        reportClaimsSolrDoc.setTaxAmount(expenseReport.getTaxAmount());
        reportClaimsSolrDoc.setCompanyExpense(expenseReport.getCompanyExpense());

        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(reportClaimsSolrDoc, customFields);
        }

        if (expenseReport.getPreviousApprover() != null) {
            reportClaimsSolrDoc.setPreviousApproverId(expenseReport.getPreviousApprover().getId());
            reportClaimsSolrDoc.setPreviousApproverName(expenseReport.getPreviousApprover().getName());
            reportClaimsSolrDoc.setPreviousApproverIdName(SolrUtils.getIdName(expenseReport.getPreviousApprover().getId(), expenseReport.getPreviousApprover().getName()));
            if (expenseReport.getPreviousApproverStatus() != null) {
                reportClaimsSolrDoc.setPreviousApproverStatusId(expenseReport.getPreviousApproverStatus().getId());
                reportClaimsSolrDoc.setPreviousApproverStatusCode(expenseReport.getPreviousApproverStatus().getCode());
            }
            reportClaimsSolrDoc.setPreviousApproverExactEmployeeId(expenseReport.getPreviousApproverExactEmployee() != null ? expenseReport.getPreviousApproverExactEmployee().getId() : null);
            reportClaimsSolrDoc.setPreviousApproverExactEmployeeName(expenseReport.getPreviousApproverExactEmployee() != null ? expenseReport.getPreviousApproverExactEmployee().getName() : "");
        }

        if (expenseReport.getCurrentApprover() != null) {
            reportClaimsSolrDoc.setCurrentApproverId(expenseReport.getCurrentApprover().getId());
            reportClaimsSolrDoc.setCurrentApproverName(expenseReport.getCurrentApprover().getName());
            reportClaimsSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(expenseReport.getCurrentApprover().getId(), expenseReport.getCurrentApprover().getName()));
            reportClaimsSolrDoc.setCurrentApproverStatusId(expenseReport.getCurrentApproverStatus() != null ? expenseReport.getCurrentApproverStatus().getId() : null);
            reportClaimsSolrDoc.setCurrentApproverStatusCode(expenseReport.getCurrentApproverStatus() != null ? expenseReport.getCurrentApproverStatus().getCode() : "");
            reportClaimsSolrDoc.setCurrentApproverExactEmployeeId(expenseReport.getCurrentApproverExactEmployee() != null ? expenseReport.getCurrentApproverExactEmployee().getId() : null);
            reportClaimsSolrDoc.setCurrentApproverExactEmployeeName(expenseReport.getCurrentApproverExactEmployee() != null ? expenseReport.getCurrentApproverExactEmployee().getName() : "");
        }

        if (expenseReport.getOverallStatus() != null) {
            reportClaimsSolrDoc.setOverallStatusId(expenseReport.getOverallStatus().getId());
            reportClaimsSolrDoc.setOverallStatusName(expenseReport.getOverallStatus().getName());
            reportClaimsSolrDoc.setOverallStatusCode(expenseReport.getOverallStatus().getCode());
        }

        return reportClaimsSolrDoc;
    }

    public Page<ExpenseReportClaimsSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrExpenseReportRepresenter.FIELD_REPORT_ID);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                switch (filterParameter.getSortField()) {
                    case AccountingConstants.TITLE_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.SORTABLE_TITLE);
                    case AccountingConstants.NUMBER_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_NUMBERING);
                    case AccountingConstants.PROJECT_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.SORTABLE_RELATED_PROJECT_NAME);
                    case AccountingConstants.REPORTER_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.SORTABLE_REPORTER_NAME);
                    case AccountingConstants.APPROVER_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.SORTABLE_APPROVER_NAME);
                    case AccountingConstants.STATUS_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.SORTABLE_STATUS_NAME);
                    case AccountingConstants.ORIGINAL_AMOUNT_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT);
                    case AccountingConstants.PAID_AMOUNT_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_PAID_AMOUNT);
                    case AccountingConstants.DUE_AMOUNT_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_DUE_AMOUNT);
                    case AccountingConstants.PERIOD_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_START_DATE);
                    case AccountingConstants.CURRENCY_COLUMN ->
                            solrSort = Sort.by(sortDirection, SolrExpenseReportRepresenter.FIELD_CURRENCY_NAME);
                }
            }
        }

        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_EXPENSE_REPORT_CLAIMS_CORE, query, ExpenseReportClaimsSolrDoc.class);
    }

    private FacetFilterRpc applyNonConvertedFilterPeriod(FacetFilterRpc facet) {
        HashMap<String, String> customData = facet.getCustomData();
        if (customData.get(STARTDATE_NC) != null) {
            facet.setStartDate(ServerUtils.parseFilterParameterDate(customData.get(STARTDATE_NC)));
        }
        if (customData.get(ENDDATE_NC) != null) {
            facet.setEndDate(ServerUtils.parseFilterParameterDate(customData.get(ENDDATE_NC)));
        }
        return facet;
    }

    public FacetFilterRpc getExpenseReportClaimsFacetFilterData(FacetFilterRpc reportsFacetFilter, String reporterName, boolean isLookup) {

        if (!reportsFacetFilter.isFilterChanges()) {
            reportsFacetFilter = commonServiceLocal.getUserFacetFilter(reportsFacetFilter);
        }

        reportsFacetFilter = applyNonConvertedFilterPeriod(reportsFacetFilter);

        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser != null ? edsUser.getCompany() : companyManager.get(SecurityContext.getCompanyID());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(reportsFacetFilter.getSearchKey());
        fp.setLookUp(isLookup);

        String[] fields = reportsFacetFilter.getSolrFieldMapCodeList(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]);

        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(expenseServiceLocal.getExpenseReportsCoreSolrQuery(fp, edsUser));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                reportsFacetFilter,
                FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                reportsFacetFilter,
                edsCompany,
                SolrExpenseReportRepresenter.FIELD_START_DATE,
                SolrExpenseReportRepresenter.FIELD_START_DATE,
                FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]
        ));
        if (StringUtils.isNotBlank(reporterName)) {
            solrQuery.append(" AND (").append(SolrExpenseReportRepresenter.SORTABLE_REPORTER_NAME).append(":\"")
                    .append(reporterName).append("\" ");
            Map<String, Double> fs = new HashMap<>();
            fs.put(SolrExpenseReportRepresenter.SORTABLE_REPORTER_NAME, SolrSearchUtils.LOW_PRIORITY);

            SolrSearchUtils searchUtils = new SolrSearchUtils();
            searchUtils.generateSearchQuery(solrQuery, fs, reporterName);

            solrQuery.append(")");
        }
        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_EXPENSE_REPORT_CLAIMS_CORE, solrQuery.toString(),
                reportsFacetFilter, ExpenseReportClaimsSolrDoc.class);

        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, reportsFacetFilter);
        if (reportsFacetFilter.getFacetContentMap().containsKey(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6])) {
            getExpenseFacetResultFromSolr(facetPage, reportsFacetFilter);
        }
        return reportsFacetFilter;
    }

    private FacetFilterRpc getExpenseFacetResultFromSolr(QueryResponse resp, FacetFilterRpc expenseFacet) {
        FacetField amountFacet = resp.getFacetField(SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
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
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]).setFacetItems(amount);
        } else {
            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]).setFacetItems(new SelectItem[0]);
        }

        FacetField expanse = resp.getFacetField(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE);
        if (expanse != null && expanse.getValues() != null) {
            int yes = 0, no = 0;
            for (FacetField.Count count : expanse.getValues()) {
                if ("false".equalsIgnoreCase(count.getName())) {
                    yes += count.getCount();
                } else {
                    no += count.getCount();
                }
            }
            SelectItem[] type = new SelectItem[2];
            type[0] = new SelectItem("false".hashCode(), "false");
            type[0].setDescription("Employee Expenses  ( <b>" + yes + "</b> )");

            type[1] = new SelectItem("true".hashCode(), "true");
            type[1].setDescription("Company Expenses ( <b>" + no + "</b> )");

            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]).setFacetItems(type);
        } else {
            if (expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]) != null) {
                expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]).setFacetItems(new SelectItem[0]);
            }
        }
        return expenseFacet;
    }
}
