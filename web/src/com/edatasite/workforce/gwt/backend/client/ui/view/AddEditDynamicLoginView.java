package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.constants.DynamicLoginConstants;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddEditDynamicLoginView extends CustomForm implements Colapse, Constants {

    private static final BackendServiceAsync service = BackendService.App.get();

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    private final String hostName;
    private DataListBox hostNameList;
    private TextBox hostBox;
    private KpiSwitcher logoEnable;
    private TextBox logoUrl;
    private KpiSwitcher descriptionEnable;
    private TextArea description;
    private KpiSwitcher faviconEnable;
    private TextBox faviconUrl;
    private KpiSwitcher socialLoginEnable;
    private KpiSwitcher forgotPasswordEnable;
    private KpiSwitcher signupEnable;
    private WfmButton2 save;


    private DynamicLogin item;

    public AddEditDynamicLoginView(String hostName) {
        super("dynamicLoginView", "Dynamic Login ADD EDIT");
        this.hostName = hostName;
    }

    @Override
    protected void addButtons() {
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(save);
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> closeTab()));
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        hostBox = new TextBox();
        hostBox.setReadOnly(true);

        hostNameList = new DataListBox();
        hostNameList.addStyleName(DEFAULT_WIDTH);

        logoEnable = new KpiSwitcher();
        logoEnable.setValue(false);

        logoUrl = new TextBox();
        logoUrl.addStyleName(DEFAULT_WIDTH);

        descriptionEnable = new KpiSwitcher();
        descriptionEnable.setValue(false);

        description = new TextArea();
        description.addStyleName(DEFAULT_WIDTH);

        faviconEnable = new KpiSwitcher();
        faviconEnable.setValue(false);

        faviconUrl = new TextBox();
        faviconUrl.addStyleName(DEFAULT_WIDTH);

        socialLoginEnable = new KpiSwitcher();
        socialLoginEnable.setValue(false);

        forgotPasswordEnable = new KpiSwitcher();
        forgotPasswordEnable.setValue(false);

        signupEnable = new KpiSwitcher();
        signupEnable.setValue(false);


        addTitleField(DynamicLoginConstants.DETAILS, wfmStrings.information());

        addField(DynamicLoginConstants.HOST_NAME, hostName == null ? hostNameList : hostBox, getTitle(backendStrings.hostName(), true));
        addField(DynamicLoginConstants.LOGO_ENABLE, logoEnable, getTitle(wfmStrings.companyLogo()));
        addField(DynamicLoginConstants.LOGO_URL, logoUrl, getTitle(wfmStrings.companyLogo() + " " + wfmStrings.enable()));
        addField(DynamicLoginConstants.DESCRIPTION_ENABLE, descriptionEnable, getTitle(wfmStrings.description()));
        addField(DynamicLoginConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(DynamicLoginConstants.FAVICON_ENABLE, faviconEnable, getTitle("Favicon"));
        addField(DynamicLoginConstants.SOCIAL_LOGIN_ENABLE, socialLoginEnable, getTitle(wfmStrings.socialLogin()));
        addField(DynamicLoginConstants.FAVICON_URL, faviconUrl, getTitle("Favicon Url"));
        addField(DynamicLoginConstants.FORGOT_PASSWORD_ENABLE, forgotPasswordEnable, getTitle(wfmStrings.forgotPassword()));
        addField(DynamicLoginConstants.SIGNUP_ENABLE, signupEnable, getTitle(backendStrings.registration()));


        show();
    }

    @Override
    protected void getDataToFillFields() {
        if (getFormType().equals(LayoutRPC.EDIT)) {
            service.getDynamicLoginItem(hostName, new AsyncCallback<DynamicLogin>() {
                @Override
                public void onFailure(Throwable caught) {
                    item = null;
                }

                @Override
                public void onSuccess(DynamicLogin result) {
                    item = result;
                    fillFields();
                }
            });
        } else {
            service.getHosts(new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    item = null;
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    setItems(result);
                }
            });
        }
    }

    private void setItems(SelectItem[] result) {
        hostNameList.setItems(result);
    }

    private void fillFields() {
        if (item == null) {
            item = new DynamicLogin();
        }
        hostBox.setText(hostName);
        logoEnable.setValue(item.getLogoEnable());
        logoUrl.setText(item.getLogoUrl());
        descriptionEnable.setValue(item.getDescriptionEnable());
        description.setText(item.getDescription());
        faviconEnable.setValue(item.getFaviconEnable());
        faviconUrl.setText(item.getFaviconUrl());
        socialLoginEnable.setValue(item.getSocialLoginEnable());
        forgotPasswordEnable.setValue(item.getForgotPasswordEnable());
        signupEnable.setValue(item.getSignUpEnable());

    }

    private void save() {
        if (!validate()) {
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        service.saveDynamicLogin(item, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(String hostname) {
                item = null;
                LoadingPanel.loading(false);
                closeTab();
                Info.show("Dynamic Login is Added", Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DYNAMIC_LOGIN_ADD_EDIT, item, AddEditDynamicLoginView.this);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (hostName == null && !Validation.validateListBoxRequired(hostNameList, new HTML(), "")) {
            isValid = false;
            hostNameList.addStyleName(ERROR_FORM_STYLE);
        }
        if (!isValid) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return isValid;
    }

    private void setValues() {
        item = item == null ? new DynamicLogin() : item;
        item.setHostname(hostName == null ? hostNameList.getSelectedItemText() : hostName);
        item.setLogoEnable(logoEnable.getValue());
        item.setLogoUrl(logoUrl.getValue());
        item.setDescriptionEnable(descriptionEnable.getValue());
        item.setDescription(description.getText());
        item.setFaviconEnable(faviconEnable.getValue());
        item.setFaviconUrl(faviconUrl.getValue());
        item.setSocialLoginEnable(socialLoginEnable.getValue());
        item.setForgotPasswordEnable(forgotPasswordEnable.getValue());
        item.setSignUpEnable(signupEnable.getValue());
    }

    @Override
    public String getPropertyCode() {
        return "dynamicLoginView";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.DYNAMIC_LOGIN_FORM;
    }

    @Override
    protected String getFormType() {
        return hostName != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
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
}
