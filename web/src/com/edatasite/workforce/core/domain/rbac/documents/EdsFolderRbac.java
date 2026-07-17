package com.edatasite.workforce.core.domain.rbac.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Sherali
 * Date: 29.05.2010
 * Time: 12:11:14
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "folderrbac",
        indexes = {
                @Index(columnList = "userid", name = "folderrbac_userid_index"),
                @Index(columnList = "groupid", name = "folderrbac_groupid_index"),
                @Index(columnList = "documentpermissionid", name = "folderrbac_documentpermissionid_index"),
                @Index(columnList = "fileheader_id", name = "folderrbac_fileheader_id_index")
        })
public class EdsFolderRbac extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsFolder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsFileHeader fileHeader;

    private int entryType = INHERITED;// 4 Custom, 3 - inherited     if entry custom permissions are valid, if inherited to read permission from relationship

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "documentPermissionId")
    private EdsDocumentPermission documentPermission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupid")
    private EdsGroup group;

    private Integer trusteeType;

    private int folderType = F_DEFAULT;

    private Integer entityId;

    private String relationship;

    private Integer relationRank;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean file = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsGroup getGroup() {
        return group;
    }

    public void setGroup(EdsGroup group) {
        this.group = group;
    }

    public Integer getTrusteeType() {
        return trusteeType;
    }

    public void setTrusteeType(Integer trusteeType) {
        this.trusteeType = trusteeType;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Integer getRelationRank() {
        return relationRank;
    }

    public void setRelationRank(Integer relationRank) {
        this.relationRank = relationRank;
    }

    public EdsFolder getFolder() {
        return folder;
    }

    public void setFolder(EdsFolder folder) {
        this.folder = folder;
    }

    public int getFolderType() {
        return folderType;
    }

    public void setFolderType(int folderType) {
        this.folderType = folderType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public EdsDocumentPermission getDocumentPermission() {
        return documentPermission;
    }

    public void setDocumentPermission(EdsDocumentPermission documentPermission) {
        this.documentPermission = documentPermission;
    }

    public EdsFileHeader getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(EdsFileHeader fileHeader) {
        this.fileHeader = fileHeader;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }
}
