package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 2:03:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventItem extends Appointment implements IsSerializable {
    public ActivityItem asActivityItem() {
        ActivityItem item = new ActivityItem();
        item.setCallLog(isCallLog());
        item.setEventObjectId(getObjectID());
        item.setEntityId(getObjectID());
        item.setSubject(getSubject());
        item.setActivityType(isCallLog() ? CrmConstants.CRM_EVENT_CALLOG : isInterview() ? CrmConstants.CRM_EVENT_INTERVIEW : Appointment.SMS == getActivityType() ? CrmConstants.SMS : CrmConstants.CRM_EVENT);
        item.setCreationDate(getCreatedDate());
        if (getStartDate() != null) {
            item.setStartDate(getStartDate());
        } else {
            item.setStartDate(null);
            item.setSStartDate("");
        }
        if (getEndDate() != null) {
            item.setDueDate(getEndDate());
        } else {
            item.setDueDate(null);
            item.setSDueDate("");
        }
        item.setStatus(isCallLog() ? (isInboundCall() ? CrmConstants.INBOUND : CrmConstants.OUTBOUND) : Appointment.SMS == getActivityType() ? CrmConstants.SENT : "");
        item.setPriority("");
        item.setInvitationResponse(getInvitationResponse());
        return item;
    }
}
