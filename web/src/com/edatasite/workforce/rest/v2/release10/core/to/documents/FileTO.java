package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class FileTO extends ResponseData {
    private Integer file_id;
    private String file_name;
    private String file_content_type;
    private Long file_size;
    private String file_url;
    private Integer folder_id;
    private String created_time;
    private String updated_time;
    private boolean is_folder;
    private OwnerTO owner;
    private Integer file_count;
    private Boolean is_shared;
    private Boolean can_delete;
    private Boolean can_rename;
    private Boolean can_share;

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

    public String getFile_content_type() {
        return file_content_type;
    }

    public void setFile_content_type(String file_content_type) {
        this.file_content_type = file_content_type;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }

    public boolean isIs_folder() {
        return is_folder;
    }

    public void setIs_folder(boolean is_folder) {
        this.is_folder = is_folder;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public OwnerTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerTO owner) {
        this.owner = owner;
    }

    public Integer getFile_count() {
        return file_count;
    }

    public void setFile_count(Integer file_count) {
        this.file_count = file_count;
    }

    public Boolean isIs_shared() {
        return is_shared;
    }

    public void setIs_shared(Boolean is_shared) {
        this.is_shared = is_shared;
    }

    public Boolean getIs_shared() {
        return is_shared;
    }

    public Boolean getCan_delete() {
        return can_delete;
    }

    public void setCan_delete(Boolean can_delete) {
        this.can_delete = can_delete;
    }

    public Boolean getCan_rename() {
        return can_rename;
    }

    public void setCan_rename(Boolean can_rename) {
        this.can_rename = can_rename;
    }

    public Boolean getCan_share() {
        return can_share;
    }

    public void setCan_share(Boolean can_share) {
        this.can_share = can_share;
    }
}
