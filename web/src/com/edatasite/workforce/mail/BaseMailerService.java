package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.BlackListManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.listeners.EdsInitListener;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BaseMailerService implements IBaseJob, Constants {
    private static final Logger log = LoggerFactory.getLogger(EdsInitListener.class);
    protected MessageManager messageManager;
    protected BlackListManager blackListManager;

    public void execute() {
        try {
            ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
            ServerSecurityContext.getInstance().setCompanyId("0");
            List<EdsSuperMessage> messages = getMessages();
            List<String> blackLists = blackListManager.getBlackList();
            for (EdsSuperMessage message : messages) {
                if (blackLists.contains(message.getTo().toLowerCase())) {
                    log.info("=== Blacklisted === {} === COMPANY_ID: {} ===", message.getTo(), message.getCompanyID());
                    message.setStatus(MessageStatusEnum.FAILED);
                    continue;
                }
                log.info("=== Start send message === COMPANY_ID: {} ===", message.getCompanyID());
                message.setAttempts(message.getAttempts() == null ? 1 : message.getAttempts() + 1);
                message.setStatus(MessageStatusEnum.IN_PROGRESS);
                if (message.getCompanyID() != null) {
                    ServerSecurityContext.getInstance().setCompanyId(message.getCompanyID());
                }
                //SEND EMAIL
                messageManager.baseMailSender(message);
                ServerSecurityContext.getInstance().setCompanyId("0");
                try {
                    Thread.sleep(100); // 100ms
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception t) {
            log.error("Error processing block of messages", t);
        }
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void setBlackListManager(BlackListManager blackListManager) {
        this.blackListManager = blackListManager;
    }

    public abstract List<EdsSuperMessage> getMessages();
}
