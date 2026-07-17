package com.edatasite.workforce.rest.v1.release10.hrms;

import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.EmployeeTO;
import com.edatasite.workforce.rest.base.to.HolidayTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants._TITLE;

/**
 * Created by Umidbek.
 */
@Tag(name = "Employee", description = "Employee API")
@RestController
@RequestMapping(value = "/employee", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiEmployeeControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;


    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        ListResult<EmployeeListItem> employeeList = employeeServiceLocal.getEmployeeList(filterParameter);
        ArrayList<EmployeeTO> result = new ArrayList<>();
        for (EmployeeListItem item : employeeList.getList()) {
            result.add(new EmployeeTO(item));
        }
        return successResponse(new ListResultTO<>(employeeList.getTotal(), result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        ProfileItem item = hrmsServiceLocal.editProfile(id, "FROM_HRMS_EMPLOYEE_VIEW", true);
        if (item == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(new EmployeeTO(item));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody EmployeeTO employee) {
        try {
            NewEmployee item = new NewEmployee();
            item.setObjectID(employee.getId());
            item.setAddSingleEmployee(true);
            item.setFname(employee.getFirstName());
            item.setLname(employee.getLastName());
            item.setActive(true);
            if (employee.getRole() != null)
                item.setRole(roleManager.getByCode(employee.getRole().getCode()).getObjectID());

            item.setCreatedFrom(Constants.EMPLOYEE_CREATED_FROM_ASSESSMENT);

            Integer employeeID = employeeServiceLocal.createEmployeeInternal(item, null);

            return successResponse(SUCCESS_SAVE, employeeID);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @RequestBody EmployeeTO employee) {
        ProfileItem employeeProfile = hrmsServiceLocal.editProfile(id, null, false);
        if (employee.getFirstName() != null) {
            employeeProfile.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null) {
            employeeProfile.setLastName(employee.getLastName());
        }
        if (employee.getPrimaryEmail() != null) {
            employeeProfile.setPrimaryEmail(employee.getPrimaryEmail());
        }
        if (employee.getPrimaryPhone() != null) {
            employeeProfile.setPrimaryPhone(employee.getPrimaryPhone());
        }

        try {
            hrmsServiceLocal.updateProfile(employeeProfile);
            return successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    @RequestMapping(value = "/{id}/timeslot", method = RequestMethod.GET)
    public Object getTimeSlot(@PathVariable(value = "id") Integer employeeId) {
        try {
            return successResponse(availabilityServiceLocal.getEmpTimeslot(employeeId));
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse();
        }
    }

    @RequestMapping(value = "/holidays", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getHolidays(@RequestBody MListingFilterParameter mFilterParameter) {
        ListingFilterParameter fp = mFilterParameter.convertToFilterParameters();
        fp.setYear(mFilterParameter.getYear());
        ListResult<HolidayItem> holidayResult = availabilityServiceLocal.getHolidays(fp);
        ArrayList<HolidayTO> result = new ArrayList<>();
        if (holidayResult.getList() != null) {
            for (HolidayItem item : holidayResult.getList()) {
                result.add(new HolidayTO(item));
            }
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/titles", method = RequestMethod.GET)
    public Object getTitleList() {
        return successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getContactSelectItems(_TITLE)));
    }

    @RequestMapping(value = "/maritalStatuses", method = RequestMethod.GET)
    public Object getMaritalStatusList() {
        return successResponse(WrapUtils.wrapSelectItemTOs(commonServiceLocal.convertReference2SelectItem(EdsEmployeeProfile.MARTIAL_STATUS, false, null)));
    }
}
