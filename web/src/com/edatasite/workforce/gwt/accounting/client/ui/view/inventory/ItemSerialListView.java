package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class ItemSerialListView extends BaseListView implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    ListingPanel<SerialItem> listingPanel;
    private final Integer productId;

    public ItemSerialListView(Integer productId) {
        super("serialnumber", accountingStrings.serialNumbers());
        this.productId = productId;
    }

    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.SerialNumberListPanel, getColumns(), getListingRequestProvider(), getListingPanelDesign());
        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];

        columns[0] = new ColumnDefinitionConfig<SerialItem, SimpleLink>(wfmStrings.number(), "number", 130) {
            @Override
            public SimpleLink getCellValue(SerialItem item) {
                SimpleLink link = new SimpleLink(item.getNumber());
                link.addClickHandler(clickEvent -> new ItemSerialView(item.getId(), item.getNumber()).open());
                return link;
            }
        };
        columns[0].setColumnSortable(true);
        columns[0].setMinimumColumnWidth(100);

        columns[1] = new ColumnDefinitionConfig<SerialItem, CheckBox>(wfmStrings.used(), "used", 130) {
            @Override
            public CheckBox getCellValue(SerialItem item) {
                if (item.getUsed()) {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setValue(true);
                    checkBox.setEnabled(false);
                    return checkBox;
                } else {
                    return null;
                }
            }
        };
        columns[1].setColumnSortable(false);
        columns[1].setMinimumColumnWidth(100);
        return columns;
    }

    private ListingRequestProvider<SerialItem> getListingRequestProvider() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setProductId(productId);
            ItemSerialService.App.get().getAllSerials(fp, new AsyncCallback<ListResult<SerialItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<SerialItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
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

            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
