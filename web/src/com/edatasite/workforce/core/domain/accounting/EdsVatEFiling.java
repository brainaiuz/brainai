package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnTransferObject;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.08.2010
 * Time: 14:49:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "vatefiling")
public class EdsVatEFiling extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String registrationNumber;       //Registration Number
    private String vatScheme;                //VAT Scheme

    private Date fromDate;                   //From
    private Date toDate;                     //To
    private Date submissionDate;             //Submission Date to HMRC

    private Integer status;                    //Status of this vat report;  1 = SUBMISSION_COMPLETED;  2 = SUBMISSION_PENDING;  3 = SUBMISSION_FAILED;

    //VAT Calculations
    @Column(precision = 25, scale = 5)
    private BigDecimal vatOnSalesAndOutputs; //Box1. VAT due this period on sales and other outputs
    @Column(precision = 25, scale = 5)
    private BigDecimal vatFromECMemberStates; //Box2. VAT due in this period on acquisitions from other EC Member States
    @Column(precision = 25, scale = 5)
    private BigDecimal totalVatDue; //Box3. Total VAT due (the sum of boxes 1 and 2)
    @Column(precision = 25, scale = 5)
    private BigDecimal vatOnPurchaseAndInputs; //Box4. VAT reclaimed in this period on purchases and other inputs (including acquisitions from EC)
    @Column(precision = 25, scale = 5)
    private BigDecimal vatToReclaimFromCustoms; //Box5. VAT to Reclaim from Customs

    //Sales and Purchases Excluding VAT
    @Column(precision = 25, scale = 5)
    private BigDecimal totalSalesAndOutputs; //Box6. Total value of sales and all other outputs excluding VAT (including supplies to EC)
    @Column(precision = 25, scale = 5)
    private BigDecimal totalPurchasesAndInputs; //Box7. Total value of purchases and all other inputs excluding VAT (including acquisitions from EC)

    //EC Supplies and Purchases Excluding VAT
    @Column(precision = 25, scale = 5)
    private BigDecimal totalSupplies; //Box8. Total value of all supplies of goods, excluding any VAT, to other EC Member States
    @Column(precision = 25, scale = 5)
    private BigDecimal totalAcquisitions; //Box9. Total value of all acquisitions of goods, excluding any VAT, from EC Member States

    @Column(name = "submitterid")
    private Integer submitter;

    private Integer companyID;

    @Type(type = "text")
    private String irMarkXML;

    @Type(type = "text")
    private String irMarkValue;

    @Type(type = "text")
    private String submissionXML;

    @Type(type = "text")
    private String responseContent;

    private String hmrcReference;

    private String errorCode;

    private String message;

    private BigDecimal flatRatePercent;

    private Boolean flatRate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVatScheme() {
        return vatScheme;
    }

    public void setVatScheme(String vatScheme) {
        this.vatScheme = vatScheme;
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

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getVatOnSalesAndOutputs() {
        return vatOnSalesAndOutputs;
    }

    public void setVatOnSalesAndOutputs(BigDecimal vatOnSalesAndOutputs) {
        this.vatOnSalesAndOutputs = vatOnSalesAndOutputs;
    }

    public BigDecimal getVatFromECMemberStates() {
        return vatFromECMemberStates;
    }

    public void setVatFromECMemberStates(BigDecimal vatFromECMemberStates) {
        this.vatFromECMemberStates = vatFromECMemberStates;
    }

    public BigDecimal getTotalVatDue() {
        return totalVatDue;
    }

    public void setTotalVatDue(BigDecimal totalVatDue) {
        this.totalVatDue = totalVatDue;
    }

    public BigDecimal getVatOnPurchaseAndInputs() {
        return vatOnPurchaseAndInputs;
    }

    public void setVatOnPurchaseAndInputs(BigDecimal vatOnPurchaseAndInputs) {
        this.vatOnPurchaseAndInputs = vatOnPurchaseAndInputs;
    }

    public BigDecimal getVatToReclaimFromCustoms() {
        return vatToReclaimFromCustoms;
    }

    public void setVatToReclaimFromCustoms(BigDecimal vatToReclaimFromCustoms) {
        this.vatToReclaimFromCustoms = vatToReclaimFromCustoms;
    }

    public BigDecimal getTotalSalesAndOutputs() {
        return totalSalesAndOutputs;
    }

    public void setTotalSalesAndOutputs(BigDecimal totalSalesAndOutputs) {
        this.totalSalesAndOutputs = totalSalesAndOutputs;
    }

    public BigDecimal getTotalPurchasesAndInputs() {
        return totalPurchasesAndInputs;
    }

    public void setTotalPurchasesAndInputs(BigDecimal totalPurchasesAndInputs) {
        this.totalPurchasesAndInputs = totalPurchasesAndInputs;
    }

    public BigDecimal getTotalSupplies() {
        return totalSupplies;
    }

    public void setTotalSupplies(BigDecimal totalSupplies) {
        this.totalSupplies = totalSupplies;
    }

    public BigDecimal getTotalAcquisitions() {
        return totalAcquisitions;
    }

    public void setTotalAcquisitions(BigDecimal totalAcquisitions) {
        this.totalAcquisitions = totalAcquisitions;
    }

    public Integer getSubmitter() {
        return submitter;
    }

    public void setSubmitter(Integer submitter) {
        this.submitter = submitter;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getIrMarkXML() {
        return irMarkXML;
    }

    public void setIrMarkXML(String irMarkXML) {
        this.irMarkXML = irMarkXML;
    }

    public String getIrMarkValue() {
        return irMarkValue;
    }

    public void setIrMarkValue(String irMarkValue) {
        this.irMarkValue = irMarkValue;
    }

    public String getSubmissionXML() {
        return submissionXML;
    }

    public void setSubmissionXML(String submissionXML) {
        this.submissionXML = submissionXML;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getHmrcReference() {
        return hmrcReference;
    }

    public void setHmrcReference(String hmrcReference) {
        this.hmrcReference = hmrcReference;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getFlatRatePercent() {
        return flatRatePercent;
    }

    public void setFlatRatePercent(BigDecimal flatRatePercent) {
        this.flatRatePercent = flatRatePercent;
    }

    public Boolean getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(Boolean flatRate) {
        this.flatRate = flatRate;
    }

    public VatReturnTransferObject createTransferObject() {
        VatReturnTransferObject transObject = new VatReturnTransferObject();
        transObject.setObjectID(objectID);
        transObject.setFrom(new DateNonConvertable(fromDate));
        transObject.setTo(new DateNonConvertable(toDate));
        transObject.setVatOnSalesAndOutputs(vatOnSalesAndOutputs);
        transObject.setVatFromECMemberStates(vatFromECMemberStates);
        transObject.setTotalVatDue(totalVatDue);
        transObject.setVatOnPurchaseAndInputs(vatOnPurchaseAndInputs);
        transObject.setVatToReclaimFromCustoms(vatToReclaimFromCustoms);
        transObject.setTotalSalesAndOutputs(totalSalesAndOutputs);
        transObject.setTotalPurchasesAndInputs(totalPurchasesAndInputs);
        transObject.setTotalSupplies(totalSupplies);
        transObject.setTotalAcquisitions(totalAcquisitions);
        transObject.setResponseContent(message);
        transObject.setStatus(status);
        return transObject;
    }
}
