package com.workforcetrack.mobile.rpc.timesheet;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/14/11
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "projectTreeList")
public class MProjectTreeList {

    private ArrayList<MProjectTree> projectTreeListItem;


    public MProjectTreeList() {
    }

    public MProjectTreeList(List<ProjectTreeForMobile> projectTreeForMobileList) {
        if (projectTreeForMobileList != null) {
            this.projectTreeListItem = new ArrayList<>();
            for (ProjectTreeForMobile projectTreeForMobile : projectTreeForMobileList) {
                this.projectTreeListItem.add(new MProjectTree(projectTreeForMobile));
            }
        }
    }


    public ArrayList<MProjectTree> getProjectTreeListItem() {
        return projectTreeListItem;
    }

    public void setProjectTreeListItem(ArrayList<MProjectTree> projectTreeListItem) {
        this.projectTreeListItem = projectTreeListItem;
    }
}
