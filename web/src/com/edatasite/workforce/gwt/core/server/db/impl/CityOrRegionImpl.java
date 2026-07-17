package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCityDistrict;
import com.edatasite.workforce.gwt.core.server.db.CityOrRegionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cityDistrictManager")
public class CityOrRegionImpl extends BaseManager<EdsCityDistrict> implements CityOrRegionManager {
    public CityOrRegionImpl() {
        super(EdsCityDistrict.class);
    }

    @Override
    public List<EdsCityDistrict> getCityOrDistrictByRegionId(Integer regionId) {
        return (List<EdsCityDistrict>) findNative("select * from city_district where regionId=" + regionId + " order by uzName", EdsCityDistrict.class);
    }
}
