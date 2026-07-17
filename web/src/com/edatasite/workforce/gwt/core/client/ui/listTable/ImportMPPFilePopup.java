package com.edatasite.workforce.gwt.core.client.ui.listTable;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 04.03.2010
 * Time: 19:07:13
 * To change this template use File | Settings | File Templates.
 */

public class ImportMPPFilePopup extends Composite implements CommandConstants {

    private WfmButton2 imp;
    private Integer objectId;
    private Integer projectId;
    private CRMLookUp project;
    private KpiCheckBox billable;
    private KpiModal popup;
    private Command submitSuccessfullyCompleted;
    private String url = "";
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public ImportMPPFilePopup(String url, Integer projectId) {
        super();
        this.url = url;
        this.projectId = projectId;
        init();
    }

    public ImportMPPFilePopup(String url) {
        super();
        this.url = url;
        init();
    }

    public KpiModal getPopup() {
        return popup;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setProjectID(Integer projectId) {
        this.projectId = projectId;
    }

    public void setSubmitCompleted(Command submitSuccessfullyCompleted) {
        this.submitSuccessfullyCompleted = submitSuccessfullyCompleted;
    }

    public void init() {
        popup = new KpiModal();
        popup.setTitle(wfmStrings.importMPPFile());
        popup.setWidth(400);
        popup.open();
        popup.addStyleName("file--ImportMPPFilePopup");

        HTML label = new HTML("<b class=customTitle>" + wfmStrings.messSelectFile() + "<font color='red'>*</font>:</b>", true);
        final FileUpload upload = new FileUpload();
        upload.setName(ATTACHMENT_PARAM_BASE + 0);

        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setFullSearch(true);
        CommonService.App.get().getProjects(false, new AbstractAsyncCallback<ProjectItem[]>() {
            public void success(final ProjectItem[] object) {
                Scheduler.get().scheduleDeferred(() -> {
                    if (projectId != null) {
                        for (ProjectItem item : object) {
                            if (item.getId().equals(projectId)) {
                                project.setSelected(item.getId(), item.getName());
                                project.setEnabled(false);
                            }
                        }

                    }
                });
            }
        });

        billable = new KpiCheckBox(wfmStrings.importTasksAsBillable());
        billable.setValue(true);

        final WfmFormPanel uploadLabel = new WfmFormPanel(url);
        uploadLabel.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);
            boolean isError = uploadLabel.getErrorString() != null;
            if (uploadLabel.getErrorString() == null) {
                objectId = uploadLabel.getObjectID();
                popup.close();
                if (submitSuccessfullyCompleted != null) {
                    submitSuccessfullyCompleted.execute();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, null, ImportMPPFilePopup.this);
                }
            }
            if (isError) {
                Info.show(wfmStrings.messParseErrorCompareFile(), Info.Type.WARNING);
            } else {
                Info.show(wfmStrings.messDataSuccUploaded(), Info.Type.INFO);
            }
        });

        VerticalPanelDiv verticalPanelDiv = new VerticalPanelDiv();
        verticalPanelDiv.add(10, label);
        verticalPanelDiv.add(10, upload);
        verticalPanelDiv.add(10, new Label(wfmStrings.messMPPFormat1()));
        verticalPanelDiv.add(10, new HTML("<b class=customTitle>" + Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()) + "<font color='red'>*</font>:</b>"));
        verticalPanelDiv.add(10, project);
        verticalPanelDiv.add(10, billable);

        uploadLabel.setWidget(verticalPanelDiv);

        imp = new WfmButton2(wfmStrings.importString(),WfmButton2.BTN_PRIMARY);
        imp.addClickHandler(event -> {
            int error = 0;
            if (!Validation.validateFileUploadRequired(upload, new HTML(wfmStrings.messSelectMPPFile()), wfmStrings.messSelectMPPFile())) {
                error++;
            }
            if (!Validation.validateFileUploadRequired(project, new HTML(wfmStrings.pleaseSelectProject()), wfmStrings.pleaseSelectProject())) {
                error++;
            }
            if (error > 0) {
                Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
                return;
            }
            if (upload.getFilename() != null && !"".equals(upload.getFilename())) {
                if (".mpp".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf("."))) ||
                        ".xml".equals(upload.getFilename().substring(upload.getFilename().lastIndexOf(".")))) {
                    imp.setEnabled(false);
                    projectId = project.getSelectedItemID();
                    uploadLabel.setParameter(BILLABLE, billable.getValue().toString());
                    uploadLabel.setObjectID(projectId);
                    uploadLabel.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(wfmStrings.messSelectMPPFile(), Info.Type.WARNING);
                }
            }
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> popup.close());

        popup.add(uploadLabel);
        popup.addButton(cancel);
        popup.addButton(imp);
    }
}