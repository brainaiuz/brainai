package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.employee.client.ui.MultiEmployeePanel;
import com.edatasite.workforce.gwt.employee.client.ui.TeamEmployeePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: March 11, 2010
 * Time: 20:20:20 AM
 * To change this template use File | Settings | File Templates.
 */

public class AddEmployeeView extends View implements Constants, Errors, Colapse {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CrmStrings employeeStrings = CrmStrings.App.get();

    private String from = "";
    private MultiEmployeePanel addMultiEmployees;
    private TeamEmployeePanel addTeamEmployees;

    private final boolean isGuideView = false;

    public AddEmployeeView() {
        super("add", wfmStrings.addEmployee());
    }

    public AddEmployeeView(String from) {
        super("add", wfmStrings.addEmployee());
        if (from == null) {
            from = "";
        }
        this.from = from;
    }

    protected Widget onInitialize() {
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
            addMultiEmployees = new MultiEmployeePanel();
        } else {
            addTeamEmployees = new TeamEmployeePanel();
        }
        addMultiEmployees.setWidth("1040px");
        addTeamEmployees.setWidth("1040px");
        if (from.equals(PA)) {
            addMultiEmployees.setCreatedFrom(EMPLOYEE_CREATED_FROM_ASSESSMENT);
            addTeamEmployees.setCreatedFrom(EMPLOYEE_CREATED_FROM_ASSESSMENT);
        }

        addMultiEmployees.setRefreshParent(addedEmployees -> {
            if (addedEmployees != null && addedEmployees.length > 0) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()), Info.Type.INFO);
                refreshOn();
            } else {
                final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                confirm.setTitle(wfmStrings.information());
                confirm.setMessage(wfmStrings.doYouWantToExit());
                confirm.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
//                        addEmployees.reinit();
                    }

                    @Override
                    public void onSubmit() {
                        if (!isGuideView) {
                            closeTab();
                        }
                    }
                });
                confirm.open();
            }
        });
        add(addMultiEmployees);
        return null;
    }

    private void refreshOn() {
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, "", AddEmployeeView.this);
        closeTab();
    }

    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
