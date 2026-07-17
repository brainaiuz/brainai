package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.agenda;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarWidget;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.PublicShortAppointmentView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ShortAppointmentView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.AppointmentUtil;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

public class AgendaView extends CalendarView {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String DAY_CELL_CONTAINER_STYLE = "day-cell-container";
    private AbsolutePanel dayPanel = new AbsolutePanel();
    private MaterialCollapsible collapsible;

    private MaterialCollapsibleItem dayItem;
    private MaterialCollapsibleBody dayBody;
    private MaterialPanel footerPanel;
    private Span loadingbar;

    /**
     * width of grid
     */
    private String width = null;

    /**
     * Adapter class that maps an Appointment to the widgets (DIV's, etc) that represent
     * it on the screen. This is necessary because a single appointment is represented by
     * many widgets. For example, an appointment is represented by a title widget,
     * a description widget, and has a "get more details" label.
     * <p/>
     * By mapping an appointment to these widgets we can easily figure out which
     * appointment the user is interacting with as they click around the AgendaView.
     */
    class AgendaViewAppointmentAdapter {

        private Widget titleLabel;
        private Widget detailsLabel;
        private Appointment appointment;
        private Widget detailsPanel;

        public AgendaViewAppointmentAdapter(Widget titleLabel, Widget detailsPanel, Widget detailsLabel, Appointment appointment) {
            this.titleLabel = titleLabel;
            this.detailsLabel = detailsLabel;
            this.detailsPanel = detailsPanel;
            this.appointment = appointment;
        }

        public Widget getTitleLabel() {
            return titleLabel;
        }

        public Widget getDetailsLabel() {
            return detailsLabel;
        }

        public Appointment getAppointment() {
            return appointment;
        }

        public Widget getDetailsPanel() {
            return detailsPanel;
        }
    }

    class AppointmentDetailPanel extends Composite {

        private Div row = new Div();

