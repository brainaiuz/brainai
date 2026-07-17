package com.edatasite.workforce.gwt.news.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.news.client.container.NewsAddSinksContainer;
import com.edatasite.workforce.gwt.news.client.container.NewsViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:15:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewsHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new NewsViewSinksContainer(containerName + strings[0], wfmStrings.newsDetails(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new NewsAddSinksContainer("newsadd", wfmStrings.createNews(), params);
    }
}
