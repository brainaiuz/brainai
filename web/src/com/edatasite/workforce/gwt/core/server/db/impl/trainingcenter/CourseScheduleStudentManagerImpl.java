package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 20:26
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseScheduleStudentManager")
public class CourseScheduleStudentManagerImpl extends BaseManager<EdsCourseScheduleStudent> implements CourseScheduleStudentManager {
    public CourseScheduleStudentManagerImpl() {
        super(EdsCourseScheduleStudent.class);
    }

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    @Autowired
    private ReferenceManager referenceManager;

    @Override
    public void deleteTemporaryRegistration(Integer bookingID, String itemUUID) {
        update("DELETE FROM EdsCourseScheduleStudent css WHERE css.courseBooking.objectID = ? AND css.itemUUID = ?", bookingID, itemUUID);
    }

    @Override
    public void deleteTemporaryRegistration(Integer bookingID) {
        update("DELETE FROM EdsCourseScheduleStudent css WHERE css.courseBooking.objectID = ? ", bookingID);
    }

    @Override
    public void deleteByCourseBooking(Integer courseBookingId) {
        EdsReference reject = referenceManager.findReference(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PARENT_STATUS, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED);
        updateNative("UPDATE " + getCompanyId() + ".courseschedulestudent SET stutus_id = " + reject.getObjectID() + " WHERE coursebooking_id = " + courseBookingId);
    }

    @Override
    public void deleteCourseAndStudentFromCourseScheduledStudent(Integer scheduledCourseID, Integer studentID) {
        update("DELETE FROM EdsCourseScheduleStudent css WHERE css.courseScheduleBooking.objectID = ? AND css.student.objectID= ?  ", scheduledCourseID, studentID);
    }

    @Override
    public void deleteStudentFromCourseScheduledStudent(Integer studentID) {
        update("DELETE FROM EdsCourseScheduleStudent css WHERE css.student.objectID= ?  ", studentID);
    }

    @Override
    public Integer getCourseScheduleAttendCount(Integer courseScheduleId) {
        return ((Long) findSingle("SELECT count(objectID) FROM EdsCourseScheduleStudent WHERE courseScheduleBooking.objectID=? and status.code != ?", courseScheduleId, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED)).intValue();
    }

    @Override
    public HashMap<Integer, List<Integer>> getStudentsPassedCourses(List<Integer> courseScheduleIDs) {
        List<Object[]> passedCourses = find("select csst.student.objectID, csst.courseScheduleBooking.course.objectID from EdsCourseScheduleStudent csst " +
                        " where csst.courseScheduleBooking.objectID IN (" + ServerUtils.getAsCommoDelimited(courseScheduleIDs, "0") + ") and csst.examStatus.code = ?",
                TCConstants.STUDENT_COURSE_SCHEDULE_EXAM_PASSED);
        HashMap<Integer, List<Integer>> studentCoursesMap = new HashMap<>();
        for (Object[] studentCourse : passedCourses) {
            Integer studentID = (Integer) studentCourse[0];
            Integer courseID = (Integer) studentCourse[1];
            if (studentCoursesMap.containsKey(studentID)) {
                studentCoursesMap.get(studentID).add(courseID);
            } else {
                List<Integer> courses = new LinkedList<>();
                courses.add(courseID);
                studentCoursesMap.put(studentID, courses);
            }
        }

        return studentCoursesMap;
    }

    @Override
    public Map<Integer, String> getStudentsExamStatus(Integer courseScheduleId, String studentIDs) {
        List<Object[]> objectsList = find("SELECT cschs.student.objectID, es.code FROM EdsCourseScheduleStudent cschs LEFT JOIN cschs.examStatus es WHERE cschs.courseScheduleBooking.objectID=? AND cschs.student.objectID IN " + studentIDs, courseScheduleId);
        Map<Integer, String> studentExamStatusMap = new HashMap<>();
        for (Object[] objects : objectsList) {
            Integer studentId = (Integer) objects[0];
            String studentExamStatusCode = (String) objects[1];
            studentExamStatusMap.put(studentId, studentExamStatusCode);
        }
        return studentExamStatusMap;
    }

    @Override
    public EdsCourseScheduleStudent getCourseScheduleStudent(EdsCourseSchedule edsCourseSchedule, Integer studentId) {
        return (EdsCourseScheduleStudent) findSingle("SELECT cschs FROM EdsCourseScheduleStudent cschs WHERE cschs.courseScheduleBooking=? AND cschs.student.objectID=? and cschs.status.code != ?", edsCourseSchedule, studentId, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED);
    }

