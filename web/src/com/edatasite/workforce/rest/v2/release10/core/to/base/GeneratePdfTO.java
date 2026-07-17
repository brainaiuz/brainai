package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 01/10/2018.
 */
public class GeneratePdfTO extends ResponseData {
    private String file_url;

    public GeneratePdfTO() {
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
