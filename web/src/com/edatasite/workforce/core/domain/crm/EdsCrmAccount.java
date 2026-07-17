package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.accounting.EdsSubsidiariesCompany;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.enums.SalesType;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 13:32:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmAccount")
public class EdsCrmAccount extends EdsTraceable implements ObjectHistory, ObjectIdentifier {
    public static final String _CRM_ACCOUNT_TYPE = "_CRM_ACCOUNT_TYPE";
    public static final String ANALYST = "ANALYST";
    public static final String COMPETITOR = "COMPETITOR";
    public static final String CONSIGNOR = "CONSIGNOR";
    public static final String CONSIGNEE = "CONSIGNEE";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String DISTRIBUTOR = "DISTRIBUTOR";
    public static final String INTEGRATOR = "INTEGRATOR";
    public static final String OTHER = "OTHER";
    public static final String PARTNER = "CRM_PARTNER";
    public static final String PRESS = "PRESS";
    public static final String PROSPECT = "PROSPECT";
    public static final String RESELLER = "RESELLER";
    public static final String SUPPLIER = "SUPPLIER";
    public static final String VENDOR = "VENDOR";

    public static final String _OWNERSHIP = "_OWNERSHIP";
    public static final String OTHER2 = "OTHER";
    public static final String PRIVATE = "PRIVATE";
    public static final String PUBLIC = "PUBLIC";
    public static final String SUBSIDARY = "SUBSIDARY";

