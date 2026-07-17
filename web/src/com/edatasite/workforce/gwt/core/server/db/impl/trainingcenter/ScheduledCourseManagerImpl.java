package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInvoiceGeneratorSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.InvoiceGeneratorStatus;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilter;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.InstructorScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("scheduledCourseManager")
public class ScheduledCourseManagerImpl extends BaseManager<EdsCourseSchedule> implements ScheduledCourseManager, Constants {

    private static final int CONFIRMED_COURSE_SCHEDULE_STUDENT_COUNT = 3;
    private static final int INSTRUCTOR_TRAVEL_TIME = 8;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public ScheduledCourseManagerImpl() {
        super(EdsCourseSchedule.class);
    }

    @Override
    public List<EdsCourseSchedule> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sc FROM EdsCourseSchedule sc ");
        sql.append("inner join sc.course c ");
        sql.append("left join sc.location l ");
        sql.append("left outer join sc.instructor i ");
        sql.append("WHERE (sc.deleted is null OR sc.deleted is false) ");

        String searchKey = fp.getSqlSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            if (fp.isLookUp()) {
                sql.append(" AND lower(sc.number) like '" + searchKey + "' ");
            } else {
                sql.append(" AND (lower(sc.number) like '").append(searchKey).append("' ");
                sql.append(" OR lower(c.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(sc.language.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.firstName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.lastName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.country.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.city) like '").append(searchKey).append("') ");
            }
        }

        if (fp.getLocationId() != null) {
            sql.append(" AND l.objectID=").append(fp.getLocationId()).append(" ");
        }

        if (fp != null && fp.getSortField() != null) {
            if (ScheduledCourseItem.NUMBER.equals(fp.getSortField())) {
                sql.append(" order by sc.number" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.COURSE.equals(fp.getSortField())) {
                sql.append(" order by c.name" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.START_DATE.equals(fp.getSortField())) {
                sql.append(" order by sc.startDate" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.END_DATE.equals(fp.getSortField())) {
                sql.append(" order by sc.endDate" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.DURATION.equals(fp.getSortField())) {
                sql.append(" order by c.duration" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.COUNT_OF_SETS.equals(fp.getSortField())) {
                sql.append(" order by sc.numberOfSeats" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.VISIBILITY.equals(fp.getSortField())) {
                sql.append(" order by sc.visibility" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.LANGUAGE.equals(fp.getSortField())) {
                sql.append(" order by sc.language.name" + (!fp.isAscending() ? " desc" : ""));
            } else {
                sql.append(" order by sc.objectID desc");
            }
        } else {
            sql.append(" order by sc.objectID desc");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public List<EdsCourseSchedule> getConfirmedScheduledCourseList(ListingFilterParameter fp) {
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sc FROM EdsCourseSchedule sc ");
        sql.append("inner join sc.course c ");
        sql.append("left join sc.location l ");
        sql.append("left outer join sc.instructor i ");
        sql.append("WHERE (sc.deleted is null OR sc.deleted is false) ");
        sql.append("AND sc.endDate >= to_date(?, 'yyyy-mm-dd') ");
        sql.append(" AND (SELECT count(css.objectID) FROM EdsCourseScheduleStudent css inner join css.courseScheduleBooking csb ")
                .append(" inner join css.status s WHERE csb.objectID = sc.objectID AND s.code = '" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED + "' ) >= ").append(CONFIRMED_COURSE_SCHEDULE_STUDENT_COUNT);


        if (user != null && !user.hasEitherRoles(DR, ADMIN)) {
            sql.append(" AND (sc.assessor is null OR sc.assessor.objectID = ").append(user.getObjectID()).append(" ) ");
        }

        String searchKey = fp.getSqlSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            if (fp.isLookUp()) {
                sql.append(" AND lower(sc.number) like '" + searchKey + "' ");
            } else {
                sql.append(" AND (lower(sc.number) like '").append(searchKey).append("' ");
                sql.append(" OR lower(c.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(sc.language.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.firstName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.lastName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.country.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.city) like '").append(searchKey).append("') ");
            }
        }


        if (fp != null && fp.getSortField() != null) {
            if (ScheduledCourseItem.NUMBER.equals(fp.getSortField())) {
                sql.append(" order by sc.number" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.COURSE.equals(fp.getSortField())) {
                sql.append(" order by c.name" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.START_DATE.equals(fp.getSortField())) {
                sql.append(" order by sc.startDate" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.END_DATE.equals(fp.getSortField())) {
                sql.append(" order by sc.endDate" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.DURATION.equals(fp.getSortField())) {
                sql.append(" order by c.duration" + (!fp.isAscending() ? " desc" : ""));
            } else if (ScheduledCourseItem.LANGUAGE.equals(fp.getSortField())) {
                sql.append(" order by sc.language.name" + (!fp.isAscending() ? " desc" : ""));
            } else {
                sql.append(" order by sc.startDate ");
            }
        } else {
            sql.append(" order by sc.startDate ");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit(), new Date());
    }

    @Override
    public List<EdsCourseSchedule> listForInstructorReassign(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sc.* FROM ").append(getCompanyId()).append(".scheduledcourse sc ");
        if (fp.getSortField() != null && "course".equals(fp.getSortField())) {
            sql.append("INNER JOIN ").append(getCompanyId()).append(".course c ON sc.course_id = c.id ");
        }
        sql.append("INNER JOIN ").append(getCompanyId()).append(".instructorScheduledCourse isch ON isch.scheduled_course_id = sc.id AND isch.instructor_id = sc.instructor_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".SickRequest sr ON sr.employeeId = sc.instructor_id AND (isch.date between to_date(to_char(sr.startdate,'yyyy-mm-dd'),'yyyy-mm-dd') and sr.enddate) ");
        sql.append("WHERE sc.deleted is not true ");

        if (fp.getEmployeeId() != null) {
            sql.append("AND sc.instructor_id = '").append(fp.getEmployeeId()).append("' ");
        }

        if (fp != null && fp.getSortField() != null) {
            if ("number".equals(fp.getSortField())) {
                sql.append(" order by sc.number" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else if ("course".equals(fp.getSortField())) {
                sql.append(" order by c.number" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else if ("startdate".equals(fp.getSortField())) {
                sql.append(" order by sc.startdate" + (fp.getSortDir() == 2 ? " desc" : ""));
            } else {
                sql.append(" order by sc.id desc");
            }
        } else {
            sql.append(" order by sc.id desc");
        }

        if (fp.getStart() != null && fp.getLimit() != null) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsCourseSchedule.class);
    }

    @Override
    public Integer getCountOfScheduledCourse(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(sc.objectID) FROM EdsCourseSchedule sc ");
        sql.append("inner join sc.course c ");
        sql.append("left join sc.location l ");
        sql.append("left outer join sc.instructor i ");
        sql.append("WHERE sc.deleted is not true ");

        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append(" AND (lower(sc.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(c.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(sc.language.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(i.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(i.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(l.country.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(l.city) like '").append(fp.getSqlSearchKey()).append("') ");
        }

        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    public Integer getCountOfConfirmedScheduledCourse(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(sc.objectID) FROM EdsCourseSchedule sc ");
        sql.append("inner join sc.course c ");
        sql.append("left join sc.location l ");
        sql.append("left outer join sc.instructor i ");
        sql.append("WHERE (sc.deleted is null OR sc.deleted is false) ");
        sql.append("AND sc.endDate >= to_date(?, 'yyyy-mm-dd') ");
        sql.append(" AND (SELECT count(css.objectID) FROM EdsCourseScheduleStudent css inner join css.courseScheduleBooking csb ")
                .append(" inner join css.status s WHERE csb.objectID = sc.objectID AND s.code = '" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED + "' ) >= ").append(CONFIRMED_COURSE_SCHEDULE_STUDENT_COUNT);


        if (fp.getScheduledCourseID() != null) {
            sql.append(" AND sc.objectID = ").append(fp.getScheduledCourseID());
        }

        String searchKey = fp.getSqlSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            if (fp.isLookUp()) {
                sql.append(" AND lower(sc.number) like '" + searchKey + "' ");
            } else {
                sql.append(" AND (lower(sc.number) like '").append(searchKey).append("' ");
                sql.append(" OR lower(c.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(sc.language.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.firstName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(i.lastName) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.country.name) like '").append(searchKey).append("' ");
                sql.append(" OR lower(l.city) like '").append(searchKey).append("') ");
            }
        }

        Long count = (Long) findSingle(sql.toString(), new Date());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Integer getCountOfInstructorReassign(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(DISTINCT sc.id) FROM ").append(getCompanyId()).append(".scheduledcourse sc ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".instructorScheduledCourse isch ON isch.scheduled_course_id = sc.id AND isch.instructor_id = sc.instructor_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".SickRequest sr ON sr.employeeId = sc.instructor_id AND (isch.date between to_date(to_char(sr.startdate,'yyyy-mm-dd'),'yyyy-mm-dd') and sr.enddate) ");
        sql.append("WHERE sc.deleted is not true ");

        if (fp.getEmployeeId() != null) {
            sql.append("AND sc.instructor_id = '").append(fp.getEmployeeId()).append("' ");
        }
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count != null ? count.intValue() : 0;

    }

    @Override
    public void deleteScheduledCourseInstructors(Integer scheduledCourseID) {
        update("DELETE FROM EdsInstructorScheduledCourse isc WHERE isc.courseSchedule.objectID = ?", scheduledCourseID);
    }

    @Override
    public List<InstructorScheduledCourseItem> getScheduledCourseInstructors(Integer scheduledCourseID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id as objectID, instructor_id as instructorID, scheduled_course_id as scheduledCourseID, date, ");
        sql.append("CASE WHEN (SELECT count(*) FROM ").append(getCompanyId()).append(".SickRequest sr WHERE sr.employeeId = isch.instructor_id AND (to_date(to_char(isch.date,'yyyy-mm-dd'),'yyyy-mm-dd') between to_date(to_char(sr.startdate,'yyyy-mm-dd'),'yyyy-mm-dd') and sr.enddate) ) > 0 THEN true ELSE false END as hasLeave ");
        sql.append("FROM ").append(getCompanyId()).append(".instructorScheduledCourse isch ");
        sql.append("WHERE isch.scheduled_course_id = '").append(scheduledCourseID).append("' ");
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(InstructorScheduledCourseItem.class));
    }

    /**
     * get All Instructors matching only course and language
     *
     * @param fp
     * @return
     */
    public List<EdsEmployee> getInstructors(ListingFilterParameter fp) {
        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setRequestParams(fp.getRequestParams());
        lfp.setStartDate(null);
        lfp.setEndDate(null);
        return getAvailableInstructors(lfp, false);
    }

    /**
     * get All Available Instructor list for given date range
     *
     * @param fp
     * @return
     */
    public List<EdsEmployee> getAvailableInstructors(ListingFilterParameter fp) {
        return getAvailableInstructors(fp, false);
    }

    @Override
    public List<EdsCourseSchedule> getAvailableCourseSchedule(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cs FROM EdsCourseSchedule cs inner join cs.course c inner join cs.location l inner join cs.language language ");
        sql.append("WHERE (cs.deleted is false OR cs.deleted is null) ");

        if (fp.getCourseID() != null) {
            sql.append(" AND c.objectID = ").append(fp.getCourseID());
        }
        if (fp.getLocationId() != null) {
            sql.append(" AND l.objectID = ").append(fp.getLocationId());
        }
        if (fp.getLanguageID() != null) {
            sql.append(" AND language.objectID = ").append(fp.getLanguageID());
        }
        if (fp.getScheduledCourseID() != null) {
            sql.append(" AND cs.objectID != ").append(fp.getScheduledCourseID());
        }

        sql.append(" AND (cs.numberOfSeats - (SELECT count(css.objectID) FROM EdsCourseScheduleStudent css inner join css.courseScheduleBooking csb ")
                .append(" inner join css.status s WHERE csb.objectID = cs.objectID AND (s.code != '" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED + "' OR css.student is null) )) > 0 ");
        //sql.append(" AND to_date(to_char(cs.startDate,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?, 'yyyy-mm-dd')");
        sql.append(" ORDER BY cs.startDate ");
        return find(sql.toString()/*, dateFormat.format(new Date())*/);
    }

    @Override
    public boolean hasAvailableCourseSchedule(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(cs.objectID) FROM EdsCourseSchedule cs inner join cs.course c inner join cs.location l inner join cs.language language ");
        sql.append("WHERE (cs.deleted is false OR cs.deleted is null) ");

        if (fp.getCourseID() != null) {
            sql.append(" AND c.objectID = ").append(fp.getCourseID());
        }
        if (fp.getLocationId() != null) {
            sql.append(" AND l.objectID = ").append(fp.getLocationId());
        }
        if (fp.getLanguageID() != null) {
            sql.append(" AND language.objectID = ").append(fp.getLanguageID());
        }
        if (fp.getScheduledCourseID() != null) {
            sql.append(" AND cs.objectID != ").append(fp.getScheduledCourseID());
        }

        sql.append(" AND (cs.numberOfSeats - (SELECT count(css.objectID) FROM EdsCourseScheduleStudent css inner join css.courseScheduleBooking csb ")
                .append(" inner join css.status s WHERE csb.objectID = cs.objectID AND (s.code != '" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED + "' OR css.student is null) )) > 0 ");
        sql.append(" AND to_date(to_char(cs.startDate,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?, 'yyyy-mm-dd')");

        BigInteger countOfItem = (BigInteger) findSingle(sql.toString(), dateFormat.format(new Date()));
        return countOfItem != null && countOfItem.intValue() > 0;
    }

    /**
     * get available instructor list for the date range
     *
     * @param fp
     * @param isOtherLocations
     * @return
     */
    private List<EdsEmployee> getAvailableInstructors(ListingFilterParameter fp, boolean isOtherLocations) {
        Map<String, Object> map = new HashMap<>();
        if (fp.getStartDate() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fp.getStartDate());
            map.put("startDate", calendar.getTime());
        }
        if (fp.getEndDate() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fp.getEndDate());
            map.put("endDate", calendar.getTime());
        }
        map.put("locationID", fp.getLocationId());
        map.put("courseID", fp.getCourseID());
        map.put("languageID", fp.getLanguageID());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inst FROM ").append("EdsEmployee inst join inst.courses c join inst.accountStatus asts ");
        sql.append("WHERE asts.code != '").append(EMPLOYEE_STATUS_RESIGNED).append("' AND inst.deleted is not true ");
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND inst.objectID NOT IN (SELECT distinct instsc.instructor.objectID FROM ")
                    .append("EdsInstructorScheduledCourse instsc join instsc.courseSchedule cs WHERE ");

            if (!isOtherLocations) {
                sql.append("((:startDate between instsc.date and instsc.endTime) or (:endDate between instsc.date and  instsc.endTime) " +
                        "or (instsc.date between :startDate and :endDate) or (instsc.endTime between :startDate and :endDate)) ");
            } else {
                sql.append("cs.location.objectID != ").append(fp.getLocationId());
                sql.append(" and ((:startDate between instsc.date and instsc.endTime) or (:endDate between instsc.date and  instsc.endTime) " +
                        " or (instsc.date between :startDate and :endDate) or (instsc.endTime between :startDate and :endDate)) ");

                String startDate = dateFormat.format(getIncDecTime((Date) fp.getStartDate().clone(), INSTRUCTOR_TRAVEL_TIME));
                String endDate = dateFormat.format(getIncDecTime((Date) fp.getEndDate().clone(), -INSTRUCTOR_TRAVEL_TIME));
                sql.append(" OR cs.location.objectID = ").append(fp.getLocationId());
                sql.append(" and (('").append(startDate).append("' between instsc.date and  instsc.endTime) or ('").append(endDate).append("' between instsc.date and instsc.endTime) ");
                sql.append(" or (instsc.date between '").append(startDate).append("' and '").append(endDate).append("') or (instsc.endTime between '").append(startDate).append("' and '").append(endDate).append("')) ");
            }

            //if a edit mode
            if (fp.getScheduledCourseID() != null) {
                sql.append(" AND cs.objectID != ").append(fp.getScheduledCourseID());
            }

            sql.append(") ");
        }

        if (!isOtherLocations) {
            sql.append("AND inst.location.objectID = :locationID AND c.objectID = :courseID ");
        } else {
            sql.append("AND inst.location.objectID != :locationID AND c.objectID = :courseID");
        }

        if (fp.getLanguageID() != null) {
            sql.append(" AND  inst.objectID IN (SELECT lang.entityId FROM EdsSpokenLanguages lang WHERE lang.language.objectID = :languageID AND lang.entityType = 'EMPLOYEE') ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("AND inst.objectID = ").append(fp.getEmployeeId());
        }

        List<EdsEmployee> availableInstructors = findByNamedParams(sql.toString(), map);

        //get other location's instructors
        if (!isOtherLocations) {
            if (availableInstructors == null) {
                availableInstructors = new ArrayList<>();
            }
            Date startDate = null, endDate = null;
            if (fp.getStartDate() != null && fp.getEndDate() != null) {
                startDate = (Date) fp.getStartDate().clone();
                endDate = (Date) fp.getEndDate().clone();

                fp.setStartDate(getIncDecTime((Date) fp.getStartDate().clone(), -INSTRUCTOR_TRAVEL_TIME));
                fp.setEndDate(getIncDecTime((Date) fp.getEndDate().clone(), INSTRUCTOR_TRAVEL_TIME));
            }
            availableInstructors.addAll(getAvailableInstructors(fp, true));

            fp.setStartDate(startDate);
            fp.setEndDate(endDate);
        }

        return availableInstructors;
    }

    private Date getIncDecTime(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);

        return cal.getTime();
    }

    @Override
    public void deleteScheduledCourseReservations(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SElECT cr.reservation_id FROM ").append(getCompanyId()).append(".courseschedule_reservation cr WHERE cr.courseschedule_id = ").append(objectID);
        List<Integer> ids = findNative(sql.toString());

        sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getCompanyId()).append(".courseschedule_reservation cr WHERE cr.courseschedule_id = ").append(objectID);
        updateNative(sql.toString());


        sql = new StringBuilder();
        sql.append("DELETE FROM EdsBookingItemReservation br WHERE br.id IN (").append(ServerUtils.getAsCommoDelimited(ids, "0", ",")).append(")");
        update(sql.toString());
    }

    @Override
    public List<Object[]> getCourseListByLocation(Integer locationId, Date nowDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sqlQuery = new StringBuilder("select c.id, c.name, c.number,l.id as languageId,l.name as languageName from " + getCompanyId() + ".scheduledcourse sc \n");
        sqlQuery.append(" inner join " + getCompanyId() + ".course c on c.id=sc.course_id\n");
        sqlQuery.append(" inner join " + getCompanyId() + ".reference l on l.id=sc.language_id\n");
        sqlQuery.append(" where sc.location_id=").append(locationId);
        sqlQuery.append(" and sc.startdate>=").append("'").append(dateFormat.format(nowDate)).append("'").append("\n");
        sqlQuery.append(" and sc.deleted IS NOT TRUE");
        sqlQuery.append(" GROUP BY c.id, c.number, c.name,l.id,l.name\n");
        sqlQuery.append(" ORDER BY c.number asc, c.id, c.name,l.id,l.name\n");

        return findNative(sqlQuery.toString());
    }

    public List<Object[]> getCourseScheduleBooking(Integer locationId, String courseIds, String languageIds, Date nowDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sqlQuery = new StringBuilder("select  sc.id,c.id as courseId,sc.language_id,my.id as intructorId,my.firstname||my.lastname as instructorname,sc.numberOfSeats,sc.startdate,sc.enddate,c.duration,count(cschs.id) as attendStudentCount\n");
        sqlQuery.append(" from " + getCompanyId() + ".scheduledcourse sc\n");
        sqlQuery.append(" inner join " + getCompanyId() + ".course c on c.id=sc.course_id \n");
        sqlQuery.append(" left join " + getCompanyId() + ".myUser my on my.id=sc.instructor_id \n");
        sqlQuery.append(" left join " + getCompanyId() + ".courseschedulestudent cschs on cschs.courseschedule_id = sc.id and" +
                " cschs.stutus_id != (select id from ").append(getCompanyId()).append(".reference where code = '").append(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED).append("' limit 1) \n");
        sqlQuery.append(" where sc.deleted is not true and sc.location_id=" + locationId + " and sc.language_id in (" + languageIds + ") and c.id in (" + courseIds + ") \n");
        sqlQuery.append(" and sc.startdate>=").append("'").append(dateFormat.format(nowDate)).append("'").append("\n");
        sqlQuery.append(" GROUP BY sc.id,c.id,sc.language_id,my.id,my.firstname||my.lastname,sc.numberOfSeats,sc.startdate,sc.enddate,c.duration\n");
        sqlQuery.append(" ORDER BY sc.startdate \n");

        return findNative(sqlQuery.toString());
    }

    public List<Object[]> getStudentAttendedCourseSchedule(Integer studentId, Integer locationId, Date nowDate) {
        StringBuilder sqlQuery = new StringBuilder("select sch.id,sch.startdate,sch.enddate, c.name, sch.number from " + getCompanyId() + ".courseschedulestudent cschs ");
        sqlQuery.append(" inner join " + getCompanyId() + ".student s on s.id = cschs.student_id ");
        sqlQuery.append(" inner join " + getCompanyId() + ".scheduledcourse sch on sch.id=cschs.courseschedule_id ");
        sqlQuery.append(" inner join " + getCompanyId() + ".course c on c.id=sch.course_id and sch.deleted is not true");
        sqlQuery.append(" inner join " + getCompanyId() + ".reference ss on ss.id=cschs.stutus_id ");
        sqlQuery.append(" where s.id=" + studentId + " and sch.location_id=" + locationId);
        sqlQuery.append(" and ss.code != '").append(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED).append("' ");
        return findNative(sqlQuery.toString());
    }

    @Override
    public EdsCourseSchedule getScheduleByDate(Integer courseID, Date scheduleStartDate) {
        List<EdsCourseSchedule> courseSchedules = find("SELECT cs FROM EdsCourseSchedule cs join cs.course c WHERE c.objectID = ? and cs.startDate = ?", courseID, scheduleStartDate);

        if (courseSchedules != null && courseSchedules.size() > 0) {
            return courseSchedules.get(0);
        }

        return null;
    }

    @Override
    public List<Integer> getNotExpiredCourseSchedulesByCourses(List<Integer> courseIDs) {
        return find("SELECT DISTINCT cs.objectID FROM EdsCourseSchedule cs where cs.deleted is not true and cs.course.objectID in (" + ServerUtils.getAsCommoDelimited(courseIDs, "0") + ") and (cs.expireDate is null or cs.expireDate > ?)", new Date());
    }

    @Override
    public ArrayList<Date> getClonedDateList(EdsCourseSchedule courseSchedule) {
        if (courseSchedule.getInstructor() == null) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cs.startDate FROM EdsCourseSchedule cs inner join cs.course c inner join cs.instructor inst inner join cs.language l ");
        sql.append("WHERE cs.deleted is false AND c.objectID = ? ");
        sql.append("AND to_date(to_char(cs.startDate,'yyyy-mm-dd'),'yyyy-mm-dd') >= to_date(?, 'yyyy-mm-dd') ");
        sql.append("AND inst.objectID = ? AND l.objectID = ? ");

        return (ArrayList<Date>) find(sql.toString(), courseSchedule.getCourse().getObjectID(), dateFormat.format(courseSchedule.getStartDate()), courseSchedule.getInstructor().getObjectID(), courseSchedule.getLanguage().getObjectID());
    }

    @Override
    public Integer getCourseScheduleDroppableStudentCount(Integer schCourseId) {
        StringBuilder sqlQuery = new StringBuilder("SELECT count(css.id) FROM " + getCompanyId() + ".courseschedulestudent css \n");
        sqlQuery.append("INNER JOIN " + getCompanyId() + ".scheduledcourse sc ON sc.id=css.courseschedule_id \n");
        sqlQuery.append("INNER JOIN " + getCompanyId() + ".course c ON c.id=sc.course_id \n");
        sqlQuery.append("INNER JOIN " + getCompanyId() + ".student s ON s.id=css.student_id \n");
        sqlQuery.append("INNER JOIN " + getCompanyId() + ".reference status ON status.id=css.stutus_id \n");
        sqlQuery.append("WHERE css.courseschedule_id=" + schCourseId + " AND  css.student_id IS NOT NULL AND status.code='_STUDENT_COURSE_SCHEDULE_ATTENDED' AND css.droppable=TRUE \n");
        sqlQuery.append("GROUP BY sc.id \n");
        BigInteger courseScheduleDrappableStudent = (BigInteger) findNativeSingle(sqlQuery.toString());
        return courseScheduleDrappableStudent.intValue();
    }

    @Override
    public EdsCourseSchedule getScheduledCourseByNumber(String number) {
        return (EdsCourseSchedule) findSingle("select cs from EdsCourseSchedule cs where (cs.deleted is null or cs.deleted is false) and cs.productNumber=?", number);
    }

    @Override
    public List<EdsCourseSchedule> getCourseScheduleItemListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select cs from EdsCourseSchedule cs where (cs.deleted is null or cs.deleted is false) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and cs.lastUpdateTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and cs.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by cs.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getCompanyDeletedCourseScheduleListForSolr(SolrReindexRpc solrReindex) {
        StringBuilder sqlQuery = new StringBuilder("select cs.objectID from EdsCourseSchedule cs ");
        sqlQuery.append("where cs.deleted=true and cs.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            sqlQuery.append(" and cs.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(sqlQuery.toString());
    }

    @Override
    public List<Integer> getCourseScheduleIDsByIDs(String ids) {
        return find("select cs.objectID from EdsCourseSchedule cs where cs.objectID IN(" + ids + ") AND (cs.deleted is null OR cs.deleted<>true)");
    }

    @Override
    public List<Integer> getCourseScheduleIdsWithLimit(Integer startat, Integer limit) {
        return findInterval("select cs.objectID from EdsCourseSchedule cs where cs.objectID > ? AND (cs.deleted is null OR cs.deleted<>true) order by cs.objectID ASC", startat, limit, limit);
    }

    @Override
    public List<Object[]> getCourseScheduleForInvoiceByPeriod(ListingFilterParameter fp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startPeriod = dateFormat.format(fp.getStartDate());
        String endPeriod = dateFormat.format(fp.getEndDate());

        StringBuilder sql = new StringBuilder();
        sql.append("select cb.customer_id, array_to_string(array_agg(sc.id), ',') as schedule_numbers from ").append(getCompanyId()).append(".scheduledcourse sc ");
        sql.append("inner join ").append(getCompanyId()).append(".courseschedulestudent css on css.courseschedule_id = sc.id ");
        sql.append("inner join ").append(getCompanyId()).append(".coursebooking cb on cb.id = css.coursebooking_id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference scs on scs.id = sc.status_id ");
        sql.append("inner join ").append(getCompanyId()).append(".reference ss on ss.id = css.stutus_id ");
        sql.append("where sc.deleted is false and cb.invoiceid is null ");
        sql.append("and scs.code = '").append(CS_DELIVERED).append("' and ss.code = '").append(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED).append("' and css.attended_status_id is not null and css.invoiceid is null ");
        sql.append("and sc.startdate between '").append(startPeriod).append("' and '").append(endPeriod).append("' ");
        sql.append("and (select count(csinv.courseschedule_id) from ").append(getCompanyId()).append(".courseschedule_invoice csinv where csinv.courseschedule_id = sc.id) = 0 ");
        sql.append("group by cb.customer_id ");

        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getInvoicedScheduleCourseList(ListingFilterParameter fp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startPeriod = dateFormat.format(fp.getStartDate());
        String endPeriod = dateFormat.format(fp.getEndDate());

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct csinv.invoice_id, max(inv.client_id) as customer_id,  array_to_string(array_agg(cs.id), ',') from ").append(getCompanyId()).append(".courseschedule_invoice csinv ");
        sql.append("inner join ").append(getCompanyId()).append(".scheduledcourse cs on cs.id = csinv.courseschedule_id ");
        sql.append("inner join ").append(getCompanyId()).append(".saleinvoice inv on inv.id = csinv.invoice_id ");
        sql.append("where cs.startdate between '").append(startPeriod).append("' and '").append(endPeriod).append("' ");
        sql.append("group by csinv.invoice_id ");
        sql.append("order by csinv.invoice_id ");

        return findNative(sql.toString());
    }

    @Override
    public boolean hasInvoice(Integer objectID) {
        List list = findNative("SELECT ci.* FROM " + getCompanyId() + ".courseschedule_invoice ci WHERE ci.courseschedule_id = ?", objectID);
        return list != null && list.size() > 0;
    }

    @Override
    public Integer countOfNotAddressedStudents(Integer objectID) {
        return ((Long) findSingle("SELECT count(css.objectID) FROM EdsCourseScheduleStudent css WHERE css.status.code != ? and css.attendedStatus is null and css.courseScheduleBooking.objectID = ?", EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED, objectID)).intValue();
    }

    @Override
    public Integer getCountOfSchedulesByCourse(EdsCourse courseId) {
        return ((Long) findSingle("select count(cs.objectID) from EdsCourseSchedule cs where cs.deleted is false and cs.course = ?", courseId)).intValue();
    }

    @Override
    public Integer getScheduleLasNumber() {
        return (Integer) findSingle("SELECT cs.intNumber FROM EdsCourseSchedule cs WHERE (cs.deleted is null OR cs.deleted is false) AND cs.intNumber IS NOT NULL ORDER BY cs.intNumber DESC");
    }

    @Override
    public List<EdsCourseSchedule> getInstructorCourseSchedules(Integer instructorID) {
        return find("select cs from EdsCourseSchedule cs where cs.instructor.objectID = ? AND (cs.deleted is null OR cs.deleted<>true) order by cs.objectID asc", instructorID);
    }

    @Override
    public EdsInvoiceGeneratorSchedule getInvoiceGeneratorSchedule(Date startDate, Date endDate) {
        return (EdsInvoiceGeneratorSchedule) findSingle("select igs from EdsInvoiceGeneratorSchedule igs where igs.status = ? and igs.startDate = ? and igs.endDate = ?", InvoiceGeneratorStatus.IN_PROCESS, startDate, endDate);
    }

    @Override
    public EdsInvoiceGeneratorSchedule getInvoiceGeneratorSchedule(Integer objectID) {
        return (EdsInvoiceGeneratorSchedule) findSingle("select igs from EdsInvoiceGeneratorSchedule igs where igs.objectID = ?", objectID);
    }

    @Override
    public void updateScheduledCoursesInvoice(ArrayList<String> sCIdList, Integer invoiceID) {
        updateNative("delete from " + getCompanyId() + ".courseschedule_invoice where invoice_id = '" + invoiceID + "' and courseschedule_id in ('" + ServerUtils.getAsCommoDelimited(sCIdList, "0", "','") + "')");

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(getCompanyId()).append(".courseschedule_invoice(courseschedule_id, invoice_id) values \n");

        int i = 0;
        for (String scheduledCourseID : sCIdList) {
            sql.append("(").append(scheduledCourseID).append(", ").append(invoiceID).append(") ");

            if (i < sCIdList.size() - 1) {
                sql.append(", \n");
            }
            i++;
        }
        sql.append("; \n");
        updateNative(sql.toString());
    }

    public List<EdsCourseSchedule> getScheduledCourses(CalendarFilter filter) {
        Map params = new HashMap();
        params.put("start", filter.getStart());
        params.put("end", filter.getEnd());
        if (filter.getLocationID() != null) {
            params.put("locationID", filter.getLocationID());
        }
        return findByNamedParams("select c from EdsCourseSchedule c where " +
                " (c.startDate <= :end and c.endDate >= :start) " +
                (filter.getLocationID() != null ? " and c.location.objectID = :locationID " : "") +
                "  and  c.deleted is not true " +
                " order by c.startDate desc", params);
    }

    public EdsCourseSchedule getFirstOrLastCourseScheduleInRecurringSeries(Integer recurrenceID, boolean isFirst) {
        return (EdsCourseSchedule) findSingle("select schedule from EdsCourseSchedule schedule where schedule.recurrenceID = ? order by schedule.fireTime " + (
                isFirst
                        ? "asc"
                        : "desc"), recurrenceID);
    }

    public EdsCourseSchedule getScheduleInstance(Integer recurrenceID, Date fireTime) {
        return (EdsCourseSchedule) findSingle("select schedule from EdsCourseSchedule schedule where schedule.recurrenceID = ? and schedule.fireTime = ?", recurrenceID, fireTime);
    }

    public void removeRecurrenceFromScheduleCourse(Integer recurrenceID, Integer companyID) {
        updateNative("update \"" + companyID + "\".scheduledcourse set recurrenceid = null where recurrenceid=" + recurrenceID);
    }
}
