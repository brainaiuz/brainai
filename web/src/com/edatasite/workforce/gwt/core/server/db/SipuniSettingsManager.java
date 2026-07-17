package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSipuniSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface SipuniSettingsManager extends Manager<EdsSipuniSettings> {

    List<EdsSipuniSettings> list(ListingFilterParameter filterParametrs);

    int listCount(ListingFilterParameter filterParameter);

    EdsSipuniSettings getSipuniSettingsByUser(Integer userId);

    EdsSipuniSettings getSipuniSettingsBySipNumber(String operatorNumber);
}
