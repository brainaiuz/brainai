package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.NamedFrame;

public class PostFormPanel extends WfmFormPanel {

    public PostFormPanel(String action, NamedFrame name) {
        super(name);
        setAction(GWT.getHostPageBaseURL() + action);
        setMethod(METHOD_POST);
    }

    public PostFormPanel(String action, String target) {
        super(null, target);
        setAction(Utils.getHostNameURL() + action);
        setMethod(METHOD_POST);
    }
}
