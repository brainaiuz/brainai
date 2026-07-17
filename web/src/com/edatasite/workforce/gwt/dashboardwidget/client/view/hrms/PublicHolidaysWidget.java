package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.AvailableLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.HolidayRPC;
import com.google.gwt.i18n.client.DateTimeFormat;
import gwt.material.design.client.ui.html.DD;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.DT;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublicHolidaysWidget extends DashboardBaseWidget {
    public PublicHolidaysWidget(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void getData() {
        setTitle(wfmStrings.publicHolidays());
        contentPanel.clear();
        DashboardWidgetService.App.get().getCompanyHolidaysForUser(new AbstractAsyncCallback<HolidayRPC>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(HolidayRPC result) {
                contentPanel.clear();
                if (result.getHolidays() != null && result.getHolidays().size() > 0) {
                    drawWidget(result);
                } else {
                    noData();
                }
            }
        });
    }

    private void drawWidget(HolidayRPC result) {
        Div wrapper = new Div("gwt-wrapper");
        Div content = new Div("widget-content");
        Div holidays = new Div("holidays");
        contentPanel.add(wrapper);
        wrapper.add(content);
        content.add(holidays);
        Date current = result.getServerDate().getNonConvertedDate();
        String[] monthNames = new String[]{wfmStrings.january(), wfmStrings.february(),
                wfmStrings.march(), wfmStrings.april(), wfmStrings.may(),
                wfmStrings.june(), wfmStrings.july(), wfmStrings.august(),
                wfmStrings.september(), wfmStrings.october(), wfmStrings.november(),
                wfmStrings.december()};
        DateTimeFormat dayF = DateTimeFormat.getFormat("d");
        DateTimeFormat ddMMF = DateTimeFormat.getFormat("d MMM");
        boolean soon = false;
        List<DL> beforeHolidays = new ArrayList<>();
        List<DL> afterHolidays = new ArrayList<>();
        for (AvailableLeaveRequest holiday : result.getHolidays()) {
            boolean passed = false;
            Date from = holiday.getFromNonConvertable().getNonConvertedDate();
            DL dl = new DL();
            DT dt = new DT();
            DD dd = new DD();
            if (!soon && from.after(current)) {
                dl.addStyleName("holidays__upcoming");
                soon = true;
            }
            if (from.before(current)) {
                dl.addStyleName("holidays__passed");
            }

            dt.setText(dayF.format(from));
            dd.setText(monthNames[from.getMonth()]);
            dl.add(dt);
            dl.add(dd);
            DD name = new DD();
            name.addStyleName("holidays__label");
            name.setText(holiday.getHolidayName());
            dl.add(name);
            if (from.after(current)) {
                afterHolidays.add(dl);
            }
            if (from.before(current)) {
                beforeHolidays.add(dl);
            }
        }
        for (DL dl : afterHolidays) {
            holidays.add(dl);
        }
        for (int i = beforeHolidays.size() - 1; i >= 0; i--) {
            holidays.add(beforeHolidays.get(i));
        }
    }

    @Override
    protected void getSampleData(boolean nodata) {
        setTitle(wfmStrings.publicHolidays());
        HolidayRPC result = new HolidayRPC();
        ArrayList<AvailableLeaveRequest> holidays = new ArrayList<>();
        DateTimeFormat df = DateTimeFormat.getFormat("ddMM");
        holidays.add(createHoliday(df.parse("0101"), accountingStrings.newYearsDay()));
        holidays.add(createHoliday(df.parse("1501"), accountingStrings.republicDay()));
        holidays.add(createHoliday(df.parse("0105"), accountingStrings.dayOfMemoryAndHonor()));
        holidays.add(createHoliday(df.parse("2805"), accountingStrings.springBankHoliday()));
        holidays.add(createHoliday(df.parse("0106"), accountingStrings.internationalChildrensDay()));
        holidays.add(createHoliday(df.parse("0109"), accountingStrings.independenceDay()));
        holidays.add(createHoliday(df.parse("2612"), accountingStrings.boxingDay()));
        holidays.add(createHoliday(df.parse("3112"), accountingStrings.newYearsEve()));
        result.setHolidays(holidays);
        result.setServerDate(new DateNonConvertable(new Date()));
        drawWidget(result);
    }

    private AvailableLeaveRequest createHoliday(Date date, String name) {
        AvailableLeaveRequest holiday = new AvailableLeaveRequest();
        holiday.setFromNonConvertable(new DateNonConvertable(date));
        holiday.setHolidayName(name);
        return holiday;
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.HOLIDAY;
    }
}
