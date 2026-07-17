package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

import java.util.Date;

/**
 * Created by KHasan on 04.11.15.
 */
public class AppointmentDragWidget extends FocusPanel {

    private String title;
    private String description;
    private Date start;
    private Date end;
    private boolean selected;
    private float top;
    private float left;
    private float width;
    private float height;


    private Appointment appointment;


    public AppointmentDragWidget(Appointment appointment) {
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        // set selected
        this.selected = selected;

        // remove selected style (if exists)
        this.removeStyleName("gwt-appointment-selected");

        // if selected, add the selected style
        if (selected) {
            this.addStyleName("gwt-appointment-selected");
        }
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        DOM.setStyleAttribute(getElement(), "top", top + "px");
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        DOM.setStyleAttribute(getElement(), "left", left + "%");
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        DOM.setStyleAttribute(getElement(), "width", width + "%");
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
        DOM.setStyleAttribute(getElement(), "height", height + "px");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        DOM.setInnerText(getElement(), title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        DOM.setInnerHTML(getElement(), description);
    }

    public int compareTo(AppointmentWidget appt) {
        // -1 0 1
        // less, equal, greater
        int compare = this.getStart().compareTo(appt.getStart());

        if (compare == 0) {
            compare = appt.getEnd().compareTo(this.getEnd());
        }

        return compare;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
