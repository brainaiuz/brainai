package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class CrmAccountDetailsItemTO extends ResponseData {

    private CrmAccountTO base_info;
    private ContactTO primary_contact;
    private TaskDetailInfoTO task;
    private ContactsTO contacts;
    private GeneralNoteTO note;

    public CrmAccountDetailsItemTO() {
    }

    public CrmAccountTO getBase_info() {
        return base_info;
    }

    public void setBase_info(CrmAccountTO base_info) {
        this.base_info = base_info;
    }

    public ContactTO getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(ContactTO primary_contact) {
        this.primary_contact = primary_contact;
    }

    public TaskDetailInfoTO getTask() {
        return task;
    }

    public void setTask(TaskDetailInfoTO task) {
        this.task = task;
    }

    public ContactsTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsTO contacts) {
        this.contacts = contacts;
    }

    public GeneralNoteTO getNote() {
        return note;
    }

    public void setNote(GeneralNoteTO note) {
        this.note = note;
    }
}
