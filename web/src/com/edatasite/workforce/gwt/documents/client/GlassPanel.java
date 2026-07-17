package com.edatasite.workforce.gwt.documents.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * @author Sherali
 */
public class GlassPanel extends Composite {

    public GlassPanel() {
        SimplePanel mySimplePanel = new SimplePanel();
        initWidget(mySimplePanel);
        setStyleName("gwt-GlassPanel");
        setWidth("100%");
        setHeight("100%");
    }
}
