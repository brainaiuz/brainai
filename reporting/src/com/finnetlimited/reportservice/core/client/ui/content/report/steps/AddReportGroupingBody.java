package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.core.client.ui.Tag;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BodyPanel;
import com.finnetlimited.reportservice.core.client.ui.table.GroupingTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:35:59
 */
public class AddReportGroupingBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabelGrouping";

    private HTML viewLabel;
    private HTMLPanel thema;
    private Tag infoImg;
    private HTML bannerPanel;
    private BodyPanel bodyPanel;
    private GroupingTable groupTable;
    private Command command;

    public AddReportGroupingBody(String name, ReportingModuleSettings settings) {
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

        HTML infoMsg = new HTML("<h2 class='sub-title'>" + reportingStrings.selecrReportingGrouping() + "</h2>");
        addWidget(infoMsg);

        infoImg = new Tag("div", "summariesInfo");
        infoImg.setSize("400px", "190px");
        //        bannerPanel = new BannerPanel();
//        bannerPanel.addWidget(infoImg);
        addWidget(infoImg);

        command = () -> getReport().setModified(true);
        groupTable = new GroupingTable();
        groupTable.setCommand(command);
        if (getReport().getId() != null) {
            CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                public void onFailure(Throwable caught) {

                }

                public void onSuccess(ReportRpc report) {
                    groupTable.drawSaveReportGroupingTable(report);
                }
            });
        } else if (getReport().getColumnMap().size() > 0) {
            groupTable.drawSaveReportGroupingTable(getReport());
        } else {
            CoreService.App.get().getSelectedColumns(getReport(), new AsyncCallback<LinkedList<ColumnRpc>>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(LinkedList<ColumnRpc> columnRpc) {
                    getReport().setSelectedColumns(columnRpc);
                    groupTable.drawGroupingTable(columnRpc);
                }
            });
        }

        bodyPanel = new BodyPanel();
        bodyPanel.addWdget(groupTable);

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
        goToNextStep(HistoryNamesType.AddColumnsReport.name());
    }

    public void back() {
        goToBackStep(HistoryNamesType.AddTypeReport.name());
    }

    public void save() {

    }

    public void addReportToDashboard() {

    }

    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {
//        groupTable.clear();
//        groupTable.drawGroupingTable(getReport().getSelectedColumns());
    }

    public ReportRpc getReportRpc() {
        setReport(groupTable.getReport(getReport()));
        return getReport();
    }
}
