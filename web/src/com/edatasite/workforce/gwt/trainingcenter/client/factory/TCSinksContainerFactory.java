package com.edatasite.workforce.gwt.trainingcenter.client.factory;

import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.history.ContactHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.salequote.SaleQuoteHistoryProcessor;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.location.client.history.ImportTestResultsHistoryProcessor;
import com.edatasite.workforce.gwt.location.client.history.LocationHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.BookingItemsHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.BookingItemsReservationHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsListView;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.history.AssessmentViewHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.CertificateHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.ChangeContractPriceHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.CourseBookingHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.CourseHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.CourseSubjectHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.InstructorHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.PassportHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.ScheduledCourseHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.StudentHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.StudentMergeHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.history.TrainingContractHistoryProcessor;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.assessment.ConfirmedScheduledCourseListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AttendenceSheetView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CertificatesListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseBookingListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseSubjectListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InstructorReassignListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InvoiceGeneratorView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.PassportsListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.StudentListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.TCScheduleView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.TrainingContractListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.ScheduledCourseListView;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Normurod
 * Date: 7/13/12
 * Time: 3:51 PM
 */
public class TCSinksContainerFactory extends SinksContainerFactory implements TCConstants, PermissionConstants {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public TCSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    private SinksContainer operation;
    private SinksContainer assessment;
    private boolean isFirstContener = true;

    @Override
    public void initDefaultContainers() {
//        if (Utils.hasPermission(TC_OPERATION_MENU)) {
//            operation = new TCOperationSinksContainer(TC_OPERATION, tcStrings.operation());
//            operation.setPreparedView(TC_SCHEDULED_COURSE);
//            showPrepairedView(operation, TC_SCHEDULED_COURSE, null, null);
//        }
//
//        if (Utils.hasPermission(TC_ASSESSMENT_MENU)) {
//            assessment = new TCAssessmentSinksContainer(TC_ASSESSMENT, wfmStrings.assessment());
//            assessment.setPreparedView("confirmedscheduledcourses");
//            setSinksContainer(assessment);
//        }
//        if (Utils.hasPermission(TC_SCHEDULE)) {
//            SinksContainer scheduleSinksContainer = new TCScheduleSinksContainer(TCConstants.TC_SCHEDULE_MENU, tcStrings.consolidatedInvoice());
//            scheduleSinksContainer.setPreparedView("invoicegenerator");
//            setSinksContainer(scheduleSinksContainer);
//        }

        if (Utils.getPropertyListingMap() != null && Utils.getPropertyListingMap().size() > 0) {
            setTCPropertyListingsMap(Utils.getPropertyListingMap());
        }
    }

