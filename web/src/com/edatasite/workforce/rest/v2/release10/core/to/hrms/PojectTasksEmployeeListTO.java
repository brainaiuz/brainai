package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListData;

import java.util.List;

public class PojectTasksEmployeeListTO extends RequestListData {

    private List<ProjectTasksTO> projects;
    private String date;

    public PojectTasksEmployeeListTO() {
    }

    public List<ProjectTasksTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectTasksTO> projects) {
        this.projects = projects;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
