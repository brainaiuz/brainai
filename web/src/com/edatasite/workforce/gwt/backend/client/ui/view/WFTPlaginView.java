package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.WFTPlaginListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 16:34:20
 * To change this template use File | Settings | File Templates.
 */
public class WFTPlaginView extends BaseListView {

    private WFTPlaginViewPanel plaginViewPanel;
    private ListingPanel<WFTPlaginListItem> listPanel;
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public WFTPlaginView() {
        super("wftPlagin", backendStrings.wftPlugin());
    }

    public void refresh() {
        listPanel.reloadPage();
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.WFTPluginListPanel, drawColumns(), provider(), designer());
        plaginViewPanel = new WFTPlaginViewPanel(this);
        listPanel.setQuickViewPanel(plaginViewPanel);
        super.setQuickViewPanel(plaginViewPanel);
        super.setListingPanel(listPanel);
        super.display();
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<WFTPlaginListItem, String>(backendStrings.plaginName(), WFTPlaginListItem.PLUGIN_NAME, 90) {

            @Override
            public String getCellValue(WFTPlaginListItem item) {
                return item.getPlaginName();
            }
        };
        columns[1] = new ColumnDefinitionConfig<WFTPlaginListItem, String>(backendStrings.version(), WFTPlaginListItem.VERSION, 90) {

            @Override
            public String getCellValue(WFTPlaginListItem item) {
                return item.getPlaginVersion() != null ? item.getPlaginVersion() : "";
            }
        };
        /*columns[1].setColumnSortable(false);*/

        columns[2] = new ColumnDefinitionConfig<WFTPlaginListItem, String>(backendStrings.updateDate(), WFTPlaginListItem.UPDATE_DATE, 90) {

            @Override
            public String getCellValue(WFTPlaginListItem item) {
                return item.getUpdateDate() != null ? "" + item.getUpdateDate() : "";
            }
        };
        /*columns[2].setColumnSortable(false);*/
        columns[3] = new ColumnDefinitionConfig<WFTPlaginListItem, String>(wfmStrings.modifiedBy(), WFTPlaginListItem.UPDATER_NAME, 100) {

            @Override
            public String getCellValue(WFTPlaginListItem item) {
                return item.getUpdaterName() != null ? item.getUpdaterName() : "";
            }
        };
        /*columns[3].setColumnSortable(false);*/

        return columns;
    }

    private ListingRequestProvider<WFTPlaginListItem> provider() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            BackendService.App.get().getPlagins(fp, new AsyncCallback<ListResult<WFTPlaginListItem>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<WFTPlaginListItem> Result) {
                    callback.onSuccess(Result);
                }
            });
        };

    }

    private ListingPanelDesign designer() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    @Override
    public String getIconStyle() {
        return "backend wftPlaView";
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
