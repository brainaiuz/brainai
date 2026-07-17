package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class KanbanCalculationEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCrmContact> TYPE = new WfmType<>(EventTypes.kanbanCalculateEventListener);
    public static WfmType<EdsOpportunity> TYPE_OPPORTUNITY = new WfmType<>(EventTypes.kanbanCalculateEventListener);
    public static WfmType<EdsCase> TYPE_CASE = new WfmType<>(EventTypes.kanbanCalculateEventListener);
    public static WfmType<EdsTask> TYPE_TASK = new WfmType<>(EventTypes.kanbanCalculateEventListener);

    @Autowired
    @Qualifier("kanbanService")
    private KanbanServiceLocal kanbanServiceLocal;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onKanbanEventAdded(event);
        }
    }

    private void onKanbanEventAdded(EdsBusinessEvent event) {
        Integer current = event.getEntityID();
        Integer prev = event.getSourceID();
        Integer next = event.getAdditionalSourceID();

        if (StringUtils.isNotBlank(event.getEntityType())) {
            if (RelationItem.TYPE_LEAD.equals(event.getEntityType())) {
                kanbanServiceLocal.setCrmContactKanbanOrder(prev, current, next, event.getCreatedByID());
            } else if (RelationItem.TYPE_OPPORTUNITY.equals(event.getEntityType())) {
                kanbanServiceLocal.setOpportunityKanbanOrder(prev, current, next, event.getCreatedByID());
            } else if (RelationItem.TYPE_CASE.equals(event.getEntityType())) {
                kanbanServiceLocal.setCaseKanbanOrder(prev, current, next, event.getCreatedByID());
            } else if (RelationItem.TYPE_TASK.equals(event.getEntityType())) {
                kanbanServiceLocal.setTaskKanbanOrder(prev, current, next, event.getCreatedByID());
            }
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
