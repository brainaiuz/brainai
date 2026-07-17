package com.workforcetrack.mobile.rpc.base;

import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 15.05.12
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFilterData {

    private List<MSelectItem> project;
    private List<MSelectItem> priority;
    private List<MSelectItem> status;
    private List<MSelectItem> assignee;
    private List<MSelectItem> client;
    private List<MSelectItem> manager;
    private List<MSelectItem> location;


    public MFilterData() {
    }

    public List<MSelectItem> getStatus() {
        return status;
    }

    public void setStatus(List<MSelectItem> status) {
        this.status = status;
    }

    public List<MSelectItem> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<MSelectItem> assignee) {
        this.assignee = assignee;
    }

    public List<MSelectItem> getClient() {
        return client;
    }

    public void setClient(List<MSelectItem> client) {
        this.client = client;
    }

    public List<MSelectItem> getManager() {
        return manager;
    }

    public void setManager(List<MSelectItem> manager) {
        this.manager = manager;
    }

    public List<MSelectItem> getLocation() {
        return location;
    }

    public void setLocation(List<MSelectItem> location) {
        this.location = location;
    }

    public List<MSelectItem> getProject() {
        return project;
    }

    public void setProject(List<MSelectItem> project) {
        this.project = project;
    }

    public List<MSelectItem> getPriority() {
        return priority;
    }

    public void setPriority(List<MSelectItem> priority) {
        this.priority = priority;
    }
}
