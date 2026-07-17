package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class IntroductionPageView extends CustomForm implements Constants, Colapse {
    private static final String formId = LayoutRPC.INTRODUCTION_PAGE_FORM;

    private TextBox okButtonNameBox;
    private TextBox cancelButtonNameBox;
    private KpiEditor kpiEditor;
    private WfmButton2 save;
    private Label okButtonTitle;
    private KpiSwitcher isAvtive;

    private String parentFormId;
    private String type;
    private IntroductionPageRpc pageRpc;

    public IntroductionPageView(String parentFormId, String type) {
        super("introView");
        setDescription(wfmStrings.introductionPage());
        this.parentFormId = parentFormId;
        this.type = type;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        kpiEditor = new KpiEditor(false);
        kpiEditor.setFromIntroPage(true);

        okButtonTitle = new Label(wfmStrings.continueButtonTitle());

        okButtonNameBox = new TextBox();
        okButtonNameBox.setPlaceHolder(wfmStrings.continueOnly());

        cancelButtonNameBox = new TextBox();
        isAvtive = new KpiSwitcher();
        isAvtive.setValue(true);
        getRpc();
        drawFields();
        show();
        return null;
    }
    private void drawFields(){
        addTitleField(CustomFormConstants.EDITOR_TITLE, wfmStrings.introductionPage());
        addField(CustomFormConstants.INTRODUCTION_TITLE, kpiEditor);

        addTitleField(CustomFormConstants.CUSTOMIZE_BUTTONS,wfmStrings.otherDetails());
        addField(CustomFormConstants.CUSTOMIZE_OK_BUTTON, okButtonNameBox, getTitle(wfmStrings.continueButtonTitle()));
        addField(CustomFormConstants.IS_ACTIVE, isAvtive, getTitle(wfmStrings.active()));
    }
    @Override
    protected void addButtons() {

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(clickEvent -> {
            save();
        });

        addButton(save);
    }

    @Override
    protected void getDataToFillFields() {
        if (pageRpc != null){
            kpiEditor.setData(pageRpc.getEditorValue());
            okButtonNameBox.setValue(pageRpc.getOkButtonName());
            cancelButtonNameBox.setValue(pageRpc.getCancelButtonName());
            isAvtive.setValue(pageRpc.getActive());
        }
    }

    @Override
    protected String getFormID() {
        return formId;
    }

    @Override
    protected String getFormType() {
        return this.type;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void save(){
        if (!validation()){
            Info.warn("You need write Introduction message");
            return;
        }
        if (pageRpc == null){
            pageRpc = new IntroductionPageRpc();
        }
        pageRpc.setEditorValue(Utils.encrypt(kpiEditor.getData()));
        if (okButtonNameBox != null && okButtonNameBox.getValue().isEmpty()) {
            pageRpc.setOkButtonName("Continue");
        } else {
            pageRpc.setOkButtonName(okButtonNameBox.getValue());
        }
        pageRpc.setCancelButtonName(cancelButtonNameBox.getValue());
        pageRpc.setActive(isAvtive.getValue());
        pageRpc.setFormId(formId);
        pageRpc.setParentFormId(parentFormId);

        LoadingPanel.loading(true);
        AllInOneService.App.get().saveIntoductionPage(pageRpc, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn("Something went wrong");
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                LoadingPanel.loading(false);
                if (aBoolean){
                    Info.show("Introduction page added successfully!!!");
                    IntroductionPageView.this.closeTab();
                }else{
                    Info.warn("Something went wrong");
                }
            }
        });

    }

    public void getRpc(){
        LoadingPanel.loading(true);
        AllInOneService.App.get().getIntoductionPageByParentFormId(parentFormId, new AsyncCallback<IntroductionPageRpc>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                pageRpc = null;
            }

            @Override
            public void onSuccess(IntroductionPageRpc pageRpcc) {
                LoadingPanel.loading(false);
                pageRpc = pageRpcc;
                getDataToFillFields();
            }
        });
    }

    private boolean validation(){
        int error = 0;
        if (kpiEditor.getData() == null || kpiEditor.getData().isEmpty()){
            error ++;
        }

        return error == 0;
    }
}