        public AppointmentDetailPanel(SimplePanel detailContainer, final Appointment appointment) {
            initWidget(detailContainer);

            row.add(drawTimeDiv(appointment));
            row.add(drawTextDiv(appointment));
            row.setStyleName("updates-row updates-cat--added");
            if (new Date().after(appointment.getStartDate())) {
                row.addStyleName("updates-row--passed");
            }

            // add the detail widget
            detailContainer.setStyleName("detailContainer");
            detailContainer.addStyleName(appointment.getStyle());
            AbsolutePanel detailDecorator = new AbsolutePanel();
            detailDecorator.setStyleName("detailDecorator");
            detailContainer.setVisible(false);
            detailContainer.add(detailDecorator);

            if (appointment.getLocation() != null && !appointment.getLocation().isEmpty()) {
                AbsolutePanel whereRow = new AbsolutePanel();
                InlineLabel whereHeader = new InlineLabel((Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) ? wfmStrings.project() : wfmStrings.where() + ": ");
                whereHeader.setStyleName("detailHeader");
                whereRow.add(whereHeader);
                whereRow.add(new InlineLabel(appointment.getLocation()));
                detailDecorator.add(whereRow);
            }

            if (appointment.getCreatedBy() != null && !appointment.getCreatedBy().isEmpty() && !isPublic()) {
                AbsolutePanel creatorRow = new AbsolutePanel();
                InlineLabel creatorHeader = new InlineLabel(wfmStrings.createdBy() + ": ");
                creatorHeader.setStyleName("detailHeader");
                creatorRow.add(creatorHeader);
                creatorRow.add(new InlineLabel(appointment.getCreatedBy()));
                detailDecorator.add(creatorRow);
            }

//            title.setText(appointment.getSubject());
            row.addClickHandler(event -> {
                if (isPublic()) {
                    final PublicShortAppointmentView appointmentView = calendarWidget.initPublicShortAppointmentView(appointment, isBookable());
                    appointmentView.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
                        int left = row.getAbsoluteLeft();
                        int top = row.getAbsoluteTop() + row.getOffsetHeight() + 3;

                        if (left + offsetWidth > Window.getClientWidth()) {
                            left -= offsetWidth + 3;
                        }

                        if (top + offsetHeight > Window.getClientHeight()) {
                            top = row.getAbsoluteTop() - offsetHeight - 3;
                        }

                        appointmentView.setPosition(left, top);
                    });
                } else {
                    final ShortAppointmentView appointmentView = calendarWidget.initShortAppointmentView(appointment);
                    appointmentView.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
                        int left = row.getAbsoluteLeft();
                        int top = row.getAbsoluteTop() + row.getOffsetHeight() + 3;

                        if (left + offsetWidth > Window.getClientWidth()) {
                            left -= offsetWidth + 3;
                        }

                        if (top + offsetHeight > Window.getClientHeight()) {
                            top = row.getAbsoluteTop() - offsetHeight - 3;
                        }

                        appointmentView.setPosition(left, top);
                    });
                }
            });
            detailDecorator.add(row);
        }

        public Div getRow() {
            return row;
        }
    }

    /**
     * FlexTable used to display a list of appointments.
     */
    private FlexTable appointmentGrid = new FlexTable();

    /**
     * DateTime format used when displaying an appointments start and end time.
     */
    private static final DateTimeFormat DEFAULT_TIME_FORMAT = DateTimeFormat.getShortTimeFormat();//DateUtils.getFormatInternal().getShortTimeFormat();

    /**
     * Style used to format this view.
     */
    private String styleName = "agenda";

    /**
     * Adds the calendar view to the calendar widget and performs required formatting.
     */
    public void attach(CalendarWidget widget) {
        super.attach(widget);

        dayPanel.setStyleName(DAY_CELL_CONTAINER_STYLE);
        Label dayLabel = new Label(wfmStrings.schedule());
        dayLabel.setStylePrimaryName("agenda__header");
        dayPanel.add(dayLabel);
        calendarWidget.getRootPanel().add(dayPanel);

        Div body = new Div();
        appointmentGrid.setCellPadding(0);
        appointmentGrid.setCellSpacing(0);
        appointmentGrid.setBorderWidth(0);
        body.add(appointmentGrid);
        body.addStyleName("scrollable-table widget--updates-links");
        calendarWidget.getRootPanel().add(body);
    }

    /**
     * Gets the style name associated with this particular view
     *
     * @return Style name.
     */
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void doLayout() {
        appointmentGrid.clear();
        for (int i = appointmentGrid.getRowCount() - 1; i >= 0; i--) {
            appointmentGrid.removeRow(i);
        }

        //Get the start date, make sure time is 0:00:00 AM
        Date tmpDate = DateUtil.minusDays(DateUtil.resetTime((Date) calendarWidget.getDate().clone()), 1);
        Date today = DateUtil.resetTime(new Date());


        int row = 0;
        row = drawDays(tmpDate, today, row, 3);

        if (appointmentGrid.getRowCount() == 0) {
            DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEEE, MMMM dd");
            Date endDate = DateUtil.addDays(calendarWidget.getDate(), calendarWidget.getDays() - 1);
            appointmentGrid.setHTML(0, 0, "<i>" + Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.youHaveNoEventsInFollowingDateRange(), wfmStrings.events()) + " <b>[" +
                    dateFormat.format(calendarWidget.getDate()) + " ; " + dateFormat.format(endDate) + "]</b>.</i>");

            appointmentGrid.getCellFormatter().setHeight(0, 0, "30px");
            appointmentGrid.getCellFormatter().setStyleName(0, 0, "no-appointmets");
            appointmentGrid.getRowFormatter().setStyleName(row, "row");
        }
    }

    private int drawDays(Date tmpDate, Date today, int row, int days) {
        for (int i = 0; i < days; i++) {
            collapsible = new MaterialCollapsible();
            collapsible.addStyleName("collapsible--arrows-left updates-list");
            collapsible.getElement().getStyle().setMargin(0, Style.Unit.PX);
            collapsible.setAccordion(false);

            dayItem = new MaterialCollapsibleItem();
            MaterialCollapsibleHeader todayHeader = new MaterialCollapsibleHeader();
            dayBody = new MaterialCollapsibleBody();
            Heading todayH3 = new Heading(HeadingSize.H3);
            Span todaySpan = new Span();
            todayH3.add(todaySpan);
            todayHeader.add(todayH3);

            dayItem.add(todayHeader);
            dayItem.add(dayBody);
            collapsible.add(dayItem);

            dayItem.getBody().setDisplay(Display.BLOCK);

            // Filter the list by date
            ArrayList<Appointment> filteredList = AppointmentUtil.getListByDateRange(calendarWidget.getAppointments(), tmpDate);

            if (filteredList != null && filteredList.size() > 0) {

                todaySpan.setText(CalendarSettings.DEFAULT_DATE_FORMAT.format(tmpDate));

                //If a Row represents the current date (Today) then we style it differently
                if (tmpDate.equals(today) || tmpDate.after(today)) {
                    dayItem.addStyleName("active");
                    dayItem.getHeader().addStyleName("active");
                } else {
                    dayItem.getBody().getElement().getStyle().setDisplay(Style.Display.NONE);
                }

                for (final Appointment appointment : filteredList) {
                    Div row1 = drawRow(appointment);

                    dayBody.add(row1);
                    appointmentGrid.setWidget(row, 0, collapsible);

                    row++;
                }
            }

            // increment the date
            tmpDate = DateUtil.addDays(tmpDate, 1);
        }

        footerPanel = new MaterialPanel("widget-footer");
        loadingbar = new Span();
        loadingbar.setStyleName("blue widget-loading--svg widget-loading");
        loadingbar.setVisible(false);

        WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
        moreButton.getElement().setInnerText(wfmStrings.loadMore());
        int finalRow = row;
        Date finalTmpDate = tmpDate;
        moreButton.addClickHandler(clickEvent -> {
            loadingbar.setVisible(true);
            drawDays(finalTmpDate, today, finalRow, 5);
            loadingbar.setVisible(false);
        });
        footerPanel.add(loadingbar);
        footerPanel.add(moreButton);
        appointmentGrid.setWidget(row, 0, footerPanel);
        return row;
    }

    public Div drawRow(Appointment appointment) {
        SimplePanel detailContainerPanel = new SimplePanel();
        AppointmentDetailPanel detailContainer = new AppointmentDetailPanel(detailContainerPanel, appointment);

        return detailContainer.getRow();
    }

    private static Div drawTimeDiv(Appointment appointment) {
        Div timeDiv = new Div("updates-row__time");
        Span dateSpan = new Span();
        Span timeSpan = null;
        if (appointment.getStartDate() != null) {
            dateSpan.setText(DateUtils.getTimeFormatShort(appointment.getStartDate()));
        }
        timeDiv.add(dateSpan);
        if (timeSpan != null) {
            timeDiv.add(timeSpan);
        }
        Span pointSpan = new Span();
        pointSpan.setStyleName("updates-row__time-point");
        timeDiv.add(pointSpan);
        return timeDiv;
    }

    private Div drawTextDiv(Appointment appointment) {
        Div textDiv = new Div("updates-row__text");
        Div titleDiv = new Div("updates-row__title");
        Div infoDiv = new Div("updates-row__info");

        Label title = new Label(appointment.getSubject());
        titleDiv.add(title);
        textDiv.add(titleDiv);

        Span infoSpan = new Span();
        infoSpan.setText(appointment.getDescription());
        infoDiv.add(infoSpan);
        textDiv.add(infoDiv);

        return textDiv;
    }

    @Override
    public void onDoubleClick(Element element, Event event) {

    }

    @Override
    public void onSingleClick(Element element, Event event) {

    }

    @Override
    public void onAppointmentSelected(Appointment appt) {

    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
