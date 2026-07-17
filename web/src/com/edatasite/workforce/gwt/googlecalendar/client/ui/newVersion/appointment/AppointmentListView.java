package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.Rectangle;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.WindowUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 2, 2010
 * Time: 3:42:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentListView {

    private final String closeButtonStyle = "close-button";
    private final String popupHeaderStyle = "on-cell-clicked";
    private final String backgroundStyle = "appointment-list-background";
    private final String listPanelStyle = "appointment-list";
    private final String appointmentParentStyle = "appointment-list-item__wrapper";
    private final String appointmentListItem = "appointment-list-item";
    private final String appointmentBorder = "appointment-list-border";

    private PopupPanel popup;
    private SaveAppointmentHandler saveHandler;
    private DeleteAppointmentHandler deleteHandler;
    private ArrayList<Appointment> appointments;

    private Date date;
    private int left;
    private int top;

    public AppointmentListView(ArrayList<Appointment> appointments, Date date, int left, int top) {
        this.appointments = appointments;
        this.date = date;
        this.left = left;
        this.top = top;

        show();
    }

    private void show() {
        Label closeButton = new Label();
        closeButton.setSize("12px", "12px");
        closeButton.setStyleName(closeButtonStyle);
        closeButton.addClickHandler(event -> popup.hide());

        HorizontalPanel headerPanel = new HorizontalPanel();
        headerPanel.setStyleName(popupHeaderStyle);
        headerPanel.setSize("100%", "15px");
        headerPanel.add(new HTML("<b>" + DateTimeFormat.getFormat("EEEE, MMMM dd").format(date) + "</b>"));
        headerPanel.add(closeButton);
        headerPanel.setCellHorizontalAlignment(closeButton, HasAlignment.ALIGN_RIGHT);

        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName(listPanelStyle);

        for (final Appointment appointment : appointments) {
            Label event = new Label(appointment.getSubject());
            event.setStyleName(appointmentListItem);
            event.addStyleName(appointment.getStyle());
            if (!appointment.isMultiDay() || !appointment.isAllDay()) {
                event.addStyleName(appointment.getStyle());
                String hour = String.valueOf(appointment.getStartDate().getHours());
                String min = String.valueOf(appointment.getStartDate().getMinutes());
                if ("0".equals(min)) {
                    min = "00";
                }
                switch (appointment.getStartDate().getHours()) {
                    case 0:
                        hour = 12 + ":" + min + " am. ";
                        break;
                    case 12:
                        hour += ":" + min + " pm. ";
                        break;
                    default:
                        if (appointment.getStartDate().getHours() > 12) {
                            hour = appointment.getStartDate().getHours() - 12 + ":" + min + " pm. ";
                        } else {
                            hour += ":" + min + " am. ";
                        }
                        break;
                }
                event.setTextAsHtml("<span class=\"appointment-hour\">" + hour + "</span>" + appointment.getSubject());
            }
            event.addClickHandler(event1 -> {
                popup.hide();
                final ShortAppointmentView appointmentView = new ShortAppointmentView(appointment);
                appointmentView.onSaveOrUpdateAppointment(saveHandler);
                appointmentView.onDeleteAppointment(deleteHandler);
                appointmentView.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
                    Rectangle rectangle = new Rectangle(left, top, offsetWidth, offsetHeight);
                    rectangle = WindowUtils.getAllowedParameter(rectangle);

                    appointmentView.setPosition(rectangle.getLeft(), rectangle.getTop());
                });
            });

            FlexPanel eventPanel = new FlexPanel();
            eventPanel.setStyleName(appointmentParentStyle);
            eventPanel.addStyleName(appointmentBorder);
            eventPanel.add(event);

            panel.add(eventPanel);
        }

        VerticalPanel table = new VerticalPanel();
        table.setStyleName(backgroundStyle);
        table.add(headerPanel);
        table.add(panel);

        popup = new PopupPanel(true);
        popup.setWidget(table);
        popup.addStyleName("cal-day-events");
        popup.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
            int relativeX = left + offsetWidth;
            int relativeY = top + offsetHeight;

            if (relativeX > Window.getClientWidth()) {
                left -= offsetWidth / 2;
            }

            if (relativeY > Window.getClientHeight()) {
                top -= offsetHeight;
                if (top < 0) {
                    top = 0;
                }
            }

            popup.setPopupPosition(left, top);
        });
    }

    public void onUpdateAppointment(SaveAppointmentHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    public void onDeleteAppointment(DeleteAppointmentHandler deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
}
