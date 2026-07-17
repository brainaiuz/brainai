package com.edatasite.workforce.gwt.project.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ProjectCostActualAddSinksContainer;

/**
 * User: Dilsh0d
 * Date: 19-May-2010
 * Time: 15:07:36
 */
public class ProjectCostActualHistoryProcessor implements HistoryProcessor {

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
//		if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)|| Utils.hasRole(Constants.PM) || Utils.hasRole(Constants.TL)|| Utils.hasRole(Constants.ADMIN_LOCATION)) {
        return new ProjectCostActualAddSinksContainer("projectcostactualadd", "Add Project Cost Actual");
//		}
//		return null;
    }
}
