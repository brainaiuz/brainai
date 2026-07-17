package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.solr.document.SinglePayrunSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.SinglePayrunSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataSolrItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunSolrItem;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
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
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@Component
public class SinglePayrunSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(SinglePayrunSolrComponent.class);
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private SinglePayrunSolrDocRepository singlePayrunSolrDocRepository;
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
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsPayslipTableItem edsPayslipTableItem) throws InterruptedException {
        this.indexes(Arrays.asList(edsPayslipTableItem));
    }

    @Transactional(readOnly = true)
    public void indexes(List<EdsPayslipTableItem> edsPayslipTableItems) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsPayslipTableItems)) {
            List<SinglePayrunSolrDoc> singlePayrunSolrDocs = new ArrayList<>();

            for (EdsPayslipTableItem edsPayslipItem : edsPayslipTableItems) {
                if (Objects.nonNull(edsPayslipItem)) {
                    try {
                        singlePayrunSolrDocs.add(createSinglePayrunDocument(edsPayslipItem.getSolrRPC(), companyId));
                        log.info("Indexed SinglePayrun Core CID - {}, objId - {}", companyId, edsPayslipItem.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* Error on Single payrun with id = {} **********************", edsPayslipItem.getObjectID());
                        throw e;
                    }
                }
            }
            if (!singlePayrunSolrDocs.isEmpty()) {
                log.info("========= Create Single payrun solr docs for company {} with size {} =========", companyId, singlePayrunSolrDocs.size());
                singlePayrunSolrDocRepository.saveAll(singlePayrunSolrDocs);
            }
        }
    }

    @Transactional(readOnly = true)
    public void indexConcurrently(List<EdsPayslipTableItem> edsPayslipTableItems) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsPayslipTableItems)) {
            ConcurrentLinkedQueue<SinglePayrunSolrDoc> singlePayrunSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsPayslipTableItem edsPayslipItem : edsPayslipTableItems) {
                if (Objects.nonNull(edsPayslipItem)) {
                    SinglePayrunSolrItem solrRPC = edsPayslipItem.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        singlePayrunSolrDocs.add(createSinglePayrunDocument(solrRPC, companyId));
                                        log.info("Indexed SinglePayrun Core CID - {}, objId - {}", companyId, edsPayslipItem.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Single payrun with id = {} **********************", edsPayslipItem.getObjectID());
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

            if (!singlePayrunSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Single payrun solr docs for company {} with size {} =========", companyId, singlePayrunSolrDocs.size());
                    singlePayrunSolrDocRepository.saveAll(singlePayrunSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Single payrun list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(SinglePayrunSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectID();
    }

    private SinglePayrunSolrDoc createSinglePayrunDocument(SinglePayrunSolrItem payslipItem, Integer companyId) {
        SinglePayrunSolrDoc singlePayrunSolrDoc = new SinglePayrunSolrDoc();

        singlePayrunSolrDoc.setOid(SolrUtils.generatedOId(companyId, payslipItem.getObjectID()));
        singlePayrunSolrDoc.setCompanyId(companyId);
        singlePayrunSolrDoc.setSinglePayrunId(payslipItem.getObjectID());

        if (payslipItem.getPreparer() != null) {
            singlePayrunSolrDoc.setPreparerId(payslipItem.getPreparer().getId());
            singlePayrunSolrDoc.setPreparerName(payslipItem.getPreparer().getName());
        }

        if (payslipItem.getEmployee() != null) {
            SelectItem employee = payslipItem.getEmployee();
            singlePayrunSolrDoc.setEmployeeId(employee.getId());
            singlePayrunSolrDoc.setEmployeeName(employee.getName());
            singlePayrunSolrDoc.setEmployeeIdName(SolrUtils.getIdName(employee.getId(), employee.getName()));
            if (payslipItem.getEmployee().getCode() != null) {
                singlePayrunSolrDoc.setEmployeeCode(payslipItem.getEmployee().getCode());
                Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
                if (StringUtils.isNotBlank(payslipItem.getEmployee().getCode()) && isIntegerNumberEnabled) {
                    singlePayrunSolrDoc.setEmployeeIntegerNumber(Integer.parseInt(payslipItem.getEmployee().getCode().replaceAll("[\\D]", "")));
                }
            }
        }

        if (payslipItem.getApprover() != null) {
            singlePayrunSolrDoc.setApproverId(payslipItem.getApprover().getId());
            singlePayrunSolrDoc.setApproverName(payslipItem.getApprover().getName());
            singlePayrunSolrDoc.setApproverIdName(SolrUtils.getIdName(payslipItem.getApprover().getId(), payslipItem.getApprover().getName()));
        }

        if (payslipItem.getApprover2() != null) {
            singlePayrunSolrDoc.setApprover2Id(payslipItem.getApprover2().getId());
            singlePayrunSolrDoc.setApprover2Name(payslipItem.getApprover2().getName());
            singlePayrunSolrDoc.setApprover2IdName(SolrUtils.getIdName(payslipItem.getApprover2().getId(), payslipItem.getApprover2().getName()));
        }

        if (payslipItem.getStatus() != null) {
            singlePayrunSolrDoc.setStatusId(payslipItem.getStatus().getObjectID());
            singlePayrunSolrDoc.setStatusName(payslipItem.getStatus().getName());
            singlePayrunSolrDoc.setStatusIdName(SolrUtils.getIdName(payslipItem.getStatus().getObjectID(), payslipItem.getStatus().getName()));
            singlePayrunSolrDoc.setStatusCode(payslipItem.getStatus().getCode());
        }

        if (payslipItem.getStatus2() != null) {
            singlePayrunSolrDoc.setStatus2Id(payslipItem.getStatus2().getObjectID());
            singlePayrunSolrDoc.setStatus2Name(payslipItem.getStatus2().getName());
            singlePayrunSolrDoc.setStatus2IdName(SolrUtils.getIdName(payslipItem.getStatus2().getObjectID(), payslipItem.getStatus2().getName()));
            singlePayrunSolrDoc.setStatus2Code(payslipItem.getStatus2().getCode());
        }

        singlePayrunSolrDoc.setFrequency(payslipItem.getFrequency());

        if (payslipItem.getPayslipTableId() != null) {
            singlePayrunSolrDoc.setPayslipTableId(payslipItem.getPayslipTableId());
            if (payslipItem.getPayrollBatchId() != null) {
                singlePayrunSolrDoc.setPayrollBatchId(payslipItem.getPayrollBatchId());
            }
        }

        singlePayrunSolrDoc.setDescription(payslipItem.getDescription());
        singlePayrunSolrDoc.setBasicSalary(payslipItem.getBasicSalary());
        singlePayrunSolrDoc.setDailyRate(payslipItem.getDailyRate());
        singlePayrunSolrDoc.setActualMonthPay(payslipItem.getActualMonthPay());
        singlePayrunSolrDoc.setAllowance(payslipItem.getAllowance());
        singlePayrunSolrDoc.setAdditionalPay(payslipItem.getAdditionalPay());
        singlePayrunSolrDoc.setDeduction(payslipItem.getDeduction());
        singlePayrunSolrDoc.setExpense(payslipItem.getExpense());
        singlePayrunSolrDoc.setTotal(payslipItem.getTotal());

        if (payslipItem.getCurrency() != null) {
            singlePayrunSolrDoc.setCurrencyId(payslipItem.getCurrency().getId());
            singlePayrunSolrDoc.setCurrencyName(payslipItem.getCurrency().getName());
            singlePayrunSolrDoc.setCurrencyIdName(SolrUtils.getIdName(payslipItem.getCurrency().getId(), payslipItem.getCurrency().getName()));
            singlePayrunSolrDoc.setCurrencySymbol(payslipItem.getCurrencySymbol());
        }

        singlePayrunSolrDoc.setUsedPertol(payslipItem.getUsedPertol());
        singlePayrunSolrDoc.setComission(payslipItem.getComission());
        singlePayrunSolrDoc.setCollection(payslipItem.getCollection());
        singlePayrunSolrDoc.setPensionRate(payslipItem.getPensionRate());
        singlePayrunSolrDoc.setNonLocalPensionRate(payslipItem.getNonLocalPensionRate());
        singlePayrunSolrDoc.setPensionValueType(payslipItem.getPensionValueType());
        singlePayrunSolrDoc.setCompanyPensionType(payslipItem.getCompanyPensionType());
        singlePayrunSolrDoc.setPensionAmount(payslipItem.getPensionAmount());
        singlePayrunSolrDoc.setCompanyPensionAmount(payslipItem.getCompanyPensionAmount());
        singlePayrunSolrDoc.setCompanyPensionRate(payslipItem.getCompanyPensionRate());
        singlePayrunSolrDoc.setCompanyNonLocalPensionRate(payslipItem.getCompanyNonLocalPensionRate());
        singlePayrunSolrDoc.setDaysWorked(payslipItem.getDaysWorked());
        singlePayrunSolrDoc.setFromDate(payslipItem.getFromDate());
        singlePayrunSolrDoc.setToDate(payslipItem.getToDate());
        singlePayrunSolrDoc.setProcessDate(payslipItem.getProcessDate());
        singlePayrunSolrDoc.setCreationDate(payslipItem.getCreationDate());
        singlePayrunSolrDoc.setApprovedDate(payslipItem.getApprovedDate());
        singlePayrunSolrDoc.setPdfTemplateId(payslipItem.getPdfTemplateId());
        singlePayrunSolrDoc.setYear(payslipItem.getYear());
        if (payslipItem.getMonth() != null) {
            singlePayrunSolrDoc.setMonthId(payslipItem.getMonth().getId());
            singlePayrunSolrDoc.setMonthName(payslipItem.getMonth().getName());
            singlePayrunSolrDoc.setMonthIdName(SolrUtils.getIdName(payslipItem.getMonth().getId(), payslipItem.getMonth().getName()));
        }
        singlePayrunSolrDoc.setApproved(payslipItem.getApproved());
        singlePayrunSolrDoc.setTransacted(payslipItem.getTransacted());
        singlePayrunSolrDoc.setSendEmail(payslipItem.getSendEmail());
        singlePayrunSolrDoc.setFromEndOfService(payslipItem.getFromEndOfService());
        singlePayrunSolrDoc.setRejectionNote(payslipItem.getRejectionNote());
        singlePayrunSolrDoc.setPaymentPolicy(payslipItem.getPaymentPolicy());
        singlePayrunSolrDoc.setDriverId(payslipItem.getDriverId());
        singlePayrunSolrDoc.setDeleted(payslipItem.getDeleted());
        singlePayrunSolrDoc.setLastUpdate(payslipItem.getLastUpdate());
        return singlePayrunSolrDoc;
    }

    public Page<SinglePayrunSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSinglePayrunRepresenter.FIELD_PROCESS_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                Sort.Direction order = !fp.isAscending() ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case SinglePayrunItem.EMPLOYEE ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.SORTABLE_EMPLOYEE_NAME);
                    case SinglePayrunItem.PERIOD ->
                            solrSort = Sort.by(Arrays.asList(new Sort.Order(order, SolrSinglePayrunRepresenter.FIELD_YEAR), new Sort.Order(order, SolrSinglePayrunRepresenter.FIELD_MONTH_ID)));
                    case SinglePayrunItem.APPROVER ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.SORTABLE_APPROVER_NAME);
                    case SinglePayrunItem.PREPARER ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.SORTABLE_PREPARER_NAME);
                    case SinglePayrunItem.TOTAL -> solrSort = Sort.by(order, SolrSinglePayrunRepresenter.FIELD_TOTAL);
                    case SinglePayrunItem.STATUS ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.FIELD_STATUS_NAME);
                    case SinglePayrunItem.CURRENCY ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.SORTABLE_CURRENCY_NAME);
                    case SinglePayrunItem.DRIVER_ID ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.SORTABLE_DRIVER_ID);
                    case SinglePayrunItem.PROCESS_DATE ->
                            solrSort = Sort.by(order, SolrSinglePayrunRepresenter.FIELD_PROCESS_DATE);
                }
            }
        } else if (fp.isCheckNumber()) {
            solrSort = Sort.by(Sort.Direction.ASC, SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER);
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_SINGLE_PAYRUN_CORE, query, SinglePayrunSolrDoc.class);
    }

    public FacetFilterRpc getSinglePayrunFacetResultFromSolr(SolrDocumentList facetPage, FacetFilterRpc singlePayrunFacet) {
        if (!facetPage.isEmpty()) {
            long lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (SolrDocument count : facetPage) {
                if (count.getFieldValue(FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]) != null) {
                    var fieldValue = Double.valueOf((String) count.getFieldValue(FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]));
                    double total = fieldValue;
                    if (total < 100) {
                        lessThan100 += fieldValue;
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += fieldValue;
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += fieldValue;
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += fieldValue;
                    } else {
                        moreThan50000 += fieldValue;
                    }
                }
            }
            SelectItem[] total = new SelectItem[5];
            total[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            total[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            total[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            total[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            total[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            total[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            total[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            total[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            total[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            total[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            singlePayrunFacet.getFacetContentMap().get(FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]).setFacetItems(total);
        } else {
            singlePayrunFacet.getFacetContentMap().get(FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]).setFacetItems(new SelectItem[0]);
        }
        return singlePayrunFacet;
    }
}
