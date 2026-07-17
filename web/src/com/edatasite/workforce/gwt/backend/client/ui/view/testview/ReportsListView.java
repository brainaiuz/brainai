package com.edatasite.workforce.gwt.backend.client.ui.view.testview;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.ReportsListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 2/20/12
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsListView extends BaseListView {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<ReportsListItem> list = null;
    private static final BackendServiceAsync async = BackendService.Reporting.get();

    private Date testedDate;
    private SchemaLookUp schemaLookUp;

    public ReportsListView() {
        super("reportsListView", backendStrings.reportingTestCase());
    }

    @Override
    public String getIconStyle() {
        return "backend reportListView";
    }


    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.ReportsListPanel, getColumnConfig(), getRequestDataProvider(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        list.setExcelListener(clickEvent -> {
            String excelURL = "common/downloadLeaveRequestListExcel";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, fp);
        });

        add(list);
        this.testedDate = new Date();
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.payAttantionForImportantPage());
        messageBox.open();
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];
        columns[0] = new ColumnDefinitionConfig<ReportsListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, 30) {
            @Override
            public Anchor getCellValue(final ReportsListItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem run = new MenuPopItem(wfmStrings.run());
                run.setCommand(() -> {
                    list.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItem().getId());
                    HashSet<ReportsListItem> itemSet = new HashSet<>();
                    itemSet.add(rowValue);
                    run(itemSet);
                });
                menuBar.addItem(run);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMaximumColumnWidth(30);
        columns[0].setMinimumColumnWidth(30);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ReportsListItem, Integer>(backendStrings.reportID(), "reportID", 20) {
            @Override
            public Integer getCellValue(ReportsListItem rowValue) {
                return rowValue.getReportID();
            }
        };

        columns[2] = new ColumnDefinitionConfig<ReportsListItem, String>(backendStrings.reportTemplateName(), "viewName", 30) {
            @Override
            public String getCellValue(ReportsListItem rowValue) {
                return rowValue.getViewName();
            }
        };
        columns[2].setMaximumColumnWidth(30);
        columns[2].setColumnSortable(true);
        columns[3] = new ColumnDefinitionConfig<ReportsListItem, String>(wfmStrings.reportName(), "reportName", 100) {
            @Override
            public String getCellValue(ReportsListItem rowValue) {
                return rowValue.getName();
            }
        };
        columns[3].setMinimumColumnWidth(50);

        columns[4] = new ColumnDefinitionConfig<ReportsListItem, String>(wfmStrings.status(), "status", 30) {
            @Override
            public String getCellValue(ReportsListItem rowValue) {
                return ("");
            }
        };
        columns[4].setMaximumColumnWidth(30);

        columns[5] = new ColumnDefinitionConfig<ReportsListItem, Widget>(wfmStrings.report(), "reportLink", 30) {

            @Override
            public Widget getCellValue(ReportsListItem rowValue) {
                Label label = new Label();
                return label;
            }
        };
        columns[5].setMaximumColumnWidth(30);
        return columns;
    }

    private GuideListingPanelDesign getDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                topPanel.add(new HTML(backendStrings.testCompany() + ": "));
                topPanel.addStyleName("file--ReportListView operPanel-table");
                schemaLookUp = new SchemaLookUp();

                topPanel.add(schemaLookUp);
                ActionButton makeTest = new ActionButton(backendStrings.make(), ActionButton.Type.BUTTON);
                makeTest.setStyle("cursor: pointer");
                makeTest.addClickHandler(be -> {
                    if (schemaLookUp.getSelectedItem() != null) {
                        list.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItem().getId());
                        list.reloadPage();
                    }
                });
                topPanel.add(makeTest);
                ActionButton clear = new ActionButton(backendStrings.clean(), ActionButton.Type.BUTTON);
                clear.setStyle("cursor: pointer");

                clear.addClickHandler(be -> {
                    if (schemaLookUp.getSelectedItem() != null) {
                        list.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItem().getId());
                        list.getFilterParametrs().setCleanTheList(true);
                        list.reloadPage();
                    }
                });
                topPanel.add(clear);
                topPanel.add(new HTML("<br/>"));

                ActionButton run = new ActionButton(wfmStrings.run(), ActionButton.Type.BUTTON);
                run.setStyle("cursor: pointer");

                run.addClickHandler(be -> {
                    if (schemaLookUp.getSelectedItem() != null) {
                        list.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItem().getId());
                        if (list.getPagingScrollTable().getSelectedRowValues() != null && list.getPagingScrollTable().getSelectedRowValues().size() > 0) {
                            run(list.getPagingScrollTable().getSelectedRowValues());
                        }
                    }
                });

                topPanel.add(run);
                return topPanel;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage("No Data"));
            }
        };
    }

    private ListingRequestProvider<ReportsListItem> getRequestDataProvider() {
        return (filterParametrs, callback) -> {
            async.getReportTemplateList(false, filterParametrs, new AsyncCallback<ListResult<ReportsListItem>>() {

                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ListResult<ReportsListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private void run(HashSet<ReportsListItem> listItems) {
        LoadingPanel.loading(true);
        async.runReport(listItems, list.getFilterParametrs().getCompanyID(), testedDate, new AsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Object o) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.reports())), Info.Type.INFO);
                list.reloadPage();
            }
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
