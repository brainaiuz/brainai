package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsBugAttachment;
import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.BugReportManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static com.edatasite.shared.sms.SmsProvider.log;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 9/3/11
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class BugReportRegistrationCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsBugReport> TYPE = new WfmType<>(EventTypes.bugReportRegistrationCustomEventListener);
    public static final String EVENT_CASE_REGISTRATION = "EVENT_CASE_REGISTRATION";
    public static final String EMAIL_BUG_REPORT_REGISTRATION = "EMAIL_BUG_REPORT_REGISTRATION";

    @Autowired
    private BugReportManager bugReportManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_CASE_REGISTRATION.equals(event.getEventType())) {
            saveCRMCaseReport(event);
        } else if (EMAIL_BUG_REPORT_REGISTRATION.equals(event.getEventType())) {
            sendEmailBugReport(event);
        }
    }

    private void saveCRMCaseReport(EdsBusinessEvent event) {
        EdsBugReport bugReport = bugReportManager.get(event.getEntityID());
        EdsReference reference = referenceManager.findReference(EdsCase._CASE_STATUS, EdsCase.NEW);
        if (bugReport != null) {
            String OldCmpanyId = SecurityContext.getInstance().getCompanyId();
            EdsUser olduser = (EdsUser) SecurityContext.getInstance().getUser();

            if ("65159".equals(OldCmpanyId)) {
                log.warn("TEMP FIX: Feedback from company 65159 skipped to prevent infinite loop. BugReport id: " + event.getEntityID());
                return;
            }

            SecurityContext.getInstance().setCompanyId("65159");
            EdsUser ourCompanyUserForExampleDiyorbek = userManager.get(414);
            EdsUser ourCompanyUserForExampleSasha = userManager.get(447);
            if (ourCompanyUserForExampleDiyorbek == null) {
                return;
            }
            SecurityContext.getInstance().setStaticUserID(ourCompanyUserForExampleDiyorbek.getObjectID());
            CaseItem caseItem = new CaseItem();
            caseItem.setDescription(bugReport.getDescription() +" ( This is the ID of the company that wrote the Feedback : " + OldCmpanyId.toString() + " )" );
            caseItem.setSubject(commonLocalizer.localize(EdsSubjects.BUG_REPORT_TITLE));
            if (bugReport.getSubject() != null && !"".equals(bugReport.getSubject())) {
                caseItem.setSubject(bugReport.getSubject());
            }
            caseItem.setEmail(bugReport.getEmail());
            caseItem.setCompany(bugReport.getCompanyName());
            caseItem.setLastName(bugReport.getCreatorName());
            caseItem.setReportedBy(bugReport.getCreatorName());
            caseItem.setLeadId(210609);
            caseItem.setEmail(bugReport.getEmail());
            caseItem.setStatus(new SelectItem(reference.getObjectID(),reference.getName(),reference.getDescription(),reference.getCode()));
            crmService.saveCase(caseItem, false);
            SecurityContext.getInstance().setCompanyId(OldCmpanyId);
            SecurityContext.getInstance().setStaticUserID(olduser.getObjectID());
        }
        event.setProcessed(false);
    }

    private void sendEmailBugReport(EdsBusinessEvent event) {
        EdsBugReport bugReport = bugReportManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        String messageContent = bugReport.getDescription();
        String subjectContent = bugReport.getSubject();
        String viewSection = bugReport.getCreatedFrom();
        Date creationDate = bugReport.getCreationTime();
        boolean hasAttachment = false;
        ArrayList<Integer> attachmentIds = new ArrayList<>();
        if (bugReport.getBugAttachments() != null && bugReport.getBugAttachments().size() > 0) {
            for (EdsBugAttachment attachment : bugReport.getBugAttachments()) {
                attachmentIds.add(attachment.getAttachmentID());
                hasAttachment = true;
            }
        }

        try {
            messageManager.sendBugReport(creator, messageContent, subjectContent, viewSection, creationDate, hasAttachment, attachmentIds);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (EdsDbException | EdsTemplateException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }
}
