package com.edatasite.workforce.gwt.accounting.client.container.guide;

import com.edatasite.workforce.gwt.accounting.client.ui.view.guide.AccountingGettingStartedView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 18.04.2009
 * Time: 12:32:04
 * To change this template use File | Settings | File Templates.
 */
public class GuideSinksContainer extends SinksContainer {
    private final static WfmStrings wfmStrings = WfmStrings.App.get();

    public GuideSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    public GuideSinksContainer() {
        super("guide", wfmStrings.gettingStarted());
    }

    public GuideSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    protected void initViews() {
        addView(new AccountingGettingStartedView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
