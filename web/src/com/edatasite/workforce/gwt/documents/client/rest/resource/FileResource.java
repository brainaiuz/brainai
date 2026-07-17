package com.edatasite.workforce.gwt.documents.client.rest.resource;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;

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
public class FileResource extends RestResource {

    public final static String NAME = "NAME";
    public final static String DOCUMENT_NAME = "DOCUMENT_NAME";
    public final static String DOCUMENT_DESCRIPTION = "DOCUMENT_DESCRIPTION";
    public final static String DOCUMENT_ID = "DOCUMENT_ID";
    public final static String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public final static String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    public final static String OWNER = "OWNER";
    public final static String CREATEBY = "CATEGORYBY";
    public final static String PATH = "PATH";
    public final static String SIZE = "SIZE";
    public final static String DATE = "DATE";
    public final static String TYPE = "TYPE";
    public final static String REMINDER_TYPE = "REMINDER_TYPE";
    public final static String DESCRIPTION = "description";
    public final static String EXPIRE_DATE = "expireDate";
    public final static String EXPIRED_DATE = "expireDate";
    public final static String ISSUED_DATE = "issuedDate";
    public final static String INSURE_NAME = "INSURE_NAME";
    public final static String INSURE_LAST_NAME = "INSURE_LAST_NAME";
    public final static String INSURANCE_STATUS = "INSURANCE_STATUS";
    public final static String INSURANCE_COST = "INSURANCE_COST";
    public final static String INSURANCE_PLAN = "INSURANCE_PLAN";
    public final static String INSURANCE_COVERAGE = "INSURANCE_COVERAGE";
    public static final int NO_SOURCE_TYPE = 0;
    public static final int EMAIL_TEMPLATE = 1;
    public static final int EMAIL_ATTACHMENT = 2;
    public final static String DOCUMENT_TYPE = "DOCUMENT_TYPE";

    private String documentID;
    private String documentOpenID;
    private String insuranceCost;
    private String insurancePlan;
    private String insuranceCoverage;
    private String insureeLastName;
    private boolean isDownloadable = true;
    private boolean isEmailTemplateAttachment;
    private String note;


