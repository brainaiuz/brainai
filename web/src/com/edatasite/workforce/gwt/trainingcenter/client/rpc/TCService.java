package com.edatasite.workforce.gwt.trainingcenter.client.rpc;


import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet.InstructorStudentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateTypeData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.AddEditCourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.InstructorScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.TimeSlotItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 9:26 PM
 */
public interface TCService extends RemoteService {

    Integer saveStudent(StudentItem studentItem);

    Integer saveGymStudentItem(StudentItem studentItem);

    StudentItem getStudentItem(Integer studentID);

    ListResult<StudentItem> getStudentList(ListingFilterParameter filterParameter);

    List<StudentItem> getStudentListForCSV(ListingFilterParameter filterParameter);

    ListResult<StudentItem> getScheduledCourseStudents(ListingFilterParameter fp);

    Boolean deleteStudent(Integer studentID);

    Boolean deleteStudentCourseScheduledStudents(Integer scheduledCourseID, Integer studentID);

    void saveCourse(CourseItem courseItem);

    Integer saveTrainingContract(TrainingContractItem courseItem);

    CourseItem getCourseItem(Integer objectID);

    TrainingContractItem getContractItem(Integer objectID);

    ListResult<CourseItem> getCourseList(ListingFilterParameter filterParameter);

    ListResult<TrainingContractItem> getTreningContractsList(ListingFilterParameter filterParameter);

    Boolean deleteCourse(Integer objectID);

    void deleteTreningContracts(Integer objectID);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getInstructors();

    ListResult<ScheduledCourseItem> getCourseScheduleList(ListingFilterParameter filterParameter);

    ListResult<ScheduledCourseItem> getCourseScheduleFromSolr(ListingFilterParameter filterParameter);

    ListResult<ScheduledCourseItem> getConfirmedScheduledCourseList(ListingFilterParameter fp);

    ListResult<ScheduledCourseItem> getInstructorReassignCourseList(ListingFilterParameter filterParameter);

    ScheduledCourseItem getCourseSchedule(Integer objectID, boolean isViewForm);

    AssessmentItem getAssessment(Integer objectID, Integer stdQuestionarieID);

    Integer saveCourseSchedule(ScheduledCourseItem scheduledCourseItem);

    void deleteCourseScheduleByIds(Integer[] courseScheduleIds);

    boolean deleteCourseSchedule(Integer objectID);

    SelectItem[] getCourseInstructors(Integer courseID);

    ScheduledCourseItem[] getInstructorScheduledCoursesByDate(Date date, Integer instructorId, Integer locationId);

    InstructorStudentItem getInstructorAndStudents(Date date, Integer courseScheduleId);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCourseStudents(ListingFilterParameter fp);

    SelectItem[] getInstructorList();

    SelectItem[] getAssessorList();

    ArrayList<InstructorScheduledCourseItem> getScheduledCourseInstructors(Integer scheduledCourseID);

    void updateScheduledCourseInstructors(InstructorScheduledCourseItem[] instructors);

    void saveAttendanceSheet(InstructorStudentItem instructorStudentItem);

    SelectItem[] getSheduleCourseInstructorsByDate(Integer locationId, Date date);

    CrmAccountItem getCustomerData(Integer customerID);

    ClientContact getContactData(Integer contactID);

    AddEditCourseBookingItem getCourseBookingAddEditData(Integer objectID);

    ScheduledCourseItem getAvailabilityData(ScheduledCourseItem scheduledCourseItem);

    ScheduledCourseItem getAvailabilityData(Integer scheduledCourseID);

    Integer setCloneOfCourseSchedule(ScheduledCourseItem scheduledCourseItem);

    CourseBookingItem getCourseListByCourseBooking(Integer locationId);

    Integer[] saveCourseBooking(CourseBookingItem courseBookingData);

    CourseBookingItem getCourseBookingItem(Integer courseBookingObjectID);

    ListResult<CourseBookingItem> getCourseBoookigList(ListingFilterParameter filterParametrs);

    ListResult<CourseBookingItem> getCourseBookingListFromSolr(ListingFilterParameter filterParametrs);

    void deleteCourseBookingByIds(Integer[] objectIDs);

    void deleteCourseBooking(Integer objectID);

