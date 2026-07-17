package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.ApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 14/12/2017.
 */

public class ExpenseDetailsTO extends ResponseData {
    private Integer id;
    private String number;
    private OwnerTO owner;
    private Object status;
    private String date;
    private String report_title;
    private String description;
    private ArrayList<ApproversTO> approvers;
    private CurrencyListTO currency;
    private CategoryTO supplier;
    private CategoryTO related_project;
    private CategoryTO fixed_asset;
    private CategoryTO tax_type;
    private String notes;
    private ArrayList<AttachmentTO> attachments;
    private ArrayList<ExpenseItemTO> items;
    private ArrayList<CustomFieldsTO> custom_fields;
    private RequestUserActionTO user_actions;
    private BigDecimal subTotal;
    private BigDecimal total;
    private BigDecimal dueAmount;

    public ExpenseDetailsTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public OwnerTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerTO owner) {
        this.owner = owner;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ApproversTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<ApproversTO> approvers) {
        this.approvers = approvers;
    }

    public CurrencyListTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyListTO currency) {
        this.currency = currency;
    }

    public CategoryTO getSupplier() {
        return supplier;
    }

    public void setSupplier(CategoryTO supplier) {
        this.supplier = supplier;
    }

    public CategoryTO getRelated_project() {
        return related_project;
    }

    public void setRelated_project(CategoryTO related_project) {
        this.related_project = related_project;
    }

    public CategoryTO getFixed_asset() {
        return fixed_asset;
    }

    public void setFixed_asset(CategoryTO fixed_asset) {
        this.fixed_asset = fixed_asset;
    }

    public CategoryTO getTax_type() {
        return tax_type;
    }

    public void setTax_type(CategoryTO tax_type) {
        this.tax_type = tax_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<ExpenseItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpenseItemTO> items) {
        this.items = items;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public RequestUserActionTO getUser_actions() {
        return user_actions;
    }

    public void setUser_actions(RequestUserActionTO user_actions) {
        this.user_actions = user_actions;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }
}
