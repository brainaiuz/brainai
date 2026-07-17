/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Admin                                                                                                        *
 * Time: 2010/5/22 6:37:6                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.shared.mail;

import com.edatasite.workforce.utils.InputStreamDataSource;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: zohid
 * Date: 23.05.2007
 * Time: 16:50:16
 * To change this template use File | Settings | File Templates.
 */

public class EdsMail extends Thread {

    private static final ConcurrentHashMap<String, Transport> TRANSPORT_POOL = new ConcurrentHashMap<>();
    private static final Map<String, Session> SESSION_CACHE = new ConcurrentHashMap<>();
    private static final Object POOL_LOCK = new Object();
    private static final Logger log = LoggerFactory.getLogger(EdsMail.class);
    private final EdsMailBody _body;


    public EdsMail(EdsMailBody _body) {
        this._body = _body;
    }

    public EdsMailBody getBody() {
        return _body.clone();
    }

    private void initTORecipients(Message msg) throws Exception {
        msg.addRecipients(Message.RecipientType.TO, _body.getTORecipientsArray());
    }

    private void initCCRecipients(Message msg) throws Exception {
        if (_body.getCCRecipients() != null) {
            msg.addRecipients(Message.RecipientType.CC, _body.getCCRecipientsArray());
        }
    }

    private void initBCCRecipients(Message msg) throws Exception {
        if (_body.getBCCRecipients() != null) {
            msg.addRecipients(Message.RecipientType.BCC, _body.getBCCRecipientsArray());
        }
    }

