package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 09.11.2017 19:47
 */
public class SolrChartOfAccountRepresenter implements IsSerializable {

    public static final String SPLIT = "@";

    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_ACCOUNT_ID = "accountId";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LAST_UPDATED_DATE = "lastUpdatedDate";
    public static final String BANK_ACCOUNT_ACTIVE = "bankAccountActive";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_PARENT_NAME = "parentName";
    public static final String FIELD_PARENT_ID_NAME = "parentIdName";
    public static final String FIELD_CURRENCY_ID = "currencyId";
    public static final String FIELD_CURRENCY_NAME = "currencyName";
    public static final String FIELD_CURRENCY_ID_NAME = "currencyIdName";
    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_TYPE_CATEGORY = "typeCategory";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";

    public static final String SORTABLE_CODE = "sortableCode";
    public static final String SORTABLE_NAME = "sortableName";
    public static final String SORTABLE_PARENT_NAME = "sortableParentName";
    public static final String SORTABLE_TYPE_NAME = "sortableTypeName";
    public static final String SORTABLE_CURRENCY_NAME = "sortableCurrencyName";
    public static final String FIELD_SHOW_IN_EXPENSE = "showInExpense";
    public static final String FIELD_ACTIVE = "active";
    public static final String FIELD_ENABLE_PAYMENTS = "enablePayments";
    public static final String FIELD_KEY = "key";
}
