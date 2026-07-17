package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.Tag;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 15:33:02
 */
public class AddReportTypeBody extends AbstractStepReportBody {

    private static final String typeContentId = "typeConent";
    private static final String leftContentId = "typeConentLeft";
    private static final String rightContentId = "typeConentRight";

    private static final String viewLabelId = "viewLabel";

    private HTML viewLabel;
    private Tag infoImg;
    private FlowPanel tabularImg;
    private FlowPanel summariesImg;
    private RadioButton tabular;
    private RadioButton summaries;
    private HTMLPanel thema;
//    private BannerPanel help;


    public AddReportTypeBody(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledSaveReportButton(false);
        setEnabledBackReportButton(false);
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

        infoImg = new Tag("div", "tabularInfo");
        infoImg.setSize("100%", "240px");
        tabularImg = new FlowPanel();
        tabularImg.addStyleName("tablularImg");
        summariesImg = new FlowPanel();
        summariesImg.addStyleName("summariesImg");

        tabular = new KpiRadioButton("reportType", reportingStrings.tabularReport());
        tabular.setName("reportType");
        tabular.addClickHandler(clickEvent -> {
            getReport().setTableType(ReportType.TABULAR.name());
            infoImg.setStyleName("tabularInfo");
            checkReportTypeSetColumnGrouping();
            getReport().setLimit(20);
            /*getReport().setModified(true);
            getReport().setPosition(1);
            getReport().setGroupColumns(new ArrayList<ColumnRpc>());
            getStepPanel().removeStep(HistoryNamesType.AddGroupingReport.name());
            clearStepBody(HistoryNamesType.ReportList.name());*/
        });

        summaries = new KpiRadioButton("reportType", reportingStrings.summaryReport());
        summaries.setName("reportType");
        summaries.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue() != null && booleanValueChangeEvent.getValue()) {
                getReport().setTableType(ReportType.SUMMARY.name());
                infoImg.setStyleName("summariesInfo");
                checkReportTypeSetColumnGrouping();
            }
            /*getReport().setModified(true);
            getReport().setPosition(1);
            getStepPanel().insertStep("Select<br/>Grouping", HistoryNamesType.AddGroupingReport.name(), 2);
            clearStepBody(HistoryNamesType.AddGroupingReport.name());
            clearStepBody(HistoryNamesType.ReportList.name());*/
        });


        if (getReport().getId() != null) {

            LoadingPanel.loading(true);
            final Boolean isClonable=getReport().getClonable();
            CoreService.App.get().getReport(getReport().getId(), new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ReportRpc result) {
                    LoadingPanel.loading(false);
                    setReport(result);
                    getReport().setClonable(isClonable);

                    if (ReportType.TABULAR.name().equals(getReport().getTableType()) || getReport().getTableType() == null) {
                        tabular.setValue(true);
                    } else if (ReportType.SUMMARY.name().equals(getReport().getTableType())) {
                        summaries.setValue(true, true);
                        getReport().setTableType(ReportType.SUMMARY.name());
                        infoImg.setStyleName("summariesInfo");
                    }
                }
            });
        } else {
            if (ReportType.TABULAR.name().equals(getReport().getTableType()) || getReport().getTableType() == null) {
                tabular.setValue(true);
            } else if (ReportType.SUMMARY.name().equals(getReport().getTableType())) {
                summaries.setValue(true, true);
                getReport().setTableType(ReportType.SUMMARY.name());
                infoImg.setStyleName("summariesInfo");
            }

        }

        viewLabel = new HTML(getReport().getViewName() + "&nbsp;");
        thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left; text-transform:uppercase;font-weight:bold'>" + reportingStrings.addReport() + " -&nbsp;</div></h2>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("left lineBtnSet");
        horizontalPanel.add(viewLabel);
        thema.add(horizontalPanel, viewLabelId);
        viewLabel.setStyleName("report-theme");

        addWidget(thema);

        //  addWidget(new HTML("<br/>"));

        HTML infoMsg = new HTML("<h2 class='sub-title'>" + reportingStrings.selectReportType() + "</h2>" +
                "<p class='help-sub-title'>" + reportingStrings.selectTheTypeOfReportToCreate() + ":</p>");
        addWidget(infoMsg);

//        help = new BannerPanel();
//        help.addWidget(infoImg);

//        addWidget(help);

        HTMLPanel typeConent = new HTMLPanel("<div class='choise'><ul id='typeConent'></ul></div>");
        HTMLPanel typeConentLeft = new HTMLPanel("<li id='typeConentLeft' class='left c'></li>");
        HTMLPanel typeConentRight = new HTMLPanel("<li id='typeConentRight' class='right c'></li>");

        HTML leftContentText = new HTML("<p class='little'>" + reportingStrings.tabularReportsInfo() + "</p>");
        HTML rightContentText = new HTML("<p class='little'>" + reportingStrings.summaryReportsListYourDataInformation() + "</p>");

        typeConentLeft.add(tabular, leftContentId);
        typeConentLeft.add(new HTML("<br/>"), leftContentId);
        typeConentLeft.add(tabularImg, leftContentId);
        typeConentLeft.add(leftContentText, leftContentId);

        typeConentRight.add(summaries, rightContentId);
        typeConentRight.add(new HTML("<br/>"), rightContentId);
        typeConentRight.add(summariesImg, rightContentId);
        typeConentRight.add(rightContentText, rightContentId);

        typeConent.add(typeConentLeft, typeContentId);
        typeConent.add(typeConentRight, typeContentId);

        addWidget(typeConent);
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
        if (getStepPanel().isGroupingAdd()) {
            goToNextStep(HistoryNamesType.AddGroupingReport.name());
        } else {
            goToNextStep(HistoryNamesType.AddColumnsReport.name());
        }

    }

    public void back() {
        //goToBackStep(HistoryNamesType.AddTypeReport.name());
    }

    public void save() {

    }

    public void addReportToDashboard() {

    }

    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    public void refreshChanges() {
        if (ReportType.TABULAR.name().equals(getReport().getTableType()) || getReport().getTableType() == null) {
            viewLabel.setText(getReport().getViewName());
            getReport().setTableType(ReportType.TABULAR.name());
            tabular.setValue(true);
            summaries.setValue(false);
            infoImg.setStyleName("tabularInfo");
        }
    }

    public ReportRpc getReportRpc() {
        getReport().setRunFromFirstStep(true);
        return getReport();
    }

    @Override
    public void clear() {
        super.clear();    //To change body of overridden methods use File | Settings | File Templates.
        tabular.setValue(true);
    }
}
