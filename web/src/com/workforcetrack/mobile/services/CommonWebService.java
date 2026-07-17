package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MCountryStates;
import com.workforcetrack.mobile.rpc.contact.MStateList;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 4:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CommonWebService {

    MCountryList getCountries();

    MStateList getStates();

    MStateList getStatesByCountryID(Integer countryID);

    MCountryStates getCountryAndStateList();

}
