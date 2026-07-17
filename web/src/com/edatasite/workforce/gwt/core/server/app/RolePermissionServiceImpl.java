package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import com.edatasite.workforce.gwt.profile.client.rpc.PermissionColumnsItem;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.LOCALE_PAYROLL;
import static com.edatasite.workforce.gwt.trainingcenter.client.TCConstants.*;

/**
 * User: Aziz
 */
@Transactional
@Service("rolePermissionService")
public class RolePermissionServiceImpl implements RolePermissionService, RolePermissionServiceLocal, Constants, Errors {

    private static final Logger log = LoggerFactory.getLogger(RolePermissionServiceImpl.class);
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private RolePermissionHistoryManager rolePermissionHistoryManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private ReportingPermissionManager reportingPermissionManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonMessageSource;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private ContainerManager containerManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private ContainerItemManager containerItemManager;
    @Autowired
    private ModuleLocalizeManager moduleLocalizeManager;
    @Autowired
    private TwilioSettingsManager twilioSettingsManager;
    @Autowired
    private EmployeeAsteriskManager employeeAsteriskManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    private ArrayList<PermissionItem> permissionRoleList = null;
    private List<RoleListItem> roleList = null;

    /**
     * current user's permission set
     *
     * @return
     */

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RoleListItem getRole(Integer id) {
        RoleListItem item = new RoleListItem();
        if (id != null) {
            EdsRole roleItem = roleManager.get(id);
            item.setObjectID(roleItem.getObjectID());
            item.setName(item.getSystem() != null && item.getSystem() ? commonMessageSource.localize(roleItem.getName()) : roleItem.getName());
            item.setDescription(roleItem.getDescription());
            item.setCode(roleItem.getCode());
            item.setSystem(roleItem.getSystem());
            item.setActive(roleItem.isActive());
        }
        return item;
    }

    @Override
    public EdsUser checkForArtificateRoles(int candidateID) {
        EdsUser user = userManager.getUser();
        EdsCrmContact candidate = crmContactManager.get(candidateID);
        user.clearArtificialRoles();
        if (candidate.getOwner() != null && user.getObjectID().equals(candidate.getOwner().getObjectID())) {
            user.addArtificialRole(roleManager.getByCode(Constants.CREATOR));
        }
        return user;
    }

    @Override
    public ListResult<PermissionItem> getPermissionListByContext(ListingFilterParameter filterParameter) {
        Map<EdsPermission, String> list = permissionManager.listRolePermission(filterParameter.getSection());
        List<RoleListItem> roleList = getRoleList(filterParameter.getSection(), filterParameter.isAllByFilter());
        ArrayList<PermissionItem> permissionList = new ArrayList<>();//new ListResult<PermissionItem>();
        ArrayList<Integer> existPermisisonIds = new ArrayList<>();
        ArrayList<Boolean> selectAll = new ArrayList<>();
        for (int i = 0; i < roleList.size(); i++) {
            selectAll.add(true);
        }
        PermissionItem zeroItem = new PermissionItem();
        zeroItem.setRightChar("");
        zeroItem.setRightCharCount(0);
        permissionList.add(zeroItem);

        getPermissionByRec(permissionList, existPermisisonIds, list, 0, roleList, 0, "", filterParameter.getSection(), selectAll);
        int indexer = 0;
        for (RoleListItem itemRole : roleList) {
            if (Boolean.TRUE.equals(selectAll.get(indexer))) {
                permissionList.get(0).setRole(itemRole.getObjectID(), Boolean.TRUE);
            }
            indexer++;
        }
        getParentLostPermissions(permissionList, existPermisisonIds, list, roleList, filterParameter.getSection(), selectAll);

        return new ListResult<>(permissionList, list.size());
    }

    @Override
    public ArrayList<PermissionItem> getReportingPermissionList(ListingFilterParameter filterParameter) {
        ArrayList<PermissionItem> permissionList = getPermissionItemListFromQueryResult();
        Map<String, Map<String, List<PermissionItem>>> categoryMap = new HashMap<>();
        Map<String, List<PermissionItem>> folderMap;

        roleList = getRoleList(filterParameter.getSection(), filterParameter.isAllByFilter());
        permissionList = checkPermissionRole(permissionList, roleList);

        for (PermissionItem item : permissionList) {
            item.setRoleList(roleList);
            folderMap = categoryMap.get(item.getCategoryName());
            if (folderMap == null) folderMap = new TreeMap<>();
            List<PermissionItem> permissionItemList = folderMap.get(item.getFolderName());
            if (permissionItemList == null) permissionItemList = new ArrayList<>();
            permissionItemList.add(item);
            permissionItemList.sort(Comparator.comparing(PermissionItem::getName));
            folderMap.put(item.getFolderName(), permissionItemList);
            categoryMap.put(item.getCategoryName(), folderMap);
        }
        return initsializationStructureForReporting(new TreeMap<>(categoryMap), roleList);
    }

    @Override
    public ArrayList<PermissionItem> updateReportingPermission(RoleListItem roleItem, Boolean value, PermissionItem permissionItem) {

        if (permissionRoleList != null) {
            if (permissionItem.getType() != null) {
                String[] splts = permissionItem.getType().split(",");
                String category = splts[0];
                String folder = splts.length > 1 ? splts[1] : null;
                if (folder != null) {
                    for (PermissionItem item : permissionRoleList) {
                        if (category.equals(item.getCategoryName()) && folder.equals(item.getFolderName())) {
                            item.setRole(roleItem.getObjectID(), value);
                            saveRolePermission(PermissionConstants.REPORTING, item.getObjectId() + "_" + roleItem.getObjectID(), value);
                        } else {
                        }
                    }
                } else {
                    for (PermissionItem item : permissionRoleList) {
                        if (category.equals(item.getCategoryName())) {
                            item.setRole(roleItem.getObjectID(), value);
                            saveRolePermission(PermissionConstants.REPORTING, item.getObjectId() + "_" + roleItem.getObjectID(), value);
                        } else {
                        }
                    }
                }
            } else {
                saveRolePermission(PermissionConstants.REPORTING, permissionItem.getObjectId() + "_" + roleItem.getObjectID(), value);
            }
        }
        return permissionRoleList;
    }

    private ArrayList<PermissionItem> checkPermissionRole(List<PermissionItem> list, List<RoleListItem> roleList) {
        ArrayList<PermissionItem> itemList = new ArrayList<>();
        LinkedHashMap<String, String> rolePermissionMap = permissionManager.getPermissionRoleAccess();
        for (PermissionItem item : list) {
            for (RoleListItem role : roleList) {
                if (rolePermissionMap.get(item.getCode()) != null && rolePermissionMap.get(item.getCode()).contains(role.getCode())) {
                    item.setRole(role.getObjectID(), Boolean.TRUE);
                } else {
                    item.setRole(role.getObjectID(), Boolean.FALSE);
                }
            }
            itemList.add(item);
        }
        return itemList;
    }

