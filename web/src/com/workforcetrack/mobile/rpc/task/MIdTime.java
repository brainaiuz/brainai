package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/21/11
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taskAssignee")
public class MIdTime {

    private Integer objectID;
    private Integer time;       // set estimated time

    public MIdTime() {

    }

    public MIdTime(Integer objectID, Integer time) {
        this.objectID = objectID;
        this.time = time;
    }

    public MIdTime(IdTime idTime) {
        if (idTime != null) {
            this.objectID = idTime.getId();
            this.time = idTime.getTime();
        }
    }


    public IdTime convertToIdTime() {
        IdTime idTime = new IdTime();
        idTime.setId(this.objectID == null || this.objectID.equals(0) ? null : this.objectID);
        idTime.setTime(this.time);

        return idTime;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
