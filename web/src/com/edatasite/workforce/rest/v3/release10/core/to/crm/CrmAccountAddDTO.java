package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 5:55 PM
 */
public class CrmAccountAddDTO extends ResponseData {
    private CrmAccountInformationDTO account_information;

    public CrmAccountInformationDTO getAccount_information() {
        return account_information;
    }

    public void setAccount_information(CrmAccountInformationDTO account_information) {
        this.account_information = account_information;
    }
}