    public FileResource() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || !(obj instanceof FileResource)) {
            return false;
        } else if (objectId != null) {
            FileResource that = (FileResource) obj;
            result = objectId.equals(that.getObjectId());
            if (bodyId != null && result) {
                result = bodyId.equals(that.getBodyId());
                if (uploadType != null && result) {
                    result = uploadType.equals(that.getUploadType());
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result || super.equals(obj);
    }

    @Override
    public int hashCode() {
        return ((objectId == null ? "" : objectId.toString()) + (bodyId == null ? "" : bodyId.toString()) + (uploadType == null ? "" : uploadType)).hashCode();
    }

    String name = "noname";

    private int sourceType = 0;

    String documentName;
    String insureeName;
    String description;

    UserResource owner;
    String ownerName;

    ArrayList<String> userPermissions = new ArrayList<>();

    private PermissionHolder permission;

    Integer ownerId;

    Integer bodyId;

    String createdBy;
    Integer createdByID;

    Date creationDate;

    Date modificationDate;

    String contentType;

    Long contentLength;

    boolean readForAll;

    boolean versioned;

    Integer version;

    boolean deleted = false;

    HashSet<PermissionHolder> permissions = new HashSet<>();

    String path;

    String folderName;

    Integer folderId;
    private Integer parentId;

    String uploadType;

    String googleOrOffice365Id;
    String googleDownloadLink;
    String officeDownloadLink;
    String amazonLink;

    private String entityName;
    private String employeeCode;

    boolean isFolder = false;
    boolean isBackFolder = false;
    Integer folderType;

    FolderResource folderResource;


    private boolean newEdsAttachment;
    private Integer emailAttachmentID;
    private String emailID;
    private Integer entityID;
    private String encryptedLinkAttribute;
    private boolean downloadFromEmailServer = false;

    private Integer typeId;
    private String type;
    private String docID;
    private DateNonConvertable issuedDate;
    private DateNonConvertable expireDate;
    private Long daysLeft = 0L;
    private Integer reminderId;
    private String reminderName;
    private ArrayList<ActionTimesTO> actionTimes;
    private String urlFromSolr;
    private Integer statusId;
    private String statusName;

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEncryptedLinkAttribute() {
        return encryptedLinkAttribute;
    }

    public void setEncryptedLinkAttribute(String encryptedLinkAttribute) {
        this.encryptedLinkAttribute = encryptedLinkAttribute;
    }

    public boolean isDownloadFromEmailServer() {
        return downloadFromEmailServer;
    }

    public void setDownloadFromEmailServer(boolean downloadFromEmailServer) {
        this.downloadFromEmailServer = downloadFromEmailServer;
    }


    public String getDownloadUrl() {
        return getDownloadUrl(null);
    }

    public String getDownloadUrl(String startsWith) {
        String url = ((startsWith != null) ? startsWith : "");
        if (Constants.AMAZON.equals(getUploadType()) || isNewEdsAttachment()) {
            if (isDownloadFromEmailServer()) {
                url = url + (url.equals("") ? CommandConstants.COMMON_URL + "/" : "") + "downloadEmailAttachment?link=" + encryptedLinkAttribute;
            } else {
                url = getAmazonLink();
//                url = url + "downloadFile?id=" + getBodyId().toString();
            }
        } else if (Constants.GOOGLE.equals(getUploadType())) {
            url = getGoogleDownloadLink();
        } else if (Constants.OFFICE_365.equals(getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(getUploadType())) {
            url = getOfficeDownloadLink();
        } else {
            url = getAmazonLink();
            if (url == null || "".equals(url)) {
                url = CommandConstants.COMMON_URL + "/downloadFile?id=" + getBodyId();
            }
        }
        return url;

    }

    public boolean isNewEdsAttachment() {
        return newEdsAttachment;
    }

    public void setNewEdsAttachment(boolean newEdsAttachment) {
        this.newEdsAttachment = newEdsAttachment;
    }

    public Integer getEmailAttachmentID() {
        return emailAttachmentID;
    }

    public void setEmailAttachmentID(Integer emailAttachmentID) {
        this.emailAttachmentID = emailAttachmentID;
    }

    public FolderResource getFolderResource() {
        return folderResource;
    }

    public void setFolderResource(FolderResource folderResource) {
        this.folderResource = folderResource;
    }

    public boolean isBackFolder() {
        return isBackFolder;
    }

    public void setBackFolder(boolean backFolder) {
        isBackFolder = backFolder;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getGoogleDownloadLink() {
        return googleDownloadLink;
    }

    public void setGoogleDownloadLink(String googleDownloadLink) {
        this.googleDownloadLink = googleDownloadLink;
    }

    public String getOfficeDownloadLink() {
        return getUrlWithParams(officeDownloadLink);
    }

    private String getUrlWithParams(String officeDocumentLink) {
        if (officeDocumentLink != null && (officeDocumentLink.endsWith(".docx") || officeDocumentLink.endsWith(".docm") || officeDocumentLink.endsWith(".odt") ||
                officeDocumentLink.endsWith(".xlsx") || officeDocumentLink.endsWith(".xlsb") || officeDocumentLink.endsWith(".xlsm") || officeDocumentLink.endsWith(".ods") ||
                officeDocumentLink.endsWith(".pptx") || officeDocumentLink.endsWith(".ppsx") || officeDocumentLink.endsWith(".odp") || officeDocumentLink.endsWith(".one"))) {
            return officeDocumentLink + "?d=w" + getDocumentOpenID().replace("-", "").toLowerCase();
        }

        return officeDocumentLink;
    }

    public void setOfficeDownloadLink(String officeDownloadLink) {
        this.officeDownloadLink = officeDownloadLink;
    }


    public String getAmazonLink() {
        return amazonLink;
    }

    public void setAmazonLink(String amazonLink) {
        this.amazonLink = amazonLink;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Retrieve the folderName.
     *
     * @return the folderName
     */
    public String getFolderName(Integer... nonesence) {
        return folderName != null ? URL.decodeComponent(folderName) : folderName;
    }

    /**
     * Modify the folderName.
     *
     * @param aFolderName the folderName to set
     */
    public void setFolderName(String aFolderName) {
        folderName = aFolderName;
    }

    public String getEncodedPath() {
        return path;
    }

    /**
     * Retrieve the path.
     *
     * @return the path
     */
    public String getPath() {
        String pathVal = URL.decodeComponent(path);
        if (entityName != null && !"".equals(entityName)) {
            pathVal = pathVal.substring(0, pathVal.lastIndexOf("/")) + "/" + entityName + pathVal.substring(pathVal.lastIndexOf("/"));
        }
        return pathVal;
    }

    /**
     * API
     *
     * @return path
     */
    public String getFilePath() {
        String pathVal = path;
        if (entityName != null && !"".equals(entityName)) {
            pathVal = pathVal.substring(0, pathVal.lastIndexOf("/")) + "/" + entityName + pathVal.substring(pathVal.lastIndexOf("/"));
        }
        return pathVal;
    }

    /**
     * Modify the path.
     *
     * @param aPath the path to set
     */
    public void setPath(String aPath) {
        path = aPath;
    }

    /**
     * Return the given size in a humanly readable form, using SI units to denote
     * size information, e.g. 1 KB = 1000 B (bytes).
     *
     * @param size in bytes
     * @return the size in human readable string
     */
    public static String getFileSizeAsString(long size) {
        if (size == 0) {
            return "";
        } else if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    /**
     * Retrieve the name.
     *
     * @return the name
     */
    public String getName(Integer... nonesence) {
        String result = null;
        try {
            result = URL.decodeComponent(name);
        } catch (Exception e) {
            result = getEncodedName();
        }
        return result;
    }

    /**
     * API
     *
     * @return name
     */
    public String getFileName() {
        return name;
    }

    /**
     * At Server side decode conception will not work, thus
     * we simply returned an encoded name.
     *
     * @return
     */
    public String getEncodedName() {
        return name;
    }

    /**
     * Modify the name.
     *
     * @param aName the name to set
     */
    public void setName(String aName) {
        if (aName != null) {
            name = aName;
        }
    }

    /**
     * Retrieve the description
     *
     * @return the description
     */
    public String getDescription() {
//        if (description != null) {
//            return URL.decodeComponent(description);
//        } else {
        return description;
//        }
    }

    /**
     * Modify the description
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @param newOwner the owner to set
     */
    public void setOwner(UserResource newOwner) {
        owner = newOwner;
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

    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
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

    /**
     * Retrieve the contentType.
     *
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Modify the contentType.
     *
     * @param newContentType the contentType to set
     */
    public void setContentType(String newContentType) {
        contentType = newContentType;
    }

    /**
     * Retrieve the contentLength.
     *
     * @return the contentLength
     */
    public Long getContentLength() {
        return contentLength;
    }

    /**
     * Modify the contentLength.
     *
     * @param newContentLength the contentLength to set
     */
    public void setContentLength(Long newContentLength) {
        contentLength = newContentLength;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
     * @param newReadForAll the readForAll to set
     */
    public void setReadForAll(boolean newReadForAll) {
        readForAll = newReadForAll;
    }

    /**
     * Retrieve the versioned.
     *
     * @return the versioned
     */
    public boolean isVersioned() {
        return versioned;
    }

    /**
     * Modify the versioned.
     *
     * @param newVersioned the versioned to set
     */
    public void setVersioned(boolean newVersioned) {
        versioned = newVersioned;
    }

    /**
     * Retrieve the version.
     *
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Modify the version.
     *
     * @param aVersion the version to set
     */
    public void setVersion(Integer aVersion) {
        version = aVersion;
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
     *
     * @param newPermissions the permissions to set
     */
    public void setPermissions(HashSet<PermissionHolder> newPermissions) {
        permissions = newPermissions;
    }

    public PermissionHolder getPermission() {
        return permission;
    }

    public void setPermission(PermissionHolder permission) {
        this.permission = permission;
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

    public ArrayList<String> getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(ArrayList<String> userPermissions) {
        this.userPermissions = userPermissions;
    }

    /**
     * Return the file size in a humanly readable form, using SI units to denote
     * size information, e.g. 1 KB = 1000 B (bytes).
     *
     * @return the fileSize
     */
    public String getFileSizeAsString() {
        if (contentLength == null) return "";
        return getFileSizeAsString(contentLength);
    }

    public String getRealPath() {
        return path;
    }

    private static String getSize(Long size, Double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat nf = NumberFormat.getFormat("######.#");
        return nf.format(res);
    }

    public boolean isShared() {
        if (isReadForAll()) {
            return true;
        }
        if (permissions != null) {
            for (PermissionHolder perm : permissions) {
                if (perm.getUser() != null && !owner.getObjectId().equals(perm.getUser().getObjectId())) {
                    return true;
                }
                if (perm.getGroup() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isShared(String ownerUser) {
        GWT.log("OWNER USER:" + ownerUser, null);
        if (isReadForAll()) {
            return true;
        }
        if (permissions != null) {
            for (PermissionHolder perm : permissions) {
                if (perm.getUser() != null && !ownerUser.equals(perm.getUser())) {
                    return true;
                }
                if (perm.getGroup() != null && perm.getGroup().isCanChange()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getLastModifiedSince() {
        if (modificationDate != null) {
            return getDate(modificationDate.getTime());
        }
        return null;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getGoogleOrOffice365Id() {
        return googleOrOffice365Id;
    }

    public void setGoogleOrOffice365Id(String googleOrOffice365Id) {
        this.googleOrOffice365Id = googleOrOffice365Id;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentID() {
        return documentID;
    }

    public String getDocumentOpenID() {
        return documentOpenID;
    }

    public void setDocumentOpenID(String documentOpenID) {
        this.documentOpenID = documentOpenID;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public ArrayList<ActionTimesTO> getActionTimes() {
        return actionTimes;
    }

    public void setActionTimes(ArrayList<ActionTimesTO> actionTimes) {
        this.actionTimes = actionTimes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public DateNonConvertable getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateNonConvertable expireDate) {
        this.expireDate = expireDate;
    }

    public DateNonConvertable getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(DateNonConvertable issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getInsureeName() {
        return insureeName;
    }

    public void setInsureeName(String insureeName) {
        this.insureeName = insureeName;
    }

    public Long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Integer getReminderId() {
        return reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(Integer createdByID) {
        this.createdByID = createdByID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getUrlFromSolr() {
        return urlFromSolr;
    }

    public void setUrlFromSolr(String urlFromSolr) {
        this.urlFromSolr = urlFromSolr;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getInsuranceCost() {
        return insuranceCost;
    }

    public void setInsuranceCost(String insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public String getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(String insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }

    public String getInsureeLastName() {
        return insureeLastName;
    }

    public void setInsureeLastName(String insureeLastName) {
        this.insureeLastName = insureeLastName;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }

    public boolean isEmailTemplateAttachment() {
        return isEmailTemplateAttachment;
    }

    public void setEmailTemplateAttachment(boolean emailTemplateAttachment) {
        isEmailTemplateAttachment = emailTemplateAttachment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
