package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/11/11
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public class AdvancedSearchRpc implements IsSerializable {
    private String searchKey;
    private HashMap<SearchModuleType, AdvancedModuleSearchRpc> moduleSearchMap;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public HashMap<SearchModuleType, AdvancedModuleSearchRpc> getModuleSearchMap() {
        if (moduleSearchMap == null) {
            moduleSearchMap = new HashMap<>();
        }
        return moduleSearchMap;
    }

    public void setModuleSearchMap(HashMap<SearchModuleType, AdvancedModuleSearchRpc> moduleSearchMap) {
        this.moduleSearchMap = moduleSearchMap;
    }
}
