package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrDbInconsistencyItem;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrInconsistencyList;
import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.List;
/*
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: June 27, 2018
 * Time: 10:01:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class SolrIndexView extends View implements Constants {
    interface SolrIndexViewUiBinder extends UiBinder<HTMLPanel, SolrIndexView> {
    }

    private static final SolrIndexViewUiBinder ourUiBinder = GWT.create(SolrIndexViewUiBinder.class);

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();

    private WfmButton2 reindex;
    private WfmButton2 showInconsistency;
    private WfmButton2 fixInconsistency;
    private SchemaLookUp schemaLookUp;
    private Integer companyID = null;
    private KpiRadioButton dateReindex, allReindex;
    private DateTimePicker timePicker, endTimePicker;
    private InconsistencyGrid grid;
    private boolean isFromPartnerBackend = false;

    @UiField
    HTMLPanel panel;
    @UiField
    Label coreLabel;
    @UiField
    WfmDropdown core;
    @UiField
    Label schemaLabel;
    @UiField
    HTMLPanel schemaPanel;
    @UiField
    Label typeLabel;
    @UiField
    HorizontalPanel typePanel;
    @UiField
    Label dateLabel;
    @UiField
    HorizontalPanel dateWidget;
    @UiField
    HTMLPanel reindexButtonPanel;
    @UiField
    HTMLPanel showInconsistencyButtonPanel;
    @UiField
    HTMLPanel fixInconsistencyButtonPanel;
    @UiField
    HTMLPanel inconsistencyPanel;

    public SolrIndexView() {
        super(SOLR_CORE, backendStrings.solrReindex());
        ourUiBinder.createAndBindUi(this);
    }

    public SolrIndexView(boolean isFromPartnerBackend) {
        super(SOLR_CORE, backendStrings.solrReindex());
        ourUiBinder.createAndBindUi(this);
        this.isFromPartnerBackend = isFromPartnerBackend;
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        add(panel);
        return null;
    }

    protected void initialize() {
        coreLabel.setText(backendStrings.solrCore());
        schemaLabel.setText(backendStrings.schema());
        typeLabel.setText(wfmStrings.type());
        dateLabel.setText(wfmStrings.modifiedDate());
        core.setWithoutDefaultValue(true);
        core.setItems(coreItems());

        core.addValueChangeHandler(event -> {
            if (ALL_CORES_ID == core.getSelectedId()) {
                showInconsistency.setEnabled(false);
                fixInconsistency.setEnabled(false);
                dateReindex.setEnabled(false);
            } else {
                showInconsistency.setEnabled(true);
                fixInconsistency.setEnabled(true);
                dateReindex.setEnabled(true);
            }
        });

        schemaLookUp = new SchemaLookUp(isFromPartnerBackend);
        schemaPanel.add(schemaLookUp);

        allReindex = new KpiRadioButton("type", wfmStrings.all());
        allReindex.setValue(true);
        allReindex.setWidth("100%");
        dateReindex = new KpiRadioButton("type", wfmStrings.byLastUpdateTime());
        allReindex.addValueChangeHandler(valueChangeEvent -> {
            timePicker.getStartDatePicker().setEnabled(false);
            timePicker.getStartTime().setEnabled(false);
            endTimePicker.getStartDatePicker().setEnabled(false);
            endTimePicker.getStartTime().setEnabled(false);
        });
        dateReindex.addValueChangeHandler(valueChangeEvent -> {
            timePicker.getStartDatePicker().setEnabled(true);
            timePicker.getStartTime().setEnabled(true);
            endTimePicker.getStartDatePicker().setEnabled(true);
            endTimePicker.getStartTime().setEnabled(true);
        });
        dateReindex.setWidth("100%");
        typePanel.add(allReindex);
        typePanel.add(dateReindex);

        timePicker = new DateTimePicker(false, false);
        timePicker.getStartTime().setValue("00:00");
        timePicker.getStartTime().setVisible(true);
        timePicker.getStartDatePicker().setWidth("100%");
        timePicker.getStartTime().setWidth("100%");
        dateWidget.add(timePicker.getStartDatePicker());
        dateWidget.add(timePicker.getStartTime());
        timePicker.getStartDatePicker().setEnabled(false);
        timePicker.getStartTime().setEnabled(false);

        endTimePicker = new DateTimePicker(false, false);
        endTimePicker.getStartTime().setValue("00:00");
        endTimePicker.getStartTime().setVisible(true);
        endTimePicker.getStartDatePicker().setWidth("100%");
        endTimePicker.getStartTime().setWidth("100%");
        dateWidget.add(endTimePicker.getStartDatePicker());
        dateWidget.add(endTimePicker.getStartTime());
        endTimePicker.getStartDatePicker().setEnabled(false);
        endTimePicker.getStartTime().setEnabled(false);

        reindex = new WfmButton2(backendStrings.reindex(), WfmButton2.BTN_SUCCESS + " btn-block", clickEvent -> {
            SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
            if (validate(true)) {
                if (core.getSelectedId() != ALL_CORES_ID) {
                    LoadingPanel.loading(true);
                }
                getCompanyID();
                if (dateReindex.getValue()) {
                    solrReindexRpc.setAllReindex(false);
                    solrReindexRpc.setLastUpdateTime(timePicker.getStartDatePicker().getDate());
                    solrReindexRpc.setLastUpdateEndTime(endTimePicker.getStartDatePicker().getDate());
                }
                solrReindexRpc.setCompanyId(companyID);
                fullReindex(solrReindexRpc);
            }
        });
        showInconsistency = new WfmButton2("Show Inconsistency", "btn btn--secondary btn-block", clickEvent -> {
            if (validate(false)) {
                LoadingPanel.loading(true);
                getCompanyID();
                analyzeInconsistency();
            }
        });
        fixInconsistency = new WfmButton2("Fix Inconsistency", WfmButton2.BTN_PRIMARY + " btn-block", clickEvent -> {
            if (validate(false)) {
                LoadingPanel.loading(true);
                getCompanyID();
                fixInconsistency();
            }
        });

        reindexButtonPanel.add(reindex);
        showInconsistencyButtonPanel.add(showInconsistency);
        fixInconsistencyButtonPanel.add(fixInconsistency);

        grid = new InconsistencyGrid();
        inconsistencyPanel.add(grid);
    }

    private List<SelectItem> coreItems() {
        List<SelectItem> result = new ArrayList<>();
        result.add(new SelectItem(ALL_CORES_ID, "All Cores"));

        result.add(new SelectItem(ADDITIONAL_PAYMENT_CORE_ID, "Additional Payment"));
        result.add(new SelectItem(CASE_CORE_ID, "Case"));
        result.add(new SelectItem(CASH_ADVANCE_CORE_ID, "Cash Advance"));
        result.add(new SelectItem(CERTIFICATE_CORE_ID, "Certificate"));
        result.add(new SelectItem(CHART_OF_ACCOUNT_CORE_ID, "Chart Of Account"));
        result.add(new SelectItem(CONTACTS_CORE_ID, "Contact"));
        result.add(new SelectItem(COURSE_BOOKING_CORE_ID, "Course Booking"));
        result.add(new SelectItem(COURSE_SCHEDULE_CORE_ID, "Course Schedule"));
        result.add(new SelectItem(CRM_ACCOUNT_CORE_ID, "Crm Account"));
        result.add(new SelectItem(CUSTOM_FORM_CORE_ID, "Custom form"));
        result.add(new SelectItem(DEPARTMENT_CORE_ID, "Department"));
        result.add(new SelectItem(EMPLOYEE_CORE_ID, "Employee"));
        result.add(new SelectItem(EMPLOYEE_STEP_CORE_ID, "Employee Step"));
        result.add(new SelectItem(EVENT_ID, "Event"));
        result.add(new SelectItem(EXPENSE_REPORT_CLAIMS_CORE_ID, "Expense Report Claims"));
        result.add(new SelectItem(FILES_CORE_ID, "File"));
        result.add(new SelectItem(GROUP_PAYRUN_CORE_ID, "Group Payrun"));
        result.add(new SelectItem(SHIPPING_DATA_CORE_ID, "Gdn/Grn"));
        result.add(new SelectItem(INVOICE_CORE_ID, "Invoice"));
        result.add(new SelectItem(LEADS_CORE_ID, "Lead"));
        result.add(new SelectItem(LEAVE_REQUEST_CORE_ID, "Leave Request"));
        result.add(new SelectItem(REQUEST_FOR_QUOTE_CORE_ID, "RFQ"));
        result.add(new SelectItem(NEWS_CORE_ID, "News"));
        result.add(new SelectItem(OPPORTUNITY_ID, "Opportunity"));
        result.add(new SelectItem(POSITION_CORE_ID, "Position"));
        result.add(new SelectItem(PRODUCT_SERVICE_CORE_ID, "Products/Services"));
        result.add(new SelectItem(PROJECT_CORE_ID, "Project"));
        result.add(new SelectItem(PURCHASE_INVOICE_CORE_ID, "Purchase Invoice"));
        result.add(new SelectItem(PURCHASE_ORDER_ID, "Purchase Order"));
        result.add(new SelectItem(QUOTES_CORE_ID, "Sale Quote&Order"));
        result.add(new SelectItem(SINGLE_PAYRUN_CORE_ID, "Single Payrun"));
        result.add(new SelectItem(TASK_CORE_ID, "Task"));
        result.add(new SelectItem(VACANCY_CORE_ID, "Vacancy"));

        if (!isFromPartnerBackend) {
            result.add(new SelectItem(SYSTEM_FOLDER_ID, "System Folder((Qilma))"));
            result.add(new SelectItem(FOLDERS_CORE_ID, "Folder(Qilma)"));
        }

        return result;
    }

    private void analyzeInconsistency() {
        showInconsistency.setEnabled(false);
        LoadingPanel.loading(true);
        if (core.getSelectedId() == null) {
            LoadingPanel.loading(false);
            return;
        }
        switch (core.getSelectedId()) {
            case TASK_CORE_ID:
                backendService.analyzeTaskInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CONTACTS_CORE_ID:
                backendService.analyzeContactInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case FILES_CORE_ID:
                backendService.analyzeFileInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case LEADS_CORE_ID:
                backendService.analyzeLeadInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CANDIDATES_CORE_ID:
                backendService.analyzeCandidateInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CRM_ACCOUNT_CORE_ID:
                backendService.analyzeCrmAccountInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case NEWS_CORE_ID:
                backendService.analyzeNewsInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case PROJECT_CORE_ID:
                backendService.analyzeProjectInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case INVOICE_CORE_ID:
                backendService.analyzeInvoiceInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CASE_CORE_ID:
                backendService.analazyCrmCaseInconsistencies(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case QUOTES_CORE_ID:
                backendService.analyzeQuoteInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case PURCHASE_ORDER_ID:
                backendService.analyzePurchaseOrderInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case OPPORTUNITY_ID:
                backendService.analyzeOpportunityInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case EVENT_ID:
                backendService.analyzeEventInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case PRODUCT_SERVICE_CORE_ID:
                backendService.analyzeProductsServicesInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case PURCHASE_INVOICE_CORE_ID:
                backendService.analyzePurchaseInvoiceInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case EXPENSE_REPORT_CLAIMS_CORE_ID:
                backendService.analyzeExpenseReportClaimsInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case COURSE_SCHEDULE_CORE_ID:
                backendService.analyzeCourseSchedulesInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case COURSE_BOOKING_CORE_ID:
                backendService.analyzeCourseBookingInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case EMPLOYEE_CORE_ID:
                backendService.analyzeEmployeeInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case SINGLE_PAYRUN_CORE_ID:
                backendService.analyzeSinglePayrunInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case GROUP_PAYRUN_CORE_ID:
                backendService.analyzeGroupPayrunInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CASH_ADVANCE_CORE_ID:
                backendService.analyzeCashAdvanceInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case VACANCY_CORE_ID:
                backendService.analyzeVacancyInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case EMPLOYEE_STEP_CORE_ID:
                backendService.analyzeEmployeeStepInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case ADDITIONAL_PAYMENT_CORE_ID:
                backendService.analyzeAdditionalPaymentInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CHART_OF_ACCOUNT_CORE_ID:
                backendService.analyzeChartOfAccountInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case LEAVE_REQUEST_CORE_ID:
                backendService.analyzeLeavRequestInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CUSTOM_FORM_CORE_ID:
                backendService.analyzeCustomFormInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case SHIPPING_DATA_CORE_ID:
                backendService.analyzeShippingDataInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case REQUEST_FOR_QUOTE_CORE_ID:
                backendService.analyzeRFQInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case CERTIFICATE_CORE_ID:
                backendService.analyzeCertificatesInconsistency(companyID, getCallbackAnalyzeInconsistencies());
                break;
            case POSITION_CORE_ID:
                backendService.analyzePositionsInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case DEPARTMENT_CORE_ID:
                backendService.analyzeDepartmentsInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            default:
                LoadingPanel.loading(false);
                break;
        }
    }

    private void fixInconsistency() {
        fixInconsistency.setEnabled(false);
        getCompanyID();
        LoadingPanel.loading(true);
        if (core.getSelectedId() == null) {
            LoadingPanel.loading(false);
            return;
        }
        switch (core.getSelectedId()) {
            case TASK_CORE_ID:
                if (companyID == 0) {
                    RbacService.App.get().fixInconsistencesInSolrAndDBForAllCompanies(getCallbackFixInconsistencies());
                } else {
                    RbacService.App.get().fixInconsistencesInSolrAndDB(companyID, getCallbackFixInconsistencies());
                }
                break;
            case CONTACTS_CORE_ID:
                backendService.fixContactInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case FILES_CORE_ID:
                backendService.fixFileIncosistencies(companyID, getCallbackFixInconsistencies());
                break;
            case LEADS_CORE_ID:
                backendService.fixLeadInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CANDIDATES_CORE_ID:
                backendService.fixCandidateInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CRM_ACCOUNT_CORE_ID:
                backendService.fixCrmAccountInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case NEWS_CORE_ID:
                backendService.fixNewsIncosistencies(companyID, getCallbackFixInconsistencies());
                break;
            case PROJECT_CORE_ID:
                backendService.fixProjectIncosistencies(companyID, getCallbackFixInconsistencies());
                break;
            case INVOICE_CORE_ID:
                backendService.fixInvoiceInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CASE_CORE_ID:
                backendService.fixCrmCaseInconsistencies(companyID, getCallbackFixInconsistencies());
                break;
            case QUOTES_CORE_ID:
                backendService.fixQuoteInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case PURCHASE_ORDER_ID:
                backendService.fixPurchaseOrderInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case OPPORTUNITY_ID:
                backendService.fixOpportunityInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case EVENT_ID:
                backendService.fixEventInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case PRODUCT_SERVICE_CORE_ID:
                backendService.fixProductsServicesInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case PURCHASE_INVOICE_CORE_ID:
                backendService.fixPurchaseInvoiceInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case EXPENSE_REPORT_CLAIMS_CORE_ID:
                backendService.fixExpenseReportClaimsInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case COURSE_SCHEDULE_CORE_ID:
                backendService.fixCourseScheduleInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case COURSE_BOOKING_CORE_ID:
                backendService.fixCourseBookingInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case EMPLOYEE_CORE_ID:
                backendService.fixEmployeeInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case SINGLE_PAYRUN_CORE_ID:
                backendService.fixSinglePayrunInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case GROUP_PAYRUN_CORE_ID:
                backendService.fixGroupPayrunInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CASH_ADVANCE_CORE_ID:
                backendService.fixCashAdvanceInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case VACANCY_CORE_ID:
                backendService.fixVacancyInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case EMPLOYEE_STEP_CORE_ID:
                backendService.fixEmployeeStepInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case ADDITIONAL_PAYMENT_CORE_ID:
                backendService.fixAdditionalPaymentInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CHART_OF_ACCOUNT_CORE_ID:
                backendService.fixChartOfAccountInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case LEAVE_REQUEST_CORE_ID:
                backendService.fixLeaveRequestInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CUSTOM_FORM_CORE_ID:
                backendService.fixCustomFormInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case SHIPPING_DATA_CORE_ID:
                backendService.fixShippingDataInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case REQUEST_FOR_QUOTE_CORE_ID:
                backendService.fixRFQInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case CERTIFICATE_CORE_ID:
                backendService.fixCertificatesInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case POSITION_CORE_ID:
                backendService.fixPositionsInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            case DEPARTMENT_CORE_ID:
                backendService.fixDepartmentsInconsistency(companyID, getCallbackFixInconsistencies());
                break;
            default:
                LoadingPanel.loading(false);
                break;
        }
    }

    private void fullReindex(SolrReindexRpc solrReindexRpc) {
        reindex.setEnabled(false);
        LoadingPanel.loading(true);
        if (core.getSelectedId() == null) {
            LoadingPanel.loading(false);
            return;
        }
        switch (core.getSelectedId()) {
            case TASK_CORE_ID:
                backendService.reindexCompanyTasks(solrReindexRpc, getCallbackIndex());
                break;
            case LEADS_CORE_ID:
                backendService.indexCompanyLeads(solrReindexRpc, getCallbackIndex());
                break;
            case CANDIDATES_CORE_ID:
                backendService.indexCompanyCandidates(solrReindexRpc, getCallbackIndex());
                break;
            case CRM_ACCOUNT_CORE_ID:
                backendService.indexCompanyCrmAccounts(solrReindexRpc, getCallbackIndex());
                break;
            case CONTACTS_CORE_ID:
                backendService.indexCompanyContacts(solrReindexRpc, getCallbackIndex());
                break;
            case NEWS_CORE_ID:
                backendService.indexCompanyNews(solrReindexRpc, getCallbackIndex());
                break;
            case FOLDERS_CORE_ID:
                //developer be careful, this method removes all items from FolderRbac and sets default permissions so all old permissions will be lost.
                //if you want using this method,  please contact with teamleader
                /* backendService.indexCompanyFolders(solrReindexRpc, getCallbackIndex());*/
                break;
            case FILES_CORE_ID:
                backendService.indexCompanyFiles(solrReindexRpc, getCallbackIndex());
                break;
            case SYSTEM_FOLDER_ID:
                backendService.indexCompanySystemFolders(solrReindexRpc, getCallbackIndex());
                break;
            case INVOICE_CORE_ID:
                backendService.indexSaleInvoice(solrReindexRpc, getCallbackIndex());
                break;
            case PROJECT_CORE_ID:
                backendService.indexCompanyProjects(solrReindexRpc, getCallbackIndex());
                break;
            case CASE_CORE_ID:
                backendService.indexCompanyCrmCase(solrReindexRpc, getCallbackIndex());
                break;
            case QUOTES_CORE_ID:
                backendService.indexSaleQuote(solrReindexRpc, getCallbackIndex());
                break;
            case PURCHASE_ORDER_ID:
                backendService.indexPurchaseOrder(solrReindexRpc, getCallbackIndex());
                break;
            case OPPORTUNITY_ID:
                backendService.indexOpportunities(solrReindexRpc, getCallbackIndex());
                break;
            case EVENT_ID:
                backendService.indexEvents(solrReindexRpc, getCallbackIndex());
                break;
            case PRODUCT_SERVICE_CORE_ID:
                backendService.indexProductsServices(solrReindexRpc, getCallbackIndex());
                break;
            case PURCHASE_INVOICE_CORE_ID:
                backendService.indexPurchaseInvoice(solrReindexRpc, getCallbackIndex());
                break;
            case EXPENSE_REPORT_CLAIMS_CORE_ID:
                backendService.indexExpenseReportClaims(solrReindexRpc, getCallbackIndex());
                break;
            case COURSE_BOOKING_CORE_ID:
                backendService.indexCourseBookings(solrReindexRpc, getCallbackIndex());
                break;
            case COURSE_SCHEDULE_CORE_ID:
                backendService.indexCourseSchedule(solrReindexRpc, getCallbackIndex());
                break;
            case EMPLOYEE_CORE_ID:
                backendService.indexEmployee(solrReindexRpc, getCallbackIndex());
                break;
            case SINGLE_PAYRUN_CORE_ID:
                backendService.indexSinglePayrun(solrReindexRpc, getCallbackIndex());
                break;
            case GROUP_PAYRUN_CORE_ID:
                backendService.indexGroupPayrun(solrReindexRpc, getCallbackIndex());
                break;
            case CASH_ADVANCE_CORE_ID:
                backendService.indexCashAdvance(solrReindexRpc, getCallbackIndex());
                break;
            case VACANCY_CORE_ID:
                backendService.indexVacancy(solrReindexRpc, getCallbackIndex());
                break;
            case EMPLOYEE_STEP_CORE_ID:
                backendService.indexEmployeeStep(solrReindexRpc, getCallbackIndex());
                break;
            case ADDITIONAL_PAYMENT_CORE_ID:
                backendService.indexAdditionalPayment(solrReindexRpc, getCallbackIndex());
                break;
            case CHART_OF_ACCOUNT_CORE_ID:
                backendService.indexChartOfAccount(solrReindexRpc, getCallbackIndex());
                break;
            case LEAVE_REQUEST_CORE_ID:
                backendService.indexLeaveRequest(solrReindexRpc, getCallbackIndex());
                break;
            case CUSTOM_FORM_CORE_ID:
                backendService.indexCustomFormItems(solrReindexRpc, getCallbackIndex());
                break;
            case SHIPPING_DATA_CORE_ID:
                backendService.indexShippingData(solrReindexRpc, getCallbackIndex());
                break;
            case REQUEST_FOR_QUOTE_CORE_ID:
                backendService.indexRFQ(solrReindexRpc, getCallbackIndex());
                break;
            case CERTIFICATE_CORE_ID:
                backendService.indexCertificates(solrReindexRpc, getCallbackIndex());
                break;
            case POSITION_CORE_ID:
                backendService.indexPositions(solrReindexRpc, getCallbackIndex());
                break;
            case DEPARTMENT_CORE_ID:
                backendService.indexDepartments(solrReindexRpc, getCallbackIndex());
                break;
            case ALL_CORES_ID:
                indexAllCoresOfCompany(solrReindexRpc);
                break;
            default:
                LoadingPanel.loading(false);
                break;
        }
    }

    private AbstractAsyncCallback<Void> getCallbackAnalyzeInconsistencies() {
        return new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                showInconsistency.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert("Failed check inconsistency-> :" + throwable.getLocalizedMessage());
            }

            @Override
            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Window.alert("Successfully checked inconsistency");
                showInconsistency.setEnabled(true);
                grid.refresher();
            }
        };
    }

    private AbstractAsyncCallback<Void> getCallbackFixInconsistencies() {
        return new AbstractAsyncCallback<Void>() {

            @Override
            public void failure(Throwable throwable) {
                fixInconsistency.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert("Failed fix inconsistency-> :" + throwable.getLocalizedMessage());
            }

            @Override
            public void success(Void aVoid) {
                fixInconsistency.setEnabled(true);
                LoadingPanel.loading(false);
                grid.refresher();
                Window.alert("Successfully fixed inconsistency");
                removeFixed();
            }
        };
    }

    private AbstractAsyncCallback<Void> getCallbackIndex() {
        return new AbstractAsyncCallback<Void>() {

            @Override
            public void failure(Throwable throwable) {
                reindex.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert("Failed to index--::" + throwable.getLocalizedMessage());
            }

            @Override
            public void success(Void aVoid) {
                reindex.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert("Successfully indexed");
            }
        };
    }

    private AbstractAsyncCallback<String> getCallbakIndexForAllCoreIndexing() {
        return new AbstractAsyncCallback<String>() {

            @Override
            public void failure(Throwable throwable) {
                reindex.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert("Failed to index--::" + throwable.getLocalizedMessage());
            }

            @Override
            public void success(String result) {
                reindex.setEnabled(true);
                LoadingPanel.loading(false);
                Window.alert(result);

            }
        };
    }

    private void removeFixed() {
        backendService.deleteFixedInconsistencesForAllCompanies(new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(Void aVoid) {
            }
        });
    }

    private void getCompanyID() {
        companyID = schemaLookUp.getSelectedItem().getId();
        if (companyID == -1) {
            companyID = 0;
        }
    }

    public class InconsistencyGrid extends AbstractDataGrid<SolrDbInconsistencyItem> {

        public InconsistencyGrid() {
            super();
            initialize();
        }

        @Override
        protected void addColums() {
            addColumn(new Column<SolrDbInconsistencyItem, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final SolrDbInconsistencyItem item) {
                    return () -> "<span>" + (!Utils.isNullOrEmpty(item.getEntityType()) ? item.getEntityType() : "") + "</span>";
                }
            }, backendStrings.solrCore());

            addColumn(new Column<SolrDbInconsistencyItem, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final SolrDbInconsistencyItem item) {
                    return () -> "<span>" + (item.getCompanyID() != null ? String.valueOf(item.getCompanyID()) : "") + "</span>";
                }
            }, backendStrings.companyID());

            addColumn(new Column<SolrDbInconsistencyItem, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final SolrDbInconsistencyItem item) {
                    return () -> "<span>" + (!Utils.isNullOrEmpty(item.getCompanyName()) ? item.getCompanyName() : "") + "</span>";
                }
            }, wfmStrings.companyName());

            addColumn(new Column<SolrDbInconsistencyItem, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final SolrDbInconsistencyItem item) {
                    return () -> "<span>" + (!Utils.isNullOrEmpty(item.getLessInSolr()) ? item.getLessInSolr() : "") + "</span>";
                }
            }, backendStrings.lessInSolr());

            addColumn(new Column<SolrDbInconsistencyItem, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final SolrDbInconsistencyItem item) {
                    return () -> "<span>" + (!Utils.isNullOrEmpty(item.getMoreInSolr()) ? item.getMoreInSolr() : "") + "</span>";
                }
            }, backendStrings.moreInSolr());
        }

        @Override
        public void refresher() {
            supplyProvider(new SolrDbInconsistencyItem[]{});
            if (companyID != null && core.getSelectedItem() != null) {
                backendService.getInconsistencyStatistic(companyID, core.getSelectedItem().getName(), new AbstractAsyncCallback<SolrInconsistencyList>() {
                    @Override
                    public void failure(Throwable caught) {
                        WfmWindow.alert(caught.getMessage());
                    }

                    @Override
                    public void success(SolrInconsistencyList result) {
                        if (result != null && result.getList() != null && result.getList().size() > 0) {
                            supplyProvider(result.getList().toArray(new SolrDbInconsistencyItem[]{}));
                            reDrawItems();
                        }
                    }
                });
            }
        }

    }

    public boolean validate(boolean p) {
        if (!Validation.validateWfmDropdown(core) || !Validation.validateLookUpRequired(schemaLookUp)) {
            Info.show("Please select Solr Core and Company fields", Info.Type.WARNING);
            return false;
        }
        if (dateReindex.getValue() && p) {
            if (!Validation.validateDate(timePicker.getStartDatePicker(), new HTML(""), true)) {
                Info.show("Please select Last Update Date field", Info.Type.WARNING);
                return false;
            }
            return true;
        }
        if ((core.getSelectedId() == 0) & (schemaLookUp.getSelectedItem().getId() == 0 || schemaLookUp.getSelectedItem().getId() == -1)) {
            Info.show("Please select Company schema instead of \"All schemas\" or \"All free schemas\"", Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void indexAllCoresOfCompany(final SolrReindexRpc solrReindexRpc) {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.confirmationMessage());
        messageBox.setMessage(backendStrings.areYouSureWantFullReindexAllCoresOfThisCompany());
        messageBox.open();
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                backendService.indexAllCoresOfSelectedCompany(solrReindexRpc, getCallbakIndexForAllCoreIndexing());
            }
        });
        reindex.setEnabled(true);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
