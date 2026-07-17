package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashSet;


/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/11/11
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class AdvancedModuleSearchRpc implements IsSerializable {
    private HashSet<Integer> moduleSectionSearch;

    public HashSet<Integer> getModuleSectionSearch() {
        if (moduleSectionSearch == null) {
            moduleSectionSearch = new HashSet<>();
        }
        return moduleSectionSearch;
    }

    public void setModuleSectionSearch(HashSet<Integer> moduleSectionSearch) {
        this.moduleSectionSearch = moduleSectionSearch;
    }
}
