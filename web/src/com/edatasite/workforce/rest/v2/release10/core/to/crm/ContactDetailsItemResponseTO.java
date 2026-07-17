package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov 02/22/2018.
 */
public class ContactDetailsItemResponseTO extends ResponseData {

    private ContactDetailsItemTO item;

    public ContactDetailsItemResponseTO() {
    }

    public ContactDetailsItemTO getItem() {
        return item;
    }

    public void setItem(ContactDetailsItemTO item) {
        this.item = item;
    }
}
