package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAsteriskSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/4/2020
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AsteriskSettingsManager extends Manager<EdsAsteriskSettings> {
    List<EdsAsteriskSettings> list(ListingFilterParameter filterParametrs);

    int listCount(ListingFilterParameter filterParameter);

    EdsAsteriskSettings getByNumber(String number);
}
