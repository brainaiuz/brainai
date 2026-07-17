package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditCertificateView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CertificateSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateViewSinksContainer extends SinksContainer {

    public CertificateViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddEditCertificateView(id));
        addView(new CertificateSummaryView(id));
    }
}
