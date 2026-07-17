package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.ModuleGlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.HostBasedModuleSettingsItem;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 23.04.14
 * Time: 19:36
 */
@Transactional
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService, ModuleServiceLocal {
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleGlobalAuthJdbcSpringManager moduleGlobalAuthJdbcSpringManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Override
    public HashSet<String> getDefaultData(Integer companyID) {
        if (companyID != null) {
            return moduleManager.getEnabledModuleCodesByCompany(companyID);
        }
        return moduleManager.getEnabledModuleCodes();
    }

    @Override
    public void save(Integer companyID, HashSet<String> codes, Boolean enable) {
        if (codes != null && codes.size() > 0) {
            if (enable) {
                for (String code : codes) {
                    EdsModule module;
                    if (companyID != null) {
                        module = moduleManager.getModuleByCodeByCompany(companyID, code);
                    } else {
                        module = moduleManager.getModuleByCode(code);
                    }
                    if (module == null) {
                        module = new EdsModule();
                    }
                    module.setCode(code);
                    if (companyID != null) {
                        if (module.getObjectID() == null) {
                            moduleManager.insertNative(companyID, module);
                        }
                        enableOrDisableEmployeeAssigment(companyID, code, EdsGenericSettings.YES);
                    } else {
                        moduleManager.createOrUpdate(module);
                        enableOrDisableEmployeeAssigment(companyID, code, EdsGenericSettings.YES);
                    }
                }
            } else {
                for (String code : codes) {
                    EdsModule module;
                    if (companyID != null) {
                        module = moduleManager.getModuleByCodeByCompany(companyID, code);
                    } else {
                        module = moduleManager.getModuleByCode(code);
                    }
                    if (module != null) {
                        if (companyID != null) {
                            moduleManager.deleteNative(companyID, module);
                            enableOrDisableEmployeeAssigment(companyID, code, EdsGenericSettings.NO);
                        } else {
                            moduleManager.delete(module);
                            enableOrDisableEmployeeAssigment(companyID, code, EdsGenericSettings.NO);
                        }
                    }
                }
            }
        }
    }

    private void enableOrDisableEmployeeAssigment(Integer companyID, String code, String value) {
        if (PermissionConstants.MONTHLY_TIMESHEET.equals(code)) {
            genericSettingsManager.saveGenericSettings(companyID, GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE, value);
        }
    }

    @Override
    public HashSet<String> getHostBasedModule(String host, boolean copyFromApp) {
        HashSet<String> modules = new HashSet<>();
        List<HostBasedModuleSettingsItem> moduleSettingsItems = moduleGlobalAuthJdbcSpringManager.getHostBasedModules(host);
        if (moduleSettingsItems != null && moduleSettingsItems.size() > 0) {
            for (HostBasedModuleSettingsItem module : moduleSettingsItems) {
                modules.add(module.getCode());
            }
        } else if (copyFromApp) {
            moduleSettingsItems = moduleGlobalAuthJdbcSpringManager.getHostBasedModules(Constants.HOST_LIVE);
            for (HostBasedModuleSettingsItem module : moduleSettingsItems) {
                modules.add(module.getCode());
                moduleGlobalAuthJdbcSpringManager.insert(module.getCode(), host);
            }
        }
        return modules;
    }

    @Override
    public void saveModules(String host, HashSet<String> codes, boolean isSave) {
        if (codes != null && codes.size() > 0 && host != null && !"".equals(host)) {
            if (isSave) {
                for (String code : codes) {
                    HostBasedModuleSettingsItem moduleSettingsItem = moduleGlobalAuthJdbcSpringManager.getModulesByCodeByHost(code, host);
                    if (moduleSettingsItem != null && !"".equals(moduleSettingsItem.getCode())) {
                    } else {
                        moduleGlobalAuthJdbcSpringManager.insert(code, host);
                    }
                }
            } else {
                for (String code : codes) {
                    moduleGlobalAuthJdbcSpringManager.delete(code, host);
                }
            }
        }
    }

    @Override
    public ArrayList<SelectItem> getAllHosts(String host) {
        Vector<String> whiteLabelDomains =  globalAuthJdbcSpringManager.getAllWhiteLabelDomains();
        int i = 0;
        ArrayList<SelectItem> items = new ArrayList<>();
        if (whiteLabelDomains != null && whiteLabelDomains.size() > 0) {
            for (String domain : whiteLabelDomains) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(i);
                selectItem.setName(domain);
                if (host.equals(domain)) {
                    selectItem.setSelected(true);
                }
                items.add(selectItem);
                i++;
            }
            whiteLabelDomains.clear();
        }
        return items;
    }

    public boolean hasEnabled(String code) {
        if (ServerUtils.isNullOrEmpty(code)) {
            return false;
        }
        EdsModule module = moduleManager.getModuleByCode(code);
        return module != null;
    }
}
