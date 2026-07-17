package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "benefit")
public class EdsBenefit extends EdsObject {

    public static final String BENEFIT_QTY_TYPE = "_BENEFIT_QTYTYPE";
    public static final String _CURRENCY = "CURRENCY";
    public static final String _ANNUAL_LEAVE_ALLOWANCE_INCREASE = "ANNUAL_LEAVE_ALLOWANCE_INCREASE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer creditToAccountID;

    private Integer debitToAccountID;

    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private EdsReference type;//Cash, Non-cash, Both

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qtytype_id")
    private EdsReference qtytype;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "benefit_employee",
            joinColumns = {@JoinColumn(name = "benefit_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")})
    private Set<EdsEmployee> employees = new HashSet<>();

    private Boolean transferrable = false;

    private Date expireDate;
    private Date lastUpdateTime;

    private Boolean qtyRestriction;

    @Column(name = "description")
    private String description;

    @Column(name = "active", columnDefinition = " boolean DEFAULT true")
    private Boolean isActive;

    @Column(name = "allowance", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double allowance = 0.0;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCreditToAccountID() {
        return creditToAccountID;
    }

    public void setCreditToAccountID(Integer creditToAccountID) {
        this.creditToAccountID = creditToAccountID;
    }

    public Integer getDebitToAccountID() {
        return debitToAccountID;
    }

    public void setDebitToAccountID(Integer debitToAccountID) {
        this.debitToAccountID = debitToAccountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public EdsReference getQtytype() {
        return qtytype;
    }

    public void setQtytype(EdsReference qtytype) {
        this.qtytype = qtytype;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public Boolean getTransferrable() {
        return transferrable;
    }

    public void setTransferrable(Boolean transferrable) {
        this.transferrable = transferrable;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getQtyRestriction() {
        return qtyRestriction;
    }

    public void setQtyRestriction(Boolean qtyRestriction) {
        this.qtyRestriction = qtyRestriction;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<EdsEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EdsEmployee> employees) {
        this.employees = employees;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public BenefitItem toBenefitItem() {
        BenefitItem benefitItem = new BenefitItem();
        benefitItem.setObjectId(getObjectID());
        benefitItem.setName(getName());
        benefitItem.setCode(getCode());
        benefitItem.setType(getType() != null ? getType().getName() : "");
        benefitItem.setTypeID(getType() != null ? getType().getObjectID() : null);
        benefitItem.setTransferrable(getTransferrable());
        benefitItem.setQtyRestriction(getQtyRestriction());
        benefitItem.setCurrency(getCurrency() != null ? getCurrency().getName() : "");
        benefitItem.setCurrencyID(getCurrency() != null ? getCurrency().getObjectID() : null);
        benefitItem.setQtytype(getQtytype() != null ? getQtytype().getName() : "");
        benefitItem.setQtytypeID(getQtytype() != null ? getQtytype().getObjectID() : null);
        if (getExpireDate() != null) {
            benefitItem.setExpireDate(new DateNonConvertable(getExpireDate()));
        }
        benefitItem.setDescription(getDescription());
        benefitItem.setActive(isActive());
        benefitItem.setAllowance(getAllowance());
        return benefitItem;
    }
}
