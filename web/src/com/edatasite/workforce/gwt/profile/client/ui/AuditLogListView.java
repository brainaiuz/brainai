package com.edatasite.workforce.gwt.profile.client.ui;

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
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * @author Hurshid on 3/18/2019
 */
public class AuditLogListView extends BaseListView implements Constants {
    private ListingPanel<HistoryItem> list;

    public AuditLogListView() {
        super(AUDIT_LOG_LIST, "Audit Log");
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AuditLogListView, getColumns(), getRequestProvider(), getPanelDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESLOT_EDIT, AuditLogListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HOLIDAY_EDIT, AuditLogListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_REASON_UPDATE, AuditLogListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;
        //Type ID
        column = new ColumnDefinitionConfig<HistoryItem, String>(wfmStrings.type(), HistoryItem.ENTITY_TYPE, 80) {
            @Override
            public String getCellValue(HistoryItem rowValue) {
                return rowValue.getEntityType();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

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
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.DATE;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>(3);
                        fields.add(ListingChooseFilter.EMPLOYEE_LOOKUP);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.AuditLogList;
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
        return (listingFilterParameter, listingCallback) -> HrmsService.App.get().getAuditLogList(listingFilterParameter, new AsyncCallback<ListResult<HistoryItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ListResult<HistoryItem> result) {
                listingCallback.onSuccess(result);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "cert certificate-icon";
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
