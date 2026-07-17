package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.form.CustomFormAttributeItem;

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
 * User: Abror Abdukadirov
 * Date: 11.10.2019 15:13
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "custom_form_attributes")
public class EdsCustomFormAttributes extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "field_id")
    private String fieldId;
    @Column(name = "field_type")
    private String fieldType;
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customFormid")
    private EdsCustomForm customForm;

    public EdsCustomFormAttributes() {
        this.fieldId = "APPROVAL_PROCESS_" + UUID.uuid(10);
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EdsCustomForm getCustomForm() {
        return customForm;
    }

    public void setCustomForm(EdsCustomForm customForm) {
        this.customForm = customForm;
    }

    public CustomFormAttributeItem toTO() {
        CustomFormAttributeItem to = new CustomFormAttributeItem();
        to.setId(this.getObjectID());
        to.setFieldType(this.getFieldType());
        to.setFieldId(this.getFieldId());
        to.setLabel(this.getLabel());
        if (this.getCustomForm() != null) {
            to.setFormId(this.getCustomForm().getFormID());
        }
        return to;
    }
}
