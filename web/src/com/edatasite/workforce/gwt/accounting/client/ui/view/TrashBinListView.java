package com.edatasite.workforce.gwt.accounting.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrashBinListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/13
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrashBinListView extends BaseListView implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel listingPanel;

    public TrashBinListView() {
        super(TRASH_BIN, wfmStrings.trashBin());
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel(ListPanelType.TrashBinListPanel, getColumns(), getListProvider(), getListDesign());
        add(listingPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRASH_BIN_SAVED, TrashBinListView.this, (sender, args) -> listingPanel.reloadPage());

        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        columns[0] = new ColumnDefinitionConfig<TrashBinListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TrashBinListItem item) {
                MenuPopItem restoreItem = new MenuPopItem(wfmStrings.restore(), "icon-task-small");
                restoreItem.ensureDebugId("restore");
                restoreItem.setCommand(() -> AccountingService.App.get().changeTrashBinStatus(item.getObjectID(), Constants.TRASH_BIN_RESTORED, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        listingPanel.reloadPage();
                    }
                }));

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> AccountingService.App.get().changeTrashBinStatus(item.getObjectID(), Constants.TRASH_BIN_REMOVED, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        listingPanel.reloadPage();
                    }
                }));

                MenuBar menuBar = new MenuBar(true);
                menuBar.addItem(restoreItem);
                menuBar.addItem(removeItem);

                ToolItem toolItem = new ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<TrashBinListItem, String>(wfmStrings.reference(), "reference", 150) {
            @Override
            public String getCellValue(TrashBinListItem item) {
                return item.getReference() != null ? item.getReference() : "";
            }
        };
        columns[1].setMinimumColumnWidth(150);
        columns[1].setMaximumColumnWidth(200);

        columns[2] = new ColumnDefinitionConfig<TrashBinListItem, String>(wfmStrings.type(), "type", 100) {
            @Override
            public String getCellValue(TrashBinListItem item) {
                if (SALE_QUOTE.equals(item.getType())) {
                    return wfmStrings.salesQuote();
                } else if (SALE_ORDER.equals(item.getType())) {
                    return accountingStrings.salesOrder();
                } else if (SALE_INVOICE.equals(item.getType())) {
                    return wfmStrings.salesInvoice();
                } else if (RECURRING_INVOICE.equals(item.getType())) {
                    return accountingStrings.recurringInvoice();
                } else if (PURCHASE_ORDER.equals(item.getType())) {
                    return wfmStrings.purchaseorder();
                } else if (PURCHASE_INVOICE.equals(item.getType())) {
                    return wfmStrings.purchaseinvoice();
                }
                return "";
            }
        };
        columns[2].setMinimumColumnWidth(100);
        columns[2].setMaximumColumnWidth(120);

        return columns;
    }

    private ListingRequestProvider getListProvider() {
        return (ListingRequestProvider<TrashBinListItem>) (filterParametrs, listingCallback) -> AccountingService.App.get().getTrashBinList(filterParametrs, new AsyncCallback<ListResult<TrashBinListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                listingCallback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<TrashBinListItem> listResult) {
                listingCallback.onSuccess(listResult);
            }
        });
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage("Currently you have no items");
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;
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
        return TRASH_BIN;
    }
}
