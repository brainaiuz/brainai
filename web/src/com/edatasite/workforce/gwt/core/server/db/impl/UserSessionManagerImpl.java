package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 24, 2007 Time: 5:39:21 PM To
 * change this template use File | Settings | File Templates.
 */

@Repository("userSessionManager")
public class UserSessionManagerImpl extends BaseManager<EdsUserSession> implements UserSessionManager {

    public UserSessionManagerImpl() {
        super(EdsUserSession.class);
    }

    public List<Integer> getUserIdsByAccDate(Date startTime, Date endTime) {
        return find("select us.user.objectID FROM EdsUserSession us " +
                "left join EdsEmployee e on e.objectID = us.user.objectID " +
                "left join EdsClientContact cl on cl.objectID = us.user.objectID " +
                "left join EdsUserContact uc on uc.objectID = us.user.objectID " +
                "where (e.company.objectID<>? or cl.company.objectID<>? or uc.company.objectID<>?) " +
                "and us.lastAccessTime between '" + startTime + "' and '" + endTime + "' group by us.user.objectID", 1);
    }

    public EdsUserSession getUserSession(String sessionID) {
        return (EdsUserSession) findSingle("select distinct us from EdsUserSession us where us.sessionID =? and us.expired<>true", sessionID);
    }

    public Boolean isSuperUser(String sessionID) {
        Boolean isSuperUser = (Boolean) findNativeSingle("select superUser from " + getCompanyId() + ".myUserSession where sessionID =? and expired<>true", sessionID);
        return isSuperUser == null ? false : isSuperUser;
    }

    @Override
    public Boolean hasNonSuperUserSession(Integer userID) {
        Boolean hasNonSuperUserSession = (Boolean) findSingle("select us.superUser from EdsUserSession us where us.user.objectID =? and us.expired<>true and us.superUser<>true", userID);
        return hasNonSuperUserSession != null;
    }

    public List<Date> getAccessDates(Date startDate, Date endDate) {
        return find("select us.lastAccessTime FROM EdsUserSession us where us.lastAccessTime between ? and ?", startDate, endDate);
    }

    @Override
    public EdsUserSession getUserLastAccessSession(EdsUser edsUser) {
        return (EdsUserSession) findSingle("select distinct us from EdsUserSession us where us.user =? and us.expired<>true order by us.lastAccessTime desc", edsUser);
    }

    public boolean isUserInWebSite(EdsUser edsUser, Date onlineDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:m:s.SSS a", Locale.ENGLISH);
        return (findSingle("select distinct us from EdsUserSession us where us.user =? and us.expired<>true " +
                "and us.lastAccessTime>='" + format.format(onlineDate) + "'  order by us.lastAccessTime desc", edsUser)) != null;
    }

    public void expireUserSession(EdsUser user) {
        update("update EdsUserSession us set expired = true where us.user=?", user);
    }

    @Override
    public void expireMobileUserSessions(List<UserCompanyDTO> companyList) {
        companyList.forEach(schema -> {
            SecurityContext.getInstance().setDatabase(schema.getClusterDbName());
            SecurityContext.getInstance().setCompanyId(schema.getCompanyID());
            try {
                String sql = "UPDATE \"" + schema.getCompanyID() + "\".myusersession " +
                        "SET expired = true " +
                        "WHERE userid = ? AND expired <> true AND useragent = 'MOBILE'";
                masterEntityManager.createNativeQuery(sql)
                        .setParameter(1, schema.getUserID())
                        .executeUpdate();
            } catch (Exception e) {
                log.error("Failed to expire mobile sessions for schema {} userId {}", schema, schema.getUserID(), e);
            }
        });
    }
}
