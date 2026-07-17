package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public class AppointmentWidget extends FocusPanel {

    private Appointment appointment;

    public AppointmentWidget(Appointment appointment) {
        this.appointment = appointment;
        String number = "";
        if (appointment.getNumberData() != null){
            number = appointment.getNumberData()+"-";
        }
        if (appointment.isAllDay()) {
            super.add(new Label(number+appointment.getSubject()));
        } else {
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
            Label appointmentLabel = new Label();
            if (!Appointment.PINK.equals(appointment.getStyle())) {
                appointmentLabel.setTextAsHtml("<span class=\"appointment-hour\">" + hour + "</span>" + number + appointment.getSubject());
            } else {
                appointmentLabel.setText(number+appointment.getSubject());
            }
            super.add(appointmentLabel);
        }
    }

    public AppointmentWidget(float left, float top, float width, float height) {
        AbsolutePanel mainPanel = new AbsolutePanel();
        super.add(mainPanel);

        mainPanel.setStylePrimaryName("gwt-appointment");
        DOM.setStyleAttribute(mainPanel.getElement(), "position", "absolute");
        DOM.setStyleAttribute(mainPanel.getElement(), "left", left + "px");
        DOM.setStyleAttribute(mainPanel.getElement(), "top", top + "px");
        DOM.setStyleAttribute(mainPanel.getElement(), "width", width + "px");
        DOM.setStyleAttribute(mainPanel.getElement(), "height", height + "px");
    }

    public Appointment getAppointment() {
        return appointment;
    }


}
