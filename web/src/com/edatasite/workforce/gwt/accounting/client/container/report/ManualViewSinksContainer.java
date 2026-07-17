package com.edatasite.workforce.gwt.accounting.client.container.report;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.manual.AddManualEntryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.manual.ManualEntrySummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 15, 2009
 * Time: 5:01:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManualViewSinksContainer extends SinksContainer {

    public ManualViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ManualEntrySummaryView(id));
        //addView(new AddEditManualJournalsView(id));
        addView(new AddManualEntryView(id));
    }
}
