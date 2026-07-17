package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
public interface CourseScheduleStudentManager extends Manager<EdsCourseScheduleStudent> {
    void deleteTemporaryRegistration(Integer bookingID, String itemUUID);

    void deleteTemporaryRegistration(Integer bookingID);

    void deleteByCourseBooking(Integer courseBookingId);

    void deleteCourseAndStudentFromCourseScheduledStudent(Integer scheduledCourseID, Integer studentID);

    void deleteStudentFromCourseScheduledStudent(Integer studentID);

    Integer getCourseScheduleAttendCount(Integer courseScheduleId);

    HashMap<Integer, List<Integer>> getStudentsPassedCourses(List<Integer> courseScheduleIDs);

    Map<Integer, String> getStudentsExamStatus(Integer courseScheduleId, String studentIDs);

    EdsCourseScheduleStudent getCourseScheduleStudent(EdsCourseSchedule edsCourseSchedule, Integer studentId);

    EdsCourseScheduleStudent getCourseScheduleStudentByStudentId(Integer scheduledCourseID, Integer studentId);

    List<EdsCourseScheduleStudent> getCourseScheduleStudentByBookingId(Integer courseBookingID);

    List<EdsCourseScheduleStudent> getRejectedCourseBookingStudentList(Integer courseBookingID);

    List<EdsCourseScheduleStudent> getCourseScheduleStudentByStatus(Integer bookingID, String status);

    List<EdsCourseScheduleStudent> getDroppableStudentList(Integer courseScheduleID);

    boolean isStudentAttended(Integer courseScheduleID);

    List<EdsCourseScheduleStudent> getCourseStudentByBooking(Integer courseBookingID, Integer scheduledCourseID);

    Integer getCourseScheduleStudentCount(Integer courseScheduleID);

    Integer getCourseScheduleConfirmedStudentCount(Integer courseScheduleID);

    List<StudentAsInvoiceItem> getCourseStudentAsInvoiceItems(Integer customerID, List<String> scheduledCourseIds);

    void removeInvoiceFromCourseStudents(Integer invoiceID);

    void removeInvoiceFromTrainingData(Integer invoiceID);

    List<EdsCourseSchedule> getScheduleListByBooking(Integer bookingID);

    List<Object[]> getParentCourseSubjects();

    HashMap<Integer, BigDecimal> getCourseSubjectsConsolidatedData(ListingFilterParameter filterParameter, boolean isPDOCustomer);

    List<Object[]> getCoursePassedStudents(ListingFilterParameter filterParameter);

    List<Object[]> getStudentPassedCourses(Integer studentID);

    List<Object[]> getStudentDetailsForIDCard(Integer certificateTypeID);

    String getCourseScheduleStudentsDetails(Integer csID, String type);

    void updateStudentsInvoice(List<Integer> integers, Integer invoiceID);

    Integer getBookingIdByScheduledCourseId(Integer scheduledCourseId);

    List<EdsCourseSchedule> getScheduleListByStudentId(Integer studentId, String sortAs);
}
