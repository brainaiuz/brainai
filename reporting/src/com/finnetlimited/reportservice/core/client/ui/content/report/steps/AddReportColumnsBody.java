package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BannerPanel;
import com.finnetlimited.reportservice.core.client.ui.table.SelectedColumnTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:35:13
 */
public class AddReportColumnsBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabel";

    private Image infoImg;
    private HTML viewLabel;
    private HTMLPanel thema;
    private BannerPanel help;
    private SelectedColumnTable selectColumnsTable;
    private Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public AddReportColumnsBody(String name, ReportingModuleSettings settings) {
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

    public Widget onInitialize() {
        setUpContent();
        return null;
    }

    private void setUpContent() {
//        infoImg = new Image(MessageInfoBundle.instance.msgInfo());
        viewLabel = new HTML(getReport().getViewName() + "&nbsp;");
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>" + reportingStrings.addReport() + " -&nbsp;</div></h2>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");
        addWidget(thema);
        //addWidget(new HTML("<br/>"));

        HTML infoMsg = new HTML("<h2 class='sub-title'>" + reportingStrings.selectReportColumns() + "</h2>" +
                "<p class='help-sub-title'>" + reportingStrings.selectColumnToIncludeInYourReport() + ":</p>");
        addWidget(infoMsg);

        /* help = new BannerPanel();
        //        help.addWidget(infoImg);
                addWidget(help);
        */
        final ReportRpc[] rpc = {new ReportRpc()};
        selectColumnsTable = new SelectedColumnTable();
        if (getReport().getId() != null) {
            CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(final ReportRpc reportRpc) {
                    rpc[0] = reportRpc;
                    CoreService.App.get().getTableColumns(getReport(), new AsyncCallback<ArrayList<TableRpc>>() {
                        public void onFailure(Throwable throwable) {
                        }

                        public void onSuccess(ArrayList<TableRpc> tables) {
                            command = () -> getReport().setModified(true);
                            getReport().setRunFromFirstStep(false);
                            selectColumnsTable.addReportRpc(rpc[0]);
                            selectColumnsTable.setCommand(command);
                            selectColumnsTable.addDataList(tables, getReport().getId() == null);
                            selectColumnsTable.setSelectedGrouping(getReport().getGroupColumns());
                        }
                    });
                }
            });
        } else {
            CoreService.App.get().getTableColumns(getReport(), new AsyncCallback<ArrayList<TableRpc>>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(ArrayList<TableRpc> tables) {
                    command = () -> getReport().setModified(true);
                    getReport().setRunFromFirstStep(false);

                    selectColumnsTable.setCommand(command);
                    selectColumnsTable.addDataList(tables, getReport().getId() == null);
                    selectColumnsTable.setSelectedGrouping(getReport().getGroupColumns());
                }
            });
        }

        selectColumnsTable.setCommand(command);

        addWidget(selectColumnsTable);
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
        goToNextStep(HistoryNamesType.AddSummariesReport.name());
    }

    public void back() {
        if (getStepPanel().isGroupingAdd()) {
            goToBackStep(HistoryNamesType.AddGroupingReport.name());
        } else {
            goToBackStep(HistoryNamesType.AddTypeReport.name());
        }
    }

    public void save() {

    }

    public void addReportToDashboard() {

    }

    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {
        viewLabel.setHTML(getReport().getViewName());
        selectColumnsTable.removeAllColumns();
        CoreService.App.get().getTableColumns(getReport(), new AsyncCallback<ArrayList<TableRpc>>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(ArrayList<TableRpc> tables) {
                selectColumnsTable.addReportRpc(getReport());
                selectColumnsTable.setSelectedGrouping(getReport().getGroupColumns());
                selectColumnsTable.addDataList(tables, false);
            }
        });
    }

    public ReportRpc getReportRpc() {
        getReport().setSelectedColumns(selectColumnsTable.getOrderColumns());
//        getReport().setAllColumn(selectColumnsTable.getAllColumns());
        selectColumnsTable.setColumnsMap(getReport());
        getReport().setRunFromFirstStep(false);
        return getReport();
    }
}
