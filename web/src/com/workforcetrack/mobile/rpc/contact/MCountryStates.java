package com.workforcetrack.mobile.rpc.contact;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/28/11
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class MCountryStates {

    MCountryList mCountryList;
    MStateList mStateList;

    public MCountryStates(){

    }
    public MCountryStates(MCountryList mCountryList, MStateList mStateList) {
        this.mCountryList = mCountryList;
        this.mStateList = mStateList;
    }

    @XmlElement(name = "countryList")
    public MCountryList getmCountryList() {
        return mCountryList;
    }

    public void setmCountryList(MCountryList mCountryList) {
        this.mCountryList = mCountryList;
    }

    @XmlElement(name = "stateList")
    public MStateList getmStateList() {
        return mStateList;
    }

    public void setmStateList(MStateList mStateList) {
        this.mStateList = mStateList;
    }
}
