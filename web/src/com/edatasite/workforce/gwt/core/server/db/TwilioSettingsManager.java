package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTwilioSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 5/30/18
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TwilioSettingsManager extends Manager<EdsTwilioSettings> {
    List<EdsTwilioSettings> list(ListingFilterParameter filterParametrs);

    int listCount(ListingFilterParameter filterParameter);

    EdsTwilioSettings getByNumber(String number);
}
