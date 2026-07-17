package com.edatasite.workforce.gwt.crm.client.factory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.AddProductHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockAdjustmentHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.accounting.StockTransferHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.AddProductCategoryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategoriesListHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.inventory.ProductCategorySummaryHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.history.report.CrmAccountBalanceHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.ImportExport.ImportBudgetManagerHistoryProcessor;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewBudgetSheetView;
import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.SupplierHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.history.CustomFormItemHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookEditHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.CRM;
import com.edatasite.workforce.gwt.crm.client.CRMDashboardSinksContainer;
import com.edatasite.workforce.gwt.crm.client.history.CampaignEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CampaignHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CaseHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactMergeHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmAccountEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmAccountHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmAccountImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmLeadImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.CrmOpportunityImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.GoogleContactImportHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.LeadEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.LeadHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MailListHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MembersGroupHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MergeHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MessagesHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MultiContactHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.MultiLeadHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.OpportunityHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.SolutionHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.WebFormEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.WebFormHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.ui.view.CampaignListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ContactListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmAccountListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.LeadListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.MailListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.MessageListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.SentMessageListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.SolutionListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.WebFormsListView;
import com.edatasite.workforce.gwt.googlecalendar.client.history.EventHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.emailcompose.AccountingEmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.BatchPaymentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.payment.InvoicePaymentHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseinvoice.PurchaseInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.purchaseorder.PurchaseOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.rfq.RequestForQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.saleinvoice.SaleInvoiceHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleOrderHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.BookingItemsHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.BookingItemsReservationHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.ContractHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.TaskHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.WorkstreamHistoryProcessor;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 13:19:31
 */
