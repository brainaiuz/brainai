package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.EditPerformanceNoteForm;
import com.edatasite.workforce.gwt.assessment.client.ui.view.ViewPerformanceNoteForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Sherzod
 * Date: May 14, 2009
 * Time: 5:25:57 PM
 */
public class NoteSinksContainer extends SinksContainer {

    public NoteSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_PERFORMANCE_NOTE_SUMMERY)) {
            addView(new ViewPerformanceNoteForm(id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERFORMANCE_NOTE)) {
            addView(new EditPerformanceNoteForm(id));
        }
    }
}
