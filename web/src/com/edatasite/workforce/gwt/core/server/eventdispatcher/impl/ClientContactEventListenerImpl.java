package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.mail.EdsSubjects;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 6, 2010
 * Time: 5:10:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ClientContactEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsClientContact> TYPE = new WfmType<>(EventTypes.clientContactEventListener);
    public static final String EVENT_IMPORT_CONTACT_FROM_ACCOUNTING = "EVENT_IMPORT_CONTACT_FROM_ACCOUNTING";
    public static final String EVENT_TYPE_STOREFRONT_ADD = "EVENT_TYPE_STOREFRONT_ADD";
    public static final String EVENT_TYPE_ADD = "ADD";
    public static final String EVENT_TYPE_EDIT = "EDIT";
    public static final String EVENT_TYPE_DELETE = "DELETE";
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_TYPE_ADD.equals(event.getEventType())) {
            onAddEvent(event);
        } else if (EVENT_TYPE_EDIT.equals(event.getEventType())) {
            onEditEvent(event);
        } else if (EVENT_TYPE_DELETE.equals(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_IMPORT_CONTACT_FROM_ACCOUNTING.equals(event.getEventType())) {
            EdsCompany company = userManager.get(event.getSourceID()).getCompany();
            EdsClientContact clientContact = clientContactManager.get(event.getEntityID());
            sendClientAddNotification(event, clientContact.getAccessMailSubject(company)/*EdsSubjects.CLIENT_ACCESS_TO + company.getName()*/);
        } else if (EVENT_TYPE_STOREFRONT_ADD.equals(event.getEventType())) {
            EdsCompany company = userManager.get(event.getSourceID()).getCompany();
            sendClientAddNotification(event, commonLocalizer.localize(EdsSubjects.STOREFRONT_CLIENT_MAIL_SUBJECT) + company.getName());
        }
    }

    public void onAddEvent(EdsBusinessEvent event) {
        sendClientAddNotification(event, commonLocalizer.localize(EdsSubjects.EMPLOYEE_ADD_NOTIFICATION));
    }

    private void sendClientAddNotification(EdsBusinessEvent event, String subject) {
        EdsClientContact clientContact = clientContactManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isSendMail1()) {
            try {
                if (clientContact.getAccess() != null && clientContact.getAccess()) {
                    String existingUsers = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), clientContact.getEmail());
                    boolean clientContactExist = false;
                    if (existingUsers != null) {
                        clientContactExist = true;
                    }
                    try {
                        if (EVENT_TYPE_STOREFRONT_ADD.equals(event.getEventType())) {
                            messageManager.sendToStoreFrontClient(clientContact, creator, subject);
                        } else if (clientContactExist) {
                            messageManager.sendToClientWithoutActivationLink(clientContact, creator, subject);
                        } else {
                            messageManager.sendToClient(clientContact, creator, subject);
                        }
                        event.setSendMail1(true);
                    } catch (EdsDbException | EdsTemplateException e) {
                        event.setSendMail1(false);
                    }
                } else {
                    event.setSendMail1(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                event.setSendMail1(false);
            }
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerClientContactAddUpdate(clientContact, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSendMail1() && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsClientContact clientContact = clientContactManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerClientContactEditUpdate(clientContact, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsClientContact clientContact = clientContactManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerClientContactDeleteUpdate(clientContact, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
