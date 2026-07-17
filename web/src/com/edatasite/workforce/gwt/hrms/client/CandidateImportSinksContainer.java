package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.ImportCandidateView;

import java.util.LinkedList;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 4:57 PM
 */
public class CandidateImportSinksContainer extends SinksContainer {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public CandidateImportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String objectId = null;
        if (params.length > 1) {
            objectId = params[1];
            addView(new ImportCandidateView(Integer.valueOf(objectId)));
        }
    }
}
