package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsTimeZone;

import java.util.List;

public interface TimeZoneManager extends Manager<EdsTimeZone> {
    List<EdsCountryZone> getCountryZones(EdsCountry country);

    List<EdsCountryZone> getCountryZones(List<Integer> countries);

    EdsCountryZone getCountryZone(Integer zoneID);

    List<EdsTimeZone> getMicrosoftTimeZones();
}