package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWeatherLocation;
import com.edatasite.workforce.gwt.core.server.db.WeatherLocationManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: 11.11.2009
 * Time: 19:16:57
 * To change this template use File | Settings | File Templates.
 */
@Repository("weatherLocationManager")
public class WeatherLocationManagerImpl extends BaseManager<EdsWeatherLocation> implements WeatherLocationManager {

    public WeatherLocationManagerImpl() {
        super(EdsWeatherLocation.class);
    }

    public List<EdsWeatherLocation> getLocationsByCountry(Integer countryId) {
        return find("select wl from EdsWeatherLocation wl where wl.country.objectID=?", countryId);
    }
}
