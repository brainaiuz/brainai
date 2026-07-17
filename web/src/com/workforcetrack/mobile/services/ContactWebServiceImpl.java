package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.workforcetrack.mobile.rpc.base.MIntegerList;
import com.workforcetrack.mobile.rpc.base.MTreeSelectItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.contact.MCompanyNameList;
import com.workforcetrack.mobile.rpc.contact.MContactCategoryList;
import com.workforcetrack.mobile.rpc.contact.MContactList;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MCountryStates;
import com.workforcetrack.mobile.rpc.contact.MStateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/10/11
 * Time: 10:54 PM
 */

@Transactional
@Service("contactWebService")
public class ContactWebServiceImpl implements ContactWebService {//<MContactListItem, MContactList> {

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private CrmContactManager crmContactManager;

    @Override
    public MStateList getStatesByCountryID(Integer countryID) {
        SelectItem[] states = commonService.getRegions();
        if (countryID == null) {
            return new MStateList(states);
        }
        List<MSelectItem> resStates = new ArrayList<>();
        for (SelectItem selectItem : states) {
            if (countryID.toString().equals(selectItem.getDescription())) {
                resStates.add(new MSelectItem(selectItem));
            }
        }
        MStateList mStateList = new MStateList();
        mStateList.setStateList(resStates);
        return mStateList;
    }

    @Override
    public MCountryList getCountries() {
        SelectItem[] countries = commonService.getCountries();
        return new MCountryList(countries);
    }

    @Override
    public MStateList getStates() {
        SelectItem[] states = commonService.getRegions();
        return new MStateList(states);
    }

    @Override
    public MCountryStates getCountryAndStateList() {
        MCountryList mCountryList = getCountries();
        MStateList mStateList = getStates();

        return new MCountryStates(mCountryList, mStateList);

    }

