package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CompanySettingsEventListener implements BusinessEventListener {
    public static WfmType<EdsCompanySettings> TYPE = new WfmType<>(EventTypes.companySettingsEventListener);
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (event.getEventType().equals(EdsMyUpdate.ADD)) {
            onAddEvent(event);
        } else if (event.getEventType().equals(EdsMyUpdate.EDIT)) {
            onEditEvent(event);
        } else if (event.getEventType().equals(EdsMyUpdate.DELETE)) {
            onDeleteEvent(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsCompanySettings companySettings = companySettingsManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCompanySettingsEdit(companySettings, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }
}
