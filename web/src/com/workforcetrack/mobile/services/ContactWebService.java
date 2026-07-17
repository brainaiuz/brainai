package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.base.MIntegerList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.contact.MCompanyNameList;
import com.workforcetrack.mobile.rpc.contact.MContactCategoryList;
import com.workforcetrack.mobile.rpc.contact.MContactList;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MCountryStates;
import com.workforcetrack.mobile.rpc.contact.MStateList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/10/11
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ContactWebService {//<MContactListItem, MContactList> extends CRUD<MContactListItem, MContactList> {

    Boolean getPermissionForDelete(Integer contactID);

    MContactList synchronizeContactsWithOutlook(MContactList mContactList) throws Exception;

    MContactList syncDeletedContactsByOutlook(List<MContactListItem> mContactListItems) throws Exception;

    MCountryList getCountries();

    MStateList getStates();

    MStateList getStatesByCountryID(Integer countryID);

    MCountryStates getCountryAndStateList();

    MCompanyNameList getCompanyList(MFilterParametrs mFilterParametrs);

    MCompanyNameList getCompanyListByName(String companyName);

    MCompanyNameList getCompanyListByName();

    //Repositories


    MContactCategoryList getContactCategory();

    Integer saveContactForOutlook(MContactListItem item);

    MIntegerList saveMultiple(MContactList items);

    //CRUD methods
    MContactList getNewList(MFilterParametrs filterParametrs);

    MContactList getList(MFilterParametrs filterParametrs);

    MContactList getList(ArrayList<Integer> objectIDs);

    MContactListItem get(Integer objectID);

    MContactListItem edit(Integer objectID);

    MContactListItem edit();

    Integer save(MContactListItem item);

    MIntegerList saveList(MContactList items);

    Boolean delete(Integer objectID, Integer ownerID);

    MIntegerList deleteList(ArrayList<Integer> objectIDs, Integer ownerID);

    MIntegerList deleteList(ArrayList<Integer> objectIDs);

    // EXCEL PLUGIN
    MContactList getListForExcel(MFilterParametrs filterParametrs);

    MContactListItem getForExcel(Integer objectID);

    Integer saveForExcel(MContactListItem item);

    MIntegerList saveListForExcel(MContactList contactList);

    // MOBILE SYNC
    MContactList getSyncList(MFilterParametrs filterParametrs);

    // MContactList syncMobileContacts(MContactList mobileContacts);
    /*MContactList syncMobileContacts(MContactList mobileContacts);
    
    EdsDeviceCrmContact getDeviceContact(String deviceID, Integer contactID);*/
}
