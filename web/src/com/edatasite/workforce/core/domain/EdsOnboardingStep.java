package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsOnboardingStepCustomFields;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/24/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "onboardingstep")
public class EdsOnboardingStep extends EdsObject {
    public static final String _ONBOARDING_STEP_STATUSES = "_ONBOARDING_STEP_STATUSES";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "formID")
    private String formID;

    @Column(name = "viewName")
    private String viewName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "showinemployeeprofile")
    private Boolean showInEmployeeProfile;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean createForm = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private EdsOnboardingStep parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodid", referencedColumnName = "id")
    private EdsOnboardingPeriod onboardingPeriod;

    /*@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "stepresponsibility",
            joinColumns = {@JoinColumn(name = "stepid")},
            inverseJoinColumns = {@JoinColumn(name = "employeeid")})
    private List<EdsEmployee> employees = new ArrayList<EdsEmployee>();*/

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "steprole",
            joinColumns = {@JoinColumn(name = "stepid")},
            inverseJoinColumns = {@JoinColumn(name = "roleid")})
    private List<EdsRole> roles = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboardingstepcustomfieldsid", unique = true)
    private EdsOnboardingStepCustomFields onboardingStepCustomFields;

    @Embedded
    private EdsAuditInfo auditInfo;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsOnboardingPeriod getOnboardingPeriod() {
        return onboardingPeriod;
    }

    public void setOnboardingPeriod(EdsOnboardingPeriod onboardingPeriod) {
        this.onboardingPeriod = onboardingPeriod;
    }

    public List<EdsRole> getRoles() {
        return roles;
    }

    public void setRoles(List<EdsRole> roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShowInEmployeeProfile() {
        return showInEmployeeProfile;
    }

    public void setShowInEmployeeProfile(Boolean showInEmployeeProfile) {
        this.showInEmployeeProfile = showInEmployeeProfile;
    }

    public boolean isCreateForm() {
        return createForm;
    }

    public void setCreateForm(boolean createForm) {
        this.createForm = createForm;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsOnboardingStep getParent() {
        return parent;
    }

    public void setParent(EdsOnboardingStep parent) {
        this.parent = parent;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public EdsOnboardingStepCustomFields getOnboardingStepCustomFields() {
        return onboardingStepCustomFields;
    }

    public void setOnboardingStepCustomFields(EdsOnboardingStepCustomFields onboardingStepCustomFields) {
        this.onboardingStepCustomFields = onboardingStepCustomFields;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public OnboardingItem getRPC() {
        OnboardingItem item = new OnboardingItem();
        item.setStepId(getObjectID());
        item.setStepName(getName());
        item.setStepDescription(getDescription());
        item.setShowInEmployeeProfile(getShowInEmployeeProfile());
        item.setCreateForm(isCreateForm());
        if (getOnboardingPeriod() != null) {
            item.setPeriodId(getOnboardingPeriod().getObjectID());
            item.setPeriodName(getOnboardingPeriod().getName());
        }
        if (getParent() != null) {
            item.setParentID(getParent().getObjectID());
            item.setParentName(getParent().getName());
        }
        item.setFormID(getFormID());
        item.setViewName(getViewName());
        return item;
    }

    public OnboardingItem getRPCShort() {
        OnboardingItem item = new OnboardingItem();
        item.setStepId(getObjectID());
        item.setStepName(getName());
        if (getParent() != null) {
            item.setParentID(getParent().getObjectID());
        }
        item.setFormID(getFormID());
        return item;
    }
}
