package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.lucene.parser.HTMLParser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.task.client.rpc.TaskComment;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE;

public class TaskViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, IPostPDFHandler {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProjectManager projectManager;

    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;

    private Boolean isFromC8032 = false;
    private final DecimalFormat numberFormat = new DecimalFormat("###.##");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private String taskNumber;
    private String taskName;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
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
            String imageUrl = getPdfLogoUrl(company, hasPhantom);
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        companyData.setCompanyEmail((company.getEmail() != null && company.getEmail().length() > 1 ? (company.getEmail()) : ""));
        companyData.setCompanyFax((company.getFaxNumber() != null && company.getFaxNumber().length() > 1 ? (company.getFaxNumber()) : ""));
        companyData.setCompanyPhone((company.getPhone() != null && company.getPhone().length() > 1 ? (company.getPhone()) : ""));

        RequestObject requestObject = (RequestObject) dataClass;
        Integer taskId = requestObject.getObjectID();

        List<EdsTask> tasks;
        if (isFromC8032) {
            EdsProject project = projectManager.get(taskId);
            tasks = taskManager.getProjectTasks(project.getObjectID(), 0, /*200*/1000);
        } else {
            tasks = new ArrayList<>();
            tasks.add(taskManager.get(taskId));
        }

        TaskSingleItem taskSingleItem = taskService.getTask(taskId, false);

        ITextSummaryView[] pdfDataArray = new ITextSummaryView[tasks.size()];
        pdf.setSummaryViewArray(pdfDataArray);

