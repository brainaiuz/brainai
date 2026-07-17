package com.edatasite.workforce.gwt.core.client.rpc.placeofsupply;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class PlaceOfSupplyItem implements IsSerializable, Serializable {

    private SelectItem[] states;
    private SelectItem[] countries;

    public PlaceOfSupplyItem() {

    }

    public SelectItem[] getStates() {
        return states;
    }

    public void setStates(SelectItem[] states) {
        this.states = states;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }
}
