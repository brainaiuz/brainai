package com.edatasite.workforce.gwt.invoice.client.container.expense;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.expense.CustomExpenseImportView;

import java.util.LinkedList;

/**
 * Created by Khasan on 14.08.14.
 */
public class ImportCustomExpenseSinksContainer extends SinksContainer {


    public ImportCustomExpenseSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String objectId = null;
        boolean isCompanyExpense = false;
        if (params.length > 1) {
            objectId = params[1];
            if (params.length>2) {
                isCompanyExpense = Constants.IMPORT_COMPANY_EXPENSE_CLAIMS.equals(params[2]);
            }
            addView(new CustomExpenseImportView(Integer.valueOf(objectId), isCompanyExpense));
        }
    }
}
