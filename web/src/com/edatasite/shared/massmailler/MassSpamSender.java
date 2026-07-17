package com.edatasite.shared.massmailler;

import com.edatasite.shared.components.SessionCryptor;
import com.edatasite.shared.mail.EdsSMTPAuthenticator;
import com.edatasite.shared.mail.Upload;
import com.edatasite.shared.sms.SmsProvider;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.marre.sms.SmsException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 14:23:53
 * To change this template use File | Settings | File Templates.
 */

public class MassSpamSender {
    public static final String EMAIL = "EMAIL";
    public static final String SMS = "SMS";

    private MassMailerBody mailerBody;
    private MassMailerCrmEntityBody crmEntityBody;
    private String sendType;
    private SmsProvider smsProvider;

    public MassSpamSender(MassMailerData data) {
        this.crmEntityBody = data.getCrmEntityBody();
        this.mailerBody = data.getBody();
        this.sendType = data.getSendType() == null ? EMAIL : data.getSendType();
        this.smsProvider = EdsSmsSettings.senderCollector.get(SecurityContext.getCompanyID());
    }

    protected boolean send() throws SmsException, IOException {
        if (SMS.equals(this.sendType)) {
            return sendSms();
        }
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", mailerBody.getHost());
            properties.put("mail.smtp.user", mailerBody.getLogin());
            properties.put("mail.smtp.auth", mailerBody.isSmtpAuth());
            properties.put("mail.smtp.port", mailerBody.getPort());
            properties.put("mail.smtp.localhost", mailerBody.getHost());
            properties.put("mail.smtp.from", mailerBody.getBouncerEmail());

            String encrypted = "?subscr=";
            encrypted = encrypted + SessionCryptor.encrypt(crmEntityBody.getEntityID().toString()) + "&type=entity";
            encrypted = encrypted + "&msg=" + SessionCryptor.encrypt(crmEntityBody.getMsgId().toString());
            if (crmEntityBody.getCompanyID() != null) {
                encrypted = encrypted + "&comid=" + SessionCryptor.encrypt(crmEntityBody.getCompanyID());
            }
            encrypted = encrypted + "&mailListID=" + SessionCryptor.encrypt(crmEntityBody.getMailListId().toString());
            String msg = mailerBody.retriveMessage(crmEntityBody, encrypted);

            //EdsContextParams.HOST
            Authenticator auth = new EdsSMTPAuthenticator(mailerBody.getLogin(), mailerBody.getPassword());
            Session session = Session.getInstance(properties, auth);

            MimeMessage message = new MimeMessage(session);
            /* set mail subjects */
            String msgSubject = mailerBody.retriveSubject(crmEntityBody);
            message.setSubject(msgSubject, "UTF-8");
            String fromFullName = "Noreply";

            if (mailerBody.getFullName() != null) {
                fromFullName = mailerBody.getFullName();
            }
            message.addFrom(new InternetAddress[]{new InternetAddress(mailerBody.getFrom(), fromFullName)});
            //END OF GOOGLE ANALYTICS ATTRIBUTES
            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            if (mailerBody.isHtml()) {
                messageBodyPart.setContent(msg, "text/html; charset=utf-8");
                messageBodyPart.setHeader("Content-Type", "text/html; charset=utf-8");
            } else {
                messageBodyPart.setText(msg, "UTF-8");
            }
            multipart.addBodyPart(messageBodyPart);
            List<Upload> uploads = mailerBody.getFiles();
            if (uploads != null && uploads.size() > 0) {
                for (Upload upload : uploads) {
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(upload.getDataHandler());
                    messageBodyPart.setFileName(upload.getFileName());
                    messageBodyPart.addHeader("Content-Type", upload.getContentType() + "; charset=UTF-8");
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            message.setContent(multipart);
            try {
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(crmEntityBody.getRecipientEmail(), false));
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            try {
                if (mailerBody.getReplyTo() != null) {
                    message.setReplyTo(InternetAddress.parse(mailerBody.getReplyTo(), false));
                } else {
                    message.setReplyTo(new InternetAddress[]{new InternetAddress(mailerBody.getFrom())});
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            try {
                if (mailerBody.getCc() != null) {
                    message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(mailerBody.getCc(), false));
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            try {
                if (mailerBody.getBcc() != null) {
                    message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailerBody.getReplyTo(), false));
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            //SET WFM HEADERS
            if (crmEntityBody.getDatabase() != null) {
                message.addHeader(Constants.MASS_MAIL_HEADER_DATABASE_TYPE, crmEntityBody.getDatabase());
            }
            if (crmEntityBody.getCompanyID() != null) {
                message.addHeader(Constants.MASS_MAIL_HEADER_COMPANYID, crmEntityBody.getCompanyID());
            }
            message.addHeader(Constants.MASS_MAIL_HEADER_MESSAGEID, crmEntityBody.getMsgId() + "");
            message.addHeader(Constants.MASS_MAIL_HEADER_ENTITYID, crmEntityBody.getEntityID() + "");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendSms() throws SmsException, IOException {
        String msg = mailerBody.getMessage();
        String phoneNumber = crmEntityBody.getRecipientMobile() != null ? crmEntityBody.getRecipientMobile().replaceAll("\\||w|\\+", "") : null;
        if (smsProvider != null && phoneNumber != null && !"".equals(phoneNumber) && !"null".equals(phoneNumber)) {
            boolean result = smsProvider.send(msg, phoneNumber);
            System.out.println(">>>>>>>> Message >>>>>>> " + msg + " >>>>>>> Phone >>>>>> " + phoneNumber + " >>>>>>> Sent >>>>>>>>>>>" + result);
            return result;
        } else {
            if (smsProvider == null) {
                throw new SmsException("There is no sender SMSPROVIDER!");
            } else {
                throw new SmsException("Destination is EMPTY! ");
            }
        }
    }

    public MessageStatusEnum sendSync() {
        try {
            if (send()) {
                return MessageStatusEnum.SENT;
            } else {
                return MessageStatusEnum.BOUNCED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageStatusEnum.FAILED;
        }
    }
}
