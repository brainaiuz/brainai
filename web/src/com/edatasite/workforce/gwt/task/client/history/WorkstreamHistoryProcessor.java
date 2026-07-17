package com.edatasite.workforce.gwt.task.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.WorkstreamAddSinksContainer;
import com.edatasite.workforce.gwt.task.client.WorkstreamViewSinksContainer;


/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 10.11.2008
 * Time: 23:15:47
 * To change this template use File | Settings | File Templates.
 */
public class WorkstreamHistoryProcessor implements HistoryProcessor {

    private ProjectStrings projectStrings = ProjectStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();


    public SinksContainer process(String containerName, String[] strings) {
        return new WorkstreamViewSinksContainer(containerName + strings[0], projectStrings.editWorkStream(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new WorkstreamAddSinksContainer("workstreamadd", wfmStrings.addWorkstream(), params);
    }

}