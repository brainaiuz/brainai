package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUserSessionTracker;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 15.07.2009
 * Time: 17:35:41
 * To change this template use File | Settings | File Templates.
 */
public interface UserSessionTrackerManager extends Manager<EdsUserSessionTracker> {

    List<EdsUserSessionTracker> list(Integer userSeesionID);
}
