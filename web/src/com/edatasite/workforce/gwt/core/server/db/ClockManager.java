package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsClock;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 7:09:22 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ClockManager extends Manager<EdsClock> {

    List<EdsClock> getClockItems(Integer type);

    List<EdsClock> getClockItems(Integer busObjectId, Integer type);

    EdsClock getClockItem(Integer ownerId, Integer busObjectId, Integer type);

    EdsClock getActiveClockForCurrentUser(Integer busObjectId, Integer type, Integer userID);

    Map<Integer, Integer> getActiveClockMapForCurrentUser(String busObjectIds, Integer type, Integer userID);

    Boolean anyActiveTimerExistsForUser(Integer userID);

    List<EdsClock> getClockItemsByUser(EdsUser user);

    List<EdsClock> getStopedClockItemsByUser(EdsUser user);

    List<EdsClock> getClockItemsByUserAndType(EdsUser user, Integer newType);
}
