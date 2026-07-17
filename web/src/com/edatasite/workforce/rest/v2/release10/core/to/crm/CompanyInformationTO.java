package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ItemTypeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh 04/02/2018.
 */
public class CompanyInformationTO extends ResponseData {
    private CategoryTO account_owner;
    private String account_name;
    private CategoryTO parent_account;
    private CategoryTO primary_contact;
    private ArrayList<ItemTypeTO> account_types;

    public CategoryTO getAccount_owner() {
        return account_owner;
    }

    public void setAccount_owner(CategoryTO account_owner) {
        this.account_owner = account_owner;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public CategoryTO getParent_account() {
        return parent_account;
    }

    public void setParent_account(CategoryTO parent_account) {
        this.parent_account = parent_account;
    }

    public CategoryTO getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(CategoryTO primary_contact) {
        this.primary_contact = primary_contact;
    }

    public ArrayList<ItemTypeTO> getAccount_types() {
        return account_types;
    }

    public void setAccount_types(ArrayList<ItemTypeTO> account_types) {
        this.account_types = account_types;
    }
}
