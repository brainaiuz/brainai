package com.edatasite.workforce.gwt.reportingsystem.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.BudgetRunReportPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.RunReportPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Virus on 8/27/14.
 */
public class ReportingStepControlView extends View implements Colapse, FittedContent {

    private ReportRpc report;
    private final String type;
    private String specificReportName;
    private final Integer id;
    private String uuid;
    private Integer budgetId;
    private Command includeGroupingCommand;
    private Command includeFilterCommand;
    private RunReportPanel runReportPanel;
    private BudgetRunReportPanel budgetRunReportPanel;
    public Integer stepId = 1;


    public ReportingStepControlView(String type, Integer id, String name, String specificReportName) {
        super("stepControl", name);
        this.type = type;
        this.id = id;
        this.specificReportName = specificReportName;
    }

    public ReportingStepControlView(String type, String uuid, String name) {
        super("stepControl", name);
        this.type = type;
        this.uuid = uuid;
        this.id = null;
    }
    public ReportingStepControlView(String type, Integer id, String name, Integer budgetId) {
        super("stepControl", name);
        this.type = type;
        this.id = id;
        this.budgetId = budgetId;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        if (budgetId != null) {
            budgetRunReportPanel = new BudgetRunReportPanel();
            budgetRunReportPanel.setView(this);
            add(budgetRunReportPanel);
        } else {
            runReportPanel = new RunReportPanel();
            runReportPanel.setView(this);
            add(runReportPanel);
        }

        loading();
        return null;
    }

    private static native String generateDate(String year, String month) /*-{
        var y = parseInt(year);
        var m = parseInt(month);

        var d = new Date(y, m, 0);

        var ny = d.getFullYear();
        var nm = d.getMonth();
        var nd = d.getDate();

        if (m < 10) {
            m = "0" + m;
        }
        var begin = "01." + m + "." + y;

        nm = nm + 1;
        if (nm < 10) {
            nm = "0" + nm;
        }
        var end = nd + "." + nm + "." + ny;
        return begin + "_" + end;
    }-*/;

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public void selectColumnsLoading(AsyncCallback<ArrayList<TableRpc>> callback) {
        ReportingService.App.get().getTableColumns(getReport(), callback);
    }

    public ReportRpc getReport() {
        return report;
    }

