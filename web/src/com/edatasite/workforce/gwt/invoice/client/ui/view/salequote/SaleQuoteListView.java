package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.CustomJsEvents;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 15:58:56
 * To change this template use File | Settings | File Templates.
 */

public class SaleQuoteListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final Reference reference = Reference.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();
    private final boolean lockCompleted = Utils.isLockCompletedProjecItems();
    private static final AccountingUtils utils = AccountingUtils.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private ListingPanel listPanel;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;
    private ListingFilterParameter fp;
    private boolean isAccountingSection = true;
    private KpiModal shell;
    private final DataListBox contactName = new DataListBox();
    private WfmButton2 okBut;
    private WfmButton2 cancelBut;
    private final String salesQuote = "salesQuote";
    private ActionButton addNew;
    private boolean removeAddNew = false;
    private boolean isBlocked = false;
    private Integer productId;
    private String relationType;


    public SaleQuoteListView() {
        super(SALE_QUOTE);
        setDescription(property.getPlural(wfmStrings.salesQuotes()));
        if (hasPermissionToAdd()) {
            setAddNew("salequote|add/add");
        }
    }

    public SaleQuoteListView(Integer productId) {
        super(SALE_QUOTE);
        this.productId = productId;
        setDescription(property.getPlural(wfmStrings.salesQuotes()));
        if (hasPermissionToAdd()) {
            setAddNew("salequote|add/add");
        }
    }

    public SaleQuoteListView(Integer productId, String relationType) {
        super(SALE_QUOTE);
        this.productId = productId;
        this.relationType = relationType;
        setDescription(property.getPlural(wfmStrings.salesQuotes()));
        if (hasPermissionToAdd()) {
            setAddNew("salequote|add/add");
        }
    }

    protected boolean hasPermissionToAdd() {
        return !isBlocked && ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)) || (!isAccountingSection && (Utils.hasPermission(CRM_SALES_QUOTE_ADD))));
    }

    public SaleQuoteListView(ListingFilterParameter filterParameters, boolean isAccountingSection) {
        super(SALE_QUOTE);
        setDescription(property.getPlural(wfmStrings.salesQuotes()));
        if (hasPermissionToAdd()) {
            setAddNew("salequote|add/add");
        }
        this.fp = filterParameters;

        this.isAccountingSection = isAccountingSection;
        if (!isAccountingSection && fp != null && fp.getCrmAccountId() == null && (fp.getOpportunityID() != null || fp.getCrmContactId() != null)) {
            InvoiceService.App.get().findIDsBy(fp, new AbstractAsyncCallback<HashMap<String, Integer>>() {
                public void failure(Throwable caught) {

                }

                public void success(HashMap<String, Integer> ids) {
                    if (ids != null && ids.size() > 0) {
                        boolean contactFound = false;
                        for (Map.Entry<String, Integer> entry : ids.entrySet()) {
                            if (CrmConstants.CRM_CONTACT.equals(entry.getKey())) {
                                fp.setCrmContactId(entry.getValue());
                                contactFound = true;
                            } else if (CrmConstants.CRM_ACCOUNT.equals(entry.getKey())) {
                                fp.setAccountID(entry.getValue());
                            }
                        }
                        if (!contactFound && fp.getOpportunityID() != null) {
                            if (addNew == null) {
                                removeAddNew = true;
                            } else {
                                addNew.removeFromParent();
                            }
                        }
                    }
                }
            });
        }
    }

    public SaleQuoteListView(ListingFilterParameter filterParameters, boolean isAccountingSection, boolean isBlocked) {
        super(SALE_QUOTE);
        setDescription(property.getSingular(wfmStrings.salesQuote()));
        if (hasPermissionToAdd()) {
            setAddNew("salequote|add/add");
        }
        this.fp = filterParameters;
        this.isBlocked = isBlocked;

        this.isAccountingSection = isAccountingSection;
        if (!isAccountingSection && fp != null && fp.getCrmAccountId() == null && (fp.getOpportunityID() != null || fp.getCrmContactId() != null)) {
            InvoiceService.App.get().findIDsBy(fp, new AbstractAsyncCallback<HashMap<String, Integer>>() {
                public void failure(Throwable caught) {

                }

                public void success(HashMap<String, Integer> ids) {
                    if (ids != null && ids.size() > 0) {
                        boolean contactFound = false;
                        for (Map.Entry<String, Integer> entry : ids.entrySet()) {
                            if (CrmConstants.CRM_CONTACT.equals(entry.getKey())) {
                                fp.setCrmContactId(entry.getValue());
                                contactFound = true;
                            } else if (CrmConstants.CRM_ACCOUNT.equals(entry.getKey())) {
                                fp.setAccountID(entry.getValue());
                            }
                        }
                        if (!contactFound && fp.getOpportunityID() != null) {
                            if (addNew == null) {
                                removeAddNew = true;
                            } else {
                                addNew.removeFromParent();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected Widget onInitialize() {
        if (!Utils.hasRole(CLIENT)) {
            listPanel = new GuideListingPanel(ListPanelType.SaleQuoteListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST_CUSTOMIZE), true, Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST_FILTER));
        } else {
            listPanel = new GuideListingPanel(ListPanelType.SaleQuoteListPanel, getColumnConfig(), getListProvider(), getListDesign(), Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST_CUSTOMIZE), false, Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST_FILTER));
        }

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveSalesQuoteCellValue((NewInvoice) rowValue, columnCodeName));

        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/saleQuoteListPDFHandler";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListPDF(pdfURL, filterParametrs);
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSaleQuotesListExcel";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListExcel(excelURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_ADDED, SaleQuoteListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, SaleQuoteListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (delete != null) {
                    delete.setVisible(true);
                }
            } else {
                if (delete != null) {
                    delete.setVisible(false);
                }
            }
        });

        add(listPanel);
        return null;
    }

    private void saveSalesQuoteCellValue(NewInvoice rowValue, String columnCodeName) {
        if (((Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT) && Utils.isCustomField(columnCodeName))) || rowValue.hasAccess()) {
            quoteService.saveSaleQuoteEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });

        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            listPanel.reloadPage();
        }
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<NewInvoice, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final NewInvoice invoice) {
                String statusCode = invoice.getStatusCode();
                final Integer objectID = invoice.getID();
                boolean isManagerApproval = invoice.getCurrentApproverSelectItem() != null;
                final boolean hasAccessToChange = !lockCompleted || (lockCompleted && !PS_CLOSED.equals(invoice.getProjectStatusCode()));

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (!DRAFT.equals(statusCode))
                    if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY) || Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY)) {
                        MenuPopItem quoteSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                        quoteSummary.ensureDebugId(salesQuote + "quoteSummary");
                        quoteSummary.setCommand(() -> {
                            if (!isAccountingSection && !Utils.isCRM()) {
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_QUOTE + "|summary/" + objectID;
                                Window.open(addSalesInvoice, "_blank", "");
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + objectID, invoice.getInvoiceNumber());
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(quoteSummary);
                    }

                if (hasAccessToChange && isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_QUOTE_CLOSED)) {
                    if ((statusCode.equals(SALE_ORDER)
                            || statusCode.equals(CLIENT_APPROVE)
                            || statusCode.equals(SUBMITTED_TO_MANAGER)
                            || statusCode.equals(OPEN)
                            || statusCode.equals(CONVERTED)
                            || statusCode.equals(OVER_DUE)
                            || statusCode.equals(PICKED)
                            || statusCode.equals(PACKED)
                            || statusCode.equals(SUBMITTED_TO_MANAGER)
                            || statusCode.equals(APPROVE)
                            || statusCode.equals(PARTIAL_SHIPPED)
                            || statusCode.equals(PARTIAL_INVOICED))) {
                        MenuPopItem closedOption = new MenuPopItem(wfmStrings.closed(), "icon-puchase-invoise-small");
                        closedOption.ensureDebugId(SALE_QUOTE + "closed");
                        closedOption.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.confirmation());
                            messageBox.setMessage(wfmStrings.areYouSureYouWanttoCloseThe_() + " " + (property.getSingular(wfmStrings.salesQuote())).toLowerCase() + "?");
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    quoteService.closedOrder(objectID, new AbstractAsyncCallback() {
                                        public void failure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(Object result) {
                                            Info.show(property.getPlural(wfmStrings.salesQuote()) + " " + wfmStrings.closedSuccessfully(), Info.Type.INFO);
                                            listPanel.reloadPage();
                                        }
                                    });
                                }
                            });
                            messageBox.open();
                        });

                        actionItemCount++;
                        menuBar.addItem(closedOption);
                    }
                }

                boolean isDraftOrReject = DRAFT.equals(statusCode) || REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode);
                boolean isEditable = (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT) || invoice.isSubmitter(Utils.getUserID())) &&
                        (isDraftOrReject ||
                        (!isManagerApproval && (isDraftOrReject || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) ||
                        (isManagerApproval && (SUBMITTED_TO_MANAGER.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode))) ||
                        (isManagerApproval && Utils.getUserID().equals(invoice.getCurrentApproverSelectItem().getId()) && isDraftOrReject));

                boolean isEditableForCrm = (Utils.hasPermission(CRM_SALES_QUOTE_EDIT) || invoice.isSubmitter(Utils.getUserID())) &&
                        (isDraftOrReject ||
                        (!isManagerApproval && (isDraftOrReject || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) ||
                        (isManagerApproval && (SUBMITTED_TO_MANAGER.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode))) ||
                        (isManagerApproval && Utils.getUserID().equals(invoice.getCurrentApproverSelectItem().getId()) && isDraftOrReject));

                boolean isFullEditable = Utils.hasPermission(ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS) &&
                        ((!isManagerApproval && (isDraftOrReject || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) ||
                        (isManagerApproval && (SUBMITTED_TO_MANAGER.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode))));

                if (hasAccessToChange && !statusCode.equals(INVOICE_STATUS_CLOSED) && ((Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT) && isEditable) || (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS) && isFullEditable) || (Utils.hasPermission(CRM_SALES_QUOTE_EDIT) && isEditableForCrm)) &&
                        (!invoice.isProgressInvoicing() || (statusCode.equals(OPEN) || statusCode.equals(APPROVE) || statusCode.equals(REJECT) || statusCode.equals(MANAGER_REJECT) || statusCode.equals(SUBMITTED_TO_MANAGER) || statusCode.equals(DRAFT) || (statusCode.equals(CLIENT_APPROVE) && (!invoice.getInvoicedItemsExist())))) && !(statusCode.equals(SHIPPED) || statusCode.equals(PARTIAL_SHIPPED))) {
                    invoice.setAccess(true);
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.ensureDebugId(salesQuote + "edit");
                    edit.setCommand(() -> {
                        if (fp != null && fp.getRelationType() != null && RelationItem.TYPE_LEAD.equals(fp.getRelationType())) {
                            String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_QUOTE + "|edit/" + objectID;
                            Window.open(addSalesInvoice, "_blank", "");
                        } else {
                            if (!isAccountingSection && !Utils.isCRM()) {
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_QUOTE + "|edit/" + objectID;
                                Window.open(addSalesInvoice, "_blank", "");
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|edit/" + objectID, invoice.getInvoiceNumber());
                            }
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (hasAccessToChange && (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_DELETE) || Utils.hasPermission(CRM_SALES_QUOTE_DELETE))) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.ensureDebugId(salesQuote + "delete");
                    delete.setCommand(() -> {
                        if (INVOICED.equals(statusCode) || PARTIAL_INVOICED.equals(statusCode) || CONVERTED.equals(statusCode)) {
                            Info.show(property.getSingular(accountingStrings.youCannotDeleteSalesQuote(), wfmStrings.salesQuote()), Info.Type.WARNING);
                            return;
                        }
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                quoteService.deleteQuote(objectID, SALE_QUOTE, new AbstractAsyncCallback<TestRPC>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(TestRPC result) {
                                        if (MessageCommand.hasShippingData == result.getMessageCommand()) {
                                            Info.show(property.getSingular(accountingStrings.cannotDeleteSalesOrderHasGoodsDeliveredNotes(), wfmStrings.salesQuote()), Info.Type.WARNING);
                                        } else {
                                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.salesQuotes()), Info.Type.INFO);
                                            listPanel.reloadPage();
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALE_QUOTE_DELETED, null, SaleQuoteListView.this);

                                            removeDeletedTab(SALE_QUOTE + objectID);
                                        }

                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }


                PropertyItem propertyItem = Utils.getProperTy(Constants.SALE_QUOTE);
                if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                    MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                    MenuBar convertMenu = new MenuBar(true);
                    convertMenu.setAutoOpen(true);
                    int convertItems = 0;
                    for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                        if (convertItem != null) {
                            convertItems = getConvertItems(invoice, menuBar, convertMenu, convertItems, convertItem);
                        }
                    }

                    if (convertItems > 0) {
                        convertMenuPopItem.setSubMenu(convertMenu);
                        actionItemCount++;
                        menuBar.addItem(convertMenuPopItem);
                    }
                }

                if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_PDF) || Utils.hasPermission(CRM_SALES_QUOTE_PDF)) {
                    MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    generatePdf.ensureDebugId(salesQuote + "generatePdf");
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    generatePdf.setCommand(() -> new PDFTemplateSelector(SALE_QUOTE, invoice.getPdfTemplateID(), new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(htmlPanel, id, objectID);
                        }
                    }));
                    add(htmlPanel);
                    actionItemCount++;
                    menuBar.addItem(generatePdf);
                }

                if (Utils.hasPermission(CONVERT_SALE_QUOTE_TO_SALE_INVOICE) && !statusCode.equals(INVOICE_STATUS_CLOSED) &&
                        hasAccessToChange
                        && (SALE_ORDER.equals(statusCode)
                        || PICKED.equals(statusCode)
                        || PACKED.equals(statusCode)
                        || PARTIAL_SHIPPED.equals(statusCode)
                        || SHIPPED.equals(statusCode)
                        || PARTIAL_INVOICED.equals(statusCode)
                        || CLIENT_APPROVE.equals(statusCode))) {

                    MenuPopItem convertToInvoice = new MenuPopItem(Property.getShortName(Constants.SALE_INVOICE, wfmStrings.convertToo(), accountingStrings.invoice()), "icon-add-category");
                    convertToInvoice.ensureDebugId(salesQuote + "convertToInvoice");
                    convertToInvoice.setCommand(() -> {

                        if (invoice.isProgressInvoicing()) {
                            quoteService.getQuote(invoice.getID(), null, new AbstractAsyncCallback<NewInvoice>() {
                                @Override
                                public void onSuccess(NewInvoice result) {
                                    if (result.getProgressInvoicingType() != null) {
                                        redirectProperly("progressinvoicing|" + result.getProgressInvoicingType() + "/" + result.getID() + "/" + false + "/" + result.getProgressInvoicingType());
                                    } else {
                                        redirectProperly("progressinvoicing|" + AccountingConstants.BY_AMOUNT + "/" + result.getID() + "/" + false);
                                    }
                                }
                            });
                        } else {
                            LoadingPanel.loading(true);
                            quoteService.changeQuoteStatus(objectID, CLIENT_APPROVE, null, false, new AbstractAsyncCallback() {
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Object result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, SaleQuoteListView.this);
                                    redirectProperly("saleinvoice|add/add/convertToInvoice/" + objectID);
                                }
                            });
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(convertToInvoice);
                }

                MenuPopItem copyMenuPopItem = new MenuPopItem(wfmStrings.copy(), "icon-add-green");

                MenuBar copyMenu = new MenuBar(true);
                copyMenu.setAutoOpen(true);

                int copyItems = 0;
                copyItems = getCopyAction(invoice, menuBar, copyMenu, copyItems);

                if (copyItems > 0 && !statusCode.equals(INVOICE_STATUS_CLOSED)) {
                    copyMenuPopItem.setSubMenu(copyMenu);
                    actionItemCount++;
                    menuBar.addItem(copyMenuPopItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, Widget>(wfmStrings.number(), InvoiceList.INVOICE_NUMBER, 120) {
            @Override
            public Widget getCellValue(NewInvoice invoice) {

                SimpleLink label = getLink(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "", null);

                if (invoice.getInvoiceNumber() != null) {
                    final Integer objectID = invoice.getID();

                    label.addClickHandler(clickEvent -> {

                        if (DRAFT.equals(invoice.getStatusCode())) {
                            boolean editPermission = Utils.hasPermission(ACCOUNTING_SALES_QUOTE_EDIT);
                            boolean editFullPermission = Utils.hasPermission(ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS);

                            if ((isAccountingSection || Utils.isAccounting()) && ((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|edit/" + objectID, invoice.getInvoiceNumber());
                            } else if (Utils.isCRM() && Utils.hasPermission(CRM_SALES_QUOTE_EDIT)) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|edit/" + objectID, invoice.getInvoiceNumber());
                            } else {
                                label.removeStyleName("uploadLinkStyle2");
                            }
                        } else {
                            if (!isAccountingSection && !Utils.isAccounting() && !Utils.isCRM()) {
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_QUOTE + "|summary/" + objectID;
                                Window.open(addSalesInvoice, "_blank", "");
                            } else if (Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY))) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + objectID, invoice.getInvoiceNumber(), invoice.getClientName());
                            }
                        }
                    });
                }
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.date(), InvoiceList.INVOICE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getInvoiceDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.validDate(), InvoiceList.DUE_DATE, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getDueDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), InvoiceList.CLIENT, 180) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getClientName();
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.amount(), InvoiceList.DUE_AMOUNT, 130) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getAmount().subtract(invoice.getPaidAmount() != null ? invoice.getPaidAmount() : ZERO));
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.status(), InvoiceList.STATUS, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                if (APPROVE.equals(invoice.getStatusCode())) {
                    return invoice.getCurrentApproverSelectItem() != null ? wfmStrings.approvedbyManager() : wfmStrings.approved();
                } else if (MANAGER_REJECT.equals(invoice.getStatusCode())) {
                    return accountingStrings.rejectedByManager();
                } else if (CLIENT_APPROVE.equals(invoice.getStatusCode())) {
                    return wfmStrings.approvedByClient();
                } else if (REJECT.equals(invoice.getStatusCode())) {
                    return accountingStrings.rejectedByClient();
                } else if (SUBMITTED_TO_MANAGER.equals(invoice.getStatusCode())) {
                    return accountingStrings.waitingForManagerApprove();
                } else if (DRAFT.equals(invoice.getStatusCode())) {
                    return wfmStrings.draft();
                } else if (OPEN.equals(invoice.getStatusCode())) {
                    return wfmStrings.open();
                } else if (CONVERTED.equals(invoice.getStatusCode())) {
                    return wfmStrings.converted();
                } else if (OVER_DUE.equals(invoice.getStatusCode())) {
                    return accountingStrings.overdue();
                } else {
                    return invoice.getStatus();
                }
            }
        };
        ColumnColor sc1 = new ColumnColor(wfmStrings.approvedbyManager(), "r", "2BBF57");
        ColumnColor sc2 = new ColumnColor(reference.INVOICE_STATUS_INVOICED(), "r", "007DE7");
        ColumnColor sc3 = new ColumnColor(accountingStrings.rejectedByClient(), "c", "DC0C0C");
        columnConfig.addColor(sc1);
        columnConfig.addColor(sc2);
        columnConfig.addColor(sc3);
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.manager(), InvoiceList.MANAGER, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return (invoice.getCurrentApproverSelectItem() != null && invoice.getCurrentApproverSelectItem().getName() != null) ? invoice.getCurrentApproverSelectItem().getName() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currency(), InvoiceList.CURRENCY, 60) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(30);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdBy(), InvoiceList.CREATOR, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return (invoice.getCreator() != null && invoice.getCreator().getName() != null) ? invoice.getCreator().getName() : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.PROJECT, wfmStrings.project()), InvoiceList.RELATED_PROJECT, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getRelatedProjectName() != null ? invoice.getRelatedProjectName() : "";
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.reference(), InvoiceList.REFERENCE, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getReference() != null ? invoice.getReference() : "";
            }
        };
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

