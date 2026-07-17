package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 28, 2009
 * Time: 4:49:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugListSummaryView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<BugsPerEmployeesListItem> list;

    public BugListSummaryView() {
        super("bugSummary", backendStrings.summaryByEmployee());
    }

    public String getIconStyle() {
        return "backend bugListSummaryView";
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.BugsListSummaryLisstPanel, drawColumns(), provider(), getDesign());

        //for pdf version not show
        list.getPdfVersion().setVisible(false);

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadBugListSummaryViewExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            list.callListExcel(excelURL, filterParametrs);
        });

        BugListSummaryQuickView bugEmployeeQuickView = new BugListSummaryQuickView();

        list.setQuickViewPanel(bugEmployeeQuickView);
        super.setQuickViewPanel(bugEmployeeQuickView);
        super.setListingPanel(list);
        super.display();
        return null;
    }

    private ListingPanelDesign getDesign() {
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
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];
        //Assigned Employee Name
        columns[0] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, String>(wfmStrings.employee(), BugsPerEmployeesListItem.EMPLOYEE, 120) {
            @Override
            public String getCellValue(BugsPerEmployeesListItem item) {
                return item.getEmployee();
            }
        };
        //Status NEW BUG
        columns[1] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(wfmStrings.New(), BugsPerEmployeesListItem.STATUS_NEW, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getStatusNew();
            }
        };
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Status RESOLVED BUG
        columns[2] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(backendStrings.resolved(), BugsPerEmployeesListItem.STATUS_RESOLVED, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getResolved();
            }
        };
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Status UNDER INVESTIGATION BUG
        columns[3] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(backendStrings.underInvestigation(), BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getUnderInvest();
            }
        };
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Status IN PROGRESS BUG
        columns[4] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(wfmStrings.inProgress(), BugsPerEmployeesListItem.STATUS_IN_PROGRESS, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getInProgress();
            }
        };
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Status IGNORED BUG
        columns[5] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(backendStrings.ignored(), BugsPerEmployeesListItem.STATUS_IGNORED, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getIgnored();
            }
        };
        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Status DONE BUG
        columns[6] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(wfmStrings.done(), BugsPerEmployeesListItem.STATUS_DONE, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getDone();
            }
        };
        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Total BUG
        columns[7] = new ColumnDefinitionConfig<BugsPerEmployeesListItem, Integer>(wfmStrings.total(), BugsPerEmployeesListItem.TOTAL_BUG, 100) {
            @Override
            public Integer getCellValue(BugsPerEmployeesListItem item) {
                return item.getTotal();
            }
        };
        columns[7].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        return columns;
    }

    private ListingRequestProvider<BugsPerEmployeesListItem> provider() {
        return (filterParametrs, callback) -> {
            BackendServiceAsync backendService = BackendService.App.get();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            backendService.getBugsPerEmployees(filterParametrs, new AbstractAsyncCallback<ListResult<BugsPerEmployeesListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<BugsPerEmployeesListItem> result) {
                    callback.onSuccess(result);
                }
            });
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