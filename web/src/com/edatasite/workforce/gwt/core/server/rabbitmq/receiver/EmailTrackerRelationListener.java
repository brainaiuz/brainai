package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EmailTrackerRelationListener extends BaseAmqpListener<String> {

    private static final Logger log = LoggerFactory.getLogger(EmailTrackerRelationListener.class);

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

    @Override
    protected void receiveMessage(String emailId) {
        Optional<EdsEmail> mailOptional = emailRepository.findById(emailId);
        if (!mailOptional.isPresent()) {
            return;
        }
        EdsEmail mail = mailOptional.get();
        Integer companyId = SecurityContext.getCompanyID();

        log.info("Started creating email relations [" + emailId + "]");
        List<String> emails = Lists.newArrayList();
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

    @Override
    protected DataMQ<String> convertMessage(String message) {
        Gson gson = new Gson();
        DataMQ<String> rawData = gson.fromJson(message, new TypeToken<DataMQ<String>>() {
        }.getType());
        Optional<EdsEmail> emailOptional = emailRepository.findById(rawData.getDataMQ());
        String clusterType = rawData.getClusterType();
        if (emailOptional.isPresent() && rawData.getClusterType() == null) {
            EdsEmail email = emailOptional.get();
            clusterType = email.getClusterType();
        }
        DataMQ<String> finalData = new DataMQ<>();
        finalData.setDataMQ(rawData.getDataMQ());
        finalData.setCompanyId(rawData.getCompanyId());
        finalData.setClusterType(clusterType);

        return finalData;
    }
}
