package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Iftixor
 * Date: 08.10.21
 * Time: 21:40:53
 */
public class RentalOrderListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final RentalOrderServiceAsync rentalOrderService = RentalOrderService.App.get();

    private ListingPanel<RentalOrderData> listingPanel;
    private int actionItemCount;
    private Integer rentalItemId;

    public RentalOrderListView() {
        super(RENTAL_ORDERS);
        setDescription(property.getPlural(wfmStrings.rentalOrders()));
        if (hasPermissionToCreateRentalOrder()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|add/add"));
        }
    }

    public RentalOrderListView(Integer rentalItemId) {
        super(RENTAL_ORDERS);
        setDescription(property.getPlural(wfmStrings.rentalOrders()));
        if (hasPermissionToCreateRentalOrder()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|add/add"));
        }
        this.rentalItemId = rentalItemId;
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.RentalOrdersListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RENTAL_ORDER_ADDED, this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RENTAL_ORDER_DELETE, this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALES_INVOICE_CONVERT_AND_ADD, this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_ADDED_FROM_RENTAL_ORDER, this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    private boolean hasPermissionToCreateRentalOrder() {
        return Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_ADD);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToCreateRentalOrder() ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToCreateRentalOrder()) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);

                    newItem.addClickHandler((clickEvent) -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|add/add"));
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                return null;
            }


            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.messCurrentlyInventoryItems(), accountingStrings.rentalOrder()));
                message.setHref("product|add/add/");
                message.setTextBeforeLink(property.getPlural(accountingStrings.messAddingInventoryClicking(), accountingStrings.rentalOrder()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
//                return Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_EDIT);
                return false;
            }
        };
    }

    private ListingRequestProvider<RentalOrderData> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadRentalOrderData(filterParametrs, callback, null);
        };
    }

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsList = new ArrayList<>();
        //Action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<RentalOrderData, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final RentalOrderData item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_SUMMARY)) {
                    MenuPopItem productSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-puchase-invoise-small");
                    productSummary.setCommand(() ->
                            SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|summary/" + item.getObjectID(), item.getNumber(), item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(productSummary);
                }

                if (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_EDIT) && !RENTAL_INVOICED.equals(item.getStatusCode())) {
                    MenuPopItem productEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    productEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|add/add/" + item.getObjectID(), item.getNumber(), item.getNumber()));
                    actionItemCount++;
                    menuBar.addItem(productEdit);
                }

                if (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_DELETE) && !RENTAL_INVOICED.equals(item.getStatusCode())) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                rentalOrderService.deleteRentalOrder(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {

                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean deleted) {
                                        LoadingPanel.loading(false);
                                        if (deleted) {
                                            Info.show(wfmMessages.yourSomethingHasBeenDeleted(accountingStrings.rentalOrder()));
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RENTAL_ORDER_DELETE, deleted, RentalOrderListView.this);
                                        }
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(removeItem);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsList.add(column);
        //Number
        column = new ColumnDefinitionConfig<RentalOrderData, SimpleLink>(wfmStrings.number(), RentalOrderData.NUMBER, 150) {

            @Override
            public SimpleLink getCellValue(final RentalOrderData item) {
                SimpleLink label = new SimpleLink(item.getNumber());
                label.addClickHandler(clickEvent ->
                        SinksContainerFactory.entryPoint.onHistoryChanged("rentalorder|summary/" + item.getObjectID(), item.getNumber(), item.getNumber()));
                return label;
            }
        };
        columnsList.add(column);
        //Customer
        column = new ColumnDefinitionConfig<RentalOrderData, String>(wfmStrings.customer(), RentalOrderData.CUSTOMER, 150) {

            @Override
            public String getCellValue(final RentalOrderData item) {
                return item.getCustomer() != null ? item.getCustomer().getName() : null;
            }
        };
        column.setMinimumColumnWidth(100);
        columnsList.add(column);

        column = new ColumnDefinitionConfig<RentalOrderData, String>(wfmStrings.expiryDate(), RentalOrderData.EXPIRATION, 150) {
            @Override
            public String getCellValue(RentalOrderData item) {
                return DateUtils.format(item.getExpirationDate());
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(100);
        columnsList.add(column);

        //status
        column = new ColumnDefinitionConfig<RentalOrderData, String>(wfmStrings.status(), RentalOrderData.STATUS, 150) {

            @Override
            public String getCellValue(final RentalOrderData item) {
                return item.getStatus() != null ? item.getStatus().getName() : "";
            }
        };
        columnsList.add(column);
        //created by
        column = new ColumnDefinitionConfig<RentalOrderData, SelectItem>(wfmStrings.createdBy(), RentalOrderData.CREATOR, 100) {
            @Override
            public SelectItem getCellValue(final RentalOrderData item) {
                return item.getCreator();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsList.add(column);
        //created by
        column = new ColumnDefinitionConfig<RentalOrderData, String>(wfmStrings.createdDate(), RentalOrderData.CREATED_DATE, 100) {
            @Override
            public String getCellValue(final RentalOrderData item) {
                return DateUtils.formatInternal(item.getCreatedDate());
            }
        };
        column.setMinimumColumnWidth(100);
        columnsList.add(column);
        //approvers
        column = new ColumnDefinitionConfig<RentalOrderData, SelectItem>(wfmStrings.approvers(), RentalOrderData.APPROVERS, 100) {
            @Override
            public SelectItem getCellValue(final RentalOrderData item) {
                return item.getApprover();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsList.add(column);

        return columnsList.toArray(new ColumnDefinitionConfig[0]);
    }

    @Override
    public String getIconStyle() {
        return "accountMark purchase-order-list";
    }

    private void loadRentalOrderData(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        if (filterParametrs == null) {
            filterParametrs =  new ListingFilterParameter();
        }
        filterParametrs.setProductId(rentalItemId);
        RentalOrderService.App.get().getRentalOrderList(filterParametrs, new AsyncCallback<ListResult<RentalOrderData>>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<RentalOrderData> listResult) {
                if (callback != null) {
                    callback.onSuccess(listResult);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (listResult.getTotal() != null && listResult.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(listResult.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }

            }
        });
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadRentalOrderData(fp, null, container);
    }

    @Override
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
        return Constants.RENTAL_ORDERS;
    }
}
