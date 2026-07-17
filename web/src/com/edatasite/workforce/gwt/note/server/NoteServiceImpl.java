package com.edatasite.workforce.gwt.note.server;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsNoteComment;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NoteCommentManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.goal.BusinessGoalManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.NoteEventListenerImpl;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.note.client.rpc.NoteService;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.to.AttachmentTO;
import com.edatasite.workforce.rest.base.to.CommentTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.NoteTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Sherali
 * Date: 31-Aug-2009
 * Time: 14:29:33
 */
@Transactional
@Service("noteService")
public class NoteServiceImpl implements NoteService, NoteServiceLocal, Constants {

    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private MeetingManager meetingManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private BusinessGoalManager businessGoalManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private NoteCommentManager noteCommentManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<HistoryListItem> noteList(ListingFilterParameter fp) {
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(fp);//getNoteList(fp);
        EdsUser employee = noteHistoryManager.getUser();
        Integer total = noteHistoryManager.getListCount(fp);
        ArrayList<HistoryListItem> results = new ArrayList<>();
        for (EdsNoteHistory noteHistory : noteList) {
            HistoryListItem item = noteHistory.getHistoryItem();
            item.setEditable(employee.equals(noteHistory.getEmployee()));
            initNoteRelatedType(item, noteHistory);
            results.add(item);
        }
        return new ListResult<>(results, total);
    }

