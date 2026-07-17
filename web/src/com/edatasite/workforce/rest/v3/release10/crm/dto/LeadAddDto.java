package com.edatasite.workforce.rest.v3.release10.crm.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeadAddDto {

    private Integer id;
    private Integer assigneeId;
    @NotNull(message = "firstName field is required")
    private String firstName;
    private String lastName;
    private String middleName;
    private String otherName;
    private Date birthDate;

    private String jobTitle;
    private String department;
    private String refIndNumber;
    private String assets;
    private String accountIndustry;
    private String telegram;
    private ArrayList<String> imAddresses;
    private ArrayList<String> webAddresses;
    @Valid
    private List<EmailDto> emails;
    private Integer companyId;
    private String status;
    private String source;
    private Integer campaignId;
    @Valid
    private List<PhoneDto> phoneNumbers;
    @Valid
    private List<NoteDto> notes;
    @Valid
    private List<AddressDto> addresses;
    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonSerialize(using = DateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    private List<? extends CustomFieldRequest> customFields;
    private IdCode industry;
    private Integer entityId;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public IdCode getIndustry() {
        return industry;
    }

    public void setIndustry(IdCode industry) {
        this.industry = industry;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRefIndNumber() {
        return refIndNumber;
    }

    public void setRefIndNumber(String refIndNumber) {
        this.refIndNumber = refIndNumber;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getAccountIndustry() {
        return accountIndustry;
    }

    public void setAccountIndustry(String accountIndustry) {
        this.accountIndustry = accountIndustry;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public List<EmailDto> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailDto> emails) {
        this.emails = emails;
    }

    public ArrayList<String> getImAddresses() {
        return imAddresses;
    }

    public void setImAddresses(ArrayList<String> imAddresses) {
        this.imAddresses = imAddresses;
    }

    public ArrayList<String> getWebAddresses() {
        return webAddresses;
    }

    public void setWebAddresses(ArrayList<String> webAddresses) {
        this.webAddresses = webAddresses;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public List<PhoneDto> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneDto> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeadAddDto leadDto)) return false;

        if (getId() != null ? !getId().equals(leadDto.getId()) : leadDto.getId() != null) return false;
        if (getAssigneeId() != null ? !getAssigneeId().equals(leadDto.getAssigneeId()) : leadDto.getAssigneeId() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(leadDto.getFirstName()) : leadDto.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(leadDto.getLastName()) : leadDto.getLastName() != null)
            return false;
        if (getJobTitle() != null ? !getJobTitle().equals(leadDto.getJobTitle()) : leadDto.getJobTitle() != null)
            return false;
        if (getEmails() != null ? !getEmails().equals(leadDto.getEmails()) : leadDto.getEmails() != null) return false;
        if (getCompanyId() != null ? !getCompanyId().equals(leadDto.getCompanyId()) : leadDto.getCompanyId() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(leadDto.getStatus()) : leadDto.getStatus() != null) return false;
        if (getSource() != null ? !getSource().equals(leadDto.getSource()) : leadDto.getSource() != null) return false;
        if (getCampaignId() != null ? !getCampaignId().equals(leadDto.getCampaignId()) : leadDto.getCampaignId() != null)
            return false;
        if (getPhoneNumbers() != null ? !getPhoneNumbers().equals(leadDto.getPhoneNumbers()) : leadDto.getPhoneNumbers() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(leadDto.getNotes()) : leadDto.getNotes() != null) return false;
        if (getAddresses() != null ? !getAddresses().equals(leadDto.getAddresses()) : leadDto.getAddresses() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(leadDto.getCreatedAt()) : leadDto.getCreatedAt() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(leadDto.getUpdatedAt()) : leadDto.getUpdatedAt() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(leadDto.getCustomFields()) : leadDto.getCustomFields() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getAssigneeId() != null ? getAssigneeId().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getJobTitle() != null ? getJobTitle().hashCode() : 0);
        result = 31 * result + (getEmails() != null ? getEmails().hashCode() : 0);
        result = 31 * result + (getCompanyId() != null ? getCompanyId().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getSource() != null ? getSource().hashCode() : 0);
        result = 31 * result + (getCampaignId() != null ? getCampaignId().hashCode() : 0);
        result = 31 * result + (getPhoneNumbers() != null ? getPhoneNumbers().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getAddresses() != null ? getAddresses().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        return result;
    }

}
