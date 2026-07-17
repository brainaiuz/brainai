package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductCategoryEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsProductCategory> TYPE = new WfmType<>(EventTypes.productCategoryEventListener);


    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        }
    }


    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsProductCategory placementObject = productCategoryManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProductCategoryAdd(placementObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsProductCategory placementObject = productCategoryManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProductCategoryEdit(placementObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsProductCategory placementObject = productCategoryManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProductCategoryDelete(placementObject, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }

        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
