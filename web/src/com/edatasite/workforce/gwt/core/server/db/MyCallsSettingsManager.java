package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMyCallsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface MyCallsSettingsManager extends Manager<EdsMyCallsSettings> {

    List<EdsMyCallsSettings> list(ListingFilterParameter filterParametrs);

    int listCount(ListingFilterParameter filterParameter);

    EdsMyCallsSettings getMyCallsSettingsByUser(Integer userId);

    EdsMyCallsSettings getMyCallsSettingsBySipNumber(String userLogin);
}
