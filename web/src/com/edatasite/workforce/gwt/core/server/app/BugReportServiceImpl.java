package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.goal.BusinessGoalManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.impl.MessageManagerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.NoteEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.BugReportRegistrationCustomEventListenerImpl;
import com.edatasite.workforce.gwt.note.client.rpc.NoteService;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 12:20:07 PM
 */

@Transactional
@Service("bugReportService")
public class BugReportServiceImpl implements BugReportService, BugReportServiceLocal, Constants {
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private BugReportManager bugReportManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private NoteService noteService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private NoteCommentManager noteCommentManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private BusinessGoalManager businessGoalManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private MeetingManager meetingManager;
    @Autowired
    private AllInOneService allInOneService;


    /**
     * @param priority           - set the priority
     * @param reportText         - the text typed or message in thrown exception
     * @param reportedById       - the userID
     * @param viewSection        - the section where bug occured
     * @param identificationType - whether bug was reported by the user or just automatically by the system
     * @param userAgent          - set the user agent
     * @param fileItems          - set the file upload items
     */
    public void sendBugReport(String priority, String reportText, String subjectText, Integer reportedById,
                              String viewSection, String identificationType, String userAgent, FileItem[] fileItems) {
        EdsBugReport bugReport = new EdsBugReport();

        EdsUser creator = reportedById != null ? employeeManager.get(reportedById) : userManager.getUser();
        if (creator == null) {
            ServerSecurityContext.getInstance().setCompanyId("1");
            creator = userManager.get(2);//Default Lochin
        }
        Date creationTime = new Date();
        creationTime.setMinutes(creationTime.getMinutes() + creator.getUserTimezone().getRawOffset() / 60000);
        bugReport.setCreatedFrom(viewSection);
        bugReport.setCreationTime(creationTime);
        bugReport.setCreator(creator.getObjectID());
        bugReport.setCreatorName(creator.getUserName());
        bugReport.setEmail(creator.getEmail());
        EdsCompany company = creator.getCompany();
        bugReport.setCompany(company.getObjectID());
        bugReport.setCompanyName(company.getName());
        if (priority != null && !"".equals(priority)) {
            bugReport.setPriority(priority);
        } else {
            bugReport.setPriority(BUG_PRIORITY_MEDIUM);
        }
        bugReport.setStatus(BUG_STATUS_NEW);
        bugReport.setDescription(reportText);
        bugReport.setSubject(subjectText);
        bugReport.setType(identificationType);
        bugReport.setUserAgent(userAgent);

        if (reportText != null && !reportText.toLowerCase().contains("uncaughtexception") && !reportText.toLowerCase().contains("implicit")) {
            bugReportManager.create(bugReport);

            if (fileItems != null) {
                for (FileItem fileItem : fileItems) {
                    EdsBugAttachment bugAttachment = new EdsBugAttachment();
                    bugAttachment.setAttachmentID(fileItem.getId());
                    bugAttachment.setBug(bugReport);
                    bugReport.addBugAttachments(bugAttachment);
                }
            }
        }

        EdsBusinessEvent registrationEvent = baseEventPostProcessor.registerEvent(BugReportRegistrationCustomEventListenerImpl.TYPE, BugReportRegistrationCustomEventListenerImpl.EVENT_CASE_REGISTRATION, bugReport, creator);
        registrationEvent.setCompanyId(company.getObjectID());
    }

//    @Transactional
//    private void registerFromBugReportToCase(EdsBugReport bugReport) {
//        ServerSecurityContext.getInstance().setCompanyId(3737);
//        EdsUser user = employeeManager.get(6551);
//        EdsBusinessEvent registrationEvent = baseEventPostProcessor.registerEvent(BugReportRegistrationCustomEventListenerImpl.TYPE, BugReportRegistrationCustomEventListenerImpl.EVENT_CASE_REGISTRATION, bugReport, user);
//        registrationEvent.setCompanyId(3737);
//    }

    public void sendBugReport(BugReportItem bugReportItem) {
        sendBugReport(bugReportItem.getPriority(), bugReportItem.getReportText(), bugReportItem.getSubjectText(), null,
                bugReportItem.getReportSection(), EdsBugReport.BYUSER, bugReportItem.getUserAgent(), bugReportItem.getAttachments());
    }

