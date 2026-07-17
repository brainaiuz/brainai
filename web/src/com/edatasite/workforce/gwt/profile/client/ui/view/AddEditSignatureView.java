package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SignatureItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 11.02.13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class AddEditSignatureView extends CustomForm implements Constants, Colapse {
    private KpiEditor editorHTML;
    private DataListBox user;
    private String test_code_ID_name = "add_edit_signature_view_";
    private Integer objectID;
    private KpiCheckBox showSignatureOnTop;
    private boolean isEditForm;

    public AddEditSignatureView() {
        super("addsignature", wfmStrings.addSignature());
    }

    public AddEditSignatureView(Integer objectID) {
        super("addsignature", wfmStrings.addSignature());
        if (objectID != null) {
            setDescription(wfmStrings.signatureEdit());
            this.objectID = objectID;
        }
    }

    private void getUsers() {
        user = new DataListBox();
        user.addStyleName(DEFAULT_WIDTH);
        user.ensureDebugId(test_code_ID_name + "user");
        isEditForm = getFormType().equals(LayoutRPC.EDIT);

        ProfileService.App.get().getUsers(isEditForm, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                if (result != null) {
                    user.setItems(result);
                    user.setSelected(Utils.getUserID());
                    user.setEnabled(Utils.hasPermission(PermissionConstants.SETTINGS_SIGNATURE_ADD) && !isEditForm);
                }
            }
        });

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        getUsers();

        //Info
        Span tooltipWrapper = new Span();

        Icon iInfo = new Icon();
        iInfo.setClass("ficon--info");
        MaterialLink iconLink = new MaterialLink();
        iconLink.add(iInfo);
        String activation = "infoDropDown";
        iconLink.setActivates(activation);

        MaterialDropDown dropDown = new MaterialDropDown(activation);
        dropDown.addStyleName("dropdown-content dropdown-content-tooltip");
        dropDown.getElement().setInnerHTML(wfmStrings.signatureHelpText());
        dropDown.setHover(true);

        tooltipWrapper.add(iconLink);
        tooltipWrapper.add(dropDown);

        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp = new HorizontalPanel();
        showSignatureOnTop = new KpiCheckBox(wfmStrings.signatureOnTop());
        showSignatureOnTop.ensureDebugId(test_code_ID_name + "post_signature");
        editorHTML = new KpiEditor();
        hp.add(editorHTML);
        hp.add(tooltipWrapper);
        vp.add(hp);
        vp.add(showSignatureOnTop);

        addTitleField(CustomFormConstants.DETAILS, wfmStrings.signatureInfo());
        addField(CustomFormConstants.USER, user, getTitle(wfmStrings.user(), true));
        addField(CustomFormConstants.SIGNATURE_CONTENT, vp, getTitle(wfmStrings.signature(), false));

        show();
        return null;
    }


    @Override
    protected void addButtons() {
        addButton(objectID != null ? wfmStrings.update() : wfmStrings.save(), WfmButton2.BTN_PRIMARY, null, (test_code_ID_name + "save_and_close_button"), (ClickHandler) event -> save());
    }

    private void save() {
        enableButton(false);
        SignatureItem signature = new SignatureItem();
        signature.setObjectID(objectID);
        signature.setUserID(user.getSelectedItem().getId());
        signature.setSignature(editorHTML.getData());
        signature.setShowSignatureOnTop(showSignatureOnTop.getValue());
        LoadingPanel.loading(true);
        ProfileService.App.get().saveSignature(signature, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                enableButton(true);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(objectID == null ? wfmStrings.messSuccessfullySaved() : wfmStrings.messSuccessfullyUpdated(), wfmStrings.signature()), Info.Type.INFO);
                closeTab();
                objectID = result;
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIGNATURE_ADD, result, AddEditSignatureView.this);
            }
        });
    }


    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getSignature(objectID, new AbstractAsyncCallback<SignatureItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SignatureItem signature) {
                LoadingPanel.loading(false);
                if (signature != null) {
                    user.setSelected(signature.getUserID());
                    editorHTML.setData(signature.getSignature() != null ? signature.getSignature() : "");
                    showSignatureOnTop.setValue(signature.isShowSignatureOnTop());
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SIGNATURE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
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