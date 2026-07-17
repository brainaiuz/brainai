package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * User : Akhror on 02/06/2021
 */
public class CandidateDTO {
    private Integer id;
    private String objectKey;

    //Candidate Information
    private String number;
    private IdCode title;
    @NotNull(message = "firstName is required")
    @NotBlank(message = "firstName cannot be empty")
    private String firstName;
    @NotNull(message = "lastName is required")
    @NotBlank(message = "lastName cannot be empty")
    private String lastName;
    @Schema(description = "Date Format (format: dd-MM-yyyy)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;
    private ItemDto status;
    private ItemDto project;
    private ItemDto source;
    private IdCode workExperience;
    private Double expectedSalary;
    private Double startSalary;
    private String currentEmployer;
    @Size(max = 1000, message = "Skills cannot be more than 1000 characters")
    private String skills;
    private List<VacancyDTO> vacancies;
    private IdName location;
    private IdName owner;
    private List<LanguagesDto> languages;
    private boolean married;
    private String spokenLanguages;
    private String vacancyNumber;
    private Integer positionId;
    private Integer timeSlotId;
    private Integer photoId;


    //other information
    private String phone;
    private String email;
    private List<IMWebAddressDto> imAddresses;
    private List<IMWebAddressDto> webAddresses;

    //allowance information
    private List<AllowanceDto> allowances;

    //notes
    private List<NoteDto> notes;

    //attachments
    private List<AttachmentTO> attachments;

    //address information
    private List<AddressDto> addresses;

    @Valid
    private List<? extends CustomFieldRequest> customFields;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;

    public CandidateDTO() {
    }

    public CandidateDTO(Integer id, String number, IdCode title, String firstName, String lastName, Date dateOfBirth, ItemDto status, ItemDto project, ItemDto source, IdCode workExperience, Double expectedSalary, String currentEmployer, String skills, List<VacancyDTO> vacancies, IdName location, IdName owner, List<LanguagesDto> languages, String phone, String email, List<IMWebAddressDto> imAddresses, List<IMWebAddressDto> webAddresses, List<AllowanceDto> allowances, List<NoteDto> notes, List<AttachmentTO> attachments, List<AddressDto> addresses, List<? extends CustomFieldRequest> customFields, Date createdAt, Date updatedAt) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.project = project;
        this.source = source;
        this.workExperience = workExperience;
        this.expectedSalary = expectedSalary;
        this.currentEmployer = currentEmployer;
        this.skills = skills;
        this.vacancies = vacancies;
        this.location = location;
        this.owner = owner;
        this.languages = languages;
        this.phone = phone;
        this.email = email;
        this.imAddresses = imAddresses;
        this.webAddresses = webAddresses;
        this.allowances = allowances;
        this.notes = notes;
        this.attachments = attachments;
        this.addresses = addresses;
        this.customFields = customFields;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IdCode getTitle() {
        return title;
    }

    public void setTitle(IdCode title) {
        this.title = title;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ItemDto getStatus() {
        return status;
    }

    public void setStatus(ItemDto status) {
        this.status = status;
    }

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
    }

    public ItemDto getSource() {
        return source;
    }

    public void setSource(ItemDto source) {
        this.source = source;
    }

    public IdCode getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(IdCode workExperience) {
        this.workExperience = workExperience;
    }

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public Double getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(Double startSalary) {
        this.startSalary = startSalary;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getCurrentEmployer() {
        return currentEmployer;
    }

    public void setCurrentEmployer(String currentEmployer) {
        this.currentEmployer = currentEmployer;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public List<VacancyDTO> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<VacancyDTO> vacancies) {
        this.vacancies = vacancies;
    }

    public IdName getLocation() {
        return location;
    }

    public void setLocation(IdName location) {
        this.location = location;
    }

    public IdName getOwner() {
        return owner;
    }

    public void setOwner(IdName owner) {
        this.owner = owner;
    }

    public List<LanguagesDto> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguagesDto> languages) {
        this.languages = languages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<IMWebAddressDto> getImAddresses() {
        return imAddresses;
    }

    public void setImAddresses(List<IMWebAddressDto> imAddresses) {
        this.imAddresses = imAddresses;
    }

    public List<IMWebAddressDto> getWebAddresses() {
        return webAddresses;
    }

    public void setWebAddresses(List<IMWebAddressDto> webAddresses) {
        this.webAddresses = webAddresses;
    }

    public List<AllowanceDto> getAllowances() {
        return allowances;
    }

    public void setAllowances(List<AllowanceDto> allowances) {
        this.allowances = allowances;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
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

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CandidateDTO)) return false;

        CandidateDTO that = (CandidateDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
            return false;
        if (getDateOfBirth() != null ? !getDateOfBirth().equals(that.getDateOfBirth()) : that.getDateOfBirth() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null) return false;
        if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) return false;
        if (getWorkExperience() != null ? !getWorkExperience().equals(that.getWorkExperience()) : that.getWorkExperience() != null)
            return false;
        if (getExpectedSalary() != null ? !getExpectedSalary().equals(that.getExpectedSalary()) : that.getExpectedSalary() != null)
            return false;
        if (getCurrentEmployer() != null ? !getCurrentEmployer().equals(that.getCurrentEmployer()) : that.getCurrentEmployer() != null)
            return false;
        if (getSkills() != null ? !getSkills().equals(that.getSkills()) : that.getSkills() != null) return false;
        if (getVacancies() != null ? !getVacancies().equals(that.getVacancies()) : that.getVacancies() != null)
            return false;
        if (getLocation() != null ? !getLocation().equals(that.getLocation()) : that.getLocation() != null)
            return false;
        if (getOwner() != null ? !getOwner().equals(that.getOwner()) : that.getOwner() != null) return false;
        if (getLanguages() != null ? !getLanguages().equals(that.getLanguages()) : that.getLanguages() != null)
            return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getImAddresses() != null ? !getImAddresses().equals(that.getImAddresses()) : that.getImAddresses() != null)
            return false;
        if (getWebAddresses() != null ? !getWebAddresses().equals(that.getWebAddresses()) : that.getWebAddresses() != null)
            return false;
        if (getAllowances() != null ? !getAllowances().equals(that.getAllowances()) : that.getAllowances() != null)
            return false;
        if (getNotes() != null ? !getNotes().equals(that.getNotes()) : that.getNotes() != null) return false;
        if (getAttachments() != null ? !getAttachments().equals(that.getAttachments()) : that.getAttachments() != null)
            return false;
        if (getAddresses() != null ? !getAddresses().equals(that.getAddresses()) : that.getAddresses() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(that.getCustomFields()) : that.getCustomFields() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(that.getUpdatedAt()) : that.getUpdatedAt() != null)
            return false;

