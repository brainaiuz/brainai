package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 13.01.12
 * Time: 19:54
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MTaskAssignee {
    private Integer objectID;
    private String name;

    public MTaskAssignee(){

    }

    public MTaskAssignee(TaskInvolvedMember item){
        this.objectID = item.getEmployeeID();
        this.name = item.getEmployee();
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

