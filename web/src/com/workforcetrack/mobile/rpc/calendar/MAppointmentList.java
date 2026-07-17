package com.workforcetrack.mobile.rpc.calendar;

import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/4/11
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MAppointmentList {


    List<MAppointment> appointment;
    Integer totalCount;

    public MAppointmentList() {

    }


    public MAppointmentList(List<Appointment> appointmentList) {
        if (appointmentList != null) {
            this.appointment = new ArrayList<>();
            for (Appointment appointment : appointmentList) {
                this.appointment.add(new MAppointment(appointment));
            }
        }
    }

    public MAppointmentList(ListResult<HolidayItem> holidayItems) {
        if (holidayItems != null && holidayItems.getList() != null && holidayItems.getList().size() > 0) {
            this.appointment = new ArrayList<>();
            this.totalCount = holidayItems.getTotal();
            for (HolidayItem holidayItem : holidayItems.getList()) {
                this.appointment.add(new MAppointment(holidayItem));
            }
        }
    }


    public List<MAppointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(List<MAppointment> appointment) {
        this.appointment = appointment;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
