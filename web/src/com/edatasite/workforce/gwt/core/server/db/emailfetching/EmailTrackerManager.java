package com.edatasite.workforce.gwt.core.server.db.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/30/11
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailTrackerManager extends Manager<EdsEmailTracker> {
    EdsEmailTracker getByCode(String[] code);

    Integer getCaseIDByTrackerID(Integer trackerID);
}
