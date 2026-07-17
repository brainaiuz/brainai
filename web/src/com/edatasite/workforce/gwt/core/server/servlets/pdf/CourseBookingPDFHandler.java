package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.trainingcenter.*;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 10/24/12
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CourseBookingPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private CourseBookingManager courseBookingManager;

    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return new ITextGenericPdfData();
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdf.setBaseInvoice(baseInvoice);

        RequestObject requestObject = (RequestObject) dataClass;
        Integer courseBookingID = requestObject.getObjectID();

        //User Data
        EdsUser user = uploadManager.getUser();

        //Company Data
        pdf.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        //Course Booking Data
        EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingID);
        EdsCrmAccount customer = courseBooking.getCustomer();
        EdsCrmContact contact = courseBooking.getContact() != null ? courseBooking.getContact() : customer.getPrimaryContact();
        //Course Booking Customer Data
        CustomisedITextTable customerData = new CustomisedITextTable();
        customerData.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        customerData.addRowWithCode(REF_IND_NUMBER, "Ref.Ind.Number", customer.getRegistrationNumber() != null ? escapeHtml(customer.getRegistrationNumber()) : "");
        customerData.addRowWithCode(COMPANY_NAME, "Company Name", escapeHtml(customer.getName()));
        customerData.addRowWithCode(PHONE_NUMBER, "Phone Number", escapeHtml(ServerUtils.refactorPhone(customer.getPhone())));
        customerData.addRowWithCode(CLIENT_FAX, "Fax", escapeHtml(ServerUtils.refactorPhone(customer.getFax())));
        customerData.addRowWithCode(EMAIL, "Email", customer.getEmail() != null ? escapeHtml(customer.getEmail()) : "");
        customerData.addRowWithCode(LOCATION, "Location", courseBooking.getLocation() != null && courseBooking.getLocation().getCity() != null ? escapeHtml(courseBooking.getLocation().getCity()) : "");
        customerData.addRowWithCode(LOCATION_CODE, "Location Code", courseBooking.getLocation() != null && courseBooking.getLocation().getCity() != null ? escapeHtml(courseBooking.getLocation().getCity().toUpperCase()) : "");
        customerData.addRowWithCode(LOCATION_PHONE, "Location Phone", courseBooking.getLocation() != null && courseBooking.getLocation().getPhone() != null ? escapeHtml(courseBooking.getLocation().getPhone()) : "");
        customerData.addRowWithCode(LOCATION_FAX, "Location Fax", courseBooking.getLocation() != null && courseBooking.getLocation().getFax() != null ? escapeHtml(courseBooking.getLocation().getFax()) : "");
        customerData.addRowWithCode(LOCATION_EMAIL, "Location Email", courseBooking.getLocation() != null && courseBooking.getLocation().getEmail() != null ? escapeHtml(courseBooking.getLocation().getEmail()) : "");
        baseInvoice.setCustomBillToAddress(customerData);

        //Course Booking Client Contact Data
        CustomisedITextTable contactData = new CustomisedITextTable();
        contactData.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (contact != null) {
            contactData.addRowWithCode(NAME, "Name", escapeHtml(contact.getFullName()));
            contactData.addRowWithCode(PHONE_NUMBER, "Phone Number", ServerUtils.refactorPhone(contact.getPrimaryPhone()));
            contactData.addRowWithCode(POSITION, "Position", contact.getPosition() != null ? escapeHtml(contact.getPosition()) : "");
            contactData.addRowWithCode(REF_IND_NUMBER, "Ref.Ind.Number", contact.getRefIndNumber() != null ? escapeHtml(contact.getRefIndNumber()) : "");
        }
        baseInvoice.setCustomNumberAndDatesTable(contactData);

        //Course Booking Nominees Data
        List<EdsCourseScheduleStudent> courseScheduleStudentList = courseScheduleStudentManager.getCourseScheduleStudentByBookingId(courseBookingID);
        if (courseScheduleStudentList != null && courseScheduleStudentList.size() > 0) {

            CustomisedITextTable bookingFormTable = new CustomisedITextTable();
            baseInvoice.setProductTableName("TRAINING COURSE BOOKING FORM");
            bookingFormTable.addColumnOrder(ITEM_NO, ITEM_NAME, REF_IND_NUMBER, RES_CARD_NUMBER, EMAIL, PHONE_NUMBER, COURSE_TITLE, COLUMN_CODE, LANGUAGE, COURSE_DATE);
            bookingFormTable.addHeaderColumns("No.:", "Nominee’s full name", "Company employee no(Ref indicator)", "Civil no(Res card)", "Email address", "Cell number", "Course title", "Course code", "Language", "Course date");
            baseInvoice.setCustomProductTable(bookingFormTable);

            int inc = 1;
            for (EdsCourseScheduleStudent courseScheduleStudent : courseScheduleStudentList) {
                EdsStudent student = courseScheduleStudent.getStudent();
                EdsCourseSchedule courseSchedule = courseScheduleStudent.getCourseScheduleBooking();
                EdsCourse course = courseSchedule.getCourse();

                String no = String.valueOf(inc);
                String name = student.getFullName() != null ? escapeHtml(student.getFullName()) : "";
                String refIndNumber = student.getContact().getRefIndNumber() != null ? escapeHtml(student.getContact().getRefIndNumber()) : "";
                String resNumber = student.getSafetyPPNumber() != null ? escapeHtml(student.getSafetyPPNumber()) : "";
                String email = student.getEmail() != null ? escapeHtml(student.getEmail()) : "";
                String phone = escapeHtml(ServerUtils.refactorPhone(student.getPhone()));
                String courseTitle = course.getName() != null ? escapeHtml(course.getName()) : "";
                String courseCode = course.getNumber() != null ? escapeHtml(course.getNumber()) : "";
                String language = courseSchedule.getLanguage() != null && courseSchedule.getLanguage().getName() != null ? escapeHtml(courseSchedule.getLanguage().getName()) : "";
                Date startDate = new Date(courseSchedule.getStartDate().getTime() + company.getTimeZone().getRawOffset());
                String strStartDate = ServerUtils.dateFormat(startDate, "dd MMM yyyy HH:mm");

                bookingFormTable.addRow(no, name, refIndNumber, resNumber, email, phone, courseTitle, courseCode, language, strStartDate);
                inc++;
            }
        }

        return pdf;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.COURSE_BOOKING;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer courseBookingID = requestObject.getObjectID();
        EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingID);
        setFileName(courseBooking.getNumber() + "_" + courseBooking.getCustomer().getName() + "_" + ServerUtils.dateFormat(user.getUserDate(new Date()), "dd_MMM_yyyy_HH:mm"));
    }
}