        return true;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public String getVacancyNumber() {
        return vacancyNumber;
    }

    public void setVacancyNumber(String vacancyNumber) {
        this.vacancyNumber = vacancyNumber;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getDateOfBirth() != null ? getDateOfBirth().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        result = 31 * result + (getSource() != null ? getSource().hashCode() : 0);
        result = 31 * result + (getWorkExperience() != null ? getWorkExperience().hashCode() : 0);
        result = 31 * result + (getExpectedSalary() != null ? getExpectedSalary().hashCode() : 0);
        result = 31 * result + (getCurrentEmployer() != null ? getCurrentEmployer().hashCode() : 0);
        result = 31 * result + (getSkills() != null ? getSkills().hashCode() : 0);
        result = 31 * result + (getVacancies() != null ? getVacancies().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (getOwner() != null ? getOwner().hashCode() : 0);
        result = 31 * result + (getLanguages() != null ? getLanguages().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getImAddresses() != null ? getImAddresses().hashCode() : 0);
        result = 31 * result + (getWebAddresses() != null ? getWebAddresses().hashCode() : 0);
        result = 31 * result + (getAllowances() != null ? getAllowances().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getAddresses() != null ? getAddresses().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", title=" + title +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", status=" + status +
                ", project=" + project +
                ", source=" + source +
                ", workExperience=" + workExperience +
                ", expectedSalary=" + expectedSalary +
                ", currentEmployer='" + currentEmployer + '\'' +
                ", skills='" + skills + '\'' +
                ", vacancies=" + vacancies +
                ", location=" + location +
                ", owner=" + owner +
                ", languages=" + languages +
                ", phones=" + phone +
                ", emails=" + email +
                ", imAddresses=" + imAddresses +
                ", webAddresses=" + webAddresses +
                ", allowances=" + allowances +
                ", notes=" + notes +
                ", attachments=" + attachments +
                ", addresses=" + addresses +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
