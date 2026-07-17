package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.AddCourseSubjectSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.CourseSubjectViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 25.12.12
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class CourseSubjectHistoryProcessor implements HistoryProcessor {
    public static final TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new CourseSubjectViewSinksContainer(containerName + strings[0], Property.get("courseSubject", tcStrings.summaryView(), tcStrings.courseSubjectView()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new AddCourseSubjectSinksContainer("courseSubjectadd", Property.get("courseSubject", tcStrings.addMess(), tcStrings.courseSubject()), params);
    }
}
