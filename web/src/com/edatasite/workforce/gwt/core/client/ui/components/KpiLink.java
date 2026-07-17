package com.edatasite.workforce.gwt.core.client.ui.components;

import com.google.gwt.event.dom.client.ClickHandler;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Author: Azazello
 * Date: 2/24/2018
 * Time: 11:43 AM
 */
public class KpiLink extends MaterialLink {
    public KpiLink() {
        super();
    }

    public KpiLink(String text) {
        super(text);
    }

    public KpiLink(String text, ClickHandler clickHandler){
        super(text);
        addClickHandler(clickHandler);
    }
}
