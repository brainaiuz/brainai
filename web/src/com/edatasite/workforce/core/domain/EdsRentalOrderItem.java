package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rental_order_item")
public class EdsRentalOrderItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_order_id")
    private EdsRentalOrder rentalOrder;

    @Type(type = "text")
    private String description;

    private Date fromDate;

    private Date toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem rentalItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id")
    private EdsItem productItem;

    @Column(precision = 14, scale = 4)
    private BigDecimal price;

    @Column(precision = 25, scale = 5)
    private BigDecimal qty;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsVat vat;

    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;

    @Column(name = "net", precision = 25, scale = 5)
    private BigDecimal net;

    @Column(name = "subTotal", precision = 25, scale = 5)
    private BigDecimal subTotal;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public EdsRentalOrder getRentalOrder() {
        return this.rentalOrder;
    }

    public void setRentalOrder(final EdsRentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public EdsItem getRentalItem() {
        return this.rentalItem;
    }

    public void setRentalItem(final EdsItem rentalItem) {
        this.rentalItem = rentalItem;
    }

    public EdsItem getProductItem() {
        return productItem;
    }

    public void setProductItem(EdsItem productItem) {
        this.productItem = productItem;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(final BigDecimal qty) {
        this.qty = qty;
    }

    public EdsVat getVat() {
        return this.vat;
    }

    public void setVat(final EdsVat vat) {
        this.vat = vat;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(final BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNet() {
        return this.net;
    }

    public void setNet(final BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getSubTotal() {
        return this.subTotal;
    }

    public void setSubTotal(final BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public RentalOrderItem toDTO() {
        RentalOrderItem rentalOrderItem = new RentalOrderItem();
        rentalOrderItem.setObjectID(getObjectID());
        rentalOrderItem.setOrderID(getRentalOrder().getObjectID());
        if (getRentalItem() != null) {
            rentalOrderItem.setRentalItem(getRentalItem().getAsProductSelectItem());
        }
        if (getProductItem() != null) {
            rentalOrderItem.setProductItem(getProductItem().getAsProductSelectItem());
        }
        rentalOrderItem.setPrice(getPrice());
        rentalOrderItem.setQty(getQty());
        rentalOrderItem.setDescription(getDescription());
        rentalOrderItem.setFromDate(getFromDate());
        rentalOrderItem.setToDate(getToDate());
        rentalOrderItem.setTaxAmount(getTaxAmount());
        rentalOrderItem.setNetAmount(getNet());
        rentalOrderItem.setSubTotal(getSubTotal());
        rentalOrderItem.setProductCategory(getRentalItem().getCategory() != null ? getRentalItem().getCategory().getAsSelectItem() : null);
        rentalOrderItem.setProductBrand(getRentalItem().getBrand() != null ? getRentalItem().getBrand().getAsSelectItem() : null);
        if (getVat() != null) {
            rentalOrderItem.setTaxItem(getVat().createTaxItem());
        }
        return rentalOrderItem;
    }
}
