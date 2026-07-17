package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.SignatureSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 12.02.13
 * Time: 12:27
 * To change this template use File | Settings | File Templates.
 */
public class SignatureViewSinksContainer extends SinksContainer {
    public SignatureViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String signatureID = null;
        if (params.length > 0) {
            signatureID = params[0];
        }
        super.addView(new SignatureSummaryView(signatureID != null ? Integer.valueOf(signatureID) : id));

    }
}
