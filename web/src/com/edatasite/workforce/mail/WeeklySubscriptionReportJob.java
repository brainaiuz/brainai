package com.edatasite.workforce.mail;

import com.edatasite.workforce.gwt.core.server.commons.ExcelBAOSHandler;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 9, 2009
 * Time: 7:06:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeeklySubscriptionReportJob implements IBaseJob {

    private MessageManager messageManager;
    private ExcelBAOSHandler excelBAOSHandler;

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void setExcelBAOSHandler(ExcelBAOSHandler excelBAOSHandler) {
        this.excelBAOSHandler = excelBAOSHandler;
    }

    public void execute() {
        try {
            messageManager.sendWeeklySubscriptionReportMessage(excelBAOSHandler.getExcelStream(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
