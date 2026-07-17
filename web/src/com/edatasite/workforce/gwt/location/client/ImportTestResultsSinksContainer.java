package com.edatasite.workforce.gwt.location.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.ImportTestResultsXMLFileView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 17.09.12
 * Time: 21:13
 * To change this template use File | Settings | File Templates.
 */

public class ImportTestResultsSinksContainer extends SinksContainer {

	public ImportTestResultsSinksContainer(String name, String description, String[] params) {
		super(name, description, params);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
	protected void initViews() {
		addView(new ImportTestResultsXMLFileView());
	}
}
