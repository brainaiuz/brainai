package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 24, 2010
 * Time: 5:32:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "picklistitem")
public class EdsPickListItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picklist_id")
    private EdsPickList pickList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    @Column(precision = 25, scale = 5)
    private BigDecimal quantity;

    @Column(precision = 25, scale = 5)
    private BigDecimal picked;

    @Column(precision = 25, scale = 5)
    private BigDecimal packed;

    @Column(precision = 25, scale = 5)
    private BigDecimal shipped;

    @Column(precision = 25, scale = 5)
    private BigDecimal numberOfPacks;

    @Column(precision = 25, scale = 5)
    private BigDecimal qtyPerPack;

    @Column(name = "box")
    private String box;

    private String reference;

    private Date shipDate;
    private Date expectedDate;
    private String carrierAccount;

    public EdsPickListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsPickList getPickList() {
        return pickList;
    }

    public void setPickList(EdsPickList pickList) {
        this.pickList = pickList;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getShipped() {
        return shipped;
    }

    public void setShipped(BigDecimal shipped) {
        this.shipped = shipped;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getCarrierAccount() {
        return carrierAccount;
    }

    public void setCarrierAccount(String carrierAccount) {
        this.carrierAccount = carrierAccount;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public BigDecimal getPacked() {
        return packed;
    }

    public void setPacked(BigDecimal packed) {
        this.packed = packed;
    }

    public BigDecimal getPicked() {
        return picked;
    }

    public void setPicked(BigDecimal picked) {
        this.picked = picked;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setNumberOfPacks(BigDecimal numberOfPacks) {
        this.numberOfPacks = numberOfPacks;
    }

    public BigDecimal getNumberOfPacks() {
        return numberOfPacks;
    }

    public void setQtyPerPack(BigDecimal qtyPerPack) {
        this.qtyPerPack = qtyPerPack;
    }

    public BigDecimal getQtyPerPack() {
        return qtyPerPack;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
