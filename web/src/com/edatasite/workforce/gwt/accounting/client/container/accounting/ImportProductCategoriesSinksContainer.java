package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ImportProductCategoriesView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ImportProductCategoriesSinksContainer extends SinksContainer {

    public ImportProductCategoriesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ImportProductCategoriesView(Integer.valueOf(params[1])));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
