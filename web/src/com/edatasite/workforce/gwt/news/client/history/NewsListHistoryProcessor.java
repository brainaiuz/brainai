package com.edatasite.workforce.gwt.news.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.news.client.container.NewsSinksContainer;

/*
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: Apr 25, 2011
 * Time: 6:01:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsListHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new NewsSinksContainer(containerName, wfmStrings.news(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
