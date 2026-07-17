package com.edatasite.workforce.gwt.modulesettings.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.FeedbackForm;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 16.04.14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class PriceTable extends View implements Colapse, PermissionConstants {

    private final ModuleServiceAsync moduleService = ModuleService.App.get();

    private Integer companyID;
    private boolean isCheckPM = false;
    private boolean isCheckCRM = false;
    private boolean isCheckAccounting = false;
    private boolean isCheckHRMS = false;
    private boolean isCheckAdditional = false;
    private boolean isCheckAddOns = false;

    public PriceTable() {
        super("pricing", wfmStrings.moduleSettings());
    }

    public PriceTable(Integer companyId) {
        super("pricing", wfmStrings.moduleSettings());
        this.companyID = companyId;
    }

    interface PriceTableUiBinder extends UiBinder<HTMLPanel, PriceTable> {
    }

    private final PriceTableUiBinder uiBinder = GWT.create(PriceTableUiBinder.class);


    @UiField
    protected HTML pleaseSelectModules;
    @UiField
    protected DataListBox hostName;
    @UiField
    protected Anchor workspace;
    @UiField
    protected Button onButtonWorkspace;
    @UiField
    protected Button offButtonWorkspace;
    @UiField
    protected Anchor selectAllWorkspace;
    @UiField
    protected KpiCheckBox callendar;
    @UiField
    protected KpiCheckBox meetingMinutes;
    @UiField
    protected KpiCheckBox stuffUpdates;
    @UiField
    protected KpiCheckBox companyNews;
    @UiField
    protected KpiCheckBox notes;
    @UiField
    protected Anchor projectManagement;
    @UiField
    protected Button onButtonPM;
    @UiField
    protected Button offButtonPM;
    @UiField
    protected Anchor selectAllPM;
    @UiField
    protected KpiCheckBox taskManagement;
    @UiField
    protected KpiCheckBox issueTracking;
    @UiField
    protected KpiCheckBox timesheet;
    @UiField
    protected KpiCheckBox monthlyTimesheet;
    @UiField
    protected KpiCheckBox resourcePlanning;
    @UiField
    protected KpiCheckBox bookingItems;
    @UiField
    protected KpiCheckBox ganttChart;
    @UiField
    protected KpiCheckBox timer;
    @UiField
    protected KpiCheckBox importFromMSProject;
    //    @UiField
//    protected CheckBox projectEmails;
    @UiField
    protected KpiCheckBox customerCenter;
    @UiField
    protected Anchor crm;
    @UiField
    protected Button onButtonCrm;
    @UiField
    protected Button offButtonCrm;
    @UiField
    protected Anchor selectAllCrm;
    @UiField
    protected KpiCheckBox leadManagement;
    @UiField
    protected KpiCheckBox contactManagement;
    @UiField
    protected KpiCheckBox opportunityTracking;
    @UiField
    protected KpiCheckBox activities;
    @UiField
    protected KpiCheckBox calenderCrm;
    //    @UiField
//    protected CheckBox webForms;
    @UiField
    protected KpiCheckBox workflowRecurrence;
    @UiField
    protected KpiCheckBox caseManagement;
    @UiField
    protected KpiCheckBox solutionsManagement;
    @UiField
    protected KpiCheckBox task;

    @UiField
    protected KpiCheckBox crmRequestForQuotes;
    @UiField
    protected KpiCheckBox messageCenter;
    @UiField
    protected KpiCheckBox smsIntegration;
    @UiField
    protected Anchor accountingAndFinance;
    @UiField
    protected Button onButtonAccounting;
    @UiField
    protected Button offButtonAccounting;
    @UiField
    protected Anchor selectAllAccounting;
    @UiField
    protected KpiCheckBox salesQuotes;
    @UiField
    protected KpiCheckBox salesOrders;
    @UiField
    protected KpiCheckBox requestForQuotes;
    @UiField
    protected KpiCheckBox recurringBills;
    @UiField
    protected KpiCheckBox supplierCenter;
    @UiField
    protected KpiCheckBox bankAccounts;
    @UiField
    protected KpiCheckBox chartOfAccounts;
    @UiField
    protected KpiCheckBox reservations;
    @UiField
    protected KpiCheckBox consignments;
    @UiField
    protected KpiCheckBox salesInvoising;
    @UiField
    KpiCheckBox requestForPurchase;
    @UiField
    KpiCheckBox timesheetInvoice;
    @UiField
    protected KpiCheckBox purchaseOrder;
    @UiField
    protected KpiCheckBox purchaseInvoicing;
    @UiField
    protected KpiCheckBox recurringInvoices;
    @UiField
    protected KpiCheckBox inventoryManagement;
    @UiField
    protected KpiCheckBox expenseReporting;
    @UiField
    protected KpiCheckBox fixedAssests;
    @UiField
    protected KpiCheckBox checks;
    @UiField
    protected KpiCheckBox manualTransactions;
    @UiField
    protected KpiCheckBox customerCenterAccounting;
    @UiField
    protected KpiCheckBox productsServicesCrm;
    @UiField
    protected KpiCheckBox productsServices;
    @UiField
    protected KpiCheckBox inventoryItems;
    @UiField
    protected KpiCheckBox rentalItems;
    @UiField
    protected KpiCheckBox assemblyItems;
    @UiField
    protected KpiCheckBox rentalOrder;
    @UiField
    protected Anchor hrms;
    @UiField
    protected Button onButtonHRMS;
    @UiField
    protected Button offButtonHRMS;
    @UiField
    protected Anchor selectAllHRMS;
    @UiField
    protected KpiCheckBox leaveManagement;
    @UiField
    protected KpiCheckBox attendingTracking;
    @UiField
    protected KpiCheckBox performanceAppraisal;
    @UiField
    protected KpiCheckBox goalManagement;
    @UiField
    protected KpiCheckBox employeeExpenses;
    @UiField
    protected KpiCheckBox employeeIncidents;
    @UiField
    protected KpiCheckBox onboarding;
    @UiField
    protected KpiCheckBox telegramChats;
    //    @UiField
//    protected CheckBox promotionsPenalties;
    @UiField
    protected Anchor additionalModules;
    @UiField
    protected Button onButtonAdditional;
    @UiField
    protected Button offButtonAdditional;
    @UiField
    protected Anchor selectAllAdditional;
    @UiField
    protected KpiCheckBox payroll;
    @UiField
    protected KpiCheckBox reportingSystem;
    @UiField
    protected KpiCheckBox documentManagement;
    @UiField
    protected KpiCheckBox trainingCenter;
    @UiField
    protected KpiCheckBox websites;
    @UiField
    protected KpiCheckBox permissionManagement;
    @UiField
    protected KpiCheckBox customFields;
    @UiField
    protected KpiCheckBox customReferences;
    @UiField
    protected KpiCheckBox syncronizeWithGoogle;
    @UiField
    protected KpiCheckBox reminders;
    @UiField
    protected Anchor addOns;
    @UiField
    protected Button onButtonAddOns;
    @UiField
    protected Button offButtonAddOns;
    @UiField
    protected Anchor selectAllAddOns;
    @UiField
    protected KpiCheckBox iPhoneApps;
    @UiField
    protected KpiCheckBox androidApps;
    @UiField
    protected KpiCheckBox timesheetPlugin;
    @UiField
    protected KpiCheckBox outlookPlugin;
    @UiField
    protected KpiCheckBox storefront;
    @UiField
    protected KpiCheckBox invoiceTemplates;

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        add(uiBinder.createAndBindUi(this));
        setTitles();
        setDefaultStyles();
        saveFunctions();
        if (Utils.isBACKEND() && companyID == null) {
            getHostData();
        }
        getData();
        pmSection();
        crmSection();

        accountingSection();
        hrmsSection();
        additionalSection();
        addOnsSection();
        return null;
    }

    private void getHostData() {
        LoadingPanel.loading(true);
        moduleService.getAllHosts(Utils.getHostName(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                hostName.setItems(result.toArray(new SelectItem[]{}));
                for (SelectItem item : result) {
                    if (item.isSelected()) {
                        hostName.setSelected(item.getId());
                        break;
                    }
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void setDefaultStyles() {
        if (Utils.isBACKEND() && companyID == null && (Utils.getHostName().equals(Constants.HOST_LIVE) || Utils.getHostName().equals(Constants.HOST_AWS))) {
            hostName.setVisible(true);
            hostName.setWidth("150px");
            hostName.addValueChangeHandler(changeEvent -> {
                if (hostName.getSelectedId() != null) {
                    getData();
                }
            });
        } else {
            hostName.setVisible(false);
        }
        onButtonWorkspace.setStyleName("btn act");
        offButtonWorkspace.setStyleName("btn");
        onButtonPM.setStyleName("btn act");
        offButtonPM.setStyleName("btn");
        onButtonCrm.setStyleName("btn act");
        offButtonCrm.setStyleName("btn");
        onButtonAccounting.setStyleName("btn act");
        offButtonAccounting.setStyleName("btn");
        onButtonHRMS.setStyleName("btn act");
        offButtonHRMS.setStyleName("btn");
        onButtonAdditional.setStyleName("btn act");
        offButtonAdditional.setStyleName("btn");
        onButtonAddOns.setStyleName("btn act");
        offButtonAddOns.setStyleName("btn");
    }

    private void saveFunctions() {
        // Workspace
        save(callendar, new HashSet<String>(Collections.singletonList(WORKSPACE_CALENDAR)));
        save(meetingMinutes, new HashSet<String>(Collections.singletonList(MEETING_MINUTES)));
        save(stuffUpdates, new HashSet<String>(Collections.singletonList(STUFF_UPDATES)));
        save(companyNews, new HashSet<String>(Collections.singletonList(COMPANY_NEWS)));
        save(notes, new HashSet<String>(Collections.singletonList(NOTES)));
        //PM
        save(taskManagement, new HashSet<String>(Collections.singletonList(TASK_MANAGEMENT)));
        save(issueTracking, new HashSet<String>(Collections.singletonList(ISSUE_TRACKING)));
        save(timesheet, new HashSet<String>(Collections.singletonList(TIMESHEET)));
        save(monthlyTimesheet, new HashSet<String>(Collections.singletonList(MONTHLY_TIMESHEET)));
        save(resourcePlanning, new HashSet<String>(Collections.singletonList(RESOURCE_PLANNING)));
        save(bookingItems, new HashSet<String>(Collections.singletonList(BOOKING_ITEMS)));
        save(ganttChart, new HashSet<String>(Collections.singletonList(GANTT_CHART)));
        save(timer, new HashSet<String>(Collections.singletonList(TIMER)));
        save(importFromMSProject, new HashSet<String>(Collections.singletonList(IMPORT_FROM_MS_PROJECT)));
        save(customerCenter, new HashSet<String>(Collections.singletonList(CUSTOMER_CENTER)));
        //CRM
        save(leadManagement, new HashSet<String>(Collections.singletonList(LEAD_MANAGEMENT)));
        save(contactManagement, new HashSet<String>(Collections.singletonList(CONTACT_MANAGEMENT)));
        save(opportunityTracking, new HashSet<String>(Collections.singletonList(OPPORTUNITY_TRACKING)));
        save(activities, new HashSet<String>(Collections.singletonList(ACTIVITIES)));
        save(calenderCrm, new HashSet<String>(Collections.singletonList(CRM_CALENDAR)));
        save(workflowRecurrence, new HashSet<String>(Collections.singletonList(IS_ACTIVE_WORKFLOW_STATUS)));
        save(caseManagement, new HashSet<String>(Collections.singletonList(CASE_MANAGEMENT)));
        save(solutionsManagement, new HashSet<String>(Collections.singletonList(SOLUTION_MANAGEMENT)));
        save(task, new HashSet<String>(Collections.singletonList(TASK_MANAGEMENT)));
        save(messageCenter, new HashSet<String>(Collections.singletonList(MESSAGE_CENTER)));
        save(smsIntegration, new HashSet<String>(Collections.singletonList(SMS_INTEGRATION)));
        save(crmRequestForQuotes, new HashSet<String>(Collections.singletonList(CRM_REQUEST_FOR_QUOTES)));
        save(productsServicesCrm, new HashSet<String>(Collections.singletonList(PRODUCTS_SERVICES_CRM)));
//        save(customerService,new HashSet<String>(Collections.singletonList(CUSTOMER_SERVICE)));

        //Accounting
        save(salesQuotes, new HashSet<String>(Collections.singletonList(SALES_QUOTES)));
        save(salesOrders, new HashSet<String>(Collections.singletonList(SALES_ORDERS)));
        save(requestForQuotes, new HashSet<String>(Collections.singletonList(REQUEST_FOR_QUOTES)));
        save(recurringBills, new HashSet<String>(Collections.singletonList(RECURRING_BILLS)));
        save(supplierCenter, new HashSet<String>(Collections.singletonList(SUPPLIER_CENTER)));
        save(bankAccounts, new HashSet<String>(Collections.singletonList(BANK_ACCOUNTS)));
        save(chartOfAccounts, new HashSet<String>(Collections.singletonList(ACCOUNTING_CHART_OF_ACCOUNTS)));
        save(reservations, new HashSet<String>(Collections.singletonList(RESERVATIONS)));
        save(consignments, new HashSet<String>(Collections.singletonList(CONSIGNMENTS)));
        save(requestForPurchase, new HashSet<String>(Collections.singletonList(REQUEST_FOR_PURCHASES)));
        save(timesheetInvoice, new HashSet<String>(Collections.singletonList(TIMESHEET_INVOICES)));
        save(salesInvoising, new HashSet<String>(Collections.singletonList(SALES_INVOICING)));
        save(purchaseOrder, new HashSet<String>(Collections.singletonList(PURCHASE_ORDERS)));
        save(purchaseInvoicing, new HashSet<String>(Collections.singletonList(PURCHASE_INVOICING)));
        save(recurringInvoices, new HashSet<String>(Collections.singletonList(RECCURING_INVOICES)));
        save(inventoryManagement, new HashSet<String>(Collections.singletonList(INVENTORY_MANAGEMENT)));
        save(expenseReporting, new HashSet<String>(Collections.singletonList(EXPENSE_REPORTING)));
        save(fixedAssests, new HashSet<String>(Collections.singletonList(FIXED_ASSESTS)));
        save(checks, new HashSet<String>(Collections.singletonList(CHECKS)));
        save(manualTransactions, new HashSet<String>(Collections.singletonList(MANUAL_TRANSACTIONS)));
        save(customerCenterAccounting, new HashSet<String>(Collections.singletonList(ACCOUNTING_CUSTOMER_CENTER)));
        save(productsServices, new HashSet<String>(Collections.singletonList(PRODUCTS_SERVICES)));
        save(inventoryItems, new HashSet<String>(Collections.singletonList(PRODUCT_INVENTORY_ITEMS)));
        save(rentalItems, new HashSet<String>(Collections.singletonList(PRODUCT_RENTAL_ITEMS)));
        save(assemblyItems, new HashSet<String>(Collections.singletonList(PRODUCT_ASSEMBLY_ITEMS)));
        save(rentalOrder, new HashSet<String>(Collections.singletonList(RENTAL_ORDER_MODULE)));
        //HRMS
        save(leaveManagement, new HashSet<String>(Collections.singletonList(LEAVE_MANAGEMENT)));
        save(attendingTracking, new HashSet<String>(Collections.singletonList(ATTENDING_TRACKING)));
        save(performanceAppraisal, new HashSet<String>(Collections.singletonList(PERFORMANCE_APPRAISAL)));
        save(goalManagement, new HashSet<String>(Collections.singletonList(GOAL_MANAGEMENT)));
        save(employeeExpenses, new HashSet<String>(Collections.singletonList(EMPLOYEE_EXPENSES)));
        save(employeeIncidents, new HashSet<String>(Collections.singletonList(EMPLOYEE_INCIDENTS)));
        save(onboarding, new HashSet<String>(Collections.singletonList(ONBOARDING)));
        save(telegramChats, new HashSet<String>(Collections.singletonList(TELEGRAM_CHATS)));
        //Additional Modules
        save(payroll, new HashSet<String>(Collections.singletonList(PAYROLL)));
        save(reportingSystem, new HashSet<String>(Collections.singletonList(REPORTING_SYSTEM)));
        save(documentManagement, new HashSet<String>(Collections.singletonList(DOCUMENT_MANAGEMENT)));
        save(trainingCenter, new HashSet<String>(Collections.singletonList(TRAINING_CENTER)));
        save(websites, new HashSet<String>(Collections.singletonList(WEBSITES)));
        save(permissionManagement, new HashSet<String>(Collections.singletonList(PERMISSION_MANAGEMENT)));
        save(customFields, new HashSet<String>(Collections.singletonList(CUSTOM_FIELDS)));
        save(customReferences, new HashSet<String>(Collections.singletonList(CUSTOM_REFERENCES)));
        save(syncronizeWithGoogle, new HashSet<String>(Collections.singletonList(SYNCRONIZE_WITH_GOOGLE)));
        save(reminders, new HashSet<String>(Collections.singletonList(REMINDERS)));
        //Add-Ons
        save(iPhoneApps, new HashSet<String>(Collections.singletonList(IPHONE_APPS)));
        save(androidApps, new HashSet<String>(Collections.singletonList(ANDROID_APPS)));
        save(timesheetPlugin, new HashSet<String>(Collections.singletonList(TIMESHEET_PLUGIN)));
        save(outlookPlugin, new HashSet<String>(Collections.singletonList(OUTLOOK_PLUGIN)));
        save(storefront, new HashSet<String>(Collections.singletonList(STOREFRONT)));
        save(invoiceTemplates, new HashSet<String>(Collections.singletonList(INVOICE_TEMPLATES)));
    }

    private void getData() {
        LoadingPanel.loading(true);
        if (Utils.isBACKEND() && companyID == null) {
            String selectedHost = hostName.getSelectedItem() != null ? hostName.getSelectedItem().getName() : Utils.getHostName();
            moduleService.getHostBasedModule(selectedHost, false, new AbstractAsyncCallback<HashSet<String>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashSet<String> result) {
                    setDefaultData(result);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            moduleService.getDefaultData(companyID, new AbstractAsyncCallback<HashSet<String>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashSet<String> result) {
                    setDefaultData(result);
                    LoadingPanel.loading(false);
                }

            });
        }
    }


    private void setDefaultData(HashSet<String> result) {
        offButtonWorkspace.setStyleName("btn act");
        onButtonWorkspace.setStyleName("btn");
        checkWorkspaceCheckboxes(false);
        showHideButton(workspace, false, "workspace");
        if (result.contains(PM_MODULE)) {
            onButtonPM.setStyleName("btn act");
            offButtonPM.setStyleName("btn");
            if (result.contains(TASK_MANAGEMENT)) taskManagement.setValue(true);
            if (result.contains(ISSUE_TRACKING)) issueTracking.setValue(true);
            if (result.contains(TIMESHEET)) timesheet.setValue(true);
            if (result.contains(MONTHLY_TIMESHEET)) monthlyTimesheet.setValue(true);
            if (result.contains(RESOURCE_PLANNING)) resourcePlanning.setValue(true);
            if (result.contains(BOOKING_ITEMS)) bookingItems.setValue(true);
            if (result.contains(GANTT_CHART)) ganttChart.setValue(true);
            if (result.contains(TIMER)) timer.setValue(true);
            if (result.contains(IMPORT_FROM_MS_PROJECT)) importFromMSProject.setValue(true);
            if (result.contains(CUSTOMER_CENTER)) customerCenter.setValue(true);
            showHideButton(projectManagement, true, "projectManagement");
        } else {
            offButtonPM.setStyleName("btn act");
            onButtonPM.setStyleName("btn");
            showHideButton(projectManagement, false, "projectManagement");
            checkPMCheckboxes(false);
        }
        //CRM
        if (result.contains(CRM_MODULE)) {
            onButtonCrm.setStyleName("btn act");
            offButtonCrm.setStyleName("btn");
            if (result.contains(LEAD_MANAGEMENT)) leadManagement.setValue(true);
            if (result.contains(CONTACT_MANAGEMENT)) contactManagement.setValue(true);
            if (result.contains(OPPORTUNITY_TRACKING)) opportunityTracking.setValue(true);
            if (result.contains(ACTIVITIES)) activities.setValue(true);
            if (result.contains(CRM_CALENDAR)) calenderCrm.setValue(true);
            if (result.contains(IS_ACTIVE_WORKFLOW_STATUS)) workflowRecurrence.setValue(true);
            if (result.contains(CASE_MANAGEMENT)) caseManagement.setValue(true);
            if (result.contains(MESSAGE_CENTER)) messageCenter.setValue(true);
            if (result.contains(SMS_INTEGRATION)) smsIntegration.setValue(true);
            if (result.contains(SOLUTION_MANAGEMENT)) solutionsManagement.setValue(true);
            if (result.contains(TASK_MANAGEMENT)) task.setValue(true);
            if (result.contains(CRM_REQUEST_FOR_QUOTES)) crmRequestForQuotes.setValue(true);
            if (result.contains(PRODUCTS_SERVICES_CRM)) productsServicesCrm.setValue(true);
//            if (result.contains(CUSTOMER_SERVICE)) customerService.setValue(true);
            showHideButton(crm, true, "crm");
        } else {
            offButtonCrm.setStyleName("btn act");
            onButtonCrm.setStyleName("btn");
            showHideButton(crm, false, "crm");
            checkCrmCheckboxes(false);
        }
        //ACCOUNTING
        if (result.contains(ACCOUNTING_MODULE)) {
            onButtonAccounting.setStyleName("btn act");
            offButtonAccounting.setStyleName("btn");
            if (result.contains(SALES_QUOTES)) salesQuotes.setValue(true);
            if (result.contains(SALES_ORDERS)) salesOrders.setValue(true);
            if (result.contains(REQUEST_FOR_QUOTES)) requestForQuotes.setValue(true);
            if (result.contains(RECURRING_BILLS)) recurringBills.setValue(true);
            if (result.contains(SUPPLIER_CENTER)) supplierCenter.setValue(true);
            if (result.contains(BANK_ACCOUNTS)) bankAccounts.setValue(true);
            if (result.contains(ACCOUNTING_CHART_OF_ACCOUNTS)) chartOfAccounts.setValue(true);
            if (result.contains(RESERVATIONS)) reservations.setValue(true);
            if (result.contains(CONSIGNMENTS)) consignments.setValue(true);
            if (result.contains(REQUEST_FOR_PURCHASES)) requestForPurchase.setValue(true);
            if (result.contains(TIMESHEET_INVOICES)) timesheetInvoice.setValue(true);
            if (result.contains(REQUEST_FOR_PURCHASES)) requestForPurchase.setValue(true);
            if (result.contains(SALES_INVOICING)) salesInvoising.setValue(true);
            if (result.contains(PURCHASE_ORDERS)) purchaseOrder.setValue(true);
            if (result.contains(PURCHASE_INVOICING)) purchaseInvoicing.setValue(true);
            if (result.contains(RECCURING_INVOICES)) recurringInvoices.setValue(true);
            if (result.contains(INVENTORY_MANAGEMENT)) inventoryManagement.setValue(true);
            if (result.contains(EXPENSE_REPORTING)) expenseReporting.setValue(true);
            if (result.contains(FIXED_ASSESTS)) fixedAssests.setValue(true);
            if (result.contains(CHECKS)) checks.setValue(true);
            if (result.contains(MANUAL_TRANSACTIONS)) manualTransactions.setValue(true);
            if (result.contains(ACCOUNTING_CUSTOMER_CENTER)) customerCenterAccounting.setValue(true);
            if (result.contains(PRODUCTS_SERVICES)) productsServices.setValue(true);
            if (result.contains(PRODUCT_INVENTORY_ITEMS)) inventoryItems.setValue(true);
            if (result.contains(PRODUCT_RENTAL_ITEMS)) rentalItems.setValue(true);
            if (result.contains(PRODUCT_ASSEMBLY_ITEMS)) assemblyItems.setValue(true);
            if (result.contains(RENTAL_ORDER_MODULE)) rentalOrder.setValue(true);
            showHideButton(accountingAndFinance, true, "accounting");
        } else {
            offButtonAccounting.setStyleName("btn act");
            onButtonAccounting.setStyleName("btn");
            showHideButton(accountingAndFinance, false, "accounting");
            checkAccountingCheckboxes(false);
        }
        //HRMS
        if (result.contains(HRMS_MODULE)) {
            onButtonHRMS.setStyleName("btn act");
            offButtonHRMS.setStyleName("btn");
            if (result.contains(LEAVE_MANAGEMENT)) leaveManagement.setValue(true);
            if (result.contains(ATTENDING_TRACKING)) attendingTracking.setValue(true);
            if (result.contains(PERFORMANCE_APPRAISAL)) performanceAppraisal.setValue(true);
            if (result.contains(GOAL_MANAGEMENT)) goalManagement.setValue(true);
            if (result.contains(EMPLOYEE_EXPENSES)) employeeExpenses.setValue(true);
            if (result.contains(EMPLOYEE_INCIDENTS)) employeeIncidents.setValue(true);
            if (result.contains(ONBOARDING)) onboarding.setValue(true);
            if (result.contains(TELEGRAM_CHATS)) telegramChats.setValue(true);
            showHideButton(hrms, true, "hrms");
        } else {
            offButtonHRMS.setStyleName("btn act");
            onButtonHRMS.setStyleName("btn");
            showHideButton(hrms, false, "hrms");
            checkHRMSCheckboxes(false);
        }
        //ADDITIONAL
        if (result.contains(ADDITIONAL_MODULE)) {
            onButtonAdditional.setStyleName("btn act");
            offButtonAdditional.setStyleName("btn");
            if (result.contains(PAYROLL)) payroll.setValue(true);
            if (result.contains(REPORTING_SYSTEM)) reportingSystem.setValue(true);
            if (result.contains(DOCUMENT_MANAGEMENT)) documentManagement.setValue(true);
            if (result.contains(TRAINING_CENTER)) trainingCenter.setValue(true);
            if (result.contains(WEBSITES)) websites.setValue(true);
            if (result.contains(PERMISSION_MANAGEMENT)) permissionManagement.setValue(true);
            if (result.contains(CUSTOM_FIELDS)) customFields.setValue(true);
            if (result.contains(CUSTOM_REFERENCES)) customReferences.setValue(true);
            if (result.contains(SYNCRONIZE_WITH_GOOGLE)) syncronizeWithGoogle.setValue(true);
            if (result.contains(REMINDERS)) reminders.setValue(true);
            showHideButton(additionalModules, true, "additional");
        } else {
            offButtonAdditional.setStyleName("btn act");
            onButtonAdditional.setStyleName("btn");
            showHideButton(additionalModules, false, "additional");
            checkAdditionalCheckboxes(false);
        }
        //ADD-ONS
        if (result.contains(ADD_ONS_MODULE)) {
            onButtonAddOns.setStyleName("btn act");
            offButtonAddOns.setStyleName("btn");
            if (result.contains(IPHONE_APPS)) iPhoneApps.setValue(true);
            if (result.contains(ANDROID_APPS)) androidApps.setValue(true);
            if (result.contains(TIMESHEET_PLUGIN)) timesheetPlugin.setValue(true);
            if (result.contains(OUTLOOK_PLUGIN)) outlookPlugin.setValue(true);
            if (result.contains(STOREFRONT)) storefront.setValue(true);
            if (result.contains(INVOICE_TEMPLATES)) invoiceTemplates.setValue(true);
            showHideButton(addOns, true, "addOns");
        } else {
            offButtonAddOns.setStyleName("btn act");
            onButtonAddOns.setStyleName("btn");
            showHideButton(addOns, false, "addOns");
            checkAddOnsCheckboxes(false);
        }
    }

    private void setTitles() {
        pleaseSelectModules.setText(wfmStrings.pleaseSelectModules());
        //Workspace
        workspace.setText(wfmStrings.workspace());
        onButtonWorkspace.setText(wfmStrings.on().toUpperCase());
        offButtonWorkspace.setText(wfmStrings.off());
        selectAllWorkspace.setText(wfmStrings.selectAll());
        callendar.setText(wfmStrings.calendar());
        meetingMinutes.setText(wfmStrings.meetingMinutes());
        stuffUpdates.setText(wfmStrings.stuffUpdates());
        companyNews.setText(wfmStrings.companyNews());
        notes.setText(wfmStrings.notes());
        //PM
        selectAllPM.setText(wfmStrings.selectAll());
        onButtonPM.setText(wfmStrings.on().toUpperCase());
        offButtonPM.setText(wfmStrings.off());
        projectManagement.setHTML(wfmStrings.projects());
        taskManagement.setText(wfmStrings.taskManagement());
        issueTracking.setText(wfmStrings.issueTracking());
        timesheet.setText(Property.get(Constants.TIMESHEET, wfmStrings.timesheet()));
        monthlyTimesheet.setText(Property.get(Constants.TIMESHEET, wfmStrings.monthlyTimeSheet(), wfmStrings.timesheet()));
        resourcePlanning.setText(wfmStrings.resourcePlanning());
        bookingItems.setText(wfmStrings.bookingItems());
        ganttChart.setText(wfmStrings.ganttChart());
        timer.setText(wfmStrings.timer());
        importFromMSProject.setText(wfmStrings.importFromMsProject());
        customerCenter.setText(Property.get(Constants.CLIENT_LIST, wfmStrings.customerCenter(), wfmStrings.customer()));
        //CRM
        crm.setText(wfmStrings.crm());
        selectAllCrm.setText(wfmStrings.selectAll());
        onButtonCrm.setText(wfmStrings.on().toUpperCase());
        offButtonCrm.setText(wfmStrings.off());
        leadManagement.setText(Property.get(Constants.LEADS, wfmStrings.leadManagement(), wfmStrings.lead()));
        contactManagement.setText(Property.get(Constants.Contacts, wfmStrings.contactManagement(), wfmStrings.contact()));
        opportunityTracking.setText(Property.get(Constants.Opportunities, wfmStrings.opportunityTracking(), wfmStrings.opportunity()));
        activities.setText(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities()));
        calenderCrm.setText(wfmStrings.calendar());
        workflowRecurrence.setText(wfmStrings.addRecurrenceWorkflow());
        caseManagement.setHTML(Property.get(Constants.CASE_LIST, wfmStrings.caseManagement(), wfmStrings.crmCase()));
        messageCenter.setHTML(wfmStrings.messageCenter() + " / <br> " + wfmStrings.inbox());
        smsIntegration.setText(wfmStrings.smsIntegration());
        solutionsManagement.setText(wfmStrings.solutions());
        task.setText(wfmStrings.tasks());
//        customerService.setText(wfmStrings.customerService2());
        crmRequestForQuotes.setText(wfmStrings.requestForQuotes());
        productsServicesCrm.setText(Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()));
        //Accounting
        accountingAndFinance.setText(wfmStrings.accounts());
        selectAllAccounting.setText(wfmStrings.selectAll());
        onButtonAccounting.setText(wfmStrings.on().toUpperCase());
        offButtonAccounting.setText(wfmStrings.off());
        salesQuotes.setText(wfmStrings.salesQuotes());
        salesOrders.setText(wfmStrings.salesOrders());
        requestForQuotes.setText(wfmStrings.requestForQuotes());
        recurringBills.setText(wfmStrings.recurringBills());
        supplierCenter.setText(wfmStrings.supplierCenter());
        bankAccounts.setText(wfmStrings.bankAccount());
        chartOfAccounts.setText(wfmStrings.chartOfAccounts());
        reservations.setText(wfmStrings.reservations());
        consignments.setText(wfmStrings.consignments());
        requestForPurchase.setText(wfmStrings.requestForPurchase());
        timesheetInvoice.setText("Timesheet Invoice");
        salesInvoising.setText(wfmStrings.saleInvoices());
        purchaseOrder.setText(wfmStrings.purchaseorder());
        purchaseInvoicing.setText(wfmStrings.purchaseInvoicing());
        recurringInvoices.setText(wfmStrings.recurringInvoices());
        inventoryManagement.setText(wfmStrings.inventoryManagement());
        expenseReporting.setText(wfmStrings.expenseReporting());
        fixedAssests.setText(wfmStrings.fixedAssets());
        checks.setText(wfmStrings.checks());
        manualTransactions.setText(wfmStrings.manualTransactions());
        customerCenterAccounting.setText(wfmStrings.customerCenter());
        productsServices.setText(Property.getPluralWithObjectCode(Constants.PRODUCTS_OR_SERVICES, wfmStrings.productsOrServices()));
        inventoryItems.setText(wfmStrings.inventoryItems());
        assemblyItems.setText(wfmStrings.assemblyItems());
        rentalItems.setText("Rental Item");
        rentalOrder.setText("Rental Order");
        //HRMS
        hrms.setText(wfmStrings.hrms());
        selectAllHRMS.setText(wfmStrings.selectAll());
        onButtonHRMS.setText(wfmStrings.on().toUpperCase());
        offButtonHRMS.setText(wfmStrings.off());
        leaveManagement.setText(wfmStrings.leaveManagement());
        attendingTracking.setText(wfmStrings.attendanceTracking());
        performanceAppraisal.setText(wfmStrings.performanceAppraisal());
        goalManagement.setText(wfmStrings.goalManagement());
        employeeExpenses.setText(wfmStrings.employeeExpenses());
        employeeIncidents.setText(wfmStrings.employeeIncidents());
        onboarding.setText(wfmStrings.onboarding());
        telegramChats.setText("Telegram Chats");
        //Additional Modules
        additionalModules.setText(wfmStrings.additionalModules());
        selectAllAdditional.setText(wfmStrings.selectAll());
        onButtonAdditional.setText(wfmStrings.on().toUpperCase());
        offButtonAdditional.setText(wfmStrings.off());
        payroll.setText(wfmStrings.payroll());
        reportingSystem.setText(wfmStrings.reportingSystem());
        documentManagement.setText(wfmStrings.documentManagement());
        trainingCenter.setText(wfmStrings.trainingCenter());
        websites.setText(wfmStrings.websites());
        permissionManagement.setText(wfmStrings.permissionManagement());
        customFields.setText(wfmStrings.customFields());
        customReferences.setText(wfmStrings.customReferences());
        syncronizeWithGoogle.setText(wfmStrings.syncronizeWithGoogle());
        reminders.setText(wfmStrings.reminders());
        //Add-Ons
        addOns.setText(wfmStrings.addOns());
        onButtonAddOns.setText(wfmStrings.on().toUpperCase());
        offButtonAddOns.setText(wfmStrings.off());
        selectAllAddOns.setText(wfmStrings.selectAll());
        iPhoneApps.setText(wfmStrings.iphoneApps());
        androidApps.setText(wfmStrings.androidApps());
        timesheetPlugin.setText(Property.get(Constants.TIMESHEET, wfmStrings.timesheetPlugin(), wfmStrings.timesheet()));
        outlookPlugin.setText(wfmStrings.outlookPlugin());
        storefront.setText(wfmStrings.storefront());
        invoiceTemplates.setText(wfmStrings.invoiceTemplates());
    }

    private void save(final KpiCheckBox checkBox, final HashSet<String> codes) {
        checkBox.addClickHandler(clickEvent -> saveData(codes, checkBox.getValue()));
    }

    private void showPopup(final KpiCheckBox checkBox, final String title) {
        checkBox.addClickHandler(clickEvent -> {
            if (checkBox.getValue()) {
                showFeedBacktPopup(title);
                checkBox.setValue(false);
            }
        });
    }

    private void saveData(HashSet<String> codes, boolean isSave) {
        if (Utils.isBACKEND() && companyID == null) {
            String selectedHost = hostName.getSelectedItem() != null ? hostName.getSelectedItem().getName() : Utils.getHostName();
            moduleService.saveModules(selectedHost, codes, isSave, new AbstractAsyncCallback<Void>() {
            });
        } else {
            moduleService.save(companyID, codes, isSave, new AbstractAsyncCallback<Void>() {
            });
        }
    }

    private void addOnsSection() {
        showHide(addOns, "addOns");
        selectAllAddOns.addClickHandler(clickEvent -> {
            if (isCheckAddOns) {
                checkAddOnsCheckboxes(false);
                isCheckAddOns = false;
                saveData(new HashSet<String>(Arrays.asList(IPHONE_APPS, ANDROID_APPS, TIMESHEET_PLUGIN, OUTLOOK_PLUGIN, STOREFRONT,
                        SERIAL_NUMBER_TRACKING, LANDING_COST, DOUBLE_TAX, INVOICE_TEMPLATES)), false);
            } else {
                checkAddOnsCheckboxes(true);
                isCheckAddOns = true;
                saveData(new HashSet<String>(Arrays.asList(IPHONE_APPS, ANDROID_APPS, TIMESHEET_PLUGIN, OUTLOOK_PLUGIN, STOREFRONT,
                        SERIAL_NUMBER_TRACKING, LANDING_COST, DOUBLE_TAX, INVOICE_TEMPLATES)), true);
            }
        });
        onButtonAddOns.addClickHandler(clickEvent -> {
            onButtonAddOns.setStyleName("btn act");
            offButtonAddOns.setStyleName("btn");
            showHideButton(addOns, true, "addOns");
            saveData(new HashSet<String>(Collections.singletonList(ADD_ONS_MODULE)), true);
        });
        offButtonAddOns.addClickHandler(clickEvent -> {
            offButtonAddOns.setStyleName("btn act");
            onButtonAddOns.setStyleName("btn");
            showHideButton(addOns, false, "addOns");
            checkAddOnsCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(ADD_ONS_MODULE, IPHONE_APPS, ANDROID_APPS, TIMESHEET_PLUGIN, OUTLOOK_PLUGIN, STOREFRONT,
                    SERIAL_NUMBER_TRACKING, LANDING_COST, DOUBLE_TAX, INVOICE_TEMPLATES)), false);
        });
    }

    private void additionalSection() {
        showHide(additionalModules, "additional");
        selectAllAdditional.addClickHandler(clickEvent -> {
            if (isCheckAdditional) {
                checkAdditionalCheckboxes(false);
                isCheckAdditional = false;
                saveData(new HashSet<String>(Arrays.asList(PAYROLL, REPORTING_SYSTEM, DOCUMENT_MANAGEMENT, TRAINING_CENTER, WEBSITES,
                        PERMISSION_MANAGEMENT, CUSTOM_FIELDS, CUSTOM_REFERENCES, SYNCRONIZE_WITH_GOOGLE, REMINDERS)), false);
            } else {
                checkAdditionalCheckboxes(true);
                isCheckAdditional = true;
                saveData(new HashSet<String>(Arrays.asList(PAYROLL, REPORTING_SYSTEM, DOCUMENT_MANAGEMENT, TRAINING_CENTER, WEBSITES,
                        PERMISSION_MANAGEMENT, CUSTOM_FIELDS, CUSTOM_REFERENCES, SYNCRONIZE_WITH_GOOGLE, REMINDERS)), true);
            }
        });
        onButtonAdditional.addClickHandler(clickEvent -> {
            onButtonAdditional.setStyleName("btn act");
            offButtonAdditional.setStyleName("btn");
            showHideButton(additionalModules, true, "additional");
            saveData(new HashSet<String>(Collections.singletonList(ADDITIONAL_MODULE)), true);
        });
        offButtonAdditional.addClickHandler(clickEvent -> {
            offButtonAdditional.setStyleName("btn act");
            onButtonAdditional.setStyleName("btn");
            showHideButton(additionalModules, false, "additional");
            checkAdditionalCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(ADDITIONAL_MODULE, PAYROLL, REPORTING_SYSTEM, DOCUMENT_MANAGEMENT, TRAINING_CENTER, WEBSITES,
                    PERMISSION_MANAGEMENT, CUSTOM_FIELDS, CUSTOM_REFERENCES, SYNCRONIZE_WITH_GOOGLE, REMINDERS)), false);
        });
    }

    private void hrmsSection() {
        showHide(hrms, "hrms");
        selectAllHRMS.addClickHandler(clickEvent -> {
            if (isCheckHRMS) {
                checkHRMSCheckboxes(false);
                isCheckHRMS = false;
                saveData(new HashSet<String>(Arrays.asList(LEAVE_MANAGEMENT, ATTENDING_TRACKING, PERFORMANCE_APPRAISAL, GOAL_MANAGEMENT,
                        EMPLOYEE_EXPENSES, EMPLOYEE_INCIDENTS, ONBOARDING, TELEGRAM_CHATS)), false);
            } else {
                checkHRMSCheckboxes(true);
                isCheckHRMS = true;
                saveData(new HashSet<String>(Arrays.asList(LEAVE_MANAGEMENT, ATTENDING_TRACKING, PERFORMANCE_APPRAISAL, GOAL_MANAGEMENT,
                        EMPLOYEE_EXPENSES, EMPLOYEE_INCIDENTS, ONBOARDING, TELEGRAM_CHATS)), true);
            }
        });
        onButtonHRMS.addClickHandler(clickEvent -> {
            onButtonHRMS.setStyleName("btn act");
            offButtonHRMS.setStyleName("btn");
            showHideButton(hrms, true, "hrms");
            saveData(new HashSet<String>(Collections.singletonList(HRMS_MODULE)), true);

        });
        offButtonHRMS.addClickHandler(clickEvent -> {
            offButtonHRMS.setStyleName("btn act");
            onButtonHRMS.setStyleName("btn");
            showHideButton(hrms, false, "hrms");
            checkHRMSCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(HRMS_MODULE, LEAVE_MANAGEMENT, ATTENDING_TRACKING, PERFORMANCE_APPRAISAL, GOAL_MANAGEMENT,
                    EMPLOYEE_EXPENSES, EMPLOYEE_INCIDENTS, ONBOARDING, TELEGRAM_CHATS)), false);
        });
    }

    private void accountingSection() {
        showHide(accountingAndFinance, "accounting");
        selectAllAccounting.addClickHandler(clickEvent -> {
            if (isCheckAccounting) {
                checkAccountingCheckboxes(false);
                isCheckAccounting = false;
                saveData(new HashSet<String>(Arrays.asList(SALES_QUOTES, SALES_ORDERS, REQUEST_FOR_QUOTES, RECURRING_BILLS, SUPPLIER_CENTER, BANK_ACCOUNTS, ACCOUNTING_CHART_OF_ACCOUNTS, RESERVATIONS, CONSIGNMENTS, SALES_INVOICING, PURCHASE_ORDERS, PURCHASE_INVOICING, RECCURING_INVOICES,
                        INVENTORY_MANAGEMENT, EXPENSE_REPORTING, FIXED_ASSESTS, CHECKS, MANUAL_TRANSACTIONS, ACCOUNTING_CUSTOMER_CENTER, REQUEST_FOR_PURCHASES, TIMESHEET_INVOICES)), false);
            } else {
                checkAccountingCheckboxes(true);
                isCheckAccounting = true;
                saveData(new HashSet<String>(Arrays.asList(SALES_QUOTES, SALES_ORDERS, REQUEST_FOR_QUOTES, RECURRING_BILLS, SUPPLIER_CENTER, BANK_ACCOUNTS, ACCOUNTING_CHART_OF_ACCOUNTS, RESERVATIONS, CONSIGNMENTS, SALES_INVOICING, PURCHASE_ORDERS, PURCHASE_INVOICING, RECCURING_INVOICES,
                        INVENTORY_MANAGEMENT, EXPENSE_REPORTING, FIXED_ASSESTS, CHECKS, MANUAL_TRANSACTIONS, ACCOUNTING_CUSTOMER_CENTER, REQUEST_FOR_PURCHASES, TIMESHEET_INVOICES)), true);
            }
        });
        onButtonAccounting.addClickHandler(clickEvent -> {
            onButtonAccounting.setStyleName("btn act");
            offButtonAccounting.setStyleName("btn");
            showHideButton(accountingAndFinance, true, "accounting");
            saveData(new HashSet<String>(Collections.singletonList(ACCOUNTING_MODULE)), true);
        });
        offButtonAccounting.addClickHandler(clickEvent -> {
            offButtonAccounting.setStyleName("btn act");
            onButtonAccounting.setStyleName("btn");
            showHideButton(accountingAndFinance, false, "accounting");
            checkAccountingCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(ACCOUNTING_MODULE, SALES_QUOTES, SALES_ORDERS, REQUEST_FOR_QUOTES, RECURRING_BILLS, SUPPLIER_CENTER, BANK_ACCOUNTS, ACCOUNTING_CHART_OF_ACCOUNTS, RESERVATIONS, CONSIGNMENTS, SALES_INVOICING, PURCHASE_ORDERS, PURCHASE_INVOICING, RECCURING_INVOICES,
                    INVENTORY_MANAGEMENT, EXPENSE_REPORTING, FIXED_ASSESTS, CHECKS, MANUAL_TRANSACTIONS, ACCOUNTING_CUSTOMER_CENTER, REQUEST_FOR_PURCHASES, TIMESHEET_INVOICES)), false);
        });
    }


    private void crmSection() {
        showHide(crm, "crm");

        selectAllCrm.addClickHandler(clickEvent -> {
            if (isCheckCRM) {
                checkCrmCheckboxes(false);
                isCheckCRM = false;
                saveData(new HashSet<String>(Arrays.asList(LEAD_MANAGEMENT, CONTACT_MANAGEMENT, OPPORTUNITY_TRACKING, ACTIVITIES,
                        CRM_CALENDAR, IS_ACTIVE_WORKFLOW_STATUS, CASE_MANAGEMENT, MESSAGE_CENTER, SMS_INTEGRATION, SOLUTION_MANAGEMENT, CRM_REQUEST_FOR_QUOTES, TASK_MANAGEMENT)), false);
            } else {
                checkCrmCheckboxes(true);
                isCheckCRM = true;
                if (Utils.isBACKEND()) {
                    saveData(new HashSet<String>(Arrays.asList(LEAD_MANAGEMENT, CONTACT_MANAGEMENT, OPPORTUNITY_TRACKING, ACTIVITIES,
                            CRM_CALENDAR, IS_ACTIVE_WORKFLOW_STATUS, CASE_MANAGEMENT, MESSAGE_CENTER, SMS_INTEGRATION, SOLUTION_MANAGEMENT, CRM_REQUEST_FOR_QUOTES, TASK_MANAGEMENT)), true);
                } else {
                    saveData(new HashSet<String>(Arrays.asList(LEAD_MANAGEMENT, CONTACT_MANAGEMENT, OPPORTUNITY_TRACKING, ACTIVITIES,
                            CRM_CALENDAR, IS_ACTIVE_WORKFLOW_STATUS, CASE_MANAGEMENT, MESSAGE_CENTER, SOLUTION_MANAGEMENT, CRM_REQUEST_FOR_QUOTES, TASK_MANAGEMENT)), true);
                }
            }
        });
        onButtonCrm.addClickHandler(clickEvent -> {
            onButtonCrm.setStyleName("btn act");
            offButtonCrm.setStyleName("btn");
            showHideButton(crm, true, "crm");
            saveData(new HashSet<String>(Collections.singletonList(CRM_MODULE)), true);

        });
        offButtonCrm.addClickHandler(clickEvent -> {
            offButtonCrm.setStyleName("btn act");
            onButtonCrm.setStyleName("btn");
            showHideButton(crm, false, "crm");
            checkCrmCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(CRM_MODULE, LEAD_MANAGEMENT, CONTACT_MANAGEMENT, OPPORTUNITY_TRACKING, ACTIVITIES,
                    CRM_CALENDAR, IS_ACTIVE_WORKFLOW_STATUS, CASE_MANAGEMENT, MESSAGE_CENTER, SMS_INTEGRATION, SOLUTION_MANAGEMENT, CRM_REQUEST_FOR_QUOTES, TASK_MANAGEMENT)), false);
        });
    }

    private void pmSection() {
        showHide(projectManagement, "projectManagement");
        selectAllPM.addClickHandler(clickEvent -> {
            if (isCheckPM) {
                checkPMCheckboxes(false);
                isCheckPM = false;
                saveData(new HashSet<String>(Arrays.asList(TASK_MANAGEMENT, ISSUE_TRACKING, TIMESHEET, MONTHLY_TIMESHEET, RESOURCE_PLANNING,
                        BOOKING_ITEMS, GANTT_CHART, TIMER, IMPORT_FROM_MS_PROJECT, PROJECT_EMAILS, CUSTOMER_CENTER)), false);

            } else {
                checkPMCheckboxes(true);
                isCheckPM = true;
                saveData(new HashSet<String>(Arrays.asList(TASK_MANAGEMENT, ISSUE_TRACKING, TIMESHEET, MONTHLY_TIMESHEET, RESOURCE_PLANNING,
                        BOOKING_ITEMS, GANTT_CHART, TIMER, IMPORT_FROM_MS_PROJECT, PROJECT_EMAILS, CUSTOMER_CENTER)), true);
            }
        });
        onButtonPM.addClickHandler(clickEvent -> {
            onButtonPM.setStyleName("btn act");
            offButtonPM.setStyleName("btn");
            showHideButton(projectManagement, true, "projectManagement");
            saveData(new HashSet<String>(Collections.singletonList(PM_MODULE)), true);
        });
        offButtonPM.addClickHandler(clickEvent -> {
            offButtonPM.setStyleName("btn act");
            onButtonPM.setStyleName("btn");
            showHideButton(projectManagement, false, "projectManagement");
            checkPMCheckboxes(false);
            saveData(new HashSet<String>(Arrays.asList(PM_MODULE, TASK_MANAGEMENT, ISSUE_TRACKING, TIMESHEET, MONTHLY_TIMESHEET, RESOURCE_PLANNING,
                    BOOKING_ITEMS, GANTT_CHART, TIMER, IMPORT_FROM_MS_PROJECT, PROJECT_EMAILS, CUSTOMER_CENTER)), false);
        });
    }

    private void showHideButton(final Anchor widget, boolean isShow, String divId) {
        if (isShow) {
            DOM.getElementById(divId).getStyle().setDisplay(Style.Display.BLOCK);
            widget.getElement().setClassName("");
        } else {
            DOM.getElementById(divId).getStyle().setDisplay(Style.Display.NONE);
            widget.getElement().setClassName("collapsed");
        }
    }

    private void showHide(final Anchor widget, final String divId) {
        widget.addClickHandler(clickEvent -> {
            String workspace1 = DOM.getElementById(divId).getStyle().getDisplay();
            if ("none".equals(workspace1)) {
                DOM.getElementById(divId).getStyle().setDisplay(Style.Display.BLOCK);
                widget.getElement().setClassName("");
            } else {
                DOM.getElementById(divId).getStyle().setDisplay(Style.Display.NONE);
                widget.getElement().setClassName("collapsed");
            }
        });
    }

    private void checkAddOnsCheckboxes(boolean isCheck) {
        iPhoneApps.setValue(isCheck);
        androidApps.setValue(isCheck);
        timesheetPlugin.setValue(isCheck);
        outlookPlugin.setValue(isCheck);
        storefront.setValue(isCheck);
        invoiceTemplates.setValue(isCheck);
    }

    private void checkAdditionalCheckboxes(boolean isCheck) {
        payroll.setValue(isCheck);
        reportingSystem.setValue(isCheck);
        documentManagement.setValue(isCheck);
        trainingCenter.setValue(isCheck);
        websites.setValue(isCheck);
        permissionManagement.setValue(isCheck);
        customReferences.setValue(isCheck);
        customFields.setValue(isCheck);
        syncronizeWithGoogle.setValue(isCheck);
        reminders.setValue(isCheck);
    }

    private void checkHRMSCheckboxes(boolean isCheck) {
        leaveManagement.setValue(isCheck);
        attendingTracking.setValue(isCheck);
        performanceAppraisal.setValue(isCheck);
        goalManagement.setValue(isCheck);
        employeeExpenses.setValue(isCheck);
        employeeIncidents.setValue(isCheck);
        onboarding.setValue(isCheck);
        telegramChats.setValue(isCheck);
    }

    private void checkAccountingCheckboxes(boolean isCheck) {
        salesQuotes.setValue(isCheck);
        salesOrders.setValue(isCheck);
        requestForQuotes.setValue(isCheck);
        recurringBills.setValue(isCheck);
        supplierCenter.setValue(isCheck);
        bankAccounts.setValue(isCheck);
        chartOfAccounts.setValue(isCheck);
        reservations.setValue(isCheck);
        consignments.setValue(isCheck);
        requestForPurchase.setValue(isCheck);
        timesheetInvoice.setValue(isCheck);
        salesInvoising.setValue(isCheck);
        purchaseOrder.setValue(isCheck);
        purchaseInvoicing.setValue(isCheck);
        recurringInvoices.setValue(isCheck);
        inventoryManagement.setValue(isCheck);
        expenseReporting.setValue(isCheck);
        fixedAssests.setValue(isCheck);
        checks.setValue(isCheck);
        manualTransactions.setValue(isCheck);
        customerCenterAccounting.setValue(isCheck);
        productsServices.setValue(isCheck);
        inventoryItems.setValue(isCheck);
        rentalItems.setValue(isCheck);
        assemblyItems.setValue(isCheck);
        rentalOrder.setValue(isCheck);
    }

    private void checkCrmCheckboxes(boolean isCheck) {
        leadManagement.setValue(isCheck);
        contactManagement.setValue(isCheck);
        opportunityTracking.setValue(isCheck);
        activities.setValue(isCheck);
        calenderCrm.setValue(isCheck);
        workflowRecurrence.setValue(isCheck);
        caseManagement.setValue(isCheck);
        messageCenter.setValue(isCheck);
        smsIntegration.setValue(isCheck);
        solutionsManagement.setValue(isCheck);
        task.setValue(isCheck);
        crmRequestForQuotes.setValue(isCheck);
        productsServicesCrm.setValue(isCheck);
    }

    private void checkCustomerCheckboxes(boolean isCheck) {
        solutionsManagement.setValue(isCheck);
        caseManagement.setValue(isCheck);
        task.setValue(isCheck);
    }

    private void checkPMCheckboxes(boolean isCheck) {
        taskManagement.setValue(isCheck);
        issueTracking.setValue(isCheck);
        timesheet.setValue(isCheck);
        monthlyTimesheet.setValue(isCheck);
        resourcePlanning.setValue(isCheck);
        bookingItems.setValue(isCheck);
        ganttChart.setValue(isCheck);
        timer.setValue(isCheck);
        importFromMSProject.setValue(isCheck);
        customerCenter.setValue(isCheck);
    }

    private void checkWorkspaceCheckboxes(boolean isCheck) {
        callendar.setValue(isCheck);
        meetingMinutes.setValue(isCheck);
        stuffUpdates.setValue(isCheck);
        companyNews.setValue(isCheck);
        notes.setValue(isCheck);
    }

    private static void showFeedBacktPopup(String title) {
        KpiModal feedBack = new KpiModal();
        feedBack.setTitle("<b class='customTitle'>" + wfmStrings.requestForDemo() + "</b>");
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("680px", "310px");
        FeedbackForm feedbackForm = new FeedbackForm("requestADemo");
        feedbackForm.setDemoRequestTitle(title);
        feedbackForm.setType("requestADemo");
        feedbackForm.setShell(feedBack);
        feedbackForm.setStyleName("workforce");
        scrollPanel.setWidget(feedbackForm);
        feedBack.setWidth("565px");
        feedBack.add(scrollPanel);
        feedBack.center();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}