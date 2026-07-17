package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.EmployeeApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 03/09/12
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/employee")
public class EmployeeApiController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    @Qualifier("availabilityService")
    private AvailabilityServiceLocal availabilityService;
    @Autowired
    private ReportService reportService;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreService coreService;

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object search(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                         @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                         @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey
    ) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);
            fp.setSearchKey(searchKey);
            ListResult<EmployeeListItem> searchResult = employeeService.getEmployees(fp);

            EmployeeApiPresenter presenter = new EmployeeApiPresenter();
            return presenter.convertToMapListing(searchResult);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object searchById(@PathVariable Integer Id) throws BaseApiException {
        try {

            ProfileItem profileItem = hrmsServiceLocal.editProfile(Id);
            SelectItem[] locations = hrmsService.getLocationList();
            SelectItem[] positions = hrmsService.getPositionsList();
            profileItem.setLocations(locations);

            EmployeeApiPresenter presenter = new EmployeeApiPresenter();
            Map<String, Object> resultMap = presenter.convertToMap(profileItem);
            resultMap.put(BaseApiPresenter.POSITION_LIST, positions);
            return resultMap;

        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object save(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Map<String, Object> saveDataMap = (Map<String, Object>) params.get(APIConstants.SAVE_DATA);
            if (saveDataMap == null || saveDataMap.isEmpty()) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }

            Integer id = 0;

            EmployeeApiPresenter presenter = new EmployeeApiPresenter();

            if (saveDataMap.get(BaseApiPresenter.OBJECT_ID) != null && ((Integer) saveDataMap.get(BaseApiPresenter.OBJECT_ID)) > 0) {
                ProfileItem item = presenter.convertToItem(saveDataMap);
                ProfileItem profileItem = hrmsServiceLocal.editProfile(item.getObjectId());
                profileItem.setFirstName(item.getFirstName());
                profileItem.setLastName(item.getLastName());
                profileItem.setMiddleName(item.getMiddleName());
                profileItem.setTitleId(item.getTitleId());
                profileItem.setBirthDate(item.getBirthDate());
                profileItem.setGender(item.getGender());
                profileItem.setMartialStatusId(item.getMartialStatusId());
                profileItem.setSpokenLanguages(item.getSpokenLanguages());

                List<Address> homeAddresses = new ArrayList<>();
                List<Address> workAddresses = new ArrayList<>();
                List<Address> otherAddresses = new ArrayList<>();
                if (profileItem != null) {
                    if (profileItem.getAddresses() != null && profileItem.getAddresses().size() > 0) {
                        for (int i = 0; i < profileItem.getAddresses().size(); i++) {
                            if (profileItem.getAddresses().get(i).getRelationType() == 1) {
                                homeAddresses.add(profileItem.getAddresses().get(i));
                            } else if (profileItem.getAddresses().get(i).getRelationType() == 2) {
                                workAddresses.add(profileItem.getAddresses().get(i));
                            } else if (profileItem.getAddresses().get(i).getRelationType() == 7) {
                                otherAddresses.add(profileItem.getAddresses().get(i));
                            }

                        }
                    }
                }
                for (Address address : item.getAddresses()) {
                    if (address.getRelationType() == 1) {
                        if (homeAddresses.size() > 0) {
                            homeAddresses.get(0).setAddress(address.getAddress());
                            homeAddresses.get(0).setAddressb(address.getAddressb());
                            homeAddresses.get(0).setCity(address.getCity());
                            homeAddresses.get(0).setCountryId(address.getCountryId());
                            homeAddresses.get(0).setStateId(address.getStateId());
                            homeAddresses.get(0).setZipCode(address.getZipCode());
                        } else {
                            profileItem.getAddresses().add(address);
                        }
                    } else if (address.getRelationType() == 2) {
                        if (workAddresses.size() > 0) {
                            workAddresses.get(0).setAddress(address.getAddress());
                            workAddresses.get(0).setAddressb(address.getAddressb());
                            workAddresses.get(0).setCity(address.getCity());
                            workAddresses.get(0).setCountryId(address.getCountryId());
                            workAddresses.get(0).setStateId(address.getStateId());
                            workAddresses.get(0).setZipCode(address.getZipCode());
                        } else {
                            profileItem.getAddresses().add(address);
                        }
                    } else if (address.getRelationType() == 7) {
                        if (otherAddresses.size() > 0) {
                            otherAddresses.get(0).setAddress(address.getAddress());
                            otherAddresses.get(0).setAddressb(address.getAddressb());
                            otherAddresses.get(0).setCity(address.getCity());
                            otherAddresses.get(0).setCountryId(address.getCountryId());
                            otherAddresses.get(0).setStateId(address.getStateId());
                            otherAddresses.get(0).setZipCode(address.getZipCode());
                        } else {
                            profileItem.getAddresses().add(address);
                        }
                    }
                }

                profileItem.setHomePhone(item.getHomePhone());
                profileItem.setWorkPhone(item.getWorkPhone());
                profileItem.setMobile(item.getMobile());
                profileItem.setHomeFax(item.getHomeFax());
                profileItem.setWorkFax(item.getWorkFax());
                profileItem.setOtherPhone(item.getOtherPhone());
                profileItem.setPrimaryEmail(item.getPrimaryEmail());
                profileItem.setHomeEmail(item.getHomeEmail());
                profileItem.setWorkEmail(item.getWorkEmail());
                profileItem.setWageRate(item.getWageRate());
                profileItem.setHomeWebSite(item.getHomeWebSite());
                profileItem.setWorkWebSite(item.getWorkWebSite());
                profileItem.setHomePage(item.getHomePage());
                profileItem.setFtp(item.getFtp());
                profileItem.setBlog(item.getBlog());
                profileItem.setProfileWebSite(item.getProfileWebSite());
                profileItem.setOtherWebSite(item.getOtherWebSite());
                profileItem.setgTalk(item.getgTalk());
                profileItem.setAIM(item.getAIM());
                profileItem.setClientChargeRate(item.getClientChargeRate());
                profileItem.setPmDepartmentID(item.getPmDepartmentID());
                profileItem.setPositionId(item.getPositionId());
                profileItem.setLocationId(item.getLocationId());
                profileItem.setStatusId(item.getStatusId());
                profileItem.setRoleId(item.getRoleId());
                id = hrmsServiceLocal.updateProfile(profileItem);


            } else {
                List<NewEmployee> list = new ArrayList<>();
                list.add(presenter.convertToItem(saveDataMap, true));
                Integer[] ids = employeeService.createEmployees(list.toArray(new NewEmployee[]{}));
                id = ids[0];
            }

            return id;

        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/roles", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getRoles() throws BaseApiException {
        try {
            ArrayList<RoleListItem> roles = coreService.getCompanyRoles();
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, roles.size());
            resultMap.put(BaseApiPresenter.ITEMS, roles);
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/locations", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getLocations() throws BaseApiException {
        try {
            SelectItem[] locations = reportService.getLocationList();
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, locations.length);
            resultMap.put(BaseApiPresenter.ITEMS, locations);
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/departments", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getDepartments() throws BaseApiException {
        try {
            DepartmentItem[] departments = employeeService.getDepartmentsSelectItem();
            List<SelectItem> list = new ArrayList<>();
            for (DepartmentItem departmentItem : departments) {
                SelectItem item = new SelectItem(departmentItem.getDepatmentID(), departmentItem.getDepartmentName());
                list.add(item);
            }

            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, list.size());
            resultMap.put(BaseApiPresenter.ITEMS, list);
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

}
