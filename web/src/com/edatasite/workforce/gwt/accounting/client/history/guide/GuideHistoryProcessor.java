package com.edatasite.workforce.gwt.accounting.client.history.guide;

import com.edatasite.workforce.gwt.accounting.client.container.guide.GuideSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 18.04.2009
 * Time: 13:13:45
 * To change this template use File | Settings | File Templates.
 */
public class GuideHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new GuideSinksContainer(containerName + strings[0], accountingStrings.guideView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
