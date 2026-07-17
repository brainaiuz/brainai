package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.AddEditCertificeteView;

import java.util.LinkedList;

/**
 * Created by Khasan on 08.09.14.
 */
public class CertificateSinksContainer extends SinksContainer {

    public CertificateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            if (Utils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_ADD)) {
                addView(new AddEditCertificeteView());
            }
        } else if (params != null && params.length == 4 && "CONVERT".equals(params[1])) {
            String formType = params[2];
            Integer convertFormId = params[3] != null && params[3].matches(Constants.REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[3]) : null;
            addView(new AddEditCertificeteView(formType, convertFormId));
        } else if (params.length >= 2) {
            String formID = params != null && params.length > 2 && !"null".equals(params[2]) ? params[2] : null;
            addView(new AddEditCertificeteView(Integer.valueOf(params[1]), formID));
        }
    }
}
