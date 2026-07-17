package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintItem;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.QueriesListDTO;

/**
 * Created by user on 1/13/2016.
 */
public interface StatusServiceLocal {

    String setUserStatus(Integer employeeId, String changeStatusCode, boolean timeSpentRequired);

    void addFingerPrintItemsToTimeTrackAll(FingerPrintItem printItem, CompanyDomain companyDomain);

    String getUserStatus(Integer userId);

    String getUserStatus();

    void proceedUserAbsentStatus(String status, Integer userId);

    void insertEmployeeTimeTrack(Integer employeeID, String status, DateNonConvertable date, String location);

    String runScripts(QueriesListDTO queries) throws Exception;
}
