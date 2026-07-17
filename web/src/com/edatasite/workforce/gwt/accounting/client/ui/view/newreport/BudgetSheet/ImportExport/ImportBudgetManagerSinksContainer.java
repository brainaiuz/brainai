package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.ImportExport;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.google.gwt.core.client.GWT;

import java.util.LinkedList;

public class ImportBudgetManagerSinksContainer extends SinksContainer {

    public ImportBudgetManagerSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        GWT.log("Params: " + params.length);
        Integer budgetId = null;
        if (params.length >= 2) {
            if (params[2] != null && params[2].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                budgetId = Integer.parseInt(params[2]);
            }
            addView(new ImportBudgetManagerView(Integer.valueOf(params[1]), budgetId));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
