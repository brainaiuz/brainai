package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class LeadDto extends ResponseData {

    private Integer id;
    /*@NotNull(message = "assigneeId field is required")*/
    private Integer assigneeId;
    private IdNameTO assisnee;
    @NotNull(message = "firstName field is required")
    private String firstName;
    /*@NotNull(message = "lastName field is required")*/
    private String lastName;
    private String jobTitle;
    @Valid
    private List<EmailDto> emails;
    private Integer companyId;
    private IdNameTO company;
    private String status;
    private String source;
    private Integer campaignId;
    private IdNameTO campaign;
    private String owner;
    private Integer OwnerId;
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
    private String primaryEmail;
    private List<IdNameTO> accountTypes;
    private IdNameTO industry;
    private String rating;
    private boolean checkForDuplicates;

    public LeadDto() {
    }

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

    public IdNameTO getAssisnee() {
        return assisnee;
    }

    public void setAssisnee(IdNameTO assisnee) {
        this.assisnee = assisnee;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<EmailDto> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailDto> emails) {
        this.emails = emails;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public IdNameTO getCompany() {
        return company;
    }

    public void setCompany(IdNameTO company) {
        this.company = company;
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

    public IdNameTO getCampaign() {
        return campaign;
    }

    public void setCampaign(IdNameTO campaign) {
        this.campaign = campaign;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(Integer ownerId) {
        OwnerId = ownerId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
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

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public List<IdNameTO> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<IdNameTO> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public IdNameTO getIndustry() {
        return industry;
    }

    public void setIndustry(IdNameTO industry) {
        this.industry = industry;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeadDto leadDto)) return false;

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

    @Override
    public String toString() {
        return "LeadDto{" +
                "id=" + id +
                ", assigneeId=" + assigneeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", emails=" + emails +
                ", companyId=" + companyId +
                ", status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", campaignId=" + campaignId +
                ", phoneNumbers=" + phoneNumbers +
                ", notes=" + notes +
                ", addresses=" + addresses +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", customFields=" + customFields +
                '}';
    }
}
