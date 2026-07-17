package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Employee
 * Date: Nov 3, 2009 4:54:05 PM
 */
public interface CommonService extends RemoteService {

    void addMembers(Integer projectId, Integer[] members);

    SelectItem[] getPositions();

    SelectItem[] getLanguages();

    SelectItem[] getAddTaskStatusDrop();

    String getRentalProductImageURL(Integer rentalId);

    SelectItem[] getBenefitApprovers();

    SelectItem[] getBenefitTypeList();

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCompanyEmployeesWithTeams();

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCompanyEmployeesWithTeams(Integer objectId, boolean isPayroll);

    ProjectItem[] getProjects(ListingFilterParameter filterParametrs, boolean crmActivityProjectNeeded, boolean withProjectNumber);

    ProjectItem[] getProjects(boolean crmActivityProjectNeeded);

    ProjectItem[] getProjects(boolean crmActivityProjectNeeded, boolean withProjectNumber);

    Integer getDepartmentByName(String name);

    Integer getDepartmentByCode(String code);

    Integer getCurrentUserDepartmentID();

    String getImageUrl(Integer id);

    String[] getFileUrl(Integer fileId, String fileType, Boolean needHostForLocalFile, boolean withSize);

    NewsComment[] getNotecomments(Integer objectId);

    NewsComment saveNoteComment(NewsComment data);

    HistoryListItem[] getDepartmentNotes(Integer departmentID);

    String saveCroppedImage(Integer id, int left, int top, int width, int height);

    FileResource saveItemCroppedImage(Integer imageID, Integer entityID, Integer entityType, int left, int top, int width, int height);

    String saveImageUrl(Integer id, Integer userID);

    String saveStudentImageUrl(Integer id, Integer studentId);

    String saveCrmContactImageUrl(Integer id, Integer crmContactID);

    SearchResultItemList searchByKeyword(DocumentsSearchItem searchItem);

    OverallSearchRpc searchByAllModule(AdvancedSearchRpc advancedSearchRpc);

    Integer getFreeTrialDaysLeft(boolean isPaidCompany);

    HashMap<String, Double> getSupportPackagePrices(String hostName);

    String getEmployeePosition(Integer userID);

    HashMap<String, SelectItem[]> getCountriesAndRegions();

    SelectItem[] getEmbasies();

    SelectItem[] getTeamList();

    SelectItem[] getCountries();

    SelectItem[] getCountries(boolean sorted);

    SelectItem[] getCountries(ListingFilterParameter fp, boolean sorted);

    SelectItem[] getRegions();

    SelectItem[] getRegions(ListingFilterParameter fp);

    SelectItem[] getRegions(Integer countryId);

    SelectItem[] getRegions(Integer countryId, ListingFilterParameter filterParameter);

    void createSystemFolders(Integer companyId);

    SelectItem[] getPurchaseInvoices();

    SelectItem[] getExpenseReports();

    FolderResource getTempFolderByCompanyID(String companyID, String userID);

    HashMap<String, SelectItem[]> getCSVColumns(Integer fileId);

    String getEmployeeImageURL(Integer userId);

    Boolean removeUserImage(Integer userId);

    SelectItem getEmployeeImageURL();

    Integer getDefaultDescriptionCharacterLimit();

    Boolean enableNewDragableWorkspace();

    Boolean showContactSynchronize();

    ListPanelToolRpc getUserListPanelSettings(ListPanelType type, String formId, Integer typeId, Integer stepID);

    void unForceGuideListingPanelVisibility(ListPanelType panelType);

    Boolean saveUserSettings(UserSettingsTypeEnum type, String key, String value);

    void saveListPanelSettings(ListPanelToolRpc settings);

    void saveEnableWorkspaceWelcomePage(boolean isCheck);

    void saveRecurrenceJob(RecurrenceJobItem item);

    RecurrenceJobItem getJob(Integer jobType);

    boolean findEMLFileInputZip(Integer uploadFileId);

    EmailTemplateItem[] getImportEMLFiles(Integer zipFileId);

    void deleteFile(Integer fileId);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFields(ViewName viewName);

    HashMap<ArrayList<String>, ArrayList<String>> getCustomFieldByEntityCategory(String entityCategory, Integer companyId);

    ArrayList<CompanyCustomFieldItem> getCustomFieldsForQuickAdd(ViewName viewName);

    CompanyCfAndPropertyItems getCompanyCustomFieldsAndFormProperties(ViewName viewName, String formID);

    CompanyCfAndPropertyItems getCandidateFormCustomFieldsForQuestion();

    ArrayList<CompanyCustomFieldItem> getCompanyAllCustomFields(ViewName viewName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByColumnCode(ViewName viewName, String columnCode);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForListView(ViewName viewName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForBaseInvoices(ViewName viewName);

    ListPanelToolRpc getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields);

    ListPanelToolRpc getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields, Integer relationship, Integer limitCustomFields);

