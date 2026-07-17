package com.edatasite.workforce.gwt.materialkanban.server.app;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanColumn;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.api.client.util.Lists;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
@Service("kanbanService")
public class KanbanServiceImpl implements KanbanService, KanbanServiceLocal {

    private static final Long RESET = -1L;
    private static final Long GAP_SPACE = 65535L;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private TaskService taskService;

    private static final Logger log = LoggerFactory.getLogger(KanbanServiceImpl.class);

    public void setCrmContactKanbanOrder(Integer pId, Integer cId, Integer nId, Integer createdByID) {
        EdsCrmContact prevContact = crmContactManager.get(pId);
        EdsCrmContact currentContact = crmContactManager.get(cId);
        EdsCrmContact nextContact = crmContactManager.get(nId);

//        crmContactManager.getContactsByIDs()

        Long prev = prevContact != null ? prevContact.getKanbanorder() : null;
        Long current = currentContact != null ? currentContact.getKanbanorder() : null;
        Long next = nextContact != null ? nextContact.getKanbanorder() : null;
        Long result = calculateOrder(prev, current, next);
        log.debug("Calculated order: {}", result);
        if (result == RESET) {
            Integer count = crmContactManager.getCountByContactTypeAndStatus(prev, currentContact.getContactType(),
                    (currentContact.getLeadStatus() != null ? currentContact.getLeadStatus().getCode() : null));
            int start = 0;
            int limit = 30;
            long position = prev != null ? prev + GAP_SPACE + 1 : GAP_SPACE;
            while (start <= count) {
                List<EdsCrmContact> contacts = crmContactManager.getContactsByContactTypeAndStatus(prev, currentContact.getContactType(),
                        (currentContact.getLeadStatus() != null ? currentContact.getLeadStatus().getCode() : null), start, limit);
                for (EdsCrmContact contact : contacts) {
                    contact.setKanbanorder(position);
                    position += GAP_SPACE + 1;
                    contactServiceLocal.updateEdsCrmContactAndIndex(contact, false, null);
                }
                start += limit;
            }
        } else {
            currentContact.setKanbanorder(result);
            crmContactManager.update(currentContact);
            try {
                contactSolrComponent.index(currentContact);
            } catch (InterruptedException e) {
                log.error("Error indexing contact", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setOpportunityKanbanOrder(Integer pId, Integer cId, Integer nId, Integer createdByID) {
        EdsOpportunity prevOpportunity = opportunityManager.get(pId);
        EdsOpportunity currentOpportunity = opportunityManager.get(cId);
        EdsOpportunity nextOpportunity = opportunityManager.get(nId);

//        crmContactManager.getContactsByIDs()

        Long prev = prevOpportunity != null ? prevOpportunity.getKanbanorder() : null;
        Long current = currentOpportunity != null ? currentOpportunity.getKanbanorder() : null;
        Long next = nextOpportunity != null ? nextOpportunity.getKanbanorder() : null;
        Long result = calculateOrder(prev, current, next);
//        System.out.println(result);
        if (result == RESET) {
            Long count = opportunityManager.getOpportunityCountByStage(prev, (currentOpportunity.getStage() != null ? currentOpportunity.getStage().getObjectID() : null));
            int start = 0;
            int limit = 30;
            long position = prev != null ? prev + GAP_SPACE + 1 : GAP_SPACE;
            while (start <= count) {
                List<EdsOpportunity> opportunities = opportunityManager.getOpportunitiesByStageId(prev, (currentOpportunity.getStage() != null ? currentOpportunity.getStage().getObjectID() : null), start, limit);
                for (EdsOpportunity opportunity : opportunities) {
                    opportunity.setKanbanorder(position);
                    position += GAP_SPACE + 1;
                    crmServiceLocal.updateOpportunity(opportunity, true);
                }
                start += limit;
            }
        } else {
            currentOpportunity.setKanbanorder(result);
            crmServiceLocal.updateOpportunity(currentOpportunity);
        }
    }

    public void setCaseKanbanOrder(Integer pId, Integer cId, Integer nId, Integer createdByID) {
        EdsCase prevCase = caseManager.get(pId);
        EdsCase currentCase = caseManager.get(cId);
        EdsCase nextCase = caseManager.get(nId);

        Long prev = prevCase != null ? prevCase.getKanbanOrder() : null;
        Long current = currentCase != null ? currentCase.getKanbanOrder() : null;
        Long next = nextCase != null ? nextCase.getKanbanOrder() : null;
        Long result = calculateOrder(prev, current, next);
        if (result == RESET) {
            Long count = caseManager.getCaseCountByStatus(prev, currentCase.getStatus());
            int start = 0;
            int limit = 30;
            long position = prev != null ? prev + GAP_SPACE + 1 : GAP_SPACE;
            while (start <= count) {
                List<EdsCase> cases = caseManager.getCasesByStatus(prev, currentCase.getStatus(), start, limit);
                for (EdsCase crmCase : cases) {
                    crmCase.setKanbanOrder(position);
                    position += GAP_SPACE + 1;
                    caseManager.update(crmCase, true);
                }
                start += limit;
            }
        } else {
            currentCase.setKanbanOrder(result);
            caseManager.update(currentCase, true);
        }
    }

    public void setTaskKanbanOrder(Integer pId, Integer cId, Integer nId, Integer createdByID) {

        var prevTask = taskManager.get(pId);
        var currentTask = taskManager.get(cId);
        var nextTask = taskManager.get(nId);

        Long prev = prevTask != null ? prevTask.getKanbanOrder() : null;
        Long current = currentTask != null ? currentTask.getKanbanOrder() : null;
        Long next = nextTask != null ? nextTask.getKanbanOrder() : null;
        Long result = calculateOrder(prev, current, next);

        if (result == RESET) {
            Long count = taskManager.getTaskCountByStatus(prev, currentTask.getStatus());
            int start = 0;
            int limit = 30;
            long position = prev != null ? prev + GAP_SPACE + 1 : GAP_SPACE;
            while (start <= count) {
                List<EdsTask> tasks = taskManager.getTasksByStatus(prev, currentTask.getStatus(), start, limit);
                for (EdsTask task : tasks) {
                    task.setKanbanOrder(position);
                    position += GAP_SPACE + 1;
                    taskManager.update(task, true);
                }
                start += limit;
            }
        } else {
            currentTask.setKanbanOrder(result);
            taskManager.update(currentTask, true);
        }
    }

    @Override
    public ArrayList<KanbanColumn> getKanbanDefaultColumns(ReferenceParentEnum parentCode) {
        ArrayList<KanbanColumn> result = Lists.newArrayList();

        List<EdsReference> statuses = referenceManager.listReferences(parentCode.name());
        if (statuses != null) {
            EdsUser user = userManager.getUser();

            ArrayList<Integer> statusIds = new ArrayList<>(statuses.stream().map(EdsReference::getObjectID).toList());
            Map<Integer, Long> taskCountMap = taskService.getNewKanbanTasksCounts(statusIds);

            result.addAll(statuses.stream().map(status -> {
                        KanbanColumn s = new KanbanColumn(status.getObjectID(), !status.isChanged() ? referenceWfmMessageSource.localize(status.getCode(), status.getName()) : status.getName(), status.getDescription());

                        s.setCode(status.getCode());
                        s.setSelected(status.isRequiredComment());
                        s.setDraggable(isDragable(status, user));

                        boolean editReference = canEdit(status, user);

                        if ((status.getViewOnlyRoles() != null && !status.getViewOnlyRoles().isEmpty()) || (status.getEmployeesCanView() != null && !status.getEmployeesCanView().isEmpty())) {
                            if (!editReference && (user.hasEitherRoles(status.getViewOnlyRoles().toArray(new EdsRole[]{})) || status.getEmployeesCanView().stream()
                                    .anyMatch(e -> user.getObjectID().equals(e.getObjectID())))) {
                                s.setDraggable(false);
                            }
                        }

                        if (status.getReferenceColor() != null) {
                            s.setColorHex(status.getReferenceColor().getHex());
                            s.setColorName(status.getReferenceColor().getName());
                            s.setColorId(status.getReferenceColor().getObjectID());
                        } else {
                            s.setColorId(0);
                            s.setColorHex("#536577");
                        }

                        s.setTaskCount(taskCountMap.getOrDefault(status.getObjectID(), 0L).intValue());

                        return s;
                    })
                    .toList());
        }

        return result;
    }

    private boolean isDragable(EdsReference item, EdsUser user) {
        if (item.getAllowedRoles().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return true;
        } else if (!item.getAllowedRoles().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return user.hasEitherRoles(item.getAllowedRoles().toArray(new EdsRole[]{}));
        } else {
            return item.getEmployeesCanEdit().stream()
                    .anyMatch(e -> user.getObjectID().equals(e.getObjectID()));
        }
    }

    private boolean canEdit(EdsReference item, EdsUser user) {
        if (item.getOppEditBtnRole().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return true;
        } else if (!item.getOppEditBtnRole().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return user.hasEitherRoles(item.getOppEditBtnRole().toArray(new EdsRole[]{}));
        } else {
            return item.getEmployeesCanEdit().stream()
                    .anyMatch(e -> user.getObjectID().equals(e.getObjectID()));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getKanbanBoardSettings(ListPanelType type) {
        EdsListPanelSettings listPanelSettings = null;
        String result = "";
        if (type != null) {
            listPanelSettings = listPanelSettingsManager.getUserListPanelSettings(type.name(), null);
        }

        if (listPanelSettings != null) {

            result = listPanelSettings.getSettingsJSONData();

            if (StringUtils.isNotBlank(listPanelSettings.getSettingsJSONData())) {
                EdsUser user = userManager.getUser();
                try {
                    JSONObject parentJSONObject = (JSONObject) new JSONParser().parse(listPanelSettings.getSettingsJSONData());
//                    JSONObject innerJSONObject = (JSONObject) parentJSONObject.get("listPanelTools");
//                    Long pageSize = (Long) parentJSONObject.get("pageSize");
                    JSONObject columns = (JSONObject) parentJSONObject.get("columns");

                    if (columns != null && !columns.isEmpty()) {

                        for (int i = 0; i < columns.size(); i++) {

                            JSONObject column = (JSONObject) columns.get(String.valueOf(i));
                            if (column == null) continue;

                            String colIdStr = Objects.toString(column.get("id"), null);
                            if (StringUtils.isBlank(colIdStr) || !StringUtils.isNumeric(colIdStr)) {
                                column.put("taskCount", 0);
                                continue;
                            }

                            Integer colId = Integer.valueOf(colIdStr);

                            try {
                                if (!"-1".equalsIgnoreCase(colIdStr)) {
                                    EdsReference status = referenceManager.get(colId);

                                    if (status != null && !status.isDeleted()) {

                                        column.put("description", status.getDescription());
                                        column.put("name", !status.isChanged() && status.isSystemReference() && status.getLocale() == null ? referenceWfmMessageSource.localize(status.getCode(), status.getName()) : status.getName());
                                        column.put("code", status.getCode());
                                        column.put("hide", false);
                                        column.put("selected", status.isRequiredComment());

                                        boolean draggable = false;
                                        if (status.getAllowedRoles().isEmpty() || !status.getAllowedRoles().isEmpty() && user.hasEitherRoles(status.getAllowedRoles().toArray(new EdsRole[]{}))) {
                                            draggable = true;
                                            column.put("draggable", true);
                                            column.put("visible", true);
                                        }
                                        if (!draggable && status.getViewOnlyRoles() != null && !status.getViewOnlyRoles().isEmpty() && user.hasEitherRoles(status.getViewOnlyRoles().toArray(new EdsRole[]{}))) {
                                            column.put("draggable", false);
                                            column.put("visible", true);
                                        } else if (!draggable) {
                                            column.put("visible", false);
                                            column.put("draggable", false);
                                        }

                                        if (status.getOppEditBtnRole() == null || (status.getOppEditBtnRole() != null && status.getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(status.getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                                            column.put("allowEdit", true);
                                        }

                                        if (status.getReferenceColor() != null) {
                                            column.put("colorid", status.getReferenceColor().getObjectID());
                                            //                                  column.put("colorname", status.getReferenceColor().getName());
                                            column.put("colorhex", status.getReferenceColor().getHex());
                                        } else {
                                            column.put("colorid", 0);
                                            column.put("colorhex", "#536577");
                                        }
                                    } else {
                                        //Status might be deleted
                                        column.put("hide", true);
                                    }
                                } else {
                                    column.put("colorid", 0);
                                    column.put("colorhex", "#536577");
                                }
                                SelectItem columnMetadata = new SelectItem(colId);
                                ListingFilterParameter filterParameter = new ListingFilterParameter();
                                filterParameter.setColumnMetadataId(colId);
                                filterParameter.setSortField(null);
                                filterParameter.setSortDir(1);

                                ListResult<TaskListItem> taskListResult = taskService.getNewKanbanTasks(filterParameter, columnMetadata);
                                column.put("taskCount", taskListResult != null && taskListResult.getTotal() != null ? taskListResult.getTotal() : 0);
                            } catch (Exception e) {
                                log.error("Error processing column id: " + colIdStr, e);
                                column.put("taskCount", 0);
                            }
                        }

                    }

                    result = parentJSONObject.toJSONString();

                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }

        return result;
    }

    @Transactional
    public void saveKanbanBoardSettings(ListPanelType type, Integer pageSize, ArrayList<KanbanColumn> columns, boolean applyForAll) {
        EdsUser loggedUser = userManager.getUser();

        if (type == null) {
            return;
        }
        EdsListPanelSettings listPanelSettings = listPanelSettingsManager.getUserListPanelSettings(loggedUser, type.name());
        if (listPanelSettings == null) {
            listPanelSettings = new EdsListPanelSettings();
            listPanelSettings.setUser(loggedUser);
            listPanelSettings.setPanelType(type.name());
            listPanelSettingsManager.create(listPanelSettings);
        }

        org.json.simple.JSONObject parentJSON = new org.json.simple.JSONObject();
        org.json.simple.JSONObject columnsJSON = new org.json.simple.JSONObject();
        for (int i = 0; i < columns.size(); i++) {

            KanbanColumn column = columns.get(i);

            org.json.simple.JSONObject columnJSON = new org.json.simple.JSONObject();
            columnJSON.put("id", column.getId());
            columnJSON.put("name", column.getName());
            columnJSON.put("description", column.getDescription());
            columnJSON.put("minimized", column.isMinimized());

            /*EdsReference status = referenceManager.get(column.getId());
            if(status!=null && status.getReferenceColor()!=null) {
                columnJSON.put("colorid", status.getReferenceColor().getObjectID());
                columnJSON.put("colorname", status.getReferenceColor().getName());
                columnJSON.put("colorhex", status.getReferenceColor().getHex());
            }*/
            columnsJSON.put(String.valueOf(i), columnJSON);
        }
        parentJSON.put("pageSize", pageSize);
        parentJSON.put("columns", columnsJSON);

        listPanelSettings.setSettingsJSONData(parentJSON.toJSONString());//WfmJsonUtils.listPanelToolsConvertToJsonData(settings)
        listPanelSettingsManager.update(listPanelSettings);
        if (applyForAll) {
            EdsListPanelSettings defaultListPanelSettings = listPanelSettingsManager.getDefaultListPanelSettings(type.name());
            if (defaultListPanelSettings == null) {
                defaultListPanelSettings = new EdsListPanelSettings();
                defaultListPanelSettings.setPanelType(type.name());
                defaultListPanelSettings.setDefaultSetting(true);
                listPanelSettingsManager.create(defaultListPanelSettings);
            }
            defaultListPanelSettings.setSettingsJSONData(parentJSON.toJSONString());
            listPanelSettingsManager.update(defaultListPanelSettings);

            List<EdsUser> companyEmployees = this.userManager.getUsers();

            if (companyEmployees != null && companyEmployees.size() > 0) {
                for (EdsUser user : companyEmployees) {
                    EdsListPanelSettings userListPanelSettings = listPanelSettingsManager.getUserListPanelSettings(user, type.name());
                    if (userListPanelSettings == null) {
                        userListPanelSettings = new EdsListPanelSettings();
                        userListPanelSettings.setUser(user);
                        userListPanelSettings.setPanelType(type.name());
                        listPanelSettingsManager.create(userListPanelSettings);
                    }
                    userListPanelSettings.setSettingsJSONData(parentJSON.toJSONString());
                    listPanelSettingsManager.update(userListPanelSettings);
                }
            }

        }
    }


    /***
     * Trello kanban calculation principle
     * @param prevOrder
     * @param currentOrder
     * @param nextOrder
     * @return
     */

    public Long calculateOrder(Long prevOrder, Long currentOrder, Long nextOrder) {
        Long result = RESET;
        prevOrder = prevOrder != null ? prevOrder : 0;

        if (currentOrder != null) {
            if (nextOrder != null) {
                Long difference = nextOrder - prevOrder;
                if (difference < 2) {
                    return RESET;
                }
                result = prevOrder + (difference / 2);
            } else {
                try {
                    result = Math.addExact(prevOrder, GAP_SPACE + 1);
                } catch (ArithmeticException e) {
                    result = RESET;
                }
            }
        }
        return result;
    }

}
