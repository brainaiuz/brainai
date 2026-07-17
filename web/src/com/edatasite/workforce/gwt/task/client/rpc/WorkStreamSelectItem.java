package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 10.11.2008
 * Time: 13:47:08
 * To change this template use File | Settings | File Templates.
 */
public class WorkStreamSelectItem extends SelectItem {
    private Integer projectId;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
