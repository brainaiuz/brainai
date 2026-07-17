package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.customfields.EdsCrmSubItemCustomFields;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmsub_item")
public class EdsCrmSubItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer entityId;

    private String entityType;

    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @Column(precision = 14, scale = 4)
    private BigDecimal price;

    @Column(precision = 25, scale = 5)
    private BigDecimal qty;

    @Column(name = "discount", precision = 25, scale = 5)
    private BigDecimal discount;

    @Column(name = "discount_amount", precision = 25, scale = 5)
    private BigDecimal discountAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_discount_id")
    private EdsDiscount itemDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitmeasurementid")
    private EdsUnitMeasurement unitMeasurement;

    @Column(name = "discountItemFixedType")
    private Integer discountItemFixedType;

    private Integer supplierID;
    private String supplierName;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmSubItemCustomFields customFields;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public EdsDiscount getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(EdsDiscount itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public EdsUnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(EdsUnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public Integer getDiscountItemFixedType() {
        return discountItemFixedType;
    }

    public void setDiscountItemFixedType(Integer discountItemFixedType) {
        this.discountItemFixedType = discountItemFixedType;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public EdsCrmSubItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmSubItemCustomFields customFields) {
        this.customFields = customFields;
    }
}
