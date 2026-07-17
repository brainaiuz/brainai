package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 27, 2011
 * Time: 12:37:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddServerUploadView extends View implements Colapse {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private TextArea2 description = new TextArea2(wfmStrings.description());
    private RadioButton hostAws = new KpiRadioButton("host", " AWS");
    private RadioButton hostLive = new KpiRadioButton("host", " LIVE");
    private WfmForm form = new WfmForm();
    private TextArea2 message = new TextArea2(wfmStrings.message());
    private WfmForm.Field messageField;
    private TextBox version = new TextBox();
    private WfmForm.Field versionField;
    private WfmButton2 saveButton;

    public AddServerUploadView() {
        super("adduploadview", backendStrings.uploadView());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        String width = "400px";
        version.setWidth(width);
        message.setWidth(width);
        description.setWidth(width);
        hostLive.setValue(true);

        versionField = form.addField(backendStrings.uploadVersion(), version, true);
        messageField = form.addField(null, message, true);
        form.addField(null, description);
//        form.addField("Host", new Widget[] {hostLive, hostAws}, false);
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, (ClickHandler) clickEvent -> save());
        form.addButton(saveButton);

        add(form);
        return null;
    }

    private void save() {
        if (!validation()) {
            return;
        }

        CompanyItem details = new CompanyItem();
        details.setName(version.getText());
        details.setCompanyName(message.getText());
        details.setDescription(description.getText());
//        details.setNewItem(hostLive.getValue());

        LoadingPanel.loading(true);
        BackendService.App.get().saveLastUploadDetails(details, new AbstractAsyncCallback<Void>() {
            public void success(Void result) {
                LoadingPanel.loading(false);
                Window.alert(backendStrings.youHaveSuccessfullySaveTheLatestServerUploadDetails());
//                closeTab();
            }
        });
    }

    private boolean validation() {
        boolean noErrors = true;

        if (!Validation.validateTextBoxRequired(version, versionField)) {
            noErrors = false;
        }
        if (!Validation.validateTextAreaRequired(message, messageField)) {
            noErrors = false;
        }

        return noErrors;
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
