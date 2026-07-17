package com.edatasite.workforce.gwt.core.server.db.myactivity;

import com.edatasite.workforce.core.domain.myactivity.EdsMyActivityType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:30:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MyActivityTypeManager extends Manager<EdsMyActivityType> {
    EdsMyActivityType getActivityType(String activityType);

    EdsMyActivityType getActivityType(String activityType, String activityTypeParent);
}
