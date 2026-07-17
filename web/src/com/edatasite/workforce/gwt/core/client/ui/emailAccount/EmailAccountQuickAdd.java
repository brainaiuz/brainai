package com.edatasite.workforce.gwt.core.client.ui.emailAccount;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmailHostLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

/**
 * Author: Azazello
 * Date: 3/15/2018
 * Time: 3:52 PM
 */
public class EmailAccountQuickAdd extends KpiSideNavBox implements Constants {
    interface EmailAccountQuickAddUiBinder extends UiBinder<HTMLPanel, EmailAccountQuickAdd> {
    }

    private static final EmailAccountQuickAddUiBinder ourUiBinder = GWT.create(EmailAccountQuickAddUiBinder.class);
    protected static MessageCenterServiceAsync service = MessageCenterService.App.get();


    @UiField
    HTMLPanel panel;

    @UiField
    protected Div gmailPanel;
    @UiField
    protected Span gmailIcon;
    @UiField
    protected Span gmailText;

    @UiField
    protected Div outlookPanel;
    @UiField
    protected Span outlookIcon;
    @UiField
    protected Span outlookText;

    @UiField
    protected Div defaultPanel;
    @UiField
    protected Span defaultIcon;
    @UiField
    protected Span defaultText;

    @UiField
    protected HTMLPanel defaultDetailPanel;
    @UiField
    protected Label emailLabel;
    @UiField
    protected TextBox email;
    @UiField
    protected Label passwordLabel;
    @UiField
    protected PasswordTextBox password;

    @UiField
    protected Label loginLabel;
    @UiField
    protected TextBox login;
    @UiField
    protected Label fromNameLabel;
    @UiField
    protected TextBox fromName;

    @UiField
    protected Label imapHostLabel;

    @UiField
    protected Label imapPortLabel;
    @UiField
    protected TextBox imapPort;

    @UiField
    protected Label smtpHostLabel;

    @UiField
    protected Label smtpPortLabel;
    @UiField
    protected TextBox smtpPort;
    @UiField
    protected KpiSwitcher performSmtp;
    @UiField
    protected WfmButton2 testConBtn;
    @UiField
    protected KpiSwitcher copySent;

    private final boolean fromGettingStarted;
    @UiField
    protected EmailHostLookUp imapServerHost;

    protected Heading header;

    boolean openPanel = false;
    protected EmailAccountItem item;
    private WfmButton2 save, markAsDone;
    @UiField
    protected EmailHostLookUp smptServerHost;
    protected String providerType;
    private String token;

    public EmailAccountQuickAdd() {
        this(false);
    }

    public EmailAccountQuickAdd(boolean fromGettingStarted) {
        super();
        this.fromGettingStarted = fromGettingStarted;
        this.providerType = providerType;
        bind();
        initialize();
        initEvents();
        show();
    }

