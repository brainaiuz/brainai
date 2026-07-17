package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;

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
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 14.05.14
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "eos_calculation")
public class EdsEosCalculation extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsEmployee creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "payslip_id")
    private P11 payslip;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "single_payrun_id")
    private EdsPayslipTableItem singlePayrun;

    @Column(name = "totalAmount", precision = 14, scale = 4)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private String reasonCode;
    private String paymentNumber;
    private Date creationDate;
    private Date hireDate;
    private Date resignationDate;
    private Integer totalWorkedDays;
    private Boolean deleted = false;
    private Integer fourDigitNumber;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsEmployee getCreator() {
        return creator;
    }

    public void setCreator(EdsEmployee creator) {
        this.creator = creator;
    }

    public P11 getPayslip() {
        return payslip;
    }

    public void setPayslip(P11 payslip) {
        this.payslip = payslip;
    }

    public EdsPayslipTableItem getSinglePayrun() {
        return singlePayrun;
    }

    public void setSinglePayrun(EdsPayslipTableItem singlePayrun) {
        this.singlePayrun = singlePayrun;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
    }

    public Integer getTotalWorkedDays() {
        return totalWorkedDays;
    }

    public void setTotalWorkedDays(Integer totalWorkedDays) {
        this.totalWorkedDays = totalWorkedDays;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setFourDigitNumber(Integer fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }

    public Integer getFourDigitNumber() {
        return fourDigitNumber;
    }
}
