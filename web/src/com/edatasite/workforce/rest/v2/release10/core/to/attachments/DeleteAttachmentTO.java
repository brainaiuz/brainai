package com.edatasite.workforce.rest.v2.release10.core.to.attachments;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 2/21/2018.
 */
public class DeleteAttachmentTO extends ResponseData {
    private Integer file_id;

    public DeleteAttachmentTO() {
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }
}
