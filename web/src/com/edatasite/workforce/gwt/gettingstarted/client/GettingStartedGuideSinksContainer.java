package com.edatasite.workforce.gwt.gettingstarted.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.gettingstarted.client.ui.view.PMGuideView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 19.06.2009
 * Time: 17:17:24
 * To change this template use File | Settings | File Templates.
 */
public class GettingStartedGuideSinksContainer extends SinksContainer {
    public GettingStartedGuideSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    public GettingStartedGuideSinksContainer(String name, String description) {
        super(name, description, null, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new PMGuideView());
    }
}
