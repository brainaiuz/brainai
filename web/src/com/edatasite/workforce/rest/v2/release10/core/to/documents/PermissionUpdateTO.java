package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 03/05/2018.
 */
public class PermissionUpdateTO extends ResponseData {
    private Integer file_id;
    private ArrayList<Integer> object_ids;
    private boolean read;
    private boolean delete;
    private boolean write;
    private boolean modify_acl;

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public ArrayList<Integer> getObject_ids() {
        return object_ids;
    }

    public void setObject_ids(ArrayList<Integer> object_ids) {
        this.object_ids = object_ids;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isModify_acl() {
        return modify_acl;
    }

    public void setModify_acl(boolean modify_acl) {
        this.modify_acl = modify_acl;
    }
}
