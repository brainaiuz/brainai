package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.client.*;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MStateList;
import com.workforcetrack.mobile.rpc.expense.MCurrencyList;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 20.06.11
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public interface ClientWebService {

    MContactItemList getContactEmails(Integer objectID, String type);

    String getClientCode();

    MCountryList getCountries();

    MStateList getRegions();

    MStateList getRegions(Integer countryID);

    MClientListItem get(Integer clientId);

//    MBillingData getContactAddress(Integer id);

    MNewClientList getList(MFilterParametrs fp);

    boolean delete(Integer clientID);

    MClientListItem edit(Integer clientId);

    MClientListItem edit();

    Integer save (MClientListItem mClientListItem);

    MClientContactListItem getClientContact(Integer id);

    MCurrencyList getCurrencies();

    MListArray getListData();

    MListArray getListData(Integer objectID);

    Integer saveClientContact(MClientContactListItem contactItem);







}
