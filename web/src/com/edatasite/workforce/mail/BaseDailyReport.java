package com.edatasite.workforce.mail;

import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 10.11.2008
 * Time: 16:33:57
 * To change this template use File | Settings | File Templates.
 */
public class BaseDailyReport implements IBaseJob {
    private MessageManager messageManager;

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Transactional
    public void execute() {
        try {
            messageManager.sendDailyReportNotification();
            messageManager.sendDailyNonActivateLinksNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
