package com.edatasite.workforce.gwt.core.server.db.impl.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.myactivity.EdsMyActivity;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:27:28 PM
 */
@Repository("myActivityManager")
public class MyActivityManagerImpl extends BaseManager<EdsMyActivity> implements MyActivityManager {

    public MyActivityManagerImpl() {
        super(EdsMyActivity.class);
    }

    @Autowired
    private MyActivityTypeManager myActivityTypeManager;

    public EdsMyActivity registerMyTaskActivity(EdsTask task, String activityType) {
        EdsMyActivity myActivity = new EdsMyActivity();
        myActivity.setActivityDate(new Date());
        myActivity.setEntityID(task.getObjectID());
        myActivity.setMyActivityType(myActivityTypeManager.getActivityType(activityType));
        myActivity.setUser(getUser());
        create(myActivity);
        return myActivity;
    }

    public EdsMyActivity registerMyTaskActivity(EdsTask task, String activityType, String entityType) {
        EdsMyActivity myActivity = new EdsMyActivity();
        myActivity.setActivityDate(new Date());
        myActivity.setEntityID(task.getObjectID());
        myActivity.setMyActivityType(myActivityTypeManager.getActivityType(activityType));
        myActivity.setUser(getUser());
        create(myActivity);
        return myActivity;
    }

    public EdsMyActivity registerMyActivity(EdsObject entity, String activityType, String entityType) {
        EdsMyActivity myActivity = new EdsMyActivity();
        myActivity.setActivityDate(new Date());
        myActivity.setEntityID(entity.getObjectID());
        myActivity.setMyActivityType(myActivityTypeManager.getActivityType(activityType));
        myActivity.setUser(getUser());

        create(myActivity);
        return myActivity;
    }
}
