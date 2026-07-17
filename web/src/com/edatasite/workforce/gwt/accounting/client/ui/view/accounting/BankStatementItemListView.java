package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingReportsImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementItemListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 20:01:57
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementItemListView extends BaseListView {

    private static final AccountingReportsImageBundles reportsImageBundle = AccountingReportsImageBundles.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<BankStatementItemListItem> list;

    private Integer bankStatementID;

    public BankStatementItemListView(Integer bankStatementID) {
        super("bankStatementItemList", accountingStrings.bankStatementItems());
        this.bankStatementID = bankStatementID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getDisagn());
        add(list);
        list.reloadPage();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_STATEMENT_ITEM_CHANGE, BankStatementItemListView.this, (sender, args) -> list.reloadPage());
        return null;
    }

    private ListingRequestProvider<BankStatementItemListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setRelationID(bankStatementID);
            AccountingService.App.get().getBankAccountStatementItemList(filterParametrs, new AbstractAsyncCallback<ListResult<BankStatementItemListItem>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<BankStatementItemListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
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
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }


    private ListPanelType getPanelType() {
        return ListPanelType.BankStatementItemListPanel;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        List<ColumnDefinitionConfig> columnList = new ArrayList<>();
        ColumnDefinitionConfig actionColumn = new ColumnDefinitionConfig<BankStatementItemListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final BankStatementItemListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem viewItem = new MenuPopItem(wfmStrings.summaryView());
                viewItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementItem|summary/" + item.getObjectID() + "/" + bankStatementID));
                actionItemCount++;
                menuBar.addItem(viewItem);

                MenuPopItem editItem = new MenuPopItem(wfmStrings.edit());
                editItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementItem|edit/" + item.getObjectID() + "/" + bankStatementID));
                actionItemCount++;
                menuBar.addItem(editItem);

                MenuPopItem deleteItem = new MenuPopItem(wfmStrings.delete());
                deleteItem.setCommand(() -> {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                    messageBox.setTitle(wfmStrings.confirmationMessage());
                    messageBox.setMessage(accountingStrings.wantToDelete()+" ?");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            AccountingService.App.get().deleteBankAccountStatementItem(item.getObjectID(), bankStatementID, new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    GWT.log(throwable.getMessage());
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.bankStatements()), Info.Type.INFO);
                                        list.reloadPage();
                                    } else {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    messageBox.open();
                });

                actionItemCount++;
                menuBar.addItem(deleteItem);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        actionColumn.setColumnSortable(false);
        actionColumn.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        actionColumn.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnList.add(actionColumn);

        ColumnDefinitionConfig statusColumn = new ColumnDefinitionConfig<BankStatementItemListItem, Widget>(wfmStrings.status(), BankStatementItemListItem.STATUS, 70) {

            @Override
            public Widget getCellValue(BankStatementItemListItem item) {
                return (item.isReconsiled() ? new Image(reportsImageBundle.reconciled()) : new Image(reportsImageBundle.notReconciled()));
            }
        };
        statusColumn.setColumnSortable(false);
        statusColumn.setMinimumColumnWidth(30);
        statusColumn.setMaximumColumnWidth(30);
        columnList.add(statusColumn);

        ColumnDefinitionConfig dateColumn = new ColumnDefinitionConfig<BankStatementItemListItem, String>(wfmStrings.date(), BankStatementItemListItem.DATE, 100) {

            @Override
            public String getCellValue(BankStatementItemListItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate()) : "";
            }
        };
        columnList.add(dateColumn);

        ColumnDefinitionConfig spentColumn = new ColumnDefinitionConfig<BankStatementItemListItem, String>(accountingStrings.spent(), BankStatementItemListItem.SPENT, 100) {

            @Override
            public String getCellValue(BankStatementItemListItem item) {
                return item.getSpent() != null ? AccountingUtils.get().formatPrice(item.getSpent()) : "";
            }
        };
        columnList.add(spentColumn);

        ColumnDefinitionConfig receivedColumn = new ColumnDefinitionConfig<BankStatementItemListItem, String>(accountingStrings.received(), BankStatementItemListItem.RECEIVED, 100) {

            @Override
            public String getCellValue(BankStatementItemListItem item) {
                return item.getReceived() != null ? AccountingUtils.get().formatPrice(item.getReceived()) : "";
            }
        };
        columnList.add(receivedColumn);

        ColumnDefinitionConfig balanceColumn = new ColumnDefinitionConfig<BankStatementItemListItem, String>(wfmStrings.balance(), BankStatementItemListItem.BALANCE, 100) {

            @Override
            public String getCellValue(BankStatementItemListItem item) {
                return item.getBalance() != null ? AccountingUtils.get().formatPrice(item.getBalance()) : "";
            }
        };
        columnList.add(balanceColumn);


        return columnList.toArray(new ColumnDefinitionConfig[columnList.size()]);
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
