package com.workforcetrack.mobile.rpc.crm;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 3/12/12
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCrmUrlParam {

    private Integer entityID;
    private String entityName;
    private Integer action;
    private String addParams;

    public MCrmUrlParam() {
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAddParams() {
        return addParams;
    }

    public void setAddParams(String addParams) {
        this.addParams = addParams;
    }
}
