package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 28.03.2009
 * Time: 19:26:00
 * To change this template use File | Settings | File Templates.
 */
public interface BudgetsheetProvider {

    void save(BigDecimal budget, Object data);
}
