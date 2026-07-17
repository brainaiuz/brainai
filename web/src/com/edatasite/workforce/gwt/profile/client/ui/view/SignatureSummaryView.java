package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SignatureItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 12.02.13
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class SignatureSummaryView extends CustomForm implements Constants, Colapse {
    private Integer objectID;
    private HTML user_name, signature_content;
    private SignatureItem item;
    private String test_code_ID_name = "summary_signature_view_";


    public SignatureSummaryView(Integer objectID) {
        super("summary", wfmStrings.signatureView());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        //edit button
        addEditButton().addClickHandler(event -> {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("signatureedit|addsignature/" + objectID, item.getUserName());
        });
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getSignature(objectID, new AbstractAsyncCallback<SignatureItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SignatureItem result) {
                item = result;
                LoadingPanel.loading(false);
                if (result != null) {
                    user_name.setHTML(result.getUserName() != null ? result.getUserName() : "");
                    signature_content.setHTML(result.getSignature() != null ? result.getSignature() : "");
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
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        user_name = new HTML();
        user_name.addStyleName(DEFAULT_WIDTH);
        user_name.ensureDebugId(test_code_ID_name + "template_name");
        signature_content = new HTML();
        signature_content.addStyleName(DEFAULT_WIDTH);
        signature_content.ensureDebugId(test_code_ID_name + "template_message_content");

        addTitleField(CustomFormConstants.DETAILS, wfmStrings.signatureInfo());

        addField(CustomFormConstants.USER, user_name, getTitle(wfmStrings.user()));
        addField(CustomFormConstants.SIGNATURE_CONTENT, signature_content, getTitle(wfmStrings.signature()));

        show();
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