//        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.poNumber(), InvoiceList.PO_NUMBER, 100) {
//            @Override
//            public String getCellValue(NewInvoice invoice) {
//                return invoice.getPoNumber() != null ? invoice.getPoNumber() : "";
//            }
//        };
//        columnConfig.setShow(false);
//        columnConfig.setMinimumColumnWidth(80);
//        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, Widget>(Property.get(Constants.Opportunities, wfmStrings.opportunity()), InvoiceList.OPPORTUNITY_NUMBER, 100) {
            @Override
            public Widget getCellValue(NewInvoice invoice) {
                final String[] url = {""};
                invoice.getOpportunityNumber();
                SimpleLink label = new SimpleLink(invoice.getOpportunityNumber() != null ? invoice.getOpportunityNumber() : "");

                label.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + invoice.getOpportunityID(), invoice.getOpportunityNumber());
                });
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.subtotal(), InvoiceList.SUB_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getSubtotal() != null ? utils.formatPrice(invoice.getSubtotal()) : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.total() + " (" + AccountingUtils.getBaseCurrencySymbol() + ")", InvoiceList.BASE_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getTotal() != null ? utils.formatPrice(invoice.getTotal()) : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.taxTotal(), InvoiceList.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getTotalTaxes() != null ? utils.formatPrice(invoice.getTotalTaxes()) : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);


        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void redirectProperly(String url) {
        if (Utils.isAccounting()) {
            goTo(url);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }

    private int getCopyAction(NewInvoice invoice, MenuBar menuBar, MenuBar copyMenu, int copyItems) {
        final boolean hasAccessToChange = !lockCompleted || (lockCompleted && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
        final Integer objectID = invoice.getID();

        if (Utils.hasPermission(CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER) && hasAccessToChange) {
            MenuPopItem copyToPO = new MenuPopItem(Property.getShortName(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), "icon-copy");
            copyToPO.ensureDebugId(salesQuote + "copyToPO");
            copyToPO.setCommand(() -> {
                copyToPO.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|add/add/copyFromSalesQuote/" + objectID);
            });
            copyMenu.addItem(copyToPO);
            copyItems++;
        }

        if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_COPY) && hasAccessToChange) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(property.getShort(wfmStrings.salesQuote()));
            copyExistingDataLink.ensureDebugId(salesQuote + "copyExistingDataLink");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|add/add/copyFromExistingData/" + objectID);
            });
            copyMenu.addItem(copyExistingDataLink);
            copyItems++;

        }
        return copyItems;
    }

    public static String getIdsOnly(Set<NewInvoice> selectedItems) {
        StringBuilder ids = null;
        for (NewInvoice item : selectedItems) {
            if (ids == null) {
                ids = new StringBuilder();
                ids.append(item.getID());
            } else {
                ids.append("," + item.getID());
            }
        }
        return ids.toString();
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? SaleQuoteListView.this::addNewSaleQuote : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return !isAccountingSection ? null : new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else
                                data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else
                                data.getCustomData().remove(ENDDATE_NC);
                            RbacService.App.get().getSaleQuoteFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public boolean isShowResetButton() {
                return isAccountingSection || (Utils.isCRM() );
            }

            @Override
            public ActionButton initTopToolBarNew() {
                addNew = null;
                if (hasPermissionToAdd()) {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT) && Utils.hasPermission(PermissionConstants.ACCOUNTING_BASE_INVOICE_ADD)) {
                        ContextMenu contextMenu = getActions();
                        if (contextMenu.getSize() > 0) {
                            addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                            addNew.addClickHandler(clickEvent -> addNew.setMenu(contextMenu.getMenuBar()));
                        }
                    } else {
                        addNew = getAddNewButton();
                        addNew.ensureDebugId(salesQuote + "addNewButton");
                        String shortCut = "Alt + Shift + q";
                        if (Utils.isChrome()) {
                            shortCut = "Alt + q";
                        }
                        addNew.setTitle(shortCut);
                        addNew.addClickHandler(clickEvent -> addNewSaleQuote());
                    }
                }
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {

                if ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_QUOTE_DELETE))
                        || (!isAccountingSection && (Utils.hasPermission(CRM_SALES_QUOTE_DELETE)))) {

                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyQuotes(), wfmStrings.salesQuotes()));
                if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD) : (Utils.hasPermission(CRM_SALES_QUOTE_ADD))) {
                    message.setTextBeforeLink(property.getSingular(accountingStrings.messAddingQuotesClicking(), wfmStrings.salesQuote()));
                    message.setHref(clickEvent -> {
                        if (isAccountingSection || (Utils.isCRM())) {
                            if (fp != null && fp.getClientId() != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("salequote|add/add/fromClientList/" + fp.getClientId());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("salequote|add/add");
                            }
                        } else if (fp != null) {
                            if (fp.getCrmAccountId() != null) {
                                sendSalesQuoteFromCRM();
                            } else if (fp.getCrmContactId() != null && fp.getOpportunityID() == null) {
                                ContactListItem item = new ContactListItem();
                                item.setObjectId(fp.getCrmContactId());
                                CRMService.App.get().addAccountToContact(item, true, new AbstractAsyncCallback<ContactListItem>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(ContactListItem result) {
                                        fp.setCrmAccountId(result.getCrmAccount().getObjectId());
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, fp.getCrmAccountId(), SaleQuoteListView.this);
                                        sendSalesQuoteFromCRM();
                                    }
                                });
                            } else if (fp.getContactID() == null) {
                                createContactAndSalesQuote();

                            }

                        }
                    });
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_EDIT);
            }
        };
    }

    private void addNewSaleQuote() {
        if (isAccountingSection || (Utils.isCRM() )) {
            if (fp != null && fp.getClientId() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("salequote|add/add/fromClientList/" + fp.getClientId());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("salequote|add/add");
            }
        } else if (fp != null) {
            if (fp.getOpportunityID() != null) {
                sendSalesQuoteFromCRMOpportunity();
            } else if (fp.getCrmAccountId() != null) {
                sendSalesQuoteFromCRM();
            } else if (fp.getCrmContactId() != null && fp.getOpportunityID() == null) {
                ContactListItem item = new ContactListItem();
                item.setObjectId(fp.getCrmContactId());
                CRMService.App.get().addAccountToContact(item, true, new AbstractAsyncCallback<ContactListItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(ContactListItem result) {
                        fp.setCrmAccountId(result.getCrmAccount().getObjectId());
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, fp.getCrmAccountId(), SaleQuoteListView.this);
                        sendSalesQuoteFromCRM();
                    }
                });
            } else if (fp.getCrmContactId() == null)
                createContactAndSalesQuote();
        }
    }

    private ContextMenu getActions() {
        ContextMenu contextMenu = new ContextMenu();

        if (hasPermissionToAdd()) {
            contextMenu.addMenuItem(property.getSingular(wfmStrings.salesQuote()), true, () -> addNewSaleQuote());
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT) && Utils.hasPermission(PermissionConstants.ACCOUNTING_BASE_INVOICE_ADD)) {
            contextMenu.addMenuItem(accountingMessages.saleBaseInvoice(Property.get(SALE_QUOTE, wfmStrings.salesQuote())), true, () -> {
                if (isAccountingSection) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("saleorderBaseInvoice|add/add/" + SaleOrderBaseInvoiceItem.SALE_QUOTE);
                } else {
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#saleorderBaseInvoice|add/add/" + SaleOrderBaseInvoiceItem.SALE_QUOTE;
                    Window.open(addSalesInvoice, "_blank", "");
                }
            });
        }

        return contextMenu;
    }

    private void sendSalesQuoteFromCRMOpportunity() {
        if (fp.getCrmAccountId() != null && fp.getCrmContactId() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + fp.getOpportunityID());

//            String addSalesQuote = GWT.getHostPageBaseURL() + "Accounting.html#" + Constants.SALE_QUOTE + "|add/add/opportunity/" + fp.getOpportunityID();
//            Window.open(addSalesQuote, "_blank", "");
        } else {
            createContactAndSalesQuote();
        }
    }

    private void createContactAndSalesQuote() {
        LoadingPanel.loading(true);
        crmService.addAccountOrContactToOpportunity(fp.getOpportunityID(), true, new AbstractAsyncCallback<OpportunityListItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(OpportunityListItem result) {
                LoadingPanel.loading(false);
                String addSalesQuote = GWT.getHostPageBaseURL() + "Accounting.html#" + Constants.SALE_QUOTE + "|add/add/opportunity/" + fp.getOpportunityID();
                Window.open(addSalesQuote, "_blank", "");
            }
        });

    }

    private void sendSalesQuoteFromCRM() {
        if (fp.getOpportunityID() != null) {
            if (fp.getCrmContactId() != null) {
                String addSalesQuote = GWT.getHostPageBaseURL() + "Accounting.html#" + Constants.SALE_QUOTE + "|add/add/opportunity/" + fp.getOpportunityID();
                Window.open(addSalesQuote, "_blank", "");
            } else {
                createContactAndSalesQuote();
            }
            return;
        } else if (fp.getCrmContactId() != null) {
            boolean isLead = fp.getRelationType() != null && RelationItem.TYPE_LEAD.equals(fp.getRelationType());
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/contact/" + fp.getCrmAccountId() + "/" + fp.getCrmContactId() + (isLead ? "/lead" : ""));
        }
        LoadingPanel.loading(true);
        ListingFilterParameter fp_ = new ListingFilterParameter();
        fp_.setAllByFilter(true);
        fp_.setAsSelectItem(true);
        final Integer crmAccountID = fp.getCrmAccountId();
        fp_.setAccountID(crmAccountID);
        ContactService.App.get().getContactList(fp_, new ListLoadConfig(), new AbstractAsyncCallback<ContactList>() {
            public void failure(Throwable ex) {
                LoadingPanel.loading(false);
            }

            public void success(final ContactList contacts) {
                String addSalesInvoice;
                LoadingPanel.loading(false);
                final SelectItem[] contactList = ContactListItem.asSelectItems(contacts.getContactListItems());
                final WfmMessageBox message = new WfmMessageBox(IconEnum.CONFIRM, Action.OkCancel, true);
                if (contactList == null || contactList.length < 1) {
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(Property.get(Constants.Contacts, wfmStrings.chooseContactForAccount(), wfmStrings.contact().toLowerCase()));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onSubmit() {
                            SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add/" + crmAccountID);
                        }
                    });
                    message.open();
                    //Popup. Please add contact for this account
                } else if (contactList.length == 1) {
                    if (!"".equals(contactList[0].getDescription())) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/contact/" + crmAccountID + "/" + contactList[0].getId());
                    } else {
                        message.setTitle(wfmStrings.confirmationMessage());
                        message.setMessage(wfmStrings.chooseEmail());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onSubmit() {
                                SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + contactList[0].getId());
                            }
                        });
                        message.open();
                    }
                } else {
                    //Show dropdown and give user chance to choose from contact list
                    shell = new KpiModal();
                    shell.ensureDebugId(salesQuote);

                    contactName.setItems(contactList);
                    contactName.ensureDebugId(salesQuote);
                    contactName.setAllowFirstItem(true);

                    Heading contactLabel = new Heading(HeadingSize.H5);
                    contactLabel.setStyleName("custom-form-item__dt file--SaleQuoteListView");
                    contactLabel.add(new Span(Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact())));
                    Div contactDiv = new Div("custom-form-item__dd");
                    contactDiv.add(contactName);

                    Div content = new Div();
                    content.add(contactLabel);
                    content.add(contactDiv);

                    shell.add(content);
                    shell.setWidth("355px");
                    shell.setTitle(Property.get(Constants.Contacts, wfmStrings.chooseContactForAccount(), wfmStrings.contact()));

                    okBut = new WfmButton2(property.getSingular(wfmStrings.sendSalesQuote(), wfmStrings.contact()), WfmButton2.BTN_PRIMARY);
                    okBut.ensureDebugId(salesQuote);

                    cancelBut = new WfmButton2(wfmStrings.cancel());
                    cancelBut.ensureDebugId(salesQuote);

                    okBut.addClickHandler(clickEvent -> {
                        if (contactName.getSelectedItem() != null && contactName.getSelectedItem().getId() != 0) {
                            if (!"".equals(contactName.getSelectedItem().getDescription())) {
                                //                     sendSalesQuote.setEnabled(true);
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/contact/" + crmAccountID + "/" + contactName.getSelectedItem().getId(), "_blank", "");

                            } else {
                                message.setTitle(wfmStrings.confirmationMessage());
                                message.setMessage(wfmStrings.chooseEmail());
                                message.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onCancel() {

                                    }

                                    @Override
                                    public void onSubmit() {
                                        SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + contactName.getSelectedItem().getId());
                                    }
                                });
                                message.open();
                            }
                        } else {
                            Info.show(Property.get(Constants.Contacts, wfmStrings.chooseContactForAccount(), wfmStrings.contact()), Info.Type.WARNING);
                        }
                    });
                    cancelBut.addClickHandler(clickEvent -> shell.close());
                    shell.addButton(cancelBut);
                    shell.addButton(okBut);
                    shell.open();
                }
            }
        });

    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectId) {
        String pdfURL = null;
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        pdfURL = CommandConstants.PDF_URL + "/savedSaleQuoteViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(5, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.SaleQuoteFacetFilter.getContentCode()[0], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return !Utils.hasRole(CLIENT);
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.SaleQuoteFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[1], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return !Utils.hasRole(CLIENT);
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.SaleQuoteFacetFilter.getContentCode()[3], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.isProjectInLineItemEnable() ? SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID : SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.isProjectInLineItemEnable() ? SolrSaleInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME : SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.SaleQuoteFacetFilter.getContentCode()[4], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.SaleQuoteFacetFilter.getContentCode()[5], accountingStrings.shipping(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_SHPPINGMETHOD_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, wfmStrings.startDate());
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, accountingStrings.validDate());
        return contentConfigure;
    }

    private ListingRequestProvider getListProvider() {
        return (filterParametrs, listingCallback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            if (filterParametrs.getFacetFilter() != null) {
                filterParametrs.setStartDate(filterParametrs.getFacetFilter().getStartDate());
                filterParametrs.setEndDate(filterParametrs.getFacetFilter().getEndDate());
            }
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);

            initSaleQuoteList(filterParametrs, listingCallback, null);
        };
    }

    private void initSaleQuoteList(ListingFilterParameter filterParametrs, ListingCallback listingCallback, Span container) {
        if (fp != null) {
            if (fp.getOpportunityID() != null) {
                filterParametrs.setOpportunityID(fp.getOpportunityID());
                if (fp.isConvertedLead() && fp.getConvertedLeadId() != null) {
                    filterParametrs.setConvertedLead(fp.isConvertedLead());
                    filterParametrs.setConvertedLeadId(fp.getConvertedLeadId());
                }
            } else {
                if (fp.getAccountID() != null) {
                    filterParametrs.setAccountID(fp.getAccountID());
                }
                if (fp.getLeadID() != null) {
                    filterParametrs.setLeadID(fp.getLeadID());
                }
                if (fp.getContactID() != null) {
                    filterParametrs.setContactID(fp.getContactID());
                }
            }
            if (fp.getClientId() != null) {
                filterParametrs.setClientId(fp.getClientId());
            }
            if (fp.getRelationID() != null && fp.getRelationType() != null) {
                filterParametrs.setRelationID(fp.getRelationID());
                filterParametrs.setRelationType(fp.getRelationType());
            }
        }
        if (productId != null) {
            filterParametrs.setProductId(productId);
        }
        filterParametrs.setFromListing(true);
        if (RelationItem.TYPE_PRODUCT_CATEGORY.equals(this.relationType)) {
            quoteService.getSaleQuoteByCategoryId(productId, new AbstractAsyncCallback<InvoiceList>() {
                @Override
                public void failure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void success(InvoiceList invoiceList) {
                    if (listingCallback != null) {
                        listingCallback.onSuccess(invoiceList);
                    }
                    statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                    if (statisticShortcut != null) {
                        if (invoiceList.getTotal() != null && invoiceList.getTotal() > 0) {
                            statisticShortcut.setText(countFormat(invoiceList.getTotal()));
                            statisticShortcut.setClass("tab-label");
                        } else {
                            statisticShortcut.setText("");
                            statisticShortcut.removeStyleName("tab-label");
                        }
                    }
                    Utils.triggerCustomJSEvent(CustomJsEvents.CHANGE_TO_CUSTOMER);
                }
            });
        } else {
            quoteService.getSaleQuoteData(filterParametrs, new AbstractAsyncCallback<InvoiceList>() {
                @Override
                public void failure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void success(InvoiceList invoiceList) {
                    if (listingCallback != null) {
                        listingCallback.onSuccess(invoiceList);
                    }
                    statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                    if (statisticShortcut != null) {
                        if (invoiceList.getTotal() != null && invoiceList.getTotal() > 0) {
                            statisticShortcut.setText(countFormat(invoiceList.getTotal()));
                            statisticShortcut.setClass("tab-label");
                        } else {
                            statisticShortcut.setText("");
                            statisticShortcut.removeStyleName("tab-label");
                        }
                    }
                    Utils.triggerCustomJSEvent(CustomJsEvents.CHANGE_TO_CUSTOMER);
                }
            });
        }
    }

    @Override
    public String getIconStyle() {
        return "salQuoLits sales-quote-list";
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(accountingStrings.quote()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        ArrayList<String> dontDeleteStatus = validateIfItsOkToDelete(selectedItems);
        if (dontDeleteStatus != null && dontDeleteStatus.size() == selectedItems.size()) {
            Info.show(property.getSingular(accountingStrings.youCannotDeleteSalesQuote(), wfmStrings.salesQuote()), Info.Type.WARNING);
        } else {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            NewInvoice item = (NewInvoice) selectedItems.iterator().next();
            String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
            messageBox.setMessage(message);
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    ArrayList<Integer> ids = getIDsOnly(selectedItems);
                    if (ids.size() > 0) {
                        LoadingPanel.loading(true);
                        quoteService.deleteSelectedQuotes(ids, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Void result) {
                                listPanel.reloadPage();
                                LoadingPanel.loading(false);
                                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.salesQuote()), Info.Type.INFO);

                                for (Integer id : ids) {
                                    removeDeletedTab(SALE_QUOTE + id);
                                }
                            }
                        });
                    }
                }
            });
            messageBox.open();
        }
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<NewInvoice> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            ids.add(item.getID());
        }
        return ids;
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.salesQuote()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(SALE_QUOTE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/savedSaleQuoteViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    public static ArrayList<String> validateIfItsOkToDelete(Set<NewInvoice> selectedItems) {
        ArrayList<String> statuses = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            if (INVOICED.equals(item.getStatusCode()) || PARTIAL_INVOICED.equals(item.getStatusCode()) || CONVERTED.equals(item.getStatusCode()) || SHIPPED.equals(item.getStatusCode()) || PARTIAL_SHIPPED.equals(item.getStatusCode()))
                statuses.add(item.getStatusCode());
        }
        return statuses;
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initSaleQuoteList(fp, null, container);
    }

    public String getPropertyCode() {
        return Constants.SALE_QUOTE;
    }

    private int getConvertItems(NewInvoice rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_CASE.equals(convertItem.getCode()) && Utils.hasPermission(ADD_NEW_CASE)) {
            final MenuPopItem convertToCase = new MenuPopItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), "icon-send-sales-invoice");
            convertToCase.setCommand(() -> {
                convertToCase.closeAll(menuBar);
                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/CONVERT/" + RelationItem.TYPE_SALEQUOTE + "/" + rowValue.getID());
                } else {
                    Utils.openURL("Crm.html#case|add/add/CONVERT/" + RelationItem.TYPE_SALEQUOTE + "/" + rowValue.getID());
                }
            });
            convertToCase.ensureDebugId("convert_case");
            convertMenu.addItem(convertToCase);
            convertItems++;
        } else if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_SALEQUOTE + "/" + rowValue.getID());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }
        return convertItems;
    }
}
