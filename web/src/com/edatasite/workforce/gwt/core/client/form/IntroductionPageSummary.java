package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class IntroductionPageSummary extends CustomForm implements Colapse {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Div bodyContent;
    private HTML bodyElements;

    private WfmButton2 okButton;

    private String customFormId;
    private IntroductionPageRpc pageRpc;

    Integer fID = null;
    String formID = null;
    String name = null;
    String lookUpType = null;
    Integer lookUpTypeId = null;
    String urlToNextPage;
    public IntroductionPageSummary(Integer fID, String formID, String name1, String lookUpType, Integer lookUpTypeId) {
        this(formID);
        this.fID = fID;
        this.formID = formID;
        this.name = name1;
        this.lookUpType = lookUpType;
        this.lookUpTypeId = lookUpTypeId;
    }
    public IntroductionPageSummary(String customFormId) {
        super("viewForm");
        setDescription(wfmStrings.introductionPage());
        this.customFormId = customFormId;

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        bodyContent = new Div();
        bodyElements = new HTML();
        bodyContent.add(bodyElements);

        addField(CustomFormConstants.INTRODUCTION_TITLE, bodyContent);
        addField(CustomFormConstants.CUSTOMIZE_OK_BUTTON, okButton);

        show();
        return null;
    }

    @Override
    protected void addButtons() {
        okButton = new WfmButton2("OK", WfmButton2.BTN_PRIMARY);

        if (pageRpc != null){
            okButton.setText(pageRpc.getOkButtonName());
        }

        okButton.addClickHandler(event -> okAction());

        addButton(okButton);

    }

    private void cancelAction() {
        IntroductionPageSummary.this.closeTab();
    }

    private void okAction() {
        IntroductionPageSummary.this.closeTab();
        if (this.lookUpType != null && this.lookUpTypeId != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add//" + this.fID + "/" + this.formID + "/" + this.name + "/" + this.lookUpType + "/" + this.lookUpTypeId);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + this.fID + "/" + this.formID + "/" + this.name);
        }
    }


    @Override
    protected void getDataToFillFields() {
        getRpc();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INTRODUCTION_PAGE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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

    public void getRpc(){
        LoadingPanel.loading(true);
        AllInOneService.App.get().getIntoductionPageByParentFormId(customFormId, new AsyncCallback<IntroductionPageRpc>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                pageRpc = null;
            }

            @Override
            public void onSuccess(IntroductionPageRpc pageRpcc) {
                LoadingPanel.loading(false);
                if (pageRpcc != null) {
                    pageRpc = pageRpcc;
                    bodyElements.setHTML(pageRpcc.getEditorValue());
                    okButton.setText(pageRpcc.getOkButtonName());
                } else if (pageRpc == null || !pageRpc.getActive()) {
                    okAction();
                }

            }
        });
    }


}
