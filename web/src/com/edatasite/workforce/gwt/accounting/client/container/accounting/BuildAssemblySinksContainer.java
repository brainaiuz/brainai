package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BuildAssemblySummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/18/12
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildAssemblySinksContainer extends SinksContainer {

    public BuildAssemblySinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_BUILD_ASSEMBLY_SUMMARY)) {
            super.addView(new BuildAssemblySummaryView(id));
        }
    }
}
