package com.edatasite.workforce.rest.v2.release10.settings;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceColor;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceColorManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.auth.ApiAuthControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.AllStatusListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsDeleteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsMiniTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.StatusAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.StatusEditTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.StatusListTO;
import com.edatasite.workforce.rest.v2.release10.enums.FlowSettingsTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
@Tag(name = "Flow Settings", description = "Flow Settings API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiFlowSettingsControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiAuthControllerV2.class);

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ReferenceColorManager referenceColorManager;
    @Autowired
    private RoleManager roleManager;

    @Operation(summary = "Get Flow Settings List", description = "Retrieves the list of flow settings. \n" +
            "Flow_type should be: LEADS, OPPORTUNITIES or TASKS")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of flow settings"),
            @ApiResponse(responseCode = "400", description = "Type flow is required")})
    @RequestMapping(value = "/flow_settings", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object list(@RequestBody FlowSettingsMiniTO flowSettings) throws RestException {
        if (flowSettings.getType_flow() == null) {
            throw new RestException("Type flow required", "Type flow required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*List<EdsReference> referenceList;
        if (FlowSettingsTypeEnum.LEADS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            referenceList = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);
        } else if (FlowSettingsTypeEnum.OPPORTUNITIES.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            referenceList = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
        } else if (FlowSettingsTypeEnum.TASKS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            referenceList = referenceManager.listReferences(EdsTask.TASK_STATUS);
        } else {
            throw new RestException(ERROR_MESSAGE, "Invalid flow type: " + flowSettings.getType_flow(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ArrayList<FlowSettingsTO> flowSettingsList = new ArrayList<>();
        if (referenceList != null && referenceList.size() > 0) {
            for (EdsReference reference : referenceList) {
                FlowSettingsTO flowSettingsTO = new FlowSettingsTO();
                flowSettingsTO.setStatus_id(reference.getObjectID());
                flowSettingsTO.setStatus_name(reference.getName());
                flowSettingsTO.setOrder_id(reference.getSorder());
                flowSettingsTO.setIs_system(reference.isSystemReference());
                if (reference.getReferenceColor() != null) {
                    ColorTO color = new ColorTO();
                    color.setId(reference.getReferenceColor().getObjectID());
                    color.setHex(reference.getReferenceColor().getHex());
                    color.setName(reference.getReferenceColor().getName());
                    flowSettingsTO.setStatus_color(color);
                }
                flowSettingsList.add(flowSettingsTO);
            }
        }
        StatusListTO statusList = new StatusListTO();
        statusList.setStatus_list(flowSettingsList);

        return successResponse(statusList);*/
        return successResponse(getStatusList(flowSettings.getType_flow()));
    }

    @Operation(summary = "Add New Flow Settings", description = "Creates new reference of flow setting. " +
            "Flow_type should be: LEADS, OPPORTUNITIES or TASKS")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have newly added flow setting details"),
            @ApiResponse(responseCode = "400", description = "Type flow is required"),
            @ApiResponse(responseCode = "401", description = "User should have necessary permission to add new flow setting"),
            @ApiResponse(responseCode = "400", description = "Status data are required")})
    @Transactional
    @RequestMapping(value = "/flow_settings", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody StatusAddTO flowSettings) throws RestException {

        if (!roleManager.getUser().hasRole(roleManager.get(EdsRole.ADMIN))) {
            throw new RestException("You don't have permissions to add flow settings.Please contract your administrator.", "You don't have permissions to add flow settings.Please contract your administrator.", ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        if (flowSettings.getStatus() == null) {
            throw new RestException("Status data not provided", "status field is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(flowSettings.getStatus().getStatus_name())) {
            throw new RestException("Status name required", "Status name required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsReference parent;
        if (FlowSettingsTypeEnum.LEADS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsCrmContact._LEAD_STATUS);
        } else if (FlowSettingsTypeEnum.OPPORTUNITIES.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsOpportunity._OPPORTUNITY_STAGE);
        } else if (FlowSettingsTypeEnum.TASKS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsTask.TASK_STATUS);
        } else {
            throw new RestException("Invalid flow type: " + flowSettings.getType_flow(), "Invalid flow type: " + flowSettings.getType_flow(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsReference newReference = new EdsReference(flowSettings.getStatus().getStatus_name().replace(" ", "_").toUpperCase(), flowSettings.getStatus().getStatus_name());
        newReference.setParent(parent);
        Integer lastOrder = referenceManager.getLastSorder(parent.getCode());
        if (lastOrder != null) {
            newReference.setSorder(lastOrder + 10);
        }

        if (flowSettings.getStatus().getStatus_color() != null && flowSettings.getStatus().getStatus_color().getId() != null && flowSettings.getStatus().getStatus_color().getId() != 0) {
            EdsReferenceColor edsReferenceColor = referenceColorManager.get(flowSettings.getStatus().getStatus_color().getId());
            if (edsReferenceColor != null) {
                newReference.setReferenceColorId(edsReferenceColor.getObjectID());
            } else {
                //Color id is wrong means client trying to send wrong data
                throw new RestException("Status color does not exist", "Status color does not exist", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            //Color id not provided
            throw new RestException("Status color required", "Status color required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            referenceManager.create(newReference);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, "Error during the saving: " + flowSettings.getType_flow(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        FlowSettingsTO flowSettingsTO = new FlowSettingsTO();
        flowSettingsTO.setStatus_id(newReference.getObjectID());
        flowSettingsTO.setStatus_name(newReference.getName());
        flowSettingsTO.setOrder_id(newReference.getSorder());
        flowSettingsTO.setIs_system(newReference.isSystemReference());
        ColorTO colorTO = new ColorTO();
        colorTO.setId(newReference.getReferenceColor().getObjectID());
        colorTO.setName(newReference.getReferenceColor().getName());
        colorTO.setHex(newReference.getReferenceColor().getHex());
        flowSettingsTO.setStatus_color(colorTO);

        return successResponse(flowSettingsTO);
    }

    @Operation(summary = "Update Flow Setting", description = "Updates the current flow setting. " +
            "Flow_type should be: LEADS, OPPORTUNITIES or TASKS")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have updated details of flow setting "),
            @ApiResponse(responseCode = "400", description = "Type flow is required"),
            @ApiResponse(responseCode = "401", description = "User should have necessary permission to update flow setting"),
            @ApiResponse(responseCode = "400", description = "Status data are required")})
    @Transactional
    @RequestMapping(value = "/flow_settings", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object update(@RequestBody StatusEditTO flowSettings) throws RestException {
        if (!roleManager.getUser().hasRole(roleManager.get(EdsRole.ADMIN))) {
            throw new RestException("You don't have permissions to modify flow settings.Please contract your administrator.", "You don't have permissions to modify flow settings.Please contract your administrator.", ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        if (flowSettings.getStatus() == null) {
            throw new RestException("Status data not provided", "status field is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (flowSettings.getStatus().getStatus_id() == null || flowSettings.getStatus().getStatus_id() == 0) {
            throw new RestException("Status id required", "Status id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsReference parent;
        String parentCode;
        if (FlowSettingsTypeEnum.LEADS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsCrmContact._LEAD_STATUS);
            parentCode = EdsCrmContact._LEAD_STATUS;
        } else if (FlowSettingsTypeEnum.OPPORTUNITIES.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsOpportunity._OPPORTUNITY_STAGE);
            parentCode = EdsOpportunity._OPPORTUNITY_STAGE;
        } else if (FlowSettingsTypeEnum.TASKS.getType().equalsIgnoreCase(flowSettings.getType_flow())) {
            parent = referenceManager.findReferenceByCode(EdsTask.TASK_STATUS);
            parentCode = EdsTask.TASK_STATUS;
        } else {
            throw new RestException("Invalid flow type: " + flowSettings.getType_flow(), "Invalid flow type: " + flowSettings.getType_flow(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsReference existingReference = referenceManager.get(flowSettings.getStatus().getStatus_id());
        if (existingReference != null) {
//            if (!existingReference.isSystemReference()) {
            boolean orderchanged = (flowSettings.getStatus().getOrder_id() != null && existingReference.getSorder() != null
                    && flowSettings.getStatus().getOrder_id().intValue() != existingReference.getSorder())
                    || (existingReference.getSorder() == null && flowSettings.getStatus().getOrder_id() != null);

            if (flowSettings.getStatus().getStatus_color() != null && flowSettings.getStatus().getStatus_color().getId() != null && flowSettings.getStatus().getStatus_color().getId() != 0) {
                EdsReferenceColor edsReferenceColor = referenceColorManager.get(flowSettings.getStatus().getStatus_color().getId());
                if (edsReferenceColor != null) {
                    existingReference.setReferenceColorId(edsReferenceColor.getObjectID());
                } else {
                    //Color id is wrong means client trying to send wrong data
                    throw new RestException("Status color does not exist", "Status color does not exist", REQUIRED, HttpStatus.BAD_REQUEST);
                }
            }
            try {
                existingReference.setParent(parent);
                String direction = "SAME";
                if (flowSettings.getStatus().getOrder_id() != null) {
                    if (existingReference.getSorder() != null) {
                        if (existingReference.getSorder() < flowSettings.getStatus().getOrder_id()) {
                            direction = "DOWN";
                        } else if (existingReference.getSorder() > flowSettings.getStatus().getOrder_id()) {
                            direction = "UP";
                        }
                    }
                    existingReference.setSorder(flowSettings.getStatus().getOrder_id());
                }

                if (StringUtils.isNotBlank(flowSettings.getStatus().getStatus_name())) {
                    //#49 Stepans board - we commented below otherwise when you add lead statuses getting duplicated
                    //existingReference.setCode(flowSettings.getStatus().getStatus_name().replaceAll(" ", "_").toUpperCase());
                    existingReference.setName(flowSettings.getStatus().getStatus_name());
                }
                referenceManager.update(existingReference);

                //Update order for others if its changed
                if (orderchanged) {
                    int sorder = flowSettings.getStatus().getOrder_id();

                    List<EdsReference> referenceList = referenceManager.find("SELECT r FROM EdsReference r WHERE "
                                    + ServerUtils.checkForDeleted("r.deleted")
                                    + " AND r.parent.code=? AND r.sorder>=? AND r.objectID<>? ORDER BY r.sorder ASC ",
                            parentCode, flowSettings.getStatus().getOrder_id(), existingReference.getObjectID());

                    for (EdsReference edsReference : referenceList) {
                        if (direction.equals("UP")) {
                            if (flowSettings.getStatus().getOrder_id().equals(edsReference.getSorder())) {
                                sorder = sorder + 10;
                                edsReference.setSorder(sorder);
                                referenceManager.update(edsReference);
                            } else {
                                sorder = sorder + 10;
                                edsReference.setSorder(sorder);
                                referenceManager.update(edsReference);
                            }
                        } else if (direction.equals("DOWN")) {
                            if (flowSettings.getStatus().getOrder_id().equals(edsReference.getSorder())) {
                                sorder = sorder + 10;
                                existingReference.setSorder(sorder);
                                referenceManager.update(existingReference);
                            } else {
                                sorder = sorder + 10;
                                edsReference.setSorder(sorder);
                                referenceManager.update(edsReference);
                            }

                        }
                    }
                }


            } catch (Exception e) {
                log.error("", e);
                throw new RestException(ERROR_MESSAGE, "Error during the saving: " + flowSettings.getType_flow(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            /*} else {
                //system reference
                throw new RestException(ERROR_MESSAGE, "System Status can not be changed: " + flowSettings.getStatus_name(), ACCESS_DENIED, HttpStatus.METHOD_NOT_ALLOWED);
            }*/
        } else {
            //reference doesnt exist
            throw new RestException("Status not found", "Status not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }


        return successResponse(getStatusList(flowSettings.getType_flow()));
//        return successResponse(new ResponseData());

    }

    @Operation(summary = "Delete Flow Setting", description = "Deletes the flow setting. " +
            "Flow_type should be: LEADS, OPPORTUNITIES or TASKS")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate message"),
            @ApiResponse(responseCode = "400", description = "Type flow is required"),
            @ApiResponse(responseCode = "401", description = "User should have necessary permission to delete the flow setting"),
            @ApiResponse(responseCode = "400", description = "Status Id is required")})
    @Transactional
    @RequestMapping(value = "/flow_settings", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object delete(@RequestBody FlowSettingsDeleteTO flowSettings) throws RestException {
        if (!roleManager.getUser().hasRole(roleManager.get(EdsRole.ADMIN))) {
            throw new RestException("You don't have permissions to delete flow settings.Please contract your administrator.", "You don't have permissions to delete flow settings.Please contract your administrator.", ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        if (flowSettings.getType_flow() == null) {
            throw new RestException("Type flow required", "Type flow required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (flowSettings.getStatus_id() == null || flowSettings.getStatus_id() == 0) {
            throw new RestException("Status id required", "Status id required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsReference existingReference = referenceManager.get(flowSettings.getStatus_id());
        if (existingReference != null) {
            if (!existingReference.isSystemReference()) {
                try {
                    existingReference.setDeleted(Boolean.TRUE);
                    referenceManager.update(existingReference);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(ERROR_MESSAGE, "Error during the deletion: " + flowSettings.getType_flow(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                //system reference
                throw new RestException("System Status can not be deleted", "System Status can not be deleted", ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } else {
            //reference doesnt exist
            throw new RestException("Status not found", "Status not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Get All Statuses", description = "Retrieves the list of all statuses of flow settings. \n" +
            "Entity_type should be: LEADS, OPPORTUNITIES or TASKS")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the list of all statuses of flow settings"),
            @ApiResponse(responseCode = "400", description = "Type flow is required")})
    @RequestMapping(value = "/{entity_type}/all_statuses", method = RequestMethod.GET)
    public Object allStatuses(@PathVariable(value = "entity_type") String entity_type) throws RestException {
        if (StringUtils.isBlank(entity_type)) {
            throw new RestException("Type flow required", "Type flow required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        StatusListTO<FlowSettingsTO> statusListTO = getStatusList(entity_type);

        if (FlowSettingsTypeEnum.LEADS.getType().equalsIgnoreCase(entity_type)) {
            statusListTO.getStatus_list().add(0, getDefaultStatus());
        }

        AllStatusListTO<FlowSettingsTO> allStatusListTO = new AllStatusListTO<>();
        allStatusListTO.setList(statusListTO.getStatus_list());
        return successResponse(allStatusListTO);
    }

    private StatusListTO<FlowSettingsTO> getStatusList(String flowSettingsType) throws RestException {
        List<EdsReference> referenceList;
        if (FlowSettingsTypeEnum.LEADS.getType().equalsIgnoreCase(flowSettingsType)) {
            referenceList = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);
        } else if (FlowSettingsTypeEnum.OPPORTUNITIES.getType().equalsIgnoreCase(flowSettingsType)) {
            referenceList = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
        } else if (FlowSettingsTypeEnum.TASKS.getType().equalsIgnoreCase(flowSettingsType)) {
            referenceList = referenceManager.listReferences(EdsTask.TASK_STATUS);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid flow type: " + flowSettingsType, INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ArrayList<FlowSettingsTO> flowSettingsList = new ArrayList<>();
        if (referenceList != null && referenceList.size() > 0) {
            for (EdsReference reference : referenceList) {
                FlowSettingsTO flowSettingsTO = new FlowSettingsTO();
                flowSettingsTO.setStatus_id(reference.getObjectID());
                flowSettingsTO.setStatus_name(reference.getName());
                flowSettingsTO.setOrder_id(reference.getSorder());
                flowSettingsTO.setIs_system(reference.isSystemReference());
                if (reference.getReferenceColor() != null) {
                    flowSettingsTO.setStatus_color(new ColorTO(reference.getReferenceColor().getObjectID(), reference.getReferenceColor().getHex(), reference.getReferenceColor().getName()));
                }
                flowSettingsTO.setEdit_permission(reference.getOppEditBtnRole().isEmpty() || userManager.getUser().hasEitherRoles(reference.getOppEditBtnRole().toArray(new EdsRole[]{})));
                flowSettingsTO.setStatus_permission(reference.getAllowedRoles().isEmpty() || userManager.getUser().hasEitherRoles(reference.getAllowedRoles().toArray(new EdsRole[]{})));
                flowSettingsTO.setView_permission(reference.getViewOnlyRoles().isEmpty() || userManager.getUser().hasEitherRoles(reference.getViewOnlyRoles().toArray(new EdsRole[]{})) || flowSettingsTO.isStatus_permission());
                flowSettingsTO.setCommentRequired(reference.isRequiredComment());
                if (FlowSettingsTypeEnum.OPPORTUNITIES.getType().equalsIgnoreCase(flowSettingsType)) {
                    flowSettingsTO.setPercentage(reference.getDescription());
                }
                flowSettingsTO.setPercentage(reference.getDescription());
                flowSettingsList.add(flowSettingsTO);
            }
        }
        StatusListTO<FlowSettingsTO> statusList = new StatusListTO<>();
        statusList.setStatus_list(flowSettingsList);
        return statusList;
    }
}
