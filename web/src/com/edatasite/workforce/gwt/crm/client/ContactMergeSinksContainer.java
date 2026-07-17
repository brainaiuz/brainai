package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.crm.client.ui.ContactMergeView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 3/14/16 4:48 PM
 */
public class ContactMergeSinksContainer extends SinksContainer {

    public ContactMergeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            if (CrmConstants.CRM_CONTACT.equals(params[1])) {
                ArrayList<Integer> ids = new ArrayList<>();
                for (int i = 2; i < params.length; i++) {
                    if (params[i] != null && params[i].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        ids.add(Integer.parseInt(params[i]));
                    }
                }
                addView(new ContactMergeView("contactmerge", "Merge Contacts", ids.toArray(new Integer[]{})));
            }
        }
    }
}
