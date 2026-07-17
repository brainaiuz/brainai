package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptAddDTO extends ResponseData {
    private Integer objectId;
    @NotNull(message = "bank account is required")
    private BankAccountTO bank_account;
    @NotNull(message = "date is required")
    private Date date;
    @JsonProperty("post_dated")
    private Boolean postDated = false;
    @JsonProperty("chq_number")
    private String chqNumber;
    private String number;
    private String reference;
    private String narration;
    private Integer salesOrderID;
    private CurrencyTO currency;
    private ProjectTO project;
    @JsonProperty("form_type")
    private Integer formType;
    @JsonProperty("tax_foreign_total")
    private BigDecimal taxForeignTotal;
    @JsonProperty("exchange_rate")
    private BigDecimal exchangeRate;
    @JsonProperty("tax_calculation_type")
    private Integer taxCalculationType;
    @NotEmpty(message = "account items are required.")
    @NotNull(message = "account items are required")
    private ArrayList<BankReceiptsAccountTO> accounts;
    private BigDecimal subtotal;
    @JsonProperty("tax_total")
    private BigDecimal taxTotal;
    @DecimalMin(value = "0.1", message = "total amount should be more than 0")
    private BigDecimal total;
    private List<NoteTO> notes;
    private List<AttachmentTO> attachments;
    private List<CompanyCustomFieldItem> customFields;

    public ReceiptAddDTO() {
    }

    public ReceiptAddDTO(Integer objectId, BankAccountTO bank_account, Date date, Boolean postDated, String chqNumber, String number, String reference, String narration, Integer salesOrderID, CurrencyTO currency, ProjectTO project, Integer formType, BigDecimal taxForeignTotal, BigDecimal exchangeRate, Integer taxCalculationType, ArrayList<BankReceiptsAccountTO> accounts, BigDecimal subtotal, BigDecimal taxTotal, BigDecimal total, List<NoteTO> notes, List<AttachmentTO> attachments, List<CompanyCustomFieldItem> customFields) {
        this.objectId = objectId;
        this.bank_account = bank_account;
        this.date = date;
        this.postDated = postDated;
        this.chqNumber = chqNumber;
        this.number = number;
        this.reference = reference;
        this.narration = narration;
        this.salesOrderID = salesOrderID;
        this.currency = currency;
        this.project = project;
        this.formType = formType;
        this.taxForeignTotal = taxForeignTotal;
        this.exchangeRate = exchangeRate;
        this.taxCalculationType = taxCalculationType;
        this.accounts = accounts;
        this.subtotal = subtotal;
        this.taxTotal = taxTotal;
        this.total = total;
        this.notes = notes;
        this.attachments = attachments;
        this.customFields = customFields;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public BankAccountTO getBank_account() {
        return bank_account;
    }

    public void setBank_account(BankAccountTO bank_account) {
        this.bank_account = bank_account;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getPostDated() {
        return postDated;
    }

    public void setPostDated(Boolean postDated) {
        this.postDated = postDated;
    }

    public String getChqNumber() {
        return chqNumber;
    }

    public void setChqNumber(String chqNumber) {
        this.chqNumber = chqNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Integer getSalesOrderID() {
        return salesOrderID;
    }

    public void setSalesOrderID(Integer salesOrderID) {
        this.salesOrderID = salesOrderID;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public ProjectTO getProject() {
        return project;
    }

    public void setProject(ProjectTO project) {
        this.project = project;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public BigDecimal getTaxForeignTotal() {
        return taxForeignTotal;
    }

    public void setTaxForeignTotal(BigDecimal taxForeignTotal) {
        this.taxForeignTotal = taxForeignTotal;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public ArrayList<BankReceiptsAccountTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<BankReceiptsAccountTO> accounts) {
        this.accounts = accounts;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<NoteTO> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteTO> notes) {
        this.notes = notes;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public List<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptAddDTO)) return false;

        ReceiptAddDTO that = (ReceiptAddDTO) o;

        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) return false;
        if (bank_account != null ? !bank_account.equals(that.bank_account) : that.bank_account != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (postDated != null ? !postDated.equals(that.postDated) : that.postDated != null) return false;
        if (chqNumber != null ? !chqNumber.equals(that.chqNumber) : that.chqNumber != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        if (narration != null ? !narration.equals(that.narration) : that.narration != null) return false;
        if (salesOrderID != null ? !salesOrderID.equals(that.salesOrderID) : that.salesOrderID != null) return false;
        if (currency != null ? !currency.equals(that.currency) : that.currency != null) return false;
        if (project != null ? !project.equals(that.project) : that.project != null) return false;
        if (formType != null ? !formType.equals(that.formType) : that.formType != null) return false;
        if (taxForeignTotal != null ? !taxForeignTotal.equals(that.taxForeignTotal) : that.taxForeignTotal != null)
            return false;
        if (exchangeRate != null ? !exchangeRate.equals(that.exchangeRate) : that.exchangeRate != null) return false;
        if (taxCalculationType != null ? !taxCalculationType.equals(that.taxCalculationType) : that.taxCalculationType != null)
            return false;
        if (accounts != null ? !accounts.equals(that.accounts) : that.accounts != null) return false;
        if (subtotal != null ? !subtotal.equals(that.subtotal) : that.subtotal != null) return false;
        if (taxTotal != null ? !taxTotal.equals(that.taxTotal) : that.taxTotal != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (attachments != null ? !attachments.equals(that.attachments) : that.attachments != null) return false;
        if (customFields != null ? !customFields.equals(that.customFields) : that.customFields != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectId != null ? objectId.hashCode() : 0;
        result = 31 * result + (bank_account != null ? bank_account.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (postDated != null ? postDated.hashCode() : 0);
        result = 31 * result + (chqNumber != null ? chqNumber.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (narration != null ? narration.hashCode() : 0);
        result = 31 * result + (salesOrderID != null ? salesOrderID.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (formType != null ? formType.hashCode() : 0);
        result = 31 * result + (taxForeignTotal != null ? taxForeignTotal.hashCode() : 0);
        result = 31 * result + (exchangeRate != null ? exchangeRate.hashCode() : 0);
        result = 31 * result + (taxCalculationType != null ? taxCalculationType.hashCode() : 0);
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        result = 31 * result + (subtotal != null ? subtotal.hashCode() : 0);
        result = 31 * result + (taxTotal != null ? taxTotal.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (attachments != null ? attachments.hashCode() : 0);
        result = 31 * result + (customFields != null ? customFields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReceiptAddDTO{" +
                "objectId=" + objectId +
                ", bank_account=" + bank_account +
                ", date=" + date +
                ", postDated=" + postDated +
                ", chqNumber='" + chqNumber + '\'' +
                ", number='" + number + '\'' +
                ", reference='" + reference + '\'' +
                ", narration='" + narration + '\'' +
                ", salesOrderID=" + salesOrderID +
                ", currency=" + currency +
                ", project=" + project +
                ", formType=" + formType +
                ", taxForeignTotal=" + taxForeignTotal +
                ", exchangeRate=" + exchangeRate +
                ", taxCalculationType=" + taxCalculationType +
                ", accounts=" + accounts +
                ", subtotal=" + subtotal +
                ", taxTotal=" + taxTotal +
                ", total=" + total +
                ", notes=" + notes +
                ", attachments=" + attachments +
                ", customFields=" + customFields +
                '}';
    }
}
