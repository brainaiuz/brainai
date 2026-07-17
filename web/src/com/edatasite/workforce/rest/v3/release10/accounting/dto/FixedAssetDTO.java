package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FixedAssetDTO {
    private Integer id;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is cannot be blank")
    private String name;
    @NotNull(message = "Cost is required")
    private BigDecimal cost;
    @NotNull(message = "Useful Life is required")
    private BigDecimal usefulLife;
    @NotNull(message = "Tax Calculation Type is required")
    private Integer taxCalculationType;
    @NotNull(message = "Expense Account is required")
    private IdNameTO expenseAccount;
    @NotNull(message = "Fixed asset account is required")
    private IdNameTO fixedAssetAccount;
    @NotNull(message = "Finance by Account is required")
    private IdNameTO financedByAccount;
    private Integer ownerId;
    private Integer accountId;
    private Integer financeAccountId;
    private Integer imageId;
    private IdNameTO image;
    private IdNameTO owner;
    private BigDecimal residualValue;

    public FixedAssetDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(BigDecimal usefulLife) {
        this.usefulLife = usefulLife;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public IdNameTO getFixedAssetAccount() {
        return fixedAssetAccount;
    }

    public void setFixedAssetAccount(IdNameTO fixedAssetAccount) {
        this.fixedAssetAccount = fixedAssetAccount;
    }

    public IdNameTO getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(IdNameTO expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public IdNameTO getFinancedByAccount() {
        return financedByAccount;
    }

    public void setFinancedByAccount(IdNameTO financedByAccount) {
        this.financedByAccount = financedByAccount;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    public Integer getFinanceAccountId() {
        return financeAccountId;
    }
    public void setFinanceAccountId(Integer financeAccountId) {
        this.financeAccountId = financeAccountId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public IdNameTO getImage() {
        return image;
    }

    public void setImage(IdNameTO image) {
        this.image = image;
    }

    public IdNameTO getOwner() {
        return owner;
    }

    public void setOwner(IdNameTO owner) {
        this.owner = owner;
    }

    public BigDecimal getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }
}
