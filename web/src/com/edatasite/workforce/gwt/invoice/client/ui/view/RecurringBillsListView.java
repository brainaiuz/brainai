package com.edatasite.workforce.gwt.invoice.client.ui.view;

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
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.RecurringInvoiceListItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.payment.InvoicePaymentView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * Created by Sherzod on 6/17/2015.
 */
public class RecurringBillsListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    protected ListingPanel<RecurringInvoiceListItem> list;

    public RecurringBillsListView() {
        super(RECURRING_BILL);
        setDescription(property.getPlural(wfmStrings.recurringBills()));
        if (hasPermissionToCreateRecurringBills()) {
            setAddNew("recurringbill|add/add");
        }
    }

    protected Widget onInitialize() {
        initListPanel();
        return null;
    }

    private GuideListingPanelDesign getListPanelDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateRecurringBills() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringbill|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.PURCHASE_INVOICE_ORDER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.CLIENT_FIELD);
                        fields.add(ListingChooseFilter.RECCURING_INVOICE_STATUS_FIELD);
                        fields.add(ListingChooseFilter.RECCURING_INVOICE_RECCURENCE_STATUS_FIELD);
                        return fields;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (hasPermissionToCreateRecurringBills()/*!Utils.hasRole(CLIENT)*/) {
                    addNew = getAddNewButton();
                    addNew.ensureDebugId(RECURRING_BILL + "addNewButton");
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("recurringbill|add/add"));
                }
                return addNew;
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
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage() {
                    public VerticalPanel getWholeMessage() {
                        VerticalPanel vp = new VerticalPanel();
                        vp.add(new Label(property.getPlural(accountingStrings.noRecurringBillsMessage(), wfmStrings.recurringBills())));
                        if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_INVOICE_ADD : ACCOUNTING_RECURRING_INVOICE_ADD)/*!Utils.hasRole(CLIENT)*/) {
                            HorizontalPanel hp = new HorizontalPanel();
                            SimpleLink link = new SimpleLink(wfmStrings.here(), "recurringbill|add/add");
                            link.setStyleName("addLinkStyle");
                            hp.add(new Label(property.getPlural(accountingStrings.noRecurringBillsMessage(), wfmStrings.recurringBills()) + " "));
                            hp.add(link);
                            vp.add(hp);
                        }
                        return vp;
                    }
                });
            }
        };
    }

    private ColumnDefinitionConfig[] getRecurringBillColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[9];

        columns[0] = new ColumnDefinitionConfig<RecurringInvoiceListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final RecurringInvoiceListItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_SUMMARY : ACCOUNTING_RECURRING_BILL_SUMMARY)) {
                    MenuPopItem invoiceSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    invoiceSummary.ensureDebugId(RECURRING_BILL + "invoiceSummary");
                    invoiceSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(RECURRING_BILL + "|summary/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(invoiceSummary);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_EDIT : ACCOUNTING_RECURRING_BILL_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.ensureDebugId(RECURRING_BILL + "edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(RECURRING_BILL + "|edit/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_DELETE : ACCOUNTING_RECURRING_BILL_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.ensureDebugId(RECURRING_BILL + "delete");
                    delete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + property.getSingular(accountingStrings.recurringBill().toLowerCase()) + "?");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                deleteInvoice(item.getObjectId());
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_COPY : ACCOUNTING_RECURRING_BILL_COPY)) {
                    MenuPopItem copyExistingDataLink = new MenuPopItem(property.getSingular(wfmStrings.copyTo(), accountingStrings.invoice()), "icon-copy");
                    copyExistingDataLink.ensureDebugId(RECURRING_BILL + "copyExistingDataLink");
                    copyExistingDataLink.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(RECURRING_BILL + "|add/add/copyFromExistingData/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(copyExistingDataLink);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<RecurringInvoiceListItem, Widget>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), RecurringInvoiceListItem.CLIENT, 200) {

            @Override
            public Widget getCellValue(RecurringInvoiceListItem item) {
                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_SUMMARY : ACCOUNTING_RECURRING_BILL_SUMMARY) && item.getClient() != null) {
                    return getLink(item.getClient(), RECURRING_BILL + "|summary/" + item.getObjectId(), "", "");
                }
                return new HTML(item.getClient());
            }
        };
        columns[1].setMinimumColumnWidth(150);
        columns[1].addStyleAttribute("padding-left", "5px");

        columns[2] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.amount(), RecurringInvoiceListItem.AMOUNT, 100) {
            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getAmountInInvoiceCurrency() != null ? AccountingUtils.get().formatPrice(item.getAmountInInvoiceCurrency()) : "";
            }
        };
        columns[2].setMinimumColumnWidth(100);
        columns[2].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[2].addStyleAttribute("padding-right", "5px");

        columns[3] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.repeats(), RecurringInvoiceListItem.REPEATS, 120) {

            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getRepeats();
            }
        };
        columns[3].setMinimumColumnWidth(100);
        columns[3].setColumnSortable(false);
        columns[3].addStyleAttribute("padding-left", "5px");
        columns[3].setShow(false);

        columns[4] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(accountingStrings.nextInvoiceDate(), RecurringInvoiceListItem.NEXT_IVOICE_DATE, 150) {

            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getNextInvoiceDate() != null ? DateUtils.format(item.getNextInvoiceDate()) : "";
            }
        };
        columns[4].setMinimumColumnWidth(100);
        columns[4].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[5] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.endDate(), RecurringInvoiceListItem.END_DATE, 100) {

            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getEndDate() != null ? DateUtils.format(item.getEndDate()) : "";
            }
        };
        columns[5].setMinimumColumnWidth(100);
        columns[5].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[6] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.status(), RecurringInvoiceListItem.STATUS, 140) {

            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                String status = "";
                /*Date d = new Date();
                if (item.getEndDate() != null && item.getEndDate().before(new Date())){
                    return accountingMessages.ended();
                }else{*/
                if (DRAFT.equals(item.getStatusCode())) {
                    status = wfmStrings.draft().toLowerCase();
                } else if (APPROVE.equals(item.getStatusCode())) {
                    status = wfmStrings.approved().toLowerCase();
                } else if (OPEN.equals(item.getStatusCode())) {
                    status = wfmStrings.sent().toLowerCase();
                } else if (PAID.equals(item.getStatusCode())) {
                    status = wfmStrings.paid().toLowerCase();
                } else if (OVER_DUE.equals(item.getStatusCode())) {
                    status = accountingStrings.overdue().toLowerCase();
                }
                //}
                return accountingMessages.invoiceWillBe(status);
            }
        };
        columns[6].setMinimumColumnWidth(100);
        columns[6].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[7] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.recurrenceStatus(), RecurringInvoiceListItem.RECURRENCE_STATUS, 140) {

            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getRecurrenceStatus();
            }
        };
        columns[7].setMinimumColumnWidth(100);
        columns[7].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[8] = new ColumnDefinitionConfig<RecurringInvoiceListItem, String>(wfmStrings.total() + " (" + AccountingUtils.get().getBaseCurrencySymbol() + ")", RecurringInvoiceListItem.BASE_AMOUNT, 140) {
            @Override
            public String getCellValue(RecurringInvoiceListItem item) {
                return item.getAmount() != null ? AccountingUtils.get().formatPrice(item.getAmount()) : "";
            }
        };
        columns[8].setMinimumColumnWidth(100);
        columns[8].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[8].addStyleAttribute("padding-right", "5px");
        columns[8].setShow(false);

        return columns;
    }

    public void initListPanel() {
        list = new GuideListingPanel(ListPanelType.RecurringBillPanel, getRecurringBillColumns(), getListData(), getListPanelDesigner());
        if (list.getChooseFilter() != null) {
            list.getChooseFilter().setAccountingListType(getName());
        }

        list.setPDFListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs.getStartDate() != null) {
                filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
            }
            if (filterParametrs.getEndDate() != null) {
                filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
            }
            list.callListPDF(CommandConstants.PDF_URL + "/recurringBillListPDFHandler", filterParametrs);
        });

        list.setExcelListener(clickEvent -> {
            if (list.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs.getStartDate() != null) {
                filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
            }
            if (filterParametrs.getEndDate() != null) {
                filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
            }
            list.callListExcel(CommandConstants.COMMON_URL + "/downloadRecurringBillsExcelHandler", filterParametrs);
        });

        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, RecurringBillsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, RecurringBillsListView.this, (sender, args) -> {
            String status = (String) args;
            if (InvoiceSummaryView.ADD_PURCHASEINVOICE.equals(status) || InvoicePaymentView.DELETE_PURCHASEINVOICE.equals(status)) {
                list.reloadPage();
            }
        });
    }

    private ListingRequestProvider<RecurringInvoiceListItem> getListData() {
        return (filterParametrs, callback) -> {
            if (filterParametrs != null) {
                if (filterParametrs.getStartDate() != null) {
                    filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
                }
                if (filterParametrs.getEndDate() != null) {
                    filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
                }
            }

            invoiceService.getRecurringBillData(filterParametrs, new AbstractAsyncCallback<ListResult<RecurringInvoiceListItem>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<RecurringInvoiceListItem> result) {
                    callback.onSuccess(result);
                }

            });
        };
    }

    private boolean hasPermissionToCreateRecurringBills() {
        return Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_RECURRING_BILL_ADD : ACCOUNTING_RECURRING_BILL_ADD);
    }

    private void deleteInvoice(Integer objectID) {
        invoiceService.deleteInvoice(objectID, RECURRING_BILL, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.salesInvoice()), Info.Type.INFO);
                list.reloadPage();
                removeDeletedTab(RECURRING_BILL + objectID);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
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
        return Constants.RECURRING_BILL;
    }
}
