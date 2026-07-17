package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.settings.dto.FinancialSettingsDTO;
import com.edatasite.workforce.rest.v3.release10.settings.service.ApiFinancialSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ACCESS_TOKEN;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.X_AUTH;

@Tag(name = "Financial Settings", description = "Financial Settings API")
@RestController
@RequestMapping(value = "/financial-settings", headers = {ACCESS_TOKEN, X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiFinancialSettingsControllerV3 {
    @Autowired
    private ApiFinancialSettingsService apiFinancialSettingsService;
    @Operation(summary = "Get Financial settings")
    @GetMapping(value = "/financial-settings", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<FinancialSettingsDTO> getFinancialSettings() throws RestException {
        return ResultTO.success(apiFinancialSettingsService.getFinancialSettings());
    }
}