public class CRMSinksContainerFactory extends SinksContainerFactory implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private SinksContainer selectedContener;
    private boolean isFirstContener = true;

    public CRMSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer("leadList");
    }

    public void initDefaultContainers() {
        if (CRM.dashboards.size() > 0) {
            CRMDashboardSinksContainer dashboardContainer = new CRMDashboardSinksContainer();
            dashboardContainer.setPreparedView("dashboard_" + dashboardContainer.normalizeName(CRM.defaultDashboardName));
            putContainer(dashboardContainer);
            setDefaultContainer(dashboardContainer.getName());
            setDashboardContainer(dashboardContainer);
        }

        if (Utils.getPropertyListingMap() != null && Utils.getPropertyListingMap().size() > 0) {
            setCRMPropertyListingsMap(Utils.getPropertyListingMap());
        }
        //As Munir asked we need to open second container if dashboard has only one view
        if (CRM.dashboards.size() == 1) {
            openSecondContainer();
        }
        AccountingUtils.instance = AccountingUtils.get();
    }

    public void registerProcessors() {
        registerHistoryProcessor(TASK, new TaskHistoryProcessor());
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());
        registerHistoryProcessor(MERGE, new MergeHistoryProcessor());
        registerHistoryProcessor(WORKSTREAM, new WorkstreamHistoryProcessor());
        registerHistoryProcessor("contactmerge", new ContactMergeHistoryProcessor());
        registerHistoryProcessor("lead", new LeadHistoryProcessor());
        registerHistoryProcessor("webform", new WebFormHistoryProcessor());
        registerHistoryProcessor("leadedit", new LeadEditHistoryProcessor());
        registerHistoryProcessor("webformedit", new WebFormEditHistoryProcessor());
        registerHistoryProcessor("multilead", new MultiLeadHistoryProcessor());
        registerHistoryProcessor("account", new CrmAccountHistoryProcessor());
        registerHistoryProcessor("accountedit", new CrmAccountEditHistoryProcessor());
        registerHistoryProcessor("contact", new ContactHistoryProcessor());
        registerHistoryProcessor("contactedit", new ContactEditHistoryProcessor());
        registerHistoryProcessor("multicontact", new MultiContactHistoryProcessor());
        registerHistoryProcessor("opportunity", new OpportunityHistoryProcessor());
        registerHistoryProcessor("requestforquote", new RequestForQuoteHistoryProcessor());
        registerHistoryProcessor("campaign", new CampaignHistoryProcessor());
        registerHistoryProcessor("campaignedit", new CampaignEditHistoryProcessor());
        registerHistoryProcessor("case", new CaseHistoryProcessor());
        registerHistoryProcessor("employee", new CrmEmployeeHistoryProcessor());
        registerHistoryProcessor("event", new EventHistoryProcessor());
        registerHistoryProcessor("import", new ContactImportHistoryProcessor());
        registerHistoryProcessor("importaccount", new CrmAccountImportHistoryProcessor());
        registerHistoryProcessor("importlead", new CrmLeadImportHistoryProcessor());
        registerHistoryProcessor("importopportunity", new CrmOpportunityImportHistoryProcessor());
        registerHistoryProcessor("importbudgetmanager", new ImportBudgetManagerHistoryProcessor());
        registerHistoryProcessor("gcontact", new GoogleContactImportHistoryProcessor());
        registerHistoryProcessor("message", new MessagesHistoryProcessor());
        registerHistoryProcessor("maillist", new MailListHistoryProcessor());
        registerHistoryProcessor("supplier", new SupplierHistoryProcessor());
        registerHistoryProcessor("client", new ClientHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
        registerHistoryProcessor("bookingitemsreservation", new BookingItemsReservationHistoryProcessor());
        registerHistoryProcessor("bookingitems", new BookingItemsHistoryProcessor());
        registerHistoryProcessor("membersgroup", new MembersGroupHistoryProcessor());
        registerHistoryProcessor("salequote", new SaleQuoteHistoryProcessor());
        registerHistoryProcessor("saleorder", new SaleOrderHistoryProcessor());
        registerHistoryProcessor("saleinvoice", new SaleInvoiceHistoryProcessor());
        registerHistoryProcessor("purchaseinvoice", new PurchaseInvoiceHistoryProcessor());
        registerHistoryProcessor("purchaseorder", new PurchaseOrderHistoryProcessor());
        registerHistoryProcessor("messagecenter", new EmailHistoryProcessor());
        registerHistoryProcessor("solution", new SolutionHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("accountingemailcompose", new AccountingEmailComposeHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor(ITEM_LIST, new CustomFormItemHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("receivepayment", new BatchPaymentHistoryProcessor());
        registerHistoryProcessor("invoicepayment", new InvoicePaymentHistoryProcessor());
        registerHistoryProcessor("productcategorylist", new ProductCategoriesListHistoryProcessor());
        registerHistoryProcessor("product", new AddProductHistoryProcessor());
        registerHistoryProcessor("productcategory", new AddProductCategoryHistoryProcessor());
        registerHistoryProcessor("productcategoryview", new ProductCategorySummaryHistoryProcessor());
        registerHistoryProcessor("stockadjustment", new StockAdjustmentHistoryProcessor());
        registerHistoryProcessor("stocktransfer", new StockTransferHistoryProcessor());
        registerHistoryProcessor("customerBalance", new CrmAccountBalanceHistoryProcessor());
        registerHistoryProcessor("webhook", new WorkflowWebHookHistoryProcessor());
        registerHistoryProcessor("webhookEdit", new WorkflowWebHookEditHistoryProcessor());
        registerHistoryProcessor("contract", new ContractHistoryProcessor());
    }

    public void registerMenuItems() {
        if (Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD, PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES, PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_ADD,
                PermissionConstants.CRM_ACCOUNT_ADD, PermissionConstants.CRM_ADD_NEW_CONTACT, PermissionConstants.CRM_MULTI_ADD_NEW_CONTACTS,
                PermissionConstants.CRM_ADD_NEW_CAMPAIGN, PermissionConstants.ADD_NEW_CASE, PermissionConstants.ADD_NEW_SOLUTION)) {
            if (Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD)) {
                addNewMenuItem(Property.get(Constants.LEADS, wfmStrings.lead()), "lead|add/add", null, "crmAddLead");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_MULTIPLE_ADD_NEW_LEADS)) {
                addNewMenuItem(Property.getPluralWithObjectCodeWithReplace(Constants.LEADS, crmStrings.multiLead(), wfmStrings.leads()), "multilead|add/add", null, "crmAddMultiLead");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES)) {
                addNewMenuItem(Property.get(Constants.Opportunities, wfmStrings.opportunity()), "opportunity|add/add", null, "crmAddOpportunity");
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_ADD)) {
                addNewMenuItem(Property.get(Constants.REQUEST_FOR_QUOTE, Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote())), "requestforquote|add/add", null, "crmAddRequestForQuote");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNT_ADD)) {
                addNewMenuItem(wfmStrings.company(), "account|add/add", null, "crmAddAccount");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT)) {
                addNewMenuItem(Property.get(Constants.Contacts, wfmStrings.customers()), "contact|add/add", null, "crmAddContact");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_MULTI_ADD_NEW_CONTACTS)) {
                addNewMenuItem(crmStrings.multiContact(), "multicontact|add/add", null, "crmAddMultiContact");
            }
            if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CAMPAIGN)) {
                addNewMenuItem(wfmStrings.campaign(), "campaign|add/add", null, "crmAddCampaign");
            }
            if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
                addNewMenuItem(Property.get(Constants.CASE_LIST, wfmStrings.caseID()), "case|add/add", null, "crmAddCase");
            }
            if (Utils.hasPermission(PermissionConstants.ADD_NEW_SOLUTION)) {
                addNewMenuItem(crmStrings.solution(), "solution|add/add", null, "crmAddSolution");
            }
        } else {
            disableAddNew();
        }
    }

    private void setCRMPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {

        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (ModuleEnum.CRM.getCode().equals(selectItem.getDescription())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case "budgetsheetView":
                                viewList.add(new NewBudgetSheetView());
                                break;
                            case LEADS:
                                viewList.add(new LeadListView());
                                break;
                            case Opportunities:
                                viewList.add(new OpportunitiesListView(null, null));
                                break;
                            case SALE_QUOTE:
                                viewList.add(new SaleQuoteListView(null, false));
                                break;
                            case CRM_ACCOUNT_LIST:
                                viewList.add(new CrmAccountListView());
                                break;
                            case Contacts:
                                viewList.add(new ContactListView());
                                break;
                            case REQUEST_FOR_QUOTE:
                                viewList.add(new RequestForQuoteListView());
                                break;
                            case EVENT_LIST:
                                viewList.add(new EventListView(null));
                                break;
                            case CASE_LIST:
                                viewList.add(new CaseListView());
                                break;
                            case SOLUTION_LIST:
                                viewList.add(new SolutionListView());
                                break;
                            case TASK:
                                viewList.add(new CrmTaskListView(null, null, false));
                                break;
                            case MAIL_LIST:
                                viewList.add(new MailListView());
                                break;
                            case "scheduled_messages":
                                viewList.add(new MessageListView());
                                break;
                            case "SentMessages":
                                viewList.add(new SentMessageListView());
                                break;
                            case CAMPAIGN_LIST:
                                viewList.add(new CampaignListView());
                                break;
                            case "webFormsList":
                                viewList.add(new WebFormsListView());
                                break;
                            case PRODUCTS_OR_SERVICES:
                                viewList.add(new ProductsServicesListView());
                                break;
                            default:
                                if (propertyItem.isCustom()) {
                                    if (Constants.PAGE.equals(propertyItem.getType())) {
                                        if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                                            viewList.add(new CustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        } else if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(propertyItem.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                                            viewList.add(new AddCustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        }
                                    } else {
                                        viewList.add(new CustomFormItemListView(propertyItem.getfID(), getLocalizedPlural(propertyItem), propertyItem.getFormID()));
                                    }
                                }
                        }
                    }
                }
            }

            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContener) {
                selectedContener = dynamicSC;
                setSelection(selectedContener);
                isFirstContener = false;
            }
            setSinksContainer(dynamicSC);
        }
    }

    private String getLocalizedPlural(PropertyItem propertyItem) {
        if (propertyItem.getlPlural() != null) {
            switch (Utils.getUserLanguage()) {
                case "en":
                    return propertyItem.getlPlural().getEnglishName();
                case "ar":
                    return propertyItem.getlPlural().getArabicName();
                case "ru":
                    return propertyItem.getlPlural().getRussianName();
                case "uz":
                    return propertyItem.getlPlural().getUzbekName();
            }
        }
        return propertyItem.getPlural();
    }
}
