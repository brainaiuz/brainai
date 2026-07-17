package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditSignatureView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 12.02.13
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class SignatureAddSinksContainer extends SinksContainer {
    public SignatureAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new AddEditSignatureView());
    }
}
