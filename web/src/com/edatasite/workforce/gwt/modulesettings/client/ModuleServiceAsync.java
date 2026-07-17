package com.edatasite.workforce.gwt.modulesettings.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 23.04.14
 * Time: 19:32
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleServiceAsync {

    void getDefaultData(Integer companyID, AsyncCallback<HashSet<String>> callback);

    void save(Integer companyID, HashSet<String> codes, Boolean save, AsyncCallback<Void> callback);

    void getHostBasedModule(String selectedHost, boolean copyFromApp, AsyncCallback<HashSet<String>> asyncCallback);

    void saveModules(String selectedHost, HashSet<String> codes, boolean save, AsyncCallback<Void> asyncCallback);

    void getAllHosts(String hostName, AsyncCallback<ArrayList<SelectItem>> asyncCallback);

    void hasEnabled(String code, AsyncCallback<Boolean> asyncCallback);
}
