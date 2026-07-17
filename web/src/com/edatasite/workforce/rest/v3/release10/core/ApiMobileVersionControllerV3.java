package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.gwt.core.server.app.settings.module.MobileVersionService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.MobileVersionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Mobile Version", description = "Mobile Version API")
@RestController
@RequestMapping(value = "/mobile/versions", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiMobileVersionControllerV3 {
    private final MobileVersionService mobileVersionService;

    @Autowired
    public ApiMobileVersionControllerV3(MobileVersionService mobileVersionService) {
        this.mobileVersionService = mobileVersionService;
    }

    @Operation(summary = "Create mobile version")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> createVersion(@RequestBody MobileVersionDTO request) {
        mobileVersionService.create(request);
        return ResultTO.success();
    }

    @Operation(summary = "Get mobile version")
    @GetMapping(path = "/{name}/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<MobileVersionDTO> getVersion(@PathVariable("name") String name,
                                                 @PathVariable("version") String version) {
        MobileVersionDTO mobileVersion = mobileVersionService.getVersion(name, version);
        return ResultTO.success(mobileVersion);
    }

    @Operation(summary = "Get mobile versions")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<MobileVersionDTO>> getAllMobileVersions(@RequestParam("name") String name) {
        List<MobileVersionDTO> mobileVersions = mobileVersionService.getAllVersionsByName(name);
        return ResultTO.success(mobileVersions);
    }

    @Operation(summary = "Delete mobile version")
    @DeleteMapping(path = "/{name}/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteVersion(@PathVariable("name") String name,
                                     @PathVariable("version") String version) {
        mobileVersionService.deleteVersion(name, version);
        return ResultTO.success();
    }
}
