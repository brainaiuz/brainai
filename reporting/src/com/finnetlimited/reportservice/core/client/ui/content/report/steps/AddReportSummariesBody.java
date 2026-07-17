package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BannerPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BodyPanel;
import com.finnetlimited.reportservice.core.client.ui.table.SummariesTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:33:30
 */
public class AddReportSummariesBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabel";

    private HTML topHelpMsg;
    private HTML viewLabel;
    private HTMLPanel thema;
    private FlowPanel summariesColImg;
    private BannerPanel help;
    private BodyPanel bodyPanel;
    private SummariesTable tables;
    private Command command;

    public AddReportSummariesBody(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledSaveReportButton(false);
        setEnabledBackReportButton(true);
    }

    @Override
    public void show() {
        DRSLoadingPanel.show();
    }

    @Override
    public void hide() {
        DRSLoadingPanel.hide();
    }

    @Override
    public Widget onInitialize() {
        setUptContent();
        return null;
    }


    private void setUptContent() {
        viewLabel = new HTML(getReport().getViewName() + "&nbsp;");
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>" + reportingStrings.addReport() + "-&nbsp;</div></h2>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");
        addWidget(thema);
        //addWidget(new HTML("<br/>"));

        topHelpMsg = new HTML("<h2 class='sub-title'>" + reportingStrings.selectReportSummarize() + "</h2>" +
                "<p class='help-sub-title'>" + reportingStrings.selectColumnToIncludeInYourReport() + ":</p>");

        addWidget(topHelpMsg);

        summariesColImg = new FlowPanel();
        summariesColImg.addStyleName("summariesColImg");


        help = new BannerPanel();
        help.addWidget(summariesColImg);
        addWidget(help);

        command = () -> getReport().setModified(true);
        tables = new SummariesTable();
        tables.setStyleName("leftBarInner");
        tables.setCommand(command);
        CoreService.App.get().getSummariesColumns(getReport(), new AsyncCallback<ArrayList<ColumnRpc>>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(ArrayList<ColumnRpc> columnRpc) {
                tables.addTadbleData(columnRpc);
            }
        });


        bodyPanel = new BodyPanel();
        bodyPanel.addWdget(tables);
        addWidget(bodyPanel);
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public void next() {
        goToNextStep(HistoryNamesType.AddChartReport.name());
    }

    public void back() {
        goToBackStep(HistoryNamesType.AddColumnsReport.name());
    }

    public void save() {

    }

    public void addReportToDashboard() {

    }


    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {
        viewLabel.setText(getReport().getViewName());
        CoreService.App.get().getSummariesColumns(getReport(), new AsyncCallback<ArrayList<ColumnRpc>>() {
            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(ArrayList<ColumnRpc> columnRpc) {
                tables.removeAllColumn();
                tables.addTadbleData(columnRpc);
            }
        });
    }

    public ReportRpc getReportRpc() {
        getReport().setSumaries(tables.getCheckedColumns());
        return getReport();
    }
}
