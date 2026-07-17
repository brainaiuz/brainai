package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CFItemTableSettingmanager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldListTO;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldCategoryEnum;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_VIEW;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIPPED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIPPING;

/**
 * Created by Normurod on 3/13/2017.
 */
@Service("itemTableSettings")
@Transactional
public class ItemTableSettingServiceImpl implements ItemTableSettingService, ItemTableSettingsServiceLocal, ItemTableConstants {

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private ItemTableSettingsManager itemTableSettingsManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private CFItemTableSettingmanager cfItemTableSettingmanager;
    @Autowired
    private UserManager userManager;
    @Autowired
    protected FinancialSettingsManager financialSettingsManager;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    private LinkedHashMap<String, ColumnConfigs> getColumnsMap() {
        LinkedHashMap<String, ColumnConfigs> map = new LinkedHashMap<>();
        map.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        map.get(PRODUCT).setSelected(true);

        map.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        map.get(DESCRIPTION).setSelected(true);

        map.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        map.get(QTY).setSelected(true);

        map.put(MEASUREMENT, new ColumnConfigs(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, "U/M"), false));

        map.put(DISCOUNT_LIST, new ColumnConfigs(DISCOUNT_LIST, commonLocalizer.localize(DISCOUNT_LIST, "Item disc."), false));
        map.put(DISCOUNT_AMT, new ColumnConfigs(DISCOUNT_AMT, commonLocalizer.localize(DISCOUNT_AMT, "Discount"), false));
        map.get(DISCOUNT_AMT).setSelected(true);

        map.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        map.get(ACCOUNT).setSelected(true);

        map.put(NET_AMT, new ColumnConfigs(NET_AMT, commonLocalizer.localize(NET_AMT, "Net Amount"), true));
        map.get(NET_AMT).setSelected(true);

        map.put(TAX_LIST, new ColumnConfigs(TAX_LIST, commonLocalizer.localize(TAX_LIST, "Tax Rate"), false));
        map.get(TAX_LIST).setSelected(true);