    @Override
    @Transactional
    public void deleteNote(Integer id) {
        try {
            EdsNoteHistory noteHistory = noteHistoryManager.get(id);
            if (noteHistory != null) {
                noteHistoryManager.delete(noteHistoryManager.get(id));
                if (noteHistory.getAttachment() != null) {
                    EdsFileHeader edsFileHeader = fileHeaderManager.getCurrentBody(noteHistory.getAttachment().getObjectID());
                    if (edsFileHeader != null) {
                        documentsServiceLocal.deleteFile(edsFileHeader.getObjectID());
                    }
                }
                //delete updates for note
                List<EdsMyUpdate> updates = myUpdateManager.getUpdatesForAffectedID(id, MyUpdateTypeManager.WORKSPACE_NOTE);
                for (EdsMyUpdate update : updates) {
                    myUpdateManager.delete(update);
                }
            }
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Integer saveNote(HistoryListItem item) {
        EdsUser user = noteHistoryManager.getUser();
        EdsNoteHistory noteHistory;
        if (item.getObjectID() != null) {
            noteHistory = noteHistoryManager.get(item.getObjectID());
            if (noteHistory == null) {
                noteHistory = new EdsNoteHistory();
            }
        } else {
            noteHistory = new EdsNoteHistory();
        }
        noteHistory.setSubject(item.getSubject());
        noteHistory.setRelatedId(item.getRelatedId());
        noteHistory.setRelatedTo(item.getRelatedToId());
        if (item.getComment() != null) {
            noteHistory.setComment(item.getComment().length() > 10000 ? item.getComment().substring(0, 10000) : item.getComment());
        }
        noteHistory.setEmployee(user);
        noteHistory.setVisibility(item.isVisibility());
        noteHistory.setAttachment((EdsUpload) uploadManager.get(item.getAttachmentID()));
        if (item.getObjectID() == null || noteHistory.getObjectID() == null) {
            noteHistory.setEventDate(new Date());
            noteHistoryManager.create(noteHistory);
            if (noteHistory.getObjectID() != null) {
                baseEventPostProcessor.registerEvent(NoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, noteHistory, user);
            }
        } else {
            if (noteHistory.getEventDate() == null) {
                noteHistory.setEventDate(new Date());
            }
            noteHistoryManager.update(noteHistory);
            if (noteHistory.getObjectID() != null) {
                baseEventPostProcessor.registerEvent(NoteEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, noteHistory, user);
            }
        }

        if (item.getProjectEmployees() != null) {
            item.getProjectEmployees();//send email to project employees
            for (IdTime projectEmployee : item.getProjectEmployees()) {
                if (Integer.valueOf(0).equals(projectEmployee.getTime())) {
                    EdsProjectEmployee pe = projectEmployeeManager.get(projectEmployee.getId());
                    EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
                    sendMessageToTaskAndProject(item, user, noteHistory, employee);
                } else if (Integer.valueOf(1).equals(projectEmployee.getTime())) {
                    EdsUser clientContact = userManager.get(projectEmployee.getId());
                    sendMessageToTaskAndProject(item, user, noteHistory, clientContact);
                }
            }
        }
        return noteHistory.getObjectID();

    }

    @Override
    @Transactional
    public Integer saveNoteComment(NewsComment data) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(data.getNewsId());
        EdsNoteComment noteComment;
        if (data.getCommentId() != null) {
            noteComment = noteCommentManager.get(data.getCommentId());
            noteComment.setDate(data.getDate());
        } else {
            noteComment = new EdsNoteComment();
            noteComment.setDate(new Date());
        }

        noteComment.setComment(data.getComment());
        noteComment.setUser(noteCommentManager.getUser());
        noteComment.setNote(noteHistory);
        noteCommentManager.create(noteComment);
        if (data.getCommentId() != null) {
            //it's ok do nothing
        } else {
            data.setDate(new Date());
            data.setUsername(noteCommentManager.getUser().getName());
        }

        return noteComment.getObjectID();
    }

    @Override
    public CommentTO getCommentForAPI(Integer id) {
        EdsNoteComment noteComment = noteCommentManager.get(id);
        CommentTO commentTO = new CommentTO();
        commentTO.setId(noteComment.getObjectID());
        commentTO.setNoteId(noteComment.getNote().getObjectID());
        commentTO.setMessage(noteComment.getComment());
        UserTO userTO = new UserTO(noteComment.getUser().getObjectID(), noteComment.getUser().getName());
        /*if (noteComment.getUser().getPhoto() != null) {
            userTO.setImageUrl(commonService.getImageUrl(noteComment.getUser().getPhoto().getObjectID()));
        }*/
        commentTO.setUser(userTO);
        commentTO.setCreationDate(ServerUtils.dateToLong(noteComment.getDate()));
        return commentTO;
    }

    @Override
    @Transactional
    public void deleteNoteComment(Integer id) {
        EdsNoteComment noteComment = noteCommentManager.get(id);
        noteCommentManager.delete(noteComment);
    }

    private void sendMessageToTaskAndProject(HistoryListItem item, EdsUser fromUser, EdsNoteHistory noteHistory, EdsUser employeeOrClientContact) {
        boolean isTask = true;
        if (item.getRelatedToId() == 1) {
            isTask = false;
        }
        try {
            messageManager.sendToAddNoteToTaskAndProject(fromUser, employeeOrClientContact, noteHistory.getComment(), isTask, item.getRelatedId());
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
    }

    static final DateFormat format = new SimpleDateFormat("MMM d, yyyy");

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getNoteDates(ListingFilterParameter fp) {
        Map<String, Integer> hMap = new TreeMap<>();
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(fp);
        for (EdsNoteHistory note : noteList) {
            String date = "";
            try {
                date = String.valueOf(format.parse(format.format(note.getEventDate())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (hMap.containsKey(date)) {
                int value = hMap.get(date);
                hMap.put(date, value + 1);
            } else {
                hMap.put(date, 1);
            }
        }
        SelectItem[] items = new SelectItem[hMap.size()];
        Set<Map.Entry<String, Integer>> entries = hMap.entrySet();
        Iterator<Map.Entry<String, Integer>> it = entries.iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
            SelectItem item = new SelectItem();
            item.setName(entry.getKey() + "||" + entry.getValue());
            item.setDescription(format.format(new Date((Long.valueOf((String) entry.getKey())))));
            SelectItem min;
            min = item;
            try {
                for (int k = 0; k < items.length; k++) {
                    if (items[k] instanceof SelectItem) {
                        if (format.parse(min.getDescription()).getTime() < format.parse(items[k].getDescription()).getTime()) {
                            min = items[k];
                            items[k] = item;
                            item = min;
                        }
                    }
                }
            } catch (ParseException ignored) {
            }
            items[i] = min;
            i++;
        }

        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getNoteRelation(ListingFilterParameter fp) {
        Map<Integer, Integer> hMap = new TreeMap<>();
        HashMap<Integer, String> hEmpName = new HashMap<>();
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(fp);
        for (EdsNoteHistory note : noteList) {
            String relation;
            int relationId;
            if (EdsNoteHistory.PROJECT == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Project";
                relationId = EdsNoteHistory.PROJECT;
            } else if (EdsNoteHistory.TASK == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Task";
                relationId = EdsNoteHistory.TASK;
            } else if (EdsNoteHistory.CLIENT == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Client";
                relationId = EdsNoteHistory.CLIENT;
            } else if (EdsNoteHistory.EMPLOYEE == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Employee";
                relationId = EdsNoteHistory.EMPLOYEE;
            } else if (EdsNoteHistory.DEPARTMENT == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Department";
                relationId = EdsNoteHistory.DEPARTMENT;
            } else if (EdsNoteHistory.SUPPLIER == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Supplier";
                relationId = EdsNoteHistory.SUPPLIER;
            } else if (EdsNoteHistory.PM_ISSUE == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Issue";
                relationId = EdsNoteHistory.PM_ISSUE;
            } else if (EdsNoteHistory.PM_CONTRACT == note.getRelatedTo() && note.getRelatedId() != null) {
                relation = "Contract";
                relationId = EdsNoteHistory.PM_CONTRACT;
            } else {
                relationId = 0;
                relation = "Unrelated";
            }

            Integer id = relationId;
            if (hMap.containsKey(id)) {
                int value = hMap.get(id);
                hMap.put(id, value + 1);
            } else {
                hMap.put(id, 1);
                hEmpName.put(id, relation);
            }
        }
        SelectItem[] items = new SelectItem[hMap.size()];
        Set<Map.Entry<Integer, Integer>> entries = hMap.entrySet();
        Iterator<Map.Entry<Integer, Integer>> it = entries.iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
            SelectItem item = new SelectItem();
            String name = hEmpName.get(entry.getKey());
            item.setName(name + " (" + entry.getValue() + " Notes)");
            item.setDescription(name);
            item.setId((Integer) entry.getKey());
            SelectItem min;
            min = item;
            for (int k = 0; k < items.length; k++) {
                if (items[k] instanceof SelectItem) {
                    if (min.getDescription().compareTo(items[k].getDescription()) > 0) {
                        min = items[k];
                        items[k] = item;
                        item = min;
                    }
                }
            }
            items[i] = min;
            i++;
        }

        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem getNote(Integer objectId) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(objectId);
        HistoryListItem item = noteHistory.getHistoryItem();
        item.setEditable(employeeManager.getUser().equals(noteHistory.getEmployee()));
        item.setEmployee(noteHistory.getEmployee().getFullName());
        item.setEmployeeID(noteHistory.getEmployee().getObjectID());

        initNoteRelatedType(item, noteHistory);

        return item;
    }

    private void initNoteRelatedType(HistoryListItem item, EdsNoteHistory noteHistory) {
        String companyID = ServerSecurityContext.getInstance().getCompanyId();

        if (EdsNoteHistory.PROJECT == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsProject project = projectManager.get(noteHistory.getRelatedId());
            if (project.getNumber() != null) {
                item.setRelatedToNumber(project.getNumber());
            }
            item.setRelatedName(project.getName());
            item.setRelatedToName("Project");
            String projectID = "project|summary/" + project.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(projectID, companyID));
        } else if (EdsNoteHistory.TASK == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsTask task = taskManager.get(noteHistory.getRelatedId());
            if (task.getNumber() != null) {
                item.setRelatedToNumber(task.getNumber());
            }
            item.setRelatedName(task.getName());
            item.setRelatedToName("Task");
            String taskID = "task|summary/" + task.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(taskID, companyID));
            item.setNotesComments(commonService.getNotecomments(noteHistory.getObjectID()));
        } else if (EdsNoteHistory.CLIENT == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsCrmAccount client = clientManager.get(noteHistory.getRelatedId());
            if (client.getNumber() != null) {
                item.setRelatedToNumber(client.getNumber());
            }
            item.setRelatedName(client.getName());
            item.setRelatedToName("Client");
            String clientID = "client|summary/" + client.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(clientID, companyID));
        } else if (EdsNoteHistory.EMPLOYEE == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsUser emp = userManager.get(noteHistory.getRelatedId());
            if (emp.getEmployee().getProfile().getEmployeeCode() != null) {
                item.setRelatedToNumber(emp.getEmployee().getProfile().getEmployeeCode());
            }
            item.setRelatedName(emp.getName());
            item.setRelatedToName("Employee");
            String empID = "employee|summary/" + emp.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(empID, companyID));
        } else if (EdsNoteHistory.DEPARTMENT == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsDepartment department = departmentManager.get(noteHistory.getRelatedId());
            item.setRelatedName(department.getName());
            item.setRelatedToName("Department");
            String departmentID = "department|summary/" + department.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(departmentID, companyID));
        } else if (EdsNoteHistory.SUPPLIER == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsCrmAccount supplier = clientManager.get(noteHistory.getRelatedId());
            if (supplier.getNumber() != null) {
                item.setRelatedToNumber(supplier.getNumber());
            }
            item.setRelatedName(supplier.getName());
            item.setRelatedToName("Supplier");
            String supplierID = "suppliersummary|summary/" + supplier.getObjectID().toString();
            item.setSectionLink("Accounting.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(supplierID, companyID));
        } else if (EdsNoteHistory.PM_ISSUE == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsIssue issue = issueManager.get(noteHistory.getRelatedId());
            if (issue.getNumber() != null) {
                item.setRelatedToNumber(issue.getNumber());
            }
            item.setRelatedName(issue.getName());
            item.setRelatedToName("Issue");
            String issueID = "issue|summary/" + issue.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(issueID, companyID));
        } else if (EdsNoteHistory.PM_CONTRACT == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsContract contract = contractManager.get(noteHistory.getRelatedId());
            if (contract.getNumber() != null) {
                item.setRelatedToNumber(contract.getNumber());
            }
            item.setRelatedName(contract.getName());
            item.setRelatedToName("Contract");
            String contractID = "contract|summary/" + contract.getObjectID().toString();
            item.setSectionLink("ProjectManagement.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(contractID, companyID));
        } else if ((EdsNoteHistory.PERSONAL_GOAL == noteHistory.getRelatedTo() || EdsNoteHistory.DEPARTMENT_GOAL == noteHistory.getRelatedTo() || EdsNoteHistory.PROJECT_GOAL == noteHistory.getRelatedTo() || EdsNoteHistory.BUSINESS_GOAL == noteHistory.getRelatedTo()) && noteHistory.getRelatedId() != null) {
            EdsGoal goal = goalManager.get(noteHistory.getRelatedId());
            item.setRelatedName(goal.getTitle());
            String type = "";
            if (EdsNoteHistory.PERSONAL_GOAL == noteHistory.getRelatedTo()) {
                item.setRelatedToName("Personal Goal");
                type = PERSONAL_GOAL;
            } else if (EdsNoteHistory.DEPARTMENT_GOAL == noteHistory.getRelatedTo()) {
                item.setRelatedToName("Department Goal");
                type = DEPARTMENT_GOAL;
            } else if (EdsNoteHistory.PROJECT_GOAL == noteHistory.getRelatedTo()) {
                item.setRelatedToName("Project Goal");
                type = PROJECT_GOAL;
            } else if (EdsNoteHistory.BUSINESS_GOAL == noteHistory.getRelatedTo()) {
                item.setRelatedToName("Bussines Goal");
                type = BUSINESS_GOAL;
            }
            String goalID = "goal|summary/" + goal.getObjectID().toString() + "/" + type;
            item.setSectionLink("Hrms.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(goalID, companyID));
        } else if (EdsNoteHistory.COMPANY_GOAL == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsBusinessGoal companyGoal = businessGoalManager.get(noteHistory.getRelatedId());
            item.setRelatedName(companyGoal.getTitle());
            item.setRelatedToName("Company Goal");
            String issueID = "goal|summary/" + companyGoal.getObjectID().toString() + "/" + COMPANY_GOAL;
            item.setSectionLink("Hrms.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(issueID, companyID));
        } else if (EdsNoteHistory.VACANCY == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsVacancy vacancy = vacancyManager.get(noteHistory.getRelatedId());
            if (vacancy.getVacancyNumber() != null) {
                item.setRelatedToNumber(vacancy.getVacancyNumber());
            }
            item.setRelatedName(vacancy.getJobTitle());
            item.setRelatedToName("Vacancy");
            item.setSectionLink("Hrms.html?link=");
            String linkID = "vacancy|summary/" + vacancy.getObjectID().toString();
            item.setRelatedToLink(getNoteRelatedToLink(linkID, companyID));
        } else if (EdsNoteHistory.PLACEMENT == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsPlacement placement = placementManager.get(noteHistory.getRelatedId());
            item.setRelatedName(placement.getCandidate().getName());
            item.setRelatedToName("Placement");
            item.setSectionLink("Hrms.html?link=");
            //editable option
            boolean isEditable = true;
            if (placement.getStatus() != null) {
                EdsReference hiredStatus = referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_HIRED);
                isEditable = !placement.getStatus().getObjectID().equals(hiredStatus.getObjectID());
            }
            String linkID = "placement|summary/" + placement.getObjectID().toString() + "/" + isEditable;
            item.setRelatedToLink(getNoteRelatedToLink(linkID, companyID));
        } else if ((EdsNoteHistory.CRM_ACCOUNT == noteHistory.getRelatedTo() || EdsNoteHistory.CRM_CASE == noteHistory.getRelatedTo() || EdsNoteHistory.CRM_LEAD == noteHistory.getRelatedTo()
                || EdsNoteHistory.CRM_CONTACT == noteHistory.getRelatedTo() || EdsNoteHistory.CRM_OPPORTUNITY == noteHistory.getRelatedTo() || EdsNoteHistory.CRM_CAMPAIGN == noteHistory.getRelatedTo())
                && noteHistory.getRelatedId() != null) {

            String itemID = "";

            if (EdsNoteHistory.CRM_ACCOUNT == noteHistory.getRelatedTo()) {
                EdsCrmAccount crmAccount = crmAccountManager.get(noteHistory.getRelatedId());
                item.setRelatedToNumber(crmAccount.getNumber());
                item.setRelatedName(crmAccount.getName());
                item.setRelatedToName("Account");
                itemID = "account|summary/" + noteHistory.getRelatedId();
            } else if (EdsNoteHistory.CRM_CASE == noteHistory.getRelatedTo()) {
                EdsCase edsCase = caseManager.get(noteHistory.getRelatedId());
                if (edsCase != null) {
                    item.setRelatedToNumber(edsCase.getCaseNumberString());
                    item.setRelatedName(edsCase.getSubject());
                    item.setRelatedToName("Case");
                    itemID = "case|summary/" + noteHistory.getRelatedId();
                }
            } else if (EdsNoteHistory.CRM_LEAD == noteHistory.getRelatedTo() || EdsNoteHistory.CRM_CONTACT == noteHistory.getRelatedTo()) {
                EdsCrmContact crmContact = crmContactManager.get(noteHistory.getRelatedId());
                item.setRelatedName(crmContact.getName());

                if (EdsCrmContact.LEAD_CONTACT.equals(crmContact.getContactType())) {
                    item.setRelatedToName("Lead");
                    itemID = "lead|summary/" + noteHistory.getRelatedId();
                } else if (EdsCrmContact.CANDIDATE.equals(crmContact.getContactType())) {
                    item.setRelatedToName("Candidate");
                    itemID = "candidate|summary/" + noteHistory.getRelatedId();
                } else {
                    item.setRelatedToName("Contact");
                    itemID = "contact|summary/" + noteHistory.getRelatedId();
                }
            } else if (EdsNoteHistory.CRM_OPPORTUNITY == noteHistory.getRelatedTo()) {
                EdsOpportunity opportunity = opportunityManager.get(noteHistory.getRelatedId());
                item.setRelatedToNumber(opportunity.getNumber());
                item.setRelatedName(opportunity.getName());
                item.setRelatedToName("Opportunity");
                itemID = "opportunity|summary/" + noteHistory.getRelatedId();
            } else if (EdsNoteHistory.CRM_CAMPAIGN == noteHistory.getRelatedTo()) {
                EdsCampaign campaign = campaignManager.get(noteHistory.getRelatedId());
                item.setRelatedName(campaign.getName());
                item.setRelatedToName("Campaing");
                itemID = "campaign|summary/" + noteHistory.getRelatedId();
            }
            item.setSectionLink("Crm.html?link=");
            item.setRelatedToLink(getNoteRelatedToLink(itemID, companyID));
        } else if (EdsNoteHistory.MEETING_MINUTES == noteHistory.getRelatedTo() && noteHistory.getRelatedId() != null) {
            EdsMeetingMinutes meetingMinutes = meetingManager.get(noteHistory.getRelatedId());
            if (meetingMinutes != null) {
                String number = meetingMinutes.getMeetingNumber() != null ? meetingMinutes.getMeetingNumber() + "-" : "";
                item.setRelatedToNumber(number);
            }
            if (meetingMinutes != null) {
                item.setRelatedName(meetingMinutes.getTitle());
            }
            item.setRelatedToName("Meeting");
        }
    }

    /**
     * Get note for API with short data
     *
     * @param objectId
     * @return noteTO
     */

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NoteTO getNoteForAPI(Integer objectId) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(objectId);
        if (noteHistory != null) {
            HistoryListItem noteItem = noteHistory.getHistoryItem();
            NoteTO noteTO = new NoteTO(noteItem);
            if (noteHistory.getAttachment() != null) {
                AttachmentTO attachmentTO = new AttachmentTO();
                EdsFileHeader edsFileHeader = fileHeaderManager.getCurrentBody(noteHistory.getAttachment().getObjectID());
                if (edsFileHeader != null) {
                    attachmentTO.setId(edsFileHeader.getObjectID());
                }
                attachmentTO.setBodyId(noteHistory.getAttachment().getObjectID());
                attachmentTO.setName(noteHistory.getAttachment().getOriginalName());
                attachmentTO.setContentType(noteHistory.getAttachment().getContentType());
                attachmentTO.setContentLength(noteHistory.getAttachment().getSize());
                attachmentTO.setDuration(noteHistory.getAttachment().getDuration());
                String fileLink = commonServiceLocal.getFileUrl(noteHistory.getAttachment().getObjectID());
                attachmentTO.setAmazonLink(fileLink);
                attachmentTO.setDownloadLink(fileLink);
                noteTO.setAttachment(attachmentTO);

            }
            /*if (noteHistory.getEmployee().getPhoto() != null) {
            noteItem.setEmployeeImageUrl(commonService.getImageUrl(noteHistory.getEmployee().getPhoto().getObjectID()));
           }*/
            return noteTO;
        }
        return null;
    }

