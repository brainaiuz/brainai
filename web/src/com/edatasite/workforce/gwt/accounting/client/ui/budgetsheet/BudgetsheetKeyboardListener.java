package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 31.03.2009
 * Time: 20:04:40
 * To change this template use File | Settings | File Templates.
 */
public interface BudgetsheetKeyboardListener {

    void onEditCell(BigDecimal cellOldValue, BigDecimal cellNewValue);
}
