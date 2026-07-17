/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/8 5:28:52                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomFormAttributeItem;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.placeofsupply.PlaceOfSupplyItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceDeviceStatus;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.OverallSearchRpc;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Employee
 * Date: Nov 3, 2009
 * Time: 4:56:48 PM
 */
public interface CommonServiceAsync {
    void addMembers(Integer projectId, Integer[] members, AsyncCallback<Void> async);

    void getBenefitTypeList(AsyncCallback<SelectItem[]> async);

    void getBenefitApprovers(AsyncCallback<SelectItem[]> async);


    void getPositions(AsyncCallback<SelectItem[]> async);

    void getLanguages(AsyncCallback<SelectItem[]> async);

    void getAddTaskStatusDrop(AsyncCallback<SelectItem[]> async);

    void getProjects(boolean crmActivityProjectNeeded, AsyncCallback<ProjectItem[]> async);

    void getProjects(boolean crmActivityProjectNeeded, boolean withProjectNumber, AsyncCallback<ProjectItem[]> async);

    void getDepartmentByName(String name, AsyncCallback<Integer> async);

    void getDepartmentByCode(String code, AsyncCallback<Integer> async);

    void getCurrentUserDepartmentID(AsyncCallback<Integer> callback);

    void getImageUrl(Integer id, AsyncCallback<String> callback);

    void getFileUrl(Integer fileId, String fileType, Boolean needHostForLocalFile, boolean withSize, AsyncCallback<String[]> callback);

    void getNotecomments(Integer objectId, AsyncCallback<NewsComment[]> callback);

    void saveNoteComment(NewsComment data, AsyncCallback<NewsComment> callback);

    void getDepartmentNotes(Integer departmentID, AsyncCallback<HistoryListItem[]> async);

    void saveCroppedImage(Integer id, int left, int top, int width, int height, AsyncCallback<String> callback);

    void saveItemCroppedImage(Integer imageID, Integer entityID, Integer entityType, int left, int top, int width, int height, AsyncCallback<FileResource> callback);

    void saveImageUrl(Integer id, Integer userID, AsyncCallback<String> callback);

    void deleteZoomCall(Integer zoomId, String url, AsyncCallback<Void> async);

    void saveStudentImageUrl(Integer id, Integer studentId, AsyncCallback<String> callback);

    void saveCrmContactImageUrl(Integer id, Integer crmContactID, AsyncCallback<String> callback);

    void searchByKeyword(DocumentsSearchItem searchItem, AsyncCallback<SearchResultItemList> callback);

    void searchByAllModule(AdvancedSearchRpc advancedSearchRpc, AsyncCallback<OverallSearchRpc> callback);

    void getSupportPackagePrices(String hostName, AsyncCallback<HashMap<String, Double>> callback);

    void getFreeTrialDaysLeft(boolean isPaidCompany, AsyncCallback<Integer> callback);

    void getEmployeePosition(Integer userID, AsyncCallback<String> callback);

    void getCountriesAndRegions(AsyncCallback<HashMap<String, SelectItem[]>> async);

    void getTeamList(AsyncCallback<SelectItem[]> callback);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getRegions(AsyncCallback<SelectItem[]> async);