    public void closeTab() {
        SinksContainer defaultContainer = SinksContainerFactory.entryPoint.getContainerFactory().getSinksContainer(Constants.REPORTING_CATEGORY);
        if (defaultContainer != null) {
            CategoryReportListView homeView = ((CategoryReportListView) defaultContainer.getViewByName().get(Constants.REPORTING_HOME));
            homeView.clear();
            homeView.onReadyToInitialize();
        }
        super.closeTab();
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public void setIncludeGroupingCommand(Command command) {
        this.includeGroupingCommand = command;
    }

    public void setIncludeFiltersCommand(Command filterChanges) {
        this.includeFilterCommand = filterChanges;
    }

    public void includeGroupingChanges() {
        if (includeGroupingCommand != null) {
            includeGroupingCommand.execute();
        } else {
            if (!getReport().getGroupColumns().isEmpty()) {
                for (ColumnRpc columnRpc : getReport().getGroupColumns()) {
                    getReport().getSelectedColumns().remove(columnRpc);
                }
                for (int i = 0; i < getReport().getGroupColumns().size(); i++) {
                    getReport().getSelectedColumns().add(i, getReport().getGroupColumns().get(i));
                }
            }
            getReport().setNowPosition(1);
        }
    }

    public void includeFilters() {
        if (includeFilterCommand != null) {
            includeFilterCommand.execute();
        }
    }

    public void runReport() {

        if (budgetId != null) {
            budgetRunReportPanel.runReport();
        } else {
            runReportPanel.runReport();
        }
    }

    public void updateReport() {

        if (budgetId != null) {
            budgetRunReportPanel.updateReport();
        } else {
            runReportPanel.updateReport();
        }
    }

    public void updateReportTable() {

        if (budgetId != null) {
            budgetRunReportPanel.updateReportTable();
        } else {
            runReportPanel.updateReportTable();
        }
    }

    public void runReport(final AbstractAsyncCallback<String> callback) {
        Utils.scrollIntoView(DOM.getElementById("inv_form_header"));
        LoadingPanel.loading(true);

        ReportingService.App.get().generateReport(getReport(), new AbstractAsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
                LoadingPanel.loading(false);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }
        });
    }

    public void close() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo
                , wfmStrings.areYouSureWantoLeaveThisReport()
                , new CloseHandler() {
            @Override
            public void onSubmit() {
                closeTab();
            }
        });
        messageBox.open();
    }

    private void goToSteep(Integer id) {
        stepId = id;
        //stepControl.goToStep(id);
    }

    public void next() {
        goToSteep(++stepId);
    }

    public void previous() {
        //stepControl.goToStep(--stepId, true);
    }

    public void getStructure(final AbstractAsyncCallback<ReportRpc> callback) {
        ReportingService.App.get().getReportStructure(getReport(), null, new AbstractAsyncCallback<ReportRpc>() {
            @Override
            public void onSuccess(ReportRpc result) {
                setReport(result);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }
        });
    }

    public void getQueryTotalResult(final AbstractAsyncCallback<ReportRpc> callback) {
        ReportingService.App.get().getQueryTotalResult(getReport(), null, new AbstractAsyncCallback<ReportRpc>() {
            @Override
            public void onSuccess(ReportRpc result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }
        });
    }

    public void showNeedCorrectView(String tabCreateChart) {

        if (budgetId != null) {
            budgetRunReportPanel.showWidgetByTabCode(tabCreateChart);
        } else {
            runReportPanel.showWidgetByTabCode(tabCreateChart);
        }
    }

    private String checkForLessTen(int date) {
        if (date < 10) {
            return "0";
        }
        return "";
    }

    private void loading() {
        if ("savedreport".equals(type)) {
            ReportingService.App.get().getReport(id, new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ReportRpc result) {
                    if (specificReportName != null) {
                        ColumnRpc newColumnRpc = null;
                        if (!result.getSelectedColumns().isEmpty()) {
                            for (ColumnRpc columnRpc : result.getSelectedColumns()) {
                                if (columnRpc.getName().equals(result.getChartConf().getxAxis().getColumn())) {
                                    newColumnRpc = columnRpc;
                                }
                            }
                        } else {
                            for (ColumnRpc columnRpc : result.getGroupColumns()) {
                                if (columnRpc.getName().equals(result.getChartConf().getxAxis().getColumn())) {
                                    newColumnRpc = columnRpc;
                                }
                            }
                        }
                        result.getFieldd().add(newColumnRpc);
                        result.getSett().add(0);
                        if (newColumnRpc.getType().equals("date")) {
                            String[] arr = specificReportName.split("-");
                            String value = "";
                            if (arr.length == 1) {
                                int year = Integer.parseInt(arr[0]);
                                String yearBegin = "01.01." + year;
                                String yearEnd = "01.01." + (year + 1);
                                value = yearBegin + "_" + yearEnd;
                                result.getOperators().add("Between");
                            } else if (arr.length == 2) {
                                value = generateDate(arr[0], arr[1]);
                                result.getOperators().add("Between");
                            } else if (arr.length == 3) {
                                int year = Integer.parseInt(arr[0]);
                                int month = Integer.parseInt(arr[1]);
                                int date = Integer.parseInt(arr[2]);
                                value = checkForLessTen(date) + date + "." + checkForLessTen(month) + month + "." + year;
                                result.getOperators().add("Equal");
                            }
                            result.getValues().add(value);
                        } else {
                            result.getOperators().add("IsEqualTo");
                            result.getValues().add(specificReportName.toLowerCase());
                        }
                        String filterPattern = result.getFilterPattern() + "  and " + result.getValues().size();
                        result.setFilterPattern(filterPattern);
                        result.setModified(true);
                        result.setClonable(false);
                    }
                    setReport(result);
                    stepId = 7;
                    if (budgetId != null) {
                        budgetRunReportPanel.init();
                    } else {
                        runReportPanel.init();
                    }
                }
            });
        } else if ("aireport".equals(type)) {
            ReportingService.App.get().getReport(uuid, new AbstractAsyncCallback<ReportRpc>() {
                @Override
                public void onSuccess(ReportRpc result) {
                    setReport(result);
                    stepId = 7;
                    runReportPanel.init();
                }
            });
        } else {
            ReportingService.App.get().getReportStructure(id, new AbstractAsyncCallback<ReportRpc>() {
                @Override
                public void onSuccess(ReportRpc result) {
                    setReport(result);
                    stepId = 7;
                    runReportPanel.init();
                }
            });
        }
    }

}
