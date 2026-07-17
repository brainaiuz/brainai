package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * User: Aziz
 * Date: 23.12.2009
 * Time: 16:18:28
 */
public class GoalWeightEditView extends CustomForm implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private Integer int_employeeID;
    private DataListBox employeeList;

    private GoalWeightEditTab goalWeightEditTab;

    private final String test_code_ID_name = "goal_weight_edit_view_";

    public GoalWeightEditView(Integer int_employeeID) {
        super("editGoalWeight", hrmsStrings.editGoalWeights());
        if (int_employeeID != null && int_employeeID != 0) {
            this.int_employeeID = int_employeeID;
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        //save & close button
        addButton(wfmStrings.save(), null, (test_code_ID_name + "save_and_close_button"), event -> {
            //save and close logic
            save();
        });
    }

    @Override
    protected void getDataToFillFields() {
        //------------------------///////////------------------/////////-----------...........----------------------////
        if (int_employeeID != null) {
            //set employees items
            EmployeeService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] employees) {
                    employeeList.setItems(employees);
                    if (int_employeeID != null) {
                        employeeList.setSelected(int_employeeID);
                    }
                    getEmployeeList(int_employeeID);
                }
            });

        }
        //------------------------///////////------------------/////////-----------...........----------------------////
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.GOAL_WEIGHT_EDIT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();

        if (int_employeeID != null) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_ADD, GoalWeightEditView.this, (sender, args) -> {
                if (employeeList != null && employeeList.getSelectedItem() != null) {
                    int_employeeID = employeeList.getSelectedItem().getId();
                    getEmployeeList(int_employeeID);
                }
            });
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COMPANY_GOAL_ADD, GoalWeightEditView.this, (sender, args) -> {
                if (employeeList != null && employeeList.getSelectedItem() != null) {
                    int_employeeID = employeeList.getSelectedItem().getId();
                    getEmployeeList(int_employeeID);
                }
            });
        }

        return null;
    }

    private void initialize() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //employees
        employeeList = new DataListBox();
        employeeList.addStyleName(DEFAULT_WIDTH);
        employeeList.ensureDebugId(test_code_ID_name + "employees");
        employeeList.addValueChangeHandler(sender -> {
            //
            if (employeeList.isSomethingSelected()) {
                int_employeeID = employeeList.getSelectedItem().getId();
                getEmployeeList(int_employeeID);
            } else {
                goalWeightEditTab.removeAll();
            }
        });

        //edit goal weights tab
        goalWeightEditTab = new GoalWeightEditTab(hrmsStrings.editGoalWeights());
        goalWeightEditTab.setSize("100%", "150px");
        goalWeightEditTab.ensureDebugId(test_code_ID_name + "edit_goal_weights_tab");
        //add business goal link
        WfmButton2 addBusinessGoal = new WfmButton2(hrmsStrings.addBusinessGoal(), null, "markPlus", event -> {
            //add business goal logic
            SinksContainerFactory.entryPoint.onHistoryChanged("busingoal|add/add//" + BUSINESS_GOAL);
        });
        addBusinessGoal.ensureDebugId(test_code_ID_name + "add_business_goal_link");
        //add department goal link
        WfmButton2 addDepartmentGoal = new WfmButton2(Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.addDepartmentGoal(), wfmStrings.department()), null, "markPlus", event -> {
            //add department goal logic
            SinksContainerFactory.entryPoint.onHistoryChanged("departmentgoal|add/add//" + DEPARTMENT_GOAL);
        });
        addDepartmentGoal.ensureDebugId(test_code_ID_name + "add_department_goal_link");
        //add project goal link
        WfmButton2 addProjectGoal = new WfmButton2(Property.get(Constants.PROJECT, hrmsStrings.addProjectGoal(), wfmStrings.project()), null, "markPlus", event -> {
            //add project goal logic
            SinksContainerFactory.entryPoint.onHistoryChanged("projectgoal|add/add//" + PROJECT_GOAL);
        });
        addProjectGoal.ensureDebugId(test_code_ID_name + "add_project_goal_link");
        //buttons panel div
        HorizontalPanelDiv buttonsPanel = new HorizontalPanelDiv();
        buttonsPanel.add(5, addBusinessGoal, addDepartmentGoal, addProjectGoal);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //add field items

        //edit goal weight details -> 1
        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.editGoalWeights() + " " + wfmStrings.details()/*"Edit goal weight details"*/);
        addField(CustomFormConstants.GOAL_ASSIGNEES, employeeList, getTitle(wfmStrings.employee(), true));
        //goal weight edit tab -> 2
        addField(CustomFormConstants.GOAL_WEIGHT, goalWeightEditTab, hrmsStrings.goals(), true);
        addField(CustomFormConstants.BUTTONS, buttonsPanel, null);

        show();
    }

    private void getEmployeeList(Integer int_employeeID) {
        goalWeightEditTab.removeAll();

    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        GoalAssigneeItem[] items = setValues();
        //register save logic
        LoadingPanel.loading(true);
        HrmsService.App.get().saveGoalAssigneeItems(items, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable t) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void o) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), hrmsStrings.goalWeight()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private GoalAssigneeItem[] setValues() {
        HashMap<String, Double> map = goalWeightEditTab.getMap();
        int i = -1, n = map.size();
        GoalAssigneeItem[] items = new GoalAssigneeItem[n];
        for (String assignID : map.keySet()) {
            GoalAssigneeItem peItem = new GoalAssigneeItem();
            Double weight = map.get(assignID);
            String[] b = assignID.split(",");
            for (String s : b) {
                if ("".equals(s)) {
                    break;
                }
                peItem.setObjectId(Integer.valueOf(s));
            }
            peItem.setWeight(weight);
            items[++i] = peItem;
        }
        return items;
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += markAsError(employeeList, !Validation.validateListBoxRequired(employeeList, new HTML(), wfmStrings.pleaseSelectEmployee()));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}