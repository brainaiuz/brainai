package com.edatasite.shared.mail;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by IntelliJ IDEA.
 * User: zohid
 * Date: 24.05.2007
 * Time: 11:10:37
 * To change this template use File | Settings | File Templates.
 */

public class EdsMailer {
    private static final String KPI_MAILER_PROPERTIES = "kpimail.properties";
    private static Logger log = LoggerFactory.getLogger(EdsMailer.class);
    private EdsMailBody _body;
    private static Map<String, EdsMailParams> mailParamsMap = new ConcurrentHashMap<>();

    public static void clearHostSetting() {
        mailParamsMap.clear();
        System.out.println("EdsMailer: >>> Cache CLEARED");
    }

    public static EdsMailParams getWhiteLabelingMailer() {
        String hostname = EdsContextParams.getHostname();
        String paramsFileName = EdsContextParams.getMailParamsFileName();
        mailParamsMap.computeIfAbsent(hostname, value -> parsePropertiesFile(paramsFileName));
        return mailParamsMap.get(hostname);
    }

    private static EdsMailParams parsePropertiesFile(String paramFilePath) {
        if (paramFilePath == null) {
            paramFilePath = KPI_MAILER_PROPERTIES;
        }
        ClassLoader loader = EdsMailer.class.getClassLoader();
        log.info(String.valueOf("Loader is not null: " + loader != null));
        String path = EdsMailer.class.getName();
        path = path.substring(0, path.lastIndexOf('.')).replace('.', '/');
        log.info("Loader path: " + path);
        Properties props = new Properties();
        EdsMailParams mailParams = new EdsMailParams();
        try {
            InputStream resourceAsStream = loader.getResourceAsStream(path + '/' + paramFilePath);
            if (resourceAsStream != null) {
                log.info(String.valueOf("Inputstream is not null: " + resourceAsStream != null));
                props.load(resourceAsStream);
                mailParams.setSmtpProtocol(props.getProperty("mail.smtp.protocol"));
                mailParams.setSmtpHost(props.getProperty("mail.smtp.host"));
                mailParams.setSmtpPort(props.getProperty("mail.smtp.port"));
                mailParams.setLogin(props.getProperty("mail.login"));
                mailParams.setPassword(props.getProperty("mail.password"));
                mailParams.setIsSSL(props.getProperty("mail.ssl"));
                mailParams.setAuth(props.getProperty("mail.isauth"));
                mailParams.setEmail(props.getProperty("mail.email"));
                mailParams.setFrom(props.getProperty("mail.from"));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return mailParams;
    }

    public static EdsMailer getNewInstance(EdsSuperMessage message, EdsEmailSetting settingsItem) throws Exception {
        if (message.getCompanyID() != null) {
            ServerSecurityContext.getInstance().setCompanyId(message.getCompanyID());
        }
        EdsMailParams mailParams = EdsMailer.getWhiteLabelingMailer();

        String login = settingsItem != null && StringUtils.isNotBlank(settingsItem.getUserName()) ? settingsItem.getUserName() : mailParams.getLogin();
        String password = settingsItem != null && StringUtils.isNotBlank(settingsItem.getPassword()) ? EncryptionHelper.decrypt(settingsItem.getPassword()) : mailParams.getPassword();
        String email = settingsItem != null && StringUtils.isNotBlank(settingsItem.getEmail()) ? settingsItem.getEmail() : mailParams.getEmail();
        String fromName = settingsItem != null && StringUtils.isNotBlank(settingsItem.getFromName()) ? settingsItem.getFromName() : mailParams.getFrom();
        String protocol = settingsItem != null && StringUtils.isNotBlank(settingsItem.getEmailSmtpProtocol()) ? settingsItem.getEmailSmtpProtocol() : mailParams.getSmtpProtocol();
        String smtpHost = settingsItem != null && StringUtils.isNotBlank(settingsItem.getEmailHostSMTP()) ? settingsItem.getEmailHostSMTP() : mailParams.getSmtpHost();
        String smtpPort = settingsItem != null && settingsItem.getEmailPortSMTP() != null ? settingsItem.getEmailPortSMTP().toString() : mailParams.getSmtpPort();
        boolean smtpAuth = settingsItem != null ? settingsItem.isSmtpAuth() : Boolean.parseBoolean(mailParams.getAuth());

        return new EdsMailer(protocol,
                        smtpHost,
                        login,
                        password,
                        smtpPort,
                        smtpAuth,
                        message.getSubject(),
                        message.getDisplaySubject(),
                        message.getText(),
                        message.getInvitationContent(),
                        email,
                        fromName,
                        message.getTo(),
                        message.getCc(),
                        message.getBcc(),
                        message.getReplyTo(),
                        message.getUploads()
                );
    }

    private EdsMailer(String protocol,
                      String host,
                      String login,
                      String password,
                      String port,
                      boolean isSmtpAuth,
                      String subject,
                      String displaySubject,
                      String message,
                      String invitationContent, String email,
                      String fromName,
                      String to,
                      String cc,
                      String bcc,
                      String replyTo,
                      List<Upload> uploads) throws Exception {
        _body = new EdsMailBody();
        _body.setUploads(uploads);
        _body.setSmtpServerHost(host);
        _body.set_smtpProtocol(protocol);
        _body.setLogin(login);
        _body.setPassword(password);
        _body.setSmtpServerPort(port);
        _body.setSmtpAuth(isSmtpAuth);
        if (subject == null) {
            _body.setSubject("");
        } else {
            _body.setSubject(subject);
        }
        if (displaySubject == null) {
            _body.setDisplaySubject("");
        } else {
            _body.setDisplaySubject(displaySubject);
        }
        if (message == null) {
            _body.setMessage("");
        } else {
            _body.setMessage(message);
        }
        if (StringUtils.isNotBlank(replyTo)) {
            _body.addReplyTo(replyTo);
        }
        _body.set_invitationContent(invitationContent);
        _body.setFromName(fromName);
        _body.addFrom(email, _body.getFromName());

        if (to.contains(",")) {
            String[] tos = to.split(",");
            for (String to_ : tos) {
                to_ = to_.replace(" ", "");
                if (StringUtils.isNotBlank(to_)) {
                    _body.addTORecipient(to_);
                }
            }
        } else {
            _body.addTORecipient(to);
        }

        if (StringUtils.isNotBlank(cc)) {
            String[] ccsAsArray = cc.split(",");
            for (String _cc : ccsAsArray) {
                if (StringUtils.isNotBlank(_cc)) {
                    _body.addCCRecipient(_cc);
                }
            }
        }

        if (StringUtils.isNotBlank(bcc)) {
            String[] bccsAsArray = bcc.split(",");
            for (String _bcc : bccsAsArray) {
                if (StringUtils.isNotBlank(_bcc)) {
                    _body.addBCCRecipient(_bcc);
                }
            }
        }
    }

    public EdsMailBody getBody() {
        return _body.clone();
    }

    public void send() {
        new EdsMail(_body).start();
    }

    public void sendSynchronized() throws Exception {
        new EdsMail(_body).send();
    }

    public void addAttachment(String originalName, String contentType, InputStream inputStream) throws Exception {
        Upload upload = new Upload();
        upload.setContentType(contentType);
        upload.setInputStream(inputStream);
        upload.setFileName(originalName);
        _body.getUploads().add(upload);
    }
}
