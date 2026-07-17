package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents configuration file located in core1
 * It covers all fields requred to store attachments and related information in Solr index
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 7:44:55 PM
 */
public class SolrAttachmentRepresenter implements IsSerializable {
    public static final String defaultSearchField = "entityName";
    public static final String FIELD_ENTITY_NAME = "entityName";
    public static final String FIELD_COMPOSITE_ID = "compositeID";
    public static final String FIELD_COMPANY_ID = "companyID";
    public static final String FIELD_ENTITY_TYPE = "entityType";
    public static final String FIELD_ENTITY_ID = "entityID";
    public static final String FIELD_ENTITY_METADATA = "entityMetaData";
    public static final String FIELD_USERS_ID = "usersID";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_ENTITY_DESCRIPTION = "entityDescription";
    private String compositeID;
    private Integer companyID;
// entity name
    private String entityName;
// Entity id is field that stores entity's id it may be  EdsAttachment .  199    321   3838
    private Integer entityID;
// Entity type may be TASK_ATTACHMENT    PROJECT_ATTACHMENT
    private String entityType;
// meta data about entity
    private String entityMetaData;
// several users can have access to this document so this field will have a value something like this : 12   4533   3453    6994    21
    private Integer[] usersID;
// content of entire document
    private String content;
    // entity description
    private String entityDescription;

    public static String getDefaultSearchField() {
        return defaultSearchField;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getCompositeID() {
        return compositeID;
    }

    public void setCompositeID(String compositeID) {
        this.compositeID = compositeID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityMetaData() {
        return entityMetaData;
    }

    public void setEntityMetaData(String entityMetaData) {
        this.entityMetaData = entityMetaData;
    }

    public Integer[] getUsersID() {
        return usersID;
    }

    public void setUsersID(Integer[] usersID) {
        this.usersID = usersID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }
}
