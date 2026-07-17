package com.finnetlimited.reportservice.core.client.ui.body;

import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSStepPanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 25-Mar-2010
 * Time: 17:00:20
 */

public abstract class AbstractStepReportBody extends LazyPanel {

    public static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final String STEP_BODY = IdType.STEP_BODY.getName();
    public static int num = 0;

    private final String id;
    private final String name;
    protected ReportingModuleSettings reportingModuleSettings;
    private boolean isEnabledSaveReportButton = true;
    private boolean isEnabledNextReportButton = true;
    private boolean isEnabledCloseReportButton = true;
    private boolean isEnabledAddReportToDashboardButton = false;
    private boolean isEnabledRunReportButton = true;
    private boolean isEnabledBackReportButton = false;
    private boolean widgetInitializing;
    private boolean widgetInitialized;
    private StepReportBody body;
    private HTMLPanel widgetPanel;


    public AbstractStepReportBody(String name, ReportingModuleSettings settings) {
        this.name = name;
        id = (STEP_BODY + num);
        num++;
        this.reportingModuleSettings = settings;
    }

    public String getName() {
        return name;
    }

    public void setBody(StepReportBody body) {
        this.body = body;
    }

    public DRSStepPanel getStepPanel() {
        return body.getStepPanel();
    }

    public ReportRpc getReport() {
        return body.getReport();
    }

    public void setReport(ReportRpc report) {
        this.body.setReport(report);
    }

    protected Widget createWidget() {
        widgetPanel = new HTMLPanel("");
        DOM.setElementAttribute(widgetPanel.getElement(), "id", id);
        return widgetPanel;
    }

    @Override
    public void onLoad() {
        show();
        ensureWidget();
    }

    @Override
    public void ensureWidget() {
        super.ensureWidget();
        ensureWidgetInitialized();
    }

    private void ensureWidgetInitialized() {
        if (widgetInitializing || widgetInitialized) {
            hide();
            return;
        }

        widgetInitializing = true;

        asyncOnInitialize(new AsyncCallback<Widget>() {
            public void onFailure(Throwable reason) {
                hide();
                widgetInitializing = false;
            }

            public void onSuccess(Widget result) {
                hide();
                widgetInitializing = false;
                widgetInitialized = true;

                if (result != null) {
                    widgetPanel.add(result);
                }

                onInitializeComplete();
            }
        });
    }

    public void show() {
    }

    public void hide() {
    }

    public void onInitializeComplete() {
    }
// Write all user interface widget this is methods

    public abstract Widget onInitialize();

    protected abstract void asyncOnInitialize(final AsyncCallback<Widget> callback);

    public abstract void next();

    public abstract void back();

    public abstract void save();

    public abstract void addReportToDashboard();

    public abstract void runReport();

    public abstract void refreshChanges();

    public abstract ReportRpc getReportRpc();

    public void addWidget(Widget widget) {
        widgetPanel.add(widget, id);
    }

    public void goToNextStep(String historyName) {
        body.goToNextStep(historyName);

    }

    public void goToBackStep(String historyName) {
        body.goToBackStep(historyName);
    }

    public void goToOwnStepDown(String historyName) {
        body.goToOwmStepDown(historyName);
    }

    public void goToClickStep(String historyName) {
        body.goToClickStep(historyName);
    }

    public boolean isWidgetInitialized() {
        return widgetInitialized;
    }

    public boolean isEnabledSaveReportButton() {
        return reportingModuleSettings.getShowButtons() && isEnabledSaveReportButton;
    }

    public void setEnabledSaveReportButton(boolean enabledSaveReportButton) {
        this.isEnabledSaveReportButton = enabledSaveReportButton;
    }

    public boolean isEnabledNextReportButton() {
        return isEnabledNextReportButton;
    }

    public void setEnabledNextReportButton(boolean enabledNextReportButton) {
        this.isEnabledNextReportButton = enabledNextReportButton;
    }

    public boolean isEnabledCloseReportButton() {
        return isEnabledCloseReportButton;
    }

    public void setEnabledCloseReportButton(boolean enabledCloseReportButton) {
        this.isEnabledCloseReportButton = enabledCloseReportButton;
    }

    public boolean isEnabledAddReportToDashboardButton() {
        return isEnabledAddReportToDashboardButton;
    }

    public void setEnabledAddReportToDashboardButton(boolean enabledAddReportToDashboardButton) {
        this.isEnabledAddReportToDashboardButton = enabledAddReportToDashboardButton;
    }

    public boolean isEnabledRunReportButton() {
        return isEnabledRunReportButton;
    }

    public void setEnabledRunReportButton(boolean enabledRunReportButton) {
        isEnabledRunReportButton = enabledRunReportButton;
    }

    public boolean isEnabledBackReportButton() {
        return isEnabledBackReportButton;
    }

    public void setEnabledBackReportButton(boolean enabledBackReportButton) {
        this.isEnabledBackReportButton = enabledBackReportButton;
    }


    @Override
    public void clear() {
        if (widgetInitialized) {
            widgetPanel.clear();
            widgetInitialized = false;
            widgetInitializing = false;
        }
    }

    public void clearStepBody(String historyName) {
        body.clearStepBody(historyName);
    }

    public void checkReportTypeSetColumnGrouping() {

        if (getReport().getTableType().equals(ReportType.SUMMARY.name())) {
            getReport().setModified(true);
            getReport().setPosition(1);
            getStepPanel().insertStep(reportingStrings.selectGrouping(), HistoryNamesType.AddGroupingReport.name(), 1);
            getStepPanel().getElement().getStyle().setWidth(920, Style.Unit.PX);
            clearStepBody(HistoryNamesType.AddGroupingReport.name());
            clearStepBody(HistoryNamesType.ReportList.name());
        } else {
            getReport().setModified(true);
            getReport().setPosition(1);
            getReport().setGroupColumns(new LinkedList<>());
            getStepPanel().removeStep(HistoryNamesType.AddGroupingReport.name());
            getStepPanel().getElement().getStyle().setWidth(781, Style.Unit.PX);
            clearStepBody(HistoryNamesType.ReportList.name());
        }
    }
}
