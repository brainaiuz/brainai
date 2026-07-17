package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AddPerformanceNoteView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Sherzod
 * Date: May 26, 2009
 * Time: 9:58:22 PM
 */
public class NoteAddSinksContainer extends SinksContainer {

    public NoteAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERFORMANCE_NOTE)) {
            addView(new AddPerformanceNoteView());
        }
    }
}
