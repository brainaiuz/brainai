package com.edatasite.workforce.gwt.location.server;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeLocation;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsLocationCustomFields;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CityOrRegionManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeLocationManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.LocationCFManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.LocationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.location.client.rpc.EmployeeLocation;
import com.edatasite.workforce.gwt.location.client.rpc.LocationList;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 19:39:00
 */
@Transactional
@Service("locationService")
public class LocationServiceImpl implements LocationService, LocationServiceLocal, Constants, Errors {
    private static final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);
    private static final Map<String, ComparatorFactory<EdsLocation>> comparatorFactories = new HashMap<>();

    static {
        comparatorFactories.put("countryName",
                sortOrder -> new AbstractComparator<EdsLocation>() {
                    public int compare(EdsLocation o1, EdsLocation o2) {
                        return internalCompare(o1.getCountry() != null ? o1.getCountry().getName() : "",
                                o2.getCountry() != null ? o2.getCountry().getName() : "", sortOrder);
                    }
                });

        comparatorFactories.put("stateName",
                sortOrder -> new AbstractComparator<EdsLocation>() {
                    public int compare(EdsLocation o1, EdsLocation o2) {
                        return internalCompare(o1.getState() != null ? o1.getState().getName() : "",
                                o2.getState() != null ? o2.getState().getName() : "", sortOrder);
                    }
                });

        comparatorFactories.put("cityName",
                sortOrder -> new AbstractComparator<EdsLocation>() {
                    public int compare(EdsLocation o1, EdsLocation o2) {
                        return internalCompare(o1.getCity(), o2.getCity(), sortOrder);
                    }
                });
    }

    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    @Qualifier("cityDistrictManager")
    private CityOrRegionManager cityOrRegionManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private EmployeeLocationManager employeeLocationManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    private int countLocations = 0;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;
    @Autowired
    @Qualifier("regionLocalizer")
    private WfmMessageSource regionLocalizer;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private LocationCFManager locationCFManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountryList() {
        return commonService.getCountries();
    }

    public Integer saveLocation(CompLocationRpc compLocationRpc) {
        EdsUser user = locationManager.getUser();
        if (locationManager.getLocationByName(compLocationRpc.getName(), compLocationRpc.getObjectID()) != null || (
                compLocationRpc.getNumberData() != null && locationManager.getLocationByCode(compLocationRpc.getNumberData().getNumberString(), compLocationRpc.getObjectID()) != null)) {
            return THIS_LOCATION_ALREADY_EXISTS;
        }
        EdsLocation compLocation = new EdsLocation();
        compLocation.setName(compLocationRpc.getName());
        compLocation.setCity(compLocationRpc.getCityName());
        compLocation.setCountry(countryManager.get(compLocationRpc.getCountryId()));
        NumberData numberData = compLocationRpc.getNumberData() != null ? compLocationRpc.getNumberData() : generateLocationNumber();
        compLocation.setIntNumber(numberData.getIntNumber());
        compLocation.setCode(numberData.getNumberString());
        if (compLocationRpc.getStateId() != null) {
            compLocation.setState(regionManager.get(compLocationRpc.getStateId()));
        }
        if (compLocationRpc.getLocaleItem() != null) {
            EdsReferenceLocale locale = allInOneServiceLocal.saveEntityLocale(compLocationRpc.getLocaleItem());
            compLocation.setLocale(locale);
        }
        if (compLocationRpc.getParent() != null && compLocationRpc.getParent().getId() != null) {
            EdsLocation parent = locationManager.get(compLocationRpc.getParent().getId());
            compLocation.setParent(parent);
        } else {
            compLocation.setParent(null);
        }
        compLocation.setEmail(compLocationRpc.getEmail());
        compLocation.setFax(compLocationRpc.getFax());
        compLocation.setPhone(compLocationRpc.getPhoneNumber());
        compLocation.setZipCode(compLocationRpc.getZipCode());
        compLocation.setOwnersId(compLocationRpc.getOwnersId());
        compLocation.setLatitude(compLocationRpc.getLatitude());
        compLocation.setLongitude(compLocationRpc.getLongitude());
        compLocation.setRadius(compLocationRpc.getRadius());
        EdsLocationCustomFields customFields = createLocationCustomFields(compLocationRpc.getCustomFieldItems());
        compLocation.setCustomFields(customFields);
        locationManager.create(compLocation);
        baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, compLocation, user);
