package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 5:55 PM
 */
public class CustomerAddDTO extends ResponseData {
    private CustomerInformationAddDTO account_information;
    private AddressInformationAddDTO address_information;
    private FinancialInformationAddDTO financial_information;

    public CustomerInformationAddDTO getAccount_information() {
        return account_information;
    }

    public void setAccount_information(CustomerInformationAddDTO account_information) {
        this.account_information = account_information;
    }

    public AddressInformationAddDTO getAddress_information() {
        return address_information;
    }

    public void setAddress_information(AddressInformationAddDTO address_information) {
        this.address_information = address_information;
    }

    public FinancialInformationAddDTO getFinancial_information() {
        return financial_information;
    }

    public void setFinancial_information(FinancialInformationAddDTO financial_information) {
        this.financial_information = financial_information;
    }
}
