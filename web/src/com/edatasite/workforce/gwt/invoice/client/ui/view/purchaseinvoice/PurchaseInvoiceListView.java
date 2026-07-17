package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PurchaseInvoiceListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private ListingPanel<NewInvoice> listPanel;
    protected HashSet selectedItems = new HashSet();
    private Integer clientID, productID, warehouseID;
    private ListingFilterParameter fp;
    private final ActionButton delete = null;

    public PurchaseInvoiceListView() {
        super(PURCHASE_INVOICE);
        setDescription(property.getPlural(wfmStrings.purchaseInvoices()));
        setPlusIcon();
    }

    public PurchaseInvoiceListView(Integer productID, Boolean isProductPI) {
        super(PURCHASE_INVOICE);
        setDescription(property.getPlural(wfmStrings.purchaseInvoices()));
        this.productID = productID;
        setPlusIcon();
    }

    public PurchaseInvoiceListView(ListingFilterParameter fP) {
        super(PURCHASE_INVOICE);
        setDescription(property.getPlural(wfmStrings.purchaseInvoices()));
        this.fp = fP;
        if (fp != null && fp.getSupplierId() != null) {
            this.clientID = fp.getSupplierId();
        }
        if (fp != null && fp.getWarehouseID() != null) {
            this.warehouseID = fp.getWarehouseID();
        }
        setPlusIcon();
    }

    private void setPlusIcon() {
        if (hasPermissionToCreatePurchaseInvoice()) {
            setAddNew("purchaseinvoice|add/add");
        }
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<NewInvoice> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NewInvoice item : selectedItems) {
            ids.add(item.getID());
        }
        return ids;
    }

    private void savePurchaseInvoiceCellValue(NewInvoice rowValue, String columnCodeName) {
        if (rowValue.hasAccess()) {
            invoiceService.savePurchaseInvoiceCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            listPanel.reloadPage();
        }
    }

    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }

    protected ListingRequestProvider<NewInvoice> getListData() {
        return (filterParameters, callback) -> invoiceService.getPurchaseInvoiceData(filterParameters, new AbstractAsyncCallback<ListResult<NewInvoice>>() {
            @Override
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void success(ListResult<NewInvoice> result) {
                callback.onSuccess(result);
            }
        });
    }

    protected void validateAndDeleteInvoice(Integer objectID) {
        LoadingPanel.loading(true);
        invoiceService.validateStockInconsistencyInDeleteProcess(StockTransactionType.PURCHASE_INVOICE, objectID, new AbstractAsyncCallback<SelectItem>() {
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

    protected void deleteInvoice(Integer objectID, boolean isCreditNote) {
        invoiceService.deleteInvoice(objectID, PURCHASE_INVOICE, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);

                if (result.equals(-3)) {
                    Info.warn(accountingStrings.filedWarning());
                } else if (result.equals(-1)) {
                    Info.show((property.getSingular(accountingStrings.cannotDeletePurchaseInvoiceMessage(), wfmStrings.purchaseinvoice())), Info.Type.WARNING);
                } else if (result.equals(-2)) {
                    Info.warn(property.getSingular(accountingStrings.errorDeletingProduct(), wfmStrings.purchaseinvoice()));
                } else {
                    Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.purchaseInvoices()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.PI_DELETE_FIXEDASSET_RELOAD, null, PurchaseInvoiceListView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, PurchaseInvoiceListView.this);
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            listPanel.reloadPage();
                        }
                    };
                    t.schedule(3500);

                    String containerName = isCreditNote ? PAYABLE_CREDIT_NOTE + objectID : PURCHASE_INVOICE + objectID;
                    removeDeletedTab(containerName);
                }
            }
        });
    }

    void validateAndVoidInvoice(Integer objectID, Date voidDate, KpiModal dialogBox) {
        LoadingPanel.loading(true);
        invoiceService.validateStockInconsistencyInDeleteProcess(StockTransactionType.PURCHASE_INVOICE, objectID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    voidInvoice(objectID, voidDate, false, dialogBox);
                } else {
                    dialogBox.close();
                    LoadingPanel.loading(false);
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                }
            }
        });
    }

    void voidInvoice(Integer objectID, Date voidDate, boolean isCreditNote, KpiModal dialogBox) {
        invoiceService.voidInvoice(objectID, new DateNonConvertable(voidDate), new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                dialogBox.close();
            }

            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                dialogBox.close();
                if (result == -2) {
                    Info.show((property.getSingular(accountingStrings.cannotVoidPurchaseInvoiceHasOutTransactions(), wfmStrings.purchaseinvoice())), Info.Type.WARNING);
                } else if (result == -3) {
                    Info.show((property.getSingular(accountingStrings.cannotVoidPurchaseInvoiceMessage(), wfmStrings.purchaseinvoice())), Info.Type.WARNING);
                } else if (result == 0) {
                    Info.show(accountingStrings.youCantInvoiceWhichHasPayment(), Info.Type.WARNING);
                } else {
                    Info.show((isCreditNote ? accountingStrings.debitNote() : accountingStrings.invoice()) + " reversed successfully", Info.Type.INFO);
                    listPanel.reloadPage();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICE_VOID, result, PurchaseInvoiceListView.this);
                }
            }
        });
    }

    boolean hasPermissionToCreatePurchaseInvoice() {
        return Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD);
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<NewInvoice, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final NewInvoice item) {
                final boolean isCreditNote = item.isCreditNote();
                final boolean isProjectBasedInvoice = item.isProjectBasedInvoice();
                String statusCode = item.getStatusCode();
                final Integer objectID = item.getID();
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getProjectStatusCode()));
                boolean hasAccountingBeforeBlockDate = (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(item.getInvoiceDate().getNonConvertedDate()));

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (!DRAFT.equals(item.getStatusCode()))
                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                        MenuPopItem purchaseSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                        purchaseSummary.setCommand(() -> {
                            if (isCreditNote) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|summary/" + item.getID(), item.getInvoiceNumber());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|summary/" + item.getID(), item.getInvoiceNumber());
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(purchaseSummary);
                    }

                boolean editPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_EDIT) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT);

                boolean editFullPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS);

                boolean isAccessToEdit = (item.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission;
                if (item.isApprover()) {
                    isAccessToEdit = editPermission && item.getCurrentApproverSelectItem() != null && Utils.getUserID().equals(item.getCurrentApproverSelectItem().getId());
                }

                if (!PENDING.equals(statusCode)) {

                    if (hasAccessToChange && isAccessToEdit && (item.getConverted() == null || !item.getConverted().equals(true))) {
                        if ((PAID.equals(statusCode) && Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT)) || INVOICE_STATUS_PENDING.equals(statusCode) || DRAFT.equals(statusCode)
                                || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)
                                || MANAGER_REJECT.equals(statusCode) || OVER_DUE.equals(statusCode) || OPEN.equals(statusCode)) {
                            if (!hasAccountingBeforeBlockDate) {
                                item.setAccess(true);

                                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                                edit.setCommand(() -> {
                                    if (isCreditNote) {
                                        SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|edit/" + item.getID(), item.getInvoiceNumber());
                                    } else {
                                        SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|edit/" + item.getID()
                                                + (isProjectBasedInvoice ? "/projectbased" : ""), item.getInvoiceNumber());
                                    }
                                });
                                actionItemCount++;
                                menuBar.addItem(edit);
                            }
                        }
                    }

                    PropertyItem propertyItem = Utils.getProperTy(Constants.PURCHASE_INVOICE);
                    if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                        MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                        MenuBar convertMenu = new MenuBar(true);
                        convertMenu.setAutoOpen(true);
                        int convertItems = 0;
                        for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                            if (convertItem != null) {
                                convertItems = getConvertItems(item, menuBar, convertMenu, convertItems, convertItem);
                            }
                        }

                        if (convertItems > 0) {
                            convertMenuPopItem.setSubMenu(convertMenu);
                            actionItemCount++;
                            menuBar.addItem(convertMenuPopItem);
                        }
                    }


                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_DELETE) && hasAccessToChange && !hasAccountingBeforeBlockDate) {
                        MenuPopItem delete = new MenuPopItem(wfmStrings.delete(),
                                "removeItemStyle-profile");
                        delete.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.warning());
                            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    if (!isCreditNote) {
                                        validateAndDeleteInvoice(item.getID());
                                    } else {
                                        deleteInvoice(item.getID(), isCreditNote);
                                    }
                                }
                            });
                            messageBox.open();
                        });
                        actionItemCount++;
                        menuBar.addItem(delete);
                    }

                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_VOID) && hasAccessToChange) {
                        if (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode)) {
                            MenuPopItem voidInvoice = new MenuPopItem(accountingStrings.voide() + " " + (isCreditNote ? accountingStrings.debitNote() : accountingStrings.invoice().toLowerCase()), "icon-puchase-invoise-small");
                            voidInvoice.setCommand(() -> {
                                final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                                confirmBox.setTitle(wfmStrings.confirmation());
                                confirmBox.setMessage(accountingStrings.areYouSureYouWantToVoidThe() + " " + (isCreditNote ? accountingStrings.debitNote() : accountingStrings.invoice()) + " ?");
                                confirmBox.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onSubmit() {
                                        final KpiModal dialogBox = new KpiModal();
                                        dialogBox.setCloseButton(true);
                                        dialogBox.setWidth(400);
                                        final DatePicker datePicker = new DatePicker(item.getInvoiceDate().getNonConvertedDate());
                                        dialogBox.setTitle(wfmStrings.selectVoidDate());
                                        datePicker.setWidth("180px");
                                        datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                                        dialogBox.add(datePicker);
                                        final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                                        dialogBox.addButton(voidButton);
                                        voidButton.addClickHandler(clickEvent -> {
                                            if (AccountingUtils.validateVoidDate(datePicker.getDate(), item.getInvoiceDate().getNonConvertedDate())) {
                                                voidButton.setEnabled(false);
                                                if (!isCreditNote) {
                                                    validateAndVoidInvoice(item.getID(), datePicker.getDate(), dialogBox);
                                                } else {
                                                    voidInvoice(item.getID(), datePicker.getDate(), isCreditNote, dialogBox);
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
                    MenuPopItem copyMenuPopItem = new MenuPopItem(wfmStrings.copy(), "icon-add-green");
                    MenuBar copyMenu = new MenuBar(true);
                    copyMenu.setAutoOpen(true);
                    int copyItems = 0;

                    copyItems = getCopyAction(item, menuBar, copyMenu, copyItems);

                    if (copyItems > 0) {
                        copyMenuPopItem.setSubMenu(copyMenu);
                        actionItemCount++;
                        menuBar.addItem(copyMenuPopItem);
                    }

                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD) && hasAccessToChange && !isCreditNote
                            && (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) {
                        MenuPopItem addCreditNote = new MenuPopItem(accountingStrings.addDebitNote(), "icon-add-category");
                        addCreditNote.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add/fromInvoice/" + item.getID()));
                        actionItemCount++;
                        menuBar.addItem(addCreditNote);
                    }


                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD)) {
                        if (hasAccessToChange && !Utils.hasRole(CLIENT)) {
                            if (isCreditNote && (APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) {
                                MenuPopItem allocateCreditNote = new MenuPopItem(accountingStrings.allocate() + " " + wfmStrings.credit(), "icon-add-category");
                                allocateCreditNote.setCommand(() -> {
                                    Command provider = () -> listPanel.reloadPage();
                                    new AllocateCreditNoteView(item.getID(), item.getInvoiceNumber(),
                                            item.getTotalInInvoiceCurrency().subtract(item.getPaidAmount() != null ? item.getPaidAmount() : ZERO), provider);
                                });
                                actionItemCount++;
                                menuBar.addItem(allocateCreditNote);
                            }
                        }

                        MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                        final HTMLPanel htmlPanel = new HTMLPanel("");
                        generatePdf.setCommand(() -> new PDFTemplateSelector(item.isCreditNote() ? PAYABLE_CREDIT_NOTE : PURCHASE_INVOICE, new ExtendedCommand() {
                            @Override
                            public void execute(Integer id) {
                                generatePDF(htmlPanel, id, objectID, item.isCreditNote());
                            }
                        }));
                        add(htmlPanel);
                        actionItemCount++;
                        menuBar.addItem(generatePdf);
                    }
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
            public Widget getCellValue(final NewInvoice item) {
                final boolean isCreditNote = item.isCreditNote();
                Label label = new Label(item.getInvoiceNumber() != null ? item.getInvoiceNumber() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> {

                    if (DRAFT.equals(item.getStatusCode())) {
                        boolean editPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_EDIT) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT);
                        boolean editFullPermission = !isCreditNote ? Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS);
                        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getProjectStatusCode()));
                        boolean hasAccountingBeforeBlockDate = (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(item.getInvoiceDate().getNonConvertedDate()));

                        if (hasAccessToChange && ((item.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission) && !item.hasAnyPayment()) {
                            if (isCreditNote) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|edit/" + item.getID(), item.getInvoiceNumber());
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|edit/" + item.getID(), item.getInvoiceNumber());
                            }
                        } else {
                            label.removeStyleName("uploadLinkStyle2");
                        }
                    } else {
                        if (isCreditNote) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|summary/" + item.getID(), item.getInvoiceNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|summary/" + item.getID(), item.getInvoiceNumber());
                        }
                    }
                });
                return label;
            }
        };
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, SimpleLink>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), InvoiceList.SUPPLIER, 300) {
            @Override
            public SimpleLink getCellValue(NewInvoice item) {
                SimpleLink label = new SimpleLink(item.getClientName() != null ? item.getClientName() : "");
                if (item.getClientID() != null && Utils.hasPermission(ACCOUNTING_SUPPLIER_SUMMARY)) {
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + item.getClientID());
                    });
                }
                return label;
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(250);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.vatNumber(), InvoiceList.CLIENT_VAT_NUMBER, 180) {
            @Override
            public String getCellValue(NewInvoice item) {
                return !Utils.isNullOrEmpty(item.getClientTrnNumber()) ? item.getClientTrnNumber() : item.getClientVatNumber();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(150);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.date(), InvoiceList.INVOICE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice item) {
                return DateUtils.format(item.getInvoiceDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.dueDate(), InvoiceList.DUE_DATE, 120) {
            @Override
            public String getCellValue(NewInvoice item) {
                return DateUtils.format(item.getDueDate());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.amount(), InvoiceList.ORIGINAL_AMOUNT, 130) {

            @Override
            public String getCellValue(NewInvoice item) {
                return getAmountAsString(item.getAmount(), item.isCreditNote());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("padding-right", "5px");
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.paidAmount(), InvoiceList.PAID_AMOUNT, 120) {
            @Override
            public String getCellValue(NewInvoice item) {
                return getAmountAsString(item.getPaidAmount(), item.isCreditNote());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("padding-right", "5px");
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.dueAmount(), InvoiceList.DUE_AMOUNT, 120) {
            @Override
            public String getCellValue(NewInvoice item) {
                return getAmountAsString(item.getDueAmount(), item.isCreditNote());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.addStyleAttribute("padding-right", "5px");
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.status(), InvoiceList.STATUS, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                if (APPROVE.equals(item.getStatusCode())) {
                    return wfmStrings.approved();
                } else if (SUBMITTED_TO_MANAGER.equals(item.getStatusCode())) {
                    return wfmStrings.submitted();
                } else if (OVER_DUE.equals(item.getStatusCode())) {
                    return accountingStrings.overdue();
                } else if (PAID.equals(item.getStatusCode())) {
                    return wfmStrings.paid();
                } else if (DRAFT.equals(item.getStatusCode())) {
                    return wfmStrings.draft();
                } else {
                    return item.getStatus();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.currency(), InvoiceList.CURRENCY, 60) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getCurrencyName() != null ? item.getCurrencyName() : "";
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.poNumber(), InvoiceList.PO_NUMBER, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getPoNumber() != null ? item.getPoNumber() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.createdBy(), InvoiceList.CREATOR, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getCreatorName() != null ? item.getCreatorName() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(Property.get(Constants.PROJECT, wfmStrings.project()), InvoiceList.RELATED_PROJECT, 150) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getRelatedProjectName() != null ? item.getRelatedProjectName() : " ";
            }
        };
        columnConfig.addStyleAttribute("paddingLeft", "5px");
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.taxTotal(), InvoiceList.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice invoice) {
                BigDecimal taxTotal = invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().multiply(invoice.getExchageRate() != null ? invoice.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO;
                return taxTotal.setScale(2, RoundingMode.HALF_UP).toString();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.total() + " (" + AccountingUtils.get().getBaseCurrencySymbol() + ")", InvoiceList.BASE_TOTAL, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getTotal() != null ? AccountingUtils.get().formatPrice(item.getTotal()) : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.reference(), InvoiceList.REFERENCE, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getReference() != null ? item.getReference() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(wfmStrings.manager(), InvoiceList.MANAGER, 100) {
            @Override
            public String getCellValue(NewInvoice item) {
                return item.getCurrentApproverSelectItem() != null ? item.getCurrentApproverSelectItem().getName() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setShow(false);
        columns.add(columnConfig);

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

        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            columnConfig = new ColumnDefinitionConfig<NewInvoice, String>(accountingStrings.zatcaStatus(), InvoiceList.ZATCA_STATUS, 100) {
                @Override
                public String getCellValue(NewInvoice invoice) {
                    return Utils.isNullOrEmpty(invoice.getZatcaStatus()) ? "N/A" : invoice.getZatcaStatus();
                }
            };
            columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfig.setMinimumColumnWidth(80);
            columns.add(columnConfig);
        }

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private int getCopyAction(NewInvoice invoice, MenuBar menuBar, MenuBar copyMenu, int copyItems) {
        final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoice.getProjectStatusCode()));
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_COPY) && hasAccessToChange) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(wfmStrings.copy(), "icon-copy");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                if (invoice.isCreditNote()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add/copyFromExistingData/" + invoice.getID());
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_INVOICE + "|add/add/copyFromExistingData/" + invoice.getID());
                }
            });
            copyItems++;
            copyMenu.addItem(copyExistingDataLink);
        }

        if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_COPY_SI)) {
            MenuPopItem copyExistingDataLink = new MenuPopItem(wfmStrings.salesInvoice(), "icon-copy");
            copyExistingDataLink.setCommand(() -> {
                copyExistingDataLink.closeAll(menuBar);
                if (invoice.isCreditNote()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|add/add/copyToSaleInvoice/" + invoice.getID());
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|add/add/copyToSaleInvoice/" + invoice.getID());
                }
            });
            copyItems++;
            copyMenu.addItem(copyExistingDataLink);
        }

        return copyItems;
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectId, boolean isCrediNote) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + (isCrediNote ? "/savedPayableCreditNoteViewPDFHandler" : "/savedPurchaseInvoiceViewPDFHandler");
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

    public static HashSet<NewInvoice> validateAndRemoveCreditNoteFromList(HashSet<NewInvoice> selectedItems) {
        for (NewInvoice item : selectedItems) {
            if (item.isCreditNote()) {
                selectedItems.remove(item);
            }
        }
        return selectedItems;
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.PurchaseInvoicePanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, false, Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_LIST_CUSTOMIZE));

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> savePurchaseInvoiceCellValue((NewInvoice) rowValue, columnCodeName));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, PurchaseInvoiceListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.FIXEDASSET_DELETE_PI_LIST_RELOAD, PurchaseInvoiceListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, PurchaseInvoiceListView.this, (sender, args) -> {
            String status = (String) args;
            if (InvoiceSummaryView.ADD_PURCHASEINVOICE.equals(status) || InvoicePaymentView.DELETE_PURCHASEINVOICE.equals(status)) {
                listPanel.reloadPage();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, PurchaseInvoiceListView.this, (sender, args) -> {
            Timer t = new Timer() {
                @Override
                public void run() {
                    listPanel.reloadPage();
                }
            };
            t.schedule(3500);
        });
        listPanel.setPDFListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/purchaseInvoiceListPDFHandler";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListPDF(pdfURL, filterParametrs);
        });

        listPanel.setExcelListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadPurchaseInvoiceExcel";
            ListingFilterParameter filterParametrs = listPanel.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
            listPanel.callListExcel(excelURL, filterParametrs);
        });

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
                if (hasPermissionToCreatePurchaseInvoice()) {
                    return PurchaseInvoiceListView.this::createNewPurchaseInvoice;
                } else if (Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD) && !Utils.hasRole(CLIENT)) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add");
                } else {
                    return null;
                }
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
                return new ListingFacetFilter() {
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
                            if (clientID != null) {
                                SelectItem[] item = new SelectItem[]{new SelectItem(clientID)};
                                if (data != null) {
                                    data.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[1]).setFacetItems(item);
                                }
                            }
                            RbacService.App.get().getPurchaseInvoiceFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
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
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                ContextMenu actions = getActions();
                int i = actions.getSize();

                if (!Utils.hasRole(CLIENT) && productID == null) {
                    if (Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD)) {
                        actions.addMenuItem(accountingStrings.debitNote(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add"));
                    }
                }
                addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                addNew.ensureDebugId(PURCHASE_INVOICE + "addNewButton");
                String shortCut = "Alt + Shift + p";
                if (Utils.isChrome()) shortCut = "Alt + p";
                addNew.setTitle(shortCut);

                final ActionButton finalAddNew = addNew;
                if (i == 0 && actions.getSize() == 1) {
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add"));
                } else if (i == 0 && actions.getSize() == 0) {
                    return new ActionButton();
                } else {
                    addNew.addClickHandler(clickEvent -> finalAddNew.setMenu(actions.getMenuBar()));
                }

                return addNew;
            }

            @Override
            public ActionButton initTopToolBarPrint() {
                return getPrintButton(clickEvent -> printSelection());
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_DELETE)) {
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
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noDataAvailableInTableList());
                if (hasPermissionToCreatePurchaseInvoice()) {
                    if (clientID != null) {
                        message.setHref("purchaseinvoice|add/add/fromSupplierList/" + clientID);
                    } else {
                        message.setHref("purchaseinvoice|add/add");
                    }
                    message.setTextBeforeLink(wfmStrings.youCanStartAddingItemByClick());
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_EDIT);
            }
        };
    }

    private void printSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.purchaseinvoice()), Info.Type.WARNING);
        } else {
            new PDFTemplateSelector(PURCHASE_INVOICE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    Set<NewInvoice> items = validateAndRemoveCreditNoteFromList(selectedItems);
                    final HTMLPanel htmlPanel = new HTMLPanel("");
                    add(htmlPanel);
                    InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(getIdsOnly(items), id);
                    String pdfURL = CommandConstants.PDF_URL + "/savedPurchaseInvoiceViewPDFHandler";
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
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
                    for (NewInvoice item : items) {
                        if (!item.isCreditNote()) {
                            validateAndDeleteInvoice(item.getID());
                            removeDeletedTab(PURCHASE_INVOICE + item.getID());
                        } else {
                            deleteInvoice(item.getID(), item.isCreditNote());
                        }
                    }

                }
            }
        });
        messageBox.open();
    }

    private ContextMenu getActions() {
        ContextMenu actions = new ContextMenu();
        if (hasPermissionToCreatePurchaseInvoice()) {
            actions.addMenuItem(property.getSingular(wfmStrings.purchaseinvoice()), true, PurchaseInvoiceListView.this::createNewPurchaseInvoice);
        }

        if (!Utils.hasRole(CLIENT) && productID == null) {

            if (Utils.hasPermission(ACCOUNTING_PAY_BILL)) {
                actions.addMenuItem(Property.get(Constants.PAYBILLS_LIST, accountingStrings.payBill()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + PAYABLE));
            }

            if (Utils.hasPermission(ACCOUNTING_SUPPLIER_CREDIT_ADD)) {
                actions.addMenuItem(Property.get(Constants.SUPPLIER_LIST, accountingStrings.supplierPrepayment(), wfmStrings.supplier()), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierCredit|add/add/"));
            }
        }
        return actions;
    }

    private void createNewPurchaseInvoice() {
        if (fp != null && fp.getOpportunityID() != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/opportunity/" + fp.getOpportunityID());
        } else if (clientID != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|add/add/fromSupplierList/" + clientID);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|add/add");
        }
    }

    private ListingRequestProvider<NewInvoice> getListProvider() {
        return (filterParametrs, listingCallback) -> {

            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            if (filterParametrs.getFacetFilter() != null) {
                filterParametrs.setStartDate(filterParametrs.getFacetFilter().getStartDate());
                filterParametrs.setEndDate(filterParametrs.getFacetFilter().getEndDate());
            }
            filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
            filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);

            initPurchaseInvoiceDataFromSolr(filterParametrs, listingCallback, null);
        };
    }

    private void initPurchaseInvoiceDataFromSolr(ListingFilterParameter filterParametrs, ListingCallback<NewInvoice> listingCallback, Span container) {
        if (clientID != null) {
            filterParametrs.setClientId(clientID);
        }
        if (fp != null) {
            if (fp.getOpportunityID() != null) {
                filterParametrs.setOpportunityID(fp.getOpportunityID());
            }
            if (fp.getRelationID() != null && fp.getRelationType() != null) {
                filterParametrs.setRelationID(fp.getRelationID());
                filterParametrs.setRelationType(fp.getRelationType());
            }
        }
        if (productID != null) {
            filterParametrs.setProductId(productID);
        }

        if (warehouseID != null) {
            filterParametrs.setWarehouseID(warehouseID);
        }

        invoiceService.getPurchaseInvoiceDataFromSolr(filterParametrs, new AbstractAsyncCallback<ListResult<NewInvoice>>() {

            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<NewInvoice> result) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }


        });
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[0], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.isProjectInLineItemEnable() ? SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_ID : SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.isProjectInLineItemEnable() ? SolrPurchaseInvoiceRepresenter.FIELD_MULTI_PROJECT_ID_NAME : SolrPurchaseInvoiceRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[1], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[2], wfmStrings.currency(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_CURRENCY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }
        });

