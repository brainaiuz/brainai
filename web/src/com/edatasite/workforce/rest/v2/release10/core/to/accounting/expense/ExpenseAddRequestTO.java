package com.edatasite.workforce.rest.v2.release10.core.to.accounting.expense;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 14/12/2017.
 */

public class ExpenseAddRequestTO extends ResponseData {
    private Integer id;
    private Integer employee_id;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    private String report_title;
    private String description;
    private ArrayList<Integer> approvers;
    private Integer currency_id;
    private Integer supplier;
    private Integer related_project;
    private Integer fixed_asset;
    private Integer tax_type;
    private String notes;
    private ArrayList<AttachmentTO> draft_attachments;
    private ArrayList<ExpenseItemTO> items;
    private ArrayList<Object> custom_fields;


    public ExpenseAddRequestTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
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

    public ArrayList<Integer> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<Integer> approvers) {
        this.approvers = approvers;
    }

    public Integer getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }

    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }

    public Integer getRelated_project() {
        return related_project;
    }

    public void setRelated_project(Integer related_project) {
        this.related_project = related_project;
    }

    public Integer getFixed_asset() {
        return fixed_asset;
    }

    public void setFixed_asset(Integer fixed_asset) {
        this.fixed_asset = fixed_asset;
    }

    public Integer getTax_type() {
        return tax_type;
    }

    public void setTax_type(Integer tax_type) {
        this.tax_type = tax_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<AttachmentTO> getDraft_attachments() {
        return draft_attachments;
    }

    public void setDraft_attachments(ArrayList<AttachmentTO> draft_attachments) {
        this.draft_attachments = draft_attachments;
    }

    public ArrayList<ExpenseItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpenseItemTO> items) {
        this.items = items;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
