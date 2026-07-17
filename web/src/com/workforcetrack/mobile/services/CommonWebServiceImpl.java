package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MCountryStates;
import com.workforcetrack.mobile.rpc.contact.MStateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("commonWebService")
public class CommonWebServiceImpl implements CommonWebService {

    @Autowired
    private CommonService commonService;

    public MStateList getStatesByCountryID(Integer countryID) {
        SelectItem[] states = commonService.getRegions();
        if (countryID == null) {
            return new MStateList(states);
        }
        List<MSelectItem> resStates = new ArrayList<>();
        MStateList mStateList = new MStateList();
        for (SelectItem selectItem : states) {
            if (countryID.toString().equals(selectItem.getDescription())) {
                resStates.add(new MSelectItem(selectItem));
            }
        }
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

}
