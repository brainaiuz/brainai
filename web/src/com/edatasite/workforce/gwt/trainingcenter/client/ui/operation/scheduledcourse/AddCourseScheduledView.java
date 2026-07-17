package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/2/12
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddCourseScheduledView extends CourseScheduledView implements Colapse {
    private static TCStrings tcStrings = TCStrings.App.get();

    public AddCourseScheduledView() {
        super("addscheduledcourse", tcStrings.addScheduledCourse());
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
