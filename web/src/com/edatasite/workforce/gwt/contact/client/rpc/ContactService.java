package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.crm.client.rpc.ContactCareerItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 4:54:05 PM
 */
public interface ContactService extends RemoteService {

    Integer enableAccess(Integer contactID, boolean enable);

    Integer saveCandidate(ContactListItem item);

    Integer updateProfile(ProfileItem item);

    ProfileItem editProfile(Integer objectID);

    ProfileItem editProfile(Integer objectID, String from, boolean isView);

    ProfileItem editProfile(Integer objectID, String from, boolean isView, Integer placementId, String fromType, Integer convertedFormId);

    ProfileItem getProfile(Integer objectID);

    ListResult<ContactListItem> getNewContactList(ListingFilterParameter filterParameter);

    ContactList getContactList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    Integer saveContact(ContactListItem item, ArrayList<Integer> mailList, boolean runWebhook);

    SelectItem saveContact(ContactListItem item, ArrayList<Integer> mailList);

    HashMap<ContactListItem, Integer> saveMultipleContacts(ArrayList<ContactListItem> contacts, boolean runWebhook);

    boolean validateUserGoogle() throws Exception;

    boolean validateUserOffice() throws Exception;

    ContactListItem[] getGoogleContacts(boolean allGoogleContacts) throws Exception;

    ContactListItem[] getWFTContacts(boolean allWFTContacts) throws Exception;

    void exportToGoogleContact(ContactListItem[] items, boolean forExport) throws Exception;

    void synchronizeContactsWithGoogle() throws Exception;

    String synchronizeContactsWithGoogleInBackground(String storageType) throws Exception;

    String synchronizeContactsInBackground(String thirdPartyService) throws Exception;

    void recurringSyncContactsWithGoogle(Integer employeeId);

    void importGoogleContacts(ContactListItem[] items, boolean fromImportView);

    ArrayList<Integer> deleteContacts(ArrayList<Integer> contactIDs, Integer ownerID, boolean deleteFromGoogle);

    ContactListItem editContact(int contactType, Integer objectId, Integer accountId, Integer webFormID, boolean forMobile);

    ContactListItem getContact(Integer objectId, Boolean fromMobile);

    ContactListItem[] getStatusHistory(Integer objectID, Integer contactType, boolean contactHistory);

    //void createSystemContactCategories(String from);

    //ContactCategoryListItem saveContactCategory(ContactCategoryListItem selectItem);

    //ArrayList<ContactCategoryListItem> getContactCategories();

    ContactListItem getContactDataForImport(String leadOrCandidatePermissionCode);

    //ArrayList<ContactCategoryListItem> getContactCategoriesWithPermissions();

    ContactCareerItem[] getContactCareers(Integer contactID);

    ContactCareerItem getContactCareer(Integer careerID);

    Integer saveContactCareer(ContactCareerItem careerItem);

    void deleteContactCareer(Integer careerID);

    SelectItem[] getCrmAccounts();

    PermissionHolder getContactPermission(Integer objectID);

    //Boolean deleteContactCategory(Integer categoryID, Integer selectedCategoryID, boolean moveWhitChildren);

    ArrayList<ContactCategoryListItem> getGoogleContactGroups(String storageType);

    void saveGoogleGroupsSettings(String storageType, GoogleGroupsSetting[] settings);

    ArrayList<GoogleGroupsSetting> getUserSettings(String storageType);

    Boolean hasContactCategorySettings(String serverType);

    //ArrayList<Integer> changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action);

    void saveContactEditCellValue(ContactListItem rowValue, String columnCodeName);

    ArrayList<SelectItem> getProjectVacancyItem(Integer objectID, Integer projectID);

    void saveContactSyncSettings(String type, String storageType);

    CrmAccountItem getContactAccount(Integer contactID);

    SelectItem getContactAccountSelect(Integer contactID);

    ProfileItem getEployeePdfTemplateList(Integer objectID);

    Boolean canDeleteCandidate(ArrayList<Integer> ids);

    class App {
        public static ContactServiceAsync get() {
            ServiceDefTarget target = GWT.create(ContactService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/contact");
            return (ContactServiceAsync) target;
        }
    }
}