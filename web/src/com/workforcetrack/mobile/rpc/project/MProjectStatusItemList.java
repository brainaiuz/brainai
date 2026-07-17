package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.project.client.rpc.ProjectStatusItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProjectStatusItemList {

    List<MProjectStatusItem> projectStatus;
    Integer totalCount;

    public MProjectStatusItemList() {
    }

    public MProjectStatusItemList(List<ProjectStatusItem> projectStatusListItems, int totalCount) {
        if (projectStatusListItems != null) {
            projectStatus = new ArrayList<>();
            for (ProjectStatusItem projectStatusItem : projectStatusListItems) {
                projectStatus.add(new MProjectStatusItem(projectStatusItem));
            }
            this.totalCount = totalCount;
        }
    }

    public MProjectStatusItemList (List<MProjectStatusItem> mProjectStatusItems, Integer totalCount) {
        if (mProjectStatusItems != null && mProjectStatusItems.size() > 0) {
            this.totalCount = totalCount;
            this.projectStatus = mProjectStatusItems;
        }
    }

    public MProjectStatusItemList (MProjectStatusItem[] mProjectStatusItems, Integer totalCount) {
        if (mProjectStatusItems != null && mProjectStatusItems.length > 0) {
            this.totalCount = totalCount;
            this.projectStatus = new ArrayList<>();
            this.projectStatus = Arrays.asList(mProjectStatusItems);
        }
    }

    public List<MProjectStatusItem> getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(List<MProjectStatusItem> projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
