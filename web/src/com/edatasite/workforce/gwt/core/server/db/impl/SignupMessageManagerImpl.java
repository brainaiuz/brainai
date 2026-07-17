package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsSignupMessage;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.SignupMessageManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Maps;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * User: Murad Satimov
 * Date: 9/26/17 5:49 PM
 */
@Repository
public class SignupMessageManagerImpl extends BaseManager<EdsSignupMessage> implements SignupMessageManager {

    private static final Logger log = LoggerFactory.getLogger(SignupMessageManagerImpl.class);

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private BlackListManager blackListManager;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;

    public SignupMessageManagerImpl() {
        super(EdsSignupMessage.class);
    }

    @Override
    public void sendFromMobileCompanyRegistrationNotification(EdsUser administrator,
                                                              Map<String, Object> companyInfo,
                                                              boolean hasAccount) throws EdsDbException {
        if (administrator == null || companyInfo == null || companyInfo.isEmpty()) {
            log.error("Error sending registration email");
            return;
        }
        try {
            final Integer companyId = (Integer) companyInfo.get("companyID");
            final String adminEmail = administrator.getEmail();
            final Locale locale = ServerUtils.getUserLocale();
            final String subject = companyId + ": Account Confirmation";
            final Map<String, Object> adminValues = new TreeMap<>();
            final String dateFormat = "dd.MM.yyyy";
            final String id = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(administrator.getObjectID().toString()));
            final String encryptedCompanyId = EncryptionHelper.encodeURL(EncryptionHelper.encrypt(companyId.toString()));

            adminValues.put("logo", EdsContextParams.getLogoWithHost(companyId));
            adminValues.put("host", EdsContextParams.getHost(companyId));
            adminValues.put("HOST", EdsContextParams.getHost());
            adminValues.put("helpHost", EdsContextParams.getHelpHost());
            adminValues.put("productName", EdsContextParams.getProductName());
            adminValues.put("phone", EdsContextParams.getPhone());
            adminValues.put("user", administrator);
            adminValues.put("uid", id);
            adminValues.put("TIME", new SimpleDateFormat(dateFormat).format(new Date()));
            adminValues.put("remoteAddr", "");
            adminValues.put("companyName", companyInfo.get("companyName"));
            adminValues.put("companyid", encryptedCompanyId);
            adminValues.put("mobileDevice", companyInfo.get("mobileDevice"));
            adminValues.put("password", companyInfo.get("password"));
            final String support = EdsContextParams.getSupportEmail();
            final String dirText = EdsTemplates.processTemplate(administrator, adminValues, EdsTemplates.NOTIFICATION_FROM_MOBILE);
            String adminText = null;

            if (hasAccount) {
                adminText = EdsTemplates.processTemplate(administrator, adminValues, EdsTemplates.SIGN_UP_FOR_ADMINISTRATOR_WITHOUT_ACTIVATION_LINK);
            } else {
                adminText = EdsTemplates.processTemplate(administrator, adminValues, EdsTemplates.SIGN_UP_FOR_ADMINISTRATOR_WITHOUT_ACTIVATION_LINK_FROM_MOBILE);
            }
            this.sendMessage(support,
                    subject,
                    dirText,
                    null,
                    false,
                    companyId,
                    true);

            this.sendMessage(adminEmail,
                    subject,
                    adminText,
                    support,
                    false,
                    companyId,
                    true);
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    @Autowired
    private ActivationLinkManager activationLinkManager;

    @Override
    public void sendCompanyRegistrationNotificationToSystem(EdsUser administrator,
                                                            EdsCompany company,
                                                            String remoteAddr) throws EdsDbException {
        String subject = "New Sign Up Notification";
        final Map<String, Object> adminValues = new TreeMap<>();
        adminValues.put("logo", EdsContextParams.getLogoWithHost(company.getObjectID()));
        adminValues.put("host", EdsContextParams.getHost());
        adminValues.put("productName", EdsContextParams.getProductName());
        adminValues.put("user", administrator);
        adminValues.put("TIME", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        adminValues.put("remoteAddr", remoteAddr);
        adminValues.put("companyName", company.getName());
        adminValues.put("userName", administrator.getName());
        adminValues.put("currentYear", LocalDateTime.now().getYear());

        try {
            final String dirText = EdsTemplates.processTemplate(administrator, adminValues, EdsTemplates.NOTIFICATION);
            final String support = EdsContextParams.getSupportEmail();

            this.sendMessage(support,
                    subject,
                    dirText,
                    null,
                    false,
                    company.getObjectID(),
                    true);
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    @Override
    public void sendCompanyRegistrationNotificationToUser(String adminEmail,
                                                          String userName,
                                                          String companyName,
                                                          String locale,
                                                          String activationLink,
                                                          boolean isIncludeActivationLink,
                                                          boolean existingUser) throws EdsDbException {
        String subject = messageSource.getMessage("signup.EmailVerify", null, "Please verify your email address for", new Locale(locale));
        EdsHostBasedSetting hostBasedSetting = hostBasedSettingManager.getLinksByHostName(EdsContextParams.getHostname());
        subject = hostBasedSetting.getProductName() != null ? subject + " " + hostBasedSetting.getProductName()  : subject;

        final Map<String, Object> adminValues = new TreeMap<>();
        adminValues.put("logo", EdsContextParams.getLogoWithHost(SecurityContext.getCompanyID()));
        adminValues.put("host", hostBasedSetting.getHostname());
        adminValues.put("companyName", companyName);
        adminValues.put("changePassword", !existingUser);
        adminValues.put("adminEmail", adminEmail);
        adminValues.put("userName", userName);
        adminValues.put("currentYear", LocalDateTime.now().getYear());
        adminValues.put("website", hostBasedSetting.getWebsite());
        adminValues.put("days", hostBasedSetting.getFreetrialdays());
        adminValues.put("productName", hostBasedSetting.getProductName());
        adminValues.put("email", hostBasedSetting.getEmail());
        adminValues.put("description", hostBasedSetting.getDescription());

        adminValues.put("keyValue", activationLink);
        try {
            String adminText;

            //existing active users with normal password shouldn't recieve message with activation link
            if (isIncludeActivationLink) {
                adminText = EdsTemplates.processTemplate(adminValues, EdsTemplates.SIGN_UP_FOR_ADMINISTRATOR, locale);
            } else {
                adminText = EdsTemplates.processTemplate(adminValues, EdsTemplates.SIGN_UP_FOR_ADMINISTRATOR_WITHOUT_ACTIVATION_LINK, locale);
                subject = messageSource.getMessage("signup.EmailSubject", null, "Account Confirmation", new Locale(locale));
            }
            final String support = EdsContextParams.getSupportEmail();

            this.sendMessage(Constants.defaultSupportEmail,
                    adminEmail,
                    null,
                    "munir@kpi.com",
                    subject,
                    adminText,
                    false,
                    support,
                    null,
                    true);
        } catch (EdsTemplateException tex) {
            throw new EdsDbException(tex);
        }
    }

    @Override
    public void sendEmployeeActivationMessage(EdsEmployee employee) throws EdsDbException, EdsTemplateException {
        try {
            final EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmployeeActivatedByManagerEmailTemplate(employee);

            //check if email is configured, otherwise throw exception
            EdsEmailSetting settings = settings = messageManager.wrapEdsCompanySystemSettings(SecurityContext.getCompanyID(), null);
            if (settings == null) {
                throw new EdsTemplateException("Corporate email is not configured for company: [" + SecurityContext.getCompanyID() + "]");
            }

            if (templateItem != null) {
                this.sendMessage(templateItem.getToEmail(),
                        templateItem.getSubject(),
                        templateItem.getMessageHTML(),
                        settings.getEmail(),
                        false,
                        SecurityContext.getCompanyID(),
                        false);
            } else {
                final String subject = commonLocalizer.localize(EdsSubjects.EMPLOYEE_ACTIVATION_NOTIFICATION);
                final Map<String, Object> requestValues = Maps.newTreeMap();
                requestValues.put("HOST", EdsContextParams.getHost(employee.getCompany().getObjectID()));
                requestValues.put("userName", employee.getUserName());
                requestValues.put("name", employee.getFullName());

                String empText = EdsTemplates.processTemplate(employee.getCreator(), requestValues, EdsTemplates.EMPLOYEE_ACTIVATE);
                this.sendMessage(employee.getEmail(),
                        subject,
                        empText,
                        settings.getEmail(),
                        false,
                        SecurityContext.getCompanyID(),
                        false);
            }
        } catch (EdsTemplateException tex) {
            tex.printStackTrace();
        }
    }

    private void sendMessage(String to,
                             String subject,
                             String text,
                             String replyTo,
                             Boolean hasAttachment,
                             Integer companyId,
                             Boolean isSystem) throws EdsDbException {
        this.sendMessage(Constants.defaultSupportEmail,
                to,
                null,
                null,
                subject,
                text,
                hasAttachment,
                replyTo,
                companyId,
                isSystem);
    }

    private void sendMessage(String fromEmail,
                             String to,
                             String cc,
                             String bcc,
                             String subject,
                             String text,
                             Boolean attachment,
                             String replyTo,
                             Integer companyId,
                             Boolean isSystem) throws EdsDbException {
        if (ServerUtils.isNullOrEmpty(to) /*|| !blackListManager.isEmailValid(to)*/) {
            log.error("Email not sent! ToEmail empty or ToEmail address in black list");
            return;
        }
        final EdsSignupMessage message = new EdsSignupMessage();

        message.setTo(to);
        message.setCc(cc);
        message.setBcc(bcc);
        if (!ServerUtils.isNullOrEmpty(replyTo)) {
            message.setFromName(replyTo);
            message.setFromEmail(replyTo);
        } else {
            message.setFromName(fromEmail);
            message.setFromEmail(fromEmail);
        }
        message.setSubject(subject);
        message.setText(text);
        message.setAttachment(attachment);
        if (!ServerUtils.isNullOrEmpty(replyTo)) {
            message.setReplyTo(replyTo);
        }
        message.setType(MessageTypeEnum.PREFERRED);
        if (companyId != null) {
            message.setCompanyID(companyId);
        } else if (getUser() != null && getUser().getCompany() != null) {
            message.setCompanyID(getUser().getCompany().getObjectID());
        }
        message.setStatus(MessageStatusEnum.SENT);
        message.setAttempts(0);
        message.setCreationDate(new Date());
        message.setSystem(isSystem);
        this.create(message);

        EdsEmailSetting settings = null;
        if (companyId != null) {
            settings = messageManager.wrapEdsCompanySystemSettings(companyId, fromEmail);
        }
        try {
            final EdsMailer mailer = EdsMailer.getNewInstance(message, settings);
            mailer.sendSynchronized();
            message.setStatus(MessageStatusEnum.SENT);
            log.info("Message sent[TO:" + message.getTo() + ",SUBJECT:" + message.getSubject() + "]");
        } catch (AddressException e) {
            message.setStatus(MessageStatusEnum.FAILED);
            log.error("Error sending email[MESSAGE_ID:" + (message.getObjectID() != null
                    ? message.getObjectID().toString()
                    : "message id is null") + "]:", e);
        } catch (MessagingException e) {
            message.setStatus(MessageStatusEnum.PENDING);
            message.setAttempts(Optional.ofNullable(message.getAttempts()).orElse(0) + 1);
        } catch (Exception e) {
            message.setStatus(MessageStatusEnum.FAILED);
            message.setAttempts(Optional.ofNullable(message.getAttempts()).orElse(0) + 1);
            log.error("Error sending email[MESSAGE_ID:" + (message.getObjectID() != null
                    ? message.getObjectID().toString()
                    : "message id is null") + "]:", e);
        }
        this.update(message);
    }

}
