package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface ShiftSettingsManager extends Manager<EdsShiftSettings> {
    List<EdsShiftSettings> getShiftSettings(ListingFilterParameter fp);
}
