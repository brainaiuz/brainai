package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.BuildAssemblyAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.BuildAssemblySinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/18/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildAssemblyItemHistoryProcessor implements HistoryProcessor {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new BuildAssemblySinksContainer(containerName + params[0], accountingStrings.buildAssembly(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new BuildAssemblyAddSinksContainer("buildAssemblyadd", accountingStrings.buildAssembly(), params);
    }
}
