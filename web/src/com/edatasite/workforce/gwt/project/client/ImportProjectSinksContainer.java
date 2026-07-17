package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.view.projectImport.ImportProjectView;

import java.util.LinkedList;

/**
 * Created by Normurod on 9/19/15.
 */
public class ImportProjectSinksContainer extends SinksContainer {

    public ImportProjectSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_IMPORT_MS_BUTTON)) {
            addView(new ImportProjectView(Integer.valueOf(params[1])));
        }
    }
}
