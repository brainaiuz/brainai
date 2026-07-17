package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetItem;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.RunReportPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.html.OptGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Virus on 9/29/14.
 */
public class SaveReportPopup extends KpiModal {
    private final ReportingStepControlView view;

    interface SaveReportPopupUiBinder extends UiBinder<HTMLPanel, SaveReportPopup> {
    }

    private static final SaveReportPopupUiBinder ourUiBinder = GWT.create(SaveReportPopupUiBinder.class);
    @UiField
    TextBox name;
    @UiField
    TextArea desctiption;
    @UiField
    MaterialComboBox<FolderRpc> folder;
    @UiField
    HTML error;
    @UiField
    LabelElement nameLabel, descLabel, foldersLabel;

    private final WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

    public SaveReportPopup(final ReportingStepControlView view, final boolean clone) {
        super();
        setCloseButton(true);
        setWidth("400px");
        addStyleName("bt_area");
        add(ourUiBinder.createAndBindUi(this));
        nameLabel.setInnerHTML(wfmStrings.name());
        descLabel.setInnerHTML(wfmStrings.description());
        foldersLabel.setInnerHTML(wfmStrings.folders());
        desctiption.setHeight("70px");
        folder.setStylePrimaryName("wfm-dropdown");
        folder.setWidth("100%");
        this.view = view;
        addButton(saveButton);
        ReportingService.App.get().getFolders(null, new AbstractAsyncCallback<FolderRpc[]>() {
            @Override
            public void onSuccess(FolderRpc[] result) {
                HashMap<Integer, ArrayList<FolderRpc>> cfol = new HashMap<>();
                for (FolderRpc rpc : result) {
                    if (!cfol.containsKey(rpc.getCategoryId())) {
                        ArrayList<FolderRpc> folders = new ArrayList<>();
                        cfol.put(rpc.getCategoryId(), folders);
                    }
                    cfol.get(rpc.getCategoryId()).add(rpc);
                }

                for (Integer key : cfol.keySet()) {
                    String categoryName = cfol.get(key).get(0).getCategoryName();
                    String groupname = categoryName != null ? categoryName : wfmStrings.sysTemFolder();
                    OptGroup group = new OptGroup(groupname);
                    for (FolderRpc folderRpc : cfol.get(key)) {
                        folder.addItem(folderRpc.getName(), folderRpc, group);
                    }
                    folder.addGroup(group);
                }
//                if (clone)
//                    loadingData();
            }
        });
        saveButton.addClickHandler(event -> {
            if (validate()) {
                saveButton.setEnabled(false);
                ReportRpc report = SaveReportPopup.this.view.getReport();
                report.setName(name.getValue());
                report.setDiscreption(desctiption.getValue());
                if (folder.getValue() != null) {
                    report.setFolderId(folder.getSingleValue() != null ? folder.getSingleValue().getId() : null);
                }
//                if (clone) {
//                    report.setClonable(true);
//                    report.setId(null);
//                    report.setCode(null);
//                }

                if (!validateCharts(view)) {
                    Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                    view.showNeedCorrectView(RunReportPanel.TAB_CREATE_CHART);
                    close();
                    return;
                }
                if (!validateWidgets(view)) {
                    Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                    view.showNeedCorrectView(RunReportPanel.TAB_CREATE_WIDGET);
                    close();
                    return;
                }

                ReportingService.App.get().saveReport(report, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        saveButton.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result != 0) {
                            view.getReport().setId(result);
                            view.getReport().setName(report.getName());
                            view.getReport().setDiscreption(report.getDiscreption());
                            view.getReport().setFolderName(report.getFolderName());

                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REPORTING_REPOT_SAVED, result, SaveReportPopup.this);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), report.getName()));
                            close();
                        } else {
                            error.setHTML("<b style='color:green'>" + wfmStrings.reportNameAlreadyExist() + "</b>");
                        }
                        saveButton.setEnabled(true);
                    }
                });
            }
        });
    }

    private boolean validateCharts(ReportingStepControlView view) {
        boolean hasChart = (view.getReport().getChartConf() != null && view.getReport().getChartConf().getModules() != null && view.getReport().getChartConf().getModules().size() > 0);
        int errors = 0;
        if (hasChart) {
            ChartConfItem chartItem = view.getReport().getChartConf();
            if (chartItem.getTitle() == null || chartItem.getTitle().isEmpty()) errors++;
            if (chartItem.getxAxis() == null) errors++;
            if (!ChartTypeEnum.GAUGE_CHART.equals(view.getReport().getChartConf().getType())) {
                if (chartItem.getSeries() == null || chartItem.getSeries().size() == 0) {
                    errors++;
                } else {
                    for (SerieConfItem serieConfItem : chartItem.getSeries()) {
                        if (serieConfItem == null || serieConfItem.getSerieColumn() == null || serieConfItem.getAggrType() == null || serieConfItem.getSerieName() == null) {
                            errors++;
                            break;
                        }
                    }
                }
            }

        }
        return errors == 0;
    }

    private boolean validateWidgets(ReportingStepControlView view) {
        boolean hasWidget = (view.getReport().getKpiWidgetItem() != null && view.getReport().getKpiWidgetItem().getModules() != null && !view.getReport().getKpiWidgetItem().getModules().isEmpty());
        int errors = 0;
        if (hasWidget) {
            KpiWidgetItem widgetItem = view.getReport().getKpiWidgetItem();
            if (widgetItem.getKpiWidgetTitle() == null || widgetItem.getKpiWidgetTitle().isEmpty()) errors++;
            if (ChartTypeEnum.RANKING_KPI.equals(widgetItem.getType()) && widgetItem.getGroupingColumn() == null)
                errors++;
            if ((ChartTypeEnum.STANDARD_KPI.equals(widgetItem.getType()) || ChartTypeEnum.GROWTH_KPI.equals(widgetItem.getType()))
                    && (widgetItem.getComparisionText() == null || widgetItem.getComparisionText().isEmpty())) errors++;
            if (!ChartTypeEnum.RANKING_KPI.equals(widgetItem.getType())) {
                if (widgetItem.getKpiWidgetMetric() == null || widgetItem.getKpiWidgetMetric().getSerieColumn() == null
                        || widgetItem.getKpiWidgetMetric().getAggrType() == null || widgetItem.getKpiWidgetMetric().getSerieName() == null)
                    errors++;
            }
        }
        return errors == 0;
    }

    public void loadingData() {
//        if (view.getReport().getId() == null) return;
        ReportRpc report = view.getReport();
        if (report.getName() != null) {
            name.setValue(report.getName());
        }
        if (report.getDiscreption() != null) {
            desctiption.setText(report.getDiscreption());
        }
        if (report.getFolderId() != null) {
            List<FolderRpc> folders = folder.getValues().stream().filter(o -> o.getId().equals(report.getFolderId())).collect(Collectors.toList());
            if (folders.size() > 0) {
                folder.setSingleValue(folders.get(0));
            }
        }
    }

    public boolean validate() {
        int errorCount = 0;
        error.setHTML("");

        if (name.getText() == null || "".equals(name.getText().trim())) {
            errorCount++;
        }
        if (folder.getValue() == null || folder.getSingleValue() == null) {
            errorCount++;
        }
        if (errorCount > 0) {
            error.setHTML("<b style='color:red;'>" + wfmStrings.fillRequiredField() + "</b>");
            return false;
        }
        return true;
    }

    public MaterialComboBox<FolderRpc> getFolder(){
        return folder;
    }
}
