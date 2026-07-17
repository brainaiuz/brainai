package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 16.02.2009
 * Time: 21:00:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "Payslip")
public class P11 extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private EdsEmployee employee;

    @Column(name = "PAYEYear")
    private Integer year;

    @Column(name = "NITableLetter")
    private String tableLetter;

    @Column(name = "ToLEL")
    private BigDecimal toLEL; //1a

    @Column(name = "LELToET")
    private BigDecimal lelToET; //1b

    @Column(name = "ETtoUAP")
    private BigDecimal etToUAP; //1c

    @Column(name = "UAPtoUEL")
    private BigDecimal uapToUEL; //1d

    @Column(name = "AboveUEL")
    private BigDecimal aboveUEL;

    @Column(name = "NIEE")
    private BigDecimal employeeNI; //1f - employees contribution payable on earnings in 1c and 1d

    @Column(name = "NIER")
    private BigDecimal employerNI;

    @Column(name = "NITotal")
    private BigDecimal totalNI; //1e - total/sum of employees and employers contributions payable

    private BigDecimal ssp;

    private BigDecimal smp;

    private BigDecimal spp;

    private BigDecimal sap;

    private BigDecimal studentLoanDeductions;

    @Column(name = "TaxCode")
    private String taxCode;

    @Column(name = "Tax")
    private BigDecimal tax;