    /**
     * Get note list for API with short data
     *
     * @param filter includes relationId,relationType
     * @return note list with count
     */

    @Override
    public ListResultTO<NoteTO> getNoteListForAPI(ListingFilterParameter filter) {
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(filter);
        if (noteList == null || noteList.isEmpty()) {
            return null;
        }
        ArrayList<NoteTO> noteTOList = new ArrayList<>(noteList.size());
        for (EdsNoteHistory noteHistory : noteList) {
            NoteTO noteTO = new NoteTO();
            noteTO.setId(noteHistory.getObjectID());
            noteTO.setSubject(noteHistory.getSubject());
            noteTO.setComment(noteHistory.getComment());
            UserTO userTO = new UserTO(noteHistory.getEmployee().getObjectID(), noteHistory.getEmployee().getName());
            if (filter.isWithImage()) {
                if (noteHistory.getEmployee().getPhoto() != null) {
                    userTO.setImageUrl(commonServiceLocal.getFileUrl(noteHistory.getEmployee().getPhoto().getObjectID()));
                }
            }
            if (noteHistory.getAttachment() != null) {
                AttachmentTO attachmentTO = new AttachmentTO();
                EdsFileHeader edsFileHeader = fileHeaderManager.getCurrentBody(noteHistory.getAttachment().getObjectID());
                if (edsFileHeader != null) {
                    attachmentTO.setId(edsFileHeader.getObjectID());
                }
                attachmentTO.setBodyId(noteHistory.getAttachment().getObjectID());
                attachmentTO.setName(noteHistory.getAttachment().getOriginalName());
                attachmentTO.setContentType(noteHistory.getAttachment().getContentType());
                attachmentTO.setContentLength(noteHistory.getAttachment().getSize());
                attachmentTO.setDuration(noteHistory.getAttachment().getDuration());
                String fileLink = commonServiceLocal.getFileUrl(noteHistory.getAttachment().getObjectID());
                attachmentTO.setAmazonLink(fileLink);
                noteTO.setAttachment(attachmentTO);
                attachmentTO.setDownloadLink(fileLink);
            }
            noteTO.setUser(userTO);

            if (noteHistory.isVisibility() == null) {
                noteTO.setVisibility(new SelectItemTO(NoteEnum.INTERNAL.getName(), NoteEnum.INTERNAL.getCode()));
            } else if (noteHistory.isVisibility()) {
                noteTO.setVisibility(new SelectItemTO(NoteEnum.PRIVATE.getName(), NoteEnum.PRIVATE.getCode()));
            } else {
                noteTO.setVisibility(new SelectItemTO(NoteEnum.PUBLIC.getName(), NoteEnum.PUBLIC.getCode()));
            }

            noteTO.setCreationDate(ServerUtils.dateToLong(noteHistory.getEventDate()));
            noteTO.setRelatedId(noteHistory.getRelatedId());
            noteTO.setRelatedToId(noteHistory.getRelatedTo());
            noteTO.setCommentCount(noteCommentManager.getCommentCountByNoteID(noteHistory.getObjectID()));

            noteTOList.add(noteTO);
        }
        return new ListResultTO<>(noteTOList.size(), noteTOList);
    }

