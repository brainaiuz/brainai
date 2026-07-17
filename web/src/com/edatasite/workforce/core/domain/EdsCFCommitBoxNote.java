package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "commetbox_notes")
public class EdsCFCommitBoxNote extends EdsSuperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(name = "comment", length = 5000)
    private String comment;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customFieldsid")
    private EdsCompanyCustomFieldsSettings customFields;

    @Column(name = "entityname")
    private String entityName;

    @Column(name = "entityCategoryName")
    private String entityCategoryName;

    @Column(name = "employeeName")
    private String employeeName;

    @Column(name = "formItemId")
    private Integer formItemId;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsCompanyCustomFieldsSettings getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCompanyCustomFieldsSettings customFields) {
        this.customFields = customFields;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityCategoryName() {
        return entityCategoryName;
    }

    public void setEntityCategoryName(String entityCategoryName) {
        this.entityCategoryName = entityCategoryName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getFormItemId() {
        return formItemId;
    }

    public void setFormItemId(Integer formItemId) {
        this.formItemId = formItemId;
    }
}