    private ArrayList<PermissionItem> getPermissionItemListFromQueryResult() {
        List<Object[]> list = permissionManager.getReportingPermissionsList();
        ArrayList<PermissionItem> permissionList = new ArrayList<>();
        for (Object[] item : list) {
            //// parsing results sql query from object to PermissionItem
            String categoryName = String.valueOf(item[4]);
            String folderName = String.valueOf(item[3]);
            String permissionName = String.valueOf(item[1]);
            PermissionItem permissionItem = new PermissionItem();
            permissionItem.setObjectId(Integer.parseInt(item[0].toString())); // reporting permission id
            permissionItem.setName(commonMessageSource.localize(String.valueOf(item[2].toString()), permissionName));             // reporting name
            permissionItem.setCode(String.valueOf(item[2].toString()));  // reporting permission code
            permissionItem.setFolderName(folderName);     // reporting folder name
            permissionItem.setCategoryName(categoryName);        // category name
            permissionList.add(permissionItem);
        }
        return permissionList;
    }

    private void getParentLostPermissions(List<PermissionItem> generatedList, ArrayList<Integer> existPermisisonIds, Map<EdsPermission, String> permissions, List<RoleListItem> roleList, String context, ArrayList<Boolean> selectAll) {
        for (Map.Entry<EdsPermission, String> entry : permissions.entrySet()) {
            if (existPermisisonIds.contains(entry.getKey().getObjectID())) {
                continue;
            }
            PermissionItem item = new PermissionItem();
            item.setObjectId(entry.getKey().getObjectID());
            item.setCode(entry.getKey().getCode());
            item.setContext(context);
            item.setName(commonMessageSource.localize(entry.getKey().getCode(), entry.getKey().getName()));
            item.setSorder(entry.getKey().getSorder());
            item.setDescription(entry.getKey().getDescription());
            item.setRightCharCount(3);
            item.setRightChar("&nbsp;&nbsp;&nbsp;&nbsp;");
            String[] hasPermissionedRoles = (entry.getValue() != null ? entry.getValue().split(",") : new String[]{});
            List<String> roleItems = new ArrayList<>(Arrays.asList(hasPermissionedRoles));
            for (int j = 0; j < roleList.size(); j++) {
                boolean t = roleItems.contains(roleList.get(j).getCode());
                if (t) {
                    item.setRole(roleList.get(j).getObjectID(), Boolean.TRUE);
                }
                if (selectAll.get(j) && !t) {
                    selectAll.set(j, false);
                }
            }
            generatedList.add(item);
        }
    }