    @Override
    public MCompanyNameList getCompanyList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCRM(true);
        if (MFilterParametrs.convert(filterParametrs, mFilterParametrs, false)) {
            return new MCompanyNameList(crmService.getLookUpItems(filterParametrs, ServerUtils.CRM_ACCOUNT).getList().toArray(new SelectItem[]{}));
        }
        return null;

    }

    @Override
    public MCompanyNameList getCompanyListByName(String companyName) {
        if (companyName == null) {
            return null;
        }
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setSearchKey(companyName);
        filterParametrs.setCRM(true);
        return new MCompanyNameList(crmService.getLookUpItems(filterParametrs, ServerUtils.CRM_ACCOUNT).getList().toArray(new SelectItem[]{}));
    }

    @Override
    public MCompanyNameList getCompanyListByName() {
        return getCompanyListByName("");
    }

    @Override
    public MContactCategoryList getContactCategory() {

        TreeSelectItem[] categories = ContactCategoryListItem.getAsTreeSelectItem(contactCategoryServiceLocal.getContactCategories());

        return new MContactCategoryList(categories);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean getPermissionForDelete(Integer contactId) {
        if (contactId == null) {
            return false;
        }
        Boolean canDelete = Boolean.FALSE;
        try {
            PermissionHolder permissionHolder = contactService.getContactPermission(contactId);
            EdsCrmContact crmContact = crmContactManager.get(contactId);
            EdsUser user = crmContactManager.getUser();
            canDelete = (permissionHolder != null && permissionHolder.isDelete()) || user.hasRoles(EdsRole.ADMIN) || crmContact.getOwner().equals(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canDelete;
    }

    @Override
    public MContactList synchronizeContactsWithOutlook(MContactList mContactList) throws Exception {

        if (mContactList == null || mContactList.getContactListItems() == null || mContactList.getContactListItems().size() == 0) {
            return null;
        }

        int start = 0;
        int totalCount = 1;
        int limit = 100;

        MFilterParametrs filterParametrs = new MFilterParametrs();
        MContactList wftContactList = null;

        Map<Integer, MContactListItem> wftContactsMapByHashCode = new HashMap<>();
        Map<Integer, MContactListItem> newContactListMap = new HashMap<>();

        //For Response
        Map<Integer, MContactListItem> changedContactListMap = new HashMap<>();

        EdsUser user = crmContactManager.getUser();

        List<MContactListItem> mContactListItems = mContactList.getContactListItems();

        while (totalCount > start) {
            //Getting contactList by limit
            filterParametrs.setStart(start);
            filterParametrs.setLimit(limit);
            wftContactList = getList(filterParametrs);

            if (wftContactList != null && wftContactList.getContactListItems() != null && wftContactList.getContactListItems().size() > 0) {
                for (MContactListItem wftContactListItem : wftContactList.getContactListItems()) {
                    StringBuilder contactInfo = new StringBuilder();
                    if (wftContactListItem.getLastName() != null || !"".equals(wftContactListItem.getLastName())) {
                        contactInfo.append(wftContactListItem.getLastName().replace(" ", ""));
                    }
                    if (wftContactListItem.getFirstName() != null || !"".equals(wftContactListItem.getFirstName())) {
                        contactInfo.append(wftContactListItem.getFirstName().replace(" ", ""));
                    }
                    if (wftContactListItem.getPrimaryEmail() != null || !"".equals(wftContactListItem.getPrimaryEmail())) {
                        contactInfo.append(wftContactListItem.getPrimaryEmail().replace(" ", ""));
                    }

                    if (contactInfo != null && !"".equals(contactInfo.toString().trim())) {
                        Integer hashCode = contactInfo.toString().trim().hashCode();
                        wftContactsMapByHashCode.put(hashCode, wftContactListItem);
                    }
                }
            }

            if (mContactListItems != null && mContactListItems.size() > 0) {
                int i = 0;
                for (MContactListItem mContactListItem : mContactListItems) {
                    if (!mContactListItem.getOwnerID().equals(user.getObjectID())) {
                        continue;
                    }
                    i++;
                    StringBuilder contactInfo = new StringBuilder();
                    if (mContactListItem.getLastName() != null || !"".equals(mContactListItem.getLastName())) {
                        contactInfo.append(mContactListItem.getLastName().replace(" ", ""));
                    }
                    if (mContactListItem.getFirstName() != null || !"".equals(mContactListItem.getFirstName())) {
                        contactInfo.append(mContactListItem.getFirstName().replace(" ", ""));
                    }
                    if (mContactListItem.getPrimaryEmail() != null || !"".equals(mContactListItem.getPrimaryEmail())) {
                        contactInfo.append(mContactListItem.getPrimaryEmail().replace(" ", ""));
                    }

                    if (contactInfo != null && !"".equals(contactInfo.toString().trim())) {
                        Integer hashCode = contactInfo.toString().trim().hashCode();

                        if (!wftContactsMapByHashCode.containsKey(hashCode)) {
                            //newContactList.add(mContactListItem);
                            newContactListMap.put(hashCode, mContactListItem);
                        } else {
                            newContactListMap.remove(hashCode);
                            if (mContactListItem.getUpdatedDate() != null && wftContactsMapByHashCode.get(hashCode).getUpdatedDate() != null &&
                                    mContactListItem.getUpdatedDate().getTime() > wftContactsMapByHashCode.get(hashCode).getUpdatedDate().getTime()) {
                                changedContactListMap.put(hashCode, mContactListItem);
                            } else {
                                changedContactListMap.put(hashCode, wftContactsMapByHashCode.get(hashCode));
                            }
                        }
                    } else {
                        if (!newContactListMap.containsKey(i)) {
                            newContactListMap.put(i, mContactListItem);
                        }

                    }
                }
            }

            totalCount = wftContactList.getTotalCount();
            start += limit;
        }

        //We save the different(new) contacts form OutlookContact to WftContacts
        List<ContactListItem> contactListItemsToSave = new ArrayList<>();
        for (MContactListItem mContactListItem : newContactListMap.values()) {
            save(mContactListItem);
        }


        //contactService.saveMultipleContacts(contactListItemsToSave.toArray(new ContactListItem[]{}));

        // Export logic
        return new MContactList((List<MContactListItem>) changedContactListMap.values(), changedContactListMap.size());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MContactList syncDeletedContactsByOutlook(List<MContactListItem> mContactListItems) throws Exception {
        int start = 0;
        int totalCount = 1;
        int limit = 100;

        MFilterParametrs filterParametrs = new MFilterParametrs();
        MContactList wftContactList = null;

        Map<Integer, MContactListItem> wftContactsMapByHashCode = new HashMap<>();
        Map<Integer, MContactListItem> newContactListMap = new HashMap<>();

        //For Response
        Map<Integer, MContactListItem> changedContactListMap = new HashMap<>();
        Map<Integer, Integer> deletedContactsMap = new HashMap<>();
        EdsUser user = crmContactManager.getUser();

        while (totalCount > start) {

            //Getting contactList by limit
            filterParametrs.setStart(start);
            filterParametrs.setLimit(limit);
            wftContactList = getList(filterParametrs);

            if (wftContactList != null && wftContactList.getContactListItems() != null && wftContactList.getContactListItems().size() > 0) {
                for (MContactListItem wftContactListItem : wftContactList.getContactListItems()) {
                    StringBuilder contactInfo = new StringBuilder();
                    if (wftContactListItem.getLastName() != null || !"".equals(wftContactListItem.getLastName())) {
                        contactInfo.append(wftContactListItem.getLastName().replace(" ", ""));
                    }
                    if (wftContactListItem.getFirstName() != null || !"".equals(wftContactListItem.getFirstName())) {
                        contactInfo.append(wftContactListItem.getFirstName().replace(" ", ""));
                    }
                    if (wftContactListItem.getPrimaryEmail() != null || !"".equals(wftContactListItem.getPrimaryEmail())) {
                        contactInfo.append(wftContactListItem.getPrimaryEmail().replace(" ", ""));
                    }

                    if (contactInfo != null && !"".equals(contactInfo.toString().trim())) {
                        Integer hashCode = contactInfo.toString().trim().hashCode();
                        wftContactsMapByHashCode.put(hashCode, wftContactListItem);
                    }
                }
            }

            if (mContactListItems != null && mContactListItems.size() > 0) {
                int i = 0;
                for (MContactListItem mContactListItem : mContactListItems) {
                    if (!mContactListItem.getOwnerID().equals(user.getObjectID())) {
                        continue;
                    }
                    i++;
                    StringBuilder contactInfo = new StringBuilder();
                    if (mContactListItem.getLastName() != null || !"".equals(mContactListItem.getLastName())) {
                        contactInfo.append(mContactListItem.getLastName().replace(" ", ""));
                    }
                    if (mContactListItem.getFirstName() != null || !"".equals(mContactListItem.getFirstName())) {
                        contactInfo.append(mContactListItem.getFirstName().replace(" ", ""));
                    }
                    if (mContactListItem.getPrimaryEmail() != null || !"".equals(mContactListItem.getPrimaryEmail())) {
                        contactInfo.append(mContactListItem.getPrimaryEmail().replace(" ", ""));
                    }

                    if (contactInfo != null && !"".equals(contactInfo.toString().trim())) {
                        Integer hashCode = contactInfo.toString().trim().hashCode();

                        if (!wftContactsMapByHashCode.containsKey(hashCode)) {
                            //newContactList.add(mContactListItem);
                            newContactListMap.put(hashCode, mContactListItem);
                            deletedContactsMap.put(hashCode, mContactListItem.getObjectID());
                        } else {
                            if (newContactListMap.containsKey(hashCode)) {
                                newContactListMap.remove(hashCode);
                                deletedContactsMap.remove(hashCode);
                            }
                            if (mContactListItem.getUpdatedDate() != null && wftContactsMapByHashCode.get(hashCode).getUpdatedDate() != null &&
                                    mContactListItem.getUpdatedDate().getTime() > wftContactsMapByHashCode.get(hashCode).getUpdatedDate().getTime()) {

                                changedContactListMap.put(hashCode, mContactListItem);
                            } else {
                                changedContactListMap.put(hashCode, wftContactsMapByHashCode.get(hashCode));
                            }

                        }
                    } else {
                        if (!newContactListMap.containsKey(i)) {
                            newContactListMap.put(i, mContactListItem);
                            deletedContactsMap.put(i, mContactListItem.getObjectID());
                        }

                    }
                }
            }

            totalCount = wftContactList.getTotalCount();
            start += limit;
        }

        //We save the different(new) contacts form OutlookContact to WftContacts
        List<ContactListItem> contactListItemsToSave = new ArrayList<>();
        for (MContactListItem mContactListItem : newContactListMap.values()) {
            delete(mContactListItem.getObjectID(), user.getObjectID());
        }


        //contactService.saveMultipleContacts(contactListItemsToSave.toArray(new ContactListItem[]{}));

        // Export logic
        return new MContactList((List<MContactListItem>) changedContactListMap.values(), changedContactListMap.size());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MContactList getNewList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        ListResult<ContactListItem> newContactList = getNewContactList(mFilterParametrs);
        MContactList mContactList = new MContactList();
        if (newContactList != null && newContactList.getList() != null && newContactList.getList().size() > 0) {
            mContactList.setContactListItems(MContactList.getContactListItemsForOutlook(newContactList.getList()));
            mContactList.setTotalCount(newContactList.getTotal());
        }

        return mContactList;
    }

    private ListResult<ContactListItem> getNewContactList(MFilterParametrs fp) {
        ListingFilterParameter listingFilterParameter = fp.convertToListingFilterParameter(null);

        FacetFilterRpc facetFilter = new FacetFilterRpc(getContactColumnCode(), getContactSolrField());
        facetFilter.setType(ListPanelType.ContactListPanel);
        if (fp.getCountryIDList() != null && fp.getContactCategoryIDList().size() > 0) {
            List<SelectItem> countryList = new ArrayList<>();
            for (Integer countryID : fp.getCountryIDList()) {
                if (countryID != null && countryID != 0) {
                    countryList.add(new SelectItem(countryID));
                }
            }

            FacetContentRpc countryFacet = facetFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[0]);
            countryFacet.setFacetItems(countryList.toArray(new SelectItem[]{}));


        }

        if (fp.getContactCategoryIDList() != null && fp.getContactCategoryIDList().size() > 0) {
            List<SelectItem> categoryList = new ArrayList<>();
            for (Integer categoryID : fp.getContactCategoryIDList()) {
                if (categoryID != null && categoryID != 0) {
                    categoryList.add(new SelectItem(categoryID));
                }
            }

            FacetContentRpc categoryFacet = facetFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[5]);
            categoryFacet.setFacetItems(categoryList.toArray(new SelectItem[]{}));
        }

        listingFilterParameter.setFacetFilter(facetFilter);
        listingFilterParameter.setBriefly(false);

        return contactService.getNewContactList(listingFilterParameter);
    }

    @Override
    public MContactList getListForExcel(MFilterParametrs filterParametrs) {
        if (filterParametrs == null) {
            return null;
        }

        ListResult<ContactListItem> newContactList = getNewContactList(filterParametrs);
        MContactList mContactList = new MContactList();
        if (newContactList != null && newContactList.getList() != null && newContactList.getList().size() > 0) {
            mContactList.setContactListItems(MContactList.getContactListItemsForExcel(newContactList.getList()));
            mContactList.setTotalCount(newContactList.getTotal());
        }

        return mContactList;
    }

    @Override
    public MContactListItem getForExcel(Integer objectID) {
        ContactListItem contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, objectID, null, null, true);
        if (contactListItem != null) {
            return MContactListItem.convertContactToExcel(contactListItem);
        }
        return null;
    }

    private HashMap<String, FacetSolrField> getContactSolrField() {
        HashMap<String, FacetSolrField> contactSolrField = new HashMap<>();
        contactSolrField.put(FacetContentType.ContactFacetFilter.getContentCode()[0], new FacetSolrField(SolrContactRepresenter.FIELD_COUNTRY_ID, SolrContactRepresenter.FIELD_COUNTRY_ID_CODE, LocalizationType.REFERENCE));
        contactSolrField.put(FacetContentType.ContactFacetFilter.getContentCode()[5], new FacetSolrField(SolrContactRepresenter.FIELD_CATEGORY_ID, SolrContactRepresenter.FIELD_CATEGORY_ID_NAME, LocalizationType.REFERENCE));
        return contactSolrField;
    }

    private ArrayList<String> getContactColumnCode() {
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.ContactFacetFilter.getContentCode()));
        return resultList;
        //return (ArrayList<String>)Arrays.asList(FacetContentType.ContactFacetFilter.getContentCode());
    }

    @Override
    public MContactList getList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        MFilterParametrs.convert(filterParametrs, mFilterParametrs, false);
        ListLoadConfig listLoadConfig = new ListLoadConfig();
        MFilterParametrs.convertToListLoadConfig(listLoadConfig, mFilterParametrs, false);
        //filterParametrs1.setAllByFilter(true);
        //filterParametrs.setBriefly(true);
        ContactList contactList = contactService.getContactList(filterParametrs, listLoadConfig);
        MContactList resultList = new MContactList();
        if (contactList != null && contactList.getContactListItems() != null && contactList.getContactListItems().length > 0) {
            resultList.setContactListItems(new ArrayList<>());
            resultList.setTotalCount(contactList.getTotalCount());
            for (ContactListItem contactListItem : contactList.getContactListItems()) {
                resultList.getContactListItems().add(MContactListItem.convertToMobile(contactListItem, false, true));
            }
        }
        return resultList;
    }

    @Override
    public MContactList getList(ArrayList<Integer> objectIDs) {
        if (objectIDs != null && objectIDs.size() > 0) {
            ListingFilterParameter lfp = new ListingFilterParameter();
            lfp.setBriefly(false);
            ContactList contactList = contactServiceLocal.getContactsByIDsFromDB(lfp, objectIDs);
            MContactList resultList = new MContactList();
            if (contactList != null && contactList.getContactListItems() != null && contactList.getContactListItems().length > 0) {
                resultList.setContactListItems(new ArrayList<>());
                resultList.setTotalCount(contactList.getTotalCount());
                for (ContactListItem contactListItem : contactList.getContactListItems()) {
                    resultList.getContactListItems().add(MContactListItem.convertToMobile(contactListItem, false, true));
                }
            }
            return resultList;
        }
        return null;
    }

    @Override
    public MContactListItem get(Integer objectID) {
        if (objectID == null || objectID == 0) {
            return null;
        }

        ContactListItem contactListItem = contactService.getContact(objectID, true);
        return MContactListItem.convertToMobile(contactListItem, false, false);
    }

    @Override
    public MContactListItem edit(Integer objectID) {

        ContactListItem contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, objectID, null, null, true);
        MContactListItem mContactListItem = MContactListItem.convertToMobile(contactListItem, false, false);
        if (contactListItem.getCategories() != null) {
            ArrayList<MTreeSelectItem> mTreeSelectItems = new ArrayList<>();
            List<TreeSelectItem> list = TreeSelectItem.withoutTreeCapability(new ArrayList<TreeSelectItem>(Arrays.asList(contactListItem.getCategories())));
            for (TreeSelectItem treeSelectItem : list) {
                mTreeSelectItems.add(new MTreeSelectItem(treeSelectItem));
            }
            mContactListItem.setContactCategories(mTreeSelectItems);
        }
        return mContactListItem;
    }

    @Override
    public MContactListItem edit() {
        return edit(null);
    }

    @Override
    public Integer save(MContactListItem item) {
        if (item == null) {
            return null;
        }
        try {
            ContactListItem contactListItem = null;
            if (item.getObjectID() != null && item.getObjectID() != 0) {
                contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, item.getObjectID(), null, null, true);
            }

            contactListItem = item.convertFromMobile(contactListItem, false);

            return contactService.saveContact(contactListItem, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    public MIntegerList saveList(MContactList items) {
        if (items == null || items.getContactListItems() == null || items.getContactListItems().size() == 0) {
            return null;
        }
        ArrayList<Integer> results = new ArrayList<>();
        for (MContactListItem item : items.getContactListItems()) {
            results.add(save(item));
        }

        return new MIntegerList(results);
    }

    @Override
    public Boolean delete(Integer objectID, Integer ownerID) {
        if (objectID == null || ownerID == null || objectID == 0) {
            return null;
        }
        try {
            ArrayList<Integer> contactIDs = new ArrayList<>();
            contactIDs.add(objectID);
            contactIDs = contactService.deleteContacts(contactIDs, ownerID, false);
            return (contactIDs.size() == 0);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MIntegerList deleteList(ArrayList<Integer> objectIDs) {
        EdsUser user = crmContactManager.getUser();
        if (user != null) {
            return deleteList(objectIDs, user.getObjectID());
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MIntegerList deleteList(ArrayList<Integer> objectIDs, Integer ownerID) {
        MIntegerList resultList = new MIntegerList();
        try {
            ArrayList<Integer> contactIDs = contactService.deleteContacts(new ArrayList<Integer>(objectIDs), ownerID, false);
            resultList.setResult(contactIDs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultList;
    }

    @Override
    public Integer saveContactForOutlook(MContactListItem item) {
        if (item == null) {
            return null;
        }
        try {
            ContactListItem contactListItem = null;
            if (item.getObjectID() != null && item.getObjectID() != 0) {
                contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, item.getObjectID(), null, null, true);
            }

            contactListItem = item.convertFromOutlook(contactListItem);
            return contactService.saveContact(contactListItem, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer saveForExcel(MContactListItem item) {
        if (item == null) {
            return null;
        }
        try {
            ContactListItem contactListItem = null;
            if (item.getObjectID() != null && item.getObjectID() != 0) {
                contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, item.getObjectID(), null, null, true);
            }
            contactListItem = item.convertContactFromExcel(contactListItem);
            return contactService.saveContact(contactListItem, null, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -3;
        }

    }

    @Override
    public MIntegerList saveListForExcel(MContactList contactList) {
        if (contactList == null || contactList.getContactListItems() == null || contactList.getContactListItems().size() == 0) {
            return null;
        }
        MIntegerList resultList = new MIntegerList();
        for (MContactListItem contact : contactList.getContactListItems()) {
            resultList.getResult().add(saveForExcel(contact));
        }
        return resultList;
    }

    @Override
    public MContactList getSyncList(MFilterParametrs filterParametrs) {
        if (filterParametrs == null) {
            return null;
        }
        ListingFilterParameter lfp = filterParametrs.convertToListingFilterParameter(null);
        lfp.setDeviceID(filterParametrs.getDeviceID());
        if (filterParametrs.getContactCategoryIDList() != null && filterParametrs.getContactCategoryIDList().size() > 0) {
            lfp.setCategories(filterParametrs.getContactCategoryIDList().toArray(new Integer[]{}));
        }
        List<MContactListItem> contactListItems = crmContactManager.getContactIDsWithStatuses(lfp);
        Integer totalCount = crmContactManager.getContactIDsWithStatusesCount(lfp);
        return new MContactList(contactListItems, totalCount);
    }

    /*
    @Override
    public MContactList syncMobileContacts(MContactList mobileContacts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

  @Override
    @Transactional
    public EdsDeviceCrmContact getDeviceContact(String deviceID, Integer contactID) {
        EdsCrmContact crmContact = crmContactManager.get(contactID);
        EdsDeviceCrmContact deviceCrmContact = crmContact.getDeviceCrmContact(deviceID, true);
        if (deviceCrmContact == null) {
            deviceCrmContact = new EdsDeviceCrmContact();
            deviceCrmContact.setContactID(contactID);
            deviceCrmContact.setDeviceID("3");
            deviceCrmContact.setStatus(ContactListItem.NEW);
            crmContact.getDeviceCrmContacts().add(deviceCrmContact);
        }
        return deviceCrmContact;
    }*/


    /* @Override
    public MContactList syncMobileContacts(MContactList mobileContacts) {
        List<MContactListItem> mergedContacts = new ArrayList<MContactListItem>();

        Map<String, EdsCrmContact> syncedContacts = new HashMap<String, EdsCrmContact>();
        Map<String, EdsCrmContact> kpiContacts = new HashMap<String, EdsCrmContact>();
        Map<String, ContactListItem> mergeContacts = new HashMap<String, ContactListItem>();


        if (mobileContacts != null && mobileContacts.getContactListItems() != null && mobileContacts.getContactListItems().size() > 0) {
            int i = 0;
            syncedContacts = getContactsMapByIDs(mobileContacts.getContactListItems(), false);
            for (MContactListItem mobileContact : mobileContacts.getContactListItems()) {
                if (!validateContactForSync(mobileContact)) {
                    continue;
                }
                List<EdsCrmContact> existingContacts = null;//crmContactManager.getContactsByiPhoneIDs(mobileContact.getiPhoneID(), mobileContact.getLastName(), mobileContact.getFirstName(), mobileContact.getPrimaryEmail());
                if (existingContacts != null && existingContacts.size() > 0) {
                    ContactListItem contactListItem = contactService.editContact(ContactListItem.CRM_CONTACT, existingContacts.get(0).getObjectID(), null, true);
                    contactListItem = mergeContact(mobileContact, contactListItem, false);
                    Integer contactID = contactService.saveContact(contactListItem, null);
                    contactListItem.setObjectId(contactID);
                    mergedContacts.add(MContactListItem.convertToMobile(contactListItem, false, false));
                } else {
                    save(mobileContact);
                }
                i++;
                if (i >= 20) {
                    i = 0;
                    jpaTemplate.flushAndClear();
                }
            }
        }
        //ContactList contactList = contactService.getContactsByIDsFromDB(fp);
        MContactList resultList = new MContactList(mergedContacts, mergedContacts.size());
        return resultList;
    }

    private boolean validateContactForSync(MContactListItem contactListItem) {
        if (contactListItem == null || contactListItem.getFirstName() == null || "".equals(contactListItem.getFirstName().trim()) ||
                contactListItem.getLastName() == null || "".equals(contactListItem.getLastName().trim())) {
            return false;
        }
        return true;
    }

    private ContactListItem mergeContact(MContactListItem mobileContact, ContactListItem wftContact, boolean isAndroid) {
        ContactListItem resultContact = new ContactListItem();
        resultContact.setiPhoneID(mobileContact.getiPhoneID());
        if (isAndroid) {

        } else {

        }
        return resultContact;
    }

    private Map<String, EdsCrmContact> getContactsMap(List<EdsCrmContact> contacts, boolean withDeviceID) {
        Map<String, EdsCrmContact> resultMap = new HashMap<String, EdsCrmContact>();
        if (contacts != null && contacts.size() > 0) {
            StringBuffer contactInfo = new StringBuffer();
            for (EdsCrmContact contact : contacts) {
                if (withDeviceID) {
                    resultMap.put(contact.getiPhoneID(), contact);
                } else {
                    contactInfo.setLength(0);
                    if (contact.getLastName() != null || !"".equals(contact.getLastName())) {
                        contactInfo.append(contact.getLastName().trim().replaceAll(" ", ""));
                    }
                    if (contact.getFirstName() != null || !"".equals(contact.getFirstName())) {
                        contactInfo.append(contact.getFirstName().trim().replaceAll(" ", ""));
                    }
                    if (contact.getPrimaryEmail() != null || !"".equals(contact.getPrimaryEmail())) {
                        contactInfo.append(contact.getPrimaryEmail().trim().replaceAll(" ", ""));
                    }
                    resultMap.put(contactInfo.toString(), contact);
                }
            }
        }
        return resultMap;
    }

    private Map<String, EdsCrmContact> getContactsMapByIDs(List<MContactListItem> contacts, boolean isAndroid) {
        Map<String, EdsCrmContact> resultMap = new HashMap<String, EdsCrmContact>();
        if (contacts != null && contacts.size() > 0) {
            StringBuffer contactInfo = new StringBuffer();
            List<String> contactDeviceIDs = new ArrayList<String>();
            for (MContactListItem contact : contacts) {
                if (isAndroid) {
                    if (contact.getAndroidID() != null && !"".equals(contact.getAndroidID())) {
                        contactDeviceIDs.add(contact.getAndroidID());
                    }
                } else {
                    if (contact.getiPhoneID() != null && !"".equals(contact.getiPhoneID())) {
                        contactDeviceIDs.add(contact.getiPhoneID());
                    }
                }
            }
            List<EdsCrmContact> contactsByDeviceID = null;//crmContactManager.getContactsByiPhoneIDs(contactDeviceIDs);
            if (contactDeviceIDs != null && contactDeviceIDs.size() > 0) {
                for (EdsCrmContact contact : contactsByDeviceID) {
                    resultMap.put(contact.getiPhoneID(), contact);
                }
            }
        }
        return resultMap;
    }
    */


    @Override
    public MIntegerList saveMultiple(MContactList items) {
        if (items == null || items.getContactListItems() == null || items.getContactListItems().size() == 0) {
            return null;
        }
        ArrayList<Integer> results = new ArrayList<>();
        for (MContactListItem item : items.getContactListItems()) {
            results.add(saveContactForOutlook(item));
        }

        return new MIntegerList(results);
    }

//    @Override
//    public Boolean delete(Integer objectID) {
//        return null;
//    }
}
