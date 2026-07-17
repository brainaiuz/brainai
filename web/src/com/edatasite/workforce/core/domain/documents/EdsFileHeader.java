package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:20:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fileheader", uniqueConstraints = @UniqueConstraint(columnNames = {"folder_id", "name"}))
public class EdsFileHeader extends EdsTraceable implements Constants {

    public static final String _DOCUMENT_TYPES = Constants._DOCUMENT_TYPES;
    public static final String _COMPANY_DOCUMENT_TYPES = Constants._COMPANY_DOCUMENT_TYPES;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /**
     * Version field for optimistic locking.
     */
    @SuppressWarnings("unused")
    @Version
    private int version;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    /**
     * The icon filename.
     */
    private String icon;

    /**
     * The file name.
     */
    @Column(name = "name")
    private String name;

    @Column(name = "documentName")
    private String documentName;

    /**
     * The parent folder of this file.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private EdsFolder folder;

    private int fileType = F_DEFAULT;

    private Integer entityId;

    /**
     * Is this a versioned file?
     */
    private boolean versioned = false;
    /**
     * Is this file temporarily deleted?
     * XXX: the columnDefinition is postgres specific, if deployment database is changed this shall be changed too
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    /**
     * Can this file be read by anyone?
     * XXX: the columnDefinition is postgres specific, if deployment database is changed this shall be changed too
     */
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean readForAll = false;

    /**
     * The owner of this file.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private EdsUser owner;

    /**
     * The bodies of this file. (A single one if not versioned.) A List so we
     * can keep order.
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "header", fetch = FetchType.LAZY)
    @OrderBy("version")
    private List<EdsFileBody> bodies = new ArrayList<>();

    /**
     * The current (most recent) body of this file. The single one if not
     * versioned.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsFileBody currentBody;

    private String documentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentType")
    private EdsReference documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enetityUser")
    private EdsUser enetityUser;

    @Column(name = "issuedDate")
    private Date issuedDate;

    @Column(name = "expireDate")
    private Date expireDate;

    @Column(name = "reminderId")
    private Integer reminderId;

    @Column(name = "reminderName")
    private String reminderName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurencedocument")
    private EdsInsuranceDocument insuranceDocument;

    /**
     * Retrieve the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Modify the icon.
     *
     * @param newIcon the new icon
     */
    public void setIcon(final String newIcon) {
        icon = newIcon;
    }

    /**
     * Retrieve the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Modify the name.
     *
     * @param newName the new name
     */
    public void setName(final String newName) {
        name = newName;
    }

    /**
     * Retrieve the folder.
     *
     * @return the folder object
     */
    public EdsFolder getFolder() {
        return folder;
    }

    /**
     * Modify the folder.
     *
     * @param newFolder the new folder
     */
    public void setFolder(final EdsFolder newFolder) {
        folder = newFolder;
    }

    /**
     * Determine whether this file is versioned or not.
     *
     * @return true if this file is versioned
     */
    public boolean isVersioned() {
        return versioned;
    }

    /**
     * Modify the versioning status of this file.
     *
     * @param newStatus the new versioning status
     */
    public void setVersioned(final boolean newStatus) {
        versioned = newStatus;
    }


    /**
     * Is this file deleted or not?.
     *
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }


    /**
     * Set whether this file is deleted .
     *
     * @param newDeleted the deletedFlag to set
     */
    public void setDeleted(boolean newDeleted) {
        deleted = newDeleted;
    }

    /**
     * Retrieve the owner.
     *
     * @return the owner
     */
    public EdsUser getOwner() {
        return owner;
    }

    /**
     * Modify the owner.
     *
     * @param newOwner the new owner
     */
    public void setOwner(final EdsUser newOwner) {
        owner = newOwner;
    }

    /**
     * Retrieve the list of bodies.
     *
     * @return the list of bodies
     */
    public List<EdsFileBody> getBodies() {
        return bodies;
    }

    /**
     * Replace the list of bodies.
     *
     * @param newBodies the new list of bodies
     */
    public void setBodies(final List<EdsFileBody> newBodies) {
        bodies = newBodies;
    }

    /**
     * Retrieve the current body.
     *
     * @return the current body
     */
    public EdsFileBody getCurrentBody() {
        return currentBody;
    }

    /**
     * Set another body as the current one.
     *
     * @param newCurrentBody the new current body
     */
    public void setCurrentBody(final EdsFileBody newCurrentBody) {
        currentBody = newCurrentBody;
    }

