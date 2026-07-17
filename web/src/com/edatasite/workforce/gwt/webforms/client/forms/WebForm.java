package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 5:15:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebForm implements IsSerializable, WebFormConstants {
    public static final String TYPE = "webFormType";
    public static final String TITLE = "webFormTitle";
    public static final String URL = "IFrameUrl";
    public static final String NUMBER_OF_FIELDS = "numberOfFields";
    private String title;
    private String description;
    private String confirmationMessage;
    private String redirectURL;
    private String emailAddress;
    private WebField[] webFields;
    private SelectItem webFormType;
    private Integer objectId;
    private Integer webFieldsCount;
    private SelectItem[] formTypes;
    private String iFrameUrl;
    private Integer companyID;
    private String buttonText;
    private boolean useCatpcha;
    private String captchaLabel;
    private String captchaDescription;
    private String captchaCantRead;
    private String captchaTryAnother;
    private boolean sendAutoResponse = false;
    private SelectItem[] emailTemplates;
    private Integer emailTemplateID;
    public LinkedHashMap<String, String> userSettings = new LinkedHashMap<>();
    private LayoutRPC customForm;
    private String customCss;
    private ReferenceItem webFormSource;
    private ArrayList<CompanyCustomFieldItem> customFields;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public WebField[] getWebFields() {
        return webFields;
    }

    public void setWebFields(WebField[] webFields) {
        this.webFields = webFields;
    }

    public SelectItem getWebFormType() {
        return webFormType;
    }

    public String getWebFormType(boolean asString) {
        return webFormType != null ? (webFormType instanceof ReferenceItem ? webFormType.getCode() : webFormType.getDescription()) : null;
    }

    public String getWebFormTypeName() {
        return webFormType != null ? webFormType.getName() : null;
    }

    public void setWebFormType(SelectItem webFormType) {
        this.webFormType = webFormType;
    }

    public Integer getWebFieldsCount() {
        return webFieldsCount;
    }

    public void setWebFieldsCount(Integer webFieldsCount) {
        this.webFieldsCount = webFieldsCount;
    }

    public SelectItem[] getFormTypes() {
        return formTypes;
    }

    public void setFormTypes(SelectItem[] formTypes) {
        this.formTypes = formTypes;
    }

    public String getiFrameUrl() {
        return iFrameUrl;
    }

    public void setiFrameUrl(String iFrameUrl) {
        this.iFrameUrl = iFrameUrl;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public boolean getUseCatpcha() {
        return useCatpcha;
    }

    public void setUseCatpcha(boolean useCatpcha) {
        this.useCatpcha = useCatpcha;
    }

    public String getCaptchaLabel() {
        return captchaLabel;
    }

    public void setCaptchaLabel(String captchaLabel) {
        this.captchaLabel = captchaLabel;
    }

    public String getCaptchaDescription() {
        return captchaDescription;
    }

    public void setCaptchaDescription(String captchaDescription) {
        this.captchaDescription = captchaDescription;
    }

    public String getCaptchaCantRead() {
        return captchaCantRead;
    }

    public void setCaptchaCantRead(String captchaCantRead) {
        this.captchaCantRead = captchaCantRead;
    }

    public String getCaptchaTryAnother() {
        return captchaTryAnother;
    }

    public void setCaptchaTryAnother(String captchaTryAnother) {
        this.captchaTryAnother = captchaTryAnother;
    }

    public boolean isSendAutoResponse() {
        return sendAutoResponse;
    }

    public void setSendAutoResponse(boolean sendAutoResponse) {
        this.sendAutoResponse = sendAutoResponse;
    }

    public SelectItem[] getEmailTemplates() {
        return emailTemplates;
    }

    public void setEmailTemplates(SelectItem[] emailTemplates) {
        this.emailTemplates = emailTemplates;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public LayoutRPC getCustomForm() {
        return customForm;
    }

    public void setCustomForm(LayoutRPC customForm) {
        this.customForm = customForm;
    }

    public boolean hasCustomContent() {
        return customForm != null && !"".equals(customForm);
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public ReferenceItem getWebFormSource() {
        return webFormSource;
    }

    public void setWebFormSource(ReferenceItem webFormSource) {
        this.webFormSource = webFormSource;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }
}
