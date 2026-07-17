package com.edatasite.workforce.gwt.core.server.db.impl.myactivity;

import com.edatasite.workforce.core.domain.myactivity.EdsMyActivity;
import com.edatasite.workforce.core.domain.myactivity.EdsMyActivityLog;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityLogManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityTypeManager;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:27:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyActivityLogManagerImpl extends BaseManager<EdsMyActivityLog> implements MyActivityLogManager {

    public MyActivityLogManagerImpl() {
        super(EdsMyActivityLog.class);
    }

    private MyActivityTypeManager myActivityTypeManager;

    public MyActivityTypeManager getMyActivityTypeManager() {
        return myActivityTypeManager;
    }

    public void setMyActivityTypeManager(MyActivityTypeManager myActivityTypeManager) {
        this.myActivityTypeManager = myActivityTypeManager;
    }

    public EdsMyActivityLog registerTaskActivity(EdsMyActivity myActivity, String currentValue, String previousValue, String activityType) {
        EdsMyActivityLog myLog = new EdsMyActivityLog();
        myLog.setCurrentValue(currentValue);
        myLog.setPreviousValue(previousValue);
        myLog.setMyActivityType(myActivityTypeManager.getActivityType(activityType));
        myLog.setActivity(myActivity);
        return myLog;
    }
}
