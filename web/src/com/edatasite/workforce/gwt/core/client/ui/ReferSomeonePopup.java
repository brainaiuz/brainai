package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Farrukh Atabaev on 13.11.18.
 */
public class ReferSomeonePopup extends Composite implements Constants{

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final MessageCenterServiceAsync service = MessageCenterService.App.get();
    private static ReferSomeonePopupUiBinder ourUiBinder = GWT.create(ReferSomeonePopupUiBinder.class);
    private static String facebookAppId = Utils.getFacebookAppId();
    @UiField
    HTMLPanel panel;
    @UiField

    MaterialRadioButton radioEmail;
    @UiField
    HTMLPanel to;
    @UiField
    MaterialLink sendEmail;
    @UiField
    MaterialTextArea emailText;
/*    @UiField
    MaterialCheckBox agreeCheckBox;*/

    @UiField
    MaterialRadioButton radioFacebook;
    @UiField
    MaterialLink postFacebook;

    @UiField
    HTMLPanel rightPanelWrapper;
    @UiField
    DivElement discountDescription;
    @UiField
    DivElement helpSmallBusinessTitle;
    @UiField
    DivElement letYourFriendsKnowLabel;
    @UiField
    Element agreeWith;
    @UiField
    Element termsAndConditions;

    CustomCommand<Object> closeCommand;
    private MultiSelectContactLookUp toText;

    public ReferSomeonePopup() {
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    private void init() {

        toText = new MultiSelectContactLookUp(Constants.BY_BOTH, this.panel);
        toText.getList().setWidth("100%");
        to.add(toText);
        helpSmallBusinessTitle.setInnerHTML(wfmStrings.helpSmallBusiness());
        discountDescription.setInnerHTML(wfmStrings.discountDescription());
        letYourFriendsKnowLabel.setInnerHTML(wfmStrings.letYourFriendsKnow());
        agreeWith.setInnerHTML(wfmStrings.agreeWith());
        emailText.setText(wfmStrings.emailText());
        termsAndConditions.setInnerHTML(wfmStrings.termsAndConditions());
        sendEmail.setText(wfmStrings.send());
        radioEmail.addClickHandler(e -> {
            rightPanelWrapper.setStyleName("payment-gateway-panel__side payment-gateway-panel__side--stripe");
        });
        radioFacebook.addClickHandler(e -> {
            rightPanelWrapper.setStyleName("payment-gateway-panel__side payment-gateway-panel__side--paypal payment-gateway-panel__side--facebook");
        });

        sendEmail.addClickHandler(e -> {
            sendReferWithEmail();
        });


        Span text = new Span(wfmStrings.post());
        postFacebook.add(text);
        postFacebook.addClickHandler(e -> {
            postFacebook.setEnabled(false);
            LoadingPanel.loading(true, panel);
            initFacebookScript(new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception e) {
                    LoadingPanel.loading(false, panel);
                    postFacebook.setEnabled(true);
                    Info.show(wfmStrings.unexpectedErrorOccuredWhileOpeningFacebook(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Void aVoid) {
                    postToFacebook();
                }
            });
        });
    }

    public static void initFacebookScript(final Callback<Void, Exception> callback) {

        ScriptInjector.fromUrl("//connect.facebook.net/en_US/all.js#xfbml=1&appId=" + facebookAppId).setCallback(
                new Callback<Void, Exception>() {
                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        callback.onSuccess(result);
                    }
                }).inject();
    }

    private native void postToFacebook() /*-{
        var that = this
        FB.ui( {
            method: 'feed',
            name: "kpi.com",
            link: "https://www.kpi.com/",
            picture: "https://www.kpi.com/wp-content/uploads/2018/06/30.png",
            caption: "Even during my trial, I spent less time on annoying admin chores. It helps keep my expenses organized and even gets me paid faster. Try it for free."
        }, function( response ) {
            that.@com.edatasite.workforce.gwt.core.client.ui.ReferSomeonePopup::postToFacebookSucess()();
        } );
    }-*/;