//        creatEmployeesLocation(compLocationRpc.getMembers(), compLocation);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsLocation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        if (compLocation.getObjectID() != null) {
            kpiLog.setEntityId(compLocation.getObjectID());
        }
        if (compLocationRpc.getLocationMembers().size() > 0) {
            saveEmployeeLocation(compLocationRpc.getLocationMembers(), compLocation.getObjectID(), true);
        }

        if (compLocationRpc.getUpdatedEmployees().size() > 0) {
            updateEmployeeLocations(compLocationRpc.getUpdatedEmployees(), compLocation);
        }

        ServerUtils.kpiLog(log, kpiLog, "ADD location");
        return compLocation.getObjectID();
    }

    private EdsLocationCustomFields createLocationCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsLocationCustomFields locationCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                locationCustomFields = locationCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                locationCustomFields = new EdsLocationCustomFields();
                locationCFManager.create(locationCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(locationCustomFields, customFieldItems);
            return locationCustomFields;
        }
        return null;
    }

    private void updateEmployeeLocations(Set<Integer> idsList, EdsLocation compLocation) {
        EdsUser user = locationManager.getUser();
        String ids = ServerUtils.getAsCommoDelimited(new ArrayList<>(idsList), "", ",");
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, compLocation, user);
        event.setCustomStringField(ids);
    }

    @Override
    public void saveEmployeeLocation(Set<Integer> locationMembers, Integer locationID, boolean isChecked) {
        EdsLocation location = isChecked ? locationManager.get(locationID) : null;
        if (locationMembers != null && !locationMembers.isEmpty()) {
            locationMembers.forEach(id -> {
                EdsEmployee employee = employeeManager.get(id);

                if (employee != null) {
                    if (!Objects.equals(employee.getLocation(), location)) {
                        baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, LocationEventListenerImpl.EMPLOYEE_LOCATION_CHANGE, location, employee);
                    }

                    if (location == null) {
                        employeeLocationManager.removeLocationHistory(employee);
                    } else {
                        employeeLocationManager.removeLocationHistory(employee, location);
                        EdsEmployeeLocation employeeLocation = new EdsEmployeeLocation();
                        employeeLocation.setUser(employee);
                        employeeLocation.setLocation(location);
                        employeeLocationManager.create(employeeLocation);
                    }

                    employee.setLocation(location);
                    try {
                        employeeSolrComponent.index(employee);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Warning: Employee with ID " + id + " is null.");
                }
            });
        }
    }

    public Integer updateLocation(CompLocationRpc locationRpc) {
        if (locationManager.getLocationByName(locationRpc.getName(), locationRpc.getObjectID()) != null ||
                locationManager.getLocationByCode(locationRpc.getNumberData().getNumberString(), locationRpc.getObjectID()) != null) {
            return THIS_LOCATION_ALREADY_EXISTS;
        }
        if (locationRpc.getObjectID() == null) {
            return 0;
        }
        EdsLocation location = locationManager.get(locationRpc.getObjectID());
        location.setIntNumber(locationRpc.getNumberData().getIntNumber());
        location.setCode(locationRpc.getNumberData().getNumberString());
        location.setName(locationRpc.getName());
        if (!location.getCity().equals(locationRpc.getCityName())) {
            location.setCity(locationRpc.getCityName());
        }
        if (!location.getCountry().getObjectID().equals(locationRpc.getCountryId())) {
            location.setCountry(countryManager.get(locationRpc.getCountryId()));
        }
        if (locationRpc.getStateId() != null) {
            location.setState(regionManager.get(locationRpc.getStateId()));
        } else {
            location.setState(null);
        }
        if (locationRpc.getLocaleItem() != null) {
            EdsReferenceLocale locale = allInOneServiceLocal.saveEntityLocale(locationRpc.getLocaleItem());
            location.setLocale(locale);
        }
        if (locationRpc.getCityOrDestrictId() != null && !(location.getCityDistrict() != null && locationRpc.getCityOrDestrictId().equals(location.getCityDistrict().getObjectID()))) {
            location.setCityDistrict(cityOrRegionManager.get(locationRpc.getCityOrDestrictId()));
        }
        if (locationRpc.getParent() != null && locationRpc.getParent().getId() != null) {
            location.setParent(locationManager.get(locationRpc.getParent().getId()));
        } else {
            location.setParent(null);
        }
        location.setLatitude(locationRpc.getLatitude());
        location.setLongitude(locationRpc.getLongitude());
        location.setRadius(locationRpc.getRadius());
        location.setEmail(locationRpc.getEmail());
        location.setFax(locationRpc.getFax());
        location.setPhone(locationRpc.getPhoneNumber());
        location.setZipCode(locationRpc.getZipCode());
        location.setOwnersId(locationRpc.getOwnersId());
        EdsLocationCustomFields customFields = createLocationCustomFields(locationRpc.getCustomFieldItems());
        location.setCustomFields(customFields);
        locationManager.update(location);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsLocation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(locationRpc.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Update location");
        baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, location, locationManager.getUser());
//        creatEmployeesLocation(locationRpc.getMembers(), location);
        if (locationRpc.getUpdatedEmployees().size() > 0) {
            updateEmployeeLocations(locationRpc.getUpdatedEmployees(), location);
        }
        return 0;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompLocationRpc getLocation(Integer locationId) {
        EdsLocation location = locationManager.get(locationId);
        CompLocationRpc compLocation = new CompLocationRpc();
        compLocation.setName(location.getLocationRealName());
        NumberData numberData = new NumberData();
        numberData.setFirstNumberString(location.getCode());
        numberData.setNumberFormat("_");
        compLocation.setNumberData(numberData);
        compLocation.setCountryName(countryLocalizer.localize(location.getCountry().getCode(), location.getCountry().getName()));
        compLocation.setCountryId(location.getCountry().getObjectID());
        if (location.getState() != null) {
            compLocation.setStateId(location.getState().getObjectID());
            compLocation.setStateName(location.getState().getName());
        }
        if (location.getLocale() != null) {
            compLocation.setLocaleItem(location.getLocale().toRPC());
        }
        if (location.getCityDistrict() != null) {
            compLocation.setCityOrDistrict(location.getCityDistrict().getAsSelectItem());
        }
        compLocation.setLatitude(location.getLatitude());
        compLocation.setLongitude(location.getLongitude());
        compLocation.setRadius(location.getRadius());
        compLocation.setEmail(location.getEmail() != null ? location.getEmail() : "");
        compLocation.setFax(location.getFax() != null ? location.getFax() : "");
        compLocation.setPhoneNumber(location.getPhone() != null ? location.getPhone() : "");
        compLocation.setCityName(location.getCity());
        compLocation.setZipCode(location.getZipCode());
        compLocation.setParent(location.getParent() != null ? location.getParent().getAsSelectItem() : null);
        ArrayList<SelectItem> owners = new ArrayList<>();
        if (location.getOwnersId() != null && !location.getOwnersId().isEmpty()) {
            for (EdsEmployee employeesById : employeeManager.getEmployeesByIds(location.getOwnersId())) {
                owners.add(new SelectItem(employeesById.getObjectID(), employeesById.getFormmattedName()));
            }
        }
        compLocation.setOwners(owners);
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Location);
        compLocation.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(location.getCustomFields(), customFieldsItems));

        return compLocation;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LocationList getLocationMaps(ListingFilterParameter fp) {
        CompLocationRpc[] compLocation = new CompLocationRpc[countLocations];
        List<EdsLocation> location = locationManager.list(fp);
        for (int i = 0; i < countLocations; i++) {
            compLocation[i] = new CompLocationRpc();
            compLocation[i].setCityName(location.get(i).getCity());
            compLocation[i].setCountryId(location.get(i).getCountry().getObjectID());
            compLocation[i].setCountryName(location.get(i).getCountry().getName());
        }

        return new LocationList(compLocation, countLocations);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LocationList getLocation(ListLoadConfig config, ListingFilterParameter fp) {
        List<EdsLocation> location = locationManager.list(fp);

        int size = location != null ? location.size() : 0;

        ComparatorFactory factory = null;
        if (config.getSortField() != null && !"".equals(config.getSortField())) {
            factory = comparatorFactories.get(config.getSortField());
        }
        int sortDir = config.getSortDir();
        if (factory == null) {
            factory = comparatorFactories.get(CompLocationRpc.COUNTRY_NAME);
            sortDir = Constants.ASC;
        }
        location.sort(factory.createComparator(sortDir));

        if (config.getLimit() > 0) {
            location = ListUtils.getSublist(location, config.getStart(), config.getLimit());
        }
        CompLocationRpc[] compLocation = new CompLocationRpc[location.size()];
        for (int i = 0; i < location.size(); i++) {
            List<EdsEmployee> employees = locationManager.getLocationEmployee(location.get(i).getObjectID());
            compLocation[i] = new CompLocationRpc();
            compLocation[i].setObjectID(location.get(i).getObjectID());
            compLocation[i].setCityName(location.get(i).getCity());
            compLocation[i].setCountryName(countryLocalizer.localize(location.get(i).getCountry().getCode(), location.get(i).getCountry().getName()));
            compLocation[i].setStateName(location.get(i).getState() != null ? location.get(i).getState().getName() : "N/A");
            compLocation[i].setEmail(location.get(i).getEmail() != null ? location.get(i).getEmail() : "");
            compLocation[i].setFax(location.get(i).getFax() != null ? location.get(i).getFax() : "");
            compLocation[i].setPhoneNumber(location.get(i).getPhone() != null ? location.get(i).getPhone() : "");
            compLocation[i].setZipCode(location.get(i).getZipCode() != null ? location.get(i).getZipCode() : "");
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Location);
            compLocation[i].setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(location.get(i).getCustomFields(), customFieldsItems));
            compLocation[i].setLocationEmployeesSize(employees.size());
        }

        LocationList locationList = new LocationList(compLocation, size);
        countLocations = location.size();
        return locationList;
    }

    @Override
    public ListResult<CompLocationRpc> getLocations(ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        List<EdsLocation> location = locationManager.getLocations(filterParameter);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsLocation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get location list");

        int size = location != null ? location.size() : 0;

        if (filterParameter.getLimit() > 0) {
            location = ListUtils.getSublist(location, filterParameter.getStart(), filterParameter.getLimit());
        }
        CompLocationRpc[] compLocation = new CompLocationRpc[location.size()];
        for (int i = 0; i < location.size(); i++) {
            int usedLocationSize = locationManager.getLocationUsedSize(location.get(i).getObjectID());
            compLocation[i] = new CompLocationRpc();
            compLocation[i].setObjectID(location.get(i).getObjectID());
            compLocation[i].setNumberData(new NumberData(location.get(i).getCode()));
            compLocation[i].setName(location.get(i).getName());
            compLocation[i].setCityName(location.get(i).getCity());
            compLocation[i].setCountryName(location.get(i).getCountry() != null ? countryLocalizer.localize(location.get(i).getCountry().getCode(), location.get(i).getCountry().getName()) : "N/A");
            compLocation[i].setStateName(location.get(i).getState() != null ? regionLocalizer.localize(location.get(i).getState().getCode(), location.get(i).getState().getName()) : "N/A");
            compLocation[i].setEmail(location.get(i).getEmail() != null ? location.get(i).getEmail() : "");
            compLocation[i].setFax(location.get(i).getFax() != null ? location.get(i).getFax() : "");
            compLocation[i].setPhoneNumber(location.get(i).getPhone() != null ? location.get(i).getPhone() : "");
            compLocation[i].setZipCode(location.get(i).getZipCode() != null ? location.get(i).getZipCode() : "");
            compLocation[i].setCityOrDestrictName(location.get(i).getCityDistrict() != null && location.get(i).getCityDistrict().getName() != null ? location.get(i).getCityDistrict().getName() : "");
            List<EdsEmployee> locationEmployees = locationManager.getLocationEmployee(location.get(i).getObjectID());
            compLocation[i].setMemberCount(locationEmployees != null ? locationEmployees.size() : 0);
            compLocation[i].setLocationEmployeesSize(usedLocationSize);
            compLocation[i].setParent(location.get(i).getParent() != null ? location.get(i).getParent().getAsSelectItem() : null);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Location);
            compLocation[i].setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(location.get(i).getCustomFields(), customFieldsItems));
            if (panelSettings != null) {
                HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(location.get(i).getCustomFields(), panelSettings.getColumnCodeName());
                compLocation[i].setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
            }
        }

        countLocations = location.size();
        return new ListResult<CompLocationRpc>(new ArrayList<>(Arrays.asList(compLocation)), size);
    }

    public void deleteLocation(Integer locationID) {
        EdsLocation location = locationManager.get(locationID);
        List<EdsEmployee> employees;
        StringBuilder employeeIds = new StringBuilder("");
        if (location != null && location.getObjectID() != null) {
            employees = locationManager.getLocationEmployee(location.getObjectID());
            if (employees != null && employees.size() > 0) {
                int i = 0;
                for (EdsEmployee employee : employees) {
                    if (i != 0) {
                        employeeIds.append(", ");
                    }
                    employeeIds.append(employee.getObjectID());
                    i++;
                }
                EdsBusinessEvent s = baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, new EdsEmployee(), locationManager.getUser());
                s.setCustomStringField(employeeIds.toString());
            }
        }
        location.setDeleted(true);
        locationManager.update(location);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsLocation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(locationID);
        ServerUtils.kpiLog(log, kpiLog, "Delete location");
    }

    public EmployeeLocation getLocationAndEmployees(Integer locationId) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsLocation.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(locationId);
        ServerUtils.kpiLog(log, kpiLog, "View location");
        EdsLocation location = locationManager.get(locationId);
        CompLocationRpc locationRpc = new CompLocationRpc();
        locationRpc.setNumberData(new NumberData(location.getCode(), location.getIntNumber()));
        locationRpc.setName(location.getName());
        locationRpc.setCityName(location.getCity());
        locationRpc.setLatitude(location.getLatitude());
        locationRpc.setLongitude(location.getLongitude());
        locationRpc.setRadius(location.getRadius());
        locationRpc.setCountryName(location.getCountry() != null ? countryLocalizer.localize(location.getCountry().getCode(), location.getCountry().getName()) : "N/A");
        locationRpc.setEmail(location.getEmail());
        locationRpc.setFax(location.getFax());
        locationRpc.setPhoneNumber(location.getPhone());
        locationRpc.setZipCode(location.getZipCode());
        locationRpc.setParent(location.getParent() != null ? location.getParent().getAsSelectItem() : null);
        if (location.getCityDistrict() != null) {
            locationRpc.setCityOrDestrictName(location.getCityDistrict().getName());
        }

        if (location.getState() != null) {
            locationRpc.setStateName(regionLocalizer.localize(location.getState().getCode(), location.getState().getName()));
        }
        ArrayList<SelectItem> owners = new ArrayList<>();
        if (location.getOwnersId() != null && !location.getOwnersId().isEmpty()) {
            for (EdsEmployee employeesById : employeeManager.getEmployeesByIds(location.getOwnersId())) {
                owners.add(new SelectItem(employeesById.getObjectID(), employeesById.getFormmattedName()));
            }
        }
        locationRpc.setOwners(owners);
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Location);
        locationRpc.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(location.getCustomFields(), customFieldsItems));
        int usedLocationsSize = locationManager.getLocationUsedSize(locationId);
        EmployeeLocation emplLacation = new EmployeeLocation();
        emplLacation.setLocation(locationRpc);
        emplLacation.setLocationUsed(usedLocationsSize > 0);

        return emplLacation;
    }

    public SelectItem[] getLocationsAsSelectItem(ListingFilterParameter fp) {
        if (fp.getEmployeeId() != null) {
            EdsLocation location = employeeManager.get(fp.getEmployeeId()).getLocation();
            if (location != null) {
                return new SelectItem[]{location.getAsSelectItem()};
            } else {
                return new SelectItem[0];
            }
        }
        return locationManager.getLocationsAsSelectItems(fp);
    }

    @Override
    public ListResult<EmployeeLocationItem> getEmployeelocations(ListingFilterParameter filterParameter) {
        ListResult<EmployeeLocationItem> result = new ListResult<>();
        List<EdsEmployeeLocation> list = employeeLocationManager.getByEmployee(filterParameter);
        if (list == null && list.isEmpty()) {
            return result;
        }
        ArrayList<EmployeeLocationItem> data = new ArrayList<>();
        for (EdsEmployeeLocation employeeLocation : list) {
            data.add(employeeLocation.getRPC());
        }
        result.setList(data);
        result.setTotal(data.size());
        return result;
    }

    @Override
    public Boolean deleteEmployeeLocation(Integer objectID) {
        employeeLocationManager.removeLocationHistory(objectID);
        return true;
    }

    @Override
    public EmployeeLocationItem getEmployeeLocation(Integer objectID) {
        EdsEmployeeLocation employeeLocation = employeeLocationManager.get(objectID);
        return employeeLocation != null ? employeeLocation.getRPC() : null;
    }

    @Override
    public void saveEmployeeLocation(EmployeeLocationItem item) {
        if (item == null) return;

        EdsEmployeeLocation employeeLocation = new EdsEmployeeLocation();
        EdsEmployee employee = employeeManager.get(item.getEmployee().getId());

        if (item.getId() != null) {
            employeeLocation = employeeLocationManager.get(item.getId());
        }

        if (item.getLocation() != null) {
            EdsLocation location = locationManager.get(item.getLocation().getId());
            employee.setLocation(location);
            employeeLocation.setLocation(location);
        }

        employeeLocation.setUser(employee);
        employeeLocation.setStartDate(item.getStartDate() != null ? item.getStartDate().getNonConvertedDate() : null);
        employeeLocation.setEndDate(item.getEndDate() != null ? item.getEndDate().getNonConvertedDate() : null);

        employeeManager.update(employee);
        employeeLocationManager.createOrUpdate(employeeLocation);

        try {
            long start = System.currentTimeMillis();
            solrManager.addEmployeeToIndex(employee);
            LocationServiceImpl.log.info("solrManager.addEmployeeToIndex" + (System.currentTimeMillis() - start));
        } catch (SolrServerException e) {
            log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
        } catch (IOException e) {
            log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
        }
    }

    @Override
    public NumberData generateLocationNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = locationManager.getLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getLocationNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getLocationNumberingFormat(), settings.getDelimetrLocationNumbering(), null, null, null, "position");
            numberData.setDelimiter(settings.getDelimetrLocationNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_LOCATION_PREFIX /*true*/);
        }
    }

    @Override
    public SelectItem[] getLocationOwnersByRole() {
        // This code is used to filter the list of users in the Location form to only show users that have the "owners" permission.
        List<EdsEmployee> owners = employeeManager.getOwnersByPermission(PermissionConstants.HRMS_OWNERS_FIELD_LOCATION);
        ArrayList<SelectItem> result = new ArrayList<>();
        owners.forEach(employee -> result.add(new SelectItem(employee.getObjectID(), employee.getFullName())));
        return result.toArray(new SelectItem[0]);
    }

    @Override
    public List<ChartNode> getLocationNodes() {
        List<EdsLocation> locations = locationManager.getLocations(new ListingFilterParameter());
        List<ChartNode> chartNodes = createTeamNodesFromLocations(locations);
        return chartNodes;
    }

    @Override
    public SelectItem getLocationAsSelectItem(Integer locationId) {
        EdsLocation location = locationManager.get(locationId);
        return location != null ? location.getAsSelectItem() : null;
    }

    public List<ChartNode> createTeamNodesFromLocations(List<EdsLocation> locations) {
        List<ChartNode> chartNodes = new ArrayList<>();

        // Create a mapping of location ID to location
        Map<Integer, EdsLocation> locationMap = new HashMap<>();
        for (EdsLocation location : locations) {
            locationMap.put(location.getObjectID(), location);
        }

        // Create a mapping of location ID to LocationNode
        Map<Integer, ChartNode> locationNodeMap = new HashMap<>();

        // Build the team nodes hierarchy iteratively
        for (EdsLocation location : locations) {
            if (location.getParent() == null) {
                ChartNode chartNode = buildTeamNodeIteratively(location, locationMap, locationNodeMap, 0);
                chartNodes.add(chartNode);
            }
        }

        return chartNodes;
    }

    private List<EdsLocation> getChildLocations(Integer parentId, Map<Integer, EdsLocation> locationMap) {
        List<EdsLocation> childLocations = new ArrayList<>();
        for (EdsLocation location : locationMap.values()) {
            Integer locationParentId = location.getParent() != null ? location.getParent().getObjectID() : null;
            if (locationParentId != null && locationParentId.equals(parentId)) {
                childLocations.add(location);
            }
        }
        return childLocations;
    }

    private ChartNode buildTeamNodeIteratively(EdsLocation location, Map<Integer, EdsLocation> locationMap,
                                               Map<Integer, ChartNode> locationNodeMap, int depth) {
        ChartNode chartNode = new ChartNode();
        chartNode.setId(location.getObjectID());
        chartNode.setName(location.getCode() != null ? location.getCode() + "-" + location.getName() : location.getName());
        chartNode.setDepth(depth);
        chartNode.setSorder(1024);

        // Use a stack for iterative traversal
        Stack<EdsLocation> edsLocationStack = new Stack<>();
        Stack<ChartNode> chartNodeStack = new Stack<>();
        edsLocationStack.push(location);
        chartNodeStack.push(chartNode);

        while (!edsLocationStack.isEmpty()) {
            EdsLocation currentLocation = edsLocationStack.pop();
            ChartNode currentNode = chartNodeStack.pop();

            // Build child nodes
            List<EdsLocation> childLocations = getChildLocations(currentLocation.getObjectID(), locationMap);
            for (EdsLocation childLocation : childLocations) {
                ChartNode childNode = new ChartNode();
                childNode.setId(childLocation.getObjectID());
                childNode.setName(childLocation.getCode() != null ? childLocation.getCode() + "-" + childLocation.getName() : childLocation.getName());
                childNode.setDepth(currentNode.getDepth() + 1);
                childNode.setSorder(1024);
                currentNode.addChild(childNode);

                edsLocationStack.push(childLocation);
                chartNodeStack.push(childNode);
            }

            // Establish parent-child relationship
            if (currentLocation.getParent() != null) {
                ChartNode parentNode = locationNodeMap.get(currentLocation.getParent().getObjectID());
                currentNode.setParent(parentNode);
            }

            // Populate locationNodeMap
            locationNodeMap.put(currentLocation.getObjectID(), currentNode);
        }

        return chartNode;
    }


}
