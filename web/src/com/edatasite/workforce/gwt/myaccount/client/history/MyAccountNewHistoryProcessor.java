package com.edatasite.workforce.gwt.myaccount.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.MyAccountNewSinksContainer;
import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Nov 26, 2008
 * Time: 12:09:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountNewHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        GWT.log("containername: " + containerName + strings[0]);
        return new MyAccountNewSinksContainer(containerName /*+ strings[0]*/, wfmStrings.myBilling());
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
