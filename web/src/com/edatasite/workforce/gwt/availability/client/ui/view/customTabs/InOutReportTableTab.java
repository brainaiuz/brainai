package com.edatasite.workforce.gwt.availability.client.ui.view.customTabs;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 12/14/11
 * Time: 4:01 PM
 */
public class InOutReportTableTab extends Composite {

    private KpiDataGrid<InOutItem> dataGrid;
    private ListDataProvider<InOutItem> dataProvider;
    private Integer int_employeeID;
    private ColumnSortEvent.ListHandler<InOutItem> listHandler;
    private ListingFilterParameter filterParameter;
    private DataListBox viewAsListBox;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Date date;
    private MaterialPanel panel = new MaterialPanel("pg_leave__io");
    private MaterialPanel content = new MaterialPanel("pg_leave__io-panel");


    public static final ProvidesKey<InOutItem> KEY_PROVIDER = item -> {
        return item == null ? null : item.getDateName();//
    };

    public InOutReportTableTab(String tabName, Integer int_employeeID) {
        this.int_employeeID = int_employeeID == null ? Utils.getUserID() : int_employeeID;

        HTML iotitle = new HTML(tabName);
        iotitle.addStyleName("pg_leave__io-panel--title");
        panel.add(iotitle);

        initData();
        initWidget(panel);
    }

    public void initData() {
        filterParameter = new ListingFilterParameter();
        viewAsListBox = new DataListBox();
        ReportService.App.get().getUserMaxRolesWithMEM(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] result) {
                viewAsListBox.setWithoutNullLabel(true);
                viewAsListBox.setItems(result);
                viewAsListBox.setSelected(result[0].getId());
            }
        });
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setSize("100%", "100%");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.thereAreNoAnyInOutDataYet(), "", null));
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        addDataDisplay(dataGrid);
        viewShow();
        content.add(dataGrid);
        panel.add(content);
    }

    public void viewShow() {
        drawing();
        initTableColumns();
    }

    public void drawing() {
        if (viewAsListBox.getSelectedItem() != null) {
            filterParameter.setViewAsId(viewAsListBox.getSelectedItem().getId());
        }
        if (date == null) {
            date = new Date();
        }
        filterParameter.setStartDate(DateUtil.getMonthFirstDay(date));
        filterParameter.setEndDate(DateUtil.getMonthLastDate(date));

        Date fromDate = filterParameter.getStartDate();
        Date toDate = DateUtil.getDayLastTime(filterParameter.getEndDate());
        Integer viewAsId = filterParameter.getViewAsId();
        boolean showDate = true;
        boolean showCheckIn = true;
        boolean showCheckOut = true;
        boolean showActualInHours = true;

        AllInOneService.App.get().getInOutReportItems(int_employeeID, fromDate, toDate, new AbstractAsyncCallback<InOutItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(InOutItem[] result) {
                dataProviderApply(result);
                dataProvider.refresh();
            }
        });
    }

    private void addDataDisplay(HasData<InOutItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void dataProviderApply(InOutItem[] result) {
        List<InOutItem> inOutItems = dataProvider.getList();
        inOutItems.clear();
        Collections.addAll(inOutItems, result);
    }

    private void initTableColumns() {
        //Date
        Column<InOutItem, String> date = new Column<InOutItem, String>(new TextCell()) {
            @Override
            public String getValue(InOutItem object) {
                try {
                    return object.getDateName() != null ? DateUtils.parseFromOneToAnotherFormat(object.getDateName(), DateUtils.getDateFormatShort(), DateUtils.format)+Utils.getHijriDate(DateUtils.parse(object.getDateName(), DateUtils.getDateFormatShort())) : "";
                } catch (DateFormatException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return "";
            }
        };
        date.setSortable(true);
        dataGrid.addColumn(date, wfmStrings.date());
        dataGrid.setColumnWidth(date, 25, Style.Unit.PCT);
        listHandler.setComparator(date, (o1, o2) -> o2.getDateName().compareToIgnoreCase(o1.getDateName()));
        //Check In
        Column<InOutItem, SafeHtml> checkInHours = new Column<InOutItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(InOutItem object) {
                return SafeHtmlUtils.fromTrustedString(getHours(object.getInHours()).getHTML());
            }
        };
        checkInHours.setSortable(false);
        dataGrid.addColumn(checkInHours, wfmStrings.checkedIn());
        dataGrid.setColumnWidth(checkInHours, 20, Style.Unit.PCT);
        //Check Out
        Column<InOutItem, SafeHtml> checkOutHours = new Column<InOutItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(InOutItem object) {
                return SafeHtmlUtils.fromTrustedString(getHours(object.getOutHours()).getHTML());
            }
        };
        checkOutHours.setSortable(false);
        dataGrid.addColumn(checkOutHours, wfmStrings.checkedOut());
        dataGrid.setColumnWidth(checkOutHours, 20, Style.Unit.PCT);

        //duration
        Column<InOutItem, SafeHtml> duration = new Column<InOutItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(InOutItem object) {
                return SafeHtmlUtils.fromTrustedString(getHours(object.getDuration()).getHTML());
            }
        };
        dataGrid.addColumn(duration, wfmStrings.duration());
    }

    private String getFormattedHour(String hour) {
        int hr;
        int mn;
        String formattedHour = "00:00";
        if (hour != null) {
            try {
                hr = Integer.parseInt(hour) / 60;
                mn = Integer.parseInt(hour) % 60;
                String hourStr = Integer.toString(hr);
                if (hourStr.length() < 2) {
                    hourStr = "0" + hourStr;
                }
                String minutesStr = Integer.toString(mn);
                if (minutesStr.length() < 2) {
                    minutesStr = "0" + minutesStr;
                }
                formattedHour = hourStr + ":" + minutesStr;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return hour;
            }
        }
        return formattedHour;
    }

    private HTML getHours(String[] hours) {
        StringBuilder text = new StringBuilder();
        if (hours != null && hours.length > 0) {
            for (String hour : hours) {
                if (hour != null) {
                    text.append(hour).append("<br>");
                } else {
                    text.append(" - : -&nbsp;&nbsp;<br>");
                }
            }
        }
        return new HTML(text.toString().isEmpty() ? " - : -&nbsp;&nbsp;" : text.toString());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}