package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.gwt.core.server.rpc.HostBasedModuleSettingsItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 30.06.14
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleGlobalAuthJdbcSpringManager {

    List<HostBasedModuleSettingsItem> getHostBasedModules(String host);

    void insert(String code, String host);

    HostBasedModuleSettingsItem getModulesByCodeByHost(String code, String host);

    void delete(String code, String host);
}
