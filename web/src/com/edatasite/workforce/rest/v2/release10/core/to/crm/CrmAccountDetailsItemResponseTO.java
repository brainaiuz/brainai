package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class CrmAccountDetailsItemResponseTO extends ResponseData {

    private CrmAccountDetailsItemTO item;

    public CrmAccountDetailsItemResponseTO() {
    }

    public CrmAccountDetailsItemTO getItem() {
        return item;
    }

    public void setItem(CrmAccountDetailsItemTO item) {
        this.item = item;
    }
}
