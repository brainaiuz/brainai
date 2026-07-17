package com.workforcetrack.mobile.rpc.task;

import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 18.01.12
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MTaskFilterData {

    private List<MSelectItem> project;
    private List<MSelectItem> client;
    private List<MSelectItem> priority;
    private List<MSelectItem> status;
    private List<MSelectItem> assignee;

    public MTaskFilterData() {

    }

    public List<MSelectItem> getProject() {
        return project;
    }

    public void setProject(List<MSelectItem> project) {
        this.project = project;
    }

    public List<MSelectItem> getClient() {
        return client;
    }

    public void setClient(List<MSelectItem> client) {
        this.client = client;
    }

    public List<MSelectItem> getPriority() {
        return priority;
    }

    public void setPriority(List<MSelectItem> priority) {
        this.priority = priority;
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
}
