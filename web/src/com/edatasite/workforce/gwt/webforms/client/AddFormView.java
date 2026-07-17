package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.webforms.client.forms.*;
import com.edatasite.workforce.gwt.webforms.client.forms.customform.AbstractAddFormViewWithCustomForm;
import com.edatasite.workforce.gwt.webforms.client.forms.customform.AddCandidateFormViewWithCustomForm;
import com.edatasite.workforce.gwt.webforms.client.forms.customform.AddCaseFormViewWithCustomForm;
import com.edatasite.workforce.gwt.webforms.client.forms.customform.AddLeadFormViewWithCustomForm;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.Date;

/**
 * User: Hayot
 * Date: Aug 12, 2010
 * Time: 5:11:08 PM
 */
public class AddFormView extends Composite implements WebFormConstants {

    private final static WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WebFormsServiceAsync webFormsService = WebFormsService.App.get();
    private WebForm webForm;
    private VerticalPanel container = new VerticalPanel();
    private SimplePanel imagePanel;

    public AddFormView(Integer formID, Integer companyId) {
        LoadingPanel.loading(true);

        webFormsService.getWebForm(formID, companyId, new AsyncCallback<WebForm>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(WebForm result) {
                LoadingPanel.loading(false);
                webForm = result;
                init();
            }
        });
        initWidget(container);
    }


    public AddFormView(WebForm form) {
        webForm = form;
        initWidget(container);
        container.setWidth("100%");
        init();
    }

    private AbstractAddFormViewWithCustomForm addCustomFormView;

    private void init() {
        Utils.userSettings = webForm.userSettings;
        HTML description = new HTML("<span>" + webForm.getDescription() + "</span>");
        HTML title = new HTML("<b class='customTitle' style='font-size:14px;'>" + webForm.getTitle() + "</b>");
        if (!webForm.hasCustomContent()) {
            container.add(title);
            container.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_CENTER);
            container.add(description);
            container.setCellHorizontalAlignment(description, HasHorizontalAlignment.ALIGN_LEFT);
        }
        final WfmButton2 saveButton = new WfmButton2(webForm.getButtonText());
        AbstractAddFormView addFormView = null;

        final VerticalPanel antiBot = new VerticalPanel();
        antiBot.setSpacing(5);
        TextBox textBox = null;
        if (webForm.getUseCatpcha()) {
            textBox = new TextBox();

            if (webForm.getCaptchaDescription() != null && !"".equals(webForm.getCaptchaDescription())) {
                antiBot.add(new HTML(webForm.getCaptchaDescription()));
            }
            imagePanel = new SimplePanel();
            addCaptchaImage();
            antiBot.add(imagePanel);
            HorizontalPanel hpLink = new HorizontalPanel();
            hpLink.add(new HTML(webForm.getCaptchaCantRead() + "&nbsp;"));
            SimpleLink addAnotherImage = new SimpleLink(webForm.getCaptchaTryAnother());
            addAnotherImage.addClickHandler(event -> addCaptchaImage());
            hpLink.add(addAnotherImage);
            antiBot.add(hpLink);
            antiBot.setCellHorizontalAlignment(imagePanel, HasHorizontalAlignment.ALIGN_LEFT);
            antiBot.setCellHorizontalAlignment(hpLink, HasHorizontalAlignment.ALIGN_LEFT);
            antiBot.add(textBox);
            antiBot.setCellHorizontalAlignment(textBox, HasHorizontalAlignment.ALIGN_LEFT);
        }
        if (webForm.hasCustomContent()) {
            if (WebFormConstants.LEAD_FORM.equals(webForm.getWebFormType().getDescription())) {
                addCustomFormView = new AddLeadFormViewWithCustomForm(webForm, antiBot, false, false);
            } else if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType().getDescription())) {
                addCustomFormView = new AddCaseFormViewWithCustomForm(webForm, antiBot);
            } else if (WebFormConstants.CANDIDATE_FORM.equals(webForm.getWebFormType().getDescription())) {
                addCustomFormView = new AddCandidateFormViewWithCustomForm(webForm, antiBot);
            }
            addCustomFormView.addField("WF_title", title, null);
            addCustomFormView.addField("WF_description", description, null);
            addCustomFormView.addField("WF_saveButton", saveButton, null);
            addWebForm(addCustomFormView, description, saveButton, textBox);
        } else {
            if (WebFormConstants.LEAD_FORM.equals(webForm.getWebFormType(true))) {
                addFormView = new AddLeadFormView(webForm, antiBot, false, false);
            } else if (WebFormConstants.CASE_FORM.equals(webForm.getWebFormType(true))) {
                addFormView = new AddCaseFormView(webForm, antiBot);
            } else if (WebFormConstants.CANDIDATE_FORM.equals(webForm.getWebFormType(true))) {
                addFormView = new AddCandidateFormView(webForm, antiBot);
            }
            addWebForm(addFormView, description, saveButton, textBox);
            HorizontalPanel hpButton = new HorizontalPanel();
            hpButton.add(saveButton);
            container.add(hpButton);
            container.setCellHorizontalAlignment(hpButton, HasHorizontalAlignment.ALIGN_CENTER);
        }
    }

    private void addCaptchaImage() {
        if (imagePanel.getWidget() != null) {
            imagePanel.remove(imagePanel.getWidget());
        }
        imagePanel.add(new Image("/jcaptcha/" + new Date().getTime() + "/?webformparams="  + Utils.getLocationString().toString()));
    }

    private void addWebForm(final AbstractAddFormViewWithCustomForm addForm, final HTML description, final WfmButton2 saveButton, final TextBox antibot) {
        container.add(addForm);
        saveButton.addClickHandler(event -> {
            if ((webForm.getUseCatpcha() && antibot != null && antibot.getText() != null && !"".equals(antibot.getText())) || !webForm.getUseCatpcha()) {
                setEnabled(false, saveButton);
                addForm.save(antibot != null ? antibot.getText() : "");
            } else if (webForm.getUseCatpcha()) {
                antibot.setStyleName("x-form-invalid");
                antibot.addFocusHandler(event1 -> antibot.removeStyleName("x-form-invalid"));
                Info.show("The characters that you entered didn't match the word verification. Please try again!", Info.Type.WARNING);
            }
        });
        addForm.setAddedSuccessfully(() -> showConfirmationThanRedirect(description));
        addForm.setErrorOn(() -> {
            if (webForm.getUseCatpcha()) {
                antibot.setText("");
                addCaptchaImage();
            }
            setEnabled(true, saveButton);
        });
    }

    private void addWebForm(final AbstractAddFormView addForm, final HTML description, final WfmButton2 saveButton, final TextBox antibot) {
        container.add(addForm);
        saveButton.addClickHandler(event -> {
            if ((webForm.getUseCatpcha() && antibot != null && antibot.getText() != null && !"".equals(antibot.getText())) || !webForm.getUseCatpcha()) {
                setEnabled(false, saveButton);
                addForm.save(antibot != null ? antibot.getText() : "");
            } else if (webForm.getUseCatpcha()) {
                antibot.setStyleName("x-form-invalid");
                antibot.addFocusHandler(event1 -> antibot.removeStyleName("x-form-invalid"));
                Info.show("The characters that you entered didn't match the word verification. Please try again!", Info.Type.WARNING);
            }
        });
        addForm.setAddedSuccessfully(() -> showConfirmationThanRedirect(description));
        addForm.setErrorOn(() -> {
            if (webForm.getUseCatpcha()) {
                antibot.setText("");
                addCaptchaImage();
            }
            setEnabled(true, saveButton);
        });
    }

    boolean redirectCancelled;

    private void showConfirmationThanRedirect(final HTML description) {
        container.clear();
        description.setHTML(webForm.getConfirmationMessage() == null ? "" : webForm.getConfirmationMessage());
        container.add(description);
        SimpleLink button = new SimpleLink(wfmStrings.back());
        button.addClickHandler(event -> {
            redirectCancelled = true;
            container.clear();
            Utils.reloadPage();
        });
        container.add(button);
        container.setCellHorizontalAlignment(description, HasHorizontalAlignment.ALIGN_CENTER);
        container.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
        if (webForm.getRedirectURL() != null && !"".equals(webForm.getRedirectURL())) {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    if (!redirectCancelled) {
                        redirectParent(webForm.getRedirectURL());
                    }
                }
            };
            timer.schedule(5000);
        }
    }

    native void redirectParent(String url)/*-{
        $wnd.parentRedirect(url);
    }-*/;

    private void setEnabled(boolean b, final WfmButton2... buttons) {
        if (buttons != null && buttons.length > 0) {
            for (final WfmButton2 button : buttons) {
                button.setEnabled(b);
            }
        }
    }

    public void show() {
        if (addCustomFormView != null) {
            addCustomFormView.show();
            addCustomFormView.onReadyToReplaceFields();
        }
    }
}