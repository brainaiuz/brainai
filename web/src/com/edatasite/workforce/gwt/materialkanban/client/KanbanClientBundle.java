package com.edatasite.workforce.gwt.materialkanban.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by Anvar Akramov on 9/5/17.
 */
public interface KanbanClientBundle extends ClientBundle {

    KanbanClientBundle INSTANCE = GWT.create(KanbanClientBundle.class);

    @ClientBundle.Source("com/edatasite/workforce/gwt/materialkanban/client/KanbanDND.css")
    TextResource injectKanbanCSS();

}
