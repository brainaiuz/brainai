package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.view.OvalPanel;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Apr 9, 2011
 * Time: 1:00:17 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class is specified for showing view for new appointment and for existing
 * appointment. It checks the title of the appointment and iether shows add new
 * view or existing view depending on the title's value.
 */
public class PublicShortAppointmentView {

    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();

    private Appointment appointment;
    private FlexTable details;
    private PopupPanel popup;
    private Label venueTime;
    private boolean isBookable = false;

    public PublicShortAppointmentView(Appointment appointment, boolean isBookable) {
        this.appointment = appointment;
        this.isBookable = isBookable;

        initialize();

        drawAppointmentView();
    }

    /**
     * ***************************************** General parts of the views *********************************
     */

    private void initialize() {
        popup = new PopupPanel(true, true);
        popup.setStyleName("cal-popup");
        popup.setAnimationEnabled(true);
        venueTime = new Label();

        details = new FlexTable();
        details.setBorderWidth(0);
    }

    private void drawOvalPanel(Panel panel) {
        OvalPanel ovalPanel = new OvalPanel();
        ovalPanel.add(panel);
        ovalPanel.addCloseButtonClickHandler(event -> popup.hide());

        popup.setWidget(ovalPanel);

        setAppointmentAndShow(appointment);
    }

    public void setAppointmentAndShow(Appointment newAppointment) {
        this.appointment = newAppointment;

        DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEE, MMMM dd");
        DateTimeFormat timeFormat = DateTimeFormat.getShortTimeFormat();


        String startTime = timeFormat.format(appointment.getStartDate()).toLowerCase();
        String endTime = "";
        if (appointment.getEndDate() != null) {
            endTime = timeFormat.format(appointment.getEndDate()).toLowerCase();
        }

        final DateTimeFormat shortDateFormat = DateTimeFormat.getShortDateFormat();
        if (appointment.isMultiDay()) {
            String start = DateUtils.formatInternal(appointment.getStartDate());// + "," + startTime;
            String end = DateUtils.formatInternal(appointment.getEndDate());// + "," + endTime;

            if (!Appointment.BLUE.equals(appointment.getStyle()) && !(Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) && !Appointment.RED.equals(appointment.getStyle())) {
                start = DateUtils.format(appointment.getStartDate());
                end = DateUtils.format(appointment.getEndDate());
            }
            if (appointment.isAllDay()) {
                venueTime.setText(shortDateFormat.format(appointment.getStartDate()) + " - " + shortDateFormat.format(appointment.getEndDate()));
            } else {
                venueTime.setText(start + " - " + end);
            }
        } else {
            if (appointment.isAllDay()) {
                venueTime.setText(shortDateFormat.format(appointment.getStartDate()) /*DateUtils.format()*/);
            } else {
                venueTime.setText(shortDateFormat.format(appointment.getStartDate()) + "," + startTime + " - " + endTime);
            }
        }

        popup.show();

        //For the view of existing appointment whatText doesn't exist, so we should check it to null.
        if (whatText != null) {
            DeferredCommand.addCommand(() -> {
                whatText.setText(appointment.getSubject() != null ? appointment.getSubject() : "");
                whatText.setFocus(true);
            });
        }
    }

    public void closePopup() {
        popup.hide();
    }

    public void setPosition(int left, int top) {
        popup.setPopupPosition(left, top);
    }

    public void setPopupPositionAndShow(PopupPanel.PositionCallback callback) {
        popup.setPopupPositionAndShow(callback);
    }

    public void onClosePopup(CloseHandler<PopupPanel> closeHandler) {
        popup.addCloseHandler(closeHandler);
    }

    public void center() {
        popup.center();
    }

    /**
     * ************************************* Below is the drawing of the existing appointment view ******************
     */

    private final String eventDetailsStyle = "event-details";

    private void drawAppointmentView() {
        details.setWidth("100%");
        details.setCellSpacing(7);
        details.setCellPadding(7);

        FlexTable table = new FlexTable();
        table.setWidth("370px");
        String color = "#3366CC";  //Color of Event
        if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {
            color = "#0D7813";                // Color of Tasks
        } else if ((Appointment.ORANGE.equals(appointment.getStyle()))) {
            color = "#BE6D00";
        } else if ((Appointment.PINK.equals(appointment.getStyle()))) {
            color = "#AA3C3D";
        } else if ((Appointment.RED.equals(appointment.getStyle()))) {
            color = "#E82223";
        } else if ((Appointment.PURPLE.equals(appointment.getStyle()))) {
            color = "#453333";
        } else if ((Appointment.DARK_PURPLE.equals(appointment.getStyle()))) {
            color = "#025CA8";
        }
        details.setHTML(0, 0, "<b style='color:" + color + "'>" + appointment.getSubject() + "</b>");
        details.getFlexCellFormatter().setColSpan(0, 0, 2);

        details.setText(1, 0, wfmStrings.description() + ":");
        String description = appointment.getDescription();
        if (description == null || description.equals("")) {
            description = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.thereIsNoDescriptionForThisEvent(), wfmStrings.event()) + "</i>";
        }
        details.setHTML(1, 1, description);

