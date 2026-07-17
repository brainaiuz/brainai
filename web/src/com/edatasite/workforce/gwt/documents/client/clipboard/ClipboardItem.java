package com.edatasite.workforce.gwt.documents.client.clipboard;

import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;

import java.io.Serializable;
import java.util.List;


/**
 * @author Sherali
 */
public class ClipboardItem implements Serializable {
    private int operation;
    private FileResource file;
    private List<FileResource> files;
    private FolderResource folderResource;
    private UserResource user;
    private GroupMemberItem member;

    public ClipboardItem() {
    }

    public ClipboardItem(int operation, List<FileResource> files) {
        this.operation = operation;
        this.files = files;
    }

    public ClipboardItem(int operation, FileResource file) {
        this.operation = operation;
        this.file = file;
    }

    public ClipboardItem(int operation, FolderResource folder) {
        this.operation = operation;
        folderResource = folder;
    }

    public ClipboardItem(int operation, UserResource user) {
        this.operation = operation;
        this.user = user;
    }

    public ClipboardItem(UserResource user) {
        operation = Clipboard.COPY;
        this.user = user;
    }

    public ClipboardItem(int operation, GroupMemberItem user) {
        this.operation = operation;
        this.member = user;
    }

    public ClipboardItem(GroupMemberItem user) {
        operation = Clipboard.COPY;
        this.member = user;
    }

    public ClipboardItem(List<FileResource> files) {
        operation = Clipboard.COPY;
        this.files = files;
    }

    public ClipboardItem(FileResource file) {
        operation = Clipboard.COPY;
        this.file = file;
    }

    public ClipboardItem(FolderResource folder) {
        operation = Clipboard.COPY;
        folderResource = folder;
    }


    /**
     * Retrieve the user.
     *
     * @return the user
     */
    public UserResource getUser() {
        return user;
    }


    /**
     * Modify the member.
     *
     * @param member the member to set
     */
    public void setMember(GroupMemberItem member) {
        this.member = member;
    }

    /**
     * Retrieve the member.
     *
     * @return the member
     */
    public GroupMemberItem getMember() {
        return member;
    }


    /**
     * Modify the user.
     *
     * @param user the user to set
     */
    public void setUser(UserResource user) {
        this.user = user;
    }

    /**
     * Retrieve the operation.
     *
     * @return the operation
     */
    public int getOperation() {
        return operation;
    }

    /**
     * Modify the operation.
     *
     * @param operation the operation to set
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * Retrieve the file.
     *
     * @return the file
     */
    public FileResource getFile() {
        return file;
    }

    /**
     * Modify the file.
     *
     * @param file the file to set
     */
    public void setFile(FileResource file) {
        this.file = file;
    }


    /**
     * Retrieve the files.
     *
     * @return the files
     */
    public List<FileResource> getFiles() {
        return files;
    }

    /**
     * checks whether the clipboard item is a file or folder
     */
    public boolean isFileOrFolder() {
        return file != null || files != null || folderResource != null;
    }

    /**
     * checks whether the clipboard item is a file (or files)
     */
    public boolean isFile() {
        return file != null || files != null;
    }

    public boolean isUser() {
        return user != null;
    }

    public boolean isMember() {
        return member != null;
    }


    /**
     * Retrieve the folderResource.
     *
     * @return the folderResource
     */
    public FolderResource getFolderResource() {
        return folderResource;
    }


    /**
     * Modify the folderResource.
     *
     * @param folderResource the folderResource to set
     */
    public void setFolderResource(FolderResource folderResource) {
        this.folderResource = folderResource;
    }


}
