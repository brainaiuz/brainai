package com.edatasite.workforce.gwt.issue.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClockManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TaskCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.IssueEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * User: Acer
 * Date: 06-Jan-2008
 * Time: 22:11:05
 */
@Transactional
@Service("issueService")
public class IssueServiceImpl implements IssueService, Constants, IssueServiceLocal {
    private static final Logger log = LoggerFactory.getLogger(IssueServiceImpl.class);
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private ClockManager clockManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskCFManager taskCFManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    //@CheckPermission(permissions = {PermissionConstants.PM_ISSUE_LIST, PermissionConstants.PM_TASKS_ISSUE, PermissionConstants.PM_PROJECT_ISSUE})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<IssueListItem> getIssuesList(ListingFilterParameter fp) {
        EdsUser user = employeeManager.getUser();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fp.getViewAsId() == null) {
            Integer viewAsFilter = ServerUtils.getMaxRoleID(user.getRolesAsIntegersString());
            fp.setViewAsId(viewAsFilter);
        }
        if (user.hasRole(roleManager.getByCode(SUPPLIER))) {
            fp.setRelationID(user.getClientContact().getClientID());
            fp.setRelationType(RelationItem.TYPE_SUPPLIER);
        }

        if (fp.getRelationID() != null && fp.getRelationType() != null) {
            List<Integer> issueIDs = relationManager.getRelationIDsByType(fp.getRelationID(), fp.getEntityID(), fp.getRelationType(), RelationItem.TYPE_ISSUE);
            fp.setIssueIDs("(" + ServerUtils.getAsCommoDelimited(issueIDs, "0") + ")");
        }
        //panel tools
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools == null) {
            ArrayList<String> columnCodeName = new ArrayList<>(Arrays.asList(IssueListItem.NUMBER, IssueListItem.NAME,
                    IssueListItem.DESCRIPTION, IssueListItem.PERIOD,
                    IssueListItem.PRIORITY, IssueListItem.STATUS,
                    IssueListItem.RESOLVER, IssueListItem.TIMESHEET));
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
        }
        fp.setColumnsOfListing(panelTools.getColumnCodeName());
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonServiceLocal.getCompanyCustomFieldsForListView(ViewName.Issues));
        }

        List<EdsIssue> issues = issueManager.list(fp);
        ArrayList<IssueListItem> result = new ArrayList<>();
        int totalCount = issues.size();
        int limit;
        if (totalCount - fp.getStart() < fp.getLimit()) {
            limit = (totalCount % fp.getLimit()) + fp.getStart();
        } else {
            limit = Math.min(totalCount, (fp.getStart() + fp.getLimit()));
        }
        for (int i = fp.getStart(); i < limit; i++) {
            setIssueParams(issues.get(i), result, fp);
        }
        //issue kpi log
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsIssue.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get issue list");

        return new ListResult<>(result, totalCount);
    }

    private List setIssueParams(EdsIssue issue, List<IssueListItem> result, ListingFilterParameter fp) {
        IssueListItem item;
        if (issue != null) {
            item = new IssueListItem();
            //issue ID
            item.setObjectID(issue.getObjectID());
            //issue numbering
            item.setNumber(issue.getNumber());
            //issue name
            item.setName(issue.getName());
            //issue description
            item.setDescription(issue.getDescription() != null ? issue.getDescription() : "N/A");
            //issue creator
            if (issue.getCreator() != null) {
                item.setIssueCreatorID(issue.getCreator().getObjectID());
            }
            //issue period -> start date
            item.setStartDate(new Date(issue.getStartDate().getTime()));
            //issue period -> end date
            item.setEndDate(new Date(issue.getDueDate().getTime()));
            //issue priority
            if (issue.getPriority() != null) {
                item.setPriority(referenceWfmMessageSource.localizeRef(issue.getPriority()));
            }
            //issue status
            if (issue.getIssueStatus() != null) {
                item.setStatus(referenceWfmMessageSource.localizeRef(issue.getIssueStatus()));
            }
            //issue reported by
            if (issue.getReportedBy() != null) {
                item.setReportedByName(issue.getReportedBy().getName());
            }
            //issue resolver
            if (issue.getResolver() != null) {
                item.setResolver(issue.getResolver().getName());
            }
            //issue timeSheet enabled
            item.setTimeSheetEnabled(issue.getEnableTimesheet() != null && issue.getEnableTimesheet());

            boolean isAssignment = false;
            Integer userID = employeeManager.getUser().getObjectID();
            if (issue.getAssignments() != null && !issue.getAssignments().isEmpty()) {
                for (EdsEmployeeTask ite : issue.getAssignments()) {
                    if (ite.getProjectEmployee() != null && ite.getProjectEmployee().getEmployeeDepartment() != null && ite.getProjectEmployee().getEmployeeDepartment().getEmployee() != null && Objects.equals(ite.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), userID)) {
                        isAssignment = true;
                        break;
                    }

                }
            }
            if ((issue.getEnableTimesheet() != null && issue.getEnableTimesheet()) && issue.getIssueStatus() != null && !issue.getIssueStatus().getCode().equals(EdsIssue._CLOSED) && isAssignment) {
                item.setShowTimer(true);
                item.setTimerIsStarted(clockManager.getActiveClockForCurrentUser(issue.getObjectID(), PM_ISSUE_TIMER, userID) != null);
            } else {
                item.setShowTimer(false);
            }

            if (issue.getProject() != null) {
                item.setProjectID(issue.getProject().getObjectID());
            }
            if (fp.isCustomFieldsShown()) {
                item.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(issue.getTaskCustomFields(), fp.getColumnsOfListing()));
            }
            item.setRelationValueMap(getIssueRelationValues(EdsRelation.TYPE_ISSUE, issue.getObjectID()));

            result.add(item);
        }
        return result;
    }

    private HashMap<String, String> getIssueRelationValues(String relationType, Integer issueID) {
        List<EdsRelation> allRelations = relationManager.getAllRelations(RelationItem.TYPE_ISSUE, issueID);
        HashMap<String, String> relationValueMap = new HashMap<>();
        if (allRelations != null && !allRelations.isEmpty()) {
            for (String typeName : RelationItem.relationTypes) {
                if (!typeName.equals(relationType)) {
                    relationValueMap.put(typeName, getRelationNames(allRelations).get(typeName));
                }
            }
        }
        return relationValueMap;
    }

    public Map<String, String> getRelationNames(List<EdsRelation> allRelations) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (EdsRelation relation : allRelations) {
            getRelationS(hashMap, RelationItem.TYPE_ISSUE, relation, true);
        }
        return hashMap;
    }

    private void getRelationS(HashMap<String, String> hashMap, String type, EdsRelation relation, boolean viceVersa) {
        String toType = relation.getToType();
        if (hashMap.containsKey(toType)) {
            String value = hashMap.get(toType);
            value = value + ", " + relation.getNameByType(type, viceVersa);
            hashMap.put(toType, value);
        } else {
            hashMap.put(toType, relation.getNameByType(type, viceVersa));
        }
    }

    public HistoryListItem[] getIssueNotes(Integer issueID) {
        EdsIssue issue = issueManager.get(issueID);
        HistoryListItem[] issueNotes;
        if (issue != null) {
            EdsNoteHistory[] issueNote = noteHistoryManager.getNoteList(new ListingFilterParameter()).toArray(new EdsNoteHistory[]{});
            List<EdsNoteHistory> histories = new LinkedList<>();
            for (EdsNoteHistory noteHistory : issueNote) {
                if ((EdsNoteHistory.PM_ISSUE == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) &&
                        noteHistory.getRelatedId().intValue() == issue.getObjectID().intValue()) {
                    histories.add(noteHistory);
                }
            }
            issueNotes = new HistoryListItem[histories.size()];
            EdsUser user = employeeManager.getUser();
            for (int i = 0; i < histories.size(); i++) {
                EdsNoteHistory notes = histories.get(i);
                HistoryListItem listItem = new HistoryListItem();
                listItem.setObjectID(notes.getObjectID());
                listItem.setEmployee(notes.getEmployee().getName());
                listItem.setSubject(notes.getSubject());
                listItem.setComment(notes.getComment());
                listItem.setVisibility(notes.isVisibility());
                listItem.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
                listItem.setEditable(user.equals(notes.getEmployee()));
                NewsComment[] noteComments = getIssueNoteComments(issue.getObjectID());
                if (noteComments.length > 0) {
                    listItem.setNotesComments(noteComments);
                } else {
                    listItem.setNotesComments(new NewsComment[0]);
                }
                issueNotes[i] = listItem;
            }
            return issueNotes;
        }
        return null;
    }

    public NewsComment[] getIssueNoteComments(Integer noteID) {
        return commonServiceLocal.getNotecomments(noteID);
    }

    public NewsComment saveIssueNoteComments(NewsComment commentData) {
        return commonServiceLocal.saveNoteComment(commentData);
    }

    private FileItem[] getIssueAttachments(List<FileResource> issueAttachments) {
        FileItem[] fileItems = {};
        if (issueAttachments != null && !issueAttachments.isEmpty()) {
            fileItems = new FileItem[issueAttachments.size()];
            for (int i = 0; i < issueAttachments.size(); i++) {
                FileResource fileResource = issueAttachments.get(i);
                FileItem fileItem = new FileItem();
                fileItem.setAttachmentId(fileResource.getBodyId());
                fileItem.setId(fileResource.getObjectId());
                fileItem.setFileName(fileResource.getEncodedName());
                fileItem.setDescription(fileResource.getDescription());
                fileItem.setSize(fileResource.getContentLength());
                fileItem.setUploadType(fileResource.getUploadType());
                fileItem.setDate(fileResource.getCreationDate());
                switch (fileResource.getUploadType()) {
                    case GOOGLE -> fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                    case OFFICE_365, OFFICE_365_SHARE_POINT -> {
                        fileItem.setDocumentID(fileResource.getDocumentID());
                        fileItem.setDocumentOpenID(fileResource.getDocumentOpenID());
                        fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    }
                    default -> fileItem.setAmazonLink(fileResource.getAmazonLink());
                }
                fileItems[i] = fileItem;
            }
        }
        return fileItems;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjectsNotStartedOngoing(Integer issueID, boolean withProjectNumber) {
        EdsUser employee = referenceManager.getUser();
        List<EdsProject> projects = projectManager.list(new ListingFilterParameter(), employee);

        if (issueID != null) {
            EdsIssue edsIssue = issueManager.get(issueID);
            EdsProject issueProject = edsIssue.getProject();
            if (issueProject != null && !projects.contains(issueProject)) {
                projects.add(issueProject);
            }
        }

        ProjectItem[] result = new ProjectItem[projects.size()];
        int i = 0;
        for (EdsProject pr : projects) {
            String issueNumberR = withProjectNumber && pr.getNumber() != null ? pr.getNumber() + " - " : "";
            result[i] = new ProjectItem(pr.getObjectID(), issueNumberR + pr.getName());
            result[i].setManager(employee.equals(pr.getManager()) || pr.isUserBackupManager(employee.getObjectID()));
            i++;
        }
        Arrays.sort(result, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getIssueStatuses(boolean isResolver) {
        List<SelectItem> items = new ArrayList<>();
        List<EdsReference> statuses = referenceManager.getIssueStatuses();
        for (EdsReference status : statuses) {
            SelectItem item = new SelectItem();
            item.setId(status.getObjectID());
            item.setName(referenceWfmMessageSource.localize(status.getCode(), status.getName()));
            if (EdsIssue._RESOLVED.equals(status.getCode())) {
                if (isResolver) {
                    items.add(item);
                }
            } else {
                items.add(item);
            }
        }
        return items.toArray(new SelectItem[]{});
    }

    public SelectItem[] getResolversRelatedTo(String relatedTo, Integer relatedId) {
        if (PROJECT_ISSUE.equals(relatedTo)) {
            EdsUser user = referenceManager.getUser();
            List<EdsEmployee> directors = employeeManager.getEmployeeByRole(EdsRole.DR);
            List<EdsEmployee> admins = employeeManager.getEmployeeByRole(EdsRole.ADMIN);
            Set<Integer> resolverIds = new HashSet<>();
            List<SelectItem> resolvers = new ArrayList<>();
            for (EdsEmployee e : directors) {
                if (isEmployeeActive(e) && !resolverIds.contains(e.getObjectID())) {
                    resolverIds.add(e.getObjectID());
                    resolvers.add(new SelectItem(e.getObjectID(), e.getName()));
                }
            }
            for (EdsEmployee a : admins) {
                if (isEmployeeActive(a) && !resolverIds.contains(a.getObjectID())) {
                    resolverIds.add(a.getObjectID());
                    resolvers.add(new SelectItem(a.getObjectID(), a.getName()));
                }
            }
            EdsProject project = projectManager.get(relatedId);
            EdsEmployee manager = project.getManager();
            if (isEmployeeActive(manager) && !resolverIds.contains(manager.getObjectID())) {
                resolverIds.add(manager.getObjectID());
                resolvers.add(new SelectItem(manager.getObjectID(), manager.getName()));
            }
            List<EdsEmployee> backupManagers = project.getBackupManagers();
            for (EdsEmployee backupManager : backupManagers) {
                if (isEmployeeActive(backupManager) && !resolverIds.contains(backupManager.getObjectID())) {
                    resolverIds.add(backupManager.getObjectID());
                    resolvers.add(new SelectItem(backupManager.getObjectID(), backupManager.getName()));
                }
            }
            PositionsSelectItem[] projectEmployees = getAssigneesWithPositions(project.getObjectID());
            for (PositionsSelectItem proEmp : projectEmployees) {
                if (proEmp.getEmployeeId() != null && !resolverIds.contains(proEmp.getEmployeeId())) {
                    resolverIds.add(proEmp.getEmployeeId());
                    resolvers.add(new SelectItem(proEmp.getEmployeeId(), proEmp.getName()));
                }
            }
            if (user instanceof EdsEmployee) {
                EdsEmployee leader = ((EdsEmployee) referenceManager.getUser()).getEmployeeTeam().getTeam().getLeader();
                if (leader != null && isEmployeeActive(leader) && !resolverIds.contains(leader.getObjectID())) {
                    resolverIds.add(leader.getObjectID());
                    resolvers.add(new SelectItem(leader.getObjectID(), leader.getName()));
                }
            }
            return resolvers.toArray(new SelectItem[]{});
        } else if (EMPLOYEE_ISSUE.equals(relatedTo)) {
            List<EdsEmployee> employees = employeeManager.getEmployeesByPermissionCode(PermissionConstants.INCIDENT_OWNER);
            Set<Integer> resolverIds = new HashSet<>();
            List<SelectItem> resolvers = new ArrayList<>();
            for (EdsEmployee e : employees) {
                if (isEmployeeActive(e) && !resolverIds.contains(e.getObjectID())) {
                    resolverIds.add(e.getObjectID());
                    resolvers.add(new SelectItem(e.getObjectID(), e.getName()));
                }
            }

            resolvers.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            return resolvers.toArray(new SelectItem[]{});

        }
        return new SelectItem[0];
    }

    private boolean isEmployeeActive(EdsUser user) {
        return !user.getDeleted();
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPriorities() {
        List<EdsReference> priorities = referenceManager.listReferences(EdsIssue.ISSUE_PRIORITY);
        return commonServiceLocal.reference2SelectItem(priorities, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getAssigneesWithPositions(Integer projectId) {
        return taskService.getAssigneesWithPositions1(projectId);
    }

    @Transactional
    public PositionsSelectItem[] getAssigneesWithPositionsForIssue(Integer projectId, Integer issueID) {
        return getAssigneesWithPositionsForIssue(projectId, issueID, false);
    }

    private PositionsSelectItem[] getAssigneesWithPositionsForIssue(Integer projectId, Integer issueID, boolean onlySelected) {
        ArrayList<PositionsSelectItem> selectItems = new ArrayList<>();
        ArrayList<Integer> existingAssignees = new ArrayList<>();
        boolean isManager = false;
        EdsUser user = userManager.getUser();
        if (issueID != null) {
            EdsIssue issue = issueManager.get(issueID);
            if ((issue.getCreator() != null && issue.getCreator().equals(user)) || issue.getProject().getManager().equals(user) ||
                    issue.getProject().isUserBackupManager(user.getObjectID()) || user.hasRole(roleManager.get(EdsRole.DR)) ||
                    user.hasRole(roleManager.get(EdsRole.ADMIN))) {
                isManager = true;
            }
            Integer viewAsFilter = ServerUtils.getMaxRoleID(user.getRolesAsIntegersString());
            for (EdsEmployeeTask et : issue.getUnDeletedAssignments()) {
                EdsEmployee resEmpl = et.getProjectEmployee().getEmployeeDepartment().getEmployee();
                if (MEM.equals(viewAsFilter)) {
                    if (user.getObjectID().equals(et.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                        existingAssignees.add(resEmpl.getObjectID());
                        break;
                    }
                } else {
                    existingAssignees.add(resEmpl.getObjectID());
                }
            }
        }
        List<Object[]> projectMembers = issueID != null ? taskServiceLocal.getProjectMembers(projectId, user, isManager) : taskServiceLocal.getProjectMembers(projectId, user, PermissionConstants.PM_ASSIGN_ISSUE_TO_MEMBER);
        List<PositionsSelectItem> newAssignees = new ArrayList<>();
        if (projectMembers != null && !projectMembers.isEmpty()) {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee resEmployee = (EdsEmployee) item[1];
                EdsDepartment team = (EdsDepartment) item[2];
                PositionsSelectItem sItem = new PositionsSelectItem();
                if (resEmployee != null) {
                    sItem.setId(pe.getObjectID());
                    sItem.setName(resEmployee.getName());
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    if (team != null) {
                        sItem.setDepartmentId(team.getObjectID());
                        sItem.setDepartmentName(team.getName());
                    }
                    sItem.setMyself(false);
                    if (user.getObjectID().equals(resEmployee.getObjectID())) {
                        sItem.setMyself(true);
                        sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    }
                    if (existingAssignees.contains(resEmployee.getObjectID())) {
                        sItem.setAssignee(true);
                        selectItems.add(sItem);
                    }
                    newAssignees.add(sItem);
                }
            }
        }

        if (onlySelected) {
            return selectItems.toArray(new PositionsSelectItem[]{});
        }

        if (!newAssignees.isEmpty()) {
            PositionsSelectItem[] newAssigneesArray = newAssignees.toArray(new PositionsSelectItem[]{});
            Arrays.sort(newAssigneesArray, new AbstractComparator<PositionsSelectItem>() {
                public int compare(PositionsSelectItem o1, PositionsSelectItem o2) {
                    return internalCompare(o1.getName() != null ? o1.getName() : "", o2.getName() != null ? o2.getName() : "", 1);
                }
            });
            return newAssigneesArray;
        }
        return new PositionsSelectItem[0];
    }

    public Integer createIssueItem(IssueItem issueItem) {
        return taskServiceLocal.createIssueItem(issueItem);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public IssueItem editIssueItem(Integer objectId, Integer relationId) {

        IssueItem issueTransfer = new IssueItem();

        if (objectId != null) {
            EdsIssue issueDomain = issueManager.get(objectId);
            EdsTask task = taskManager.getTaskByIssueId(issueDomain.getObjectID());
            issueTransfer = issueDomain.getRPC();
            issueTransfer.setTaskID(task.getObjectID());
            if (issueDomain.getResolver() != null && issueDomain.getResolver().getDeleted()) {
                issueTransfer.setResolverID(null);
                issueTransfer.setResolverName("");
            }
            issueTransfer.setShowTimer(false);
            if (issueDomain.getIssueStatus() != null && issueDomain.getEnableTimesheet() && !EdsTask.CLOSED.equals(issueDomain.getIssueStatus().getCode()) && !EdsTask.COMPLETED.equals(issueDomain.getIssueStatus().getCode())) {
                issueTransfer.setShowTimer(true);
                issueTransfer.setTimerIsStarted(clockManager.getActiveClockForCurrentUser(issueDomain.getObjectID(), PM_ISSUE_TIMER, employeeManager.getUser().getObjectID()) != null);
            }

            //issue numbering
            NumberData numberData = generateIssueNumber();
            numberData.setNumberString(issueDomain.getNumber());
            numberData.setIntNumber(issueDomain.getIntNumber());
            issueTransfer.setNumberData(numberData);
            //issue employees
            TaskInvolvedMember[] issueMembers = taskServiceLocal.getAssignments(issueDomain.getObjectID());
            if (issueMembers != null && issueMembers.length > 0) {
                ArrayList<Integer> issueEmployeeIDs = new ArrayList<>();
                PositionsSelectItem[] issueEmployees = new PositionsSelectItem[issueMembers.length];
                HashMap<Integer, PositionsSelectItem> issueEmployeesCO = new HashMap<>();
                int i = 0;
                EdsUser user = userManager.getUser();
                Integer viewAsFilter = ServerUtils.getMaxRoleID(user.getRolesAsIntegersString());
                for (TaskInvolvedMember employee : issueMembers) {
                    if (MEM.equals(viewAsFilter)/* || CLIENT.equals(viewAsFilter)*/) {
                        if (user.getObjectID().equals(employee.getAssignEmployeeID())) {
                            issueEmployeeIDs.add(employee.getEmployeeID());
                            issueEmployees[i] = new PositionsSelectItem();
                            issueEmployees[i].setId(employee.getEmployeeID());
                            issueEmployees[i].setName(employee.getEmployee());
                            issueEmployees[i].setDepartmentId(employee.getEmployeeTeamID());
                            issueEmployees[i].setDepartmentName(employee.getEmployeeTeam());
                            issueEmployees[i].setActualTime(employee.getActualTime());
                            EdsEmployee edsEmployee = employeeManager.get(employee.getAssignEmployeeID());
                            if (edsEmployee != null) {
                                issueEmployees[i].setPositionName(edsEmployee.getPosition() != null ? edsEmployee.getPosition().getName() : "");
                            }
                            issueEmployees[i].setTime(employee.getEstimatedTime());
                            issueEmployeesCO.put(employee.getEmployeeID(), issueEmployees[i]);
                            break;
                        }
                    } else {
                        issueEmployeeIDs.add(employee.getEmployeeID());
                        issueEmployees[i] = new PositionsSelectItem();
                        issueEmployees[i].setId(employee.getEmployeeID());
                        issueEmployees[i].setName(employee.getEmployee());
                        issueEmployees[i].setDepartmentId(employee.getEmployeeTeamID());
                        issueEmployees[i].setDepartmentName(employee.getEmployeeTeam());
                        issueEmployees[i].setActualTime(employee.getActualTime());
                        EdsEmployee edsEmployee = employeeManager.get(employee.getAssignEmployeeID());
                        if (edsEmployee != null) {
                            issueEmployees[i].setPositionName(edsEmployee.getPosition() != null ? edsEmployee.getPosition().getName() : "");
                        }
                        issueEmployees[i].setTime(employee.getEstimatedTime());
                        issueEmployeesCO.put(employee.getEmployeeID(), issueEmployees[i]);
                        i++;
                    }
                }
                issueTransfer.setIssueEmployeeIDs(issueEmployeeIDs);
                issueTransfer.setIssueEmployees(issueEmployees);
                issueTransfer.setIssueEmployeeItems(issueEmployeesCO);
            } else {
                if (issueDomain.getProject() != null) {
                    issueTransfer.setIssueEmployees(getAssigneesWithPositionsForIssue(issueDomain.getProject().getObjectID(), issueDomain.getObjectID(), true));
                }
            }
            //issue attachments
            EdsProject issueDomainProject = issueDomain.getProject();
            if (issueDomainProject != null) {
                List<FileResource> issueAttachments = attachmentUtilsManager.getAttachments(F_PR_ISSUE, issueDomainProject.getObjectID(), issueDomain.getObjectID());//toDo
                FileItem[] fileItems = getIssueAttachments(issueAttachments);
                issueTransfer.setAttachments(fileItems);
            } else {
                issueTransfer.setAttachments(new FileItem[]{});
            }
            //issue relations
            issueTransfer.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_ISSUE, issueDomain.getObjectID())));
            //
            issueTransfer.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(issueDomain.getTaskCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Issues)));
            EdsUser user = userManager.getUser();
            if (issueDomainProject != null && (issueDomainProject.getManager().equals(user) || issueDomainProject.isUserBackupManager(user.getObjectID()))) {
                issueTransfer.setPermission(EDIT);
            } else if (user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN)) || user.hasRole(roleManager.get(EdsRole.TL))) {
                issueTransfer.setPermission(EDIT);
            } else {
                if (issueDomain.getCreator() == null) {
                    issueDomain.setCreator(user);
                }
                if (issueDomain.getCreator().equals(user)) {
                    issueTransfer.setPermission(EDIT);
                } else {
                    issueTransfer.setPermission(READ);
                }
            }
        }
        EdsProject defaultProject = userManager.getUser().getCompany().getDefaultProject();
        if (defaultProject != null) {
            issueTransfer.setDefaultProjectID(defaultProject.getObjectID());
            issueTransfer.setDefaultProjectName(defaultProject.getName());
        }
        if (relationId != null) {
            EdsTask task = taskManager.get(relationId);
            if (task.getProject() != null) {
                issueTransfer.setDefaultProjectID(task.getProject().getObjectID());
                issueTransfer.setDefaultProjectName(task.getProject().getName());
            }
        }
        issueTransfer.setPriorities(getPriorities());
        issueTransfer.setStatuses(getIssueStatuses(false));
        if (!userManager.getUser().hasRole(roleManager.getByCode(SUPPLIER))) {
            issueTransfer.setReportedByItems(getCompanyEmployees());
        }

        issueTransfer.setSupplier(userManager.getUser().hasRole(SUPPLIER));

        return issueTransfer;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem editProjectItem(Integer relationId) {
        if (relationId == null) {
            return null;
        }

        EdsTask task = taskManager.get(relationId);
        if (task != null && task.getProject() != null) {
            return new ProjectItem(task.getProject().getObjectID(), task.getProject().getName());
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployees() {
        //company employees
        EdsUser currentUser = userManager.getUser();
        EdsEmployee currentEmployee = null;
        if (currentUser.isEmployee()) {
            currentEmployee = employeeManager.get(currentUser.getObjectID());
        }
        List<EdsEmployee> employees = employeeManager.getEmployees(currentEmployee != null ? currentEmployee.getCompany() : currentUser.getCompany());
        if (currentEmployee != null) {
            employees.remove(currentEmployee);
        }

        SelectItem[] result = new SelectItem[employees.size() + 1];
        if (currentEmployee != null) {
            result[0] = new SelectItem(currentEmployee.getObjectID(), currentEmployee.getFullName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
        } else {
            result[0] = new SelectItem(currentUser.getObjectID(), currentUser.getFullName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
        }
        int i = 1;
        for (EdsEmployee employee : employees) {
            result[i] = new SelectItem(employee.getObjectID(), employee.getName());
            i++;
        }
        Arrays.sort(result, Comparator.comparing(SelectItem::getName));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectTasks(Integer projectId) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectId);
        fp.setTaskStatusId(ALL_DUE_TASKS);
        List<EdsTask> tasks = taskManager.list(fp);
        SelectItem[] result = new SelectItem[tasks.size()];
        int i = 0;
        for (EdsTask task : tasks) {
            result[i] = new SelectItem();
            result[i].setId(task.getObjectID());
            result[i].setName(task.getName());
            i++;
        }
        return result;
    }

    public Boolean checkAccess(Integer issueID, String permission) {
        EdsUser user = issueManager.getUser();
        EdsTask issue = taskManager.get(issueID);
        if (issue.getProject() != null && user.getObjectID().equals(issue.getProject().getManager().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.PMOFPR));
        }
        if (issue.getCreator() != null && user.getObjectID().equals(issue.getCreator().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.CREATOR));
        }
        return (issue.getCreator() != null && issue.getCreator().getObjectID().equals(user.getObjectID())) || ServerUtils.hasPermission(permission);
    }

    public HashSet<String> getPermissions(Integer issueID, String context) {
        return rolePermissionServiceLocal.getPermissionList(context, taskServiceLocal.checkForArtificateRoles(issueID));
    }

    public Boolean deleteIssue(Integer issueID) {
        EdsUser user = issueManager.getUser();
        EdsTask issue = taskManager.get(issueID);
        EdsIssue realIssue = issueManager.get(issue.getObjectID());
        if (!checkAccess(issueID, PermissionConstants.PM_ISSUE_REMOVE)) {
            return false;
        }
        issue.setUpdater(user);
        issue.setLastUpdateTime(new Date());
        taskManager.deleteTask(issue);
        baseEventPostProcessor.registerEvent(IssueEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, realIssue, user);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsIssue.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(issueID);
        ServerUtils.kpiLog(log, kpiLog, "Delete issue");
        return true;
    }

    public Boolean deleteIssueMass(ArrayList<Integer> objectIDs) {
        for (Integer ID : objectIDs) {
            deleteIssue(ID);
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateIssueNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = issueManager.getIssuesLastIntNumber();
        if (settings != null && settings.getIssueNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getIssueNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_ISSUE_PREFIX);
        }
    }

    @Override
    public boolean saveIssueEditCellValue(IssueListItem rowValue, String columnCodeName) {
        EdsIssue edsIssue = issueManager.get(rowValue.getObjectID());
        try {
            EdsTaskCustomFields issueCustomField = edsIssue.getTaskCustomFields();
            if (issueCustomField == null) {
                issueCustomField = new EdsTaskCustomFields();
                taskCFManager.create(issueCustomField);
                edsIssue.setTaskCustomFields(issueCustomField);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(issueCustomField, rowValue.getCustomFieldsMap(), columnCodeName);
            edsIssue.setLastUpdateTime(new Date());
//            solrManager.addTaskToIndex(edsIssue, company.getObjectID());
            taskSolrComponent.index(edsIssue);
            return true;
        } catch (Exception e) {
            log.error("Issue List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }
}
