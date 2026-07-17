package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ImportBrandView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ImportBrandSinksContainer extends SinksContainer {

    public ImportBrandSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ImportBrandView(Integer.valueOf(params[1])));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
