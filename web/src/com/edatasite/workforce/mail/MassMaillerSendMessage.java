package com.edatasite.workforce.mail;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.scheduler.BaseRecurrenceJob;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 21:36:22
 * To change this template use File | Settings | File Templates.
 */
public class MassMaillerSendMessage extends BaseRecurrenceJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Logger logger = LoggerFactory.getLogger(MassMaillerSendMessage.class);
        final Date date = new Date();
        logger.info("MassMailerSendMessage.execute started at :" + date.toString());

        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get("companyID");
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get("recObjectId");
        Integer mailMessageID = (Integer) jobExecutionContext.getMergedJobDataMap().get("busObjectId");
        Integer userID = (Integer) jobExecutionContext.getMergedJobDataMap().get("userID");
        String c = (String) jobExecutionContext.getMergedJobDataMap().get("busObjectParam");
        Integer loop = c != null && !"".equals(c) && c.matches(Constants.REGEX_INTEGER) ? Integer.parseInt(c) : 0;
        if (companyID != null) {
            String database = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID);
            ServerSecurityContext.getInstance().setDatabase(database);
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        if (userID != null) {
            SecurityContext.getInstance().setStaticUserID(userID);
        }
        logger.info("MassMailerSendMessage.execute companyID=" + companyID + "; mailMessageID=" + mailMessageID + "; recurrenceId=" + recurrenceId + "; Database type: " + ServerSecurityContext.getInstance().getDatabase());
        List<Object[]> items = massMailServiceLocal.getEntitiesForMassMail(mailMessageID, companyID, loop);
        if (mailMessageID != null && companyID != null && items != null && items.size() > 0) {
            try {
                massMailServiceLocal.sendMassMail(mailMessageID, companyID, items);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        recurrenceService.updateRecurrence(recurrenceId, true, true);
        if (items != null && items.size() >= Constants.MASSMAIL_LIMIT) {
            Date startDate = new Date();
            startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes() + 2);
            Date endDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes() + 10);
            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setEnabled(true);
            recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
            recurrenceJobItem.setJobType(SchedulerConstant.MASS_MAILING_RECURRENCE);
            recurrenceJobItem.setBusObjectId(mailMessageID);
            recurrenceJobItem.setInterval(1);
            recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
            recurrenceJobItem.setEndType(SchedulerConstant.END_BY_DATE);
            recurrenceJobItem.setEndDate(endDate);
            recurrenceJobItem.setStartDate(startDate);
            recurrenceJobItem.setYearlyMonth(startDate.getMonth() + 1);
            recurrenceJobItem.setMonthlyOrYearlyDay(startDate.getDate());
            recurrenceJobItem.setBusObjectParams((loop + 1) + "");
            recurrenceService.saveRecurrenceJob(recurrenceJobItem);
        } else {
            massMailServiceLocal.updateMessageStatusToSent(mailMessageID);
        }
        logger.info("MassMailerSendMessage.execute finished in :" + (new Date().getTime() - date.getTime()));
    }
}
