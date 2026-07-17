package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 12:00 PM
 */

public class CurrentInterviewsTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<Appointment> dataGrid;
    private ListDataProvider<Appointment> dataProvider;

    public static final ProvidesKey<Appointment> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public CurrentInterviewsTab(String tabName) {
        super(tabName);
    }

    public void addDataDisplay(HasData<Appointment> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("100%", "100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmMessages.thereAreNoSomethingItemsYet(hrmsStrings.interviews().toLowerCase()), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
        supplyProvider(new Appointment[]{});
        dataProvider.refresh();
    }

    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_EVENT_ADD, CurrentInterviewsTab.this, (sender, args) -> fillInterviews());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, CurrentInterviewsTab.this, (sender, args) -> fillInterviews());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_DELETED, CurrentInterviewsTab.this, (sender, args) -> fillInterviews());
        fillInterviews();
    }

    private void fillInterviews() {
        RecruitmentService.App.get().getCurrentInterviews(new AbstractAsyncCallback<ArrayList<Appointment>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ArrayList<Appointment> result) {
                if (result != null) {
                    supplyProvider(result.toArray(new Appointment[]{}));
                    dataProvider.refresh();
                }
            }
        });
    }

    private void initTableColumns() {
        // Name
        Column<Appointment, String> candidateName = new Column<Appointment, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final Appointment object) {
                return object.getOwnerName() != null ? object.getOwnerName() : "";
            }
        };
        candidateName.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + object.getObjectID()));
        dataGrid.addColumn(candidateName, hrmsStrings.candidateName());
        dataGrid.setColumnWidth(candidateName, 20, Style.Unit.PCT);
        // Interview date
        Column<Appointment, String> interviewDate = new Column<Appointment, String>(new TextCell()) {
            @Override
            public String getValue(final Appointment object) {
                if (object.getCreatedDate() != null) {
                    if (object.isAllDay()) {
                        return DateUtils.format(object.getCreatedDate())+ Utils.getHijriDate(object.getCreatedDate());
                    } else {
                        return DateUtils.formatInternal(object.getCreatedDate())+ Utils.getHijriDate(object.getCreatedDate());
                    }
                }
                return "";
            }
        };
        dataGrid.addColumn(interviewDate, hrmsStrings.interviewDate());
        dataGrid.setColumnWidth(interviewDate, 15, Style.Unit.PCT);
        //shared employees
        Column<Appointment, String> employees = new Column<Appointment, String>(new TextCell()) {
            @Override
            public String getValue(Appointment object) {
                return object.getSharedEmployeesString() != null ? object.getSharedEmployeesString() : "";
            }
        };
        dataGrid.addColumn(employees, wfmStrings.assignees());
        dataGrid.setColumnWidth(employees, 15, Style.Unit.PCT);
    }

    private void supplyProvider(Appointment[] reportResults) {
        List<Appointment> tableses = dataProvider.getList();
        tableses.clear();
        dataGrid.setPageSize(200);
        Collections.addAll(tableses, reportResults);
    }
}