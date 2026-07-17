package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.workforcetrack.mobile.rpc.accounting.MTaxItem;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement(name = "expenseListItem")
public class MExpenseListItem {

    private Integer objectID;
    private Integer categoryID;
    private Integer accountID;
    private String categoryName;
    private String accountName;
    private String description;
    private BigDecimal units;
    private BigDecimal costPerUnit;
    private Integer currencyID;
    private String currencyName;
    private BigDecimal exchageRate;
    private Date incurredDate;
    private String glCode;
    private BigDecimal subtotal;
    private BigDecimal baseSubtotal;
    private Integer cashOrCardType;

    private MTaxItem tax;
    private BigDecimal taxAmountInBase;//Tax Amount In Base Currency

    private List<MFileResource> attachments;


    public MExpenseListItem() {

    }


    public MExpenseListItem(ExpenseListItem expenseListItem) {
        if (expenseListItem != null) {
            this.objectID = expenseListItem.getId();
            this.categoryID = expenseListItem.getCategoryId();
            this.accountID = expenseListItem.getAccountId();
            this.categoryName = expenseListItem.getCategoryName();
            this.accountName = expenseListItem.getAccountName();
            this.description = expenseListItem.getDescription();
            this.units = expenseListItem.getUnits();
            this.costPerUnit = expenseListItem.getCostPerUnit();
            this.currencyID = expenseListItem.getCurrencyId();
            this.currencyName = expenseListItem.getCurrencyName();
            this.exchageRate = expenseListItem.getExchageRate();
            this.incurredDate = expenseListItem.getIncurredDate();
            this.glCode = expenseListItem.getGlCode();
            this.subtotal = expenseListItem.getSubtotal();
            this.baseSubtotal = expenseListItem.getBaseSubtotal();
            this.cashOrCardType = expenseListItem.getCashOrCardType();

            if (expenseListItem.getTax() != null) {
                this.tax = new MTaxItem(expenseListItem.getTax());
            }
            this.taxAmountInBase = expenseListItem.getTaxAmountInBase();
            if (expenseListItem.getAttachments() != null && expenseListItem.getAttachments().length > 0) {
                this.attachments = new ArrayList<>();
                for (FileResource fileResource : expenseListItem.getAttachments()) {
                    this.attachments.add(new MFileResource(fileResource));
                }
            }
        }

    }

    public ExpenseListItem convertToExpenseListItem(ExpenseListItem expenseListItem) {
        if (expenseListItem == null) {
            expenseListItem = new ExpenseListItem();
        }
        expenseListItem.setId(this.objectID);
        expenseListItem.setCategoryId(this.categoryID);
        expenseListItem.setAccountId(this.accountID);
        expenseListItem.setCategoryName(this.categoryName);
        expenseListItem.setAccountName(this.accountName);
        expenseListItem.setDescription(this.description);
        expenseListItem.setUnits(this.units);
        expenseListItem.setCostPerUnit(this.costPerUnit);
        expenseListItem.setCurrencyId(this.currencyID);
        expenseListItem.setCurrencyName(this.currencyName);
        expenseListItem.setExchageRate(this.exchageRate);
        expenseListItem.setIncurredDate(this.incurredDate);
        expenseListItem.setGlCode(this.glCode);
        expenseListItem.setSubtotal(this.subtotal);
        expenseListItem.setBaseSubtotal(this.baseSubtotal);
        expenseListItem.setCashOrCardType(this.cashOrCardType);

        if (getTax() != null) {
            expenseListItem.setTax(getTax().convertToTaxItem(null));
        } else {
            expenseListItem.setTax(null);
        }
        expenseListItem.setTaxAmountInBase(getTaxAmountInBase());
        expenseListItem.setTaxAmountInTc(getTaxAmountInBase());
        if (getAttachments() != null && getAttachments().size() > 0) {
            ArrayList<FileResource> fileResources = new ArrayList<>();
            for (MFileResource fileResource : getAttachments()) {
                fileResources.add(fileResource.convert(null));
            }
            expenseListItem.setAttachments(fileResources.toArray(new FileResource[]{}));
        } else {
            expenseListItem.setAttachments(null);
        }
        return expenseListItem;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public Date getIncurredDate() {
        return incurredDate;
    }

    public void setIncurredDate(Date incurredDate) {
        this.incurredDate = incurredDate;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getBaseSubtotal() {
        return baseSubtotal;
    }

    public void setBaseSubtotal(BigDecimal baseSubtotal) {
        this.baseSubtotal = baseSubtotal;
    }

    public Integer getCashOrCardType() {
        return cashOrCardType;
    }

    public void setCashOrCardType(Integer cashOrCardType) {
        this.cashOrCardType = cashOrCardType;
    }

    public MTaxItem getTax() {
        return tax;
    }

    public void setTax(MTaxItem tax) {
        this.tax = tax;
    }

    public BigDecimal getTaxAmountInBase() {
        return taxAmountInBase;
    }

    public void setTaxAmountInBase(BigDecimal taxAmountInBase) {
        this.taxAmountInBase = taxAmountInBase;
    }

    public List<MFileResource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MFileResource> attachments) {
        this.attachments = attachments;
    }
}
