package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralDocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddEmployeeDocumentsView extends GeneralDocumentsView implements Colapse {

    private WfmButton2 saveClose;


    public AddEmployeeDocumentsView(Integer id, Integer employeeID) {
        super("document", wfmStrings.documents(), Constants.F_EMPLOYEE_PROFILE, employeeID, employeeID, null);
    }

    public AddEmployeeDocumentsView(Integer id, Integer employeeID, String typeCode) {
        super("document", wfmStrings.documents(), Constants.F_EMPLOYEE_PROFILE, employeeID, employeeID, typeCode);
    }

    @Override
    public String getIconStyle() {
        return "hrms employee-profile-document";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_DOCUMENTS_FORM;
    }

    @Override
    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_PROFILE_DOCUMENT_LIST);
    }

    @Override
    protected void addButtons() {
        saveClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveClose.addClickHandler(clickEvent -> save());
        addButton(saveClose);

    }

    protected void getDataToFillFields() {

    }

    private void save() {
        if (validate(generalFileUpload)) {
            enableButton(false);
            LoadingPanel.loading(true);
            DocumentsService.App.get().updateFiles(generalFileUpload.getNewlyAttachedFiles(), entityId, typeCode, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Void v) {
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, v, AddEmployeeDocumentsView.this);
                    closeTab();
                }
            });
        }
    }

    private Boolean validate(GeneralFileUpload generalFileUpload) {
        int errors = 0;
        if (generalFileUpload.getAttachedFiles() == null || generalFileUpload.getAttachedFiles().length == 0) {
            generalFileUpload.setStyleName("x-form-invalid");
            Utils.scrollIntoView(generalFileUpload.getElement());
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return errors == 0;
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

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}