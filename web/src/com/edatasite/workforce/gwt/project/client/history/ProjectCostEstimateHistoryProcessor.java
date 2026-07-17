package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ProjectCostEstimateAddSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 29.04.2010
 * Time: 12:37:35
 * To change this template use File | Settings | File Templates.
 */
public class ProjectCostEstimateHistoryProcessor implements HistoryProcessor {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
//		if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)|| Utils.hasRole(Constants.PM) || Utils.hasRole(Constants.TL)|| Utils.hasRole(Constants.ADMIN_LOCATION)) {
        return new ProjectCostEstimateAddSinksContainer("projectcostestimateadd", "Add Project Cost Estimate");
//		}
//		return null;
    }
}
