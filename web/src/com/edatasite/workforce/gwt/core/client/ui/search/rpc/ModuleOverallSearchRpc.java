package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 28/11/11
 * Time: 19:00
 * To change this template use File | Settings | File Templates.
 */
public class ModuleOverallSearchRpc implements IsSerializable {
    private HashMap<Integer, ModuleSectionRpc> moduleOveralSearchMap;

    public HashMap<Integer, ModuleSectionRpc> getModuleOveralSearchMap() {
        if (moduleOveralSearchMap == null) {
            moduleOveralSearchMap = new HashMap<>();
        }
        return moduleOveralSearchMap;
    }

    public void setModuleOveralSearchMap(HashMap<Integer, ModuleSectionRpc> moduleOveralSearchMap) {
        this.moduleOveralSearchMap = moduleOveralSearchMap;
    }
}
