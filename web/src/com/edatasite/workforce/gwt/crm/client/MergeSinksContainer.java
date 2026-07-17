package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.crm.client.ui.CrmAccountMergeView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 7/4/11
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MergeSinksContainer extends SinksContainer {
    public MergeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            if (CrmConstants.CRM_ACCOUNT.equals(params[1])) {
                ArrayList<Integer> ids = new ArrayList<>();
                for (int i = 2; i < params.length; i++) {
                    if (params[i] != null && params[i].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        ids.add(Integer.parseInt(params[i]));
                    }
                }
                addView(new CrmAccountMergeView("crmAccountMerge", "Merge Accounts", ids.toArray(new Integer[]{})));
            }
        }
    }
}
