package com.workforcetrack.mobile.rpc.contact;

import com.workforcetrack.mobile.rpc.calendar.MAppointment;
import com.workforcetrack.mobile.rpc.calendar.MTaskListItem;
import com.workforcetrack.mobile.rpc.crm.MHistoryListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 19.01.12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MContactData {

    private List<MTaskListItem> task;
    private List<MAppointment> event;
    private List<MHistoryListItem> note;

    public MContactData() {

    }

    public List<MTaskListItem> getTask() {
        if (task == null) {
            task = new ArrayList<>();
        }
        return task;
    }

    public List<MAppointment> getEvent() {
        if (event == null) {
            event = new ArrayList<>();
        }
        return event;
    }

    public List<MHistoryListItem> getNote() {
        if (note == null) {
            note = new ArrayList<>();
        }
        return note;
    }
}
