package com.finnetlimited.reportservice.core.client.ui.panel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.finnetlimited.reportservice.core.client.ui.ReportingBaseEntryPoint;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.refresh.DRSRefresh;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: User
 * Date: 30.04.12
 * Time: 14:54
 */
public class DRSSaveReportSchedulePopup extends KpiModal implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CoreServiceAsync coreService = CoreService.App.get();

    private HTML error = new HTML("");


    private FlexPanel schedulePanel;
    private DRSButton saveReport;
    private Anchor createDefaultFolder;
    private DRSButton close;
    private ReportRpc report;
    private Command command;
    private Command changeModifyStatus;
    private ReportReminder recurringFormView;
    private RecurrenceJobItem recurrenceJobItem;
    private Integer savedReportId;
    private FlowPanel popupBody;
    private HorizontalPanel topPanel;

    private DataListBox emailTemplate;
    private final TableColumn[] columns = new TableColumn[2];
    private SelectPanel targetEmployes;

    private FlowPanel sharedPanel = null;

    private final String xml;
    private HorizontalPanel choosetemplateRow;
    private Integer reportID = null;
    private Integer selectedEmailTemplateID = null;

    public DRSSaveReportSchedulePopup(String title, String xml) {
        this.xml = xml;
        setTitle(title);
        setSize("450px", "280px");
    }

    private void init() {
        error = new HTML("");
        choosetemplateRow = new HorizontalPanel();
        recurringFormView = new ReportReminder();

        //save button panel
        saveReport = new DRSButton(wfmStrings.saveReport(), DRSButton.BUTTON_STYLE);
        close = new DRSButton("Close", DRSButton.BUTTON_STYLE);
        saveReport.addClickHandler(clickEvent -> {
            if (validate()) {
                save();
            }
        });
        close.addClickHandler(clickEvent -> close());
        HorizontalPanel savePanel = new HorizontalPanel();
        savePanel.add(saveReport);
        savePanel.setCellVerticalAlignment(saveReport, VerticalPanel.ALIGN_MIDDLE);
        savePanel.add(close);
        savePanel.setCellVerticalAlignment(close, VerticalPanel.ALIGN_MIDDLE);


        popupBody = new FlowPanel();
        topPanel = new HorizontalPanel();
        popupBody.setStyleName("order-table");
        getEmailTemplates();

        HorizontalPanel naughtRow = new HorizontalPanel();


        HorizontalPanel schedulePanelRow = new HorizontalPanel();
        naughtRow.setStyleName("naught-style-name");

        VerticalPanel verticalPanel = new VerticalPanel();
        if (!error.equals("")) {
            naughtRow.add(error);
        }
        choosetemplateRow.add(new HTML("<b style='color:#015d9f;'> " + wfmStrings.template() + ":</b>"));
        choosetemplateRow.setCellHorizontalAlignment(emailTemplate, HasHorizontalAlignment.ALIGN_RIGHT);
        choosetemplateRow.add(emailTemplate);

        if (recurrenceJobItem != null) {
            recurringFormView.drawForm(recurrenceJobItem);
        } else {
            recurringFormView.drawForm(null);
        }
        schedulePanel = new FlexPanel();

        schedulePanel.add(choosetemplateRow);
        schedulePanel.add(recurringFormView);

        drawSubscriptionOptions();


        schedulePanelRow.add(schedulePanel);
        verticalPanel.setStyleName("left-panel");
        verticalPanel.add(naughtRow);

        verticalPanel.add(schedulePanelRow);
        topPanel.add(verticalPanel);
        topPanel.setStyleName("top-panel-style");
        verticalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        verticalPanel.add(savePanel);
        popupBody.add(topPanel);


        add(popupBody);
    }

    private void save() {
        ReportRpc reportTemp = saveEvent.saveReportEvent();
        if (reportTemp != null) {
            report = reportTemp;
        }
        report.setSelectedColumns(getReport().getSelectedColumns());
        if (recurringFormView != null) {
            if (!recurringFormView.validate()) {
                return;
            }
            RecurrenceJobItem recurrenceJobItem = recurringFormView.getData();
            report.setStartDate(recurringFormView.getData().getStartDate().toString());
            if (recurringFormView.getData().getEndDate() != null) {
                report.setEndDate(recurringFormView.getData().getEndDate().toString());
            }
            if (recurrenceJobItem != null) {
                report.setRecurrenceJobItem(recurrenceJobItem);
            }
        }
        report.setEmailTemplateItem(emailTemplate.getSelectedItem(true));
        ArrayList<Integer> users = new ArrayList<>();
        if (targetEmployes.getSelectedItems() != null || targetEmployes.getSelectedItems().length != 0) {
            users.addAll(Arrays.asList(targetEmployes.getSelectedItems()));
        } else {
            users.add(Utils.getUserID());
        }
        report.setTargetUsers(users);


        saveReport.setEnabled(false);
        close.setEnabled(false);
        coreService.saveReport(report, new AsyncCallback<Integer>() {
            public void onFailure(Throwable throwable) {
                saveReport.setEnabled(true);
                close.setEnabled(true);
                close();
            }

            public void onSuccess(Integer result) {
                if (result != 0) {
                    ReportingBaseEntryPoint.loadPermission();
                    report.setOwner(true);
                    DRSRefresh.registrationRefreshPages(HistoryNamesType.ReportList);
                    saveReport.setEnabled(true);
                    close.setEnabled(true);
                    if (command != null) {
                        command.execute();
                    }
                    if (changeModifyStatus != null) {
                        changeModifyStatus.execute();
                    }
                    savedReportId = result;
                    close();

                } else {
                    error.setHTML("<b style='color:green'>" + wfmStrings.reportNameAlreadyExist() + "</b>");
                    saveReport.setEnabled(true);
                    close.setEnabled(true);
                }

            }
        });
    }

    public boolean validate() {
        int errorCount = 0;
        error.setHTML("");
        errorCount += (recurringFormView != null && !recurringFormView.validate()) ? 1 : 0;

        if (errorCount > 0) {
            error.setHTML("<b style='color:red;'>" + wfmStrings.fillRequiredField() + "</b>");
            return false;
        }
        return true;
    }

    public interface SaveReportEvent {
        ReportRpc saveReportEvent();
    }

    private SaveReportEvent saveEvent;

    public void addSaveEvent(SaveReportEvent saveEvent) {
        this.saveEvent = saveEvent;
    }

    private void clearAll() {
        error.setHTML("&nbsp;");
    }

    public void showPopup(Integer id, Integer selectedEmailTemplateID, final Boolean isinit) {
        reportID = id;
        this.selectedEmailTemplateID = selectedEmailTemplateID;
        clearAll();
        center();
        if (id != null) {
            DRSLoadingPanel.show();
            coreService.getFolderByReportId(id, new AsyncCallback<FolderRpc>() {
                public void onFailure(Throwable caught) {
                    DRSLoadingPanel.hide();
                }

                public void onSuccess(FolderRpc result) {
                    DRSLoadingPanel.hide();

                    if (result.getRecurrenceId() != null) {
                        recurrenceJobItem = result.getRecurrenceJobItem();
                    }
                    if (isinit) {
                        clear();
                        init();
                    }
                    open();
                }
            });
        } else {
            open();
        }
    }

    private void drawSubscriptionOptions() {
        sharedPanel = new FlowPanel();
        sharedPanel.add(new HTML("<b style='color:#015d9f;'>" + wfmStrings.shareThisReportWith() + ":</b>"));

        columns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee(), 210);
        columns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action(), 50);
        targetEmployes = new SelectPanel(columns, true);

        targetEmployes.setStyleName(com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment.BLUE);
        targetEmployes.setHeight(210);
        targetEmployes.getTable().setHeight(210);
        targetEmployes.setTreePanelHeight(220);
        targetEmployes.setTreePanelWidth(200);
        targetEmployes.setSearchBoxWidth("154px");
        targetEmployes.setSearchBoxHeight("14px");

        targetEmployes.hideAvailablityCheckBox();

        getEmployess();
        targetEmployes.checkAllItems(true);
        targetEmployes.expandTreeView();
        sharedPanel.add(targetEmployes);
        topPanel.add(sharedPanel);
    }

    private void getEmailTemplates() {
        emailTemplate = new DataListBox();
        emailTemplate.setWidth("200px");
        EmailTemplateService.App.get().getEmailTemplates(REPORT_REMINDER_CATEGORY, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                emailTemplate.setItems(selectItems);

                if (selectedEmailTemplateID != null) {
                    emailTemplate.setSelected(selectedEmailTemplateID);
                }
            }
        });
    }

    public void getEmployess() {
        DRSLoadingPanel.show(targetEmployes);
        AllInOneService.App.get().getCompanyEmployeesForTree(new AbstractAsyncCallback<ArrayList<TeamEmployees>>() {
            public void success(ArrayList<TeamEmployees> result) {
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
                if (reportID != null) {
                    coreService.getEmployeeIDsByReportID(reportID, new AsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            DRSLoadingPanel.hide();
                        }

                        @Override
                        public void onSuccess(ArrayList<Integer> integers) {

                            DRSLoadingPanel.hide();

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
                            DRSLoadingPanel.hide();
                        }
                    });
                } else {
                    targetEmployes.expandTreeView();
                }
            }
        });

    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getChangeModifyStatus() {
        return changeModifyStatus;
    }

    public void setChangeModifyStatus(Command changeModifyStatus) {
        this.changeModifyStatus = changeModifyStatus;
    }

    public Integer getSavedReportId() {
        return savedReportId;
    }
}