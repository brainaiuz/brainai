package com.workforcetrack.mobile.rpc.timesheet;

import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/14/11
 * Time: 10:38 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MProjectTree extends MSelectItem{

    private List<MProjectTask> tasks;


    public MProjectTree(){

    }

    public MProjectTree(ProjectTreeForMobile projectTreeForMobile){
        if (projectTreeForMobile != null) {
            this.setObjectID(projectTreeForMobile.getId());
            this.setName(projectTreeForMobile.getName());
            this.setDescription(projectTreeForMobile.getDescription());

            if (projectTreeForMobile.getTasks() != null) {
                this.tasks = new ArrayList<>();
                for (ProjectTaskForMobile projectTask : projectTreeForMobile.getTasks()) {
                    this.tasks.add(new MProjectTask(projectTask));
                }
            }
        }

    }


    public List<MProjectTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<MProjectTask> tasks) {
        this.tasks = tasks;
    }
}
