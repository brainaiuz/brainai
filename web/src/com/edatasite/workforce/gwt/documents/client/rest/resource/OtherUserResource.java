package com.edatasite.workforce.gwt.documents.client.rest.resource;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 * To change this template use File | Settings | File Templates.
 */
public class OtherUserResource extends RestResource {

    public OtherUserResource() {
    }

    String username;
    ArrayList<FolderResource> folders = new ArrayList<>();
    ArrayList<FileResource> files = new ArrayList<>();

    private boolean filesExpanded = false;

    /**
     * Retrieve the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modify the username.
     *
     * @param aUsername the username to set
     */
    public void setUsername(String aUsername) {
        username = aUsername;
    }

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

    public String getName() {
        String[] names = uri.split("/");
        return names[names.length - 1];
    }

    @Override
    public String getLastModifiedSince() {
        return null;
    }

    public void setFilesExpanded(boolean filesExpanded) {
        this.filesExpanded = filesExpanded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OtherUserResource)) return false;
        OtherUserResource that = (OtherUserResource) o;
        if (getObjectId() != null && getObjectId().equals(that.getObjectId())) return true;
        if (getUsername() != null && getUsername().equals(that.getUsername())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        if (getObjectId() != null) return Objects.hash(getObjectId());
        if (getUsername() != null) return Objects.hash(getUsername());
        return super.hashCode();
    }
}
