package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.ImportGroupPayrunView;

import java.util.LinkedList;

public class ImportGroupPayrunSinksContainer extends SinksContainer {

    public ImportGroupPayrunSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_IMPORT)) {
            String objectId;
//        Integer maxNoAccessCount = 0;
            if (params.length > 1) {
                objectId = params[1];
//            maxNoAccessCount = (params.length > 3 && params[3] != null) && "".equals(params[3]) ? Integer.parseInt(params[3]) : 0;
                addView(new ImportGroupPayrunView(Integer.valueOf(objectId)/*, maxNoAccessCount*/));
            }
        }
    }
}