    /**
     * Retrieve the audit info.
     *
     * @return the audit info object
     */
    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    /**
     * Modify the audit info.
     *
     * @param newAuditInfo the new audit info
     */
    public void setAuditInfo(final EdsAuditInfo newAuditInfo) {
        auditInfo = newAuditInfo;
    }


    /**
     * Add a body to list of bodies.
     *
     * @param body InfoItemBody The body to add.
     */
    public void addBody(final EdsFileBody body) {
        if (body == null) {
            throw new IllegalArgumentException("Can't add a null FileBody.");
        }
        // Remove from old header
        if (body.getHeader() != null) {
            throw new IllegalArgumentException("Trying to add a FileBody that already belongs to a FileHeader.");
        }

        // Set child in parent
        getBodies().add(body);
        // Set parent in child
        body.setHeader(this);

        // Update version number
        if (currentBody == null) {
            body.setVersion(1);
        } else {
            body.setVersion(currentBody.getVersion() + 1);
        }

        currentBody = body;
    }


    /**
     * Retrieve the readForAll.
     *
     * @return the readForAll
     */
    public boolean isReadForAll() {
        return readForAll;
    }


    /**
     * Modify the readForAll.
     *
     * @param readForAll the readForAll to set
     */
    public void setReadForAll(boolean readForAll) {
        this.readForAll = readForAll;
    }

