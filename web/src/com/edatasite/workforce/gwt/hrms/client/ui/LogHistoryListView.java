package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Hurshid on 8/16/2017.
 */
public class LogHistoryListView extends BaseListView implements Constants {
    private final Integer entityID;
    private ListingPanel<HistoryItem> list;

    public LogHistoryListView(Integer entityID) {
        super(EMPLOYEE_UPDATES_LIST, wfmStrings.logHistory());
        this.entityID = entityID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.EmployeeHistoryListView, getColumns(), getRequestProvider(), getPanelDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_PROFILE_UPDATE, LogHistoryListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_LIST_EDIT_CELL, LogHistoryListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, LogHistoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;
        //Contact Name
        if (entityID == null) {
            column = new ColumnDefinitionConfig<HistoryItem, SimpleLink>(wfmStrings.employeeCode(), HistoryItem.ENTITY_CODE, 80) {
                @Override
                public SimpleLink getCellValue(HistoryItem rowValue) {
                    return getLink(rowValue.getEmployeeCode(), "employeeProfile|employeeProfileView/" + rowValue.getUserID(), rowValue.getEmployeeCode());
                }
            };
            column.setMinimumColumnWidth(100);
            column.setShow(true);
            columns.add(column);

            column = new ColumnDefinitionConfig<HistoryItem, SimpleLink>(wfmStrings.employee(), HistoryItem.ENTITY_NAME, 130) {
                @Override
                public SimpleLink getCellValue(HistoryItem rowValue) {
                    return getLink(rowValue.getEntityName(), "employeeProfile|employeeProfileView/" + rowValue.getUserID(), rowValue.getEmployeeCode(), rowValue.getEmployeeCode());
                }
            };
            column.setMinimumColumnWidth(130);
            column.setShow(true);
            columns.add(column);
        }

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

    private ListingPanelDesign getPanelDesigner() {
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
                        ArrayList<String> fields = new ArrayList<>(entityID == null ? 3 : 2);
                        if (entityID == null) {
                            fields.add(ListingChooseFilter.EMPLOYEE_LOOKUP);
                        }
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.EmployeeHistoryList;
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

    private ListingRequestProvider<HistoryItem> getRequestProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setEntityID(entityID);
            HrmsService.App.get().getEmployeeUpdatesList(listingFilterParameter, new AsyncCallback<ListResult<HistoryItem>>() {
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

