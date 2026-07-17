package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.WindowUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

public class DayViewMultiDayBody extends Composite {

    private final int APPOINTMENT_PANEL_MAX_HEIGHT = 155;
    private final String TIMELINE_EMPTY_CELL_STYLE = "leftEmptyCell";
    private final String SCROLLBAR_EMPTY_CELL_STYLE = "rightEmptyCell";
    private final String DAY_CONTAINER_CELL_STYLE = "centerDayContainerCell";
    private final String SPLITTER_STYLE = "splitter";

    protected ScrollPanel scrollPanel = new ScrollPanel();
    protected AbsolutePanel grid = new AbsolutePanel();
    protected SimplePanel gridOverlay = new SimplePanel();
    private FlexTable header = new FlexTable();

    public DayViewMultiDayBody() {
        scrollPanel.add(header);

        initWidget(scrollPanel);

        this.addStyleName("multiDayBody__wrapper");
        header.setStyleName("multiDayBody");

        //insert two rows ... first row holds multi-day appointments
        // second row is just a splitter
        header.insertRow(0);
        header.insertRow(0);
        //insert 3 cells
        //1st cell is empty to align with the timeline
        //2nd cell holds appointments
        //3rd cell is empty, aligns with scrollbar
        header.insertCell(0, 0);
        header.insertCell(0, 0);
        header.insertCell(0, 0);

        //add panel to hold appointments
        header.setWidget(0, 1, grid);

        //set cell styles
        header.getCellFormatter().setStyleName(0, 0, TIMELINE_EMPTY_CELL_STYLE);
        header.getCellFormatter().setStyleName(0, 1, DAY_CONTAINER_CELL_STYLE);
        header.getCellFormatter().setStyleName(0, 2, SCROLLBAR_EMPTY_CELL_STYLE);
//        header.getCellFormatter().setWidth(0, 2, /*WindowUtils.getScrollBarWidth(true) + */"1px");

        //default grid to 30px height
        grid.setHeight("30px");

        header.getFlexCellFormatter().setColSpan(1, 0, 3);
        header.setCellPadding(0);
        header.setBorderWidth(0);
        header.setCellSpacing(0);

        AbsolutePanel splitter = new AbsolutePanel();
        splitter.setStylePrimaryName(SPLITTER_STYLE);
        header.setWidget(1, 0, splitter);
    }

    public void determineScrollBarWidth(int appointmentPanelHeight) {
        if (appointmentPanelHeight > APPOINTMENT_PANEL_MAX_HEIGHT) {
            header.getCellFormatter().setWidth(0, 2, "1px");
            scrollPanel.setHeight(APPOINTMENT_PANEL_MAX_HEIGHT + "px");
            scrollPanel.setAlwaysShowScrollBars(false);
        } else {
            header.getCellFormatter().setWidth(0, 2, "6px");
            scrollPanel.setHeight(appointmentPanelHeight + "px");
//            DOM.setStyleAttribute(scrollPanel.getElement(), "overflow", "hidden");
        }
    }

    public void setDays(int days) {
        grid.clear();
        float dayWidth = 100f / days;
        float dayLeft = 0f;

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
