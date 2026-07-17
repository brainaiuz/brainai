package com.edatasite.workforce.gwt.task.client.ui.view.tabpanels;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamAssigneeItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.edatasite.workforce.gwt.task.client.ui.view.WorkstreamSummaryView;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * User: Ilhombek
 * Date: Apr 6, 2011
 * Time: 5:00:36 PM
 */
public class WorkstreamAssigneesTab extends CustomTabWidget {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final WorkstreamSingleItem workstream;
    private WorkstreamSummaryView parentView;
    private KpiDataGrid<WorkstreamAssigneeItem> dataGrid;
    private ListDataProvider<WorkstreamAssigneeItem> dataProvider;


    public WorkstreamAssigneesTab(String tabName, WorkstreamSingleItem workstream) {
        super(tabName);
        this.workstream = workstream;
    }

    public static final ProvidesKey<WorkstreamAssigneeItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    private void initTableColumns() {
        // Name
        Column<WorkstreamAssigneeItem, String> name = new Column<WorkstreamAssigneeItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(WorkstreamAssigneeItem item) {
                return refactor(item.getName());
            }
        };
        name.setFieldUpdater((index, item, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("employee|summary/" + item.getEmployeeID()));
        dataGrid.addColumn(name, wfmStrings.name());
        dataGrid.setColumnWidth(name, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        if (!Utils.hasRole(Constants.CLIENT)) {
            //EstimatedTime
            Column<WorkstreamAssigneeItem, String> estimatedTime = new Column<WorkstreamAssigneeItem, String>(new TextCell()) {
                @Override
                public String getValue(WorkstreamAssigneeItem file) {
                    return refactor(Utils.formatMinutes(file.getTime()));
                }
            };
            dataGrid.addColumn(estimatedTime, wfmStrings.estimatedTime());
            dataGrid.setColumnWidth(estimatedTime, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        }

        if (!Utils.hasRole(Constants.CLIENT)) {
            //ActualTime
            Column<WorkstreamAssigneeItem, String> actualTime = new Column<WorkstreamAssigneeItem, String>(new TextCell()) {
                @Override
                public String getValue(WorkstreamAssigneeItem file) {
                    return refactor(Utils.formatMinutes(file.getActualSpentTime()));
                }
            };
            dataGrid.addColumn(actualTime, wfmStrings.actualTime());
            dataGrid.setColumnWidth(actualTime, 15, com.google.gwt.dom.client.Style.Unit.PCT);
        }

        //Percent
        Column<WorkstreamAssigneeItem, SafeHtml> percent = new Column<WorkstreamAssigneeItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(WorkstreamAssigneeItem file) {
                return SafeHtmlUtils.fromTrustedString(parentView.getPercentCompletedPanel(String.valueOf(file.getPercent())).toString());
            }
        };
        dataGrid.addColumn(percent, wfmStrings.percent());
        dataGrid.setColumnWidth(percent, 30, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void emptyTableWidget() {
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.thereAreNoAssigneesForThisWorkstream(), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
    }

    public void addDataDisplay(HasData<WorkstreamAssigneeItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void supplyProvider(WorkstreamAssigneeItem[] caseEmails) {
        List<WorkstreamAssigneeItem> emailList = dataProvider.getList();
        emailList.clear();
        Collections.addAll(emailList, caseEmails);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public void setParentView(WorkstreamSummaryView parentView) {
        this.parentView = parentView;
    }

    @Override
    public void viewShow() {
        WorkstreamAssigneeItem[] involvedMembers = workstream.getAssignees();
        emptyTableWidget();
        dataProvider.refresh();
        supplyProvider(involvedMembers);
    }
}