package com.edatasite.workforce.rest.v3.release10.settings.service;

import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.settings.dto.LocationDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiLocationService {
    private final LocationManager locationManager;
    private final CommonService commonService;

    public ApiLocationService(LocationManager locationManager, CommonService commonService) {
        this.locationManager = locationManager;
        this.commonService = commonService;
    }

    public List<LocationDto> getLocationList(ListingFilterParameter fp) {
        List<EdsLocation> locations = locationManager.getLocations(fp);
        return locations.stream().map(ApiLocationService::toLocationDto).toList();
    }

    public LocationDto getLocationById(Integer id) {
        EdsLocation location = locationManager.get(id);
        var companyCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(location.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Location));
        LocationDto locationDto = toLocationDto(location);
        if (CollectionUtils.isNotEmpty(companyCustomFieldItems)) {
            List<CustomFieldDto> customFields = companyCustomFieldItems.stream()
                    .map(CustomFieldsUtils::getCustomFieldDto)
                    .toList();
            locationDto.setCustomFields(customFields);
        }
        return locationDto;
    }

    private static LocationDto toLocationDto(EdsLocation location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getObjectID());
        locationDto.setName(location.getName());
        locationDto.setLatitude(location.getLatitude());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setRadius(location.getRadius());
        return locationDto;
    }
}
