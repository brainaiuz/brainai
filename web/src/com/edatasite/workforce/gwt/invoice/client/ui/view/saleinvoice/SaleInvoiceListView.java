package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
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
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote.AllocateCreditNoteView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.InvoicePaymentView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Jun-2011
 * Time: 13:53:16
 */
public class SaleInvoiceListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final Reference reference = Reference.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private ListingPanel listPanel;
    private Integer crmAccountID;
    private Integer warehouseID;
    private Integer productID;
    private boolean isAccountingSection = true;
    private ListingFilterParameter fp;
    private ActionButton customImportAction;
    private boolean isBlocked = false;
    protected HashSet selectedItems = new HashSet();
    private final ActionButton delete = null;

    public SaleInvoiceListView() {
        super(SALE_INVOICE);
        setDescription(property.getPlural(wfmStrings.saleInvoices()));
        if (hasPermissionToCreateInvoice()) {
            setAddNew("saleinvoice|add/add");
        }
    }

    public SaleInvoiceListView(ListingFilterParameter fP, boolean isAccountingSection) {
        this((Integer) null, isAccountingSection);
        this.fp = fP;

        if (fp != null) {
            this.crmAccountID = fp.getCrmAccountId();
            this.productID = fp.getProductId();
            this.warehouseID = fp.getWarehouseID();
        }
        if (!isAccountingSection && fp != null && crmAccountID == null && fp.getCrmContactId() != null) {
            InvoiceService.App.get().findIDsBy(fp, new AbstractAsyncCallback<HashMap<String, Integer>>() {
                public void failure(Throwable caught) {

                }

                public void success(HashMap<String, Integer> ids) {
                    if (ids != null && ids.size() > 0) {
                        for (Map.Entry<String, Integer> entry : ids.entrySet()) {
                            if (CrmConstants.CRM_CONTACT.equals(entry.getKey())) {
                                fp.setCrmContactId(entry.getValue());
                            } else if (CrmConstants.CRM_ACCOUNT.equals(entry.getKey())) {
                                fp.setAccountID(entry.getValue());
                                crmAccountID = fp.getCrmAccountId();
                            }
                        }
                    }
                }
            });
        }
    }

    public SaleInvoiceListView(ListingFilterParameter fP, boolean isAccountingSection, boolean isBlocked) {
        this((Integer) null, isAccountingSection);
        this.fp = fP;
        this.isBlocked = isBlocked;
        if (fp != null) {
            this.crmAccountID = fp.getCrmAccountId();
            this.productID = fp.getProductId();
        }
        if (!isAccountingSection && fp != null && crmAccountID == null && fp.getCrmContactId() != null) {
            InvoiceService.App.get().findIDsBy(fp, new AbstractAsyncCallback<HashMap<String, Integer>>() {
                public void failure(Throwable caught) {

                }

                public void success(HashMap<String, Integer> ids) {
                    if (ids != null && ids.size() > 0) {
                        for (Map.Entry<String, Integer> entry : ids.entrySet()) {
                            if (CrmConstants.CRM_CONTACT.equals(entry.getKey())) {
                                fp.setCrmContactId(entry.getValue());
                            } else if (CrmConstants.CRM_ACCOUNT.equals(entry.getKey())) {
                                fp.setAccountID(entry.getValue());
                                crmAccountID = fp.getCrmAccountId();
                            }
                        }
                    }
                }
            });
        }
    }

    public SaleInvoiceListView(Integer crmAccountID, boolean isAccountingSection) {
        super(SALE_INVOICE);
        setDescription(property.getPlural(wfmStrings.saleInvoices()));
        this.crmAccountID = crmAccountID;
        this.isAccountingSection = isAccountingSection;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<NewInvoice> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            ids.add(item.getID());
        }
        return ids;
    }

    private boolean hasPermissionToCreateInvoice() {
        return !isBlocked && ((Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD))
                || (!Utils.isAccounting() && (Utils.hasPermission(CRM_SALES_INVOICE_ADD))));
    }

    public static HashSet<NewInvoice> validateAndRemoveCreditNoteFromList(HashSet<NewInvoice> selectedItems, boolean isPrint) {
        for (NewInvoice item : selectedItems) {
            if (item.isCreditNote() || (isPrint && item.isProjectBasedInvoice())) {
                selectedItems.remove(item);
            }
        }
        return selectedItems;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.SaleInvoiceListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_SALES_INVOICE_LIST_CUSTOMIZE), !Utils.hasRole(Constants.CLIENT), Utils.hasPermission(ACCOUNTING_SALES_INVOICE_LIST_FILTER));
        listPanel.setCustomFieldsEditCellSaveChanges((CellChange<NewInvoice>) (rowValue, columnCodeName) -> saveInvoiceEditCellValue(rowValue, columnCodeName));
        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = "pdf/saleInvoceListPDFHandler";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListPDF(pdfURL, filterParametrs);
        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSaleInvoicesListExcel";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListExcel(excelURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, SaleInvoiceListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, SaleInvoiceListView.this, (sender, args) -> {
            String status = (String) args;
            if (InvoiceSummaryView.ADD_SALEINVOICE.equals(status) || InvoicePaymentView.DELETE_SALEINVOICE.equals(status)) {
                listPanel.reloadPage();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, SaleInvoiceListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    listPanel.reloadPage();
                }
            };
            t.schedule(3500);
            if (args != null)
                Info.show(args.toString(), Info.Type.INFO);
        });

        if (Utils.isVatRegistered() && Utils.isSaudiCompany()) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALE_INVOICE_SENDED_TO_ZATCA, SaleInvoiceListView.this, (sender, args) -> listPanel.reloadPage());
        }
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
                return hasPermissionToCreateInvoice() ? SaleInvoiceListView.this::newSaleInvoice : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                if (productID != null) {
                    return null;
                }
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
                            RbacService.App.get().getSaleInvoiceFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                if (isAccountingSection && productID == null) {
                    ContextMenu contextMenu = getActions();
                    ActionButton more = null;
                    if (contextMenu.getSize() > 0) {
                        more = getAddNewButton(ActionButton.Type.TOOLMENU);
                        final ActionButton finalMore = more;
                        more.addClickHandler(clickEvent -> finalMore.setMenu(contextMenu.getMenuBar()));
                    }
                    return more;
                } else {
                    return getNewButton();
                }
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_DELETE) : (Utils.hasPermission(CRM_SALES_INVOICE_DELETE))) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.noSalesInvoicesMessage(), wfmStrings.salesInvoice()));
                if (!Utils.hasRole(CLIENT)) {
//                    message.setHref("saleinvoice|add/add");
                    message.setTextBeforeLink(property.getPlural(accountingStrings.noSalesInvoicesBeforeLinkMessage(), wfmStrings.saleInvoices()));
                    message.setHref(clickEvent -> {
                        if (isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD)) {
                            if (fp != null && fp.getClientId() != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add/fromClientList/" + fp.getClientId());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add");
                            }
                        } else if (crmAccountID != null && Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
                            sendSaleInvoiceFromCRM();
                        }
                    });
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.salesInvoice()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(SALE_INVOICE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    Set<NewInvoice> items = validateAndRemoveCreditNoteFromList(selectedItems, true);
                    if (items.isEmpty()) {
                        Info.show(accountingStrings.youShouldSelectOnlySalesInvoice(), Info.Type.WARNING);
                    } else {
                        final HTMLPanel htmlPanel = new HTMLPanel("");
                        add(htmlPanel);
                        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(items), id);
                        String pdfURL = CommandConstants.PDF_URL + "/savedSaleInvoceViewPDFHandler";
                        HashMap<String, String> parametrs = requestObject.getRequestParams();
                        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                    }
                }
            });
        }
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(accountingStrings.invoice()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        HashSet<NewInvoice> items = selectedItems;
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(items);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    invoiceService.deleteSelectedInvoices(ids, SALE_INVOICE, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            listPanel.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.salesInvoice()), Info.Type.INFO);

                            for (Integer id : ids) {
                                removeDeletedTab(SALE_INVOICE + id);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private ContextMenu getActions() {
        ContextMenu contextMenu = new ContextMenu();

        if (!isBlocked && ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD))
                || (!isAccountingSection && (Utils.hasPermission(CRM_SALES_INVOICE_ADD))))) {
            contextMenu.addMenuItem(property.getSingular(wfmStrings.salesInvoice()), true, () -> newSaleInvoice());
        }

        if (Utils.hasPermission(PM_MAIN_MENU) && Utils.hasPermission(ACCOUNTING_TIMESHEET_INVOICE_ADD)) {
            contextMenu.addMenuItem(accountingStrings.timesheetInvoice(), true, () -> {
                if (isAccountingSection) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("projectBaseInvoice|add/add");
                } else {
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + "projectBaseInvoice" + "|add/add/" + fp.getCrmAccountId();
                    Window.open(addSalesInvoice, "_blank", "");
                }
            });
        }

        if (Utils.hasPermission(ACCOUNTING_RECURRING_INVOICE_ADD)) {
            contextMenu.addMenuItem(Property.get(Constants.RECURRING_INVOICE, accountingStrings.recurringInvoice()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringinvoice|add/add"));
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_ADD)) {
            contextMenu.addMenuItem(accountingStrings.creditNote(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|add/add"));
        }
        if (Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT)) {
            contextMenu.addMenuItem(accountingStrings.receivePayment(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + RECEIVABLE));
        }
        if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_ADD)) {
            contextMenu.addMenuItem(Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addPrepayment()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|add/add/"));
        }

        return contextMenu;
    }

    private ActionButton getNewButton() {
        ActionButton addNew = null;

        if (!isBlocked && ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD))
                || (!isAccountingSection && (Utils.hasPermission(CRM_SALES_INVOICE_ADD))))) {
            addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
            addNew.ensureDebugId(SALE_INVOICE + "addNew");
            String shortCut = "Alt + Shift + i";

            if (Utils.isChrome()) {
                shortCut = "Alt + i";
            }
            addNew.setTitle(shortCut);

            addNew.addClickHandler(ch -> {
                newSaleInvoice();
            });
        }
        return addNew;
    }

    private void newSaleInvoice() {
        if (isAccountingSection) {
            if (fp != null && fp.getClientId() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add/contact/" + crmAccountID);
                //new ta
//                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + Constants.SALE_INVOICE + "|add/add/contact/" + crmAccountID;
//                Window.open(addSalesInvoice, "_blank", "");
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|add/add");
            }
        } else if (crmAccountID != null) {
            sendSaleInvoiceFromCRM();
        } else if (fp.getOpportunityID() != null) {
            sendSaleInvoiceFromCRM();
        } else if (fp != null && fp.getCrmContactId() != null) {
            ContactListItem item = new ContactListItem();
            item.setObjectId(fp.getCrmContactId());
            CRMService.App.get().addAccountToContact(item, true, new AbstractAsyncCallback<ContactListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ContactListItem result) {
                    crmAccountID = result.getCrmAccount().getObjectId();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, crmAccountID, SaleInvoiceListView.this);
                    sendSaleInvoiceFromCRM();
                }
            });
        }
    }

    private MenuPopItem createNimbleImportMenu() {
        final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.NIMBLE_COMMERCE, null);
        imp.setSubmitCompleted(() -> {
            if (imp.getObjectId() != null) {
                goTo("importnimblecommerce|add/add/" + imp.getObjectId());
            }
        });
        MenuPopItem importNimbleCsvItem = new MenuPopItem(accountingStrings.nimbleData());
        importNimbleCsvItem.setCommand(() -> imp.open());
        return importNimbleCsvItem;
    }

    private MenuPopItem createCustomInvoicesImportMenu() {
        final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.CUSTOM_INVOICE, null);
        imp.setSubmitCompleted(() -> {
            if (imp.getObjectId() != null) {
                goTo("importcustominvoice|add/add/" + imp.getObjectId());
            }
        });
        MenuPopItem importNimbleCsvItem = new MenuPopItem(wfmStrings.invoices());
        importNimbleCsvItem.setCommand(() -> imp.open());
        return importNimbleCsvItem;
    }

    private void sendSaleInvoiceFromCRM() {
        if (fp != null && fp.getOpportunityID() != null) {
            String addSalesInvoice = Constants.SALE_INVOICE + "|add/add/opportunity/" + fp.getOpportunityID();
            SinksContainerFactory.entryPoint.onHistoryChanged(addSalesInvoice);
        } else if (fp != null && fp.getCrmContactId() != null) {
            boolean isLead = fp.getRelationType() != null && RelationItem.TYPE_LEAD.equals(fp.getRelationType());
            String addSalesInvoice = Constants.SALE_INVOICE + "|add/add/contact/" + crmAccountID + "/" + fp.getContactID() + (isLead ? "/lead" : "");
            SinksContainerFactory.entryPoint.onHistoryChanged(addSalesInvoice);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/contact/" + crmAccountID);
        }
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(5, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[0], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
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

        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[1], wfmStrings.status(), new FacetFieldConfigure() {
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

        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[4], wfmStrings.currency(), new FacetFieldConfigure() {
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
        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[5], accountingStrings.shipping(), new FacetFieldConfigure() {
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

//        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[7], accountingStrings.originalAmount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrSaleInvoiceRepresenter.FIELD_TOTAL_INVOICE_CURRENCY;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[9], wfmStrings.inTarget(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrSaleInvoiceRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrSaleInvoiceRepresenter.FIELD_IN_TARGET;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        contentConfigure.addContentConfigure(FacetContentType.SaleInvoiceFacetFilter.getContentCode()[10], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_IS_CREDITNODE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_IS_CREDITNODE;
            }

            @Override
            public boolean isWithID() {
                return false;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, wfmStrings.invoiceDate());
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, wfmStrings.dueDate());
        contentConfigure.addContentConfigureDateListBox(SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, wfmStrings.createdDate());

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

            initSaleInvoiceList(filterParametrs, listingCallback, null);
        };
    }

    private void initSaleInvoiceList(ListingFilterParameter filterParametrs, ListingCallback listingCallback, Span container) {
        if (fp != null) {
            if (filterParametrs.getCrmContactId() == null) {
                filterParametrs.setCrmContactId(fp.getCrmContactId());
            }
            if (fp.getRelationID() != null && fp.getRelationType() != null) {
                filterParametrs.setRelationID(fp.getRelationID());
                filterParametrs.setRelationType(fp.getRelationType());
            }
            if (fp.getOpportunityID() != null) {
                filterParametrs.setOpportunityID(fp.getOpportunityID());
                filterParametrs.setCrmContactId(null);
            }
        }
        filterParametrs.setClientId(crmAccountID);
        filterParametrs.setProductId(productID);
        filterParametrs.setWarehouseID(warehouseID);

        if (this.fp != null && RelationItem.TYPE_PRODUCT_CATEGORY.equals(this.fp.getRelationType())) {
            invoiceService.getSaleInvoiceDataByCategoryId(this.fp, new AbstractAsyncCallback<InvoiceList>() {
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
                        if (customImportAction != null && (invoiceList.isNimbleCommerceEnabled() || invoiceList.isCustomInvoiceImportEnabled())) {
                            MenuBar importMenu = new MenuBar(true);
                            if (invoiceList.isNimbleCommerceEnabled()) {
                                importMenu.addItem(createNimbleImportMenu());
                            }
                            if (invoiceList.isCustomInvoiceImportEnabled()) {
                                importMenu.addItem(createCustomInvoicesImportMenu());
                            }
                            customImportAction.setMenu(importMenu);
                            customImportAction.setVisible(true);
                        }
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
            invoiceService.getSaleInvoiceData(filterParametrs, new AbstractAsyncCallback<InvoiceList>() {
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
                        if (customImportAction != null && (invoiceList.isNimbleCommerceEnabled() || invoiceList.isCustomInvoiceImportEnabled())) {
                            MenuBar importMenu = new MenuBar(true);
                            if (invoiceList.isNimbleCommerceEnabled()) {
                                importMenu.addItem(createNimbleImportMenu());
                            }
                            if (invoiceList.isCustomInvoiceImportEnabled()) {
                                importMenu.addItem(createCustomInvoicesImportMenu());
                            }
                            customImportAction.setMenu(importMenu);
                            customImportAction.setVisible(true);
                        }
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

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        List<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        CustomColumnDefinitionConfig column = new ColumnDefinitionConfig<NewInvoice, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 100) {
            @Override
            public Anchor getCellValue(final NewInvoice invoice) {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
                boolean hasAccountingBeforeBlockDate = (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(invoice.getInvoiceDate().getNonConvertedDate()));

                String statusCode = invoice.getStatusCode();
                final Integer objectID = invoice.getID();
                final boolean isCreditNote = invoice.isCreditNote();
                final boolean isProjectBasedInvoice = invoice.isProjectBasedInvoice();

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (!DRAFT.equals(statusCode))
                    if ((isAccountingSection && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY)) || (!isAccountingSection && (Utils.hasPermission(CRM_SALES_INVOICE_SUMMARY)))) {
                        MenuPopItem invoiceSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                        invoiceSummary.setCommand(() -> {
                            if (!isAccountingSection) {
                                String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + SALE_INVOICE + "|summary/" + objectID;
                                Window.open(addSalesInvoice, "_blank", "");
                            } else {
                                if (isCreditNote) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|summary/" + objectID, invoice.getInvoiceNumber());
                                } else {
                                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + objectID + ((invoice.getInvoiceCustomType() != null && !invoice.getInvoiceCustomType().isEmpty()) ? ("/" + invoice.getInvoiceCustomType()) : ""), invoice.getInvoiceNumber());
                                }
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(invoiceSummary);
                    }

                boolean editPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_EDIT) : Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_EDIT);
                boolean editFullPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS) : Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS);
                boolean canEdit = !(PAID.equals(invoice.getStatusCode()) || OVER_DUE.equals(invoice.getStatusCode()) || APPROVE.equals(invoice.getStatusCode()))
                        || (invoice.getCurrentApproverSelectItem() == null || invoice.getCurrentApproverSelectItem() != null && invoice.getCurrentApproverSelectItem().getId() == null)
                        || (invoice.getCurrentApproverSelectItem() != null && invoice.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()));

                if (APPROVE.equals(invoice.getStatusCode()) && invoice.getCurrentApproverSelectItem() != null) {
                    canEdit = invoice.getCurrentApproverSelectItem().getId().equals(Utils.getUserID());
                }

                if (!PENDING.equals(statusCode)) {

                    if (isAccountingSection && hasAccessToChange && canEdit && !invoice.isZatcaReported()) {
                        if (((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)) {
                            if ((PAID.equals(statusCode) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT)) || DRAFT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode) || OVER_DUE.equals(statusCode) || OPEN.equals(statusCode)) {
                                if (!hasAccountingBeforeBlockDate) {
                                    invoice.setAccess(true);
                                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                                    edit.setCommand(() -> {
                                        if (isCreditNote) {
                                            SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|edit/" + objectID, invoice.getInvoiceNumber());
                                        } else {
                                            SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|edit/" + objectID + (isProjectBasedInvoice ? "/projectbased" : "") + ((invoice.getInvoiceCustomType() != null && !invoice.getInvoiceCustomType().isEmpty()) ? ("/" + invoice.getInvoiceCustomType()) : ""), invoice.getInvoiceNumber());
                                        }
                                    });
                                    actionItemCount++;
                                    menuBar.addItem(edit);
                                }
                            }
                        }

                        if (isAccountingSection ? (invoice.isSubmitter(Utils.getUserID()) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_DELETE)) || Utils.hasPermission(ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS) : (Utils.hasPermission(CRM_SALES_INVOICE_DELETE)) && !invoice.isZatcaReported()) {
                            if (!hasAccountingBeforeBlockDate) {
                                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                                delete.setCommand(() -> {
                                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                    messageBox.setTitle(wfmStrings.warning());
                                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                                    messageBox.addCloseHandler(new CloseHandler() {
                                        @Override
                                        public void onSubmit() {
                                            if (isCreditNote) {
                                                validateAndDeleteInvoice(objectID);
                                            } else {
                                                deleteInvoice(objectID, isCreditNote);
                                            }
                                        }
                                    });
                                    messageBox.open();
                                });
                                actionItemCount++;
                                menuBar.addItem(delete);
                            }
                        }

                        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
                            final MenuPopItem sendToTarget = new MenuPopItem(accountingStrings.sendToTarget(), "icon-sales-quote-small");
                            sendToTarget.setEnabled(!invoice.isInTarget());
                            sendToTarget.setCommand(() -> {
                                sendToTarget.setEnabled(false);
                                TargetErpService.App.get().sendInvoiceToTarget(invoice.getID(), new AsyncCallback<String>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        sendToTarget.setEnabled(true);
                                    }

                                    @Override
                                    public void onSuccess(String s) {
                                        if (s != null && !s.startsWith("OK")) {
                                            sendToTarget.setEnabled(true);
                                            Info.show(s, Info.Type.WARNING);
                                        } else {
                                            Info.show(s.replace("OK:", ""), Info.Type.INFO);
                                        }
                                    }
                                });
                            });
                            actionItemCount++;
                            menuBar.addItem(sendToTarget);
                        }

                        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_VOID)) {
                            if (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode)) {
                                MenuPopItem voidInvoice = null;
                                String language = Utils.userSettings.get(LANGUAGE_FOR_USER);
                                if (language.equals("ru")) {
                                    voidInvoice = new MenuPopItem(accountingStrings.voide(), "icon-remove-storefront");
                                } else {
                                    voidInvoice = new MenuPopItem(accountingStrings.voide() + " " + (isCreditNote ? accountingStrings.creditNote() : accountingStrings.invoice()), "icon-remove-storefront");

                                }
                                voidInvoice.setCommand(() -> {
                                    final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                                    confirmBox.setTitle(wfmStrings.confirmation());
                                    confirmBox.setMessage(accountingStrings.areYouSureYouWantToVoidThe() + " " + (isCreditNote ? accountingStrings.creditNote() : accountingStrings.invoice()) + " ?");
                                    confirmBox.addCloseHandler(new CloseHandler() {
                                        @Override
                                        public void onSubmit() {
                                            final KpiModal dialogBox = new KpiModal();
                                            dialogBox.setCloseButton(true);
                                            dialogBox.setWidth(400);
                                            final DatePicker datePicker = new DatePicker(invoice.getInvoiceDate().getNonConvertedDate());
                                            dialogBox.setTitle(wfmStrings.selectVoidDate());
                                            datePicker.setWidth("180px");
                                            datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                                            dialogBox.add(datePicker);
                                            final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide());
                                            dialogBox.addButton(voidButton);
                                            voidButton.addClickHandler(clickEvent -> {
                                                if (AccountingUtils.validateVoidDate(datePicker.getDate(), invoice.getInvoiceDate().getNonConvertedDate())) {
                                                    voidButton.setEnabled(false);
                                                    if (isCreditNote) {
                                                        validateAndVoidInvoice(objectID, datePicker.getDate(), dialogBox);
                                                    } else {
                                                        voidInvoice(objectID, datePicker.getDate(), isCreditNote, invoice.isProgressInvoicing(), dialogBox);
                                                    }
                                                }
                                            });
                                            dialogBox.open();
                                        }
                                    });
                                    confirmBox.open();
                                });
                                actionItemCount++;
                                menuBar.addItem(voidInvoice);
                            }
                        }


                        if (!Utils.hasRole(CLIENT)) {
                            if (isCreditNote && (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) {
                                MenuPopItem allocateCreditNote = new MenuPopItem(wfmStrings.allocateCredit());
                                allocateCreditNote.setCommand(() -> {
                                    Command provider = () -> listPanel.reloadPage();
                                    new AllocateCreditNoteView(objectID, invoice.getInvoiceNumber(),
                                            invoice.getTotalInInvoiceCurrency().subtract(invoice.getPaidAmount() != null ? invoice.getPaidAmount() : ZERO), provider);
                                });
                                actionItemCount++;
                                menuBar.addItem(allocateCreditNote);
                            }
                        }
                    }

                    if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_PDF) || Utils.hasPermission(CRM_SALES_INVOICE_PDF)) {
                        MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                        final HTMLPanel htmlPanel = new HTMLPanel("");
                        generatePdf.setCommand(() -> new PDFTemplateSelector((invoice.isProjectBasedInvoice() ? PROJECT_BASED_INVOICE : invoice.isCreditNote() ? RECEIVABLE_CREDIT_NOTE : SALE_INVOICE), invoice.getPdfTemplateID(), new ExtendedCommand() {
                            @Override
                            public void execute(Integer id) {
                                generatePDF(htmlPanel, id, objectID, invoice.isCreditNote(), invoice.isProjectBasedInvoice());
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
                }
                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(100);
        column.setMinimumColumnWidth(100);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, Widget>(wfmStrings.number(), InvoiceList.INVOICE_NUMBER, 100) {
            @Override
            public Widget getCellValue(final NewInvoice invoice) {
                Label label = new Label(invoice.getInvoiceNumber() != null ? invoice.getInvoiceNumber() : "");
                if (invoice.getInvoiceNumber() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    final Integer objectID = invoice.getID();

                    final boolean isCreditNote = invoice.isCreditNote();
                    label.addClickHandler(clickEvent -> {

                        if (DRAFT.equals(invoice.getStatusCode())) {
                            boolean editPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_EDIT) : Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_EDIT);
                            boolean editFullPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS) : Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS);

                            if (isAccountingSection && ((invoice.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)) {

                                if (isCreditNote) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|edit/" + objectID, invoice.getInvoiceNumber());
                                } else {
                                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|edit/" + objectID + (invoice.isProjectBasedInvoice() ? "/projectbased" : "") + ((invoice.getInvoiceCustomType() != null && !invoice.getInvoiceCustomType().isEmpty()) ? ("/" + invoice.getInvoiceCustomType()) : ""), invoice.getInvoiceNumber());
                                }
                            } else {
                                label.removeStyleName("uploadLinkStyle2");
                            }
                        } else {

                            if (!isAccountingSection && (Utils.hasPermission(CRM_SALES_INVOICE_SUMMARY))) {
                                String saleInvoiceSummary = SALE_INVOICE + "|summary/" + objectID;
                                SinksContainerFactory.entryPoint.onHistoryChanged(saleInvoiceSummary, invoice.getInvoiceNumber());
                            } else {
                                if (isCreditNote) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|summary/" + objectID, invoice.getInvoiceNumber());
                                } else {
                                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + objectID + ((invoice.getInvoiceCustomType() != null && !invoice.getInvoiceCustomType().isEmpty()) ? ("/" + invoice.getInvoiceCustomType()) : ""), invoice.getInvoiceNumber());
                                }
                            }
                        }
                    });
                }
                return label;
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), InvoiceList.CLIENT, 180) {
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
        column.setColumnSortable(true);
        column.setMinimumColumnWidth(150);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.vatNumber()), InvoiceList.CLIENT_VAT_NUMBER, 180) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                String vatNumber = !Utils.isNullOrEmpty(invoice.getClientTrnNumber()) ? invoice.getClientTrnNumber() : invoice.getClientVatNumber();
                return vatNumber;
            }
        };
        column.setMinimumColumnWidth(150);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.invoiceDate(), InvoiceList.INVOICE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getInvoiceDate());
            }
        };
        column.setMinimumColumnWidth(100);
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.dueDate(), InvoiceList.DUE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getDueDate());
            }
        };
        column.setMinimumColumnWidth(100);
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.amount(), InvoiceList.ORIGINAL_AMOUNT, 120) {
            @Override
            public String getCellValue(NewInvoice inovoice) {
                return getAmountAsString(inovoice.getAmount(), inovoice.isCreditNote());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.paidAmount(), InvoiceList.PAID_AMOUNT, 100) {
            @Override
            public String getCellValue(NewInvoice inovoice) {
                return getAmountAsString(inovoice.getPaidAmount(), inovoice.isCreditNote());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            column = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.zatcaStatus(), InvoiceList.ZATCA_STATUS, 100) {
                @Override
                public String getCellValue(NewInvoice inovoice) {
                    return Utils.isNullOrEmpty(inovoice.getZatcaStatus()) ? "N/A" : inovoice.getZatcaStatus();
                }
            };
            column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            column.setMinimumColumnWidth(80);
            columns.add(column);
        }

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.dueAmount(), InvoiceList.DUE_AMOUNT, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return getAmountAsString(invoice.getDueAmount(), invoice.isCreditNote());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.taxTotal(), InvoiceList.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                BigDecimal taxTotal = invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().multiply(invoice.getExchageRate() != null ? invoice.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO;
                return getAmountAsString(taxTotal.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP), false);
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(150);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.status(), InvoiceList.STATUS, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                if (APPROVE.equals(invoice.getStatusCode())) {
                    return wfmStrings.approved();
                } else if (SUBMITTED_TO_MANAGER.equals(invoice.getStatusCode())) {
                    return wfmStrings.submitted();
                } else if (OVER_DUE.equals(invoice.getStatusCode())) {
                    return accountingStrings.overdue();
                } else if (DRAFT.equals(invoice.getStatusCode())) {
                    return wfmStrings.draft();
                } else if (PAID.equals(invoice.getStatusCode())) {
                    return wfmStrings.paid();
                } else if (OPEN.equals(invoice.getStatusCode())) {
                    return wfmStrings.open();
                } else {
                    return invoice.getStatus();
                }
            }
        };
        column.addColor(new ColumnColor(reference.OVER_DUE(), "r", "DC0C0C"));
        column.addColor(new ColumnColor(reference.PAID(), "r", "2BBF57"));
        column.addColor(new ColumnColor(reference.DRAFT(), "c", "007DE7"));
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currency(), InvoiceList.CURRENCY, 60) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(30);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdBy(), InvoiceList.CREATOR, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCreatorName() != null ? invoice.getCreatorName() : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.PROJECT, wfmStrings.project()), InvoiceList.RELATED_PROJECT, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getRelatedProjectName() != null ? invoice.getRelatedProjectName() : "";
            }
        };
        column.setMinimumColumnWidth(120);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.total() + " (" + AccountingUtils.get().getBaseCurrencySymbol() + ")", InvoiceList.BASE_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getTotal() != null ? AccountingUtils.get().formatPrice(invoice.getTotal()) : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.reference(), InvoiceList.REFERENCE, 80) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getReference() != null ? invoice.getReference() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        column.setMinimumColumnWidth(50);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.quoteNo(), InvoiceList.QUOTE_NUMBER, 80) {
            @Override
            public String getCellValue(NewInvoice inovoice) {
                return inovoice.getQuoteNumber() != null ? inovoice.getQuoteNumber() : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        column.setMinimumColumnWidth(50);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.manager(), InvoiceList.MANAGER, 150) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getCurrentApproverSelectItem() != null ? invoice.getCurrentApproverSelectItem().getName() : "";
            }
        };
        column.setMinimumColumnWidth(80);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.subtotal(), InvoiceList.SUB_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return invoice.getSubtotal() != null ? AccountingUtils.get().formatPrice(invoice.getSubtotal()) : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        column.setShow(false);
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<NewInvoice, Widget>(Property.get(Constants.Opportunities, wfmStrings.opportunity()), InvoiceList.OPPORTUNITY_NUMBER, 100) {
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
        column.setColumnSortable(false);
        column.setShow(false);
        column.setMinimumColumnWidth(80);
        columns.add(column);
        column = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdDate(), InvoiceList.CREATED_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                return DateUtils.format(invoice.getCreationDate());
            }
        };
        column.setMinimumColumnWidth(100);
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    void validateAndDeleteInvoice(Integer objectID) {
        LoadingPanel.loading(true);
        invoiceService.validateStockInconsistencyInDeleteProcess(StockTransactionType.CREDIT_NOTE, objectID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    deleteInvoice(objectID, false);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    void deleteInvoice(Integer objectID, boolean isCreditNote) {
        invoiceService.deleteInvoice(objectID, SALE_INVOICE, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {

                if (result == -3) {
                    Info.warn(accountingStrings.filedWarning());
                } else if (result == -2) {
                    Info.warn(property.getSingular(accountingStrings.errorDeletingProduct(), wfmStrings.salesInvoice()));
                } else if (result == -1) {
                    Info.warn(accountingStrings.cannotDeleteCreditNote());
                } else {
                    Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.salesInvoice()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, null, SaleInvoiceListView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, null, SaleInvoiceListView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, SaleInvoiceListView.this);
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            listPanel.reloadPage();
                        }
                    };
                    t.schedule(3500);

                    String containerName = isCreditNote ? RECEIVABLE_CREDIT_NOTE + objectID : SALE_INVOICE + objectID;
                    removeDeletedTab(containerName);
                }
            }
        });
    }

    void validateAndVoidInvoice(Integer objectID, Date voidDate, KpiModal dialogBox) {
        LoadingPanel.loading(true);
        invoiceService.validateStockInconsistencyInDeleteProcess(StockTransactionType.CREDIT_NOTE, objectID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    voidInvoice(objectID, voidDate, false, false, dialogBox);
                } else {
                    dialogBox.close();
                    LoadingPanel.loading(false);
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                }
            }
        });
    }

    void voidInvoice(Integer objectID, Date voidDate, boolean isCreditNote, boolean isProgressInvoicing, KpiModal dialogBox) {
        invoiceService.voidInvoice(objectID, new DateNonConvertable(voidDate), new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                dialogBox.close();
            }

            public void success(Integer result) {
                dialogBox.close();
                if (result == -2) {
                    Info.warn(property.getSingular(accountingStrings.cannotDeleteCreditNoteHasOutTransactions(), wfmStrings.salesInvoice()));
                } else if (result == -3) {
                    Info.warn(accountingStrings.cannotDeleteCreditNote());
                } else if (result == 0) {
                    Info.show(accountingStrings.youCantInvoiceWhichHasPayment(), Info.Type.WARNING);
                } else {
                    Info.show(accountingStrings.invoiceReversedSuccessfully(), Info.Type.INFO);
                    listPanel.reloadPage();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_VOID, result, SaleInvoiceListView.this);
                    if (isProgressInvoicing) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, null, SaleInvoiceListView.this);
                    }
                }
            }
        });
    }

    private int getCopyAction(NewInvoice invoice, MenuBar menuBar, MenuBar copyMenu, int copyItems) {
        boolean hasAccountingBeforeBlockDate = (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(invoice.getInvoiceDate().getNonConvertedDate()));
        final boolean isCreditNote = invoice.isCreditNote();
        final Integer objectID = invoice.getID();
        String statusCode = invoice.getStatusCode();
        final boolean isProjectBasedInvoice = invoice.isProjectBasedInvoice();

        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_COPY)) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(isCreditNote ? accountingStrings.creditNote() : property.getShort(wfmStrings.salesInvoice()), "icon-copy");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                if (isCreditNote) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|add/add/copyFromExistingData/" + objectID);
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|add/add/copyFromExistingData/" + objectID);
                }
            });
            if (!isProjectBasedInvoice) {
                copyMenu.addItem(copyExistingDataLink);
                copyItems++;
            }
        }

        if (!isCreditNote) {
            if (Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_ADD) && !hasAccountingBeforeBlockDate) {
                if (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode)) {
                    MenuPopItem addCreditNote = new MenuPopItem(accountingStrings.creditNote(), "icon-add-category");
                    addCreditNote.setCommand(() -> {
                        addCreditNote.closeAll(menuBar);
                        SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|add/add/fromInvoice/" + objectID);
                    });
                    copyMenu.addItem(addCreditNote);
                    copyItems++;
                }
            }

            if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_COPYTOPO)) {
                MenuPopItem copyToPO = new MenuPopItem(Property.getShortName(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), "icon-copy");
                copyToPO.setCommand(() -> {
                    copyToPO.closeAll(menuBar);
                    SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|add/add/copyFromSalesInvoice/" + objectID);
                });
                copyMenu.addItem(copyToPO);
                copyItems++;
            }

            if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_COPYTOPI)) {
                MenuPopItem copyToPI = new MenuPopItem(Property.getShortName(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), "icon-copy");
                copyToPI.setCommand(() -> {
                    copyToPI.closeAll(menuBar);
                    SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|add/add/copySalesInvoice/" + objectID);
                });
                copyMenu.addItem(copyToPI);
                copyItems++;
            }
        }
        return copyItems;
    }

    private void saveInvoiceEditCellValue(NewInvoice rowValue, String columnCode) {
        boolean editPermission = !rowValue.isCreditNote() ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_EDIT) : Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_EDIT);
        if ((editPermission && Utils.isCustomField(columnCode)) || rowValue.hasAccess()) {
            invoiceService.saveInvoiceEditCellValue(rowValue, columnCode, new AbstractAsyncCallback() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            listPanel.reloadPage();
        }
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectId, boolean isCreditNote, boolean isProjectBasedInvoice) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + (isProjectBasedInvoice ? "/savedProjectBaseInvoiceViewPDFHandler" : (isCreditNote ? "/savedReceivableCreditNoteViewPDFHandler" : "/savedSaleInvoceViewPDFHandler"));
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private String getAmountAsString(BigDecimal amount, Boolean isCreditNote) {
        if (isCreditNote) {
            return "(" + AccountingUtils.get().formatPrice(amount.abs()) + ")";
        } else {
            return AccountingUtils.get().formatPrice(amount);
        }
    }

    @Override
    public String getIconStyle() {
        return "salesInsList sales-invoice-list";
    }

    public class AddNewMenus extends PopupPanel implements ClickHandler {
        private final MenuBar contextMenu = new MenuBar(true);

        public AddNewMenus() {
            super(true);
            setAnimationEnabled(true);
            createMenu();
            add(contextMenu);
        }

        private MenuBar createMenu() {
            contextMenu.clearItems();
            contextMenu.setAutoOpen(false);

            if (Utils.hasPermission(ACCOUNTING_TIMESHEET_INVOICE_ADD)) {
                contextMenu.addItem(accountingStrings.timesheetInvoice(), true, () -> {
                    if (isAccountingSection) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("projectBaseInvoice|add/add");
                    } else {
                        String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + "projectBaseInvoice" + "|add/add/" + fp.getCrmAccountId();
                        Window.open(addSalesInvoice, "_blank", "");
                    }
                }).ensureDebugId(SALE_INVOICE + accountingStrings.timesheetInvoice());
            }

            if (Utils.hasPermission(ACCOUNTING_RECURRING_INVOICE_ADD)) {
                contextMenu.addItem(Property.get(Constants.RECURRING_INVOICE, accountingStrings.recurringInvoice()), true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringinvoice|add/add")).ensureDebugId(SALE_INVOICE + accountingStrings.recurringInvoice());
            }
            if (Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_ADD)) {
                contextMenu.addItem(accountingStrings.creditNote(), true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|add/add")).ensureDebugId(SALE_INVOICE + accountingStrings.creditNote());
            }
            if (Utils.hasPermission(ACCOUNTING_RECEIVE_PAYMENT)) {
                contextMenu.addItem(accountingStrings.receivePayment(), true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + RECEIVABLE)).ensureDebugId(SALE_INVOICE + accountingStrings.receivePayment());
            }
            if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_ADD)) {
                contextMenu.addItem(Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addPrepayment()), true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|add/add/")).ensureDebugId(SALE_INVOICE + Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addPrepayment()));
            }

            return contextMenu;
        }

        @Override
        public void onClick(ClickEvent clickEvent) {
            final int left = clickEvent.getRelativeElement().getAbsoluteLeft();
            final int top = clickEvent.getRelativeElement().getAbsoluteTop() + clickEvent.getRelativeElement().getOffsetHeight();
            this.setPopupPosition(left, top);
            this.show();
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
        initSaleInvoiceList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return SALE_INVOICE;
    }
}
