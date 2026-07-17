package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;

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
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/5/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assemblyItemHistory")
public class EdsAssemblyItemBuildHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "date")
    private Date buildDate;

    @Column(name = "qty", precision = 11, scale = 2)
    private BigDecimal qty = new BigDecimal(1);

    @Column(name = "aseembly_item_id")
    Integer assemblyItemID;

    @Column(name = "saved_assembly_item_id")
    Integer savedAssemblyItemID;

    @Column(name = "transaction_id")
    Integer transactionID;

    @Column(name = "warehouse_id")
    Integer warehouseID;

    @OneToMany(mappedBy = "history", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EdsAssemblyBuildHistoryItem> items;

    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @Column(name = "creatorid")
    private Integer creatorId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid", updatable = false, insertable = false)
    private EdsUser creator;

    @Column(name = "assembly_item_code")
    private String assemblyItemCode;

    private Integer intNumber;

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


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getAssemblyItemID() {
        return assemblyItemID;
    }

    public void setAssemblyItemID(Integer assemblyItemID) {
        this.assemblyItemID = assemblyItemID;
    }

    public Integer getSavedAssemblyItemID() {
        return savedAssemblyItemID;
    }

    public void setSavedAssemblyItemID(Integer savedAssemblyItemID) {
        this.savedAssemblyItemID = savedAssemblyItemID;
    }

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public List<EdsAssemblyBuildHistoryItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<EdsAssemblyBuildHistoryItem> items) {
        this.items = items;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
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

    public String getAssemblyItemCode() {
        return assemblyItemCode;
    }

    public void setAssemblyItemCode(String assemblyItemCode) {
        this.assemblyItemCode = assemblyItemCode;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
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

    public AssemblyItem getRpc() {
        AssemblyItem item = new AssemblyItem();
        item.setId(getObjectID());
        item.setDate(getBuildDate() != null ? new DateNonConvertable(getBuildDate()) : null);
        item.setNumberData(new NumberData(getAssemblyItemCode(), getIntNumber()));
        item.setWarehouseId(getWarehouseID());
        item.setQuantity(getQty());
        item.setCreator(getCreator() != null ? getCreator().getAsSelectItem() : null);
        item.setCreatedDate(getCreatedDate() != null ? new DateNonConvertable(getCreatedDate()) : null);
        item.setUpdater(getUpdater() != null ? getUpdater().getAsSelectItem() : null);
        item.setUpdatedDate(getUpdatedDate() != null ? new DateNonConvertable(getUpdatedDate()) : null);
        return item;
    }
}
