package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsWeatherLocation;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: 11.11.2009
 * Time: 19:16:30
 * To change this template use File | Settings | File Templates.
 */
public interface WeatherLocationManager {
    List<EdsWeatherLocation> getLocationsByCountry(Integer countryId);
}
