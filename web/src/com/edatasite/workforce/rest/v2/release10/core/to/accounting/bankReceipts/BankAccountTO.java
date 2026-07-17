package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class BankAccountTO {
    @Min(value = 1, message = "bank account id should be more than 0")
    private Integer id;
    @JsonProperty("account_code")
    private String accountCode;
    private String name;

    public BankAccountTO() {
    }

    public BankAccountTO(Integer id, String accountCode, String name) {
        this.id = id;
        this.accountCode = accountCode;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccountTO)) return false;

        BankAccountTO that = (BankAccountTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (accountCode != null ? !accountCode.equals(that.accountCode) : that.accountCode != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (accountCode != null ? accountCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BankAccountTO{" +
                "id=" + id +
                ", accountCode='" + accountCode + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
