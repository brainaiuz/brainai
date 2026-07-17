package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jun 2, 2010
 * Time: 8:38:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetFilterData implements IsSerializable {

    private SelectItem[] projects;
    private SelectItem[] workstreams;
    private SelectItem[] clients;

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public SelectItem[] getWorkstreams() {
        return workstreams;
    }

    public void setWorkstreams(SelectItem[] workstreams) {
        this.workstreams = workstreams;
    }
}
