package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class LeadDetailsItemResponseTO extends ResponseData {

    private LeadDetailsItemTO item;

    public LeadDetailsItemResponseTO() {
    }

    public LeadDetailsItemTO getItem() {
        return item;
    }

    public void setItem(LeadDetailsItemTO item) {
        this.item = item;
    }
}
