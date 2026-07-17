package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 24, 2010
 * Time: 5:47:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CompanyCustomFieldsManager extends Manager<EdsCompanyCustomFieldsSettings> {
    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(String entityName, String dataType);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByRelationship(String entityName, Integer relationship, Integer limitCustomFields);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(String entityName, String dataType, String category, Integer relationship, Integer objectID, Integer limitCustomFields);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFields(String entityName, String dataType, String category, Integer relationship, Integer objectID, Integer limitCustomFields, Boolean withDaletedValues);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForListView(String entityName);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFormFiledsByFormId(String entityName, String entitycategoryname, String uiType);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByCategoryForListView(String entityName, String categoryName);

    ArrayList<String> getCompanyCustomFieldsByEntityCategory(String entityCategory, Integer companyId);

    ArrayList<String> getCompanyCustomFieldsByEntityNative(String entityName, Integer companyId);

    EdsCompanyCustomFieldsSettings getCompanyCustomFieldColumnCode(String entityName, String categoryName, String uitype);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForFiltering(String entityName);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsForBaseInvoices(String entityName);

    EdsCompanyCustomFieldsSettings getCompanyCustomField(String entityName, String columnCode);

    EdsCompanyCustomFieldsSettings getCompanyCustomField(String entityName, String caegoryName, String columnCode);

    List<EdsCompanyCustomFieldsSettings> getCustomFields(ListingFilterParameter fp);

    EdsCompanyCustomFieldsSettings getByAliasName(String entityName, String aliasName);

    List<EdsCompanyCustomFieldsSettings> getCompanyLeadContactAndOpportunityCustomFields(String lead, String contact, String opportunity, String account);

    Integer getCustomFieldsCount(ListingFilterParameter fp);

    EdsCompanyCustomFieldsSettings getCompanyCustomFieldByEntityNameAndFieldName(String entityName, String fieldName);

    void deleteCustomFieldValues(String tableName, String fieldName);

    List<EdsCompanyCustomFieldsSettings> getAllDeletedCustomFieldsByViewName(String viewname, String categoryName);

    void deleteProductCustomFieldValues(String tableName, String fieldName, Integer relationship);

    void deleteLeadCustomFieldValues(String tableName, String fieldName);

    void deleteCandidateCustomFieldValues(String tableName, String fieldName);

    void deleteOpportunityCustomFieldValues(String tableName, String fieldName);

    void deleteCrmAccountCustomFieldValues(String tableName, String fieldName);

    void deleteContactCustomFieldValues(String tableName, String fieldName);

    void deleteCrmCaseCustomFieldValues(String tableName, String fieldName);

    void deleteInvoiceCustomFieldValues(String tableName, String fieldName, String type);

    void deleteQuoteCustomFieldValues(String tableName, String fieldName, String type, Boolean isSaleOrder);

    void deleteCustomFormCustomFieldValues(String tableName, String fieldName, String form_id);

    void deleteCustomFormItemTableCustomFieldValues(String tableName, String fieldName, String form_id);

    boolean isCustomAliasNameExists(String type, String category, String aliasName, Integer fieldID);

    void deleteCustomFieldValidations(Integer objectID);

    EdsCompanyCustomFieldsSettings getByColumnCode(String viewName, String fieldID);

    SelectItem[] getCustomFieldDataByQuery(Integer companyID, String query, String ...searchKey);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByEntityName(String entityName);

    List<EdsCompanyCustomFieldsSettings> getCustomFieldsForQuickAdd(String entityName);

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsByLookUpType(String lookUpType, boolean isCrmAccount, boolean isCustomForm);

    List<String> getCompanyCustomFieldsColumnCodesList(String entityName);

    List<EdsCompanyCustomFieldsSettings> getCompanyFileUploadCustomFields();

    List<EdsCompanyCustomFieldsSettings> getCompanyCustomFieldsWithCategory(String entityName, String entityCategory);

    List<String> getSeeRelatedCompanyCustomFields(String entityName, String entityCategory);

    void copyCustomFields(Integer fromCompanyID, Integer toCompanyID, String entityName, String categoryName);

    void deleteStepCustomFieldPermissions(String viewName);

    void deleteStepCustomFields(String viewName);

    boolean isCustomNameExists(String viewName, String name);

    List<String> getValuesOfAutoNumberingByTableName(CompanyCustomFieldItem customFieldItem, boolean checkForDate);

    void deletePrepaymentCustomFieldValues(String customFieldTableName, String columnCode, String entityName);

    List<EdsCompanyCustomFieldsSettings> getEntityCustomFields(String entityName, String entityCategoryName);

    List<String> getCustomFieldsCodeForLocale();

    List<EdsCompanyCustomFieldsSettings> getCFByUiTypes(String viewName, List<String> uiTypes, String entityCategoryName);

    List<EdsCompanyCustomFieldsSettings> getCFByUiTypesForHrBot();

    Map<Integer, EdsCompanyCustomFieldsSettings> getCFByIdsForHrBot(List<Integer> fieldIds );

    ArrayList<EdsCompanyCustomFieldsSettings> getDeletedCustomFieldsByType(ArrayList<String> fieldTypes);

    void clearAllDeletedFields(List<Integer> ids);


}
