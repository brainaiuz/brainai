package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ManualEntryLineItemDto extends DynamicDto {
    @NotNull(message = "Account is required")
    private IdCode account;
    private BigDecimal debit;
    private BigDecimal credit;
    private String description;
    private IdName name;
    private IdName billTo;
    private IdCode project;

    public ManualEntryLineItemDto() {
    }

    public ManualEntryLineItemDto(IdCode account, BigDecimal debit, BigDecimal credit, String description, IdName name, IdName billTo, IdCode project) {
        this.account = account;
        this.debit = debit;
        this.credit = credit;
        this.description = description;
        this.name = name;
        this.billTo = billTo;
        this.project = project;
    }

    public IdCode getAccount() {
        return account;
    }

    public void setAccount(IdCode account) {
        this.account = account;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdName getName() {
        return name;
    }

    public void setName(IdName name) {
        this.name = name;
    }

    public IdName getBillTo() {
        return billTo;
    }

    public void setBillTo(IdName billTo) {
        this.billTo = billTo;
    }

    public IdCode getProject() {
        return project;
    }

    public void setProject(IdCode project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManualEntryLineItemDto)) return false;

        ManualEntryLineItemDto that = (ManualEntryLineItemDto) o;

        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (debit != null ? !debit.equals(that.debit) : that.debit != null) return false;
        if (credit != null ? !credit.equals(that.credit) : that.credit != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (billTo != null ? !billTo.equals(that.billTo) : that.billTo != null) return false;
        if (project != null ? !project.equals(that.project) : that.project != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (debit != null ? debit.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (billTo != null ? billTo.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ManualEntryLineItemDto{" +
                "account=" + account +
                ", debit=" + debit +
                ", credit=" + credit +
                ", description='" + description + '\'' +
                ", name=" + name +
                ", billTo=" + billTo +
                ", project=" + project +
                '}';
    }
}
