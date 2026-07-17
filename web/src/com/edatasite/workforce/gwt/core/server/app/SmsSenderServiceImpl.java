package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsSmsMessage;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.core.tools.SoapRequestBuilder;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SmsSenderService;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
import com.edatasite.workforce.gwt.core.server.db.SmsMessageManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.listeners.EdsInitListener;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Sherzod
 * Date: 15.01.2011
 * Time: 18:08:17
 */
@Transactional
@Service("smsSenderService")
public class SmsSenderServiceImpl implements SmsSenderService, SmsSenderServiceLocal {
    private static final Logger log = LoggerFactory.getLogger(EdsInitListener.class);

    @Autowired
    private SmsMessageManager smsMessageManager;
    @Autowired
    private SmsManager smsManager;
    @Autowired
    private MessageManager messageManager;

    @Override
    public void processSmsSender(EdsSmsMessage message) throws Exception {
        if (message.getCompanyID() != null) ServerSecurityContext.getInstance().setCompanyId(message.getCompanyID());
        EdsMessage mailMessage = new EdsMessage();
        mailMessage.setTo("kgoutgoing@kg.om");
        mailMessage.setType(MessageTypeEnum.NON_PREFERRED);
        mailMessage.setStatus(MessageStatusEnum.PENDING);
        mailMessage.setCreationDate(new Date());
        try {
            SoapRequestBuilder s = new SoapRequestBuilder();
            s.setServer("62.231.246.118");
            s.setMethodName("SendSMS");
            s.setXmlNamespace("SMSPushWs");
            s.setWebServicePath("/TsmsPush/SendSMS.asmx");
            s.setSoapAction(s.getXmlNamespace() + "/" + s.getMethodName());// " SMSPushWs / SendSMS ";

            s.AddParameter("UserName", "kg");
            s.AddParameter("Password", "kg@123");
            s.AddParameter("PhoneNo", message.getPhoneNumber());// "96895417082");
            s.AddParameter("Message", message.getText());
            s.AddParameter("Priority", 9);
            s.AddParameter("Schdate", "");
            s.AddParameter("Sender", "KG");
            s.AddParameter("AppID", "0000");
            s.AddParameter("SRC_KEY", "KG");

            s.sendRequest();
            log.info("Sms sent[TO:" + message.getPhoneNumber() + "]");
            mailMessage.setSubject("SMS SENT");
            mailMessage.setText("Sms sent[TO:" + message.getPhoneNumber() + "]\n CONTENT:" + message.getText());
            messageManager.create(mailMessage);
            ServerSecurityContext.getInstance().removeCompanyId();
        } catch (Exception e) {
            mailMessage.setSubject("FAILED: Cannot send sms");
            mailMessage.setText("FAILED while Sending to " + message.getPhoneNumber() + "\n" + e);
            messageManager.create(mailMessage);
            ServerSecurityContext.getInstance().removeCompanyId();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean smsToQueue(ArrayList<SmsSendItem> smss) {
        SmsSettings defaultSetting = getDefaultSmsSettingsForCompany();
        if (smss != null && smss.size() > 0) {
            smss.forEach(sms -> {
                EdsSmsMessage edsSmsMessage = new EdsSmsMessage();
                edsSmsMessage.setStatus(MessageStatusEnum.PENDING);
                edsSmsMessage.setPhoneNumber(sms.getToNumber());
                edsSmsMessage.setAttempts(0);
                edsSmsMessage.setText(sms.getMessageText());
                edsSmsMessage.setSettingID(sms.getSettingID() == null && defaultSetting != null ? defaultSetting.getObjectID() : sms.getSettingID());
                edsSmsMessage.setCompanyID(SecurityContext.getCompanyID());
                smsMessageManager.create(edsSmsMessage);
            });
        }
        return Boolean.TRUE;
    }

    private SmsSettings getDefaultSmsSettingsForCompany() {
        EdsSmsSettings settings = smsManager.getDefault();
        return settings == null ? null : settings.getRPC();
    }
}