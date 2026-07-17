package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;

public interface AccountExecuteCommand {
    void execute(AccountItem selectedID);
}
