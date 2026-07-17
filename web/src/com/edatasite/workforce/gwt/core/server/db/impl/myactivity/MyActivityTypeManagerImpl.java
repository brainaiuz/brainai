package com.edatasite.workforce.gwt.core.server.db.impl.myactivity;

import com.edatasite.workforce.core.domain.myactivity.EdsMyActivityType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myactivity.MyActivityTypeManager;
import org.springframework.stereotype.Repository;

/**
 * User: Ilhombek
 * Date: Dec 7, 2009
 * Time: 2:26:49 PM
 */
@Repository("myActivityTypeManager")
public class MyActivityTypeManagerImpl extends BaseManager<EdsMyActivityType> implements MyActivityTypeManager {

    public MyActivityTypeManagerImpl() {
        super(EdsMyActivityType.class);
    }

    public EdsMyActivityType getActivityType(String activityType) {
        return (EdsMyActivityType) findSingle("SELECT myact FROM EdsMyActivityType myact WHERE myact.type = ?", activityType);
    }

    public EdsMyActivityType getActivityType(String activityType, String activityTypeParent) {
        return (EdsMyActivityType) findSingle("SELECT myact FROM EdsMyActivityType myact WHERE myact.type = ? AND myact.parent.type = ?", activityType, activityTypeParent);
    }
}
