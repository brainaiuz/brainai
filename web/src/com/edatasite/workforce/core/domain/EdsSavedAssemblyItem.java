package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsBuildAssemblyCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "savedAssemblyItem")
public class EdsSavedAssemblyItem extends EdsApprovable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @Column(name = "creatorid")
    private Integer creatorId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid", updatable = false, insertable = false)
    private EdsUser creator;

    private Integer itemId;

    private String itemName;

    private BigDecimal quantity;

    private Integer warehouseID;

    private Date date;

    @Column(name = "assembly_item_code")
    private String assemblyItemCode;

    @Column(name = "deleted")
    private Boolean deleted;

    private Integer intNumber;

    @Column(name = "account_id")
    private Integer accountId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'BUILD_ASSEMBLY'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfields_id")
    private EdsBuildAssemblyCustomFields customFields;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_assembly_history_id")
    private EdsAssemblyItemBuildHistory buildAssemblyBuildHistory;

    @Column(name = "updaterid")
    private Integer updaterId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "updaterid", updatable = false, insertable = false)
    private EdsUser updater;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    private Date approvedDate;

    @OneToMany(mappedBy = "edsSavedAssemblyItem", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsAssemblyItemItems> itemTables = new HashSet<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public EdsBuildAssemblyCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBuildAssemblyCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsAssemblyItemBuildHistory getBuildAssemblyBuildHistory() {
        return buildAssemblyBuildHistory;
    }

    public void setBuildAssemblyBuildHistory(EdsAssemblyItemBuildHistory buildAssemblyBuildHistory) {
        this.buildAssemblyBuildHistory = buildAssemblyBuildHistory;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
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

    public Set<EdsAssemblyItemItems> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsAssemblyItemItems> itemTables) {
        this.itemTables = itemTables;
    }

    public String getAssemblyItemCode() {
        return assemblyItemCode;
    }

    public void setAssemblyItemCode(String assemblyItemCode) {
        this.assemblyItemCode = assemblyItemCode;
    }

    public AssemblyItem getRpc() {
        AssemblyItem item = new AssemblyItem();
        item.setId(getObjectID());
        item.setAssemblyItemId(getItemId() != null ? getItemId() : null);
        item.setAssemblyItem(new SelectItem(getItemId(), getItemName()));
        item.setDate(getDate() != null ? new DateNonConvertable(getDate()) : null);
        item.setNumberData(new NumberData(getAssemblyItemCode(), getIntNumber()));
        item.getNumberData().setNumberFormat(EdsNumberingSettings.DEF_ASSEMBLY_PREFIX + "_0001");
        item.setWarehouseId(getWarehouseID());
        item.setQuantity(getQuantity());
        item.setBuilt(getBuildAssemblyBuildHistory() != null && !getBuildAssemblyBuildHistory().isDeleted());
        item.setCreator(getCreator() != null ? getCreator().getAsSelectItem() : null);
        item.setCreatedDate(getCreatedDate() != null ? new DateNonConvertable(getCreatedDate()) : null);
        item.setUpdater(getUpdater() != null ? getUpdater().getAsSelectItem() : null);
        item.setUpdatedDate(getUpdatedDate() != null ? new DateNonConvertable(getUpdatedDate()) : null);
        if (getStatus() != null) {
            item.setStatus(getStatus().getAsSelectItem());
            item.setStatusCode(getStatus().getCode());
        }
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
//        initApproverData(item);
        return item;
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
        setStatus(overallStatus);
        setOverallStatus(overallStatus);
    }

    @Override
    public void jumpToPreviousApprover() {
        super.jumpToPreviousApprover();
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.BUILD_ASSEMBLY_STATUS_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.BUILD_ASSEMBLY_STATUS_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_REJECTED);
        }
        return null;
    }
}
