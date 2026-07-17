package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsRFPCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfp")
public class EdsRFP extends EdsApprovable {

    public static final String DRAFT = "DRAFT";
    public static final String APPROVE = "APPROVE";
    public static final String REJECT = "REJECT";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String number;
    private Integer intNumber;

    private Date dueDate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "rfpid")
    @OrderBy("objectID")
    private List<EdsRFPItem> items = new ArrayList<>();

    //DRAFT, SUBMITTED, APPROVED, REJECTED, CONVERTED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'REQUEST_FOR_PURCHASE'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();


    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(length = 500)
    private String rejectionReason;

    @Transient
    List<CompanyCustomFieldItem> itemCustomFields;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsRFPCustomFields customFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<EdsRFPItem> getItems() {
        return items;
    }

    public void setItems(List<EdsRFPItem> items) {
        this.items = items;
    }

    public EdsReference getStatus() {
        return getOverallStatus();
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(List<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public RFPData createRFPdata() {
        RFPData rfp = new RFPData();
        rfp.setObjectID(getObjectID());
        rfp.setCreatedDate(getCreatedDate());
        rfp.setDueDate(getDueDate());
        rfp.setCreator(getCreator().getAsSelectItem());
        if (getProject() != null) {
            rfp.setRelatedProject(getProject().getAsSelectItem());
        }
        if (getClient() != null) {
            rfp.setCustomer(getClient().getAsSelectItem());
        }
        if (getStatus() != null) {
            rfp.setStatus(getStatus().getCode());
        } else {
            rfp.setStatus(DRAFT);
        }

        return rfp;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
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
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVE.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && REJECT.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, REJECT);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, APPROVE);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, REJECT);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.INVOICE_STATUS, REJECT);
        }
        return null;
    }

    public void updateStatus(EdsReference status) {
        if (getCurrentApprover() != null) {
            getCurrentApprover().setStatus(status);
        }
    }

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
        if (getCurrentApprover().getObjectID().equals(prevPrevApprover.getObjectID())) {
            setEntityStatus(getCurrentApprover().getStatus());
        }
    }


    public EdsCompany getCompany() {
        return getCreator().getCompany();

    }


    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public EdsRFPCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsRFPCustomFields customFields) {
        this.customFields = customFields;
    }
}