    /**
     * Get note comment list for API
     *
     * @param filter includes relationType e.g. EdsNoteHistory.PROJECT, EdsNoteHistory.TASK
     *               relationId e.g. EdsProject Id, EdsTask Id
     *               relationToId e.g. EdsNoteHistory Id
     * @return list of commentTo object
     */
    @Override
    public ListResultTO<CommentTO> getCommentListForAPI(ListingFilterParameter filter) {
        EdsNoteHistory noteHistory = noteHistoryManager.get(filter.getRelationToID());
        ArrayList<CommentTO> noteCommentTOList = new ArrayList<>();
        if (noteHistory != null) {
            Set<EdsNoteComment> noteCommentList = noteHistory.getNoteComments();
            for (EdsNoteComment comment : noteCommentList) {
                CommentTO commentTO = new CommentTO();
                commentTO.setId(comment.getObjectID());
                commentTO.setNoteId(noteHistory.getObjectID());
                UserTO userTO = new UserTO(comment.getUser().getObjectID(), comment.getUser().getName());
                if (comment.getUser().getPhoto() != null) {
                    userTO.setImageUrl(commonService.getImageUrl(comment.getUser().getPhoto().getObjectID()));
                }
                commentTO.setUser(userTO);
                commentTO.setMessage(comment.getComment());
                commentTO.setCreationDate(ServerUtils.dateToLong(comment.getDate()));
                noteCommentTOList.add(commentTO);
            }
        }
        return new ListResultTO<>(noteCommentTOList.size(), noteCommentTOList);

    }

