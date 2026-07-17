package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ChartOfAccountsSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.01.2011
 * Time: 0:36:50
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsSummarySinksContainer extends SinksContainer {

    public ChartOfAccountsSummarySinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    @Override
    protected void initViews() {
        addView(new ChartOfAccountsSummaryView(id));
    }
}
