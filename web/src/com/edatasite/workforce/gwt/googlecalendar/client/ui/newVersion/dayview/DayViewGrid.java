package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.HasSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.FormattingUtil;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The DayGrid draws the grid that displays days / time intervals in the
 * body of the calendar.
 */
public class DayViewGrid extends Composite {

    class Div extends ComplexPanel {

        public Div() {
            setElement(DOM.createDiv());
        }

        @Override
        public boolean remove(Widget w) {
            return super.remove(w);
        }

        @Override
        public void add(Widget w) {
            super.add(w, getElement());
        }
    }

    private static final int CELL_HEIGHT = 50;
    private static final String INTERVAL_MAJOR_STYLE = "major-time-interval";
    private static final String INTERVAL_MINOR_STYLE = "minor-time-interval";
    private static final String WORKING_HOUR_STYLE = "working-hours";

    protected ComplexPanel grid = new Div();
    protected SimplePanel gridOverlay = new SimplePanel();

    private HasSettings settings = null;

    private static final int HOURS_PER_DAY = 24;

    public DayViewGrid(HasSettings settings) {
        initWidget(grid);
        this.settings = settings;
    }

    public void build(int workingHourStart, int workingHourStop, int days) {
        grid.clear();

        int intervalsPerHour = settings.getSettings().getIntervalsPerHour();//2; //30 minute intervals
        float intervalSize = settings.getSettings().getPixelsPerInterval();

        this.setHeight((intervalsPerHour * intervalSize * HOURS_PER_DAY) + "px");

        float dayWidth = 100f / days;
        float dayLeft = 0f;

        for (int i = 0; i < HOURS_PER_DAY; i++) {
            boolean isWorkingHours = (i >= workingHourStart && i <= workingHourStop);

            //create major interval
            SimplePanel sp1 = new SimplePanel();
            sp1.setStyleName("major-time-interval");
            sp1.setHeight((intervalSize + FormattingUtil.getBorderOffset()) + "px");

            //if working hours set
            if (isWorkingHours) {
                sp1.addStyleName("working-hours");
            }

            //add to body
            grid.add(sp1);

            for (int x = 0; x < intervalsPerHour - 1; x++) {
                SimplePanel sp2 = new SimplePanel();
                sp2.setStyleName("minor-time-interval");

                sp2.setHeight((intervalSize + FormattingUtil.getBorderOffset()) + "px");
                if (isWorkingHours) {
                    sp2.addStyleName("working-hours");
                }
                grid.add(sp2);
            }
        }

        for (int day = 0; day < days; day++) {
            dayLeft = dayWidth * day;

            SimplePanel dayPanel = new SimplePanel();
            dayPanel.setStyleName("day-separator");
            grid.add(dayPanel);
            DOM.setStyleAttribute(dayPanel.getElement(), "left", dayLeft + "%");
        }

        gridOverlay.setSize("100%", "100%");
        DOM.setStyleAttribute(gridOverlay.getElement(), "position", "absolute");
        DOM.setStyleAttribute(gridOverlay.getElement(), "left", "0px");
        DOM.setStyleAttribute(gridOverlay.getElement(), "top", "0px");
        grid.add(gridOverlay);
    }
}