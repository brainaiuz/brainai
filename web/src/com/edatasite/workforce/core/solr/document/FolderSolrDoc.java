package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:28.
 */
@SolrDocument(collection = "folderCore")
public class FolderSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("folderId")
    @Indexed(name = "folderId", type = "pint", required = true)
    private Integer folderId;

    @Field("documentId")
    private String documentId;

    @Field("documentName")
    private String documentName;

    @Field("issuedDate")
    private Date issuedDate;

    @Field("expireDate")
    private Date expireDate;

    @Field("reminderId")
    private Integer reminderId;

    @Field("reminderName")
    private String reminderName;

    @Field("insureeName")
    private String insureeName;

    @Field("insureeLastName")
    private String insureeLastName;

    @Field("insuranceStatusId")
    private Integer insuranceStatusId;

    @Field("insuranceStatusName")
    private String insuranceStatusName;

    @Field("insuranceStatusIdName")
    @Indexed(name = "insuranceStatusIdName", type = "string", stored = false)
    private String insuranceStatusIdName;

    @Field("insuranceCost")
    private String insuranceCost;

    @Field("insurancePlan")
    private String insurancePlan;

    @Field("insuranceCovarage")
    private String insuranceCovarage;

    @Field("documentType")
    private String documentType;

    @Field("documentTypeId")
    private Integer documentTypeId;

    @Field("documentTypeIdName")
    @Indexed(name = "documentTypeIdName", type = "string", stored = false)
    private String documentTypeIdName;

    @Field("dateCreation")
    private Date dateCreation;

    @Field("dateModification")
    private Date dateModification;

    @Field("permissions")
    @Indexed(name = "permissions", type = "string")
    private List<String> permissions = new ArrayList<>();

    @Field("relationships")
    @Indexed(name = "relationships", type = "string")
    private List<String> relationships = new ArrayList<>();

    @Field("folderName")
    private String folderName;

    @Field("folderDescription")
    private String folderDescription;

    @Field("folderConstantName")
    private String folderConstantName;

    @Field("entityId")
    private Integer entityId;

    @Field("folderTypeId")
    private Integer folderTypeId;

    @Field("parentId")
    private Integer parentId;

    @Field("hasParent")
    private Boolean hasParent;

    @Field("deleted")
    private Boolean deleted;

    @Field("ownerId")
    private Integer ownerId;

    @Field("ownerName")
    private String ownerName;

    @Field("modifiedId")
    private Integer modifiedId;

    @Field("createdId")
    private Integer createdId;

    @Field("isFile")
    private Boolean isFile;

    @Field("size")
    private Long size;

    @Field("contentType")
    private String contentType;

    @Field("uploadType")
    private String uploadType;

    @Field("downloadUrl")
    private String downloadUrl;

    @Field("bodyId")
    private Integer bodyId;

    @Field("type")
    private Integer type;

    @Field("userViewers")
    @Indexed(name = "userViewers", type = "pint")
    private List<Integer> userViewers = new ArrayList<>();

    @Field("groupViewers")
    @Indexed(name = "groupViewers", type = "pint")
    private List<Integer> groupViewers = new ArrayList<>();

    @Field("entityUserName")
    private String entityUserName;

    @Field("entityUserId")
    private Integer entityUserId;

    @Field("entityUserIdName")
    @Indexed(name = "entityUserIdName", type = "string", stored = false)
    private String entityUserIdName;

    @Field("entityUserNumber")
    private String entityUserNumber;

    @Field("entityUserIntegerNumber")
    private Long entityUserIntegerNumber;

    @Field("employeeCode")
    @Indexed(name = "employeeCode", type = "pint")
    private List<String> employeeCode = new ArrayList<>();

    @Field("createdName")
    private String createdName;

    @Field("employeeName")
    private String employeeName;

    @Field("documentDescription")
    private String documentDescription;

    @Field("createdIdName")
    @Indexed(name = "createdIdName", type = "string", stored = false)
    private String createdIdName;

    @Field("reminderIdName")
    @Indexed(name = "reminderIdName", type = "string", stored = false)
    private String reminderIdName;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
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

    public String getInsureeName() {
        return insureeName;
    }

    public void setInsureeName(String insureeName) {
        this.insureeName = insureeName;
    }

    public String getInsureeLastName() {
        return insureeLastName;
    }

    public void setInsureeLastName(String insureeLastName) {
        this.insureeLastName = insureeLastName;
    }

    public Integer getInsuranceStatusId() {
        return insuranceStatusId;
    }

    public void setInsuranceStatusId(Integer insuranceStatusId) {
        this.insuranceStatusId = insuranceStatusId;
    }

    public String getInsuranceStatusName() {
        return insuranceStatusName;
    }

    public void setInsuranceStatusName(String insuranceStatusName) {
        this.insuranceStatusName = insuranceStatusName;
    }

    public String getInsuranceStatusIdName() {
        return insuranceStatusIdName;
    }

    public void setInsuranceStatusIdName(String insuranceStatusIdName) {
        this.insuranceStatusIdName = insuranceStatusIdName;
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

    public String getInsuranceCovarage() {
        return insuranceCovarage;
    }

    public void setInsuranceCovarage(String insuranceCovarage) {
        this.insuranceCovarage = insuranceCovarage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Integer documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentTypeIdName() {
        return documentTypeIdName;
    }

    public void setDocumentTypeIdName(String documentTypeIdName) {
        this.documentTypeIdName = documentTypeIdName;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<String> relationships) {
        this.relationships = relationships;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderDescription() {
        return folderDescription;
    }

    public void setFolderDescription(String folderDescription) {
        this.folderDescription = folderDescription;
    }

    public String getFolderConstantName() {
        return folderConstantName;
    }

    public void setFolderConstantName(String folderConstantName) {
        this.folderConstantName = folderConstantName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getFolderTypeId() {
        return folderTypeId;
    }

    public void setFolderTypeId(Integer folderTypeId) {
        this.folderTypeId = folderTypeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getHasParent() {
        return hasParent != null && hasParent;
    }

    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }

    public Boolean getDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }

    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    public Boolean getFile() {
        return isFile != null && isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getUserViewers() {
        return userViewers;
    }

    public void setUserViewers(List<Integer> userViewers) {
        this.userViewers = userViewers;
    }

    public List<Integer> getGroupViewers() {
        return groupViewers;
    }

    public void setGroupViewers(List<Integer> groupViewers) {
        this.groupViewers = groupViewers;
    }

    public String getEntityUserName() {
        return entityUserName;
    }

    public void setEntityUserName(String entityUserName) {
        this.entityUserName = entityUserName;
    }

    public Integer getEntityUserId() {
        return entityUserId;
    }

    public void setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
    }

    public String getEntityUserIdName() {
        return entityUserIdName;
    }

    public void setEntityUserIdName(String entityUserIdName) {
        this.entityUserIdName = entityUserIdName;
    }

    public String getEntityUserNumber() {
        return entityUserNumber;
    }

    public void setEntityUserNumber(String entityUserNumber) {
        this.entityUserNumber = entityUserNumber;
    }

    public Long getEntityUserIntegerNumber() {
        return entityUserIntegerNumber;
    }

    public void setEntityUserIntegerNumber(Long entityUserIntegerNumber) {
        this.entityUserIntegerNumber = entityUserIntegerNumber;
    }

    public List<String> getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(List<String> employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getCreatedIdName() {
        return createdIdName;
    }

    public void setCreatedIdName(String createdIdName) {
        this.createdIdName = createdIdName;
    }

    public String getReminderIdName() {
        return reminderIdName;
    }

    public void setReminderIdName(String reminderIdName) {
        this.reminderIdName = reminderIdName;
    }
}
