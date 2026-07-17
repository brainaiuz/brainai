package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingReportsImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 12:29:55
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementListView extends BaseListView {

    private static final AccountingReportsImageBundles reportsImageBundle = AccountingReportsImageBundles.App.get();

    private static final AccountingStrings accountStrings = AccountingStrings.App.get();

    private ListingPanel<BankStatementListItem> list;

    private Integer bankAccountID;

    public BankStatementListView(Integer bankAccountID) {
        super("bankStatements", accountStrings.bankStatements());
        this.bankAccountID = bankAccountID;
    }

    public BankStatementListView(Integer bankAccountID,String bankName) {
        super("bankStatements", bankName);
        this.bankAccountID = bankAccountID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getDisagn());
        add(list);
        list.reloadPage();
        return null;
    }

    private ListingRequestProvider<BankStatementListItem> getListingRequestProvider() {
        return (filterParametrs, listingCallback) -> AccountingService.App.get().getBankAccountStatements(bankAccountID, filterParametrs, new AsyncCallback<ListResult<BankStatementListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                listingCallback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<BankStatementListItem> result) {
                listingCallback.onSuccess(result);
            }
        });
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
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage(accountStrings.noBankStatementsMessage()));
            }
        };

    }


    private ListPanelType getPanelType() {
        return ListPanelType.BankStatementListPanel;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        columns[0] = new ColumnDefinitionConfig<BankStatementListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final BankStatementListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem viewItemsList = new MenuPopItem(accountStrings.viewItems());
                viewItemsList.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementItemList|bankStatementItems/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(viewItemsList);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[1] = new ColumnDefinitionConfig<BankStatementListItem, Widget>(wfmStrings.status(), BankStatementListItem.STATUS, 70) {

            @Override
            public Widget getCellValue(BankStatementListItem item) {
                return (item.isReconciled() ? new Image(reportsImageBundle.reconciled()) : new Image(reportsImageBundle.notReconciled()));
            }
        };

        columns[2] = new ColumnDefinitionConfig<BankStatementListItem, String>(accountStrings.importedDate(), BankStatementListItem.IMPORTED_DATE, 100) {

            @Override
            public String getCellValue(BankStatementListItem item) {
                return item.getImportedDate() != null ? DateUtils.format(item.getImportedDate()) : "";
            }
        };

        columns[3] = new ColumnDefinitionConfig<BankStatementListItem, String>(wfmStrings.startDate(), BankStatementListItem.START_DATE, 100) {

            @Override
            public String getCellValue(BankStatementListItem item) {
                return item.getStartDate() != null ? DateUtils.format(item.getStartDate()) : "";
            }
        };

        columns[4] = new ColumnDefinitionConfig<BankStatementListItem, String>(wfmStrings.endDate(), BankStatementListItem.END_DATE, 100) {

            @Override
            public String getCellValue(BankStatementListItem item) {
                return item.getEndDate() != null ? DateUtils.format(item.getEndDate()) : "";
            }
        };

        columns[5] = new ColumnDefinitionConfig<BankStatementListItem, String>(accountStrings.startBalance(), BankStatementListItem.START_BALANCE, 100) {

            @Override
            public String getCellValue(BankStatementListItem item) {
                return item.getStartBalance() != null ? AccountingUtils.get().formatPrice(item.getStartBalance()) : "";
            }
        };

        columns[6] = new ColumnDefinitionConfig<BankStatementListItem, String>(accountStrings.endBalance(), BankStatementListItem.END_BALANCE, 100) {

            @Override
            public String getCellValue(BankStatementListItem item) {
                return item.getEndBalance() != null ? AccountingUtils.get().formatPrice(item.getEndBalance()) : "";
            }
        };

        return columns;
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
