package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomFormItemsEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCustomFormItems> TYPE = new WfmType<>(EventTypes.customFormItemsEventListener);

    public static String EVENT_CUSTOM_FROM_ITEM_MANAGER_REJECT = "EVENT_CUSTOM_FROM_ITEM_MANAGER_REJECT";
    public static String EVENT_CUSTOM_FROM_ITEM_MANAGER_APPROVE = "EVENT_CUSTOM_FROM_ITEM_MANAGER_APPROVE";
    public static String EVENT_CUSTOM_FROM_ITEM_SUBMITTED_TO_MANAGER = "EVENT_CUSTOM_FROM_ITEM_SUBMITTED_TO_MANAGER";

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_CUSTOM_FROM_ITEM_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_CUSTOM_FROM_ITEM_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_CUSTOM_FROM_ITEM_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        }
    }

    public void onAddEvent(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormItemAdd(cfItem.getObjectID(), creator, event.getTime(),
                                                                                 cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
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
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormItemEdit(cfItem.getObjectID(), creator, event.getTime(),
                                                                                  cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
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
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormItemDelete(cfItem.getObjectID(), creator, event.getTime(),
                                                                                    cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
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
    }

    public void onSubmittedToManager(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormSubmittedToManager(cfItem.getObjectID(), creator, event.getTime(),
                                                                                        cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser manager = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormManagerApproveUpdate(cfItem.getObjectID(), manager, event.getTime(),
                                                                                          cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    private void onManagerRejectEvent(EdsBusinessEvent event) {
        EdsCustomFormItems cfItem = customFormItemManager.get(event.getEntityID());
        EdsUser manager = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerCustomFormManagerRejectUpdate(cfItem.getObjectID(), manager, event.getTime(),
                                                                                         cfItem.getCustomForm() != null ? cfItem.getCustomForm().getFormID() : null);
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }
}
