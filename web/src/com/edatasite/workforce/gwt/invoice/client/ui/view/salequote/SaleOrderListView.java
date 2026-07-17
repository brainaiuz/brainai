package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 19, 2010
 * Time: 5:54:48 PM
 * To change this template use File | Settings | File Templates.
 */

public class SaleOrderListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();
    protected HashSet selectedItems = new HashSet();
    private ListingFilterParameter fp;
    private boolean isAccountingSection = true;
    private final String salesOrder = "salesOrder";
    private KpiModal shell;
    private final DataListBox contactName = new DataListBox();
    private WfmButton2 okBut;
    private WfmButton2 cancelBut;
    private ListingPanel listPanel;
    private final ActionButton deleteBtn = null;
    private ActionButton addNew;
    private boolean removeAddNew = false;
    private boolean isBlocked = false;
    private final boolean isAlmadarSerials = Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
    private Integer productId;

    public SaleOrderListView() {
        super(SALE_ORDER_CODE);
        setDescription(property.getPlural(wfmStrings.salesOrders()));
        setPlusIcon();
    }

    public SaleOrderListView(Integer productId) {
        super(SALE_ORDER_CODE);
        this.productId = productId;
        setDescription(property.getPlural(wfmStrings.salesOrders()));
        setPlusIcon();
    }

    public SaleOrderListView(ListingFilterParameter filterParameters, boolean isAccountingSection) {
        //bu constructorga ClientViewSinksContainer classidan kelinadi.
        super(SALE_ORDER_CODE);
        setDescription(property.getPlural(wfmStrings.salesOrders()));
        setPlusIcon();

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

    private void setPlusIcon() {
        if ((hasPermissionToCreateQuote() && hasPermissionForBaseInvoice()) || hasPermissionToCreateQuote()) {
            setAddNew("saleorder|add/add");
        } else if (hasPermissionForBaseInvoice()) {
            setAddNew("saleorderBaseInvoice|add/add");
        }
    }

    public SaleOrderListView(ListingFilterParameter filterParameters, boolean isAccountingSection, boolean isBlocked) {
        //bu constructorga ClientViewSinksContainer classidan kelinadi.
        super(SALE_ORDER_CODE);
        setDescription(property.getPlural(accountingStrings.salesOrder()));
        setPlusIcon();
        this.fp = filterParameters;
        this.isAccountingSection = isAccountingSection;
        this.isBlocked = isBlocked;

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

    public static ArrayList<Integer> getIDsOnly(HashSet<NewInvoice> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            ids.add(item.getID());
        }
        return ids;
    }

    public static ArrayList<String> validateIfItsOkToDelete(Set<NewInvoice> selectedItems) {
        ArrayList<String> statuses = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            if (INVOICED.equals(item.getStatusCode()) || PARTIAL_INVOICED.equals(item.getStatusCode()) || CONVERTED.equals(item.getStatusCode()))
                statuses.add(item.getStatusCode());
        }
        return statuses;
    }

    @Override
    protected Widget onInitialize() {
        if (!Utils.hasRole(CLIENT)) {
            listPanel = new GuideListingPanel(ListPanelType.SaleOrderListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST_CUSTOMIZE), true, Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST_FILTER));
        } else {
            listPanel = new GuideListingPanel(ListPanelType.SaleOrderListPanel, getColumnConfig(), getListProvider(), getListDesign(), Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST_CUSTOMIZE), false, Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST_FILTER));
        }

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveSalesQuoteCellValue((NewInvoice) rowValue, columnCodeName));

        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/saleOrderListPDFHandler";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null
                    ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate())
                    : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null
                    ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate())
                    : null);
            listPanel.callListPDF(pdfURL, filterParametrs);
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSaleOrdersListExcel";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null
                    ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate())
                    : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null
                    ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate())
                    : null);
            listPanel.callListExcel(excelURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALESORDER_ADDED, SaleOrderListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEORDER_SHIPPED, SaleOrderListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_ADDED, SaleOrderListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEORDER_APPROVAL, SaleOrderListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    listPanel.reloadPage();
                }
            };
            t.schedule(3500);
        });

        listPanel.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (deleteBtn != null) {
                    deleteBtn.setVisible(true);
                }
            } else {
                if (deleteBtn != null) {
                    deleteBtn.setVisible(false);
                }
            }
        });

        add(listPanel);
        return null;
    }

    private void saveSalesQuoteCellValue(NewInvoice rowValue, String columnCodeName) {
        if (((Utils.hasPermission(ACCOUNTING_SALES_ORDER_EDIT) && Utils.isCustomField(columnCodeName))) || rowValue.hasAccess()) {
            quoteService.saveSaleQuoteEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Boolean result) {
                    super.success(result);
                    listPanel.reloadPage();
                }
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
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                if ((!isAccountingSection || Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY)) && !DRAFT.equals(statusCode)) {
                    MenuPopItem quoteSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    quoteSummary.ensureDebugId(SALE_ORDER + "orderSummary");
                    quoteSummary.setCommand(() -> {
                        if (!isAccountingSection) {
                            String addSalesOrder = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_ORDER_CODE + "|summary/" + objectID;
                            Window.open(addSalesOrder, "_blank", "");
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + objectID, invoice.getInvoiceNumber());
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(quoteSummary);
                }

                if (hasAccessToChange && isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_ORDER_PICKLIST)) {
                    if ((statusCode.equals(SALE_ORDER)
                            || statusCode.equals(PICKED)
                            || statusCode.equals(PACKED)
                            || statusCode.equals(PARTIAL_SHIPPED)
                            || statusCode.equals(SHIPPED)
                            || statusCode.equals(INVOICED)) && invoice.getPickListID() != null) {
                        MenuPopItem pickList = new MenuPopItem(wfmStrings.picklist(), "icon-puchase-invoise-small");
                        pickList.ensureDebugId(SALE_ORDER + "pickList");
                        pickList.setCommand(() -> {
                            if (isAlmadarSerials) {
                                quoteService.checkForCreditLimit(invoice.getPickListID(), new AbstractAsyncCallback<SaveResult>() {
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(SaveResult result) {
                                        if (result.getExceededCreditLimit()) {
                                            WfmMessageBox creditLimitExceedMessage = new WfmMessageBox(IconEnum.WARN, Action.OK);
                                            String message = accountingMessages.creditLimitPicklistMessage(result.getMessage(), AccountingUtils.get().formatPrice(result.getCreditLimit()), AccountingUtils.get().formatPrice(result.getRemainingBalance()));
                                            creditLimitExceedMessage.setMessage(message);
                                            creditLimitExceedMessage.open();
                                        } else {
                                            SinksContainerFactory.entryPoint.onHistoryChanged("picklist|edit/" + invoice.getPickListID(), "Picklist: " + invoice.getInvoiceNumber());
                                        }
                                    }
                                });
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("picklist|edit/" + invoice.getPickListID(), "Picklist: " + invoice.getInvoiceNumber());
                            }
                        });

                        actionItemCount++;
                        menuBar.addItem(pickList);
                    }
                }

                PropertyItem propertyItem = Utils.getProperTy(Constants.SALE_ORDER_CODE);
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

                if (hasAccessToChange && isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_ORDER_CLOSED)) {
                    if ((statusCode.equals(SALE_ORDER)
                            || statusCode.equals(PICKED)
                            || statusCode.equals(PACKED)
                            || statusCode.equals(SUBMITTED_TO_MANAGER)
                            || statusCode.equals(APPROVE)
                            || statusCode.equals(PARTIAL_SHIPPED)
                            || statusCode.equals(PARTIAL_INVOICED))) {
                        MenuPopItem closedOption = new MenuPopItem(wfmStrings.closed(), "icon-puchase-invoise-small");
                        closedOption.ensureDebugId(SALE_ORDER + "closed");
                        closedOption.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.confirmation());
                            messageBox.setMessage(wfmStrings.areYouSureYouWanttoCloseThe_() + " " + (property.getSingular(accountingStrings.salesOrder())).toLowerCase() + "?");
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    quoteService.closedOrder(objectID, new AbstractAsyncCallback() {
                                        public void failure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(Object result) {
                                            Info.show(property.getPlural(accountingStrings.salesOrder()) + " " + wfmStrings.closedSuccessfully(), Info.Type.INFO);
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


                boolean isEditable = (SUBMITTED_TO_MANAGER.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SALE_ORDER.equals(statusCode) || PARTIAL_INVOICED.equals(statusCode) || PICKED.equals(statusCode) || PACKED.equals(statusCode) || SHIPPED.equals(statusCode) || PARTIAL_SHIPPED.equals(statusCode) || INVOICED.equals(statusCode) || CONVERTED.equals(statusCode));
                boolean isFullEditable = SUBMITTED_TO_MANAGER.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SALE_ORDER.equals(statusCode) || PICKED.equals(statusCode) || PACKED.equals(statusCode) || SHIPPED.equals(statusCode) || PARTIAL_SHIPPED.equals(statusCode) || INVOICED.equals(statusCode) || CONVERTED.equals(statusCode);
                boolean hasEditPermission = ((Utils.hasPermission(ACCOUNTING_SALES_ORDER_EDIT) && isEditable) || (Utils.hasPermission(ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS) && isFullEditable));
                boolean hasInvoice = INVOICED.equals(statusCode) || PARTIAL_INVOICED.equals(statusCode) || CONVERTED.equals(statusCode);
                if (hasAccessToChange && hasEditPermission && ((invoice.isProgressInvoicing() && !invoice.getInvoicedItemsExist()) || !invoice.isProgressInvoicing()) || DRAFT.equals(statusCode)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.ensureDebugId(SALE_ORDER + "edit");
                    if (hasInvoice) {
                        if (Utils.hasGenericAccess(GenericSettingsEnum.CAN_EDIT_SALES_ORDER_IF_HAS_INVOICE_WORKAROUND)) {
                            edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|edit/" + objectID, invoice.getInvoiceNumber()));
                        }else {
                            edit.setCommand(() -> Info.show("Please delete the invoice to edit the sales order.", Info.Type.INFO));
                        }
                    } else {
                        edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|edit/" + objectID, invoice.getInvoiceNumber()));
                    }
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_SALES_ORDER_DELETE) /*&& DRAFT.equals(statusCode)*/) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.ensureDebugId(SALE_ORDER + "delete");
                    delete.setCommand(() -> {

                        if (INVOICED.equals(statusCode) || PARTIAL_INVOICED.equals(statusCode) || CONVERTED.equals(statusCode)) {
                            Info.show(property.getSingular(accountingStrings.youCannotDeleteSalesOrder(), accountingStrings.salesOrder()), Info.Type.WARNING);
                            return;
                        }

                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                quoteService.deleteQuote(objectID, SALE_QUOTE, new AbstractAsyncCallback<TestRPC>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(TestRPC result) {
                                        LoadingPanel.loading(false);
                                        if (MessageCommand.hasShippingData == result.getMessageCommand()) {
                                            Info.show(property.getSingular(accountingStrings.cannotDeleteSalesOrderHasGoodsDeliveredNotes(), accountingStrings.salesOrder()), Info.Type.WARNING);
                                            return;
                                        }
                                        Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.salesOrder()), Info.Type.INFO);
                                        listPanel.reloadPage();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, null, SaleOrderListView.this);

                                        removeDeletedTab(SALE_ORDER_CODE + objectID);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_PDF) || Utils.hasPermission(CRM_SALES_ORDER_PDF)) {
                    MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                    generatePdf.ensureDebugId(SALE_ORDER + "generatePdf");
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    generatePdf.setCommand(() -> new PDFTemplateSelector(SALE_ORDER, invoice.getPdfTemplateID(), new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(htmlPanel, id, objectID);
                        }
                    }));
                    add(htmlPanel);
                    actionItemCount++;
                    menuBar.addItem(generatePdf);
                }


                MenuPopItem copyMenuPopItem = new MenuPopItem(wfmStrings.copy(), "icon-add-green");
                MenuBar copyMenu = new MenuBar(true);
                copyMenu.setAutoOpen(true);
                int copyItems = 0;

                copyItems = getCopyAction(invoice, menuBar, copyMenu, copyItems);

                if (copyItems > 0) {
                    copyMenuPopItem.setSubMenu(copyMenu);
                    actionItemCount++;
                    menuBar.addItem(copyMenuPopItem);
                }


                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
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

                Label label = new Label(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "");

                if (invoice.getInvoiceNumber() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    final Integer objectID = invoice.getID();

                    if (DRAFT.equals(invoice.getStatusCode())) {
                        boolean editPermission = Utils.hasPermission(ACCOUNTING_SALES_ORDER_EDIT);
                        boolean editFullPermission = Utils.hasPermission(ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS);

                        if (isAccountingSection && ((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)) {
                            label.addClickHandler(clickEvent -> {
                                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|edit/" + objectID, invoice.getInvoiceNumber());
                            });
                        } else {
                            label.removeStyleName("uploadLinkStyle2");
                        }
                    } else {
                        label.addClickHandler(clickEvent -> {
                            SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + objectID, invoice.getInvoiceNumber());
                        });
                    }
                }
                return label;
            }
        };
        columnConfig.addStyleAttribute("padding-left", "5px");
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), InvoiceList.CLIENT, 210) {
            @Override
            public SimpleLink getCellValue(NewInvoice invoice) {
                SimpleLink label = new SimpleLink(invoice.getClientName() != null ? invoice.getClientName() : "");
                if (invoice.getClientID() != null && Utils.hasPermission(ACCOUNTING_CUSTOMER_SUMMARY)) {
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + invoice.getClientID());
                    });
                }
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.addStyleAttribute("padding-left", "5px");
        columnConfig.setMinimumColumnWidth(160);
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

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.validDate(), InvoiceList.DUE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getDueDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.amount(), InvoiceList.DUE_AMOUNT, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return AccountingUtils.get().formatPrice(invoice.getAmount().subtract(invoice.getPaidAmount() != null
                        ? invoice.getPaidAmount()
                        : ZERO));
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("padding-right", "5px");
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.status(), InvoiceList.STATUS, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                if (APPROVE.equals(invoice.getStatusCode())) {
                    return wfmStrings.approved();
                } else if (SUBMITTED_TO_MANAGER.equals(invoice.getStatusCode())) {
                    return wfmStrings.submitted();
                } else if (SALE_ORDER.equals(invoice.getStatusCode())) {
                    return accountingStrings.salesOrder();
                } else if (INVOICED.equals(invoice.getStatusCode())) {
                    return accountingStrings.invoiced();
                } else if (SHIPPED.equals(invoice.getStatusCode())) {
                    return accountingStrings.shipped();
                } else if (DRAFT.equals(invoice.getStatusCode())) {
                    return wfmStrings.draft();
                } else if (OPEN.equals(invoice.getStatusCode())) {
                    return wfmStrings.open();
                } else if (OVER_DUE.equals(invoice.getStatusCode())) {
                    return accountingStrings.overdue();
                } else if (PAID.equals(invoice.getStatusCode())) {
                    return wfmStrings.paid();
                } else {
                    return invoice.getStatus();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);


        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currency(), InvoiceList.CURRENCY, 60) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(30);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdBy(), InvoiceList.CREATOR, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCreatorName() != null ? invoice.getCreatorName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

