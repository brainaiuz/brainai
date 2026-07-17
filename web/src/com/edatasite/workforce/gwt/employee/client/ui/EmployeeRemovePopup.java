package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.localization.EmployeeMessages;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.rpc.TeamEmployee;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * User: java
 * Date: 26.03.2009
 * Time: 21:00:35
 */
public class EmployeeRemovePopup extends Composite implements Constants {

    private static final EmployeeMessages employeeMessages = EmployeeMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    //Employee view popup variable
    private final Integer employeeId;
    private final ListingPanel listPanel;
    private final String employeeName;
    private KpiModal popupEmployee;
    private TeamEmployee teams;
    private TeamEmployee managers;
    final KpiCheckBox removeContactCheckBox;
    //Teams Popup variable
    private WfmButton2 ok;
    private WfmButton2 cancel;
    private KpiModal teamShell;
    private KpiModal projectShell;
    private DataListBox listBox;
    private DataListBox managerList;
    private final boolean isRemove;
    private String roles;
    private final String fromView;
    private Command deleteCommand;
    private DatePicker resignationDate;
    public VerticalPanel panel;
    private ArrayList<ReferenceItem> references;
    private LinkedHashMap<KpiRadioButton, ReferenceItem> radiobuttons;
    private LinkedHashMap<KpiRadioButton, TextArea2> descriptions;
    private ReferenceItem rejectionReason;

    public void selectionListener() {
        setEnabled(false);
        init(isRemove);
    }

    public EmployeeRemovePopup(String fromView, String employeeId, String employeeName, ListingPanel listPanel, boolean isRemove) {
        this.fromView = fromView;
        this.employeeId = Integer.valueOf(employeeId);
        this.employeeName = employeeName;
        this.listPanel = listPanel;
        this.isRemove = isRemove;
        removeContactCheckBox = new KpiCheckBox("&nbsp;", true);
        removeContactCheckBox.setValue(false);
        initWidget(new KpiCheckBox());
    }

    private boolean isFromTrainingCENTER() {
        return EmployeeListView.FROM_TRAINING_CENTER.equals(fromView);
    }

    private boolean isFromHRMS() {
        return EmployeeListView.FROM_HRMS.equals(fromView);
    }

    private boolean isFromPAYROLL() {
        return EmployeeListView.FROM_PAYROLL.equals(fromView);
    }

    private boolean isFromPM() {
        return EmployeeListView.FROM_PM.equals(fromView);
    }

    private void initManager() {
        managerList = new DataListBox();

        EmployeeService.App.get().getManagerByEmployeeId(employeeId, new AbstractAsyncCallback<TeamEmployee>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(TeamEmployee result) {
                managers = result;
                if ("".equals(managers.getTeamNames()) || managers.getObjectId() == null) {
                    removeEmployee();
                } else {
                    if (listBox == null || listBox.getItems() == null) {
                        getEmployeeListForManger();
                    } else {
                        managerList.setItems(listBox.getItems());
                        deleteManagerPopupView();
                    }
                }
            }
        });