    void saveAddViewPosition(Integer companyID, ViewAddFiledsCodeName viewFieldsCode, ArrayList<String> onlyViewShowfieldCodeName);

    String getMoreMenuSettings(String actionName);

    SelectItem saveCRMContactCompanyLogo(Integer uploadId, Integer crmContactId);

    SelectItem getCrmContactCompanyLogo();

    TwilioContactItem getIncomingCallerDetails(String phoneNumber);

    ArrayList<ContactTypeForTwilio> getOtherContactTypes(String phoneNumber);

    void deleteFacetFilter(Integer deleleFilterId);

    SaveFilterSelectItems getSavedFacetFilterList(ListPanelType type, Integer typeId);

    Integer saveFacetFilter(FacetFilterRpc facetFilter, ListPanelType type);

    /**
     * @param paramMap should contains FacetFilterRpc facetFilterRpc, ListPanelType type
     * @return
     */
    Integer saveFacetFilter(HashMap<String, Object> paramMap);

    CompanyCustomFieldItem checkCFNameExists(String type, String category, String fieldName, String aliasName, Integer fieldID, boolean isItemTable, String itemTableOrFormType);

    String getStudentImageURL(Integer studentID);

    String validateAddressByUSPS(Address address);

    String saveCrmAccountLogoUrl(Integer imageID, Integer objectId);

    Boolean removeLocalizationItem(Integer id);

    LinkedHashMap<String, LinkedHashMap<String, SelectItem>> getReportingTopMenu();

    ArrayList<CompanyCustomFieldItem> getCompanyStepCategoryCustomFields(Integer stepID);

    ArrayList<SelectItem> getOnboardingStepdList(ListingFilterParameter fp);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionEmployees(Integer positionID);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionDepartments(SelectItem position);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionsForKpiTree(SelectItem position);


    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getLocationsForKpiTree();


    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getDepartmentsForKpiTree(SelectItem department);

    String getTotalCharge(ProjectPosition po);

    String getCalculateUnitPrice(ProjectPosition po);

    SelectItem[] convertReference2SelectItem(String referenceName, boolean useSortOrder, String defaultSelection);

    AttendanceReportLogItem getEmployeePresentTime(String employeeID, String dateItem);

    Integer saveAttendanceHour(EmployeePresentItem item);

    ListResult<SelectItem> getCountriesList(ListingFilterParameter fp);

    SelectItem[] getVacancyTypeItems();

    SelectItem[] getReligionItems();

    SelectItem[] getReferenceItems(ListingFilterParameter fp);

    SelectItem[] getTaxTreatmentItems();

    Boolean checkProjectBillable(Integer projectId);

    CompanyAddress getCompanyAddress();

    SelectItem getCompanyDefaultCountry();

    SelectItem[] getInvoicesQuotesAsSelectItem(ListingFilterParameter fp);

    SelectItem[] getReferences(ReferenceParentEnum parentCode);

    SelectItem[] getReferences(String parentCode);

    EmployeeProfileItem getEmployeeProfile();

    HashMap<Integer, String[]> getExistingCustomFields(String entityName, String entityCategoryName, Integer objectID);

    CompanyCustomFieldItem getCustomFieldData(Integer objectID);

    CompanyCustomFieldItem getCustomFieldByAlias(String entityName, String alias);

    void saveCustomFields(CompanyCustomFieldItem item, boolean fromItemtable);

    void saveStaticFieldProperty(String formId, FormProperty formProperty);

    String saveCroppedImage(Integer entityId, String type, Integer imageID, int left, int top, int width, int height);

    DateNonConvertable getFinancialYearStart();

    String resetCompany();

    CompanyCustomFieldItem getCompanyCustomFieldByEntityNameAndFieldName(ViewName productServiceView, String article);

    SelectItem[] getSkillsForEmployeeAssignment();

    String setSideNavBarPosition(String position);

    String loadSideNavPosition();

    PlaceOfSupplyItem getPlaceOfSupply(String taxTreatmentType);

    SelectItem getDefaultPlaceOfSupply();

    String[] checkCFName(String entityName, String name, String uiType);

    void deleteCustomField(String entityName, String columnCode);

    String saveCustomField(CustomizeFormItem field);

    Integer[] getCustomFieldsCount(String formID);

    void updateCustomField(String entityName, String name, boolean mandatory);

    CompanyCustomFieldItem getCustomFieldByEntityNameAndColumnCode(String entityName, String columnCode);

    CustomFormItem getCustomForm(Integer objectId);

    ArrayList<SelectItem> getCustomForms();

    Email getEmailbyTrackerid(Integer trackerId);

