package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 27.04.12
 * Time: 20:00
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CrmOpportunityEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsOpportunity> TYPE = new WfmType<>(EventTypes.crmOpportunityEventListener);
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private MessageManager messageManager;

    public static String OPPORTUNITY_APPROVED = "OPPORTUNITY_APPROVED";
    public static String OPPORTUNITY_SUBMITTED = "OPPORTUNITY_SUBMITTED";
    public static String OPPORTUNITY_REJECTED = "OPPORTUNITY_REJECTED";
    public static String OPPORTUNITY_DRAFT = "OPPORTUNITY_DRAFT";
    public static String OPPORTUNITY_ADD = "OPPORTUNITY_ADD";
    public static String OPPORTUNITY_EDIT = "OPPORTUNITY_EDIT";
    public static String OPPORTUNITY_DELETE = "OPPORTUNITY_DELETE";

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (OPPORTUNITY_APPROVED.equals(event.getEventType())) {
            onApproveEvent(event);
        } else if (OPPORTUNITY_REJECTED.equals(event.getEventType())) {
            onRejectEvent(event);
        } else if (OPPORTUNITY_DRAFT.equals(event.getEventType())) {
            onDraftEvent(event);
        } else if (OPPORTUNITY_SUBMITTED.equals(event.getEventType())) {
            onDraftEvent(event);
        }
    }

    private void onOpportunityCreated(EdsBusinessEvent event, boolean isNew) {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        if (event.getEntityID() != null) {
            EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
            if (opportunity != null && opportunity.getAssignee() != null && !opportunity.getAssignee().getObjectID().equals(event.getSourceID())) {
                messageManager.sendOpportunityAssigned(opportunity, opportunity.getAssignee(), isNew, event.getSourceID());
            }
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityAddUpdate(opportunity, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        if (event.getCustomStringField() != null && event.getCustomStringField().equals("email")) {
            onOpportunityCreated(event, true);
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityDeleteUpdate(opportunity, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityEditUpdate(opportunity, creator, event.getTime());
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
        if (event.getCustomStringField() != null && event.getCustomStringField().equals("email")) {
            onOpportunityCreated(event, false);
        }
    }

    private void onDraftEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityDraft(opportunity, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setProcessed(true);
        }
    }


    private void onApproveEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityApprove(opportunity, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setProcessed(true);
        }
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsOpportunity opportunity = opportunityManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerOpportunityReject(opportunity, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setProcessed(true);
        }

    }

}
