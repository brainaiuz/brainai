package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;

public class EmployeeDocsTO {
    private AttachmentTO attachment;
    private IdNameTO owner;

    public EmployeeDocsTO() {
    }

    public EmployeeDocsTO(AttachmentTO attachment, IdNameTO owner) {
        this.attachment = attachment;
        this.owner = owner;
    }

    public AttachmentTO getAttachment() {
        return attachment;
    }

    public IdNameTO getOwner() {
        return owner;
    }
}
