package com.edatasite.workforce.gwt.myaccount.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.MyAccountSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Nov 26, 2008
 * Time: 12:09:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings)//must be ---> strings.length<=3
    {
        return new MyAccountSinksContainer(containerName + strings[0], wfmStrings.myBilling());  //To change body of implemented methods use File | Settings | File Templates.
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
