package com.edatasite.workforce.rest.v3.release10.crm.dto.contact;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ContactCreateDto {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String email;
    private String mobile;
    private String jobTitle;
    private Integer titleId;
    private String titleName;
    private ArrayList<PhoneDto> phoneNumbers;
    private ArrayList<EmailDto> emails;
    private List<AttachmentTO> attachments;
    private CrmAccountTO crmAccount;

    @Valid
    private List<? extends CustomFieldRequest> customFields;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public ArrayList<PhoneDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<PhoneDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public ArrayList<EmailDto> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<EmailDto> emails) {
        this.emails = emails;
    }

    public CrmAccountTO getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(CrmAccountTO crmAccount) {
        this.crmAccount = crmAccount;
    }
}
