package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldDefault;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoiceField;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;

/**
 * Created by Azazello on 10/6/16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowInvoiceField")
public class EdsWorkflowInvoiceField extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field")
    private EdsModelFieldCustom field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fieldDefault")
    @ForeignKey(name = "none")
    private EdsModelFieldDefault fieldDefault;

    private String action;
    private String value;
    private String customFieldID;

    @Column(name = "percentage", columnDefinition = "boolean default false")
    private boolean percentage = false;

    @Column(name = "demandOn", columnDefinition = "boolean default false")
    private boolean demandOn = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsModelFieldCustom getField() {
        return field;
    }

    public void setField(EdsModelFieldCustom field) {
        this.field = field;
    }

    public EdsModelFieldDefault getFieldDefault() {
        return fieldDefault;
    }

    public void setFieldDefault(EdsModelFieldDefault fieldDefault) {
        this.fieldDefault = fieldDefault;
    }

    public String getValue() {
        return value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomFieldID() {
        return customFieldID;
    }

    public void setCustomFieldID(String customFieldID) {
        this.customFieldID = customFieldID;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }

    public boolean isDemandOn() {
        return demandOn;
    }

    public void setDemandOn(boolean demandOn) {
        this.demandOn = demandOn;
    }

    public WorkflowInvoiceField getRPC() {
        WorkflowInvoiceField item = new WorkflowInvoiceField();
        item.setObjectID(getObjectID());
        item.setValue(getValue());
        item.setAction(getAction());
        item.setCustomFieldID(getCustomFieldID());
        item.setPercentage(isPercentage());
        item.setDemandOn(isDemandOn());
        if (getField() != null) {
            item.setField(getField().getRPC(null));
        } else if (getFieldDefault() != null) {
            item.setField(getFieldDefault().getRPC(null));
        }
        return item;
    }
}
