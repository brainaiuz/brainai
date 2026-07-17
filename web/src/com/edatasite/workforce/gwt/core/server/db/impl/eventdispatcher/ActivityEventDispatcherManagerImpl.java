//package com.edatasite.workforce.gwt.core.server.db.impl.eventdispatcher;
//
//import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
//import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.ActivityEventDispatcherManager;
//import com.edatasite.workforce.core.domain.businessevent.EdsActivityEvent;
//import com.edatasite.workforce.core.domain.myactivity.EdsMyActivity;
//
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.dao.DataAccessException;
//
///**
// * User: Abdulaziz
// * Date: Dec 16, 2009
// * Time: 3:13:02 PM
// */
//public class ActivityEventDispatcherManagerImpl extends BaseManager<EdsActivityEvent> implements ActivityEventDispatcherManager {
//    public ActivityEventDispatcherManagerImpl() {
//        super(EdsActivityEvent.class);
//    }
//
//    public EdsActivityEvent regregisterActivityEvent(EdsMyActivity activity, Integer entity,String eventType,String processorName){
//        EdsActivityEvent event = new EdsActivityEvent();
//        event.setActivity(activity);
//        event.setEventType(eventType);
//        event.setSourceID(activity.getUser().getObjectID());
//        event.setEntityID(entity);
//        event.setProcessorName(processorName);
//        event.setProcessed(false);
//        event.setTime(new Date());
//        create(event);
//        return event;
//    }
//    public List<EdsActivityEvent> getActivityEvents(Integer activityID){
//        return (List<EdsActivityEvent>) find("SELECT ae FROM EdsActivityEvent ae WHERE ae.activity.objectID = ?",activityID);
//    }
//    public void removeEvent(EdsActivityEvent event){
//
//        try {
//            updateNative("delete from activityevent where id = "+event.getObjectID());
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            updateNative("delete from businessevent where id = "+event.getObjectID());
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
