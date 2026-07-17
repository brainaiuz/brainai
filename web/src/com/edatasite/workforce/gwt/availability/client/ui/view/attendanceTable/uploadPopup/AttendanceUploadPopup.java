
package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.uploadPopup;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import gwt.material.design.client.constants.TextAlign;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 26.09.2009
 * Time: 13:01:41
 */

public class AttendanceUploadPopup implements CommandConstants {

    private static final DateTimeFormat format = DateTimeFormat.getFormat("MMMM");

    private KpiModal downloadPopup;
    private RadioButton lastYear;
    private DataListBox monthList;
    private WfmButton2 cancel;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final String teamIds;
    private final Integer locationID;

    private Date date;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final Integer projectID;

    private Command submitSuccessfullyCompleted;

    public void setSubmitCompleted(Command submitSuccessfullyCompleted) {
        this.submitSuccessfullyCompleted = submitSuccessfullyCompleted;
    }

    public AttendanceUploadPopup(String teamIds, Integer locationID, Integer projectID) {
        this.teamIds = teamIds;
        this.locationID = locationID;
        this.projectID = projectID;
        init();
    }

    private void init() {
        downloadPopup = new KpiModal();
        downloadPopup.setTitle(hrmsStrings.downloadXLSTemplateFile());
        downloadPopup.setWidth("450px");
        downloadPopup.open();

        final VerticalPanelDiv cont = new VerticalPanelDiv();
        final HTML message = new HTML("<font color='#19468A'>" + hrmsStrings.htmlText11() + "</font>");
        final HTML note = new HTML("<font color='red'>" + hrmsStrings.htmlText12() + "</font>");

        lastYear = new KpiRadioButton("year", "&nbsp;&nbsp;" + wfmStrings.lastYear(), true);
        RadioButton currentYear = new KpiRadioButton("year", "&nbsp;&nbsp;" + hrmsStrings.currentYear(), true);
        currentYear.setValue(true);

        final VerticalPanelDiv radioButtonsPanel = new VerticalPanelDiv();
        radioButtonsPanel.add(8, currentYear, lastYear);

        monthList = new DataListBox();
        monthList.setWidth("140px");
        monthList.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        monthList.setWithoutNullLabel(true);
        monthList.setItems(getMonthNameItem());
        monthList.setSelected(new Date().getMonth() + 1);

        final HorizontalPanelDiv radioListBoxPanel = new HorizontalPanelDiv(true);
        radioListBoxPanel.add(radioButtonsPanel, com.google.gwt.dom.client.Style.Float.LEFT);
        radioListBoxPanel.add(monthList, com.google.gwt.dom.client.Style.Float.RIGHT);

        WfmButton2 download = new WfmButton2(wfmStrings.download(), WfmButton2.BTN_PRIMARY);
        download.addClickHandler(event -> {
            downloadPopup.close();
            downloadXslTemplate();
        });
        cancel = new WfmButton2(wfmStrings.skip(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> drawUploadPupup());
        cont.add(20, message, note, radioListBoxPanel/*, buttons*/);

        downloadPopup.add(cont);
        downloadPopup.addButton(cancel);
        downloadPopup.addButton(download);
    }

    private void downloadXslTemplate() {
        int year = date.getYear();
        int month = monthList.getSelectedItem().getId();
        if (lastYear.getValue()) {
            year--;
        }
        String action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/attendanceReportImportExcelFile";
        action += "?year=" + year + "&month=" + month + "&teamIds=" + (teamIds != null ? teamIds : "null") + "&locationID=" + (locationID != null ? locationID : "null") + "&projectID=" + (projectID != null ? projectID : "null");
        Window.open(action, "_blank", "");
    }

    private void drawUploadPupup() {

        final HTML note = new HTML("<font color='#19468A'>" + hrmsStrings.htmlText12() + "</font>");
        final HTML label = new HTML("<font size='2.5'>" + wfmStrings.messSelectFile() + "</forn>");

        final FileUpload upload = new FileUpload();
        upload.setName(ATTACHMENT_PARAM_BASE + 0);

        final WfmFormPanel uploadLabel = new WfmFormPanel("/uploadAttendanceXslFile");
        uploadLabel.addFormHandler(new FormHandler() {

            public void onSubmit(FormSubmitEvent event) {
                downloadPopup.setTitleWithLoader(wfmStrings.importingData(),true);
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                downloadPopup.setTitleWithLoader(wfmStrings.info(),true);
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                messageBox.setTextAlign(TextAlign.CENTER);
                messageBox.setTitleWithLoader(wfmStrings.info(),false);
                messageBox.setMessage(uploadLabel.getErrorString() != null ? uploadLabel.getErrorString() : hrmsStrings.message11part2());
                if (uploadLabel.getErrorString() == null) {
                    if (submitSuccessfullyCompleted != null) {
                        submitSuccessfullyCompleted.execute();
                    }
                    downloadPopup.close();
                }
                messageBox.open();
                if (uploadLabel.getErrorString() == null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_IMPORT_FILE_FOR_ATTENDANCE_TRACKING_REPORT, upload.getFilename(), messageBox);
                }
            }
        });

        uploadLabel.setWidget(upload);

        WfmButton2 imp = new WfmButton2(hrmsStrings.getPropertyImport(), WfmButton2.BTN_PRIMARY);
        imp.addClickHandler(event -> {
            if (upload.getFilename() != null && !"".equals(upload.getFilename())) {
                if (upload.getFilename().lastIndexOf(".xls") != -1) {
                    uploadLabel.submit();
                    LoadingPanel.loading(true);
                } else {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                    //messageBox.setSize(300, 120);
                    messageBox.setTitle(wfmStrings.info());
                    messageBox.setMessage(hrmsStrings.message12part2());
                    messageBox.open();
                }
            }
        });

        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> downloadPopup.close());

        final VerticalPanelDiv cont = new VerticalPanelDiv();
        cont.add(16, note, label, uploadLabel/*, buttons*/);

        downloadPopup.getContent().clear();
        downloadPopup.getFooter().clear();

        downloadPopup.setTitle(hrmsStrings.uploadXLSTemplateFile());
        downloadPopup.add(cont);
        downloadPopup.addButton(cancel);
        downloadPopup.addButton(imp);



    }

    private SelectItem[] getMonthNameItem() {
        SelectItem[] items = new SelectItem[12];
        date = new Date();
        for (int i = 0; i < 12; i++) {
            date.setMonth(i);
            items[i] = new SelectItem(i + 1, format.format(date));
        }
        return items;
    }
}

