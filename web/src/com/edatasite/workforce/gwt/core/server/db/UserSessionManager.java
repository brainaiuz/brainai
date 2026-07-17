package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 5:04:04 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserSessionManager extends Manager<EdsUserSession> {

    String NAME = "userSessionManager";

    EdsUserSession getUserSession(String sessionID);

    List<Integer> getUserIdsByAccDate(Date startTime, Date endTime);

    List<Date> getAccessDates(Date startDate, Date endDate);

    EdsUserSession getUserLastAccessSession(EdsUser edsUser);

    boolean isUserInWebSite(EdsUser edsUser, Date onlineDate);

    void expireUserSession(EdsUser user);

    Boolean isSuperUser(String sessionID);

    Boolean hasNonSuperUserSession(Integer userID);

    void expireMobileUserSessions(List<UserCompanyDTO> companyList);
}