    public static final String CUSTOM_FIELD_AS_MARGIN = "double_value50";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "objectKey", unique = true, updatable = false)
    private String objectKey;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsUser owner;*/

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "crmaccount_owners",
            joinColumns = {@JoinColumn(name = "crmaccount_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsUser> owners = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private EdsCrmAccount parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = " deleted = 'false' ")
    private List<EdsCrmAccount> childList = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "number")
    private String number;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "number_integer")
    private Integer numberInteger;

    @Column(name = "note")
    private String note;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "crmAccount_types",
            joinColumns = {@JoinColumn(name = "crmaccount_id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id")}
    )
    private Set<EdsReference> accountTypes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccount")
    @Where(clause = " deleted = 'false' ")
    private Set<EdsCrmContact> crmContacts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownership")
    private EdsReference ownership;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating")
    private EdsReference rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annualRevenueId")
    private EdsReference annualRevenue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numberOfEmployeesId")
    private EdsReference numberOfEmployees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationTypeId")
    private EdsReference organizationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry")
    private EdsReference industry;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatCategoryId")
    private EdsReference vatCategory;

    @Column(name = "otherorganizationType")
    private String otherOrganizationType;

    private String otherIndustry;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "website")
    private String website;

    @Column(name = "client_id")
    private Integer clientID;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "sasuuLastUpdatedTime")
    private Date sasuuLastUpdatedTime;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean blocked = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean reverseChargeApplicable = false; // we need this field when reverseCharge is Enabled.

    @Column(name = "importFileID")
    private Integer importFileID;

    @Column(name = "signupcompanyid")
    private Integer signupCompanyId;

    @Column(name = "entity_id")
    private Integer entityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientTypeId")
    private EdsReference clientType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "billingaddressid")
    private EdsAddress billingAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mailingaddressid")
    private EdsAddress mailingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(name = "vatNumber")
    private String vatNumber;

    @Column(name = "registrationNumber")
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodId")
    private EdsPaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaignid")
    private EdsCampaign campaign;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmCustomFields customFields;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo")
    private EdsAttachment logo;

    private String description;

    @Column(precision = 25, scale = 5)
    private BigDecimal balanceAmount;

    private Date balanceDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal supplierBalanceAmount;

    private Date supplierBalanceDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal appliedCredit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "termsid")
    private EdsInvoiceTerms terms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receivable")
    private EdsAccount receivable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payable")
    private EdsAccount payable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsidiary")
    private EdsSubsidiariesCompany subsidiary;

    private Boolean isPaid;

    @Column(name = "clientBalance", precision = 25, scale = 5)
    private BigDecimal clientBalance = BigDecimal.ZERO;

    @Column(name = "supplierBalance", precision = 25, scale = 5)
    private BigDecimal supplierBalance = BigDecimal.ZERO;

    @Column(name = "otherBalance", precision = 25, scale = 5)
    private BigDecimal otherBalance = BigDecimal.ZERO;

    private Boolean balanceCalculated = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseID")
    private EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentID")
    private EdsDepartment department;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID")
    @Where(clause = " entityType = '" + EdsAddress.ENTITY_TYPE_CRM_ACCOUNT + "' and relationType = 0 and deleted = 'false'")
    private List<EdsAddress> billingAddresses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID")
    @Where(clause = " entityType = '" + EdsAddress.ENTITY_TYPE_CRM_ACCOUNT + "' and relationType = 1 and deleted = 'false'")
    private List<EdsAddress> mailingAddresses = new ArrayList<>();

    @Transient
    private List<EdsAddress> billAddressesTransient = new ArrayList<>();

    @Transient
    private List<EdsAddress> mailAddressesTransient = new ArrayList<>();


    //CLIENT
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Set<EdsProject> projects = new HashSet<>();

    @Column(name = "quickbook_customer_id")
    private String quickbookCustomerID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

    @Column(name = "saasu_guid")
    private String saasuGUID;

    @Column(name = "saasuLastUpdatedUid")
    private String saasuLastUpdatedUid;

    private Integer magentoEntityId;

    private Date magentoLastSyncDate;

    @Column(name = "creditLimit", precision = 25, scale = 5)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "quoteCreditLimit", precision = 25, scale = 5)
    private BigDecimal quoteCreditLimit = BigDecimal.ZERO;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "client_price_level",
            joinColumns = {@JoinColumn(name = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "price_level_id")})
    private List<EdsPriceLevel> priceLevels = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "discount_applied_clients",
            joinColumns = {@JoinColumn(name = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "discount_id")})
    private List<EdsDiscount> discounts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id")
    private EdsShippingMethod shippingMethod;

    @Column(name = "in_target", columnDefinition = "boolean default false")
    private Boolean inTarget = false;

    @Column(name = "targetId")
    private String targetId;

    @Transient
    private EdsClientContact[] clientContacts;

    //for imports only...
    @Transient
    private Integer ownerID;

    private String bankName;
    private String accountName;
    private String accountNo;
    private String swiftCode;
    private String sortCode;
    private String ibanCode;
    private String branch;
    private String bankAddress;

    private String vatCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankAccountId")
    private EdsBankAccount bankAccount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private List<EdsExpense> expense = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatid")
    private EdsVat vat;

    @Transient
    private CompanyCustomFieldItem[] transientCustomFields;
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean showPrimaryContactAddress = false;

    //Below 3 fields were added as part of GCC VAT changes
    @Column(name = "tax_treatment")
    private Integer taxtreatmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_treatment", insertable = false, updatable = false)
    private EdsReference taxTreatment;
    private String trn;
    @Column(name = "placeofsupply_countryid")
    private Integer placeofsupplyCountryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_countryid", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCountry placeOfSupplyCountry;
    @Column(name = "placeofsupply_stateid")
    private Integer placeofsupplyStateId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_stateid", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsRegion placeOfSupplyState;

    @Transient
    private String entityType;
    @Column(columnDefinition = "boolean DEFAULT false")
    private boolean createGlAccount = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accountid")
    private List<EdsCrmContactItemParams> itemParams = new ArrayList<>();

    private SalesType salesType;

    @Column(name = "crNumber")
    private String crNumber;

    @Column(name = "pasportNumber")
    private String pasportNumber;

    @Column(name = "faiUuid")
    private String faiUuid;

    public BigDecimal getAppliedCredit() {
        return appliedCredit != null ? appliedCredit : BigDecimal.ZERO;
    }

    public void setTerms(EdsInvoiceTerms terms) {
        if (!ServerUtils.equalsEdsObject(this.terms, terms)) {
            addChange(CustomFormConstants.CLIENT_INVOICE_TERM);
        }
        this.terms = terms;
    }

    public Boolean isPaid() {
        return isPaid != null && isPaid;
    }

    public Set<EdsReference> getAccountTypes() {
        if (accountTypes == null) {
            accountTypes = new HashSet<>();
        }
        return accountTypes;
    }

    public Set<EdsCrmContact> getCrmContacts() {
        if (crmContacts == null) {
            crmContacts = new HashSet<>();
        }
        return crmContacts;
    }

    public void addAccountType(EdsReference accountType) {
        if (accountType != null) {
            getAccountTypes().add(accountType);
        }
    }

    public EdsCrmAccount getParent() {
        if (!ServerUtils.equalsEdsObject(this.parent, parent)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_PARENT);
        }
        return parent;
    }

    public void setCurrency(EdsCurrency currency) {
        if (!ServerUtils.equalsEdsObject(this.currency, currency)) {
            addChange(CustomFormConstants.CURRENCY);
        }
        this.currency = currency;
    }

    public void setVatNumber(String vatNumber) {
        if (!ServerUtils.equalsString(this.vatNumber, vatNumber)) {
            addChange(CustomFormConstants.VAT_NUMBER);
        }
        this.vatNumber = vatNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (!ServerUtils.equalsString(this.registrationNumber, registrationNumber)) {
            addChange(CustomFormConstants.REGISTRATION_NUMBER);
        }
        this.registrationNumber = registrationNumber;
    }

    public void setPaymentMethod(EdsPaymentMethod paymentMethod) {
        if (!ServerUtils.equalsEdsObject(this.paymentMethod, paymentMethod)) {
            addChange(CustomFormConstants.PAYMENT_METHOD);
        }
        this.paymentMethod = paymentMethod;
    }

    /*public void setOwner(EdsUser owner) {
        if (!ServerUtils.equalsEdsObject(this.owner, owner)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_OWNER);
        }
        this.owner = owner;
    }

    public EdsCompany getCompany() {
        return getOwner() == null ? null : getOwner().getCompany();
    }*/

    public void addOwner(EdsUser owner) {
        if (owner != null) {
            getOwners().add(owner);
            addChange(CustomFormConstants.CRM_ACCOUNT_OWNER);
        }
    }

    public HashMap<Integer, EdsUser> getOwnersMap() {
        HashMap<Integer, EdsUser> ownersMap = new HashMap<>();
        if (getOwners() == null || getOwners().isEmpty()) {
            return ownersMap;
        }
        getOwners().forEach(owner -> ownersMap.put(owner.getObjectID(), owner));
        return ownersMap;
    }


    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_NUMBER);
        }
        this.number = number;
        if (this.number != null && !"".equals(this.number)) {
            setNumberInteger(this.number);
        } else {
            setNumberInteger(0);
        }
    }

    private void setNumberInteger(String number) {
        String numberInteger = "";
        if (number != null && !"".equals(number)) {
            StringBuilder numberIntegerBuilder = new StringBuilder();
            for (int i = 0; i < number.length(); i++) {
                String letter = number.substring(i, i + 1);
                if (letter != null && !"".equals(letter) && letter.matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    numberIntegerBuilder.append(letter);
                }
            }
            numberInteger = numberIntegerBuilder.toString();
        }
        if ("".equals(numberInteger)) {
            setNumberInteger(0);
        } else {
            try {
                setNumberInteger(Integer.valueOf(numberInteger));
            } catch (NumberFormatException e) {
                setNumberInteger(0);
            }
        }
    }

    /**
     * donot use this method. but donot remove.
     *
     * @param numberInteger
     */
    @Deprecated
    public void setNumberInteger(Integer numberInteger) {
        this.numberInteger = numberInteger;
    }

    public void setIndustry(EdsReference industry) {
        if (!ServerUtils.equalsEdsObject(this.industry, industry)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_INDUSTRY);
        }
        this.industry = industry;
    }

    public void setOwnership(EdsReference ownership) {
        if (!ServerUtils.equalsEdsObject(this.ownership, ownership)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_OWNERSHIP);
        }
        this.ownership = ownership;
    }

    public void setRating(EdsReference rating) {
        if (!ServerUtils.equalsEdsObject(this.rating, rating)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_RATING);
        }
        this.rating = rating;
    }

    public void setAnnualRevenue(EdsReference annualRevenue) {
        if (!ServerUtils.equalsEdsObject(this.annualRevenue, annualRevenue)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE);
        }
        this.annualRevenue = annualRevenue;
    }

    public void setNumberOfEmployees(EdsReference numberOfEmployees) {
        if (!ServerUtils.equalsEdsObject(this.numberOfEmployees, numberOfEmployees)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE);
        }
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setOrganizationType(EdsReference organizationType) {
        if (!ServerUtils.equalsEdsObject(this.organizationType, organizationType)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE);
        }
        this.organizationType = organizationType;
    }

    public void setEmail(String email) {
        if (!ServerUtils.equalsString(this.email, email)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_EMAIL);
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (!ServerUtils.equalsString(this.phone, phone)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_PHONE);
        }
        this.phone = phone;
    }

    public void setFax(String fax) {
        if (!ServerUtils.equalsString(this.fax, fax)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_FAX);
        }
        this.fax = fax;
    }

    public void setWebsite(String website) {
        if (!ServerUtils.equalsString(this.website, website)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_WEBSITE);
        }
        this.website = website;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_NAME);
        }
        this.name = name;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public EdsAddress getBillingAddress(boolean... doNotReturnNull) {
        if (billingAddress == null && doNotReturnNull != null && doNotReturnNull.length > 0 && doNotReturnNull[0]) {
            return new EdsAddress();
        }
        return billingAddress;
    }

    public void setBillingAddress(EdsAddress billingAddress) {
        this.billingAddress = billingAddress;
        if (this.billingAddress != null) {
            this.billingAddress.setEntityID(getObjectID());
            this.billingAddress.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            this.billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
            this.billingAddress.setAccountID(getObjectID());
        }
    }

    public void setImportBillingAddress(EdsAddress billingAddress) {
        if (this.billingAddress != null && billingAddress != null) {
            if (billingAddress.getName() != null) {
                this.billingAddress.setName(billingAddress.getName());
            }
            if (billingAddress.getAddress() != null) {
                this.billingAddress.setAddress(billingAddress.getAddress());
            }
            if (billingAddress.getAddressb() != null) {
                this.billingAddress.setAddressb(billingAddress.getAddressb());
            }
            if (billingAddress.getCity() != null) {
                this.billingAddress.setCity(billingAddress.getCity());
            }
            if (billingAddress.getCountry() != null) {
                this.billingAddress.setCountry(billingAddress.getCountry());
            }
            if (billingAddress.getState() != null) {
                this.billingAddress.setState(billingAddress.getState());
            }
            if (billingAddress.getZipCode() != null) {
                this.billingAddress.setZipCode(billingAddress.getZipCode());
            }
        } else {
            this.billingAddress = billingAddress;
        }
        if (this.billingAddress != null) {
            this.billingAddress.setEntityID(getObjectID());
            this.billingAddress.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            this.billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
            this.billingAddress.setAccountID(getObjectID());
        }
    }

    public EdsAddress getMailingAddress(boolean... doNotReturnNull) {
        if (mailingAddress == null && doNotReturnNull != null && doNotReturnNull.length > 0 && doNotReturnNull[0]) {
            return new EdsAddress();
        }
        return mailingAddress;
    }

    public void setMailingAddress(EdsAddress mailingAddress) {
        this.mailingAddress = mailingAddress;
        if (this.mailingAddress != null) {
            this.mailingAddress.setEntityID(getObjectID());
            this.mailingAddress.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            this.mailingAddress.setRelationType(EdsAddress.MAILING_ADDRESS);
            this.mailingAddress.setAccountID(getObjectID());
        }
    }

    public void setImportMailingAddress(EdsAddress mailingAddress) {
        if (this.mailingAddress != null && mailingAddress != null) {
            if (mailingAddress.getName() != null) {
                this.mailingAddress.setName(mailingAddress.getName());
            }
            if (mailingAddress.getAddress() != null) {
                this.mailingAddress.setAddress(mailingAddress.getAddress());
            }
            if (mailingAddress.getAddressb() != null) {
                this.mailingAddress.setAddressb(mailingAddress.getAddressb());
            }
            if (mailingAddress.getCity() != null) {
                this.mailingAddress.setCity(mailingAddress.getCity());
            }
            if (mailingAddress.getCountry() != null) {
                this.mailingAddress.setCountry(mailingAddress.getCountry());
            }
            if (mailingAddress.getState() != null) {
                this.mailingAddress.setState(mailingAddress.getState());
            }
            if (mailingAddress.getZipCode() != null) {
                this.mailingAddress.setZipCode(mailingAddress.getZipCode());
            }
        } else {
            this.mailingAddress = mailingAddress;
        }
        if (this.mailingAddress != null) {
            this.mailingAddress.setEntityID(getObjectID());
            this.mailingAddress.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            this.mailingAddress.setRelationType(EdsAddress.MAILING_ADDRESS);
            this.mailingAddress.setAccountID(getObjectID());
        }
    }

    public boolean isInTarget() {
        return inTarget != null && inTarget;
    }

    public void addBillAddressTransient(EdsAddress address) {
        billAddressesTransient.add(address);
    }

    public void addMailAddressTransient(EdsAddress address) {
        mailAddressesTransient.add(address);
    }

    public Integer getPrimaryBillingAddressId() {
        if (billingAddresses != null && !billingAddresses.isEmpty()) {
            for (EdsAddress address : billingAddresses) {
                if (address.isPrimary()) {
                    return address.getObjectID();
                }
            }

            return billingAddresses.get(0).getObjectID();
        }
        return null;
    }

    public Integer getPrimaryMalingAddressId() {
        if (mailingAddresses != null && !mailingAddresses.isEmpty()) {
            for (EdsAddress address : mailingAddresses) {
                if (address.isPrimary()) {
                    return address.getObjectID();
                }
            }

            return mailingAddresses.get(0).getObjectID();
        }
        return null;
    }

    public List<EdsAddress> getAddresses() {
        List<EdsAddress> addresses = new ArrayList<>();
        if (getBillingAddresses() != null) {
            addresses.addAll(getBillingAddresses());
        }
        if (getMailingAddress() != null) {
            addresses.addAll(getMailingAddresses());
        }
        return addresses;
    }

    public Address getAddressData() {
        Address billingAddress = new Address();
        if (billingAddress != null) {
            if (billingAddress.getCountry() != null) {
                billingAddress.setCountryId(billingAddress.getCountryId());
            }
            if (billingAddress.getState() != null) {
                billingAddress.setStateId(billingAddress.getStateId());
            }
            if (billingAddress.getAddress() != null) {
                billingAddress.setAddress(billingAddress.getAddress());
            }
            if (billingAddress.getCity() != null) {
                billingAddress.setCity(billingAddress.getCity());
            }
            if (billingAddress.getZipCode() != null) {
                billingAddress.setZipCode(billingAddress.getZipCode());
            }
        }
        return billingAddress;
    }

    public CrmAccountItem getRPCForContact(CrmAccountItem item) {
        if (item == null) {
            item = new CrmAccountItem();
        }
        item.setName(getName());
        item.setNumber(getNumber());
        item.setObjectId(getObjectID());
        return item;
    }

    public CrmAccountItem getRPC(CrmAccountItem item, boolean brief) {
        if (item == null) {
            item = new CrmAccountItem();
        }
        item.setDeleted(isDeleted());
        item.setObjectKey(getObjectKey());
        item.setObjectId(getObjectID());
        item.setName(getName());
        item.setNumber(getNumber());
        if ((getPrefix() == null || getPrefix().isEmpty()) && !ServerUtils.isNullOrEmpty(getNumber()) && getNumberInteger() != null) {
            item.setPrefix(getNumber().replace(EdsNumberingSettings.decimalFormat.format(getNumberInteger()), ""));
        } else {
            item.setPrefix(getPrefix());
        }
        item.setIntNumber(getNumberInteger());
        item.setNote(getNote());
        item.setEmail(getEmail());
        item.setPhone(getPhone());
        item.setFax(getFax());
        item.setWebsite(getWebsite());
        if (getCreditLimit() != null) {
            item.setCreditLimit(getCreditLimit());
        }
        if (getSubsidiary() != null) {
            item.setSubsidiary(new SelectItem(getSubsidiary().getObjectID(), getSubsidiary().getCompanyName(), getSubsidiary().getCurrencyID().toString()));
        }
        if (getBillingAddress() != null) {
            Address billAddress = getBillingAddress().getRPC();
            billAddress.setPrimary(true);
            item.setBillAddresses(new Address[]{billAddress});
        }
        if (getMailingAddress() != null) {
            Address mailAddress = getMailingAddress().getRPC();
            mailAddress.setPrimary(true);
            item.setMailAddresses(new Address[]{mailAddress});
        }
        item.setShowContactAddress(isShowPrimaryContactAddress());
        if (!brief) {
            if (getMailingAddresses() != null && getMailingAddresses().size() > 0) {
                List<Address> addressRPCs = new ArrayList<>();
                for (EdsAddress address : getMailingAddresses()) {
                    if (address != null && (!address.isDeleted())) {
                        addressRPCs.add(address.getRPC());
                    }
                }
                if (addressRPCs.size() > 0) {
                    item.setMailAddresses(addressRPCs.toArray(new Address[]{}));
                }
            }
            if (getBillingAddresses() != null && getBillingAddresses().size() > 0) {
                List<Address> addressRPCs = new ArrayList<>();
                for (EdsAddress address : getBillingAddresses()) {
                    if (address != null && (!address.isDeleted())) {
                        addressRPCs.add(address.getRPC());
                    }
                }
                if (addressRPCs.size() > 0) {
                    item.setBillAddresses(addressRPCs.toArray(new Address[]{}));
                }
            }

            if (getWarehouse() != null) {
                item.setWarehouse(new SelectItem(getWarehouse().getObjectID(), getWarehouse().getName()));
            }
            if (getDepartment() != null) {
                item.setDepartment(new SelectItem(getDepartment().getObjectID(), getDepartment().getName()));
            }
        }
        item.setVatNumber(getVatNumber());
        item.setRegistrationNumber(getRegistrationNumber());
        /*if (getOwner() != null) {
            item.setOwnerID(getOwner().getObjectID());
            item.setOwnerName(getOwner().getName());
        }*/
        ArrayList<SelectItem> owners = new ArrayList<>();
        if (getOwners() != null && getOwners().size() > 0) {
            StringBuilder ownerNames = new StringBuilder();
            for (EdsUser owner : getOwners()) {
                owners.add(new SelectItem(owner.getObjectID(), owner.getName(), null, true));
                ownerNames.append(owner.getName()).append(",");
            }
            item.setSelectedOwners(owners);
            item.setOwnerNames(ownerNames.toString().substring(0, ownerNames.toString().length() - 1));
        }
        if (item.getOwnerItems() != null && item.getOwnerItems().length > 0) {
            for (SelectItem selectItem : item.getOwnerItems()) {
                owners.add(new SelectItem(selectItem.getId(), selectItem.getName(), selectItem.getDescription(), selectItem.isSelected()));
            }
        }
        if (owners != null && owners.size() > 0) {
            item.setOwnerItems(owners.toArray(new SelectItem[0]));
        }
        if (getCreator() != null) {
            item.setCreatorID(getCreator().getObjectID());
        }
        if (getParent() != null) {
            CrmAccountItem parent = new CrmAccountItem();
            parent.setObjectId(getParent().getObjectID());
            parent.setName(getParent().getName());
            item.setParent(parent);
        }
        if (getEntityID() != null) {
            item.setEntityID(getEntityID());
        }
        /*if (getOwner() != null) {
            item.setOwnerName(getOwner().getName());
        }*/
        if (getRating() != null) {
            item.setRatingId(getRating().getObjectID());
            item.setRating(getRating().getLocalizedName());
        }
        if (getNumberOfEmployees() != null) {
            item.setNumberOfEmployee(getNumberOfEmployees().getLocalizedName());
            item.setNumberOfEmployeeID(getNumberOfEmployees().getObjectID());
        }
        if (getAnnualRevenue() != null) {
            item.setAnnualRevenue(getAnnualRevenue().getLocalizedName());
            item.setAnnualRevenueID(getAnnualRevenue().getObjectID());
            item.setAnnualRevenueCode(getAnnualRevenue().getCode());
        }
        if (getOrganizationType() != null) {
            item.setOrganizationType(getOrganizationType().getLocalizedName());
            item.setOrganizationTypeID(getOrganizationType().getObjectID());
            item.setOrganizationTypeCode(getOrganizationType().getCode());
            item.setOtherOrganizationType(getOtherOrganizationType());
        }
        if (getOwnership() != null) {
            item.setOwnershipId(getOwnership().getObjectID());
            item.setOwnership(getOwnership().getLocalizedName());
            item.setOwnershipCode(getOwnership().getCode());
        }
        if (getIndustry() != null) {
            item.setIndustryID(getIndustry().getObjectID());
            item.setIndustry(getIndustry().getLocalizedName());
            item.setIndustryCode(getIndustry().getCode());
            item.setOtherIndustry(getOtherIndustry());
        }
        if (getCurrency() != null) {
            item.setCurrency(getCurrency().getName());
            item.setCurrencyId(getCurrency().getObjectID());
        }
        if (getPaymentMethod() != null) {
            item.setPaymentMethod(getPaymentMethod().getName());
            item.setPaymentMethodId(getPaymentMethod().getObjectID());
        }
        if (getCampaign() != null) {
            item.setCampaign(getCampaign().getAsSelectItem());
            item.setCampaignId(getCampaign().getObjectID());
            item.setCampaignName(getCampaign().getName());
        }
        if (getBankAccount() != null) {
            item.setBankAccountId(getBankAccount().getObjectID());
            item.setBankAccount(getBankAccount().getAccount() != null ? getBankAccount().getAccount().getName() : null);
        }
        if (getAccountTypes() != null && getAccountTypes().size() > 0) {
            List<EdsReference> list = new ArrayList<>();
            list.addAll(getAccountTypes());
            List<Integer> selectedAccountTypes = EdsObject.getObjectIDs(list);
            if (item.getAccountTypes() != null && item.getAccountTypes().length > 0) {
                for (final SelectItem selectItem : item.getAccountTypes()) {
                    if (selectItem != null && selectedAccountTypes.contains(selectItem.getId())) {
                        selectItem.setSelected(true);
                    }
                }
            } else {
                item.setAccountTypes(ServerUtils.getAsSelectItem(list, ServerUtils.REFERENCE));
                SelectItem.setSelected(item.getAccountTypes(), true);
            }
        }
        if (getClientType() != null) {
            item.setClientType(new SelectItem(getClientType().getObjectID(), clientType.getName()));
        }
        if (getVatCategory() != null) {
            item.setVatCategory(new SelectItem(getVatCategory().getObjectID(), vatCategory.getName()));
        }

        item.setBalanceAmount(getBalanceAmount() != null ? getBalanceAmount().doubleValue() : null);
        item.setBalanceDate(getBalanceDate() != null ? new DateNonConvertable(getBalanceDate()) : null);

        item.setSupplierBalanceAmount(getSupplierBalanceAmount() != null ? getSupplierBalanceAmount().doubleValue() : null);
        item.setSupplierBalanceDate(getSupplierBalanceDate() != null ? new DateNonConvertable(getSupplierBalanceDate()) : null);

        item.setCreditLimit(getCreditLimit());
        item.setQuoteCreditLimit(getQuoteCreditLimit());
        item.setClientBalance(getClientBalance().doubleValue());
        item.setSupplierBalance(getSupplierBalance().doubleValue());
        if (getTerms() != null) {
            item.setTermsItem(getTerms().getAsSelectItem());
        }
        if (getVat() != null) {
            item.setVat(getVat().createTaxItem());
        }
        if (isClient() && getReceivable() != null && !getReceivable().isDeleted()) {
            item.setAccountsReceivablePayable(getReceivable().createAccountItem());
        } else if (isSupplier() && getPayable() != null && !getPayable().isDeleted()) {
            item.setAccountsReceivablePayable(getPayable().createAccountItem());
        }
        if (isCreateGlAccount()) {
            item.setCreateGlAccount(isCreateGlAccount());
        }
        item.setInTarget(isInTarget());
        item.setTargetId(getTargetId());
        //client
        item.setQuickbookCustomerID(getQuickbookCustomerID());
        item.setQuickbookEditSequence(getQuickbookEditSequence());
        item.setSaasuGUID(getSaasuGUID());
        item.setSaasuLastUpdatedDate(getSasuuLastUpdatedTime());
        item.setSaasuLastUpdatedUid(getSaasuLastUpdatedUid());
        //supplier
        item.setBankName(getBankName());
        item.setAccountName(getAccountName());
        item.setAccountNo(getAccountNo());
        item.setSwiftCode(getSwiftCode());
        item.setSortCode(getSortCode());
        item.setIbanCode(getIbanCode());
        item.setBranch(getBranch());
        item.setBankAddress(getBankAddress());
        item.setReverseChargeApplicable(isReverseChargeApplicable());
        item.setHasContacts(!getCrmContacts().isEmpty());
        item.setCrNumber(getCrNumber());
        if (getSalesType() != null) {
            item.setSalesTypeId(getSalesType().getId());
            item.setSalesType(getSalesType().name());
        }
        if (getPasportNumber() != null) {
            item.setPassportNumber(getPasportNumber());
        }
        return item;
    }

    public List<SolrInputDocument> getSolrDocument(Integer companyID) {
        List<SolrInputDocument> docs = new ArrayList<>();
        Iterator<EdsReference> iterator = getAccountTypes().iterator();
        SolrInputDocument doc = getSolrDocumentWithoutType(companyID);
        while (iterator.hasNext()) {
            EdsReference type = iterator.next();
            if (type != null) {
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_ID, type.getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_NAME, type.getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_CODE, type.getCode());
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_ID_CODE, type.getObjectID() + SolrCrmAccountRepresenter.SPLIT + type.getCode());
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_ID_CODE_NAME, type.getObjectID() + SolrCrmAccountRepresenter.SPLIT + type.getCode() + SolrCrmAccountRepresenter.SPLIT + type.getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_TYPE_IDS, type.getObjectID());
            }
        }
        docs.add(doc);
        return docs;
    }

    public CrmAccountSolrItem getSolrRPC() {
        CrmAccountSolrItem item = new CrmAccountSolrItem();

        item.setObjectId(getObjectID());
        SelectItem crmAccount = new SelectItem(getObjectID(), getName());
        crmAccount.setCode(getNumber());
        item.setCrmAccount(crmAccount);
        item.setBlocked(isBlocked());

        if (getOwners() != null) {
            item.setOwner(getOwners().stream().map(owner -> new SelectItem(owner.getObjectID(), owner.getFullName())).toList());
        }
        if (getParent() != null) {
            item.setCrmAccountParent(getParent().getAsSelectItem());
        }

        if (getOwnership() != null) {
            item.setOwnership(getOwnership().getAsSelectItem());
        }
        if (getOrganizationType() != null) {
            item.setOrganizationType(getOrganizationType().getAsSelectItem());
        }
        if (getVat() != null) {
            item.setTax(new SelectItem(getVat().getObjectID(), getVat().getTaxNameAndRateAsString()));
        }
        if (getNumberOfEmployees() != null) {
            item.setNumberOfEmployees(getNumberOfEmployees().getAsSelectItem());
        }
        if (getAnnualRevenue() != null) {
            item.setAnnualRevenue(getAnnualRevenue().getAsSelectItem());
        }
        if (getRating() != null) {
            item.setRating(getRating().getAsSelectItem());
        }
        if (getIndustry() != null) {
            item.setIndustry(getIndustry().getAsSelectItem());
        }

        item.setEmail(getEmail());
        item.setPhone(getPhone());
        item.setFax(getFax());
        item.setWebsite(getWebsite());
        if (getPrimaryContact() != null) {
            item.setContact(getPrimaryContact().getAsSelectItem());
        }

        if (getBillingAddress() != null) {
            item.setBillingAddress(getBillingAddress().getRPC());
        }
        if (getMailingAddress() != null) {
            item.setMailingAddress(getMailingAddress().getRPC());
        }
        if (getCurrency() != null) {
            item.setCurrency(getCurrency().getAsSelectItem());
        }
        if (getTerms() != null) {
            item.setTerm(getTerms().getAsSelectItem());
        }
        item.setVatNumber(getVatNumber());
        item.setTrnNumber(getTrn());
        item.setRegistrationNumber(getRegistrationNumber());

        if (getPaymentMethod() != null) {
            SelectItem paymentMethod = getPaymentMethod().getAsSelectItem();
            paymentMethod.setCode(getPaymentMethod().getCode());
            item.setPaymentMethod(paymentMethod);
        }
        if (getCampaign() != null) {
            item.setCampaign(getCampaign().getAsSelectItem());
        }
        item.setCreationDate(getCreationTime());
        item.setLastUpdateDate(getLastUpdateTime());
        item.setClientBalance(getClientBalance().doubleValue());
        item.setSupplierBalance(getSupplierBalance().doubleValue());
        item.setCreditLimit(getCreditLimit() != null ? getCreditLimit().doubleValue() : 0d);

        if (getBankAccount() != null && getBankAccount().getAccount() != null) {
            item.setBankName(getBankAccount().getAccount().getName());
        }
        item.setInTarget(isInTarget());
        if (getSalesType() != null) {
            item.setSalesType(new SelectItem(getSalesType().getId(), getSalesType().name()));
        }

        Iterator<EdsReference> iterator = getAccountTypes().iterator();
        while (iterator.hasNext()) {
            EdsReference type = iterator.next();
            if (type != null) {
                SelectItem accountType = new SelectItem(type.getObjectID(), type.getName());
                accountType.setCode(type.getCode());
                item.getType().add(accountType);
            }
        }

        return item;
    }

    private SolrInputDocument getSolrDocumentWithoutType(Integer companyID) {
        String compositeID = companyID.toString() + "_" + getObjectID().toString();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrCrmAccountRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrCrmAccountRepresenter.FIELD_COMPOSITE_ID, compositeID);
        doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID, getObjectID());
        doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME, getName());
        doc.addField(SolrCrmAccountRepresenter.FIELD_COMPOSITE_CRM_ACCOUNT_NAME, getName());
        doc.addField(SolrCrmAccountRepresenter.FIELD_ACCOUNT_NAME_COMPOSITE, getName());//api search
        doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID_NAME, SolrUtils.getIdName(this));
        doc.addField(SolrCrmAccountRepresenter.FIELD_BLOCKED, isBlocked());
        /*if (getOwner() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_ID, getOwner().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_NAME, getOwner().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME, SolrUtils.getIdName(getOwner().getObjectID(), getOwner().getFullName()));
        }*/
        if (getOwners() != null) {
            getOwners().forEach(owner -> {
                doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_ID, owner.getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_NAME, owner.getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_OWNER_ID_NAME, SolrUtils.getIdName(owner.getObjectID(), owner.getFullName()));
            });
        }
        if (getParent() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID, getParent().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_NAME, getParent().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID_NAME, SolrUtils.getIdName(getParent()));
        }

        doc.addField(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NUMBER, getNumber());
        if (getOwnership() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_ID, getOwnership().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_NAME, getOwnership().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_CODE, getOwnership().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_ID_CODE, SolrUtils.getIdName(getOwnership().getObjectID(), getOwnership().getCode()));
            doc.addField(SolrCrmAccountRepresenter.FIELD_OWNERSHIP_ID_CODE_NAME, getOwnership().getObjectID() + SolrCrmAccountRepresenter.SPLIT + getOwnership().getCode() + SolrCrmAccountRepresenter.SPLIT + getOwnership().getName());
        }
        if (getOrganizationType() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_ID, getOrganizationType().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_NAME, getOrganizationType().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_CODE, getOrganizationType().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_ID_CODE, SolrUtils.getIdName(getOrganizationType().getObjectID(), getOrganizationType().getCode()));
            doc.addField(SolrCrmAccountRepresenter.FIELD_ORGANIZATION_TYPE_ID_CODE_NAME, getOrganizationType().getObjectID() + SolrCrmAccountRepresenter.SPLIT + getOrganizationType().getCode() + SolrCrmAccountRepresenter.SPLIT + getOrganizationType().getName());
        }
        if (getNumberOfEmployees() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_NUMBER_OF_EMPLOYEES_ID, getNumberOfEmployees().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_NUMBER_OF_EMPLOYEES_NAME, getNumberOfEmployees().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_NUMBER_OF_EMPLOYEES_CODE, getNumberOfEmployees().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_NUMBER_OF_EMPLOYEES_ID_CODE, SolrUtils.getIdName(getNumberOfEmployees().getObjectID(), getNumberOfEmployees().getCode()));
        }
        if (getAnnualRevenue() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_ID, getAnnualRevenue().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_NAME, getAnnualRevenue().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_CODE, getAnnualRevenue().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_ID_CODE, SolrUtils.getIdName(getAnnualRevenue().getObjectID(), getAnnualRevenue().getCode()));
            doc.addField(SolrCrmAccountRepresenter.FIELD_ANNUAL_REVENUE_ID_CODE_NAME, getAnnualRevenue().getObjectID() + SolrCrmAccountRepresenter.SPLIT + getAnnualRevenue().getCode() + SolrCrmAccountRepresenter.SPLIT + getAnnualRevenue().getName());
        }
        if (getRating() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_RATING_ID, getRating().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_RATING_NAME, getRating().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_RATING_CODE, getRating().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_RATING_ID_CODE, SolrUtils.getIdName(getRating().getObjectID(), getRating().getCode()));
        }
        if (getIndustry() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID, getIndustry().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_NAME, getIndustry().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_CODE, getIndustry().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID_CODE, SolrUtils.getIdName(getIndustry().getObjectID(), getIndustry().getCode()));
            doc.addField(SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID_CODE_NAME, getIndustry().getObjectID() + SolrCrmAccountRepresenter.SPLIT + getIndustry().getCode() + SolrCrmAccountRepresenter.SPLIT + getIndustry().getName());
        }

        doc.addField(SolrCrmAccountRepresenter.FIELD_EMAIL, getEmail());
        doc.addField(SolrCrmAccountRepresenter.FIELD_PHONE, getPhone());
        doc.addField(SolrCrmAccountRepresenter.FIELD_FAX, getFax());
        doc.addField(SolrCrmAccountRepresenter.FIELD_WEBSITE, getWebsite());
        if (getBillingAddress() != null) {
            EdsAddress billingAddress = getBillingAddress();
            doc.addField(SolrCrmAccountRepresenter.FIELD_ADRESS1_ID, billingAddress.getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_STREET, billingAddress.getAddress());
            doc.addField(SolrCrmAccountRepresenter.FIELD_STREETB, billingAddress.getAddressb());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CITY, billingAddress.getCity());
            doc.addField(SolrCrmAccountRepresenter.FIELD_POST_CODE, billingAddress.getZipCode());
            if (billingAddress.getCountry() != null) {
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID, billingAddress.getCountry().getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME, billingAddress.getCountry().getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_CODE, billingAddress.getCountry().getCode());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE, SolrUtils.getIdName(billingAddress.getCountry().getObjectID(), billingAddress.getCountry().getCode()));
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE_NAME, billingAddress.getCountry().getObjectID() + SolrCrmAccountRepresenter.SPLIT +
                        billingAddress.getCountry().getCode() + SolrCrmAccountRepresenter.SPLIT + billingAddress.getCountry().getName());
            }
            if (billingAddress.getState() != null) {
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_ID, billingAddress.getState().getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_NAME, billingAddress.getState().getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_ID_NAME, SolrUtils.getIdName(billingAddress.getState()));
            }
        }
        if (getMailingAddress() != null) {
            EdsAddress mailingAddress = getMailingAddress();
            doc.addField(SolrCrmAccountRepresenter.FIELD_ADRESS2_ID, mailingAddress.getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_STREET2, mailingAddress.getAddress());
            doc.addField(SolrCrmAccountRepresenter.FIELD_STREET2B, mailingAddress.getAddressb());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CITY2, mailingAddress.getCity());
            doc.addField(SolrCrmAccountRepresenter.FIELD_POST_CODE2, mailingAddress.getZipCode());
            if (mailingAddress.getCountry() != null) {
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID2, mailingAddress.getCountry().getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME2, mailingAddress.getCountry().getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_CODE2, mailingAddress.getCountry().getCode());
                doc.addField(SolrCrmAccountRepresenter.FIELD_COUNTRY_ID_CODE2, SolrUtils.getIdName(mailingAddress.getCountry().getObjectID(), mailingAddress.getCountry().getCode()));
            }
            if (mailingAddress.getState() != null) {
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_ID2, mailingAddress.getState().getObjectID());
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_NAME2, mailingAddress.getState().getName());
                doc.addField(SolrCrmAccountRepresenter.FIELD_STATE_ID_NAME2, SolrUtils.getIdName(mailingAddress.getState()));
            }
        }
        if (getCurrency() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_ID_NAME, SolrUtils.getIdName(getCurrency()));
        }
        if (getTerms() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_TERM_ID, getTerms().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_TERM_NAME, getTerms().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_TERM_ID_NAME, SolrUtils.getIdName(getTerms()));
        }
        doc.addField(SolrCrmAccountRepresenter.FIELD_VAT_NUMBER, getVatNumber());
        doc.addField(SolrCrmAccountRepresenter.FIELD_TRN_NUMBER, getTrn());
        doc.addField(SolrCrmAccountRepresenter.FIELD_REGISTRATION_NUMBER, getRegistrationNumber());
        if (getPaymentMethod() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_ID, getPaymentMethod().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_NAME, getPaymentMethod().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_CODE, getPaymentMethod().getCode());
            doc.addField(SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_ID_CODE, SolrUtils.getIdName(getPaymentMethod().getObjectID(), getPaymentMethod().getCode()));
        }
        doc.addField(SolrCrmAccountRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrCrmAccountRepresenter.FIELD_LAST_UPDATED_DATE, getLastUpdateTime());
        doc.addField(SolrCrmAccountRepresenter.FIELD_CLIENT_BALANCE, getClientBalance().doubleValue());
        doc.addField(SolrCrmAccountRepresenter.FIELD_SUPPLIER_BALANCE, getSupplierBalance().doubleValue());
        doc.addField(SolrCrmAccountRepresenter.FIELD_CREDIT_LIMIT, getCreditLimit() != null ? getCreditLimit().doubleValue() : 0d);
        if (getBankAccount() != null && getBankAccount().getAccount() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_BANK_NAME, getBankAccount().getAccount().getName());
        }
        if (getVat() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_TAX_ID, getVat().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_TAX_NAME, getVat().getTaxNameAndRateAsString());
            doc.addField(SolrCrmAccountRepresenter.FIELD_TAX_ID_NAME, SolrUtils.getIdName(getVat().getObjectID(), getVat().getTaxNameAndRateAsString()));
        }
        if (getPrimaryContact() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_CONTACT_ID, getPrimaryContact().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CONTACT_NAME, getPrimaryContact().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CONTACT_EMAIL, getPrimaryContact().getPrimaryEmail());
        }
        doc.addField(SolrCrmAccountRepresenter.FIELD_IN_TARGET, isInTarget());
        doc.addField(SolrCrmAccountRepresenter.FIELD_BALANCE_DATE, getBalanceDate());
        doc.addField(SolrCrmAccountRepresenter.FIELD_SUPPLIER_BALANCE_DATE, getSupplierBalanceDate());
        doc.addField(SolrCrmAccountRepresenter.FIELD_SAASU_GUID, getSaasuGUID());
        doc.addField(SolrCrmAccountRepresenter.FIELD_SAASU_UPDATED_DATE, getSasuuLastUpdatedTime());
        doc.addField(SolrCrmAccountRepresenter.FIELD_SAASU_UPDATED_UID, getSaasuLastUpdatedUid());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    private boolean isTypeExists(String type_) {
        boolean result = false;
        if (getAccountTypes() != null && getAccountTypes().size() > 0) {
            for (EdsReference type : getAccountTypes()) {
                if (type != null && type.getCode().equalsIgnoreCase(type_)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public Address[] getAddressData(List<EdsAddress> addressList, EdsAddress primaryAddress) {
        int i = 0;
        List<Address> addrArray = new ArrayList<>();
        Integer primaryID = null;
        if (primaryAddress != null) {
            Address rpc = primaryAddress.getRPC();
            rpc.setPrimary(true);
            addrArray.add(rpc);
            primaryID = primaryAddress.getObjectID();
        }
        for (EdsAddress addr : addressList) {
            if (primaryID == null || !primaryID.equals(addr.getObjectID())) {
                Address data = addr.getRPC();
                addrArray.add(data);
            }
        }
        return addrArray.toArray(new Address[]{});
    }

    public boolean isClient() {
        return isTypeExists(EdsCrmAccount.CUSTOMER);
    }

    public boolean isSupplier() {
        return isTypeExists(EdsCrmAccount.SUPPLIER);
    }

    public boolean isVendor() {
        return isTypeExists(EdsCrmAccount.VENDOR);
    }

    public static String getByTypeForHQLQueries(String value, String type) {
        return " left join " + value + ".accountTypes type where type.code = '" + type + "'";
    }

    public static String getByTypeForNativeQueries(String value, String type) {
        return " inner join \"" + SecurityContext.getCompanyID() + "\".crmAccount_types typeIDs on typeIDs.crmaccount_id = " + value + ".id" +
                " inner join \"" + SecurityContext.getCompanyID() + "\".reference types on types.id = typeIDs.type_id" +
                " where " +
                " types.code = '" + type + "'";
    }

    public EdsCrmContact getPrimaryContact() {
        if (getCrmContacts().size() > 0) {
            for (EdsCrmContact cc : getCrmContacts()) {
                if (cc.getPrimaryContact() != null && cc.getPrimaryContact()) {
                    return cc;
                }
            }
        }
        return null;
    }

    public void merge(EdsCrmAccount account) {
        if (account.getName() != null) {
            setName(account.getName());
        }
    }

    public BigDecimal getClientBalance() {
        return clientBalance != null ? clientBalance : BigDecimal.ZERO;
    }

    public BigDecimal getSupplierBalance() {
        return supplierBalance != null ? supplierBalance : BigDecimal.ZERO;
    }

    public Boolean getBalanceCalculated() {
        return balanceCalculated != null ? balanceCalculated : false;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public void setLastUpdateTime(Date lastUpdatedDate) {
        inTarget = false;
        this.lastUpdateTime = lastUpdatedDate;
    }

    @Override
    public void setUpdater(EdsUser user) {
    }

    public List<EdsCrmContactItemParams> getItemParams() {
        return itemParams;
    }

    public void setItemParams(List<EdsCrmContactItemParams> itemParams) {
        this.itemParams = itemParams;
    }

    public List<EdsCrmContactItemParams> getItemParams(int param) {
        if (itemParams == null || itemParams.size() == 0) {
            return itemParams;
        }
        List<EdsCrmContactItemParams> paramList = new ArrayList<>();
        for (EdsCrmContactItemParams itemParam : itemParams) {
            if (itemParam.getParam().equals(param)) {
                paramList.add(itemParam);
            }
        }
        return paramList;
    }

    public HashMap<Integer, ArrayList<String>> getParams(int type) {
        HashMap<Integer, ArrayList<String>> params = new LinkedHashMap<>();
        for (EdsCrmContactItemParams itemParam : getItemParams(type)) {
            if (itemParam.getRelation() != null && itemParam.getRelation() > 0) {
                if (params.containsKey(itemParam.getRelation())) {
                    params.get(itemParam.getRelation()).add(itemParam.getValue());
                } else {
                    params.put(itemParam.getRelation(), new ArrayList<>());
                    params.get(itemParam.getRelation()).add(itemParam.getValue());
                }
            }
        }
        return params;
    }

    public SelectItem[] getAccountTypeList() {
        SelectItem[] result = new SelectItem[this.getAccountTypes().size()];
        if (this.getAccountTypes() != null && this.getAccountTypes().size() > 0) {
            int i = 0;
            for (EdsReference reference : this.getAccountTypes()) {
                if (reference != null) {
                    result[i] = new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode(), true);
                    i++;
                }
            }
        }
        return result;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        /*if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_OWNER)) {
            return getOwner();
        }*/
        if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_OWNER)) {
            return getOwners();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_PARENT)) {
            return getParent();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.PRIMARY_CONTACT)) {
            return getPrimaryContact();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
            return getAccountTypes();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_OWNERSHIP)) {
            return getOwnership();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE)) {
            return getOrganizationType();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE)) {
            return getNumberOfEmployees();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE)) {
            return getAnnualRevenue();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_RATING)) {
            return getRating();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_INDUSTRY)) {
            return getIndustry();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_EMAIL)) {
            return getEmail();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_PHONE)) {
            return getPhone();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_WEBSITE)) {
            return getWebsite();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_FAX)) {
            return getFax();
        } else if (fieldID.equals(CustomFormConstants.CURRENCY)) {
            return getCurrency();
        } else if (fieldID.equals(CustomFormConstants.VAT_NUMBER)) {
            return getVatNumber();
        } else if (fieldID.equals(CustomFormConstants.PAYMENT_METHOD)) {
            return getPaymentMethod();
        } else if (fieldID.equals(CustomFormConstants.REGISTRATION_NUMBER)) {
            return getRegistrationNumber();
        } else if (fieldID.equals(CustomFormConstants.CLIENT_INVOICE_TERM)) {
            return getTerms();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        } else if (fieldID.equals(CustomFormConstants.CLIENT_AMOUNT)) {
            return getBalanceAmount();
        } else if (fieldID.equals(CustomFormConstants.CLIENT_AS_OF_DATE)) {
            return getBalanceDate();
        }
        return super.getRealValue(fieldID);
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_OWNER)) {
                //setOwner((EdsUser) value);
                if (value instanceof EdsUser) {
                    addOwner((EdsUser) value);
                }
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NAME)) {
                setName((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_PARENT)) {
                setParent((EdsCrmAccount) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NUMBER)) {
                setNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
                if (value != null && value instanceof EdsReference) {
                    addAccountType((EdsReference) value);
                }
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_INDUSTRY)) {
                setIndustry((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_OWNERSHIP)) {
                setOwnership((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_ORGANIZATION_TYPE)) {
                setOrganizationType((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NUMBER_OF_EMPLOYEE)) {
                setNumberOfEmployees((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_ANNUAL_REVENUE)) {
                setAnnualRevenue((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_RATING)) {
                setRating((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_EMAIL)) {
                setEmail((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_PHONE)) {
                setPhone((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_WEBSITE)) {
                setWebsite((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_FAX)) {
                setFax((String) value);
            } else if (fieldID.equals(CustomFormConstants.CURRENCY)) {
                setCurrency((EdsCurrency) value);
            } else if (fieldID.equals(CustomFormConstants.VAT_NUMBER)) {
                setVatNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.PAYMENT_METHOD)) {
                setPaymentMethod((EdsPaymentMethod) value);
            } else if (fieldID.equals(CustomFormConstants.REGISTRATION_NUMBER)) {
                setRegistrationNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.CLIENT_INVOICE_TERM)) {
                setTerms((EdsInvoiceTerms) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected boolean hasCustomConditionChecking(String fieldID) {
        if (fieldID != null) {
            if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
                return true;
            } else if (fieldID.equals(CustomFormConstants.PRIMARY_CONTACT)) {
                return true;
            }
        }
        return super.hasCustomConditionChecking(fieldID);
    }

    @Override
    protected boolean checkConditionCustom(String fieldID, String operands, String value) {
        if (fieldID != null) {
            if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
                if (getAccountTypes().size() > 0) {
                    for (EdsReference item : getAccountTypes()) {
                        String realValue = item.getName();
                        if (value != null && value.contains("@")) {
                            String[] values = value.split("@");
                            if (values != null && values.length > 0) {
                                String splitValue = "";
                                if (values.length >= 2) {
                                    splitValue = values[1];
                                }
                                if (checkStrings(realValue, operands, splitValue)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (fieldID.equals(CustomFormConstants.PRIMARY_CONTACT)) {
                if (getPrimaryContact() != null) {
                    EdsCrmContact edsCrmContact = getPrimaryContact();
                    String realValue = edsCrmContact.getName();
                    if (value != null && value.contains("@")) {
                        String[] values = value.split("@");
                        if (values != null && values.length >= 2) {
                            if (checkStrings(realValue, operands, values[1])) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return super.checkConditionCustom(fieldID, operands, value);
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsReference getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(EdsReference taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public EdsCountry getPlaceOfSupplyCountry() {
        return placeOfSupplyCountry;
    }

    public void setPlaceOfSupplyCountry(EdsCountry placeOfSupplyCountry) {
        this.placeOfSupplyCountry = placeOfSupplyCountry;
    }

    public EdsRegion getPlaceOfSupplyState() {
        return placeOfSupplyState;
    }

    public void setPlaceOfSupplyState(EdsRegion placeOfSupplyState) {
        this.placeOfSupplyState = placeOfSupplyState;
    }

    public Integer getTaxtreatmentId() {
        return taxtreatmentId;
    }

    public void setTaxtreatmentId(Integer taxtreatmentId) {
        this.taxtreatmentId = taxtreatmentId;
    }

    public Integer getPlaceofsupplyCountryId() {
        return placeofsupplyCountryId;
    }

    public void setPlaceofsupplyCountryId(Integer placeofsupplyCountryId) {
        this.placeofsupplyCountryId = placeofsupplyCountryId;
    }

    public Integer getPlaceofsupplyStateId() {
        return placeofsupplyStateId;
    }

    public void setPlaceofsupplyStateId(Integer placeofsupplyStateId) {
        this.placeofsupplyStateId = placeofsupplyStateId;
    }

    public SelectItem getPlaceOfSupply() {

        if (getPlaceofsupplyCountryId() != null) {
            SelectItem country = getPlaceOfSupplyCountry().getAsSelectItem();
            country.setCode(getPlaceOfSupplyCountry().getCode());
            country.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.COUNTRY);
            return country;
        } else if (getPlaceofsupplyStateId() != null) {
            SelectItem state = getPlaceOfSupplyState().getAsSelectItem();
            state.setCode(getPlaceOfSupplyState().getCode());
            state.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.REGION);
            return state;
        }
        return null;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String getObjectKey() {
        return objectKey;
    }

    @Override
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public List<EdsUser> getOwners() {
        return owners;
    }

    public void setOwners(List<EdsUser> owners) {
        this.owners = owners;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setParent(EdsCrmAccount parent) {
        this.parent = parent;
    }

    public List<EdsCrmAccount> getChildList() {
        return childList;
    }

    public void setChildList(List<EdsCrmAccount> childList) {
        this.childList = childList;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getNumberInteger() {
        return numberInteger;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAccountTypes(Set<EdsReference> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setCrmContacts(Set<EdsCrmContact> crmContacts) {
        this.crmContacts = crmContacts;
    }

    public EdsReference getOwnership() {
        return ownership;
    }

    public EdsReference getRating() {
        return rating;
    }

    public EdsReference getAnnualRevenue() {
        return annualRevenue;
    }

    public EdsReference getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public EdsReference getOrganizationType() {
        return organizationType;
    }

    public EdsReference getIndustry() {
        return industry;
    }

    public String getOtherOrganizationType() {
        return otherOrganizationType;
    }

    public void setOtherOrganizationType(String otherOrganizationType) {
        this.otherOrganizationType = otherOrganizationType;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Date getSasuuLastUpdatedTime() {
        return sasuuLastUpdatedTime;
    }

    public void setSasuuLastUpdatedTime(Date sasuuLastUpdatedTime) {
        this.sasuuLastUpdatedTime = sasuuLastUpdatedTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public Integer getSignupCompanyId() {
        return signupCompanyId;
    }

    public void setSignupCompanyId(Integer signupCompanyId) {
        this.signupCompanyId = signupCompanyId;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public EdsReference getClientType() {
        return clientType;
    }

    public void setClientType(EdsReference clientType) {
        this.clientType = clientType;
    }

    public EdsReference getVatCategory() {
        return vatCategory;
    }

    public void setVatCategory(EdsReference vatCategory) {
        this.vatCategory = vatCategory;
    }

    public EdsAddress getBillingAddress() {
        return billingAddress;
    }

    public EdsAddress getMailingAddress() {
        return mailingAddress;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public EdsPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public EdsCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(EdsCampaign campaign) {
        this.campaign = campaign;
    }

    public EdsCrmCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsAttachment getLogo() {
        return logo;
    }

    public void setLogo(EdsAttachment logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public BigDecimal getSupplierBalanceAmount() {
        return supplierBalanceAmount;
    }

    public void setSupplierBalanceAmount(BigDecimal supplierBalanceAmount) {
        this.supplierBalanceAmount = supplierBalanceAmount;
    }

    public Date getSupplierBalanceDate() {
        return supplierBalanceDate;
    }

    public void setSupplierBalanceDate(Date supplierBalanceDate) {
        this.supplierBalanceDate = supplierBalanceDate;
    }

    public void setAppliedCredit(BigDecimal appliedCredit) {
        this.appliedCredit = appliedCredit;
    }

    public EdsInvoiceTerms getTerms() {
        return terms;
    }

    public EdsAccount getReceivable() {
        return receivable;
    }

    public void setReceivable(EdsAccount receivable) {
        this.receivable = receivable;
    }

    public EdsAccount getPayable() {
        return payable;
    }

    public void setPayable(EdsAccount payable) {
        this.payable = payable;
    }

    public EdsSubsidiariesCompany getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(EdsSubsidiariesCompany subsidiary) {
        this.subsidiary = subsidiary;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public void setClientBalance(BigDecimal clientBalance) {
        this.clientBalance = clientBalance;
    }

    public void setSupplierBalance(BigDecimal supplierBalance) {
        this.supplierBalance = supplierBalance;
    }

    public BigDecimal getOtherBalance() {
        return otherBalance;
    }

    public void setOtherBalance(BigDecimal otherBalance) {
        this.otherBalance = otherBalance;
    }

    public void setBalanceCalculated(Boolean balanceCalculated) {
        this.balanceCalculated = balanceCalculated;
    }

    public List<EdsAddress> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<EdsAddress> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<EdsAddress> getMailingAddresses() {
        return mailingAddresses;
    }

    public void setMailingAddresses(List<EdsAddress> mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }

    public List<EdsAddress> getBillAddressesTransient() {
        return billAddressesTransient;
    }

    public void setBillAddressesTransient(List<EdsAddress> billAddressesTransient) {
        this.billAddressesTransient = billAddressesTransient;
    }

    public List<EdsAddress> getMailAddressesTransient() {
        return mailAddressesTransient;
    }

    public void setMailAddressesTransient(List<EdsAddress> mailAddressesTransient) {
        this.mailAddressesTransient = mailAddressesTransient;
    }

    public Set<EdsProject> getProjects() {
        return projects;
    }

    public void setProjects(Set<EdsProject> projects) {
        this.projects = projects;
    }

    public String getQuickbookCustomerID() {
        return quickbookCustomerID;
    }

    public void setQuickbookCustomerID(String quickbookCustomerID) {
        this.quickbookCustomerID = quickbookCustomerID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
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

    public List<EdsPriceLevel> getPriceLevels() {
        return priceLevels;
    }

    public void setPriceLevels(List<EdsPriceLevel> priceLevels) {
        this.priceLevels = priceLevels;
    }

    public List<EdsDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<EdsDiscount> discounts) {
        this.discounts = discounts;
    }

    public EdsShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(EdsShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
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

    public EdsClientContact[] getClientContacts() {
        return clientContacts;
    }

    public void setClientContacts(EdsClientContact[] clientContacts) {
        this.clientContacts = clientContacts;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<EdsExpense> getExpense() {
        return expense;
    }

    public void setExpense(List<EdsExpense> expense) {
        this.expense = expense;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public CompanyCustomFieldItem[] getTransientCustomFields() {
        return transientCustomFields;
    }

    public void setTransientCustomFields(CompanyCustomFieldItem[] transientCustomFields) {
        this.transientCustomFields = transientCustomFields;
    }

    public boolean isShowPrimaryContactAddress() {
        return showPrimaryContactAddress;
    }

    public void setShowPrimaryContactAddress(boolean showPrimaryContactAddress) {
        this.showPrimaryContactAddress = showPrimaryContactAddress;
    }

    public boolean isCreateGlAccount() {
        return createGlAccount;
    }

    public void setCreateGlAccount(boolean createGlAccount) {
        this.createGlAccount = createGlAccount;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public SalesType getSalesType() {
        return salesType;
    }

    public void setSalesType(SalesType salesType) {
        this.salesType = salesType;
    }

    public String getCrNumber() {
        return crNumber;
    }

    public void setCrNumber(String crNumber) {
        this.crNumber = crNumber;
    }

    public String getPasportNumber() {
        return pasportNumber;
    }

    public void setPasportNumber(String pasportNumber) {
        this.pasportNumber = pasportNumber;
    }

    public String getFaiUuid() {
        return faiUuid;
    }

    public void setFaiUuid(String faiUuid) {
        this.faiUuid = faiUuid;
    }
}
