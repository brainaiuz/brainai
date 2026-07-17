package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:20:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "copiedfileheader", uniqueConstraints = @UniqueConstraint(columnNames = {"folder_id", "name"}))
public class EdsCopiedFileHeader extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Embedded
    private EdsAuditInfo auditInfo;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private EdsFolder folder;

    private int fileType = F_DEFAULT;

    private Integer entityId;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private EdsUser owner;

    private Integer fileHeaderId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsFolder getFolder() {
        return folder;
    }

    public void setFolder(EdsFolder folder) {
        this.folder = folder;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    /**
     * Retrieve the full path of the file, URL-encoded in the form:
     * /parent1/parent2/parent3/name
     *
     * @return the full path from the root of the files namespace
     */
    public String getPath() {
        try {
            return folder.getPath() + URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFileHeaderId(Integer fileHeaderId) {
        this.fileHeaderId = fileHeaderId;
    }

    public Integer getFileHeaderId() {
        return fileHeaderId;
    }
}


