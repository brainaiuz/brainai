package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 6:43 PM
 */
public class AddressInformationAddDTO extends ResponseData {
    private ArrayList<AddressAddDTO> billing_addresses;
    private ArrayList<AddressAddDTO> mailing_addresses;

    public ArrayList<AddressAddDTO> getBilling_addresses() {
        return billing_addresses;
    }

    public void setBilling_addresses(ArrayList<AddressAddDTO> billing_addresses) {
        this.billing_addresses = billing_addresses;
    }

    public ArrayList<AddressAddDTO> getMailing_addresses() {
        return mailing_addresses;
    }

    public void setMailing_addresses(ArrayList<AddressAddDTO> mailing_addresses) {
        this.mailing_addresses = mailing_addresses;
    }
}