    private String getNoteRelatedToLink(String linkID, String companyID) {
        return EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(linkID)) + "&cid=" + EncryptionHelper.encryptURL(companyID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getNoteUsers(ListingFilterParameter fp) {
        Map<Integer, Integer> hMap = new TreeMap<>();
        HashMap<Integer, String> hEmpName = new HashMap<>();
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(fp);
        for (EdsNoteHistory note : noteList) {
            Integer emId = note.getEmployee().getObjectID();
            hEmpName.put(emId, note.getEmployee().getName());
            if (hMap.containsKey(emId)) {
                int value = hMap.get(emId);
                hMap.put(emId, value + 1);
            } else {
                hMap.put(emId, 1);
                hEmpName.put(emId, note.getEmployee().getName());
            }
        }
        SelectItem[] items = new SelectItem[hMap.size()];
        Set<Map.Entry<Integer, Integer>> entries = hMap.entrySet();
        Iterator<Map.Entry<Integer, Integer>> it = entries.iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) it.next();
            SelectItem item = new SelectItem();
            String name = hEmpName.get(entry.getKey());
            item.setName(name + " (" + entry.getValue() + " Notes)");
            item.setDescription(name);
            item.setId((Integer) entry.getKey());
            SelectItem min;
            min = item;
            for (int k = 0; k < items.length; k++) {
                if (items[k] instanceof SelectItem) {
                    if (min.getDescription().compareTo(items[k].getDescription()) > 0) {
                        min = items[k];
                        items[k] = item;
                        item = min;
                    }
                }
            }
            items[i] = min;
            i++;
        }

