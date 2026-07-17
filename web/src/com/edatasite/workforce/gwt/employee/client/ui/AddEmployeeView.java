package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddEmployeeView extends View implements Constants, Errors, Colapse {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private String from = "";
    private MultiEmployeePanel addMultiEmployees;
    private TeamEmployeePanel addTeamEmployees;
    public static String EMPLOYEE_VIEW = "employeeView";

    private final boolean isGuideView = false;
    private final boolean hrmsAddEmployee = false;

    public AddEmployeeView(String from) {
        super("add", wfmStrings.addEmployees());
        if (from == null) {
            from = "";
        }
        this.from = from;
    }

    protected Widget onInitialize() {
        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
            addMultiEmployees = new MultiEmployeePanel();
            addMultiEmployees.addStyleName("reachFullWidth");

            if (from.equals(PA)) {
                addMultiEmployees.setCreatedFrom(EMPLOYEE_CREATED_FROM_ASSESSMENT);
            }

            addMultiEmployees.setCheckLimit(new MultiEmployeePanel.CheckLimit() {
                @Override
                public void limitExceeded() {
                    limitExceed();
                }
            });

            addMultiEmployees.setRefreshParent(addedEmployees -> {
                refreshParent(addedEmployees);
            });

            add(addMultiEmployees);
        } else {
            addTeamEmployees = new TeamEmployeePanel();
            addTeamEmployees.addStyleName("reachFullWidth");


            if (from.equals(PA)) {
                addTeamEmployees.setCreatedFrom(EMPLOYEE_CREATED_FROM_ASSESSMENT);
            }
            addTeamEmployees.setCheckLimit(new TeamEmployeePanel.CheckLimit() {
                @Override
                public void limitExceeded() {
                    limitExceed();
                }
            });

            addTeamEmployees.setRefreshParent(addEmployees -> refreshParent(addEmployees));

            add(addTeamEmployees);
        }
        return null;
    }

    private void refreshParent(NewEmployee[] addedEmployees) {
        if (addedEmployees != null && addedEmployees.length > 0) {
            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.employee()), Info.Type.INFO);
            Integer lastEmployeeID = addedEmployees[addedEmployees.length - 1].getObjectID();
            closeTab();
            /*if (lastEmployeeID > 0) {
                refreshOnDemand("lastEmployeeIDforIssue", lastEmployeeID.toString(), true);
            }
            refreshOn();*/
        } else {
            final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            confirm.setTitle(wfmStrings.information());
            confirm.setMessage(wfmStrings.doYouWantToExit());
            confirm.addCloseHandler(new CloseHandler() {
                @Override
                public void onCancel() {
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
    }

    private void limitExceed() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        VerticalPanel vp = new VerticalPanel();
        HTML html = new HTML(wfmStrings.usersLimitExceeded());
        SimpleLink simpleLink = new SimpleLink(wfmStrings.currentSubscriptionSection());
        simpleLink.addClickHandler(sender -> Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html#settings|CurrentUsagePlanView"));
        horizontalPanel.add(html);
        horizontalPanel.add(simpleLink);
        WfmPanel pane = new WfmPanel(WfmPanel.Styles.GRAY);
        pane.setWidget(horizontalPanel);
        pane.setWidth("850px");
        vp.add(pane);
        vp.setSpacing(5);
        add(vp);
    }

    public void initialize() {
        onInitialize();
    }

    public String getIconStyle() {
        return null;
    }

    public MultiEmployeePanel getAddEmployees() {
        return addMultiEmployees;
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
