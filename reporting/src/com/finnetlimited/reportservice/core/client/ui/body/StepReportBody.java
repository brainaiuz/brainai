package com.finnetlimited.reportservice.core.client.ui.body;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.ReportContent;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.content.report.steps.*;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSStepPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ${Dilsh0d}
 * Date: 26-Mar-2010
 * Time: 15:37:23
 */

public class StepReportBody extends HTMLPanel {

    public static final String id = IdType.STEP_BODY_OWNER.getName();

    private DRSButton save;
    private DRSButton next;
    private DRSButton back;
    private DRSButton addReportToDashboard;
    private DRSButton runReport;

    private DRSStepPanel stepPanel;
    private ReportContent reportContent;
    private AbstractStepReportBody widget;
    private ReportRpc report = new ReportRpc();
    private Map<String, AbstractStepReportBody> widgetMap = new HashMap<>();
    private ReportingModuleSettings reportingModuleSettings;

    public StepReportBody(DRSStepPanel stepPanel, ReportContent reportContent, ReportingModuleSettings settings) {
        super("");
        this.stepPanel = stepPanel;
        this.reportContent = reportContent;
        this.reportingModuleSettings = settings;
        getElement().setAttribute("id", id);
        setStyleName("reportingMainContent");
    }

    public interface HistoryChanges {
        void historyChanges(String name);
    }

    private HistoryChanges historyChanges;

    public void addHistoryChanges(HistoryChanges historyChanges) {
        this.historyChanges = historyChanges;
    }

    private void addWidget(AbstractStepReportBody reportStepBody) {
        if (widget != null) {
            remove(widget);
        }
        widget = reportStepBody;
        widget.setBody(this);
        if (widget.isEnabledSaveReportButton() && report.isLibrary()) {
            widget.setEnabledSaveReportButton(false);
        }
        save.setVisible(widget.isEnabledSaveReportButton());
        next.setVisible(widget.isEnabledNextReportButton());
        back.setVisible(widget.isEnabledBackReportButton());
        //        addReportToDashboard.setVisible(widget.isEnabledAddReportToDashboardButton() && reportingModuleSettings.getShowButtons());
        runReport.setVisible(widget.isEnabledRunReportButton() && reportingModuleSettings.getShowButtons());

        if (reportingModuleSettings.enableWFTListing()) {
            runReport.setVisible(true);
            runReport.setText("Filter");
        }
        add(widget, id);

    }

    public void init(Integer id, String historyName) {
        report.setId(id);
        if (reportContent.getDataType() != null) {
            report.setViewName(reportContent.getDataType());
        }
        setUpOption();
        addWidgetByParam(historyName);
    }

    private void setUpOption() {

        /*   if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddDataTypeReport.name())) {
           AddReportDataTypeBody addReportDataTypeBody = new AddReportDataTypeBody(HistoryNamesType.AddDataTypeReport.name(), this.reportingModuleSettings);
           widgetMap.put(addReportDataTypeBody.getName(), addReportDataTypeBody);
       } */

        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddGroupingReport.name())) {
            AddReportGroupingBody groupingBody = new AddReportGroupingBody(HistoryNamesType.AddGroupingReport.name(), reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddGroupingReport.name(), groupingBody);
        }
        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddTypeReport.name())) {
            AddReportTypeBody typeReportBody = new AddReportTypeBody(HistoryNamesType.AddTypeReport.name(), reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddTypeReport.name(), typeReportBody);
        }
        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddColumnsReport.name())) {
            AddReportColumnsBody addReportColumnsBody = new AddReportColumnsBody(HistoryNamesType.AddColumnsReport.name(), this.reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddColumnsReport.name(), addReportColumnsBody);
        }
        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddSummariesReport.name())) {
            AddReportSummariesBody addReportSummariesBody = new AddReportSummariesBody(HistoryNamesType.AddSummariesReport.name(), this.reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddSummariesReport.name(), addReportSummariesBody);

        }
        /*if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddOrderReport.name())) {
            AddReportOrderBody addReportOrderBody = new AddReportOrderBody(HistoryNamesType.AddOrderReport.name(), this.reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddOrderReport.name(), addReportOrderBody);
        }*/
        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddFilterReport.name())) {
            AddReportFilterBody addReportFilterBody = new AddReportFilterBody(HistoryNamesType.AddFilterReport.name(), this.reportingModuleSettings);
            widgetMap.put(HistoryNamesType.AddFilterReport.name(), addReportFilterBody);
        }
        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.ReportList.name())) {
            ReportListBody reportListBody = new ReportListBody(HistoryNamesType.ReportList.name(), this.reportingModuleSettings);
            widgetMap.put(reportListBody.getName(), reportListBody);
        }
