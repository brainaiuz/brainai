/*
package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;

import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmDialogBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;


*/
/**
 * Created by IntelliJ IDEA.
 * User: sasna
 * Date: 17.04.2009
 * Time: 19:37:44
 *//*

public class ImportPopup extends Anchor implements CommandConstants {

    private KpiModal popup;
    private Button imp;
    private Button cancel;
    private String url = "";

    public ImportPopup(int styleName) {
        this(styleName, "/uploadChartOfAccounts");
    }

    public ImportPopup(int styleName, String url) {
       // super(styleName);
        this.url = url;
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                init();
            }
        });
    }

    private Command submitSuccessfullyCompleted;

    public void setSubmitCompleted(Command submitSuccessfullyCompleted) {
        this.submitSuccessfullyCompleted = submitSuccessfullyCompleted;
    }

    public void init() {
        popup = new KpiModal();
        popup.setText(coreStrings.importChartAccounts());
        popup.setSize(350, 170);
        popup.open();

        HTML label = new HTML("<font size='2.5'><b>" + coreStrings.selectFileToImport() + "<b></forn>");
        label.setWidth("250px");
        final FileUpload upload = new FileUpload();
        upload.setWidth("250px");
        upload.setName(ATTACHMENT_PARAM_BASE + 0);

        final WfmFormPanel uploadPanel = new WfmFormPanel(url);
        uploadPanel.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                if (uploadPanel.getErrorString() == null) {
                    if (submitSuccessfullyCompleted != null) {
                        submitSuccessfullyCompleted.execute();
                    }
                    popup.close();
                }
                 Info.show("", uploadPanel.getErrorString() != null ? coreStrings.parseError() : coreStrings.uploaded(), Info.Type.INFO);
            }
        });
        uploadPanel.setWidget(upload);

        imp = new Button(coreStrings.getPropertyImport(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (upload.getFilename() != null && upload.getFilename() != "") {
                    uploadPanel.setParameter(DESCRIPTION_PARAM_NAME, "Chart Of Accounts");
                    uploadPanel.setParameter(UPLOAD_TYPE_PARAM_NAME, "CHART_OF_ACCOUNTS");
                    uploadPanel.submit();
                    LoadingPanel.loading(true);
                }
            }
        });

        cancel = new Button(coreStrings.cancel(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.close();
            }
        });
        final VerticalPanelDiv cont = new VerticalPanelDiv();
        final HorizontalPanelDiv buttonsPanel = new HorizontalPanelDiv();

        buttonsPanel.add(7, imp, cancel);
        HTML html = new HTML("<a target='_blank' href='/docs/sample-chartOfAccounts.csv'>" + coreStrings.downloadSample() + "</a>");
        cont.add(10, label, uploadPanel, html, buttonsPanel);

        popup.add(cont);

    }
}
*/
