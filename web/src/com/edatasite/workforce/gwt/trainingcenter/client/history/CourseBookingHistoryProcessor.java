package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CourseBookingAddSinkContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CourseBookingViewSinkContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingHistoryProcessor implements HistoryProcessor {

    private TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new CourseBookingViewSinkContainer(containerName + params[0], tcStrings.courseBookingSummary(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new CourseBookingAddSinkContainer("courseBookingadd", tcStrings.addCourseBooking(), params);
    }
}
