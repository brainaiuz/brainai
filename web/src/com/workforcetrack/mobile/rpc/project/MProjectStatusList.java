package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: HAveANiceDay
 * Date: 16.06.11
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class MProjectStatusList {

    ArrayList<MSelectItem> projectStatus;

    public MProjectStatusList() {}

    public MProjectStatusList(SelectItem[] statusList) {
        if (statusList != null) {
            this.projectStatus = new ArrayList<>();
            for (SelectItem selectItem : statusList) {
                this.projectStatus.add(new MSelectItem(selectItem));
            }
        }
    }
    public ArrayList<MSelectItem> getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ArrayList<MSelectItem> projectStatus) {
        this.projectStatus = projectStatus;
    }
}
