package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 22.04.13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "csvtemplateitem")
public class EdsCsvTemplateItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csvtemplate_id")
    private EdsCsvTemplate csvTemplate;

    @Column(name = "systemfield")
    private String systemField;

    @Column(name = "value")
    private String value;//csvcolumnname(when isSystemValue=false) or systemfieldvalue(when isSystemValue=true)

    private Boolean isSystemValue = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCsvTemplate getCsvTemplate() {
        return csvTemplate;
    }

    public void setCsvTemplate(EdsCsvTemplate csvTemplate) {
        this.csvTemplate = csvTemplate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystemField() {
        return systemField;
    }

    public void setSystemField(String systemField) {
        this.systemField = systemField;
    }

    public Boolean isSystemValue() {
        return isSystemValue != null ? isSystemValue : false;
    }

    public void setSystemValue(Boolean systemValue) {
        isSystemValue = systemValue;
    }
}
