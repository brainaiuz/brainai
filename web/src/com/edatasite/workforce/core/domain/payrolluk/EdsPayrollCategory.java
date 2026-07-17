package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 16.02.2009
 * Time: 21:21:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "category")
public class EdsPayrollCategory extends EdsObject {

    public static final String PAYMENT = PayrollConstants.CATEGORY_PAYMENT;
    public static final String DEDUCTION = PayrollConstants.CATEGORY_DEDUCTION;
    public static final String TAX = PayrollConstants.CATEGORY_TAX;
    public static final String LOAN = PayrollConstants.CATEGORY_LOAN;
    public static final String EMPLOYER_CONTRIBUTION = PayrollConstants.CATEGORY_EMPLOYER_CONTRIBUTION;
    public static final String MATERIAL_AID = PayrollConstants.CATEGORY_MATERIAL_AID;

    @Column(name = "code")
    private String code;

    @Column(name = "system_code")
    private String systemCode;

    @Column(name = "preferencial_limit")
    private BigDecimal preferencialLimit;

    private Integer creditToAccountID;

    private Integer debitToAccountID;

    private Boolean deductFromEmployer; /*if true deduction is covered at employer's expense, else deducted from employee*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id")
    private EdsFormula formula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pensionScheme_id")
    private EdsPensionScheme pensionScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sickleavesettingsid")
    private EdsSickLeaveSettings sickLeaveSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endOfServiceSettings_id")
    private EndOfServiceSettings endOfServiceSettings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA,
            name = "paymentDeductionsCategories",
            joinColumns = {@JoinColumn(name = "categoryId")},
            inverseJoinColumns = {@JoinColumn(name = "paymentDeductionId")})
    private Set<EdsPaymentDeduction> paymentDeductions = new HashSet<>();

    @Column(name = "isAdvancePayment")
    private Boolean isAdvancePayment;

    @Column(name = "isCashAdvance")
    private Boolean isCashAdvance;

    @Column(name = "recurring")
    private Boolean isRecurring;

    @Column(name = "defaultCategory")
    private Boolean defaultCategory;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "NIable")
    private Boolean niable;

    @Column(name = "pensionable")
    private Boolean pensionable;

    @Column(name = "Taxable")
    private Boolean taxable;

    @Column(name = "excludeInCustomDeductions")
    private Boolean excludeInCustomDeductions;

    @Column(name = "deleted")
    private Boolean deleted;

    private String type;

    private Boolean arabic;

    private Boolean forAll;

    @Column(name = "excludeSickLeave", columnDefinition = "boolean DEFAULT false")
    private boolean excludeSickLeave;

    @Column(name = "excludeAnnualLeave", columnDefinition = "boolean DEFAULT false")
    private boolean excludeAnnualLeave;

    @Column(name = "nonMoneyType", columnDefinition = "boolean DEFAULT false")
    private boolean nonMoneyType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    public Integer getObjectID() {
        return objectID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public BigDecimal getPreferencialLimit() {
        return preferencialLimit;
    }

    public void setPreferencialLimit(BigDecimal preferencialLimit) {
        this.preferencialLimit = preferencialLimit;
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

    public Boolean isDeductFromEmployer() {
        return deductFromEmployer != null ? deductFromEmployer : false;
    }

    public void setDeductFromEmployer(Boolean deductFromEmployer) {
        this.deductFromEmployer = deductFromEmployer;
    }

    public EdsFormula getFormula() {
        return formula;
    }

    public void setFormula(EdsFormula formula) {
        this.formula = formula;
    }

    public Boolean isAdvancePayment() {
        return isAdvancePayment;
    }

    public void setAdvancePayment(Boolean advancePayment) {
        isAdvancePayment = advancePayment;
    }

    public Boolean isCashAdvance() {
        return isCashAdvance != null ? isCashAdvance : Boolean.FALSE;
    }

    public void setCashAdvance(Boolean cashAdvance) {
        isCashAdvance = cashAdvance;
    }

    public Boolean getRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public Boolean getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return name;    }

    public String getRealName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNiable() {
        return niable;
    }

    public void setNiable(Boolean niable) {
        this.niable = niable;
    }

    public Boolean getPensionable() {
        return pensionable;
    }

    public void setPensionable(Boolean pensionable) {
        this.pensionable = pensionable;
    }

    //if null then return true (for old categories)
    public Boolean getTaxable() {
        return taxable == null || taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public boolean getExcludeInCustomDeductions() {
        return excludeInCustomDeductions != null && excludeInCustomDeductions;
    }

    public void setExcludeInCustomDeductions(Boolean excludeInCustomDeductions) {
        this.excludeInCustomDeductions = excludeInCustomDeductions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isArabic() {
        return arabic != null ? arabic : Boolean.FALSE;
    }

    public void setArabic(Boolean arabic) {
        this.arabic = arabic;
    }

    public Boolean isForAll() {
        return forAll;
    }

    public void setForAll(Boolean forAll) {
        this.forAll = forAll;
    }

    public boolean isExcludeSickLeave() {
        return this.excludeSickLeave;
    }

    public void setExcludeSickLeave(final boolean excludeSickLeave) {
        this.excludeSickLeave = excludeSickLeave;
    }

    public boolean isExcludeAnnualLeave() {
        return this.excludeAnnualLeave;
    }

    public void setExcludeAnnualLeave(final boolean excludeAnnualLeave) {
        this.excludeAnnualLeave = excludeAnnualLeave;
    }

    public boolean isNonMoneyType() {
        return this.nonMoneyType;
    }

    public void setNonMoneyType(final boolean nonMoneyType) {
        this.nonMoneyType = nonMoneyType;
    }

    public EdsPensionScheme getPensionScheme() {
        return pensionScheme;
    }

    public void setPensionScheme(EdsPensionScheme pensionScheme) {
        this.pensionScheme = pensionScheme;
    }

    public EndOfServiceSettings getEndOfServiceSettings() {
        return endOfServiceSettings;
    }

    public void setEndOfServiceSettings(EndOfServiceSettings endOfServiceSettings) {
        this.endOfServiceSettings = endOfServiceSettings;
    }
    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }


    public Set<EdsPaymentDeduction> getPaymentDeductions() {
        if (paymentDeductions == null) {
            paymentDeductions = new HashSet<>();
        }
        return paymentDeductions;
    }

    public void setPaymentDeductions(Set<EdsPaymentDeduction> paymentDeductions) {
        this.paymentDeductions = paymentDeductions;
    }

    public void addPaymentDeduction(EdsPaymentDeduction paymentDeduction) {
        getPaymentDeductions().add(paymentDeduction);
    }

    public EdsSickLeaveSettings getSickLeaveSettings() {
        return sickLeaveSettings;
    }

    public void setSickLeaveSettings(EdsSickLeaveSettings sickLeaveSettings) {
        this.sickLeaveSettings = sickLeaveSettings;
    }

    public CategoryObject getRpcObject() {
        final CategoryObject catObj = new CategoryObject(objectID, name, code);
        catObj.setTaxable(taxable);
        catObj.setExcludeInCustomDeductions(excludeInCustomDeductions);
        catObj.setNiable(niable);
        catObj.setPensionable(pensionable);
        catObj.setDeductFromEmployer(deductFromEmployer);
        catObj.setType(type);
        catObj.setAdvancePayment(isAdvancePayment);
        return catObj;
    }

    public PaymentDeductionSelectItem createPaymentDeductionSelectItem() {
        PaymentDeductionSelectItem categoryItem = new PaymentDeductionSelectItem(getObjectID(), getName(), getCode(), getType());
        categoryItem.setSystemCode(getSystemCode());
        categoryItem.setTaxable(getTaxable());
        categoryItem.setExcludeInCustomDeductions(getExcludeInCustomDeductions());
        categoryItem.setNonMoneyType(isNonMoneyType());
        if (getFormula() != null) {
            if (getFormula().getSimpleRate() != null) {
                categoryItem.setSimpleRate(getFormula().getSimpleRate().getAsRPC());
            } else if (getFormula().getMultiRangeRates() != null && getFormula().getMultiRangeRates().size() > 0) {
                List<EdsMultiRangeRate> rateList = getFormula().getMultiRangeRates();
                ArrayList<CategoryRate> categoryRates = new ArrayList<>();
                for (EdsMultiRangeRate rangeRate : rateList) {
                    categoryRates.add(rangeRate.getAsRPC());
                }
                categoryItem.setMultiRangeRates(categoryRates);
            }
        }
        return categoryItem;
    }
}
