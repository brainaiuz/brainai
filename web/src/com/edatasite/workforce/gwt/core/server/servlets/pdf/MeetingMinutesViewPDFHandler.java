package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsMeetingAgendaDiscussion;
import com.edatasite.workforce.core.domain.EdsMeetingAgendaTopic;
import com.edatasite.workforce.core.domain.EdsMeetingAttendees;
import com.edatasite.workforce.core.domain.EdsMeetingMinutes;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsMeetingMinutesCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaDiscussionManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAgendaTopicManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingAttendeesManager;
import com.edatasite.workforce.gwt.core.server.db.MeetingManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MeetingMinutesViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private MeetingManager meetingManager;
    @Autowired
    private MeetingAttendeesManager meetingAttendeesManager;
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
    private String meetingNo;

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        return requestObject.getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        EdsUser user = uploadManager.getUser();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer meetingId = requestObject.getObjectID();
        EdsMeetingMinutes item = meetingManager.get(meetingId);
        DateFormat dateFormat = getCompanyShortDateFormat(company);

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        this.meetingNo = escapeHtml(item.getMeetingNumber());
        String meetingNumber = escapeHtml(item.getMeetingNumber());
        String title = escapeHtml(item.getTitle());
        String calledBy = item.getCalledBy() != null ? escapeHtml(item.getCalledBy().getName()) : "";
        String startDate = longDateFormat(item.getStartDate());
        String endDate = longDateFormat(item.getDueDate());
        String location = escapeHtml(item.getLocation());
        String type = item.getType() != null ? escapeHtml(item.getType().getName()) : "";
        String purpose = escapeHtml(item.getPurpose());
        String projectName = item.getProject() != null ? escapeHtml(item.getProject().getName()) : "";
        String prepairedBy = item.getPrepairedBy() != null ? item.getPrepairedBy().getName() : "";

        List<EdsMeetingAttendees> edsMeetingAttendees = meetingAttendeesManager.getMeetingAttendesMeetingId(meetingId);
        StringBuilder attendees = new StringBuilder("");
        StringBuilder absent = new StringBuilder("");
        for (EdsMeetingAttendees attendeesListItem : edsMeetingAttendees) {
            if (!attendeesListItem.isAttendees()) {
                absent.append(attendeesListItem.getAttendeesEmployee() != null ? attendeesListItem.getAttendeesEmployee().getName() + "\n" : "");
            }
        }

        if (item.getNonCompanyAttendees() != null && !"".equals(item.getNonCompanyAttendees())) {
            String[] attendeesArray = item.getNonCompanyAttendees().split(",");
            for (String attendeesItem : attendeesArray) {
                attendees.append(escapeHtml(attendeesItem) + "\n");
            }
        }

        /*Details Start*/
        CustomisedITextTable meetingTable = new CustomisedITextTable();
        meetingTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        meetingTable.addRowWithCode(MEETING_ID, commonLocalizer.localize("number", "Meeting ID"), meetingNumber);
        meetingTable.addRowWithCode(TITLE, commonLocalizer.localize("title", "Title"), title);
        meetingTable.addRowWithCode("CALLED_BY", commonLocalizer.localize("calledBy", "Called By"), calledBy);
        meetingTable.addRowWithCode(EXP_START_DATE, commonLocalizer.localize("startDate", "Start Date"), startDate);
        meetingTable.addRowWithCode(EXP_END_DATE, commonLocalizer.localize("endDate", "End Date"), endDate);
        meetingTable.addRowWithCode(LOCATION, commonLocalizer.localize("location", "Location"), location);
        meetingTable.addRowWithCode(TYPE, commonLocalizer.localize("type", "Type"), type);
        meetingTable.addRowWithCode("PURPOSE", commonLocalizer.localize("purpose", "Purpose"), purpose);
        meetingTable.addRowWithCode(PROJECT_NAME, commonLocalizer.localize("project", "Project"), projectName);
        meetingTable.addRowWithCode("ATTENDEES", commonLocalizer.localize("attendees", "Attendees"), attendees.toString());
        meetingTable.addRowWithCode("ABSENT", commonLocalizer.localize("absent", "Absent"), absent.toString());
        meetingTable.addRowWithCode("PREPARED_BY", commonLocalizer.localize("preparedBy", "Prepared By"), prepairedBy);
        /*Details End*/

        /*Agenda Topic Start*/
        HashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customDataList = new HashMap<>();
        LinkedList<HashMap<String, CustomisedITextTable>> agendaTopics = new LinkedList<>();
        List<EdsMeetingAgendaTopic> edsMeetingAgendaTopics = meetingAgendaTopicManager.getAgendTopicByMeetingId(meetingId);
        for (EdsMeetingAgendaTopic agendaListItem : edsMeetingAgendaTopics) {
            HashMap<String, CustomisedITextTable> agenda = new HashMap<>();
            CustomisedITextTable agendaTopicTable = new CustomisedITextTable();

            agendaTopicTable.addColumn("DISCUSSION_POINTS", commonLocalizer.localize("discussionPoints", "Discussion points"));
            agendaTopicTable.addColumn("ACTION_POINTS", commonLocalizer.localize("actionPoints", "Action points"));
            agendaTopicTable.addColumn("ASSIGNEED_TO", commonLocalizer.localize("assigneedTo", "Assigneed To"));
            agendaTopicTable.addColumn("START_DATE", commonLocalizer.localize(PdfLocalizationName.startDate, "Start Date"));
            agendaTopicTable.addColumn("DUE_DATE", commonLocalizer.localize(PdfLocalizationName.endDate, "End Date"));
            List<String> values = Lists.newArrayList();
            List<EdsMeetingAgendaDiscussion> discussions = meetingAgendaDiscussionManager.getAgendDiscussionsByTopicID(agendaListItem.getObjectID());
            if (discussions != null && !discussions.isEmpty()) {
                for (EdsMeetingAgendaDiscussion discussion : discussions) {
                    values.add(escapeHtml(discussion.getDiscussionPoints()));
                    values.add(escapeHtml(discussion.getActionPoints()));
                    values.add(discussion.getAssignedTo() != null ? escapeHtml(discussion.getAssignedTo().getName()) : "");
                    values.add(discussion.getStartDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(discussion.getStartDate())) : dateFormat(discussion.getStartDate())) : "");
                    values.add(discussion.getDueDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(discussion.getDueDate())) : dateFormat(discussion.getDueDate())) : "");
                    agendaTopicTable.addRow(values.toArray(new String[]{}));
                    values.clear();
                }
            }
            agenda.put(commonLocalizer.localize(PdfLocalizationName.agendaTopic) + ": " + (agendaListItem.getName() != null ? agendaListItem.getName() : ""), agendaTopicTable);
            agendaTopics.add(agenda);
        }
        /*Agenda Topic End*/

        /*Notes Start*/
        CustomisedITextTable notesTable = new CustomisedITextTable();
        List<String> noteValues = Lists.newArrayList();
        notesTable.addColumn(COMMENT, commonLocalizer.localize("comment", "Comment"));
        notesTable.addColumn("ADDED_BY", commonLocalizer.localize("addedBy", "Added By"));
        notesTable.addColumn("DATE", commonLocalizer.localize("date", "Date"));

        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setRelationID(meetingId);
        parameter.setGroupById(EdsNoteHistory.MEETING_MINUTES);
        List<EdsNoteHistory> noteList = noteHistoryManager.getNoteList(parameter);
        if (noteList != null && !noteList.isEmpty()) {
            notesTable.setName(commonLocalizer.localize("notes", "Notes"));
            for (EdsNoteHistory history : noteList) {
                noteValues.add(escapeHtml(history.getComment()));
                noteValues.add(history.getEmployee() != null ? escapeHtml(history.getEmployee().getFullName()) : "");
                noteValues.add(dateFormat(history.getEventDate()));
                notesTable.addRow(noteValues.toArray(new String[]{}));
                noteValues.clear();
            }
        }
        /*Notes End*/

        /*Attachment Start*/
        CustomisedITextTable attachmentTable = new CustomisedITextTable();
        List<String> attachValues = Lists.newArrayList();

        ArrayList<FileResource> attachmentsLists = (ArrayList<FileResource>) attachmentUtilsManager.getAttachments(F_MEETING_MINUTES, meetingId, meetingId);
        if (attachmentsLists != null && attachmentsLists.size() > 0) {
            attachmentTable.setName(commonLocalizer.localize(PdfLocalizationName.attachments));
            attachmentTable.addColumn("FILE_NAME", commonLocalizer.localize(PdfLocalizationName.fileName));
            attachmentTable.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
            for (FileResource attachment : attachmentsLists) {
                CellData[] cellData = new CellData[1];
                cellData[0] = new CellData(ITextTableList.CELL_LINK);
                cellData[0].setText(attachment.getEncodedName());
                cellData[0].setLink(attachment.getAmazonLink());
                attachValues.add(cellData[0].toString());
                attachValues.add(escapeHtml(attachment.getDescription()));
                attachmentTable.addRow(attachValues.toArray(new String[]{}));
                attachValues.clear();
            }
        }
        /*Attachment End*/
        CustomisedITextTable meetingCustomFieldTable = new CustomisedITextTable();
        meetingCustomFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        meetingCustomFieldTable.setCustomFields(getCustomFields(user, item.getEdsMeetingMinutesCustomFields()));

        customData.put("MEETING_TABLE", meetingTable);
        customData.put("NOTE_DETAILS", notesTable);
        customData.put("ATTACHMENT_TABLE", attachmentTable);
        customData.put("MEETING_CUSTOM_FIELD", meetingCustomFieldTable);
        customDataList.put("AGENDA_TOPIC", agendaTopics);

        pdfData.setCustomListData(customDataList);
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private String getDateTime(Date dateString, Date endDate){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(dateString);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(endDate);
         //Get weekDay Name
        String weekDay = "";
        int dayOfWeek = calendarStart.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY == dayOfWeek) weekDay = "Monday";
        else if (Calendar.TUESDAY == dayOfWeek) weekDay = "Tuesday";
        else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "Wednesday";
        else if (Calendar.THURSDAY == dayOfWeek) weekDay = "Thursday";
        else if (Calendar.FRIDAY == dayOfWeek) weekDay = "Friday";
        else if (Calendar.SATURDAY == dayOfWeek) weekDay = "Saturday";
        else if (Calendar.SUNDAY == dayOfWeek) weekDay = "Sunday";

        // Day
        int dayOfMonth = calendarStart.get(Calendar.DAY_OF_MONTH);
        // Month Name
        int monthId = calendarStart.get(Calendar.MONTH);
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (monthId >= 0 && monthId <= 11 ) {
            month = months[monthId];
        }
        //Year
        int year = calendarStart.get(Calendar.YEAR);
        //Start Hour minute
        String hourOfDay = getHourOrMinute(calendarStart.get(Calendar.HOUR_OF_DAY));
        String minute = getHourOrMinute(calendarStart.get(Calendar.MINUTE));
        //End Hour minute
        String endHourOfDay = getHourOrMinute(calendarEnd.get(Calendar.HOUR_OF_DAY));
        String endMinute = getHourOrMinute(calendarEnd.get(Calendar.MINUTE));

        return weekDay + " " + dayOfMonth +"th. of " + month + " " + year +", " + hourOfDay + ":" + minute + " to " + endHourOfDay + ":" + endMinute;
    }
     private Calendar getDate(String dateString){
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
         sdf.setTimeZone(TimeZone.getDefault());
         Date date = null;
         try {
             date = sdf.parse(dateString);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         return calendar;
     }
     private String getHourOrMinute(int value){
        return value < 10 ? "0" + value : "" + value;
     }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsUser user, EdsMeetingMinutesCustomFields meetingMinutesCustomFields) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (meetingMinutesCustomFields != null ) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(meetingMinutesCustomFields, commonService.getCompanyCustomFields(ViewName.MeetingMInutesView));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : "—");
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "—");
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put(HRMS, itemCusFields);
            }
        }
        return customFields;
    }

    private String getSize(Long size, double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer meettingId = requestObject.getObjectID();
        EdsMeetingMinutes meetingMinutes = meetingManager.get(meettingId);
        if (meetingMinutes != null && meetingMinutes.getTitle() != null) {
            setFileName((meetingMinutes.getTitle().length() > 24 ? meetingMinutes.getTitle().substring(0, 24) : meetingMinutes.getTitle()) + "_" + dateFormat(new Date()));
        } else {
            setFileName("MeetingMinutes_" + dateFormat(new Date()));
        }
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Meeting #" + meetingNo;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.MEETING_MINUTES;
    }
}
