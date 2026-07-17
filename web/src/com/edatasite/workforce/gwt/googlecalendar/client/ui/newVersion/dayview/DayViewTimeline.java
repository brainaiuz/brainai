package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.HasSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.FormattingUtil;
import com.google.gwt.user.client.ui.*;

/**
 * The Timeline Class is a sequential display of the hours in a day. Each
 * hour label should visually line up to a cell in the DayGrid.
 */
public class DayViewTimeline extends Composite {

    private static final String TIME_LABEL_STYLE = "hour-label";

    private AbsolutePanel timelinePanel = new AbsolutePanel();
    private HasSettings settings;
    private final String[] HOURS = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
            "12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
    private final String AM = " AM";
    private final String PM = " PM";

    public DayViewTimeline(HasSettings settings) {
        initWidget(timelinePanel);
        timelinePanel.setStylePrimaryName("time-strip");
        this.settings = settings;
        prepare();
    }

    public void prepare() {
        timelinePanel.clear();
        float labelHeight = settings.getSettings().getIntervalsPerHour() * settings.getSettings().getPixelsPerInterval();

        int i = 0;
        if (settings.getSettings().isOffsetHourLabels()) {
            i = 1;
            SimplePanel sp = new SimplePanel();
            sp.setHeight((labelHeight / 2) + "px");
            timelinePanel.add(sp);
        }

        while (i < HOURS.length) {
            String hour = HOURS[i];
            i++;

            //block
            SimplePanel hourWrapper = new SimplePanel();
            hourWrapper.setStylePrimaryName(TIME_LABEL_STYLE);

            hourWrapper.setHeight((labelHeight + FormattingUtil.getBorderOffset()) + "px");

            FlowPanel flowPanel = new FlowPanel();
            flowPanel.setStyleName("hour-layout");

            Label hourLabel = new Label(hour);
            hourLabel.setStylePrimaryName("hour-text");
            flowPanel.add(hourLabel);

            String amPm = "";
            if (i < 13) {
                amPm = AM;
            } else if (i >= 13) {
                amPm = PM;
            }

            Label ampmLabel = new Label(amPm);
            ampmLabel.setStylePrimaryName("ampm-text");
            flowPanel.add(ampmLabel);

            hourWrapper.add(flowPanel);

            timelinePanel.add(hourWrapper);
        }
    }
}