    @Override
    public EdsCourseScheduleStudent getCourseScheduleStudentByStudentId(Integer scheduledCourseID, Integer studentId) {
        return (EdsCourseScheduleStudent) findSingle("SELECT cschs FROM EdsCourseScheduleStudent cschs WHERE  cschs.courseScheduleBooking.objectID=? AND cschs.student.objectID=?", scheduledCourseID, studentId);
    }

    @Override
    public List<EdsCourseScheduleStudent> getCourseScheduleStudentByBookingId(Integer courseBookingID) {
        return find("SELECT cs FROM EdsCourseScheduleStudent cs LEFT JOIN cs.status ref  WHERE cs.courseBooking.objectID=? AND cs.student IS NOT NULL AND ref.code !='" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED + "'", courseBookingID);
    }

    @Override
    public List<EdsCourseScheduleStudent> getRejectedCourseBookingStudentList(Integer courseBookingID) {
        return find("SELECT cs FROM EdsCourseScheduleStudent cs LEFT JOIN cs.status ref  WHERE cs.courseBooking.objectID=? AND cs.student IS NOT NULL ", courseBookingID);
    }

    @Override
    public List<EdsCourseScheduleStudent> getCourseScheduleStudentByStatus(Integer bookingID, String status) {
        return find("SELECT css FROM EdsCourseScheduleStudent css LEFT JOIN css.courseBooking cb LEFT JOIN css.status s WHERE cb.objectID = ? AND s.code = ? ", bookingID, status);
    }

    @Override
    public List<EdsCourseScheduleStudent> getDroppableStudentList(Integer courseScheduleID) {
        return find("SELECT css FROM EdsCourseScheduleStudent css INNER JOIN css.courseScheduleBooking cs INNER JOIN css.status s WHERE css.droppable is true AND s.code = ? AND cs.objectID = ?", EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED, courseScheduleID);
    }

    @Override
    public boolean isStudentAttended(Integer courseScheduleID) {
        List attendedStudentList = find("SELECT sat FROM EdsStudentAttended sat join sat.instructorScheduledCourse isc join isc.courseSchedule cs WHERE cs.objectID = ?", courseScheduleID);

        return attendedStudentList != null && attendedStudentList.size() > 0;
    }

    @Override
    public List<EdsCourseScheduleStudent> getCourseStudentByBooking(Integer courseBookingID, Integer scheduledCourseID) {
        return find("SELECT css FROM EdsCourseScheduleStudent css join css.courseBooking cb join css.courseScheduleBooking cs join css.status s WHERE css.student IS NOT NULL AND s.code != '" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED + "' AND cb.objectID = ? AND cs.objectID = ?", courseBookingID, scheduledCourseID);
    }

    @Override
    public Integer getCourseScheduleStudentCount(Integer courseScheduleID) {
        return ((Long) findSingle("SELECT count(objectID) FROM EdsCourseScheduleStudent css WHERE css.student is not null and (css.student.contact is not null and css.student.contact.deleted is not true) and css.courseScheduleBooking.objectID = ? and css.status.code != ?", courseScheduleID, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED)).intValue();
    }

    @Override
    public Integer getCourseScheduleConfirmedStudentCount(Integer courseScheduleID) {
        return ((Long) findSingle("SELECT count(objectID) FROM EdsCourseScheduleStudent css WHERE css.student is not null and css.courseScheduleBooking.objectID = ? and css.status.code = ?", courseScheduleID, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED)).intValue();
    }

