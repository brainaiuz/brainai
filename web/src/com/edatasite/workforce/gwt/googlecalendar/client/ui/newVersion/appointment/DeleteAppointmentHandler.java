package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Mar 12, 2010
 * Time: 3:56:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DeleteAppointmentHandler {

    void onDelete(Appointment appointment);

    void onDeleteTask(TaskSingleItem taskSingleItem);
}
