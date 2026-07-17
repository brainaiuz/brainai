package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Ilhombek
 * Date: 16.06.2010
 * Time: 17:46:45
 */
@Transactional
public class NoteEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsNoteHistory> TYPE = new WfmType<>(EventTypes.noteEventListener);
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                if (noteHistory != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerNoteAddUpdate(noteHistory, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
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
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                if (noteHistory != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerNoteEditUpdate(noteHistory, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
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
    }

}
