package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificateTemplateViewSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.CustomizeCertificateSinksContainer;

/**
 * Created by Khasan on 30.09.14.
 */
public class CustomizeCertificateHistoryProcessor implements HistoryProcessor {
    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CertificateTemplateViewSinksContainer(containerName + strings[0], "Template View", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CustomizeCertificateSinksContainer("customizeCertificateadd", params.length == 1 ? HrmsStrings.App.get().addCertificateTemplate() : HrmsStrings.App.get().customizeCertificate(), params);
    }
}
