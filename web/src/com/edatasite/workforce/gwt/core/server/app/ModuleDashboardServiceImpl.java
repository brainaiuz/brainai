package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsRolePermission;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboard;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboardSetupConfiguration;
import com.edatasite.workforce.core.domain.dashboard.EdsDefaultComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsUserDashboardSettings;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardDefaultComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.UserDashboardSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardSetupConfigurationManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DefaultComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.UserDashboardSettingsManager;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 14:34
 */
@Service("moduleDashboardService")
public class ModuleDashboardServiceImpl implements ModuleDashboardService {

    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private DashboardManager dashboardManager;
    @Autowired
    private DefaultComponentsManager defaultComponentsManager;
    @Autowired
    private DashboardComponentsManager dashboardComponentsManager;
    @Autowired
    private UserDashboardSettingsManager userDashboardSettingsManager;
    @Autowired
    private DashboardSetupConfigurationManager dashboardSetupConfigurationManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    @Override
    @Transactional(readOnly = true)
    public ListResult<ModuleDashboardListItem> getModuleDashboardList(ListingFilterParameter fp) {
        ArrayList<ModuleDashboardListItem> result = Lists.newArrayList();

        Integer count = dashboardManager.getListCount(fp);
        if (count != null && count > 0) {
            List<EdsDashboard> edsDashboards = dashboardManager.getList(fp);
            result.addAll(edsDashboards.stream().map(EdsDashboard::getRPC).toList());
        }
        return new ListResult<>(result, count);
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<SelectItem> getModuleDashboards(ListingFilterParameter fp) {
        ArrayList<SelectItem> result = Lists.newArrayList();
        EdsUser edsUser = dashboardManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(edsUser);
        if (edsUser == null || ModuleEnum.getModule(fp.getModule()) == null) {
            return result;
        }
        fp.setObjectIDs(Lists.newArrayList(edsUser.getRoleIds()));

        List<EdsDashboard> edsDashboards = dashboardManager.getUserDashboardList(fp);
        edsDashboards.forEach(dashboard -> result.add(getAsLocalizedSelectItem(userSettings.getInternationalization(), dashboard)));
        return result;
    }

    public SelectItem getAsLocalizedSelectItem(String language, EdsDashboard dashboard) {
        if (dashboard.getLocalization() != null) {
            switch (language) {
                case "en" -> {
                    return new SelectItem(dashboard.getObjectID(), dashboard.getLocalization().getEnglishName(), null, dashboard.isDefault());
                }
                case "ar" -> {
                    return new SelectItem(dashboard.getObjectID(), dashboard.getLocalization().getArabicName(), null, dashboard.isDefault());
                }
                case "ru" -> {
                    return new SelectItem(dashboard.getObjectID(), dashboard.getLocalization().getRussianName(), null, dashboard.isDefault());
                }
                case "uz" -> {
                    return new SelectItem(dashboard.getObjectID(), dashboard.getLocalization().getUzbekName(), null, dashboard.isDefault());
                }
            }
        }
        return new SelectItem(dashboard.getObjectID(), dashboard.getName(), null, dashboard.isDefault());
    }

    private void setDashboardLocalization(String language, EdsDashboard dashboard) {
        switch (language) {
            case "en" -> dashboard.setName(dashboard.getLocalization().getEnglishName());
            case "ar" -> dashboard.setName(dashboard.getLocalization().getArabicName());
            case "ru" -> dashboard.setName(dashboard.getLocalization().getRussianName());
            case "uz" -> dashboard.setName(dashboard.getLocalization().getUzbekName());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleDashboardListItem getModuleDashboardItem(Integer objectId) {
        ModuleDashboardListItem result = new ModuleDashboardListItem();
        if (objectId == null) {
            return result;
        }
        EdsDashboard edsDashboard = dashboardManager.get(objectId);
        if (edsDashboard != null && !edsDashboard.isDeleted()) {
            result = edsDashboard.getRPC();
        }
        return result;
    }

    @Override
    @Transactional
    public Integer saveDashboardComponents(UserDashboardSettingsItem data) {
        if (data == null || data.getDashboardId() == null) {
            return -1; //Dashboard not found
        }
        if (data.getActiveComponents() == null || data.getActiveComponents().isEmpty()) {
            return -2; //Please add widgets
        }
        EdsDashboard edsDashboard = dashboardManager.get(data.getDashboardId());
        if (edsDashboard == null) {
            return -1; //Dashboard not found
        }
        List<Integer> componentIds = Lists.newArrayList();
        for (DashboardComponentItem component : data.getActiveComponents()) {
            EdsDefaultComponents edsDefaultComponent = defaultComponentsManager.getItemByCode(component.getComponentCode());
            if (edsDefaultComponent == null) {
                continue;
            }
            componentIds.add(edsDefaultComponent.getObjectID());
            EdsDashboardComponents edsComponent = dashboardComponentsManager.getItemByDashboardIdAndComponentId(data.getDashboardId(),
                    edsDefaultComponent.getObjectID());
            if (edsComponent == null) {
                edsComponent = new EdsDashboardComponents();
                edsComponent.setDashboard(edsDashboard);
                edsComponent.setComponent(edsDefaultComponent);
            }
            edsComponent.setX(component.getX());
            edsComponent.setY(component.getY());
            edsComponent.setWidth(component.getWidth());
            edsComponent.setMinWidth(component.getMinWidth());
            edsComponent.setHeight(component.getHeight());
            edsComponent.setMinHeight(component.getMinHeight());
            dashboardComponentsManager.createOrUpdate(edsComponent);
        }
        if (!componentIds.isEmpty()) {
            if (!edsDashboard.isActive()) {
                edsDashboard.setActive(true);
            }
            edsDashboard.setUpdator(dashboardManager.getUser());
            edsDashboard.setUpdatedDate(new Date());
            dashboardManager.update(edsDashboard);
            dashboardComponentsManager.deleteNotExistByIds(data.getDashboardId(), componentIds);
        }
        if (data.isApplyForAll()) {
            userDashboardSettingsManager.deleteAllByDashboardId(edsDashboard.getObjectID());
        }
        return 1; //Successfully saved
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<DashboardDefaultComponentItem> getComponentList(Integer dashboardId, ArrayList<String> componentCodes) {
        ArrayList<DashboardDefaultComponentItem> result = Lists.newArrayList();
        EdsDashboard edsDashboard = dashboardManager.get(dashboardId);
        if (edsDashboard == null || edsDashboard.getModule() == null) {
            return result;
        }
        if (componentCodes != null) {
            componentCodes.removeIf(Objects::isNull);
        }
        List<EdsDefaultComponents> edsComponents = defaultComponentsManager.getListByModuleAndComponentCodes(edsDashboard.getModule(),
                componentCodes);
        result.addAll(edsComponents.stream().map(EdsDefaultComponents::getShortRPC).toList());
        return result;
    }

    @Override
    public Integer getDashboardWidgetsMaxCount(Integer dashboardId) {
        EdsDashboard dashboard = dashboardManager.get(dashboardId);
        return (dashboard != null && dashboard.getNumberOfWidgets() != null) ? Integer.parseInt(dashboard.getNumberOfWidgets()) : -1;
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<DashboardComponentItem> getDashboardComponentList(Integer dashboardId) {
        ArrayList<DashboardComponentItem> result = Lists.newArrayList();

        if (dashboardId == null) {
            return result;
        }
        List<EdsDashboardComponents> edsDashboardComponents = dashboardComponentsManager.getListByDashboardId(dashboardId);
        result.addAll(edsDashboardComponents.stream().map(EdsDashboardComponents::getRPC).toList());

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleDashboardListItem getModuleDashboardItemForEdit(Integer objectId) {
        ModuleDashboardListItem result = new ModuleDashboardListItem();

        EdsDashboard edsDashboard = dashboardManager.get(objectId);
        if (edsDashboard != null) {
            result = edsDashboard.getRPC();

            if (edsDashboard.getAccesses() != null) {
                edsDashboard.getAccesses().removeIf(role -> EdsRole.ADMIN_CODE.equals(role.getCode()) || EdsRole.CREATOR_CODE.equals(role.getCode()));
                result.setSelectedRoleIds(edsDashboard.getAccesses()
                        .stream()
                        .filter(edsRole -> edsRole != null && (!edsRole.getDeleted() || edsRole.getCode().equals(EdsRole.ESS_USER_CODE)))
                        .map(EdsRole::getObjectID)
                        .collect(Collectors.toCollection(ArrayList::new)));
            }
        }
        ArrayList<SelectItem> allRoles = allInOneServiceLocal.getAllRoles();
        allRoles.removeIf(role -> EdsRole.ADMIN_CODE.equals(role.getCode()) || EdsRole.CREATOR_CODE.equals(role.getCode()));
        result.setRoles(allRoles);
        return result;
    }

    @Override
    @Transactional
    public Integer saveModuleDashboardItem(ModuleDashboardListItem item) {
        if (item == null || item.getModule() == null) {
            return -1;
        }
        if (dashboardManager.duplicateDashboardName(item.getObjectId(), item.getModule(), item.getName())) {
            return -2;
        }
        EdsDashboard edsDashboard = dashboardManager.get(item.getObjectId());
        if (edsDashboard == null) {
            edsDashboard = new EdsDashboard();
            edsDashboard.setCreator(dashboardManager.getUser());
            edsDashboard.setCreationDate(new Date());
        } else {
            edsDashboard.setUpdator(dashboardManager.getUser());
            edsDashboard.setUpdatedDate(new Date());
        }
        edsDashboard.setName(item.getName());
        edsDashboard.setNumberOfWidgets(item.getNumberOfWidgets());
        edsDashboard.setModule(item.getModule());
        edsDashboard.setDefault(item.isDefault());
        edsDashboard.setActive(item.isActive());

        List<EdsRole> edsRoles = new ArrayList<>();
        edsRoles.add(roleManager.get(EdsRole.ADMIN));
        edsRoles.add(roleManager.getByCode(EdsRole.CREATOR_CODE));

        if (item.getSelectedRoleIds().size() > 0) {
            for (Integer roleId : item.getSelectedRoleIds()) {
                EdsRole edsRole = roleManager.get(roleId);
                if (edsRole != null) {
                    edsRoles.add(edsRole);
                }
            }
        }
        edsDashboard.getAccesses().clear();
        edsDashboard.setAccesses(edsRoles);
        dashboardManager.createOrUpdate(edsDashboard);

        if (edsDashboard.isDefault()) {
            dashboardManager.updateDefaultDashboards(edsDashboard.getObjectID(), edsDashboard.getModule());
        }
        if (item.getModule() != null && "MYWORKSPACE".equals(item.getModule().name())) {
            EdsRolePermission rolePermissionItem;
            if (item.getSelectedRoleIds().size() > 0) {
                for (Integer roleId : item.getSelectedRoleIds()) {
                    rolePermissionItem = rolePermissionManager.getRolePermission(roleId, PermissionConstants.WORKSPACE_MAIN_MENU);
                    if (rolePermissionItem == null) {
                        rolePermissionItem = new EdsRolePermission();
                        rolePermissionItem.setRole(roleManager.get(roleId));
                        rolePermissionItem.setPermissioncode(PermissionConstants.WORKSPACE_MAIN_MENU);
                    }
                    rolePermissionItem.setPriviledgeCode(PermissionConstants.ALLOW);

                    rolePermissionManager.createOrUpdate(rolePermissionItem);
                }
            }
            if (item.getRoleMap() != null && item.getRoleMap().size() > 0) {
                for (Map.Entry<Integer, Boolean> roleMap : item.getRoleMap().entrySet()) {
                    if (!roleMap.getValue()) {
                        rolePermissionItem = rolePermissionManager.getRolePermission(roleMap.getKey(), PermissionConstants.WORKSPACE_MAIN_MENU);
                        if (rolePermissionItem == null) {
                            rolePermissionItem = new EdsRolePermission();
                            rolePermissionItem.setRole(roleManager.get(roleMap.getKey()));
                            rolePermissionItem.setPermissioncode(PermissionConstants.WORKSPACE_MAIN_MENU);
                        }
                        rolePermissionItem.setPriviledgeCode(PermissionConstants.DENY);

                        rolePermissionManager.createOrUpdate(rolePermissionItem);
                    }
                }
            }
        }

        return edsDashboard.getObjectID();
    }

    @Override
    @Transactional
    public Integer saveUserDashboardSettings(UserDashboardSettingsItem data) {
        if (data == null || data.getDashboardId() == null) {
            return -1; //Dashboard not found
        }
        EdsDashboard edsDashboard = dashboardManager.get(data.getDashboardId());
        if (edsDashboard == null) {
            return -1; //Dashboard not found
        }
        EdsUser edsUser = userDashboardSettingsManager.getUser();
        if (edsUser == null) {
            return -2; //User not found
        }
        List<Integer> componentIds = Lists.newArrayList();
        for (DashboardComponentItem widget : data.getActiveComponents()) {
            EdsDefaultComponents edsDefaultComponent = defaultComponentsManager.getItemByCode(widget.getComponentCode());
            if (edsDefaultComponent == null) {
                continue;
            }
            componentIds.add(edsDefaultComponent.getObjectID());
            EdsUserDashboardSettings edsSettings = userDashboardSettingsManager.getItemByDashboardIdAndComponentIdAndUserId(data.getDashboardId(),
                    edsDefaultComponent.getObjectID(),
                    edsUser.getObjectID());
            if (edsSettings == null) {
                edsSettings = new EdsUserDashboardSettings();
                edsSettings.setDashboard(edsDashboard);
                edsSettings.setComponent(edsDefaultComponent);
                edsSettings.setUser(edsUser);
            }
            edsSettings.setX(widget.getX());
            edsSettings.setY(widget.getY());
            edsSettings.setWidth(widget.getWidth());
            edsSettings.setMinWidth(widget.getMinWidth());
            edsSettings.setHeight(widget.getHeight());
            edsSettings.setMinHeight(widget.getMinHeight());
            userDashboardSettingsManager.createOrUpdate(edsSettings);
        }
        if (!componentIds.isEmpty()) {
            userDashboardSettingsManager.deleteNotExistByIds(data.getDashboardId(), edsUser.getObjectID(), componentIds);
        }

        return 1; //Successfully saved
    }

    @Override
    @Transactional(readOnly = true)
    public UserDashboardSettingsItem getUserDashboardSettings(Integer dashboardId) {
        UserDashboardSettingsItem result = new UserDashboardSettingsItem();

        if (dashboardId == null) {
            return result;
        }
        EdsUser currentUser = userDashboardSettingsManager.getUser();
        if (currentUser == null) {
            return result;
        }
        var edsUserComponents = userDashboardSettingsManager.getListByDashboardIdAndUserId(dashboardId, currentUser.getObjectID());
        if (edsUserComponents == null || edsUserComponents.isEmpty()) {
            List<EdsDashboardComponents> edsDashboardComponents = dashboardComponentsManager.getListByDashboardId(dashboardId);
            result.getActiveComponents().addAll(edsDashboardComponents.stream().map(EdsDashboardComponents::getRPC).toList());
            return result;
        }
        List<Integer> activeComponentIds = Lists.newArrayList();
        for (EdsUserDashboardSettings edsUserComponent : edsUserComponents) {
            result.getActiveComponents().add(edsUserComponent.getRPC());
            if (edsUserComponent.getComponent() != null) {
                activeComponentIds.add(edsUserComponent.getComponent().getObjectID());
            }
        }
        var edsInactiveComponents = userDashboardSettingsManager.getUserInactiveComponentList(dashboardId, activeComponentIds, currentUser.getObjectID());
        if (edsInactiveComponents != null && !edsInactiveComponents.isEmpty()) {
            result.getInactiveComponents().addAll(edsInactiveComponents.stream().map(EdsDashboardComponents::getRPC).toList());
        }
        return result;
    }

    @Override
    @Transactional
    public Integer deleteModuleDashboardItem(Integer objectId) {
        EdsDashboard edsDashboard = dashboardManager.get(objectId);
        if (edsDashboard == null) {
            return -1;
        }
        if (edsDashboard.getAccesses() != null) {
            edsDashboard.getAccesses().clear();
        }
        edsDashboard.setDeleted(true);
        dashboardManager.update(edsDashboard);

        return 1;
    }

    @Transactional
    public ArrayList<GettingStartedItem> getDashboardSetupConfiguration(Integer dashboardId) {
        EdsUser user = userManager.getUser();
        ArrayList<GettingStartedItem> items = new ArrayList<>();

        boolean setup = true;
        for (EdsDashboardSetupConfiguration config : dashboardSetupConfigurationManager.getListByDashboardId(dashboardId)) {
            GettingStartedItem item = config.getRPC();

            boolean hasPermission = true;
            switch (item.getType()) {
                case Constants.DASHBOARD_GETTING_STARTED.COMPANY_SETUP:
                    hasPermission = ServerUtils.hasPermission(PermissionConstants.SETTINGS_COMPANY_SETTINGS);
                    break;
                case Constants.DASHBOARD_GETTING_STARTED.INVITE_USER:
                    hasPermission = ServerUtils.hasPermission(PermissionConstants.HRMS_ADD_NEW_EMPLOYEE);
                    Integer[] allEmployeesMaxCounts = allInOneServiceLocal.getAllEmployeesMaxCount();
                    if (allEmployeesMaxCounts != null && allEmployeesMaxCounts.length > 0) {
                        if (!(allEmployeesMaxCounts[Constants.ACTIVE] > 0 || allEmployeesMaxCounts[Constants.ESS] > 0)) {
                            item.setState(GettingStartedItem.State.PASSED);
                            config.setState(GettingStartedItem.State.PASSED);
                            dashboardSetupConfigurationManager.update(config);
                        }
                    }
                    break;
                case Constants.DASHBOARD_GETTING_STARTED.USER_PROFILE:
                    hasPermission = ServerUtils.hasPermission(PermissionConstants.HRMS_EDIT_OWN_PROFILE);
                    break;
                case Constants.DASHBOARD_GETTING_STARTED.DATA_MIGRATION:
                    hasPermission = user.hasRole(EdsRole.ADMIN_CODE) || user.hasRole(EdsRole.ADMIN_LOCATION_CODE) || user.hasRole(EdsRole.DR_CODE);
                    break;
                case Constants.DASHBOARD_GETTING_STARTED.CONFIGURE_EMAIL:
                    break;
            }
            item.setEnabled(hasPermission);

            if (item.isEnabled()) {
                setup = false;
            }

            items.add(item);
        }

        return setup ? null : items;
    }

    @Transactional
    public void updateDashboardSetupConfiguration(GettingStartedItem item) {
        EdsDashboardSetupConfiguration config = dashboardSetupConfigurationManager.get(item.getId());

        if (config == null) {
            return;
        }

        config.setState(item.getState());
        dashboardSetupConfigurationManager.update(config);
    }
}
