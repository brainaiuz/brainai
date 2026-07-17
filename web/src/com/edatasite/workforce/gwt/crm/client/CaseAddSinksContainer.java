package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddCaseView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EditCaseForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CaseAddSinksContainer extends SinksContainer {

    public CaseAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        AddCaseView addCaseView;
        Integer caseID = null;

        if (params != null && params.length == 4 && "CONVERT".equals(params[1])) {
            String formType = params[2];
            Integer convertFormId = params[3] != null && params[3].matches(Constants.REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[3]) : null;
            addView(new AddCaseView(convertFormId, formType));
        } else if (params != null && params.length == 4 && "RELATION".equals(params[1])) {
            String formType = params[2];
            Integer convertFormId = params[3] != null && params[3].matches(Constants.REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[3]) : null;
            addView(new AddCaseView(null, convertFormId, formType, null));
        } else if (params != null && params.length > 2 && (AddCaseView.FROM_OUTLOOK.equals(params[2]) || COPY.equals(params[2]))) {
            if (AddCaseView.FROM_OUTLOOK.equals(params[2])) {
                addView(new AddCaseView(params[1]));
            } else {
                addView(new AddCaseView(Integer.valueOf(params[1]), true));
            }
        } else if (params != null) {
            Integer relationID = params.length > 2 && !"null".equals(params[2]) ? Integer.valueOf(params[2]) : null;
            String relationType = params.length > 3 && !"null".equals(params[3]) ? params[3] : null;
            String relationName = params.length > 4 && !"null".equals(params[4]) ? params[4] : null;
//            Integer contactID = params.length > 5 && !"null".equals(params[5]) && !"undefined".equals(params[5]) ? Integer.valueOf(params[5]) : null;
//            String contactName = params.length > 6 && !"null".equals(params[6]) ? params[6] : null;
//            Integer accountID = params.length > 7 && !"null".equals(params[7]) && !"undefined".equals(params[7]) ? Integer.valueOf(params[7]) : null;
//            String accountName = params.length > 8 && !"null".equals(params[8]) ? params[8] : null;
            if (params.length == 2 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER)) {
                caseID = Integer.parseInt(params[1]);
            }
            if (caseID == null) {
                addCaseView = new AddCaseView(caseID, relationID, relationType, relationName);
//                addCaseView.setPredefinedTags(relations.toArray(new RelationItem[]{}));
                addView(addCaseView);
            } else {
                addView(new EditCaseForm(caseID));
                /*if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
                    addView(new EventListView(null, caseID, RelationItem.TYPE_CASE));
                }
                if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
                    addView(new CrmTaskListView(caseID, RelationItem.TYPE_CASE));
                }
                if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
                    super.addView(new EmailListView(RelationItem.TYPE_CASE, caseID));
                }*/
            }
        }
    }
}