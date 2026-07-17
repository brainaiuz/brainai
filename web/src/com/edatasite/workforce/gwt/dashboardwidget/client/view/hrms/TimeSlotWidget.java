package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import gwt.material.design.client.ui.html.*;

import java.util.Date;

public class TimeSlotWidget extends DashboardBaseWidget {

    public TimeSlotWidget(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void getData() {
        DashboardWidgetService.App.get().getEmpTimeslot(new AbstractAsyncCallback<TimeslotItem>() {
            @Override
            public void failure(Throwable throwable) {
                noData();
            }

            @Override
            public void success(TimeslotItem timeSlot) {
                contentPanel.clear();
                if (timeSlot != null) {
                    contentPanel.add(drawWidget(timeSlot));
                } else {
                    noData();
                }
            }
        });
    }

    private Table drawWidget(TimeslotItem timeSlot) {
        setTitle(wfmStrings.myTimeslot());
        int dow = new Date().getDay();
        TData[] data = new TData[]{new TData(wfmStrings.monday(), timeSlot.getMonday(), dow == 1),
                new TData(wfmStrings.tuesday(), timeSlot.getTuesday(), dow == 2),
                new TData(wfmStrings.wednesday(), timeSlot.getWednesday(), dow == 3),
                new TData(wfmStrings.thursday(), timeSlot.getThursday(), dow == 4),
                new TData(wfmStrings.friday(), timeSlot.getFriday(), dow == 5),
                new TData(wfmStrings.saturday(), timeSlot.getSaturday(), dow == 6),
                new TData(wfmStrings.sunday(), timeSlot.getSunday(), dow == 7)};
        Div wrapper = new Div("gwt-wrapper");
        Div content = new Div("widget-content");
        Table table = new Table();
        table.addStyleName("timeslot");
        TableHead thead = getThead(data);
        TableBody tableBody = getTBody(data);
        table.add(thead);
        table.add(tableBody);
        return table;
    }

    private TableHead getThead(TData[] data) {
        TableHead thead = new TableHead();
        TableRow tr = new TableRow();

        for (TData dow : reorderWeekDays(data)) {
            TableHeadCell th = new TableHeadCell(dow.getWeekDay());
            if (dow.isToday()) {
                th.addStyleName("current");
                th.add(new Span(wfmStrings.today()));
            }
            tr.add(th);
        }
        thead.add(tr);
        return thead;
    }

    private TableBody getTBody(TData[] data) {
        TableBody tbody = new TableBody();
        TableRow startRow = new TableRow();
        for (TData datum : data) {
            startRow.add(createCell(datum, 0));
        }
        tbody.add(startRow);
        TableRow endRow = new TableRow();
        for (TData datum : data) {
            endRow.add(createCell(datum, 1));
        }
        tbody.add(endRow);
        return tbody;
    }

    private TableDataCell createCell(TData datum, int startEnd) {
        TableDataCell cell = new TableDataCell();
        if (datum.isToday()) {
            cell.addStyleName("current");
        }
        String value = "";
        if (datum.getStartEnd()[0] == datum.getStartEnd()[1]) {
            value = "—";
            cell.addStyleName("day-off");
        } else {
            value = formatNumber(datum.getStartEnd()[startEnd] / 60) + ":" + formatNumber(datum.getStartEnd()[startEnd] % 60);
        }
        cell.setText(value);
        return cell;
    }

    private TData[] reorderWeekDays(TData[] array) {
        int weekStartDate = Integer.valueOf(Utils.userSettings.get(Constants.OVERALL_DATE_PICKER_WEEK_START));
        int shift = 0;
        if (weekStartDate == 1) {
            shift = 1;
        } else if (weekStartDate != 1 && weekStartDate != 2) {
            shift = 2;
        }

        // Array shifting
        shift = 7 - shift;
        int i, j, k;
        TData temp;
        for (i = 0; i < gcd(shift, 7); i++) {
            /* move i-th values of blocks */
            temp = array[i];
            j = i;
            while (true) {
                k = j + shift;
                if (k >= 7)
                    k = k - 7;
                if (k == i)
                    break;
                array[j] = array[k];
                j = k;
            }
            array[j] = temp;
        }


        return array;
    }

    int gcd(int a, int b) {
        if (b == 0)
            return a;
        else
            return gcd(b, a % b);
    }

    @Override
    protected void getSampleData(boolean nodata) {
        TimeslotItem timeSlot = new TimeslotItem();
        timeSlot.setMonday(new int[]{480, 1020});
        timeSlot.setTuesday(new int[]{480, 1020});
        timeSlot.setWednesday(new int[]{480, 1020});
        timeSlot.setThursday(new int[]{480, 1020});
        timeSlot.setFriday(new int[]{480, 1020});
        timeSlot.setSaturday(new int[]{480, 720});
        timeSlot.setSunday(new int[]{0, 0});
        contentPanel.add(drawWidget(timeSlot));
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.TIMESLOT;
    }

    private String formatNumber(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        return String.valueOf(time);
    }

    private class TData {
        private String weekDay;
        private int[] startEnd;
        private boolean today;

        public TData(String weekDay, int[] startEnd, boolean today) {
            this.weekDay = weekDay;
            this.startEnd = startEnd;
            this.today = today;
        }

        public int[] getStartEnd() {
            return startEnd;
        }

        public void setStartEnd(int[] startEnd) {
            this.startEnd = startEnd;
        }

        public String getWeekDay() {
            return weekDay;
        }

        public void setWeekDay(String weekDay) {
            this.weekDay = weekDay;
        }

        public boolean isToday() {
            return today;
        }

        public void setToday(boolean today) {
            this.today = today;
        }
    }

}
