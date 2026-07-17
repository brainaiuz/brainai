package com.edatasite.workforce.rest.v4.hrms.controller;

import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v4.hrms.service.OrgBoardSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Org Board Settings", description = "Org Board Settings Public API")
@RestController
@RequestMapping(
        value = "/hr/orgboard",
        headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
)
public class OrgBoardSettingsController {

    Logger log = LoggerFactory.getLogger(OrgBoardSettingsController.class);

    private final OrgBoardSettingsService settingsService;

    public OrgBoardSettingsController(OrgBoardSettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Operation(summary = "Get Org Board Settings")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get Org Board Settings"))
    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<OrgBoardSettingsItem> getOrgBoardSettings() {
        Integer userId = SecurityContext.getInstance().getUserId();
        if (userId == null) {
            log.error("Invalid User {}", userId);
            throw new RuntimeException("Invalid user!");
        }
        log.info("Get Org Board Settings for employee {}", userId);
        OrgBoardSettingsItem settings = settingsService.getOrgBoardSettings(userId);
        return ResultTO.success(settings);
    }

    @Operation(summary = "Update Org Board Settings")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Update Org Board Settings"))
    @RequestMapping(path = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<OrgBoardSettingsItem> updateOrgBoardSettings(@RequestBody OrgBoardSettingsItem body) {
        Integer userId = SecurityContext.getInstance().getUserId();
        if (userId == null) {
            log.error("Invalid User {}", userId);
            throw new RuntimeException("Invalid user!");
        }
        log.info("Update Org Board Settings for employee {}", userId);
        OrgBoardSettingsItem settings = settingsService.updateOrgBoardSettings(userId, body);
        return ResultTO.success(settings);
    }
}
