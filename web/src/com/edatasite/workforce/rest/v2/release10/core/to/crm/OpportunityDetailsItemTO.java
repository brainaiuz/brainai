package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class OpportunityDetailsItemTO extends ResponseData {

    private OpportunityTO base_info;
    private TaskDetailInfoTO task;
    private ContactTO linked_person;
    private GeneralNoteTO note;
    private FilteredStatusItemTO status;
    private ContactsTO contacts;
    private Integer email_count;

    public OpportunityDetailsItemTO() {
    }

    public OpportunityTO getBase_info() {
        return base_info;
    }

    public void setBase_info(OpportunityTO base_info) {
        this.base_info = base_info;
    }

    public TaskDetailInfoTO getTask() {
        return task;
    }

    public void setTask(TaskDetailInfoTO task) {
        this.task = task;
    }

    public ContactTO getLinked_person() {
        return linked_person;
    }

    public void setLinked_person(ContactTO linked_person) {
        this.linked_person = linked_person;
    }

    public GeneralNoteTO getNote() {
        return note;
    }

    public void setNote(GeneralNoteTO note) {
        this.note = note;
    }

    public FilteredStatusItemTO getStatus() {
        return status;
    }

    public void setStatus(FilteredStatusItemTO status) {
        this.status = status;
    }

    public ContactsTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsTO contacts) {
        this.contacts = contacts;
    }

    public Integer getEmail_count() {
        return email_count;
    }

    public void setEmail_count(Integer email_count) {
        this.email_count = email_count;
    }
}
