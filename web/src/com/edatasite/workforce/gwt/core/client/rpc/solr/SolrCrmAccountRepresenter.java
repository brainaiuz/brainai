package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 25.04.2011
 * Time: 19:16:40
 * To change this template use File | Settings | File Templates.
 */
public class SolrCrmAccountRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_API_COMPOSITE = "apiComposite";

    public static final String FIELD_BLOCKED = "blocked";

    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_OWNER_NAME = "ownerName";
    public static final String FIELD_OWNER_ID_NAME = "ownerIdName";

    public static final String FIELD_CRM_ACCOUNT_PARENT_ID = "crmAccountParentId";
    public static final String FIELD_CRM_ACCOUNT_PARENT_NAME = "crmAccountParentName";
    public static final String FIELD_CRM_ACCOUNT_PARENT_ID_NAME = "crmAccountParentIdName";

    public static final String FIELD_CRM_ACCOUNT_ID = "crmAccountId";
    public static final String FIELD_CRM_ACCOUNT_NAME = "crmAccountName";
    public static final String FIELD_CRM_ACCOUNT_NAME_TEXT_FIELD = "n";
    public static final String FIELD_CRM_ACCOUNT_ID_NAME = "crmAccountIdName";

    public static final String FIELD_CRM_ACCOUNT_NUMBER = "crmAccountNumber";

    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_TYPE_ID_CODE = "typeIdCode";
    public static final String FIELD_TYPE_ID_CODE_NAME = "typeIdCodeName";
    public static final String FIELD_TYPE_IDS = "typeIds";

    public static final String FIELD_INDUSTRY_ID = "industryId";
    public static final String FIELD_INDUSTRY_NAME = "industryName";
    public static final String FIELD_INDUSTRY_CODE = "industryCode";
    public static final String FIELD_INDUSTRY_ID_CODE = "industryIdCode";
    public static final String FIELD_INDUSTRY_ID_CODE_NAME = "industryIdCodeName";

    public static final String FIELD_OWNERSHIP_ID = "ownershipId";
    public static final String FIELD_OWNERSHIP_NAME = "ownershipName";
    public static final String FIELD_OWNERSHIP_CODE = "ownershipCode";
    public static final String FIELD_OWNERSHIP_ID_CODE = "ownershipIdCode";
    public static final String FIELD_OWNERSHIP_ID_CODE_NAME = "ownershipIdCodeName";

    public static final String FIELD_ORGANIZATION_TYPE_ID = "organizationTypeId";
    public static final String FIELD_ORGANIZATION_TYPE_NAME = "organizationTypeName";
    public static final String FIELD_ORGANIZATION_TYPE_CODE = "organizationTypeCode";
    public static final String FIELD_ORGANIZATION_TYPE_ID_CODE = "organizationTypeIdCode";
    public static final String FIELD_ORGANIZATION_TYPE_ID_CODE_NAME = "organizationTypeIdCodeName";

    public static final String FIELD_NUMBER_OF_EMPLOYEES_ID = "numberOfEmployeesId";
    public static final String FIELD_NUMBER_OF_EMPLOYEES_NAME = "numberOfEmployeesName";
    public static final String FIELD_NUMBER_OF_EMPLOYEES_CODE = "numberOfEmployeesCode";
    public static final String FIELD_NUMBER_OF_EMPLOYEES_ID_CODE = "numberOfEmployeesIdCode";

    public static final String FIELD_ANNUAL_REVENUE_ID = "annualRevenueId";
    public static final String FIELD_ANNUAL_REVENUE_NAME = "annualRevenueName";
    public static final String FIELD_ANNUAL_REVENUE_CODE = "annualRevenueCode";
    public static final String FIELD_ANNUAL_REVENUE_ID_CODE = "annualRevenueIdCode";
    public static final String FIELD_ANNUAL_REVENUE_ID_CODE_NAME = "annualRevenueIdCodeName";

    public static final String FIELD_RATING_ID = "ratingId";
    public static final String FIELD_RATING_NAME = "ratingName";
    public static final String FIELD_RATING_CODE = "ratingCode";
    public static final String FIELD_RATING_ID_CODE = "ratingIdCode";


    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_FAX = "fax";
    public static final String FIELD_WEBSITE = "website";

    public static final String FIELD_ADRESS1_ID = "adress1Id";
    public static final String FIELD_STREET = "street";
    public static final String FIELD_STREETB = "streetb";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTRY_ID = "countryId";
    public static final String FIELD_COUNTRY_NAME = "countryName";
    public static final String FIELD_COUNTRY_CODE = "countryCode";
    public static final String FIELD_COUNTRY_ID_CODE = "countryIdCode";
    public static final String FIELD_COUNTRY_ID_CODE_NAME = "countryIdCodeName";
    public static final String FIELD_STATE_ID = "stateId";
    public static final String FIELD_STATE_NAME = "stateName";
    public static final String FIELD_STATE_ID_NAME = "stateIdName";
    public static final String FIELD_POST_CODE = "postCode";

    public static final String FIELD_ADRESS2_ID = "adress2Id";
    public static final String FIELD_STREET2 = "street2";
    public static final String FIELD_STREET2B = "street2b";
    public static final String FIELD_CITY2 = "city2";
    public static final String FIELD_COUNTRY_ID2 = "countryId2";
    public static final String FIELD_COUNTRY_NAME2 = "countryName2";
    public static final String FIELD_COUNTRY_CODE2 = "countryCode2";
    public static final String FIELD_COUNTRY_ID_CODE2 = "countryIdCode2";
    public static final String FIELD_STATE_ID2 = "stateId2";
    public static final String FIELD_STATE_NAME2 = "stateName2";
    public static final String FIELD_STATE_ID_NAME2 = "stateIdName2";
    public static final String FIELD_POST_CODE2 = "postCode2";
    public static final String FIELD_BALANCE_DATE = "balanceDate";
    public static final String FIELD_SUPPLIER_BALANCE_DATE = "supplierBalanceDate";
    public static final String FIELD_SAASU_GUID = "saasuGuid";
    public static final String FIELD_SAASU_UPDATED_DATE = "saasuUpdatedDate";
    public static final String FIELD_SAASU_UPDATED_UID = "saasuUpdatedUid";

    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";

    public static final String FIELD_TERM_ID = "termId";
    public static final String FIELD_TERM_NAME = "termName";
    public static final String FIELD_TERM_ID_NAME = "termIdName";

    public static final String FIELD_CONTACT_ID = "contactId";
    public static final String FIELD_CONTACT_NAME = "contactName";
    public static final String FIELD_CONTACT_EMAIL = "contactEmail";

    public static final String FIELD_VAT_NUMBER = "vatNumber";
    public static final String FIELD_TRN_NUMBER = "trnNumber";
    public static final String FIELD_REGISTRATION_NUMBER = "registrationNumber";

    public static final String FIELD_PAYMENT_METHOD_ID = "paymentMethodId";
    public static final String FIELD_PAYMENT_METHOD_NAME = "paymentMethodName";
    public static final String FIELD_PAYMENT_METHOD_CODE = "paymentMethodCode";
    public static final String FIELD_PAYMENT_METHOD_ID_CODE = "paymentMethodIdCode";

    public static final String FIELD_CREATED_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_DATE = "lastUpdateDate";

    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_COMPOSITE_CRM_ACCOUNT_NAME = "compositeCrmAccountName";
    public static final String FIELD_ACCOUNT_NAME_COMPOSITE = "accountNameComposite";

    public static final String FIELD_CLIENT_BALANCE = "clientBalance";
    public static final String FIELD_SUPPLIER_BALANCE = "supplierBalance";
    public static final String FIELD_CREDIT_LIMIT = "creditLimit";
    public static final String FIELD_SALES_TYPE = "salesTypeName";

    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";

    public static final String FIELD_BANK_NAME = "bankName";
    public static final String FIELD_TAX_ID = "taxId";
    public static final String FIELD_TAX_NAME = "taxName";
    public static final String FIELD_TAX_ID_NAME = "taxIdName";
    public static final String FIELD_IN_TARGET = "inTarget";
    public static final String FIELD_DUE_DATE = "dueDate";

    // Solr solrtable fields
    public static final String SORTABLE_CRM_ACCOUNT_NAME = "sortableCrmAccountName";
    public static final String SORTABLE_CRM_ACCOUNT_NUMBER = "sortableCrmAccountNumber";
    public static final String SORTABLE_PHONE = "sortablePhone";
    public static final String SORTABLE_COUNTRY_NAME = "sortableCountryName";
    public static final String SORTABLE_COUNTRY_NAME2 = "sortableCountryName2";
    public static final String SORTABLE_EMAIL = "sortableEmail";
    public static final String SORTABLE_INDUSTRY_NAME = "sortableIndustryName";
    public static final String SORTABLE_ORGANIZATION_TYPE_NAME = "sortableOrganizationTypeName";
    public static final String SORTABLE_ANNUAL_REVENUE_NAME = "sortableAnnualRevenueName";
    public static final String SORTABLE_NUMBER_OF_EMPLOYEES_NAME = "sortableNumberOfEmployeesName";
    public static final String SORTABLE_RATING_NAME = "sortableRatingName";
    public static final String SORTABLE_OWNERSHIP_NAME = "sortableOwnershipName";
    public static final String SORTABLE_CRM_ACCOUNT_PARENT_NAME = "sortableCrmAccountParentName";
    public static final String SORTABLE_STREET = "sortableStreet";
    public static final String SORTABLE_STREET2 = "sortableStreet2";
    public static final String SORTABLE_CITY = "sortableCity";
    public static final String SORTABLE_CITY2 = "sortableCity2";
    public static final String SORTABLE_POST_CODE = "sortablePostCode";
    public static final String SORTABLE_POST_CODE2 = "sortablePostCode2";
    public static final String SORTABLE_STATE_NAME = "sortableStateName";
    public static final String SORTABLE_STATE_NAME2 = "sortableStateName2";
    public static final String SORTABLE_FAX = "sortableFax";
    public static final String SORTABLE_CURRENCY_NAME = "sortableCurrencyName";
    public static final String SORTABLE_VAT_NUMBER = "sortableVatNumber";
    public static final String SORTABLE_TRN_NUMBER = "sortableTrnNumber";
    public static final String SORTABLE_PAYMENT_METHOD_NAME = "sortablePaymentMethodName";
    public static final String SORTABLE_WEBSITE = "sortableWebsite";
    public static final String SORTABLE_CLIENT_BALANCE = "sortableClientBalance";
    public static final String SORTABLE_SUPPLIER_BALANCE = "sortableSupplierBalance";
    public static final String SORTABLE_CREDIT_LIMIT = "sortableCreditLimit";
    public static final String SORTABLE_BANK_NAME = "sortableBankName";
    public static final String SORTABLE_TAX_NAME = "sortableTaxName";

    public static String getSortingField(String sortingFieldOfClientSide) {
        if (SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID.equals(sortingFieldOfClientSide)) {
            return FIELD_CRM_ACCOUNT_ID;
        } else if (CrmAccountItem.ACCOUNT_NAME.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CRM_ACCOUNT_NAME;
        } /*else if (CrmAccountItem.OWNER.equals(sortingFieldOfClientSide)) {
            return SORTABLE_OWNER_NAME;
        }*/ else if (CrmAccountItem.ACCOUNT_NUMBER.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CRM_ACCOUNT_NUMBER;
        } else if (CrmAccountItem.PHONE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PHONE;
        } else if (CrmAccountItem.COUNTRY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_COUNTRY_NAME;
        } else if (CrmAccountItem.COUNTRY2.equals(sortingFieldOfClientSide)) {
            return SORTABLE_COUNTRY_NAME2;
        } else if (CrmAccountItem.EMAIL.equals(sortingFieldOfClientSide)) {
            return SORTABLE_EMAIL;
        } else if (CrmAccountItem.INDUSTRY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_INDUSTRY_NAME;
        } else if (CrmAccountItem.ORGANIZATION_TYPE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_ORGANIZATION_TYPE_NAME;
        } else if (CrmAccountItem.ANNUAL_REVENUE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_ANNUAL_REVENUE_NAME;
        } else if (CrmAccountItem.NUMBER_OF_EMPLOYEES.equals(sortingFieldOfClientSide)) {
            return SORTABLE_NUMBER_OF_EMPLOYEES_NAME;
        } else if (CrmAccountItem.RATING.equals(sortingFieldOfClientSide)) {
            return SORTABLE_RATING_NAME;
        } else if (CrmAccountItem.OWNERSHIP.equals(sortingFieldOfClientSide)) {
            return SORTABLE_OWNERSHIP_NAME;
        } else if (CrmAccountItem.PARENT_ACCOUNT_NAME.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CRM_ACCOUNT_PARENT_NAME;
        } else if (CrmAccountItem.STREET.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STREET;
        } else if (CrmAccountItem.STREET2.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STREET2;
        } else if (CrmAccountItem.CITY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CITY;
        } else if (CrmAccountItem.CITY2.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CITY2;
        } else if (CrmAccountItem.POST_CODE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_POST_CODE;
        } else if (CrmAccountItem.POST_CODE2.equals(sortingFieldOfClientSide)) {
            return SORTABLE_POST_CODE2;
        } else if (CrmAccountItem.STATE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STATE_NAME;
        } else if (CrmAccountItem.STATE2.equals(sortingFieldOfClientSide)) {
            return SORTABLE_STATE_NAME2;
        } else if (CrmAccountItem.FAX.equals(sortingFieldOfClientSide)) {
            return SORTABLE_FAX;
        } else if (CrmAccountItem.LAST_MODIFIED.equals(sortingFieldOfClientSide)) {
            return FIELD_LAST_UPDATED_DATE;
        } else if (CrmAccountItem.CREATION_DATE.equals(sortingFieldOfClientSide)) {
            return FIELD_CREATED_DATE;
        } else if (CrmAccountItem.CURRENCY.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CURRENCY_NAME;
        } else if (CrmAccountItem.VAT_NUMBER.equals(sortingFieldOfClientSide)) {
            return SORTABLE_VAT_NUMBER;
        } else if (CrmAccountItem.TRN_NUMBER.equals(sortingFieldOfClientSide)) {
            return SORTABLE_TRN_NUMBER;
        } else if (CrmAccountItem.PAYMENT_METHOD.equals(sortingFieldOfClientSide)) {
            return SORTABLE_PAYMENT_METHOD_NAME;
        } else if (CrmAccountItem.WEBSITE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_WEBSITE;
        }else if (CrmAccountItem.CLIENT_BALANCE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CLIENT_BALANCE;
        }else if (CrmAccountItem.SUPPLIER_BALANCE.equals(sortingFieldOfClientSide)) {
            return SORTABLE_SUPPLIER_BALANCE;
        } else if (CrmAccountItem.CREDIT_LIMIT.equals(sortingFieldOfClientSide)) {
            return SORTABLE_CREDIT_LIMIT;
        } else if (CrmAccountItem.BANK_ACCOUNT.equals(sortingFieldOfClientSide)) {
            return SORTABLE_BANK_NAME;
        } else if (CrmAccountItem.TAX.equals(sortingFieldOfClientSide)) {
            return SORTABLE_TAX_NAME;
        }
        return null;
    }
}
