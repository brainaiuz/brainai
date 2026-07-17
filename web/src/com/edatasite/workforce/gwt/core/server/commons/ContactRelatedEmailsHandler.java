package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContactRelatedEmailsHandler implements HttpRequestHandler, Constants {

    private static final Logger log = LoggerFactory.getLogger(ContactRelatedEmailsHandler.class);

    @Autowired
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private RelationManager relationManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyID = request.getParameter("company_id");
        Integer emailSettingId = Integer.valueOf(request.getParameter("email_setting_id"));
        Integer folderId = Integer.valueOf(request.getParameter("folder_id"));
        log.info("Contact related emails fixer has been launched. CompanyID:[" + companyID + "], SettingsID:[" + emailSettingId +"]");

        List<EdsEmail> emails =emailRepository.getAllEmailsByFolderId(emailSettingId, companyID, folderId);

        log.info("Emails count: " + emails.size());
        for (EdsEmail email : emails) {
            List<EdsRelation> relations = relationManager.getAllRelations(RelationItem.TYPE_EMAIL_TRACKER, email.getTrackerId());
            if (relations != null && relations.size() > 0) {
                continue;
            }
            linkRelations(email.getId());
        }

    }

    //copy paste qilingan code (EmailTrackerRelationListener.java)
    private void linkRelations(String emailId) {
        Optional<EdsEmail> mailOptional = emailRepository.findById(emailId);
        if (!mailOptional.isPresent()) {
            return;
        }
        Integer companyId = SecurityContext.getCompanyID();

        log.info("Started creating email relations [" + emailId + "]");
        List<String> emails = Lists.newArrayList();
        EdsEmail mail = mailOptional.get();
        if (mail.getFrom() != null) {
            String fromEmail = mail.getFrom();
            if (fromEmail.contains("<") && fromEmail.contains(">") && fromEmail.indexOf("<") < fromEmail.indexOf(">")) {
                fromEmail = fromEmail.substring(fromEmail.indexOf("<") + 1, fromEmail.indexOf(">")).trim();
            }
            emails.add(fromEmail);
        }
        emails.add(mail.getTo());
        if (mail.getToCC() != null && !mail.getToCC().isEmpty()) {
            emails.addAll(Arrays.asList(mail.getToCC().trim().split(",")));
        }
        ArrayList<RelationItem> relationItems = Lists.newArrayList();
        for (String email : emails) {
            if (email == null) {
                continue;
            }
            log.info("Related email - [" + email + "]");
            EdsCrmContact contact = crmContactManager.getContactByEmail(email, companyId);
            if (contact != null) {
                RelationItem relationItem = RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, contact.getObjectID(), contact.getName());
                relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                relationItems.add(relationItem);
                if (contact.getCrmAccount() != null) {
                    RelationItem crmAccountRelation = RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, contact.getCrmAccount().getObjectID(), contact.getCrmAccount().getName());
                    crmAccountRelation.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                    relationItems.add(crmAccountRelation);
                }
                EdsCompanySettings edsCompanySettings = companySettingsManager.getCompanySettings(companyId);
                if (edsCompanySettings.getEmailAutoLinking()) {
                    EdsOpportunity opportunity = opportunityManager.getOpportunityByContactId(contact.getObjectID());
                    if (opportunity != null) {
                        RelationItem crmAccountRelation = RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID(), opportunity.getName());
                        crmAccountRelation.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                        relationItems.add(crmAccountRelation);
                    }
                }
            } else {
                EdsCrmContact lead = crmContactManager.getLeadByEmail(email, companyId);
                if (lead != null) {
                    RelationItem relationItem = RelationItem.newEventRelation(RelationItem.TYPE_LEAD, lead.getObjectID(), lead.getName());
                    relationItem.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                    relationItems.add(relationItem);
                    if (lead.getCrmAccount() != null) {
                        RelationItem crmAccountRelation = RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, lead.getCrmAccount().getObjectID(), lead.getCrmAccount().getName());
                        crmAccountRelation.setFromType(RelationItem.TYPE_EMAIL_TRACKER);
                        relationItems.add(crmAccountRelation);
                    }
                }
            }
        }
        if (relationItems.size() > 0) {
            allInOneService.saveRelations(RelationItem.TYPE_EMAIL_TRACKER, mail.getTrackerId(), mail.getSubject(), relationItems);
        }
    }
}
