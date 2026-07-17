package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.signup.MCreatedCompany;
import com.workforcetrack.mobile.rpc.signup.MNewCompany;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SignUpWebService {

    MCreatedCompany createCompany(MNewCompany mNewCompany);

    MCountryList getCountries();

    String getWFTPlugin(String pluginName);
}
