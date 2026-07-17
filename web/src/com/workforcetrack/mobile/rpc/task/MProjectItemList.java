package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.workforcetrack.mobile.rpc.project.MProjectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "projectItemList")
public class MProjectItemList {

    private List<MProjectItem> projectItem;

    public MProjectItemList (){

    }

    public MProjectItemList (ProjectItem[] projectItems){
        if (projectItems != null){
            this.projectItem = new ArrayList<>();
            for (ProjectItem projectItem : projectItems) {
                this.projectItem.add(new MProjectItem(projectItem));
            }
        }

    }


    public List<MProjectItem> getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(List<MProjectItem> projectItem) {
        this.projectItem = projectItem;
    }
}
