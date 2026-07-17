
package com.edatasite.workforce.rest.v2.release10.core.to.documents;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Modified by Abdurakhmonov Farrukh on 02/14/2017.
 */

public class FileUpdateTO extends ResponseData {
    private Integer file_id;
    private String file_name;

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
