/*
package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.finnetlimited.reportservice.core.client.bundle.LoadingBundle;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportDirectoryPathRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSMultiListBox;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.BannerPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.MessagePanel1;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

*/
/**
 * User: ${Dilsh0d}
 * Date: 13-Mar-2010
 * Time: 19:36:47
 *//*

public class AddReportDataTypeBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabel";

    private HTMLPanel thema;
    private HTML viewLabel;
    private Image infoImg;
    private BannerPanel helpPanel;
    private MessagePanel1 messagePanel;
    private DRSMultiListBox reportLisBox;
    private ReportRpc rpc;

    public AddReportDataTypeBody(String name, ReportingModuleSettings settings) {
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
        setUpContent();
        return null;
    }

    private void setUpContent() {
        viewLabel = new HTML(getReport().getViewName() + "&nbsp;");
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>Add Report -&nbsp;</div></h2>");

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");
        addWidget(thema);

        // addWidget(new HTML("<br/>"));

        HTML infoText = new HTML("<h2 class='sub-title'>Select Data Type</h2>" +
                "<p class='help-sub-title'>Select the type of data you wish to report on.</p>");

        addWidget(infoText);

        reportLisBox = new DRSMultiListBox();
        reportLisBox.addChangeEvent(new DRSMultiListBox.ChangeEvent() {
            public void changeEvent(SelectItem reportTemplate) {
                changeReportCategory(reportTemplate);
            }
        });

        addWidget(reportLisBox);
        if (getReport().getId() != null) {
            CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(final ReportRpc reportRpc) {
                    rpc = reportRpc;
                    CoreService.App.get().getReportList(new AsyncCallback<ArrayList<ReportDirectoryPathRpc>>() {
                        public void onFailure(Throwable throwable) {

                        }

                        public void onSuccess(ArrayList<ReportDirectoryPathRpc> selectListRpc) {
                            reportLisBox.addList(selectListRpc);
                            reportLisBox.setSelectedByName(new SelectItem(rpc.getXmlTemplateId(), rpc.getViewName()));
                            viewLabel.setText(reportLisBox.getSelectedTemplate().getName());
                            reportRpc.setXmlTemplateId(reportRpc.getXmlTemplateId());
                            reportRpc.setViewCode(reportRpc.getViewCode());
                            inspectReportId();
                        }
                    });
                }
            });
        } else {
            CoreService.App.get().getReportList(new AsyncCallback<ArrayList<ReportDirectoryPathRpc>>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(ArrayList<ReportDirectoryPathRpc> selectListRpc) {
                    reportLisBox.addList(selectListRpc);
                    viewLabel.setText(reportLisBox.getSelectedTemplate().getName());
                    getReport().setXmlTemplateId(reportLisBox.getSelectedTemplate().getId());
                    getReport().setViewCode(reportLisBox.getSelectedTemplate().getDescription());
                    inspectReportId();
                }
            });
        }
        infoImg = new Image(LoadingBundle.instance.msgInfo());
        helpPanel = new BannerPanel();
        helpPanel.addWidget(infoImg);
        addWidget(helpPanel);

        messagePanel = new MessagePanel1();
//        messagePanel.addWidget(new HTML("<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>" +
//                "<p>Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>"));
        messagePanel.addWidget(new HTML("<p>Dynamic Reporting System allows users easily create, save and print their reports using the data in Workforcetrack.</p>" +
                "<p>Also users can upload their own reports and modify them into different types of reports within the system.</p>" +
                "<p>To run the reports users have to select data types from the list available and they can set other options in the next steps.</p>"));

        addWidget(messagePanel);
    }

    public void changeReportCategory(SelectItem reportTemplate) {
        getReport().setModified(true);
        viewLabel.setText(reportTemplate.getName());
        getStepPanel().removeStep(HistoryNamesType.AddGroupingReport.name());
        clearStepBody(HistoryNamesType.AddFilterReport.name());
        clearStepBody(HistoryNamesType.AddTypeReport.name());
    }

    private void inspectReportId() {
        if (getReport().getId() != null) {
            CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(ReportRpc report) {
                    reportLisBox.setSelectedByName(new SelectItem(rpc.getXmlTemplateId(), report.getViewName(), report.getViewCode()));
                    viewLabel.setText(report.getViewName());
                    setReport(report);
                    checkReportTypeSetColumnGrouping();
                }
            });
        } else if (getReport().getViewName() != null) {
            reportLisBox.setSelectedByName(new SelectItem(rpc.getXmlTemplateId(), getReport().getViewName(), getReport().getViewCode()));
            viewLabel.setText(getReport().getViewName());
        }
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
        goToNextStep(HistoryNamesType.AddDataTypeReport.name());
    }

    public void back() {
        //goToOwnStepDown(HistoryNamesType.FolderList.name());
    }

    public void save() {

    }

    public void addReportToDashboard() {

    }

    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {

    }

    public ReportRpc getReportingRpc() {
        getReport().getSelectedColumns();
        if (getReport().getViewName() != null && !getReport().getViewName().equals(reportLisBox.getSelectedTemplate().getName())) {
            setReport(new ReportRpc());
        }
        getReport().setViewName(reportLisBox.getSelectedTemplate().getName());
        getReport().setRunFromFirstStep(true);
        getReport().setXmlTemplateId(reportLisBox.getSelectedTemplate().getId());
        getReport().setViewCode(reportLisBox.getSelectedTemplate().getDescription());
        if (getReport().getId() == null) {
            getReport().setModified(true);
        }
        return getReport();
    }
}
*/
