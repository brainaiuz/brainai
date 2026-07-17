package com.edatasite.workforce.gwt.core.server.db.myactivity;

import com.edatasite.workforce.core.domain.myactivity.EdsMyActivity;
import com.edatasite.workforce.core.domain.myactivity.EdsMyActivityLog;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:32:45 PM
 */
public interface MyActivityLogManager extends Manager<EdsMyActivityLog> {
    EdsMyActivityLog registerTaskActivity(EdsMyActivity myActivity, String currentValue, String previousValue, String activityType);
}
