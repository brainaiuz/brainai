package com.edatasite.workforce.core.domain.webforms;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 28, 2010
 * Time: 2:40:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "webforms")
public class EdsWebForm extends EdsObject implements WebFormConstants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "company_id")
    private Integer company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private EdsEmployee owner;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "confirmationMessage", length = 2000)
    private String confirmationMessage;

    @Column(name = "redirecturl")
    private String redirectURL;

    @Column(name = "emailAddress")
    private String emailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webFormSource")
    private EdsReference webFormSource;

    @Column(name = "iframe_url")
    private String iFrameUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "buttontext")
    private String buttonText = "Save";

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "createdTime")
    private Date createdTime = new Date();

    @Column(name = "lastUpdatedTime")
    private Date lastUpdatedTime = new Date();

    @Column(name = "token")
    private String token;

    @Column(name = "useCaptcha")
    private Boolean useCaptcha = true;

    @Column(name = "sendAutoResponse")
    private Boolean sendAutoResponse = false;

    @Column(name = "emailTemplate_id")
    private Integer emailTemplateID;

    @ManyToOne
    @JoinColumn(name = "lastEditedById")
    private EdsUser lastEditedBy;

    @Column(name = "captchalabel")
    private String captchaLabel;

    @Column(name = "captchadescription")
    private String captchaDescription;

    @Column(name = "captchacantread")
    private String captchaCantRead;

    @Column(name = "captchatryanotherlink")
    private String captchaTryAnotherLink;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "webform_id")
    @OrderBy(value = "sortOrder")
    private Set<EdsWebField> fields = new HashSet<>();

    @Column(name = "layoutID")
    private Integer layoutID;

    @Column(name = "isCustomLayout", columnDefinition = "boolean default false")
    private boolean isCustomLayout;

    @Column(name = "customcss")
    @Type(type = "text")
    private String customCss;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCompany getCompany() {
        if (company != null) {
            return ((CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager")).get(company);
        }
        return null;
    }

    public Integer getCompanyID() {
        return company;
    }

    public void setEdsCompany(EdsCompany company) {
        this.company = company != null ? company.getObjectID() : null;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public EdsReference getWebFormSource() {
        return webFormSource;
    }

    public void setWebFormSource(EdsReference webFormSource) {
        this.webFormSource = webFormSource;
    }

    public String getiFrameUrl() {
        return iFrameUrl;
    }

    public void setiFrameUrl(String iFrameUrl) {
        this.iFrameUrl = iFrameUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null ? deleted : false;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public EdsUser getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(EdsUser lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<EdsWebField> getFields() {
        return fields;
    }

    public void setFields(Set<EdsWebField> fields) {
        this.fields = fields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 2000) {
            description = description.substring(0, 1999);
        }
        this.description = description;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        if (confirmationMessage != null && confirmationMessage.length() > 2000) {
            confirmationMessage = confirmationMessage.substring(0, 1999);
        }
        this.confirmationMessage = confirmationMessage;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EdsEmployee getOwner() {
        return owner;
    }

    public void setOwner(EdsEmployee owner) {
        this.owner = owner;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Boolean isUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(Boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
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

    public String getCaptchaTryAnotherLink() {
        return captchaTryAnotherLink;
    }

    public void setCaptchaTryAnotherLink(String captchaTryAnotherLink) {
        this.captchaTryAnotherLink = captchaTryAnotherLink;
    }

    public Boolean getSendAutoResponse() {
        return sendAutoResponse;
    }

    public void setSendAutoResponse(Boolean sendAutoResponse) {
        this.sendAutoResponse = sendAutoResponse;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public Integer getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(Integer layoutID) {
        this.layoutID = layoutID;
    }

    public boolean isCustomLayout() {
        return isCustomLayout;
    }

    public void setCustomLayout(boolean customLayout) {
        isCustomLayout = customLayout;
    }

    public WebForm getRPC(boolean briefly, WebForm... items) {
        WebForm item = new WebForm();
        if (items != null && items.length > 0 && items[0] != null) {
            item = items[0];
        }
        item.setObjectId(getObjectID());
        item.setTitle(getTitle());
        item.setButtonText(getButtonText());
        item.setDescription(getDescription());
        item.setConfirmationMessage(getConfirmationMessage());
        item.setRedirectURL(getRedirectURL());
        item.setEmailAddress(getEmailAddress());
        item.setUseCatpcha(isUseCaptcha() == null ? false : isUseCaptcha());
        item.setCompanyID(getCompanyID());
        item.setCaptchaLabel(getCaptchaLabel());
        item.setCaptchaDescription(getCaptchaDescription());
        item.setCaptchaCantRead(getCaptchaCantRead());
        item.setCaptchaTryAnother(getCaptchaTryAnotherLink());
        item.setSendAutoResponse(getSendAutoResponse() == null ? false : getSendAutoResponse());
        item.setEmailTemplateID(getEmailTemplateID());
        if (getWebFormSource() != null) {
            item.setWebFormSource(getWebFormSource().getRPC());
        }
        if (getCompany() != null && getCompany().getCompanySettings() != null) {
            item.userSettings.put(Constants.LONG_DATE_FORMAT, getCompany().getCompanySettings().getLongDateFormat());
            item.userSettings.put(Constants.SHORT_DATE_FORMAT, getCompany().getCompanySettings().getShortDateFormat());
        } else {
            item.userSettings.put(Constants.LONG_DATE_FORMAT, "MMM dd, yyyy [HH:mm]");//MMM dd, yyyy [HH:mm] e.g. Jan 31, 2010 [08:30];
            item.userSettings.put(Constants.SHORT_DATE_FORMAT, "MMM dd, yyyy");//MMM dd, yyyy e.g. Jan 31, 2010
        }
        item.userSettings.put(Constants.COMPANY_ID, getCompanyID().toString());//Left non-encrypted for web forms
        item.userSettings.put(Constants.USER_ID, getOwner().getObjectID().toString());
        if (getType() != null) {
            item.setWebFormType(getType().getRPC());
        }
        item.setWebFieldsCount(getShowedFields(getFields()));
        item.setiFrameUrl(getiFrameUrl());
        if (!briefly) {
            item.setWebFields(getFieldsAsRPC());
        }
        item.setCustomCss(getCustomCss());
        return item;
    }

    private Integer getShowedFields(Set<EdsWebField> fields) {
        int fieldCount = 0;
        if (getFields() != null && getFields().size() > 0) {
            for (EdsWebField edsWebField : getFields().toArray(new EdsWebField[]{})) {
                if (edsWebField != null && edsWebField.getShowInForm() != null && edsWebField.getShowInForm()) {
                    fieldCount++;
                }
            }
        }
        return fieldCount;
    }

    private WebField[] getFieldsAsRPC() {
        if (getFields() != null && getFields().size() > 0) {
            WebField[] webFields = new WebField[getFields().size()];
            int i = 0;
            for (EdsWebField edsWebField : getFields().toArray(new EdsWebField[]{})) {
                if (edsWebField != null) {
                    webFields[i++] = edsWebField.getRPC(null);
                }
            }
            return webFields;
        }
        return null;
    }
}
