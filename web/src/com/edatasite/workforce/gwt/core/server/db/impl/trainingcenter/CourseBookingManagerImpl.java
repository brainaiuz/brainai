package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseBookingManager")
public class CourseBookingManagerImpl extends BaseManager<EdsCourseBooking> implements CourseBookingManager, Constants {
    public CourseBookingManagerImpl() {
        super(EdsCourseBooking.class);
    }

    @Override
    public Integer getCourseBookingLastIntNumber() {
        return (Integer) findSingle("SELECT c.intNumber FROM EdsCourseBooking c WHERE c.intNumber IS NOT NULL ORDER BY c.intNumber DESC ");
    }

    @Override
    public List<EdsCourseBooking> getCourseBookingList(ListingFilterParameter filterParametrs) {
        StringBuilder hqlQuery = new StringBuilder("SELECT DISTINCT cb FROM EdsCourseBooking cb ");
        hqlQuery.append(" LEFT JOIN cb.customer customer ");
        hqlQuery.append(" LEFT JOIN cb.location location ");
        hqlQuery.append(" LEFT JOIN location.country country ");
        hqlQuery.append(" LEFT JOIN cb.status status ");
        hqlQuery.append(" WHERE (cb.deleted is null or cb.deleted is false) ");

        String searchKey = filterParametrs.getSqlSearchKey();
        if (searchKey != null && !"".equals(searchKey)) {
            hqlQuery.append(" AND (lower(cb.number) like '").append(searchKey).append("' ");
            hqlQuery.append(" OR lower(customer.name) like '").append(searchKey).append("' ");
            hqlQuery.append(" OR lower(country.name) like '").append(searchKey).append("') ");
        }

        if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
            if (CourseBookingItem.NUMBER.equals(filterParametrs.getSortField())) {
                hqlQuery.append("ORDER BY cb.number ");
            } else if (CourseBookingItem.CREATIONDATE.equals(filterParametrs.getSortField())) {
                hqlQuery.append(" order by cb.creationDate ");
            } else if (CourseBookingItem.CUSTOMER.equals(filterParametrs.getSortField())) {
                hqlQuery.append("ORDER BY customer.name ");
            } else {
                hqlQuery.append("ORDER BY cb.objectID ");
            }
            hqlQuery.append(!filterParametrs.isAscending() ? "DESC" : "ASC");
        } else {
            hqlQuery.append("ORDER BY cb.objectID DESC ");
        }
        return findInterval(hqlQuery.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
    }

    @Override
    public List<EdsCourseBooking> getCourseBookings(int startat, int limit) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select cb from EdsCourseBooking cb where (cb.deleted is null or cb.deleted = false) ");
        sqlQuery.append(" order by cb.objectID ASC ");
        return findInterval(sqlQuery.toString(), startat, limit);
    }

    @Override
    public Integer getCourseBookingTotal(ListingFilterParameter filterParametrs) {
        StringBuilder hqlQuery = new StringBuilder("SELECT DISTINCT count(cb.objectID) FROM EdsCourseBooking cb ");
        hqlQuery.append(" LEFT JOIN cb.customer customer ");
        hqlQuery.append(" LEFT JOIN cb.location location ");
        hqlQuery.append(" LEFT JOIN location.country country ");
        hqlQuery.append(" WHERE 1=1 ");
        String searchKey = filterParametrs.getSqlSearchKey();
        if (searchKey != null && !"".equals(searchKey)) {
            hqlQuery.append(" AND (lower(cb.number) like '").append(searchKey).append("' ");
            hqlQuery.append(" OR lower(customer.name) like '").append(searchKey).append("' ");
            hqlQuery.append(" OR lower(country.name) like '").append(searchKey).append("') ");
        }
        Long count = (Long) findSingle(hqlQuery.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public void deleteCourseBooking(Integer objectID) {
        updateNative("UPDATE " + getCompanyId() + ".coursebooking SET deleted = true WHERE id=" + objectID);
    }

    @Override
    public List<EdsStudent> getStudentListByCourseScheduleId(Integer courseScheduleId) {
        return find("SELECT cschs.student FROM EdsCourseScheduleStudent cschs INNER JOIN cschs.courseScheduleBooking schb " +
                " WHERE schb.objectID=? AND cschs.student IS NOT NULL " +
//                "  AND cschs.student.contact.deleted IS FALSE " +
                "  AND cschs.status.code !='" + EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED + "' ", courseScheduleId);
    }

    @Override
    public List<Integer> getRejectedCourseBookingForSolr() {
        return find("SELECT cb.objectID FROM EdsCourseBooking cb WHERE cb.status.code = ?", BOOKING_REJECTED);
    }

    @Override
    public void mergeBookingCustomers(ArrayList<Integer> ids, Integer masterID) {
        updateNative("UPDATE " + getCompanyId() + ".coursebooking SET customer_id = " + masterID + " WHERE customer_id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    @Override
    public List<Integer> getCourseBookingIDsByIDs(String ids) {
        return find("select cs.objectID from EdsCourseBooking cs where cs.objectID IN(" + ids + ") AND (cs.deleted is null OR cs.deleted<>true)");
    }

    @Override
    public List<Integer> getCourseBookingIdsWithLimit(Integer startat, Integer limit) {
        return findInterval("select cs.objectID from EdsCourseBooking cs where cs.objectID > ? AND (cs.deleted is null OR cs.deleted<>true) order by cs.objectID ASC", startat, limit, limit);
    }
}
