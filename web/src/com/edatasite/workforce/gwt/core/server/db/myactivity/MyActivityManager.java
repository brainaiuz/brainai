package com.edatasite.workforce.gwt.core.server.db.myactivity;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.myactivity.EdsMyActivity;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:36:28 PM
 */
public interface MyActivityManager extends Manager<EdsMyActivity> {
//    public EdsMyActivity registerMyTaskActivity(EdsTask task, String activityType);
//    public EdsMyActivity registerMyTaskActivity(EdsTask task, String activityType,String entityType);

    EdsMyActivity registerMyActivity(EdsObject entity, String activityType, String entityType);
}