//        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.poNumber(), InvoiceList.PO_NUMBER, 80) {
//            @Override
//            public String getCellValue(NewInvoice inovoice) {
//                return inovoice.getPoNumber();
//            }
//        };
//        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
//        columnConfig.addStyleAttribute("padding-right", "5px");
//        columnConfig.setMinimumColumnWidth(50);
//        columnConfig.setShow(false);
//        columns.add(columnConfig);

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

        columnConfig = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.PROJECT, wfmStrings.project()), InvoiceList.RELATED_PROJECT, 120) {
            @Override
            public SimpleLink getCellValue(NewInvoice invoice) {
                SimpleLink label = new SimpleLink(invoice.getRelatedProjectName() != null ? invoice.getRelatedProjectName() : "");

                label.addClickHandler(clickEvent -> {
                    Utils.openURL("ProjectManagement.html#project|summary/"+ invoice.getRelatedProjectID());
                });
                return label;
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
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
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

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.netAmount(), InvoiceList.NET_AMOUNT_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getNetAmountTotal() != null ? utils.formatPrice(invoice.getNetAmountTotal()) : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.remainingBalance(), InvoiceList.REMAINING_BALANCE, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                String remainingBalance = wfmStrings.notAvailable();
                BigDecimal invociedAmount = invoice.getInvoicedAmount() != null ? invoice.getInvoicedAmount() : ZERO;
                if (invoice.getTotalInInvoiceCurrency() != null) {
                    remainingBalance = AccountingUtils.get().formatPrice(invoice.getTotalInInvoiceCurrency().subtract(invociedAmount));
                }
                return remainingBalance;
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("padding-right", "5px");
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.Opportunities, wfmStrings.opportunity()), InvoiceList.OPPORTUNITY, 100) {
            @Override
            public SimpleLink getCellValue(NewInvoice invoice) {
                SimpleLink label = new SimpleLink(invoice.getOpportunity() != null ? invoice.getOpportunity() : "");

                label.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + invoice.getOpportunityID(), invoice.getOpportunity());
                });
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        return columns.toArray(new ColumnDefinitionConfig[]{});
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

    private boolean hasPermissionForBaseInvoice() {
        return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT) && Utils.hasPermission(PermissionConstants.ACCOUNTING_BASE_INVOICE_ADD);
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (hasPermissionToCreateQuote() || hasPermissionForBaseInvoice()) {
                    return SaleOrderListView.this::createNewOrder;
                }
                return null;
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
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            RbacService.App.get().getSaleOrderFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                return isAccountingSection;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                addNew = null;

                if (hasPermissionToCreateQuote() || hasPermissionForBaseInvoice()) {
                    if (hasPermissionForBaseInvoice()) {
                        ContextMenu contextMenu = getActions();
                        if (contextMenu.getSize() > 0) {
                            addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                            addNew.addClickHandler(clickEvent -> addNew.setMenu(contextMenu.getMenuBar()));
                        }
                    } else {
                        addNew = getAddNewButton();
                        addNew.ensureDebugId(SALE_ORDER + "addNewButton");
                        addNew.addClickHandler(clickEvent -> {
                            createNewOrder();
                        });
                    }
                }
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_ORDER_DELETE)) ||
                        (!isAccountingSection &&
                                (Utils.hasRole(DR) ||
                                        Utils.hasRole(ADMIN) ||
                                        Utils.hasRole(ACCOUNTANT) ||
                                        Utils.hasRole(PM) ||
                                        Utils.hasRole(SALESPERSON) ||
                                        Utils.hasRole(SALESMAN)))) {
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
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.noSalesOrderMessage(), accountingStrings.salesOrder()));
                if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)/*!Utils.hasRole(CLIENT)*/) {
                    //message.setHref("saleorder|add/add");
                    message.setTextBeforeLink(property.getSingular(accountingStrings.noSalesOrderBeforeLinkMessage(), accountingStrings.salesOrder()));
                    message.setHref(event -> {
                        createNewOrder();
                    });
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            public void initDataEmptyTable(HTML emptyTable) {
                emptyTable.setText("");
                emptyTable.setStyleName("drawColumns");
                emptyTable.addClickHandler(clickEvent -> {
                    //To change body of implemented methods use File | Settings | File Templates.
                });
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_EDIT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(accountingStrings.salesOrder()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(SALE_ORDER, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(selectedItems), id);
                    String pdfURL = CommandConstants.PDF_URL + "/savedSaleOrderViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                }
            });
        }
    }

    private boolean hasPermissionToCreateQuote() {
        return !isBlocked &&
                ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD))
                        || (!isAccountingSection && Utils.hasPermission(CRM_SALES_ORDER_ADD)));
    }

    private void createNewOrder() {
        if (isAccountingSection) {
            if (fp != null && fp.getClientId() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|add/add/fromClientList/" + fp.getClientId());
            } else if ((hasPermissionToCreateQuote() && hasPermissionForBaseInvoice()) || hasPermissionToCreateQuote()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|add/add");
            } else if (hasPermissionForBaseInvoice()) {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleorderBaseInvoice|add/add");
            }
        } else if (fp != null && fp.getOpportunityID() != null) {
            sendSalesOrderFromCRMOpportunity();
        } else if (fp != null && fp.getCrmAccountId() != null) {
            sendSalesQuoteFromCRM();
        }
    }

    private ContextMenu getActions() {
        ContextMenu contextMenu = new ContextMenu();

        if (hasPermissionToCreateQuote()) {
            contextMenu.addMenuItem(property.getSingular(accountingStrings.salesOrder()), true, () -> createNewOrder());
        }

        if (hasPermissionForBaseInvoice()) {
            contextMenu.addMenuItem(accountingMessages.saleOrderBaseInvoice(), true, () -> {
                if (isAccountingSection) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("saleorderBaseInvoice|add/add/" + SaleOrderBaseInvoiceItem.SALE_ORDER);
                } else {
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#saleorderBaseInvoice|add/add/" + SaleOrderBaseInvoiceItem.SALE_ORDER;
                    Window.open(addSalesInvoice, "_blank", "");
                }
            });
        }

        return contextMenu;
    }

    private void sendSalesOrderFromCRMOpportunity() {
        if (fp.getCrmAccountId() != null && fp.getCrmContactId() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + fp.getOpportunityID());
        } else {
            createContactAndSalesOrder();
        }
    }

    private void sendSalesQuoteFromCRM() {
        if (fp != null) {
            if (fp.getOpportunityID() != null) {
                if (fp.getCrmContactId() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + fp.getOpportunityID());
                } else {
                    createContactAndSalesOrder();
                }
                return;
            } else if (fp.getCrmContactId() != null) {
                boolean isLead = fp.getRelationType() != null && RelationItem.TYPE_LEAD.equals(fp.getRelationType());
                String addSalesOrder = Constants.SALE_ORDER_CODE + "|add/add/contact/" + fp.getCrmAccountId() + "/" + fp.getCrmContactId() + (isLead ? "/lead" : "");
                SinksContainerFactory.entryPoint.onHistoryChanged(addSalesOrder);
                return;
            }
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
                String addSalesOrder;
                LoadingPanel.loading(false);
                final SelectItem[] contactList = ContactListItem.asSelectItems(contacts.getContactListItems());
                final WfmMessageBox message = new WfmMessageBox(IconEnum.CONFIRM, Action.OkCancel, true);
                if (contactList == null || contactList.length < 1) {
                    message.setTitle(wfmStrings.confirmationMessage());
                    message.setMessage(Property.get(Constants.Contacts, wfmStrings.chooseContactForAccount(), wfmStrings.contact()));
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                            //             sendSalesInvoice.setEnabled(true);
                        }

                        @Override
                        public void onSubmit() {
                            //             sendSalesInvoice.setEnabled(true);
                            SinksContainerFactory.entryPoint.onHistoryChanged("contact|add/add/" + crmAccountID);
                        }
                    });
                    message.open();
                    //Popup. Please add contact for this account
                } else if (contactList.length == 1) {
                    if (!"".equals(contactList[0].getDescription())) {
                        //          sendSalesQuote.setEnabled(true);
                        addSalesOrder = Constants.SALE_ORDER_CODE + "|add/add/contact/" + crmAccountID + "/" + contactList[0].getId();
                        SinksContainerFactory.entryPoint.onHistoryChanged(addSalesOrder);
                    } else {
                        message.setTitle(wfmStrings.confirmationMessage());
                        message.setMessage(wfmStrings.chooseEmail());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                //                 sendSalesInvoice.setEnabled(true);
                            }

                            @Override
                            public void onSubmit() {
                                //                  sendSalesInvoice.setEnabled(true);
                                SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + contactList[0].getId());
                            }
                        });
                        message.open();
                    }
                } else {
                    //Show dropdown and give user chance to choose from contact list
                    shell = new KpiModal();
                    shell.ensureDebugId(salesOrder);

                    contactName.setItems(contactList);
                    contactName.ensureDebugId(salesOrder);
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

                    okBut = new WfmButton2(property.getSingular(wfmStrings.sendSalesOrder(), accountingStrings.salesOrder()), WfmButton2.BTN_PRIMARY);
                    okBut.ensureDebugId(salesOrder);

                    cancelBut = new WfmButton2(wfmStrings.cancel());
                    cancelBut.ensureDebugId(salesOrder);

                    okBut.addClickHandler(clickEvent -> {
                        if (contactName.getSelectedItem() != null && contactName.getSelectedItem().getId() != 0) {
                            if (!"".equals(contactName.getSelectedItem().getDescription())) {
                                //                     sendSalesQuote.setEnabled(true);
                                String addSalesOrder1 = Constants.SALE_ORDER_CODE + "|add/add/contact/" + crmAccountID + "/" + contactName.getSelectedItem().getId();
                                SinksContainerFactory.entryPoint.onHistoryChanged(addSalesOrder1);
                                shell.close();
                            } else {
                                message.setTitle(wfmStrings.confirmationMessage());
                                message.setMessage(wfmStrings.chooseEmail());
                                message.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onCancel() {
                                        //                             sendSalesInvoice.setEnabled(true);
                                    }

                                    @Override
                                    public void onSubmit() {
                                        //                             sendSalesInvoice.setEnabled(true);
                                        SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact/" + contactName.getSelectedItem().getId());
                                    }
                                });
                                message.open();
                            }
                        } else {
                            Info.show(Property.get(Constants.Contacts, wfmStrings.chooseContactForAccount(), wfmStrings.contact()), Info.Type.WARNING);
                        }
                    });
                    cancelBut.addClickHandler(clickEvent -> {
                        //                sendSalesQuote.setEnabled(true);
                        shell.close();
                    });
                    shell.addButton(cancelBut);
                    shell.addButton(okBut);
                    shell.open();
                }
            }
        });

    }

    private void createContactAndSalesOrder() {
        LoadingPanel.loading(true);
        CRMService.App.get().addAccountOrContactToOpportunity(fp.getOpportunityID(), true, new AbstractAsyncCallback<OpportunityListItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(OpportunityListItem result) {
                LoadingPanel.loading(false);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + fp.getOpportunityID());
            }
        });

    }

    private int getCopyAction(NewInvoice invoice, MenuBar menuBar, MenuBar copyMenu, int copyItems) {
        final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
        String statusCode = invoice.getStatusCode();
        final Integer objectID = invoice.getID();

        if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_SALES_ORDER_COPYTOPO) && !statusCode.equals(INVOICE_STATUS_CLOSED)) {
            MenuPopItem copyToPO = new MenuPopItem(Property.getShortName(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), "icon-copy");
            copyToPO.ensureDebugId(SALE_ORDER + "copyToPO");
            copyToPO.setCommand(() -> {
                copyToPO.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|add/add/copyFromSalesOrder/" + objectID);
            });
            copyMenu.addItem(copyToPO);
            copyItems++;
        }

        if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_SALES_ORDER_COPYTOSQ) && !statusCode.equals(INVOICE_STATUS_CLOSED)) {
            MenuPopItem copySODataLink = new MenuPopItem(Property.getShortName(Constants.SALE_QUOTE, wfmStrings.salesQuote()), "icon-copy");
            copySODataLink.ensureDebugId(SALE_ORDER + "copyToQuoteink");
            copySODataLink.setCommand(() -> {
                copySODataLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|add/add/copyFromSOData/" + objectID);
            });
            copyMenu.addItem(copySODataLink);
            copyItems++;
        }

        if (hasAccessToChange && Utils.hasPermission(ACCOUNTING_SALES_ORDER_COPYTOSO) && !statusCode.equals(INVOICE_STATUS_CLOSED)/*!Utils.hasRole(CLIENT)*/) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(property.getShort(accountingStrings.salesOrder()), "icon-copy");
            copyExistingDataLink.ensureDebugId(SALE_ORDER + "copyExistingDataLink");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|add/add/copyFromExistingData/" + objectID);
            });
            copyMenu.addItem(copyExistingDataLink);
            copyItems++;
        }

        return copyItems;
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectId) {
        String pdfURL = null;
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        pdfURL = CommandConstants.PDF_URL + "/savedSaleOrderViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[0], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
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
        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }
        });
