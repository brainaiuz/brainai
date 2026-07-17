/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/24 6:20:25                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;


/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 10, 2009
 * Time: 5:16:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebFormSummaryView extends CustomForm2 implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private final Integer objectId;
    CRMServiceAsync crmService = CRMService.App.get();

    private HTML webFormType;
    private HTML webFormTitle;
    private HTML webFormDescription;
    private HTML webFormConfirmationMessage;
    private HTML webFormEmail;
    private HTML redirectURL;
    private HTML webFormFormUrl;
    private HTML webFormCaptcha;
    private HTML webFormEmbedCode;
    private HTML webFormFormPreview;
    private HTML webFormCaptchaLabel;
    private HTML webFormCaptchaDescription;

    public WebFormSummaryView(Integer objectId) {
        super("summary", crmStrings.webFormView());
        this.objectId = objectId;
    }

    private WebForm item;

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        return Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_WEB_FORM)) {
            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(event -> deleteWebFormItem(item));
            options.add(delete);
        }
        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_WEB_FORM)) {
            addEditButton().addClickHandler(event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("webformedit|editwebform/" + item.getObjectId(), item.getTitle());
            });
        }
    }

    private void deleteWebFormItem(WebForm item) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.sureYouWantToDelete() );
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                crmService.deleteWebForm(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable caught) {
                        Info.show(wfmStrings.error(), Info.Type.WARNING);
                    }

                    public void success(Void result) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), crmStrings.crmForm()), Info.Type.INFO);
                        closeTab();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WEB_FORM_DELETE, result, WebFormSummaryView.this);
                    }
                });
            }
        });
        messageBox.open();
    }

    @Override
    protected void getDataToFillFields() {
        crmService.getWebForm(objectId, new AbstractAsyncCallback<WebForm>() {
            public void failure(Throwable throwable) {
                throwable.getMessage();
            }

            public void success(WebForm item) {
                setWebForm(item);
                setData();
                addListeners();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CRM_WEB_FORM_FOR_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    private void setWebForm(WebForm item) {
        if (this.item == null || item.getObjectId().equals(this.item.getObjectId())) {
            this.item = item;
        }
    }

    private void addListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WEB_FORM_ADD_EDIT, WebFormSummaryView.this, (sender, args) -> {
            if (args != null && item != null && args instanceof WebForm && ((WebForm) args).getObjectId().equals(item.getObjectId())) {
                clear();
                LoadingPanel.loading(true);
                crmService.getWebForm(objectId, new AbstractAsyncCallback<WebForm>() {
                    public void failure(Throwable throwable) {
                        throwable.getMessage();
                        LoadingPanel.loading(false);
                    }

                    public void success(WebForm item) {
                        LoadingPanel.loading(false);
                        setWebForm(item);
                        setData();
                    }
                });
            }
        });
    }

    @Override
    protected void registerFields() {
        webFormType = new HTML();
        webFormTitle = new HTML();
        webFormDescription = new HTML();
        webFormConfirmationMessage = new HTML();
        webFormEmail = new HTML();
        redirectURL = new HTML();
        webFormFormUrl = new HTML();
        webFormCaptcha = new HTML();
        webFormEmbedCode = new HTML();
        webFormFormPreview = new HTML();
        webFormCaptchaLabel = new HTML();
        webFormCaptchaDescription = new HTML();


        addTitleField(WEBFORM.CONFIGURE_WEB_FORM_TITLE, crmStrings.configureWebForm());
        addTitleField(WEBFORM.CONFIGURE_WEB_FIELDS_TITLE, crmStrings.configureWebFields());

        addField(WEBFORM.FORM_TYPE, webFormType);
        addField(WEBFORM.TITLE, webFormTitle);
        addField(WEBFORM.DESCRIPTION, webFormDescription);
        addField(WEBFORM.CONFIRMATION_MESSAGE, webFormConfirmationMessage);
        addField(WEBFORM.NOTIFICATION_EMAIL_ADDRESS, webFormEmail);
        addField(WEBFORM.REDIRECT, redirectURL);
        addField(WEBFORM.FORM_URL, webFormFormUrl);
        addField(WEBFORM.CAPTCHA, webFormCaptcha);
        addField(WEBFORM.CAPTCHA_LABEL, webFormCaptchaLabel);
        addField(WEBFORM.CAPTCHA_DESCRIPTION, webFormCaptchaDescription);

        addField(WEBFORM.IFRAME_CODE, webFormEmbedCode);
        addField(WEBFORM.FORM_PREVIEW, webFormFormPreview);

        show();

    }


    private void setData() {
        webFormType.setHTML(item.getWebFormTypeName());
        webFormTitle.setHTML(item.getTitle());
        webFormDescription.setHTML(item.getDescription());
        webFormConfirmationMessage.setHTML(item.getConfirmationMessage());
        webFormEmail.setHTML(item.getEmailAddress());
        redirectURL.setHTML(item.getRedirectURL());
        webFormFormUrl.setHTML("<a href=\"" + Utils.getHostURL() + "WebForms.html?link=" + item.getiFrameUrl() + "\" target=\"_blank\">" + Utils.getHostURL() + "WebForms.html?link=" + item.getiFrameUrl() + "</a>");
        webFormCaptcha.setHTML(item.getUseCatpcha() ? "Yes" : "No");
        webFormEmbedCode.setHTML("&lt;iframe src='" + Utils.getHostURL() + "WebForms.html?link=" + item.getiFrameUrl() + "' width='550' height='700' /&gt;");
//        webFormFormPreview.setHTML("<iframe src='" + Utils.getHostURL() + "WebForms.html?link=" + item.getiFrameUrl() + "' width='550' height='700' /></iframe>");
        webFormCaptchaLabel.setHTML(item.getCaptchaLabel());
        webFormCaptchaDescription.setHTML(item.getCaptchaDescription());
    }

    public String getIconStyle() {
        return "task task-list";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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