    private void sendReferWithEmail(){
        sendEmail.setEnabled(false);
        if(!validateReferWithEmail()){
            sendEmail.setEnabled(true);
            return;
        }
        Email newEmail = new Email();
        newEmail.setContent(emailText.getText());
        newEmail.setToEmails(toText.getSelectedItemsAsString());

        LoadingPanel.loading(true, panel);
        service.sendReferMessage(newEmail, new AbstractAsyncCallback<Void>() {
            public void success(Void result) {
                LoadingPanel.loading(false, panel);
                sendEmail.setEnabled(true);
                Info.show(wfmStrings.yourMessageHasBeenSent(), Info.Type.INFO);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                sendEmail.setEnabled(true);
                Info.show(wfmStrings.unexpectedErrorOccuredWhileSending(), Info.Type.WARNING);
            }
        });
    }

    private void postToFacebookSucess(){
        service.sendUserPostedToFacebookEmail(new AbstractAsyncCallback<Void>() {
            public void success(Void result) {
                LoadingPanel.loading(false, panel);
                postFacebook.setEnabled(true);
                Info.show(wfmStrings.yourPostHasBeenSubmitted(), Info.Type.INFO);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                postFacebook.setEnabled(true);
                Info.show(wfmStrings.unexpectedErrorOccuredWhileOpeningFacebook(), Info.Type.WARNING);
            }
        });
    }

    public boolean validateReferWithEmail() {
        int errors = 0;
        if (!validateEmail(toText, true)) {
            errors++;
        }
        /*if (!agreeCheckBox.isChecked() == true) {
            agreeCheckBox.addStyleName(ERROR_FORM_STYLE);
            agreeCheckBox.addClickHandler(event -> agreeCheckBox.removeStyleName(ERROR_FORM_STYLE));
            errors ++;
        }*/

        if (Utils.isNullOrEmpty(emailText.getText())) {
            emailText.addStyleName(ERROR_FORM_STYLE);
            emailText.addKeyDownHandler(event -> emailText.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        return true;
    }

    private boolean validateEmail(final MultiSelectContactLookUp textBox, boolean notEmptyAlso) {
        int errors = 0;
        boolean foundAtLeastOne = false;
        if (textBox.getSelectedItems().size() != 0) {
            for (SelectItem item : textBox.getSelectedItems()) {
                item.setName(item.getName().trim());
                foundAtLeastOne = true;
                if (item.getName().contains("<") && item.getName().contains(">")) {
                    String emailItem = item.getName().substring(item.getName().lastIndexOf("<") + 1, item.getName().lastIndexOf(">"));
                    if (!Utils.validateEmail(emailItem, false)) {
                        textBox.addStyleName(ERROR_FORM_STYLE);
                        textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
                        errors++;
                    }
                } else if (!Utils.validateEmail(item.getName(), false)) {
                    textBox.addStyleName(ERROR_FORM_STYLE);
                    textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
                    errors++;
                }
            }
        }
        if (notEmptyAlso && !foundAtLeastOne) {
            textBox.addStyleName(ERROR_FORM_STYLE);
            textBox.getSuggestBox().addKeyDownHandler(event -> textBox.removeStyleName(ERROR_FORM_STYLE));
            errors++;
        }
        return errors == 0;
    }

    public CustomCommand<Object> getCloseCommand() {
        return closeCommand;
    }

    public void setCloseCommand(CustomCommand<Object> closeCommand) {
        this.closeCommand = closeCommand;
    }

    public interface PricingStyle extends ClientBundle {
        @CssResource.NotStrict
        @Source("AllPricingView.css")
        CssResource markupPricing();
    }

    interface ReferSomeonePopupUiBinder extends UiBinder<HTMLPanel, ReferSomeonePopup> {
    }
}
