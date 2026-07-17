package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogItem;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogList;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetApplySave;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetSaveCallback;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetSaveListCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 30.03.11
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */

public class RecurrenceLogListView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    private ListingPanel<RecurrenceLogItem> listingTable;
    private final String name;
    private Date startDate = new Date();
    private Date endDate = new Date();


    public ListingFilterParameter getFiterParametrs() {
        return null;
    }

    public RecurrenceLogListView(String name, String description) {
        super(name, description);
        this.name = name;
    }

    public String getIconStyle() {
        switch (name) {
            case "loadedRecurrenceLog":
                return "backend recLogListView";
            case "firedRecurrenceLog":
                return "backend firedRecurrenceLog";
            case "lateRecurrenceLog":
                return "backend lateRecurrenceLog";
            default:
                return null;
        }
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.RecurrenceLogPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[7];
        int index = 0;

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(wfmStrings.jobType(), RecurrenceLogItem.JOBTYPE, 150) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getJobType();
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(80);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.cronExpression(), RecurrenceLogItem.CRONEXPRESSION, 150) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getCronExpression();
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(120);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.normalFireTime(), RecurrenceLogItem.NORMALFIRETIME, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getNormalFireTime() != null ? dateFormat.format(rowValue.getNormalFireTime()) : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(100);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.lateFireTime(), RecurrenceLogItem.LATEFIRETIME, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getLateFireTime() != null ? dateFormat.format(rowValue.getLateFireTime()) : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(100);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.isFired(), RecurrenceLogItem.ISFIRED, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getFired() != null ? rowValue.getFired().toString() : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(100);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.recurrenceID(), RecurrenceLogItem.RECURRENCEID, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getRecurrenceID() != null ? rowValue.getRecurrenceID().toString() : "";
            }
        };
        columnConfigs[index++].setMinimumColumnWidth(100);

        columnConfigs[index] = new ColumnDefinitionConfig<RecurrenceLogItem, String>(backendStrings.companyID(), RecurrenceLogItem.COMPANYID, 120) {
            public String getCellValue(RecurrenceLogItem rowValue) {
                return rowValue.getCompanyID() != null ? rowValue.getCompanyID().toString() : "";
            }
        };
        columnConfigs[index].setMinimumColumnWidth(100);

        return columnConfigs;
    }

    private FacetApplySave getApplySave() {
        return new FacetApplySave() {
            public void getSaveFilterListItem(final FacetSaveListCallback saveCallback) {
            }

            public void saveFilter(FacetFilterRpc facetFilterRpc, final FacetSaveCallback saveCallback) {
            }

            public void applyFilter(FacetFilterRpc facetFilter) {
                listingTable.getFilterParametrs().setFacetFilter(facetFilter);
                listingTable.reloadPage();
            }

            public void deleteFilter(Integer deleleFilterId, AsyncCallback<Void> callback) {
            }
        };
    }

    private ListingRequestProvider<RecurrenceLogItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            if ("loadedRecurrenceLog".equals(name)) {
                BackendService.App.get().getRecurrenceJobItems(filterParametrs, new AbstractAsyncCallback<RecurrenceLogList>() {
                    @Override
                    public void failure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void success(RecurrenceLogList result) {
                        callback.onSuccess(result);
                    }
                });
            } else {
                DateUtil.resetTime(startDate);
                DateUtil.getDayLastTime(endDate);
                filterParametrs.setFacetFilterJson(name);
                filterParametrs.setStartDate(startDate);
                filterParametrs.setEndDate(endDate);
                BackendService.App.get().getRecurrenceHistory(filterParametrs, new AbstractAsyncCallback<RecurrenceLogList>() {
                    @Override
                    public void failure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void success(RecurrenceLogList result) {
                        callback.onSuccess(result);
                    }
                });
            }
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
//            public void initFacetFilter(ListingFacetFilter chooseFilter) {
//            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                if (!"loadedRecurrenceLog".equals(name)) {
                    final DatePicker fromDate = new DatePicker();
                    fromDate.setDate(new Date());
                    fromDate.getPopup().addPopupListener((sender, autoClosed) -> startDate = fromDate.getDate());

                    final DatePicker toDate = new DatePicker();
                    toDate.setDate(new Date());
                    toDate.getPopup().addPopupListener((sender, autoClosed) -> endDate = toDate.getDate());
                    ActionButton showButton = new ActionButton(wfmStrings.show());
                    showButton.addClickHandler(clickEvent -> listingTable.reloadPage());
                    topPanel.add(new HTML(wfmStrings.from() + ": "));
                    topPanel.add(fromDate);
                    topPanel.add(new HTML(wfmStrings.to() + ": "));
                    topPanel.add(toDate);
                    topPanel.add(showButton);
                }
                return topPanel;
            }

            public void initImportExportToolBarWidgets(ExportImportOption option) {
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.thereAreNoRecurrences());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
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
