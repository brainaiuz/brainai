package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 9:26:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemeData implements IsSerializable {
    private Integer pensionSchemaId;
    private String schemeName;
    private Integer providerId;
    private String providerName;
    private Integer schemeType;
    private String schemeTypeName;
    private String otherAcRef;

    private Integer deductionType;
    private Integer deductFrom;
    private BigDecimal deductionValue;
    private BigDecimal nonLocalDeductionValue;
    private String employeeContribution;
    private String employerContribution;
    private Integer allowTaxRelief;
    private Integer reduceByBasicRateTax;
    private Integer sspPayment;
    private Integer smpPayment;
    private Integer sapPayment;
    private Integer sppPayment;

    private Integer employerDeductionType;
    private BigDecimal employerDeductionValue;
    private BigDecimal employerNonLocalDeductionValue;
    private Integer employerSspPayment;
    private Integer employerSmpPayment;
    private Integer employerSapPayment;
    private Integer employerSppPayment;

    private BigDecimal empMaxTaxableAmount = BigDecimal.ZERO;
    private BigDecimal compMaxTaxableAmount = BigDecimal.ZERO;

    private Integer wagesInsufficient;
    private ArrayList<PaymentDeductionSelectItem> allowances;

    public Integer getObjectId() {
        return pensionSchemaId;
    }

    public void setObjectId(Integer pensionSchemaId) {
        this.pensionSchemaId = pensionSchemaId;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getSchemeType() {
        return schemeType;
    }

    public String getSchemeTypeName() {
        return schemeTypeName;
    }

    public void setSchemeTypeName(String schemeTypeName) {
        this.schemeTypeName = schemeTypeName;
    }

    public String getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(String employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public String getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(String employerContribution) {
        this.employerContribution = employerContribution;
    }


    public void setSchemeType(Integer schemeType) {
        this.schemeType = schemeType;
    }

    public String getOtherAcRef() {
        return otherAcRef;
    }

    public void setOtherAcRef(String otherAcRef) {
        this.otherAcRef = otherAcRef;
    }

    public Integer getDeductionType() {
        return deductionType;
    }

    public void setDeductionType(Integer deductionType) {
        this.deductionType = deductionType;
    }

    public BigDecimal getDeductionValue() {
        return deductionValue;
    }

    public void setDeductionValue(BigDecimal deductionValue) {
        this.deductionValue = deductionValue;
    }

    public BigDecimal getNonLocalDeductionValue() {
        return nonLocalDeductionValue;
    }

    public void setNonLocalDeductionValue(BigDecimal nonLocalDeductionValue) {
        this.nonLocalDeductionValue = nonLocalDeductionValue;
    }

    public Integer getDeductFrom() {
        return deductFrom;
    }

    public void setDeductFrom(Integer deductFrom) {
        this.deductFrom = deductFrom;
    }

    public Integer getAllowTaxRelief() {
        return allowTaxRelief;
    }

    public void setAllowTaxRelief(Integer allowTaxRelief) {
        this.allowTaxRelief = allowTaxRelief;
    }

    public Integer getReduceByBasicRateTax() {
        return reduceByBasicRateTax;
    }

    public void setReduceByBasicRateTax(Integer reduceByBasicRateTax) {
        this.reduceByBasicRateTax = reduceByBasicRateTax;
    }

    public Integer getSspPayment() {
        return sspPayment;
    }

    public void setSspPayment(Integer sspPayment) {
        this.sspPayment = sspPayment;
    }

    public Integer getSmpPayment() {
        return smpPayment;
    }

    public void setSmpPayment(Integer smpPayment) {
        this.smpPayment = smpPayment;
    }

    public Integer getSapPayment() {
        return sapPayment;
    }

    public void setSapPayment(Integer sapPayment) {
        this.sapPayment = sapPayment;
    }

    public Integer getSppPayment() {
        return sppPayment;
    }

    public void setSppPayment(Integer sppPayment) {
        this.sppPayment = sppPayment;
    }

    public Integer getEmployerDeductionType() {
        return employerDeductionType;
    }

    public void setEmployerDeductionType(Integer employerDeductionType) {
        this.employerDeductionType = employerDeductionType;
    }

    public BigDecimal getEmployerDeductionValue() {
        return employerDeductionValue;
    }

    public void setEmployerDeductionValue(BigDecimal employerDeductionValue) {
        this.employerDeductionValue = employerDeductionValue;
    }

    public BigDecimal getEmployerNonLocalDeductionValue() {
        return employerNonLocalDeductionValue;
    }

    public void setEmployerNonLocalDeductionValue(BigDecimal employerNonLocalDeductionValue) {
        this.employerNonLocalDeductionValue = employerNonLocalDeductionValue;
    }

    public ArrayList<PaymentDeductionSelectItem> getAllowances() {
        if (allowances == null) {
            allowances = new ArrayList<>();
        }
        return allowances;
    }

    public void setAllowances(ArrayList<PaymentDeductionSelectItem> allowances) {
        this.allowances = allowances;
    }

    public Integer getEmployerSspPayment() {
        return employerSspPayment;
    }

    public void setEmployerSspPayment(Integer employerSspPayment) {
        this.employerSspPayment = employerSspPayment;
    }

    public Integer getEmployerSmpPayment() {
        return employerSmpPayment;
    }

    public void setEmployerSmpPayment(Integer employerSmpPayment) {
        this.employerSmpPayment = employerSmpPayment;
    }

    public Integer getEmployerSapPayment() {
        return employerSapPayment;
    }

    public void setEmployerSapPayment(Integer employerSapPayment) {
        this.employerSapPayment = employerSapPayment;
    }

    public Integer getEmployerSppPayment() {
        return employerSppPayment;
    }

    public void setEmployerSppPayment(Integer employerSppPayment) {
        this.employerSppPayment = employerSppPayment;
    }

    public Integer getWagesInsufficient() {
        return wagesInsufficient;
    }

    public void setWagesInsufficient(Integer wagesInsufficient) {
        this.wagesInsufficient = wagesInsufficient;
    }

    public BigDecimal getEmpMaxTaxableAmount() {
        return empMaxTaxableAmount != null ? empMaxTaxableAmount : BigDecimal.ZERO;
    }

    public void setEmpMaxTaxableAmount(BigDecimal empMaxTaxableAmount) {
        this.empMaxTaxableAmount = empMaxTaxableAmount;
    }

    public BigDecimal getCompMaxTaxableAmount() {
        return compMaxTaxableAmount != null ? compMaxTaxableAmount : BigDecimal.ZERO;
    }

    public void setCompMaxTaxableAmount(BigDecimal compMaxTaxableAmount) {
        this.compMaxTaxableAmount = compMaxTaxableAmount;
    }
}
