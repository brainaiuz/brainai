package com.edatasite.workforce.gwt.materialkanban.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

public class KanbanColumn extends SelectItem {

    private boolean minimized;
    private Integer taskCount;


    public KanbanColumn() {

    }

    public KanbanColumn(Integer id, String name) {
        super(id, name);
    }

    public KanbanColumn(Integer id, String name, String description) {
        super(id, name, description);
    }

    public KanbanColumn(Integer id, String name, boolean minimized) {
        super(id, name);
        this.minimized = minimized;
    }

    public KanbanColumn(Integer id, String name, String description, boolean minimized) {
        super(id, name, description);
        this.minimized = minimized;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }
}