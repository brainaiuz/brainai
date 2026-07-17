package com.edatasite.workforce.rest.v3.release10.crm.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 3/23/2021 7:54 PM
 */
public class OpportunityDto extends DynamicDto {
    private Integer id;
    private String number;
    @NotNull(message = "Name is required")
    private String name;
    private IdCode assignee;
    @NotNull(message = "Customer is required")
    private ItemDto customer;
    private IdName contact;
    private IdCode source;

    @NotNull(message = "Stage is required")
    private IdCode stage;
    private Double amount;
    private IdCode currency;
    @JsonAlias({"exchangeRate", "exchangerate"})
    private BigDecimal exchangeRate;
    @JsonAlias({"taxCalcType", "taxcalctype", "tax_calc_type"})
    @Pattern(regexp = "NO_TAX|TAX_INCLUSIVE|TAX_EXCLUSIVE", message = "taxCalcType must be one of NO_TAX/TAX_INCLUSIVE/TAX_EXCLUSIVE")
    private String taxCalcType;

    @NotNull(message = "Close date is required.")
    @JsonAlias({"closeDate", "closedate"})
    //@JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeDate;
    private Double probability;
    private String nextStep;

    @JsonAlias({"keepItems", "keepitems"})
    private boolean keepItems;
    @Valid
    private List<OpportunityItemDto> items;

    @Valid
    private List<NoteDto> notes;
    @Valid
    @JsonAlias({"customFields", "customfields"})
    private List<CustomFieldRequest> customFields;

    private ItemDto rejectReason;
    private String rejectNote;
    private OpportunityContactTO opportunityContacts;
    private IdCode type;

    private IdNameTO campaign;
    private List<AttachmentTO> attachments;
    private String email;
    private String phone;


    public OpportunityDto() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdCode getAssignee() {
        return assignee;
    }

    public void setAssignee(IdCode assignee) {
        this.assignee = assignee;
    }

    public ItemDto getCustomer() {
        return customer;
    }

    public void setCustomer(ItemDto customer) {
        this.customer = customer;
    }

    public IdName getContact() {
        return contact;
    }

    public void setContact(IdName contact) {
        this.contact = contact;
    }

    public IdCode getSource() {
        return source;
    }

    public void setSource(IdCode source) {
        this.source = source;
    }

    public IdCode getStage() {
        return stage;
    }

    public void setStage(IdCode stage) {
        this.stage = stage;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public IdCode getCurrency() {
        return currency;
    }

    public void setCurrency(IdCode currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getTaxCalcType() {
        return taxCalcType;
    }

    public void setTaxCalcType(String taxCalcType) {
        this.taxCalcType = taxCalcType;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public boolean isKeepItems() {
        return keepItems;
    }

    public void setKeepItems(boolean keepItems) {
        this.keepItems = keepItems;
    }

    public List<OpportunityItemDto> getItems() {
        return items;
    }

    public void setItems(List<OpportunityItemDto> items) {
        this.items = items;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public String toString() {
        return "OpportunityDto{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", assignee=" + assignee +
                ", customer=" + customer +
                ", contact=" + contact +
                ", source=" + source +
                ", stage=" + stage +
                ", amount=" + amount +
                ", currency=" + currency +
                ", exchangeRate=" + exchangeRate +
                ", taxCalcType='" + taxCalcType + '\'' +
                ", closeDate=" + closeDate +
                ", probability=" + probability +
                ", keepItems=" + keepItems +
                ", items=" + items +
                ", notes=" + notes +
                ", customFields=" + customFields +
                '}';
    }

    public ItemDto getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(ItemDto rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public OpportunityContactTO getOpportunityContacts() {
        return opportunityContacts;
    }

    public void setOpportunityContacts(OpportunityContactTO opportunityContacts) {
        this.opportunityContacts = opportunityContacts;
    }

    public IdCode getType() {
        return type;
    }

    public void setType(IdCode type) {
        this.type = type;
    }

    public IdNameTO getCampaign() {
        return campaign;
    }

    public void setCampaign(IdNameTO campaign) {
        this.campaign = campaign;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
