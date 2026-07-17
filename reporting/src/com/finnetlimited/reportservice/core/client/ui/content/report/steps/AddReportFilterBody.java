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
import com.finnetlimited.reportservice.core.client.ui.table.FilterTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.LinkedList;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:34:57
 */
public class AddReportFilterBody extends AbstractStepReportBody {

    private static final String filterInfo = "images/groupingInfo.png";
    private static final String viewLabelId = "viewLabelFilter";

    private HTMLPanel thema;
    private HTML viewLabel;
    private Image infoImg;
    private BannerPanel bannerPanel;
    private BodyPanel bodyPanel;
    private FilterTable filterTable;
    private Command command;

    public AddReportFilterBody(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledNextReportButton(false);
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
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>" + reportingStrings.addReport() + " -&nbsp;</div></h2>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");
        addWidget(thema);

        HTML infoMsg = new HTML("<h2 class='sub-title'>" + reportingStrings.selectReportFilter() + "</h2>");
        addWidget(infoMsg);

        infoImg = new Image(filterInfo);
        bannerPanel = new BannerPanel();
        bannerPanel.addWidget(infoImg);

        getReport().setFilterOptionChanged(true);
        command = () -> {
            getReport().setFilterOptionChanged(true);
            getReport().setModified(true);
        };
        filterTable = new FilterTable();
        filterTable.setCommand(command);
        if (!getReport().getColumnMap().isEmpty() && getReport().getId() == null) {
            filterTable.drawFilterTable(new LinkedList<>(getReport().getColumnMap().values()), getReport().getTableType(), getReport());
        } else if (getReport().getSelectedColumns().size() != 0 && getReport().getId() == null) {
            filterTable.drawFilterTable(getReport().getSelectedColumns(), getReport().getTableType(), getReport());
        } else {
            if (getReport().getId() != null && getReport().getColumnMap().isEmpty()) {
                CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                    public void onFailure(Throwable caught) {

                    }

                    public void onSuccess(ReportRpc report) {
                        filterTable.drawSaveFilterReports(report);
                        setReport(report);
                        refreshChanges();
                    }
                });
            } else if (!getReport().getColumnMap().isEmpty()) {
                filterTable.drawSaveFilterReports(getReport());
                refreshChanges();
            } else {
                CoreService.App.get().getSelectedColumns(getReport(), new AsyncCallback<LinkedList<ColumnRpc>>() {
                    public void onFailure(Throwable throwable) {

                    }

                    public void onSuccess(LinkedList<ColumnRpc> columnRpc) {
                        filterTable.drawFilterTable(columnRpc, getReport().getTableType(), getReport());
                        getReport().setSelectedColumns(columnRpc);
                        if (getReport().getColumnMap().isEmpty()) {
                            for (ColumnRpc rpc : columnRpc) {
                                getReport().getColumnMap().put(rpc.getName(), rpc);
                            }
                        }
                        refreshChanges();
                    }
                });
            }
        }
        filterTable.drawCustomFilterTable(getReport());

        bodyPanel = new BodyPanel();
        bodyPanel.setStyleName("filtertable");
        bodyPanel.addWdget(filterTable);
        addWidget(bodyPanel);


//             refreshChanges();


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
        goToNextStep(HistoryNamesType.ReportList.name());
    }

    public void back() {
        goToBackStep(HistoryNamesType.AddChartReport.name());
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
        if (!getReport().getColumnMap().isEmpty()) {
            filterTable.refreshFilterTable(new LinkedList<>(getReport().getColumnMap().values()), getReport().getTableType());
        } else {
            filterTable.refreshFilterTable(getReport().getSelectedColumns(), getReport().getTableType());
        }

        filterTable.drawSaveFilterReports(getReport());

    }

    public ReportRpc getReportRpc() {
        setReport(filterTable.getReport(getReport()));
        return getReport();
    }
}
