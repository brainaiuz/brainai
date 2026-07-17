package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CourseAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CourseViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/20/12
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class CourseHistoryProcessor implements HistoryProcessor {
    private static final TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CourseViewSinksContainer(containerName + strings[0], "Course View", strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        if (params.length == 2 && params[1] != null) {
            return new CourseAddSinksContainer("courseadd", tcStrings.editCourseView(), params);
        } else {
            return new CourseAddSinksContainer("courseadd", tcStrings.addCourseView(), params);
        }
    }
}
