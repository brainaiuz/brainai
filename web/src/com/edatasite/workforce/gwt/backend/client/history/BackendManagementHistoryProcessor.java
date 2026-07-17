package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.container.BackendManagementAddSinksContainer;
import com.edatasite.workforce.gwt.backend.client.container.BackendManagementListViewSinksContainer;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/24/12
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class BackendManagementHistoryProcessor implements HistoryProcessor {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
        return new BackendManagementListViewSinksContainer(containerName + strings[0], wfmStrings.backend(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return new BackendManagementAddSinksContainer("backendManagementadd", backendStrings.backendOptions(), params);
	}
}
