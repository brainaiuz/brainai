package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.crm.client.rpc.ContactCareerItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 4:56:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContactServiceAsync {

    void enableAccess(Integer contactID, boolean enable, AsyncCallback<Integer> callback);

    void saveCandidate(ContactListItem item, AsyncCallback<Integer> abstractAsyncCallback);

    void updateProfile(ProfileItem item, AsyncCallback<Integer> callback);

    void editProfile(Integer objectID, AsyncCallback<ProfileItem> callback);

    void editProfile(Integer objectID, String from, boolean isView, AsyncCallback<ProfileItem> callback);

    void getProfile(Integer objectID, AsyncCallback<ProfileItem> async);

    Request getNewContactList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ContactListItem>> callback);

    Request getContactList(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<ContactList> async);

    void getContactPermission(Integer objectID, AsyncCallback<PermissionHolder> async);

    void saveContact(ContactListItem item, ArrayList<Integer> mailList, boolean runWebhook, AsyncCallback<Integer> async);

    void saveContact(ContactListItem item, ArrayList<Integer> mailList, AsyncCallback<SelectItem> async);

    void saveMultipleContacts(ArrayList<ContactListItem> contacts, boolean runWebhook, AsyncCallback<HashMap<ContactListItem, Integer>> async);

    void validateUserGoogle(AsyncCallback<Boolean> async);

    void validateUserOffice(AsyncCallback<Boolean> async);

    void getGoogleContacts(boolean allGoogleContacts, AsyncCallback<ContactListItem[]> async);

    void getWFTContacts(boolean allWFTContacts, AsyncCallback<ContactListItem[]> async);

    void exportToGoogleContact(ContactListItem[] items, boolean forExport, AsyncCallback<Void> async);

    void synchronizeContactsWithGoogle(AsyncCallback<Void> async);

    void synchronizeContactsWithGoogleInBackground(String storageType, AsyncCallback<String> async);

    void synchronizeContactsInBackground(String storageType, AsyncCallback<String> async);

    void recurringSyncContactsWithGoogle(Integer employeeId, AsyncCallback<Void> async);

    void importGoogleContacts(ContactListItem[] items, boolean fromImportView, AsyncCallback<Void> async);

    void deleteContacts(ArrayList<Integer> contactIDs, Integer ownerID, boolean deleteFromGoogle, AsyncCallback<ArrayList<Integer>> async);

    void editContact(int contactType, Integer objectId, Integer accountId, Integer webFormID, boolean forMobile, AsyncCallback<ContactListItem> async);

    void getContact(Integer objectId, Boolean fromMobile, AsyncCallback<ContactListItem> async);

    void getStatusHistory(Integer objectID, Integer contactType, boolean contactHistory, AsyncCallback<ContactListItem[]> async);

    //void saveContactCategory(ContactCategoryListItem selectItem, AsyncCallback<ContactCategoryListItem> asyncCallback);

    //void getContactCategories(AsyncCallback<ArrayList<ContactCategoryListItem>> asyncCallback);

    void getContactDataForImport(String leadOrCandidatePermissionCode, AsyncCallback<ContactListItem> asyncCallback);

    //void getContactCategoriesWithPermissions(AsyncCallback<ArrayList<ContactCategoryListItem>> asyncCallback);

    //void createSystemContactCategories(String from, AsyncCallback<Void> async);

    void getContactCareers(Integer contactID, AsyncCallback<ContactCareerItem[]> callback);

    void getContactCareer(Integer careerID, AsyncCallback<ContactCareerItem> callback);

    void saveContactCareer(ContactCareerItem careerItem, AsyncCallback<Integer> callback);

    void deleteContactCareer(Integer careerID, AsyncCallback<Void> callback);

    void getCrmAccounts(AsyncCallback<SelectItem[]> callback);

    //void deleteContactCategory(Integer categoryID, Integer selectedCategoryID, boolean moveWhitChildren, AsyncCallback<Boolean> asyncCallback);

    void getGoogleContactGroups(String storageType, AsyncCallback<ArrayList<ContactCategoryListItem>> callback);

    void saveGoogleGroupsSettings(String storageType, GoogleGroupsSetting[] settings, AsyncCallback<Void> callback);

    void getUserSettings(String storageType, AsyncCallback<ArrayList<GoogleGroupsSetting>> callback);

    void hasContactCategorySettings(String serverType, AsyncCallback<Boolean> callback);

    //void changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action, AsyncCallback<ArrayList<Integer>> asyncCallback);

    void saveContactEditCellValue(ContactListItem rowValue, String columnCodeName, AsyncCallback callback);

    void getProjectVacancyItem(Integer objectID, Integer projectID, AsyncCallback<ArrayList<SelectItem>> callback);

    void saveContactSyncSettings(String type, String storageType, AsyncCallback<Void> abstractAsyncCallback);

    void getContactAccount(Integer contactID, AsyncCallback<CrmAccountItem> abstractAsyncCallback);

    void getContactAccountSelect(Integer contactID, AsyncCallback<SelectItem> async);

    void getEployeePdfTemplateList(Integer objectID, AsyncCallback<ProfileItem> callback);

    void canDeleteCandidate(ArrayList<Integer> ids, AsyncCallback<Boolean> callback);

    void editProfile(Integer objectID, String from, boolean isView, Integer placementId, String fromType, Integer convertedFormId, AsyncCallback<ProfileItem> async);
}