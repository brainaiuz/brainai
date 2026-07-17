package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.client.MFilterParametrs;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 10.07.11
 * Time: 6:44
 * To change this template use File | Settings | File Templates.
 */
public interface StatusWebService {

    String setUserStatus(String changeStatusCode, Boolean timeSpentRequared);

    Boolean setUserLocation(MFilterParametrs fp, Integer userID);

    Boolean setUserLocation(MFilterParametrs fp);
}
