package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCityDistrict;

import java.util.List;

public interface CityOrRegionManager extends Manager<EdsCityDistrict> {
    List<EdsCityDistrict> getCityOrDistrictByRegionId(Integer regionId);
}
