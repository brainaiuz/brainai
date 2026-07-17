package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsClock;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.ClockManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 7:10:06 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("clockManager")
public class ClockManagerImpl extends BaseManager<EdsClock> implements ClockManager {

    public ClockManagerImpl() {
        super(EdsClock.class);
    }

    public List<EdsClock> getClockItems(Integer type) {
        return find("select clock from EdsClock clock where clock.relation = ?", type);
    }

    public List<EdsClock> getClockItems(Integer busObjectId, Integer type) {
        return find("select clock from EdsClock clock where clock.busObjectId = ? and clock.relation = ? order by clock.startDate desc", busObjectId, type);
    }

    public EdsClock getClockItem(Integer ownerId, Integer busObjectId, Integer type) {
        if (ownerId == null || busObjectId == null || type == null) {
            return null;
        }
        return (EdsClock) findSingle("select clock from EdsClock clock where clock.owner.objectID = ? and clock.busObjectId = ? and clock.relation = ?", ownerId, busObjectId, type);
    }

    public EdsClock getActiveClockForCurrentUser(Integer busObjectId, Integer type, Integer userID) {
        return (EdsClock) findSingle("select clock from EdsClock clock where clock.busObjectId = ? and clock.relation = ? and clock.started = true and clock.owner.objectID = ?", busObjectId, type, userID);
    }

    public Map<Integer, Integer> getActiveClockMapForCurrentUser(String busObjectIds, Integer type, Integer userID) {
        List<Object[]> listObjects = (List<Object[]>) find("select clock.busObjectId,clock.objectID from EdsClock clock where clock.busObjectId IN (" + busObjectIds + ") and clock.relation = ? and clock.started = true and clock.owner.objectID = ?", type, userID);
        Map<Integer, Integer> taskClockMap = new HashMap<>();
        for (Object[] objects : listObjects) {
            taskClockMap.put((Integer) objects[0], (Integer) objects[1]);
        }
        return taskClockMap;
    }

    @Override
    public Boolean anyActiveTimerExistsForUser(Integer userID) {
        String companyID = getCompanyId();
        String sql = "select count(c.id) from " + /*35159*/companyID + ".clock c\n" +
                " left join " + companyID + ".task t on t.id = c.busobjectid \n" +
                " left join " + companyID + ".employeetask et on et.taskid = c.busobjectid\n " +
                " left join " + companyID + ".projectemployee pe on pe.id = et.projectemployeeid\n " +
                " left join " + companyID + ".teamemployee te on te.id = pe.employeedepartmentid\n " +
                " where relation = 2 and started = true and et.deleted = false and te.employeeid = " + userID +
                " and c.ownerid = " + userID +
                " and t.deleted = false ";
        BigInteger count = (BigInteger) findNativeSingle(sql);
        return count != null && count.intValue() > 0;
    }

    @Override
    public List<EdsClock> getClockItemsByUser(EdsUser user) {
        return find("select clock from EdsClock clock where clock.owner=? and clock.started = true order by clock.id desc", user);
    }

    @Override
    public List<EdsClock> getStopedClockItemsByUser(EdsUser user) {
        return find("select clock from EdsClock clock where clock.owner=? and clock.started = false and clock.cumulativeTime is not null and clock.reset = false order by clock.id desc", user);
    }

    @Override
    public List<EdsClock> getClockItemsByUserAndType(EdsUser user, Integer newType) {
        return find("select clock from EdsClock clock where clock.owner=? and clock.relation=? and clock.started = true order by clock.id desc", user, newType);
    }
}