    private void getPermissionByRec(List<PermissionItem> generatedList, ArrayList<Integer> existPermisisonIds, Map<EdsPermission, String> permissions, int parent, List<RoleListItem> roleList, int count, String rightChar, String context, ArrayList<Boolean> selectAll) {
        Map<EdsPermission, String> permissionsExtra = new LinkedHashMap<>();
        Map<EdsPermission, String> permissionsItems = new LinkedHashMap<>();
        for (Map.Entry<EdsPermission, String> entry : permissions.entrySet()) {
            if (entry.getKey().getParent() != null) {
                if (entry.getKey().getParent() != parent) {
                    permissionsExtra.put(entry.getKey(), entry.getValue());
                } else {
                    permissionsItems.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if (permissions.size() > 0) {
            count++;
            if (parent != 0) {
                rightChar += "&nbsp;&nbsp;&nbsp;&nbsp;";
            }
        }
        for (Map.Entry<EdsPermission, String> entry : permissionsItems.entrySet()) {
            PermissionItem item;
            item = new PermissionItem();
            item.setObjectId(entry.getKey().getObjectID());
            item.setCode(entry.getKey().getCode());
            item.setContext(context);
            item.setName(commonMessageSource.localize(entry.getKey().getCode(), entry.getKey().getName()));
            item.setSorder(entry.getKey().getSorder());
            item.setDescription(entry.getKey().getDescription());
            item.setRightCharCount(count);
            item.setRightChar(rightChar);
            String[] hasPermissionedRoles = (entry.getValue() != null ? entry.getValue().split(",") : new String[]{});
            List<String> roleItems = new ArrayList<>(Arrays.asList(hasPermissionedRoles));
            for (int j = 0; j < roleList.size(); j++) {
                boolean t = roleItems.contains(roleList.get(j).getCode());
                if (t) {
                    item.setRole(roleList.get(j).getObjectID(), Boolean.TRUE);
                }
                if (selectAll.get(j) && !t) {
                    selectAll.set(j, false);
                }
            }
            generatedList.add(item);
            existPermisisonIds.add(item.getObjectId());
            getPermissionByRec(generatedList, existPermisisonIds, permissionsExtra, entry.getKey().getObjectID(), roleList, count, rightChar, context, selectAll);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PermissionColumnsItem getRolesPermissions(boolean isSuperUser) {
        PermissionColumnsItem item = new PermissionColumnsItem();
        List<EdsPermission> listContext = permissionManager.listContext();
        HashSet<String> listAvailableModules = moduleManager.getEnabledModuleCodes();
        ArrayList<PermissionItem> items = new ArrayList<>();
        PermissionItem pItem;
        String statusCode;
        for (EdsPermission edsPermission : listContext) {
            if (PermissionConstants.WORKSPACE_CONTEXT.equals(edsPermission.getContext())
                    || PermissionConstants.MYWORKSPACE_CONTEXT.equals(edsPermission.getContext())
                    || PermissionConstants.DASHBOARD_CONTEXT.equals(edsPermission.getContext())
                    || PermissionConstants.MYACCOUNT_CONTEXT.equals(edsPermission.getContext())) {
                continue;
            }
            if (!listAvailableModules.contains(edsPermission.getModuleCode())) {//This module is unavailable
                continue;
            }
            statusCode = edsPermission.getName() != null ? edsPermission.getName().replace(" ", "_") : edsPermission.getName();
            pItem = new PermissionItem();
            pItem.setObjectId(edsPermission.getObjectID());
            pItem.setCode(edsPermission.getCode());
            pItem.setContext(edsPermission.getContext());
            pItem.setName(commonMessageSource.localize(statusCode, edsPermission.getName()));
            pItem.setLocalizationName(pItem.getName());
            pItem.setRoleList(getRoleList(edsPermission.getContext(), isSuperUser));
            items.add(pItem);
        }
        item.setSectionList(items);
        item.setRoleList(getRoleList("", isSuperUser));
        Comparator<RoleListItem> nameSorter = Comparator.comparing(RoleListItem::getName);

        item.getRoleList().sort(nameSorter);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<String> getMainMenuPermissions() {
        EdsUser user = roleManager.getUser();
        if (user == null || user.getRoles() == null) {
            return new HashSet<>();
        }
        try {
            return permissionManager.getMainMenuPermissions(user);
        } catch (Exception ex) {
            log.error("SEVERE! Error while getting user's specific permission list", ex);
            ex.printStackTrace();
        }
        return new HashSet<>();


    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<String> getPermissionList(String context) {
        return getPermissionList(context, roleManager.getUser());
    }

    private String getLocalizedContainerName(String userLanguage, EdsContainer container) {
        if (container.getLocalization() != null) {
            switch (userLanguage) {
                case "en" -> {
                    return container.getLocalization().getEnglishName();
                }
                case "ar" -> {
                    return container.getLocalization().getArabicName();
                }
                case "ru" -> {
                    return container.getLocalization().getRussianName();
                }
                case "uz" -> {
                    return container.getLocalization().getUzbekName();
                }
            }
        }
        return container.getDefaultName();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PermissionSettings getPermissionSettings(String moduleName) {
        PermissionSettings settings = new PermissionSettings();
        EdsUser user = roleManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        settings.setUserLanguage(userSettings.getInternationalization());
        settings.setUserLocation(user.getLocation() != null ? user.getLocation().getAsSelectItem() : new SelectItem());
        EdsEmployee emp = employeeManager.get(user.getObjectID());
        if (emp != null && emp.getTeam() != null) {
            EdsDepartment team = employeeManager.get(user.getObjectID()).getTeam();
            settings.setUserDepartmentID(team != null ? team.getObjectID() : null);
            settings.setUserDepartmentAsSelectItem(team != null ? team.getAsSelectItem(ServerUtils.getUserLocale().getLanguage()) : new SelectItem());
        }
        if (user != null) {
            settings.setUserID(user.getObjectID().toString());
            settings.setRoles(user.getRolesAsIntegersString());
            settings.setRolesCodes(user.getRolesCodeAsString());
            settings.setEnabledModules(moduleService.getDefaultData(user.getCompany().getObjectID()));
            if (PermissionConstants.REPORTING.equals(moduleName)) {
                settings.setPermissions(getReportingPermissionList(moduleName, user));
            } else {
                settings.setPermissions(getPermissionList(moduleName, user));
            }
        }
        String[] roles = user != null ? user.getRolesCodeAsString().split(",") : new String[0];
        List<EdsProperty> properties = propertManager.findByModuleCode(moduleName);
        settings.setPropertyItemMap(properties.stream()
                .collect(Collectors.toMap(EdsProperty::getObjectName, x -> x.toItem(false), (x1, x2) -> x1, HashMap::new)));

        List<EdsContainer> containers = containerManager.getContainerBySorder(moduleName);

        if (containers != null && !containers.isEmpty()) {
            LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap = new LinkedHashMap<>();
            for (EdsContainer container : containers) {
                List<EdsContainerItem> containerItemList = containerItemManager.getItemsByContainer(container.getObjectID(), false);
                if (containerItemList != null && !containerItemList.isEmpty()) {
                    LinkedList<PropertyItem> propertyItemList = new LinkedList<>();
                    for (EdsContainerItem containerItem : containerItemList) {
                        PropertyItem propertyItem = containerItem.toItem();
                        if (propertyItem != null && propertyItem.getObjectName() != null) {
                            if (ModuleEnum.CRM.getCode().equals(moduleName)) {
                                getCrmSectionListings(user, propertyItemList, propertyItem);
                            } else if (ModuleEnum.HRMS.getCode().equals(moduleName)) {
                                getHrmsSectionListings(user, propertyItemList, propertyItem);
                            } else if (ModuleEnum.ACCOUNTING.getCode().equals(moduleName)) {
                                getAccountingSectionListings(user, propertyItemList, propertyItem);
                            } else if (ModuleEnum.PAYROLL.getCode().equals(moduleName)) {
                                getPayrollSectionListings(user, propertyItemList, propertyItem);
                            } else if (ModuleEnum.TRAINING_CENTER.getCode().equals(moduleName)) {
                                getTCSectionListings(user, propertyItemList, propertyItem);
                            } else if (ModuleEnum.PM.getCode().equals(moduleName)) {
                                getPMSectionListings(user, propertyItemList, propertyItem);
                            } else {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    }
                    SelectItem selectItem = new SelectItem();
                    selectItem.setId(container.getObjectID());
                    selectItem.setOrderId(container.getSorder());
                    selectItem.setName(container.isChanged() ? getLocalizedContainerName(userSettings.getInternationalization(), container) : commonLocalizer.localize(container.getDefaultName(), container.getDefaultName()));
                    selectItem.setCode(container.getCode());
                    selectItem.setDescription(moduleName);
                    selectItem.setCategory(container.getPreparedView());

                    if (propertyItemList != null && !propertyItemList.isEmpty()) {
                        if (ModuleEnum.CRM.getCode().equals(container.getModuleCode())) {
                            getCrmSectionTabs(propertyListingsMap, container, propertyItemList, selectItem);
                        } else if (ModuleEnum.HRMS.getCode().equals(container.getModuleCode())) {
                            getHrmsSectionTabs(propertyListingsMap, container, propertyItemList, selectItem);
                        } else if (ModuleEnum.ACCOUNTING.getCode().equals(container.getModuleCode())) {
                            getAccountingSectionTabs(propertyListingsMap, container, propertyItemList, selectItem);
                        } else if (ModuleEnum.TRAINING_CENTER.getCode().equals(container.getModuleCode())) {
                            getTCSectionTabs(propertyListingsMap,  propertyItemList, selectItem);
                        } else if (ModuleEnum.PM.getCode().equals(container.getModuleCode())) {
                            getPMSectionTabs(propertyListingsMap,  propertyItemList, selectItem);
                        } else if (ModuleEnum.PAYROLL.getCode().equals(container.getModuleCode())) {
                            if (hasPermission(PermissionConstants.PAYROLL_MAIN_CONTENT) && container.getCode().equals(PAYROLL_HOME)) {
                                getPayrollSectionTabs(propertyListingsMap, propertyItemList, selectItem);
                            } else if (container.getCode().equals(MY_PAYROLL_HOME) && roles.length == 1 && roles[0].equals("MEM")) {
                                getPayrollSectionTabs(propertyListingsMap, propertyItemList, selectItem);
                            }
                            if (hasPermission(PermissionConstants.PAYROLL_REPORTS) && PAYROLL_REPORT_HOME.equals(container.getCode())) {
                                getPayrollSectionTabs(propertyListingsMap, propertyItemList, selectItem);
                            }
                        } else {
                            propertyListingsMap.put(selectItem, propertyItemList);
                        }
                    }
                }
            }
            settings.setPropertyListingsMap(propertyListingsMap);
        }
        settings.setGenericSettings(Sets.newHashSet(genericSettingsManager.getEnabledGenericSettings()));
        List<EdsModuleLocalize> moduleLocalizes = moduleLocalizeManager.listModuleLocalize();
        if (moduleLocalizes != null && !moduleLocalizes.isEmpty()) {
            HashMap<String, String> moduleLocalizeMap = new HashMap<>();
            for (EdsModuleLocalize moduleLocalize : moduleLocalizes) {
                moduleLocalizeMap.put(moduleLocalize.getModuleCode(), moduleLocalize.getName());
            }
            settings.setModuleLocalizeMap(moduleLocalizeMap);
        }
        //Load Twilio Settings
        List<EdsTwilioSettings> twilioSettings = twilioSettingsManager.list(new ListingFilterParameter());
        settings.setTwilioNumbers(twilioSettings.stream().map(ts -> new SelectItem(ts.getObjectID(), ts.getNumber())).collect(Collectors.toCollection(ArrayList::new)));

        //Load Asterisk Settings
        List<EdsEmployeeAsterisk> asteriskSettings = employeeAsteriskManager.getEmployeeAsteriskSettings(user.getObjectID());
        if (asteriskSettings != null) {
            ArrayList<AsteriskSettings> rpcAsterisk = asteriskSettings.stream().filter(s -> s.getAsteriskSettings() != null && StringUtils.isNotBlank(s.getUsername()) && StringUtils.isNotBlank(s.getPassword()) && s.isActive()).map(asterisk -> {
                AsteriskSettings astr = new AsteriskSettings();
                astr.setAsteriskNumber(asterisk.getAsteriskSettings().getNumber());
                astr.setAsteriskUsername(asterisk.getUsername());
                astr.setAsteriskPassword(asterisk.getPassword());
                astr.setAsteriskHost(asterisk.getAsteriskSettings().getAsteriskHost());
                astr.setAsteriskPort(asterisk.getAsteriskSettings().getAsteriskPort());
                return astr;
            }).collect(Collectors.toCollection(ArrayList::new));

            settings.setAsteriskSettings(rpcAsterisk);
        }

        return settings;
    }

    private void getAccountingSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, EdsContainer container, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWareHouseEnabled = financialSettings.getEnableMultiWarehouse();

        switch (container.getCode()) {
            case "accounting" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_ACCOUNTING_MENU)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "transaction" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_TRANSACTION_MENU)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "report" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_REPORTS_MENU)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "warehouse" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSE_MENU) && isMultiWareHouseEnabled) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "production" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PRODUCTION_MENU)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            default -> propertyListingsMap.put(selectItem, propertyItemList);
        }
    }

    private void getHrmsSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, EdsContainer container, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        switch (container.getCode()) {
            case HRMS_MAIN -> {
                if (hasPermission(PermissionConstants.HRMS_SECTION)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "employee" -> {
                if (hasPermission(PermissionConstants.HRMS_CURENT_EMPLOYEE_PROFILE_TAB)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "availability" -> {
                if (hasPermission(PermissionConstants.HRMS_ATTENDANCE_TRACKING_TAB)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "recruitment" -> {
                if (hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_GOAL_MANAGEMENT)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case "pa" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_HRMS_PERFORMANCE_APPRAISALS) && hasPermission(PermissionConstants.HRMS_PERFORMANCE_APPRAISALS)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            default -> propertyListingsMap.put(selectItem, propertyItemList);
        }
    }

    private void getCrmSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, EdsContainer container, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        switch (container.getCode()) {
            case CRM_WELCOME -> {
                if (hasPermission(PermissionConstants.CRM_SALES_TAB)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case CRM_ACCOUNT_LIST_2 -> {
                if (hasPermission(PermissionConstants.CUSTOMER_SERVICE_TAB)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            case EMAIL_MARKETING -> {
                if (hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                    propertyListingsMap.put(selectItem, propertyItemList);
                }
            }
            default -> propertyListingsMap.put(selectItem, propertyItemList);
        }
    }

    private void getPayrollSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        propertyListingsMap.put(selectItem, propertyItemList);
    }

    private void getPayrollSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        switch (propertyItem.getObjectName()) {
            case EMLOYEE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SINGLE_PAYRUN_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_PAYSLIP_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PAYSLIP_TABLE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CASH_ADVANCE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BENEFIT_REQUESTS -> {
                if (hasPermission(PermissionConstants.MY_BENEFIT_REQUEST_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EMPLOYEE_TEMPLATE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_PENDING_CHANGES)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case ADDITIONAL_PAYMENT_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case ADDITIONAL_PAYMENT_ITEM_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case END_OF_SERVICE_GRATUITY -> {
                if (hasPermission(PermissionConstants.END_OF_SERVICE_GRATUITY_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case OVERTIME -> {
                if (hasPermission(PermissionConstants.PAYROLL_OVERTIME_LIST) && genericSettingsManager.isSettingsEnabled(LOCALE_PAYROLL)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case WPS_REPORT -> {
                if (hasPermission(PermissionConstants.PAYROLL_WPS_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case END_OF_SERVICE_REPORT -> {
                if (hasPermission(PermissionConstants.PAYROLL_END_OF_SERVICE_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PENSION_CONTRIBUTION_REPORT -> {
                if (hasPermission(PermissionConstants.PAYROLL_PENSION_CONTRIBUTION_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CASH_ADVANCE_REPORT -> propertyItemList.add(propertyItem);
            case SALARY_REPORT -> propertyItemList.add(propertyItem);
            case SALARY_TRANSACTIONS -> propertyItemList.add(propertyItem);
            case MULTI_CASH_ADVANCE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_MULTI_CASH_ADVANCE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RECURRING_PAY_DEDUCTION_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_RECURRING_PD_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }


    private void getTCSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        propertyListingsMap.put(selectItem, propertyItemList);
    }
    private void getTCSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        switch (propertyItem.getObjectName()) {
            case COURSE_SCHEDULES -> {
                if (hasPermission(PermissionConstants.TC_SCHEDULED_COURSE_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TC_COURSE -> {
                if (hasPermission(PermissionConstants.TC_COURSE_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "coursesubject" -> {
                if (hasPermission(PermissionConstants.TC_COURSE_SUBJECT_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case  BOOKINGITEMS_LIST-> {
                if (hasPermission(PermissionConstants.TC_BOOKING_ITEMS_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TC_EMLOYEE_LIST, "instructorReassignList", TC_COURSE_BOOKING, TC_TRAINING_CONTRACT, TC_CERTIFICATE,TC_PASSPORT,
                 "invoicegenerator", "scheduleinvoice","confirmedscheduledcourses" -> propertyItemList.add(propertyItem);
            case TCConstants.TC_STUDENTS-> propertyItemList.add(propertyItem);
            case TCConstants.TC_ATTENDENCE_SHEET -> propertyItemList.add(propertyItem);

            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }

    private void getCrmSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        boolean isEnableLogistics = logisticsEnabled != null;
        boolean isAccountingSetup = user.getCompany().getAccountingSetup();

        switch (propertyItem.getObjectName()) {
            case LEADS -> {
                if (hasPermission(PermissionConstants.CRM_LEADS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "budgetsheetView" -> {
                if (hasPermission(PermissionConstants.CRM_BUDGET_SHEET)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case Opportunities -> {
                if (hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SALE_QUOTE -> {
                if (hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST) && isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CRM_ACCOUNT_LIST -> {
                if (hasPermission(PermissionConstants.CRM_ACCOUNTS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case Contacts -> {
                if (hasPermission(PermissionConstants.CRM_CONTACTS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case REQUEST_FOR_QUOTE -> {
                if (isAccountingSetup && hasPermission(PermissionConstants.CRM_REQUEST_FOR_QUOTE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EVENT_LIST -> {
                if (hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CASE_LIST -> {
                if (hasPermission(PermissionConstants.CRM_CASES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SOLUTION_LIST -> {
                if (hasPermission(PermissionConstants.CRM_SOLUTIONS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TASK -> {
                if (hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case MAIL_LIST -> {
                if (hasPermission(PermissionConstants.CRM_MAILING_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "scheduled_messages" -> {
                if (hasPermission(PermissionConstants.CRM_QUEUED_MESSAGES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "SentMessages" -> {
                if (hasPermission(PermissionConstants.CRM_SENT_MESSAGES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CAMPAIGN_LIST -> {
                if (hasPermission(PermissionConstants.CRM_CAMPAIGNS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "webFormsList" -> {
                if (hasPermission(PermissionConstants.CRM_WEB_FORMS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PRODUCTS_OR_SERVICES -> {
                if (hasPermission(PermissionConstants.CRM_PRODUCT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }

    private void getHrmsSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        switch (propertyItem.getObjectName()) {
            case EMLOYEE_LIST -> {
                if (hasPermission(PermissionConstants.HRMS_EMPLOYEES)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EMPLOYEE_DOCUMENTS -> {
                if (hasPermission(PermissionConstants.EMPLOYEE_DOCUMENTS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case COMPANY_DOCUMENTS -> {
                if (hasPermission(PermissionConstants.COMPANY_DOCUMENTS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BENEFIT_REQUESTS -> {
                if (hasPermission(PermissionConstants.BENEFIT_REQUEST_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "meeting" -> {
                if (hasPermission(PermissionConstants.MEETING_MINUTES_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CERTIFICATES_LIST -> {
                if (hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "incidentList" -> {
                if (hasPermission(PermissionConstants.HRMS_INCIDENT_LIST) || hasPermission(PermissionConstants.HRMS_SEE_ALL_INCIDENT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "organizationChart" -> {
                if (hasPermission(PermissionConstants.HRMS_ORGANIZATION_CHART_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "departmentOrgChartView" -> {
                if (hasPermission(PermissionConstants.HRMS_TEAM_ORGANIZATION_CHART_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "notifications" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SERVER_PUSH_NOTIFICATION) && hasPermission(PermissionConstants.HRMS_NOTIFICATIONS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case NEWS_LIST -> {
                if (hasPermission(PermissionConstants.HRMS_COMPANY_NEWS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EMPLOYEE_PROFILE_VIEW -> {
                if (hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE_SUMMARY)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "leave_request_list" -> propertyItemList.add(propertyItem);
            case "leave_planner_list" -> propertyItemList.add(propertyItem);
            case "my_attendance" -> {
                if (propertyItem.getContainer() != null && "availability".equals(propertyItem.getContainer().getCode())) {
                    if (hasPermission(PermissionConstants.HRMS_MY_ATTENDANCE)) {
                        propertyItemList.add(propertyItem);
                    }
                } else {
                    if (hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
                        propertyItemList.add(propertyItem);
                    }
                }
            }
            case EXPENSES_CLAIM -> {
                if ((hasPermission(PermissionConstants.HRMS_EXPENCE_REPORT)) && user.getCompany().getAccountingSetup()) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CASH_ADVANCE_LIST -> {
                if (hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PAYSLIP_LIST -> {
                if (hasPermission(PermissionConstants.HRMS_PAYSLIP_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case DEPENDENT_LIST -> {
                if (hasPermission(PermissionConstants.HRMS_DEPENDENT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PERSONAL_GOAL + GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_PERSONAL_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TALENT_PROFILE -> {
                if (hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TEAM_AVAILABILITY_VIEW -> {
                if (hasPermission(PermissionConstants.HRMS_ATTENDANCE_TRACKING)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "attendanceReport" -> {
                if (hasPermission(PermissionConstants.HRMS_ATTENDANCE_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "terminalAttendance" -> {
                if (hasPermission(PermissionConstants.HRMS_TERMINAL_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "shift" -> {
                if (hasPermission(PermissionConstants.HRMS_SHIFT) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHIFT_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case GROUP_PLACEMENT -> {
                if (hasPermission(PermissionConstants.HRMS_GROUP_PLACEMENT) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.GROUP_PLACEMENT_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "brigada" -> {
                if (hasPermission(PermissionConstants.HRMS_BRIGADA) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHIFT_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "rotation" -> {
                if (hasPermission(PermissionConstants.HRMS_ROTATION)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case POSITION1 -> {
                if (hasPermission(PermissionConstants.HRMS_POSITION)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "annualLeaveBalance" -> {
                if (hasPermission(PermissionConstants.HRMS_ANNUAL_LEAVE_BALANCE_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case VACANCY -> {
                if (hasPermission(PermissionConstants.HRMS_VACANCY_LIST_VIEW) || hasPermission(PermissionConstants.HRMS_VACANCY_SEE_ALL)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CANDIDATE -> {
                if (hasPermission(PermissionConstants.HRMS_CANDIDATE_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "shortListView" -> {
                if (hasPermission(PermissionConstants.HRMS_SHORT_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PLACEMENT -> {
                if (hasPermission(PermissionConstants.HRMS_PLACEMENT_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EVENT_LIST -> {
                if (hasPermission(PermissionConstants.HRMS_ACTIVITIES_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case GROUP_GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_GROUP_PERSONAL_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case DEPARTMENT_GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PROJECT_GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_PROJECT_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BUSINESS_GOAL + GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_BUSINESS_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case COMPANY_GOAL + GOAL -> {
                if (hasPermission(PermissionConstants.HRMS_COMPANY_GOALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "simpleAppraisal" -> {
                if (hasPermission(PermissionConstants.HRMS_SIMPLE_APPRAISALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "appraisalsArchive" -> {
                if (hasPermission(PermissionConstants.HRMS_SIMPLE_APPRAISALS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "appraisalTemplate" -> {
                if (hasPermission(PermissionConstants.HRMS_TEMPLATES)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "competencesView" -> {
                if (hasPermission(PermissionConstants.HRMS_COMPETENCES)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "competencesGroupView" -> {
                if (hasPermission(PermissionConstants.HRMS_COMPETENCES)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "performanceNote" -> {
                if (hasPermission(PermissionConstants.HRMS_PERFORMANCE_NOTE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "singlePayrunList" -> {
                if (hasPermission(PermissionConstants.HRMS_SINGLE_PAYRUN_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "task" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PROJECT_MANAGEMENT_TASKS_FOR_HRMS) && hasPermission(PermissionConstants.PM_TASKS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PROJECT -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_PROJECT_MANAGEMENT_PROJECT_FOR_HRMS) && hasPermission(PermissionConstants.PM_PROJECT_LIST)) {
                    propertyItemList.add((propertyItem));
                }
            }
            case "subscriptionvendors" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SUBSCRIPTION_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "subscriptions" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SUBSCRIPTION_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "subscriptionusages" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SUBSCRIPTION_ENABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BACKUPS_EMPLOYEE -> {
                if (hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case NEW_FLAME_ORG_CHART -> propertyItemList.add(propertyItem);

            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }

    private void getAccountingSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        boolean isEnableLogistics = logisticsEnabled != null;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean isMultiWareHouseEnabled = financialSettings.getEnableMultiWarehouse();
        boolean isShowVatReturnReport = accountingService.getVatReturnReportVisibility();

        switch (propertyItem.getObjectName()) {
            case SALE_QUOTE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SALE_ORDER_CODE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SALE_INVOICE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RECURRING_INVOICE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_RECURRING_INVOICE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case REQUEST_FOR_QUOTE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case REQUEST_FOR_PURCHASE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_PURCHASE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PURCHASE_ORDER -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PURCHASE_INVOICE -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RECURRING_BILL -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_RECURRING_BILL_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case FIXED_ASSETS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_FIXED_ASSET_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EXPENSES_CLAIM -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_LIST) || hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CLIENT_LIST -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case SUPPLIER_LIST -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PRODUCTS_OR_SERVICES -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case INVENTORY_ITEMS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_INVENTORY_LIST) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RENTAL_PRODUCTS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_RENTAL_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RENTAL_ORDERS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case ASSEMBLY_PRODUCTS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_ASSEMBLY_ITEM_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BUILD_ASSEMBLY_PRODUCTS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BUILD_ASSEMBLY_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BANKACCOUNT -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TRASH_BIN -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_TRASH_BIN_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "STOCK_ADJUSTMENT" -> {
                if (propertyItem.getContainer() != null && "accounting".equals(propertyItem.getContainer().getCode())) {
                    if (!isMultiWareHouseEnabled && hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST) && !isEnableLogistics) {
                        propertyItemList.add(propertyItem);
                    } else if (isMultiWareHouseEnabled && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
                        propertyItemList.add(propertyItem);
                    }
                } else {
                    if (isMultiWareHouseEnabled && hasPermission(isEnableLogistics ? PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST : PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
                        propertyItemList.add(propertyItem);
                    }
                }
            }
            case "STOCK_OUT" -> {
                if (propertyItem.getContainer() != null && "accounting".equals(propertyItem.getContainer().getCode())) {
                    if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION) && isMultiWareHouseEnabled && hasPermission(isEnableLogistics ? PermissionConstants.LOGISTICS_STOCK_ADJUSTMENT_LIST : PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
                        propertyItemList.add(propertyItem);
                    }
                }
            }
            case "consignment" -> {
                if ((genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || user.getCompany().getParentCompanyId() != null) && hasPermission(PermissionConstants.ACCOUNTING_CONSIGNMENT_LIST_VIEW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "CASH_RECEIPT" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CASH_RECEIPTS_AND_PAYMENTS) && hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "CASH_PAYMENT" -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CASH_RECEIPTS_AND_PAYMENTS) && hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "RECEIVE_MONEY" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "SPEND_MONEY" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_SPEND)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CUSTOMER_PREPAYMENT -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "supplierPrepayment" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "checkList" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_CHECK_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case MANUAL_TRANSACTIONS -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "BATCH_RECEIVE_PAYMENT" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PAYBILLS_LIST -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PAY_BILL_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "goodsreceivednotes" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_GRN_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "goodsdeliverednotes" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_GDN_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "newprofitLoss" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_PROFIT_AND_LOSS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "balanceSheet" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BALANCE_SHEET)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "trialBalance" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_TRIAL_BALANCE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "cashFlowStatement" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_CASH_FLOW)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "arAgingSummary" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_AGING_SUMMARY_RECEIVABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "apAgingSummary" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_AGING_SUMMARY_PAYABLE)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "journalReport" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_JOURNAL_REPORT)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "transactionsByPeriod" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_ACCOUNT_TRANSACTIONS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "stockValuation" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_STOCK_VALUATION) && !isEnableLogistics) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "vatReturnsSaudiOrUae", "oldGccVatReturn" -> {
                if (isShowVatReturnReport && (ServerUtils.isUAECompany(user.getCompany()) || ServerUtils.isKSACompany(user.getCompany()) || ServerUtils.isUKCompany(user.getCompany())) && hasPermission(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "vatReturn" -> {
                if (isShowVatReturnReport && !((ServerUtils.isUAECompany(user.getCompany()) || ServerUtils.isKSACompany(user.getCompany()) || ServerUtils.isUKCompany(user.getCompany())) && hasPermission(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) && hasPermission(PermissionConstants.ACCOUNTING_VAT_RETURN)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "vatReturns" -> {
                if (isShowVatReturnReport && !((ServerUtils.isUAECompany(user.getCompany()) || ServerUtils.isKSACompany(user.getCompany()) || ServerUtils.isUKCompany(user.getCompany())) && hasPermission(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) && hasPermission(PermissionConstants.ACCOUNTING_VAT_RETURNS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "budgetsheetView" -> {
                if (hasPermission(PermissionConstants.ACCOUNTING_BUDGET_SHEET)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "warehouseList" -> {
                if (isMultiWareHouseEnabled && hasPermission(isEnableLogistics ? PermissionConstants.LOGISTICS_WAREHOUSE_LIST : PermissionConstants.ACCOUNTING_WAREHOUSE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case "STOCK_TRANSFER" -> {
                if (isMultiWareHouseEnabled && hasPermission(isEnableLogistics ? PermissionConstants.LOGISTICS_STOCK_TRANSFER_LIST : PermissionConstants.ACCOUNTING_STOCK_TRANSFER_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }

    private void getPMSectionTabs(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap, LinkedList<PropertyItem> propertyItemList, SelectItem selectItem) {
        propertyListingsMap.put(selectItem, propertyItemList);
    }

    private void getPMSectionListings(EdsUser user, LinkedList<PropertyItem> propertyItemList, PropertyItem propertyItem) {
        switch (propertyItem.getObjectName()) {
            case TASK -> {
                if (hasPermission(PermissionConstants.PM_TASKS_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case ISSUE -> {
                if (hasPermission(PermissionConstants.PM_ISSUE_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TIMESHEET -> {
                if (hasPermission(PermissionConstants.PM_TIMESHEET)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case MONTHLYTIMESHEET -> {
                boolean isMonthlyTimesheetEnabled = moduleManager.getModuleByCodeByCompany(moduleManager.getUser().getCompany().getObjectID(), Constants.MONTHLY_TIMESHEET) != null;
                if (isMonthlyTimesheetEnabled && hasPermission(PermissionConstants.MONTHLY_TIMESHEET)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case TIMESHEET_APPROVAL_LIST -> {
                if (hasPermission(PermissionConstants.PM_TIMESHEET_APPROVAL)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case PROJECT -> {
                if (hasPermission(PermissionConstants.PM_PROJECT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CLIENT_LIST -> {
                if (hasPermission(PermissionConstants.PM_CUSTOMER_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EMLOYEE_LIST -> {
                if (hasPermission(PermissionConstants.PM_EMPLOYEE_LIST) && !permissionManager.getUser().getCompany().getObjectID().equals(22240)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case BOOKINGITEMS_LIST -> {
                if (hasPermission(PermissionConstants.PM_BOOKING_ITEMS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_BOOKING_ITEMS)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case RESOURCE_UTIL -> {
                if (hasPermission(PermissionConstants.PM_RESOURCE_UTILIZATION_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case CONTRACT_LIST -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE) && hasPermission(PermissionConstants.PM_CONTRACT_LIST)) {
                    propertyItemList.add(propertyItem);
                }
            }
            case EXPENSES_CLAIM -> {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EXPENSE_USE_AS_INTERNAL_INVOICE) && hasPermission(PermissionConstants.PM_PROJECT_EXPENSE_CLAIMS) && hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
                    propertyItemList.add(propertyItem);
                }
            }

            default -> {
                if (propertyItem.getfID() != null && propertyItem.isCustom()) {
                    if (Constants.PAGE.equals(propertyItem.getType())) {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            List<Integer> customFormItems = customFormItemManager.getCustomFormItemsByFormId(propertyItem.getfID());
                            if (customFormItems != null && !customFormItems.isEmpty()) {
                                propertyItem.setSelectedItemID(customFormItems.get(0));
                            }
                            if ((propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_SUMMARY_" + user.getCompany().getObjectID())) || (propertyItem.getSelectedItemID() != null && hasPermission(propertyItem.getFormID() + "_EDIT_" + user.getCompany().getObjectID())) || hasPermission(propertyItem.getFormID() + "_ADD_" + user.getCompany().getObjectID())) {
                                propertyItemList.add(propertyItem);
                            }
                        }
                    } else {
                        if (hasPermission(propertyItem.getFormID() + "_" + user.getCompany().getObjectID())) {
                            propertyItemList.add(propertyItem);
                        }
                    }
                }
            }
        }
    }

    private HashSet<String> getReportingPermissionList(String context, EdsUser user) {
        HashSet<String> permissionItems = new HashSet<>();
        if (user == null || user.getRoles() == null) {
            return permissionItems;
        }
        try {
            boolean isAdmin = roleManager.hasRole(user, Constants.ADMIN);
            if (isAdmin) {
                return reportingPermissionManager.getPermissionCodeList();
            }
            return reportingPermissionManager.getUsersPermissionsListNative(context, user);
        } catch (Exception ex) {
            log.error("SEVERE! Error while getting user's permission list", ex);
            ex.printStackTrace();
        }
        return permissionItems;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<String> getPermissionList(String context, EdsUser user) {
        HashSet<String> permissionItems = new HashSet<>();
        if (user == null || user.getRoles() == null) {
            return permissionItems;
        }
        try {
            boolean isAdmin = roleManager.hasRole(user, Constants.ADMIN);
            //if admin, return all permissions
            if (isAdmin) {
                return permissionManager.getPermissionCodeList();
            }
            return permissionManager.getUsersPermissionsListNative(context, user);
        } catch (Exception ex) {
            log.error("SEVERE! Error while getting user's permission list", ex);
            ex.printStackTrace();
        }
        return permissionItems;
    }

    @Override
    public boolean hasPermission(String code) {
        return code != null && permissionManager.hasPermission(code, userManager.getUser());
    }

    @Override
    public boolean hasReportingPermission(String code, EdsUser user) {
        return code != null && permissionManager.hasReportingPermission(code, user);
    }

    @Override
    public boolean hasPermission(String code, EdsUser user) {
        return !(code == null || user == null) && permissionManager.hasPermission(code, user);
    }


    @Override
    public ListResult<RoleListItem> getRoleList(ListingFilterParameter fp) {
        ArrayList<RoleListItem> items = new ArrayList<>();
        List<EdsRole> roleList = roleManager.getRoleList(fp);
        int size = roleList != null ? roleList.size() : 0;
        if (fp.getLimit() > 0) {
            roleList = ListUtils.getSublist(roleList, fp.getStart(), fp.getLimit());
        }
        RoleListItem item;
        for (EdsRole aRoleList : roleList) {
            item = new RoleListItem();
            item.setName(commonMessageSource.localize(aRoleList.getCode(), aRoleList.getName()));
            item.setCode(aRoleList.getCode());
            item.setObjectID(aRoleList.getObjectID());
            item.setSystem(aRoleList.getSystem() != null && aRoleList.getSystem());
            item.setActive(aRoleList.isActive());
            items.add(item);
        }
        size = roleManager.getRoleDeletedItemCount();
        return new ListResult<>(items, size);
    }

    @Override
    public Integer saveRole(RoleListItem roleListItem) {
        EdsRole role = roleManager.getByName(roleListItem.getName());
        if (role != null && !role.getObjectID().equals(roleListItem.getObjectID())) {
            return -1;
        }
        role = roleListItem.getObjectID() != null ? roleManager.get(roleListItem.getObjectID()) : new EdsRole();
        role.setName(roleListItem.getName());
        role.setDescription(roleListItem.getDescription());
        String code = roleListItem.getName()
                .replace("'", "")
                .replace(" ", "_")
                .replace("‘","")
                .toUpperCase();
        role.setCode(code);
        role.setActive(roleListItem.isActive());
        roleManager.createOrUpdate(role);
        return role.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectItem> getRoles() {
        List<EdsRole> roleList = roleManager.list();
        ArrayList<SelectItem> result = new ArrayList<>();
        for (EdsRole role : roleList) {
            SelectItem item = new SelectItem(role.getObjectID(), role.getName());
            result.add(item);
        }
        return result;
    }

    @Override
    public Boolean deleteRole(Integer roleID) {
        if (roleID != null) {
            EdsRole roleItem = roleManager.get(roleID);
            int company = roleManager.getUser().getCompany().getObjectID();
            List<EdsUser> listUser = roleManager.getRoleUsers(company, roleID);

            for (EdsUser aListUser : listUser) {
                roleManager.removeRoleFromUser(roleID, aListUser.getObjectID());
            }
            roleItem.setDeleted(true);
            roleManager.update(roleItem);
        }
        return true;
    }

    @Override
    public void changeRoleStatus(Integer roleID) {
        if (roleID != null) {
            EdsRole role = roleManager.get(roleID);
            role.setActive(!role.isActive());
            roleManager.update(role);
        }
    }

    @Override
    public Boolean saveRolePermissions(int roleId, boolean priviladge, String context) {
        if (PermissionConstants.REPORTING.equals(context)) {
            List<EdsReportingPermission> permissions = permissionManager.listReportingPermissions(context);
            for (EdsReportingPermission item : permissions) {
                saveRolePermission(context, item.getObjectID() + "_" + roleId, priviladge);
            }
        } else {
            List<EdsPermission> permissions = permissionManager.listPermissions(context);
            for (EdsPermission item : permissions) {
                saveRolePermission(context, item.getObjectID() + "_" + roleId, priviladge);
            }
        }

        return true;
    }

    @Override
    public Boolean saveRolePermission(String context, String rolePermission, boolean priviladge) { //rolePermission dagi qiymat quydai kurinshda keladi permissionID_roleID
        if (rolePermission != null) {
            String[] splts = rolePermission.split("_");
            if (splts.length > 1) {
                int permissionID = Integer.parseInt(splts[0]);
                String permissioncode;
                String permissionName;
                if (PermissionConstants.REPORTING.equals(context)) {
                    permissioncode = reportingPermissionManager.get(permissionID) != null ? reportingPermissionManager.get(permissionID).getCode() : null;
                    permissionName = reportingPermissionManager.get(permissionID) != null ? reportingPermissionManager.get(permissionID).getName() : null;
                } else {
                    EdsPermission permission = permissionManager.get(permissionID);
                    permissioncode = permission != null ? permission.getCode() : null;
                    permissionName = permission != null ? permission.getName() : null;
                }
                if (permissioncode == null) {
                    return false;
                }
                int roleID = Integer.parseInt(splts[1]);
                EdsRolePermission rolePermissionItem = rolePermissionManager.getRolePermission(roleID, permissioncode);
                if (rolePermissionItem == null) {
                    rolePermissionItem = new EdsRolePermission();
                    rolePermissionItem.setRole(roleManager.get(roleID));
                    rolePermissionItem.setPermissioncode(permissioncode);
                }
                rolePermissionItem.setPriviledgeCode((priviladge) ? PermissionConstants.ALLOW : PermissionConstants.DENY);
                rolePermissionManager.createOrUpdate(rolePermissionItem);

                EdsRolePermissionHistory rolePermissionHistory = new EdsRolePermissionHistory();
                rolePermissionHistory.setPermissioncode(permissioncode);
                rolePermissionHistory.setPermissionName(permissionName);
                rolePermissionHistory.setContext(context);
                rolePermissionHistory.setRoleName(roleManager.get(roleID).getName());
                rolePermissionHistory.setOldValue(priviladge ? PermissionConstants.DENY : PermissionConstants.ALLOW);
                rolePermissionHistory.setNewValue((priviladge) ? PermissionConstants.ALLOW : PermissionConstants.DENY);
                rolePermissionHistory.setUpdater(userManager.getUser());
                rolePermissionHistory.setLastUpdateTime(new Date());
                rolePermissionHistoryManager.createOrUpdate(rolePermissionHistory);

                return true;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<RoleListItem> getRoleList(String context, boolean isAll) {
        List<EdsEmployee> ess = employeeManager.getEmployeeByRoleCode(EdsRole.ESS_USER_CODE);
        List<EdsRole> roleList = roleManager.findByContext(context);

        if (ess != null && !ess.isEmpty() && isAll) {
            EdsRole essRole = roleManager.getByCode(ESS_USER_CODE);
            roleList.add(essRole);
        }
        ArrayList<RoleListItem> result = new ArrayList<>();
        for (EdsRole roleItem : roleList) {
            if (Objects.equals(roleItem.getObjectID(), ADMIN) ||
                    Objects.equals(roleItem.getObjectID(), ONE_OFF) ||
                    Objects.equals(roleItem.getObjectID(), CHAT_EXPERT) ||
                    Objects.equals(roleItem.getObjectID(), CALENDAR_VIEWER) ||
                    Objects.equals(roleItem.getObjectID(), CALENDAR_EDITOR)) {
                continue;
            }
            RoleListItem item = new RoleListItem();

            item.setCode(roleItem.getCode());
            item.setDescription(roleItem.getDescription());
            item.setName(commonMessageSource.localize(roleItem.getCode(), roleItem.getName()));
            item.setObjectID(roleItem.getObjectID());
            item.setSystem(roleItem.getSystem());
            item.setActive(roleItem.isActive());
            if (roleItem.getModuleCode() != null) {
                item.setModuleCode(new HashSet<>(Arrays.asList(roleItem.getModuleCode().split(","))));
            }
            result.add(item);
        }
        return result;
    }

    private ArrayList<PermissionItem> initsializationStructureForReporting(Map<String, Map<String, List<PermissionItem>>> stringMap, List<RoleListItem> items) {
        permissionRoleList = new ArrayList<>();
        Map<String, List<PermissionItem>> folderMap = null;
        List<String> categories = new ArrayList<>(stringMap.keySet());
        PermissionItem firstItem = new PermissionItem();
        String rightChar = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        firstItem.setRightCharCount(1);
        firstItem.setRightChar("");
        permissionRoleList.add(firstItem);

        int defaultId = 0;
        for (String module : stringMap.keySet()) {
            if (!module.equals("  Action"))
                permissionRoleList.add(createPermissionItemForReporting(--defaultId, "code" + defaultId, PermissionConstants.REPORTING, module.toUpperCase(), 1, "", null, module, null, module, items)); //  modul permission
            for (String folderName : stringMap.get(module).keySet()) {
                permissionRoleList.add(createPermissionItemForReporting(--defaultId, "code" + defaultId, PermissionConstants.REPORTING, folderName, 2, rightChar, null, module, folderName, module + "," + folderName, items));
                for (PermissionItem permissionItem : stringMap.get(module).get(folderName)) {
                    PermissionItem permission = createPermissionItemForReporting(permissionItem.getObjectId(), permissionItem.getCode(), PermissionConstants.REPORTING, permissionItem.getName(), 4, rightChar + rightChar, permissionItem.getRoleDictionary(), permissionItem.getCategoryName(), permissionItem.getFolderName(), null, permissionItem.getRoleList());
                    if (permissionItem.getCode().equals(PermissionConstants.REPORTING_MAIN_MENU)) {
                        permission.setName(permission.getName().toUpperCase());
                        permission.setRightCharCount(1);
                        permission.setRightChar("");
                        permissionRoleList.add(1, permission);
                    } else {
                        permissionRoleList.add(permission);
                    }
                }
            }
        }
        return permissionRoleList;
    }

    private PermissionItem createPermissionItemForReporting(Integer objectId, String code, String context, String name, int rightCharCount, String rightChar, HashMap<Integer, Boolean> roleDictionary, String categoryName, String folderName, String type, List<RoleListItem> roleListItems) {
        PermissionItem item = new PermissionItem();
        item.setObjectId(objectId);
        item.setCode(code);
        item.setContext(context != null ? context : "");
        item.setName(name);
        item.setRightChar(rightChar);
        item.setRightCharCount(rightCharCount);
//        item.setRoleDictionary(roleDictionary == null ? new HashMap<>() : roleDictionary);
        item.setCategoryName(categoryName);
        item.setFolderName(folderName);
        item.setRoleList(roleListItems);
        item.setType(type);
        if (roleDictionary == null) {
            roleDictionary = new HashMap<>();
            for (RoleListItem roleListItem : roleListItems) {
                roleDictionary.put(roleListItem.getObjectID(), Boolean.FALSE);
            }
        }
        item.setRoleDictionary(roleDictionary);
        return item;
    }

    @Override
    @Transactional
    public Boolean resetRolePermissions(String context) {
        rolePermissionManager.deleteRolePermissionsByContext(context);

        rolePermissionManager.copyDefaultRolePermissions(context);
        return true;
    }

    @Override
    public boolean saveRoleSettings(HashMap<String, String> maps) {
        if (maps == null || maps.size() == 0) {
            return false;
        }
        maps.forEach((k, v) -> {
            EdsRole role = roleManager.getByCode(k);
            role.setModuleCode(v);
        });
        return false;
    }
}

