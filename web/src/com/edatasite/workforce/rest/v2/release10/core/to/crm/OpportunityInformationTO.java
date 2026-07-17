package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 3/27/2018.
 **/
public class OpportunityInformationTO extends ResponseData {
    private CategoryTO assignee;
    private CategoryTO backup_assignee;
    private String name;
    private BigDecimal amount;
    private Integer currency;
    private String close_date;
    private CategoryTO company;
    private CategoryTO contact;
    private CategoryTO stage;
    private ArrayList<NoteDto> notes;
    private ArrayList<CustomFieldsTO> customFields;

    public OpportunityInformationTO() {
    }

    public CategoryTO getAssignee() {
        return assignee;
    }

    public void setAssignee(CategoryTO assignee) {
        this.assignee = assignee;
    }

    public CategoryTO getBackup_assignee() {
        return backup_assignee;
    }

    public void setBackup_assignee(CategoryTO backup_assignee) {
        this.backup_assignee = backup_assignee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }

    public CategoryTO getCompany() {
        return company;
    }

    public void setCompany(CategoryTO company) {
        this.company = company;
    }

    public CategoryTO getContact() {
        return contact;
    }

    public void setContact(CategoryTO contact) {
        this.contact = contact;
    }

    public CategoryTO getStage() {
        return stage;
    }

    public void setStage(CategoryTO stage) {
        this.stage = stage;
    }

    public ArrayList<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteDto> notes) {
        this.notes = notes;
    }

    public ArrayList<CustomFieldsTO> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CustomFieldsTO> customFields) {
        this.customFields = customFields;
    }
}
