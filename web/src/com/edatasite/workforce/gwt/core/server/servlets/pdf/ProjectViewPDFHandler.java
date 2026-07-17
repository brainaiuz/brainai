
package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE;

public class ProjectViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private TaskService taskService;

    private static final Logger log = LoggerFactory.getLogger(ProjectViewPDFHandler.class);
    private final DecimalFormat numberFormat = new DecimalFormat("###.##");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        long begin = System.currentTimeMillis();
        boolean isKendah = company.getObjectID().equals(56895);

        HTMLParser htmlParser = new HTMLParser();
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        ITextCompanyData companyData = new ITextCompanyData();
        pdf.setCompanyData(companyData);

        // Company Data
        companyData.setCompanyName(escapeHtml(company.getName()));
        companyData.setAddress(company.getAddress1() != null ? company.getAddress1() : "");
        companyData.setCity(company.getCity() != null ? company.getCity() : "");
        companyData.setPostCode((company.getPostCode() != null && !"".equals(company.getPostCode())) ? company.getPostCode() : "");
        companyData.setCountry((company.getCountryZone() != null && company.getCountryZone().getCountry() != null) ? company.getCountryZone().getCountry().getName() : "");
        try {
            String imageUrl = getPdfLogoUrl(company, false);
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        companyData.setCompanyEmail((company.getEmail() != null && company.getEmail().length() > 1 ? (company.getEmail()) : ""));
        companyData.setCompanyFax((company.getFaxNumber() != null && company.getFaxNumber().length() > 1 ? (company.getFaxNumber()) : ""));
        companyData.setCompanyPhone((company.getPhone() != null && company.getPhone().length() > 1 ? (company.getPhone()) : ""));

        ITextSummaryView pdfData = new ITextSummaryView();
        pdf.setSummaryView(pdfData);
        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        EdsUser user = uploadManager.getUser();

        RequestObject requestObject = (RequestObject) dataClass;
        Integer projectId = requestObject.getObjectID();

        EdsProject project = projectManager.get(projectId);

        Map<EdsDepartment, List<EdsEmployee>> departmentEmployees = new HashMap<>();
        Map<String, Map<Integer, EdsTask>> tasksByStatus = new HashMap<>();

        List<EdsProjectEmployee> pes = projectManager.getEmployeesByProject(projectId);

        Map<Integer, EdsEmployee> projectEmployees = new HashMap<>();
        int count = 0;
        int size = pes.size();
        boolean[] accepted = new boolean[size];
        for (int j = count; j < size; j++) {
            if (!accepted[j]) {
                List<EdsEmployee> es = new LinkedList<>();
                EdsDepartment dep = null;
                if (pes.get(j).getEmployeeDepartment() != null) {
                    dep = pes.get(j).getEmployeeDepartment().getTeam();
                }

                for (int i = count; i < size; i++) {
                    if (!accepted[i]
                            && dep != null
                            && pes.get(i).getEmployeeDepartment() != null
                            && dep.getObjectID().equals(
                            pes.get(i).getEmployeeDepartment().getTeam().getObjectID())) {
                        es.add(pes.get(i).getEmployeeDepartment().getEmployee());
                        projectEmployees.put(pes.get(i).getEmployeeDepartment().getEmployee().getObjectID(), pes.get(i).getEmployeeDepartment().getEmployee());
                        accepted[i] = true;
                    }
                }
                departmentEmployees.put(dep, es);
                accepted[j] = true;
            }
            count++;
        }
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable projectTable = new CustomisedITextTable();
        projectTable.setName(pdfWfmMessageSource.localize(PdfLocalizationName.projectInformation));

        Double[] projectCostAndTimeSpent = timeSheetManager.getProjectCostAndTimeSpent(projectId, null);
        EdsCrmContact relationContact = projectManager.getRelationContact(project.getNumber());

        String projectName = getResultOrLongDash(project.getName());
        String projectNum = getResultOrLongDash(project.getNumber());
        String projectDescription = getResultOrLongDash(project.getDescription());
        String projectManagerName = project.getManager() != null ? getResultOrLongDash(project.getManager().getFullName()) : "—";
        String projectClient = project.getClient() != null ? getResultOrLongDash(project.getClient().getName()) : "—";
        String projectClientContact = project.getClient() != null && project.getClient().getPrimaryContact() != null ? escapeHtml(project.getClient().getPrimaryContact().getFullName()) : "";
        String projectStatus = project.getStatus() != null && project.getStatus().getCode() != null && project.getStatus().getName() != null ? referenceWfmMessageSource.localize(project.getStatus().getCode(), project.getStatus().getName()) : "—";
        String projectStartDate = project.getStartDate() != null ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(project.getStartDate(), user.getUserTimezone())) : "—";
        String projectDueDate = project.getEndDate() != null ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(project.getEndDate(), user.getUserTimezone())) : "—";
        String estimatedTime = projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[PROJECT_ESTIMATED_TIME_SPENT] != null ? projectCostAndTimeSpent[PROJECT_ESTIMATED_TIME_SPENT].intValue() : 0) : "00:00";
        String projectHourSpend = projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[PROJECT_HOURS_SPENT] != null ? projectCostAndTimeSpent[PROJECT_HOURS_SPENT].intValue() : 0) : "00:00";
        String projectActualHourSpend = projectCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(projectCostAndTimeSpent[PROJECT_ACTUAL_TIME_SPENT] != null ? projectCostAndTimeSpent[PROJECT_ACTUAL_TIME_SPENT].intValue() : 0) : "00:00";
        String relationContactName = relationContact != null ? escapeHtml(relationContact.getName()) : "";
        String relationContactDateOfBirth = relationContact != null && relationContact.getDateOfBirth() != null ? shortDateFormat.format(ServerUtils.convertServerDateToUserDate(relationContact.getDateOfBirth(), user.getUserTimezone())) : "";

        projectTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        projectTable.addRowWithCode("PROJECT_NUMBER", commonLocalizer.localize(PdfLocalizationName.projectNumber), escapeHtml(projectNum));
        projectTable.addRowWithCode("PROJECT_NAME", commonLocalizer.localize(PdfLocalizationName.projectName), escapeHtml(projectName));
        projectTable.addRowWithCode("DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description), projectDescription.replace("\r\n", "<br/>").replace("\n", "<br/>"));
        projectTable.addRowWithCode("PROJECT_MANAGER", commonLocalizer.localize(PdfLocalizationName.projectManager), projectManagerName);
        projectTable.addRowWithCode("PROJECT_CLIENT", commonLocalizer.localize(PdfLocalizationName.customer), projectClient);
        projectTable.addRowWithCode("PROJECT_CLIENT_CONTACT", commonLocalizer.localize(PdfLocalizationName.contact), projectClientContact);
        projectTable.addRowWithCode("PROJECT_STATUS", commonLocalizer.localize(PdfLocalizationName.status), escapeHtml(projectStatus));
        projectTable.addRowWithCode("START_DATE", commonLocalizer.localize(PdfLocalizationName.startDateField), projectStartDate);
        projectTable.addRowWithCode("DUE_DATE", commonLocalizer.localize(PdfLocalizationName.dueDate), projectDueDate);
        projectTable.addRowWithCode("ESTIMATED_TIME", commonLocalizer.localize(PdfLocalizationName.estimatedTime), estimatedTime);
        projectTable.addRowWithCode("TIME_SPENT", commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), projectHourSpend);
        projectTable.addRowWithCode("ACTUAL_TIME_SPENT", commonLocalizer.localize(PdfLocalizationName.actualTimeSpent), projectActualHourSpend);
        projectTable.addRowWithCode("RELATION_CONTACT_NAME", "", relationContactName);
        projectTable.addRowWithCode("RELATION_CONTACT_DATE_OF_BIRTH", "", relationContactDateOfBirth);
        customData.put(PROJECT_CONTENT_TABLE, projectTable);


        // Lists of tasks by status
        Map<Integer, EdsTask> notStarted = new LinkedHashMap<>();
        Map<Integer, EdsTask> inProgress = new LinkedHashMap<>();
        Map<Integer, EdsTask> completed = new LinkedHashMap<>();
        Map<Integer, EdsTask> awaited = new LinkedHashMap<>();
        Map<Integer, EdsTask> closed = new LinkedHashMap<>();

        // For each team...
        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customDataList = new HashMap<>();
        LinkedList<HashMap<String, CustomisedITextTable>> projectMembers = new LinkedList<>();
        LinkedList<HashMap<String, CustomisedITextTable>> projectTasks = new LinkedList<>();
        int i = 0;
        List<EdsTask> tasksList = new LinkedList<>();
        for (EdsDepartment dep : departmentEmployees.keySet()) {
            if (dep != null) {
                HashMap<String, CustomisedITextTable> listDepartmentDetails = new HashMap<>();
                CustomisedITextTable membersInvolvedTable = new CustomisedITextTable();
                membersInvolvedTable.setName(commonLocalizer.localize(PdfLocalizationName.membersInvolved) + " (" + escapeHtml(dep.getName()) + ")");
                membersInvolvedTable.addColumn("MEMBERS", commonLocalizer.localize(PdfLocalizationName.membersOnly));
                membersInvolvedTable.addColumn("NOT_STARTED", commonLocalizer.localize(PdfLocalizationName.notStarted));
                membersInvolvedTable.addColumn("IN_PROGRESS", commonLocalizer.localize(PdfLocalizationName.inProgress));
                membersInvolvedTable.addColumn("WAITING_FOR_SOMEONE", commonLocalizer.localize(PdfLocalizationName.waitingForSomeone));
                membersInvolvedTable.addColumn("COMPLETED", commonLocalizer.localize(PdfLocalizationName.completed));
                membersInvolvedTable.addColumn("CLOSED", commonLocalizer.localize(PdfLocalizationName.closed));
                membersInvolvedTable.addColumn("ESTIMATED_TIME", commonLocalizer.localize(PdfLocalizationName.estimatedTime));
                membersInvolvedTable.addColumn("TIME_SPENT", commonLocalizer.localize(PdfLocalizationName.timeSpentOnly));
                membersInvolvedTable.addColumn("ACTUAL_TIME_SPENT", commonLocalizer.localize(PdfLocalizationName.actualTimeSpent));
                for (EdsEmployee e : departmentEmployees.get(dep)) {
                    List<EdsEmployeeTask> ets = employeeTaskManager.getProjectEmployeeTasks(e.getObjectID(), project.getObjectID());
                    String memberName = e.getFullName();
                    String notStarted_ = "—", inProgress_ = "—", awaited_ = "—", completedCount_ = "—", closedCount_ = "—", totalHourSpent_ = "00:00", estmatedTimee = "00:00", actualHorsSpentt = "00:00";
                    if (ets.size() > 0) {
                        int notStartedCount = 0;
                        int inProgressCount = 0;
                        int awaitedCount = 0;
                        int completedCount = 0;
                        int closedCount = 0;
                        Integer totalHourSpent = 0;
                        Integer estmatedTime = 0;
                        Integer actualHorsSpent = 0;
                        for (EdsEmployeeTask et : ets) {
                            EdsTask task = et.getTask();
                            Double[] projectTimeSpent = timeSheetManager.getTimeSpentByEmployee(task.getObjectID(), e.getObjectID());
                            if (task != null && task.getStatus() != null && !"".equals(task.getStatus().getName())) {
                                if (task.getStatus().getCode().equals(EdsTask.NOT_STARTED)) {
                                    notStarted.put(task.getObjectID(), task);
                                    notStartedCount++;
                                }
                                if (task.getStatus().getCode().equals(EdsTask.IN_PROGRESS)) {
                                    inProgress.put(task.getObjectID(), task);
                                    inProgressCount++;
                                }
                                if (task.getStatus().getCode().equals(EdsTask.WAITING_FOR_SOMEONE_ELSE)) {
                                    awaited.put(task.getObjectID(), task);
                                    awaitedCount++;
                                }
                                if (task.getStatus().getCode().equals(EdsTask.COMPLETED)) {
                                    completed.put(task.getObjectID(), task);
                                    completedCount++;
                                }
                                if (task.getStatus().getCode().equals(EdsTask.CLOSED)) {
                                    closed.put(task.getObjectID(), task);
                                    closedCount++;
                                }
                                if (et.getEstimatedTime() != null) {
                                    estmatedTime += et.getEstimatedTime();
                                }
                                if (projectTimeSpent != null) {
                                    totalHourSpent += projectTimeSpent[2] != null ? projectTimeSpent[2].intValue() : 0;
                                    actualHorsSpent += projectTimeSpent[0] != null ? projectTimeSpent[0].intValue() : 0;
                                }
                            }
                        }
                        notStarted_ = notStartedCount != 0 ? String.valueOf(notStartedCount) : "—";
                        inProgress_ = inProgressCount != 0 ? String.valueOf(inProgressCount) : "—";
                        awaited_ = awaitedCount != 0 ? String.valueOf(awaitedCount) : "—";
                        completedCount_ = completedCount != 0 ? String.valueOf(completedCount) : "—";
                        closedCount_ = closedCount != 0 ? String.valueOf(closedCount) : "—";
                        totalHourSpent_ = ServerUtils.getTimeSpentHM(totalHourSpent);
                        estmatedTimee = Utils.timeSpentToString(estmatedTime);
                        actualHorsSpentt = ServerUtils.getTimeSpentHM(actualHorsSpent);
                    }
                    membersInvolvedTable.addRow(escapeHtml(memberName), notStarted_, inProgress_, awaited_, completedCount_, closedCount_, estmatedTimee, totalHourSpent_, actualHorsSpentt);
                }
                listDepartmentDetails.put(commonLocalizer.localize(PdfLocalizationName.membersInvolved) + " (" + escapeHtml(dep.getName()) + ")", membersInvolvedTable);
                projectMembers.add(listDepartmentDetails);
            }
        }
        customDataList.put(MEMBERS_INVOLVED, projectMembers);
        if (notStarted.size() > 0) {
            tasksByStatus.put(commonLocalizer.localize(PdfLocalizationName.notStarted), notStarted);
        }
        if (inProgress.size() > 0) {
            tasksByStatus.put(commonLocalizer.localize(PdfLocalizationName.inProgress), inProgress);
        }
        if (awaited.size() > 0) {
            tasksByStatus.put(commonLocalizer.localize(PdfLocalizationName.waitingForSomeone), awaited);
        }
        if (completed.size() > 0) {
            tasksByStatus.put(commonLocalizer.localize(PdfLocalizationName.completed), completed);
        }
        if (closed.size() > 0) {
            tasksByStatus.put(commonLocalizer.localize(PdfLocalizationName.closed), closed);
        }

        if (isKendah) {
            for (Map.Entry<Integer, EdsTask> item : completed.entrySet()) {
                tasksList.add(item.getValue());
            }
        }

        // For each status...
        for (String status : tasksByStatus.keySet()) {
            HashMap<String, CustomisedITextTable> listTaskDetails = new HashMap<>();
            CustomisedITextTable taskByStatusesTable = new CustomisedITextTable();
            taskByStatusesTable.setName(commonLocalizer.localize(PdfLocalizationName.tasks) + " (" + escapeHtml(status) + ")");

            taskByStatusesTable.addColumn("TASK_NAME", commonLocalizer.localize(PdfLocalizationName.taskName));
            taskByStatusesTable.addColumn("TASK_NUMBER", commonLocalizer.localize(PdfLocalizationName.taskNo));
            taskByStatusesTable.addColumn("DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description));
            taskByStatusesTable.addColumn("ASSIGNES", commonLocalizer.localize(PdfLocalizationName.assignees));
            taskByStatusesTable.addColumn("PRIORITY", commonLocalizer.localize(PdfLocalizationName.priority));
            taskByStatusesTable.addColumn("START_DATE", commonLocalizer.localize(PdfLocalizationName.startDateField));
            taskByStatusesTable.addColumn("DUE_DATE", commonLocalizer.localize(PdfLocalizationName.dueDate));
            taskByStatusesTable.addColumn("PERCENT", commonLocalizer.localize(PdfLocalizationName.percentCompleted));
            taskByStatusesTable.addColumn("ESTIMATED_TIME", commonLocalizer.localize(PdfLocalizationName.estimatedTime));
            taskByStatusesTable.addColumn("TIME_SPENT", commonLocalizer.localize(PdfLocalizationName.timeSpentOnly));
            taskByStatusesTable.addColumn("ACTUAL_TIME", commonLocalizer.localize(PdfLocalizationName.actualTimeSpent));

            List<EdsTask> tasks = new ArrayList<>(tasksByStatus.get(status).values());
            tasks.sort((o1, o2) -> ((Comparable) o2.getStartDate()).compareTo(o1.getStartDate()));

            if (tasks.size() > 0) {
                for (EdsTask t : tasks) {
                    String taskName = getResultOrLongDash(t.getName());
                    String taskNumber = getResultOrLongDash(t.getNumber());
                    Double[] taskCostAndTimeSpent = timeSheetManager.getTimeSpentByEmployee(t.getObjectID(), null);
                    String taskDescription = getResultOrLongDash(t.getDescription());
                    StringBuilder taskAssignees = new StringBuilder();
                    if ((t.getAssignments() != null) && t.getAssignments().size() > 0) {
                        Map<Integer, EdsEmployee> addedEmployees = new HashMap<>();
                        String delimitr = "";
                        for (EdsEmployeeTask emplTask : t.getAssignments()) {
                            if (emplTask.getProjectEmployee() != null && emplTask.getProjectEmployee().getEmployeeDepartment() != null && emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee() != null && !addedEmployees.containsKey(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID()) && projectEmployees.containsKey(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                                addedEmployees.put(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee());
                                taskAssignees.append(delimitr);
                                taskAssignees.append(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName() != null ? emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName() : "N/A");
                                delimitr = ", ";
                            }
                        }
                    }
                    String taskPriority = t.getPriority() != null ? referenceWfmMessageSource.localize(t.getPriority().getCode(), t.getPriority().getName()) : "—";
                    String taskStartDate = t.getStartDate() != null ? ServerUtils.getDateAsString(user.getUserDate(t.getStartDate())) : "—";
                    String taskDueDate = t.getDueDate() != null ? ServerUtils.getDateAsString(user.getUserDate(t.getDueDate())) : "—";
                    String taskPercentCompleted = t.getPercent() != null ? "" + t.getPercent() : "—";
                    String estmatedTime = taskCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[1] != null ? taskCostAndTimeSpent[1].intValue() : 0) : "00:00";
                    String actualHourSpent = taskCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[0] != null ? taskCostAndTimeSpent[0].intValue() : 0) : "00:00";
                    String taskHourSpent = taskCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[2] != null ? taskCostAndTimeSpent[2].intValue() : 0) : "00:00";

                    taskByStatusesTable.addRow(taskName, taskNumber, taskDescription, escapeHtml(taskAssignees.toString()), escapeHtml(taskPriority),
                            taskStartDate, taskDueDate, taskPercentCompleted, estmatedTime, taskHourSpent, actualHourSpent);
                }
                listTaskDetails.put(commonLocalizer.localize(PdfLocalizationName.tasks) + " (" + escapeHtml(status) + ")", taskByStatusesTable);
                projectTasks.add(listTaskDetails);
            }

        }
        customDataList.put(PROJECT_TASKS, projectTasks);
        if (isKendah) {
            List<CompanyCustomFieldItem> customFieldItemsProject = CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), commonService.getCompanyCustomFields(ViewName.Project));

            Date fromDate = null;
            Date toDate = null;
            if (customFieldItemsProject != null && !customFieldItemsProject.isEmpty()) {
                for (CompanyCustomFieldItem item : customFieldItemsProject) {
                    if ("From".equals(item.getFieldName())) {
                        if (item.getFieldDateNonConvertedValue() != null) {
                            fromDate = item.getFieldDateNonConvertedValue().getNonConvertedDate();
                        }
                    }
                    if ("To".equals(item.getFieldName())) {
                        if (item.getFieldDateNonConvertedValue() != null) {
                            toDate = item.getFieldDateNonConvertedValue().getNonConvertedDate();
                        }
                    }
                }
            }

            customData.put("PROJECT_TASKS_CUSTOM_FIELDS_TOTAL", getTaskCustomFieldData(tasksList, user, fromDate, toDate));
            customData.put("PROJECT_TASK_PRODUCT_TABLE", getTaskCustomFieldProductData(tasksList, user, fromDate, toDate));
            customDataList.put("PROJECT_TASKS_FOR_KENDAH", getProjectTaskForKendah(tasksList, projectEmployees, user, fromDate, toDate));
