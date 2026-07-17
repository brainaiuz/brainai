package com.edatasite.workforce.gwt.expenses.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ExpenseMessages extends Messages {

    String entityIsRequired(String entityName);

    String onRowN(Integer rowOrder);

    class App {
        private static ExpenseMessages instance;

        public static ExpenseMessages get() {
            if (instance == null) {
                instance = GWT.create(ExpenseMessages.class);
            }
            return instance;
        }
    }
}