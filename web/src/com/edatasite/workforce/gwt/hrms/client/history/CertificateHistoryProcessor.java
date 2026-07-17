package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificateSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificateViewSinksContainer;
/**
 * Created by Khasan on 08.09.14.
 */
public class CertificateHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CertificateViewSinksContainer(containerName + strings[0], "View Certificate", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String description = hrmsStrings.addHrLetters();
        if (params.length >= 2 && params[1] != null) {
            description = hrmsStrings.editHrLetters();
        }
        return new CertificateSinksContainer("certificateadd", description, params);
    }
}
