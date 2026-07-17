package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.HasSettings;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

import java.util.Date;

public class DayViewBody extends Composite {

    private FlexTable layout = new FlexTable();
    private ScrollPanel scrollPanel = new ScrollPanel();
    private DayViewTimeline timeline = null;
    private DayViewGrid grid = null;
    private AbsolutePanel absolutePanel = null;
    private HasSettings settings = null;

    public void add(Widget w) {
        scrollPanel.add(w);
    }

    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }

    public DayViewGrid getGrid() {
        return grid;
    }

    public DayViewTimeline getTimeline() {
        return timeline;
    }

    public DayViewGrid getDayViewGrid() {
        return grid;
    }

    public DayViewTimeline getDayViewTimeline() {
        return timeline;
    }

    public DayViewBody(HasSettings settings, AbsolutePanel absolutePanel) {
        initWidget(scrollPanel);
        this.settings = settings;
        this.timeline = new DayViewTimeline(settings);
        this.absolutePanel = absolutePanel;
        this.grid = new DayViewGrid(settings);
        scrollPanel.setStylePrimaryName("scroll-area");

        DOM.setStyleAttribute(scrollPanel.getElement(), "overflowX", "hidden");
        DOM.setStyleAttribute(scrollPanel.getElement(), "overflowY", "scroll");

        layout.setCellPadding(0);
        layout.setBorderWidth(0);
        layout.setCellSpacing(0);

        String width = "99%";

        if (Utils.isOpera()) {
            width = (Window.getClientWidth() - 255) + "px";
        }
        layout.getColumnFormatter().setWidth(1, width);
        // set vertical alignment
        VerticalAlignmentConstant valign = HasVerticalAlignment.ALIGN_TOP;
        layout.getCellFormatter().setVerticalAlignment(0, 0, valign);
        layout.getCellFormatter().setVerticalAlignment(0, 1, valign);

        // grid.build(8, 17, 1);
        grid.setStyleName("gwt-appointment-panel");

        layout.getCellFormatter().setWidth(0, 0, "60px");
        DOM.setStyleAttribute(layout.getElement(), "tableLayout", "fixed");

        absolutePanel.add(grid);
        layout.setWidget(0, 0, timeline);
        layout.setWidget(0, 1, absolutePanel);
        scrollPanel.add(layout);
    }

    public void setDays(Date date, int days) {
        grid.build(settings.getSettings().getWorkingHourStart(), settings.getSettings().getWorkingHourEnd(), days);
    }

    private int scrollPosition = 0;

    @Override
    public void onDetach() {
        super.onDetach();
        scrollPosition = scrollPanel.getElement().getScrollTop();
    }

    @Override
    public void onAttach() {
        super.onAttach();
        if (scrollPanel != null) {
            scrollPanel.getElement().setScrollTop(scrollPosition);
        }
    }
}
