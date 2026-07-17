package com.edatasite.workforce.rest.v2.release10.core.to.crm;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class ContactAddressAddTO extends CompanyAddressInformationTO {

    private String type;

    public ContactAddressAddTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
