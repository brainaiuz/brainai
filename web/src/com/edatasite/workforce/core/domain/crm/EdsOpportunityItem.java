package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
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

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/27/11
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "opportunity_item")
public class EdsOpportunityItem extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private EdsOpportunity opportunity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    public BigDecimal getItemCalculatedTaxAmount() {
        BigDecimal netAmount = qty.multiply(price);
        BigDecimal itemDiscount;
        if (getDiscount() != null) {
            itemDiscount = netAmount.multiply(getDiscount()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
        } else {
            itemDiscount = getDiscountAmount() != null ? getDiscountAmount() : AccountingConstants.ZERO;
        }

        BigDecimal discountedTotal = netAmount.subtract(itemDiscount);
        BigDecimal taxPercent = AccountingConstants.ZERO;
        if (getVat() != null) {
            taxPercent = getVat().getEffectiveRateAsBigDecimal();
        }

        if (getOpportunity().getTaxCalculationType() != null && AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(getOpportunity().getTaxCalculationType())) {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
        } else {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
        }
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        if (fieldID.equals(CustomFormConstants.ITEM)) {
            return getItem();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsOpportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(EdsOpportunity opportunity) {
        this.opportunity = opportunity;
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

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }
}
