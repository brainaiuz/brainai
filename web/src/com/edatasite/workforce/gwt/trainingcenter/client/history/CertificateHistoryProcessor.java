package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CertificateAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CertificateViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateHistoryProcessor implements HistoryProcessor{

    private static final TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CertificateViewSinksContainer(containerName + strings[0], tcStrings.issueCertificate(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CertificateAddSinksContainer("certificateadd", tcStrings.addCourseView(), params);
    }
}
