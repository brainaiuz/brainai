package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ImportTallyManualTransactionView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by dilshod on 23-Mar-16.
 */
public class ImportTallyManualTransactionSinksContainer extends SinksContainer {

    public ImportTallyManualTransactionSinksContainer(String name, String description, String[] params) {
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
            addView(new ImportTallyManualTransactionView(Integer.valueOf(objectId)));
        }
    }
}
