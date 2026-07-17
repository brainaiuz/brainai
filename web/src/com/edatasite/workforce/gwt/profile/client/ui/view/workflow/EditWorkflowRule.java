package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;

/**
 * Created by Hayot on 2/28/14.
 */
public class EditWorkflowRule extends AddWorkflowRule implements NoColapse {

    public EditWorkflowRule(Integer objectId, boolean recurrence) {
        super(objectId, null, recurrence);
    }
}
