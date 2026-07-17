package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Faxriddin Taslimov : 08/07/15
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeedocumentremindersettings")
public class EdsEmployeeDocumentReminderSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "entityType")
    private Integer entityType;

    @Column(name = "fieldValue")
    private String fieldValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateId")
    private EdsEmailTemplate template;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "employeedocumentreminderId")
    private Set<EdsHrReminderTimeAction> timeActions;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "employeedocumentreminder_role",
            joinColumns = {@JoinColumn(name = "employeedocumentreminder_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> roles = new HashSet<>();

 /*   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "hrreminder_workflows",
            joinColumns = {@JoinColumn(name = "hrreminder_id")},
            inverseJoinColumns = {@JoinColumn(name = "workflowrule_id")}
    )
    private Set<EdsWorkflowRule> workflowRules = new HashSet<EdsWorkflowRule>();*/

    @Column(name = "fieldcode")
    private String fieldcode;

    @Column(name = "itemId")
    private Integer itemId;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public EdsEmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EdsEmailTemplate template) {
        this.template = template;
    }

    public Set<EdsHrReminderTimeAction> getTimeActions() {
        if (timeActions == null) {
            timeActions = new HashSet<>();
        }
        return timeActions;
    }

    public void setTimeActions(Set<EdsHrReminderTimeAction> timeActions) {
        this.timeActions = timeActions;
    }

    public Set<EdsRole> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<EdsRole> roles) {
        this.roles = roles;
    }

   /* public Set<EdsWorkflowRule> getWorkflowRules() {
        if (workflowRules == null) {
            workflowRules = new HashSet<EdsWorkflowRule>();
        }
        return workflowRules;
    }

    public void setWorkflowRules(Set<EdsWorkflowRule> workflowRules) {
        this.workflowRules = workflowRules;
    }*/

    public void setFieldcode(String fieldcode) {
        this.fieldcode = fieldcode;
    }

    public String getFieldcode() {
        return fieldcode;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
