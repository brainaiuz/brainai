package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ImportBankTransferView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by dilsh0d madrahimov on 07/02/2017.
 */
public class ImportBankTransferSinksContainer extends SinksContainer {

    public ImportBankTransferSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length >= 2) {
            addView(new ImportBankTransferView(Integer.valueOf(params[1]), params[2]));
        }
    }
}
