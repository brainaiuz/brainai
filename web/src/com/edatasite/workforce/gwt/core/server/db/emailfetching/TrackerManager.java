package com.edatasite.workforce.gwt.core.server.db.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsTracker;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by Azazello on 3/30/15.
 */
public interface TrackerManager extends Manager<EdsTracker> {
    Integer getByMessageIDs(Integer emailSettingId, String... messageIDs);
}