    public void sendBugReportNew(BugReportItem bugReportItem) {
        sendBugReport(bugReportItem.getPriority(), bugReportItem.getReportText(), bugReportItem.getSubjectText(), bugReportItem.getReportedBy(),
                bugReportItem.getReportSection(), "", bugReportItem.getUserAgent(), bugReportItem.getAttachments());
    }

    public Integer addNote(HistoryListItem item) {
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
        noteHistory.setSubject(item.getSubject().length() > 250 ? item.getSubject().substring(0, 250) : item.getSubject());
        noteHistory.setRelatedId(item.getRelatedId());
        noteHistory.setRelatedTo(item.getRelatedToId());
        noteHistory.setComment(item.getComment().length() > /*3000*/10000 ? item.getComment().substring(0, /*3000*/10000) : item.getComment());
        noteHistory.setEmployee(user);
        noteHistory.setVisibility(item.isVisibility());
        noteHistory.setSuperUser(ServerUtils.isSuperUser());
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
        if (item.getEmployeeIds() != null && item.getEmployeeIds().size() > 0) {
            for (Integer employeeID : item.getEmployeeIds()) {
                EdsEmployee employee = employeeManager.get(employeeID);
                messageManager.sendToAddNoteMessage(item, user, noteHistory, employee);
            }
        }
        return noteHistory.getObjectID();
    }

    private void sendMessageToTaskAndProject(HistoryListItem item, EdsUser fromUser,
                                             EdsNoteHistory noteHistory, EdsUser employeeOrClientContact) {
        boolean isTask = true;
        if (item.getRelatedToId() == 1) {
            isTask = false;
        }
        try {
            messageManager.sendToAddNoteToTaskAndProject(fromUser,
                    employeeOrClientContact, noteHistory.getComment(), isTask, item.getRelatedId());
        } catch (EdsTemplateException e) {
            e.printStackTrace();
        }
    }

    public void deleteNote(Integer id) {
        noteService.deleteNote(id);
    }

