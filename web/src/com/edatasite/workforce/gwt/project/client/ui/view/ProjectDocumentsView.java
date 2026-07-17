package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class ProjectDocumentsView extends View implements Constants {

    private Integer projectID;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public ProjectDocumentsView(Integer projectID) {
        super("document", wfmStrings.documents());
        this.projectID = projectID;
    }


    public String getIconStyle() {
        return null;
    }

    public ImageResource getIconImage() {
        return null;
    }


    @Override
    protected Widget onInitialize() {
        GeneralFileUpload generalFileUpload = new GeneralFileUpload(F_PROJECT, projectID, projectID);
        generalFileUpload.setWidth(ATTACHMENT_WIDTH);
        add(generalFileUpload);
        return null;
    }

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
