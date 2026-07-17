package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.ui.view.UsagePlanView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 1/28/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountUsagePlanSummarySinksContainer extends SinksContainer {

    public MyAccountUsagePlanSummarySinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new UsagePlanView(id));
    }
}
