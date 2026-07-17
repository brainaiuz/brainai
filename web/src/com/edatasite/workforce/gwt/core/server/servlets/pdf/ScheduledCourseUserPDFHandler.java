package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseReservation;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 9/22/12
 * Time: 1:46 AM
 */
public class ScheduledCourseUserPDFHandler extends AbstractITextPostPdfHandler {
    private static final String LONG_DATE_FORMAT = "MMM dd, yyyy [HH:mm]";

    @Autowired
    private TCService tcService;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        ITextSummaryView pdfData = new ITextSummaryView();
//        pdf.setSummaryView(pdfData);
//        pdf.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        pdf.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);

        EdsUser user = uploadManager.getUser();
        final EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
        }
        Date startDate = null;
        try {
            if (filterParametrs.getStartDateNC() != null && filterParametrs.getViewType() != null) {
                startDate = ServerUtils.parseDate(filterParametrs.getStartDateNC(), filterParametrs.getViewType(), user.getCompany().getTimeZone().getID());
                Integer timeSlotID = null;
                int day = ServerUtils.getDayOfWeek(startDate);
                EdsCourseSchedule courseSchedule = scheduledCourseManager.get(filterParametrs.getScheduledCourseID());
                EdsLocation location = courseSchedule != null && courseSchedule.getLocation() != null ? courseSchedule.getLocation() : null;
                timeSlotID = location != null ? location.getTimeSlotID() : null;
                timeSlotID = timeSlotID == null ? user.getCompany().getDefaultTimeSlot().getObjectID() : timeSlotID;
                Integer minutes = timeSlotItemManager.getStartMinutesByDay(day, timeSlotID);
                if (!filterParametrs.isActualStart()) {
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    startDate.setMinutes(startDate.getMinutes() + minutes);
                }
            }
        } catch (Exception e) {
        }
        ScheduledCourseItem scheduleCourseItem = tcService.getCourseSchedule(filterParametrs.getScheduledCourseID(), true);
        CourseItem course = tcService.getCourseItem(scheduleCourseItem.getCourseID());

        //table 1
        ITextTableList courseDetails = new ITextTableList(2);
        courseDetails.setName("Scheduled course details");
        courseDetails.setTotalWidth(200);
        courseDetails.setBorderWidth(0);
        courseDetails.addPdfTableRows("Number :", scheduleCourseItem.getNumber());
        courseDetails.addPdfTableRows("Location :", scheduleCourseItem.getLocationName());
        courseDetails.addPdfTableRows("Course :", scheduleCourseItem.getCourseName());
        courseDetails.addPdfTableRows("Language :", scheduleCourseItem.getLanguageName());
        courseDetails.addPdfTableRows("Instructor :", scheduleCourseItem.getInstructorName());
        courseDetails.addPdfTableRows("Start Date :", longDateFormat(scheduleCourseItem.getStartDate()));
        courseDetails.addPdfTableRows("End Date :", longDateFormat(scheduleCourseItem.getEndDate()));

        pdfData.addTable(courseDetails);

        List<StudentItem> studentListForPDF = tcService.getStudentListForCSV(filterParametrs);
        Integer clickerID = 1;
        if (studentListForPDF != null) {
            List<String> header = new ArrayList<>();
            header.add(commonLocalizer.localize(PdfLocalizationName.clickerID));
            header.add(commonLocalizer.localize(PdfLocalizationName.firstName));
            header.add(commonLocalizer.localize(PdfLocalizationName.lastName));
            header.add(commonLocalizer.localize(PdfLocalizationName.residenceNumber));
            header.add(commonLocalizer.localize(PdfLocalizationName.companyEmployeeNumber));
            header.add(commonLocalizer.localize(PdfLocalizationName.refIndNumber));
            header.add(commonLocalizer.localize(PdfLocalizationName.studentID));
            header.add(commonLocalizer.localize(PdfLocalizationName.companyName));
            header.add(commonLocalizer.localize(PdfLocalizationName.phone));
            header.add(commonLocalizer.localize(PdfLocalizationName.email));
//            header.add(commonLocalizer.localize(PdfLocalizationName.gender));
            header.add(commonLocalizer.localize(PdfLocalizationName.DOB));
            header.add(commonLocalizer.localize(PdfLocalizationName.nationality));
            header.add(commonLocalizer.localize(PdfLocalizationName.signature));
            header.add(commonLocalizer.localize(PdfLocalizationName.examStatus));


            //table 2
            String roomNumber = null;
            if (scheduleCourseItem.getReservations() != null && scheduleCourseItem.getReservations().length > 0) {
                for (ScheduledCourseReservation reserve : scheduleCourseItem.getReservations()) {
                    if ("Room".equals(reserve.getItemCategory())) {
                        roomNumber = reserve.getItem();
                    }
                }
            }
            ITextTableList studentList = new ITextTableList(header.size());
            studentList.addPdfTableHeader(header.toArray(new String[0]));

            String startDateShort =  longDateFormat(scheduleCourseItem.getStartDate());
            String endDateShort = longDateFormat(scheduleCourseItem.getEndDate());

            studentList.setNamePositionCenter(scheduleCourseItem.getNumber() + " " + course.getNumberData().getNumberString()
                    + " - " + scheduleCourseItem.getLocationName() + " - " + scheduleCourseItem.getLanguageName()
                    + (scheduleCourseItem.getInstructorID() != null ? " - " + scheduleCourseItem.getInstructorName() : "")
                    + (roomNumber != null ? " - " + roomNumber : "") + " " + startDateShort + " - " + endDateShort);

            int dayCount = 0;
            if (startDate != null) {
               dayCount = DateUtil.countDays(scheduleCourseItem.getStartDate(), startDate);
            }
             studentList.setNamePositionRight("Day " + dayCount + " - " + (startDate != null ? longDateFormat(startDate, true) : longDateFormat(scheduleCourseItem.getStartDate(), false)));

            for (StudentItem item : studentListForPDF) {
                List<String> itemList = new ArrayList<>();
                itemList.add(clickerID.toString());
                itemList.add(item.getFirstName());
                itemList.add(item.getLastName());
                itemList.add(item.getSafetyPPNumber()); //residence#
                itemList.add(item.getCompEmpNum());
                itemList.add(item.getRefIndNumber());
                itemList.add(item.getObjectId() != null ? item.getObjectId().toString() : "N/A");
                itemList.add(item.getCompany());
                itemList.add(ServerUtils.refactorPhone(item.getPrimaryPhone()));
                itemList.add(item.getPrimaryEmail());
//                itemList.add(ServerUtils.refactorNA(item.getGender()));
                String birthday = item.getBirthDate() != null && item.getBirthDate().getNonConvertedDate() != null ? ServerUtils.dateFormat(item.getBirthDate().getNonConvertedDate(), "dd/MM/yyyy") : "";
                itemList.add(birthday);

                itemList.add(item.getNationality());
                itemList.add(null);
                itemList.add(item.getExamStatus());

                studentList.addPdfTableRows(itemList.toArray(new String[0]));
                clickerID++;
            }
            //pdfData.addTable(studentList);
            pdf.setListTable(studentList);
            pdf.getListTable().addTableWidthPercentage(10, 15, 15, 10, 10, 10, 10, 15, 15, 20, 15, 15, 30, 15);
        }
        return pdf;
    }

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        return new Document(PageSize.A4.rotate(), 20, 20, 110, 30);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ScheduledCourseItem scheduleCourseItem = tcService.getCourseSchedule(filterParametrs.getScheduledCourseID(), true);
        String firstName = user.getFirstName() != null ? user.getFirstName().replace(" ", "") : "";
        String lastName = user.getLastName() != null ? user.getLastName().replace(" ", "") : "";
        String date = ServerUtils.dateFormat(user.getUserDate(), "MM_dd_yyyy");
        String userName = scheduleCourseItem.getInstructorName() != null ? scheduleCourseItem.getInstructorName() : (firstName + "_" + lastName);
        setFileName(scheduleCourseItem.getNumber() + "_" + userName + "_" + date);
    }
}
