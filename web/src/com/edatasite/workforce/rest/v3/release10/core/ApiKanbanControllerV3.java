package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitiesList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanColumn;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityTO;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.*;
import com.edatasite.workforce.rest.v3.release10.crm.dto.contact.OpportunityByStageTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Kanban", description = "Kanban Public API")
@RestController
@RequestMapping(path = "/kanban")
public class ApiKanbanControllerV3 {
    private final UserManager userManager;
    private final KanbanService kanbanService;
    private final TaskService taskService;
    private final CRMService crmService;
    private final ReferenceManager referenceManager;

    public ApiKanbanControllerV3(UserManager userManager, KanbanService kanbanService, TaskService taskService, CRMService crmService, ReferenceManager referenceManager) {
        this.userManager = userManager;
        this.kanbanService = kanbanService;
        this.taskService = taskService;
        this.crmService = crmService;
        this.referenceManager = referenceManager;
    }

    @Operation(summary = "Get Kanban Columns", description = "Retrieve all columns for kanban based on provided type")
    @GetMapping(path = "/columns", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = "application/json")
    public ResponseEntity<?> getKanbanColumn(@RequestParam String viewType) {
        if (viewType == null || viewType.isEmpty()) {
            return ResponseEntity.badRequest().body("viewType parameter is required and cannot be null or empty");
        }

        ListPanelType type;
        try {
            type = ListPanelType.valueOf(viewType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid viewType parameter.");
        }

        try {
            String jsonString = kanbanService.getKanbanBoardSettings(type);

            Map<String, Object> response = new HashMap<>();

            if (jsonString == null || jsonString.isBlank()) {
                if ("OpportunitiesKanbanPanel".equals(viewType) ){
                List<KanbanColumn> defaultColumns =
                        kanbanService.getKanbanDefaultColumns(ReferenceParentEnum._OPPORTUNITY_STAGE);

                response.put("columns", defaultColumns);
                response.put("pageSize", 10);}
                else if ("TaskKanbanPanel".equals(viewType)) {
                    List<KanbanColumn> defaultColumns =
                            kanbanService.getKanbanDefaultColumns(ReferenceParentEnum._TASK_STATUS);
                    response.put("columns", defaultColumns);
                    response.put("pageSize", 10);
                }
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(jsonString, Map.class);

                Map<String, Object> columnsObject = (Map<String, Object>) jsonMap.get("columns");
                List<Map<String, Object>> columnsArray = columnsObject.entrySet().stream()
                        .sorted(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())))
                        .map(entry -> (Map<String, Object>) entry.getValue())
                        .collect(Collectors.toList());

                response.put("columns", columnsArray);
                response.put("pageSize", jsonMap.get("pageSize"));
            }

            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }
    }

    @Operation(summary = "Update Kanban Item Order", description = "Update the order of items in Kanban")
    @GetMapping(path = "/reorder", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = "application/json")
    public ResponseEntity<String> updateItemOrder(
            @RequestParam Integer statusId,
            @RequestParam Integer itemId,
            @RequestParam(required = false) Integer prevLayoutData,
            @RequestParam(required = false) Integer afterLayoutData) {

        if (statusId == null || itemId == null ) {
            return ResponseEntity.badRequest().body("All parameters are required: statusId, itemId, prevLayoutData, afterLayoutData");
        }

        try {
            taskService.changeTaskKanbanOrder(
                    new SelectItem(statusId),
                    itemId,
                    prevLayoutData,
                    afterLayoutData
            );
            return ResponseEntity.ok("Kanban item order updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Kanban item order: " + e.getMessage());
        }
    }

    @Operation(summary = "Update Kanban Column Order", description = "Update the order of column in Kanban")
    @PostMapping(path = "/column/reorder", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateColumnOrder(@RequestBody UpdateKanbanColumnDTO updateKanbanColumnDTO){

        if (updateKanbanColumnDTO == null ||updateKanbanColumnDTO.getViewType() == null || updateKanbanColumnDTO.getViewType().isEmpty()) {
            return ResponseEntity.badRequest().body("viewType parameter is required and cannot be null or empty");
        }

        ListPanelType type;
        try {
            type = ListPanelType.valueOf(updateKanbanColumnDTO.getViewType());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid viewType parameter.");
        }
        ArrayList<KanbanColumn> kanbanColumns = updateKanbanColumnDTO.getColumns().stream()
                .map(it -> new KanbanColumn(it.getId(), it.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
         kanbanService.saveKanbanBoardSettings(type, updateKanbanColumnDTO.getSize(), kanbanColumns, false);
        return ResponseEntity.ok("Kanban column order updated successfully");

    }


    @Operation(summary = "Update Opportunity Kanban  Item Order", description = "Update the order of opportunity items in Kanban")
    @GetMapping(path = "opportunity/reorder", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = "application/json")
    public ResponseEntity<String> updateOppItemOrder(
            @RequestParam Integer statusId,
            @RequestParam Integer itemId,
            @RequestParam(required = false) Integer index,
            @RequestParam(required = false) Integer prevLayoutData,
            @RequestParam(required = false) Integer afterLayoutData) {

        if (statusId == null || itemId == null ) {
            return ResponseEntity.badRequest().body("All parameters are required: statusId, itemId, prevLayoutData, afterLayoutData");
        }

        try {
            crmService.changeOpportunityKanbanOrder(
                    new SelectItem(statusId),
                    itemId,
                    index,
                    prevLayoutData,
                    afterLayoutData
            );
            return ResponseEntity.ok("Kanban item order updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Kanban item order: " + e.getMessage());
        }
    }
    @RequestMapping(value = "/list/by-status", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<List<OpportunityByStageTO>> opportunityByStage(@RequestBody ListParamsDTO listParams) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.OpportunitiesListPanel);
        fp.setHasOnlyClientAccess(false);

        List<OpportunityByStageTO> response = getOpportunityByStage(fp);
        return ResultTO.success(response);
    }

    @Operation(summary = "Get Kanban items by status", description = "Get Kanban items by status (opportunity, lead, task)")
    @PostMapping(path = "kanban/status/items",
            headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
            produces = "application/json")
    public ResponseEntity<?> getKanbanItemsByStatus(@RequestBody ListParamsDTO listParams,
                                                    @RequestParam Integer statusId,
                                                    @RequestParam String type) {

        if (statusId == null || listParams == null || type == null) {
            return ResponseEntity.badRequest()
                    .body("All parameters are required: statusId, listParams, type (OPPORTUNITY | LEAD | TASK)");
        }

        // Build filter params
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(
                listParams, ListPanelType.OpportunitiesKanbanPanel);
        fp.setHasOnlyClientAccess(false);

        List<OpportunityTO> result;
        switch (type.toUpperCase()) {
            case "OPPORTUNITY":
                result = getOppotunityKanbanItemsByStatus(fp, statusId);
                break;
            case "LEAD":
                result = getLeadKanbanItemsByStatus(fp, statusId);
                break;
            case "TASK":
                result = getTaskKanbanItemsByStatus(fp, statusId);
                break;
            default:
                return ResponseEntity.badRequest()
                        .body("Invalid type. Allowed values: OPPORTUNITY, LEAD, TASK");
        }

        return ResponseEntity.ok(result);
    }

    public List<OpportunityTO> getOppotunityKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        OpportunitiesList<OpportunityListItem> opportunityKanbanItem =
                crmService.getNewKanbanOpportunities(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> opportunityTOS = new ArrayList<>();

        for (OpportunityListItem item : opportunityKanbanItem.getList()) {
            OpportunityTO opportunityTO = new OpportunityTO();
            opportunityTO.setName(item.getOpportunityName());

            if (item.getContact() != null) {
                IdName contactDTO = new IdName();
                contactDTO.setName(item.getContact());
                contactDTO.setId(item.getContactId());
                contactDTO.addProperty("email", item.getContactPrimaryEmail());
                contactDTO.addProperty("phone", item.getContactPrimaryPhone());
                opportunityTO.setContact(contactDTO);
            }

            opportunityTO.setAmmount(item.getAmount());
            opportunityTO.setCurrency(item.getCurrency());
            opportunityTO.setClosing_date(item.getClosingDate());
            opportunityTO.setClosing_date_Id(item.getClosingDateID());
            opportunityTO.addProperty("assignFullName", item.getAssignee());
            opportunityTO.addProperty("assignId", item.getAssigneeId());

            Optional.ofNullable(item.getCrmAccountItem())
                    .ifPresent(crmAccount -> opportunityTO.setCustomer(
                            new ItemDto(crmAccount.getObjectId(), crmAccount.getName(), crmAccount.getNumber())));

            opportunityTO.setItem_id(item.getObjectId());
            opportunityTO.setStatus_id(item.getStage() != null ? item.getStage().getId() : 0);

            if (item.getCreatedDate() != null) {
                opportunityTO.setDate_added(dateFormat.format(item.getCreatedDate()));
            }

            opportunityTOS.add(opportunityTO);
        }

        return opportunityTOS;
    }

    public List<OpportunityTO> getTaskKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        ListResult<TaskListItem> tasksKanbanItem =
                taskService.getNewKanbanTasks(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> taskTOS = new ArrayList<>();

        for (TaskListItem task : tasksKanbanItem.getList()) {
            OpportunityTO taskTO = new OpportunityTO();
            taskTO.setName(task.getName());

            IdName contactDTO = new IdName();
            contactDTO.setName(task.getProjectName());
            contactDTO.setId(task.getProjectId());
            contactDTO.addProperty("assignee", task.getAssigneeFullNames());
//            contactDTO.addProperty("phone", task.getAssignedToPhone());
            taskTO.setContact(contactDTO);

            taskTO.setItem_id(task.getObjectID());
            taskTO.setStatus_id(task.getTaskStatusId() != null ? task.getTaskStatusId() : 0);

            if (task.getCreationDate() != null) {
                taskTO.setDate_added(dateFormat.format(task.getCreationDate()));
            }

            taskTOS.add(taskTO);
        }

        return taskTOS;
    }

    public List<OpportunityTO> getLeadKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        ListResult<ContactListItem> leadsKanbanItem =
                crmService.getNewKanbanLeads(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> leadTOS = new ArrayList<>();

        for (ContactListItem lead : leadsKanbanItem.getList()) {
            OpportunityTO leadTO = new OpportunityTO();
            leadTO.setName(lead.getFullName());

            IdName contactDTO = new IdName();
            contactDTO.setName(lead.getFullName());
            contactDTO.setId(lead.getObjectId());
            contactDTO.addProperty("email", lead.getPrimaryEmail());
            contactDTO.addProperty("phone", lead.getPrimaryPhone());
            leadTO.setContact(contactDTO);

            leadTO.setItem_id(lead.getObjectId());
            leadTO.setStatus_id(lead.getLeadStatus() != null ? lead.getLeadStatus().getId() : 0);

            if (lead.getCreatedDate() != null) {
                leadTO.setDate_added(dateFormat.format(lead.getCreatedDate()));
            }

            leadTOS.add(leadTO);
        }

        return leadTOS;
    }


    public List<OpportunityByStageTO> getOpportunityByStage(ListingFilterParameter fp) {

        Map<Integer, OpportunityByStageTO> map = Map.of();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if ("OPPORTUNITY".equals(fp.getRelationType())) {
            map = new HashMap<>();
            List<EdsReference> stages = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);
            double total = 0.0;

            for (EdsReference stage : stages) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(stage.getObjectID());
                opportunityByStageTO.setStageTitle(stage.getName());
                opportunityByStageTO.setProbility(stage.getDescription() + "%");
                opportunityByStageTO.setRequiredComment(stage.isRequiredComment());

                OpportunitiesList<OpportunityListItem> opportunityKanbanItem =
                        crmService.getNewKanbanOpportunities(fp, stage.getAsSelectItem());

                List<OpportunityTO> opportunityTOS = new ArrayList<>();

                for (OpportunityListItem item : opportunityKanbanItem.getList()) {
                    OpportunityTO opportunityTO = new OpportunityTO();
                    opportunityTO.setName(item.getOpportunityName());

                    if (item.getContact() != null) {
                        IdName contactDTO = new IdName();
                        contactDTO.setName(item.getContact());
                        contactDTO.setId(item.getContactId());
                        contactDTO.addProperty("email", item.getContactPrimaryEmail());
                        contactDTO.addProperty("phone", item.getContactPrimaryPhone());
                        opportunityTO.setContact(contactDTO);
                    }

                    opportunityTO.setAmmount(item.getAmount());
                    opportunityTO.setCurrency(item.getCurrency());
                    opportunityTO.setClosing_date(item.getClosingDate());
                    opportunityTO.setClosing_date_Id(item.getClosingDateID());
                    opportunityTO.addProperty("assignFullName", item.getAssignee());
                    opportunityTO.addProperty("assignId", item.getAssigneeId());

                    Optional.ofNullable(item.getCrmAccountItem())
                            .ifPresent(crmAccount -> opportunityTO.setCustomer(
                                    new ItemDto(crmAccount.getObjectId(), crmAccount.getName(), crmAccount.getNumber())));

                    opportunityTO.setItem_id(item.getObjectId());
                    opportunityTO.setStatus_id(item.getStage() != null ? item.getStage().getId() : 0);

                    if (item.getCreatedDate() != null) {
                        opportunityTO.setDate_added(dateFormat.format(item.getCreatedDate()));
                    }
                    if (item.getAmount() != null) {
                        total += item.getAmount();
                    }
                    Double stageTotal = opportunityKanbanItem.getTotalAmount() != null
                            ? opportunityKanbanItem.getTotalAmount()
                            : total;
                    opportunityByStageTO.setTotalAmount(stageTotal);
                    opportunityTOS.add(opportunityTO);
                }

                opportunityByStageTO.setOpportunity(opportunityTOS);
                opportunityByStageTO.setTotalCount(opportunityKanbanItem.getTotal());

                map.put(stage.getObjectID(), opportunityByStageTO);
            }
        } else if ("LEADS".equals(fp.getRelationType())) {
            map = new HashMap<>();
            List<EdsReference> statuses = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);

            for (EdsReference status : statuses) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(status.getObjectID());
                opportunityByStageTO.setStageTitle(status.getName());
                opportunityByStageTO.setProbility(status.getDescription() + "%");

                ListResult<ContactListItem> leadsKanbanItem =
                        crmService.getNewKanbanLeads(fp, status.getAsSelectItem());

                List<OpportunityTO> leadTOS = new ArrayList<>();

                for (ContactListItem lead : leadsKanbanItem.getList()) {
                    OpportunityTO leadTO = new OpportunityTO();
                    leadTO.setName(lead.getFullName());

                    IdName contactDTO = new IdName();
                    contactDTO.setName(lead.getFullName());
                    contactDTO.setId(lead.getObjectId());
                    contactDTO.addProperty("email", lead.getPrimaryEmail());
                    contactDTO.addProperty("phone", lead.getPrimaryPhone());
                    leadTO.setContact(contactDTO);

                    leadTO.setItem_id(lead.getObjectId());
                    leadTO.setStatus_id(lead.getLeadStatus() != null ? lead.getLeadStatus().getId() : 0);

                    if (lead.getCreatedDate() != null) {
                        leadTO.setDate_added(dateFormat.format(lead.getCreatedDate()));
                    }

                    leadTOS.add(leadTO);
                }
                opportunityByStageTO.setTotalCount(leadsKanbanItem.getTotal());
                opportunityByStageTO.setOpportunity(leadTOS);
                map.put(status.getObjectID(), opportunityByStageTO);
            }
        } else if ("TASKS".equals(fp.getRelationType())) {
            fp.setRelationType(null);
            map = new HashMap<>();
            List<EdsReference> statuses = referenceManager.listReferences(EdsTask.TASK_STATUS);

            for (EdsReference status : statuses) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(status.getObjectID());
                opportunityByStageTO.setStageTitle(status.getName());
                opportunityByStageTO.setRequiredComment(status.isRequiredComment());

                ListResult<TaskListItem> tasksKanbanItem =
                        taskService.getNewKanbanTasks(fp, status.getAsSelectItem());

                List<OpportunityTO> taskTOS = new ArrayList<>();

                for (TaskListItem task : tasksKanbanItem.getList()) {
                    OpportunityTO taskTO = new OpportunityTO();
                    taskTO.setName(task.getName());

                    IdName contactDTO = new IdName();
                    contactDTO.setName(task.getProjectName());
                    contactDTO.setId(task.getProjectId());
                    contactDTO.addProperty("assignee", task.getAssigneeFullNames());
//                    contactDTO.addProperty("phone", task.getAssignedToPhone());
                    taskTO.setContact(contactDTO);

                    taskTO.setItem_id(task.getObjectID());
                    taskTO.setStatus_id(task.getTaskStatusId() != null ? task.getTaskStatusId() : 0);

                    if (task.getCreationDate() != null) {
                        taskTO.setDate_added(dateFormat.format(task.getCreationDate()));
                    }

                    taskTOS.add(taskTO);
                }
                opportunityByStageTO.setTotalCount(tasksKanbanItem.getTotal());
                opportunityByStageTO.setOpportunity(taskTOS);
                map.put(status.getObjectID(), opportunityByStageTO);
            }
        }

        return new ArrayList<>(map.values());
    }

}
