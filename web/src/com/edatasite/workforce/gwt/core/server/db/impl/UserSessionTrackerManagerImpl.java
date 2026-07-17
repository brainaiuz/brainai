package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUserSessionTracker;
import com.edatasite.workforce.gwt.core.server.db.UserSessionTrackerManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 15.07.2009
 * Time: 17:37:54
 * To change this template use File | Settings | File Templates.
 */
@Repository("userSessionTrackerManager")
public class UserSessionTrackerManagerImpl extends BaseManager<EdsUserSessionTracker> implements UserSessionTrackerManager {

    public UserSessionTrackerManagerImpl() {
        super(EdsUserSessionTracker.class);
    }

    public List<EdsUserSessionTracker> list(Integer userSeesionID) {
        return find("select user from EdsUserSessionTracker user where user.userSession.objectID = ?", userSeesionID);
    }
}
