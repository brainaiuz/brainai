package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 23.04.14
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
@Repository("moduleManager")
public class ModuleManagerImpl extends BaseManager<EdsModule> implements ModuleManager {

    public ModuleManagerImpl() {
        super(EdsModule.class);
    }

    @Override
    public List<EdsModule> getEnabledModules() {
        return find("select m from EdsModule m where m.active <> false ");
    }

    @Override
    public EdsModule getModuleByCode(String code) {
        return (EdsModule) findSingle("select m from EdsModule m where m.active <> false and m.code=? ", code);
    }

    @Override
    public EdsModule getModule(String code) {
        return (EdsModule) findSingle("select m from EdsModule m where m.code=? ", code);
    }

    @Override
    public HashSet<String> getEnabledModuleCodes() {
        HashSet<String> modules = new HashSet<>();
        List<EdsModule> edsModules = getEnabledModules();
        if (edsModules != null) {
            for (EdsModule module : edsModules) {
                modules.add(module.getCode());
            }
        }
        return modules;
    }

    @Override
    public HashSet<String> getEnabledModuleCodesByCompany(Integer companyID) {
        HashSet<String> modules = new HashSet<>();
        List<EdsModule> edsModules = getEnabledModulesByCompany(companyID);
        if (edsModules != null) {
            for (EdsModule module : edsModules) {
                modules.add(module.getCode());
            }
        }
        return modules;
    }

    @Override
    public EdsModule getModuleByCodeByCompany(Integer companyID, String code) {
        return (EdsModule) findNativeSingle("select m.* from \"" + companyID + "\".mymodule m where m.active <> false and m.code='" + code + "'", EdsModule.class);
    }

    @Override
    public void insertNative(Integer companyID, EdsModule module) {
        updateNative("update \"" + companyID + "\".mymodule set active='true' WHERE code = '" + module.getCode() + "'");
    }

    @Override
    public void deleteNative(Integer companyID, EdsModule module) {
        updateNative("update \"" + companyID + "\".mymodule set active='false' WHERE code = '" + module.getCode() + "'");
    }

    @Override
    public void updateSectionModules(String section) {
        updateNative("update " + getCompanyId() + ".mymodule set active='false',sorder=0 WHERE section = '" + section + "'");
    }

    private List<EdsModule> getEnabledModulesByCompany(Integer companyID) {
        return (List<EdsModule>) findNative("select m.* from \"" + companyID + "\".mymodule m where m.active <> false ", EdsModule.class);
    }

    @Override
    public List<EdsModule> getAllModules() {
        return find("select m from EdsModule m ");
    }

    @Override
    public List<EdsModule> getAllModulesBySection(String section) {
        return find("select m from EdsModule m where m.section =? order by sorder", section);
    }

    @Override
    public List<String> getAllModuleCodesByCompanyIdAndSection(Integer companyId, String section) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("section", section);
        return findByNamedParams("select m.code from EdsModule m where m.section = :section and m.active <> false ", params);
    }
}