    public void deleteNoteComment(Integer noteCommentId) {
        EdsNoteComment noteComment = noteCommentManager.get(noteCommentId);
        noteCommentManager.delete(noteComment);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<HistoryListItem> noteList() {
        ListingFilterParameter fp = new ListingFilterParameter();
        return noteService.noteList(fp);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getNoteRelatedList(int i) {
        SelectItem[] items = new SelectItem[0];
        if (i == 1) {//project
            List<EdsProject> projectList = projectManager.list();
            items = new SelectItem[projectList.size()];
            int k = 0;
            for (EdsProject project : projectList) {
                SelectItem item = new SelectItem();
                item.setId(project.getObjectID());
                item.setName(project.getName());
                items[k++] = item;
            }
        } else if (i == 2) {//task
            EdsReference inProgress = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
            EdsReference waiting = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.WAITING_FOR_SOMEONE_ELSE);
//            EdsReference onhold = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.ON_HOLD);
            EdsReference notStarted = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStatusValues(inProgress.getObjectID() + "," + waiting.getObjectID() + "," + /*onhold.getObjectID()*/notStarted.getObjectID());
            List<EdsTask> taskList = taskManager.list(fp);
            items = new SelectItem[taskList.size()];
            int k = 0;
            for (EdsTask task : taskList) {
                SelectItem item = new SelectItem();
                item.setId(task.getObjectID());
                item.setName(task.getName());
                items[k++] = item;
            }
        } else if (i == 3) {//client
            List<EdsCrmAccount> clientList = crmAccountManager.getList(new ListingFilterParameter(), EdsCrmAccount.CUSTOMER);
            items = new SelectItem[clientList.size()];
            int k = 0;
            for (EdsCrmAccount client : clientList) {
                SelectItem item = new SelectItem();
                item.setId(client.getObjectID());
                item.setName(client.getName());
                items[k++] = item;
            }
        } else if (i == 4) {//employee
            items = allInOneService.getEmployeesAsSelectItem(new ListingFilterParameter());
        } else if (i == 5) {//department
            List<EdsDepartment> departmentList = departmentManager.list();
            items = new SelectItem[departmentList.size()];
            int k = 0;
            for (EdsDepartment department : departmentList) {
                SelectItem item = new SelectItem();
                item.setId(department.getObjectID());
                item.setName(department.getName());
                items[k++] = item;
            }
        } else if (i == 6) {//supplier
            List<EdsCrmAccount> supplierList = crmAccountManager.getList(new ListingFilterParameter(), EdsCrmAccount.SUPPLIER);
            items = new SelectItem[supplierList.size()];
            int k = 0;
            for (EdsCrmAccount supplier : supplierList) {
                SelectItem item = new SelectItem();
                item.setId(supplier.getObjectID());
                item.setName(supplier.getName());
                items[k++] = item;
            }
        } else if (i == 7) {//issue
            List<EdsIssue> issueList = issueManager.list(new ListingFilterParameter());
            items = new SelectItem[issueList.size()];
            int k = 0;
            for (EdsIssue issue : issueList) {
                SelectItem item = new SelectItem();
                item.setId(issue.getObjectID());
                item.setName(issue.getName());
                items[k++] = item;
            }
        } else if (i == 8) {//meeting minutes
            List<EdsMeetingMinutes> meetingList = meetingManager.getMeetingMinutesList();
            items = new SelectItem[meetingList.size()];
            int k = 0;
            for (EdsMeetingMinutes meetingMinutes : meetingList) {
                SelectItem item = new SelectItem();
                item.setId(meetingMinutes.getObjectID());
                item.setName(meetingMinutes.getTitle());
                items[k++] = item;
            }
        } else if (i == 9 || i == 10 || i == 11 || i == 12) {//goal
            EdsReference goalReference = null;
            if (i == 9) {
                goalReference = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
            } else if (i == 10) {
                goalReference = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
            } else if (i == 11) {
                goalReference = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PROJECT_GOAL);
            } else if (i == 12) {
                goalReference = referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.BUSINESS_GOAL);
            }

            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setCrmEntityId(goalReference != null ? goalReference.getObjectID() : null);
            filterParametrs.setAllGoals(true);
            List<EdsGoal> goalList = goalManager.list(filterParametrs);

            items = new SelectItem[goalList.size()];
            int k = 0;
            for (EdsGoal goal : goalList) {
                SelectItem item = new SelectItem();
                item.setId(goal.getObjectID());
                item.setName(goal.getTitle());
                items[k++] = item;
            }

        } else if (i == 13) {//company goal
            List<EdsBusinessGoal> goalList = businessGoalManager.list(new ListingFilterParameter());
            items = new SelectItem[goalList.size()];
            int k = 0;
            for (EdsBusinessGoal goal : goalList) {
                SelectItem item = new SelectItem();
                item.setId(goal.getObjectID());
                item.setName(goal.getTitle());
                items[k++] = item;
            }

        }
        return items;
    }

    public void setBugReportManager(BugReportManager bugReportManager) {
        this.bugReportManager = bugReportManager;
    }

    public void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    public void setMessageManager(MessageManagerImpl messageManager) {
        this.messageManager = messageManager;
    }

    public void setEmployeeManager(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setDepartmentManager(DepartmentManager departmentManager) {
        this.departmentManager = departmentManager;
    }

    public BugReportItem[] getFeedBackHistory(Integer userID) {
        List<EdsBugReport> bugReports = bugReportManager.getFeedBacksByUser(userID != null ? userID : userManager.getUser().getObjectID());
        BugReportItem[] feedBackItems = new BugReportItem[bugReports.size()];
        int i = 0;
        for (EdsBugReport bugReport : bugReports) {
            BugReportItem bugReportItem = new BugReportItem();
            bugReportItem.setReportText(bugReport.getDescription() != null ? bugReport.getDescription() : "");
            bugReportItem.setSubjectText(bugReport.getSubject() != null ? bugReport.getSubject() : "");
            bugReportItem.setCreationDate(bugReport.getCreationTime());
            bugReportItem.setBugStatus(bugReport.getStatus() != null ? bugReport.getStatus() : "");
            feedBackItems[i] = bugReportItem;
            i++;
        }
        return feedBackItems;
    }

    public Boolean isEmployee() {
        return bugReportManager.getUser() != null && bugReportManager.getUser().isEmployee();
    }
}
