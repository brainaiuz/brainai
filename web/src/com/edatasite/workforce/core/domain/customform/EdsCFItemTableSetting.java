package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.UUID;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "form_item_table_setting")
public class EdsCFItemTableSetting extends EdsObject {

    public static final String DEPARTMENT = "DEPARTMENT";
    public static final String PROJECT = "PROJECT";
    public static final String LOCATION = "LOCATION";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String POSITION = "POSITION";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private String uuid;

    @Column(name = "form_Id")
    private String customForm;

    @Column(name = "settingsJSONData")
    @Type(type = "text")
    private String settingsJSONData;

    private Integer entityId;
    private Integer relationFieldId;
    private String entity;
    private String relationField;


    public EdsCFItemTableSetting() {
        this.uuid = "ITEM_TABLE_" + UUID.uuid(10);
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCustomForm() {
        return customForm;
    }

    public void setCustomForm(String customForm) {
        this.customForm = customForm;
    }

    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getRelationField() {
        return relationField;
    }

    public void setRelationField(String relationField) {
        this.relationField = relationField;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getRelationFieldId() {
        return relationFieldId;
    }

    public void setRelationFieldId(Integer relationFieldId) {
        this.relationFieldId = relationFieldId;
    }
}
