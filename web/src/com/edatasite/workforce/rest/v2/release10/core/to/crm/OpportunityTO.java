package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class OpportunityTO extends ResponseData {
    private String name;
    private String number;
    private String company;
    private Double ammount;
    private String currency;
    private IdName contact;
    @NotNull(message = "Customer is required")
    private ItemDto customer;

    private Date closing_date;
    private Integer closing_date_Id;
    private Integer item_id;
    private Integer status_id;
    private String date_added;
    private CurrencyValueTO item_price;
    private CurrencyValueTO company_price;
    private String tasks_presence;

    public OpportunityTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public CurrencyValueTO getItem_price() {
        return item_price;
    }

    public void setItem_price(CurrencyValueTO item_price) {
        this.item_price = item_price;
    }

    public CurrencyValueTO getCompany_price() {
        return company_price;
    }

    public void setCompany_price(CurrencyValueTO company_price) {
        this.company_price = company_price;
    }

    public String getTasks_presence() {
        return tasks_presence;
    }

    public void setTasks_presence(String tasks_presence) {
        this.tasks_presence = tasks_presence;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getClosing_date() {
        return closing_date;
    }

    public void setClosing_date(Date closing_date) {
        this.closing_date = closing_date;
    }

    public Integer getClosing_date_Id() {
        return closing_date_Id;
    }

    public void setClosing_date_Id(Integer closing_date_Id) {
        this.closing_date_Id = closing_date_Id;
    }

    public IdName getContact() {
        return contact;
    }

    public void setContact(IdName contact) {
        this.contact = contact;
    }

    public ItemDto getCustomer() {
        return customer;
    }

    public void setCustomer(ItemDto customer) {
        this.customer = customer;
    }
}