    boolean cancelGymStudentCourseBooking(Integer studentId, Integer scheduleCourseId);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, Integer type);

    CourseScheduleListItem setTemporaryLock(Integer courseBookingID, CourseScheduleListItem lockItem, boolean prePaid);

    void expireTemporaryLock(Integer bookingID);

    void expireTemporaryLock(Integer bookingID, String itemUUID);

    boolean isExistStudentWithResidenceNumber(Integer objectID, String residenceNumber, Integer customerID);

    StudentItem findStudentByResidenneNum(String residenceNum, Integer courseBookingID, Integer locationID);

    CourseBookingItem saveCourseBookingAttendedStudents(CourseBookingItem courseBookingItem);

    CourseBookingItem saveGymCourseBookingAttendedStudents(CourseBookingItem courseBookingItem);

    ListResult<CertificateData> getCertificateList(ListingFilterParameter filterParameter);

    Integer saveCertificateData(CertificateData certificateData);

    CertificateData getCertificateData(Integer certificateID, boolean fullData);

    CertificateTypeData getCertificateTypeTemplateData(Integer certificateTypeID);

    CourseBookingItem getBookingStudentItems(Integer courseBookingID);

    void updateCourseBookingStatus(Integer bookingID, String status);

    CrmAccountItem getCustomerByRegistrationNumber(String regisstrationNumber);

    StudentItem findStudentByCompanyEmployeeNumber(String companyEmpNum, Integer courseBookingID, Integer locationID);

    Boolean validateStudentEmailToExisting(String email, Integer courseBookingID);

    boolean studentReschedule(Integer studentID, Integer fsID, Integer tsID);

    boolean studentDropOff(Integer courseScheduleID);

    ScheduledCourseItem[] getAvailableScheduleCourseDates(Integer objectID);

    void saveCSCEditCellValue(ScheduledCourseItem rowValue, String columnCodeName);

//    void reGenerateScheduledCourseEvents();

//    void reGenerateScheduledCourseTimes();

    ListResult<AssessmentItem> getAssessmentList(ListingFilterParameter filterParametrs);

    String importStudent(Integer objectId, Integer attachmentId);

    SelectItem[] getAttendStudentStatus();

    SelectItem[] getAttendStatusList();

    SelectItem[] getCourseScheduleStatusList();

    void saveAttendStudentEditCellValue(StudentItem rowValue, Integer scheduledCourseID, String columnCodeName);

    Boolean deleteCertificate(Integer objectID);

    Boolean unAssignInstructorFromScheduledCourse(Integer scheduledCourseID);

    TCScheduleData getTCScheduleData(DateNonConvertable startDate, DateNonConvertable endDate, ListingFilterParameter filterParameter);

    Integer saveTCScheduleData(TCScheduleData scheduleData);

    void generateInvoices(ListingFilterParameter fp);

    void reGenerateInvoices(ListingFilterParameter fp);

    void executeScheduledTasker();

    HashMap<Integer, TimeSlotItem> getTimeSlotItem(Integer locationID);

    SelectItem[] getStudentCustomerListAsSelectItem();

    ArrayList<StudentItem> getStudentListForMerge(Integer[] studentIds);

    Boolean mergeStudents(StudentItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs);

    CourseSubjectItem getCourseSubject(Integer objectId);

    Integer saveCourseSubject(CourseSubjectItem courseSubjectItem);

    List<SelectItem> getCourseSubjectParent(Integer objectId);

    ListResult<CourseSubjectItem> getCourseSubjectList(ListingFilterParameter filterParameter);

    Integer deleteCourseSubject(Integer objectId);

    TreeSelectItem[] getCourseSubjectAsSelectItem(Integer objectId);

    ContractCoursePriceItem[] getContractCoursePrices(Integer contractID);

    void changeContractCoursePrices(Integer contractID, List<ContractCoursePriceItem> items);

    ArrayList<ContractCoursePriceItem> updatePrices(Integer contractID);

    ListResult<PassportData> getPassportsList(ListingFilterParameter filterParameter);

    PassportData getPassportData(Integer objectID);

    CourseItem[] getPassportCourses(Integer studentID);

    Integer savePassport(PassportData passport, boolean isNew);

    Boolean deletePassport(Integer passportID);

    boolean checkPassportNumber(String numberString, String number);

    Date[] checkDayForAvailibility(Integer courseScheduleID, Date startDate, Date endDate);

    Integer checkGeneratorSchedule(DateNonConvertable startDate, DateNonConvertable endDate);

    void scheduleGenerateInvoice(Integer scheduleID);

    void scheduleRegenerateInvoice(Integer scheduleID);
    void saveCourseScheduleInstance(Integer scheduleID);

    class App {
        public static TCServiceAsync get() {
            ServiceDefTarget target = GWT.create(TCService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/trainingcenter");
            return (TCServiceAsync) target;
        }
    }
}