        map.put(TAX_AMT, new ColumnConfigs(TAX_AMT, commonLocalizer.localize(TAX_AMT, "Tax Amount"), false));
        return map;
    }

    /**
     * List all of default columns that enabling from settings
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForAll() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>(getColumnsMap());
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, DEPARTMENT, organizeDepartment, false));
        }
        if (genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED)) {
            columnMap.put(DOUBLE_TAX_LIST, new ColumnConfigs(DOUBLE_TAX_LIST, DOUBLE_TAX_LIST, commonLocalizer.localize(DOUBLE_TAX_LIST, "Tax Rate 2"), false));
        }
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, PROJECT, commonLocalizer.localize(PROJECT, "Project"), false));
        }

        return columnMap;
    }

    /**
     * List all of default columns that enabling from settings
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForCRMSubItems(ItemTableEnum section) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, commonLocalizer.localize("item", "Item")), false,18));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, commonLocalizer.localize("description", "Description")), false));
        columnMap.put(QTY, new ColumnConfigs(QTY, QTY, ItemTableEnum.BILL_OF_MATERIALS_ITEM.equals(section) ? commonLocalizer.localize("plannedQty", "Planned Qty") : commonLocalizer.localize(QTY, "Qty"), false));
        columnMap.put(MEASUREMENT, new ColumnConfigs(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, commonLocalizer.localize("unitMeasurement", "U/M"))), false,8));
        if (ItemTableEnum.SUPPLIER_ITEM.equals(section)) {
            columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, COSTPRICE, commonLocalizer.localize(UNITPRICE, commonLocalizer.localize("price", "Price")), false));
        } else {
            columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, UNITPRICE, commonLocalizer.localize(UNITPRICE, commonLocalizer.localize("price", "Price")), false));
        }
        if (ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section)) {
            columnMap.put(DISCOUNT_AMT, new ColumnConfigs(DISCOUNT_AMT, commonLocalizer.localize(DISCOUNT_AMT, "Discount"), false));
            columnMap.put(CLIENT, new ColumnConfigs(CLIENT, SUPPLIER, commonLocalizer.localize(CLIENT, "Supplier"), false));
            columnMap.put(CATEGORY, new ColumnConfigs(CATEGORY, commonLocalizer.localize(CATEGORY, "Category"), false));
            columnMap.put(BRAND, new ColumnConfigs(BRAND, commonLocalizer.localize(BRAND, "Brand"), false));
            columnMap.put(TAX_LIST, new ColumnConfigs(TAX_LIST, commonLocalizer.localize(TAX_LIST, "Tax Rate"), false));
            columnMap.put(NET_AMT, new ColumnConfigs(NET_AMT, commonLocalizer.localize(NET_AMT, "Net Amount"), false));
            columnMap.put(TOTAL_AMT, new ColumnConfigs(TOTAL_AMT, commonLocalizer.localize(TOTAL_AMT, "Total Amount"), false));
            if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                columnMap.put(PROJECT, new ColumnConfigs(PROJECT, PROJECT, commonLocalizer.localize(PROJECT, "Project"), false));
            }
        } else if (ItemTableEnum.BILL_OF_MATERIALS_ITEM.equals(section)) {
            columnMap.put(QTY_ON_HAND, new ColumnConfigs(QTY_ON_HAND, commonLocalizer.localize(QTY_ON_HAND, commonLocalizer.localize("onHand", "On hand")), true,5));
            columnMap.put(REQUESTED_BEFORE, new ColumnConfigs(REQUESTED_BEFORE, commonLocalizer.localize(REQUESTED_BEFORE, commonLocalizer.localize("requestedBefore", "Requested Before")), true,5));
            columnMap.put(REQUEST_QTY, new ColumnConfigs(REQUEST_QTY, commonLocalizer.localize(REQUEST_QTY, commonLocalizer.localize("requestedQty", "Requested Qty")), true,5));
            columnMap.put(NET_AMT, new ColumnConfigs(NET_AMT, commonLocalizer.localize(NET_AMT, commonLocalizer.localize("netAmount", "Net Amount")), false));
            columnMap.put(CHECKBOX, new ColumnConfigs(CHECKBOX, commonLocalizer.localize(CHECKBOX, commonLocalizer.localize("request", "Request")), true,5));
        }

        //Default selected columns when no data in Database
        if (columnMap.get(PRODUCT) != null) {
            columnMap.get(PRODUCT).setSelected(true);
        }
        if (columnMap.get(DESCRIPTION) != null) {
            columnMap.get(DESCRIPTION).setSelected(true);
        }
        if (columnMap.get(QTY) != null) {
            columnMap.get(QTY).setSelected(true);
        }
        if (columnMap.get(MEASUREMENT) != null && !ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section)) {
            columnMap.get(MEASUREMENT).setSelected(true);
        }
        if (columnMap.get(UNITPRICE) != null) {
            columnMap.get(UNITPRICE).setSelected(true);
        }
        if (columnMap.get(NET_AMT) != null) {
            columnMap.get(NET_AMT).setSelected(true);
        }
        if (columnMap.get(TOTAL_AMT) != null) {
            columnMap.get(TOTAL_AMT).setSelected(true);
        }
        if (columnMap.get(TAX_LIST) != null && !ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section)) {
            columnMap.get(TAX_LIST).setSelected(true);
        }

        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForRFQ() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(RECEIPTS, new ColumnConfigs(RECEIPTS, commonLocalizer.localize(RECEIPTS, "Receipts"), false));
        columnMap.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        columnMap.put(MEASUREMENT, new ColumnConfigs(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, "U/M"), false));
        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "UnitPrice"), false));
        columnMap.put(SUPPLIER, new ColumnConfigs(SUPPLIER, commonLocalizer.localize(SUPPLIER, "Supplier"), false));
        columnMap.put(COMISSION, new ColumnConfigs(COMISSION, commonLocalizer.localize(COMISSION, "Comission"), false));
        columnMap.put(REMARK, new ColumnConfigs(REMARK, commonLocalizer.localize(REMARK, "Remarks"), false));

        //Default selected columns when no data in Database
        columnMap.get(PRODUCT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(RECEIPTS).setSelected(true);
        columnMap.get(QTY).setSelected(true);
        columnMap.get(MEASUREMENT).setSelected(true);
        columnMap.get(UNITPRICE).setSelected(true);
        columnMap.get(SUPPLIER).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForRFP() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }
        columnMap.put(MEASUREMENT, new ColumnConfigs(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, "U/M"), false));
        columnMap.put(QTY_ON_HAND, new ColumnConfigs(QTY_ON_HAND, commonLocalizer.localize(QTY_ON_HAND, "QtyOnHand"), false));

        //Default selected columns when no data in Database
        columnMap.get(PRODUCT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(QTY).setSelected(true);
        columnMap.get(MEASUREMENT).setSelected(true);
        columnMap.get(QTY_ON_HAND).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForManualJournal() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        columnMap.put(DEBIT, new ColumnConfigs(DEBIT, commonLocalizer.localize(DEBIT, "Debit"), true));
        columnMap.put(CREDIT, new ColumnConfigs(CREDIT, commonLocalizer.localize(CREDIT, "Credit"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(NAME, new ColumnConfigs(NAME, commonLocalizer.localize(NAME, "Name"), false));
        columnMap.put(BILLING, new ColumnConfigs(BILLING, commonLocalizer.localize(BILLING, "Billing"), false));
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, commonLocalizer.localize(PROJECT, "Project"), false));
        }
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }

        //Default selected columns when no data in Database
        columnMap.get(ACCOUNT).setSelected(true);
        columnMap.get(DEBIT).setSelected(true);
        columnMap.get(CREDIT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(NAME).setSelected(true);
        columnMap.get(BILLING).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForBankPayment() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(REFERENCE, new ColumnConfigs(REFERENCE, commonLocalizer.localize("reference", "Reference"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, commonLocalizer.localize("amount", "Amount"), true));
        columnMap.put(TAX_RATE, new ColumnConfigs(TAX_RATE, commonLocalizer.localize("taxRate", "Tax Rate"), false));
        columnMap.put(NAME, new ColumnConfigs(NAME, commonLocalizer.localize("name", "Name"), false));
        columnMap.put(CLIENT, new ColumnConfigs(CLIENT, commonLocalizer.localize("BILL_TO", "Bill To"), false));

        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsProperty property = propertManager.findByCode(Constants.PROJECT);
            String organizeProject = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PROJECT, "Project");
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, organizeProject, false));
        }
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }

        //Default selected columns when no data in Database
        columnMap.get(ACCOUNT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(REFERENCE).setSelected(true);
        columnMap.get(AMOUNT).setSelected(true);
        columnMap.get(TAX_RATE).setSelected(true);
        columnMap.get(NAME).setSelected(true);
        columnMap.get(CLIENT).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForCashPayment() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(REFERENCE, new ColumnConfigs(REFERENCE, commonLocalizer.localize("reference", "Reference"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, commonLocalizer.localize("amount", "Amount"), true));
        columnMap.put(TAX_RATE, new ColumnConfigs(TAX_RATE, commonLocalizer.localize("taxRate", "Tax Rate"), false));
        columnMap.put(NAME, new ColumnConfigs(NAME, commonLocalizer.localize("name", "Name"), false));
        columnMap.put(CLIENT, new ColumnConfigs(CLIENT, commonLocalizer.localize("BILL_TO", "Bill To"), false));
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsProperty property = propertManager.findByCode(Constants.PROJECT);
            String organizeProject = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PROJECT, "Project");
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, organizeProject, false));
        }
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }

        //Default selected columns when no data in Database
        columnMap.get(ACCOUNT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(REFERENCE).setSelected(true);
        columnMap.get(AMOUNT).setSelected(true);
        columnMap.get(TAX_RATE).setSelected(true);
        columnMap.get(NAME).setSelected(true);
        columnMap.get(CLIENT).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForBankReceipt() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(REFERENCE, new ColumnConfigs(REFERENCE, commonLocalizer.localize("reference", "Reference"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, commonLocalizer.localize("amount", "Amount"), true));
        columnMap.put(TAX_RATE, new ColumnConfigs(TAX_RATE, commonLocalizer.localize("taxRate", "Tax Rate"), false));
        columnMap.put(NAME, new ColumnConfigs(NAME, commonLocalizer.localize("name", "Name"), false));
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsProperty property = propertManager.findByCode(Constants.PROJECT);
            String organizeProject = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PROJECT, "Project");
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, organizeProject, false));
        }
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }

        //Default selected columns when no data in Database
        columnMap.get(ACCOUNT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(REFERENCE).setSelected(true);
        columnMap.get(AMOUNT).setSelected(true);
        columnMap.get(TAX_RATE).setSelected(true);
        columnMap.get(NAME).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForCashReceipt() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(ACCOUNT, new ColumnConfigs(ACCOUNT, commonLocalizer.localize(ACCOUNT, "Account"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(REFERENCE, new ColumnConfigs(REFERENCE, commonLocalizer.localize("reference", "Reference"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, commonLocalizer.localize("amount", "Amount"), true));
        columnMap.put(TAX_RATE, new ColumnConfigs(TAX_RATE, commonLocalizer.localize("taxRate", "Tax Rate"), false));
        columnMap.put(NAME, new ColumnConfigs(NAME, commonLocalizer.localize("name", "Name"), false));
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            EdsProperty property = propertManager.findByCode(Constants.PROJECT);
            String organizeProject = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(PROJECT, "Project");
            columnMap.put(PROJECT, new ColumnConfigs(PROJECT, organizeProject, false));
        }
        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }

        //Default selected columns when no data in Database
        columnMap.get(ACCOUNT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(REFERENCE).setSelected(true);
        columnMap.get(AMOUNT).setSelected(true);
        columnMap.get(TAX_RATE).setSelected(true);
        columnMap.get(NAME).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForLead() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        columnMap.put(MEASUREMENT, new ColumnConfigs(MEASUREMENT, commonLocalizer.localize(MEASUREMENT, "U/M"), false));
        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "Price"), false));
        columnMap.put(SUPPLIER, new ColumnConfigs(SUPPLIER, commonLocalizer.localize(SUPPLIER, "Supplier"), false));
        columnMap.put(CATEGORY, new ColumnConfigs(CATEGORY, commonLocalizer.localize(CATEGORY, "Category"), false));
        columnMap.put(BRAND, new ColumnConfigs(BRAND, commonLocalizer.localize(BRAND, "Brand"), false));
        columnMap.put(TAX_LIST, new ColumnConfigs(TAX_LIST, commonLocalizer.localize(TAX_LIST, "Tax Rate"), false));
        columnMap.put(NET_AMT, new ColumnConfigs(NET_AMT, commonLocalizer.localize(NET_AMT, "Net Amount"), false));
        columnMap.put(TOTAL_AMT, new ColumnConfigs(TOTAL_AMT, commonLocalizer.localize(TOTAL_AMT, "Total Amount"), false));

        //Default selected columns when no data in Database
        columnMap.get(PRODUCT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(QTY).setSelected(true);
        columnMap.get(MEASUREMENT).setSelected(true);
        columnMap.get(UNITPRICE).setSelected(true);
        columnMap.get(SUPPLIER).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForAdditionalPayment() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(EMPLOYEE, new ColumnConfigs(EMPLOYEE, commonLocalizer.localize("employee", "Employee"), true));
        columnMap.put(BASIC_SALARY, new ColumnConfigs(BASIC_SALARY, commonLocalizer.localize("basicSalary", "Basic Salary"), true));
        columnMap.put(PERCENTAGE, new ColumnConfigs(PERCENTAGE, commonLocalizer.localize("percentage", "Percentage"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, commonLocalizer.localize("amount", "Amount"), false));
        columnMap.put(CATEGORY, new ColumnConfigs(CATEGORY, commonLocalizer.localize("category", "Category"), false));
        columnMap.put(PAYMENT_DATE, new ColumnConfigs(PAYMENT_DATE, commonLocalizer.localize("paymentDate", "Payment Date"), false));
        columnMap.put("EMPLOYER_CONTRIBUTION", new ColumnConfigs("EMPLOYER_CONTRIBUTION", commonLocalizer.localize("employerContribution", "Employer Contribution"), false));
        columnMap.put("TAX", new ColumnConfigs("TAX", commonLocalizer.localize("tax", "Tax"), false));
        columnMap.put("DEDUCTION", new ColumnConfigs("DEDUCTION", commonLocalizer.localize("deduction", "Deduction"), false));
        columnMap.put("TOTAL_SALARY", new ColumnConfigs("TOTAL_SALARY", commonLocalizer.localize("total", "Total"), false));

        columnMap.get(EMPLOYEE).setSelected(true);
        columnMap.get(BASIC_SALARY).setSelected(true);
        columnMap.get(PERCENTAGE).setSelected(true);
        columnMap.get(AMOUNT).setSelected(true);
        columnMap.get(CATEGORY).setSelected(true);
        columnMap.get(PAYMENT_DATE).setSelected(true);
        columnMap.get("EMPLOYER_CONTRIBUTION").setSelected(true);
        columnMap.get("TAX").setSelected(true);
        columnMap.get("DEDUCTION").setSelected(true);
        columnMap.get("TOTAL_SALARY").setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForRotation() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(EMPLOYEE, new ColumnConfigs(EMPLOYEE, commonLocalizer.localize("employee", "Employee"), true));
        columnMap.put(CURRENT_LOCATION, new ColumnConfigs(CURRENT_LOCATION, commonLocalizer.localize("currentLocation", "Current Location"), true));
        columnMap.put(CURRENT_DEPARTMENT, new ColumnConfigs(CURRENT_DEPARTMENT, commonLocalizer.localize("currentDepartment", "Current Department"), true));
        columnMap.put(CURRENT_POSIITON, new ColumnConfigs(CURRENT_POSIITON, commonLocalizer.localize("currentPosition", "Current Position"), false));
        columnMap.put(NEW_LOCATION, new ColumnConfigs(NEW_LOCATION, commonLocalizer.localize("newLocation", "New Location"), false));
        columnMap.put(NEW_DEPARTMENT, new ColumnConfigs(NEW_DEPARTMENT, commonLocalizer.localize("newDepartment", "New Department"), false));
        columnMap.put(NEW_POSITION, new ColumnConfigs(NEW_POSITION, commonLocalizer.localizeWithParam("addNew", commonLocalizer.localize("position")), false));

        columnMap.get(EMPLOYEE).setSelected(true);
        columnMap.get(CURRENT_LOCATION).setSelected(true);
        columnMap.get(CURRENT_DEPARTMENT).setSelected(true);
        columnMap.get(CURRENT_POSIITON).setSelected(true);
        columnMap.get(NEW_LOCATION).setSelected(true);
        columnMap.get(NEW_DEPARTMENT).setSelected(true);
        columnMap.get(NEW_POSITION).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForExperience() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(HIRE_DATE, new ColumnConfigs(HIRE_DATE, commonLocalizer.localize("hireDate", "Hire Date"), false));
        columnMap.put(RESIGN_DATE, new ColumnConfigs(RESIGN_DATE, commonLocalizer.localize("resignDate", "Resign Date"), false));
        columnMap.put(INDUSTRY, new ColumnConfigs(INDUSTRY, commonLocalizer.localize("industry", "Industry"), false));
        columnMap.put(POSITION, new ColumnConfigs(POSITION, commonLocalizer.localize("position", "Position"), false));
        columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, commonLocalizer.localize("department", "Department"), false));
        columnMap.put(ORGANIZATION, new ColumnConfigs(ORGANIZATION, commonLocalizer.localize("organization", "Organization"), false));

        columnMap.get(HIRE_DATE).setSelected(true);
        columnMap.get(RESIGN_DATE).setSelected(true);
        columnMap.get(INDUSTRY).setSelected(true);
        columnMap.get(POSITION).setSelected(true);
        columnMap.get(DEPARTMENT).setSelected(true);
        columnMap.get(ORGANIZATION).setSelected(true);

        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getDefaultColumnsForGroupPlacement() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(LOCATION, new ColumnConfigs(LOCATION, commonLocalizer.localize("location", "Location"), true));
        columnMap.put(TYPE, new ColumnConfigs(TYPE, commonLocalizer.localize("type", "Type"), true));
        columnMap.put(CANDIDATE, new ColumnConfigs(CANDIDATE, commonLocalizer.localize("candidate", "Candidate"), false));
        columnMap.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, commonLocalizer.localize("department", "Department"), false));
        columnMap.put(POSITION, new ColumnConfigs(POSITION, commonLocalizer.localize("position", "Position"), false));
        columnMap.put(VACANCY, new ColumnConfigs(VACANCY, commonLocalizer.localize("vacancy", "Vacancy"), false));
        columnMap.put(FROM_DATE, new ColumnConfigs(FROM_DATE, commonLocalizer.localize("effectiveDate", "Effective Date"), false));

        columnMap.get(LOCATION).setSelected(true);
        columnMap.get(TYPE).setSelected(true);
        columnMap.get(CANDIDATE).setSelected(true);
        columnMap.get(DEPARTMENT).setSelected(true);
        columnMap.get(POSITION).setSelected(true);
        columnMap.get(VACANCY).setSelected(true);
        columnMap.get(FROM_DATE).setSelected(true);
        return columnMap;
    }

    /**
     * List all of Sale Credit Note columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllCNColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            columnMap.put(FAI_CATEGORY, new ColumnConfigs(FAI_CATEGORY, commonLocalizer.localize(FAI_CATEGORY, "FAI Category"), false));
        }

        initCustomFields(CustomFieldSection.SaleInvoiceItem, columnMap);

        return columnMap;
    }

    /**
     * List all of Sale invoice item columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllSIIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
            columnMap.get(WAREHOUSE).setSelected(true);
        }
        if (financialSettings.isEnableDeferredTransaction()) {
            columnMap.put(FROM_DATE, new ColumnConfigs(FROM_DATE, commonLocalizer.localize(FROM_DATE, "From"), false));
            columnMap.get(FROM_DATE).setSelected(true);

            columnMap.put(TO_DATE, new ColumnConfigs(TO_DATE, commonLocalizer.localize(TO_DATE, "To"), false));
            columnMap.get(TO_DATE).setSelected(true);
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            columnMap.put(FAI_CATEGORY, new ColumnConfigs(FAI_CATEGORY, FAI_CATEGORY, commonLocalizer.localize("category", "Fai Category"), false));
            columnMap.get(FAI_CATEGORY).setSelected(true);
        }

        initCustomFields(CustomFieldSection.SaleInvoiceItem, columnMap);

        return columnMap;
    }

    /**
     * List all of Purchase invoice item columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllPIIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, COSTPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
            columnMap.get(WAREHOUSE).setSelected(true);
        }
        if (financialSettings.isEnableDeferredTransaction()) {
            columnMap.put(FROM_DATE, new ColumnConfigs(FROM_DATE, commonLocalizer.localize(FROM_DATE, "From"), false));
            columnMap.get(FROM_DATE).setSelected(true);

            columnMap.put(TO_DATE, new ColumnConfigs(TO_DATE, commonLocalizer.localize(TO_DATE, "To"), false));
            columnMap.get(TO_DATE).setSelected(true);
        }
        columnMap.put(CLIENT, new ColumnConfigs(CLIENT, SUPPLIER, commonLocalizer.localize("BILL_TO", "Bill To"), false));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            columnMap.put(FAI_CATEGORY, new ColumnConfigs(FAI_CATEGORY, FAI_CATEGORY, commonLocalizer.localize("category", "Fai Category"), false));
            columnMap.get(FAI_CATEGORY).setSelected(false);
        }

        initCustomFields(CustomFieldSection.PurchaseInvoiceItem, columnMap);

        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllDNColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, COSTPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
            columnMap.get(WAREHOUSE).setSelected(true);
        }
        columnMap.put(CLIENT, new ColumnConfigs(CLIENT, SUPPLIER, commonLocalizer.localize("BILL_TO", "Bill To"), false));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            columnMap.put(FAI_CATEGORY, new ColumnConfigs(FAI_CATEGORY, FAI_CATEGORY, commonLocalizer.localize("category", "Fai Category"), false));
            columnMap.get(FAI_CATEGORY).setSelected(false);
        }
        initCustomFields(CustomFieldSection.PurchaseInvoiceItem, columnMap);

        return columnMap;
    }

    /**
     * List all of Sale quote item columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllSQIColumns(boolean isSalesOrder) {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_DISCOUNT_ENABLE)) {
            columnMap.put(DOUBLE_DISCOUNT_LIST, new ColumnConfigs(DOUBLE_DISCOUNT_LIST, "Item disc. 2", false));
            columnMap.put(DOUBLE_DISCOUNT_AMT, new ColumnConfigs(DOUBLE_DISCOUNT_AMT, commonLocalizer.localize(DOUBLE_DISCOUNT_AMT, "Discount 2"), false));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.QUOTE_COMISSION_ENABLED)) {
            columnMap.put(COMISSION, new ColumnConfigs(COMISSION, commonLocalizer.localize(COMISSION, "Comission %"), false));
        }
        columnMap.put(TOTAL_AMT, new ColumnConfigs(TOTAL_AMT, commonLocalizer.localize(TOTAL_AMT, "Total Amount"), false));
        columnMap.put(ATTACHMENT, new ColumnConfigs(ATTACHMENT, commonLocalizer.localize(ATTACHMENT, "Attachment"), false));

        initCustomFields(isSalesOrder ? CustomFieldSection.SaleOrderItem : CustomFieldSection.SaleQuoteItem, columnMap);
        return columnMap;
    }

    /**
     * List all of Purchase order item columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllPOIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAll();

        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, COSTPRICE, commonLocalizer.localize(UNITPRICE, "Price"), true));
        columnMap.get(UNITPRICE).setSelected(true);

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.QUOTE_COMISSION_ENABLED)) {
            columnMap.put(COMISSION, new ColumnConfigs(COMISSION, commonLocalizer.localize(COMISSION, "Comission %"), false));
        }
        columnMap.put(TOTAL_AMT, new ColumnConfigs(TOTAL_AMT, commonLocalizer.localize(TOTAL_AMT, "Total Amount"), false));

        initCustomFields(CustomFieldSection.PurchaseOrderItem, columnMap);
        return columnMap;
    }

    /**
     * List all of Expense report item columns
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllERIColumns() {
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        LinkedHashMap<String, ColumnConfigs> map = new LinkedHashMap<>();

        map.put(ACCOUNT_LIST, new ColumnConfigs(ACCOUNT_LIST, commonLocalizer.localize(ACCOUNT_LIST, "Category"), true));
        map.get(ACCOUNT_LIST).setSelected(true);

        map.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), true));
        map.get(DESCRIPTION).setSelected(true);

        map.put(UNITS, new ColumnConfigs(UNITS, commonLocalizer.localize(UNITS, "Units"), true));
        map.get(UNITS).setSelected(true);

        map.put(COST, new ColumnConfigs(COST, commonLocalizer.localize(COST, "Cost per unit"), true));
        map.get(COST).setSelected(true);

        map.put(TAX_LIST, new ColumnConfigs(TAX_LIST, commonLocalizer.localize(TAX_LIST, "Tax Rate"), false));
        map.get(TAX_LIST).setSelected(true);

        if (genericSettings.contains(GenericSettingsEnum.DOUBLE_TAX_ENABLED)) {
            map.put(DOUBLE_TAX_LIST, new ColumnConfigs(DOUBLE_TAX_LIST, commonLocalizer.localize(DOUBLE_TAX_LIST, "Tax Rate 2"), false));
        }
        map.put(TOTAL, new ColumnConfigs(TOTAL, commonLocalizer.localize(TOTAL, "Total"), true));
        map.get(TOTAL).setSelected(true);

        map.put(BASE_SUBTOTAL, new ColumnConfigs(BASE_SUBTOTAL, commonLocalizer.localize("baseTotal", "Base Total"), false));

        map.put(RECEIPTS_PANEL, new ColumnConfigs(RECEIPTS_PANEL, commonLocalizer.localize(RECEIPTS_PANEL, "Receipts"), false));
        map.get(RECEIPTS_PANEL).setSelected(true);

        map.put(CUSTOMER_LIST, new ColumnConfigs(CUSTOMER_LIST, commonLocalizer.localize(CUSTOMER_LIST, "Bill to"), false));
        map.put(MARKUP_AMOUNT, new ColumnConfigs(MARKUP_AMOUNT, commonLocalizer.localize(MARKUP_AMOUNT, "Markup Amount"), false));

        if (financialSettings.getEnableAccountingDepartmentRelation()) {
            EdsProperty property = propertManager.findByCode(Constants.DEPARTMENT_LIST);
            String organizeDepartment = property != null && property.getSingular() != null ? property.getSingular() : commonLocalizer.localize(DEPARTMENT, "Department");
            map.put(DEPARTMENT, new ColumnConfigs(DEPARTMENT, organizeDepartment, false));
        }
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU) && genericSettings.contains(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            map.put(PROJECT, new ColumnConfigs(PROJECT, commonLocalizer.localize(PROJECT, "Project"), false));
        }
        if (genericSettings.contains(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
            map.put(PO_LIST, new ColumnConfigs(PO_LIST, commonLocalizer.localize(PO_LIST, "Purchase order"), false));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
            map.put(FAI_CATEGORY, new ColumnConfigs(FAI_CATEGORY, commonLocalizer.localize("faiCategory", "FAI Category"), false));
        }

        initCustomFields(CustomFieldSection.ExpenseReportItem, map);

        return map;
    }

    /**
     * List all of Oppotunity sub item columns
     *
     * @return
     */
    protected LinkedHashMap<String, ColumnConfigs> getAllCRMSubIColumns(ItemTableEnum section) {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForCRMSubItems(section);

        if (ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section)) {
            initCustomFields(CustomFieldSection.OpportunitySubItem, columnMap);
        } else if (ItemTableEnum.CLIENT_ITEM.equals(section)) {
            initCustomFields(CustomFieldSection.ClientItem, columnMap);
        } else if (ItemTableEnum.SUPPLIER_ITEM.equals(section)) {
            initCustomFields(CustomFieldSection.SupplierItem, columnMap);
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllRFQIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForRFQ();
        initCustomFields(CustomFieldSection.RFQItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllRentalOrderColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(BRAND, new ColumnConfigs(BRAND, commonLocalizer.localize(BRAND, "Brand"), false));
        columnMap.put(CATEGORY, new ColumnConfigs(CATEGORY, commonLocalizer.localize(CATEGORY, "Category"), false));
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        columnMap.put(UNITPRICE, new ColumnConfigs(UNITPRICE, commonLocalizer.localize(UNITPRICE, "UnitPrice"), true));
        columnMap.put(TAX_LIST, new ColumnConfigs(TAX_LIST, commonLocalizer.localize(TAX_LIST, "Tax Rate"), false));
        columnMap.put(NET_AMT, new ColumnConfigs(NET_AMT, commonLocalizer.localize(NET_AMT, "Net Amount"), false));
        columnMap.put(TOTAL_AMT, new ColumnConfigs(TOTAL_AMT, commonLocalizer.localize(TOTAL_AMT, "Total Amount"), false));
        columnMap.put(PRODUCT_FOR_RENT, new ColumnConfigs(PRODUCT_FOR_RENT, commonLocalizer.localize(PRODUCT, "Product"), false));

        //Default selected columns when no data in Database
        columnMap.get(BRAND).setSelected(true);
        columnMap.get(CATEGORY).setSelected(true);
        columnMap.get(PRODUCT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(QTY).setSelected(true);
        columnMap.get(UNITPRICE).setSelected(true);
        columnMap.get(TAX_LIST).setSelected(true);
        columnMap.get(NET_AMT).setSelected(true);
        columnMap.get(TOTAL_AMT).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllPickListColumns() {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        columnMap.put(REFERENCE, new ColumnConfigs(REFERENCE, commonLocalizer.localize(REFERENCE, "Reference"), false));
        if (fs.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
        }
        columnMap.put(QTY, new ColumnConfigs(QTY, commonLocalizer.localize(QTY, "Qty"), true));
        columnMap.put(NUMBER_OF_PACKS, new ColumnConfigs(NUMBER_OF_PACKS, commonLocalizer.localize(NUMBER_OF_PACKS, "# of Packs"), false));
        columnMap.put(QTY_PER_PACK, new ColumnConfigs(QTY_PER_PACK, commonLocalizer.localize(QTY_PER_PACK, "Qty per Pack"), false));
        columnMap.put(QTY_ON_HAND, new ColumnConfigs(QTY_ON_HAND, commonLocalizer.localize(QTY_ON_HAND, "Qty on hand"), false));
        columnMap.put(AVAILABLE_QTY, new ColumnConfigs(AVAILABLE_QTY, commonLocalizer.localize(AVAILABLE_QTY, "Available stock"), false));
        columnMap.put(BOOK_RESERVATION, new ColumnConfigs(BOOK_RESERVATION, commonLocalizer.localize(BOOK_RESERVATION, "Book Reservation"), false));
        columnMap.put(SHIPPED, new ColumnConfigs(SHIPPED, commonLocalizer.localize(SHIPPED, "Shipped"), true));
        columnMap.put(SHIPPING, new ColumnConfigs(SHIPPING, commonLocalizer.localize(SHIPPING, "Shipping"), false));

        //Default selected columns when no data in Database
        columnMap.get(PRODUCT).setSelected(true);
        columnMap.get(DESCRIPTION).setSelected(true);
        columnMap.get(REFERENCE).setSelected(true);
        if (fs.getEnableMultiWarehouse()) {
            columnMap.get(WAREHOUSE).setSelected(true);
        }
        columnMap.get(QTY).setSelected(true);
        columnMap.get(NUMBER_OF_PACKS).setSelected(true);
        columnMap.get(QTY_PER_PACK).setSelected(true);
        columnMap.get(QTY_ON_HAND).setSelected(true);
        columnMap.get(AVAILABLE_QTY).setSelected(true);
        columnMap.get(BOOK_RESERVATION).setSelected(true);
        columnMap.get(SHIPPED).setSelected(true);
        columnMap.get(SHIPPING).setSelected(true);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllRFPIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForRFP();
        initCustomFields(CustomFieldSection.RFPItem, columnMap);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse()) {
            columnMap.put(WAREHOUSE, new ColumnConfigs(WAREHOUSE, commonLocalizer.localize(WAREHOUSE, "Warehouse"), true));
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllManualJournalIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForManualJournal();
        initCustomFields(CustomFieldSection.ManualJournalItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllBankPaymentIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForBankPayment();
        initCustomFields(CustomFieldSection.BankPaymentItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllCashPaymentIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForCashPayment();
        initCustomFields(CustomFieldSection.CashPaymentItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllBankReceiptIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForBankReceipt();
        initCustomFields(CustomFieldSection.BankReceiptItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllCashReceiptIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForCashReceipt();
        initCustomFields(CustomFieldSection.CashReceiptItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllLeadIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForLead();
        initCustomFields(CustomFieldSection.LeadItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllAdditionalPaymentIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForAdditionalPayment();
        initCustomFields(CustomFieldSection.AdditionalPaymentItem, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllRotationIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForRotation();
        initCustomFields(CustomFieldSection.RotationItemTable, columnMap);
        return columnMap;
    }
    protected LinkedHashMap<String, ColumnConfigs> getAllExperienceIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForExperience();
        initCustomFields(CustomFieldSection.ExperienceItemTable, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAllGroupPlacementIColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = getDefaultColumnsForGroupPlacement();
        initCustomFields(CustomFieldSection.GroupPlacementItemTable, columnMap);
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getCustomFormItemCustomFields(String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        //columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
        //columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
        List<EdsCompanyCustomFieldsSettings> fields = companyCustomFieldsManager.getCompanyCustomFieldsByCategoryForListView(CustomFieldSection.CustomFormItemTable.name(), uuid);

        if (CollectionUtils.isNotEmpty(fields)) {
            fields.forEach(field -> {
                ColumnConfigs columnConfig = new ColumnConfigs();
                columnConfig.setCompanyCustomFieldID(field.getObjectID());
                columnConfig.setTitle(field.getFieldName());
                columnConfig.setCode(field.getColumnCode());
                columnConfig.setWidth(field.getColumnWidth());
                columnConfig.setAliasName(field.getAliasName());
                columnConfig.setDataType(field.getDataType());
                columnConfig.setUiType(field.getUiType());
                columnConfig.setRequired(field.getRequired());
                columnMap.put(field.getColumnCode(), columnConfig);
            });
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getOpportunityItemCustomFields(String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        List<EdsCompanyCustomFieldsSettings> fields = companyCustomFieldsManager.getCompanyCustomFieldsByCategoryForListView(CustomFieldSection.OpportunityItemTable.name(), uuid);

        if (CollectionUtils.isNotEmpty(fields)) {
            fields.forEach(field -> {
                ColumnConfigs columnConfig = new ColumnConfigs();
                columnConfig.setCompanyCustomFieldID(field.getObjectID());
                columnConfig.setTitle(field.getFieldName());
                columnConfig.setCode(field.getColumnCode());
                columnConfig.setWidth(field.getColumnWidth());
                columnConfig.setAliasName(field.getAliasName());
                columnConfig.setDataType(field.getDataType());
                columnConfig.setUiType(field.getUiType());
                columnConfig.setRequired(field.getRequired());
                columnMap.put(field.getColumnCode(), columnConfig);
            });
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getEmployeeItemCustomFields(String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        List<EdsCompanyCustomFieldsSettings> fields = companyCustomFieldsManager.getCompanyCustomFieldsByCategoryForListView(CustomFieldSection.EmployeeItemTable.name(), uuid);

        if (CollectionUtils.isNotEmpty(fields)) {
            fields.forEach(field -> {
                ColumnConfigs columnConfig = new ColumnConfigs();
                columnConfig.setCompanyCustomFieldID(field.getObjectID());
                columnConfig.setTitle(field.getFieldName());
                columnConfig.setCode(field.getColumnCode());
                columnConfig.setWidth(field.getColumnWidth());
                columnConfig.setAliasName(field.getAliasName());
                columnConfig.setDataType(field.getDataType());
                columnConfig.setUiType(field.getUiType());
                columnConfig.setRequired(field.getRequired());
                columnMap.put(field.getColumnCode(), columnConfig);
            });
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getItemTableCustomFields(String entityName, String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        List<EdsCompanyCustomFieldsSettings> fields = companyCustomFieldsManager.getCompanyCustomFieldsByCategoryForListView(entityName, uuid);

        if (CollectionUtils.isNotEmpty(fields)) {
            fields.forEach(field -> {
                ColumnConfigs columnConfig = new ColumnConfigs();
                columnConfig.setCompanyCustomFieldID(field.getObjectID());
                columnConfig.setTitle(field.getFieldName());
                columnConfig.setCode(field.getColumnCode());
                columnConfig.setWidth(field.getColumnWidth());
                columnConfig.setAliasName(field.getAliasName());
                columnConfig.setDataType(field.getDataType());
                columnConfig.setUiType(field.getUiType());
                columnConfig.setRequired(field.getRequired());
                columnMap.put(field.getColumnCode(), columnConfig);
            });
        }
        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getOpportunityStageHistory() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();

        columnMap.put("STAGE", new ColumnConfigs("STAGE", "STAGE", commonLocalizer.localize("stage", "Stage"), false));
        columnMap.put("NOTE", new ColumnConfigs("NOTE", "NOTE", commonLocalizer.localize("note", "Note"), false));
        columnMap.put(AMOUNT, new ColumnConfigs(AMOUNT, AMOUNT, commonLocalizer.localize("amount", "Amount"), false));
        columnMap.put("PROBABILITY", new ColumnConfigs("PROBABILITY", "PROBABILITY", commonLocalizer.localize("probability", "Probability"), false));
        columnMap.put("EXPECTED_REVENUE", new ColumnConfigs("EXPECTED_REVENUE", "EXPECTED_REVENUE", commonLocalizer.localize("expectedRevenue", "Expected Revenue"), false));
        columnMap.put("MODIFIED_BY", new ColumnConfigs("MODIFIED_BY", "MODIFIED_BY", commonLocalizer.localize("modifiedBy", "Modified By"), false));
        columnMap.put("MODIFIED_DATE", new ColumnConfigs("MODIFIED_DATE", "MODIFIED_DATE", commonLocalizer.localize("modifiedDate", "Modified date"), false));

        if (columnMap.get("STAGE") != null) {
            columnMap.get("STAGE").setSelected(true);
        }
        if (columnMap.get("NOTE") != null) {
            columnMap.get("NOTE").setSelected(true);
        }
        if (columnMap.get(AMOUNT) != null) {
            columnMap.get(AMOUNT).setSelected(true);
        }
        if (columnMap.get("PROBABILITY") != null) {
            columnMap.get("PROBABILITY").setSelected(true);
        }
        if (columnMap.get("EXPECTED_REVENUE") != null) {
            columnMap.get("EXPECTED_REVENUE").setSelected(true);
        }
        if (columnMap.get("MODIFIED_BY") != null) {
            columnMap.get("MODIFIED_BY").setSelected(true);
        }
        if (columnMap.get("MODIFIED_DATE") != null) {
            columnMap.get("MODIFIED_DATE").setSelected(true);
        }

        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> getAttachmentColumns() {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();

        columnMap.put("NAME", new ColumnConfigs("NAME", "NAME", commonLocalizer.localize("name", "Name"), false, true, true));
        columnMap.put(DESCRIPTION, new ColumnConfigs("DESCRIPTION", "DESCRIPTION", commonLocalizer.localize("description", "Description"), false, true, true));
        columnMap.put("DOCUMENT_ID", new ColumnConfigs("DOCUMENT_ID", "DOCUMENT_ID", commonLocalizer.localize("documentID", "Document ID"), false, true, true));
        columnMap.put("TYPE", new ColumnConfigs("TYPE", "TYPE", commonLocalizer.localize("type", "Type"), false, true, true));
        columnMap.put("CREATED_DATE", new ColumnConfigs("CREATED_DATE", "CREATED_DATE", commonLocalizer.localize("createdDate", "Created Date"), false, true, false));
        columnMap.put("FILE_SIZE", new ColumnConfigs("FILE_SIZE", "FILE_SIZE", commonLocalizer.localize("fileSize", "File Size"), false, true, false));
        columnMap.put("DOWNLOAD", new ColumnConfigs("DOWNLOAD", "DOWNLOAD", commonLocalizer.localize("download", "Download"), false, true, false));
        columnMap.put("REMOVE", new ColumnConfigs("REMOVE", "REMOVE", commonLocalizer.localize("delete", "Remove"), false, true, false));
        columnMap.put("ISSUED_DATE", new ColumnConfigs("ISSUED_DATE", "ISSUED_DATE", commonLocalizer.localize("issuedDate", "Issued Date"), false, false, true));
        columnMap.put("EXPIRY_DATE", new ColumnConfigs("EXPIRY_DATE", "EXPIRY_DATE", commonLocalizer.localize("expiryDate", "Expiry Date"), false, false, true));
        columnMap.put("ENABLE_REMINDER", new ColumnConfigs("ENABLE_REMINDER", "ENABLE_REMINDER", commonLocalizer.localize("enableReminder", "Enable Reminder"), false, false, true));

        return columnMap;
    }

    private LinkedHashMap<String, ColumnConfigs> initializeConfiguredColumnsNew(ColumnConfigs[] configs, LinkedHashMap<String, ColumnConfigs> columnMap) {

        if (configs != null && configs.length > 0) {
            columnMap.forEach((k, v) -> {
                if (!k.equals(WAREHOUSE)) {
                    v.setSelected(false);
                }
            });

            int order = 0;
            for (ColumnConfigs config : configs) {
                if (columnMap.get(config.getCode()) != null) {
                    columnMap.get(config.getCode()).setSelected(config.isSelected());
                    columnMap.get(config.getCode()).setWidth(config.getWidth());
                    columnMap.get(config.getCode()).setRequired(config.isRequired());
                    columnMap.get(config.getCode()).setDisabled(config.isDisabled());
                    columnMap.get(config.getCode()).setHasDefault(config.hasDefault());
                    columnMap.get(config.getCode()).setTitle(config.getTitle());
                    columnMap.get(config.getCode()).setChanged(config.isChanged());
                    columnMap.get(config.getCode()).setMinValue(config.getMinValue());
                    columnMap.get(config.getCode()).setAllowedRoles(config.getAllowedRoles());
                    columnMap.get(config.getCode()).setAllowedRolesView(config.getAllowedRolesView());
                    columnMap.get(config.getCode()).setAllowedRolesDisabled(config.getAllowedRolesDisabled());
                    columnMap.get(config.getCode()).setOrder(++order);
                }
            }

            columnMap.forEach((k, v) -> {
                if (v.getOrder() == 0) {
                    v.setOrder(999);
                }
            });

            return columnMap.entrySet().stream().sorted(Comparator.comparing(m -> m.getValue().getOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        return columnMap;
    }

    protected LinkedHashMap<String, ColumnConfigs> initializeConfiguredColumns(ColumnConfigs[] configs, LinkedHashMap<String, ColumnConfigs> columnMap) {

        if (configs != null && configs.length > 0) {
            columnMap.forEach((k, v) -> v.setSelected(false));

            int order = 0;
            for (ColumnConfigs config : configs) {
                if (columnMap.get(config.getCode()) != null) {
                    columnMap.get(config.getCode()).setSelected(true);
                    columnMap.get(config.getCode()).setOrder(++order);
                }
            }

            columnMap.forEach((k, v) -> {
                if (v.getOrder() == 0) {
                    v.setOrder(999);
                }
            });

            return columnMap.entrySet().stream().sorted(Comparator.comparing(m -> m.getValue().getOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        return columnMap;
    }

    public ItemTableSettingsItem getTableSettingsColumnConfigsNew(ItemTableEnum section, String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = switch (section) {
            case SALE_INVOICE_ITEM -> getAllSIIColumns();
            case CREDIT_NOTE_ITEM -> getAllCNColumns();
            case PURCHASE_INVOICE_ITEM -> getAllPIIColumns();
            case DEBIT_NOTE_ITEM -> getAllDNColumns();
            case SALE_QUOTE_ITEM -> getAllSQIColumns(false);
            case SALE_ORDER_ITEM -> getAllSQIColumns(true);
            case PURCHASE_ORDER_ITEM -> getAllPOIColumns();
            case EXPENSE_CLAIM_ITEM -> getAllERIColumns();
            case OPPORTUNITY_SUB_ITEM, CLIENT_ITEM, SUPPLIER_ITEM, BILL_OF_MATERIALS_ITEM -> getAllCRMSubIColumns(section);
            case RFQ_ITEM -> getAllRFQIColumns();
            case RENTAL_ORDER_ITEM -> getAllRentalOrderColumns();
            case PICKLIST -> getAllPickListColumns();
            case RFP_ITEM -> getAllRFPIColumns();
            case CUSTOM_FORM -> getCustomFormItemCustomFields(uuid);
            case OPPORTUNITY_CUSTOM_ITEM -> getOpportunityItemCustomFields(uuid);
            case EMPLOYEE_CUSTOM_ITEM -> getEmployeeItemCustomFields(uuid);
            case MANUAL_JOURNAL_ITEM -> getAllManualJournalIColumns();
            case BANK_PAYMENT_ITEM -> getAllBankPaymentIColumns();
            case CASH_PAYMENT_ITEM -> getAllCashPaymentIColumns();
            case BANK_RECEIPT_ITEM -> getAllBankReceiptIColumns();
            case CASH_RECEIPT_ITEM -> getAllCashReceiptIColumns();
            case OPPORTUNITY_STAGE_HISTORY -> getOpportunityStageHistory();
            case CLIENT_FORM_ATTACHMENTS, PRODUCT_ATTACHMENTS, GENERAL_ATTACHMENTS, COMPANY_DOC_ATTACHMENTS -> getAttachmentColumns();
            case LEAD_ITEM -> getAllLeadIColumns();
            case ADDITIONAL_PAYMENT_ITEM -> getAllAdditionalPaymentIColumns();
            case PLACEMENT_CUSTOM_ITEM -> getItemTableCustomFields(CustomFieldSection.PlacementItemTable.name(), uuid);
            case PROJECT_CUSTOM_ITEM -> getItemTableCustomFields(CustomFieldSection.ProjectItemTable.name(), uuid);
            case VACANCY_CUSTOM_ITEM -> getItemTableCustomFields(CustomFieldSection.VacancyItemTable.name(), uuid);
            case ROTATION_ITEM_TABLE -> getAllRotationIColumns();
            case EXPERIENCE_ITEM_TABLE -> getAllExperienceIColumns();
            case CANDIDATE_CUSTOM_ITEM -> getItemTableCustomFields(CustomFieldSection.CandidateCustomItemTable.name(), uuid);
            default -> new LinkedHashMap<>();
        };

        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + section.getTitle() + "_" + SecurityContext.getCompanyID();

        if (StringUtils.isEmpty(RedisClient.getKey(key))) {
            if (!(ItemTableEnum.CUSTOM_FORM.equals(section) || ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM.equals(section) || ItemTableEnum.EMPLOYEE_CUSTOM_ITEM.equals(section) || ItemTableEnum.PLACEMENT_CUSTOM_ITEM.equals(section) || ItemTableEnum.PROJECT_CUSTOM_ITEM.equals(section))) {
                EdsItemTableSettings edsItemTableSettings = new EdsItemTableSettings();
                edsItemTableSettings.setSection(section);

                Gson gson = new Gson();
                ColumnConfigs[] columnConfigs = columnMap.values().toArray(new ColumnConfigs[0]);
                edsItemTableSettings.setSettingsJSONData(gson.toJson(columnConfigs));
                itemTableSettingsManager.create(edsItemTableSettings);

                RedisClient.setKey(key, columnConfigs, ColumnConfigs[].class);
            }
        }


        ItemTableSettingsItem item = new ItemTableSettingsItem();
        ColumnConfigs[] configs = new ColumnConfigs[0];
        LinkedList<ColumnConfigs> clist = new LinkedList<>();
        if (uuid != null) {
            EdsCFItemTableSetting its = cfItemTableSettingmanager.findByUUID(uuid);
            if (its != null && StringUtils.isNotBlank(its.getSettingsJSONData())) {
                Gson gson = new Gson();
                ColumnConfigs[] columns = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
                for (ColumnConfigs column : columns) {
                    if (columnMap != null && columnMap.get(column.getCode()) != null) {
                        ColumnConfigs columnConfigs = columnMap.get(column.getCode());
                        column.setCompanyCustomFieldID(columnConfigs.getCompanyCustomFieldID());
                        column.setTitle(commonLocalizer.localize(columnConfigs.getCode(), columnConfigs.getTitle()));
                        column.setUiType(columnConfigs.getUiType());
                        column.setAliasName(columnConfigs.getAliasName());
                        column.setDataType(columnConfigs.getDataType());
                        column.setRequired(columnConfigs.isRequired());

                        clist.add(column);
                    }
                }
                configs = clist.toArray(new ColumnConfigs[0]);
            }
        } else {
            configs = getColumnConfigsNew(section);
        }

        columnMap = initializeConfiguredColumnsNew(configs, columnMap);

        Collection<ColumnConfigs> allColumns;

        if (uuid != null && clist.size() == 0) {
            allColumns = columnMap.values().stream()
                    .peek(x -> x.setSelected(true))
                    .collect(Collectors.toList());
        } else {
            allColumns = columnMap.values();
        }
        item.setAllColumns(allColumns.toArray(new ColumnConfigs[0]));

        if (ItemTableEnum.SALE_INVOICE_ITEM.equals(section)) {
            Arrays.stream(item.getAllColumns())
                    .filter(columnConfig -> "ACCOUNT".equals(columnConfig.getCode()))
                    .forEach(columnConfig -> columnConfig.setRequired(true));
        } else if (ItemTableEnum.SALE_QUOTE_ITEM.equals(section) || ItemTableEnum.SALE_ORDER_ITEM.equals(section)) {
            Arrays.stream(item.getAllColumns())
                    .filter(columnConfig -> "ACCOUNT".equals(columnConfig.getCode()))
                    .forEach(columnConfig -> columnConfig.setRequired(false));
        }

        return item;
    }

    public ItemTableSettingsItem getTableSettingsColumnConfigs(ItemTableEnum section, String uuid) {
        LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
        switch (section) {
            case SALE_INVOICE_ITEM:
                columnMap = getAllSIIColumns();
                break;
            case CREDIT_NOTE_ITEM:
                columnMap = getAllCNColumns();
                break;
            case PURCHASE_INVOICE_ITEM:
                columnMap = getAllPIIColumns();
                break;
            case DEBIT_NOTE_ITEM:
                columnMap = getAllDNColumns();
                break;
            case SALE_QUOTE_ITEM:
                columnMap = getAllSQIColumns(false);
            case SALE_ORDER_ITEM:
                columnMap = getAllSQIColumns(true);
                break;
            case PURCHASE_ORDER_ITEM:
                columnMap = getAllPOIColumns();
                break;
            case EXPENSE_CLAIM_ITEM:
                columnMap = getAllERIColumns();
                break;
            case OPPORTUNITY_SUB_ITEM:
            case CLIENT_ITEM:
            case SUPPLIER_ITEM:
            case BILL_OF_MATERIALS_ITEM:
                columnMap = getAllCRMSubIColumns(section);
                break;
            case RFQ_ITEM:
                columnMap = getAllRFQIColumns();
                break;
            case RENTAL_ORDER_ITEM:
                columnMap = getAllRentalOrderColumns();
                break;
            case RFP_ITEM:
                columnMap = getAllRFPIColumns();
                break;
            case CUSTOM_FORM:
                columnMap = getCustomFormItemCustomFields(uuid);
                break;
            case OPPORTUNITY_CUSTOM_ITEM:
                columnMap = getOpportunityItemCustomFields(uuid);
                break;
            case EMPLOYEE_CUSTOM_ITEM:
                columnMap = getEmployeeItemCustomFields(uuid);
                break;
            case PLACEMENT_CUSTOM_ITEM:
                columnMap = getItemTableCustomFields(CustomFieldSection.PlacementItemTable.name(), uuid);
                break;
            case PROJECT_CUSTOM_ITEM:
                columnMap = getItemTableCustomFields(CustomFieldSection.ProjectItemTable.name(), uuid);
                break;
            case VACANCY_CUSTOM_ITEM:
                columnMap = getItemTableCustomFields(CustomFieldSection.VacancyItemTable.name(), uuid);
                break;
            case MANUAL_JOURNAL_ITEM:
                columnMap = getAllManualJournalIColumns();
                break;
            case BANK_PAYMENT_ITEM:
                columnMap = getAllBankPaymentIColumns();
                break;
            case CASH_PAYMENT_ITEM:
                columnMap = getAllCashPaymentIColumns();
                break;
            case BANK_RECEIPT_ITEM:
                columnMap = getAllBankReceiptIColumns();
                break;
            case CASH_RECEIPT_ITEM:
                columnMap = getAllCashReceiptIColumns();
                break;
            case LEAD_ITEM:
                columnMap = getAllLeadIColumns();
                break;
            case ADDITIONAL_PAYMENT_ITEM:
                columnMap = getAllAdditionalPaymentIColumns();
                break;
            case ROTATION_ITEM_TABLE:
                columnMap = getAllRotationIColumns();
                break;
            case EXPERIENCE_ITEM_TABLE:
                columnMap = getAllExperienceIColumns();
                break;
            case CANDIDATE_CUSTOM_ITEM:
                columnMap = getItemTableCustomFields(CustomFieldSection.CandidateCustomItemTable.name(), uuid);
                break;
        }

        ColumnConfigs[] configs = new ColumnConfigs[0];
        LinkedList<ColumnConfigs> clist = new LinkedList<>();
        if (uuid != null) {
            EdsCFItemTableSetting its = cfItemTableSettingmanager.findByUUID(uuid);
            if (its != null && StringUtils.isNotBlank(its.getSettingsJSONData())) {
                Gson gson = new Gson();
                ColumnConfigs[] columns = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
                for (ColumnConfigs column : columns) {
                    if (columnMap != null && columnMap.get(column.getCode()) != null) {
                        column.setTitle(commonLocalizer.localize(column.getCode(), column.getTitle()));
                        clist.add(column);
                    }
                }
                configs = clist.toArray(new ColumnConfigs[]{});
            }
        } else {
            configs = getColumnConfigs(section);
        }
        columnMap = initializeConfiguredColumns(configs, columnMap);

        ItemTableSettingsItem item = new ItemTableSettingsItem();
        Collection<ColumnConfigs> allColumns;

        if (uuid != null && clist.size() == 0) {
            allColumns = columnMap.values().stream()
                    .peek(x -> x.setSelected(true))
                    .collect(Collectors.toList());
        } else {
            allColumns = columnMap.values();
        }
        item.setAllColumns(allColumns.toArray(new ColumnConfigs[]{}));

        if (ItemTableEnum.SALE_INVOICE_ITEM.equals(section)) {
            Arrays.stream(item.getAllColumns())
                    .filter(columnConfig -> "ACCOUNT".equals(columnConfig.getCode()))
                    .forEach(columnConfig -> columnConfig.setRequired(true));
        } else if (ItemTableEnum.SALE_QUOTE_ITEM.equals(section) || ItemTableEnum.SALE_ORDER_ITEM.equals(section)) {
            Arrays.stream(item.getAllColumns())
                    .filter(columnConfig -> "ACCOUNT".equals(columnConfig.getCode()))
                    .forEach(columnConfig -> columnConfig.setRequired(false));
        }

        return item;
    }

    @Override
    public ColumnConfigs[] getColumnConfigsNew(ItemTableEnum section) {

        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + section.getTitle() + "_" + SecurityContext.getCompanyID();
        ColumnConfigs[] settingsJSONData = RedisClient.getKey(key, ColumnConfigs[].class);

        if (settingsJSONData == null || settingsJSONData != null && settingsJSONData.length == 0) {
            Gson gson = new Gson();
            EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(section);
            if (its != null) settingsJSONData = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
        }

        LinkedHashMap<String, ColumnConfigs> columnMap = null;
        LinkedList<ColumnConfigs> clist = new LinkedList<>();

        if (settingsJSONData != null && settingsJSONData.length > 0) {
            switch (section) {
                case SALE_INVOICE_ITEM -> columnMap = getAllSIIColumns();
                case CREDIT_NOTE_ITEM -> columnMap = getAllCNColumns();
                case SALE_QUOTE_ITEM -> columnMap = getAllSQIColumns(false);
                case SALE_ORDER_ITEM -> columnMap = getAllSQIColumns(true);
                case PURCHASE_INVOICE_ITEM -> columnMap = getAllPIIColumns();
                case DEBIT_NOTE_ITEM -> columnMap = getAllDNColumns();
                case PURCHASE_ORDER_ITEM -> columnMap = getAllPOIColumns();
                case OPPORTUNITY_SUB_ITEM, BILL_OF_MATERIALS_ITEM, CLIENT_ITEM, SUPPLIER_ITEM ->
                        columnMap = getAllCRMSubIColumns(section);
                case EXPENSE_CLAIM_ITEM -> columnMap = getAllERIColumns();
                case RFQ_ITEM -> columnMap = getAllRFQIColumns();
                case RENTAL_ORDER_ITEM -> columnMap = getAllRentalOrderColumns();
                case RFP_ITEM -> columnMap = getAllRFPIColumns();
                case OPPORTUNITY_STAGE_HISTORY -> columnMap = getOpportunityStageHistory();
                case CLIENT_FORM_ATTACHMENTS, PRODUCT_ATTACHMENTS, GENERAL_ATTACHMENTS, COMPANY_DOC_ATTACHMENTS ->
                        columnMap = getAttachmentColumns();
                case MANUAL_JOURNAL_ITEM -> columnMap = getAllManualJournalIColumns();
                case BANK_PAYMENT_ITEM -> columnMap = getAllBankPaymentIColumns();
                case CASH_PAYMENT_ITEM -> columnMap = getAllCashPaymentIColumns();
                case BANK_RECEIPT_ITEM -> columnMap = getAllBankReceiptIColumns();
                case CASH_RECEIPT_ITEM -> columnMap = getAllCashReceiptIColumns();
                case LEAD_ITEM -> columnMap = getAllLeadIColumns();
                case ADDITIONAL_PAYMENT_ITEM -> columnMap = getAllAdditionalPaymentIColumns();
                case ROTATION_ITEM_TABLE -> columnMap = getAllRotationIColumns();
                case EXPERIENCE_ITEM_TABLE -> columnMap = getAllExperienceIColumns();
                case PICKLIST -> columnMap = getAllPickListColumns();
            }

            ColumnConfigs[] columns = settingsJSONData;
            for (ColumnConfigs column : columns) {
                if (columnMap != null && columnMap.get(column.getCode()) != null) {
                    ColumnConfigs defaultConfigs = columnMap.get(column.getCode());
                    if (!column.isChanged()) {
                        column.setTitle(defaultConfigs.getTitle());
                        if (section == ItemTableEnum.CLIENT_FORM_ATTACHMENTS
                                || section == ItemTableEnum.PRODUCT_ATTACHMENTS
                                || section == ItemTableEnum.GENERAL_ATTACHMENTS
                                || section == ItemTableEnum.COMPANY_DOC_ATTACHMENTS){
                            column.setTitle(column.isChanged() ? column.getTitle() : commonLocalizer.localize(column.getCode(), column.getTitle()));
                        }
                    } else {
                        column.setTitle(column.getTitle());
                    }
                    column.setCode(defaultConfigs.getCode());
                    column.setDataType(defaultConfigs.getDataType());
                    column.setAliasName(defaultConfigs.getAliasName());
                    column.setUiType(defaultConfigs.getUiType());
                    if (column.getCompanyCustomFieldID() != null) {
                        column.setRequired(defaultConfigs.isRequired());
                        column.setTitle(defaultConfigs.getTitle());
                    }
                    clist.add(column);
                }
            }
        } else if (ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section) || ItemTableEnum.BILL_OF_MATERIALS_ITEM.equals(section)) {
            columnMap = getDefaultColumnsForCRMSubItems(section);

            columnMap.values().forEach(column -> {
                column.setTitle(commonLocalizer.localize(column.getCode(), column.getTitle()));
                clist.add(column);
            });
        }
        return clist.toArray(new ColumnConfigs[0]);
    }

    @Override
    public ColumnConfigs[] getColumnConfigs(ItemTableEnum section) {
        return getColumnConfigs(section, false);
    }

    @Override
    public ColumnConfigs[] getColumnConfigs(ItemTableEnum section, boolean isSettings) {
        return getColumnConfigs(section, isSettings, false);
    }

    @Override
    public List<CustomFieldListTO> getColumnConfigsForAPI(ItemTableEnum section) {
        ColumnConfigs[] sections = getColumnConfigs(section, false);
        List<CustomFieldListTO> columns = new ArrayList<>();
        for (ColumnConfigs col : sections) {
            CustomFieldListTO column = new CustomFieldListTO();
            column.setId(col.getCompanyCustomFieldID());
            column.setRequired(col.isRequired());
            column.setTitle(col.getTitle());
            column.setSystem(col.getCompanyCustomFieldID() == null);
            column.setSelected(col.isSelected());
            column.setCode(col.getCode());
            if ("Text".equalsIgnoreCase(col.getDataType()) && ("TextBox".equalsIgnoreCase(col.getUiType()) || "TextArea".equalsIgnoreCase(col.getUiType()))) {
                column.setField_type(CustomFieldCategoryEnum.TEXT_INPUT.getCategory());
            } else if ("Date".equalsIgnoreCase(col.getDataType())) {
                column.setField_type(CustomFieldCategoryEnum.DATE.getCategory());
            } else if ("Number".equalsIgnoreCase(col.getDataType()) && "TextBox".equalsIgnoreCase(col.getUiType())) {
                column.setField_type(CustomFieldCategoryEnum.NUMBER_INPUT.getCategory());
            } else if ("File Upload".equalsIgnoreCase(col.getDataType())) {
                column.setField_type(CustomFieldCategoryEnum.FILE_UPLOAD.getCategory());
            } else if (("Text".equalsIgnoreCase(col.getDataType()) || "Number".equalsIgnoreCase(col.getDataType())) && ("RadioButton".equalsIgnoreCase(col.getUiType()) || "DropDown".equalsIgnoreCase(col.getUiType()) || "LookUp".equalsIgnoreCase(col.getUiType()))) {
                column.setField_type(CustomFieldCategoryEnum.CATEGORY_CHOOSE.getCategory());
            } else if (("Text".equalsIgnoreCase(col.getDataType()) || "Number".equalsIgnoreCase(col.getDataType())) && "CheckBox".equalsIgnoreCase(col.getUiType())) {
                column.setField_type(CustomFieldCategoryEnum.MULTIPLY_CHOOSE.getCategory());
            }
            columns.add(column);
        }
        return columns;
    }

    @Override
    public ColumnConfigs[] getColumnConfigs(ItemTableEnum section, boolean isSettings, boolean fromView) {
        LinkedList<ColumnConfigs> configList = new LinkedList<>();
        if (section != null) {
            EdsUser user = userManager.getUser();
            String key = CacheConstants.ITEM_TABLE_SECTION + "_" + section.getTitle() + "_" + SecurityContext.getCompanyID();
            ColumnConfigs[] settingsJSONData = new ColumnConfigs[0];
            try {
                settingsJSONData = RedisClient.getKey(key, ColumnConfigs[].class);
            } catch (Exception e) {
                settingsJSONData = null;
                e.printStackTrace();
            }

            if (settingsJSONData == null || settingsJSONData.length == 0) {
                EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(section);
                if (its != null && its.getSettingsJSONData() != null && !its.getSettingsJSONData().isEmpty()) {
                    settingsJSONData = new Gson().fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
                }
            }
            LinkedHashMap<String, ColumnConfigs> columnMap = null;

            if (settingsJSONData != null && settingsJSONData.length > 0) {
                switch (section) {
                    case SALE_INVOICE_ITEM -> columnMap = getAllSIIColumns();
                    case CREDIT_NOTE_ITEM -> columnMap = getAllCNColumns();
                    case SALE_QUOTE_ITEM -> columnMap = getAllSQIColumns(false);
                    case SALE_ORDER_ITEM -> columnMap = getAllSQIColumns(true);
                    case PURCHASE_INVOICE_ITEM -> columnMap = getAllPIIColumns();
                    case DEBIT_NOTE_ITEM -> columnMap = getAllDNColumns();
                    case PURCHASE_ORDER_ITEM -> columnMap = getAllPOIColumns();
                    case OPPORTUNITY_SUB_ITEM, BILL_OF_MATERIALS_ITEM, CLIENT_ITEM, SUPPLIER_ITEM ->
                            columnMap = getAllCRMSubIColumns(section);
                    case EXPENSE_CLAIM_ITEM -> columnMap = getAllERIColumns();
                    case RFQ_ITEM -> columnMap = getAllRFQIColumns();
                    case RENTAL_ORDER_ITEM -> columnMap = getAllRentalOrderColumns();
                    case RFP_ITEM -> columnMap = getAllRFPIColumns();
                    case OPPORTUNITY_STAGE_HISTORY -> columnMap = getOpportunityStageHistory();
                    case CLIENT_FORM_ATTACHMENTS, PRODUCT_ATTACHMENTS, GENERAL_ATTACHMENTS, COMPANY_DOC_ATTACHMENTS ->
                            columnMap = getAttachmentColumns();
                    case MANUAL_JOURNAL_ITEM -> columnMap = getAllManualJournalIColumns();
                    case BANK_PAYMENT_ITEM -> columnMap = getAllBankPaymentIColumns();
                    case CASH_PAYMENT_ITEM -> columnMap = getAllCashPaymentIColumns();
                    case BANK_RECEIPT_ITEM -> columnMap = getAllBankReceiptIColumns();
                    case CASH_RECEIPT_ITEM -> columnMap = getAllCashReceiptIColumns();
                    case LEAD_ITEM -> columnMap = getAllLeadIColumns();
                    case ADDITIONAL_PAYMENT_ITEM -> columnMap = getAllAdditionalPaymentIColumns();
                    case ROTATION_ITEM_TABLE -> columnMap = getAllRotationIColumns();
                    case EXPERIENCE_ITEM_TABLE -> columnMap = getAllExperienceIColumns();
                    case GROUP_PLACEMENT_ITEM_TABLE -> columnMap = getAllGroupPlacementIColumns();
                }

                ColumnConfigs[] newConfigs = getColumnConfigsNew(section);
                Map<String, ColumnConfigs> newConfigMap = new HashMap<>();
                if (newConfigs != null) {
                    for (ColumnConfigs c : newConfigs) {
                        newConfigMap.put(c.getCode(), c);
                    }
                }

                for (ColumnConfigs column : settingsJSONData) {
                    boolean rolePermission = fromView
                            ? user == null || column.getAllowedRolesView() == null || column.getAllowedRolesView().isEmpty()
                            || user.hasEitherRoles(column.getAllowedRolesView().toArray(new Integer[0]))
                            : user == null || column.getAllowedRoles() == null || column.getAllowedRoles().isEmpty()
                            || user.hasEitherRoles(column.getAllowedRoles().toArray(new Integer[0]));

                    boolean hasPermission = isSettings || rolePermission;

                    ColumnConfigs columnConfigs = newConfigMap.get(column.getCode());
                    if (columnConfigs == null && columnMap != null) {
                        columnConfigs = columnMap.get(column.getCode());
                    }
                    if (columnConfigs != null && hasPermission) {
                        if (!columnConfigs.isSelected()) {
                            continue;
                        }
                        column.setCode(columnConfigs.getCode());
                        column.setDataType(columnConfigs.getDataType());
                        column.setUiType(columnConfigs.getUiType());
                        column.setAliasName(columnConfigs.getAliasName());
                        if (column.isChanged()) {
                            column.setTitle(column.getTitle());
                        } else {
                            column.setTitle(commonLocalizer.localize(column.getCode(),
                                    commonLocalizer.localize(column.getTitle().replace(" ", "_").toLowerCase(), column.getTitle())));
                        }
                        if (column.getAllowedRolesDisabled() != null && !column.getAllowedRolesDisabled().isEmpty()) {
                            if (user != null && user.hasEitherRoles(column.getAllowedRolesDisabled().toArray(new Integer[]{}))) {
                                column.setDisabled(false);
                            }
                        }
                        configList.add(column);
                    }
                }
            } else if (ItemTableEnum.OPPORTUNITY_SUB_ITEM.equals(section) || ItemTableEnum.BILL_OF_MATERIALS_ITEM.equals(section)) {
                columnMap = getDefaultColumnsForCRMSubItems(section);

                for (ColumnConfigs column : columnMap.values()) {
                    column.setTitle(commonLocalizer.localize(column.getCode(), column.getTitle()));
                    configList.add(column);
                }
            }
        }
        if (configList !=null && !configList.isEmpty()) {
            int width = 100 / configList.size();
            for (ColumnConfigs cc : configList) {
                if (cc.getWidth() == null) {
                    cc.setWidth(width);
                }
            }
        }
        return configList.toArray(new ColumnConfigs[]{});
    }

    @Override
    public void saveColumnConfigs(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid) {
        if (uuid != null) {
            EdsCFItemTableSetting its = cfItemTableSettingmanager.findByUUID(uuid);

            if (its == null) {
                its = new EdsCFItemTableSetting();
            }
            its.setUuid(uuid);

            Gson gson = new Gson();
            its.setSettingsJSONData(gson.toJson(columnConfigs));

            cfItemTableSettingmanager.createOrUpdate(its);
            return;
        }
        EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(section);

        if (its == null) {
            its = new EdsItemTableSettings();
        }
        its.setSection(section);

        Gson gson = new Gson();
        its.setSettingsJSONData(gson.toJson(columnConfigs));
        itemTableSettingsManager.createOrUpdate(its);

        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + section.getTitle() + "_" + SecurityContext.getCompanyID();
        RedisClient.removeKey(key);
        RedisClient.setKey(key, columnConfigs, ColumnConfigs[].class);

    }

    @Override
    public void saveColumnConfigsNew(ItemTableEnum section, ColumnConfigs[] columnConfigs, String uuid, SelectItem entity, SelectItem relation) {
        if (uuid != null) {
            EdsCFItemTableSetting its = cfItemTableSettingmanager.findByUUID(uuid);

            if (its == null) {
                its = new EdsCFItemTableSetting();
            }
            its.setUuid(uuid);
            its.setEntity(entity != null ? entity.getName() : null);
            its.setEntityId(entity != null ? entity.getId() : null);
            its.setRelationField(relation != null ? relation.getName() : null);
            its.setRelationFieldId(relation != null ? relation.getId() : null);

            Gson gson = new Gson();
            its.setSettingsJSONData(gson.toJson(columnConfigs));

            cfItemTableSettingmanager.createOrUpdate(its);
            return;
        }

        for (ColumnConfigs cf : columnConfigs) {
            if (cf != null && cf.getCompanyCustomFieldID() != null) {
                EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.get(cf.getCompanyCustomFieldID());
                if (companyCustomFieldsSettings != null && companyCustomFieldsSettings.getAllowedRoles() != null) {
                    ArrayList<Integer> rolesIds = new ArrayList<>();
                    for (EdsRole role : companyCustomFieldsSettings.getAllowedRoles()) {
                        rolesIds.add(role.getObjectID());
                    }
                    cf.setAllowedRoles(rolesIds);
                } else {
                    cf.setAllowedRoles(null);
                }
            }
        }

        EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(section);
        if (its != null && its.getSettingsJSONData() != null) {
            ColumnConfigs[] dbConfigs = new Gson().fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);

            for (ColumnConfigs incoming : columnConfigs) {
                for (ColumnConfigs db : dbConfigs) {
                    if (incoming.getCode().equals(db.getCode())) {
                        if (db.isChanged() && !db.getTitle().equals(incoming.getTitle())) {
                            incoming.setTitle(db.getTitle());
                            incoming.setChanged(true);
                            incoming.setAliasName(db.getAliasName());
                            incoming.setMinValue(db.getMinValue());
                        }
                    }
                }
            }
        }

        Gson gson = new Gson();
        String finalJson = gson.toJson(columnConfigs);

        if (its == null) {
            its = new EdsItemTableSettings();
            its.setSection(section);
        }
        its.setSettingsJSONData(finalJson);
        itemTableSettingsManager.createOrUpdate(its);

        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + section.getTitle() + "_" + SecurityContext.getCompanyID();
        RedisClient.removeKey(key);
        RedisClient.setKey(key, columnConfigs, ColumnConfigs[].class);
    }

    @Override
    public HashMap<String, ColumnConfigs[]> getColumnConfigs(String formID) {
        EdsUser user = userManager.getUser();
        HashMap<String, ColumnConfigs[]> map = new HashMap<>();

        List<EdsCFItemTableSetting> itsList = cfItemTableSettingmanager.findByFormId(formID);
        List<EdsCompanyCustomFieldsSettings> fields = null;

        if (LayoutRPC.OPPORTUNITY_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.OpportunityItemTable.name());
        } else if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.EmployeeItemTable.name());
        } else if (LayoutRPC.PLACEMENT_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.PlacementItemTable.name());
        } else if (LayoutRPC.PROJECT_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.ProjectItemTable.name());
        } else if (LayoutRPC.VACANCY_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.VacancyItemTable.name());
        } else if (LayoutRPC.CANDIDATE_FORM.equals(formID)) {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.CandidateCustomItemTable.name());
        } else {
            fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.CustomFormItemTable.name());
        }

        if (CollectionUtils.isNotEmpty(itsList)) {
            List<EdsCompanyCustomFieldsSettings> finalFields = fields;
            EdsUser loggedUser = userManager.getUser();
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(loggedUser);
            itsList.forEach(its -> {
                LinkedList<ColumnConfigs> clist = new LinkedList<>();
                LinkedHashMap<String, ColumnConfigs> columnMap = new LinkedHashMap<>();
                //columnMap.put(PRODUCT, new ColumnConfigs(PRODUCT, commonLocalizer.localize(PRODUCT, "Item"), true));
                //columnMap.put(DESCRIPTION, new ColumnConfigs(DESCRIPTION, commonLocalizer.localize(DESCRIPTION, "Description"), false));
                if (CollectionUtils.isNotEmpty(finalFields)) {
                    finalFields.stream()
                            .filter(x -> x.getEntityCategoryName().equals(its.getUuid()))
                            .forEach(field ->
                                    columnMap.put(field.getColumnCode(), new ColumnConfigs(field.getColumnCode(), field.getFieldNameLocalization(userSettings.getInternationalization()), field.getRequired(), field.getColumnWidth()))
                            );
                }
                if (StringUtils.isNotBlank(its.getSettingsJSONData())) {
                    Gson gson = new Gson();
                    ColumnConfigs[] columns = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
                    for (ColumnConfigs column : columns) {

                        EdsCompanyCustomFieldsSettings setting = companyCustomFieldsManager.get(column.getCompanyCustomFieldID());
                        HashSet<EdsRole> roles = new HashSet<>();
                        if (setting != null && !setting.getAllowedRoles().isEmpty()) {
                            roles.addAll(setting.getAllowedRoles());
                        }

                        if (columnMap.get(column.getCode()) != null && (user == null || user.hasEitherRoles(roles.toArray(new EdsRole[]{})) || roles.isEmpty())) {
                            if (setting != null && setting.getCustomFormlocalization() != null) {
                                column.setTitle(setting.getFieldNameLocalization(userSettings.getInternationalization()));
                            }
                            clist.add(column);
                        }
                    }
                } else {
                    columnMap.forEach((key, value) -> clist.add(value));
                }
                map.put(its.getUuid(), clist.toArray(new ColumnConfigs[0]));
            });
        }
        return map;
    }

    @Override
    public ArrayList<CustomFormItem> getCustomFormItems() {
        HashMap<String, HashMap<Integer, SelectItem>> cfItemTableEntityMap = new HashMap<>();
        List<EdsCustomForm> customFormList = customFormManager.list(new ListingFilterParameter());
        if (customFormList.size() == 0) {
            return null;
        }

        ArrayList<CustomFormItem> list = new ArrayList<>();
        for (EdsCustomForm customForm : customFormList) {
            HashMap<Integer, SelectItem> cfItemTableMap = new HashMap<>();
            if (customForm != null) {
                CustomFormItem item = new CustomFormItem();

                item.setObjectId(customForm.getObjectID());
                item.setName(customForm.getName());
                if (customForm.getProperty() != null) {
                    item.setPlural(customForm.getProperty().getPlural());
                    item.setShortName(customForm.getProperty().getShortcut());
                    item.setContext(customForm.getProperty().getModuleCode());
                    item.setCustom(customForm.getProperty().getCustom());
                }
                List<EdsCFItemTableSetting> edsCFItemTableSetting = cfItemTableSettingmanager.findByFormId(customForm.getFormID());
                if (edsCFItemTableSetting != null && edsCFItemTableSetting.size() > 0) {
                    SelectItem[] selectItems = new SelectItem[edsCFItemTableSetting.size()];
                    int i = 0;
                    for (EdsCFItemTableSetting itemTableSetting : edsCFItemTableSetting) {

                        if (itemTableSetting != null) {

                            SelectItem itemTable = new SelectItem();
                            itemTable.setId(itemTableSetting.getObjectID());
                            itemTable.setName(itemTableSetting.getName());
                            itemTable.setDescription(itemTableSetting.getUuid());
                            itemTable.setItemTableRelation(itemTableSetting.getRelationField());
                            itemTable.setItemTableRelationId(itemTableSetting.getRelationFieldId());
                            itemTable.setItemTableEntity(itemTableSetting.getEntity());
                            itemTable.setItemTableEntityId(itemTableSetting.getEntityId());
                            item.setFormId(customForm.getFormID());
                            selectItems[i] = itemTable;
                            i++;
                            cfItemTableMap.put(itemTableSetting.getObjectID(), itemTable);
                        }
                    }
                    cfItemTableEntityMap.put(customForm.getFormID(), cfItemTableMap);
                    item.setCfItemTableEntityMap(cfItemTableEntityMap);
                    item.setTableArray(selectItems);
                }
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public ArrayList<SelectItem> getCustomFormItemByFormID(String formID, String itemTableName) {
        ArrayList<SelectItem> list = new ArrayList<>();


        EdsCustomForm customFormList = customFormManager.findByFormID(formID);
        if (customFormList == null) {
            return null;
        }
        SelectItem customForm = new SelectItem();
        customForm.setId(customFormList.getObjectID());
        customForm.setName(customFormList.getName());
        list.add(customForm);

        List<EdsCFItemTableSetting> edsCFItemTableSetting = cfItemTableSettingmanager.findByFormId(formID);

        if (edsCFItemTableSetting != null && edsCFItemTableSetting.size() > 0) {
            for (EdsCFItemTableSetting itemTableSetting : edsCFItemTableSetting) {

                if (itemTableSetting.getCustomForm() != null && itemTableSetting.getName().equals(itemTableName)) {

                    SelectItem itemTable = new SelectItem();
                    itemTable.setId(itemTableSetting.getObjectID());
                    itemTable.setName(itemTableSetting.getName());
                    itemTable.setDescription(itemTableSetting.getUuid());
                    itemTable.setItemTableEntity(itemTableSetting.getEntity());
                    itemTable.setItemTableEntityId(itemTableSetting.getEntityId());
                    itemTable.setItemTableRelation(itemTableSetting.getRelationField());
                    itemTable.setItemTableRelationId(itemTableSetting.getRelationFieldId());
                    list.add(itemTable);
                }
            }
        }


        SelectItem itemTable = new SelectItem();
        list.add(itemTable);

        return list;
    }

    @Override
    public SelectItem[] getOpportunityItemTables(String formID) {
        List<EdsCFItemTableSetting> itemTableSettings = cfItemTableSettingmanager.findByFormId(formID);
        SelectItem[] selectItems = new SelectItem[itemTableSettings.size()];
        int i = 0;
        for (EdsCFItemTableSetting itemTableSetting : itemTableSettings) {
            SelectItem item = new SelectItem();
            item.setId(itemTableSetting.getObjectID());
            item.setName(itemTableSetting.getName());
            item.setDescription(itemTableSetting.getUuid());
            selectItems[i] = item;
            i++;
        }

        return selectItems;
    }

    @Override
    public SelectItem[] getProjectItemTables(String formID) {
        List<EdsCFItemTableSetting> itemTableSettings = cfItemTableSettingmanager.findByFormId(formID);
        SelectItem[] selectItems = new SelectItem[itemTableSettings.size()];
        int i = 0;
        for (EdsCFItemTableSetting itemTableSetting : itemTableSettings) {
            SelectItem item = new SelectItem();
            item.setId(itemTableSetting.getObjectID());
            item.setName(itemTableSetting.getName());
            item.setDescription(itemTableSetting.getUuid());
            selectItems[i] = item;
            i++;
        }

        return selectItems;
    }

    @Override
    public SelectItem[] getEmployeeItemTables(String formID) {
        List<EdsCFItemTableSetting> itemTableSettings = cfItemTableSettingmanager.findByFormId(formID);
        SelectItem[] selectItems = new SelectItem[itemTableSettings.size()];
        int i = 0;
        for (EdsCFItemTableSetting itemTableSetting : itemTableSettings) {
            SelectItem item = new SelectItem();
            item.setId(itemTableSetting.getObjectID());
            item.setName(itemTableSetting.getName());
            item.setDescription(itemTableSetting.getUuid());
            selectItems[i] = item;
            i++;
        }

        return selectItems;
    }

    @Override
    public SelectItem[] getItemTablesByFormID(String formID) {
        List<EdsCFItemTableSetting> itemTableSettings = cfItemTableSettingmanager.findByFormId(formID);
        SelectItem[] selectItems = new SelectItem[itemTableSettings.size()];
        int i = 0;
        for (EdsCFItemTableSetting itemTableSetting : itemTableSettings) {
            SelectItem item = new SelectItem();
            item.setId(itemTableSetting.getObjectID());
            item.setName(itemTableSetting.getName());
            item.setDescription(itemTableSetting.getUuid());
            selectItems[i] = item;
            i++;
        }

        return selectItems;
    }

    @Override
    public SelectItem[] getItemTableEntities(String formID) {
        if (StringUtils.isBlank(formID)) {
            return new SelectItem[0];
        }
        String suffixToRemove = "_FORM";

        List<EdsCompanyCustomFieldsSettings> companyCustomFieldsWithCategory = companyCustomFieldsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), CUSTOM_VIEW + formID.replace(suffixToRemove, ""));

        List<SelectItem> itemTableEntities = companyCustomFieldsWithCategory.stream()
                .filter(edsCompanyCustomFieldsSettings ->
                        edsCompanyCustomFieldsSettings.getUiType().equals(Constants.UI_TYPE_LOOKUP) &&
                                edsCompanyCustomFieldsSettings.getLookUpType() != null &&
                                (edsCompanyCustomFieldsSettings.getLookUpType().equals(CustomFieldLookUpTypeEnum.DEPARTMENT) ||
                                        edsCompanyCustomFieldsSettings.getLookUpType().equals(CustomFieldLookUpTypeEnum.PROJECT) ||
                                        edsCompanyCustomFieldsSettings.getLookUpType().equals(CustomFieldLookUpTypeEnum.LOCATION)))
                .map(edsCompanyCustomFieldsSettings ->
                        new SelectItem(edsCompanyCustomFieldsSettings.getObjectID(), edsCompanyCustomFieldsSettings.getLookUpType().name()))
                .collect(Collectors.toList());

        return itemTableEntities.toArray(new SelectItem[]{});
    }


    private void initCustomFields(CustomFieldSection section, LinkedHashMap<String, ColumnConfigs> columnMap) {
        List<EdsCompanyCustomFieldsSettings> fields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(section.name());
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(userManager.getUser());

        if (!CollectionUtils.isEmpty(fields)) {
            fields.forEach(field -> {
                ColumnConfigs columnConfig = new ColumnConfigs();
                columnConfig.setCompanyCustomFieldID(field.getObjectID());
                columnConfig.setTitle(field.getFieldNameLocalization(userSettings.getInternationalization()));
                columnConfig.setCode(field.getColumnCode());
                columnConfig.setWidth(field.getColumnWidth());
                columnConfig.setAliasName(field.getFieldNameLocalization(userSettings.getInternationalization()));
                columnConfig.setDataType(field.getDataType());
                columnConfig.setUiType(field.getUiType());
                columnConfig.setRequired(field.getRequired());
                columnMap.put(field.getColumnCode(), columnConfig);
            });
        }
    }

    @Override
    public void updateItemTableSettings(String columnCode, String fieldName, String entityName) {
        ArrayList<ItemTableEnum> section = getSectionByEntityName(entityName);
        if (section != null && section.size() > 0) {
            if (section.size() == 1) {
                LinkedList<ColumnConfigs> clist = new LinkedList<>();
                if (ItemTableEnum.CUSTOM_FORM.equals(section.get(0)) || ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM.equals(section.get(0)) || ItemTableEnum.EMPLOYEE_CUSTOM_ITEM.equals(section.get(0)) || ItemTableEnum.PLACEMENT_CUSTOM_ITEM.equals(section.get(0)) || ItemTableEnum.CANDIDATE_CUSTOM_ITEM.equals(section.get(0)) || ItemTableEnum.PROJECT_CUSTOM_ITEM.equals(section.get(0))) {
                    EdsCompanyCustomFieldsSettings settings = companyCustomFieldsManager.getCompanyCustomField(entityName, columnCode);
                    if (settings == null) {
                        return;
                    }
                    EdsCFItemTableSetting its = cfItemTableSettingmanager.findByUUID(settings.getEntityCategoryName());
                    if (its != null && StringUtils.isNotBlank(its.getSettingsJSONData())) {
                        Gson gson = new Gson();
                        ColumnConfigs[] columns = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
                        for (ColumnConfigs column : columns) {
                            if (!column.getCode().equals(columnCode)) {
                                clist.add(column);
                            }
                        }
                    } else {
                        its = new EdsCFItemTableSetting();
                    }
                    ColumnConfigs columnConfigs = new ColumnConfigs(columnCode, fieldName, true);
                    columnConfigs.setSelected(true);
                    clist.add(columnConfigs);
                    Gson gson = new Gson();
                    its.setSettingsJSONData(gson.toJson(clist.toArray(new ColumnConfigs[]{})));
                    cfItemTableSettingmanager.createOrUpdate(its);
                    return;
                }
                saveColumnConfig(columnCode, fieldName, clist, section.get(0));
            } else if (section.size() > 1) {
                for (ItemTableEnum itemTableEnum : section) {
                    LinkedList<ColumnConfigs> clist = new LinkedList<>();
                    saveColumnConfig(columnCode, fieldName, clist, itemTableEnum);
                }
            }
        }
    }

    private void saveColumnConfig(String columnCode, String fieldName, LinkedList<ColumnConfigs> clist, ItemTableEnum itemTableEnum) {
        EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(itemTableEnum);
        if (its != null) {
            Gson gson = new Gson();
            ColumnConfigs[] columns = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
            for (ColumnConfigs column : columns) {
                if (!column.getCode().equals(columnCode)) {
                    clist.add(column);
                }
            }
        } else {
            its = new EdsItemTableSettings();
        }
        ColumnConfigs columnConfigs = new ColumnConfigs(columnCode, fieldName, true);
        columnConfigs.setSelected(true);
        clist.add(columnConfigs);
        Gson gson = new Gson();
        ColumnConfigs[] columnConfigList = clist.toArray(new ColumnConfigs[]{});
        its.setSettingsJSONData(gson.toJson(columnConfigList));
        its.setSection(itemTableEnum);
        itemTableSettingsManager.createOrUpdate(its);

        String key = CacheConstants.ITEM_TABLE_SECTION + "_" + itemTableEnum.getTitle() + "_" + SecurityContext.getCompanyID();
        RedisClient.removeKey(key);
        RedisClient.setKey(key, columnConfigList, ColumnConfigs[].class);
    }

    private ArrayList<ItemTableEnum> getSectionByEntityName(String entityName) {
        ArrayList<ItemTableEnum> list = new ArrayList<>();
        if (CustomFieldSection.SaleInvoiceItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.SALE_INVOICE_ITEM);
            list.add(ItemTableEnum.CREDIT_NOTE_ITEM);
        } else if (CustomFieldSection.SaleQuoteItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.SALE_QUOTE_ITEM);
        } else if (CustomFieldSection.SaleOrderItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.SALE_ORDER_ITEM);
        } else if (CustomFieldSection.PurchaseInvoiceItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.PURCHASE_INVOICE_ITEM);
            list.add(ItemTableEnum.DEBIT_NOTE_ITEM);
        } else if (CustomFieldSection.PurchaseOrderItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.PURCHASE_ORDER_ITEM);
        } else if (CustomFieldSection.OpportunitySubItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.OPPORTUNITY_SUB_ITEM);
        } else if (CustomFieldSection.RFQItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.RFQ_ITEM);
        } else if (CustomFieldSection.RentalOrderItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.RENTAL_ORDER_ITEM);
        } else if (CustomFieldSection.RFPItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.RFP_ITEM);
        } else if (CustomFieldSection.ClientItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.CLIENT_ITEM);
        } else if (CustomFieldSection.SupplierItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.SUPPLIER_ITEM);
        } else if (CustomFieldSection.CustomFormItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.CUSTOM_FORM);
        } else if (CustomFieldSection.OpportunityItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.OPPORTUNITY_CUSTOM_ITEM);
        } else if (CustomFieldSection.ProjectItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.PROJECT_CUSTOM_ITEM);
        } else if (CustomFieldSection.ManualJournalItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.MANUAL_JOURNAL_ITEM);
        } else if (CustomFieldSection.BankPaymentItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.BANK_PAYMENT_ITEM);
        } else if (CustomFieldSection.CashPaymentItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.CASH_PAYMENT_ITEM);
        } else if (CustomFieldSection.BankReceiptItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.BANK_RECEIPT_ITEM);
        } else if (CustomFieldSection.CashReceiptItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.CASH_RECEIPT_ITEM);
        } else if (CustomFieldSection.LeadItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.LEAD_ITEM);
        } else if (CustomFieldSection.EmployeeItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.EMPLOYEE_CUSTOM_ITEM);
        } else if (CustomFieldSection.AdditionalPaymentItem.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.ADDITIONAL_PAYMENT_ITEM);
        } else if (CustomFieldSection.PlacementItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.PLACEMENT_CUSTOM_ITEM);
        } else if (CustomFieldSection.RotationItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.ROTATION_ITEM_TABLE);
        } else if (CustomFieldSection.ExperienceItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.EXPERIENCE_ITEM_TABLE);
        } else if (CustomFieldSection.CandidateCustomItemTable.getName().name().equals(entityName)) {
            list.add(ItemTableEnum.CANDIDATE_CUSTOM_ITEM);
        }
        return list;
    }
}
