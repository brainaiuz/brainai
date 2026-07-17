package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 6:43 PM
 */
public class AddressInformationEditDTO extends ResponseData {
    private ArrayList<AddressEditDTO> billing_addresses;
    private ArrayList<AddressEditDTO> mailing_addresses;

    public ArrayList<AddressEditDTO> getBilling_addresses() {
        return billing_addresses;
    }

    public void setBilling_addresses(ArrayList<AddressEditDTO> billing_addresses) {
        this.billing_addresses = billing_addresses;
    }

    public ArrayList<AddressEditDTO> getMailing_addresses() {
        return mailing_addresses;
    }

    public void setMailing_addresses(ArrayList<AddressEditDTO> mailing_addresses) {
        this.mailing_addresses = mailing_addresses;
    }
}
