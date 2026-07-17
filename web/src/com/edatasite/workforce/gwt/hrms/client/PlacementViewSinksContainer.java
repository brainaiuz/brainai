package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.EditPlacementForm;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.ViewPlacementForm;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 7/5/12
 * Time: 7:46 PM
 */
public class PlacementViewSinksContainer extends SinksContainer {
    public PlacementViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        boolean isEditable = params[1] != null && "true".equals(params[1]);
        addView(new ViewPlacementForm(id, isEditable));
        if (isEditable) {
            addView(new EditPlacementForm(id));
        }
    }
}
