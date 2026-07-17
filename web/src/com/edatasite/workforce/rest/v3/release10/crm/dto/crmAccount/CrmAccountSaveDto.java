package com.edatasite.workforce.rest.v3.release10.crm.dto.crmAccount;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.AddressAddDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CrmAccountSaveDto {
    private Integer objectId;
    private String objectKey;
    private String ownerName;
    private Integer ownerId;
    private ArrayList<Integer> selectedOwners;
    private String ownerNames;

    private String name;
    private Integer nameId;
    private String prefix;
    private Integer intNumber;
    private String number;
    private Integer numberId;

    private List<Integer> accountTypes;
    private Integer accountTypeID;

    private String email;
    private Integer emailId;
    private String phone;
    private Integer phoneId;
    private String fax;
    private Integer faxId;
    private String website;
    private Integer websiteId;

    private String note;
    private Integer noteId;

    private Integer entityId;

    private Integer importFileId;
    private Integer titleId;

    private Integer industryId;
    private String ownership;

    private Integer ratingId;
    private String rating;

    private String organizationType;

    private String annualRevenue;

    private Integer currencyId;
    private String currency;

    private Integer bankAccountId;
    private String bankAccount;

    private Integer paymentMethodId;
    private String paymentMethod;

    private String vatNumber;
    private String registrationNumber;
    private Integer logoId;
    private String logoUrl;

    private Integer parentID;

    private Date createdDate;
    private Date lastUpdatedDate;

    private BigDecimal creditLimit;
    private BigDecimal quoteCreditLimit;

    private boolean fromSignUp;
    private boolean fromSaasu;
    private boolean fromMobile;
    private boolean keyClient;
    private boolean fromQuickbooks;
    private boolean blocked;
    private boolean reverseChargeApplicable;
    private Boolean inTarget;
    private String targetId;
    private HashMap<String, Boolean> accountTypesDisabled;


//    private ArrayList<HistoryListItem> notes;

    private Date saasuLastUpdatedDate;
    private String saasuLastUpdatedUid;

    private Integer magentoEntityId;
    private Date magentoLastSyncDate;
    private boolean showContactAddress;
    private boolean hasContacts;
    private boolean createGlAccount;

    private Integer companyId;

    private String trn;

    private Integer taxTreatmentId;
    private Integer placeOfSupplyCountryId;
    private Integer placeOfSupplyStateId;
    private List<? extends CustomFieldRequest> customFields;
    private Boolean fromGoogle;
    private String googleUserId;
    private AddressAddDTO address;

    public AddressAddDTO getAddress() {
        return address;
    }

    public void setAddress(AddressAddDTO address) {
        this.address = address;
    }

    public CrmAccountSaveDto() {
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<Integer> getSelectedOwners() {
        return selectedOwners;
    }

    public void setSelectedOwners(ArrayList<Integer> selectedOwners) {
        this.selectedOwners = selectedOwners;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getNumberId() {
        return numberId;
    }

    public void setNumberId(Integer numberId) {
        this.numberId = numberId;
    }

    public List<Integer> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<Integer> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public Integer getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(Integer accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getFaxId() {
        return faxId;
    }

    public void setFaxId(Integer faxId) {
        this.faxId = faxId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getImportFileId() {
        return importFileId;
    }

    public void setImportFileId(Integer importFileId) {
        this.importFileId = importFileId;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }


    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(String annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getLogoId() {
        return logoId;
    }

    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getQuoteCreditLimit() {
        return quoteCreditLimit;
    }

    public void setQuoteCreditLimit(BigDecimal quoteCreditLimit) {
        this.quoteCreditLimit = quoteCreditLimit;
    }

    public boolean isFromSignUp() {
        return fromSignUp;
    }

    public void setFromSignUp(boolean fromSignUp) {
        this.fromSignUp = fromSignUp;
    }

    public boolean isFromSaasu() {
        return fromSaasu;
    }

    public void setFromSaasu(boolean fromSaasu) {
        this.fromSaasu = fromSaasu;
    }

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    public boolean isKeyClient() {
        return keyClient;
    }

    public void setKeyClient(boolean keyClient) {
        this.keyClient = keyClient;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isReverseChargeApplicable() {
        return reverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = reverseChargeApplicable;
    }

    public Boolean getInTarget() {
        return inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public HashMap<String, Boolean> getAccountTypesDisabled() {
        return accountTypesDisabled;
    }

    public void setAccountTypesDisabled(HashMap<String, Boolean> accountTypesDisabled) {
        this.accountTypesDisabled = accountTypesDisabled;
    }
//
//    public ArrayList<HistoryListItem> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(ArrayList<HistoryListItem> notes) {
//        this.notes = notes;
//    }

    public Date getSaasuLastUpdatedDate() {
        return saasuLastUpdatedDate;
    }

    public void setSaasuLastUpdatedDate(Date saasuLastUpdatedDate) {
        this.saasuLastUpdatedDate = saasuLastUpdatedDate;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public Integer getMagentoEntityId() {
        return magentoEntityId;
    }

    public void setMagentoEntityId(Integer magentoEntityId) {
        this.magentoEntityId = magentoEntityId;
    }

    public Date getMagentoLastSyncDate() {
        return magentoLastSyncDate;
    }

    public void setMagentoLastSyncDate(Date magentoLastSyncDate) {
        this.magentoLastSyncDate = magentoLastSyncDate;
    }

    public boolean isShowContactAddress() {
        return showContactAddress;
    }

    public void setShowContactAddress(boolean showContactAddress) {
        this.showContactAddress = showContactAddress;
    }

    public boolean isHasContacts() {
        return hasContacts;
    }

    public void setHasContacts(boolean hasContacts) {
        this.hasContacts = hasContacts;
    }

    public boolean isCreateGlAccount() {
        return createGlAccount;
    }

    public void setCreateGlAccount(boolean createGlAccount) {
        this.createGlAccount = createGlAccount;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public Integer getTaxTreatmentId() {
        return taxTreatmentId;
    }

    public void setTaxTreatmentId(Integer taxTreatmentId) {
        this.taxTreatmentId = taxTreatmentId;
    }

    public Integer getPlaceOfSupplyCountryId() {
        return placeOfSupplyCountryId;
    }

    public void setPlaceOfSupplyCountryId(Integer placeOfSupplyCountryId) {
        this.placeOfSupplyCountryId = placeOfSupplyCountryId;
    }

    public Integer getPlaceOfSupplyStateId() {
        return placeOfSupplyStateId;
    }

    public void setPlaceOfSupplyStateId(Integer placeOfSupplyStateId) {
        this.placeOfSupplyStateId = placeOfSupplyStateId;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public Boolean getFromGoogle() {
        return fromGoogle;
    }

    public void setFromGoogle(Boolean fromGoogle) {
        this.fromGoogle = fromGoogle;
    }

    public String getGoogleUserId() {
        return googleUserId;
    }

    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }
}
