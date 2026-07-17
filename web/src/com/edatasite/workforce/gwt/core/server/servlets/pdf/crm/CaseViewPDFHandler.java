package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCustomView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 31-May-2009
 * Time: 07:30:04
 * To change this template use File | Settings | File Templates.
 */
public class CaseViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private CRMService crmService;

    @Autowired
    private AllInOneService allInOneService;

    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RbacService rbacService;

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ContactService contactService;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private CaseManager caseManager;

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CASE_SUMMARY;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextCustomView pdfData = new ITextCustomView();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.CUSTOMVIEW);

        SimpleDateFormat dateType = getCompanyShortDateFormat(taskManager.getUser().getCompany());

        RequestObject requestObject = (RequestObject) dataClass;
        int id = requestObject.getObjectID();
        CaseItem item = crmService.getCase(id, false);
        float[] colsWidth = {1f, 5.3f};
        PdfPTable caseContainer = new PdfPTable(colsWidth);
        caseContainer.setWidthPercentage(100);

        String defaultFontName = getDefaultFont(taskManager.getUser().getCompany());
        //Case content start
        Font lableFont = FontFactory.getFont(defaultFontName, BaseFont.IDENTITY_H, 11, Font.BOLD);
        lableFont.setColor(76, 126, 173);

        Font font = FontFactory.getFont(defaultFontName, BaseFont.IDENTITY_H, 10, Font.NORMAL);

        Font fontHeader = FontFactory.getFont(defaultFontName, BaseFont.IDENTITY_H, 10, Font.BOLD);
        fontHeader.setColor(116, 116, 116);

        PdfPCell cellSpace = new PdfPCell();
        cellSpace.setColspan(2);
        cellSpace.setHorizontalAlignment(1);
        cellSpace.setPadding(10f);
        cellSpace.setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSpace5PX = new PdfPCell();
        cellSpace5PX.setColspan(2);
        cellSpace5PX.setPadding(5f);
        cellSpace5PX.setBorder(Rectangle.NO_BORDER);

        PdfPCell cellLabel = new PdfPCell();
        cellLabel.setVerticalAlignment(1);
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setNoWrap(true);

        PdfPCell cell = generateCell();

        PdfPCell innerCell = new PdfPCell();
        innerCell.setHorizontalAlignment(0);
        innerCell.setVerticalAlignment(1);
        innerCell.setPaddingBottom(8f);
        innerCell.setBorderColor(new Color(213, 213, 213));


        PdfPCell innerHeaderCell = new PdfPCell();
        innerHeaderCell.setVerticalAlignment(1);
        innerHeaderCell.setPaddingBottom(8);
        innerHeaderCell.setBorderColor(new Color(213, 213, 213));
        innerHeaderCell.setHorizontalAlignment(1);

        cellSpace.setPhrase(new Phrase(commonLocalizer.localize("number") + ". -[" + item.getCaseNumber() + "]", new Font(Font.COURIER, 13, Font.BOLD)));
        caseContainer.addCell(cellSpace);
        cellSpace.setPhrase(new Phrase(""));

        cellLabel.setPhrase(new Phrase(crmLocalizer.localize(PdfLocalizationName.subject) + ":", lableFont));
        caseContainer.addCell(cellLabel);

        cell.setPhrase(new Phrase(item.getSubject(), font));
        caseContainer.addCell(cell);
        caseContainer.addCell(cellSpace);

        //Case reported by start

        String name = "";
        if (item.getAccountId() != null) {
            name = item.getAccountName();
        } else if (item.getLeadId() != null) {
            name = item.getLead();
        } else if (item.getCrmContactID() != null) {
            name = item.getCrmContact();
        }
        String firstName = name;
        String lastName = name;
        if (name.contains(" ")) {
            firstName = name.substring(0, name.indexOf(" "));
            lastName = name.substring(name.indexOf(" ") + 1, name.length());
        }

        cellLabel.setPhrase(new Phrase(crmLocalizer.localize(PdfLocalizationName.reportedBy) + ":", lableFont));
        caseContainer.addCell(cellLabel);
        if (!"".equals(firstName)) {
            PdfPTable reported = new PdfPTable(4);
            reported.setWidthPercentage(100);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.firstName), fontHeader));
            reported.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.lastName), fontHeader));
            reported.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.company), fontHeader));
            reported.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.phone), fontHeader));
            reported.addCell(innerHeaderCell);

            innerCell.setPhrase(new Phrase(firstName, font));
            reported.addCell(innerCell);
            innerCell.setPhrase(new Phrase(lastName, font));
            reported.addCell(innerCell);
            innerCell.setPhrase(new Phrase(item.getCompany() != null ? item.getCompany() : "", font));
            reported.addCell(innerCell);
            innerCell.setPhrase(new Phrase(item.getPhone() != null ? item.getPhone().replace("|", "") : "", font));
            reported.addCell(innerCell);
            cell = generateCell();
            cell.addElement(reported);
            caseContainer.addCell(cell);
        } else {
            cell.setPhrase(new Phrase(" "));
            caseContainer.addCell(cell);
        }
        caseContainer.addCell(cellSpace);
        //Case reported by end

        String description = getDescription(item.getDescription());
        cell = generateCell();
        cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.description) + ":", lableFont));
        caseContainer.addCell(cellLabel);
        cell.setPhrase(new Phrase(description, font));
        caseContainer.addCell(cell);
        caseContainer.addCell(cellSpace);

        cell = generateCell();
        cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.assignee) + ":", lableFont));
        caseContainer.addCell(cellLabel);
        cell.setPhrase(new Phrase(item.getCaseAssigneeName() != null ? item.getCaseAssigneeName() : " ", font));
        caseContainer.addCell(cell);
        caseContainer.addCell(cellSpace);

        //Case notes start
        ArrayList<HistoryListItem> list = allInOneService.getNotes(id, RelationItem.TYPE_CASE);

        if (list != null && list.size() > 0) {
            cellLabel.setPhrase(new Phrase(crmLocalizer.localize(PdfLocalizationName.notes) + ":", lableFont));
            caseContainer.addCell(cellLabel);
            String subject = "";
            String relatedName = "";
            String date = "";
            for (int i = 0; i < list.size(); i++) {
                subject = list.get(i).getComment(true) != null ? getDescription(list.get(i).getComment(true)) : "";
                relatedName = list.get(i).getEmployee() != null ? list.get(i).getEmployee() : "";
                date = list.get(i).getEventDate() != null ? dateType.format(list.get(i).getEventDate()) : "";
                if (i > 0) {
                    cellLabel.setPhrase(new Phrase("", lableFont));
                    caseContainer.addCell(cellLabel);
                }
                cell = generateCell();
                cell.setPhrase(new Phrase(subject, font));
                caseContainer.addCell(cell);
                cell = generateCell2();
                cellLabel.setPhrase(new Phrase("", lableFont));
                caseContainer.addCell(cellLabel);
                cell.setPhrase(new Phrase(relatedName + ". " + date, fontHeader));
                caseContainer.addCell(cell);
                caseContainer.addCell(cellSpace);

            }
        } else {
            cell.setPhrase(new Phrase(" "));
            caseContainer.addCell(cell);
        }
        caseContainer.addCell(cellSpace);
        //Case notes end

        //Case attachments start
        ArrayList<FileResource> resource = documentsService.getFileResources(Constants.F_CASE, id, id);
        PdfPTable attachment = new PdfPTable(3);
        attachment.setWidthPercentage(100);
        attachment.setSpacingAfter(10);
        attachment.setSpacingBefore(10);
        cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.attachment) + ":", lableFont));
        caseContainer.addCell(cellLabel);
        if (resource != null && resource.size() > 0) {
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileName), fontHeader));
            attachment.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileSize), fontHeader));
            attachment.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileType), fontHeader));
            attachment.addCell(innerHeaderCell);
            for (FileResource aResource : resource) {
                innerCell.setPhrase(new Phrase(aResource.getEncodedName(), font));
                attachment.addCell(innerCell);

                innerCell.setPhrase(new Phrase(aResource.getContentLength() != null ? getFileSizeAsString(aResource.getContentLength()) : "", font));
                attachment.addCell(innerCell);

                innerCell.setPhrase(new Phrase(aResource.getContentType() != null ? aResource.getContentType() : "", font));
                attachment.addCell(innerCell);

            }
            cell = generateCell();
            cell.addElement(attachment);
            caseContainer.addCell(cell);
        } else {
            cell.setPhrase(new Phrase(" "));
            caseContainer.addCell(cell);
        }
        caseContainer.addCell(cellSpace);
        //Case attachments end*/


        //case tasks start
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLimit(LIMIT_PDF_ROWS);
        filterParameter.setRelationID(id);
        filterParameter.setRelationType(RelationItem.TYPE_CASE);
        filterParameter.setCrmTaskList(true);
        TaskList taskList = taskService.getTaskList(filterParameter);
        List<TaskListItem> taskLists = taskList.getList();

        for (TaskListItem task : taskLists) {
            PdfPCell cellTask = new PdfPCell();
            cellTask.setColspan(2);

            caseContainer.addCell(cellSpace);
            float[] colsWidth2 = {1.4f, 5f};
            PdfPTable taskTable = new PdfPTable(colsWidth2);
            taskTable.setWidthPercentage(100);

            String taskName = task.getName() != null ? task.getName() : "";//task name
            String taskDescription = escapeHtml(task.getDescription() != null ? clearBrTag(task.getDescription()) : "");//task description
            String taskNumber = task.getNumber() == null ? "" : task.getNumber();
            //task number start
            cell = generateCell();
            cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.taskNo) + ":", lableFont));
            cell.setPhrase(new Phrase(taskNumber, font));
            taskTable.addCell(cellLabel);
            taskTable.addCell(cell);
            taskTable.addCell(cellSpace5PX);
            //task number end

            //task name start
            cell = generateCell();
            cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.taskName) + ":", lableFont));
            cell.setPhrase(new Phrase(taskName, font));
            taskTable.addCell(cellLabel);
            taskTable.addCell(cell);
            taskTable.addCell(cellSpace5PX);
            //task name end

            //task description start
            cell = generateCell();
            cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.description) + ":", lableFont));
            cell.setPhrase(new Phrase(taskDescription, font));
            taskTable.addCell(cellLabel);
            taskTable.addCell(cell);
            //task description end

            //task extra start
            String startDate = task.getStartDate() != null ? dateFormat(task.getStartDate()) : "";
            String dueDate = task.getDueDate() != null ? dateFormat(task.getDueDate()) : "";
            String priority = task.getPriorityName() != null ? task.getPriorityName() : "";
            String status = task.getStatusName() != null ? task.getStatusName() : "";

            PdfPTable extraTable = new PdfPTable(4);
            extraTable.setWidthPercentage(100);

            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.startDate), fontHeader));
            extraTable.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(accountingLocalizer.localize(PdfLocalizationName.dueDate), fontHeader));
            extraTable.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.priority), fontHeader));
            extraTable.addCell(innerHeaderCell);
            innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.status), fontHeader));
            extraTable.addCell(innerHeaderCell);

            innerCell.setPhrase(new Phrase(startDate, font));
            extraTable.addCell(innerCell);
            innerCell.setPhrase(new Phrase(dueDate, font));
            extraTable.addCell(innerCell);
            innerCell.setPhrase(new Phrase(priority, font));
            extraTable.addCell(innerCell);
            innerCell.setPhrase(new Phrase(status, font));
            extraTable.addCell(innerCell);

            cellLabel.setPhrase(new Phrase(" "));
            taskTable.addCell(cellLabel);
            cell = generateCell();
            cell.addElement(extraTable);
            taskTable.addCell(cell);
            taskTable.addCell(cellSpace5PX);
            //task extra end

            //members involved start
            TaskInvolvedMember[] taskMembersInvolved = taskService.getAssignments(task.getObjectID());

            cellLabel.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.membersInvolved) + ":", lableFont));
            taskTable.addCell(cellLabel);
            if (taskMembersInvolved != null && taskMembersInvolved.length > 0) {
                PdfPTable membersTable = new PdfPTable(3);
                membersTable.setWidthPercentage(100);
                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.employee), fontHeader));
                membersTable.addCell(innerHeaderCell);
                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.status), fontHeader));
                membersTable.addCell(innerHeaderCell);
                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.timeSpent), fontHeader));
                membersTable.addCell(innerHeaderCell);
                for (TaskInvolvedMember member : taskMembersInvolved) {
                    String employeeName = member.getEmployee();//employee name
                    String employeeTaskStatus = member.getStatusName();//employee task status
//                    String employeeEstimatedTime = ServerUtils.timeSpentToString(member.getEstimatedTime());//employee estimated time
                    String employeeTimSpent = ServerUtils.timeSpentToString(member.getActualTime());//employee time spent

                    innerCell.setPhrase(new Phrase(employeeName, font));
                    membersTable.addCell(innerCell);

                    innerCell.setPhrase(new Phrase(employeeTaskStatus, font));
                    membersTable.addCell(innerCell);

                    innerCell.setPhrase(new Phrase(employeeTimSpent, font));
                    membersTable.addCell(innerCell);
                }
                cell = generateCell();
                cell.addElement(membersTable);
                taskTable.addCell(cell);
            } else {
                cell.setPhrase(new Phrase(" "));
                taskTable.addCell(cell);
            }
            taskTable.addCell(cellSpace5PX);
            //members involved end

            //notes table start
            HistoryListItem[] note = taskService.getTaskNotes(task.getObjectID());
            if (note != null && note.length > 0) {
                cellLabel.setPhrase(new Phrase(crmLocalizer.localize(PdfLocalizationName.notes) + ":", lableFont));
                taskTable.addCell(cellLabel);
                String subject = "";
                String relatedName = "";
                String date = "";
                for (int i = 0; i < note.length; i++) {
                    subject = note[i].getComment(true) != null ? getDescription(note[i].getComment(true)) : "";
                    relatedName = note[i].getEmployee() != null ? note[i].getEmployee() : "";
                    date = note[i].getEventDate() != null ? dateType.format(note[i].getEventDate()) : "";
                    if (i > 0) {
                        cellLabel.setPhrase(new Phrase("", lableFont));
                        taskTable.addCell(cellLabel);
                    }
                    cell = generateCell();
                    cell.setPhrase(new Phrase(escapeHtml(subject.replace("<br/>", "")), font));
                    taskTable.addCell(cell);
                    cell = generateCell2();
                    cellLabel.setPhrase(new Phrase("", lableFont));
                    taskTable.addCell(cellLabel);
                    cell.setPhrase(new Phrase(relatedName + ". " + date, fontHeader));
                    taskTable.addCell(cell);
                    taskTable.addCell(cellSpace);

                }
            } else {
                cell.setPhrase(new Phrase(" "));
                taskTable.addCell(cell);
            }
            taskTable.addCell(cellSpace5PX);
            //notes table end

            //attachments start
            FileResource[] attachmentsLists = taskService.getTaskAttachments(task.getObjectID());
            if (attachmentsLists != null && attachmentsLists.length > 0) {
                cell.setPhrase(new Phrase(crmLocalizer.localize(PdfLocalizationName.attachment) + ":", lableFont));
                taskTable.addCell(cell);
                PdfPTable attachmentTable = new PdfPTable(3);
                attachmentTable.setWidthPercentage(100);

                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileName), fontHeader));
                attachmentTable.addCell(innerHeaderCell);
                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileSize), fontHeader));
                attachmentTable.addCell(innerHeaderCell);
                innerHeaderCell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.fileType), fontHeader));
                attachmentTable.addCell(innerHeaderCell);
                for (FileResource _attachment : attachmentsLists) {
                    String fileName = _attachment.getEncodedName() != null ? _attachment.getEncodedName() : "";//file name
                    String fileType = _attachment.getContentType() != null ? _attachment.getContentType() : "";//file type
                    String fileSize = _attachment.getContentLength() != null ? getFileSizeAsString(_attachment.getContentLength()) : "";//file size

                    innerCell.setPhrase(new Phrase(fileName, font));
                    attachmentTable.addCell(innerCell);

                    innerCell.setPhrase(new Phrase(fileSize, font));
                    attachmentTable.addCell(innerCell);

                    innerCell.setPhrase(new Phrase(fileType, font));
                    attachmentTable.addCell(innerCell);

                }
                cell = generateCell();
                PdfPTable attachmentTask = new PdfPTable(3);
                attachmentTask.setWidthPercentage(100);
                attachmentTask.setSpacingAfter(10);
                attachmentTask.setSpacingBefore(10);
                cell.addElement(attachmentTask);
                taskTable.addCell(cell);
            } else {
                cell.setPhrase(new Phrase(" "));
                taskTable.addCell(cell);
            }
            taskTable.addCell(cellSpace5PX);
            //attachments end
            cellTask.addElement(taskTable);
            caseContainer.addCell(cellTask);
        }
        //case tasks end

        float[] colsWidth3 = {1f, 1f, 1f, 3f};
        PdfPTable empTable = new PdfPTable(colsWidth3);
        empTable.setWidthPercentage(100);

        cell = generateCell();
        EdsUser user = taskManager.getUser();
        cell.setHorizontalAlignment(0);
        cell.setPhrase(new Phrase(""));
        empTable.addCell(cell);
        empTable.addCell(cell);
        cell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.employee) + ":", new Font(Font.COURIER, 12, Font.BOLD)));
        empTable.addCell(cell);
        cell.setPhrase(new Phrase(user.getFullName(), new Font(Font.COURIER, 12)));
        empTable.addCell(cell);

        cell.setPhrase(new Phrase(""));
        empTable.addCell(cell);
        empTable.addCell(cell);
        cell.setPhrase(new Phrase(commonLocalizer.localize(PdfLocalizationName.company) + ":", new Font(Font.COURIER, 12, Font.BOLD)));
        empTable.addCell(cell);
        cell.setPhrase(new Phrase(user.getCompany().getName(), new Font(Font.COURIER, 12)));
        empTable.addCell(cell);
        cellSpace.addElement(empTable);
        caseContainer.addCell(cellSpace);


        pdfData.setCustomTable(caseContainer);
        pdf.setCustomView(pdfData);
        return pdf;
    }

    private String getDescription(String description) {
        if (!"".equals(description.trim())) {
            description = description.trim();
            org.jsoup.nodes.Document htmlDocument = Jsoup.parse(description);
            if (htmlDocument != null) {
                description = htmlDocument.text();
            }
        }
        return description;
    }

    private PdfPCell generateCell() {
        PdfPCell cell = new PdfPCell();
        cell.setPaddingBottom(4f);
        cell.setVerticalAlignment(1);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private PdfPCell generateCell2() {
        PdfPCell cell = new PdfPCell();
        cell.setPaddingBottom(2f);
        cell.setVerticalAlignment(1);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        RequestObject requestObject = (RequestObject) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        int id = requestObject.getObjectID();
        CaseItem item = crmService.getCase(id, false);
        EdsUser user = taskManager.getUser();
        ContactListItem contactItem = new ContactListItem();
        if (item.getCrmContactID() != null) {
            contactItem = contactService.getContact(item.getCrmContactID(), false);
        }
        SimpleDateFormat dateType = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Integer contactRequestCount = caseManager.getContactRequestCount(item.getCrmContactID(), item.getObjectId());
        Object[] creationDateAndCaseNumber = caseManager.getLastCaseNumberAndCreationDate(item.getCrmContactID());

        String lastRequestCaseNumber = "";
        String lastRequestCreationDate = "";
        if (creationDateAndCaseNumber != null) {
            lastRequestCaseNumber = creationDateAndCaseNumber[1] != null ? creationDateAndCaseNumber[1].toString() : "";
            lastRequestCreationDate = creationDateAndCaseNumber[2] != null ? dateAndTimeFormat.format(creationDateAndCaseNumber[2]) : "";
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customDataList = new HashMap<>();
        CustomisedITextTable noteTable = new CustomisedITextTable();
        CustomisedITextTable noteHeaderTable = new CustomisedITextTable();
        CustomisedITextTable attachmentTable = new CustomisedITextTable();
        CustomisedITextTable attachmentHeaderTable = new CustomisedITextTable();
        CustomisedITextTable caseTable = new CustomisedITextTable();

        caseTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        caseTable.addRowWithCode("CASE_NUMBER", crmLocalizer.localize("caseNumber"), escapeHtml(item.getCaseNumber()));
        caseTable.addRowWithCode("SUBJECT", crmLocalizer.localize(PdfLocalizationName.subject), escapeHtml(item.getSubject()));
        caseTable.addRowWithCode(CREATOR, commonLocalizer.localize(PdfLocalizationName.creator), escapeHtml(user.getFullName()));
        caseTable.addRowWithCode(REPORTER, crmLocalizer.localize(PdfLocalizationName.reportedBy), escapeHtml(item.getReportedBy()));
        caseTable.addRowWithCode(REPORTER_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(item.getEmail()));
        caseTable.addRowWithCode("REPORTER_PHONE", commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(item.getPhone()));
        caseTable.addRowWithCode("REPORTER_COMPANY", commonLocalizer.localize(PdfLocalizationName.company), escapeHtml(item.getCompany()));
        caseTable.addRowWithCode(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(getDescription(item.getDescription())));
        caseTable.addRowWithCode(ASSIGNEE_NAME, commonLocalizer.localize(PdfLocalizationName.assignee), escapeHtml(item.getCaseAssigneeName()));
        caseTable.addRowWithCode(CREATED_DATE, commonLocalizer.localize(PdfLocalizationName.createdDate), item.getCreatedDate() != null ? dateType.format(item.getCreatedDate()) : "");
        caseTable.addRowWithCode("CREATED_DATE_TIME", commonLocalizer.localize(PdfLocalizationName.createdDate), item.getCreatedDate() != null ? dateAndTimeFormat.format(item.getCreatedDate()) : "");
        caseTable.addRowWithCode("CONTACT_REQUEST_COUNT", "", contactRequestCount.toString());
        caseTable.addRowWithCode("LAST_REQUEST_CREATION_DATE", "", lastRequestCreationDate);
        caseTable.addRowWithCode("LAST_REQUEST_CASE_NUMBER", "", lastRequestCaseNumber);

        noteTable.addColumnOrder("SUBJECT", "PUBLISHED_BY", "DATE");

        ArrayList<HistoryListItem> list = allInOneService.getNotes(id, RelationItem.TYPE_CASE);
        noteHeaderTable.addColumnOrder("NOTES", "SUBJECT", "PUBLISHED_BY", "DATE");
        noteHeaderTable.addRowWithCode("NOTES", crmLocalizer.localize(PdfLocalizationName.notes),
                crmLocalizer.localize(PdfLocalizationName.subject),
                commonLocalizer.localize(PdfLocalizationName.publishedBy),
                crmLocalizer.localize(PdfLocalizationName.date));
        if (list.size() > 0) {
            String subject = "";
            String relatedName = "";
            String date = "";
            for (HistoryListItem listItem : list) {
                subject = listItem.getComment() != null ? getDescription(listItem.getComment(true)) : "";
                relatedName = escapeHtml(listItem.getEmployee());
                date = listItem.getEventDate() != null ? longDateFormat(listItem.getEventDate()) : "";
                noteTable.addRow(subject, relatedName, date);
            }
        }
        attachmentTable.addColumnOrder("FILE_NAME", "FILE_SIZE", "FILE_TYPE");
        ArrayList<FileResource> resource = documentsService.getFileResources(Constants.F_CASE, id, id);
        attachmentHeaderTable.addColumnOrder("ATTACHMENTS_TITLE", "FILE_NAME", "FILE_SIZE", "FILE_TYPE");
        attachmentHeaderTable.addRowWithCode("ATTACHMENTS", commonLocalizer.localize(PdfLocalizationName.attachment),
                commonLocalizer.localize(PdfLocalizationName.fileName),
                commonLocalizer.localize(PdfLocalizationName.fileSize),
                commonLocalizer.localize(PdfLocalizationName.fileType));
        if (resource != null && resource.size() > 0) {

            for (FileResource aResource : resource) {
                attachmentTable.addRow(aResource.getEncodedName(), aResource.getContentLength() != null ? getFileSizeAsString(aResource.getContentLength()) : "", aResource.getContentType() != null ? aResource.getContentType() : "");
            }
        }
        customData.put("NOTE_TABLE", noteTable);
        customData.put("NOTE_HEADER_TABLE", noteHeaderTable);
        customData.put("ATTACHMENT_TABLE", attachmentTable);
        customData.put("ATTACHMENT_HEADER_TABLE", attachmentHeaderTable);
        customData.put("CASE_TABLE", caseTable);

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLimit(LIMIT_PDF_ROWS);
        filterParameter.setRelationID(id);
        filterParameter.setRelationType(RelationItem.TYPE_CASE);
        filterParameter.setCrmTaskList(true);

        TaskList taskList = taskService.getTaskList(filterParameter);
        List<TaskListItem> taskLists = taskList.getList();
        LinkedList<HashMap<String, CustomisedITextTable>> listTaskAll = new LinkedList<>();
        for (TaskListItem task : taskLists) {
            HashMap<String, CustomisedITextTable> listTaskDetails = new HashMap<>();
            CustomisedITextTable taskContentTable = new CustomisedITextTable();

            String taskName = escapeHtml(task.getName());//task name
            String taskDescription = escapeHtml(task.getDescription() != null ? clearBrTag(task.getDescription()) : "");//task description
            String taskNumber = escapeHtml(task.getNumber());
            //task number start
            taskContentTable.addColumnOrder("TASK_NUMBER_TITLE", "TASK_NUMBER");
            taskContentTable.addRowWithCode("TASKNUMBER", commonLocalizer.localize(PdfLocalizationName.taskNo), taskNumber);
            listTaskDetails.put("TASK_NUMBER_TABLE", taskContentTable);
            //task number end

            //task name start
            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("TASK_NAME_TITLE", "TASK_NAME");
            taskContentTable.addRowWithCode("TASKNAME", commonLocalizer.localize(PdfLocalizationName.taskName), taskName);
            listTaskDetails.put("TASK_NAME_TABLE", taskContentTable);
            //task me end

            //task description start
            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("TASK_DESCRIPTION_TITLE", "TASK_DESCRIPTION");
            taskContentTable.addRowWithCode("TASKDESCRIPTION", commonLocalizer.localize(PdfLocalizationName.description), taskDescription);
            listTaskDetails.put("TASK_DESCRIPTION_TABLE", taskContentTable);
            //task description end

            //task extra start

            String startDate = task.getStartDate() != null ? dateFormat(task.getStartDate()) : "";
            String dueDate = task.getDueDate() != null ? dateFormat(task.getDueDate()) : "";
            String priority = escapeHtml(task.getPriorityName());
            String status = escapeHtml(task.getStatusName());

            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("START_DATE_TITLE", "START_DATE");
            taskContentTable.addRowWithCode("STARTDATE", commonLocalizer.localize(PdfLocalizationName.startDate), startDate);
            listTaskDetails.put("TASK_EXTRA_START_DATE_TABLE", taskContentTable);

            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("DUE_DATE_TITLE", "DUE_DATE");
            taskContentTable.addRowWithCode("DUEDATE", accountingLocalizer.localize(PdfLocalizationName.dueDate), dueDate);
            listTaskDetails.put("TASK_EXTRA_DUE_DATE_TABLE", taskContentTable);

            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("PRIORITY_TITLE", "PRIORITY");
            taskContentTable.addRowWithCode("PRIORITY", commonLocalizer.localize(PdfLocalizationName.priority), priority);
            listTaskDetails.put("TASK_EXTRA_PRIORITY_TABLE", taskContentTable);

            taskContentTable = new CustomisedITextTable();
            taskContentTable.addColumnOrder("STATUS_TITLE", "STATUS");
            taskContentTable.addRowWithCode("STATUS", commonLocalizer.localize(PdfLocalizationName.status), status);
            listTaskDetails.put("TASK_EXTRA_STATUS_TABLE", taskContentTable);
            //task extra end

            //members involved start
            CustomisedITextTable membersInvolvedTable = new CustomisedITextTable();
            CustomisedITextTable membersInvolvedHeaderTable = new CustomisedITextTable();
            membersInvolvedTable.addColumnOrder("EMPLOYEE_NAME", "EMPLOYEE_TASK_STATUS", /*"EMPLOYEE_ESTIMATED_TIME", */"EMPLOYEE_TIME_SPENT");
            TaskInvolvedMember[] taskMembersInvolved = taskService.getAssignments(task.getObjectID());
            membersInvolvedHeaderTable.addColumnOrder("MEMBERS_INVOLVED_TITLE", "EMPLOYEE_NAME", "EMPLOYEE_TASK_STATUS", /*"EMPLOYEE_ESTIMATED_TIME", */"EMPLOYEE_TIME_SPENT");
            membersInvolvedHeaderTable.addRowWithCode("MEMBERS_INVOLVED", commonLocalizer.localize(PdfLocalizationName.membersInvolved),
                    commonLocalizer.localize(PdfLocalizationName.employee),
                    commonLocalizer.localize(PdfLocalizationName.status),
                    commonLocalizer.localize(PdfLocalizationName.timeSpent));
            if (taskMembersInvolved != null && taskMembersInvolved.length > 0) {

                for (TaskInvolvedMember member : taskMembersInvolved) {
                    String employeeName = escapeHtml(member.getEmployee());//employee name
                    String employeeTaskStatus = escapeHtml(member.getStatusName());//employee task status
//                    String employeeEstimatedTime = ServerUtils.timeSpentToString(member.getEstimatedTime());//employee estimated time
                    String employeeTimSpent = ServerUtils.timeSpentToString(member.getActualTime());//employee time spent

                    membersInvolvedTable.addRow(employeeName, employeeTaskStatus, /*employeeEstimatedTime, */employeeTimSpent);
                }
            }
            listTaskDetails.put("TASK_MEMBERS_INVOLVED", membersInvolvedTable);
            listTaskDetails.put("TASK_MEMBERS_HEADER_INVOLVED", membersInvolvedHeaderTable);
            //members involved end

            //notes table start
            HistoryListItem[] notes = taskService.getTaskNotes(task.getObjectID());
            CustomisedITextTable notesTable = new CustomisedITextTable();
            notesTable.addColumnOrder("NOTE_SUBJECT", "NOTE_PUBLISHED_BY", "NOTE_DATE");
            if (notes != null && notes.length > 0) {
                for (HistoryListItem note : notes) {
                    String noteSubject = getDescription(note.getSubject());//note subject
                    String notePublishedBy = escapeHtml(note.getEmployee());//note published by
                    String noteDate = note.getEventDate() != null ? longDateFormat(note.getEventDate()) : "";//note date

                    notesTable.addRow(noteSubject, notePublishedBy, noteDate);
                }
            }
            listTaskDetails.put("TASK_NOTE", notesTable);
            //notes table end

            //attachments start
            CustomisedITextTable attachmentsTable = new CustomisedITextTable();
            attachmentsTable.addColumnOrder("FILE_NAME", "FILE_DOWNLOAD_URL", "FILE_TYPE", "FILE_SIZE"/*, "FILE_DESCRIPTION", "FILE_DATE", */);
            FileResource[] attachmentsLists = taskService.getTaskAttachments(task.getObjectID());

            if (attachmentsLists != null && attachmentsLists.length > 0) {
                for (FileResource attachment : attachmentsLists) {
                    String fileName = attachment.getEncodedName() != null ? attachment.getEncodedName() : "";//file name
                    String fileDownloadURL = escapeHtml(getDownloadURL(attachment));//file download URL
//                    String fileDescription = attachment.getDescription() != null && !"".equals(attachment.getDescription()) ? attachment.getDescription() : "";//file description
//                    String fileDate = attachment.getModificationDate() != null ? dateFormat(attachment.getModificationDate()) : "";//file date
                    String fileType = attachment.getContentType() != null ? attachment.getContentType() : "";//file type
                    String fileSize = attachment.getContentLength() != null ? getFileSizeAsString(attachment.getContentLength()) : "";//file size

                    attachmentsTable.addRow(fileName, fileDownloadURL, fileType, fileSize/*, fileDescription, fileDate, */);
                }
            }
            listTaskDetails.put("TASK_ATTACHMENT", attachmentsTable);
            //attachments end
            listTaskAll.add(listTaskDetails);
        }
        customDataList.put("CASE_TASKS", listTaskAll);
        pdfData.setCustomListData(customDataList);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (contactItem != null && contactItem.getCustomFields() != null && !contactItem.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : contactItem.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = dateType.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(customField.getFieldStringValue()));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue);
                    }
                    default ->
                            customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()));
                }
            }
        }
        customData.put("CONTACT_CUSTOM_FIELD", customFieldTable);

        CustomisedITextTable caseCustomFieldTable = new CustomisedITextTable();
        caseCustomFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        caseCustomFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (item.getCustomFields() != null && !item.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : item.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = dateType.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        caseCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(customField.getFieldStringValue()));
                        }
                        caseCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue);
                    }
                    default ->
                            caseCustomFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()));
                }
            }
        }
        customData.put("CASE_CUSTOM_FIELD", caseCustomFieldTable);


        pdfData.setCustomData(customData);
        return pdfData;
    }

    private String clearBrTag(String val) {
        return val.replace("<br>", " ").replace("</br>", " ");
    }

    private String getDownloadURL(FileResource fileResource) {
        if (Constants.GOOGLE.equals(fileResource.getUploadType())) {
            return fileResource.getGoogleDownloadLink();
        } else if (Constants.OFFICE_365.equals(fileResource.getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            return fileResource.getOfficeDownloadLink();
        }
        return EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/downloadFile?id=" + fileResource.getBodyId().toString();
    }

    private String getFileSizeAsString(Long size) {
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    private String getSize(Long size, double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        int id = requestObject.getObjectID();
        CaseItem item = crmService.getCase(id, false);
        if (item != null) {
            String subject = item.getSubject().contains("[") ? item.getSubject().substring(0, item.getSubject().indexOf("[")) : item.getSubject();
            setFileName((subject.length() > 24 ? subject.substring(0, 24) : subject) + "_" + dateFormat(new Date()));
        } else {
            setFileName("case_" + id + "_" + dateFormat(new Date()));
        }
    }

}
