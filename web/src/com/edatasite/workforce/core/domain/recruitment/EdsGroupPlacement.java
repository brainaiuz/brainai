package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "group_placement")
public class EdsGroupPlacement extends EdsApprovable {

    public static final String APPROVED = "GROUP_PLACEMENT_APPROVED";
    public static final String REJECTED = "GROUP_PLACEMENT_REJECTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @Column(name = "date")
    private Date date;

    @Column(name = "placement_code")
    private String placementCode;


    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsGroupPlacementCustomFields customFields;

    @Column(name = "deleted")
    private Boolean deleted;

    private Integer intNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "updaterId")
    private EdsUser updater;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    private Date approvedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'GROUP_PLACEMENT'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToMany(mappedBy = "edsGroupPlacement", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsGroupPlacementItemTable> itemTables = new HashSet<>();


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());

    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && REJECTED.equals(getCurrentApprover().getStatus().getCode());

    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.getByCode(APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.getByCode(REJECTED);
        }
        return null;
    }


    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlacementCode() {
        return placementCode;
    }

    public void setPlacementCode(String placementCode) {
        this.placementCode = placementCode;
    }

    public EdsGroupPlacementCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsGroupPlacementCustomFields customFields) {
        this.customFields = customFields;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
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

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Set<EdsGroupPlacementItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsGroupPlacementItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public GroupPlacementItem toRpc() {
        GroupPlacementItem item = new GroupPlacementItem();
        item.setId(getObjectID());
        initApproverData(item);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApproverEmployee(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getPlacementCode() != null) {
            item.setPlacementCode(getPlacementCode());
        }
        item.setDate(getDate());
        item.setCreatedDate(new DateNonConvertable(getCreatedDate()));
        item.setUpdatedDate(new DateNonConvertable(getUpdatedDate()));
        item.setCreator(getCreator() != null ? new SelectItem(getCreator().getObjectID(), getCreator().getName()) : null);
        item.setUpdater(getUpdater() != null ? new SelectItem(getUpdater().getObjectID(), getUpdater().getName()) : null);
        if (getOverallStatus() != null) {
            item.setOverallStatus(getOverallStatus().getRPC());
        }
        return item;
    }

}