    @Override
    public void registerProcessors() {
        registerHistoryProcessor(TC_SALE_QUOTE, new SaleQuoteHistoryProcessor());
        //students
        registerHistoryProcessor("courseSubject", new CourseSubjectHistoryProcessor());
        registerHistoryProcessor(TC_STUDENTS, new StudentHistoryProcessor());
        registerHistoryProcessor(TC_COURSE, new CourseHistoryProcessor());

        registerHistoryProcessor(TC_SCHEDULED_COURSE, new ScheduledCourseHistoryProcessor());
        registerHistoryProcessor(TC_INSTRUCTOR, new InstructorHistoryProcessor());
        registerHistoryProcessor(TC_COURSE_BOOKING, new CourseBookingHistoryProcessor());
        registerHistoryProcessor(TC_TRAINING_CONTRACT, new TrainingContractHistoryProcessor());
        registerHistoryProcessor(TC_CERTIFICATE, new CertificateHistoryProcessor());
        registerHistoryProcessor(TC_PASSPORT, new PassportHistoryProcessor());
        registerHistoryProcessor(TC_ASSESSMENT_VIEW, new AssessmentViewHistoryProcessor());
        registerHistoryProcessor(TC_CHANGE_CONTRACT_PRICE, new ChangeContractPriceHistoryProcessor());

        registerHistoryProcessor("client", new ClientHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
        registerHistoryProcessor("contact", new ContactHistoryProcessor());
        registerHistoryProcessor("bookingitems", new BookingItemsHistoryProcessor());
        registerHistoryProcessor("bookingitemsreservation", new BookingItemsReservationHistoryProcessor());
        registerHistoryProcessor("location", new LocationHistoryProcessor());
        registerHistoryProcessor("xmlimport", new ImportTestResultsHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor(Constants.MERGE, new StudentMergeHistoryProcessor());
    }

    public void registerMenuItems() {
        if (Utils.hasPermission(PermissionConstants.TC_SCHEDULE_COURSE_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(TC_SCHEDULED_COURSE,tcStrings.courseSchedules()), TC_SCHEDULED_COURSE + "|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.TC_SUBJECT_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode("coursesubject",tcStrings.courseSubject()), "courseSubject|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.TC_COURSE_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(TC_COURSE,wfmStrings.courses()), TC_COURSE + "|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.TC_BOOKING_ITEMS_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode("bookingItemsList",wfmStrings.bookingItems()), "bookingitems|add/add");
        }
        addNewMenuItem(Property.getPluralWithObjectCode("instructorReassignList",wfmStrings.instructors()), "tcInstructor|add/add");
        if (Utils.hasPermission(PermissionConstants.TC_STUDENT_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(TC_STUDENTS,tcStrings.students()), TC_STUDENTS + "|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.TC_NEW_COURSE_BOOKING)) {
            addNewMenuItem(Property.getPluralWithObjectCode(TC_COURSE_BOOKING,tcStrings.courseBooking()), TC_COURSE_BOOKING + "|addCourseBooking/add");
        }
        if (Utils.hasPermission(PermissionConstants.TC_TRAINING_CUSTOMER_CONTRACT_ADD)) {
            addNewMenuItem(Property.getPluralWithObjectCode(TC_TRAINING_CONTRACT,tcStrings.customerContracts()), TC_TRAINING_CONTRACT + "|add/add");
        }
    }

    private void setTCPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {
        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (selectItem.getDescription().contains(ModuleEnum.TRAINING_CENTER.getCode())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case COURSE_SCHEDULES:
                                viewList.add(new ScheduledCourseListView());
                                break;
                            case TC_COURSE:
                                viewList.add(new CourseListView());
                                break;
                            case "coursesubject":
                                viewList.add(new CourseSubjectListView());
                                break;
                            case BOOKINGITEMS_LIST:
                                viewList.add(new BookingItemsListView());
                                break;
                            case TC_EMLOYEE_LIST:
                                viewList.add(new EmployeeListView(EmployeeListView.FROM_TRAINING_CENTER));
                                break;
                            case TCConstants.TC_STUDENTS:
                                viewList.add(new StudentListView(TCConstants.TC_STUDENTS));
                                break;
                            case TCConstants.TC_ATTENDENCE_SHEET:
                                viewList.add(new AttendenceSheetView());
                                break;
                            case "instructorReassignList":
                                viewList.add(new InstructorReassignListView());
                                break;
                            case TC_COURSE_BOOKING:
                                viewList.add(new CourseBookingListView());
                                break;
                            case TC_TRAINING_CONTRACT:
                                viewList.add(new TrainingContractListView());
                                break;
                            case TC_CERTIFICATE:
                                viewList.add(new CertificatesListView());
                                break;
                            case TC_PASSPORT:
                                viewList.add(new PassportsListView());
                                break;
                            case "confirmedscheduledcourses":
                                viewList.add(new ConfirmedScheduledCourseListView());
                                break;
                            case "invoicegenerator":
                                viewList.add(new InvoiceGeneratorView());
                                break;
                            case "scheduleinvoice":
                                viewList.add(new TCScheduleView());
                                break;

                            default:
                                if (propertyItem.isCustom()) {
                                    if (Constants.PAGE.equals(propertyItem.getType())) {
                                        if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                                            viewList.add(new CustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        } else if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(propertyItem.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                                            viewList.add(new AddCustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        }
                                    } else {
                                        viewList.add(new CustomFormItemListView(propertyItem.getfID(), getLocalizedPlural(propertyItem), propertyItem.getFormID()));
                                    }
                                }
                        }
                    }
                }
            }
            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContener) {
                setSelection(dynamicSC);
                isFirstContener = false;
            }
            setSinksContainer(dynamicSC);
        }
    }
    private String getLocalizedPlural(PropertyItem propertyItem) {
        if (propertyItem.getlPlural() != null) {
            switch (Utils.getUserLanguage()) {
                case "en":
                    return propertyItem.getlPlural().getEnglishName();
                case "ar":
                    return propertyItem.getlPlural().getArabicName();
                case "ru":
                    return propertyItem.getlPlural().getRussianName();
                case "uz":
                    return propertyItem.getlPlural().getUzbekName();
            }
        }
        return propertyItem.getPlural();
    }

}
