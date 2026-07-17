package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.settings.mobile.EdsUserModule;
import com.edatasite.workforce.core.enums.ContextCode;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;
import com.edatasite.workforce.gwt.core.client.rpc.fakeContainer.PseudoContainerService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.settings.module.UserModuleService;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.ToggleUserModuleDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.UserModuleDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.UserModuleListDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.settings.module.UserModuleSectionTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.MODULE_ACCOUNTING;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MODULE_CRM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MODULE_HRMS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MODULE_PAYROLL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MODULE_PM;

@Tag(name = "User Module", description = "User Module API")
@RestController
@RequestMapping(value = "/user/module", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiModuleControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiModuleControllerV3.class);
    private final ModuleManager moduleManager;
    private final UserModuleService userModuleService;
    private final RoleManager roleManager;
    private final List<String> sections = Arrays.asList("pm", "hrms", "accounting");
    private final ProfileService profileService;
    private final PseudoContainerService pseudoContainerService;

    @Autowired
    public ApiModuleControllerV3(ModuleManager moduleManager, UserModuleService userModuleService, RoleManager roleManager, ProfileService profileService, PseudoContainerService pseudoContainerService) {
        this.moduleManager = moduleManager;
        this.userModuleService = userModuleService;
        this.roleManager = roleManager;
        this.profileService = profileService;
        this.pseudoContainerService = pseudoContainerService;
    }

    @Operation(summary = "Get all modules")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get all modules"))
    @RequestMapping(path = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<UserModuleListDTO>> getAllModules() {
        log.info("REST request to get user modules");
        int companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());

        List<EdsUserModule> userModules = userModuleService.findAllByUserIdAndSelected(roleManager.getUser().getObjectID(), null);
        List<UserModuleListDTO> res = new ArrayList<>();
        for (String section : sections) {
            List<String> moduleCodes = moduleManager.getAllModuleCodesByCompanyIdAndSection(companyId, section);
            UserModuleListDTO userModuleListDTO = new UserModuleListDTO();
            userModuleListDTO.setTitle(section);
            userModuleListDTO.setChildren(userModuleService.mapUserModule(new HashSet<>(moduleCodes), userModules, false));
            res.add(userModuleListDTO);
        }
        return ResultTO.success(res);
    }

    @Operation(summary = "Get all modules")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get all modules"))
    @RequestMapping(path = "/list-selected", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<UserModuleDTO>> getSelectedModules() {
        log.info("REST request to get user modules");
        int companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        List<EdsUserModule> userModules = userModuleService.findAllByUserIdAndSelected(roleManager.getUser().getObjectID(), true);
        HashSet<String> moduleCodes = moduleManager.getEnabledModuleCodesByCompany(companyId);
        return ResultTO.success(userModuleService.mapUserModule(moduleCodes, userModules, true));
    }

    @Operation(summary = "Toggle selected modules")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Toggle selected modules"))
    @RequestMapping(path = "/toggle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> toggleUserModules(@RequestBody List<ToggleUserModuleDTO> request) {
        log.info("REST request to toggle user modules");
        int companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        HashSet<String> companyModuleCodes = moduleManager.getEnabledModuleCodesByCompany(companyId);
        List<ToggleUserModuleDTO> modulesToSave = request.stream()
                .filter(m -> companyModuleCodes.contains(m.getModuleCode()))
                .collect(Collectors.toList());
        userModuleService.toggleModules(modulesToSave);
        return ResultTO.success();
    }


    @Operation(summary = "Get user modules")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get user modules"))
    @RequestMapping(path = "/user_modules", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<UserModuleDTO>> getUserModules() {
        log.info("REST request to get user has modules");
        return ResultTO.success(userModuleService.getUserModules());
    }

    @Operation(summary = "Get module tabs")
    @RequestMapping(value = "/module-tabs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ArrayList<PropertyItem>> getModuleTabs(@RequestParam(value = "module", required = false) String module) {
        log.info("REST request to get module tabs");
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setActive(true);
        filterParameter.setModule(module);
        return ResultTO.success(profileService.getPropertyItems(filterParameter).getList());
    }

    @Operation(summary = "Get sections")
    @GetMapping(value = "/sections", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ArrayList<PseudoMenuItem>> getSections(@RequestParam(value = "context") String context) {
        log.info("REST request to get sections");
        return switch (ContextCode.valueOf(context)) {
            case ACCOUNTING -> ResultTO.success(pseudoContainerService.getAccountingMenuItems(MODULE_ACCOUNTING));
            case CRM -> ResultTO.success(pseudoContainerService.getCrmMenuItems(MODULE_CRM));
            case HRMS -> ResultTO.success(pseudoContainerService.getHRMSMenuItems(MODULE_HRMS));
            case PM -> ResultTO.success(pseudoContainerService.getPMMenuItems(MODULE_PM));
            case PAYROLL -> ResultTO.success(pseudoContainerService.getPayrollMenuItems(MODULE_PAYROLL));
            case REPORTING -> ResultTO.success(pseudoContainerService.getReportingMenuItems());
            case DOCUMENTS -> ResultTO.success(pseudoContainerService.getDocsMenuItems());
            case TRAININGCENTER -> ResultTO.success(pseudoContainerService.getTrainingCentesMenuItems());
            default -> ResultTO.failure("context is invalid", 400);
        };
    }

    @Operation(summary = "Get sections")
    @GetMapping(value = "/sections/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<UserModuleSectionTO>> getSections(@RequestParam(value = "context") List<ContextCode> context) {
        log.info("REST request to get sections by list");
        List<UserModuleSectionTO> userModuleSectionTOS = new ArrayList<>();
        if (context.contains(ContextCode.ACCOUNTING)) {
            UserModuleSectionTO accountingModule = new UserModuleSectionTO(ContextCode.ACCOUNTING, pseudoContainerService.getAccountingMenuItems(MODULE_ACCOUNTING));
            userModuleSectionTOS.add(accountingModule);
        }
        if (context.contains(ContextCode.CRM)) {
            UserModuleSectionTO crmModule = new UserModuleSectionTO(ContextCode.CRM, pseudoContainerService.getCrmMenuItems(MODULE_CRM));
            userModuleSectionTOS.add(crmModule);
        }
        if (context.contains(ContextCode.HRMS)) {
            UserModuleSectionTO hrmsModule = new UserModuleSectionTO(ContextCode.HRMS, pseudoContainerService.getHRMSMenuItems(MODULE_HRMS));
            userModuleSectionTOS.add(hrmsModule);
        }
        if (context.contains(ContextCode.PM)) {
            UserModuleSectionTO pmModule = new UserModuleSectionTO(ContextCode.PM, pseudoContainerService.getPMMenuItems(MODULE_PM));
            userModuleSectionTOS.add(pmModule);
        }
        if (context.contains(ContextCode.PAYROLL)) {
            UserModuleSectionTO payrollModule = new UserModuleSectionTO(ContextCode.PAYROLL, pseudoContainerService.getPayrollMenuItems(MODULE_PAYROLL));
            userModuleSectionTOS.add(payrollModule);
        }
        if (context.contains(ContextCode.REPORTING)) {
            UserModuleSectionTO reportingModule = new UserModuleSectionTO(ContextCode.REPORTING, pseudoContainerService.getReportingMenuItems());
            userModuleSectionTOS.add(reportingModule);
        }
        if (context.contains(ContextCode.DOCUMENTS)) {
            UserModuleSectionTO documentsModule = new UserModuleSectionTO(ContextCode.DOCUMENTS, pseudoContainerService.getDocsMenuItems());
            userModuleSectionTOS.add(documentsModule);
        }
        if (context.contains(ContextCode.TRAININGCENTER)) {
            UserModuleSectionTO trainingcenterModule = new UserModuleSectionTO(ContextCode.TRAININGCENTER, pseudoContainerService.getTrainingCentesMenuItems());
            userModuleSectionTOS.add(trainingcenterModule);
        }
        return ResultTO.success(userModuleSectionTOS);
    }

}
