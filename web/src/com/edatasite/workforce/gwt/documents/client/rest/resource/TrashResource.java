package com.edatasite.workforce.gwt.documents.client.rest.resource;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 * To change this template use File | Settings | File Templates.
 */
public class TrashResource extends RestResource {

    public TrashResource() {
    }

    ArrayList<FolderResource> subFolders = new ArrayList<>();
    ArrayList<FolderResource> folders = new ArrayList<>();
    ArrayList<FileResource> files = new ArrayList<>();

    /**
     * Retrieve the folders.
     *
     * @return the folders
     */
    public ArrayList<FolderResource> getFolders() {
        return folders;
    }

    /**
     * Modify the folders.
     *
     * @param newFolders the folders to set
     */
    public void setFolders(ArrayList<FolderResource> newFolders) {
        folders = newFolders;
    }

    /**
     * Retrieve the files.
     *
     * @return the files
     */
    public ArrayList<FileResource> getFiles() {
        return files;
    }

    /**
     * Modify the files.
     *
     * @param newFiles the files to set
     */
    public void setFiles(ArrayList<FileResource> newFiles) {
        files = newFiles;
    }

    public ArrayList<FolderResource> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(ArrayList<FolderResource> subFolders) {
        this.subFolders = subFolders;
    }

    public ArrayList<FolderResource> getTrashedFolders() {
        return subFolders;
    }

    @Override
    public String getLastModifiedSince() {
        return null;
    }
}