        if (Appointment.ORANGE.equals(appointment.getStyle()) || Appointment.PURPLE.equals(appointment.getStyle()) || Appointment.RED.equals(appointment.getStyle())) {
            details.setText(2, 0, wfmStrings.period() + ":");
            details.setWidget(2, 1, venueTime);
        } else {
            details.setText(2, 0, wfmStrings.when() + ":");
            details.setWidget(2, 1, venueTime);
        }

        if (!Appointment.ORANGE.equals(appointment.getStyle()) && !Appointment.PURPLE.equals(appointment.getStyle()) &&
                !Appointment.DARK_PURPLE.equals(appointment.getStyle()) && !Appointment.RED.equals(appointment.getStyle())) {
            String location = appointment.getLocation();
            if (Appointment.GREEN.equals(appointment.getStyle()) || appointment.isTask()) {  //condition for task
                details.setText(3, 0, wfmStrings.project() + ":");
                if (location == null || location.equals("")) {
                    location = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.noLocationAppointedToThisEvent(), wfmStrings.event()) + "</i>";
                }
            } else {
                details.setText(3, 0, wfmStrings.where() + ":");
                if (location == null || location.equals("")) {
                    location = "<i>" + Property.get(Constants.EVENT_LIST, wfmStrings.noLocationAppointedToThisEvent(), wfmStrings.event()) + "</i>";
                }
            }
            details.setHTML(3, 1, location);
        }
        /* add shared user(s)*/
        if (appointment.getObjectID() != null) {
            calendarService.getEventSharedEmployees(appointment.getObjectID(), appointment.getCreatedBy(), new AbstractAsyncCallback<String>() {
                @Override
                public void success(String result) {
                    if (!result.equals("")) {
                        details.setHTML(4, 0, wfmStrings.sharedWith());
                        details.setText(4, 1, result);
                    }
                }
            });
        }

        details.getCellFormatter().setVerticalAlignment(1, 0, HasAlignment.ALIGN_TOP);
        details.getCellFormatter().setWordWrap(1, 1, true);

        int index = 5;

        // Attachments related
        final Integer newIndex = index++;
        calendarService.getEventAttachments(appointment.getObjectID(), new AbstractAsyncCallback<FileResource[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(FileResource[] fileResources) {
                VerticalPanel filesPanel = new VerticalPanel();
                filesPanel.setWidth("300px");
                StringBuilder fileNames = new StringBuilder("");
                if (fileResources != null && fileResources.length > 0) {
                    details.setHTML(newIndex, 0, "Attachments: ");
                    for (final FileResource file : fileResources) {
                        SimpleLink downloadLink = new SimpleLink(file.getName());
                        downloadLink.addClickHandler(sender -> {
                            if (file.getBodyId() != null) {
                                String action = file.getDownloadUrl();
//                                    if (file.getUploadType().equals(Constants.AMAZON)) {
//                                        action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadFile?id=" + file.getBodyId().toString();
//                                    } else {
//                                        action = file.getGoogleDownloadLink();
//                                    }
                                Window.open(action, "_blank", "");
                            }
                        });
                        filesPanel.add(downloadLink);
                    }
                    details.setWidget(newIndex, 1, filesPanel);
                }
            }
        });
        HorizontalPanel panel = new HorizontalPanel();
        panel.setWidth("100%");
        if (appointment.isBooking() && isBookable && new Date().before(appointment.getStartDate())) {
            SimpleLink bookLink = new SimpleLink(wfmStrings.book());
            bookLink.addClickHandler(clickEvent -> {
                popup.hide();
                new PublicBookingAppointmentView(appointment);
            });
            panel.add(bookLink);
            panel.setCellHorizontalAlignment(bookLink, HasAlignment.ALIGN_CENTER);
        }

        details.setWidget(index, 0, panel);
        details.getFlexCellFormatter().setColSpan(index, 0, 3);
        details.getCellFormatter().setStyleName(index, 0, eventDetailsStyle);
        table.getCellFormatter().setHorizontalAlignment(index++, 0, HasAlignment.ALIGN_RIGHT);

        table.setWidget(1, 0, details);
        table.getCellFormatter().setWordWrap(0, 0, true);

        drawOvalPanel(table);
    }

    private final String validationStyle = "x-form-invalid";

    private Button createEvent;
    private TextBox whatText;
    private SimpleLink eventDetails;
}