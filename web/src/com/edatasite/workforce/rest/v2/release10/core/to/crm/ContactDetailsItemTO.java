package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.GeneralNoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class ContactDetailsItemTO extends ResponseData {

    private ContactTO base_info;
    private TaskDetailInfoTO task;
    private CrmAccountTO company;
    private GeneralNoteTO note;

    @Valid
    private List<? extends CustomFieldRequest> customFields;
    private List<AttachmentTO> attachments;

    public ContactDetailsItemTO() {
    }

    public ContactTO getBase_info() {
        return base_info;
    }

    public void setBase_info(ContactTO base_info) {
        this.base_info = base_info;
    }

    public TaskDetailInfoTO getTask() {
        return task;
    }

    public void setTask(TaskDetailInfoTO task) {
        this.task = task;
    }

    public CrmAccountTO getCompany() {
        return company;
    }

    public void setCompany(CrmAccountTO company) {
        this.company = company;
    }

    public GeneralNoteTO getNote() {
        return note;
    }

    public void setNote(GeneralNoteTO note) {
        this.note = note;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }
}
