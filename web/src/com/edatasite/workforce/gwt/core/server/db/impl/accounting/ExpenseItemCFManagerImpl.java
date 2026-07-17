package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsExpenseItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExpenseItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("expenseItemCFManager")
public class ExpenseItemCFManagerImpl extends BaseManager<EdsExpenseItemCustomFields> implements ExpenseItemCFManager {

    public ExpenseItemCFManagerImpl() {
        super(EdsExpenseItemCustomFields.class);
    }
}
