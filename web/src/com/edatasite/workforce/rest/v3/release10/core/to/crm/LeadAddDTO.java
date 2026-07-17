package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created By : Dilsh0d Madrahimov on 10/7/2019 5:23 PM
 */
public class LeadAddDTO extends ResponseData {
    private LeadInformationDTO lead_information;
    private AddressInformationAddDTO address_information;
    private String note;
    private Integer industryId;

    public LeadInformationDTO getLead_information() {
        return lead_information;
    }

    public void setLead_information(LeadInformationDTO lead_information) {
        this.lead_information = lead_information;
    }

    public AddressInformationAddDTO getAddress_information() {
        return address_information;
    }

    public void setAddress_information(AddressInformationAddDTO address_information) {
        this.address_information = address_information;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }
}
