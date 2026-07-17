package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.tax.TaxTO;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 14/12/2017.
 */

public class ExpenseItemTO extends ResponseData {
    private Integer id;
    private CategoryTO category;
    private String description;
    private BigDecimal units;
    private BigDecimal cost_per_unit;
    private TaxTO tax;
    private ArrayList<AttachmentTO> draft_receipts;
    private CategoryTO bill_to;
    private BigDecimal markup_amount;

    public ExpenseItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryTO getCategory() {
        return category;
    }

    public void setCategory(CategoryTO category) {
        this.category = category;
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

    public BigDecimal getCost_per_unit() {
        return cost_per_unit;
    }

    public void setCost_per_unit(BigDecimal cost_per_unit) {
        this.cost_per_unit = cost_per_unit;
    }

    public TaxTO getTax() {
        return tax;
    }

    public void setTax(TaxTO tax) {
        this.tax = tax;
    }

    public ArrayList<AttachmentTO> getDraft_receipts() {
        return draft_receipts;
    }

    public void setDraft_receipts(ArrayList<AttachmentTO> draft_receipts) {
        this.draft_receipts = draft_receipts;
    }

    public CategoryTO getBill_to() {
        return bill_to;
    }

    public void setBill_to(CategoryTO bill_to) {
        this.bill_to = bill_to;
    }

    public BigDecimal getMarkup_amount() {
        return markup_amount;
    }

    public void setMarkup_amount(BigDecimal markup_amount) {
        this.markup_amount = markup_amount;
    }
}
