package com.edatasite.workforce.gwt.modulesettings.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 23.04.14
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleService extends RemoteService {

    HashSet<String> getDefaultData(Integer companyID);

    void save(Integer companyID, HashSet<String> codes, Boolean isSave);

    HashSet<String> getHostBasedModule(String host, boolean copyFromApp);

    void saveModules(String host, HashSet<String> codes, boolean save);

    ArrayList<SelectItem> getAllHosts(String host);

    boolean hasEnabled(String code);

    class App {
        public static ModuleServiceAsync get() {
            ServiceDefTarget target = GWT.create(ModuleService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/module");
            return (ModuleServiceAsync) target;
        }
    }
}
