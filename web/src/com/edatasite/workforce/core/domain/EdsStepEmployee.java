package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeStepCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/25/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stepemployee")
public class EdsStepEmployee extends EdsApprovable {
    public static final String _STEP_TYPES = "_STEP_TYPES";
    public static final String EMPLOYEE_TYPE = EmployeeStepItem.EMPLOYEE_TYPE;
    public static final String CANDIDATE_TYPE = EmployeeStepItem.CANDIDATE_TYPE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid", referencedColumnName = "id")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateid", referencedColumnName = "id")
    private EdsCrmContact candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stepid", referencedColumnName = "id")
    private EdsOnboardingStep onboardingStep;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseClaim")
    private Set<EdsExpenseReport> expenseClaims = new HashSet<>();

    @Column(name = "done")
    private Boolean done = false;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean workflowItem = false;

    private Integer workflowID;
    private String workflowStartDate;

    @Column(name = "workflow_due_date")
    private Integer workflowDueDate;

    private String workflowDueDateGranularity;

    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;

    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeestepcustomfieldsid", unique = true)
    private EdsEmployeeStepCustomFields employeeStepCustomFields;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean deleted = false;

    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean archived = false;

    @Embedded
    private EdsAuditInfo auditInfo;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "stepEmployeeType = 'EMPLOYEE_STEP'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "step_employee_id")
    @Where(clause = "deleted is null or deleted = 'false'")
    private List<EdsCertificateOfEmployment> linkedCertificates;

    /* start of APPROVE_ fields*/
    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && isOk(getCurrentApprover().getApproveStatusID()) && getCurrentApprover().getStatus().getObjectID().equals(getCurrentApprover().getApproveStatusID());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && isOk(getCurrentApprover().getRejectStatusID()) && getCurrentApprover().getStatus().getObjectID().equals(getCurrentApprover().getRejectStatusID());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.get(getCurrentApprover().getRejectStatusID());
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.get(getCurrentApprover().getApproveStatusID());
        }
        return null;
    }
    /* end of APPROVE_ fields*/

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        if (!ServerUtils.equalsEdsObject(this.employee, employee)) {
            addChange(CustomFormConstants.EMPLOYEE);
        }
        this.employee = employee;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        if (!ServerUtils.equalsEdsObject(this.candidate, candidate)) {
            addChange(CustomFormConstants.CANDIDAT);
        }
        this.candidate = candidate;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public EdsOnboardingStep getOnboardingStep() {
        return onboardingStep;
    }

    public void setOnboardingStep(EdsOnboardingStep onboardingStep) {
        this.onboardingStep = onboardingStep;
    }

    public Set<EdsExpenseReport> getExpenseClaims() {
        if (expenseClaims == null) {
            expenseClaims = new HashSet<>();
        }
        return expenseClaims;
    }

    public List<EdsCertificateOfEmployment> getLinkedCertificates() {
        if (linkedCertificates == null) linkedCertificates = new ArrayList<>();
        return linkedCertificates;
    }

    public void setLinkedCertificates(List<EdsCertificateOfEmployment> linkedCertificates) {
        this.linkedCertificates = linkedCertificates;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setWorkflowItem(boolean workflowItem) {
        this.workflowItem = workflowItem;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public String getWorkflowStartDate() {
        return workflowStartDate;
    }

    public void setWorkflowStartDate(String workflowStartDate) {
        this.workflowStartDate = workflowStartDate;
    }

    public Integer getWorkflowDueDate() {
        return workflowDueDate;
    }

    public void setWorkflowDueDate(Integer workflowDueDate) {
        this.workflowDueDate = workflowDueDate;
    }

    public String getWorkflowDueDateGranularity() {
        return workflowDueDateGranularity;
    }

    public void setWorkflowDueDateGranularity(String workflowDueDateGranularity) {
        this.workflowDueDateGranularity = workflowDueDateGranularity;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public EdsEmployeeStepCustomFields getEmployeeStepCustomFields() {
        return employeeStepCustomFields;
    }

    public void setEmployeeStepCustomFields(EdsEmployeeStepCustomFields employeeStepCustomFields) {
        this.employeeStepCustomFields = employeeStepCustomFields;
    }

    public EdsReference getStatus() {
        return getOverallStatus();
    }


    @Override
    public void setEntityStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(getOverallStatus(), status)) {
            addChange(CustomFormConstants.STATUS);
        }
        setOverallStatus(status);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
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

    public String getFormID() {
        return getOnboardingStep() != null ? getOnboardingStep().getFormID() : null;
    }

    public EmployeeStepItem getRPC(EmployeeStepItem item) {
        item = item == null ? new EmployeeStepItem() : item;
        item.setObjectID(getObjectID());
        if (getOnboardingStep() != null) {
            item.setStepID(getOnboardingStep().getObjectID());
        }
        if (getType() != null) {
            item.setTypeID(getType().getObjectID());
            item.setTypeName(getType().getName());
            item.setTypeCode(getType().getCode());
        }
        if (getStatus() != null) {
            item.setStatusID(getStatus().getObjectID());
            item.setStatusName(getStatus().getName());
        }
        if (getEmployee() != null) {
            item.setEmployeeID(getEmployee().getObjectID());
            boolean showNumber = getEmployee().getProfile() != null && getEmployee().getProfile().getEmployeeCode() != null && !"".equals(getEmployee().getProfile().getEmployeeCode());
            item.setEmployeeName((showNumber ? getEmployee().getProfile().getEmployeeCode() + " - " : "") + getEmployee().getFullName());
            item.setEmployeeCode(showNumber ? getEmployee().getProfile().getEmployeeCode() : null);
        } else if (getCandidate() != null) {
            item.setEmployeeID(getCandidate().getObjectID());
            item.setCandidateCode(getCandidate().getNumber());
            item.setEmployeeName(getCandidate().getFullName());
        }
        if (getAuditInfo() != null) {
            item.setCreationDate(getAuditInfo().getCreationDate());
            item.setUpdatedDate(getAuditInfo().getModificationDate());
        }
        for (EdsExpenseReport r : getExpenseClaims()) {
            item.getExpenses().add(new SelectItem(r.getObjectID(), r.getTitle()));
        }
        for (EdsCertificateOfEmployment cetificate : getLinkedCertificates()) {
            item.getLinkedCertificates().add(new SelectItem(cetificate.getObjectID(), cetificate.getNumber()));
        }
        item.setArchived(isArchived());
        return item;
    }

    public EmployeeStepSolrItem getSolrRPC() {
        EmployeeStepSolrItem employeeStepSolrItem = new EmployeeStepSolrItem();

        employeeStepSolrItem.setObjectId(getObjectID());
        employeeStepSolrItem.setWorkflowId(getWorkflowID());

        if (getOnboardingStep() != null) {
            EdsOnboardingStep onboardingStep = getOnboardingStep();
            employeeStepSolrItem.setOnboardingStep(onboardingStep.getAsSelectItem());
            employeeStepSolrItem.setOnboardingStepFormId(onboardingStep.getFormID());
        }

        if (getEmployee() != null) {
            SelectItem employee = getEmployee().getAsSelectItem();
            if (getEmployee().getProfile() != null) {
                employee.setCode(getEmployee().getProfile().getEmployeeCode());
            }
            employeeStepSolrItem.setEmployee(employee);
            if (getEmployee().getLocation() != null) {
                EdsLocation location = getEmployee().getLocation();
                String locations = (location.getCountry() != null ? location.getCountry().getName() : "")
                        + "," + (location.getState() != null ? (location.getState().getName() + ",") : "")
                        + location.getCity();
                employeeStepSolrItem.setEmployeeLocation(new SelectItem(location.getObjectID(), locations));
                if (location.getState() != null) {
                    employeeStepSolrItem.setEmployeeLocationState(location.getState().getName());
                }
                employeeStepSolrItem.setEmployeeLocationCity(location.getCity());
            }
        } else if (getCandidate() != null) {
            SelectItem candidate = getCandidate().getAsSelectItem();
            candidate.setCode(getCandidate().getNumber());
            employeeStepSolrItem.setEmployee(candidate);

            if (getCandidate().getPrefferedLocation() != null) {
                EdsLocation location = getCandidate().getPrefferedLocation();
                String locations = (location.getCountry() != null ? location.getCountry().getName() : "") + ","
                        + (location.getState() != null ? (location.getState().getName() + ",") : "")
                        + location.getCity();
                employeeStepSolrItem.setEmployeeLocation(new SelectItem(location.getObjectID(), locations));
                if (location.getState() != null) {
                    employeeStepSolrItem.setEmployeeLocationState(location.getState().getName());
                }
                employeeStepSolrItem.setEmployeeLocationCity(location.getCity());
            }
        }

        if (getAuditInfo() != null) {
            EdsAuditInfo auditInfo = getAuditInfo();
            employeeStepSolrItem.setCreationDate(auditInfo.getCreationDate());
            employeeStepSolrItem.setModificationDate(auditInfo.getModificationDate());

            if (auditInfo.getCreatedBy() != null) {
                EdsUser createdBy = auditInfo.getCreatedBy();
                employeeStepSolrItem.setCreator(new SelectItem(createdBy.getObjectID(), createdBy.getFullName()));
            }
        }

        if (getStatus() != null) {
            EdsReference status = getStatus();
            employeeStepSolrItem.setStatus(new SelectItem(status.getObjectID(), status.getName()));
        }
        if (getType() != null) {
            EdsReference type = getType();
            employeeStepSolrItem.setType(new SelectItem(type.getObjectID(), type.getName()));
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            employeeStepSolrItem.setCurrentApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
            employeeStepSolrItem.setApproverApproveStatusId(getCurrentApprover().getApproveStatusID());
            employeeStepSolrItem.setApproverRejectStatusId(getCurrentApprover().getRejectStatusID());
        }
        employeeStepSolrItem.setArchived(isArchived());

        return employeeStepSolrItem;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.EMPLOYEE)) {
                setEmployee((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setEntityStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CANDIDAT)) {
                setCandidate((EdsCrmContact) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getEmployeeStepCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getEmployeeStepCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.EMPLOYEE)) {
            return getEmployee();
        } else if (fieldID.equals(CustomFormConstants.CANDIDAT)) {
            return getCandidate();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getAuditInfo().getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getAuditInfo().getModificationDate();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getEmployeeStepCustomFields() != null ? CustomFieldsUtils.getObjectValue(getEmployeeStepCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    public SolrInputDocument indexToSolr(Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrEmployeeStepRepresenter.FIELD_COMPOSITE_ID, companyID + "_" + getObjectID());
        doc.addField(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID, Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        doc.addField(SolrEmployeeStepRepresenter.FIELD_STEP_ID, getObjectID());
        doc.addField(SolrEmployeeStepRepresenter.FIELD_WORKFLOW_ID, getWorkflowID());
        doc.addField(SolrEmployeeStepRepresenter.FIELD_ARCHIVED, isArchived());
        if (getOnboardingStep() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_ONBOARDING_STEP_ID, getOnboardingStep().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_ONBOARDING_STEP_FORM_ID, getOnboardingStep().getFormID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_ONBOARDING_STEP_NAME, getOnboardingStep().getName());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_ONBOARDING_STEP_ID_NAME, getOnboardingStep().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getOnboardingStep().getName());
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_APPROVER_APPROVE_STATUS_ID, getCurrentApprover().getApproveStatusID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_APPROVER_REJECT_STATUS_ID, getCurrentApprover().getRejectStatusID());
        }
        if (getEmployee() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_ID, getEmployee().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_NAME, getEmployee().getName());
            if (getEmployee().getProfile() != null) {
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_CODE, getEmployee().getProfile().getEmployeeCode());
            }
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_ID_NAME, getEmployee().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getEmployee().getName());
            if (getEmployee().getLocation() != null) {
                String locations = (getEmployee().getLocation().getCountry() != null ? getEmployee().getLocation().getCountry().getName() : "")
                        + ","
                        + (getEmployee().getLocation().getState() != null ? (getEmployee().getLocation().getState().getName() + ",") : "")
                        + getEmployee().getLocation().getCity();
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID, getEmployee().getLocation().getObjectID());
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_NAME, locations);
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID_NAME, getEmployee().getLocation().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + locations);
                if (getEmployee().getLocation().getState() != null) {
                    doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_STATE, getEmployee().getLocation().getState().getName());
                }
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_CITY, getEmployee().getLocation().getCity());
            }
        } else if (getCandidate() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_ID, getCandidate().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_NAME, getCandidate().getName());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_CANDIDATE_CODE, getCandidate().getNumber());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_ID_NAME, getCandidate().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getCandidate().getName());
            if (getCandidate().getPrefferedLocation() != null) {
                String locations = (getCandidate().getPrefferedLocation().getCountry() != null ? getCandidate().getPrefferedLocation().getCountry().getName() : "")
                        + ","
                        + (getCandidate().getPrefferedLocation().getState() != null ? (getCandidate().getPrefferedLocation().getState().getName() + ",") : "")
                        + getCandidate().getPrefferedLocation().getCity();
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID, getCandidate().getPrefferedLocation().getObjectID());
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_NAME, locations);
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_ID_NAME, getCandidate().getPrefferedLocation().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + locations);
                if (getCandidate().getPrefferedLocation().getState() != null) {
                    doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_STATE, getCandidate().getPrefferedLocation().getState().getName());
                }
                doc.addField(SolrEmployeeStepRepresenter.FIELD_EMPLOYEE_LOCATION_CITY, getCandidate().getPrefferedLocation().getCity());
            }
        }
        if (getStatus() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getStatus().getName());
        }
        if (getType() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_TYPE_ID, getType().getObjectID());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_TYPE_CODE, getType().getCode());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_TYPE_NAME, getType().getName());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_TYPE_ID_NAME, getType().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getType().getName());
        }
        if (getAuditInfo() != null) {
            doc.addField(SolrEmployeeStepRepresenter.FIELD_CREATION_DATE, getAuditInfo().getCreationDate());
            doc.addField(SolrEmployeeStepRepresenter.FIELD_MODIFICATION_DATE, getAuditInfo().getModificationDate());
            if (getAuditInfo().getCreatedBy() != null) {
                doc.addField(SolrEmployeeStepRepresenter.FIELD_CREATOR_ID, getAuditInfo().getCreatedBy().getObjectID());
                doc.addField(SolrEmployeeStepRepresenter.FIELD_CREATOR_NAME, getAuditInfo().getCreatedBy().getFullName());
                doc.addField(SolrEmployeeStepRepresenter.FIELD_CREATOR_ID_NAME, getAuditInfo().getCreatedBy().getObjectID() + SolrEmployeeStepRepresenter.SPLIT + getAuditInfo().getCreatedBy().getFullName());
            }
        }
        CustomFieldsUtils.setInSolrCustomFields(doc, getEmployeeStepCustomFields());
        return doc;
    }
}
