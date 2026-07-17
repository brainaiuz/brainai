package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v3.release10.accounting.constants.AccountingAPIFieldConstants;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"properties"})
public class BaseInvoiceDto implements AccountingAPIFieldConstants, Serializable {
    private Integer id;
    @JsonAlias({"objectkey", "objectKey"})
    private String objectKey;
    @NotNull(message = "Customer is required")
    @JsonAlias({"client", "customer", "supplier"})
    private ItemDto customer;
    private String number;
    private String status;
    private String currencyCode;
    private List<ItemDto> approver;
    private BigDecimal exchangeRate;
    @NotNull(message = "Invoice date is required.")
//    @JsonSerialize(using = CustomDateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
//    @JsonSerialize(using = CustomDateSerializer.class)
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;
    private IdName terms;
    @Pattern(regexp = "DUE_DATE|TERMS", message = "dueDateType must be one of DUE_DATE/TERMS")
    private String dueDateType;
    private String reference;
    @JsonAlias({"taxCalcType", "taxcalctype", "tax_calc_type"})
    @Pattern(regexp = "NO_TAX|TAX_INCLUSIVE|TAX_EXCLUSIVE", message = "taxCalcType must be one of NO_TAX/TAX_INCLUSIVE/TAX_EXCLUSIVE")
    private String taxCalcType;
    @JsonAlias({"priceLevel", "price_level"})
    private ItemDto priceLevel;
    @JsonAlias({"bankAccount", "bank_account"})
    private IdDTO bankAccount;
    @JsonAlias({"project", "relatedProject", "related_project"})
    private IdCode project;
//    @Valid
//    @NotNull(message = "You must provide Invoice line items.")
    private List<LineItemDto> items;
    private String statusColor;

    @Valid
    private List<? extends CustomFieldRequest> customFields;
    private IdNameTO taxCalcTypeItem;
    private BigDecimal totalInInvoiceCurrency;

    /**
     * This one is for "handling Custom Field" purpose
     */
    @JsonIgnore
    private Map<String, Object> properties;

    public BaseInvoiceDto() {
    }

    @JsonAnySetter
    public void addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    @JsonIgnore
    public void setSupplier(ItemDto supplier) {
        addProperty(SUPPLIER, supplier);
    }

    @JsonIgnore
    public void setStatusCode(String statusCode) {
        addProperty(STATUS_CODE, statusCode);
    }

    @JsonIgnore
    public void setContact(ItemDto contact) {
        addProperty(CONTACT, contact);
    }

    @JsonIgnore
    public void setOpportunity(ItemDto opportunity) {
        addProperty(OPPORTUNITY, opportunity);
    }

    @JsonIgnore
    public void setBillingAddress(AddressDto address) {
        addProperty(BILLING_ADDRESS, address);
    }

    @JsonIgnore
    public void setShippingAddress(AddressDto address) {
        addProperty(SHIPPING_ADDRESS, address);
    }

    @JsonIgnore
    public void setCurrentApprover(ItemDto approver) {
        addProperty(CURRENT_APPROVER, approver);
    }

    @JsonIgnore
    public void setSubTotal(BigDecimal subTotal) {
        addProperty(SUBTOTAL, subTotal);
    }

    @JsonIgnore
    public void setDiscountTotal(BigDecimal discountTotal) {
        addProperty(DISCOUNT_TOTAL, discountTotal);
    }

    @JsonIgnore
    public void setTaxTotal(BigDecimal taxTotal) {
        addProperty(TAX_TOTAL, taxTotal);
    }

    @JsonIgnore
    public void setTotal(BigDecimal total) {
        addProperty(TOTAL, total);
    }

    @JsonIgnore
    public void setTotalInBase(BigDecimal totalInBase) {
        addProperty(TOTAL_IN_BASE, totalInBase);
    }

    @JsonIgnore
    public void setCreatedDate(String createdDate) {
        addProperty(CREATED_DATE, createdDate);
    }

    @JsonIgnore
    public void setUpdatedDate(String updatedDate) {
        addProperty(UPDATED_DATE, updatedDate);
    }

    @JsonIgnore
    public boolean isExistingObject() {
        return StringUtils.isNotBlank(getObjectKey()) || getId() != null || StringUtils.isNotBlank(getNumber());
    }

    public String getStringValue(String key) {
        Object value = getProperties().get(key);

        if (value != null && value instanceof String) {
            return (String) value;
        }
        return value != null ? value.toString() : null;
    }

    String getString(String key) {
        Object value = getProperties().get(key);
        if (value != null && value instanceof String) {
            return (String) value;
        }
        return null;
    }

    Integer getInteger(String key) {
        Object value = getProperties().get(key);
        if (value != null && value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public ItemDto getCustomer() {
        return customer;
    }

    public void setCustomer(ItemDto customer) {
        this.customer = customer;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public IdName getTerms() {
        return terms;
    }

    public void setTerms(IdName terms) {
        this.terms = terms;
    }

    public String getDueDateType() {
        return dueDateType;
    }

    public void setDueDateType(String dueDateType) {
        this.dueDateType = dueDateType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTaxCalcType() {
        return taxCalcType;
    }

    public void setTaxCalcType(String taxCalcType) {
        this.taxCalcType = taxCalcType;
    }

    public ItemDto getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(ItemDto priceLevel) {
        this.priceLevel = priceLevel;
    }

    public IdDTO getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(IdDTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public IdCode getProject() {
        return project;
    }

    public void setProject(IdCode project) {
        this.project = project;
    }

    public List<LineItemDto> getItems() {
        return items;
    }

    public void setItems(List<LineItemDto> items) {
        this.items = items;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<ItemDto> getApprover() {
        return approver;
    }

    public void setApprover(List<ItemDto> approver) {
        this.approver = approver;
    }

    public IdNameTO getTaxCalcTypeItem() {
        return taxCalcTypeItem;
    }

    public void setTaxCalcTypeItem(IdNameTO taxCalcTypeItem) {
        this.taxCalcTypeItem = taxCalcTypeItem;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }
}
