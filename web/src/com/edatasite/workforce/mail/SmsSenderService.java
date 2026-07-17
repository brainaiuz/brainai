package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsBlackList;
import com.edatasite.workforce.core.domain.EdsSmsMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SmsSenderServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.SmsMessageManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SmsSenderService implements IBaseJob, Constants {
    private static Logger log = LoggerFactory.getLogger(SmsSenderService.class);
    private static boolean running = false;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private SmsSenderServiceLocal smsSenderServiceLocal;
    @Autowired
    private SmsMessageManager smsMessageManager;
    @Autowired
    private BlackListManager blackListManager;

    public void execute() {
        if (running) return;
        running = true;
        String databaseType = SpringPropertiesUtil.getProperty("bg_databaseType");
        try {
            ServerSecurityContext.getInstance().setDatabase(databaseType);
            List<EdsSmsMessage> messages = getMessages();
            messages.forEach(message -> {
                log.info("=== Start send SMS ===");
                message.setAttempts(message.getAttempts() == null ? 1 : message.getAttempts() + 1);
                try {
                    smsSenderServiceLocal.processSmsSender(message);
                    message.setStatus(MessageStatusEnum.SENT);
                } catch (Throwable t) {
                    log.error("Error sending [SMS_MESSAGE_ID:" + (message.getObjectID().toString()) + "]:", t);
                    if (message.getAttempts() >= 2) {
                        EdsBlackList blackList = new EdsBlackList();
                        blackList.setEmail(message.getPhoneNumber());
                        blackList.setHostName(EdsContextParams.getHostname());
                        blackListManager.create(blackList);
                        message.setStatus(MessageStatusEnum.FAILED);
                    } else {
                        message.setStatus(MessageStatusEnum.PENDING);
                    }
                }
                smsMessageManager.update(message);
            });
        } catch (Throwable t) {
            log.error("Error processing block of smsMessages", t);
        }
        running = false;
    }

    public List<EdsSmsMessage> getMessages() {
        log.info("===Start select all SMS pending messages for sending===");
        return messageManager.findNative("SELECT m.*, 0 as clazz_ FROM \"public\".smsmessage m WHERE m.status='" + MessageStatusEnum.PENDING + "' AND m.phonenumber<>'' limit 50", EdsSmsMessage.class);
    }
}