//        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[2], wfmStrings.amount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });

        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[3], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
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

        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[4], wfmStrings.currency(), new FacetFieldConfigure() {
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

        contentConfigure.addContentConfigure(FacetContentType.SaleOrderFacetFilter.getContentCode()[5], accountingStrings.shipping(), new FacetFieldConfigure() {
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
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null
                    ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate())
                    : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null
                    ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate())
                    : null);

            initSaleOrderList(filterParametrs, listingCallback, null);
        };
    }

    private void initSaleOrderList(ListingFilterParameter filterParametrs, ListingCallback listingCallback, Span container) {
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
        if (this.fp != null && RelationItem.TYPE_PRODUCT_CATEGORY.equals(this.fp.getRelationType())) {
            quoteService.getSaleOrderDataByCategoryId(this.fp.getCategoryID(), new AbstractAsyncCallback<InvoiceList>() {
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
                }
            });
        } else {
            quoteService.getSaleOrderData(filterParametrs, new AbstractAsyncCallback<InvoiceList>() {
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
            Info.show(accountingMessages.pleaseSelectOneRow(property.getShort(wfmStrings.so(), wfmStrings.order())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        ArrayList<String> dontDeleteStatus = validateIfItsOkToDelete(selectedItems);
        if (dontDeleteStatus != null && dontDeleteStatus.size() == selectedItems.size()) {
            Info.show(property.getSingular(accountingStrings.youCannotDeleteSalesOrder(), accountingStrings.salesOrder()), Info.Type.WARNING);
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
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_CONVERTED_TO_SALEORDER, null, SaleOrderListView.this);
                                LoadingPanel.loading(false);
                                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.saleorder()), Info.Type.INFO);
                                for (Integer id : ids) {
                                    removeDeletedTab(SALE_ORDER_CODE + id);
                                }
                            }
                        });
                    }
                }
            });
            messageBox.open();
        }
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
        initSaleOrderList(fp, null, container);
    }

    public String getPropertyCode() {
        return Constants.SALE_ORDER_CODE;
    }

    private int getConvertItems(NewInvoice rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_CASE.equals(convertItem.getCode()) && Utils.hasPermission(ADD_NEW_CASE)) {
            final MenuPopItem convertToCase = new MenuPopItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), "icon-send-sales-invoice");
            convertToCase.setCommand(() -> {
                convertToCase.closeAll(menuBar);
                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/CONVERT/" + RelationItem.TYPE_SALEORDER + "/" + rowValue.getID());
                } else {
                    Utils.openURL("Crm.html#case|add/add/CONVERT/" + RelationItem.TYPE_SALEORDER + "/" + rowValue.getID());
                }
            });
            convertToCase.ensureDebugId("convert_case");
            convertMenu.addItem(convertToCase);
            convertItems++;
        } else if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_SALEORDER + "/" + rowValue.getID());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }
        return convertItems;
    }
}
