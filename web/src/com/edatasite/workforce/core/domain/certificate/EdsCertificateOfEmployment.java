package com.edatasite.workforce.core.domain.certificate;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsCertificateCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateSolrItem;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Khasan on 11.09.14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificateofemployment")
public class EdsCertificateOfEmployment extends EdsApprovable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer intNumber;
    private String number;

    @Column(name = "employeeid")
    private Integer employeeid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid", updatable = false, insertable = false)
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificatetypeid")
    private EdsCertificateOfEmploymentType certificateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificatefieldsid")
    private EdsCertificateOfEmploymentFields fields;

    private Date creationDate;
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatirid")
    private EdsUser createrBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatorId")
    private EdsUser updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_employee_id")
    private EdsStepEmployee stepEmployee;

    @Column(name = "contenthtml")
    @Type(type = "text")
    private String contentHTML;

    private String attachmentIDs;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "stepEmployeeType = 'CERTIFICATE_OF_EMPLOYMENT'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted = false;

    @Column(name = "rejectionNote")
    @Type(type = "text")
    private String rejectionNote;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsCertificateCustomFields customFields;

    @Column(name = "modifiedDate")
    private Date lastUpdateTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificateOfEmployment", fetch = FetchType.LAZY)
    @OrderBy(value = "creationDate DESC")
    private List<EdsCertificateOfEmployeeNote> certificateNotes = new ArrayList<>();



    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsCertificateOfEmploymentType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(EdsCertificateOfEmploymentType certificateType) {
        this.certificateType = certificateType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsUser getCreaterBy() {
        return createrBy;
    }

    public void setCreaterBy(EdsUser createrBy) {
        this.createrBy = createrBy;
    }

    public String getContentHTML() {
        return contentHTML;
    }

    public void setContentHTML(String contentHTML) {
        this.contentHTML = contentHTML;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public EdsCertificateOfEmploymentFields getFields() {
        return fields;
    }

    public void setFields(EdsCertificateOfEmploymentFields fields) {
        this.fields = fields;
    }

    public String getAttachmentIDs() {
        return attachmentIDs;
    }

    public void setAttachmentIDs(String attachmentIDs) {
        this.attachmentIDs = attachmentIDs;
    }

    public EdsStepEmployee getStepEmployee() {
        return stepEmployee;
    }

    public void setStepEmployee(EdsStepEmployee stepEmployee) {
        this.stepEmployee = stepEmployee;
    }

    public CertificateItem createCertificateData() {
        CertificateItem certificateItem = new CertificateItem();
        certificateItem.setObjectId(getObjectID());
        certificateItem.setCertificateNumber(new NumberData(getNumber(), getIntNumber()));
        certificateItem.getCertificateNumber().setNumberFormat("CERT_0001");
        certificateItem.setCreationDate(getCreationDate());
        certificateItem.setCreatedBy(new SelectItem(getCreaterBy().getObjectID(), getCreaterBy().getFullName()));
        if (getUpdatedDate() != null) {
            certificateItem.setUpdatedDate(getModificationDate());
            certificateItem.setUpdatedBy(new SelectItem(getUpdatedBy().getObjectID(), getUpdatedBy().getFullName()));
        }
        certificateItem.setCertificateType(new SelectItem(getCertificateType().getObjectID(), getCertificateType().getName()));
        certificateItem.setPdfHeaderFooter(getCertificateType().getHeaderFooter());
        certificateItem.setEmployee(new SelectItem(getEmployee().getObjectID(), getEmployee().getName()));
        certificateItem.setEmployeeCode(getEmployee().getProfile() != null ? getEmployee().getProfile().getEmployeeCode() : "");
        certificateItem.setFormID(getCertificateType().getFormID());
        if (getOverallStatus() != null) {
            certificateItem.setStatusCode(getOverallStatus().getCode());
        }
        initApproverData(certificateItem);
        certificateItem.setTextBox1(getFields() != null ? getFields().getTextBox1() : "");
        certificateItem.setTextBox2(getFields() != null ? getFields().getTextBox2() : "");
        certificateItem.setTextBox3(getFields() != null ? getFields().getTextBox3() : "");
        certificateItem.setTextBox4(getFields() != null ? getFields().getTextBox4() : "");
        certificateItem.setTextBox5(getFields() != null ? getFields().getTextBox5() : "");
        certificateItem.setTextBox6(getFields() != null ? getFields().getTextBox6() : "");
        certificateItem.setTextBox7(getFields() != null ? getFields().getTextBox7() : "");
        certificateItem.setTextBox8(getFields() != null ? getFields().getTextBox8() : "");
        certificateItem.setTextBox9(getFields() != null ? getFields().getTextBox9() : "");
        certificateItem.setTextBox10(getFields() != null ? getFields().getTextBox10() : "");
        certificateItem.setTextBox11(getFields() != null ? getFields().getTextBox11() : "");
        certificateItem.setTextBox12(getFields() != null ? getFields().getTextBox12() : "");
        certificateItem.setTextBox13(getFields() != null ? getFields().getTextBox13() : "");
        certificateItem.setTextBox14(getFields() != null ? getFields().getTextBox14() : "");
        certificateItem.setTextBox15(getFields() != null ? getFields().getTextBox15() : "");
        certificateItem.setTextBox16(getFields() != null ? getFields().getTextBox16() : "");
        certificateItem.setTextBox17(getFields() != null ? getFields().getTextBox17() : "");
        certificateItem.setTextBox18(getFields() != null ? getFields().getTextBox18() : "");
        certificateItem.setTextArea1(getFields() != null ? getFields().getTextArea1() : "");
        certificateItem.setTextArea2(getFields() != null ? getFields().getTextArea2() : "");
        certificateItem.setTextArea3(getFields() != null ? getFields().getTextArea3() : "");
        certificateItem.setTextArea4(getFields() != null ? getFields().getTextArea4() : "");
        certificateItem.setTextArea5(getFields() != null ? getFields().getTextArea5() : "");
        certificateItem.setTextArea6(getFields() != null ? getFields().getTextArea6() : "");
        certificateItem.setTextArea7(getFields() != null ? getFields().getTextArea7() : "");
        certificateItem.setTextArea8(getFields() != null ? getFields().getTextArea8() : "");
        return certificateItem;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
        }
    }

    @Override
    public void jumpToPreviousApprover() {
        EdsApprover prevPrevApprover = null;
        EdsApprover prevApprover = null;
        for (EdsApprover approver : getApprovers()) {
            if (isOk(prevPrevApprover)) {
                prevApprover = approver;
            } else {
                prevPrevApprover = approver;
            }
            if (getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                int currentIndex = getApprovers().indexOf(prevApprover);
                if (currentIndex > 0) {
                    EdsApprover prev = getApprovers().get(currentIndex - 1);
                    if (prev != null) {
                        setCurrentApprover(prev);
                    }
                } else {
                    setCurrentApprover(prevApprover);
                }
                if (currentIndex >= 2) {
                    EdsApprover prevPrev = getApprovers().get(currentIndex - 2);
                    if (prevPrev != null) {
                        setPrevApprover(prevPrev);
                    }
                } else {
                    setPrevApprover(null);
                }
                break;
            }
        }
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        super.setValueForField(field, value);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.HRMS.CERTIFICATE_OF_EMPLOYMENT.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.HRMS.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE)) {
            return getEmployee();
        } else if (fieldID.equals(CustomFormConstants.HRMS.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE)) {
            return getCertificateType();
        }
        return super.getRealValue(fieldID);
    }

    public EdsCertificateCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCertificateCustomFields customFields) {
        this.customFields = customFields;
    }

    public SolrInputDocument wrapToSolrDocument(EdsCertificateOfEmployment certificate, Integer companyID) {
        String compositeId = companyID + "_" + certificate.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrCertificateRepresenter.FIELD_COMPOSITE_ID, compositeId);
        doc.addField(SolrCertificateRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID, certificate.getObjectID());
        doc.addField(SolrCertificateRepresenter.FIELD_NUMBER, certificate.getNumber());

        if (certificate.getEmployee() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_EMPLOYEE_ID, certificate.getEmployee().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_EMPLOYEE_NAME, certificate.getEmployee().getFullName());
            doc.addField(SolrCertificateRepresenter.FIELD_EMPLOYEE_CODE, certificate.getEmployee().getProfile() != null ? certificate.getEmployee().getProfile().getEmployeeCode() : "");
            doc.addField(SolrCertificateRepresenter.FIELD_EMPLOYEE_ID_NAME, certificate.getEmployee().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getEmployee().getName());
        }

        if (certificate.getCertificateType() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_TYPE_ID, certificate.getCertificateType().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_TYPE_NAME, certificate.getCertificateType().getName());
            doc.addField(SolrCertificateRepresenter.FIELD_TYPE_ID_NAME, certificate.getCertificateType().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getCertificateType().getName());
        }

        if (certificate.getCurrentApprover() != null && certificate.getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_CURRENT_APPROVER_ID, certificate.getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_CURRENT_APPROVER_NAME, certificate.getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrCertificateRepresenter.FIELD_CURRENT_APPROVER_ID_NAME, certificate.getCurrentApprover().getExactEmployee().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getCurrentApprover().getExactEmployee().getFullName());
        }

        if (certificate.getOverallStatus() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_STATUS_ID, certificate.getOverallStatus().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_STATUS_NAME, certificate.getOverallStatus().getName());
            doc.addField(SolrCertificateRepresenter.FIELD_STATUS_ID_NAME, certificate.getOverallStatus().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getOverallStatus().getName());
        }

        doc.addField(SolrCertificateRepresenter.FIELD_CREATED_DATE, certificate.getCreationDate());
        if (certificate.getCreaterBy() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_CREATED_BY_ID, certificate.getCreaterBy().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_CREATED_BY_NAME, certificate.getCreaterBy().getFullName());
            doc.addField(SolrCertificateRepresenter.FIELD_CREATED_BY_ID_NAME, certificate.getCreaterBy().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getCreaterBy().getFullName());
        }
        if (certificate.getUpdatedBy() != null) {
            doc.addField(SolrCertificateRepresenter.FIELD_ISSUED_BY_ID, certificate.getUpdatedBy().getObjectID());
            doc.addField(SolrCertificateRepresenter.FIELD_ISSUED_BY_NAME, certificate.getUpdatedBy().getFullName());
            doc.addField(SolrCertificateRepresenter.FIELD_ISSUED_BY_ID_NAME, certificate.getUpdatedBy().getObjectID() + SolrCertificateRepresenter.SPLIT + certificate.getUpdatedBy().getFullName());
            doc.addField(SolrCertificateRepresenter.FIELD_ISSUED_DATE, certificate.getModificationDate());
        }

        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public CertificateSolrItem getSolrRPC() {
        CertificateSolrItem certificateSolrItem = new CertificateSolrItem();

        certificateSolrItem.setObjectId(getObjectID());
        certificateSolrItem.setNumber(getNumber());

        if (getEmployee() != null) {
            certificateSolrItem.setEmployee(new SelectItem(getEmployee().getObjectID(), getEmployee().getFullName(), getEmployee().getProfile() != null ? getEmployee().getProfile().getEmployeeCode() : ""));
        }

        if (getCertificateType() != null) {
            certificateSolrItem.setType(new SelectItem(getCertificateType().getObjectID(), getCertificateType().getName()));
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            certificateSolrItem.setCurrentApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }

        if (getOverallStatus() != null) {
            certificateSolrItem.setStatus(getOverallStatus().getAsSelectItem());
        }

        certificateSolrItem.setCreatedDate(getCreationDate());
        if (getCreaterBy() != null) {
            certificateSolrItem.setCreatedBy(getCreaterBy().getAsSelectItem());
        }
        certificateSolrItem.setIssuedDate(getUpdatedDate());
        if (getUpdatedBy() != null) {
            certificateSolrItem.setIssuedBy(getUpdatedBy().getAsSelectItem());
        }

        return certificateSolrItem;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Integer employeeid) {
        this.employeeid = employeeid;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EdsUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(EdsUser updatedBy) {
        this.updatedBy = updatedBy;
    }
}
