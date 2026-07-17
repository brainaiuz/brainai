package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "adjustment_item")
public class EdsAdjustmentItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id")
    private EdsStockAdjustment adjustment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @Column(name = "price", precision = 25, scale = 5)
    private BigDecimal price;

    @Column(name = "currentQty", precision = 25, scale = 5)
    private BigDecimal currentQty;

    @Column(name = "usedQty", precision = 25, scale = 5)
    private BigDecimal usedQty;

    @Column(name = "newQty", precision = 25, scale = 5)
    private BigDecimal newQty;

    @Column(name = "qty", precision = 25, scale = 5)
    private BigDecimal qty;

    @Transient
    private List<String> serials;

    @Transient
    private List<String> assignedSerials;

    @Transient
    private List<ProductTrackBatchItem> batchItems;

    @Transient
    private List<ProductTrackBatchItem> assignedBatchItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurementid")
    private EdsUnitMeasurement measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    public Integer getObjectID() {
        return objectID;
    }

    public EdsStockAdjustment getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(EdsStockAdjustment adjustment) {
        this.adjustment = adjustment;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    public BigDecimal getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(BigDecimal usedQty) {
        this.usedQty = usedQty;
    }

    public BigDecimal getNewQty() {
        return newQty;
    }

    public void setNewQty(BigDecimal newQty) {
        this.newQty = newQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }

    public List<String> getAssignedSerials() {
        return assignedSerials;
    }

    public void setAssignedSerials(List<String> assignedSerials) {
        this.assignedSerials = assignedSerials;
    }

    public List<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(List<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }

    public List<ProductTrackBatchItem> getAssignedBatchItems() {
        return assignedBatchItems;
    }

    public void setAssignedBatchItems(List<ProductTrackBatchItem> assignedBatchItems) {
        this.assignedBatchItems = assignedBatchItems;
    }

    public EdsUnitMeasurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(EdsUnitMeasurement measurement) {
        this.measurement = measurement;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public ProductItem getDataAsRPC() {
        ProductItem productItem = new ProductItem();
        productItem.setLineItemID(getObjectID());

        if (getItem() != null) {
            productItem.setObjectId(getItem().getObjectID());
            productItem.setProductNumber(item.getProductNumber());
            productItem.setName(item.getName());
            productItem.setDescription(item.getDescription());
        }
        productItem.setCurrentQty(currentQty);
        productItem.setUsedQty(usedQty);
        productItem.setNewQty(newQty);
        productItem.setTotalQty(qty);

        if (warehouse != null) {
            productItem.setWarehouseId(warehouse.getObjectID());
            productItem.setWarehouseName(warehouse.getName());
        }
        if (getProject() != null) {
            productItem.setProjectID(getProject().getObjectID());
            productItem.setProjectName(getProject().getName());
        }
        if (getAccount() != null) {
            productItem.setAccountID(getAccount().getObjectID());
            productItem.setAccount(getAccount().getName());
        }
        if (measurement != null) {
            productItem.setUnitMeasurementId(measurement.getObjectID());
            productItem.setUnitMeasurementName(measurement.getName());
        }

        if (getDepartment() != null) {
            productItem.setDepartmentId(getDepartment().getObjectID());
            productItem.setDepartmentName(getDepartment().getName());
        }
        BigDecimal unitPrice = getPrice() != null ? getPrice() : getItem() != null ? getItem().getUnitPrice() : null;
        productItem.setUnitpPrice(unitPrice);
        return productItem;
    }
}
