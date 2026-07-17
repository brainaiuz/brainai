package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.mail.EdsSMTPAuthenticator;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.eclipse.angus.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 24/08/11
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class EmailConnectionUtils {
    private static final Logger log = LoggerFactory.getLogger(EmailConnectionUtils.class);

    /**
     * <i>... IMAP connection ...</i>
     *
     * @param setting
     * @return
     */
    public static Store getStore(EdsEmailSetting setting) throws MessagingException {
        String host = setting.getEmailHost();
        String userName = setting.getUserName();
        String password = EncryptionHelper.decrypt(setting.getPassword());
        Integer port = setting.getEmailPort();
        Store store;
        Session session = getEmailSession(setting);
        try {
            store = session.getStore();
            if (port != null) {
                log.info("Connecting to the store with host: " + host + " and port: " + port);
                store.connect(host, port, userName, password);
            } else {
                log.info("Connecting to the store with host: " + host);
                store.connect(host, userName, password);
            }
            EmailSessionCache.put(setting.getSessionKey(), session);
        } catch (MessagingException e) {
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------- Email cid=" + ServerSecurityContext.getInstance().getCompanyId() + "; Email=" + setting.getEmail() + ";" + (host != null ? host.toUpperCase() : " HOST IS NULL ") + "--Incoming Server Connection Error  ----------------------------");
            if (e instanceof AuthenticationFailedException) {
                System.out.println("--------------------------------------------------Authentification Failed ------------------------------------------------------------");
            } else {
                System.out.println(e.getMessage());
            }
            throw e;
        }
        return store;
    }

    /**
     * <i>... IMAP,SMTP Session ...</i>
     *
     * @param setting
     * @return
     */
    public static Session getEmailSession(EdsEmailSetting setting) {
        Properties properties1 = getInComingServerProperties(setting.getEmailHost(), setting.getUserName(), setting.getEmailPort());
        Properties properties2 = getOutGoingServerProperties(setting.getEmailHostSMTP(), setting.getUserName(), setting.getEmailPortSMTP(), setting.isSmtpAuth());
        properties1.putAll(properties2);
        Authenticator auth = new EdsSMTPAuthenticator(setting.getUserName(), EncryptionHelper.decrypt(setting.getPassword()));
        return Session.getInstance(properties1, auth);
    }

    /**
     * <i>... IMAP,SMTP Session ...</i>
     *
     * @param setting
     * @return
     */
    public static Session getEmailSession(EmailAccountItem setting) {
        return getEmailSession(setting, null);
    }

    /**
     * <i>... IMAP,SMTP Session ...</i>
     *
     * @param setting
     * @return
     */
    public static Session getEmailSession(EmailAccountItem setting, Properties customProperties) {
        Properties properties1 = getInComingServerProperties(setting.getImapHost(), setting.getUserName(), setting.getImapPort());
        Properties properties2 = getOutGoingServerProperties(setting.getSmtpHost(), setting.getUserName(), setting.getSmtpPort(), setting.isSmtpAuth());
        properties1.putAll(properties2);
        if (customProperties != null) {
            properties1.putAll(customProperties);
        }
        Authenticator auth = new EdsSMTPAuthenticator(setting.getUserName(), setting.getPassword());
        return Session.getInstance(properties1, auth);
    }

    /**
     * <i>... Default Email Properties ...</i>
     *
     * @param setting
     * @return
     */
    public static Properties getDefaultProperties(EdsEmailSetting setting) {
        Properties properties1 = getInComingServerProperties(setting.getEmailHost(), setting.getUserName(), setting.getEmailPort());
        Properties properties2 = getOutGoingServerProperties(setting.getEmailHostSMTP(), setting.getUserName(), setting.getEmailPortSMTP(), setting.isSmtpAuth());
        properties1.putAll(properties2);
        return properties1;
    }

    /**
     * <b>... IMAP server properties ...</b>
     *
     * @param host
     * @param userName
     * @param customProperties
     * @return
     */
    private static Properties getInComingServerProperties(String host, String userName, Integer port) {
        Properties properties = new Properties();
        if (host != null) {
            String prefix = EdsEmailSetting.isSecure(port) ? "mail.imaps." : "mail.imap.";
            properties.setProperty("mail.store.protocol", EdsEmailSetting.isSecure(port) ? "imaps" : "imap");
            properties.setProperty(prefix + "host", host);
            properties.setProperty(prefix + "connectiontimeout", "30000");
            properties.setProperty(prefix + "timeout", "30000");
            properties.put(prefix + "auth.ntlm.domain", host);

            properties.put(prefix + "sasl.mechanisms", "CRAM-MD5,GSSAPI,DIGEST-MD5,PLAIN");
            properties.put(prefix + "sasl.enable", "true");
            properties.put(prefix + "sasl.authorizationid", userName);
            properties.put(prefix + "sasl. xgwtrustedapphack.enable", "true");

            properties.put(prefix + "partialfetch", "false");
            properties.put(prefix + "fetchsize", "1048576");
            properties.put("mail.mime.charset", "UTF-8");
            properties.put("mail.mime.ignoreunknownencoding", "true");

            /*
            * javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException:
            * PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
            * unable to find valid certification path to requested target
             * */
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            properties.put(prefix + "ssl.trust", "*");
            properties.put(prefix + "ssl.socketFactory", sf);
        }
        return properties;
    }

    /**
     * <i>... Out Going server properties ...</i>
     *
     * @param host
     * @param userName
     * @param password
     * @param port
     * @param isSmtpConNotAuth
     * @param customProperties
     * @return
     */
    private static Properties getOutGoingServerProperties(String host, String userName, Integer port, boolean isSmtpAuth) {
        Properties properties = new Properties();
        if (host != null) {
            String prefix = EdsEmailSetting.isSecure(port) ? "mail.smtps." : "mail.smtp.";
            properties.setProperty("mail.transport.protocol", EdsEmailSetting.isSecure(port) ? "smtps" : "smtp");
            properties.setProperty(prefix + "host", host);
            properties.setProperty(prefix + "connectiontimeout", "60000"); // 1 minutes
            properties.setProperty(prefix + "timeout", "60000");// 1 minutes
            properties.setProperty("mail.mime.address.strict", "false");// 1 minutes
            if (isSmtpAuth) {
                properties.setProperty(prefix + "starttls.enable", EdsEmailSetting.TLS_PORT.equals(port) ? "true" : "false");
                properties.setProperty(prefix + "user", userName);
                properties.setProperty(prefix + "auth", "true");
                properties.setProperty(prefix + "auth.ntlm.domain", host);
                properties.setProperty(prefix + "ssl.trust", "*");
            }
        }
        return properties;
    }
}
