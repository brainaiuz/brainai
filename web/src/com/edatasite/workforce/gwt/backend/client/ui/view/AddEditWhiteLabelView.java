package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.constants.DynamicLoginConstants;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddEditWhiteLabelView extends CustomForm implements Colapse, Constants {

    private static final BackendServiceAsync service = BackendService.App.get();

    private final String hostName;
    private TextBox hostNameBox;
    private TextBox productName;
    private TextBox logoUrl;
    private TextBox android;
    private TextBox ios;
    private TextBox description;
    private TextBox freeTrialDays;
    private TextBox email;
    private TextBox openAiToken;
    private TextBox website;
    private TextBox scheduleDemoUrl;
    private TextBox phoneNumber;
    private CheckBox showScheduleDemo;
    private CheckBox showPhoneNumber;
    private CheckBox showWiki;
    private CheckBox showAppLinks;
    private GeneralFileUpload uploadForm;
    private GeneralFileUpload favIcon;
    private Integer objectId;

    private WfmButton2 save;


    private DynamicLogin item;

    public AddEditWhiteLabelView(String hostName, Integer id) {
        super("whiteLabelView", "hostName");
        this.hostName = hostName;
        this.objectId = id;
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

        hostNameBox = new TextBox();
        hostNameBox.addStyleName(DEFAULT_WIDTH);

        productName = new TextBox();
        productName.addStyleName(DEFAULT_WIDTH);

        logoUrl = new TextBox();
        logoUrl.addStyleName(DEFAULT_WIDTH);

        email = new TextBox();
        email.addStyleName(DEFAULT_WIDTH);

        openAiToken = new TextBox();
        openAiToken.addStyleName(DEFAULT_WIDTH);

        android = new TextBox();
        android.addStyleName(DEFAULT_WIDTH);

        ios = new TextBox();
        ios.addStyleName(DEFAULT_WIDTH);

        description = new TextBox();
        description.addStyleName(DEFAULT_WIDTH);

        freeTrialDays = new TextBox();
        freeTrialDays.addStyleName(DEFAULT_WIDTH);

        website = new TextBox();
        website.addStyleName(DEFAULT_WIDTH);

        scheduleDemoUrl = new TextBox();
        scheduleDemoUrl.addStyleName(DEFAULT_WIDTH);

        phoneNumber = new TextBox();
        phoneNumber.addStyleName(DEFAULT_WIDTH);

        showScheduleDemo = new CheckBox();
        showPhoneNumber = new CheckBox();
        showWiki = new CheckBox();
        showAppLinks = new CheckBox();


        uploadForm = new GeneralFileUpload(F_WHITE_LABEL_LOGO, objectId, objectId);

        new KpiToolTip(
                uploadForm,
                "<html>" +
                        "<b>Accepted Formats:</b><br>" +
                        "• Only JPEG (JPG) and PNG formats are allowed for image uploads.<br>" +
                        "• Please avoid uploading other file formats, as they may not display correctly.<br><br>" +

                        "<b>Maximum File Size:</b><br>" +
                        "• Image size must not exceed 100 KB.<br>" +
                        "• Larger images may slow down page loading and affect user experience." +
                        "</html>"
        );
        favIcon = new GeneralFileUpload(F_WHITE_LABEL_FAVICON, objectId, objectId);


        addTitleField(DynamicLoginConstants.DETAILS, wfmStrings.information());
        addField(DynamicLoginConstants.OPENAI, openAiToken, "OpenAi Token");
        addField(DynamicLoginConstants.HOST_NAME, hostNameBox, "hostName");
        addField(DynamicLoginConstants.PRODUCT_NAME, productName, wfmStrings.product() + wfmStrings.name());
        addField(DynamicLoginConstants.LOGO_URL, uploadForm, wfmStrings.logos(), true,true);
        addField(DynamicLoginConstants.FAVICON_URL, favIcon, "favIcon");
        addField(DynamicLoginConstants.EMAIL, email, wfmStrings.email());
        addField(DynamicLoginConstants.ANDROID, android, wfmStrings.android());
        addField(DynamicLoginConstants.IOS, ios, "Iphone");
        addField(DynamicLoginConstants.WEBSITE, website, wfmStrings.website());
        addField(DynamicLoginConstants.DESCRIPTION, description, wfmStrings.description());
        addField(DynamicLoginConstants.FREETRIALDAYS, freeTrialDays, "Free Trial Days");
        addField(DynamicLoginConstants.SCHEDULE_DEMO_URL, scheduleDemoUrl, "Schedule Demo Link");
        addField(DynamicLoginConstants.PHONE_NUMBER, phoneNumber, "Phone Number");
        addField(DynamicLoginConstants.SHOW_SCHEDULE_DEMO, showScheduleDemo, "Show Schedule Demo");
        addField(DynamicLoginConstants.SHOW_PHONE_NUMBER, showPhoneNumber, "Show Phone Number");
        addField(DynamicLoginConstants.SHOW_WIKI, showWiki, "Show Wiki");
        addField(DynamicLoginConstants.SHOW_APP_LINKS, showAppLinks, "Show App Links");


        show();
    }


    @Override
    protected void getDataToFillFields() {
        if (getFormType().equals(LayoutRPC.EDIT)) {
            service.getWhiteLabelItem(hostName, new AsyncCallback<DynamicLogin>() {
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
        }
    }


    private void fillFields() {
        if (item == null) {
            item = new DynamicLogin();
        }
        hostNameBox.setText(item.getHostname());
        productName.setText(item.getProductName());
        logoUrl.setText(item.getLogoUrl());
        email.setText(item.getEmail());
        openAiToken.setValue(item.getOpenAiToken());
        website.setText(item.getWebsite());
        android.setText(item.getAndroid());
        ios.setText(item.getIos());
        description.setText(item.getDescription());
        freeTrialDays.setText(item.getFreeTrialDays());
        scheduleDemoUrl.setText(item.getScheduleDemoUrl());
        phoneNumber.setText(item.getPhoneNumber());
        showScheduleDemo.setValue(item.getShowScheduleDemo() == null || item.getShowScheduleDemo());
        showPhoneNumber.setValue(item.getShowPhoneNumber() == null || item.getShowPhoneNumber());
        showWiki.setValue(item.getShowWiki() == null || item.getShowWiki());
        showAppLinks.setValue(item.getShowAppLinks() == null || item.getShowAppLinks());

    }

    private void save() {
        if (!validate()) {
            return;
        }
        setValues();
        service.saveWhiteLabelItems(item, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                item = null;
                LoadingPanel.loading(false);
                closeTab();
                Info.show(wfmStrings.messSuccessfullySaved(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WHITE_LABEL_ADD_EDIT, item, AddEditWhiteLabelView.this);
            }
        });
    }

    private boolean validate() {
        boolean isValid = true;
        if (!isValid) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return isValid;
    }

    private void setValues() {
        item = item == null ? new DynamicLogin() : item;
        item.setProductName(productName.getValue());
        item.setLogoUrl(logoUrl.getValue());
        item.setHostname(hostName);
        item.setEmail(email.getValue());
        item.setOpenAiToken(openAiToken.getValue());
        item.setWebsite(website.getValue());
        item.setAndroid(android.getValue());
        item.setIos(ios.getValue());
        item.setDescription(description.getValue());
        item.setFreeTrialDays(freeTrialDays.getValue());
        item.setScheduleDemoUrl(scheduleDemoUrl.getValue());
        item.setPhoneNumber(phoneNumber.getValue());
        item.setShowScheduleDemo(showScheduleDemo.getValue());
        item.setShowPhoneNumber(showPhoneNumber.getValue());
        item.setShowWiki(showWiki.getValue());
        item.setShowAppLinks(showAppLinks.getValue());
        if (uploadForm != null) {
            item.setAttachments(uploadForm.getAttachedFiles());
        }
        if (favIcon != null) {
            item.setFavIcon(favIcon.getAttachedFiles());
        }
    }

    @Override
    public String getPropertyCode() {
        return "whiteLabelView";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WHITE_LABEL_FORM;
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
