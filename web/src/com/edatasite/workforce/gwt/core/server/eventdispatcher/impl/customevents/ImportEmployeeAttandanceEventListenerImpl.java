package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 19.08.2010
 * Time: 17:26:09
 */
@Transactional
public class ImportEmployeeAttandanceEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.importEmployeeAttandenceCustomEventListener);
    public static String EVENT_ATTANDANCE_ADD_TO_DATABASE = "EVENT_ATTANDANCE_ADD_TO_DATABASE";

    @Autowired
    private AvailabilityServiceLocal availabilityService;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_ATTANDANCE_ADD_TO_DATABASE.equals(event.getEventType())) {
            onAdd(event);
        }
    }


    private void onAdd(EdsBusinessEvent event) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCompanyID(event.getCompanyId());

        ServerSecurityContext.getInstance().setCompanyId(fp.getCompanyID());
        String employeeIDs = event.getCustomStringField();
        if (employeeIDs != null && !"".equals(employeeIDs)) {
            List<Integer> ids = ServerUtils.getStringAsList(event.getCustomStringField(), ",");
            if (ids != null && ids.size() > 0) {
                for (Integer employeeId : ids) {
                    availabilityService.createOrUpdateLeaveAllowance(employeeId);
                    availabilityService.createAttendaceRawDataRecords(employeeId, 0);
                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