//    private BigDecimal additionalPaymentRate;
//
//    private BigDecimal additionalDeductionRate;

    private BigDecimal totalBonus;

    private BigDecimal grossPayInPeriod;//pay in current week/month

    private BigDecimal taxablePayInPeriod;//taxable pay in current week/month

    private BigDecimal totalPayToDate;

    private BigDecimal totalFreePay;

    private BigDecimal totalAdditionalPay;  //K Code

    private BigDecimal totalTaxablePayToDate;

    private BigDecimal totalTaxDue;

    private BigDecimal taxDueEndCurrPeriod;   //K Code

    private BigDecimal regulatoryLimit;    //K Code

    private BigDecimal taxDeductedRefunded;

    private BigDecimal taxNotDeducted;    //K code only

    @Column(name = "paymentmethod")
    private String paymentmethod;
    @Column(name = "payment_policy")
    @Type(type = "text")
    private String paymentPolicy;

    @Column(name = "TotalTax")
    private BigDecimal totalTax; //The total tax payed until today

    @Column(name = "NetPay")
    private BigDecimal netPay;//The total "Chistiy" pay "na ruki!!! :)"

    //period related should be instead of payperiod field
    @Column(name = "Frequency")
    private Integer frequency; //0-week, 1-month, 2- year (see Frequency)

    @Column(name = "Period")
    private Integer payPeriod; //week or month number

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private P11 parent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private EdsReference status;

    private Date processDate;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean isOnIndustrialAction;

    private Boolean deleted = false;

    private Boolean fromGroupPayrun;

    private Boolean fromEndOfService;

    private Boolean sendCopyToEmployee;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(Integer payPeriod) {
        this.payPeriod = payPeriod;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTableLetter() {
        return tableLetter;
    }

    public void setTableLetter(String tableLetter) {
        this.tableLetter = tableLetter;
    }

    public BigDecimal getToLEL() {
        return toLEL;
    }

    public void setToLEL(BigDecimal toLEL) {
        this.toLEL = toLEL;
    }

    public BigDecimal getLelToET() {
        return lelToET;
    }

    public void setLelToET(BigDecimal lelToET) {
        this.lelToET = lelToET;
    }

    public BigDecimal getEtToUAP() {
        return etToUAP;
    }

    public void setEtToUAP(BigDecimal etToUAP) {
        this.etToUAP = etToUAP;
    }

    public BigDecimal getUapToUEL() {
        return uapToUEL;
    }

    public void setUapToUEL(BigDecimal uapToUEL) {
        this.uapToUEL = uapToUEL;
    }

    public BigDecimal getAboveUEL() {
        return aboveUEL;
    }

    public void setAboveUEL(BigDecimal aboveUEL) {
        this.aboveUEL = aboveUEL;
    }

    public BigDecimal getEmployeeNI() {
        return employeeNI;
    }

    public void setEmployeeNI(BigDecimal employeeNI) {
        this.employeeNI = employeeNI;
    }

    public BigDecimal getEmployerNI() {
        return employerNI;
    }

    public void setEmployerNI(BigDecimal employerNI) {
        this.employerNI = employerNI;
    }

    public BigDecimal getTotalNI() {
        return totalNI;
    }

    public void setTotalNI(BigDecimal totalNI) {
        this.totalNI = totalNI;
    }

    public BigDecimal getSsp() {
        return ssp;
    }

    public void setSsp(BigDecimal ssp) {
        this.ssp = ssp;
    }

    public BigDecimal getSmp() {
        return smp;
    }

    public void setSmp(BigDecimal smp) {
        this.smp = smp;
    }

    public BigDecimal getSpp() {
        return spp;
    }

    public void setSpp(BigDecimal spp) {
        this.spp = spp;
    }

    public BigDecimal getSap() {
        return sap;
    }

    public void setSap(BigDecimal sap) {
        this.sap = sap;
    }

    public BigDecimal getStudentLoanDeductions() {
        return studentLoanDeductions;
    }

    public void setStudentLoanDeductions(BigDecimal studentLoanDeductions) {
        this.studentLoanDeductions = studentLoanDeductions;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

//    public BigDecimal getAdditionalPaymentRate() {
//        return additionalPaymentRate;
//    }
//
//    public void setAdditionalPaymentRate(BigDecimal additionalPaymentRate) {
//        this.additionalPaymentRate = additionalPaymentRate;
//    }
//
//    public BigDecimal getAdditionalDeductionRate() {
//        return additionalDeductionRate;
//    }
//
//    public void setAdditionalDeductionRate(BigDecimal additionalDeductionRate) {
//        this.additionalDeductionRate = additionalDeductionRate;
//    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public BigDecimal getGrossPayInPeriod() {
        return grossPayInPeriod;
    }

    public void setGrossPayInPeriod(BigDecimal payInPeriod) {
        this.grossPayInPeriod = payInPeriod;
    }

    public BigDecimal getTaxablePayInPeriod() {
        return taxablePayInPeriod;
    }

    public void setTaxablePayInPeriod(BigDecimal taxablePayInPeriod) {
        this.taxablePayInPeriod = taxablePayInPeriod;
    }

    public BigDecimal getTotalPayToDate() {
        return totalPayToDate;
    }

    public void setTotalPayToDate(BigDecimal totalPayToDate) {
        this.totalPayToDate = totalPayToDate;
    }

    public BigDecimal getTotalFreePay() {
        return totalFreePay;
    }

    public void setTotalFreePay(BigDecimal totalFreePay) {
        this.totalFreePay = totalFreePay;
    }

    public BigDecimal getTotalAdditionalPay() {
        return totalAdditionalPay;
    }

    public void setTotalAdditionalPay(BigDecimal totalAdditionalPay) {
        this.totalAdditionalPay = totalAdditionalPay;
    }

    public BigDecimal getTotalTaxablePayToDate() {
        return totalTaxablePayToDate;
    }

    public void setTotalTaxablePayToDate(BigDecimal totalTaxablePayToDate) {
        this.totalTaxablePayToDate = totalTaxablePayToDate;
    }

    public BigDecimal getTotalTaxDue() {
        return totalTaxDue;
    }

    public void setTotalTaxDue(BigDecimal totalTaxDue) {
        this.totalTaxDue = totalTaxDue;
    }

    public BigDecimal getTaxDueEndCurrPeriod() {
        return taxDueEndCurrPeriod;
    }

    public void setTaxDueEndCurrPeriod(BigDecimal taxDueEndCurrPeriod) {
        this.taxDueEndCurrPeriod = taxDueEndCurrPeriod;
    }

    public BigDecimal getRegulatoryLimit() {
        return regulatoryLimit;
    }

    public void setRegulatoryLimit(BigDecimal regulatoryLimit) {
        this.regulatoryLimit = regulatoryLimit;
    }

    public BigDecimal getTaxDeductedRefunded() {
        return taxDeductedRefunded;
    }

    public void setTaxDeductedRefunded(BigDecimal taxDeductedRefunded) {
        this.taxDeductedRefunded = taxDeductedRefunded;
    }

    public BigDecimal getTaxNotDeducted() {
        return taxNotDeducted;
    }

    public void setTaxNotDeducted(BigDecimal taxNotDeducted) {
        this.taxNotDeducted = taxNotDeducted;
    }

    public P11 getParent() {
        return parent;
    }

    public void setParent(P11 parent) {
        this.parent = parent;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public boolean isOnIndustrialAction() {
        return isOnIndustrialAction;
    }

    public void setOnIndustrialAction(boolean onIndustrialAction) {
        isOnIndustrialAction = onIndustrialAction;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isFromGroupPayrun() {
        return fromGroupPayrun != null ? fromGroupPayrun : false;
    }

    public void setFromGroupPayrun(Boolean fromGroupPayrun) {
        this.fromGroupPayrun = fromGroupPayrun;
    }

    public Boolean isFromEndOfService() {
        return fromEndOfService != null ? fromEndOfService : false;
    }

    public void setFromEndOfService(Boolean fromEndOfService) {
        this.fromEndOfService = fromEndOfService;
    }

    public Boolean getSendCopyToEmployee() {
        return sendCopyToEmployee !=null ? sendCopyToEmployee : false;
    }

    public void setSendCopyToEmployee(Boolean sendCopyToEmployee) {
        this.sendCopyToEmployee = sendCopyToEmployee;
    }
}
