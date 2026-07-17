package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInvoiceGeneratorSchedule;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilter;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.InstructorScheduledCourseItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ScheduledCourseManager extends Manager<EdsCourseSchedule> {

    List<EdsCourseSchedule> list(ListingFilterParameter fp);

    List<EdsCourseSchedule> getConfirmedScheduledCourseList(ListingFilterParameter fp);

    List<EdsCourseSchedule> listForInstructorReassign(ListingFilterParameter fp);

    Integer getCountOfScheduledCourse(ListingFilterParameter fp);

    Integer getCountOfConfirmedScheduledCourse(ListingFilterParameter fp);

    Integer getCountOfInstructorReassign(ListingFilterParameter filterParameter);

    void deleteScheduledCourseInstructors(Integer scheduledCourseID);

    List<InstructorScheduledCourseItem> getScheduledCourseInstructors(Integer scheduledCourseID);

    List<EdsEmployee> getInstructors(ListingFilterParameter fp);

    List<EdsEmployee> getAvailableInstructors(ListingFilterParameter fp);

    List<EdsCourseSchedule> getAvailableCourseSchedule(ListingFilterParameter fp);

    boolean hasAvailableCourseSchedule(ListingFilterParameter fp);

    void deleteScheduledCourseReservations(Integer objectID);

    List<Object[]> getCourseListByLocation(Integer locationId, Date nowDate);

    List<Object[]> getCourseScheduleBooking(Integer locationId, String courseIds, String languageIds, Date nowDate);

    List<Object[]> getStudentAttendedCourseSchedule(Integer studentId, Integer locationId, Date nowDate);

    EdsCourseSchedule getScheduleByDate(Integer courseID, Date scheduleStartDate);

    List<Integer> getNotExpiredCourseSchedulesByCourses(List<Integer> courseIDs);

    ArrayList<Date> getClonedDateList(EdsCourseSchedule courseSchedule);

    Integer getCourseScheduleDroppableStudentCount(Integer schCourseId);

    EdsCourseSchedule getScheduledCourseByNumber(String number);

    List<EdsCourseSchedule> getCourseScheduleItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getCompanyDeletedCourseScheduleListForSolr(SolrReindexRpc solrReindex);

    List<Integer> getCourseScheduleIDsByIDs(String IDs);

    List<Integer> getCourseScheduleIdsWithLimit(Integer startat, Integer limit);

    List<Object[]> getCourseScheduleForInvoiceByPeriod(ListingFilterParameter fp);

    List<Object[]> getInvoicedScheduleCourseList(ListingFilterParameter fp);

    boolean hasInvoice(Integer objectID);

    Integer countOfNotAddressedStudents(Integer objectID);

    Integer getCountOfSchedulesByCourse(EdsCourse courseId);

    Integer getScheduleLasNumber();

    List<EdsCourseSchedule> getInstructorCourseSchedules(Integer instructorID);

    EdsInvoiceGeneratorSchedule getInvoiceGeneratorSchedule(Date startDate, Date endDate);

    EdsInvoiceGeneratorSchedule getInvoiceGeneratorSchedule(Integer objectID);

    void updateScheduledCoursesInvoice(ArrayList<String> sCIdList, Integer invoiceID);

    List<EdsCourseSchedule> getScheduledCourses(CalendarFilter filter);

    EdsCourseSchedule getFirstOrLastCourseScheduleInRecurringSeries(Integer recurrenceID, boolean b);

    EdsCourseSchedule getScheduleInstance(Integer recurrenceID, Date fireTime);

    void removeRecurrenceFromScheduleCourse(Integer recurrenceID, Integer companyID);


}
