package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 17.09.12
 * Time: 21:00
 * To change this template use File | Settings | File Templates.
 */

public class ImportTestResultsXMLFileView extends View implements Constants,CommandConstants {

	private static final TCStrings tcStrings = TCStrings.App.get();
	private Command submitSuccessfullyCompleted;

	public ImportTestResultsXMLFileView() {
		super("addxmlimport", tcStrings.importXMLFile());
	}

	@Override
	public String getIconStyle() {
		return null;
	}

	protected Widget onInitialize() {
		VerticalPanel verticalPanel = new VerticalPanel();
		final WfmButton2 importButton = new WfmButton2(wfmStrings.importString());
		HTML label = new HTML("<b class=customTitle>" + wfmStrings.messSelectFile() + "<font color='red'>*</font>:</b>", true);
		final FileUpload upload = new FileUpload();
		upload.setName(ATTACHMENT_PARAM_BASE + 0);
		final WfmFormPanel uploadLabel = new WfmFormPanel("/TestResultsUploadHandler");
		uploadLabel.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);
            boolean isError = uploadLabel.getErrorString() != null;
            if (uploadLabel.getErrorString() == null) {
                if (submitSuccessfullyCompleted != null) {
                    submitSuccessfullyCompleted.execute();
                }
            }
            importButton.setEnabled(true);
            if (isError) {
                Info.show(event.getResults(), Info.Type.WARNING);
            } else {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyImported(), tcStrings.xmlFile()), Info.Type.INFO);
            }
        });

		importButton.setWidth("80px");
		importButton.addClickHandler(event -> {
            int error = 0;
            if (!Validation.validateFileUploadRequired(upload, new HTML(tcStrings.messSelectXMLFile()), tcStrings.messSelectXMLFile())) {
                error++;
            }
            if (error > 0) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                return;
            }
            if (upload.getFilename() != null && !"".equals(upload.getFilename())) {
                if (".xml".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf(".")))) {
                    importButton.setEnabled(false);
                    uploadLabel.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(tcStrings.messSelectXMLFile(), Info.Type.WARNING);
                }
            }
        });
		uploadLabel.setWidget(upload);
		verticalPanel.add(label);
		verticalPanel.add(uploadLabel);
		verticalPanel.add(importButton);
		label.getElement().getStyle().setMargin(5d, Style.Unit.PX);
		uploadLabel.getElement().getStyle().setMargin(5d, Style.Unit.PX);
		importButton.getElement().getStyle().setMargin(5d, Style.Unit.PX);
		add(verticalPanel);
		return null;
	}

	@Override
	public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
		GWT.runAsync(new RunAsyncCallback() {
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess() {
				callback.onSuccess(onInitialize());
			}
		});
	}
}
