package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 03/27/2018.
 */
public class OpportunityEditTO extends ResponseData {

    private Integer assignee;
    private Integer backup_assignee;
    private String name;
    private BigDecimal amount;
    private Integer currency;
    private String close_date;
    private Integer company;
    private Integer contact;
    private Integer stage;
    private ArrayList<NoteDto> notes;
    private List<OpportunitySubItemsTO> opportunity_sub_items;

    public OpportunityEditTO() {
    }

    public OpportunityEditTO(Integer assignee, Integer backup_assignee, String name, BigDecimal amount, Integer currency, String close_date, Integer company, Integer contact, Integer stage, ArrayList<NoteDto> notes, List<OpportunitySubItemsTO> opportunity_sub_items) {
        this.assignee = assignee;
        this.backup_assignee = backup_assignee;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.close_date = close_date;
        this.company = company;
        this.contact = contact;
        this.stage = stage;
        this.notes = notes;
        this.opportunity_sub_items = opportunity_sub_items;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public Integer getBackup_assignee() {
        return backup_assignee;
    }

    public void setBackup_assignee(Integer backup_assignee) {
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

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public ArrayList<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteDto> notes) {
        this.notes = notes;
    }

    public List<OpportunitySubItemsTO> getOpportunity_sub_items() {
        return opportunity_sub_items;
    }

    public void setOpportunity_sub_items(List<OpportunitySubItemsTO> opportunity_sub_items) {
        this.opportunity_sub_items = opportunity_sub_items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpportunityEditTO)) return false;

        OpportunityEditTO that = (OpportunityEditTO) o;

        if (getAssignee() != null ? !getAssignee().equals(that.getAssignee()) : that.getAssignee() != null)
            return false;
        if (getBackup_assignee() != null ? !getBackup_assignee().equals(that.getBackup_assignee()) : that.getBackup_assignee() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null) return false;
        if (getCurrency() != null ? !getCurrency().equals(that.getCurrency()) : that.getCurrency() != null)
            return false;
        if (getClose_date() != null ? !getClose_date().equals(that.getClose_date()) : that.getClose_date() != null)
            return false;
        if (getCompany() != null ? !getCompany().equals(that.getCompany()) : that.getCompany() != null) return false;
        if (getContact() != null ? !getContact().equals(that.getContact()) : that.getContact() != null) return false;
        if (getStage() != null ? !getStage().equals(that.getStage()) : that.getStage() != null) return false;
        if (getNotes() != null ? !getNotes().equals(that.getNotes()) : that.getNotes() != null) return false;
        if (getOpportunity_sub_items() != null ? !getOpportunity_sub_items().equals(that.getOpportunity_sub_items()) : that.getOpportunity_sub_items() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAssignee() != null ? getAssignee().hashCode() : 0;
        result = 31 * result + (getBackup_assignee() != null ? getBackup_assignee().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        result = 31 * result + (getClose_date() != null ? getClose_date().hashCode() : 0);
        result = 31 * result + (getCompany() != null ? getCompany().hashCode() : 0);
        result = 31 * result + (getContact() != null ? getContact().hashCode() : 0);
        result = 31 * result + (getStage() != null ? getStage().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getOpportunity_sub_items() != null ? getOpportunity_sub_items().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OpportunityEditTO{" +
                "assignee=" + assignee +
                ", backup_assignee=" + backup_assignee +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", close_date='" + close_date + '\'' +
                ", company=" + company +
                ", contact=" + contact +
                ", stage=" + stage +
                ", notes=" + notes +
                ", opportunity_sub_items=" + opportunity_sub_items +
                '}';
    }
}
