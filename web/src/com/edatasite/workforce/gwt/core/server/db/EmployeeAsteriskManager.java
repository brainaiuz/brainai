package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployeeAsterisk;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/4/2020
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeeAsteriskManager extends Manager<EdsEmployeeAsterisk> {

    List<EdsEmployeeAsterisk> list(Integer asteriskSettingsId, ListingFilterParameter filterParametrs);

    int listCount(Integer asteriskSettingsId, ListingFilterParameter filterParameter);

    EdsEmployeeAsterisk getEmployeeAsteriskSettings(Integer asteriskSettingsId, Integer employeeId);
    List<EdsEmployeeAsterisk> getEmployeeAsteriskSettings(Integer employeeId);
    List<EdsEmployeeAsterisk> getByAsteriskUsername(String asteriskUsername);
}
