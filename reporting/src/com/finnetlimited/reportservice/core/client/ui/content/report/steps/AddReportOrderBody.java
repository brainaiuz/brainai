/*
package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.finnetlimited.reportservice.core.client.bundle.LoadingBundle;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BannerPanel;
import com.finnetlimited.reportservice.core.client.ui.table.OrderTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

*/
/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:35:33
 *//*

public class AddReportOrderBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabel";

    private HTML topMsg;
    private HTML viewLabel;
    private HTMLPanel thema;
    private Image orderInfoImg;
    private OrderTable orderTable;
    private BannerPanel help;
    private Command command;

    public AddReportOrderBody(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledSaveReportButton(false);
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
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>Add Report -&nbsp;</div></h2>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");
        addWidget(thema);
        //addWidget(new HTML("<br/>"));

        addWidget(thema);

        topMsg = new HTML("<h2 class='sub-title'>Select Report Order</h2>" +
                "<p class='help-sub-title'>Select the columns to include in your report:</p>");

        addWidget(topMsg);

        orderInfoImg = new Image(LoadingBundle.instance.orderInfo());

        help = new BannerPanel();
        help.addWidget(orderInfoImg);

        addWidget(help);

        orderTable = new OrderTable();
        if (getReport().getSelectedColumns().size() != 0) {
            for (int i = 0; i < getReport().getGroupColumns().size(); i++) {
                for (int j = 0; j < getReport().getSelectedColumns().size(); j++) {
                    if (getReport().getGroupColumns().get(i).getName().equals(getReport().getSelectedColumns().get(j).getName())) {
                        getReport().getSelectedColumns().remove(j);
                    }
                }
            }

            orderTable.addColumns(getReport().getSelectedColumns());
        } else {
            CoreService.App.get().getSelectedColumns(getReport(), new AsyncCallback<ArrayList<ColumnRpc>>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(ArrayList<ColumnRpc> columnRpc) {
                    command = new Command() {
                        public void execute() {
                            getReport().setModified(true);
                        }
                    };
                    orderTable.setCommand(command);
                    orderTable.addColumns(columnRpc);
                }
            });
        }
        addWidget(orderTable);
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
        goToNextStep(HistoryNamesType.AddOrderReport.name());
    }

    public void back() {
        goToBackStep(HistoryNamesType.AddOrderReport.name());
    }

    public void save() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addReportToDashboard() {

    }

    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {
        if (getReport().getSelectedColumns().size() != 0) {
            orderTable.removeAllColumn();
            if (getReport().getGroupColumns().size() != 0) {
                for (int i = 0; i < getReport().getGroupColumns().size(); i++) {
                    for (int j = 0; j < getReport().getSelectedColumns().size(); j++) {
                        if (getReport().getGroupColumns().get(i).getName().equals(getReport().getSelectedColumns().get(j).getName())) {
                            getReport().getSelectedColumns().remove(j);
                        }
                    }
                }
            }
            orderTable.addColumns(getReport().getSelectedColumns());
        } else {
            CoreService.App.get().getSelectedColumns(getReport(), new AsyncCallback<ArrayList<ColumnRpc>>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(ArrayList<ColumnRpc> columnRpc) {
                    orderTable.removeAllColumn();
                    orderTable.addColumns(columnRpc);
                }
            });
        }
    }

    public ReportRpc getReportingRpc() {
        ArrayList<ColumnRpc> list = orderTable.getCustomOrderColumns();
        if (getReport().getGroupColumns().size() != 0) {
            list.addAll(0, getReport().getGroupColumns());
        }
        getReport().setSelectedColumns(list);
        return getReport();
    }
}
*/
