package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 30/03/2018.
 */

public class StageHistoryTO extends ResponseData {

    private String name;
    private FilteredStatusItemTO status;
    private CurrencyValueTO amount;
    private Float probability;
    private BigDecimal revenue;
    private String action;
    private ContactTO modifier;
    private String updated_date;
    private String closed_date;
    //This field is used for sorting by date
    private Date updateDate;

    public StageHistoryTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilteredStatusItemTO getStatus() {
        return status;
    }

    public void setStatus(FilteredStatusItemTO status) {
        this.status = status;
    }

    public CurrencyValueTO getAmount() {
        return amount;
    }

    public void setAmount(CurrencyValueTO amount) {
        this.amount = amount;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ContactTO getModifier() {
        return modifier;
    }

    public void setModifier(ContactTO modifier) {
        this.modifier = modifier;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getClosed_date() {
        return closed_date;
    }

    public void setClosed_date(String closed_date) {
        this.closed_date = closed_date;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
