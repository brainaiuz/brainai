package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 18.02.2009
 * Time: 22:16:57
 * To change this template use File | Settings | File Templates.
 */
public class CategoryObject implements IsSerializable {

    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";

    private String code;
    private Integer codeId;
    private Integer companyID;
    private Integer creditToAccountID;
    private Integer debitToAccountID;
    private Boolean deductFromEmployer;
    private Boolean editable;
    private Integer id;
    private Boolean isAdvancePayment;
    private Boolean isCashAdvance;
    private Boolean defaultCategory;
    private boolean excludeSickLeave;
    private boolean excludeAnnualLeave;
    private boolean nonMoneyType;
    private ArrayList<CategoryRate> multiRangeRates = new ArrayList<>();
    private String name;
    private Integer nameId;
    private Boolean niable;
    private Boolean pensionable;
    private CategoryRate simpleRate;
    private Boolean taxable;
    private Boolean excludeInCustomDeductions;
    private String type;
    private Integer typeID;
    private AccountItem debitToAccount;
    private AccountItem creditToAccount;
    private AccountItem defaultAccountForCashLoans;
    private boolean uk;
    private boolean arabic;
    private Integer useInId;
    private String systemCode;
    private ReferenceLocale localeItem;


    public CategoryObject() {
    }

    public CategoryObject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryObject(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
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

    public AccountItem getDebitToAccount() {
        return debitToAccount;
    }

    public void setDebitToAccount(AccountItem debitToAccount) {
        this.debitToAccount = debitToAccount;
    }

    public AccountItem getCreditToAccount() {
        return creditToAccount;
    }

    public void setCreditToAccount(AccountItem creditToAccount) {
        this.creditToAccount = creditToAccount;
    }

    public AccountItem getDefaultAccountForCashLoans() {
        return defaultAccountForCashLoans;
    }

    public void setDefaultAccountForCashLoans(AccountItem defaultAccountForCashLoans) {
        this.defaultAccountForCashLoans = defaultAccountForCashLoans;
    }

    public Boolean isDeductFromEmployer() {
        return deductFromEmployer;
    }

    public void setDeductFromEmployer(Boolean deductFromEmployer) {
        this.deductFromEmployer = deductFromEmployer;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Boolean defaultCategory) {
        this.defaultCategory = defaultCategory;
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

    public void addMultiRangeRate(CategoryRate multiRangeRate) {
        multiRangeRates.add(multiRangeRate);
    }

    public ArrayList<CategoryRate> getMultiRangeRates() {
        return multiRangeRates;
    }

    public void setMultiRangeRates(ArrayList<CategoryRate> multiRangeRates) {
        this.multiRangeRates = multiRangeRates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNiable() {
        return niable != null ? niable : false;
    }

    public void setNiable(Boolean niable) {
        this.niable = niable;
    }

    public Boolean getPensionable() {
        return pensionable != null ? pensionable : false;
    }

    public void setPensionable(Boolean pensionable) {
        this.pensionable = pensionable;
    }

    public CategoryRate getSimpleRate() {
        return simpleRate;
    }

    public void setSimpleRate(CategoryRate simpleRate) {
        this.simpleRate = simpleRate;
    }

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

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUk() {
        return uk;
    }

    public void setUk(boolean uk) {
        this.uk = uk;
    }

    public boolean isArabic() {
        return arabic;
    }

    public void setArabic(boolean arabic) {
        this.arabic = arabic;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public Integer getUseInId() {
        return useInId;
    }

    public void setUseInId(Integer useInId) {
        this.useInId = useInId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }
}