        managerList.addValueChangeHandler(changeEvent -> managerList.setStyleName(""));
    }
    // Manager delete popup view

    private void deleteManagerPopupView() {
        projectShell = new KpiModal();
        if (isRemove) {
            projectShell.setTitle(wfmStrings.deleteProjectManager());
        } else {
            projectShell.setTitle(wfmStrings.terminateProjectManager());
        }
        projectShell.setWidth(400);

        ok = new WfmButton2(wfmStrings.yes(), WfmButton2.BTN_PRIMARY);
        ok.addClickHandler(clickEvent -> {
            if (managerList != null && managerList.isSomethingSelected()) {
                managerList.setStyleName("");
                ok.setEnabled(false);
                cancel.setEnabled(false);
                removeManagers();
            } else {
                if (managerList != null) {
                    managerList.setStyleName("x-form-invalid");
                }
            }
        });

        cancel = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_RESET, event -> {
            projectShell.close();
            setEnabled(true);
        });


        projectShell.clearContent();
        projectShell.addWidget(new HTML(), employeeMessages.isProjectManager(employeeName) + " " + " <b>" + managers.getTeamNames() + "</b>");
        projectShell.addWidget(managerList, wfmStrings.assignAnotherProjectManager());
        if (isRemove) {
            projectShell.addWidget(removeContactCheckBox, wfmStrings.removeFromContactsToo());
        }

        projectShell.addButton(cancel);
        projectShell.addButton(ok);

        projectShell.center();

    }

    /* remove Project manager if project manager heself delete employee*/

    private void removeManagers() {
        LoadingPanel.loading(true);
        EmployeeService.App.get().removeProjectManagers(managers.getObjectId(), employeeId, managerList.getSelectedItem().getId(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                removeEmployee();
                projectShell.close();
            }
        });
    }

    // this method worked if employee no team leader

    private void getEmployeeListForManger() {
        EmployeeService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                teamShell.close();
            }

            @Override
            public void success(SelectItem[] item) {
                managerList.setItems(item);
                removeDepartmentItem(managerList, employeeId);
                deleteManagerPopupView();
            }
        });
    }

    /* if Employee has Team leader that delete leader team */

    private void initTeams() {
        EmployeeService.App.get().getTeamByEmployeeId(employeeId, new AbstractAsyncCallback<TeamEmployee>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(TeamEmployee result) {
                teams = result;
                if ("".equals(teams.getTeamNames()) || teams.getObjectId() == null) {
                    if (Utils.containsInString(roles, PM)) {
                        initManager();
                    } else {
                        removeEmployee();
                    }
                } else {
                    getEmployeesList();
                }
            }
        });
    }

    /*  get Teams List */

    private void getEmployeesList() {
        listBox = new DataListBox();

        EmployeeService.App.get().getCompanyEmployeesAsSelectItems(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                listBox.setItems(result);
                removeDepartmentItem(listBox, employeeId);
                deleteTeamPopupView();
            }
        });

        listBox.addValueChangeHandler(changeEvent -> listBox.setStyleName(""));

    }

    /* delete Team Popup view */

    private void deleteTeamPopupView() {
        teamShell = new KpiModal();
        if (isRemove) {
            teamShell.setTitle(wfmStrings.deleteDepartmentLeader());
        } else {
            teamShell.setTitle(wfmStrings.terminateDepartmentLeader());
        }
        teamShell.setWidth(400);

        ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);

        ok.addClickHandler(event -> {
            if (listBox != null && listBox.isSomethingSelected()) {
                listBox.setStyleName("");
                ok.setEnabled(false);
                cancel.setEnabled(false);
                removeTeams();
            } else {
                if (listBox != null) {
                    listBox.setStyleName("x-form-invalid");
                }
            }
        });

        cancel = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_RESET, event -> {
            teamShell.close();
            setEnabled(true);
        });

        teamShell.clearContent();
        teamShell.addWidget(new HTML(), employeeMessages.isDepartmentLeader(employeeName) + " " + " <b>" + teams.getTeamNames() + "</b>");
        teamShell.addWidget(listBox, wfmStrings.assignAnotherDepartmentLeader());
        if (isRemove) {
            teamShell.addWidget(removeContactCheckBox, wfmStrings.removeFromContactsToo());
        }

        teamShell.addButton(cancel);
        teamShell.addButton(ok);

        teamShell.center();
    }

    /* remove  Team */

    private void removeTeams() {
        LoadingPanel.loading(true);
        EmployeeService.App.get().removeDepartmentLeader(teams.getObjectId(), employeeId, listBox.getSelectedItem().getId(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                EmployeeService.App.get().getEmployeeRoles(employeeId, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(String roles) {
                        teamShell.close();
                        if (Utils.containsInString(roles, PM)) {
                            initManager();
                        } else {
                            removeEmployee();
                            teamShell.close();
                        }
                    }
                });
            }
        });
    }

    /* delete list values current delete teams names */

    private void removeDepartmentItem(DataListBox list, Integer employeeId) {
        SelectItem[] items = list.getItems();
        SelectItem[] newItem = new SelectItem[items.length - 1];
        int n = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i].getId().equals(employeeId)) {
                n = i;
                break;
            } else {
                newItem[i] = items[i];
            }
        }
        if (n != -1) {
            for (int j = n; j < items.length - 1; j++) {
                items[j] = items[j + 1];
                newItem[j] = items[j];
            }
        }
        list.clear();
        list.setItems(newItem);
    }
    /* employee delete message box */

    private void init(boolean isRemove) {
        popupEmployee = new KpiModal();
        popupEmployee.setTitle(isRemove ? wfmStrings.warning() : wfmStrings.reasonForLosing());
        popupEmployee.setWidth(400);

        VerticalPanel panel = new VerticalPanel();
        panel.addStyleName("options-stack-top");

        if (!isRemove) {
            radiobuttons = new LinkedHashMap<>();
            AllInOneService.App.get().getReferenceChildren(Constants.EMPLOYEE_REJECTION_REASON, new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ArrayList<ReferenceItem> result) {
                    references = new ArrayList<>(result);
                    if (!references.isEmpty()) {
                        for (ReferenceItem referenceItem : references) {
                            KpiRadioButton radioButton = new KpiRadioButton("reference", referenceItem.getName());
                            radiobuttons.put(radioButton, referenceItem);
                            panel.add(radioButton);
                        }
                    }
                    popupEmployee.add(panel);
                }
            });
        }

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, event -> {
            if (!isRemove && (resignationDate.getDate() == null || validate())) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                resignationDate.addStyleName(Constants.ERROR_FORM_STYLE);
            } else {
                EmployeeService.App.get().getEmployeeRoles(employeeId, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(String roles) {
                        setEnabled(false);
                        if (Utils.containsInString(roles, TL)) {
                            initTeams();
                        } else if (Utils.containsInString(roles, PM)) {
                            initManager();
                        } else {
                            removeEmployee();
                        }
                    }
                });
                popupEmployee.close();
            }
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET, event -> {
            popupEmployee.close();
            setEnabled(true);
        });

        HTML center = new HTML(isRemove ?
                employeeMessages.wantToDeleteEmployee(employeeName) :
                employeeMessages.wantToTerminateTheEmploymentOf(employeeName));
        resignationDate = new DatePicker();

        popupEmployee.clearContent();
        popupEmployee.addWidget(center, "");
        if (isRemove) {
            popupEmployee.addWidget(removeContactCheckBox, wfmStrings.removeFromContactsToo());
        } else {
            popupEmployee.addWidget(resignationDate, wfmStrings.specifyResignationDate());
        }

        popupEmployee.addButton(cancel);
        popupEmployee.addButton(ok);

        popupEmployee.center();
    }
    /* remove employee */

    private void removeEmployee() {
        LoadingPanel.loading(true);
        if (radiobuttons != null && radiobuttons.size() > 0) {
            for (KpiRadioButton radioButton : radiobuttons.keySet()) {
                if (radioButton != null && radioButton.getValue()) {
                    rejectionReason = radiobuttons.get(radioButton);
                    String note = null;
                    note = rejectionReason.getName();
                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        note = note + ": " + textArea2.getText();
                    }
                    rejectionReason.setCategory(note);
                }
            }
        }
        if (isRemove) {
            EmployeeService.App.get().deleteEmployee(employeeId, removeContactCheckBox.getValue(), isRemove, null, rejectionReason, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    popupEmployee.close();
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void o) {
                    LoadingPanel.loading(false);
                    if (popupEmployee != null) {
                        popupEmployee.close();
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_DELETE, null, EmployeeRemovePopup.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, EmployeeRemovePopup.this);
                    Info.show((Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), isFromTrainingCENTER() ? wfmStrings.instructor() : wfmStrings.employee())), Info.Type.INFO);
                    if (listPanel != null) {
                        listPanel.reloadPage();
                    } else {
                        if (deleteCommand != null) {
                            deleteCommand.execute();
                        }
                    }
                    setEnabled(true);
                }
            });
        } else {
            EmployeeService.App.get().deleteEmployee(employeeId, false, isRemove, new DateNonConvertable(resignationDate.getDate()), rejectionReason, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    popupEmployee.close();
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void o) {
                    LoadingPanel.loading(false);
                    if (popupEmployee != null) {
                        popupEmployee.close();
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_DELETE, null, EmployeeRemovePopup.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, EmployeeRemovePopup.this);
                    Info.show(wfmStrings.employmentHasBeenTerminatedSuccessfully(), Info.Type.INFO);
                    if (listPanel != null) {
                        listPanel.reloadPage();
                    }
                    setEnabled(true);
                }
            });
        }
    }

    public boolean validate() {
        boolean errorFound = true;
        if (radiobuttons != null && radiobuttons.size() > 0) {
            for (KpiRadioButton radioButton : radiobuttons.keySet()) {
                if (radioButton != null && radioButton.getValue()) {
                    errorFound = false;

                    if (descriptions != null && descriptions.get(radioButton) != null) {
                        TextArea2 textArea2 = descriptions.get(radioButton);
                        errorFound = !Validation.validateTextAreaRequired(textArea2);
                    }
                    break;
                }
            }
        }
        return errorFound;
    }

    public void setEnabled(boolean enabled) {
    }

    public void setDeleteCommand(Command deleteCommand) {
        this.deleteCommand = deleteCommand;
    }
}
