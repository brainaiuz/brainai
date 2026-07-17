package com.edatasite.workforce.gwt.backend.client.history;

import com.edatasite.workforce.gwt.backend.client.ExportSchemaSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 12, 2010
 * Time: 7:13:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportSchemaHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {

        /*SinksContainer container = new AddSchemaSinksContainer(containerName + strings[0], "Backend");
        return container;*/
        return null;
    }

    public SinksContainer processAdd(String[] params) {

        return new ExportSchemaSinksContainer("exportschemaadd", wfmStrings.exportSchema());
    }

}