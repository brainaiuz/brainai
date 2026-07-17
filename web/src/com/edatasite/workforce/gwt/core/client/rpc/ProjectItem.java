package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectItem extends SelectItem {

    private Map<String, List<CustomTableRpc>> customTableItems = new HashMap<>();


    public Map<String, List<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(Map<String, List<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public ProjectItem() {
        super();
    }

    public ProjectItem(Integer id, String name) {
        super(id, name);
    }

    public ProjectItem(Integer id, String name, boolean isSeleced) {
        super(id, name, "", isSeleced);
    }

    private boolean isManager = false;

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean isManager) {
        this.isManager = isManager;
    }

}
