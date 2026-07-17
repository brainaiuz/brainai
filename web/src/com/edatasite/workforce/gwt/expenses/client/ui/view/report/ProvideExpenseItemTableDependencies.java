package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportViewParameters;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

public interface ProvideExpenseItemTableDependencies {
    ExpenseReportViewParameters getFormParams();

    ExpenseReportsListItem getExpenseReportData();

    HashMap<String, Widget> getWidgetsMap();

    ColumnConfig[] getColumns();

    CurrencyWidget getCurrencyWidget();

    HashMap<String, CompanyCustomFieldItem> getCustomFieldsMap();

    ProjectLookUp getProjectLookUp();

}