        return items;
    }

    @Override
    public HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployees() {
        HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> allEmployees = new HashMap<>();
        SelectItem[] companyEmployees = availabilityServiceLocal.getCompanyEmployeesAsAdmin();
        for (SelectItem result : companyEmployees) {

            WfmTreeItem departmentT = null;
            if (result.getDescription() != null && !"".equals(result.getDescription())) {
                String[] idAndName = result.getDescription().split("#");
                String departmentID = idAndName[0];
                String departmentName = idAndName[1];
                //departments
                if (allEmployees.keySet().size() > 0) {
                    for (WfmTreeItem existDepartmentT : allEmployees.keySet()) {
                        if (existDepartmentT.getId().equals(Integer.valueOf(departmentID))) {
                            departmentT = existDepartmentT;
                            break;
                        } else {
                            departmentT = new WfmTreeItem();
                            departmentT.setId(Integer.valueOf(departmentID));
                            departmentT.setName(departmentName);
                            departmentT.setChecked(true);
                        }
                    }
                } else {
                    departmentT = new WfmTreeItem();
                    departmentT.setId(Integer.valueOf(departmentID));
                    departmentT.setName(departmentName);
                    departmentT.setChecked(true);
                }
            } else {
                departmentT = new WfmTreeItem();
                departmentT.setId(0);
                departmentT.setName(commonLocalizer.localize("companyEmployees"));
                departmentT.setChecked(true);
            }
            //employees
            WfmTreeItem employeeT = new WfmTreeItem();
            employeeT.setId(result.getId());
            employeeT.setName(result.getName());
            employeeT.setChecked(true);
            //
            allEmployees.computeIfAbsent(departmentT, k -> new LinkedList<>());
            if (allEmployees.get(departmentT) != null) {
                allEmployees.get(departmentT).add(employeeT);
            }
        }
        return allEmployees;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void setDepartmentManager(DepartmentManager departmentManager) {
        this.departmentManager = departmentManager;
    }
}