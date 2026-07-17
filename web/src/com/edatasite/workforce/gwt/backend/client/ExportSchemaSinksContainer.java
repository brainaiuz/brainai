package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.ExportSchemaView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;


/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 12, 2010
 * Time: 7:15:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportSchemaSinksContainer extends SinksContainer {

    public ExportSchemaSinksContainer(String name, String description) {
        super(name, description, null, CLOSE);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        ExportSchemaView exportSchemaView = new ExportSchemaView();
        addView(exportSchemaView);
    }

}