package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class ProjectCalendarView extends View {

    private Integer projectId;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    public ProjectCalendarView(Integer projectId) {
        super(""/*"googlecalendar"*/, projectStrings.projectsCalendar());
        this.projectId = projectId;
    }

    public String getDescription() {
        return projectStrings.projectsCalendar();
    }

    public String getIconStyle() {
        return null;
    }

    public void registerSectionInHistory(SinksContainer sinksContainer) {

    }

    public void show() {
        /*ProjectService.App.get().getProjectForEdit(this.projectId, new AsyncCallback() {
              public void failure(Throwable caught) {

              }

              public void success(Object result) {
                  EditProject project = (EditProject) result;
                  getProjectEvents(project.getName());
              }
          });*/


    }

    private void getProjectEvents(final String projectName) {

    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
