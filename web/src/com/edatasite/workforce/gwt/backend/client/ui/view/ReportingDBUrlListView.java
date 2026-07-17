package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingDBUrlListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingDBUrlListView extends BaseListView {

    private ListingPanel<ReportingDBUrlListItem> listingTable;
    private final BackendStrings backendStrings = BackendStrings.App.get();

    public ReportingDBUrlListView(String description) {
        super("reportingDBUrlListView", description);
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(null, getColumnConfig(), getRequestDataProvider(), getDesignCode());
        add(listingTable);
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.payAttantionForImportantPage());
        messageBox.open();
        return null;
    }

    private ListingPanelDesign getDesignCode() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> getActionLink(null));
                return addNew;
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

    private void getActionLink(Integer parameter) {
        SinksContainerFactory.entryPoint.onHistoryChanged("reportingdburl|add/add" + (parameter == null ? "" : "/" + parameter));
    }

    private ListingRequestProvider<ReportingDBUrlListItem> getRequestDataProvider() {
        return (filterParametrs, callback) -> {
            CoreService.App.get().getReportDBUrlList(filterParametrs, new AsyncCallback<ListResult<ReportingDBUrlListItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.error());
                }

                @Override
                public void onSuccess(ListResult<ReportingDBUrlListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<ReportingDBUrlListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ReportingDBUrlListItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit());
                edit.setCommand(() -> getActionLink(rowValue.getId()));
                menuBar.addItem(edit);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                delete.setCommand(() -> {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo);
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            CoreService.App.get().deleteReportingDBUrl(rowValue.getId(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                }

                                @Override
                                public void onSuccess(Void aVoid) {
                                    listingTable.reloadPage();
                                }
                            });
                        }
                    });
                    messageBox.center();
                });
                menuBar.addItem(delete);

                final ToolItem toolItem = new ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ReportingDBUrlListItem, String>(backendStrings.dbUrl(), "dburl", 100) {
            @Override
            public String getCellValue(ReportingDBUrlListItem rowValue) {
                return rowValue.getDbUrl();
            }
        };
        columns[1].setMinimumColumnWidth(40);

        columns[2] = new ColumnDefinitionConfig<ReportingDBUrlListItem, String>(wfmStrings.username(), "username", 50) {
            @Override
            public String getCellValue(ReportingDBUrlListItem rowValue) {
                return rowValue.getUserName();
            }
        };
        columns[2].setMinimumColumnWidth(40);

        columns[3] = new ColumnDefinitionConfig<ReportingDBUrlListItem, String>(wfmStrings.password(), "password", 50) {
            @Override
            public String getCellValue(ReportingDBUrlListItem rowValue) {
                return rowValue.getPassword();
            }
        };
        columns[3].setMinimumColumnWidth(40);

        return columns;
    }

    @Override
    public String getIconStyle() {
        return "backend reportListView";
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
