package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 4, 2008
 * Time: 12:25:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class Core implements EntryPoint {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public void onModuleLoad() {
        RootPanel.get().add(new HTML(wfmStrings.helloWorld()));
    }
}
