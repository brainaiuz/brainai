package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 02/15/2018.
 */
public class DocumentHistoryLogTO extends ResponseData {
    private Integer id;
    private String name;
    private String created_date;
    private String modified_date;
    private Boolean is_deleted;
    private Boolean is_shared;
    private Boolean is_file;
    private String created_by;
    private String modified_by;

    public DocumentHistoryLogTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Boolean getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(Boolean is_shared) {
        this.is_shared = is_shared;
    }

    public Boolean getIs_file() {
        return is_file;
    }

    public void setIs_file(Boolean is_file) {
        this.is_file = is_file;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }
}