    @Override
    public List<StudentAsInvoiceItem> getCourseStudentAsInvoiceItems(Integer customerID, List<String> scheduledCourseIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  css.id as objectID, s.id as studentID, con.firstName, con.lastname, " +
                "sc.id as courseScheduleID, sc.number as courseScheduleNumber, " +
                "c.id as courseID, c.number as courseCode, " +
                "cb.id as courseBookingID, cb.number as courseBookingNumber, css.price as price, css.stopfee as stopFee from ")
                .append(getCompanyId()).append(".courseschedulestudent css ");
        sql.append("inner join ").append(getCompanyId()).append(".student s on s.id = css.student_id ");
        sql.append("inner join ").append(getCompanyId()).append(".crmcontact con on con.id = s.contact_id ");
        sql.append("inner join ").append(getCompanyId()).append(".scheduledcourse sc on sc.id = css.courseschedule_id ");
        sql.append("inner join ").append(getCompanyId()).append(".course c on c.id = sc.course_id ");
        sql.append("inner join ").append(getCompanyId()).append(".coursebooking cb on cb.id = css.coursebooking_id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference ss on ss.id = css.stutus_id ");
        sql.append("where ss.code = '_STUDENT_COURSE_SCHEDULE_ATTENDED' and css.attended_status_id is not null and css.invoiceid is null and cb.invoiceid is null ");
        sql.append("and cb.customer_id = ").append(customerID).append(" and sc.id in ('").append(ServerUtils.getAsCommoDelimited(scheduledCourseIds, "0", "','")).append("') ");
        sql.append("order by sc.startdate, con.firstname, con.lastname ");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(StudentAsInvoiceItem.class));
    }

    @Override
    public void removeInvoiceFromCourseStudents(Integer invoiceID) {
        update("UPDATE EdsCourseScheduleStudent SET invoiceID = null WHERE invoiceID = ?", invoiceID);
    }

    @Override
    public void removeInvoiceFromTrainingData(Integer invoiceID) {
        //clear invoice from scheduled course student
        removeInvoiceFromCourseStudents(invoiceID);

        //clear invoice from scheduled course
        updateNative("DELETE FROM " + getCompanyId() + ".courseschedule_invoice WHERE invoice_id = " + invoiceID);
    }

    @Override
    public List<EdsCourseSchedule> getScheduleListByBooking(Integer bookingID) {
        return find("SELECT cs FROM EdsCourseScheduleStudent css join css.courseBooking cb join css.courseScheduleBooking cs WHERE cb.objectID = ?", bookingID);
    }

    public List<Object[]> getParentCourseSubjects() {
        String companyID = getCompanyId();
        return findNative("select id, name, c_group from " + companyID + ".coursesubject where parentId is null and (deleted is null or deleted<>true) order by sorder");
    }

    @Override
    public HashMap<Integer, BigDecimal> getCourseSubjectsConsolidatedData(ListingFilterParameter filterParameter, boolean isPDOCustomer) {
        String companyID = getCompanyId();

        List<Integer> customerIDs = findNative("select cus.id from " + companyID + ".crmaccount cus where " + ServerUtils.checkForDeleted("cus.deleted") +
                " and (cus.parent_id=" + filterParameter.getCrmAccountId().toString() + " or cus.id=" + filterParameter.getCrmAccountId().toString() + ")");

        StringBuilder query = new StringBuilder();
        query.append("select distinct css.id, subj.id subj_id, subj.parentId, css.price, css.stopfee, cb.location_id  from " + companyID + ".courseschedulestudent css ");
        query.append("inner join " + companyID + ".coursebooking cb on cb.id=css.coursebooking_id ");
        query.append("inner join " + companyID + ".scheduledcourse cs on cs.id=css.courseschedule_id ");
        query.append("inner join " + companyID + ".course crs on crs.id=cs.course_id ");
        query.append("inner join " + companyID + ".coursesubject subj on subj.id=crs.subjectId ");
        query.append("inner join " + companyID + ".reference ss on ss.id = css.stutus_id ");
        query.append("left join " + companyID + ".crmaccount acc on acc.id=cb.customer_id ");
        query.append("left join " + companyID + ".reference st on st.id=css.attended_status_id ");
        query.append("where css.invoiceid is not null and cb.customer_id in (" + ServerUtils.getAsCommoDelimited(customerIDs, "0") + ") and cs.startDate between ? and ? ");
        query.append(" AND ss.code != '_STUDENT_COURSE_SCHEDULE_REJECTED' ");
        query.append(" AND (st.code = '" + Constants.STUDENT_ATTENDED + "' OR acc.payForNoShows is true) ");

        List<Object[]> dataList = (List<Object[]>) findNative(query.toString(), filterParameter.getStartDate(), filterParameter.getEndDate());

        HashMap<Integer, BigDecimal> dataMap = new HashMap<>();
        if (isPDOCustomer) {
            for (Object[] data : dataList) {
                BigDecimal price = ((BigDecimal) data[3]).add((BigDecimal) data[4]);
                Integer subjectID = data[2] != null ? ((Integer) data[2]) : ((Integer) data[1]);
                if (dataMap.containsKey(subjectID)) {
                    dataMap.put(subjectID, dataMap.get(subjectID).add(price));
                } else {
                    dataMap.put(subjectID, price);
                }
            }
        } else {
            for (Object[] data : dataList) {
                BigDecimal price = ((BigDecimal) data[3]).add((BigDecimal) data[4]);
                Integer locationID = (Integer) data[5];
                if (locationID != null && price != null) {
                    if (dataMap.containsKey(locationID)) {
                        dataMap.put(locationID, dataMap.get(locationID).add(price));
                    } else {
                        dataMap.put(locationID, price);
                    }
                }
            }
        }

        return dataMap;
    }

    @Override
    public List<Object[]> getCoursePassedStudents(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.id, cc.firstname||' '||cc.lastname, ca.name FROM ").append(getCompanyId()).append(".courseScheduleStudent css ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".student s ON css.student_id = s.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmContact cc ON s.contact_id = cc.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount ca ON s.customer_id = ca.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r ON css.exam_stutus_id = r.id ");
        sql.append("WHERE " + ServerUtils.checkForDeleted("s.deleted"));
        sql.append(" AND s.active is true");
        sql.append(" AND r.code = '" + TCConstants.STUDENT_COURSE_SCHEDULE_EXAM_PASSED + "'");
        if (filterParameter.getSearchKey() != null) {
            sql.append(" AND lower(cc.firstname) like '").append(filterParameter.getSearchKey()).append("'");
        }
        sql.append(" ORDER BY cc.firstname");
        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getStudentPassedCourses(Integer studentID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.number, c.name, cs.startdate, cs.expiredate FROM ").append(getCompanyId()).append(".courseScheduleStudent css ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".scheduledCourse cs ON css.courseSchedule_id = cs.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".course c ON cs.course_id = c.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r ON css.exam_stutus_id = r.id ");
        sql.append("WHERE " + ServerUtils.checkForDeleted("c.deleted"));
        if (studentID != null) {
            sql.append(" AND css.student_id = " + studentID);
        }
        sql.append(" AND r.code = '" + TCConstants.STUDENT_COURSE_SCHEDULE_EXAM_PASSED + "'");
        sql.append(" ORDER BY c.number");
        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getStudentDetailsForIDCard(Integer certificateTypeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cs.number, cs.startDate, cs.endDate, css.grade FROM ").append(getCompanyId()).append(".courseScheduleStudent css ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".scheduledCourse cs ON css.courseSchedule_id = cs.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".course c ON cs.course_id = c.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r ON css.exam_stutus_id = r.id ");
        sql.append(" WHERE c.id = (SELECT courseid FROM ").append(getCompanyId()).append(".certificatetypecourses where certificatetypeid = ").append(certificateTypeID).append(") ");
        sql.append(" AND r.code = '" + TCConstants.STUDENT_COURSE_SCHEDULE_EXAM_PASSED + "'");
        sql.append(" ORDER BY cs.endDate desc limit 1");
        return findNative(sql.toString());
    }

    @Override
    public String getCourseScheduleStudentsDetails(Integer csID, String type) {
        StringBuilder sql = new StringBuilder();
        StringBuilder subSql = new StringBuilder();
        if ("name".equals(type)) {
            subSql.append(" array_to_string(array_agg(cc.firstName||' '||cc.lastName),',') ");
        } else if ("email".equals(type)) {
            subSql.append(" array_to_string(array_agg(cc.primaryEmail),',') ");
        } else {
            subSql.append(" array_to_string(array_agg(replace(cc.primaryPhone, '|', '')),',') ");
        }
        sql.append("SELECT").append(subSql);
        sql.append("FROM ").append(getCompanyId()).append(".courseScheduleStudent css ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".student s ON css.student_id = s.id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmContact cc ON cc.id = s.contact_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("cc.deleted"));
        sql.append("AND css.courseschedule_id = " + csID);
        return (String) findNativeSingle(sql.toString());
    }

    @Override
    public void updateStudentsInvoice(List<Integer> integers, Integer invoiceID) {
        update("update EdsCourseScheduleStudent css set css.invoiceID = ? where css.objectID in ('" + ServerUtils.getAsCommoDelimited(integers, "0", "','") + "')", invoiceID);
    }

    public Integer getBookingIdByScheduledCourseId(Integer scheduledCourseId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(cb.id) from ").append(getCompanyId()).append(".courseschedulestudent cs \n");
        sql.append("left join ").append(getCompanyId()).append(".coursebooking cb on cs.coursebooking_id = cb.id \n");
        sql.append("where 1=1 and cs.courseschedule_id = ").append(scheduledCourseId).append(" and cb.deleted is not true \n");
        sql.append("group by courseschedule_id");
        return (Integer) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsCourseSchedule> getScheduleListByStudentId(Integer studentId, String sortAs) {
        return find("SELECT css.courseScheduleBooking FROM EdsCourseScheduleStudent css  WHERE css.student.objectID = ? and css.courseBooking.deleted is not true order by css.courseScheduleBooking.startDate " + sortAs, studentId);
    }

}
