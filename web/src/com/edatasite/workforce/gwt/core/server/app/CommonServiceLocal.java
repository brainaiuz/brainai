package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ListPanelItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.UserRequestItemMQ;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.rest.v3.release10.settings.dto.UserSettingsDto;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectFile;

import java.io.File;
import java.util.*;

/**
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 20:37:25
 */
public interface CommonServiceLocal {

    TimeSlot getCurrentEmployeeTimeSlot();

    SelectItem[] getAddTaskStatusDrop();

    Integer saveZipFileForAttachment(File zipFile) throws Exception;

    FacetFilterRpc getUserFacetFilter(FacetFilterRpc facetFilter);

    FolderResource getFolderResource(int folderType, Integer entityId);

    void deleteFolder(final Integer folderId) throws InsufficientPermissionsException, ObjectNotFoundException;

    void deleteFile(Integer fileId);

    void deleteFiles(List<Integer> fileIds);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByCategory(ViewName viewName, String category);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFields(ViewName viewName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForListView(ViewName viewName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByCategoryForListView(ViewName viewName, String category);

    ArrayList<CompanyCustomFieldItem> getCompanyCategoryCustomFields(Integer formID);

    NewsComment[] getNotecomments(Integer objectId);

    NewsComment saveNoteComment(NewsComment data);

    Integer getFreeTrialDaysLeft(boolean isPaidCompany);

    void executeSubscriptionExpirationReport();

    String getImageUrl(Integer id);

    SelectItem[] reference2SelectItem(List<EdsReference> statuses, String defaultSelection);

    SelectItem[] referenceSelectItemWithPleaseSelect(List<EdsReference> statuses);

    ProjectCalendar createProjectCalendar(ProjectFile file, EdsTimeSlot defaultTimeSlot, String calendarName);

    TimeSlot getEmployeeTimeSlot(Integer employeeId);

    TimeSlot getEmployeeTimeSlot(Integer employeeId, Date date);

    void saveCompanyParent(Integer companyId, Integer parentCompanyId);

    String getCompaniesClusterType(Integer companyId);

    Map<Integer, String> getCompaniesClusterType(String companyIds);

    Map<Integer, String> getSubsidiariesCompanyClusterType(Integer parentId);

    String getFileUrl(Integer fileID);

    String getFileUrl(Integer fileId, String fileType, Boolean needHostForLocal);

    UsagePlanItem usagePlanSaveAndGetId(UsagePlanItem usagePlan);

    Double getUserRatePerHOST(/*Integer userCount, */String hostName, String pricingPackageName);

    Double getSupportPackagePricePerHostPerPackage(String hostName, String supportPackageNAME);

    void createProjectFolder(Integer projectId);

    void createExpensePaymentFolder(Integer projectId);

    void createMailMessageFolder(Integer mailMessageID);

    void indexCompanyFolders(SolrReindexRpc solrReindex);

    void indexFiles(SolrReindexRpc solrReindex);

    FolderResource getTempFolder();

    void removeDocumentEntries(Integer userId);

    void reIndexProjectDocument(Integer projectId);

    void reIndexTaskDocument(Integer taskId);

    String addGoogleSyncToQueue(String eventType);

    String addContactSyncToQueue(String eventType);

    void activeCompany(ConsolidationCompanyList rowValue);

    SelectItem[] getBonusRecommendationApprovers(Integer employeeID);

    void updateCompanyTimezones();

    SelectItem[] convertReference2SelectItem(String referenceName, boolean useSortOrder, String defaultSelection);

    SelectItem[] convertReference2SelectItem2(String referenceName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForFiltering(ViewName viewName);

    void deleteAttachment(Integer attachmentId);

    void deleteAttachment(Integer attachmentId, Integer companyId);

    SelectItem[] getCountries();

    SelectItem[] getRegions();

    SelectItem[] getRegions(Integer countryID);

    void createCustomFieldFolder(Integer objectID);

    List<EdsCompanyCustomFieldsSettings> getEdsCompanyCustomFieldsSettingses(Integer stepID, String viewName);

    List<ConsolidationCompanyList> getConsolidationCompanyList(Map<Integer, ConsolidationCompanyList> companyCosolidationMap, String ids, String dbName);

    Integer indexCompanyVacancy(SolrReindexRpc solrReindexRpc, Integer start, int limit);

    void saveEmployeePresentTimeFromAPI(Integer employeeID, Date startTime, Date endTime, Integer availableStatusID, boolean fingerprintEnabled, boolean isCustomFingerPrint);

    String saveImageUrl(Integer id, Integer userID);

    String saveCrmContactImageUrl(Integer id, Integer crmContactID);

    String getWFTPlugin(String pluginName);

    ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByRelationship(ViewName viewName, Integer relationship, Integer limitCustomFields);

    SaveFilterSelectItems getSavedFacetFilterList(ListPanelType type, Integer typeId);

    Integer saveFacetFilter(FacetFilterRpc facetFilter, ListPanelType type);

    void deleteFacetFilter(Integer filterId);

    SelectItem[] getReferences(ReferenceParentEnum parentCode);

    CompanyCustomFieldItem getValidCustomFieldItem(Map.Entry<Integer, String> extraColumnEntry, Integer columnId, String columnValue, RejectedImportRecord[] rejectedRow, String fieldName);

    DateNonConvertable getFinancialYearStart();

    void createWorkflowModule(String code, String name, boolean create);

    void indexCustomFormItems(ListingFilterParameter filterParameter);

    ArrayList<SelectItem[]> getCSVColumns(Integer attachmentid, Integer needrowcount);

    public ListResult<FormItems> getCustomFormItems(ListingFilterParameter fp);

    Integer saveCustomFormItem(FormItems item);

    FormItems getCustomFormItem(Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String formType, Integer convertFormId);

    String getMaxValueOfAutoNumbering(CompanyCustomFieldItem columnCode);

    String getCrmCaseSolrQuery(ListingFilterParameter fp, EdsCompany edsCompany, FacetFilterRpc facetFilter);

    List<CompanyCustomFieldItem> getCompanyAllCustomFields(ViewName viewName);

    HashMap<String, Object> getLocaledCustomFiledMap(HashMap<String, Object> map, List<CompanyCustomFieldItem> customFieldItems);

    UserSettingsDto getUserSettingsViwe(UserSettingsTypeEnum type, String viewType);

    void getFacetFilterWithLocale(HashMap<String, FacetContentRpc> map, List<CompanyCustomFieldItem> customFieldItems);

    void saveListPanelMq(ListPanelItemMQ message);

    void saveuserRequestTrackingMq(UserRequestItemMQ message);

    List<String> getCFsColumnCodeByUiTypes(ViewName viewName, List<String> uiTypes);

    EdsImportFile saveImportFile(ImportFile importFile, EdsUser employee);
}
