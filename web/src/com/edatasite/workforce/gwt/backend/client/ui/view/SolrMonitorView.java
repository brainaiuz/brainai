package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.SolrMonitorRpc;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
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
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/03/12
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */
public class SolrMonitorView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<SolrMonitorRpc> monitorListingPanel;

    public SolrMonitorView() {
        super("solrMonitor", backendStrings.solrMonitor());
    }

    public String getIconStyle() {
        return "backend solrMonitorView";
    }

    @Override
    protected Widget onInitialize() {
        monitorListingPanel = new ListingPanel<>(ListPanelType.SolrMonitorListPanel, getColumns(), getReqestProvider(), getListingDesign());
        add(monitorListingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SolrMonitorRpc value) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem optimizeItem = new MenuPopItem("Optimize", "icon-taskdoc-small");
                optimizeItem.setCommand(() -> {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(backendStrings.areYouSureYouWantToOptimize() + " " + value.getCoreName());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            BackendService.App.get().optimizeSolrCore(value.getCoreName(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void aVoid) {
                                    LoadingPanel.loading(false);
                                    Info.show(value.getCoreName().toUpperCase() + " " + backendStrings.hasBeenOptimized(), Info.Type.INFO);
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuBar.addItem(optimizeItem);
                ToolItem toolItem = new ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, SimpleLink>(backendStrings.solrCoreName(), SolrMonitorRpc.CORE_NAME, 120) {
            @Override
            public SimpleLink getCellValue(SolrMonitorRpc value) {
                return new SimpleLink(value.getCoreName().toUpperCase(), "solrCoreComanyList|solrCompanyList/" + value.getCoreId() + "/" + value.getCoreName());
            }
        };
        columnConfig.addStyleAttribute("font-weight", "bold");
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, Integer>(backendStrings.numberDocs(), SolrMonitorRpc.NUM_DOCS, 80) {
            @Override
            public Integer getCellValue(SolrMonitorRpc value) {
                return value.getNumDocs();
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, String>(backendStrings.coreSize(), SolrMonitorRpc.CORE_SIZE, 80) {
            @Override
            public String getCellValue(SolrMonitorRpc value) {
                return value.getFileSize();
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, String>(wfmStrings.startTime(), SolrMonitorRpc.START_TIME, 80) {
            @Override
            public String getCellValue(SolrMonitorRpc value) {
                return DateUtils.formatInternal(value.getStartTime());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SolrMonitorRpc, String>(wfmStrings.modifiedDate(), SolrMonitorRpc.LAST_MODIFIED, 80) {
            @Override
            public String getCellValue(SolrMonitorRpc value) {
                return DateUtils.formatInternal(value.getLastModified());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<SolrMonitorRpc> getReqestProvider() {
        return (filterParametrs, callback) -> BackendService.App.get().getSolrMonitorStatistic(filterParametrs, new AsyncCallback<ListResult<SolrMonitorRpc>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<SolrMonitorRpc> solrMonitorRpc) {
                callback.onSuccess(solrMonitorRpc);
            }
        });
    }

    private ListingPanelDesign getListingDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }
        };
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
