package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class LeadDetailsItemTO extends ResponseData {

    private LeadTO base_info;
    private TaskDetailInfoTO task;
    private GeneralNoteTO note;
    private FilteredStatusItemTO status;
    private ContactsTO contacts;

    public LeadDetailsItemTO() {
    }

    public LeadTO getBase_info() {
        return base_info;
    }

    public void setBase_info(LeadTO base_info) {
        this.base_info = base_info;
    }

    public TaskDetailInfoTO getTask() {
        return task;
    }

    public void setTask(TaskDetailInfoTO task) {
        this.task = task;
    }

    public FilteredStatusItemTO getStatus() {
        return status;
    }

    public void setStatus(FilteredStatusItemTO status) {
        this.status = status;
    }

    public GeneralNoteTO getNote() {
        return note;
    }

    public void setNote(GeneralNoteTO note) {
        this.note = note;
    }

    public ContactsTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsTO contacts) {
        this.contacts = contacts;
    }
}
