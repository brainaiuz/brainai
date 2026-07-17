package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.PlacementAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.PlacementViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Ilhombek
 * Date: 7/5/12
 * Time: 7:44 PM
 */
public class PlacementHistoryProcessor implements HistoryProcessor {

	private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return new PlacementViewSinksContainer(containerName + strings[0], hrmsStrings.placementView(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return new PlacementAddSinksContainer("placementadd", hrmsStrings.addPlacement(), params);
	}
}