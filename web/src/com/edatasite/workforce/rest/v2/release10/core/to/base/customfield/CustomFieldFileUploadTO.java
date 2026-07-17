package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class CustomFieldFileUploadTO extends CategoryTO {
    private ArrayList<AttachmentTO> files;

    public CustomFieldFileUploadTO() {
    }

    public CustomFieldFileUploadTO(Integer id, String title, ArrayList<AttachmentTO> files) {
        super(id, title);
        this.files = files;
    }

    public ArrayList<AttachmentTO> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<AttachmentTO> files) {
        this.files = files;
    }
}
