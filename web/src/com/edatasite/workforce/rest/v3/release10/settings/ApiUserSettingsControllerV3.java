package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.profile.client.rpc.CredentialsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.settings.dto.CredentialsItemDto;
import com.edatasite.workforce.rest.v3.release10.settings.dto.UserSettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ACCESS_TOKEN;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.X_AUTH;

@Tag(name = "User Credentials", description = "User Credentials API")
@RestController
@RequestMapping(value = "/user_settings", headers = {ACCESS_TOKEN, X_AUTH})
public class ApiUserSettingsControllerV3 {
    private final ProfileService profileService;
    private final RolePermissionService rolePermissionService;
    private final CommonService commonService;
    private final CommonServiceLocal commonServiceLocal;
    private final PermissionManager permissionManager;
    private final RoleManager roleManager;

    public ApiUserSettingsControllerV3(ProfileService profileService,
                                       RolePermissionService rolePermissionService,
                                       CommonService commonService,
                                       CommonServiceLocal commonServiceLocal,
                                       PermissionManager permissionManager,
                                       RoleManager roleManager) {
        this.profileService = profileService;
        this.rolePermissionService = rolePermissionService;
        this.commonService = commonService;
        this.commonServiceLocal = commonServiceLocal;
        this.permissionManager = permissionManager;
        this.roleManager = roleManager;
    }

    @Operation(summary = "Employee Language update", description = "Employee Language update")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @PatchMapping(value = "/language/{langCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean changeUserLanguage(@PathVariable String langCode) {
        CredentialsItem userCredentials = profileService.getCredentials();

        if (userCredentials == null || langCode == null || langCode.isEmpty()) {
            return Boolean.FALSE;
        }
        if (langCode.equals(userCredentials.getInternationalization())) {
            return Boolean.FALSE;
        }
        userCredentials.setInternationalization(langCode);
        userCredentials.setCurrentPass("");
        profileService.saveCredentials(userCredentials);
        return Boolean.TRUE;
    }

    @Operation(summary = "Get user permissions", description = "User Role and permissions by Context. Context need to be one of this accounting,pm,hrms,settings,crm,payroll,profileAA")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "User Role and permissions"))
    @GetMapping(path = "/permissions/{context}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserRoleAndPermissions(@PathVariable String context) {
        PermissionSettings permissionSettings = rolePermissionService.getPermissionSettings(context);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userID", permissionSettings.getUserID());
        result.put("roles", permissionSettings.getRoles());
        result.put("permissions", permissionSettings.getPermissions());
        result.put("lang", permissionSettings.getUserLanguage());
        return result;
    }

    @Operation(summary = "Get user permissions")
    @GetMapping(path = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<ItemDto>> getUserPermissions(@RequestParam("context") String context) {
        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setSection(context);
        parameter.setAllByFilter(false);
        ListResult<PermissionItem> permissionList = rolePermissionService.getPermissionListByContext(parameter);
        List<ItemDto> response = permissionList.getList().stream()
                .filter(e -> ServerUtils.hasPermission(e.getCode()))
                .map(e -> new ItemDto(e.getObjectId(), e.getName(), e.getCode()))
                .collect(Collectors.toList());
        return ResultTO.success(response);
    }


    @Operation(summary = "Update task view state (list or kanban) for a user")
    @PutMapping(path = "/viewState", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateTaskViewState(@RequestParam("viewType") String viewType,
                                       @RequestParam("value") String value) {
        return commonService.saveUserSettings(UserSettingsTypeEnum.ItemsDisplayOptions, viewType, value);
    }

    @Operation(summary = "Get task view state (list or kanban) for a user")
    @GetMapping(path = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSettingsDto getTaskViewState(@RequestParam("viewType") String viewType) {
        return commonServiceLocal.getUserSettingsViwe(UserSettingsTypeEnum.ItemsDisplayOptions, viewType);
    }

    @Operation(summary = "Get separated permission codes even admin")
    @GetMapping(path = "/permissions/codes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<HashSet<String>> getUserPermissionCodes(@RequestParam("context") String context) {
        HashSet<String> permissionList = permissionManager.getUsersPermissionsListNative(context, roleManager.getUser());
        return ResultTO.success(permissionList);
    }

    @Operation(summary = "User has a permission")
    @GetMapping(path = "/permissions/hasPermission", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> hasPermission(@RequestParam("permission") String permission) {
        return ResultTO.success(ServerUtils.hasPermission(permission));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<CredentialsItemDto> getUserCredentials() {
        var userCredentials = profileService.getCredentials();
        var dto = new CredentialsItemDto();
        dto.setEmail(userCredentials.getEmail());
        dto.setLogin(userCredentials.getLogin());
        dto.setInternationalization(userCredentials.getInternationalization());
        dto.setTimeZoneId(userCredentials.getTimeZoneId());
        dto.setCountryID(userCredentials.getCountryID());
        dto.setStartPage(userCredentials.getStartPage());
        dto.setRegistrationType(userCredentials.getRegistrationType());
        dto.setAdvancedPasswordEnabled(userCredentials.getAdvancedPasswordEnabled());
        return ResultTO.success(dto);
    }
}
