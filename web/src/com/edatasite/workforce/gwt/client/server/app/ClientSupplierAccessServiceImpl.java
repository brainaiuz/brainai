package com.edatasite.workforce.gwt.client.server.app;

import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/24/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ClientSupplierAccessServiceImpl implements ClientSupplierAccessService, Constants {

    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    PasswordGenerator pg = new PasswordGenerator(6);

    @Override
    @Transactional
    public Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm, boolean sendActivationEmail) {
        EdsCrmContact crmContact = crmContactManager.get(contactID);
        if (crmContact.getPrimaryEmail() == null) {
            return -1;
        }
        EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(contactID);

        if (clientContact == null) {
            Integer clientContactCount = clientContactManager.getClientContactsCount(crmContact.getPrimaryEmail());
            if (clientContactCount != null && clientContactCount > 0) {
                return -1;//If client with this email already exists within this company, return error
            }

            if (userManager.findUser(crmContact.getPrimaryEmail().toLowerCase()) != null) {
                return -1;//User with this username already exists
            }
            clientContact = createClientContact(crmContact, fromSubscriptionForm, sendActivationEmail);
        } else {
            clientContact.setAccess(true);
        }

        crmContact.setEntityContactID(clientContact.getObjectID());
        crmContact.setAccessEnabled(true);
        crmContact.addChange("ACCESS_ENABLED");
        crmContactManager.update(crmContact);
        try {
            contactSolrComponent.index(crmContact);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        EdsBusinessEvent workflowEvent = baseEventsPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmContact, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CONTACT);

        if (clientContact.hasRole(EdsRole.CLIENT_CODE)) {
            initClientGroups(clientContact);
        }

        return clientContact.getObjectID();
    }

    private EdsClientContact createClientContact(EdsCrmContact crmContact, Boolean fromSubscriptionForm, boolean sendActivationEmail) {
        if (crmContact.getPrimaryEmail() == null) {
            return null;
        }

        EdsClientContact clientContact = new EdsClientContact();
        clientContact.setFirstName(crmContact.getFirstName());
        clientContact.setUserName(crmContact.getPrimaryEmail());
        clientContact.setLastName(crmContact.getLastName());
        clientContact.setEmail(crmContact.getPrimaryEmail());
        clientContact.setCrmContact(crmContact);

        clientContact.setAccess(true);
        clientContact.setRandom(ServerUtils.randomstring());
        boolean userNameExist = false;

        if (fromSubscriptionForm != null && fromSubscriptionForm) {
            String password = pg.generateAsString();
//            clientContact.setActive(true);
            clientContact.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            clientContact.setPassword(password);
        } else {
            String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), crmContact.getPrimaryEmail());
            if (password == null) {
                password = pg.generateAsString();
                clientContact.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING));
            } else {
                userNameExist = true;
//                clientContact.setActive(true);
                clientContact.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
                clientContact.setPassword(password);
            }
        }

        Set<EdsReference> crmAccountTypes = crmContact.getCrmAccount().getAccountTypes();
        for (EdsReference accType : crmAccountTypes) {
            if (CrmAccountItem.CUSTOMER.equals(accType.getCode())) {
                roleManager.addRole(clientContact, EdsRole.CLIENT);
            }
            if (CrmAccountItem.SUPPLIER.equals(accType.getCode())) {
                clientContact.addRole(roleManager.getByCode(Constants.SUPPLIER));
                clientContactManager.update(clientContact);
            }
        }

        clientContactManager.create(clientContact);
        userManager.saveUserAuthenticationData(clientContact, Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()), !userNameExist, false);

        if (sendActivationEmail) {
            if (fromSubscriptionForm == null || !fromSubscriptionForm) {
                sendActivationLinkToClientContact(clientContact, userNameExist);
            } else {
                sendUsernamePasswordToClientContact(clientContact, userNameExist);
            }
        }

        return clientContact;
    }

    @Override
    public Integer disableAccess(Integer contactID) {
        EdsCrmContact crmContact = crmContactManager.get(contactID);
        crmContact.setAccessEnabled(false);
        crmContact.addChange("ACCESS_ENABLED");
        crmContactManager.update(crmContact);
        try {
            contactSolrComponent.index(crmContact);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        EdsBusinessEvent workflowEvent = baseEventsPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, crmContact, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CONTACT);

        EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(contactID);
        if (clientContact != null) {
            clientContact.setAccess(false);
            clientContactManager.update(clientContact);
            return clientContact.getObjectID();
        } else {
            return null;
        }
    }

    public Boolean sendActivationLinkToClientContact(EdsClientContact clientContact, boolean userNameExist) {
        boolean isSendMessage;
        EdsUser creator = userManager.getUser();
        String subject = clientContact.getAccessMailSubject(creator.getCompany());
        try {
            if (userNameExist) {
                messageManager.sendToClientWithoutActivationLink(clientContact, creator, subject);
                isSendMessage = true;
            } else {
                messageManager.sendToClient(clientContact, creator, subject);
                isSendMessage = true;
            }
        } catch (EdsDbException | EdsTemplateException e) {
            e.printStackTrace();
            isSendMessage = false;

        }
        return isSendMessage;
    }

    private Boolean sendUsernamePasswordToClientContact(EdsClientContact clientContact, boolean userNameExist) {
        boolean isSendMessage;

        EdsCompany company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));

        String subject = clientContact.getAccessMailSubject(company);
        try {
            if (userNameExist) {
                messageManager.sendToClientWithoutUsernamePassword(clientContact, subject);
                isSendMessage = true;
            } else {
                messageManager.sendToClientUsernamePassword(clientContact, subject);
                isSendMessage = true;
            }
        } catch (EdsDbException | EdsTemplateException e) {
            e.printStackTrace();
            isSendMessage = false;

        }
        return isSendMessage;
    }

    @Override
    public void initClientGroups(EdsUser client) {
        EdsTrustee userTrustee = trusteeManager.getTrustee(client);
        client.getMembershipGroups().clear();
        if (userTrustee == null) {
            userTrustee = trusteeManager.getTrustee(client);
        }
        if (client.getRoles() != null && client.getRoles().size() > 0) {
            for (EdsRole role : client.getRoles()) {
                if (EdsRole.CLIENT.equals(role.getObjectID())) {
                    EdsGroup clients = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);
                    client.getMembershipGroups().add(clients);
                    clients.getMembers().add(userTrustee);
                }
            }
        } else if (client.isClientContact()) {
            EdsGroup clients = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);
            client.getMembershipGroups().add(clients);
            clients.getMembers().add(userTrustee);
        }
    }
}
