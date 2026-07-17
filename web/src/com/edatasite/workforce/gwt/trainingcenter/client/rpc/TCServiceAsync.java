package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
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
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface TCServiceAsync {

    void saveStudent(StudentItem studentItem, AsyncCallback<Integer> callback);

    void saveGymStudentItem(StudentItem studentItem, AsyncCallback<Integer> callback);

    void getStudentItem(Integer studentID, AsyncCallback<StudentItem> callback);

    void getStudentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<StudentItem>> callback);

    void getStudentListForCSV(ListingFilterParameter filterParameter, AsyncCallback<List<StudentItem>> callback);

    void deleteStudent(Integer studentID, AsyncCallback<Boolean> callback);

    void deleteStudentCourseScheduledStudents(Integer scheduledCourseID, Integer studentID, AsyncCallback<Boolean> callback);

    void saveCourse(CourseItem courseItem, AsyncCallback<Void> callback);

    void saveTrainingContract(TrainingContractItem courseItem, AsyncCallback<Integer> callback);

    void getCourseItem(Integer objectID, AsyncCallback<CourseItem> callback);

    void getContractItem(Integer objectID, AsyncCallback<TrainingContractItem> callback);

    void getCourseList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CourseItem>> callback);

    void getTreningContractsList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<TrainingContractItem>> callback);

    void deleteCourse(Integer objectID, AsyncCallback<Boolean> callback);

    void deleteTreningContracts(Integer objectID, AsyncCallback<Void> callback);

    void getInstructors(AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    void getCourseScheduleList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ScheduledCourseItem>> async);

    void getCourseScheduleFromSolr(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ScheduledCourseItem>> async);

    void deleteCourseSchedule(Integer objectID, AsyncCallback<Boolean> async);

    void getCourseSchedule(Integer objectID, boolean isViewForm, AsyncCallback<ScheduledCourseItem> async);

    void saveCourseSchedule(ScheduledCourseItem scheduledCourseItem, AsyncCallback<Integer> async);

    void getCourseInstructors(Integer courseID, AsyncCallback<SelectItem[]> async);

    void getInstructorScheduledCoursesByDate(Date date, Integer instructorId, Integer locationId, AsyncCallback<ScheduledCourseItem[]> async);

    void getCourseStudents(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    void getInstructorList(AsyncCallback<SelectItem[]> async);

    void getInstructorReassignCourseList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ScheduledCourseItem>> async);

    void getInstructorAndStudents(Date date, Integer courseScheduleId, AsyncCallback<InstructorStudentItem> async);

    void getScheduledCourseInstructors(Integer scheduledCourseID, AsyncCallback<ArrayList<InstructorScheduledCourseItem>> async);

    void updateScheduledCourseInstructors(InstructorScheduledCourseItem[] instructors, AsyncCallback<Void> async);

    void saveAttendanceSheet(InstructorStudentItem instructorStudentItem, AsyncCallback<Void> async);

    void getScheduledCourseStudents(ListingFilterParameter fp, AsyncCallback<ListResult<StudentItem>> async);

    void getSheduleCourseInstructorsByDate(Integer locationId, Date date, AsyncCallback<SelectItem[]> async);

    void getCustomerData(Integer customerID, AsyncCallback<CrmAccountItem> asyncCallback);

    void getContactData(Integer contactID, AsyncCallback<ClientContact> asyncCallback);

    void getCourseBookingAddEditData(Integer objectID, AsyncCallback<AddEditCourseBookingItem> asyncCallback);

    void getAvailabilityData(ScheduledCourseItem scheduledCourseItem, AsyncCallback<ScheduledCourseItem> async);

    void getAvailabilityData(Integer scheduledCourseID, AsyncCallback<ScheduledCourseItem> async);

    void getCourseListByCourseBooking(Integer locationId, AsyncCallback<CourseBookingItem> asyncCallback);

    void saveCourseBooking(CourseBookingItem courseBookingData, AsyncCallback<Integer[]> callback);

    void getCourseBookingItem(Integer courseBookingObjectID, AsyncCallback<CourseBookingItem> callback);

    void getCourseBoookigList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CourseBookingItem>> asyncCallback);

    void deleteCourseBooking(Integer objectID, AsyncCallback<Void> callback);

    void cancelGymStudentCourseBooking(Integer studentId, Integer scheduleCourseId, AsyncCallback<Boolean> callback);

    void getLookUpItems(ListingFilterParameter filterParametrs, Integer type, AsyncCallback<SelectItem[]> callback);

    void setTemporaryLock(Integer courseBookingID, CourseScheduleListItem lockItem, boolean prePaid, AsyncCallback<CourseScheduleListItem> async);

    void expireTemporaryLock(Integer bookingID, AsyncCallback<Void> async);

    void expireTemporaryLock(Integer bookingID, String itemUUID, AsyncCallback<Void> async);

    void findStudentByResidenneNum(String residenceNum, Integer courseBookingID, Integer locationID, AsyncCallback<StudentItem> asyncCallback);

    void saveCourseBookingAttendedStudents(CourseBookingItem courseBookingItem, AsyncCallback<CourseBookingItem> callback);

    void saveGymCourseBookingAttendedStudents(CourseBookingItem courseBookingItem, AsyncCallback<CourseBookingItem> callback);

    void getBookingStudentItems(Integer objectID, AsyncCallback<CourseBookingItem> callback);

    void getCertificateList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CertificateData>> callback);

    void saveCertificateData(CertificateData certificateData, AsyncCallback<Integer> callback);

    void getCertificateData(Integer certificateID, boolean fullData, AsyncCallback<CertificateData> callback);

    void getCertificateTypeTemplateData(Integer certificateTypeID, AsyncCallback<CertificateTypeData> callback);

    void updateCourseBookingStatus(Integer bookingID, String status, AsyncCallback<Void> async);

    void getCustomerByRegistrationNumber(String regisstrationNumber, AsyncCallback<CrmAccountItem> async);

    void findStudentByCompanyEmployeeNumber(String companyEmpNum, Integer courseBookingID, Integer locationID, AsyncCallback<StudentItem> asyncCallback);

    void validateStudentEmailToExisting(String email, Integer courseBookingID, AsyncCallback<Boolean> asyncCallback);

    void setCloneOfCourseSchedule(ScheduledCourseItem scheduledCourseItem, AsyncCallback<Integer> async);

    void studentReschedule(Integer studentID, Integer fsID, Integer tsID, AsyncCallback<Boolean> async);

    void studentDropOff(Integer courseScheduleID, AsyncCallback<Boolean> async);

    void getAvailableScheduleCourseDates(Integer objectID, AsyncCallback<ScheduledCourseItem[]> async);

    void getConfirmedScheduledCourseList(ListingFilterParameter fp, AsyncCallback<ListResult<ScheduledCourseItem>> async);

    void saveCSCEditCellValue(ScheduledCourseItem rowValue, String columnCodeName, AsyncCallback<Void> async);

    void getAssessmentList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<AssessmentItem>> async);

    void getAssessment(Integer objectID, Integer stdQuestionarieID, AsyncCallback<AssessmentItem> async);

    void importStudent(Integer objectId, Integer attachmentId, AsyncCallback<String> asyncCallback);

    void getAttendStudentStatus(AsyncCallback<SelectItem[]> callback);

    void saveAttendStudentEditCellValue(StudentItem rowValue, Integer scheduledCourseID, String columnCodeName, AsyncCallback<Void> callback);

    void deleteCertificate(Integer objectID, AsyncCallback<Boolean> asyncCallback);

    void unAssignInstructorFromScheduledCourse(Integer scheduledCourseID, AsyncCallback<Boolean> async);

    void getAssessorList(AsyncCallback<SelectItem[]> async);

    void getTCScheduleData(DateNonConvertable startDate, DateNonConvertable endDate, ListingFilterParameter filterParameter, AsyncCallback<TCScheduleData> callback);

    void saveTCScheduleData(TCScheduleData scheduleData, AsyncCallback<Integer> callback);

    void executeScheduledTasker(AsyncCallback<Void> callback);

    void getCourseBookingListFromSolr(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CourseBookingItem>> async);

    void deleteCourseBookingByIds(Integer[] objectIDs, AsyncCallback<Void> async);

    void deleteCourseScheduleByIds(Integer[] courseScheduleIds, AsyncCallback<Void> async);

    void getAttendStatusList(AsyncCallback<SelectItem[]> async);

    void getCourseScheduleStatusList(AsyncCallback<SelectItem[]> async);

    void generateInvoices(ListingFilterParameter fp, AsyncCallback<Void> async);

    void getTimeSlotItem(Integer locationID, AsyncCallback<HashMap<Integer, TimeSlotItem>> async);

    void isExistStudentWithResidenceNumber(Integer objectID, String residenceNumber, Integer customerID, AsyncCallback<Boolean> async);

    void getStudentCustomerListAsSelectItem(AsyncCallback<SelectItem[]> async);

    void getStudentListForMerge(Integer[] studentIds, AsyncCallback<ArrayList<StudentItem>> async);

    void mergeStudents(StudentItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs, AsyncCallback<Boolean> async);

    void reGenerateInvoices(ListingFilterParameter fp, AsyncCallback<Void> async);

    void getCourseSubject(Integer objectId, AsyncCallback<CourseSubjectItem> async);

    void saveCourseSubject(CourseSubjectItem subjectItem, AsyncCallback<Integer> async);

    void getCourseSubjectParent(Integer objectId, AsyncCallback<List<SelectItem>> async);

    void getCourseSubjectList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CourseSubjectItem>> async);

    void deleteCourseSubject(Integer objectId, AsyncCallback<Integer> async);

    void getCourseSubjectAsSelectItem(Integer objectId, AsyncCallback<TreeSelectItem[]> asyncCallback);

    void getContractCoursePrices(Integer contractID, AsyncCallback<ContractCoursePriceItem[]> asyncCallback);

    void changeContractCoursePrices(Integer contractID, List<ContractCoursePriceItem> items, AsyncCallback<Void> asyncCallback);

    void updatePrices(Integer contractID, AsyncCallback<ArrayList<ContractCoursePriceItem>> asyncCallback);

    void getPassportsList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PassportData>> asyncCallback);

    void getPassportData(Integer objectID, AsyncCallback<PassportData> asyncCallback);

    void getPassportCourses(Integer studentID, AsyncCallback<CourseItem[]> asyncCallback);

    void savePassport(PassportData passport, boolean isNew, AsyncCallback<Integer> asyncCallback);

    void deletePassport(Integer passportID, AsyncCallback<Boolean> asyncCallback);

    void checkPassportNumber(String numberString, String number, AsyncCallback<Boolean> asyncCallback);

    void checkDayForAvailibility(Integer courseScheduleID, Date startDate, Date endDate, AsyncCallback<Date[]> asyncCallback);

    void checkGeneratorSchedule(DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<Integer> asyncCallback);

//    void reGenerateScheduledCourseTimes(AsyncCallback<Void> async);
    void scheduleGenerateInvoice(Integer scheduleID, AsyncCallback<Void> asyncCallback);

    void scheduleRegenerateInvoice(Integer scheduleID, AsyncCallback<Void> asyncCallback);
    void saveCourseScheduleInstance(Integer scheduleID, AsyncCallback<Void> asyncCallback);
}
