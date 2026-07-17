package com.edatasite.workforce.gwt.core.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 6/4/15 4:05 PM
 */
public enum BankAccountTypeEnum implements IsSerializable {

    ASSETS("ASSETS", "ASSETS"),
    LIABILITIES("LIABILITIES", "LIABILITIES"),
    EQUITY("EQUITY", "EQUITY"),
    REVENUE("REVENUE", "REVENUE"),
    EXPENSES("EXPENSES", "EXPENSES"),

    BANK("BANK", "Bank"),
    CURRENT_ASSET("CURRENT_ASSET", "Current Asset"),
    FIXED_ASSET("FIXED_ASSET", "Fixed Asset"),
    PREPAYMENT("PREPAYMENT", "Prepayment"),
    CREDIT_CARD("CREDIT_CARD", "CREDIT_CARD"),

    LIABILITY("LIABILITY", "Liability"),
    LONG_TERM_LIABILITY("LONG_TERM_LIABILITY", "Non-Current Liability"),
    CURRENT_LIABILITY("CURRENT_LIABILITY", "Current Liability"),

    COST_OF_SALES("COST_OF_SALES", "Cost Of Sales"),
    DIRECT_EXPENSES("DIRECT_EXPENSES", "Direct Expenses"),
    OVERHEAD("OVERHEAD", "Overhead"),
    DEPRECIATION("DEPRECIATION", "Depreciation"),

    SALES("SALES", "Sales"),
    OTHER_INCOME("OTHER_INCOME", "Other Income");

    public String code;
    public String name;

    BankAccountTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
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

    public static BankAccountTypeEnum getEnumByCode(String selectedCode) {
        for (BankAccountTypeEnum bankAccountTypeEnum : BankAccountTypeEnum.values()) {
            if (bankAccountTypeEnum.getCode() == selectedCode) {
                return bankAccountTypeEnum;
            }
        }
        return null;
    }
}
