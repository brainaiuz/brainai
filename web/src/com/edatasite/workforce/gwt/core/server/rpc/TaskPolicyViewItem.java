package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:15:26 PM
 */
public class TaskPolicyViewItem implements Serializable {
    private String description;
    private String name;
    private TaskPolicyItem[] items;

    public TaskPolicyItem[] getItems() {

        return items;
    }

    public void setItems(TaskPolicyItem[] items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
