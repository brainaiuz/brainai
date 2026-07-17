package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/08/12
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public interface CourseBookingManager extends Manager<EdsCourseBooking> {
    Integer getCourseBookingLastIntNumber();

    List<EdsCourseBooking> getCourseBookingList(ListingFilterParameter filterParametrs);
    List<EdsCourseBooking> getCourseBookings(int startat, int limit);

    Integer getCourseBookingTotal(ListingFilterParameter filterParametrs);

    void deleteCourseBooking(Integer objectID);

    List<EdsStudent> getStudentListByCourseScheduleId(Integer courseScheduleId);

    List<Integer> getRejectedCourseBookingForSolr();

    void mergeBookingCustomers(ArrayList<Integer> ids, Integer masterID);

    List<Integer> getCourseBookingIDsByIDs(String IDs);

    List<Integer> getCourseBookingIdsWithLimit(Integer startat, Integer limit);


}
