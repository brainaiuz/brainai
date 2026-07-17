package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
@SolrDocument(collection = "crmAccountCore")
public class CrmAccountSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("doNonShow")
    private Boolean doNonShow;

    @Field("blocked")
    private Boolean blocked = false;

    @Field("crmAccountParentId")
    private Integer crmAccountParentId;

    @Field("crmAccountParentName")
    private String crmAccountParentName;

    @Field("crmAccountParentIdName")
    private String crmAccountParentIdName;

    @Field("crmAccountId")
    private Integer crmAccountId;

    @Field("crmAccountName")
    private String crmAccountName;

    @Field("crmAccountIdName")
    @Indexed(name = "crmAccountIdName", type = "string", stored = false)
    private String crmAccountIdName;

    @Field("crmAccountNumber")
    private String crmAccountNumber;

    @Field("typeId")
    @Indexed(name = "typeId", type = "pints", stored = false)
    private List<Integer> typeId = new ArrayList<>();

    @Field("typeName")
    @Indexed(name = "typeName", type = "strings")
    private List<String> typeName = new ArrayList<>();

    @Field("typeCode")
    @Indexed(name = "typeCode", type = "strings")
    private List<String> typeCode = new ArrayList<>();

    @Field("typeIdCode")
    @Indexed(name = "typeIdCode", type = "strings")
    private List<String> typeIdCode = new ArrayList<>();

    @Field("typeIdCodeName")
    @Indexed(name = "typeIdCodeName", type = "strings")
    private List<String> typeIdCodeName = new ArrayList<>();

    @Field("typeIds")
    @Indexed(name = "typeIds", type = "pints")
    private List<Integer> typeIds = new ArrayList<>();

    @Field("ownerId")
    @Indexed(name = "ownerId", type = "pints", stored = false)
    private List<Integer> ownerId = new ArrayList<>();

    @Field("ownerName")
    @Indexed(name = "ownerName", type = "strings")
    private List<String> ownerName = new ArrayList<>();

    @Field("ownerIdName")
    @Indexed(name = "ownerIdName", type = "strings")
    private List<String> ownerIdName = new ArrayList<>();

    @Field("ownershipId")
    @Indexed(name = "ownershipId", type = "pint", stored = false)
    private Integer ownershipId;

    @Field("ownershipName")
    private String ownershipName;

    @Field("ownershipCode")
    private String ownershipCode;

    @Field("ownershipIdCode")
    @Indexed(name = "ownershipIdCode", type = "string", stored = false)
    private String ownershipIdCode;

    @Field("ownershipIdCodeName")
    @Indexed(name = "ownershipIdCodeName", type = "string", stored = false)
    private String ownershipIdCodeName;

    @Field("industryId")
    @Indexed(name = "industryId", type = "pint", stored = false)
    private Integer industryId;

    @Field("industryName")
    private String industryName;

    @Field("industryCode")
    private String industryCode;

    @Field("industryIdCode")
    @Indexed(name = "industryIdCode", type = "string", stored = false)
    private String industryIdCode;

    @Field("industryIdCodeName")
    @Indexed(name = "industryIdCodeName", type = "string", stored = false)
    private String industryIdCodeName;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("fax")
    private String fax;

    @Field("website")
    private String website;

    @Field("organizationTypeId")
    @Indexed(name = "organizationTypeId", type = "pint", stored = false)
    private Integer organizationTypeId;

    @Field("organizationTypeName")
    private String organizationTypeName;

    @Field("organizationTypeCode")
    private String organizationTypeCode;

    @Field("organizationTypeIdCode")
    @Indexed(name = "organizationTypeIdCode", type = "string", stored = false)
    private String organizationTypeIdCode;

    @Field("organizationTypeIdCodeName")
    @Indexed(name = "organizationTypeIdCodeName", type = "string", stored = false)
    private String organizationTypeIdCodeName;

    @Field("numberOfEmployeesId")
    @Indexed(name = "numberOfEmployeesId", type = "pint", stored = false)
    private Integer numberOfEmployeesId;

    @Field("numberOfEmployeesName")
    private String numberOfEmployeesName;

    @Field("numberOfEmployeesCode")
    private String numberOfEmployeesCode;

    @Field("numberOfEmployeesIdCode")
    @Indexed(name = "numberOfEmployeesIdCode", type = "string", stored = false)
    private String numberOfEmployeesIdCode;

    @Field("annualRevenueId")
    @Indexed(name = "annualRevenueId", type = "pint", stored = false)
    private Integer annualRevenueId;

    @Field("annualRevenueName")
    private String annualRevenueName;

    @Field("annualRevenueCode")
    private String annualRevenueCode;

    @Field("annualRevenueIdCode")
    @Indexed(name = "annualRevenueIdCode", type = "string", stored = false)
    private String annualRevenueIdCode;

    @Field("annualRevenueIdCodeName")
    @Indexed(name = "annualRevenueIdCodeName", type = "string", stored = false)
    private String annualRevenueIdCodeName;

    @Field("ratingId")
    @Indexed(name = "ratingId", type = "pint", stored = false)
    private Integer ratingId;

    @Field("ratingName")
    private String ratingName;

    @Field("ratingCode")
    private String ratingCode;

    @Field("ratingIdCode")
    @Indexed(name = "ratingIdCode", type = "string", stored = false)
    private String ratingIdCode;

    @Field("adress1Id")
    private Integer adress1Id;

    @Field("street")
    private String street;

    @Field("streetb")
    private String streetb;

    @Field("city")
    private String city;

    @Field("countryId")
    private Integer countryId;

    @Field("countryName")
    private String countryName;

    @Field("countryCode")
    private String countryCode;

    @Field("countryIdCode")
    @Indexed(name = "countryIdCode", type = "string", stored = false)
    private String countryIdCode;

    @Field("countryIdCodeName")
    @Indexed(name = "countryIdCodeName", type = "string", stored = false)
    private String countryIdCodeName;

    @Field("stateId")
    private Integer stateId;

    @Field("stateName")
    private String stateName;

    @Field("stateIdName")
    @Indexed(name = "stateIdName", type = "string", stored = false)
    private String stateIdName;

    @Field("postCode")
    private String postCode;

    @Field("adress2Id")
    private Integer adress2Id;

    @Field("street2")
    private String street2;

    @Field("street2b")
    private String street2b;

    @Field("city2")
    private String city2;

    @Field("countryId2")
    private Integer countryId2;

    @Field("countryName2")
    private String countryName2;

    @Field("countryCode2")
    private String countryCode2;

    @Field("countryIdCode2")
    @Indexed(name = "countryIdCode2", type = "string", stored = false)
    private String countryIdCode2;

    @Field("stateId2")
    private Integer stateId2;

    @Field("stateName2")
    private String stateName2;

    @Field("stateIdName2")
    @Indexed(name = "stateIdName2", type = "string", stored = false)
    private String stateIdName2;

    @Field("postCode2")
    private String postCode2;

    @Field("currencyId")
    @Indexed(name = "currencyId", type = "pint", stored = false)
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("termId")
    @Indexed(name = "termId", type = "pint", stored = false)
    private Integer termId;

    @Field("termName")
    private String termName;

    @Field("termIdName")
    @Indexed(name = "termIdName", type = "string", stored = false)
    private String termIdName;

    @Field("vatNumber")
    private String vatNumber;

    @Field("trnNumber")
    private String trnNumber;

    @Field("registrationNumber")
    private String registrationNumber;

    @Field("paymentMethodId")
    @Indexed(name = "paymentMethodId", type = "pint", stored = false)
    private Integer paymentMethodId;

    @Field("paymentMethodName")
    private String paymentMethodName;

    @Field("paymentMethodCode")
    private String paymentMethodCode;

    @Field("paymentMethodIdCode")
    @Indexed(name = "paymentMethodIdCode", type = "string", stored = false)
    private String paymentMethodIdCode;

    @Field("creationDate")
    private Date creationDate;

    @Field("lastUpdateDate")
    private Date lastUpdateDate;

    @Field("clientBalance")
    private Double clientBalance;

    @Field("supplierBalance")
    private Double supplierBalance;

    @Field("creditLimit")
    private Double creditLimit;

    @Field("bankName")
    private String bankName;

    @Field("taxId")
    @Indexed(name = "taxId", type = "pint", stored = false)
    private Integer taxId;

    @Field("taxName")
    private String taxName;

    @Field("taxIdName")
    @Indexed(name = "taxIdName", type = "string", stored = false)
    private String taxIdName;


    @Field("inTarget")
    private Boolean inTarget;

    @Field("balanceDate")
    private Date balanceDate;


    @Field("supplierBalanceDate")
    private Date supplierBalanceDate;

    @Field("saasuGuid")
    private String saasuGuid;

    @Field("saasuUpdatedDate")
    private Date saasuUpdatedDate;

    @Field("saasuUpdatedUid")
    private String saasuUpdatedUid;

    @Field("contactId")
    private Integer contactId;

    @Field("contactName")
    private String contactName;

    @Field("contactEmail")
    private String contactEmail;

    @Field("salesTypeId")
    private Integer salesTypeId;

    @Field("salesTypeName")
    private String salesTypeName;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getDoNonShow() {
        return doNonShow != null && doNonShow;
    }

    public void setDoNonShow(Boolean doNonShow) {
        this.doNonShow = doNonShow;
    }

    public Boolean getBlocked() {
        return blocked != null && blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Integer getCrmAccountParentId() {
        return crmAccountParentId;
    }

    public void setCrmAccountParentId(Integer crmAccountParentId) {
        this.crmAccountParentId = crmAccountParentId;
    }

    public String getCrmAccountParentName() {
        return crmAccountParentName;
    }

    public void setCrmAccountParentName(String crmAccountParentName) {
        this.crmAccountParentName = crmAccountParentName;
    }

    public String getCrmAccountParentIdName() {
        return crmAccountParentIdName;
    }

    public void setCrmAccountParentIdName(String crmAccountParentIdName) {
        this.crmAccountParentIdName = crmAccountParentIdName;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }

    public String getCrmAccountName() {
        return crmAccountName;
    }

    public void setCrmAccountName(String crmAccountName) {
        this.crmAccountName = crmAccountName;
    }

    public String getCrmAccountIdName() {
        return crmAccountIdName;
    }

    public void setCrmAccountIdName(String crmAccountIdName) {
        this.crmAccountIdName = crmAccountIdName;
    }

    public String getCrmAccountNumber() {
        return crmAccountNumber;
    }

    public void setCrmAccountNumber(String crmAccountNumber) {
        this.crmAccountNumber = crmAccountNumber;
    }

    public List<Integer> getTypeId() {
        return typeId;
    }

    public void setTypeId(List<Integer> typeId) {
        this.typeId = typeId;
    }

    public List<String> getTypeName() {
        return typeName;
    }

    public void setTypeName(List<String> typeName) {
        this.typeName = typeName;
    }

    public List<String> getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(List<String> typeCode) {
        this.typeCode = typeCode;
    }

    public List<String> getTypeIdCode() {
        return typeIdCode;
    }

    public void setTypeIdCode(List<String> typeIdCode) {
        this.typeIdCode = typeIdCode;
    }

    public List<String> getTypeIdCodeName() {
        return typeIdCodeName;
    }

    public void setTypeIdCodeName(List<String> typeIdCodeName) {
        this.typeIdCodeName = typeIdCodeName;
    }

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public List<Integer> getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(List<Integer> ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(List<String> ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getOwnerIdName() {
        return ownerIdName;
    }

    public void setOwnerIdName(List<String> ownerIdName) {
        this.ownerIdName = ownerIdName;
    }

    public Integer getOwnershipId() {
        return ownershipId;
    }

    public void setOwnershipId(Integer ownershipId) {
        this.ownershipId = ownershipId;
    }

    public String getOwnershipName() {
        return ownershipName;
    }

    public void setOwnershipName(String ownershipName) {
        this.ownershipName = ownershipName;
    }

    public String getOwnershipCode() {
        return ownershipCode;
    }

    public void setOwnershipCode(String ownershipCode) {
        this.ownershipCode = ownershipCode;
    }

    public String getOwnershipIdCode() {
        return ownershipIdCode;
    }

    public void setOwnershipIdCode(String ownershipIdCode) {
        this.ownershipIdCode = ownershipIdCode;
    }

    public String getOwnershipIdCodeName() {
        return ownershipIdCodeName;
    }

    public void setOwnershipIdCodeName(String ownershipIdCodeName) {
        this.ownershipIdCodeName = ownershipIdCodeName;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryIdCode() {
        return industryIdCode;
    }

    public void setIndustryIdCode(String industryIdCode) {
        this.industryIdCode = industryIdCode;
    }

    public String getIndustryIdCodeName() {
        return industryIdCodeName;
    }

    public void setIndustryIdCodeName(String industryIdCodeName) {
        this.industryIdCodeName = industryIdCodeName;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(Integer organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    public String getOrganizationTypeName() {
        return organizationTypeName;
    }

    public void setOrganizationTypeName(String organizationTypeName) {
        this.organizationTypeName = organizationTypeName;
    }

    public String getOrganizationTypeCode() {
        return organizationTypeCode;
    }

    public void setOrganizationTypeCode(String organizationTypeCode) {
        this.organizationTypeCode = organizationTypeCode;
    }

    public String getOrganizationTypeIdCode() {
        return organizationTypeIdCode;
    }

    public void setOrganizationTypeIdCode(String organizationTypeIdCode) {
        this.organizationTypeIdCode = organizationTypeIdCode;
    }

    public String getOrganizationTypeIdCodeName() {
        return organizationTypeIdCodeName;
    }

    public void setOrganizationTypeIdCodeName(String organizationTypeIdCodeName) {
        this.organizationTypeIdCodeName = organizationTypeIdCodeName;
    }

    public Integer getNumberOfEmployeesId() {
        return numberOfEmployeesId;
    }

    public void setNumberOfEmployeesId(Integer numberOfEmployeesId) {
        this.numberOfEmployeesId = numberOfEmployeesId;
    }

    public String getNumberOfEmployeesName() {
        return numberOfEmployeesName;
    }

    public void setNumberOfEmployeesName(String numberOfEmployeesName) {
        this.numberOfEmployeesName = numberOfEmployeesName;
    }

    public String getNumberOfEmployeesCode() {
        return numberOfEmployeesCode;
    }

    public void setNumberOfEmployeesCode(String numberOfEmployeesCode) {
        this.numberOfEmployeesCode = numberOfEmployeesCode;
    }

    public String getNumberOfEmployeesIdCode() {
        return numberOfEmployeesIdCode;
    }

    public void setNumberOfEmployeesIdCode(String numberOfEmployeesIdCode) {
        this.numberOfEmployeesIdCode = numberOfEmployeesIdCode;
    }

    public Integer getAnnualRevenueId() {
        return annualRevenueId;
    }

    public void setAnnualRevenueId(Integer annualRevenueId) {
        this.annualRevenueId = annualRevenueId;
    }

    public String getAnnualRevenueName() {
        return annualRevenueName;
    }

    public void setAnnualRevenueName(String annualRevenueName) {
        this.annualRevenueName = annualRevenueName;
    }

    public String getAnnualRevenueCode() {
        return annualRevenueCode;
    }

    public void setAnnualRevenueCode(String annualRevenueCode) {
        this.annualRevenueCode = annualRevenueCode;
    }

    public String getAnnualRevenueIdCode() {
        return annualRevenueIdCode;
    }

    public void setAnnualRevenueIdCode(String annualRevenueIdCode) {
        this.annualRevenueIdCode = annualRevenueIdCode;
    }

    public String getAnnualRevenueIdCodeName() {
        return annualRevenueIdCodeName;
    }

    public void setAnnualRevenueIdCodeName(String annualRevenueIdCodeName) {
        this.annualRevenueIdCodeName = annualRevenueIdCodeName;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public String getRatingCode() {
        return ratingCode;
    }

    public void setRatingCode(String ratingCode) {
        this.ratingCode = ratingCode;
    }

    public String getRatingIdCode() {
        return ratingIdCode;
    }

    public void setRatingIdCode(String ratingIdCode) {
        this.ratingIdCode = ratingIdCode;
    }

    public Integer getAdress1Id() {
        return adress1Id;
    }

    public void setAdress1Id(Integer adress1Id) {
        this.adress1Id = adress1Id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetb() {
        return streetb;
    }

    public void setStreetb(String streetb) {
        this.streetb = streetb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryIdCode() {
        return countryIdCode;
    }

    public void setCountryIdCode(String countryIdCode) {
        this.countryIdCode = countryIdCode;
    }

    public String getCountryIdCodeName() {
        return countryIdCodeName;
    }

    public void setCountryIdCodeName(String countryIdCodeName) {
        this.countryIdCodeName = countryIdCodeName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateIdName() {
        return stateIdName;
    }

    public void setStateIdName(String stateIdName) {
        this.stateIdName = stateIdName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Integer getAdress2Id() {
        return adress2Id;
    }

    public void setAdress2Id(Integer adress2Id) {
        this.adress2Id = adress2Id;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet2b() {
        return street2b;
    }

    public void setStreet2b(String street2b) {
        this.street2b = street2b;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public Integer getCountryId2() {
        return countryId2;
    }

    public void setCountryId2(Integer countryId2) {
        this.countryId2 = countryId2;
    }

    public String getCountryName2() {
        return countryName2;
    }

    public void setCountryName2(String countryName2) {
        this.countryName2 = countryName2;
    }

    public String getCountryCode2() {
        return countryCode2;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getCountryIdCode2() {
        return countryIdCode2;
    }

    public void setCountryIdCode2(String countryIdCode2) {
        this.countryIdCode2 = countryIdCode2;
    }

    public Integer getStateId2() {
        return stateId2;
    }

    public void setStateId2(Integer stateId2) {
        this.stateId2 = stateId2;
    }

    public String getStateName2() {
        return stateName2;
    }

    public void setStateName2(String stateName2) {
        this.stateName2 = stateName2;
    }

    public String getStateIdName2() {
        return stateIdName2;
    }

    public void setStateIdName2(String stateIdName2) {
        this.stateIdName2 = stateIdName2;
    }

    public String getPostCode2() {
        return postCode2;
    }

    public void setPostCode2(String postCode2) {
        this.postCode2 = postCode2;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermIdName() {
        return termIdName;
    }

    public void setTermIdName(String termIdName) {
        this.termIdName = termIdName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTrnNumber() {
        return trnNumber;
    }

    public void setTrnNumber(String trnNumber) {
        this.trnNumber = trnNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getPaymentMethodIdCode() {
        return paymentMethodIdCode;
    }

    public void setPaymentMethodIdCode(String paymentMethodIdCode) {
        this.paymentMethodIdCode = paymentMethodIdCode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Double getClientBalance() {
        return clientBalance;
    }

    public void setClientBalance(Double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public Double getSupplierBalance() {
        return supplierBalance;
    }

    public void setSupplierBalance(Double supplierBalance) {
        this.supplierBalance = supplierBalance;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public String getTaxIdName() {
        return taxIdName;
    }

    public void setTaxIdName(String taxIdName) {
        this.taxIdName = taxIdName;
    }

    public Boolean getInTarget() {
        return inTarget != null && inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Date getSupplierBalanceDate() {
        return supplierBalanceDate;
    }

    public void setSupplierBalanceDate(Date supplierBalanceDate) {
        this.supplierBalanceDate = supplierBalanceDate;
    }

    public String getSaasuGuid() {
        return saasuGuid;
    }

    public void setSaasuGuid(String saasuGuid) {
        this.saasuGuid = saasuGuid;
    }

    public Date getSaasuUpdatedDate() {
        return saasuUpdatedDate;
    }

    public void setSaasuUpdatedDate(Date saasuUpdatedDate) {
        this.saasuUpdatedDate = saasuUpdatedDate;
    }

    public String getSaasuUpdatedUid() {
        return saasuUpdatedUid;
    }

    public void setSaasuUpdatedUid(String saasuUpdatedUid) {
        this.saasuUpdatedUid = saasuUpdatedUid;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getSalesTypeId() {
        return salesTypeId;
    }

    public void setSalesTypeId(Integer salesTypeId) {
        this.salesTypeId = salesTypeId;
    }

    public String getSalesTypeName() {
        return salesTypeName;
    }

    public void setSalesTypeName(String salesTypeName) {
        this.salesTypeName = salesTypeName;
    }
}
