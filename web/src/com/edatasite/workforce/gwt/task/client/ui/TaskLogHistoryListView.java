package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class TaskLogHistoryListView extends BaseListView implements Constants {
    protected Integer entityID;
    protected ListingPanel<HistoryItem> list;

    public TaskLogHistoryListView(String name, Integer entityID) {
        super(name, wfmStrings.logHistory());
        this.entityID = entityID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.TaskHistoryListView, getColumns(), getRequestProvider(), getPanelDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, TaskLogHistoryListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_LIST_EDIT_CELL, TaskLogHistoryListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, TaskLogHistoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;
        //Field ID
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.fieldName(), HistoryItem.FIELD_ID, 80) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getField() != null ? rowValue.getField() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);
        //From
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.oldValue(), HistoryItem.FROM, 80) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getColumnValue(true);
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);
        //To
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.newValue(), HistoryItem.TO, 80) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getColumnValue(false);
            }
        };
        column.setMinimumColumnWidth(80);
        column.setColumnSortable(false);
        columns.add(column);

        //User
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.modifiedBy(), HistoryItem.MODIFIED_BY, 130) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getUserName() != null ? rowValue.getUserName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //Date
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.modifiedDate(), HistoryItem.MODIFIED_DATE, 80) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getUpdatedDate() != null ? DateUtils.formatInternal(rowValue.getUpdatedDate()) : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    protected ListingPanelDesign getPanelDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.DATE;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>(2);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.TaskHistoryList;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noUpdatesText());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    protected ListingRequestProvider<HistoryItem> getRequestProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setEntityID(entityID);
            TaskService.App.get().getTaskUpdatesList(listingFilterParameter, new AsyncCallback<ListResult<HistoryItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<HistoryItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "employee employee-list";
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
}