    protected void send() throws Exception {
        log.warn("-!--!--!--!--!--!--!--!--!-BOSHLANDI-!--!--!--!--!--!--!--!--!--!--!-");
        Session session = getSession();
        MimeMessage msg = buildMimeMessage(session);

        String poolKey = buildPoolKey();
        Transport transport = getOrCreateTransport(session, poolKey);

        try {
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (MessagingException e) {
            removeFromPool(poolKey);
            log.warn("Transport xatosi, pooldan o'chirildi [{}]: {}", poolKey, e.getMessage());
            throw e;
        }
    }

    private String buildPoolKey() {
        return _body.getSmtpServerHost() + ":" +
                _body.getSmtpServerPort() + ":" +
                _body.getLogin();
    }

    private Transport getOrCreateTransport(Session session, String poolKey) throws MessagingException {
        Transport transport = TRANSPORT_POOL.get(poolKey);
        if (transport != null && transport.isConnected()) {
            return transport;
        }

        synchronized (POOL_LOCK) {
            transport = TRANSPORT_POOL.get(poolKey);
            if (transport != null && transport.isConnected()) {
                return transport; // boshqa thread yaratib qo'ygan bo'lishi mumkin
            }

            if (transport != null) {
                closeTransportQuietly(transport);
            }

            transport = session.getTransport(_body.get_smtpProtocol());
            if (_body.isSmtpAuth()) {
                transport.connect(
                        _body.getSmtpServerHost(),
                        StringUtils.isEmpty(_body.getSmtpServerPort())
                                ? -1 : Integer.parseInt(_body.getSmtpServerPort()),
                        _body.getLogin(),
                        _body.getPassword()
                );
            } else {
                transport.connect();
            }

            log.info("New transport created [{}]", poolKey);
            TRANSPORT_POOL.put(poolKey, transport);
        }

        return transport;
    }

    private void removeFromPool(String poolKey) {
        Transport transport = TRANSPORT_POOL.remove(poolKey);
        if (transport != null) {
            closeTransportQuietly(transport);
        }
    }

    private void closeTransportQuietly(Transport transport) {
        try {
            if (transport != null && transport.isConnected()) {
                transport.close();
            }
        } catch (MessagingException e) {
            log.warn("Transport yopishda xato: {}", e.getMessage());
        }
    }

    public static void closeAllTransports() {
        TRANSPORT_POOL.forEach((key, transport) -> {
            try {
                if (transport != null && transport.isConnected()) {
                    transport.close();
                    log.info("Transport yopildi [{}]", key);
                }
            } catch (MessagingException e) {
                log.warn("Transport yopishda xato [{}]: {}", key, e.getMessage());
            }
        });
        TRANSPORT_POOL.clear();
    }

    @PreDestroy
    public void destroy() {
        closeAllTransports();
        log.info("All transports are closed");
    }

    private MimeMessage buildMimeMessage(Session session) throws Exception {
        MimeMessage msg = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();

        // Text body
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(_body.getMessage(), "UTF-8");
        messageBodyPart.setHeader("Content-Type", "text/html");
        multipart.addBodyPart(messageBodyPart);

        // Calendar invitation
        if (StringUtils.isNotBlank(_body.get_invitationContent())) {
            BodyPart calendarPart = new MimeBodyPart();
            calendarPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            calendarPart.setHeader("Content-ID", "calendar_message");
            calendarPart.setDataHandler(new DataHandler(
                    new ByteArrayDataSource(_body.get_invitationContent(), "text/calendar")
            ));
            multipart.addBodyPart(calendarPart);
        }

        // Attachments
        if (_body.getUploads() != null) {
            for (Upload upload : _body.getUploads()) {
                if (upload.getInputStream() != null) {
                    MimeBodyPart attachPart = getMimeBodyPart(upload);
                    multipart.addBodyPart(attachPart);
                }
            }
        }

        msg.setSubject(_body.getDisplaySubject(), "UTF-8");
        msg.setHeader("Content-Type", "text/html");
        msg.addFrom(_body.getFromArray());
        if (_body.getReplyToArray() != null) {
            msg.setReplyTo(_body.getReplyToArray());
        }
        msg.setContent(multipart);
        initTORecipients(msg);
        initCCRecipients(msg);
        initBCCRecipients(msg);

        return msg;
    }

    private static MimeBodyPart getMimeBodyPart(Upload upload) throws MessagingException, IOException {
        MimeBodyPart attachPart = new MimeBodyPart();
        attachPart.setDataHandler(new DataHandler(
                new InputStreamDataSource(
                        upload.getFileName(),
                        upload.getContentType(),
                        upload.getInputStream()
                )
        ));
        attachPart.setFileName(upload.getFileName());
        attachPart.addHeader("Content-Type", upload.getContentType() + "; charset=UTF-8");
        return attachPart;
    }

    private Session getSession() {
        String cacheKey = _body.getSmtpServerHost() + ":" + _body.getSmtpServerPort() + ":" + _body.getLogin();
        return SESSION_CACHE.computeIfAbsent(cacheKey, k -> createSession());
    }

    private Session createSession() {
        String port = _body.getSmtpServerPort();
        boolean isSecure = isSecurePort(port);
        String protocol = isSecure ? "smtps" : "smtp";
        String prefix = "mail." + protocol + ".";

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty(prefix + "host", _body.getSmtpServerHost());
        props.setProperty(prefix + "auth", "true");
        props.setProperty(prefix + "ssl.trust", "*");
        props.setProperty("mail.mime.address.strict", "false");

        // Port
        if (StringUtils.isNotBlank(port)) {
            props.setProperty(prefix + "port", port);
        }

        if ("587".equals(port) || "25".equals(port)) {
            props.setProperty(prefix + "starttls.enable", "true");
            props.setProperty(prefix + "starttls.required", "true");
        } else if (isSecure) {
            props.setProperty(prefix + "ssl.enable", "true");
        }

        props.setProperty(prefix + "connectiontimeout", "10000"); // 10s — ulanish vaqti
        props.setProperty(prefix + "timeout",            "30000"); // 30s — javob kutish
        props.setProperty(prefix + "writetimeout",       "30000"); // 30s — yuborish vaqti

        if (StringUtils.isNotBlank(_body.getSmtpServerHost())) {
            props.setProperty(prefix + "auth.ntlm.domain", _body.getSmtpServerHost());
        }

        Authenticator auth = new EdsSMTPAuthenticator(_body.getLogin(), _body.getPassword());
        return Session.getInstance(props, _body.isSmtpAuth() ? auth : null);
    }

    private boolean isSecurePort(String port) {
        return "465".equals(port) || "993".equals(port);
    }

    public void run() {
        setName("Mail sender thread " + System.currentTimeMillis());
        try {
            send();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
