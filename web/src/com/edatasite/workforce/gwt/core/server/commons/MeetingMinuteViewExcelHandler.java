package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.Date;
import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 10.11.16
 * Time: 11:31
 */
public class MeetingMinuteViewExcelHandler extends BaseExcelHandler {

    @Autowired
    private MeetingManager meetingManager;

    @Autowired
    private MeetingAttendeesManager meetingAttendeesManager;


    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private MeetingAgendaTopicManager meetingAgendaTopicManager;

    @Autowired
    private MeetingAgendaDiscussionManager meetingAgendaDiscussionManager;

    @Autowired
    private NoteHistoryManager noteHistoryManager;

    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CrmContactManager contactManager;

    private static final Logger logger = LoggerFactory.getLogger(MeetingMinuteViewExcelHandler.class);
    int rowIndex = 0;

    @Override
    protected void setFileName() {
        filename = "Meeting Minutes";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        Integer objectID = filterParameter.getObjectId();
        try {
            EdsUser user = userManager.getUser();
            List<EdsMeetingAgendaTopic> edsMeetingAgendaTopics = meetingAgendaTopicManager.getAgendTopicByMeetingId(objectID);
            EdsMeetingMinutes meetingMinutes = meetingManager.get(objectID);

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Meeting Minutes");
            sheet.setDefaultColumnWidth(20);
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(0, 10000);

            rowIndex = 0;
            createHeaderTable(meetingMinutes, workbook, sheet, user);
            createItemTable(edsMeetingAgendaTopics, workbook, sheet);

            return workbook;
        } catch (Exception exp) {
            exp.printStackTrace();
            logger.error("Cannot generate " + filename + " excel report, exception: " + exp);
        }
        return null;
    }