//        if (this.reportingModuleSettings.getShowAllSteps() || reportingModuleSettings.getActiveSteps().contains(HistoryNamesType.AddReportToDashboard.name())) {
//            AddReportToDashboard addReportToDashboard = new AddReportToDashboard(HistoryNamesType.AddReportToDashboard.name(), this.reportingModuleSettings);
//            widgetMap.put(addReportToDashboard.getName(), addReportToDashboard);
//        }
    }

    private void addWidgetByParam(String historyName) {
        if (historyName != null) {
            addWidget(widgetMap.get(historyName));
        } else {
            addWidget(widgetMap.get(HistoryNamesType.AddDataTypeReport.name()));
        }
    }

    public void goToNextStep(String historyName) {

        //     historyName = reportingModuleSettings.getActiveSteps().get(reportingModuleSettings.getActiveSteps().indexOf(historyName) + 1);
        if (widgetMap.containsKey(historyName)) {
            if (historyChanges != null) {
                historyChanges.historyChanges(historyName);
            }
            setReport(widget.getReportRpc());
            if (widgetMap.get(historyName).isWidgetInitialized()) {
                widgetMap.get(historyName).refreshChanges();
            }
            addWidget(widgetMap.get(historyName));
        }
    }

    public void goToBackStep(String historyName) {
        //  historyName = reportingModuleSettings.getActiveSteps().get(reportingModuleSettings.getActiveSteps().indexOf(historyName) - 1);
        if (widgetMap.containsKey(historyName)) {
            if (historyChanges != null) {
                historyChanges.historyChanges(historyName);
            }
            setReport(widget.getReportRpc());
            addWidget(widgetMap.get(historyName));
        }
    }

    public void goToClickStep(String historyName) {
        if (widgetMap.containsKey(historyName)) {
            setReport(widget.getReportRpc());
            if (historyChanges != null) {
                historyChanges.historyChanges(historyName);
            }
            if (widgetMap.get(historyName).isWidgetInitialized()) {
                widgetMap.get(historyName).refreshChanges();
            }
            addWidget(widgetMap.get(historyName));
        }
    }

    public void goToOwmStepDown(String historyName) {
        reportContent.refreshContent(historyName);
        reportContent.goToContent(historyName);
    }

    public void clearStepBody(String historyName) {
        if (widgetMap.get(historyName).isWidgetInitialized()) {
            widgetMap.get(historyName).clear();
        }
    }

    public void nextStep() {
        widget.next();
    }

    public void backStep() {
        widget.back();
    }

    public void runReport() {
        widget.runReport();
    }

    public void saveStep() {
        widget.save();
    }

    public void addReportToDashboard() {
        widget.addReportToDashboard();
    }

    public void refresh() {
        widget.refreshChanges();
    }

    public DRSStepPanel getStepPanel() {
        return stepPanel;
    }

    public boolean isSaveReport() {
        return widget.isEnabledSaveReportButton();
    }

    public boolean isNextReport() {
        return widget.isEnabledNextReportButton();
    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
//        if (!(report.isOwner() || Utils.hasPermission(report.getPermissionCode(false)) || Utils.hasPermission(report.getPermissionCode(true)) || report.getClonable() || "Private".equals(report.getFolderType()))) {
//            Utils.redirect(GWT.getHostPageBaseURL() + Constants.WORKSPACE_URL);
//        }
        this.report = report;
    }

    public void setSave(DRSButton save) {
        this.save = save;
    }

    public void setNext(DRSButton next) {
        this.next = next;
    }

    public void setBack(DRSButton back) {
        this.back = back;
    }

    public void setAddReportToDashboard(DRSButton addReportToDashboard) {
        this.addReportToDashboard = addReportToDashboard;
    }

    public void setRunReport(DRSButton runReport) {
        this.runReport = runReport;
    }

    public ReportContent getReportContent() {
        return reportContent;
    }

    public void setReportContent(ReportContent reportContent) {
        this.reportContent = reportContent;
    }
}
