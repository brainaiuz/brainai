package com.edatasite.workforce.gwt.documents.client.rpc.solr;

/**
 * User: Sherali
 * Date: 01.06.2010
 * Time: 17:43:53
 */

import java.sql.Date;

public class SolrFolderRepresenter {

    public static final String SPLIT = "@";
    public static final String FIELD_FOLDER_ID = "folderId";
    public static final String FIELD_COMPANY_ID = "companyId";
    //Compositeid composed from companyid + folderid + trusteeid + trusteetype
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_OWNER_NAME = "ownerName";
    public static final String FIELD_GROUP_ID = "groupId";
    public static final String FIELD_TRUSTEE_TYPE = "trusteeType";
    public static final String FIELD_GROUP_NAME = "groupName";
    public static final String FIELD_DATE_CREATION = "dateCreation";
    public static final String FIELD_DATE_MODIFICATION = "dateModification";
    public static final String FIELD_RANK = "rank";
    public static final String FIELD_PERMISSIONS = "permissions";
    public static final String FIELD_RELATIONSHIPS = "relationships";
    public static final String FIELD_FOLDER_NAME = "folderName";
    public static final String FIELD_FOLDER_CONSTANT_NAME = "folderConstantName";//second file constant name
    public static final String FIELD_FOLDER_DESCRIPTION = "folderDescription";//second file description
    public static final String FIELD_ENTITY_ID = "entityId";
    public static final String FIELD_FOLDER_TYPE_ID = "folderTypeId";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_HAS_PARENT = "hasParent";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_MODIFIED_ID = "modifiedId";
    public static final String FIELD_CREATED_ID = "createdId";
    public static final String FIELD_IS_FILE = "isFile";
    public static final String FIELD_ENTITY_METADATA = "entityMetadata";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_CONTENT_TYPE = "contentType";
    public static final String FIELD_UPLOAD_TYPE = "uploadType";
    public static final String FIELD_DOWNLOAD_URL = "downloadUrl";
    public static final String FIELD_BODY_ID = "bodyId";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_USER_VIEWERS = "userViewers";
    public static final String FIELD_GROUP_VIEWERS = "groupViewers";
    public static final String FIELD_DOCUMENT_ID = "documentId";
    public static final String FIELD_ISSUED_DATE = "issuedDate";
    public static final String FIELD_EXPIRE_DATE = "expireDate";
    public static final String FIELD_REMINDER_ID = "reminderId";
    public static final String FIELD_REMINDER_NAME = "reminderName";
    public static final String FIELD_DOCUMENT_TYPE = "documentType";
    public static final String FIELD_DOCUMENT_TYPE_ID = "documentTypeId";
    public static final String FIELD_DOCUMENT_TYPE_ID_NAME = "documentTypeIdName";
    public static final String FIELD_DOCUMENT_NAME = "documentName";
    public static final String FIELD_ENTITY_USER_ID = "entityUserId";
    public static final String FIELD_ENTITY_USER_NAME = "entityUserName";
    public static final String FIELD_ENTITY_USER_ID_NAME = "entityUserIdName";
    public static final String FIELD_ENTITY_USER_NUMBER = "entityUserNumber";
    public static final String FIELD_ENTITY_USER_INTEGER_NUMBER = "entityUserIntegerNumber";
    public static final String FIELD_CREATED_BY_ID = "createdById";
    public static final String FIELD_CREATED_NAME = "createdName";
    public static final String FIELD_CREATED_ID_NAME = "createdIdName";
    public static final String FIELD_EMPLOYEE_ID = "employeeId";
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";
    public static final String FIELD_EMPLOYEE_ID_NAME = "employeeIdName";
    public static final String FIELD_REMINDER_ID_NAME = "reminderIdName";

    public static final String FIELD_INSUREE_NAME = "insureeName";
    public static final String FIELD_INSUREE_LAST_NAME = "insureeLastName";
    public static final String FIELD_INSURANCE_STATUS_ID = "insuranceStatusId";
    public static final String FIELD_INSURANCE_STATUS_NAME = "insuranceStatusName";
    public static final String FIELD_INSURANCE_STATUS_ID_NAME = "insuranceStatusIdName";
    public static final String FIELD_INSURANCE_COST = "insuranceCost";
    public static final String FIELD_INSURANCE_PLAN = "insurancePlan";
    public static final String FIELD_INSURANCE_COVARAGE = "insuranceCovarage";


