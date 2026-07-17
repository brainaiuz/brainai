package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Anvar Akramov on 20/11/2017.
 */
public class AttachmentTO extends ResponseData {
    private String file_name;
    private String link;

    public AttachmentTO() {
    }

    public AttachmentTO(String file_name, String link) {
        this.file_name = file_name;
        this.link = link;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
