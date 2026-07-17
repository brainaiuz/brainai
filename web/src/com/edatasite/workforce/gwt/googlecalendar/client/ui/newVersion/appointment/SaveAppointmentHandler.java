package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 6, 2010
 * Time: 3:31:18 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This interface is responsible for saving, updating and sharing appointments.
 * We are saving or updating and sharing the appointment by simply using this
 * interface. On the server side we are checking the id of the appointment and
 * according appointment's id value we are saving or updating it.
 */
public interface SaveAppointmentHandler {

    /**
     * If sharedEmployees returns null value, it automatically saves or updates according id value.
     * If sharedEmployees has value, besides saving or updating it also shares that appointment.
     *
     * @param appointment
     */
    void onSaveOrUpdate(Appointment appointment);

    void onSaveOrUpdateTask(Appointment appointment);

}
