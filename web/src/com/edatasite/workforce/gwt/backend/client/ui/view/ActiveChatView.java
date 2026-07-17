package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.notifications.Info.show;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 30/10/11
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class ActiveChatView extends View {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private KpiCheckBox isActiveLiveChat;
    private KpiCheckBox isActiveExpertChat;
    //    private SchemaLookUp schemaLookUp;
    private DataListBox companyList;
    //    private WfmButton2 clear;
    private WfmButton2 save;
    private WfmButton2 registerEmployeeButton;


    public ActiveChatView() {
        super("chatactive", backendStrings.chatsEnable());
    }

    public String getIconStyle() {
        return "backend activeChatView";
    }

    protected Widget onInitialize() {
//        schemaLookUp = new SchemaLookUp();
        companyList = new DataListBox();
        companyList.setWidth("200px");
        companyList.setWithoutNullLabel(true);

        isActiveLiveChat = new KpiCheckBox();
        isActiveExpertChat = new KpiCheckBox();
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        registerEmployeeButton = new WfmButton2(backendStrings.registerEmployeesToChat());

        companyList.addValueChangeHandler(changeEvent -> {
            if (companyList.getSelectedItem() != null && !companyList.getSelectedItem().getId().equals(0)) {
                LoadingPanel.loading(true);
                BackendService.App.get().getChatActivities(companyList.getSelectedItem().getId(), new AsyncCallback<Boolean[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Boolean[] values) {
                        LoadingPanel.loading(false);
                        isActiveLiveChat.setValue(values[0]);
                        isActiveExpertChat.setValue(values[1]);
                    }
                });
            }
        });

        save.addClickHandler(clickEvent -> {
            if (!validate()) {
                return;
            }
            LoadingPanel.loading(true);
            save.setEnabled(false);
            BackendService.App.get().saveChatActivities(companyList.getSelectedItem().getId(), isActiveLiveChat.getValue(), isActiveExpertChat.getValue(), new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                }

                @Override
                public void onSuccess(Void aVoid) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                }
            });
        });

        BackendService.App.get().getSchemasAsSelectItem(null, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                companyList.setItems(selectItems);
            }
        });

        registerEmployeeButton.addClickHandler(clickEvent -> {
            if (companyList.getSelectedItem() != null && !companyList.getSelectedItem().getId().equals(0)) {
                registrationShell();
            }
        });

        WfmForm content = new WfmForm();

        content.addField(wfmStrings.company(), companyList, true);

        content.addField(backendStrings.isActiveLiveChat(), isActiveLiveChat);
        content.addField(backendStrings.isActiveExpertPanelChat(), isActiveExpertChat);
        content.addButton(save);
        content.addButton(registerEmployeeButton);

        add(content);
        return null;
    }

    private void registrationShell() {
        LoadingPanel.loading(true);
        registerEmployeeButton.setEnabled(false);
        BackendService.App.get().registrationChatUsers(companyList.getSelectedItem().getId(), new AsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                show(backendStrings.failedDuringAddingEmployeesToTheGroup(), Info.Type.WARNING);
                registerEmployeeButton.setEnabled(true);
            }

            @Override
            public void onSuccess(Object o) {
                LoadingPanel.loading(false);
                show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.employees()), Info.Type.WARNING);
                registerEmployeeButton.setEnabled(true);
            }
        });
    }

    private boolean validate() {
        return companyList.getSelectedItem() != null && !companyList.getSelectedItem().getId().equals(0);
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
