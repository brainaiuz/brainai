package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class OpportunityDetailsItemResponseTO extends ResponseData {

    private OpportunityDetailsItemTO item;

    public OpportunityDetailsItemResponseTO() {
    }

    public OpportunityDetailsItemTO getItem() {
        return item;
    }

    public void setItem(OpportunityDetailsItemTO item) {
        this.item = item;
    }
}
