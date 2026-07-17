package com.edatasite.workforce.gwt.documents.client.rest.resource;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 * To change this template use File | Settings | File Templates.
 */
public class FolderResource extends RestResource implements IsSerializable, Constants {

    public FolderResource() {
    }

    String name;

    UserResource owner;

    String createdBy;

    String modifiedBy;

    Date creationDate;

    Date modificationDate;

    private ArrayList<FolderResource> subfolders = new ArrayList<>();

    HashSet<PermissionHolder> permissions = new HashSet<>();

    private ArrayList<FolderResource> folders = new ArrayList<>();

    private ArrayList<FileResource> files = new ArrayList<>();

    private boolean deleted = false;

    private String parentName;

    private Integer parentId;

    private FolderResource parent;

    protected PermissionHolder permission = new PermissionHolder();

    private int fileType = F_DEFAULT;

    private Integer entityId;

    private int rank = 0;

    protected long filesCount;

    boolean isSystemFolder = false;

    private boolean hasChild = false;

    private String driveFolderId;
    private String driveFolderName;

    private Long duration;

    String path;

    /**
     * Modify the parentName.
     *
     * @param aParentName the parentName to set
     */
    public void setParentName(String aParentName) {
        parentName = aParentName;
    }

    public FolderResource getParent() {
        return parent;
    }

    public void setParent(FolderResource parent) {
        this.parent = parent;
    }

    /**
     * Retrieve the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getEncodeName() {
        return name;
    }

    /**
     * Modify the name.
     *
     * @param aName the name to set
     */
    public void setName(String aName) {
        name = aName;
    }

    /**
     * Retrieve the owner.
     *
     * @return the owner
     */
    public UserResource getOwner() {
        return owner;
    }

    /**
     * Modify the owner.
     *
     * @param anOwner the owner to set
     */
    public void setOwner(UserResource anOwner) {
        owner = anOwner;
    }

    /**
     * Retrieve the createdBy.
     *
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Modify the createdBy.
     *
     * @param aCreatedBy the createdBy to set
     */
    public void setCreatedBy(String aCreatedBy) {
        createdBy = aCreatedBy;
    }

    /**
     * Retrieve the modifiedBy.
     *
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Modify the modifiedBy.
     *
     * @param aModifiedBy the modifiedBy to set
     */
    public void setModifiedBy(String aModifiedBy) {
        modifiedBy = aModifiedBy;
    }

    /**
     * Retrieve the creationDate.
     *
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Modify the creationDate.
     *
     * @param aCreationDate the creationDate to set
     */
    public void setCreationDate(Date aCreationDate) {
        creationDate = aCreationDate;
    }

    /**
     * Retrieve the modificationDate.
     *
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * Modify the modificationDate.
     *
     * @param aModificationDate the modificationDate to set
     */
    public void setModificationDate(Date aModificationDate) {
        modificationDate = aModificationDate;
    }

    public ArrayList<FolderResource> getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(ArrayList<FolderResource> subfolders) {
        this.subfolders = subfolders;
    }

    /**
     * Retrieve the permissions.
     *
     * @return the permissions
     */
    public HashSet<PermissionHolder> getPermissions() {
        return permissions;
    }

    /**
     * Modify the permissions.
     * TODO DO NOT USE THIS METHOD IN A LOOP
     *
     * @param newPermissions the permissions to set
     */
    @Deprecated
    public void setPermissions(HashSet<PermissionHolder> newPermissions) {
        permissions = newPermissions;
    }

    /**
     * Retrieve the deleted.
     *
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Modify the deleted.
     *
     * @param newDeleted the deleted to set
     */
    public void setDeleted(boolean newDeleted) {
        deleted = newDeleted;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public PermissionHolder getPermission() {
        return permission;
    }

    public void setPermission(PermissionHolder permission) {
        this.permission = permission;
    }

    public String getParentName() {
        return parentName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isShared() {

        for (PermissionHolder perm : permissions) {
            if (perm.getUser() != null && !owner.getObjectId().equals(perm.getUser().getObjectId())) {
                return true;
            }
            if (perm.getGroup() != null) {
                return true;
            }
        }
        return false;
    }

//    public long getFilesCount() {
//        return filesCount;
//    }

//    public void setFilesCount(long filesCount) {
//        this.filesCount = filesCount;
//    }

    @Override
    public String getLastModifiedSince() {
        if (modificationDate != null) {
            return getDate(modificationDate.getTime());
        }
        return null;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDriveFolderId() {
        return driveFolderId;
    }

    public void setDriveFolderId(String driveFolderId) {
        this.driveFolderId = driveFolderId;
    }

    public String getDriveFolderName() {
        return driveFolderName;
    }

    public void setDriveFolderName(String driveFolderName) {
        this.driveFolderName = driveFolderName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