    private void createHeaderTable(EdsMeetingMinutes meetingMinutes, HSSFWorkbook workbook, HSSFSheet sheet, EdsUser user) {
        int cellIndex = 2;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(user.getCompany().getName());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell(workbook));

        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.meetingMinutesList));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell(workbook));

        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell(workbook));
        rowIndex++;

        //Empty row
        cellIndex = 0;
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);

        //Meeting ID
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.number));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell meetingIDCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        meetingIDCell.setCellValue(meetingMinutes.getMeetingNumber());

        //Title
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.title));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell titleCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        titleCell.setCellValue(meetingMinutes.getTitle());

        //Called By
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.calledBy));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell calledByCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        calledByCell.setCellValue(meetingMinutes.getCalledBy() != null ? meetingMinutes.getCalledBy().getName() : "");

        //Start Date
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.startDate));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell startDateCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        startDateCell.setCellValue(longDateFormat(meetingMinutes.getStartDate()));

        //End Date
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.endDate));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell endDateCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        endDateCell.setCellValue(longDateFormat(meetingMinutes.getDueDate()));

        //Location
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.location));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell locationCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        locationCell.setCellValue(meetingMinutes.getLocation());

        //Type
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.type));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell typeCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        typeCell.setCellValue(meetingMinutes.getType() != null ? meetingMinutes.getType().getName() : "");

        //Purpose
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.purpose));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell purposeCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        purposeCell.setCellValue(meetingMinutes.getPurpose());

        //Attendees & Absent
        List<EdsMeetingAttendees> edsMeetingAttendees = meetingAttendeesManager.getMeetingAttendesMeetingId(meetingMinutes.getObjectID());
        StringBuilder attendees = new StringBuilder();
        StringBuilder absent = new StringBuilder();
        for (EdsMeetingAttendees attendeesListItem : edsMeetingAttendees) {
            if (!attendeesListItem.isAttendees()) {
                absent.append(attendeesListItem.getAttendeesEmployee() != null ? attendeesListItem.getAttendeesEmployee().getName() + ",\n" : "");
            }
        }

        if (meetingMinutes.getNonCompanyAttendees() != null && !"".equals(meetingMinutes.getNonCompanyAttendees())) {
            String[] attendessStrings = meetingMinutes.getNonCompanyAttendees().split(",");
            for (String attendessItem : attendessStrings) {
                attendees.append(attendessItem != null && !attendessItem.isEmpty() ? attendessItem + ",\n" : "");
            }
        }

        //Attendees
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.attendees));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell attendeesCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        attendeesCell.setCellValue((attendees.length() > 3 ? attendees.substring(0, attendees.length() - 2) : attendees.toString()));

        //Absent
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.absentLetter));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell absentCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        absentCell.setCellValue(absent.length() > 3 ? absent.substring(0, absent.length() - 2) : absent.toString());

        //Prepared By
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.preparedBy));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell preparedByCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        preparedByCell.setCellValue(meetingMinutes.getPrepairedBy() != null ? meetingMinutes.getPrepairedBy().getName() : "");

        //Next Meeting
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.nextMeeting));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

        HSSFCell nextMeetingCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        nextMeetingCell.setCellValue(meetingMinutes.getNextMeetingDate() != null ? dateFormat(meetingMinutes.getNextMeetingDate()) : "");

    }


    private void createItemTable(List<EdsMeetingAgendaTopic> edsMeetingAgendaTopics, HSSFWorkbook workbook, HSSFSheet sheet) {
        int cellIndex = 0;
        int cellCount = 5;

        for (EdsMeetingAgendaTopic agendaListItem : edsMeetingAgendaTopics) {

            //Empty row
            rowIndex++;
            HSSFRow emptyCell = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
            rowIndex++;

            HSSFRow agendaTopicCell = generateOneRowWithEmptyCell(rowIndex, cellIndex, sheet);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.agendaTopic) + " " + (agendaListItem.getName() != null ? agendaListItem.getName() : ""));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell(workbook));

            rowIndex++;
            emptyCell = generateOneRowWithEmptyCell(rowIndex, cellCount, sheet);

            sheet.getRow(rowIndex).getCell(0).setCellValue(commonLocalizer.localize(PdfLocalizationName.discussionPoints));
            sheet.getRow(rowIndex).getCell(0).setCellStyle(getBlueStyleCell(workbook));
            sheet.getRow(rowIndex).setHeight((short) 500);

            sheet.getRow(rowIndex).getCell(1).setCellValue(commonLocalizer.localize(PdfLocalizationName.actionPoints));
            sheet.getRow(rowIndex).getCell(1).setCellStyle(getBlueStyleCell(workbook));

            sheet.getRow(rowIndex).getCell(2).setCellValue(commonLocalizer.localize(PdfLocalizationName.assignedTo));
            sheet.getRow(rowIndex).getCell(2).setCellStyle(getBlueStyleCell(workbook));

            sheet.getRow(rowIndex).getCell(3).setCellValue(commonLocalizer.localize(PdfLocalizationName.startDate));
            sheet.getRow(rowIndex).getCell(3).setCellStyle(getBlueStyleCell(workbook));

            sheet.getRow(rowIndex).getCell(4).setCellValue(commonLocalizer.localize(PdfLocalizationName.dueDate));
            sheet.getRow(rowIndex).getCell(4).setCellStyle(getBlueStyleCell(workbook));

            List<EdsMeetingAgendaDiscussion> discussions = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(agendaListItem.getObjectID());
            if (discussions != null && !discussions.isEmpty()) {
                for (EdsMeetingAgendaDiscussion discussion : discussions) {
                    rowIndex++;
                    HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount, sheet);
                    String discussionPoint = discussion.getDiscussionPoints() != null ? discussion.getDiscussionPoints() : "";
                    String actionPoint = discussion.getActionPoints() != null ? discussion.getActionPoints() : "";
                    String assignedTo = discussion.getAssignedTo() != null ? discussion.getAssignedTo().getName() : "";
                    String startDate = discussion.getStartDate() != null ? dateFormat(discussion.getStartDate()) : "";
                    String endDate = discussion.getDueDate() != null ? dateFormat(discussion.getDueDate()) : "";
                    if (!discussionPoint.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                        sheet.getRow(rowIndex).getCell(0).setCellValue(discussionPoint);
                        sheet.getRow(rowIndex).getCell(1).setCellValue(actionPoint);
                        sheet.getRow(rowIndex).getCell(2).setCellValue(assignedTo);
                        sheet.getRow(rowIndex).getCell(3).setCellValue(startDate);
                        sheet.getRow(rowIndex).getCell(4).setCellValue(endDate);
                    }
                }
            }
        }
    }

    private HSSFRow generateOneRowWithEmptyCell(int rowNumber, int cells, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= cells; i++) {
            row.createCell(i);
        }
        return row;
    }

    private CellStyle getHeaderTitleStyleCell(HSSFWorkbook workbook) {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderFieldStyleCell(HSSFWorkbook workbook) {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getBlueStyleCell(HSSFWorkbook workbook) {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }


}
