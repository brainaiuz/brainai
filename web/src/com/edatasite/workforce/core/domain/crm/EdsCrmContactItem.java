package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Type;

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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "lead_item")
public class EdsCrmContactItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmContact_id")
    private EdsCrmContact crmContact;

    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    private String itemName;

    @Column(precision = 14, scale = 4)
    private BigDecimal price;

    @Column(precision = 25, scale = 5)
    private BigDecimal qty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitmeasurementid")
    private EdsUnitMeasurement unitMeasurement;

    private Integer supplierID;
    private String supplierName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsItemCustomFields customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private EdsProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandid")
    private EdsBrand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsVat vat;

    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;

    @Column(name = "net", precision = 25, scale = 5)
    private BigDecimal net;

    @Column(name = "subtotal", precision = 25, scale = 5)
    private BigDecimal subTotal;

    public BigDecimal getItemCalculatedTaxAmount() {
        BigDecimal netAmount = qty.multiply(price);
        BigDecimal itemDiscount;
        itemDiscount = netAmount.divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);

        BigDecimal discountedTotal = netAmount.subtract(itemDiscount);
        BigDecimal taxPercent = AccountingConstants.ZERO;
        if (getVat() != null) {
            taxPercent = getVat().getEffectiveRateAsBigDecimal();
        }
        return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        this.crmContact = crmContact;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public EdsUnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(EdsUnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
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

    public EdsItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsProductCategory getCategory() {
        return category;
    }

    public void setCategory(EdsProductCategory category) {
        this.category = category;
    }

    public EdsBrand getBrand() {
        return brand;
    }

    public void setBrand(EdsBrand brand) {
        this.brand = brand;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }
}
