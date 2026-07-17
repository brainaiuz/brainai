package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddReferenceView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceSinksContainer extends SinksContainer implements Constants {

    public ReferenceSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params[0].equals("DEPARTMENT_TITLES")) {
            addView(new AddReferenceView("DEPARTMENT_TITLES"));
            return;
        }
        if (params != null && id == null && params.length > 0 && !params[0].equals("")) {
            addView(new AddReferenceView(id, params[0]));
        } else if (params != null && params.length > 1 && params[1] != null) {
            addView(new AddReferenceView(id, params[1]));
        } else if (id != null) {
            addView(new AddReferenceView(id));
        }
    }
}
