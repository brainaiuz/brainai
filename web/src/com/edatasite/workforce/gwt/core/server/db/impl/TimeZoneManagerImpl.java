package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsTimeZone;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("zoneManager")
public class TimeZoneManagerImpl extends BaseManager<EdsTimeZone> implements TimeZoneManager {

    public TimeZoneManagerImpl() {
        super(EdsTimeZone.class);
    }

    public List<EdsCountryZone> getCountryZones(EdsCountry country) {

        Map<String, EdsCountry> map = new HashMap<>();
        map.put("country", country);

        return findByNamedParams("select cz from EdsCountryZone cz where cz.country =:country order by cz.zone", map);
    }

    public List<EdsCountryZone> getCountryZones(List<Integer> countries) {
        return find("select cz from EdsCountryZone cz where cz.country.objectID in (" + ServerUtils.getAsCommoDelimited(countries, "0", ",") + ") order by cz.country.objectID");
    }

    public EdsCountryZone getCountryZone(Integer zoneID) {
        Map<String, Integer> map = new HashMap<>();
        map.put("zoneID", zoneID);
        return (EdsCountryZone) findSingleByNamedParams("select cz from EdsCountryZone cz where cz.objectID = :zoneID", map);
    }

    @Override
    public List<EdsTimeZone> getMicrosoftTimeZones() {
        List<EdsTimeZone> list = (List<EdsTimeZone>) find("select zone from EdsTimeZone zone where zone.microsoftZoneID != null order by zone.objectID asc");
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }

}
