package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.CreateEMLTemplatesView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.09.2010
 * Time: 16:48:44
 * To change this template use File | Settings | File Templates.
 */
public class CreateEMLTemplatesSinksContainer extends SinksContainer {

    public CreateEMLTemplatesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new CreateEMLTemplatesView(id));
    }
}
