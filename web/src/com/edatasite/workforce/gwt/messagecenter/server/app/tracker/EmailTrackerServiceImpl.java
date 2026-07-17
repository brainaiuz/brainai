package com.edatasite.workforce.gwt.messagecenter.server.app.tracker;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service("emailTrackerService")
public class EmailTrackerServiceImpl implements EmailTrackerService {

    private static final DecimalFormat trackerNumberFormat = new DecimalFormat("00000");

    @Autowired
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailTrackerManager emailTrackerManager;
    @Autowired
    private CrmContactManager crmContactManager;

    @Override
    public EdsEmailTracker createTracker(EdsEmailTracker tracker) {
        EntityManager em = jpaTemplate.createHibernateEntityManager();
        Transaction tx = null;

        try (org.hibernate.Session session = em.unwrap(org.hibernate.Session.class)) {
            tx = session.getTransaction();
            tx.begin();
            if (tracker == null) {
                tracker = new EdsEmailTracker();
                session.persist(tracker);
                session.flush();

                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                if (settings != null && settings.getTrackerPrefix() != null) {
                    tracker.setPrefix(settings.getTrackerPrefix());
                } else {
                    tracker.setPrefix(SecurityContext.getCompanyID().toString() + "-");
                }
            }
            if (tracker.getCounter() == null) {
                tracker.setCounter(tracker.getObjectID());
            }
            tracker.setCode((tracker.getPrefix() != null ? tracker.getPrefix() : "") + trackerNumberFormat.format(tracker.getCounter().doubleValue()));

            session.update(tracker);
            session.flush();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return tracker;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addTrackerToCrmContactOrLead(Integer trackerID, String edsEmailID) {
        EdsEmailTracker emailTracker = emailTrackerManager.get(trackerID);
        EdsEmail edsEmail = emailRepository.findById(edsEmailID).get();
        Integer companyID = SecurityContext.getCompanyID();
        String fromEmail = edsEmail.getFrom();
        Set<String> emails = new HashSet<>();
        EdsCrmContact crmContact;

        String toEmail = edsEmail.getTo();
        if (toEmail != null && toEmail.length() > 0) {
            for (String email : toEmail.split(",")) {
                email = deleteNameFromEmail(email);
                if (email != null && !"".equalsIgnoreCase(email)) {
                    emails.add(email);
                }
            }
        }
        fromEmail = deleteNameFromEmail(fromEmail);
        if (fromEmail != null && !"".equalsIgnoreCase(fromEmail)) {
            emails.add(fromEmail);
        }

        for (String email : emails) {
            crmContact = crmContactManager.getContactByEmail(email, companyID);
            if (crmContact != null) {
                crmContact.addTracker(emailTracker);
            }
            crmContact = crmContactManager.getLeadByEmail(email, companyID);
            if (crmContact != null) {
                crmContact.addTracker(emailTracker);
            }
        }
    }

    private String deleteNameFromEmail(String str) {
        if (str.contains("<")) {
            str = str.substring(str.indexOf("<") + 1, str.indexOf(">"));
        }
        return str;
    }
}
