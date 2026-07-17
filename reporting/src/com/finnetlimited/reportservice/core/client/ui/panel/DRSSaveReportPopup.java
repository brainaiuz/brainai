package com.finnetlimited.reportservice.core.client.ui.panel;


import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.finnetlimited.reportservice.core.client.ui.ReportingBaseEntryPoint;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.refresh.DRSRefresh;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: ${Dilsh0d}
 * Date: 29-Mar-2010
 * Time: 17:31:34
 */
public class DRSSaveReportPopup extends KpiModal implements Constants {

    private static final CoreServiceAsync coreService = CoreService.App.get();

    private HTML error;
    private TextBox name;
    private TextArea description;

    private DRSListBox folderList;
    private DRSButton saveReport;

    private DRSButton close;
    private ReportRpc report;
    private Command command;
    private Command changeModifyStatus;

    private Integer savedReportId;
    private FlowPanel popupBody;
    private HorizontalPanel topPanel;


    private String xml;

    private Integer reportID = null;

    public DRSSaveReportPopup(String title, String xml) {
        this.xml = xml;
        setTitle(title);
        setSize("450px", "200px");
//        setText("Save Custom Report ");
        init();
    }

    private void init() {
        error = new HTML("");
        name = new TextBox();
        name.setWidth("392px");
        folderList = new DRSListBox();
        folderList.setWidth("397px");
        coreService.getFolderList(new AsyncCallback<ArrayList<SelectListRpc>>() {
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<SelectListRpc> folderRpcs) {
                ArrayList<SelectListRpc> selectListRpcs = new ArrayList<>();
                selectListRpcs.addAll(folderRpcs);
                folderList.addItems(selectListRpcs);
            }
        });
        description = new TextArea();
        description.setSize("392px", "50px");


        saveReport = new DRSButton("Save Report", DRSButton.BUTTON_STYLE);
        close = new DRSButton("Close", DRSButton.BUTTON_STYLE);

        saveReport.addClickHandler(clickEvent -> {
            if (validate()) {
                save();
            }
        });

        close.addClickHandler(clickEvent -> close());

        HorizontalPanel savePanel = new HorizontalPanel();
        savePanel.add(saveReport);
        savePanel.setCellVerticalAlignment(saveReport, VerticalPanel.ALIGN_MIDDLE);
        savePanel.add(close);
        savePanel.setCellVerticalAlignment(close, VerticalPanel.ALIGN_MIDDLE);


        popupBody = new FlowPanel();
        topPanel = new HorizontalPanel();
        popupBody.setStyleName("order-table");


        HorizontalPanel naughtRow = new HorizontalPanel();

        HorizontalPanel firstRow = new HorizontalPanel();
        HorizontalPanel secondRow = new HorizontalPanel();
        HorizontalPanel thirdRow = new HorizontalPanel();


        naughtRow.setStyleName("naught-style-name");
        firstRow.setStyleName("first-style-name");
        secondRow.setStyleName("second-style-name");
        thirdRow.setStyleName("third-style-name");
        VerticalPanel verticalPanel = new VerticalPanel();
        if (!error.equals("")) {
            naughtRow.add(error);
        }


        firstRow.add(new HTML("<b style='color:#015d9f;'>Name<span style='color:red;'>*</span>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>"));
        firstRow.setCellHorizontalAlignment(name, HasHorizontalAlignment.ALIGN_RIGHT);
        firstRow.add(name);
        secondRow.add(new HTML("<b style='color:#015d9f;'>Folder<span style='color:red;'>*</span>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>"));
        secondRow.setCellHorizontalAlignment(folderList, HasHorizontalAlignment.ALIGN_RIGHT);
        secondRow.add(folderList);
        thirdRow.add(new HTML("<b style='color:#015d9f;'>Description:</b>"));
        thirdRow.setCellHorizontalAlignment(description, HasHorizontalAlignment.ALIGN_RIGHT);
        thirdRow.add(description);

        verticalPanel.setStyleName("left-panel");
        verticalPanel.add(naughtRow);
        verticalPanel.add(firstRow);
        verticalPanel.add(secondRow);
        verticalPanel.add(thirdRow);

        topPanel.add(verticalPanel);
        topPanel.setStyleName("top-panel-style");
        verticalPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        verticalPanel.add(savePanel);
        popupBody.add(topPanel);

        add(popupBody);
    }

    private void save() {
        ReportRpc reportTemp = saveEvent.saveReportEvent();
        if (reportTemp != null) {
            report = reportTemp;
        }
        report.setSelectedColumns(getReport().getSelectedColumns());
        report.setName(name.getText());
        report.setFolderId(folderList.getSelectId());
        report.setDiscreption(description.getText());

        saveReport.setEnabled(false);
        close.setEnabled(false);
        coreService.saveReport(report, new AsyncCallback<Integer>() {
            public void onFailure(Throwable throwable) {
                saveReport.setEnabled(true);
                close.setEnabled(true);
                close();
            }

            public void onSuccess(Integer result) {
                if (result != 0) {
                    DRSRefresh.registrationRefreshPages(HistoryNamesType.ReportList);
                    saveReport.setEnabled(true);
                    ReportingBaseEntryPoint.loadPermission();
                    report.setOwner(true);
                    close.setEnabled(true);
                    if (command != null) {
                        command.execute();
                    }
                    if (changeModifyStatus != null) {
                        changeModifyStatus.execute();
                    }
                    savedReportId = result;
                    close();

                } else {
                    error.setHTML("<b style='color:green'>" + wfmStrings.reportNameAlreadyExist() + "</b>");
                    saveReport.setEnabled(true);
                    close.setEnabled(true);
                }

            }
        });
    }

    public boolean validate() {
        int errorCount = 0;
        error.setHTML("");

        if (name.getText() == null || "".equals(name.getText().trim())) {
            errorCount++;
        }
        if (folderList.getSelectedIndex() == 0) {
            errorCount++;
        }
        if (errorCount > 0) {
            error.setHTML("<b style='color:red;'>" + wfmStrings.fillRequiredField() + "</b>");
            return false;
        }
        return true;
    }

    public interface SaveReportEvent {
        ReportRpc saveReportEvent();
    }

    private SaveReportEvent saveEvent;

    public void addSaveEvent(SaveReportEvent saveEvent) {
        this.saveEvent = saveEvent;
    }

    private void clearAll() {
        error.setHTML("&nbsp;");
        name.setText("");
        folderList.setSelectedIndex(0);
        description.setText("");
    }

    public void showPopup(Integer id) {
        reportID = id;

        clearAll();
        center();
        if (id != null) {
            DRSLoadingPanel.show();
            coreService.getFolderByReportId(id, new AsyncCallback<FolderRpc>() {
                public void onFailure(Throwable caught) {
                    DRSLoadingPanel.hide();
                }

                public void onSuccess(FolderRpc result) {
                    DRSLoadingPanel.hide();
                    open();
                    name.setText(result.getReports().get(0).getName());
                    folderList.setSelectedName(result.getName());
                    description.setText(result.getReports().get(0).getDescription());

                }
            });
        } else {
            open();
        }
    }

    public ReportRpc getReport() {
        return report;
    }

    public void setReport(ReportRpc report) {
        this.report = report;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getChangeModifyStatus() {
        return changeModifyStatus;
    }

    public void setChangeModifyStatus(Command changeModifyStatus) {
        this.changeModifyStatus = changeModifyStatus;
    }

    public Integer getSavedReportId() {
        return savedReportId;
    }


}
