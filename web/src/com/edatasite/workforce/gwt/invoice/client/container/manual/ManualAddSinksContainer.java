package com.edatasite.workforce.gwt.invoice.client.container.manual;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.PermissionDeniedView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.manual.AddManualEntryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 16.04.2009
 * Time: 21:45:11
 * To change this template use File | Settings | File Templates.
 */
public class ManualAddSinksContainer extends SinksContainer {

    public ManualAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        // addView(new AddEditManualJournalsView());
        if (!Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_ADD)) {
            addView(new PermissionDeniedView("You do not have permission to add Manual Entries"));
        } else {
            if (params.length > 1) {
                addView(new AddManualEntryView(params));
            } else {
                addView(new AddManualEntryView());
            }
        }

    }
}
