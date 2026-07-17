package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Shohruh on 07-Oct-15.
 */
@Transactional
public class EndOfServiceSettingsEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EndOfServiceSettings> TYPE = new WfmType<>(EventTypes.endOfServiceSettingsEventListener);

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    EndOfServiceManager endOfServiceManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EndOfServiceSettings endOfServiceSettings = endOfServiceManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerEndOfServiceSettingsEditUpdate(endOfServiceSettings, creator, event.getTime());
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
}
