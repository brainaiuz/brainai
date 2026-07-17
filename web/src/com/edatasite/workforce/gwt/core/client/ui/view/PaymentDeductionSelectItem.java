package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: Dec 8, 2009
 * Time: 6:01:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentDeductionSelectItem extends SelectItem {

    private String code;
    private String systemCode;
    private String type;
    private Boolean taxable;
    private Boolean excludeInCustomDeductions;
    private boolean nonMoneyType;
    private CategoryRate simpleRate;
    private ArrayList<CategoryRate> multiRangeRates;

    public PaymentDeductionSelectItem() {

    }

    public PaymentDeductionSelectItem(Integer id, String name, String code, String type) {
        super(id, name);
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //taxable is null by default
    public Boolean getTaxable() {
        return taxable == null || taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public boolean isExcludeInCustomDeductions() {
        return excludeInCustomDeductions != null && excludeInCustomDeductions;
    }

    public void setExcludeInCustomDeductions(Boolean excludeInCustomDeductions) {
        this.excludeInCustomDeductions = excludeInCustomDeductions;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public CategoryRate getSimpleRate() {
        return simpleRate;
    }

    public void setSimpleRate(CategoryRate simpleRate) {
        this.simpleRate = simpleRate;
    }

    public ArrayList<CategoryRate> getMultiRangeRates() {
        return multiRangeRates;
    }

    public void setMultiRangeRates(ArrayList<CategoryRate> multiRangeRates) {
        this.multiRangeRates = multiRangeRates;
    }

    public boolean isNonMoneyType() {
        return this.nonMoneyType;
    }

    public void setNonMoneyType(final boolean nonMoneyType) {
        this.nonMoneyType = nonMoneyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PaymentDeductionSelectItem that = (PaymentDeductionSelectItem) o;

        return code != null ? code.equals(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