    /**
     * Returns current body version number formatted for display. Returns "-"
     * for non-versioned file.
     *
     * @return the current version
     */
    public String getCurrentVersionString() {
        if (isVersioned()) {
            return String.valueOf(currentBody.getVersion());
        }
        return "-";
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

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * Constructs and returns a DTO for this instance for use by remote clients
     *
     * @return FileResource
     */
    public FileResource getDTO() {
        final FileResource f = new FileResource();
        f.setObjectId(objectID);
        if (folder.getParent() != null) {
            f.setParentId(folder.getParent().getObjectID());
        }
        f.setFolderId(folder.getObjectID());
        f.setEntityID(getEntityId());
        f.setName(name);
        f.setDocumentName(getDocumentName());
        f.setBodyId(currentBody.getObjectID());
        f.setPath(getPath());
//            f.setFolder(folder.getDTO());
        f.setFolderName(folder.getName());
        f.setVersioned(versioned);
        f.setVersion(currentBody.getVersion());
        f.setOwner(owner.getDTO());
        f.setContentLength(currentBody.getFileSize());
        f.setDescription(currentBody.getDescription());
        f.setCreationDate(auditInfo.getCreationDate());
        if (getIssuedDate() != null) {
            f.setIssuedDate(new DateNonConvertable(getIssuedDate()));
        }
        if (getExpireDate() != null) {
            f.setExpireDate(new DateNonConvertable(getExpireDate()));
        }
        f.setReminderId(getReminderId());
        f.setReminderName(getReminderName());
        f.setDocID(getDocumentID());
        f.setDocumentID(getDocumentID());
        if (getDocumentType() != null) {
            f.setTypeId(getDocumentType().getObjectID());
            f.setType(getDocumentType().getName());
        }
        if (getInsuranceDocument() != null) {
            f.setInsureeName(getInsuranceDocument().getInsureeName());
            f.setInsureeLastName(getInsuranceDocument().getInsureeLastName());
            if (getInsuranceDocument().getStatusId() != null) {
                f.setStatusId(getInsuranceDocument().getStatusId());
                if (getInsuranceDocument().getStatusId().equals(1)) {
                    f.setStatusName("Employee");
                } else if (getInsuranceDocument().getStatusId().equals(2)) {
                    f.setStatusName("Emp Dependent");
                }
            }
            f.setInsuranceCost(getInsuranceDocument().getInsuranceCost());
            f.setInsurancePlan(getInsuranceDocument().getInsurancePlan());
            f.setInsuranceCoverage(getInsuranceDocument().getInsuranceCoverage());
        }
        f.setContentType(currentBody.getContentType());
        f.setDeleted(isDeleted());
        f.setReadForAll(readForAll);
        if (AMAZON.equals(currentBody.getType().getCode())) {
            f.setUploadType(AMAZON);
        } else if (GOOGLE.equals(currentBody.getType().getCode())) {
            f.setUploadType(GOOGLE);
        } else if (OFFICE_365.equals(currentBody.getType().getCode())) {
            f.setUploadType(OFFICE_365);
        } else if (OFFICE_365_SHARE_POINT.equals(currentBody.getType().getCode())) {
            f.setUploadType(OFFICE_365_SHARE_POINT);
        } else if (MINIO.equals(currentBody.getType().getCode())) {
            f.setUploadType(MINIO);
        } else if (LOCAL.equals(currentBody.getType().getCode())) {
            f.setUploadType(LOCAL);
        }
        f.setModificationDate(auditInfo.getModificationDate());
        f.setCreatedBy(auditInfo.getCreatedBy().getDTO().getFullName());
        f.setCreatedByID(auditInfo.getCreatedBy().getObjectID());
        return f;
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

    /**
     * Return the total space occupied by this file in storage
     * (i.e. the total size of all bodies)
     *
     * @return long
     */
    public long getTotalSize() {
        long total = 0;
        for (EdsFileBody body : getBodies()) {
            total += body.getFileSize();
        }
        return total;
    }

    public SolrInputDocument indexToSolr(List<EdsFolderRbac> fileRbacEntries, Integer companyId, Boolean isIntegerNumberEnabled, EdsUser user, String url) {
        List<Integer> users = new ArrayList<>();
        List<Integer> groups = new ArrayList<>();
        if (fileRbacEntries != null && fileRbacEntries.size() > 0) {
            for (EdsFolderRbac folderRbac : fileRbacEntries) {
                if (EdsTrusteeType.USER.equals(folderRbac.getTrusteeType())) {
                    users.add(folderRbac.getUser().getObjectID());
                } else if (EdsTrusteeType.GROUP.equals(folderRbac.getTrusteeType())) {
                    if (folderRbac.getGroup() != null) {
                        groups.add(folderRbac.getGroup().getObjectID());
                    }
                }
            }
        }
        String compositID = "";
        compositID = companyId + "_" + getObjectID();
        SolrInputDocument solrDoc = new SolrInputDocument();
        Integer currentRelationRank = 1;

        solrDoc.addField(SolrFolderRepresenter.FIELD_COMPOSITE_ID, compositID);
        solrDoc.addField(SolrFolderRepresenter.FIELD_COMPANY_ID, companyId);
        solrDoc.addField(SolrFolderRepresenter.FIELD_FOLDER_ID, getObjectID());
        solrDoc.addField(SolrFolderRepresenter.FIELD_FOLDER_NAME, getFolder() != null ? getFolder().getName() : "");
        solrDoc.addField(SolrFolderRepresenter.FIELD_OWNER_ID, getOwner().getObjectID());
        solrDoc.addField(SolrFolderRepresenter.FIELD_OWNER_NAME, getOwner().getName());
        solrDoc.addField(SolrFolderRepresenter.FIELD_FOLDER_DESCRIPTION, getCurrentBody().getDescription());
        solrDoc.addField(SolrFolderRepresenter.FIELD_DATE_CREATION, getAuditInfo().getCreationDate());
        solrDoc.addField(SolrFolderRepresenter.FIELD_DATE_MODIFICATION, getAuditInfo().getModificationDate());
        solrDoc.addField(SolrFolderRepresenter.FIELD_FOLDER_CONSTANT_NAME, getName());
        solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_ID, getEntityId());
        solrDoc.addField(SolrFolderRepresenter.FIELD_FOLDER_TYPE_ID, getFileType());
        solrDoc.addField(SolrFolderRepresenter.FIELD_HAS_PARENT, false);
        solrDoc.addField(SolrFolderRepresenter.FIELD_PARENT_ID, getFolder().getObjectID());
        solrDoc.addField(SolrFolderRepresenter.FIELD_DELETED, isDeleted());
        if (getAuditInfo().getCreatedBy() != null) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_CREATED_ID, getAuditInfo().getCreatedBy().getObjectID());
            solrDoc.addField(SolrFolderRepresenter.FIELD_CREATED_NAME, getAuditInfo().getCreatedBy().getName());
            solrDoc.addField(SolrFolderRepresenter.FIELD_CREATED_ID_NAME, getAuditInfo().getCreatedBy().getObjectID() + SolrFolderRepresenter.SPLIT + getAuditInfo().getCreatedBy().getName());
        }
        final EdsUser modifiedBy = this.getAuditInfo().getModifiedBy();

