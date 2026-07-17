package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek
 * Date: 05.07.12
 * Time: 20:34
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class FixedAssetEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsFixedAsset> TYPE = new WfmType<>(EventTypes.fixedAssetEventListener);
    public static String EVENT_FIXED_ASSET_DISPOSE = "EVENT_FIXED_ASSET_DISPOSE";
    public static String EVENT_FIXED_ASSET_OWNER_ADD = "EVENT_FIXED_ASSET_OWNER_ADD";
    public static String EVENT_FIXED_ASSET_OWNER_EDIT = "EVENT_FIXED_ASSET_OWNER_EDIT";
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private FixedAssetManager fixedAssetManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_FIXED_ASSET_DISPOSE.equalsIgnoreCase(event.getEventType())) {
            onDisposeEvent(event);
        } else if (EVENT_FIXED_ASSET_OWNER_ADD.equalsIgnoreCase(event.getEventType())) {
            onOwnerAddEvent(event);
        } else if (EVENT_FIXED_ASSET_OWNER_EDIT.equalsIgnoreCase(event.getEventType())) {
            onOwnerEditEvent(event);
        }

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsFixedAsset fixedAsset = fixedAssetManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetAddUpdate(fixedAsset, creator, event.getTime());
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

        EdsUser creator = userManager.get(event.getSourceID());
        EdsFixedAsset fixedAsset = fixedAssetManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetEditUpdate(fixedAsset, creator, event.getTime());
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

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetDeleteUpdate(event.getEntityID(), event.getCustomStringField(), creator, event.getTime());
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

    public void onDisposeEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetDisposeUpdate(event.getEntityID(), event.getCustomStringField(), creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }
    }

    public void onOwnerAddEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsFixedAsset fixedAsset = fixedAssetManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetOwnerAddUpdate(fixedAsset, creator, event.getTime());
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

    public void onOwnerEditEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsFixedAsset fixedAsset = fixedAssetManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerFixedAssetOwnerEditUpdate(fixedAsset, creator, event.getTime());
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
