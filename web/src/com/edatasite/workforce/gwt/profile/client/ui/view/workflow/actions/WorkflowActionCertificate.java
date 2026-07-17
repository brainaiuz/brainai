package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by shohruh on 01-Apr-17.
 */
public class WorkflowActionCertificate extends AbstractWorkflowAction implements Constants.WorkflowActionConstants.Certificate {
    int r;

    public WorkflowActionCertificate(WorkflowAction action) {
        super();
        setCellSpacing(10);
        setCellPadding(10);
        getElement().setAttribute("style", "border-spacing:10px;border-collapse:separate");
        setHeight("200px");

        this.action = action;
        fieldsMap = action.getFieldsAsMap();
        SelectItem[] fields = getColumnsAsReferenceItems(action.getFields());

        map = action.getItemsAsMap();

        r = 0;
        setWidget(r, 0, new HTML(""));
        setWidget(r, 1, new HTML(wfmStrings.customFields()));
        setWidget(r, 2, new HTML(wfmStrings.defaultValue()));
        addWidget(wfmStrings.certificateType(), fields, FIELD_TYPE, map.get(FIELD_TYPE), CERTIFICATE_LIST_BOX, true);
        int i = 4;
        for (String box : WorkflowAction.getCertificateFields()) {
            addWidget(box, fields, i, map.get(i), box.contains("area")? TEXT_2: TEXT, false);
            i++;
        }
    }

    public boolean validate() {
        return true;
    }

    public String getActionName(){
        return wfmStrings.certificate();
    }
}