        if (modifiedBy != null) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_MODIFIED_ID, modifiedBy.getObjectID());
        }
        solrDoc.addField(SolrFolderRepresenter.FIELD_IS_FILE, true);
        solrDoc.addField(SolrFolderRepresenter.FIELD_SIZE, getCurrentBody().getFileSize());
        solrDoc.addField(SolrFolderRepresenter.FIELD_CONTENT_TYPE, getCurrentBody().getContentType());
        solrDoc.addField(SolrFolderRepresenter.FIELD_UPLOAD_TYPE, getCurrentBody().getType().getCode());
        solrDoc.addField(SolrFolderRepresenter.FIELD_DOWNLOAD_URL, url);
        solrDoc.addField(SolrFolderRepresenter.FIELD_BODY_ID, getCurrentBody().getObjectID());
        solrDoc.addField(SolrFolderRepresenter.FIELD_DOCUMENT_ID, getDocumentID());
        solrDoc.addField(SolrFolderRepresenter.FIELD_ISSUED_DATE, getIssuedDate());
        solrDoc.addField(SolrFolderRepresenter.FIELD_EXPIRE_DATE, getExpireDate());
        solrDoc.addField(SolrFolderRepresenter.FIELD_REMINDER_ID, getReminderId());
        solrDoc.addField(SolrFolderRepresenter.FIELD_REMINDER_NAME, getReminderName());
        if (getDocumentType() != null) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID, getDocumentType().getObjectID());
            solrDoc.addField(SolrFolderRepresenter.FIELD_DOCUMENT_TYPE, getDocumentType().getCode());
            solrDoc.addField(SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID_NAME, getDocumentType().getObjectID() + SolrFolderRepresenter.SPLIT + getDocumentType().getName());
        }
        if (getInsuranceDocument() != null) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_INSUREE_NAME, getInsuranceDocument().getInsureeName());
            solrDoc.addField(SolrFolderRepresenter.FIELD_INSUREE_LAST_NAME, getInsuranceDocument().getInsureeLastName());
            solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_STATUS_ID, getInsuranceDocument().getStatusId());
            if (getInsuranceDocument().getStatusId() != null) {
                String statusname = null;
                if (getInsuranceDocument().getStatusId().equals(1)) {
                    solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_STATUS_NAME, "Employee");
                    statusname = "Employee";
                } else if (getInsuranceDocument().getStatusId().equals(2)) {
                    solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_STATUS_NAME, "Emp Dependent");
                    statusname = "Emp Dependent";
                }
                solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_STATUS_ID_NAME, getInsuranceDocument().getStatusId() + SolrFolderRepresenter.SPLIT + statusname);
            }

            solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_COST, getInsuranceDocument().getInsuranceCost());
            solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_PLAN, getInsuranceDocument().getInsurancePlan());
            solrDoc.addField(SolrFolderRepresenter.FIELD_INSURANCE_COVARAGE, getInsuranceDocument().getInsuranceCoverage());
        }
        EdsUser enetityUser = getEnetityUser();

        if (enetityUser != null) { //not using enttity user
            solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_USER_ID, enetityUser.getObjectID());
            solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_USER_NAME, enetityUser.getName());
            solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_USER_ID_NAME, enetityUser.getObjectID() + SolrFolderRepresenter.SPLIT + enetityUser.getName());
            try {
                if ((enetityUser instanceof EdsEmployee) && enetityUser.getEmployee() != null && enetityUser.getEmployee().getProfile() != null) {
                    EdsEmployeeProfile employeeProfile = enetityUser.getEmployee().getProfile();
                    solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_USER_NUMBER, employeeProfile.getEmployeeCode());
                    String code = employeeProfile.getEmployeeCode();
                    if (code != null && !"".equals(code) && isIntegerNumberEnabled) {
                        solrDoc.addField(SolrFolderRepresenter.FIELD_ENTITY_USER_INTEGER_NUMBER, Long.parseLong(code.replaceAll("[\\D]", "")));
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        solrDoc.addField(SolrFolderRepresenter.FIELD_DOCUMENT_NAME, getDocumentName());
        for (Integer userId : users) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_USER_VIEWERS, userId);
        }

        for (Integer groupId : groups) {
            solrDoc.addField(SolrFolderRepresenter.FIELD_GROUP_VIEWERS, groupId);
        }

        return solrDoc;
    }

    public EdsReference getDocumentType() {
        return documentType;
    }

    public void setDocumentType(EdsReference documentType) {
        this.documentType = documentType;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public EdsUser getEnetityUser() {
        return enetityUser == null ? owner : enetityUser;
    }

    public void setEnetityUser(EdsUser enetityUser) {
        this.enetityUser = enetityUser;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public EdsInsuranceDocument getInsuranceDocument() {
        return insuranceDocument;
    }

    public void setInsuranceDocument(EdsInsuranceDocument insuranceDocument) {
        this.insuranceDocument = insuranceDocument;
    }
}

