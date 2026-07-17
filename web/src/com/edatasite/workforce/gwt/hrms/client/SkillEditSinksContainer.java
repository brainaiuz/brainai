package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.EditSkillView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 10.07.2017 17:19
 */
public class SkillEditSinksContainer extends SinksContainer {

    public SkillEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectId = null;
        if (params.length > 1) {
            objectId = Integer.valueOf(params[1]);
        }
        addView(new EditSkillView(objectId, true));
    }
}
