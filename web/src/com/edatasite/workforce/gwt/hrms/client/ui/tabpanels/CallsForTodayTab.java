package com.edatasite.workforce.gwt.hrms.client.ui.tabpanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/21/12
 * Time: 12:19 PM
 */

public class CallsForTodayTab extends CustomTabWidget {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<EventItem> dataGrid;
    private ListDataProvider<EventItem> dataProvider;

    public static final ProvidesKey<EventItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public CallsForTodayTab(String tabName) {
        super(tabName);
    }

    public void addDataDisplay(HasData<EventItem> display) {
        dataProvider.addDataDisplay(display);
    }

    @Override
    public void initData() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmMessages.thereAreNoSomethingItemsYet(hrmsStrings.callsForToday().toLowerCase()), "", null));
        dataGrid.getEmptyTableWidget().getElement().getStyle().setMarginTop(-50, com.google.gwt.dom.client.Style.Unit.PX);
        addDataDisplay(dataGrid);
        add(dataGrid);
        initTableColumns();
    }

    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CALENDAR_EVENT_ADD, CallsForTodayTab.this, (sender, args) -> fillCalls());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, CallsForTodayTab.this, (sender, args) -> fillCalls());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_DELETED, CallsForTodayTab.this, (sender, args) -> fillCalls());
        fillCalls();
    }

    private void fillCalls() {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setRelationType(null);
        filterParametrs.setEventType(Appointment.CALL_LOG);
        FacetFilterRpc facetFilter = new FacetFilterRpc();
        facetFilter.setStartDate(DateUtil.resetTime(new Date()));
        facetFilter.setEndDate(DateUtil.getDayLastTime(new Date()));
        facetFilter.setFilterChanges(true);
        facetFilter.setType(ListPanelType.EventsListPanel);
        filterParametrs.setFacetFilter(facetFilter);
        filterParametrs.setCreatedFrom(Appointment.FROM_HRMS);
        filterParametrs.setLimit(20);
        CRMService.App.get().getEventList(filterParametrs, new AbstractAsyncCallback<ListResult<EventItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<EventItem> result) {
                if (result != null && result.getList() != null) {
                    supplyProvider(result.getList().toArray(new EventItem[]{}));
                    dataProvider.refresh();
                }
            }
        });
    }

    private void initTableColumns() {
        //Date Entered
        Column<EventItem, String> dateEntered = new Column<EventItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final EventItem object) {
                return object.getStartDate() != null ? DateUtils.format(object.getStartDate()) : "";
            }
        };
        dateEntered.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + object.getObjectID()));
        dataGrid.addColumn(dateEntered, hrmsStrings.dateEntered());
        dataGrid.setColumnWidth(dateEntered, 20, Style.Unit.PCT);
        //Summary
        Column<EventItem, String> summary = new Column<EventItem, String>(new TextCell()) {
            @Override
            public String getValue(final EventItem object) {
                return object.getSubject();
            }
        };
        dataGrid.addColumn(summary, wfmStrings.summaryView());
        dataGrid.setColumnWidth(summary, 15, Style.Unit.PCT);
        //Shared employees
        Column<EventItem, String> employees = new Column<EventItem, String>(new TextCell()) {
            @Override
            public String getValue(EventItem object) {
                return object.getSharedEmployeesString() != null ? object.getSharedEmployeesString() : "";
            }
        };
        dataGrid.addColumn(employees, wfmStrings.assignees());
        dataGrid.setColumnWidth(employees, 15, Style.Unit.PCT);
    }

    private void supplyProvider(EventItem[] reportResults) {
        List<EventItem> tableses = dataProvider.getList();
        tableses.clear();
        dataGrid.setPageSize(200);
        Collections.addAll(tableses, reportResults);
    }
}