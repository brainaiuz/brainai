package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/25/11
 * Time: 10:17 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "projectList")
public class MProjectList {

    private List<MProjectListItem> projectListItem;
    private Integer totalCount;

    public MProjectList() {
    }

    public MProjectList(ProjectList projectList) {
        if (projectList != null) {
            if (projectList.getResults() != null) {
                this.projectListItem = new ArrayList<>();
                for (ProjectListItem projectListItem : projectList.getResults()) {
                    this.projectListItem.add(new MProjectListItem(projectListItem));
                }
            }
            this.totalCount = projectList.getTotalCount();
        }
    }

    public MProjectList(ListResult<ProjectListItem> projectList) {
        if (projectList != null) {
            this.totalCount = projectList.getTotal();
            this.projectListItem = new ArrayList<>();
            for (ProjectListItem projectListItem : projectList.getList()) {
                this.projectListItem.add(new MProjectListItem(projectListItem));
            }
        }
    }

    public List<MProjectListItem> getProjectListItem() {
        return projectListItem;
    }

    public void setProjectListItem(List<MProjectListItem> projectListItem) {
        this.projectListItem = projectListItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
