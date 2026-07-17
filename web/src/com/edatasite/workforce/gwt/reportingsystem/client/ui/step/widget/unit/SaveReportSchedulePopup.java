package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Virus on 10/1/14.
 */
public class SaveReportSchedulePopup extends KpiModal implements SchedulerConstant {
    private final ReportingStepControlView view;

    interface SaveReportSchedulePopupUiBinder extends UiBinder<HTMLPanel, SaveReportSchedulePopup> {
    }

    private static final SaveReportSchedulePopupUiBinder ourUiBinder = GWT.create(SaveReportSchedulePopupUiBinder.class);
    @UiField
    DataListBox emailTemplate;
    @UiField
    FlowPanel sharedPanel;
    @UiField
    LabelElement chooseEmailTemp;
    @UiField
    RecurringWidget recurringWidget;

    private final TableColumn[] columns = new TableColumn[2];
    private SelectPanel targetEmployes;

    private final WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
    private final WfmButton2 closeButton = new WfmButton2(wfmStrings.close());

    public SaveReportSchedulePopup(ReportingStepControlView view) {
        super();
        setSize(940, 800);
        addStyleName("bt_area");
        add(ourUiBinder.createAndBindUi(this));
        recurringWidget.setInitialClasses("group-box--no-padding recurringWidget--cron-mini");
        this.view = view;

        chooseEmailTemp.setInnerHTML(wfmStrings.template());

        drawSubscriptionOptions();
        loadingData();
        initHandlers();
        addButton(closeButton);
        addButton(saveButton);
    }

    @Override
    public void center() {
        super.center();
        setData();
    }


    private void setData() {
        RecurrenceJobItem jobItem = view.getReport().getRecurrenceJobItem();
        recurringWidget.setData(jobItem);
    }

    private void initHandlers() {
        saveButton.addClickHandler(event -> {
            saveButton.setEnabled(false);
            RecurrenceJobItem jobItem = recurringWidget.getData();
            jobItem.setJobType(SchedulerConstant.RECURRING_REPORT);
            view.getReport().setRecurrenceJobItem(jobItem);

            //email template
            view.getReport().setEmailTemplateItem(emailTemplate.getSelectedItem());
            //target users
            ArrayList<Integer> users = new ArrayList<>();
            if (targetEmployes.getSelectedItems() != null || targetEmployes.getSelectedItems().length != 0) {
                users.addAll(Arrays.asList(targetEmployes.getSelectedItems()));
            } else {
                users.add(Utils.getUserID());
            }
            view.getReport().setTargetUsers(users);

            ReportingService.App.get().saveReport(view.getReport(), new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    saveButton.setEnabled(true);
                    close();
                    Info.show(wfmStrings.failed(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    view.getReport().setId(result);
                    saveButton.setEnabled(true);
                    close();
                }
            });
            saveButton.setEnabled(true);
        });
        closeButton.addClickHandler(event -> close());
    }


    private void loadingData() {
        EmailTemplateService.App.get().getEmailTemplates(Constants.REPORT_REMINDER_CATEGORY, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                emailTemplate.setItems(selectItems);
                SelectItem emailTemplateItem = view.getReport().getEmailTemplateItem();
                if (emailTemplateItem != null && emailTemplateItem.getId() != null) {
                    emailTemplate.setSelected(emailTemplateItem.getId());
                }
            }
        });
        LoadingPanel.loading(true);
        AllInOneService.App.get().getCompanyEmployeesForTree(new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onSuccess(ArrayList<TeamEmployees> result) {
                targetEmployes.clearTreeView();
                TreeSelect.setTickAllVisible(result.size() != 0);
                for (TeamEmployees teamEmployee : result) {
                    targetEmployes.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
                }

                boolean isChecked = false;
                for (int i = 0; i < targetEmployes.getTree().getItemCount(); i++) {
                    final com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem parent = (com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem) targetEmployes.getTree().getItem(i);
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        final com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem child = (com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem) parent.getChild(j);
                        if (child.getItem().getId().equals(Utils.getUserID())) {
                            child.setChecked(true);
                            targetEmployes.onTreeItemSelection(child, null);
                            isChecked = true;
                            break;
                        }
                    }
                    if (isChecked) {
                        break;
                    }
                }

                if (view.getReport().getId() != null) {
                    ReportingService.App.get().getEmployeeIDsByReportID(view.getReport().getId(), new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(ArrayList<Integer> integers) {

                            for (int i = 0; i < targetEmployes.getTree().getItemCount(); i++) {
                                NTreeSelectItem parent = (NTreeSelectItem) targetEmployes.getTree().getItem(i);
                                for (int j = 0; j < parent.getChildCount(); j++) {
                                    NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                                    if (child.getItem().getId().equals(Utils.getUserID())) {
                                        if (integers.size() > 0 && !integers.contains(Utils.getUserID())) {
                                            child.setChecked(false);
                                            targetEmployes.onTreeItemSelection(child, null);
                                        }
                                    }
                                    for (Integer id : integers) {
                                        if (child.getItem().getId().equals(id)) {
                                            child.setChecked(true);
                                            targetEmployes.onTreeItemSelection(child, null);
                                            break;
                                        }
                                    }
                                }
                            }

                            targetEmployes.expandTreeView();
                            LoadingPanel.loading(false);
                        }
                    });
                } else {
                    targetEmployes.expandTreeView();
                }
            }
        });
        if (view.getReport().getRecurrenceJobItem() != null) {
            recurringWidget.setData(view.getReport().getRecurrenceJobItem());
        }
    }

    private void drawSubscriptionOptions() {
        columns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee(), 210);
        columns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action(), 50);
        targetEmployes = new SelectPanel(columns, true);

        targetEmployes.setStyleName(com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment.BLUE + "_left");

        targetEmployes.hideAvailablityCheckBox();

        targetEmployes.checkAllItems(true);
        targetEmployes.expandTreeView();

        FormGroup formGroup = new FormGroup(wfmStrings.shareThisReportWith(), targetEmployes);
        sharedPanel.add(formGroup);
    }

}