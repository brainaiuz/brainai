package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.IncidentAddSinksContainer;
import com.edatasite.workforce.gwt.availability.client.IncidentSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Sherzod
 * Date: May 25, 2009
 * Time: 2:28:57 PM
 */
public class IncidentHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    //must be ---> strings.length<=3
    public SinksContainer process(String containerName, String[] strings) {
        return new IncidentSinksContainer(containerName + strings[0], hrmsStrings.incidentView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new IncidentAddSinksContainer("incidentadd", hrmsStrings.addIncident(), params);
    }
}