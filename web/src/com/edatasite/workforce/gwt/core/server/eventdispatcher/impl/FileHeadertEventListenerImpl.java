package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud  Babaev
 * Date: Sep 24, 2011
 * Time: 6:47:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class FileHeadertEventListenerImpl extends CustomBusinessEventListenerAdapter implements Constants {

    public static WfmType<EdsFileHeader> TYPE = new WfmType<>(EventTypes.fileHeaderEventListener);

    public static String EVENT_ADD = "ADD_PROJECT";
    public static String EVENT_DELETE = "DELETE_PROJECT";
    public static String ISSUE_FILE_ADD = "ISSUE_FILE_ADD";
    public static String PROJECT_FILE_ADD = "PROJECT_FILE_ADD";
    public static String TASK_FILE_ADD = "TASK_FILE_ADD";

    @Autowired
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserManager userManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (PROJECT_FILE_ADD.equals(event.getEventType()) || TASK_FILE_ADD.equals(event.getEventType()) ||
                ISSUE_FILE_ADD.equals(event.getEventType())) {
            onAddEvent(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        onAdd(event);
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsFileHeader fileHeader = fileHeaderManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());

        if (!event.isSendMail1()) {
            try {
                Integer companyID = creator.getCompany().getObjectID();

                FileResource fileResource = fileHeader.getDTO();
                String creatorName = fileResource.getCreatedBy();
                String creationDate = ServerUtils.shortDateFormat(fileResource.getCreationDate(), creator);
                String description = fileResource.getDescription();
                String fileName = fileHeader.getName();

                String subject = "Document Upload Notification";

                if (fileHeader.getFileType() == F_PROJECT) {//             Document upload To Project
                    Integer projectID = fileHeader.getFolder().getEntityId();
                    EdsProject project = projectManager.get(projectID);
                    if (project != null && !project.getDeleted()) {
                        List<EdsEmployee> projectEmployees = projectEmployeeManager.getEmployeesByProject(project.getObjectID());
                        for (EdsEmployee employee : projectEmployees) {
                            if (!employee.getObjectID().equals(creator.getObjectID())) {

                                String urlLINK = EncryptionHelper.encryptURL("project/" + project.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());

                                EmailTemplateItem templateItem = emailTemplateServiceLocal.getDocumentUploadTemplateItem(creator, employee, creatorName, fileName, description, project.getName(), creationDate, urlLINK, DOC_UPLOAD_TO_PROJECT_CATEGORY);
                                if (templateItem != null) {
                                    messageManager.registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyID);
                                } else {
                                    String to = employee.getEmail();
                                    Map<String, Object> values = new TreeMap<>();
                                    values.put("assigneeName", employee.getFullName());
                                    values.put("creatorName", creatorName);
                                    values.put("documentUploadToProjectName", project.getName());
                                    values.put("creationDate", creationDate);
                                    values.put("fileName", fileName);
                                    values.put("description", description);
                                    values.put("host", EdsContextParams.getHost(companyID));
                                    values.put("link", urlLINK);

                                    String contentText = EdsTemplates.processTemplate(employee, values, EdsTemplates.DOCUMENT_UPLOAD_TO_PROJECT);

                                    messageManager.sendDocumentsUploadNotification(to, subject, contentText, employee.getObjectID(), companyID, EmailNotificationConstants.DOC_UPLOAD_TO_PROJECT_NOTIFICATION);
                                }
                            }
                        }
                    }
                    event.setSendMail1(true);
                } else if (fileHeader.getFileType() == F_TASK) {//             Document upload To Task
                    Integer taskID = fileHeader.getEntityId();
                    EdsTask task = taskManager.get(taskID);
                    if (task != null && !task.getDeleted()) {
                        for (EdsEmployeeTask employeeTask : task.getUnDeletedAssignments()) {
                            EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                            if (employee != null && employee.getCompany().getActive() &&
                                    !employee.getDeleted() && !employeeTask.getDeleted() && !employee.getObjectID().equals(creator.getObjectID())) {

                                String urlLINK = EncryptionHelper.encryptURL("task/" + task.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());

                                EmailTemplateItem templateItem = emailTemplateServiceLocal.getDocumentUploadTemplateItem(creator, employee, creatorName, fileName, description, task.getName(), creationDate, urlLINK, DOC_UPLOAD_TO_TASK_CATEGORY);
                                if (templateItem != null) {
                                    messageManager.registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyID);
                                } else {
                                    String to = employee.getEmail();
                                    Map<String, Object> values = new TreeMap<>();
                                    values.put("assigneeName", employee.getFullName());
                                    values.put("creatorName", creatorName);
                                    values.put("documentUploadToTaskName", task.getName());
                                    values.put("creationDate", creationDate);
                                    values.put("fileName", fileName);
                                    values.put("description", description);
                                    values.put("documentUploadToProjectName", task.getProject().getName());
                                    values.put("host", EdsContextParams.getHost(companyID));
                                    values.put("link", urlLINK);

                                    String contentText = EdsTemplates.processTemplate(employee, values, EdsTemplates.DOCUMENT_UPLOAD_TO_TASK);

                                    messageManager.sendDocumentsUploadNotification(to, subject, contentText, employee.getObjectID(), companyID, EmailNotificationConstants.DOC_UPLOAD_TO_TASK_NOTIFICATION);
                                }
                            }
                        }
                    }
                    event.setSendMail1(true);
                } else if (fileHeader.getFileType() == F_PR_ISSUE) {//             Document upload To Issue
                    Integer issueID = fileHeader.getEntityId();
                    EdsIssue issue = issueManager.get(issueID);
                    if (issue != null && !issue.getDeleted()) {
                        for (EdsEmployeeTask employeeTask : issue.getUnDeletedAssignments()) {
                            EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                            if (employee != null && employee.getCompany().getActive() &&
                                    !employee.getDeleted() && !employeeTask.getDeleted() && !employee.getObjectID().equals(creator.getObjectID())) {

                                String urlLINK = EncryptionHelper.encryptURL("issue/" + issue.getObjectID()) + "&" + U_ID + "=" + EncryptionHelper.encryptURL(employee.getObjectID().toString()) + "&" + C_ID + "=" + EncryptionHelper.encryptURL(companyID.toString());

                                EmailTemplateItem templateItem = emailTemplateServiceLocal.getDocumentUploadTemplateItem(creator, employee, creatorName, fileName, description, issue.getName(), creationDate, urlLINK, DOC_UPLOAD_TO_ISSUE_CATEGORY);
                                if (templateItem != null) {
                                    messageManager.registerInternalMessageBasic(templateItem.getToEmail(), templateItem.getSubject(), templateItem.getMessageHTML(), companyID);
                                } else {
                                    String to = employee.getEmail();
                                    Map<String, Object> values = new TreeMap<>();
                                    values.put("assigneeName", employee.getFullName());
                                    values.put("creatorName", creatorName);
                                    values.put("documentUploadToIssueName", issue.getName());
                                    values.put("creationDate", creationDate);
                                    values.put("fileName", fileName);
                                    values.put("description", description);
                                    values.put("host", EdsContextParams.getHost(companyID));
                                    values.put("link", urlLINK);

                                    String contentText = EdsTemplates.processTemplate(employee, values, EdsTemplates.DOCUMENT_UPLOAD_TO_ISSUE);

                                    messageManager.sendDocumentsUploadNotification(to, subject, contentText, employee.getObjectID(), companyID, EmailNotificationConstants.DOC_UPLOAD_TO_ISSUE_NOTIFICATION);
                                }
                            }
                        }
                    }
                    event.setSendMail1(true);
                } else if (fileHeader.getFileType() == F_CASE) {//             Document upload F_CASE
                    event.setSendMail1(true);
                } else {
                    event.setSendMail1(true);
                }
            } catch (EdsDbException | EdsTemplateException e) {
                event.setSendMail1(false);
            }
        }

        if (!event.isMyUpdatesItemAdd()) {
            try {
                if (PROJECT_FILE_ADD.equals(event.getEventType())) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerFileUpload(fileHeader, creator, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                } else {
                    if (TASK_FILE_ADD.equals(event.getEventType())) {
                        EdsMyUpdate myUpdate = myUpdateManager.registerFileUploadForTask(fileHeader, creator, event.getTime());
                        myUpdate.setSuperUser(event.isSuperUser());
                    } else {
                        if (ISSUE_FILE_ADD.equals(event.getEventType())) {
                            EdsMyUpdate myUpdate = myUpdateManager.registerFileUploadForIssue(fileHeader, creator, event.getTime());
                            myUpdate.setSuperUser(event.isSuperUser());
                        }
                    }
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        if (event.isMyUpdatesItemAdd() && event.isSendMail1()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