    SelectItem[] getCustomFormItemsForLookUp(String form_id);

    String saveCustomForm(CustomFormItem item);

    ListResult<FormItems> getCustomFormItems(ListingFilterParameter filterParameter);

    Integer saveCustomFormItem(FormItems item);

    FormItems getCustomFormItem(Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String convertFormType, Integer convertFormId);

    CustomTableRpc[] getCustomItemTable(Integer id, String uuid);

    CustomTableRpc[] getOpportunityItemtable(Integer id, String uuid);

    CustomTableRpc[] getEmployeeItemtable(Integer id, String uuid);

    CustomTableRpc[] getPlacementItemtable(Integer id, String uuid);

    CustomTableRpc[] getProjectItemtable(Integer id, String uuid);

    CustomTableRpc[] getCandidateItemTable(Integer id, String uuid);

    ArrayList<CompanyCustomFieldItem> getCompanyCategoryCustomFields(Integer formID);

    String getMaxValueOfAutoNumbering(CompanyCustomFieldItem columnCode);

    List<HistoryNote> getCustomFormItemHistoryNotes(Integer id, String viewType);

    Integer createCustomFormItemNote(Integer transferID, HistoryListItem hisItem);

    Boolean deleteCustomFormItemNote(Integer bankTransferID);

    void deleteCustomFormItem(Integer objectId);

    String checkCustomFormAttributeCount(String fieldType, String formID);

    ArrayList<CustomFormAttributeItem> getCustomFormAttributes(String formId);

    void approveOrRejectCustomFormItem(Integer objectId, String statusCode);

    Boolean isEnableApprovers(String formId);

    String getDynamicImageUrl(Integer id);

    void saveCustomFormItemCellValue(FormItems rowValue, String columnCodeName);

    void deleteZoomCall(Integer zoomId, String Url);

    SelectItem[] getSerialNumbers(ListingFilterParameter fp);

    SelectItem[] getBatchType(ListingFilterParameter fp);

    SelectItem[] getBatchWarehouse(ListingFilterParameter fp);

    ArrayList<SelectItem> getDynamicCustomForms(String lookUpType, boolean isCrmAccount);

    void memorizedCustomFormItem(Integer objectID, String formID);

    LinkedHashMap<String, FormProperty> getFormProperty(String caseForm);

    CompanyCustomFieldItem getCompanyCustomFieldById(Integer objectId);

    SelectItem[] getBankAccountCurrencyList(ListingFilterParameter fp);

    SelectItem[] getCompanyAllDropDownCustomFiedsByEntityName(String entityName, Integer objectId, String costumFieldAliceName, boolean isCutomForm);

    FormItems getCustomFormTimerItems(String formId);

    CustomFormLocalization getCFLocalization(Integer id, LocalizationTypeEnum type);

    ArrayList<String> getItemTableCustomForms(String itemTable, Integer companyId);

    LinkedHashMap<String, ArrayList<SelectItem>> getColumnsItems(ArrayList<String> columns);

    Boolean customFormIsQuizForm(String formId);

    CompanyCFAndFormItems getCustomFormCfAndItem(ViewName viewName, Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String convertFormType, Integer convertFormId);

    KanbanItemColumnConfigs[] getKanbanColumnConfigs(KanbanItemSettingEnum settingEnum);

    Integer saveKanbanItemSettings(KanbanItemSettingEnum settingEnum, KanbanItemColumnConfigs[] columnConfigs) throws ObjectNotFoundException;

    SelectItem[] getRelatedFieldsBySectionName(String sectionCode);

    HashMap<String, KanbanItemColumnConfigs[]> getKanbanItemFieldsAsMap();

    CustomTableRpc[] getVacancyItemtable(Integer id, String uuid);

    QuickAddColumnConfigs[] getQuickAddColumnConfigs(QuickAddSettingsForm form);

    Integer saveQuickAddSettings(QuickAddSettingsForm form, QuickAddColumnConfigs[] columnConfigs) throws ObjectNotFoundException;

    HashMap<String, SelectItem> getItemTableSettingsMap(String formId);


    HashMap<String, ArrayList<CustomTableRpc>> getItemTableValues(String formId, ArrayList<SelectItem> item);

    SelectItem getUserLocation();

    ArrayList<AttendanceTerminal> getAttendanceTerminals();

    ArrayList<AttendanceTerminal> getAttendanceTerminals(String searchKey);

    AttendanceTerminal getAttendanceTerminal(Integer id);

    Integer saveAttendanceTerminal(AttendanceTerminal domain);

    void deleteAttendanceTerminal(Integer id);

    ArrayList<AttendanceDeviceStatus> fetchAttendanceTerminalStatus();

    class App {
        public static CommonServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/common");
            return (CommonServiceAsync) target;
        }
    }
}
