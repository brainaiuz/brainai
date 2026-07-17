package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.SetTestCompanyView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 28.02.2009
 * Time: 10:52:03
 * To change this template use File | Settings | File Templates.
 */
public class SetTestCompanySinksContainer extends SinksContainer {

	public SetTestCompanySinksContainer(String name, String description, String[] strings) {
		super(name, description, strings);
	}

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		addView(new SetTestCompanyView());
	}
}