package com.workforcetrack.api.presenter;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.workforcetrack.api.base.RestServiceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 03/09/12
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeApiPresenter extends BaseApiPresenter {

    public Map<String, Object> convertToMapListing(ListResult<EmployeeListItem> items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (EmployeeListItem item : items.getList()) {
            list.add(convertToMapListing(item));
        }
        map.put(TOTAL_COUNT, items.getTotal());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMapListing(EmployeeListItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(FIRST_NAME, item.getFirstName());
        map.put(MIDDLE_NAME, item.getMiddleName());
        map.put(LAST_NAME, item.getLastName());
        map.put(EMAIL, item.getEmail());
        map.put(ROLE, item.getRole());
        map.put(START_DATE, DateUtils.format(item.getStartDate()));
        map.put(ACTIVE, item.getActive());
        map.put(DEPARTMENT, item.getDepartment());
        map.put(STATUS_NAME, item.getStatus());
        map.put(STATUS_CODE, item.getStatusCode());
        map.put(PHONE, item.getPhoneNumber() != null ? RestServiceUtils.cleanPhoneNumber(item.getPhoneNumber()) : "N/A");
        map.put(LAST_UPDATE, item.getLastUpdate());
        map.put(LOCATION, item.getLocation());
        return map;
    }

    public Map<String, Object> convertToMap(ProfileItem item) {
        Map<String, Object> map = new LinkedHashMap<>();

        map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getEmployeeId());
        map.put(FIRST_NAME, item.getFirstName());
        map.put(LAST_NAME, item.getLastName());
        map.put(MIDDLE_NAME, item.getMiddleName());
        map.put(TITLE, item.getTitle());
        map.put(TITLE_ID, item.getTitleId());
        map.put(BIRTH_DATE, item.getBirthDate() != null ? item.getBirthDate().getDate() : null);
        map.put(GENDER, item.getGender());
        map.put(MARTIAL_STATUS, item.getMartialStatus());
        map.put(MARTIAL_STATUS_ID, item.getMartialStatusId());
        map.put(STATUS_ID, item.getStatusId());
        map.put(STATUS, item.getStatus());
        map.put(LOCATION, item.getLocationName());
        map.put(LOCATION_ID, item.getLocationId());
        map.put(CLIENT_CHARGE_RATE, item.getClientChargeRate());
        map.put(EMPLOYEE_CODE, item.getEmpCode());
        map.put(EMPLOYMENT_MODE, item.getEmpMode());
        map.put(EMPLOYMENT_MODEID, item.getEmpModeId());
        map.put(HIRE_DATE, item.getHireDate());
        map.put(TERMS_OF_CONTRACT, item.getTermsOfContract());
        map.put(DEPARTMENT, item.getDepartment());
        map.put(DEPARTMENT_ID, item.getPmDepartmentID());
        map.put(POSITION, item.getPosition());
        map.put(POSITION_ID, item.getPositionId());

        map.put(HOME_ADDRESSES, null);
        map.put(WORK_ADDRESSES, null);
        map.put(OTHER_ADDRESSES, null);

        List<Address> homeAddresses = new ArrayList<>();
        List<Address> workAddresses = new ArrayList<>();
        List<Address> otherAddresses = new ArrayList<>();
        if (item != null) {
            if (item.getAddresses() != null && item.getAddresses().size() > 0) {
                for (int i = 0; i < item.getAddresses().size(); i++) {
                    if (item.getAddresses().get(i).getRelationType() == 1) {
                        homeAddresses.add(item.getAddresses().get(i));
                    } else if (item.getAddresses().get(i).getRelationType() == 2) {
                        workAddresses.add(item.getAddresses().get(i));
                    } else if (item.getAddresses().get(i).getRelationType() == 7) {
                        otherAddresses.add(item.getAddresses().get(i));
                    }

                }
            }

            if (homeAddresses.size() > 0) {
                map.put(HOME_ADDRESSES, convertToMap(homeAddresses.get(0)));
            }

            if (workAddresses.size() > 0) {
                map.put(WORK_ADDRESSES, convertToMap(workAddresses.get(0)));
            }

            if (otherAddresses.size() > 0) {
                map.put(OTHER_ADDRESSES, convertToMap(otherAddresses.get(0)));
            }

        }
        map.put(HOME_PHONE, item.getHomePhone());
        map.put(WORK_PHONE, item.getWorkPhone());
        map.put(MOBILE_PHONE, item.getMobile());
        map.put(HOME_FAX, item.getHomeFax());
        map.put(WORK_FAX, item.getWorkFax());
        map.put(OTHER_PHONE, item.getOtherPhone());
        map.put(EMAIL, item.getEmail());
        map.put(WORK_EMAIL, item.getWorkEmail());
        map.put(HOME_EMAIL, item.getHomeEmail());
        map.put(WAGE_RATE, item.getWageRate());
        map.put(HOME_WEBSITE, item.getHomeWebSite());
        map.put(WORK_WEBSITE, item.getWorkWebSite());
        map.put(HOME_PAGE, item.getHomePage());
        map.put(FTP, item.getFtp());
        map.put(BLOG, item.getBlog());
        map.put(PROFILE_WEBSITE, item.getProfileWebSite());
        map.put(OTHER_WEBSITE, item.getOtherWebSite());
        map.put(GTALK, item.getgTalk());
        map.put(AIM, item.getAIM());
        map.put(COUNTRYS, item.getCountries());
        map.put(DEPARTMENT_LIST, item.getPmDepartmentItems());
        map.put(LOCATION_LIST, item.getLocations());
        map.put(STATUS_LIST, item.getStatusList());
        map.put(ROLE_ID, item.getRoleId());
        map.put(ROLE_LIST, item.getRoleList());
        map.put(TITLE_LIST, item.getTitleList());
        map.put(SPOKEN_LANGUAGES, item.getSpokenLanguages());
        map.put(MARTIAL_STATUS_LIST, item.getMartialStatusList());
        map.put(IMAGE_URL, item.getEmployeeImageUrl());
        return map;
    }

    private Map<String, Object> convertToMap(Address address) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, address.getObjectID());
        map.put(ADDRESS, address.getAddress());
        map.put(ADDRESSB, address.getAddressb());
        map.put(CITY, address.getCity());
        map.put(COUNTRY, address.getCountry());
        map.put(COUNTRY_ID, address.getCountryId());
        map.put(STATE, address.getState());
        map.put(STATE_ID, address.getStateId());
        map.put(POST_CODE, address.getZipCode());
        return map;
    }


    public Map<String, Object> convertToMap(WorkspaceHomeUnavailableEmployeesRpc[] items) {
        Map<String, Object> map = new LinkedHashMap<>();
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (WorkspaceHomeUnavailableEmployeesRpc item : items) {
            list.add(convertToMap(item));
        }
        map.put(TOTAL_COUNT, list.size());
        map.put(ITEMS, list);
        return map;
    }

    public Map<String, Object> convertToMap(WorkspaceHomeUnavailableEmployeesRpc item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(OBJECT_ID, item.getObjectID());
        map.put(EMPLOYEE, item.getEmployeeID());
        map.put(EMPLOYEE, item.getEmployeeName());
        map.put(FROM_DATE, item.getFromDate());
        map.put(TO_DATE, item.getToDate());
        map.put(FROM_SDATE, item.getFromSDate());
        map.put(TO_SDATE, item.getToSDate());
        map.put(EMPLOYEE_IMAGE_URL, item.getEmployeePhotoURL());
        map.put(LEAVE_TYPE, item.getLeaveType());
        map.put(IS_LINKABLE, item.getLinkable());

        return map;
    }

    public ProfileItem convertToItem(Map<String, Object> map) throws ParseException, ClassCastException {
        ProfileItem item = new ProfileItem();
        item.setObjectId((Integer) map.get(OBJECT_ID));
        item.setEmployeeId((Integer) map.get(OBJECT_ID));
        item.setFirstName((String) map.get(FIRST_NAME));
        item.setLastName((String) map.get(LAST_NAME));
        item.setMiddleName((String) map.get(MIDDLE_NAME));
        item.setTitleId((Integer) map.get(TITLE_ID));

        SimpleDateFormat dateFormat = new SimpleDateFormat(RestServiceUtils.JSON_DATE_FORMAT);

        if (map.get(BIRTH_DATE) != null) {
            Date date = dateFormat.parse((String) map.get(BIRTH_DATE));
            item.setBirthDate(new DateNonConvertable(date));
        }
        item.setGender((String) map.get(GENDER));
        item.setMartialStatusId((Integer) map.get(MARTIAL_STATUS_ID));
        //item.setSpokenLanguages((String) map.get(SPOKEN_LANGUAGES));

        ArrayList<Address> addresses = new ArrayList<>();
        if (map.get(HOME_ADDRESSES) != null) {
            Address homeAddress = null;
            homeAddress = convertToAddressItem((Map<String, Object>) map.get(HOME_ADDRESSES));
            homeAddress.setRelationType(1);
            addresses.add(homeAddress);
        }
        if (map.get(WORK_ADDRESSES) != null) {
            Address workAddress = convertToAddressItem((Map<String, Object>) map.get(WORK_ADDRESSES));
            workAddress.setRelationType(2);
            addresses.add(workAddress);
        }

        if (map.get(OTHER_ADDRESSES) != null) {
            Address otherAddress = convertToAddressItem((Map<String, Object>) map.get(OTHER_ADDRESSES));
            otherAddress.setRelationType(7);
            addresses.add(otherAddress);
        }

        if (addresses.size() > 0) {
            item.setAddresses(addresses);
        }

        item.setHomePhone((ArrayList<String>) map.get(HOME_PHONE));
        item.setWorkPhone((ArrayList<String>) map.get(WORK_PHONE));
        item.setMobile((ArrayList<String>) map.get(MOBILE_PHONE));
        item.setHomeFax((ArrayList<String>) map.get(HOME_FAX));
        item.setWorkFax((ArrayList<String>) map.get(WORK_FAX));
        item.setOtherPhone((ArrayList<String>) map.get(OTHER_PHONE));
        item.setPrimaryEmail((String) map.get(EMAIL));
        item.setHomeEmail((ArrayList<String>) map.get(HOME_EMAIL));
        item.setWorkEmail((ArrayList<String>) map.get(WORK_EMAIL));

        item.setWageRate(RestServiceUtils.convertToDouble(map.get(WAGE_RATE)));
        item.setHomeWebSite((ArrayList<String>) map.get(HOME_WEBSITE));
        item.setWorkWebSite((ArrayList<String>) map.get(WORK_WEBSITE));
        item.setHomePage((ArrayList<String>) map.get(HOME_PAGE));
        item.setFtp((ArrayList<String>) map.get(FTP));
        item.setBlog((ArrayList<String>) map.get(BLOG));
        item.setProfileWebSite((ArrayList<String>) map.get(PROFILE_WEBSITE));
        item.setOtherWebSite((ArrayList<String>) map.get(OTHER_WEBSITE));
        item.setgTalk((ArrayList<String>) map.get(GTALK));
        item.setAIM((ArrayList<String>) map.get(AIM));

        item.setClientChargeRate(RestServiceUtils.convertToDouble(map.get(CLIENT_CHARGE_RATE)));
        item.setPmDepartmentID((Integer) map.get(DEPARTMENT_ID));
        item.setPositionId((Integer) map.get(POSITION_ID));
        item.setLocationId((Integer) map.get(LOCATION_ID));
        item.setStatusId((Integer) map.get(STATUS_ID));
        item.setRoleId(map.get(ROLE_ID) != null ? ((ArrayList<Integer>) map.get(ROLE_ID)).toArray(new Integer[]{}) : null);

        return item;
    }


    public NewEmployee convertToItem(Map<String, Object> map, boolean isNewEmployee) {
        NewEmployee item = new NewEmployee();
        item.setFname((String) map.get(FIRST_NAME));
        item.setLname((String) map.get(LAST_NAME));
        item.setEmail((String) map.get(EMAIL));
        item.setRole(map.get(ROLE_ID) != null ? ((List<Integer>) map.get(ROLE_ID)).get(0) : null);
        item.setDepartment((Integer) map.get(DEPARTMENT_ID));
        item.setLocationId((Integer) map.get(LOCATION_ID));
        return item;
    }
}