//        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5], accountingStrings.paidAmount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });

//        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4], wfmStrings.dueAmount(), new FacetFieldConfigure() {
//            @Override
//            public String getSolrFieldCriteriaName() {
//                return SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public String getSolrFacetFieldName() {
//                return SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT;
//            }
//
//            @Override
//            public boolean isConditionItemId() {
//                return false;
//            }
//        });

        contentConfigure.addContentConfigure(FacetContentType.ClientFacetFilter.getContentCode()[1], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE;
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
        contentConfigure.addContentConfigureDateListBox(SolrPurchaseInvoiceRepresenter.FIELD_INVOICE_DATE, wfmStrings.startDate());
        contentConfigure.addContentConfigureDateListBox(SolrPurchaseInvoiceRepresenter.FIELD_DUE_DATE, wfmStrings.dueDate());

        return contentConfigure;
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
    public String getPropertyCode() {
        return PURCHASE_INVOICE;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initPurchaseInvoiceDataFromSolr(fp, null, container);
    }

    private int getConvertItems(NewInvoice rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (RelationItem.TYPE_SALEINVOICE.equals(convertItem.getCode()) && (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_ADD)
                || (Utils.isCRM() && (Utils.hasPermission(CRM_SALES_INVOICE_ADD))))) {
            final MenuPopItem convertToSI = new MenuPopItem(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), "icon-send-sales-invoice");
            convertToSI.setCommand(() -> {
                convertToSI.closeAll(menuBar);

                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|edit/CONVERT/" + RelationItem.TYPE_PURCHASE_INVOICE + "/" + rowValue.getID());
                } else {
                    Utils.openURL("Accounting.html#saleinvoice|edit/CONVERT/" + RelationItem.TYPE_PURCHASE_INVOICE + "/" + rowValue.getID());
                }
            });
            convertToSI.ensureDebugId("convert_case");
            convertMenu.addItem(convertToSI);
            convertItems++;
        }
        return convertItems;
    }
}
