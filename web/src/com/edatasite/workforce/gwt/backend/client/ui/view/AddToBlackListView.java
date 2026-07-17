package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * User: admin
 * Date: Jan 5, 2010
 * Time: 5:58:20 PM
 */
public class AddToBlackListView extends BaseListView {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private List<TextBox> emailBoxes;
    private List<String> validEmails;

    public AddToBlackListView() {
        super("add", backendStrings.addToBlackList());
    }

    @Override
    public String getIconStyle() {
        return "icon-addToBlackListView";
    }

    @Override
    protected Widget onInitialize() {
        emailBoxes = new ArrayList<>();
        validEmails = new ArrayList<>();
        VerticalPanel panel = new VerticalPanel();

        panel.setSpacing(15);
        int i = 0;
        TextBox box;
        while (i < 10) {
            box = new TextBox();
            emailBoxes.add(box);
            panel.add(box);
            i++;
        }

        WfmButton2 button = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        button.addClickHandler(event -> save());

        panel.add(button);

        add(panel);
        return null;
    }

    private void save() {
        if (validate()) {
            String[] emails = new String[validEmails.size()];
            for (int i = 0; i < validEmails.size(); i++) {
                emails[i] = validEmails.get(i);
            }
            BackendService.App.get().saveBlackEmails(emails, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    closeTab();
                    Info.show(wfmStrings.failed(), Info.Type.WARNING);

                }

                @Override
                public void success(Void result) {
                    closeTab();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.blackList()), Info.Type.WARNING);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TO_BLACK_LIST, result, AddToBlackListView.this);
                }
            });
        } else {
            Info.show(backendStrings.inputValidEmails(), Info.Type.WARNING);
        }
    }

    private boolean validate() {
        validEmails.clear();
        for (TextBox box : emailBoxes) {
            if (box != null && box.getText() != null && !"".equals(box.getText())) {
                validEmails.add(box.getText());
            }
        }

        for (String email : validEmails) {
            if (!Validation.validEmailFormat(email, false)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}