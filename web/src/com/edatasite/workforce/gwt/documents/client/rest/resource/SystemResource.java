package com.edatasite.workforce.gwt.documents.client.rest.resource;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 27.05.2010
 * Time: 14:51:04
 * To change this template use File | Settings | File Templates.
 */
public class SystemResource implements IsSerializable {

    public SystemResource() {
    }

    private Integer objectId;

    private String name;

    private boolean filesExpanded = false;

    ArrayList<FolderResource> subFolders = new ArrayList<>();

    ArrayList<FileResource> files = new ArrayList<>();

    public ArrayList<FolderResource> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(ArrayList<FolderResource> subFolders) {
        this.subFolders = subFolders;
    }

    public ArrayList<FileResource> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<FileResource> files) {
        this.files = files;
    }

    public void setFilesExpanded(boolean filesExpanded) {
        this.filesExpanded = filesExpanded;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
