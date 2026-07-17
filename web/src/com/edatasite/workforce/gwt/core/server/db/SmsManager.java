package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/15/11
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SmsManager extends Manager<EdsSmsSettings> {
    List<EdsSmsSettings> list(ListingFilterParameter filterParametrs);

    EdsSmsSettings getDefault();
}
