package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.math.BigDecimal;

public class AllowanceDto {
    private ItemDto allowance;
    private BigDecimal amount;

    public AllowanceDto() {
    }

    public AllowanceDto(ItemDto allowance, BigDecimal amount) {
        this.allowance = allowance;
        this.amount = amount;
    }

    public ItemDto getAllowance() {
        return allowance;
    }

    public void setAllowance(ItemDto allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AllowanceDto)) return false;

        AllowanceDto that = (AllowanceDto) o;

        if (getAllowance() != null ? !getAllowance().equals(that.getAllowance()) : that.getAllowance() != null)
            return false;
        if (getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAllowance() != null ? getAllowance().hashCode() : 0;
        result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AllowanceDto{" +
                "allowance=" + allowance +
                ", amount=" + amount +
                '}';
    }
}
