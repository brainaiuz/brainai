package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.ScheduledCourseAddSinkContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.ScheduledCourseSinkContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseHistoryProcessor implements HistoryProcessor {

    private static TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ScheduledCourseSinkContainer(containerName + strings[0], tcStrings.scheduledCourseDetails(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new ScheduledCourseAddSinkContainer("scheduledcourseadd", tcStrings.newCourseSchedule(), params);
    }
}
