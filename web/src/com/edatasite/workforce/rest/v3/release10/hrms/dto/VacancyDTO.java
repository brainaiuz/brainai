package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.LocaleDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * User : Akhror on 29/06/2021
 */
public class VacancyDTO {
    private Integer id;

    //general details
    @NotNull(message = "jobTitle is required")
    @NotBlank(message = "jobTitle cannot be empty")
    private String jobTitle;
    private LocaleDto jobTitleLocale;
    private String description;
    private String requirements;
    private ItemDto status;
    private ItemDto type;
    private Integer vacantPlaceCount;
    private String proposedSalary;

    private String gender;
    @Schema(description = "Date Format (format: dd-mm-yyyy)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    @NotNull(message = "startDate is required")
    private Date startDate;
    @Schema(description = "Date Format (format: dd-mm-yyyy)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    @NotNull(message = "endDate is required")
    private Date endDate;

    //internal details
    private String number;
    @NotNull(message = "location is required")
    private IdName location;
    @NotNull(message = "position is required")
    private IdName position;
    private IdName department;
    private IdName country;
    private IdName embassy;

    @Schema(description = "Date Format (format: dd-mm-yyyy)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date contractStartDate;
    @Schema(description = "Date Format (format: dd-mm-yyyy)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date contractEndDate;
    private ItemDto project;
    private IdName manager;
    private ItemDto religion;

    //position information
    private IdName requiredDegree;
    private String responsibilities;
    private IdName jobFamily;
    private IdName jobType;

    //notes
    private List<NoteDto> notes;

    //attachments
    private List<AttachmentTO> attachments;

    @Valid
    private List<? extends CustomFieldRequest> customFields;

    public VacancyDTO() {
    }

    public VacancyDTO(Integer id, String jobTitle, String description, String requirements, ItemDto status, ItemDto type, Integer vacantPlaceCount, String proposedSalary, String gender, Date startDate, Date endDate, String number, IdName location, IdName position, IdName country, IdName embassy, Date contractStartDate, Date contractEndDate, ItemDto project, IdName manager, ItemDto religion, IdName requiredDegree, String responsibilities, IdName jobFamily, IdName jobType, List<NoteDto> notes, List<AttachmentTO> attachments, List<? extends CustomFieldRequest> customFields) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.description = description;
        this.requirements = requirements;
        this.status = status;
        this.type = type;
        this.vacantPlaceCount = vacantPlaceCount;
        this.proposedSalary = proposedSalary;
        this.gender = gender;
        this.startDate = startDate;
        this.endDate = endDate;
        this.number = number;
        this.location = location;
        this.position = position;
        this.country = country;
        this.embassy = embassy;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.project = project;
        this.manager = manager;
        this.religion = religion;
        this.requiredDegree = requiredDegree;
        this.responsibilities = responsibilities;
        this.jobFamily = jobFamily;
        this.jobType = jobType;
        this.notes = notes;
        this.attachments = attachments;
        this.customFields = customFields;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public ItemDto getStatus() {
        return status;
    }

    public void setStatus(ItemDto status) {
        this.status = status;
    }

    public ItemDto getType() {
        return type;
    }

    public void setType(ItemDto type) {
        this.type = type;
    }

    public Integer getVacantPlaceCount() {
        return vacantPlaceCount;
    }

    public void setVacantPlaceCount(Integer vacantPlaceCount) {
        this.vacantPlaceCount = vacantPlaceCount;
    }

    public String getProposedSalary() {
        return proposedSalary;
    }

    public void setProposedSalary(String proposedSalary) {
        this.proposedSalary = proposedSalary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IdName getLocation() {
        return location;
    }

    public void setLocation(IdName location) {
        this.location = location;
    }

    public IdName getPosition() {
        return position;
    }

    public void setPosition(IdName position) {
        this.position = position;
    }

//    public IdName getCountry() {
//        return country;
//    }

//    public void setCountry(IdName country) {
//        this.country = country;
//    }

//    public IdName getEmbassy() {
//        return embassy;
//    }

//    public void setEmbassy(IdName embassy) {
//        this.embassy = embassy;
//    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public ItemDto getProject() {
        return project;
    }

    public void setProject(ItemDto project) {
        this.project = project;
    }

    public IdName getManager() {
        return manager;
    }

    public void setManager(IdName manager) {
        this.manager = manager;
    }

//    public ItemDto getReligion() {
//        return religion;
//    }

//    public void setReligion(ItemDto religion) {
//        this.religion = religion;
//    }

    public IdName getRequiredDegree() {
        return requiredDegree;
    }

    public void setRequiredDegree(IdName requiredDegree) {
        this.requiredDegree = requiredDegree;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public IdName getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(IdName jobFamily) {
        this.jobFamily = jobFamily;
    }

    public IdName getJobType() {
        return jobType;
    }

    public void setJobType(IdName jobType) {
        this.jobType = jobType;
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

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public IdName getDepartment() {
        return department;
    }

    public void setDepartment(IdName department) {
        this.department = department;
    }

    public LocaleDto getJobTitleLocale() {
        return jobTitleLocale;
    }

    public void setJobTitleLocale(LocaleDto jobTitleLocale) {
        this.jobTitleLocale = jobTitleLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VacancyDTO)) return false;

        VacancyDTO that = (VacancyDTO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getJobTitle() != null ? !getJobTitle().equals(that.getJobTitle()) : that.getJobTitle() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getRequirements() != null ? !getRequirements().equals(that.getRequirements()) : that.getRequirements() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getVacantPlaceCount() != null ? !getVacantPlaceCount().equals(that.getVacantPlaceCount()) : that.getVacantPlaceCount() != null)
            return false;
        if (getProposedSalary() != null ? !getProposedSalary().equals(that.getProposedSalary()) : that.getProposedSalary() != null)
            return false;
        if (getGender() != null ? !getGender().equals(that.getGender()) : that.getGender() != null) return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null) return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) return false;
        if (getLocation() != null ? !getLocation().equals(that.getLocation()) : that.getLocation() != null)
            return false;
        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null)
            return false;
//        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;
//        if (getEmbassy() != null ? !getEmbassy().equals(that.getEmbassy()) : that.getEmbassy() != null) return false;
        if (getContractStartDate() != null ? !getContractStartDate().equals(that.getContractStartDate()) : that.getContractStartDate() != null)
            return false;
        if (getContractEndDate() != null ? !getContractEndDate().equals(that.getContractEndDate()) : that.getContractEndDate() != null)
            return false;
        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null) return false;
        if (getManager() != null ? !getManager().equals(that.getManager()) : that.getManager() != null) return false;
//        if (getReligion() != null ? !getReligion().equals(that.getReligion()) : that.getReligion() != null)
//            return false;
        if (getRequiredDegree() != null ? !getRequiredDegree().equals(that.getRequiredDegree()) : that.getRequiredDegree() != null)
            return false;
        if (getResponsibilities() != null ? !getResponsibilities().equals(that.getResponsibilities()) : that.getResponsibilities() != null)
            return false;
        if (getJobFamily() != null ? !getJobFamily().equals(that.getJobFamily()) : that.getJobFamily() != null)
            return false;
        if (getJobType() != null ? !getJobType().equals(that.getJobType()) : that.getJobType() != null) return false;
        if (getNotes() != null ? !getNotes().equals(that.getNotes()) : that.getNotes() != null) return false;
        if (getAttachments() != null ? !getAttachments().equals(that.getAttachments()) : that.getAttachments() != null)
            return false;
        if (getCustomFields() != null ? !getCustomFields().equals(that.getCustomFields()) : that.getCustomFields() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getJobTitle() != null ? getJobTitle().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getRequirements() != null ? getRequirements().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getVacantPlaceCount() != null ? getVacantPlaceCount().hashCode() : 0);
        result = 31 * result + (getProposedSalary() != null ? getProposedSalary().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
//        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
//        result = 31 * result + (getEmbassy() != null ? getEmbassy().hashCode() : 0);
        result = 31 * result + (getContractStartDate() != null ? getContractStartDate().hashCode() : 0);
        result = 31 * result + (getContractEndDate() != null ? getContractEndDate().hashCode() : 0);
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        result = 31 * result + (getManager() != null ? getManager().hashCode() : 0);
//        result = 31 * result + (getReligion() != null ? getReligion().hashCode() : 0);
        result = 31 * result + (getRequiredDegree() != null ? getRequiredDegree().hashCode() : 0);
        result = 31 * result + (getResponsibilities() != null ? getResponsibilities().hashCode() : 0);
        result = 31 * result + (getJobFamily() != null ? getJobFamily().hashCode() : 0);
        result = 31 * result + (getJobType() != null ? getJobType().hashCode() : 0);
        result = 31 * result + (getNotes() != null ? getNotes().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VacancyDTO{" +
                "id=" + id +
                ", jobTitle='" + jobTitle + '\'' +
                ", description='" + description + '\'' +
                ", requirements='" + requirements + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", vacantPlaceCount=" + vacantPlaceCount +
                ", proposedSalary='" + proposedSalary + '\'' +
                ", gender='" + gender + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", number='" + number + '\'' +
                ", location=" + location +
                ", position=" + position +
                ", country=" + country +
                ", embassy=" + embassy +
                ", contractStartDate=" + contractStartDate +
                ", contractEndDate=" + contractEndDate +
                ", project=" + project +
                ", manager=" + manager +
                ", religion=" + religion +
                ", requiredDegree=" + requiredDegree +
                ", responsibilities='" + responsibilities + '\'' +
                ", jobFamily=" + jobFamily +
                ", jobType=" + jobType +
                ", notes=" + notes +
                ", attachments=" + attachments +
                ", customFields=" + customFields +
                '}';
    }
}