        //custom pdf for MONTEX ELEKTRONIKA company
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        int summaryID = 0;
        for (EdsTask task : tasks) {
            ITextSummaryView pdfData = new ITextSummaryView();
            ITextTableList firstTable = new ITextTableList(4);
            pdfData.addTable(firstTable);
            ITextTableList secondTable = new ITextTableList(4);
            pdfData.addTable(secondTable);
            ITextTableList noteTable = new ITextTableList(4);
            pdfData.addTable(noteTable);

            pdfDataArray[summaryID++] = pdfData;

            CustomisedITextTable customTable = new CustomisedITextTable();
            Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.Task));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                            cols.put(COLUMN_VALUE, (item.getFieldDateNonConvertedValue() != null && item.getFieldDateNonConvertedValue().getNonConvertedDate() != null) ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else if (CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue()) ? escapeHtml(numberFormat.format(Double.valueOf(item.getFieldStringValue()))) : "");
                        } else {
                            cols.put(COLUMN_VALUE, escapeHtml(item.getFieldStringValue()));
                            SelectItem entityType = item.getEntityType();
                            if (entityType != null && entityType.getReferenceCode() != null) {
                                String code = entityType.getReferenceCode();
                                switch (code) {
                                    case EMPLOYEE:
                                }
                            }
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(item.getFieldName(), cols);
                        }
                    }
                }
                customFields.put("TASK", itemCusFields);
                customTable.setCustomFields(customFields);
            }

            customData.put("CUSTOM_FIELD", customTable);

            /*project custom field*/
            CustomisedITextTable projectCustomTable = new CustomisedITextTable();
            Map<String, LinkedHashMap<String, Map<String, String>>> projectCustomFields = new HashMap<>();
            EdsProjectCustomFields edsProjectCustomFields = task.getProject() != null &&
                    task.getProject().getProjectCustomFields() != null ?
                    task.getProject().getProjectCustomFields() :
                    null;
            List<CompanyCustomFieldItem> customFieldItemsProject = CustomFieldsUtils.setRPCCustomFieldItems(edsProjectCustomFields,
                    commonService.getCompanyCustomFields(ViewName.Project));

            if (customFieldItemsProject != null && customFieldItemsProject.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItemsProject) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                            cols.put(COLUMN_VALUE, (item.getFieldDateNonConvertedValue() != null && item.getFieldDateNonConvertedValue().getNonConvertedDate() != null) ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else if (CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue()) ? escapeHtml(numberFormat.format(Double.valueOf(item.getFieldStringValue()))) : "");
                        } else {
                            cols.put(COLUMN_VALUE, escapeHtml(item.getFieldStringValue()));
                            SelectItem entityType = item.getEntityType();
                            if (entityType != null && entityType.getReferenceCode() != null) {
                                String code = entityType.getReferenceCode();
                                switch (code) {
                                    case EMPLOYEE:
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

            EdsProject project = task.getProject();
            String pNumber = (project != null && project.getNumber() != null) ? project.getNumber() : "";
            firstTable.addPdfTableRows(pNumber, escapeHtml(project.getName()), escapeHtml(task.getName()), " "/*section*/);

            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
            /*SimpleDateFormat longDateFormat = getCompanyShortDateFormat(company);*/
            String dueDate = task.getDueDate() != null ? shortDateFormat.format(task.getDueDate()) : " ";
            String description = "";
            try {
                description = escapeHtml(task.getDescription() != null ? htmlParser.performParse(task.getDescription()) : " ");
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
            StringBuilder assignees = new StringBuilder();
            StringBuilder assignees2 = new StringBuilder();
            List<EdsProjectEmployee> taskAssignees_ = taskManager.getTaskAssignees(task.getObjectID());
            if (taskAssignees_ != null && taskAssignees_.size() > 0) {
                String delimitr = "";
                String pref = "";
                for (EdsProjectEmployee member : taskAssignees_) {
                    EdsEmployee emp = member.getEmployeeDepartment() != null ? member.getEmployeeDepartment().getEmployee() : new EdsEmployee();
                    assignees.append(delimitr).append(emp.getFullName());
                    assignees2.append(pref).append(emp.getFullName());
                    delimitr = "<br/> ";
                    pref = ", ";
                }
            }
            secondTable.addPdfTableRows(assignees.toString(), dueDate, escapeHtml(description).replace("\n", "<br/>"), " ");

            HistoryListItem[] notes = taskService.getTaskNotes(taskId);
            if (notes != null) {
                for (HistoryListItem note : notes) {
                    noteTable.addPdfTableRows(note.getEmployee(), (note.getEventDate() != null ? shortDateFormat.format(note.getEventDate()) : ""),
                            escapeHtml(note.getSubject()), escapeHtml(note.getComment()));
                }
            }

            StringBuilder bManagers = new StringBuilder();
            String prefix = "";
            if (project != null) {
                for (EdsEmployee backupManager : project.getBackupManagers()) {
                    bManagers.append(prefix);
                    bManagers.append(backupManager.getFullName());
                    prefix = ", ";
                }
            }

            //task number table
            this.taskNumber = task.getNumber() == null ? "" : task.getNumber();
            CustomisedITextTable taskNumberTable = new CustomisedITextTable();
            taskNumberTable.addColumnOrder("NAME");
            taskNumberTable.addRowWithCode("TASK_NUMBER", this.taskNumber);
            customData.put("TASK_NUMBER_TABLE", taskNumberTable);

            //task content table
            CustomisedITextTable taskContentTable = new CustomisedITextTable();
            String taskProjectName = "";
            String projectManager = "";
            String backupManagers = "";
            String taskProjectClientName = "";
            String clientContact = "";
            if (project != null) {
                taskProjectName = escapeHtml(project.getName());
                try {
                    projectManager = project.getManager() != null ? escapeHtml(project.getManager().getFullName()) : "";
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }

            backupManagers = bManagers != null ? escapeHtml(bManagers.toString()) : "";

            if (project != null && project.getClient() != null) {
                taskProjectClientName = escapeHtml(project.getClient().getName());
                clientContact = project.getClient().getPrimaryContact() != null
                        ? escapeHtml(project.getClient().getPrimaryContact().getName())
                        : "";
            }

            String billingAddressName = "";
            String clientAddress = "";
            String clientAddress2 = "";
            String clientState = "";
            String clientCity = "";
            String clientCountry = "";
            String clientZipCode = "";
            if (project != null && project.getClient() != null && project.getClient().getBillingAddress() != null) {
                EdsAddress billingAddress = project.getClient().getBillingAddress();
                billingAddressName = escapeHtml(billingAddress.getName());
                clientAddress = escapeHtml(billingAddress.getAddress());
                clientAddress2 = escapeHtml(billingAddress.getAddressb());
                clientState = escapeHtml(billingAddress.getStateName());
                clientCity = escapeHtml(billingAddress.getCity());
                clientCountry = escapeHtml(billingAddress.getCountryName());
                clientZipCode = escapeHtml(billingAddress.getZipCode());
            }

            String mailingAddressName = "";
            String clientMailAddress = "";
            String clientMailAddress2 = "";
            String clientMailState = "";
            String clientMailCity = "";
            String clientMailCountry = "";
            String clientMailZipCode = "";
            if (project != null && project.getClient() != null && project.getClient().getMailingAddress() != null) {
                EdsAddress mailingAddress = project.getClient().getMailingAddress();
                mailingAddressName = escapeHtml(mailingAddress.getName());
                clientMailAddress = escapeHtml(mailingAddress.getAddress());
                clientMailAddress2 = escapeHtml(mailingAddress.getAddressb());
                clientMailState = escapeHtml(mailingAddress.getStateName());
                clientMailCity = escapeHtml(mailingAddress.getCity());
                clientMailCountry = escapeHtml(mailingAddress.getCountryName());
                clientMailZipCode = escapeHtml(mailingAddress.getZipCode());
            }

            this.taskName = escapeHtml(task.getName() != null ? task.getName() : "");//task name
            String taskDescription = escapeHtml(task.getDescription() != null ? task.getDescription() : "");//task description
            String taskCreatedDate = task.getCreationTime() == null ? "" : dateFormat(task.getCreationTime());//task creation date
            taskCreatedDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(taskCreatedDate) : taskCreatedDate;
            String taskStartDate = task.isAllDay() != null && task.isAllDay() ? dateFormat(task.getStartDate()) : longDateFormat(task.getStartDate());//task start date
            taskStartDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(taskStartDate) : taskStartDate;
            String taskDueDate = task.isAllDay() != null && task.isAllDay() ? dateFormat(task.getDueDate()) : longDateFormat(task.getDueDate());//task due date
            taskDueDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(taskDueDate) : taskDueDate;
            String taskPriority = task.getPriority() == null ? "" : referenceWfmMessageSource.localizeRef(task.getPriority());//task priority
            String taskStatus = task.getStatus() == null ? "" : referenceWfmMessageSource.localizeRef(task.getStatus());//task status
            String estimatedTime = task.getEstimatedTime() != null ? ServerUtils.getTimeSpentHM(task.getEstimatedTime()) : "00:00";
            String timeSpent = task.getTimespent() != null ? ServerUtils.getTimeSpentHM(task.getTimespent()) : "00:00";
            String taskBillable = task.getBillable() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no);

            taskContentTable.addColumnOrder("NAME");
            taskContentTable.addRowWithCode("TASK_NUMBER", this.taskNumber);
            taskContentTable.addRowWithCode("PROJECT_NAME", taskProjectName);
            taskContentTable.addRowWithCode("PROJECT_MANAGER", projectManager);
            taskContentTable.addRowWithCode("BACKUP_MANAGERs", backupManagers);
            taskContentTable.addRowWithCode("TASK_ASSIGNEES", assignees.toString());
            taskContentTable.addRowWithCode("TASK_ASSIGNEES2", assignees2.toString());
            taskContentTable.addRowWithCode("CLIENT_NAME", taskProjectClientName);
            taskContentTable.addRowWithCode("TASK_NAME", this.taskName);
            taskContentTable.addRowWithCode("BILLABLE", taskBillable);
            taskContentTable.addRowWithCode("TASK_DESCRIPTION", taskDescription.replace("\r\n", "<br/>").replace("\n", "<br/>"));
            //for special descriptions
            taskContentTable.addRowWithCode("TASK_DESCRIPTION1", "<p>" + taskDescription.replace("\r\n", "<br/>").replace("\n", "<br/></p><p style='text-indent:1em'>") + "</p>");

            taskContentTable.addRowWithCode("TASK_CREATED_DATE", taskCreatedDate);
            taskContentTable.addRowWithCode("TASK_START_DATE", taskStartDate);
            taskContentTable.addRowWithCode("TASK_DUE_DATE", taskDueDate);
            taskContentTable.addRowWithCode("TASK_PRIORITY", taskPriority);
            taskContentTable.addRowWithCode("TASK_STATUS", taskStatus);
            taskContentTable.addRowWithCode("ESTIMATED_TIME", estimatedTime);
            taskContentTable.addRowWithCode("TIME_SPENT", timeSpent);
            taskContentTable.addRowWithCode("PROJECT_NUMBER", pNumber);
            taskContentTable.addRowWithCode(BILL_ADDRESS_NAME, billingAddressName);
            taskContentTable.addRowWithCode(BILL_ADDRESS, clientAddress);
            taskContentTable.addRowWithCode(BILL_ADDRESS2, clientAddress2);
            taskContentTable.addRowWithCode(BILL_STATE, clientState);
            taskContentTable.addRowWithCode(BILL_CITY, clientCity);
            taskContentTable.addRowWithCode(BILL_COUNTRY, clientCountry);
            taskContentTable.addRowWithCode(BILL_ZIPCODE, clientZipCode);
            taskContentTable.addRowWithCode(CLIENT_CONTACT, clientContact);
            taskContentTable.addRowWithCode(MAIL_ADDRESS_NAME, mailingAddressName);
            taskContentTable.addRowWithCode(MAIL_ADDRESS, clientMailAddress);
            taskContentTable.addRowWithCode(MAIL_ADDRESS2, clientMailAddress2);
            taskContentTable.addRowWithCode(MAIL_STATE, clientMailState);
            taskContentTable.addRowWithCode(MAIL_CITY, clientMailCity);
            taskContentTable.addRowWithCode(MAIL_COUNTRY, clientMailCountry);
            taskContentTable.addRowWithCode(MAIL_ZIPCODE, clientMailZipCode);
            customData.put("TASK_CONTENT_TABLE", taskContentTable);

            //task time entry members table
            CustomisedITextTable taskTimeEntryMembersTable = new CustomisedITextTable();
            taskTimeEntryMembersTable.addColumnOrder("EMPLOYEE_NAME", "EMPLOYEE_TASK_COMMENT",
                                                     "EMPLOYEE_TASK_TIME_ENTRY_DATE", "EMPLOYEE_TIME_SPENT",
                                                     "EMPLOYEE_TASK_NAME", STATUS);
            TaskTimeEntriesItem[] taskTimeEntries = taskService.getTaskTimeEntries(task.getObjectID());

            if (taskTimeEntries != null && taskTimeEntries.length > 0) {
                for (TaskTimeEntriesItem member : taskTimeEntries) {
                    String employeeName = escapeHtml(member.getEmloyee());//employee name
                    String employeeTaskComment = escapeHtml(member.getComment());//employee task comment
                    String employeeTaskTimeEntryDate = dateFormat(member.getDate().getNonConvertedDate());//employee task time entry date
                    String employeeTimSpent = member.getTimeSpent() != null ? ServerUtils.timeSpentToString(member.getTimeSpent()) : "";//employee time spent
                    String employeeTaskName = escapeHtml(member.getTaskName());
                    String status = escapeHtml(member.getStatus());
                    taskTimeEntryMembersTable.addRow(employeeName, employeeTaskComment, employeeTaskTimeEntryDate,
                                                     employeeTimSpent, employeeTaskName, status);
                }
            }
            customData.put("TASK_TIME_ENTRY_MEMBERS_TABLE", taskTimeEntryMembersTable);


            //task assignees
            CustomisedITextTable taskAssigneesTable = new CustomisedITextTable();
            taskAssigneesTable.addColumnOrder("EMPLOYEE_CODE", "EMPLOYEE_NAME", "ASSIGNEE_STATUS", "TIME_SPENT");
            PositionsSelectItem[] taskAssignees = taskSingleItem.getIssueEmployees();

            if (taskAssignees != null && taskAssignees.length > 0) {
                for (PositionsSelectItem item : taskAssignees) {
                    EdsReference assigneeStatus = referenceManager.getByName(item.getStatusName());
                    taskAssigneesTable.addRow(
                            escapeHtml(item.getEmployeeNumber()),
                            escapeHtml(item.getName()),
                            escapeHtml(referenceWfmMessageSource.localizeRef(assigneeStatus)),
                            item.getTimeSpent() != null ? ServerUtils.timeSpentToString(item.getTimeSpent()) : ""
                    );
                }
            }
            customData.put("TASK_ASSIGNEES", taskAssigneesTable);

            //notes table
            CustomisedITextTable notesTable = new CustomisedITextTable();
            notesTable.addColumnOrder("NOTE_SUBJECT", "NOTE_PUBLISHED_BY", "NOTE_DATE");
            if (notes != null && notes.length > 0) {
                for (HistoryListItem note : notes) {
                    String noteComment = "";//note comment
                    try {
                        noteComment = note.getComment() != null ? htmlParser.performParse(note.getComment()) : "";//note comment
                    } catch (SAXException | IOException e) {
                        noteComment = "";
                    }

                    String notePublishedBy = escapeHtml(note.getEmployee());//note published by
                    String noteDate = note.getEventDate() != null ? longDateFormat(note.getEventDate()) : "";//note date

                    notesTable.addRow(escapeHtml(noteComment), notePublishedBy, noteDate);
                }
            }
            customData.put("NOTES_TABLE", notesTable);

            //attachments table
            CustomisedITextTable attachmentsTable = new CustomisedITextTable();
            attachmentsTable.addColumnOrder("FILE_NAME", "FILE_DOWNLOAD_URL");
            FileResource[] attachmentsLists = taskService.getTaskAttachments(task.getObjectID());

            if (attachmentsLists != null && attachmentsLists.length > 0) {
                for (FileResource attachment : attachmentsLists) {
                    String fileName = escapeHtml(attachment.getEncodedName());
                    String fileDownloadURL = escapeHtml(getDownloadURL(attachment));
                    attachmentsTable.addRow(fileName, fileDownloadURL);
                }
            }
            customData.put("ATTACHMENTS_TABLE", attachmentsTable);
        }

        CustomisedITextTable currentTable = new CustomisedITextTable();
        currentTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        currentTable.addRow(CURRENT_DATE, dateFormat.format(currentDate));
        currentTable.addRow("CURRENT_YEAR", Calendar.getInstance().get(Calendar.YEAR) + "");
        currentTable.addRow("CURRENT_TIME", timeFormat.format(userManager.getUser().getUserDate()));

        customData.put("CURRENT", currentTable);

        pdf.setCustomData(customData);

        Map<String, String> localizeLabels = new LinkedHashMap<>();
        localizeLabels.put("PROJECT_LABEL", commonLocalizer.localize(PdfLocalizationName.project));
        localizeLabels.put("TASK_NUMBER_LABEL", commonLocalizer.localize(PdfLocalizationName.taskNo));
        localizeLabels.put("TASK_NAME_LABEL", commonLocalizer.localize(PdfLocalizationName.taskName));
        localizeLabels.put("START_DATE_LABEL", commonLocalizer.localize(PdfLocalizationName.startDate));
        localizeLabels.put("DUE_DATE_LABEL", commonLocalizer.localize(PdfLocalizationName.dueDate));
        localizeLabels.put("STATUS_LABEL", commonLocalizer.localize(PdfLocalizationName.status));
        localizeLabels.put("BILLABLE_LABEL", commonLocalizer.localize(PdfLocalizationName.billable));
        localizeLabels.put("ESTIMATED_TIME_LABEL", commonLocalizer.localize(PdfLocalizationName.estimatedTime));
        localizeLabels.put("PRIORITY_LABEL", commonLocalizer.localize(PdfLocalizationName.priority));
        localizeLabels.put("DESCRIPTION_LABEL", commonLocalizer.localize(PdfLocalizationName.description));
        localizeLabels.put("TASK_INFORMATION_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.taskInformation));
        localizeLabels.put("TASK_DESCRIPTION_LABEL", commonLocalizer.localize(PdfLocalizationName.taskDescription));
        localizeLabels.put("ASSIGNEES_LABEL", commonLocalizer.localize(PdfLocalizationName.assignees));
        localizeLabels.put("EMPLOYEE_CODE_LABEL", commonLocalizer.localize(PdfLocalizationName.employeeCode));
        localizeLabels.put("EMPLOYEE_NAME_LABEL", commonLocalizer.localize(PdfLocalizationName.employee));
        localizeLabels.put("ASSIGNEES_STATUS_LABEL", commonLocalizer.localize(PdfLocalizationName.assigneeStatus));
        localizeLabels.put("TIME_SPENT_LABEL", commonLocalizer.localize(PdfLocalizationName.elapsedTime));
        localizeLabels.put("ADDITIONAL_INFORMATION_LABEL", commonLocalizer.localize(PdfLocalizationName.additionalInformation));


        pdf.setLocalizeLabels(localizeLabels);

        return pdf;
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

    private String getSize(Long size, double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    private boolean hasAnyComment(TaskComment[] taskComments) {
        boolean hasComment = false;
        if (taskComments != null & taskComments.length > 0) {
            for (TaskComment tc : taskComments) {
                if (tc != null && tc.getText() != null && !tc.getText().equals("") && tc.getUser() != null) {
                    return true;
                }
            }
        }

        return hasComment;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    protected Object getDataClass(HttpServletRequest request) {
        isFromC8032 = request.getParameter("fromC8032") != null;
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer taskId = requestObject.getObjectID();
        if (isFromC8032) {
            EdsProject project = projectManager.get(taskId);
            setFileName((project.getName().length() > 24 ? project.getName().substring(0, 24) : project.getName()) + "_" + dateFormat(new Date()));
        } else {
            EdsTask task = taskManager.get(taskId);
            StringBuilder fileName = new StringBuilder();
            fileName.append(task.getNumber()).append("-").append((task.getName().length() > 50 ? task.getName().substring(0, 50) : task.getName()).replaceAll("[^a-zA-Z0-9\\._]+", ""));
            setFileName(fileName.toString());
        }
    }

    private boolean hasTimeEntries(TaskTimeEntriesItem[] timeEntriesItems) {
        boolean hasItem = false;
        if (timeEntriesItems != null && timeEntriesItems.length > 0) {
            for (TaskTimeEntriesItem tt : timeEntriesItems) {
                if (tt != null && tt.getEmloyee() != null) {
                    return true;
                }
            }
        }

        return hasItem;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.TASK;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (StringUtils.isNotBlank(this.taskNumber) && StringUtils.isNotBlank(this.taskName)) {
            return this.taskNumber.concat(" - ").concat(this.taskName);
        }
        return commonLocalizer.localize(PdfLocalizationName.task);
    }
}
