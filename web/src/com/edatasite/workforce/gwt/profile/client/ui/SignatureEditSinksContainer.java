package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditSignatureView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 14.02.13
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public class SignatureEditSinksContainer extends SinksContainer {
    public SignatureEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new AddEditSignatureView(id));
    }
}
