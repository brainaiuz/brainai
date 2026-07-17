package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.AddCrmAccountView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountAddSinksContainer extends SinksContainer {

    public CrmAccountAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params != null && params.length >= 2) {
            if (AddCrmAccountView.FROM_OUTLOOK.equals(params[2])) {
                addView(new AddCrmAccountView(params[1]));
            } else if (COPY.equals(params[2])) {
                addView(new AddCrmAccountView(Integer.valueOf(params[1]), true));
            } else if (AddCrmAccountView.FROM_OPPORTUNITY.equals(params[1])) {
                addView(new AddCrmAccountView(true, params[2] != null && !params[2].isEmpty() ? Integer.valueOf(params[2]) : null));
            }
        } else {
            addView(new AddCrmAccountView(null, false));
        }


    }
}
