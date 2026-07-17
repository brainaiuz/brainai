package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ACCESS_TOKEN;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.X_AUTH;

@Tag(name = "Generic Settings")
@RestController
@RequestMapping(path = "/generic-settings", headers = {ACCESS_TOKEN, X_AUTH})
public class ApiGenericSettingsControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiGenericSettingsControllerV3.class);
    private final GenericSettingsManager genericSettingsManager;

    public ApiGenericSettingsControllerV3(GenericSettingsManager genericSettingsManager) {
        this.genericSettingsManager = genericSettingsManager;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<GenericSettingsEnum, Boolean> getSettings(@RequestParam String[] keys) {
        log.info("REST request to get generic settings");
        GenericSettingsEnum[] settingsEnums = Arrays.stream(keys)
                .map(GenericSettingsEnum::valueOf)
                .toArray(GenericSettingsEnum[]::new);
        Set<GenericSettingsEnum> enabledSettings = genericSettingsManager.getByKeys(settingsEnums);
        return Arrays.stream(settingsEnums)
                .collect(Collectors.toMap(Function.identity(), enabledSettings::contains));
    }
}