//            customData.put("TASK_ATTACHMENTS", getTaskAttachments(tasksList));
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(project.getObjectID());
        fp.setRelationType(RelationItem.TYPE_PROJECT);

        List<EdsIssue> issues = issueManager.list(fp);

        if (issues != null && issues.size() > 0) {
            CustomisedITextTable issueTable = new CustomisedITextTable();
            issueTable.setName(commonLocalizer.localize(PdfLocalizationName.relatedIssues));
            issueTable.addColumn("ISSUE_NAME", commonLocalizer.localize(PdfLocalizationName.name));
            issueTable.addColumn("ISSUE_DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description));
            issueTable.addColumn("ISSUE_PERIOD", commonLocalizer.localize(PdfLocalizationName.period));
            issueTable.addColumn("ISSUE_STATUS", commonLocalizer.localize(PdfLocalizationName.status));
            for (EdsIssue issue : issues) {
                String name = getResultOrLongDash(issue.getName());
                String description = getResultOrLongDash(issue.getDescription());
                String period = dateFormat(issue.getStartDate()) + " - " + dateFormat(issue.getDueDate());
                String status = issue.getIssueStatus() != null ? issue.getIssueStatus().getName() : "—";
                issueTable.addRow(name, description, period, status);
            }
            customData.put(ISSUE_DETAILS, issueTable);
        }
        HistoryListItem[] projectNotesLists = projectService.getProjectNotes(project.getObjectID(), null);
        if (projectNotesLists != null && projectNotesLists.length > 0) {
            CustomisedITextTable projectNotes = new CustomisedITextTable();
            projectNotes.setName(commonLocalizer.localize(PdfLocalizationName.projectNotes));

            projectNotes.addColumn("NOTE_SUBJECT", commonLocalizer.localize(PdfLocalizationName.subjectOnly));
            projectNotes.addColumn("NOTE_DESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description));
            projectNotes.addColumn("EMLPOYEE", commonLocalizer.localize(PdfLocalizationName.employee));
            projectNotes.addColumn("DATE", commonLocalizer.localize(PdfLocalizationName.date));

            for (HistoryListItem note : projectNotesLists) {
                String subject = getResultOrLongDash(note.getSubject());
                String description = null;
                try {
                    description = note.getComment() != null ? htmlParser.performParse(note.getComment()) : "—";
                } catch (SAXException | IOException e) {
                    description = "—";
                }
                String eventDate = longDateFormat(note.getEventDate());
                String employee = getResultOrLongDash(note.getEmployee());
                projectNotes.addRow(subject, description, employee, eventDate);
            }
            customData.put(NOTE_DETAILS, projectNotes);
        }
        FileResource[] attachmentsLists = projectService.getProjectAttachments(project.getObjectID());

        if (attachmentsLists != null && attachmentsLists.length > 0) {
            CustomisedITextTable projectAttachments = new CustomisedITextTable();
            projectAttachments.setName(commonLocalizer.localize(PdfLocalizationName.attachments));

            projectAttachments.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
            projectAttachments.addColumn(ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
            projectAttachments.addColumn("SIZE", commonLocalizer.localize(PdfLocalizationName.sizeField));
            projectAttachments.addColumn(DATE, commonLocalizer.localize(PdfLocalizationName.date));
            projectAttachments.addColumn("FILE_DOWNLOAD_URL", commonLocalizer.localize(PdfLocalizationName.urlname));

            for (FileResource attachment : attachmentsLists) {
                String attachments = getResultOrLongDash(attachment.getEncodedName());
                String description = getResultOrLongDash(attachment.getDescription());
                String date = longDateFormat(attachment.getModificationDate()) != null ? longDateFormat(attachment.getModificationDate()) : "—";
                String fileSize = attachment.getContentLength() != null ? getFileSizeAsString(attachment.getContentLength()) : "—";
                String downloadUrl = getDownloadURL(attachment);
                projectAttachments.addRow(escapeHtml(attachments), escapeHtml(description), fileSize, date, downloadUrl);
            }
            customData.put(ATTACHMENTS, projectAttachments);
        }

        CustomisedITextTable projectCustomTable = new CustomisedITextTable();
        projectCustomTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        Map<String, LinkedHashMap<String, Map<String, String>>> projectCustomFields = new HashMap<>();
        List<CompanyCustomFieldItem> customFieldItemsProject = CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), commonService.getCompanyCustomFields(ViewName.Project));

        if (customFieldItemsProject != null && customFieldItemsProject.size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            List<FileResource> fileResourceList = new ArrayList<>();
            for (CompanyCustomFieldItem item : customFieldItemsProject) {
                if (item != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(ServerUtils.convertServerDateToUserDate(item.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()))) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                        cols.put(COLUMN_VALUE, item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue()) ? escapeHtml(numberFormat.format(Double.valueOf(item.getFieldStringValue()))) : "—");
                    } else if (DATA_TYPE_FILE_UPLOAD.equals(item.getDataType()) && item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue())) {
                        fileResourceList.addAll(attachmentUtilsManager.getAttachments(F_CUSTOM_FIELD_ITEM, Double.valueOf(item.getFieldStringValue()).intValue(), item.getObjectId()));
                        String downloadUrl = "";
                        if (fileResourceList.size() > 0) {
                            for (FileResource file : fileResourceList) {
                                downloadUrl = getDownloadURL(file);
                            }
                        }
                        cols.put(COLUMN_VALUE, downloadUrl);
                    } else {
                        cols.put(COLUMN_VALUE, getResultOrLongDash(item.getFieldStringValue()));
                        SelectItem entityType = item.getEntityType();
                        if (entityType != null && entityType.getReferenceCode() != null) {
                            String code = entityType.getReferenceCode();
                            if (code.equals(EMPLOYEE)) {
                            }
                        }
                    }
                    if (item.getFieldName() != null) {
                        itemCusFields.put(item.getFieldName(), cols);
                    }
                }
            }
            projectCustomFields.put("PROJECT", itemCusFields);
            projectCustomTable.setCustomFields(projectCustomFields);
        }

        customData.put("PROJECT_CUSTOM_FIELD", projectCustomTable);
        customData.put("RELATION_CONTACT_CUSTOM_FIELD", getRelationContactCustomField(relationContact, user));
        customData.put("CURRENT_DATA", getCurrentData(user));

        pdf.setCustomListData(customDataList);
        pdf.setCustomData(customData);
        log.info("PROJECT VIEW CUSTOM PDF TOOK: " + (System.currentTimeMillis() - begin) + ", cId=" + SecurityContext.getCompanyID());
        return pdf;
    }

    private CustomisedITextTable getTaskAttachments(List<EdsTask> tasksList) {
        CustomisedITextTable taskAttachmentData = new CustomisedITextTable();
        taskAttachmentData.addColumn("FILE_NAME", "");
        taskAttachmentData.addColumn("FILE_DOWNLOAD_URL", "");
        taskAttachmentData.addColumn("TASK_NUMBER", "");
        for (EdsTask task : tasksList) {
            FileResource[] attachmentsLists = taskService.getTaskAttachments(task.getObjectID());
            if (attachmentsLists != null) {
                for (FileResource attachment : attachmentsLists) {
                    if (!attachment.getEncodedName().contains("signature_")) {
                        String fileName = escapeHtml(attachment.getEncodedName());
                        String fileDownloadURL = escapeHtml(getDownloadURL(attachment));
                        String taskNumber = escapeHtml(task.getNumber());
                        taskAttachmentData.addRow(fileName, fileDownloadURL, taskNumber);
                    }
                }
            }
        }
        return taskAttachmentData;
    }

    private LinkedList<HashMap<String, CustomisedITextTable>> getProjectTaskForKendah(List<EdsTask> tasks, Map<Integer, EdsEmployee> projectEmployees, EdsUser user, Date fromDate, Date toDate) {
        HashMap<String, CustomisedITextTable> listTaskDetails = new HashMap<>();
        LinkedList<HashMap<String, CustomisedITextTable>> projectTasks = new LinkedList<>();
        CustomisedITextTable taskTable = new CustomisedITextTable();
        taskTable.addColumn("TASK_NUMBER", commonLocalizer.localize(PdfLocalizationName.taskNo));
        taskTable.addColumn("ASSIGNES", commonLocalizer.localize(PdfLocalizationName.assignees));
        taskTable.addColumn("START_DATE", commonLocalizer.localize(PdfLocalizationName.startDateField));
        taskTable.addColumn("DUE_DATE", commonLocalizer.localize(PdfLocalizationName.dueDate));
        taskTable.addColumn("START_TIME_CUSTOM_FIELD", commonLocalizer.localize(PdfLocalizationName.actualTimeSpent));
        taskTable.addColumn("END_TIME_CUSTOM_FIELD", commonLocalizer.localize(PdfLocalizationName.actualTimeSpent));

        if (tasks != null && tasks.size() > 0 && fromDate != null && toDate != null) {
            tasks.sort((o1, o2) -> ((Comparable) o1.getStartDate().getTime()).compareTo(o2.getStartDate().getTime()));
            for (EdsTask t : tasks) {
                if ((user.getUserDate(t.getStartDate()).equals(fromDate) || user.getUserDate(t.getStartDate()).after(fromDate)) && (user.getUserDate(t.getStartDate()).equals(toDate) || user.getUserDate(t.getStartDate()).before(toDate))) {
                    String taskNumber = getResultOrLongDash(t.getNumber());
                    StringBuilder taskAssignees = new StringBuilder();
                    if ((t.getAssignments() != null) && t.getAssignments().size() > 0) {
                        Map<Integer, EdsEmployee> addedEmployees = new HashMap<>();
                        String delimitr = "";
                        for (EdsEmployeeTask emplTask : t.getAssignments()) {
                            if (emplTask.getProjectEmployee() != null && emplTask.getProjectEmployee().getEmployeeDepartment() != null && emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee() != null && !addedEmployees.containsKey(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID()) && projectEmployees.containsKey(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                                addedEmployees.put(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee());
                                taskAssignees.append(delimitr);
                                taskAssignees.append(emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName() != null ? emplTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName() : "N/A");
                                delimitr = ", ";
                            }
                        }
                    }

                    String taskStartDate = t.getStartDate() != null ? ServerUtils.getDateAsString(user.getUserDate(t.getStartDate())) : "—";
                    String taskDueDate = t.getDueDate() != null ? ServerUtils.getDateAsString(user.getUserDate(t.getDueDate())) : "—";

                    String startTime = "";
                    String endTime = "";

                    List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(t.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task));
                    if (customFieldItems != null && !customFieldItems.isEmpty()) {
                        for (CompanyCustomFieldItem item : customFieldItems) {
                            if ("Start Time".equals(item.getFieldName())) {
                                if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                                    startTime = item.getFieldStringValue();
                                }
                            } else if ("End Time".equals(item.getFieldName())) {
                                if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                                    endTime = item.getFieldStringValue();
                                }
                            }
                        }
                    }
                    taskTable.addRow(taskNumber, escapeHtml(taskAssignees.toString()), taskStartDate, taskDueDate, startTime, endTime);
                }
            }
            listTaskDetails.put(commonLocalizer.localize(PdfLocalizationName.tasks), taskTable);
            projectTasks.add(listTaskDetails);
        }

        return projectTasks;
    }

    private CustomisedITextTable getTaskCustomFieldData(List<EdsTask> tasks, EdsUser user, Date fromDate, Date toDate) {
        if (tasks == null || tasks.size() == 0) return null;
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);

        List<String> visitTypeList = new LinkedList<>();
        List<String> pestNotedList = new LinkedList<>();
        List<String> recommendationList = new LinkedList<>();
        if (fromDate != null && toDate != null) {
            for (EdsTask task : tasks) {
                if ((user.getUserDate(task.getStartDate()).equals(fromDate) || user.getUserDate(task.getStartDate()).after(fromDate)) && (user.getUserDate(task.getStartDate()).equals(toDate) || user.getUserDate(task.getStartDate()).before(toDate))) {
                    List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task));

                    if (customFieldItems != null && !customFieldItems.isEmpty()) {
                        for (CompanyCustomFieldItem item : customFieldItems) {
                            if (CompanyCustomFieldItem.TEXT.equals(item.getDataType())) {
                                if ("Visit Type/Treatment Provided".equals(item.getFieldName())) {
                                    if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                                        String[] valueArrays = item.getFieldStringValue().split(",");
                                        visitTypeList.addAll(Arrays.asList(valueArrays));
                                    }
                                } else if ("Pest Noted".equals(item.getFieldName())) {
                                    if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                                        String[] valueArrays = item.getFieldStringValue().split(",");
                                        pestNotedList.addAll(Arrays.asList(valueArrays));
                                    }
                                } else if ("Recommendations to Client".equals(item.getFieldName())) {
                                    if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                                        String[] valueArrays = item.getFieldStringValue().split(",");
                                        recommendationList.addAll(Arrays.asList(valueArrays));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Set<String> uniqueSetForVisitType = new HashSet<>(visitTypeList);
        for (String temp : uniqueSetForVisitType) {
            customFieldTable.addRowWithCode(temp, temp, Collections.frequency(visitTypeList, temp) + "");
        }

        Set<String> uniqueSetForPestNoted = new HashSet<>(pestNotedList);
        for (String temp : uniqueSetForPestNoted) {
            customFieldTable.addRowWithCode(temp, temp, Collections.frequency(pestNotedList, temp) + "");
        }

        Set<String> uniqueSetForRecommendation = new HashSet<>(recommendationList);
        for (String temp : uniqueSetForRecommendation) {
            customFieldTable.addRowWithCode(temp, temp, Collections.frequency(recommendationList, temp) + "");
        }
        return customFieldTable;
    }

    public CustomisedITextTable getTaskCustomFieldProductData(List<EdsTask> tasks, EdsUser user, Date fromDate, Date toDate) {
        CustomisedITextTable productsConsumptionTable = new CustomisedITextTable();
        productsConsumptionTable.addColumn("PRDUCT_NAME1", "");
        productsConsumptionTable.addColumn("PRDUCT_NAME2", "");
        productsConsumptionTable.addColumn("PRDUCT_NAME3", "");
        productsConsumptionTable.addColumn("PRDUCT_NAME4", "");
        productsConsumptionTable.addColumn("DOSAGE1", "");
        productsConsumptionTable.addColumn("DOSAGE2", "");
        productsConsumptionTable.addColumn("DOSAGE3", "");
        productsConsumptionTable.addColumn("DOSAGE4", "");
        productsConsumptionTable.addColumn("TARGET_PEST1", "");
        productsConsumptionTable.addColumn("TARGET_PEST2", "");
        productsConsumptionTable.addColumn("TARGET_PEST3", "");
        productsConsumptionTable.addColumn("TARGET_PEST4", "");
        productsConsumptionTable.addColumn("TOTAL1", "");
        productsConsumptionTable.addColumn("TOTAL2", "");
        productsConsumptionTable.addColumn("TOTAL3", "");
        productsConsumptionTable.addColumn("TOTAL4", "");
        productsConsumptionTable.addColumn("U_M1", "");
        productsConsumptionTable.addColumn("U_M2", "");
        productsConsumptionTable.addColumn("U_M3", "");
        productsConsumptionTable.addColumn("U_M4", "");

        if (fromDate != null && toDate != null) {
            for (EdsTask task : tasks) {
                if ((user.getUserDate(task.getStartDate()).equals(fromDate) || user.getUserDate(task.getStartDate()).after(fromDate)) && (user.getUserDate(task.getStartDate()).equals(toDate) || user.getUserDate(task.getStartDate()).before(toDate))) {
                    List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task));
                    String productName1 = "";
                    String productName2 = "";
                    String productName3 = "";
                    String productName4 = "";
                    String dosage1 = "";
                    String dosage2 = "";
                    String dosage3 = "";
                    String dosage4 = "";
                    String targetPest1 = "";
                    String targetPest2 = "";
                    String targetPest3 = "";
                    String targetPest4 = "";
                    String total1 = "";
                    String total2 = "";
                    String total3 = "";
                    String total4 = "";
                    String unitMeasurement1 = "";
                    String unitMeasurement2 = "";
                    String unitMeasurement3 = "";
                    String unitMeasurement4 = "";

                    if (customFieldItems != null && !customFieldItems.isEmpty()) {
                        for (CompanyCustomFieldItem item : customFieldItems) {
                            if (CompanyCustomFieldItem.TEXT.equals(item.getDataType()) || CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                                switch (item.getFieldName()) {
                                    case "Products/Equipments Used (1)" ->
                                            productName1 = escapeHtml(item.getFieldStringValue());
                                    case "Products/Equipments Used (2)" ->
                                            productName2 = escapeHtml(item.getFieldStringValue());
                                    case "Products/Equipments Used (3)" ->
                                            productName3 = escapeHtml(item.getFieldStringValue());
                                    case "Products/Equipments Used (4)" ->
                                            productName4 = escapeHtml(item.getFieldStringValue());
                                    case "Dosage (1)" -> dosage1 = escapeHtml(item.getFieldStringValue());
                                    case "Dosage (2)" -> dosage2 = escapeHtml(item.getFieldStringValue());
                                    case "Dosage (3)" -> dosage3 = escapeHtml(item.getFieldStringValue());
                                    case "Dosage (4)" -> dosage4 = escapeHtml(item.getFieldStringValue());
                                    case "Target Pest (1)" -> targetPest1 = escapeHtml(item.getFieldStringValue());
                                    case "Target Pest (2)" -> targetPest2 = escapeHtml(item.getFieldStringValue());
                                    case "Target Pest (3)" -> targetPest3 = escapeHtml(item.getFieldStringValue());
                                    case "Target Pest (4)" -> targetPest4 = escapeHtml(item.getFieldStringValue());
                                    case "Total Usage (1)" -> total1 = escapeHtml(item.getFieldStringValue());
                                    case "Total Usage (2)" -> total2 = escapeHtml(item.getFieldStringValue());
                                    case "Total Usage (3)" -> total3 = escapeHtml(item.getFieldStringValue());
                                    case "Total Usage (4)" -> total4 = escapeHtml(item.getFieldStringValue());
                                    case "u/m (1)" -> unitMeasurement1 = escapeHtml(item.getFieldStringValue());
                                    case "u/m (2)" -> unitMeasurement2 = escapeHtml(item.getFieldStringValue());
                                    case "u/m (3)" -> unitMeasurement3 = escapeHtml(item.getFieldStringValue());
                                    case "u/m (4)" -> unitMeasurement4 = escapeHtml(item.getFieldStringValue());
                                }
                            }
                        }
                    }
                    productsConsumptionTable.addRow(productName1, productName2, productName3, productName4, dosage1, dosage2, dosage3, dosage4,
                            targetPest1, targetPest2, targetPest3, targetPest4, total1, total2, total3, total4, unitMeasurement1, unitMeasurement2, unitMeasurement3, unitMeasurement4);
                }
            }
        }

        Map<String, ArrayList<SelectItem>> itemMap = new LinkedHashMap<>();
        for (HashMap<String, String> hashMap : productsConsumptionTable.getRows().values()) {
            if (itemMap.containsKey(hashMap.get("PRDUCT_NAME1"))) {
                itemMap.get(hashMap.get("PRDUCT_NAME1")).add(new SelectItem(null, hashMap.get("TARGET_PEST1"), hashMap.get("TOTAL1"), hashMap.get("U_M1")));
            } else {
                itemMap.put(hashMap.get("PRDUCT_NAME1"), new ArrayList<>(Collections.singletonList(new SelectItem(null, hashMap.get("TARGET_PEST1"), hashMap.get("TOTAL1"), hashMap.get("U_M1")))));
            }
            if (itemMap.containsKey(hashMap.get("PRDUCT_NAME2"))) {
                itemMap.get(hashMap.get("PRDUCT_NAME2")).add(new SelectItem(null, hashMap.get("TARGET_PEST2"), hashMap.get("TOTAL2"), hashMap.get("U_M2")));
            } else {
                itemMap.put(hashMap.get("PRDUCT_NAME2"), new ArrayList<>(Collections.singletonList(new SelectItem(null, hashMap.get("TARGET_PEST2"), hashMap.get("TOTAL2"), hashMap.get("U_M2")))));
            }
            if (itemMap.containsKey(hashMap.get("PRDUCT_NAME3"))) {
                itemMap.get(hashMap.get("PRDUCT_NAME3")).add(new SelectItem(null, hashMap.get("TARGET_PEST3"), hashMap.get("TOTAL3"), hashMap.get("U_M3")));
            } else {
                itemMap.put(hashMap.get("PRDUCT_NAME3"), new ArrayList<>(Collections.singletonList(new SelectItem(null, hashMap.get("TARGET_PEST3"), hashMap.get("TOTAL3"), hashMap.get("U_M3")))));
            }
            if (itemMap.containsKey(hashMap.get("PRDUCT_NAME4"))) {
                itemMap.get(hashMap.get("PRDUCT_NAME4")).add(new SelectItem(null, hashMap.get("TARGET_PEST4"), hashMap.get("TOTAL4"), hashMap.get("U_M4")));
            } else {
                itemMap.put(hashMap.get("PRDUCT_NAME4"), new ArrayList<>(Collections.singletonList(new SelectItem(null, hashMap.get("TARGET_PEST4"), hashMap.get("TOTAL4"), hashMap.get("U_M4")))));
            }
        }

        CustomisedITextTable categoriesGroup = new CustomisedITextTable();
        categoriesGroup.addColumnOrder(CATEGORY, "TARGET", TOTAL, ITEM_UNIT_MEASUREMENT);

        for (Map.Entry<String, ArrayList<SelectItem>> entry : itemMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey())) {
                String productName = entry.getKey();
                BigDecimal totalSum = BigDecimal.ZERO;
                Map<String, String> targerPestMap = new HashMap();
                StringBuilder targetPest = new StringBuilder();
                StringBuilder unitMeasurement = new StringBuilder();

                for (SelectItem valueItem : entry.getValue()) {
                    if (!ServerUtils.isNullOrEmpty(valueItem.getName())) {
                        targerPestMap.put(valueItem.getName(), "");
                    }
                    if (!ServerUtils.isNullOrEmpty(valueItem.getDescription())) {
                        int length = valueItem.getDescription().length();
                        StringBuilder mergeDigit = new StringBuilder();
                        for (int i = 0; i < length; i++) {
                            if (Character.isDigit(valueItem.getDescription().charAt(i))) {
                                String tempDigit = valueItem.getDescription().substring(i, i + 1);
                                mergeDigit.append(tempDigit);
                            }
                        }
                        if (!ServerUtils.isNullOrEmpty(mergeDigit.toString())) {
                            totalSum = totalSum.add(new BigDecimal(mergeDigit.toString()));
                        }
                    }
                    if (!ServerUtils.isNullOrEmpty(valueItem.getCategory())) {
                        unitMeasurement.append(valueItem.getCategory()).append(", ");
                    }
                }
                for (Map.Entry m : targerPestMap.entrySet()) {
                    targetPest.append(m.getKey()).append(", ");
                }

                categoriesGroup.addRow(productName, targetPest.toString().replaceAll(", $", ""), numberFormat.format(totalSum), unitMeasurement.toString().replaceAll(", $", ""));
            }
        }

        return categoriesGroup;
    }

    private String getFileSizeAsString(Long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    private String getDownloadURL(FileResource fileResource) {
        String url = "";
        if (Constants.GOOGLE.equals(fileResource.getUploadType())) {
            url = fileResource.getGoogleDownloadLink();
        } else if (Constants.OFFICE_365.equals(fileResource.getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            url = fileResource.getOfficeDownloadLink();
        } else {
            url = fileResource.getAmazonLink();
        }
        return StringUtils.isNotEmpty(url) ? url : "";
    }

    public CustomisedITextTable getCurrentData(EdsUser user) {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        customTable.addRowWithCode(CURRENT_DATE, "", shortDateFormat.format(ServerUtils.convertServerDateToUserDate(new Date(), user.getUserTimezone())));
        customTable.addRowWithCode("CURRENT_YEAR", "", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRowWithCode("CURRENT_TIME", "", timeFormat.format(userManager.getUser().getUserDate()));
        return customTable;
    }

    private CustomisedITextTable getRelationContactCustomField(EdsCrmContact relationContact, EdsUser user) {
        CustomisedITextTable relContactCusFieldTable = new CustomisedITextTable();
        relContactCusFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        EdsCrmCustomFields edsCrmCustomFields = relationContact != null && relationContact.getCustomFields() != null ? relationContact.getCustomFields() : null;
        List<CompanyCustomFieldItem> customFieldItemsContact = CustomFieldsUtils.setRPCCustomFieldItems(edsCrmCustomFields,
                commonService.getCompanyCustomFields(ViewName.Contact));
        if (customFieldItemsContact != null && customFieldItemsContact.size() > 0) {
            for (CompanyCustomFieldItem item : customFieldItemsContact) {
                switch (item.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (item.getFieldDateNonConvertedValue() != null) {
                            dateValue = shortDateFormat.format(ServerUtils.convertServerDateToUserDate(item.getFieldDateNonConvertedValue().getNonConvertedDate(), user.getUserTimezone()));
                        }
                        relContactCusFieldTable.addRowWithCode(item.getFieldName(), item.getFieldName(), escapeHtml(dateValue));
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(item.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(item.getFieldStringValue()));
                        }
                        relContactCusFieldTable.addRowWithCode(item.getFieldName(), item.getFieldName(), escapeHtml(numberValue));
                    }
                    default ->
                            relContactCusFieldTable.addRowWithCode(item.getFieldName(), item.getFieldName(), escapeHtml(item.getFieldStringValue()));
                }
            }
        }
        return relContactCusFieldTable;
    }

    private String getSize(Long size, double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer projectId = requestObject.getObjectID();
        EdsProject project = projectManager.get(projectId);
        setFileName((project.getName().length() > 24 ? project.getName().substring(0, 24) : project.getName()) + "_" + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PROJECT_SUMMARY;
    }

    @Override
    protected String getTableName(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsProject project = projectManager.get(requestObject.getObjectID());
        String projectNumber = project != null ? escapeHtml(project.getNumber()) : "";
        String projectName = project != null ? escapeHtml(project.getName()) : "";
        return projectNumber.concat(" - ").concat(projectName);
    }
}
