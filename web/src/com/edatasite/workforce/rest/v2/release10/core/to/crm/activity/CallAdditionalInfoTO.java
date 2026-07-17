package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 03/17/2018.
 */
public class CallAdditionalInfoTO extends ResponseData {
    private String call_type;
    private String address;
    private RecurrenceTO recurrence;
    private ArrayList<Object> links;
    private ArrayList<TimeTO> reminders;
    private ArrayList<AttachmentTO> attachments;
    private ArrayList<EventEmployeeTO> employees;
    private ArrayList<EventGuestTO> guests;

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RecurrenceTO getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(RecurrenceTO recurrence) {
        this.recurrence = recurrence;
    }

    public ArrayList<Object> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Object> links) {
        this.links = links;
    }

    public ArrayList<TimeTO> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<TimeTO> reminders) {
        this.reminders = reminders;
    }

    public ArrayList<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<EventEmployeeTO> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<EventEmployeeTO> employees) {
        this.employees = employees;
    }

    public ArrayList<EventGuestTO> getGuests() {
        return guests;
    }

    public void setGuests(ArrayList<EventGuestTO> guests) {
        this.guests = guests;
    }

}
