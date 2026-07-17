package com.edatasite.workforce.gwt.trainingcenter.server;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/16/12
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TCServiceLocal {

    void expireTemporaryLocks(Integer companyID);

    BookingItemForApprove getCourseBookingForConfirmation(Integer bookingID, Integer companyID);

    void updateCourseBookingStatus(Integer bookingID, Integer companyID, String status);

    Integer[] saveCourseBooking(CourseBookingItem courseBookingData);

	String[] importXML(InputStream inputStream);

    boolean reGenerateScheduledCourseTimes(Integer listStart, Integer listLimit);

    boolean reGenerateScheduledCourseEvents(Integer listStart, Integer listLimit);

    ListResult<ScheduledCourseItem> getCourseScheduleFromSolr(ListingFilterParameter filterParameter);

    ListResult<CourseItem> getCourseList(ListingFilterParameter filterParameter);

    CourseItem getCourseItem(Integer objectID);

    Boolean deleteCourse(Integer objectID);

    void saveCourse(CourseItem courseItem);

    Integer getCourseLastIntNumber();
}
