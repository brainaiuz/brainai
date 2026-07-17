package com.edatasite.workforce.gwt.myaccount.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.MyAccountUsagePlanSummarySinksContainer;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 1/28/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyAccountUsagePlanSummaryHistoryProcessor implements HistoryProcessor {

	private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return new MyAccountUsagePlanSummarySinksContainer(containerName + strings[0], myAccountStrings.usagePlanSummary(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return null;
	}
}
