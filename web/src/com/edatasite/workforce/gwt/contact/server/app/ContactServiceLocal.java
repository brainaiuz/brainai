package com.edatasite.workforce.gwt.contact.server.app;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.gwt.contact.client.rpc.CommonItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.rest.base.to.AddressTO;
import com.edatasite.workforce.rest.base.to.ContactParamTO;
import com.edatasite.workforce.rest.base.to.ContactTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddZapierContactTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 20:37:25
 * To change this template use File | Settings | File Templates.
 */
public interface ContactServiceLocal {

    ContactList getContactsByIDsFromDBForExport(ListingFilterParameter filterParametrs, List<Integer> lessObjectIDs);

    ContactList getContactsByIDsFromDB(ListingFilterParameter fp, List<Integer> lessObjectIDs);

    Integer saveContact(ContactListItem contactItem, ArrayList<Integer> mailingList, EdsUser user, boolean savingFromCrmAccount, boolean runWebhook);

    Integer saveOffice365Contact(ContactListItem item, EdsUser user);

    void saveCandidateVacancies(ArrayList<SelectItem> itemVacancies, EdsCrmContact contact);

    Integer saveContactDetailsOfEmployee(EdsEmployee employee, ContactListItem contactListItem, Boolean isFromSignUp, Address... addresses);

    ContactListItem getRPCFromContactSolrDoc(ContactSolrDoc contactSolrDoc, ListingFilterParameter fp, Map<Integer, EdsContactCategory> categories, Map<Integer, EdsCountry> countries, boolean validateUserGoogle, boolean validateUserOffcie, Map<Integer, Boolean> forActivationLink, Map<Integer, Integer> hasPlacement, HashMap<Integer, EdsCrmAccount> crmAccountHashMap);

    void updateEdsCrmContactAndIndex(EdsCrmContact contact, boolean newCreated, EdsUser user);

    EdsCrmCustomFields saveCustomFields(EdsCrmCustomFields edsCrmCustomField, List<CompanyCustomFieldItem> customFieldItems);

    SelectItem[] getContactSelectItems(String parentCode);

    //ListResultTO<ContactTO> getContactListForAPI(ListingFilterParameter filter);

    SelectItem[] getAccountTypes();

    SelectItem[] getRelationships();

    List<AddressTO> getAddresses(Integer id);

    List<ContactParamTO> getContactParams(Integer id, Integer paramType);

    List<SelectItemTO> getContactParamsAsSelectItemTO(Integer id, Integer paramType);

    ContactTO getContactParams(Integer contactId);

    List<ContactParamTO> saveContactParams(ContactListItem item, Integer paramType);

    List<SelectItemTO> saveContactParamsAsSelectItemTO(ContactListItem item, Integer paramType);

    void deleteContactParam(Integer id);

    //List<Integer> getContactCategoryIDs(Integer id);

    void copyEmployeeCustomFields(EdsEmployeeCustomFields employeeCustomFields, EdsCrmContact contact);

    ListResult<ContactListItem> getNewContactList(ListingFilterParameter filterParameter);

    ContactListItem getContact(Integer objectId, Boolean fromMobile);

    Integer saveContact(ContactListItem item, ArrayList<Integer> mailList, boolean runWebhook);
    Integer getEntityIdByLeadId(Integer leadId);

    ArrayList<Integer> deleteContacts(ArrayList<Integer> contactIDs, Integer ownerID, boolean deleteFromGoogle);

    //ContactCategoryListItem saveContactCategory(ContactCategoryListItem selectItem);

    ContactListItem[] getStatusHistory(Integer objectID, Integer contactType, boolean contactHistory);

    //ArrayList<ContactCategoryListItem> getContactCategories();

    Integer saveCandidate(ContactListItem item);

    ContactListItem getKanbanLeadFromSolrDoc(ContactSolrDoc contactSolrDoc);

    ContactListItem getKanbanLeadFromSolrDoc(SolrDocument doc);

    ContactListItem getKanbanCandidateFromSolrDoc(SolrDocument doc, EdsUser user);

    Integer saveCompanyContactsToSpeadsheedFile(String fileName, Integer companyId, Sheet sh, Integer rowCount);

    ContactListItem editContact(int contactType, Integer objectId, Integer accountId, Integer webFormID, boolean forMobile);

    PermissionHolder getContactPermission(Integer objectID);

    void saveContactEditCellValue(ContactListItem rowValue, String columnCodeName);

    void updateContactsWithNoCategories();

    SelectItem[] getCandidatesAsSelectItem(ListLoadConfig config, ListingFilterParameter filterParametrs);

    ArrayList<CommonItem> getCandidatePerVacancyChartData();

    ArrayList<PhoneTO> convertToPhoneTO(EdsCrmContact contact);

    ArrayList<String> convertContactEmails(EdsCrmContact contact);

    CrmAccountTO convertCompany(EdsCrmAccount company);

    ArrayList<EntityContactAddressTO> convertAddresses(EdsCrmContact contact);

    AddZapierContactTO convertToContact(EdsCrmContact contact);

    com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO convertToContactTO(EdsCrmContact edsCrmContact);

    void deleteLead(Integer id);

}