    void getRegions(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void createSystemFolders(Integer companyId, AsyncCallback<Void> async);

    void getPurchaseInvoices(AsyncCallback<SelectItem[]> async);

    void getExpenseReports(AsyncCallback<SelectItem[]> async);

    void getTempFolderByCompanyID(String companyID, String userID, AsyncCallback<FolderResource> callback);

    void getCSVColumns(Integer fileId, AsyncCallback<HashMap<String, SelectItem[]>> async);

    void getEmployeeImageURL(Integer userId, AsyncCallback<String> async);

    void removeUserImage(Integer userID, AsyncCallback<Boolean> callback);

    void getEmployeeImageURL(AsyncCallback<SelectItem> async);

    void getDefaultDescriptionCharacterLimit(AsyncCallback<Integer> callback);

    void enableNewDragableWorkspace(AsyncCallback<Boolean> callback);

    void showContactSynchronize(AsyncCallback<Boolean> callback);

    void unForceGuideListingPanelVisibility(ListPanelType panelType, AsyncCallback<Void> callback);

    void saveUserSettings(UserSettingsTypeEnum type, String key, String value, AsyncCallback<Boolean> callback);

    void saveListPanelSettings(ListPanelToolRpc settings, AsyncCallback<Void> callback);

    void saveEnableWorkspaceWelcomePage(boolean isCheck, AsyncCallback<Void> callback);

    void saveRecurrenceJob(RecurrenceJobItem item, AsyncCallback<Void> callback);

    void getJob(Integer jobType, AsyncCallback<RecurrenceJobItem> callback);

    void findEMLFileInputZip(Integer uploadFileId, AsyncCallback<Boolean> callback);

    void getImportEMLFiles(Integer zipFileId, AsyncCallback<EmailTemplateItem[]> callback);

    void deleteFile(Integer fileId, AsyncCallback<Void> callback);

    void getCompanyCustomFields(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void getCustomFieldByEntityCategory(String entityCategory, Integer companyId, AsyncCallback<HashMap<ArrayList<String>, ArrayList<String>>> async);

    void getCompanyAllCustomFields(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void getCompanyCustomFieldsByColumnCode(ViewName viewName, String columnCode, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void getCompanyCustomFieldsForListView(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields, AsyncCallback<ListPanelToolRpc> callback);

    void saveAddViewPosition(Integer companyID, ViewAddFiledsCodeName viewFieldsCode, ArrayList<String> onlyViewShowfieldCodeName, AsyncCallback<Void> callback);

    void getMoreMenuSettings(String actionName, AsyncCallback<String> callback);

    void saveCRMContactCompanyLogo(Integer uploadId, Integer crmContactId, AsyncCallback<SelectItem> async);

    void getCrmContactCompanyLogo(AsyncCallback<SelectItem> async);

    void getIncomingCallerDetails(String phoneNumber, AsyncCallback<TwilioContactItem> async);

    void deleteFacetFilter(Integer deleleFilterId, AsyncCallback<Void> callback);

    void getSavedFacetFilterList(ListPanelType type, Integer typeId, AsyncCallback<SaveFilterSelectItems> callback);

    void saveFacetFilter(HashMap<String, Object> paramMap, AsyncCallback<Integer> callback);

    void getProjects(ListingFilterParameter filterParametrs, boolean crmActivityProjectNeeded, boolean withProjectNumber, AsyncCallback<ProjectItem[]> async);

    void checkCFNameExists(String type, String category, String fieldName, String aliasName, Integer fieldID, boolean isItemTable, String itemTableOrFormType, AsyncCallback<CompanyCustomFieldItem> async);

    void getCountries(boolean sorted, AsyncCallback<SelectItem[]> async);

    void getCountries(ListingFilterParameter fp, boolean sorted, AsyncCallback<SelectItem[]> async);

    void validateAddressByUSPS(Address address, AsyncCallback<String> callback);

    void getCompanyEmployeesWithTeams(AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void getStudentImageURL(Integer studentID, AsyncCallback<String> callback);

    void getCompanyEmployeesWithTeams(Integer objectId, boolean isPayroll, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void saveCrmAccountLogoUrl(Integer imageID, Integer objectId, AsyncCallback<String> abstractAsyncCallback);

    void removeLocalizationItem(Integer id, AsyncCallback<Boolean> callback);

    void getReportingTopMenu(AsyncCallback<LinkedHashMap<String, LinkedHashMap<String, SelectItem>>> callback);

    void saveFacetFilter(FacetFilterRpc facetFilter, ListPanelType type, AsyncCallback<Integer> async);

    void getCompanyStepCategoryCustomFields(Integer stepID, AsyncCallback<ArrayList<CompanyCustomFieldItem>> callback);

    void getOnboardingStepdList(ListingFilterParameter fp, AsyncCallback<ArrayList<SelectItem>> callback);

    void getPositionEmployees(Integer positionID, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> asyncCallback);

    void getPositionDepartments(SelectItem position, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> asyncCallback);


    void getPositionsForKpiTree(SelectItem position, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> asyncCallback);

    void getLocationsForKpiTree(AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> asyncCallback);

    void getDepartmentsForKpiTree(SelectItem department, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> asyncCallback);

    void getTotalCharge(ProjectPosition po, AsyncCallback<String> callback);

    void getCalculateUnitPrice(ProjectPosition po, AsyncCallback<String> callback);

    void convertReference2SelectItem(String referenceName, boolean useSortOrder, String defaultSelection, AsyncCallback<SelectItem[]> asyncCallback);

    void getEmployeePresentTime(String employeeID, String dateItem, AsyncCallback<AttendanceReportLogItem> callback);

    void getCountriesList(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> callback);

    void getVacancyTypeItems(AsyncCallback<SelectItem[]> callback);

    void getReligionItems(AsyncCallback<SelectItem[]> callback);

    void getReferenceItems(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void checkProjectBillable(Integer projectId, AsyncCallback<Boolean> callback);

    void getCompanyAddress(AsyncCallback<CompanyAddress> callback);

    void getCompanyDefaultCountry(AsyncCallback<SelectItem> callback);

    void getRegions(Integer countryId, AsyncCallback<SelectItem[]> async);

    void getRegions(Integer countryId, ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getInvoicesQuotesAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void getReferences(ReferenceParentEnum parentCode, AsyncCallback<SelectItem[]> callback);

    void getReferences(String parentCode, AsyncCallback<SelectItem[]> callback);

    void getEmployeeProfile(AsyncCallback<EmployeeProfileItem> asyncCallback);

    void getExistingCustomFields(String entityName, String entityCategoryName, Integer objectID, AsyncCallback<HashMap<Integer, String[]>> async);

    void getCustomFieldData(Integer objectID, AsyncCallback<CompanyCustomFieldItem> async);

    void saveCustomFields(CompanyCustomFieldItem item, boolean fromItemtable, AsyncCallback<Void> callback);

    void saveStaticFieldProperty(String formId, FormProperty formProperty, AsyncCallback<Void> callback);

    void saveCroppedImage(Integer entityId, String type, Integer imageID, int left, int top, int width, int height, AsyncCallback<String> async);

    void getFinancialYearStart(AsyncCallback<DateNonConvertable> async);

    void resetCompany(AsyncCallback<String> async);

    void getCompanyCustomFieldByEntityNameAndFieldName(ViewName viewName, String columnCode, AsyncCallback<CompanyCustomFieldItem> callback);

    void getSkillsForEmployeeAssignment(AsyncCallback<SelectItem[]> async);

    void setSideNavBarPosition(String position, AsyncCallback<String> callback);

    void loadSideNavPosition(AsyncCallback<String> callback);

    void getPlaceOfSupply(String taxTreatmentType, AsyncCallback<PlaceOfSupplyItem> async);

    void getDefaultPlaceOfSupply(AsyncCallback<SelectItem> async);

    void checkCFName(String entityName, String name, String uiType, AsyncCallback<String[]> asyncCallback);

    void deleteCustomField(String entityName, String columnCode, AsyncCallback<Void> asyncCallback);

    void saveCustomField(CustomizeFormItem field, AsyncCallback<String> callback);

    void getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields, Integer relationship, Integer limitCustomFields, AsyncCallback<ListPanelToolRpc> async);

    void getCustomFieldsCount(String formID, AsyncCallback<Integer[]> asyncCallback);

    void updateCustomField(String entityName, String name, boolean mandatory, AsyncCallback<Void> voidAsyncCallback);

    void getCustomFieldByEntityNameAndColumnCode(String entityName, String columnCode, AsyncCallback<CompanyCustomFieldItem> async);

    void getCustomForm(Integer objectId, AsyncCallback<CustomFormItem> callback);

    void getCustomFormItemsForLookUp(String form_id, AsyncCallback<SelectItem[]> callback);

    void saveCustomForm(CustomFormItem item, AsyncCallback<String> callback);

    void getCustomFormItems(ListingFilterParameter fp, AsyncCallback<ListResult<FormItems>> callback);

    void saveCustomFormItem(FormItems item, AsyncCallback<Integer> callback);

    void getCustomFormItem(Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String convertFormType, Integer convertFormId, AsyncCallback<FormItems> callback);

    void getCustomFormTimerItems(String formId, AsyncCallback<FormItems> callback);

    void getCustomItemTable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getOpportunityItemtable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getEmployeeItemtable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getPlacementItemtable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getProjectItemtable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getCandidateItemTable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void getCompanyCategoryCustomFields(Integer formID, AsyncCallback<ArrayList<CompanyCustomFieldItem>> callback);

    void getMaxValueOfAutoNumbering(CompanyCustomFieldItem customFieldItem, AsyncCallback<String> callback);

    void getCustomFormItemHistoryNotes(Integer id, String viewType, AsyncCallback<List<HistoryNote>> callback);

    void createCustomFormItemNote(Integer transferID, HistoryListItem hisItem, AsyncCallback<Integer> callback);

    void deleteCustomFormItemNote(Integer bankTransferID, AsyncCallback<Boolean> callback);

    void getCustomForms(AsyncCallback<ArrayList<SelectItem>> callback);

    void deleteCustomFormItem(Integer objectId, AsyncCallback<Void> callback);

    void checkCustomFormAttributeCount(String fieldType, String formID, AsyncCallback<String> callback);

    void getCustomFormAttributes(String formID, AsyncCallback<ArrayList<CustomFormAttributeItem>> callback);

    void approveOrRejectCustomFormItem(Integer objectID, String statusCode, AsyncCallback<Void> callback);

    void isEnableApprovers(String formId, AsyncCallback<Boolean> callback);

    void getDynamicImageUrl(Integer id, AsyncCallback<String> callback);

    void saveCustomFormItemCellValue(FormItems rowValue, String columnCodeName, AsyncCallback<Void> async);

    void getEmailbyTrackerid(Integer trackerId, AsyncCallback<Email> async);

    void getDynamicCustomForms(String lookUpType, boolean isCrmAccount, AsyncCallback<ArrayList<SelectItem>> async);

    void getOtherContactTypes(String phoneNumber, AsyncCallback<ArrayList<ContactTypeForTwilio>> async);

    void getSerialNumbers(ListingFilterParameter fp, AsyncCallback<SelectItem[]> selectItemAsyncCallback);

    void getBatchType(ListingFilterParameter fp, AsyncCallback<SelectItem[]> selectItemAsyncCallback);

    void getBatchWarehouse(ListingFilterParameter fp, AsyncCallback<SelectItem[]> selectItemAsyncCallback);

    void getTaxTreatmentItems(AsyncCallback<SelectItem[]> async);

    void getUserListPanelSettings(ListPanelType type, String formId, Integer typeId, Integer stepID, AsyncCallback<ListPanelToolRpc> async);

    void getCompanyCustomFieldsAndFormProperties(ViewName viewName, String formID, AsyncCallback<CompanyCfAndPropertyItems> async);

    void getCandidateFormCustomFieldsForQuestion(AsyncCallback<CompanyCfAndPropertyItems> async);

    void getCustomFieldsForQuickAdd(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void memorizedCustomFormItem(Integer objectID, String formID, AsyncCallback<Void> async);

    void getFormProperty(String caseForm, AsyncCallback<LinkedHashMap<String, FormProperty>> async);

    void getCompanyCustomFieldById(Integer objectId, AsyncCallback<CompanyCustomFieldItem> asyncCallback);

    void getCompanyCustomFieldsForBaseInvoices(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> async);

    void getBankAccountCurrencyList(ListingFilterParameter fp, AsyncCallback<SelectItem[]> selectedItems);

    void getCompanyAllDropDownCustomFiedsByEntityName(String entityName, Integer objectId, String costumFieldAliceName, boolean isCutomForm, AsyncCallback<SelectItem[]> selectedItems);

    void getItemTableCustomForms(String itemTable, Integer companyId, AsyncCallback<ArrayList<String>> async);

    void getRentalProductImageURL(Integer rentalId, AsyncCallback<String> async);

    void getEmbasies(AsyncCallback<SelectItem[]> async);

    void getColumnsItems(ArrayList<String> columns, AsyncCallback<LinkedHashMap<String, ArrayList<SelectItem>>> async);

    void getCFLocalization(Integer id, LocalizationTypeEnum type, AsyncCallback<CustomFormLocalization> localizationAsyncCallback);

    void getCustomFieldByAlias(String entityName, String alias, AsyncCallback<CompanyCustomFieldItem> async);

    void customFormIsQuizForm(String formId, AsyncCallback<Boolean> async);

    void getCustomFormCfAndItem(ViewName viewName, Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String convertFormType, Integer convertFormId, AsyncCallback<CompanyCFAndFormItems> async);

    void getKanbanColumnConfigs(KanbanItemSettingEnum settingEnum, AsyncCallback<KanbanItemColumnConfigs[]> async);

    void getQuickAddColumnConfigs(QuickAddSettingsForm form, AsyncCallback<QuickAddColumnConfigs[]> async);

    void saveKanbanItemSettings(KanbanItemSettingEnum settingEnum, KanbanItemColumnConfigs[] columnConfigs, AsyncCallback<Integer> async);

    void saveQuickAddSettings(QuickAddSettingsForm form, QuickAddColumnConfigs[] columnConfigs, AsyncCallback<Integer> async);

    void getRelatedFieldsBySectionName(String sectionCode, AsyncCallback<SelectItem[]> async);

    void getKanbanItemFieldsAsMap(AsyncCallback<HashMap<String, KanbanItemColumnConfigs[]>> async);

    void getVacancyItemtable(Integer id, String uuid, AsyncCallback<CustomTableRpc[]> asyncCallback);

    void saveAttendanceHour(EmployeePresentItem item, AsyncCallback<Integer> async);

    void getItemTableSettingsMap(String formID, AsyncCallback<HashMap<String, SelectItem>> async);


    void getItemTableValues(String formID, ArrayList<SelectItem> uuid, AsyncCallback<HashMap<String, ArrayList<CustomTableRpc>>> async);

    void getUserLocation(AsyncCallback<SelectItem> async);

    void getAttendanceTerminals(AsyncCallback<ArrayList<AttendanceTerminal>> async);

    void getAttendanceTerminals(String searchKey, AsyncCallback<ArrayList<AttendanceTerminal>> async);

    void getAttendanceTerminal(Integer id, AsyncCallback<AttendanceTerminal> async);

    void saveAttendanceTerminal(AttendanceTerminal domain, AsyncCallback<Integer> async);

    void deleteAttendanceTerminal(Integer id, AsyncCallback<Void> async);

    void fetchAttendanceTerminalStatus(AsyncCallback<ArrayList<AttendanceDeviceStatus>> async);
}
