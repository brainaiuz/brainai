package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.LeavePaymentItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 16.02.2009
 * Time: 21:20:01
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "PaymentDeduction")
public class EdsPaymentDeduction extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "categoryID")
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "categoryID", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsPayrollCategory category;

    @Column(name = "employeeID")
    private Integer employeeId;
    @ManyToOne
    @JoinColumn(name = "employeeID", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsEmployee employee;

    @ManyToOne
    @JoinColumn(name = "candidateID")
    private EdsCrmContact candidate;

    @ManyToOne
    @JoinColumn(name = "employeeTemplateID")
    private EdsEmployeePayrollSettingsTemplate employeeTemplate;

    @Column(name = "pds_id")
    private Integer payrollGlobalSettingsItemId;
    @ManyToOne
    @JoinColumn(name = "pds_id", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none"))
    private EdsPayrollGlobalSettingsItem payrollGlobalSettingsItem;

    @ManyToOne
    @JoinColumn(name = "addpayment_id")
    private EdsAdditionalPayment additionalPayment;

    @Column(name = "PaymentAmount")
    private BigDecimal paymentAmount;

    @Column(name = "RateAmount")
    private BigDecimal rateAmount;

    @Column(name = "leaveDaysCount")
    private BigDecimal leaveDaysCount;

    @Column(name = "PaymentDate")
    private Date paymentDate;


    @Column(name = "additionalPaymentDate")
    private Date additionalPaymentDate;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//    @JoinTable(schema = org.postgresql.core.v3.EdsScope.PRIVATE_SCHEMA,
//            name = "payslip_category_relation",
//            joinColumns = {@JoinColumn(name = "pay_ded_id")},
//            inverseJoinColumns = {@JoinColumn(name = "payslipId")}
//    )
//    private List<P11> payslips = new ArrayList<P11>();

    @ManyToOne
    @JoinColumn(name = "StatusID")
    private EdsReference status;

    @ManyToOne
    @JoinColumn(name = "leavereasonid")
    private EdsReference leavereason;

    //For Advance Payments
    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    private Integer numberOfPeriods;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    private BigDecimal percentage;

    private BigDecimal commission;

    @Column(name = "pay_type")
    private Integer payType; //0-Fixed, 1-Percentage from Basic Salary, 2-Percentage from Basic Salary + Allowances

    private Integer leaveMinutes;
    private Integer leavePaymentYear;
    private Integer leaveDays;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentDeductionsCategories",
            joinColumns = {@JoinColumn(name = "paymentDeductionId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrollCategory> linkedCategories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentDeduction")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrollPaymentItem> paymentItems = new HashSet<>();

    @Column(name = "basicSalaryAmount")
    private BigDecimal basicSalaryAmount;

    @Column(name = "basicAndadditionalAmount")
    private BigDecimal basicAndadditionalAmount;

    private Boolean fromAllAllowances;

    private Boolean fullPayed;

    private Boolean deleted;

    private Boolean isRecurring;

    private Boolean isSalaryObject;

    private Integer cashAdvanceID;

    private Integer recurringPayDeductionID;

    private Integer parentIdForTemplate;

    private String remarks;

    @Enumerated(EnumType.STRING)
    private EPPaymentType paymentType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_deduction_id")
    private final Set<EdsPayslipPayments> payments = new HashSet<>();

    @Transient
    private Integer payslipCategoryDays;
    @Transient
    private Integer payslipCategoryMinutes;
    @Transient
    private BigDecimal totalPayment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldid")
    private EdsItemCustomFields customFields;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "employerContribution")
    private BigDecimal employerContribution;

    @Column(name = "deduction")
    private BigDecimal deduction;
    @Column(name = "taxCategoryList")
    @Type(type = "text")
    private String taxCategoryList;

    @Column(name = "employerContributionCategoryList")
    @Type(type = "text")
    private String employerContributionCategoryList;
    @Column(name = "customDeductionCategoryList")
    @Type(type = "text")
    private String customDeductionCategoryList;

    @Column(name = "basicSalaryPartAmount")
    private BigDecimal basicSalaryPartAmount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        if (category != null) {
            this.category = category;
            this.categoryId = category.getObjectID();
        }
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employeeId = employee != null ? employee.getObjectID() : null;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        this.candidate = candidate;
    }

    public EdsEmployeePayrollSettingsTemplate getEmployeeTemplate() {
        return employeeTemplate;
    }

    public void setEmployeeTemplate(EdsEmployeePayrollSettingsTemplate employeeTemplate) {
        this.employeeTemplate = employeeTemplate;
    }

    public Integer getPayrollGlobalSettingsItemId() {
        return payrollGlobalSettingsItemId;
    }

    public void setPayrollGlobalSettingsItemId(Integer payrollGlobalSettingsItemId) {
        this.payrollGlobalSettingsItemId = payrollGlobalSettingsItemId;
    }

    public EdsPayrollGlobalSettingsItem getPayrollGlobalSettingsItem() {
        return payrollGlobalSettingsItem;
    }

    public void setPayrollGlobalSettingsItem(EdsPayrollGlobalSettingsItem payrollGlobalSettingsItem) {
        this.payrollGlobalSettingsItem = payrollGlobalSettingsItem;
    }

    public EdsAdditionalPayment getAdditionalPayment() {
        return additionalPayment;
    }

    public void setAdditionalPayment(EdsAdditionalPayment additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(BigDecimal rateAmount) {
        this.rateAmount = rateAmount;
    }

    public BigDecimal getLeaveDaysCount() {
        return leaveDaysCount;
    }

    public void setLeaveDaysCount(BigDecimal leaveDaysCount) {
        this.leaveDaysCount = leaveDaysCount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getAdditionalPaymentDate() {
        return additionalPaymentDate;
    }

    public void setAdditionalPaymentDate(Date additionalPaymentDate) {
        this.additionalPaymentDate = additionalPaymentDate;
    }

    //    public List<P11> getPayslips() {
//        return payslips;
//    }
//
//    public void setPayslips(List<P11> payslips) {
//        this.payslips = payslips;
//    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        this.numberOfPeriods = numberOfPeriods;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean isFromAllAllowances() {
        return fromAllAllowances != null && fromAllAllowances;
    }

    public void setFromAllAllowances(Boolean fromAllAllowances) {
        this.fromAllAllowances = fromAllAllowances;
    }

    public Boolean isFullPayed() {
        if (totalPayment == null) {
            totalPayment = BigDecimal.ZERO;
            for (EdsPayslipPayments payments : payments) {
                totalPayment = totalPayment.add(payments.getPaymentTotal());
            }
        }
        return totalAmount != null && totalAmount.compareTo(totalPayment) <= 0 ? true : false;
    }

    public BigDecimal getRemainingAmount() {
        if (totalPayment == null) {
            totalPayment = BigDecimal.ZERO;
            for (EdsPayslipPayments payments : payments) {
                totalPayment = totalPayment.add(payments.getPaymentTotal());
            }
        }
        return totalAmount != null ? totalAmount.subtract(totalPayment) : BigDecimal.ZERO;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getCashAdvanceID() {
        return cashAdvanceID;
    }

    public void setCashAdvanceID(Integer cashAdvanceID) {
        this.cashAdvanceID = cashAdvanceID;
    }

    public Integer getRecurringPayDeductionID() {
        return recurringPayDeductionID;
    }

    public void setRecurringPayDeductionID(Integer recurringPayDeductionID) {
        this.recurringPayDeductionID = recurringPayDeductionID;
    }

    public Integer getParentIdForTemplate() {
        return parentIdForTemplate;
    }

    public void setParentIdForTemplate(Integer parentIdForTemplate) {
        this.parentIdForTemplate = parentIdForTemplate;
    }

    public Boolean IsRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean isSalaryObject() {
        return isSalaryObject != null ? isSalaryObject : Boolean.FALSE;
    }

    public void setSalaryObject(Boolean salaryObject) {
        isSalaryObject = salaryObject;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Set<EdsPayrollCategory> getLinkedCategories() {
        return linkedCategories;
    }

    public void setLinkedCategories(Set<EdsPayrollCategory> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }

    public Set<EdsPayrollPaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(Set<EdsPayrollPaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public EdsReference getLeavereason() {
        return leavereason;
    }

    public void setLeavereason(EdsReference leavereason) {
        this.leavereason = leavereason;
    }

    public Integer getLeaveMinutes() {
        return leaveMinutes;
    }

    public void setLeaveMinutes(Integer leaveMinutes) {
        this.leaveMinutes = leaveMinutes;
    }

    public Integer getLeavePaymentYear() {
        return leavePaymentYear;
    }

    public void setLeavePaymentYear(Integer leavePaymentYear) {
        this.leavePaymentYear = leavePaymentYear;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Integer getPayslipCategoryDays() {
        return payslipCategoryDays;
    }

    public void setPayslipCategoryDays(Integer payslipCategoryDays) {
        this.payslipCategoryDays = payslipCategoryDays;
    }

    public Integer getPayslipCategoryMinutes() {
        return payslipCategoryMinutes;
    }

    public void setPayslipCategoryMinutes(Integer payslipCategoryMinutes) {
        this.payslipCategoryMinutes = payslipCategoryMinutes;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public EPPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EPPaymentType paymentType) {
        this.paymentType = paymentType;
    }


    public BigDecimal getBasicSalaryAmount() {
        return basicSalaryAmount;
    }

    public void setBasicSalaryAmount(BigDecimal basicSalaryAmount) {
        this.basicSalaryAmount = basicSalaryAmount;
    }

    public BigDecimal getBasicAndadditionalAmount() {
        return basicAndadditionalAmount;
    }

    public void setBasicAndadditionalAmount(BigDecimal basicAndadditionalAmount) {
        this.basicAndadditionalAmount = basicAndadditionalAmount;
    }

    public EdsItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public void setTax(final BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getEmployerContribution() {
        return this.employerContribution;
    }

    public void setEmployerContribution(final BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public String getTaxCategoryList() {
        return this.taxCategoryList;
    }

    public void setTaxCategoryList(final String taxCategoryList) {
        this.taxCategoryList = taxCategoryList;
    }

    public String getEmployerContributionCategoryList() {
        return this.employerContributionCategoryList;
    }

    public void setEmployerContributionCategoryList(final String employerContributionCategoryList) {
        this.employerContributionCategoryList = employerContributionCategoryList;
    }

    public String getCustomDeductionCategoryList() {
        return customDeductionCategoryList;
    }

    public void setCustomDeductionCategoryList(String customDeductionCategoryList) {
        this.customDeductionCategoryList = customDeductionCategoryList;
    }

    public BigDecimal getBasicSalaryPartAmount() {
        return this.basicSalaryPartAmount;
    }

    public void setBasicSalaryPartAmount(final BigDecimal basicSalaryPartAmount) {
        this.basicSalaryPartAmount = basicSalaryPartAmount;
    }

    public PaymentDeductionObject getRPC() {
        PaymentDeductionObject paymentDeduction = new PaymentDeductionObject();
        paymentDeduction.setId(getObjectID());
        if (this.getEmployee() != null) {
            SelectItem item = new SelectItem();
            item.setId(getEmployee().getObjectID());
            item.setName(getEmployee().getFullName());
            if (getEmployee().getProfile() != null) {
                item.setDescription(getEmployee().getProfile().getEmployeeCode());
            }
            paymentDeduction.setEmployee(item);
        }
        paymentDeduction.setAmount(getPaymentAmount());
        paymentDeduction.setPaymentAmount(getPaymentAmount());
        paymentDeduction.setPaymentDate(getPaymentDate());
        paymentDeduction.setAdditionalPaymentDate(getAdditionalPaymentDate() != null ? new DateNonConvertable(getAdditionalPaymentDate()) : null);
        paymentDeduction.setStatusName(/*(item.getPayslip() != null && item.getPayslip().getStatus() != null) ? item.getPayslip().getStatus().getName() : */"");
        paymentDeduction.setTotalAmount(getTotalAmount());
        paymentDeduction.setTax(getTax());
        paymentDeduction.setEmployerContribution(getEmployerContribution());
        paymentDeduction.setDeduction(getDeduction());
        paymentDeduction.setBasicSalaryPartAmount(getBasicSalaryPartAmount());
        paymentDeduction.setStarttDate(getStartDate() != null ? new DateNonConvertable(getStartDate()) : null);
        paymentDeduction.setEnddDate(getEndDate() != null ? new DateNonConvertable(getEndDate()) : null);
        paymentDeduction.setRemarks(getRemarks());
        if (getCategory() != null) {
            paymentDeduction.setCategoryItem(getCategory().createPaymentDeductionSelectItem());
            paymentDeduction.setPaymentCategory(EdsPayrollCategory.PAYMENT.equals(getCategory().getType()));
            paymentDeduction.setDeductionCategory(EdsPayrollCategory.DEDUCTION.equals(getCategory().getType()));
            paymentDeduction.setTaxCategory(EdsPayrollCategory.TAX.equals(getCategory().getType()));
            paymentDeduction.setEmployerContributionCategory(EdsPayrollCategory.EMPLOYER_CONTRIBUTION.equals(getCategory().getType()));
            paymentDeduction.setMaterialAidCategory(EdsPayrollCategory.MATERIAL_AID.equals(getCategory().getType()));
        }
        paymentDeduction.setLoan(paymentDeduction.isDeductionCategory() && getStartDate() != null && getEndDate() == null);
        paymentDeduction.setCashAdvanceID(getCashAdvanceID());
        paymentDeduction.setType(getPayType());
        paymentDeduction.setPercentage(getPercentage());
        paymentDeduction.setCommission(getCommission());
        paymentDeduction.setFromAllAllowances(isFromAllAllowances());
        paymentDeduction.setSalaryObject(isSalaryObject());
        paymentDeduction.setLeaveDaysCount(getLeaveDaysCount());
        paymentDeduction.setAdditionalPayment(getAdditionalPayment() != null);
        paymentDeduction.setPaymentType(getPaymentType());
        paymentDeduction.setReference(getAdditionalPayment() != null ? getAdditionalPayment().getReference() : null);
        paymentDeduction.setEmployeeBasicSalary(getBasicSalaryAmount() != null ? getBasicSalaryAmount() : BigDecimal.ZERO);
        paymentDeduction.setBasicPlusAllowance(getBasicAndadditionalAmount() != null ? getBasicAndadditionalAmount() : BigDecimal.ZERO);
        if (getLeavereason() != null) {
            LeavePaymentItem leaveItem = new LeavePaymentItem(getLeavePaymentYear(), getLeaveDays());
            leaveItem.setLeaveMinutes(getLeaveMinutes());
            paymentDeduction.setLeavePaymentItem(leaveItem);
        }
        paymentDeduction.setPayslipCategoryDays(this.getPayslipCategoryDays());
        paymentDeduction.setPayslipCategoryMinutes(this.getPayslipCategoryMinutes());
        return paymentDeduction;
    }

    public SelectItem getEmployeeAsSelectItem() {
        if (getEmployee() == null) return null;
        SelectItem item = new SelectItem(getEmployee().getObjectID(), getEmployee().getFullName());
        if (getEmployee().getProfile() != null && !ServerUtils.isNullOrEmpty(getEmployee().getProfile().getEmployeeCode())) {
            item.setCode(getEmployee().getProfile().getEmployeeCode());
        }
        return item;
    }
}
