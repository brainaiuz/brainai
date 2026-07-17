package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 03/27/2018.
 */
public class OpportunityAddTO extends OpportunityEditTO {

    private ArrayList<Object> custom_fields;

    public OpportunityAddTO() {
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
