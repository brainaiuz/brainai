package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 28/11/11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class OverallSearchRpc implements IsSerializable {
    private HashMap<SearchModuleType, ModuleOverallSearchRpc> overallSearchMap;

    public HashMap<SearchModuleType, ModuleOverallSearchRpc> getOverallSearchMap() {
        if (overallSearchMap == null) {
            overallSearchMap = new HashMap<>();
        }
        return overallSearchMap;
    }

    public void setOverallSearchMap(HashMap<SearchModuleType, ModuleOverallSearchRpc> overallSearchMap) {
        this.overallSearchMap = overallSearchMap;
    }
}