    protected void bind() {
        ourUiBinder.createAndBindUi(this);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_ACCOUNT_SUCCESS, (sender, args) -> onEmailAccountEvent((String) args));
    }

    protected void initialize() {
        //header
        header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.addEmailAccount());
        addHeader(header);

        //Google
        gmailIcon.add(new Image("../mainStyles/new-ui/icons/logo_gmail.svg"));
        gmailText.setText("Connect Google account");

        //Outlook
        outlookIcon.add(new Image("../mainStyles/new-ui/icons/logo_outlook.svg"));
        outlookText.setText("Connect Outlook account");

        //Default
        defaultIcon.add(new SvgIcon(SvgEnum.settings));
        defaultText.setText("Use custom provider");
        defaultPanel.addClickHandler(clickEvent -> togglePanel());

        emailLabel.setText(wfmStrings.emailAddress());
        loginLabel.setText(wfmStrings.username());
        passwordLabel.setText(wfmStrings.password());
        fromNameLabel.setText(wfmStrings.fromName());
        imapHostLabel.setText(wfmStrings.incomingServerHost());
        imapPortLabel.setText("Port");
        smtpHostLabel.setText(wfmStrings.outgoingServerHost());
        smtpPortLabel.setText("Port");
        performSmtp.setOnLabel(wfmStrings.smtpConnectionNotUsesAuth());
        copySent.setOnLabel(wfmStrings.saveCopyToSentFolder());

        Validation.addNumericKeyboardListener(imapPort);
        imapPort.setText("993");
        Validation.addNumericKeyboardListener(smtpPort);
        smtpPort.setText("465");

        performSmtp.setValue(true);


        ListingFilterParameter income=new ListingFilterParameter();
        income.setCategory("income");

        service.getImapHostAndSmptHost(income, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                try {
                    imapServerHost.setItems("income",result);
                } catch (Exception e) {
                    e.printStackTrace();
                }            }
        });


        ListingFilterParameter outcome=new ListingFilterParameter();
        outcome.setCategory("outcome");

        service.getImapHostAndSmptHost(outcome, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                try {
                    smptServerHost.setItems("outcome",result);
                } catch (Exception e) {
                    e.printStackTrace();
                }            }
        });


        testConBtn.setText(wfmStrings.testConnection());
        testConBtn.addClickHandler(event -> {
            if (validateForm()) {
                test();
            }
        });

        addBody(panel);

        initButtons();
    }

    protected void initEvents() {
        gmailPanel.addClickHandler(clickEvent -> GoogleAuthorizationPanel.redirectToGoogleMailAuthPage());
//        outlookPanel.addClickHandler(clickEvent -> GoogleAuthorizationPanel.redirectToGoogleMailAuthPage());
    }

    protected void initButtons() {
        //footer
        markAsDone = new WfmButton2(wfmStrings.markAsDone(), WfmButton2.BTN_SUCCESS);
        markAsDone.addClickHandler(event -> command.execute());
        if (fromGettingStarted) {
            addFooter(markAsDone);
        }

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            enableButtons(false);
            if (validateForm()) {
                save();
            } else {
                enableButtons(true);
            }
        });
        addFooter(save);
    }

    private void onEmailAccountEvent(String data) {
        GoogleAuthorizationPanel.closePopupWindow();
        EmailItem emailItem = JsonUtils.safeEval(data);
        item = new EmailAccountItem();
        item.setEmail(emailItem.getEmail());
        item.setProvider(emailItem.getProvider());
        item.setRefreshToken(emailItem.getRefreshToken());
        fillFieldsWithData();
    }

    protected void fillFieldsWithData() {
        providerType = item.getProvider();

        if (Constants.GOOGLE.equals(providerType)) {
            gmailText.setText(item.getEmail());
            token = item.getRefreshToken();
        } else if (Constants.OFFICE_365.equals(providerType)) {
            outlookText.setText(item.getEmail());
            token = item.getRefreshToken();
        } else {
            email.setText(item.getEmail());
            fromName.setText(item.getFromName());
            login.setText(item.getUserName());
            password.setText(item.getPassword());

            SelectItem iMapHost = new SelectItem(1, item.getImapHost());
            imapServerHost.setSelected(iMapHost);

            if (item.getImapPort() != null) {
                imapPort.setText(item.getImapPort().toString());
            }

            SelectItem smtpHost = new SelectItem(1, item.getImapHost());
            smptServerHost.setSelected(smtpHost);

            if (item.getSmtpPort() != null) {
                smtpPort.setText(item.getSmtpPort().toString());
            }
            performSmtp.setValue(item.isSmtpAuth());
            copySent.setValue(item.isSaveCopyToSentFolder());
        }
    }

    public boolean validateForm() {
        if (providerType == null) {
            Info.warn("No provider was selected");
            return false;
        }

        int errors = 0;
        if (Constants.GOOGLE.equals(providerType)) {
            if (!Validation.validEmailFormat(gmailText.getText(), false)) {
                errors++;
            }
        } else if (Constants.OFFICE_365.equals(providerType)) {
            if (!Validation.validEmailFormat(outlookText.getText(), false)) {
                errors++;
            }
        } else {
            if (!Validation.validateTextBoxRequired(email)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(login)) {
                errors++;
            }
            if (!Validation.validateTextBoxRequired(password)) {
                errors++;
            }
//            if (!Validation.validateLookUpRequired(imapServerHost)) {
//                errors++;
//            }
            if (!Validation.validateTextBoxRequired(imapPort)) {
                errors++;
            }
//            if (!Validation.validateLookUpRequired(smptServerHost)) {
//                errors++;
//            }
            if (!Validation.validateTextBoxRequired(smtpPort)) {
                errors++;
            }
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    protected void setValues() {
        item = new EmailAccountItem();
        item.setProvider(providerType);
        item.setActive(true);
        if (Constants.GOOGLE.equals(providerType)) {
            item.setEmail(gmailText.getText());
            item.setRefreshToken(token);
        } else if (Constants.OFFICE_365.equals(providerType)) {
            item.setEmail(outlookText.getText());
            item.setRefreshToken(token);
        } else {
            item.setEmail(email.getText());
            item.setUserName(login.getText());
            item.setFromName(fromName.getText());
            item.setPassword(password.getText());
            item.setImapHost(imapServerHost.getText());
            item.setImapPort(Integer.valueOf(imapPort.getText()));
            item.setSmtpHost(smptServerHost.getText());
            item.setSmtpPort(Integer.valueOf(smtpPort.getText()));
            item.setSmtpAuth(performSmtp.getValue());
            item.setSaveCopyToSentFolder(copySent.getValue());
        }
    }

    public void test() {
        enableButtons(false);
        setValues();
        LoadingPanel.loading(true, panel);
        service.testConnection(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                enableButtons(true);
                LoadingPanel.loading(false, panel);
                Info.warn(wfmStrings.someErrorsOccurredWhileTesting());
            }

            @Override
            public void success(Integer result) {
                enableButtons(true);
                LoadingPanel.loading(false, panel);
                if (result == 0) {
                    Info.show(wfmStrings.yourEmailAccountSuccessfullyConfigured());
                } else {
                    Info.warn(getConnectionErrorAsMessage(result), result == EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER ? 15000 : Info.DURATION);
                }
            }
        });
    }

    public void save() {
        enableButtons(false);

        setValues();
        LoadingPanel.loading(true, panel);
        if (!Constants.DEFAULTT.equals(providerType)) {
            saveAccount();
        } else {
            service.testConnection(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false, panel);
                    enableButtons(true);
                    Info.warn(wfmStrings.someErrorsOccurredWhileTesting());
                }

                @Override
                public void success(Integer result) {
                    if (result != null) {
                        if (result == 0) {
                            saveAccount();
                        } else {
                            LoadingPanel.loading(false, panel);
                            enableButtons(true);
                            Info.warn(getConnectionErrorAsMessage(result));
                        }
                    } else {
                        LoadingPanel.loading(false, panel);
                        enableButtons(true);
                    }
                }
            });
        }
    }

    private void saveAccount() {
        service.saveEmailAccount(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false, panel);
                enableButtons(true);
                Info.warn(wfmStrings.errorOccurredSavingChanges());
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                if (result > 0) {
                    enableButtons(true);
                    Info.warn(wfmStrings.youCanHaveOnlyOneEmail());
                } else {
                    if (fromGettingStarted) {
                        markAsDone.setVisible(true);
                        save.setVisible(false);
                    } else {
                        remove();
                    }
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.emailAccounts()));
                }
            }
        });
    }

    private void togglePanel() {
        if (openPanel) {
            providerType = null;
            defaultDetailPanel.removeStyleName("fade--in");
            defaultDetailPanel.addStyleName("fade--out");
            openPanel = false;
        } else {
            providerType = Constants.DEFAULTT;
            defaultDetailPanel.removeStyleName("fade--out");
            defaultDetailPanel.addStyleName("fade--in");
            openPanel = true;
        }
    }

    private String getConnectionErrorAsMessage(int errorCode) {
        if (EmailAccountItem.ERROR_CREDENTIAL == errorCode) {
            return wfmStrings.canNotConnectToMail();
        } else if (EmailAccountItem.ERROR_COULDNOTCONNECT == errorCode || EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION == errorCode || EmailAccountItem.ERROR_CONNECTIONTIMEDOUT == errorCode) {
            return wfmStrings.couldNotConnectToTheServer();
        } else if (EmailAccountItem.ERROR_CONNECTIONREFUSED == errorCode) {
            return wfmStrings.connectionRefused();
        } else if (EmailAccountItem.ERROR_CANTSENDEMAIL == errorCode) {
            return wfmStrings.couldNotConnectToTheSmtpServer();
        }
        if (EmailAccountItem.SETUP_CORPORATE_EMAIL_NOT_SETUP_USER_EMAIL == errorCode) {
            return wfmStrings.enteredIsAlreadyUsedCorporateEmail();
        } else if (EmailAccountItem.SETUP_USER_EMAIL_NOT_SETUP_CORPORATE_EMAIL == errorCode) {
            return wfmStrings.enteredIsAlreadyUsedUserEmail();
        }
        if (errorCode > EmailAccountItem.ERROR_SMTP_SERVER && errorCode != EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER) {
            errorCode = errorCode - EmailAccountItem.ERROR_SMTP_SERVER;
            if (EmailAccountItem.ERROR_CREDENTIAL == errorCode) {
                return wfmStrings.canNotConnectToMailSMTP();
            } else if (EmailAccountItem.ERROR_COULDNOTCONNECT == errorCode || EmailAccountItem.ERROR_UNKNOWNHOSTEXCEPTION == errorCode || EmailAccountItem.ERROR_CONNECTIONTIMEDOUT == errorCode) {
                return wfmStrings.couldNotConnectToTheSmtpServer();
            } else if (EmailAccountItem.ERROR_CONNECTIONREFUSED == errorCode) {
                return wfmStrings.connectionRefusedSMTP();
            }
        }
        if (errorCode == EmailAccountItem.ERROR_CREDENTIAL_CHECK_BROWSER) {
            return wfmStrings.loginToGmail();
        }
        return wfmStrings.someErrorsOccurred();
    }

    private void enableButtons(boolean enable) {
        markAsDone.setEnabled(enable);
        save.setEnabled(enable);
    }

    final static class EmailItem extends JavaScriptObject {

        protected EmailItem(){

        }

        public native String getEmail() /*-{
            return this.email;
        }-*/;

        public native String getProvider() /*-{
            return this.provider;
        }-*/;

        public native String getRefreshToken() /*-{
            return this.refreshToken;
        }-*/;
    }
}
