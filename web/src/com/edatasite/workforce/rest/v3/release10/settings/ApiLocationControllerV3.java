package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.settings.dto.LocationDto;
import com.edatasite.workforce.rest.v3.release10.settings.service.ApiLocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Locations", description = "Location Public API")
@RestController
@RequestMapping(path = "/location", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiLocationControllerV3 {
    private final ApiLocationService apiLocationService;

    public ApiLocationControllerV3(ApiLocationService apiLocationService) {
        this.apiLocationService = apiLocationService;
    }

    @PostMapping(path = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LocationDto> get(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.TaskListPanel);
        return apiLocationService.getLocationList(fp);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationDto getLocationById(@PathVariable("id") Integer id) {
        return apiLocationService.getLocationById(id);
    }
}
