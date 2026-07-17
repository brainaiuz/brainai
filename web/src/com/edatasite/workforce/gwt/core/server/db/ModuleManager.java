package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsModule;

import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 23.04.14
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleManager extends Manager<EdsModule> {

    List<EdsModule> getEnabledModules();

    EdsModule getModuleByCode(String code);

    EdsModule getModule(String code);

    HashSet<String> getEnabledModuleCodes();

    HashSet<String> getEnabledModuleCodesByCompany(Integer companyID);

    EdsModule getModuleByCodeByCompany(Integer companyID, String code);

    void insertNative(Integer companyID, EdsModule module);

    void deleteNative(Integer companyID, EdsModule module);

    List<EdsModule> getAllModules();

    List<EdsModule> getAllModulesBySection(String section);

    void updateSectionModules(String section);

    List<String> getAllModuleCodesByCompanyIdAndSection(Integer companyId, String section);
}
