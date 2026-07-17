package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ImportChartOfAccountsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 4, 2011
 * Time: 11:26:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportChartOfAccountsSinksContainer extends SinksContainer{

    public ImportChartOfAccountsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            boolean fromGettingStarted = params.length>2 && "fromGettingStarted".equals(params[2]);
            addView(new ImportChartOfAccountsView(Integer.valueOf(objectId), fromGettingStarted));
        }
    }
}
