package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsVat;
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

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 5.04.2019
 * Time: 20:15:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bill_of_entry_item")
public class EdsBillOfEntryItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "billofentry_id")
    private Integer billofentry_id;

    @Column(name = "assessable_value", precision = 25, scale = 5)
    private BigDecimal assessableValue;

    @Column(name = "custom_duty_additional_charges", precision = 25, scale = 5)
    private BigDecimal customDutyAdditionalCharges;

    @Column(name = "taxable_amount", precision = 25, scale = 5)
    private BigDecimal taxableAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @Column(name = "item_name")
    @Type(type = "text")
    private String itemName;

    @Type(type = "text")
    private String categoryName;

    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private EdsVat tax;

    private Integer sorder;

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public Integer getBillofentry_id() {
        return billofentry_id;
    }

    public void setBillofentry_id(Integer billofentry_id) {
        this.billofentry_id = billofentry_id;
    }

    public BigDecimal getAssessableValue() {
        return assessableValue;
    }

    public void setAssessableValue(BigDecimal assessableValue) {
        this.assessableValue = assessableValue;
    }

    public BigDecimal getCustomDutyAdditionalCharges() {
        return customDutyAdditionalCharges;
    }

    public void setCustomDutyAdditionalCharges(BigDecimal customDutyAdditionalCharges) {
        this.customDutyAdditionalCharges = customDutyAdditionalCharges;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }
}
