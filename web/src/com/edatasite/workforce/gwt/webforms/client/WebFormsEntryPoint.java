package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddCaseView;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddLeadView;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.*;

import java.util.Date;

public class WebFormsEntryPoint implements EntryPoint {
    private VerticalPanel container;
    private static final WebFormsServiceAsync webFormsService = WebFormsService.App.get();
    public static WebFormsResources resources = GWT.create(WebFormsResources.class);
    
    private WebForm webForm;

    public interface WebFormsResources extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/crm/client/crm.css")
        CssResource crm();
    }

    private FlowPanel f;

    public void onModuleLoad() {
        resources.crm().ensureInjected();
        f = new FlowPanel();
        f.addStyleName("workarea");
        container = new VerticalPanel();
        container.setWidth("500px");
        container.addStyleName("cm-webform spacing5-padding5");
        f.add(container);
        RootPanel.get().addStyleName("container-1");
        RootPanel.get().add(f);
        generateForm();
        scrollbarEvent();
    }

    public static native void scrollbarEvent() /*-{
        $wnd.contentScroll('.scrollbar-external', '.frame__content')
        $wnd.contentScroll('.frame__nav__scroll', '.frame__nav')
    }-*/;

    private void generateForm() {
        LoadingPanel.loading(true);
        webFormsService.decryptLink(getLocationHref(), new AsyncCallback<WebForm>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                errorOccured();
            }

            @Override
            public void onSuccess(final WebForm result) {
                if (result != null) {
                    webForm = result;
                    String customCss = result.getCustomForm() != null && result.getCustomForm().getCustomCss() != null ? result.getCustomForm().getCustomCss() : result.getCustomCss();
                    if (customCss != null && !"".equals(customCss)) {
                        Utils.addStyleTag(customCss);
                    }
                    Utils.userSettings = result.userSettings;
                    if (Utils.userSettings != null && Utils.userSettings.containsKey(Constants.SESSION_ID)) {
                        Cookies.setCookie(Constants.SESSION_ID_COOKIE+"_webform", Utils.userSettings.get(Constants.SESSION_ID));
                    }
                    if (result.getCustomForm() != null && !LayoutRPC.WEB_FORM.equals(result.getCustomForm().getFormID())) {
                        String formID = result.getCustomForm().getFormID() == null ? "" : result.getCustomForm().getFormID();
                        f.removeFromParent();
                        Utils.setWebFormID(result.getObjectId());
                        Utils.setButtonText(result.getButtonText());
                        final CustomForm view = getViewByFormID(formID);
                        if (view != null) {
                            view.setFormRPC(webForm.getCustomForm());
                            RootPanel.get().add(view.onReadyToInitialize());
                            RootPanel.get().addStyleName("container");
                            view.show();
                            view.getElement().getStyle().setWidth(100, Style.Unit.PCT);
                            view.getElement().getStyle().setHeight(100, Style.Unit.PCT);
                            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.CAPTCHA_ADD_TO_FORM, (sender, args) -> addCaptchaToView(view));
                            addCaptchaToView(view);
                        }
                        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.WEB_FORM_SAVED, (sender, args) -> {
                            Integer objectID = args != null && args instanceof Integer ? Integer.valueOf(args.toString()) : null;
                            onWebFormSaved(objectID, result.getCustomForm().getFormID());
                        });
                    } else {
                        AddFormView form = new AddFormView(result);
                        container.add(form);
                        form.show();
                    }
                } else {
                    errorOccured();
                }
                removeLoadingBar();
                LoadingPanel.loading(false);
            }
        });
    }

    protected void removeLoadingBar() {
        if (RootPanel.get("Loading-Message") != null) {
            DOM.removeChild(RootPanel.get().getElement(), RootPanel.get("Loading-Message").getElement());
        }
    }
    private TextBox antibotTextBox;
    private SimplePanel imagePanel;

    private void addCaptchaToView(CustomForm view) {
        if (webForm.getUseCatpcha()) {
            antibotTextBox = new TextBox();
            antibotTextBox.addBlurHandler(event -> Utils.setAntibot(antibotTextBox.getText()));
            antibotTextBox.addStyleName("width250");
            if (webForm.getCaptchaDescription() != null && !"".equals(webForm.getCaptchaDescription())) {
                view.addTitleField(CustomFormConstants.CAPTCHA.DESCRIPTION, webForm.getCaptchaDescription());
            }
            imagePanel = new SimplePanel();
            addCaptchaImage();
            view.addField(CustomFormConstants.CAPTCHA.IMAGE, imagePanel);
            view.addTitleField(CustomFormConstants.CAPTCHA.CANT_READ, webForm.getCaptchaCantRead());
            SimpleLink addAnotherImage = new SimpleLink(webForm.getCaptchaTryAnother());
            addAnotherImage.addClickHandler(event -> addCaptchaImage());
            view.addField(CustomFormConstants.CAPTCHA.RELOAD_IMAGE, addAnotherImage);
            view.addField(CustomFormConstants.CAPTCHA.TEXTBOX, antibotTextBox);
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.CAPTCHA_IS_EMPTY_ERROR, (sender, args) -> antibotTextBox.addStyleName(Constants.ERROR_FORM_STYLE));
        } else {
            Utils.setAntibot(Constants.NO_CAPTCHA_USED);
        }
    }

    private void addCaptchaImage() {
        if (imagePanel.getWidget() != null) {
            imagePanel.remove(imagePanel.getWidget());
        }
        Image image = new Image("/jcaptcha/" + new Date().getTime() + "/?webformparams=" + Utils.getLocationString().toString());
        image.addClickHandler(clickEvent -> addCaptchaImage());
        imagePanel.add(image);
    }

    private void onWebFormSaved(Integer objectID, String formID) {
        if (objectID != null) {
            if (objectID.intValue() == Constants.ANTIBOT_ERROR) {
                antibotTextBox.addStyleName(Constants.ERROR_FORM_STYLE);
                antibotTextBox.addKeyPressHandler(event -> antibotTextBox.removeStyleName(Constants.ERROR_FORM_STYLE));
                addCaptchaImage();
            } else if (webForm != null && objectID > 0) {
                if (Utils.userSettings != null && Utils.userSettings.containsKey(Constants.SESSION_ID)) {
                    ClientSecurityContext.get().setSessionId(Utils.userSettings.get(Constants.SESSION_ID));
                }
                webFormsService.sendEmailNotifications(webForm.getObjectId(), objectID, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(Void result) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
                if (webForm.getConfirmationMessage() != null) {
                    RootPanel.get().clear();
                    FlowPanel p = new FlowPanel();
                    p.getElement().getStyle().setWidth(100, Style.Unit.PCT);
                    p.add(new HTML(webForm.getConfirmationMessage()));
                    RootPanel.get().add(p);
                    if (webForm.getRedirectURL() != null) {
                        Timer timer = new Timer() {
                            @Override
                            public void run() {
                                redirectParent(webForm.getRedirectURL());
                            }
                        };
                        timer.schedule(5000);
                    }
                } else if (webForm.getRedirectURL() != null) {
                    Utils.redirect(webForm.getRedirectURL());
                }
            }
        }
    }

    private CustomForm getViewByFormID(String formID) {
        CustomForm view = null;
//        if (LayoutRPC.CANDIDATE_FORM.equals(formID)) {
////            view = new AddCandidateView();  //in future we'll remove this module, currently no clients, need to remove this webForm module from CRM
//        } else

            if (LayoutRPC.LEAD_FORM.equals(formID)) {
            view = new AddLeadView("");
        } else if (LayoutRPC.CASE_FORM.equals(formID)) {
            view = new AddCaseView("");
        } else if (LayoutRPC.CONTACT_FORM.equals(formID)) {
            view = new AddContactView("", "", "");
        }
        return view;
    }

    private void errorOccured() {
        Info.show("Sorry, the form is not working now. \n Please check the url.", Info.Type.WARNING);
    }

    native void redirectParent(String url)/*-{
        $wnd.parentRedirect(url);
    }-*/;

    private String getLocationHref() {
        return Utils.getLocationString().toString();
    }
}