    public static final String SORTABLE_FILE_NAME = "sortableFileName";
    public static final String SORTABLE_FOLDER_NAME = "sortableFolderName";
    public static final String SORTABLE_CONTENT_TYPE = "sortableContentType";
    public static final String SORTABLE_EMPLOYEE_NAME = "sortableEmployeeName";
    public static final String SORTABLE_CREATED_BY = "sortableCreatedBy";
    public static final String SORTABLE_CREATED_NAME = "sortableCreatedName";
    public static final String SORTABLE_EMPLOYEE_CODE = "sortableEmployeeCode";
    public static final String SORTABLE_DOCUMENT_NAME = "sortableDocumentName";
    public static final String SORTABLE_DOCUMENT_ID = "sortableDocumentId";
    public static final String SORTABLE_DOCUMENT_TYPE = "sortableDocumentType";
    public static final String SORTABLE_REMINDER_NAME = "sortableReminderName";
    public static final String SORTABLE_ISSUED_DATE = "sortableIssuedDate";
    public static final String EXPIRE_DATE = "expireDate";
    public static final String SORTABLE_EXPIRE_DATE = "sortableExpireDate";
    public static final String SORTABLE_DATE_CREATION = "sortableDateCreation";
    public static final String SORTABLE_DOCUMENT_DESCRIPTION = "sortableDocumentDescription";
    public static final String SORTABLE_NUMBER = "sortableNumber";

    private Integer FOLDER_ID;
    private Integer COMPANY_ID;
    private String COMPOSITE_ID;
    private Integer OWNER_ID;
    private Integer GROUP_ID;
    private String TRUSTEE_TYPE;
    private Integer ENTITY_ID;
    private int FOLDER_TYPE_ID;
    private String FOLDER_CONSTANT_NAME;
    private String GROUP_NAME;
    private Date DATE_CREATION;
    private Date DATE_MODIFICATION;
    private String RANK;
    private String[] PERMISSIONS;
    private String[] RELATIONSHIPS;
    private String FOLDER_NAME;
    private String FOLDER_DESCRIPTION;
    private Integer PARENT_ID;
    private boolean HAS_PARENT;
    private boolean DELETED;
    private Integer MODIFIED_ID;
    private Integer CREATED_ID;
    private Integer USER_ID;
    private String COMPOSITE;
    private boolean file;
    private String ENTITY_METADATA;
    private String CONTENT;
    private long SIZE;
    private String CONTENT_TYPE;
    private int TYPE;
    private String EMPLOYEE_NAME;
    private String CREATED_BY;
    private String EMPLOYEE_CODE;

    public static final String getDefaultSearchField() {
        return FIELD_COMPOSITE;
    }

    public Integer getFOLDER_ID() {
        return FOLDER_ID;
    }

    public void setFOLDER_ID(Integer FOLDER_ID) {
        this.FOLDER_ID = FOLDER_ID;
    }

    public Integer getCOMPANY_ID() {
        return COMPANY_ID;
    }

    public void setCOMPANY_ID(Integer COMPANY_ID) {
        this.COMPANY_ID = COMPANY_ID;
    }

    public String getCOMPOSITE_ID() {
        return COMPOSITE_ID;
    }

    public void setCOMPOSITE_ID(String COMPOSITE_ID) {
        this.COMPOSITE_ID = COMPOSITE_ID;
    }

    public Integer getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(Integer GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public String getTRUSTEE_TYPE() {
        return TRUSTEE_TYPE;
    }

    public void setTRUSTEE_TYPE(String TRUSTEE_TYPE) {
        this.TRUSTEE_TYPE = TRUSTEE_TYPE;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String GROUP_NAME) {
        this.GROUP_NAME = GROUP_NAME;
    }

    public Integer getENTITY_ID() {
        return ENTITY_ID;
    }

    public void setENTITY_ID(Integer ENTITY_ID) {
        this.ENTITY_ID = ENTITY_ID;
    }

    public int getFOLDER_TYPE_ID() {
        return FOLDER_TYPE_ID;
    }

    public void setFOLDER_TYPE_ID(int FOLDER_TYPE_ID) {
        this.FOLDER_TYPE_ID = FOLDER_TYPE_ID;
    }

    public String getFOLDER_CONSTANT_NAME() {
        return FOLDER_CONSTANT_NAME;
    }

    public void setFOLDER_CONSTANT_NAME(String FOLDER_CONSTANT_NAME) {
        this.FOLDER_CONSTANT_NAME = FOLDER_CONSTANT_NAME;
    }

    public Date getDATE_CREATION() {
        return DATE_CREATION;
    }

    public void setDATE_CREATION(Date DATE_CREATION) {
        this.DATE_CREATION = DATE_CREATION;
    }

    public Date getDATE_MODIFICATION() {
        return DATE_MODIFICATION;
    }

    public void setDATE_MODIFICATION(Date DATE_MODIFICATION) {
        this.DATE_MODIFICATION = DATE_MODIFICATION;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String[] getPERMISSIONS() {
        return PERMISSIONS;
    }

    public void setPERMISSIONS(String[] PERMISSIONS) {
        this.PERMISSIONS = PERMISSIONS;
    }

    public String[] getRELATIONSHIPS() {
        return RELATIONSHIPS;
    }

    public void setRELATIONSHIPS(String[] RELATIONSHIPS) {
        this.RELATIONSHIPS = RELATIONSHIPS;
    }

    public String getFOLDER_NAME() {
        return FOLDER_NAME;
    }

    public void setFOLDER_NAME(String FOLDER_NAME) {
        this.FOLDER_NAME = FOLDER_NAME;
    }

    public String getFOLDER_DESCRIPTION() {
        return FOLDER_DESCRIPTION;
    }

    public void setFOLDER_DESCRIPTION(String FOLDER_DESCRIPTION) {
        this.FOLDER_DESCRIPTION = FOLDER_DESCRIPTION;
    }

    public String getCOMPOSITE() {
        return COMPOSITE;
    }

    public void setCOMPOSITE(String COMPOSITE) {
        this.COMPOSITE = COMPOSITE;
    }

    public Integer getPARENT_ID() {
        return PARENT_ID;
    }

    public void setPARENT_ID(Integer PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }

    public boolean isHAS_PARENT() {
        return HAS_PARENT;
    }

    public void setHAS_PARENT(boolean HAS_PARENT) {
        this.HAS_PARENT = HAS_PARENT;
    }

    public Integer getOWNER_ID() {
        return OWNER_ID;
    }

    public void setOWNER_ID(Integer OWNER_ID) {
        this.OWNER_ID = OWNER_ID;
    }

    public boolean isDELETED() {
        return DELETED;
    }

    public void setDELETED(boolean DELETED) {
        this.DELETED = DELETED;
    }

    public Integer getMODIFIED_ID() {
        return MODIFIED_ID;
    }

    public void setMODIFIED_ID(Integer MODIFIED_ID) {
        this.MODIFIED_ID = MODIFIED_ID;
    }

    public Integer getCREATED_ID() {
        return CREATED_ID;
    }

    public void setCREATED_ID(Integer CREATED_ID) {
        this.CREATED_ID = CREATED_ID;
    }

    public Integer getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(Integer USER_ID) {
        this.USER_ID = USER_ID;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public String getENTITY_METADATA() {
        return ENTITY_METADATA;
    }

    public void setENTITY_METADATA(String ENTITY_METADATA) {
        this.ENTITY_METADATA = ENTITY_METADATA;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public long getSIZE() {
        return SIZE;
    }

    public void setSIZE(long SIZE) {
        this.SIZE = SIZE;
    }

    public String getCONTENT_TYPE() {
        return CONTENT_TYPE;
    }

    public void setCONTENT_TYPE(String CONTENT_TYPE) {
        this.CONTENT_TYPE = CONTENT_TYPE;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public String getEMPLOYEE_NAME() {
        return EMPLOYEE_NAME;
    }

    public void setEMPLOYEE_NAME(String EMPLOYEE_NAME) {
        this.EMPLOYEE_NAME = EMPLOYEE_NAME;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getEMPLOYEE_CODE() {
        return EMPLOYEE_CODE;
    }

    public void setEMPLOYEE_CODE(String EMPLOYEE_CODE) {
        this.EMPLOYEE_CODE = EMPLOYEE_CODE;
    }

}

