package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.NewPosition;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.UserService;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.PatchRequestDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.UserAddDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.UserBatchAddDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.UserGenericAddDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.DepartmentDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.ExperienceDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.ExperienceRequestDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.ExperienceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * User : Dilsh0d Madrahimov on 9/17/2019 2:57 PM
 */
@Tag(name = "Users", description = "Users Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiUserControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiUserControllerV3.class);
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CommonService commonService;


    @Operation(summary = "Add User", description = "Add New User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object add(@RequestBody UserAddDTO userAddTO) throws RestException {

        if (userAddTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(userAddTO.getFirst_name())) {
            throw new RestException("First name is required", "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userAddTO.getLast_name())) {
            throw new RestException("Last name is required", "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(userAddTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(userAddTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address " + userAddTO.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        if (userAddTO.getDriver_id() != null && userAddTO.getDriver_id() > 0) {
            EdsEmployee employee = employeeManager.getEmployeeByDriverNumber(userAddTO.getDriver_id());
            if (employee != null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "User with this driver ID " + userAddTO.getDriver_id() + " has already been assigned to " + employee.getName(), CONFLICT, HttpStatus.CONFLICT);
            }
        }
        Integer positionId = null;
        if (StringUtils.isNotBlank(userAddTO.getPosition())) {
            String strPosition = userAddTO.getPosition().trim().replaceAll("\\s", "");
            strPosition = userAddTO.getDriver_id() != null && userAddTO.getDriver_id() > 0 ? "Driver- " + strPosition : strPosition;
            EdsPosition edsPosition = positionManager.getByName(strPosition);
            if (edsPosition != null) {
                positionId = edsPosition.getObjectID();
            }

            if (edsPosition == null) {
                NewPosition newPosition = new NewPosition();
                newPosition.setName(strPosition);
                positionId = employeeServiceLocal.createPosition(newPosition);
            }
        }

        NewEmployee employee = new NewEmployee();
        employee.setFname(userAddTO.getFirst_name());
        employee.setLname(userAddTO.getLast_name());
        employee.setEmail(userAddTO.getEmail());
        employee.setDriverNumber(userAddTO.getDriver_id());
        employee.setActive(true);
        employee.setCreatedFrom(Constants.EMPLOYEE_CREATED_FROM_ASSESSMENT);
        employee.setIsFromMultiEmployee(true);
        employee.setHasAccess(false);
        employee.setPositionId(positionId);

        Integer result;
        try {
            result = employeeServiceLocal.createEmployeeInternal(employee, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Errors.EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS == result) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "User with this email " + userAddTO.getEmail() + " already exists", CONFLICT, HttpStatus.CONFLICT);
        } else if (Errors.EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST == result) {
            throw new RestException("Invalid email address", "Invalid email address:" + userAddTO.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (result < 0) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Add Generic User", description = "Add New generic User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/add_user", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object addUser(@RequestBody UserGenericAddDTO userAddTO) throws RestException {

        if (userAddTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(userAddTO.getFirst_name())) {
            throw new RestException("First name is required", "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userAddTO.getLast_name())) {
            throw new RestException("Last name is required", "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(userAddTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(userAddTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address " + userAddTO.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }


        NewEmployee employee = new NewEmployee();
        employee.setFname(userAddTO.getFirst_name());
        employee.setLname(userAddTO.getLast_name());
        employee.setEmail(userAddTO.getEmail());
        if (StringUtils.isNotBlank(userAddTO.getDepartment())) {
            List<EdsDepartment> departments = departmentManager.getDepartmentByName(userAddTO.getDepartment());
            if (departments != null) {

                employee.setDepartment(departments.get(0).getObjectID());
            } else {
                EdsDepartment newDepartment = new EdsDepartment();
                newDepartment.setName(userAddTO.getDepartment());
                departmentManager.create(newDepartment);
                employee.setDepartment(newDepartment.getObjectID());

            }
        }
        if (StringUtils.isNotBlank(userAddTO.getCity())) {
            EdsLocation location = locationManager.getLocationByName(userAddTO.getCity());
            if (location != null) {
                employee.setLocationId(location.getObjectID());
            } else {
                EdsLocation newLocation = new EdsLocation();
                newLocation.setCity(userAddTO.getCity());
                newLocation.setCountry(countryManager.getCountryByName(userAddTO.getCountry()));
                locationManager.create(newLocation);
                employee.setLocationId(newLocation.getObjectID());
            }
        }
        if (userAddTO.getCustomFields() != null) {
            employee.setCustomFields(CustomFieldsUtils.convertCustomFields(userAddTO.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Employee), null));
        }
        employee.setActive(true);
        employee.setCreatedFrom(Constants.EMPLOYEE_CREATED_FROM_ASSESSMENT);
        employee.setIsFromMultiEmployee(true);
        employee.setHasAccess(userAddTO.isHasAccess());
        employee.setEssUser(userAddTO.isEssUser());

        Integer result;
        try {
            result = employeeServiceLocal.createEmployeeInternal(employee, null);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (Errors.EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS == result) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "User with this email " + userAddTO.getEmail() + " already exists", CONFLICT, HttpStatus.CONFLICT);
        } else if (Errors.EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST == result) {
            throw new RestException("Invalid email address", "Invalid email address:" + userAddTO.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (result < 0) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Add Multi Users", description = "Add Multi Users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/users_batch", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object addBatch(@RequestBody UserBatchAddDTO userBatchAddDTO) throws RestException {

        if (userBatchAddDTO == null || userBatchAddDTO.getUsers() == null || userBatchAddDTO.getUsers().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<NewEmployee> employees = new ArrayList<>();
        for (UserAddDTO userAddTO : userBatchAddDTO.getUsers()) {
            if (StringUtils.isBlank(userAddTO.getFirst_name())) {
                throw new RestException("First name is required", "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isBlank(userAddTO.getLast_name())) {
                throw new RestException("Last name is required", "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isNotBlank(userAddTO.getEmail())) {
                if (!EMAIL_PATTERN.matcher(userAddTO.getEmail()).matches()) {
                    throw new RestException("Invalid email address", "Invalid email address " + userAddTO.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }

            if (userAddTO.getDriver_id() != null && userAddTO.getDriver_id() > 0) {
                EdsEmployee employee = employeeManager.getEmployeeByDriverNumber(userAddTO.getDriver_id());
                if (employee != null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "User with this driver ID " + userAddTO.getDriver_id() + " has already been assigned to " + employee.getName(), CONFLICT, HttpStatus.CONFLICT);
                }
            }

            Integer positionId = null;
            if (StringUtils.isNotBlank(userAddTO.getPosition())) {
                String strPosition = userAddTO.getPosition().trim().replaceAll("\\s", "");
                strPosition = userAddTO.getDriver_id() != null && userAddTO.getDriver_id() > 0 ? "Driver- " + strPosition : strPosition;
                EdsPosition edsPosition = positionManager.getByName(strPosition);
                if (edsPosition != null) {
                    positionId = edsPosition.getObjectID();
                }

                if (edsPosition == null) {
                    NewPosition newPosition = new NewPosition();
                    newPosition.setName(strPosition);
                    positionId = employeeServiceLocal.createPosition(newPosition);
                }
            }

            NewEmployee employee = new NewEmployee();
            employee.setFname(userAddTO.getFirst_name());
            employee.setLname(userAddTO.getLast_name());
            employee.setEmail(userAddTO.getEmail());
            employee.setDriverNumber(userAddTO.getDriver_id());
            employee.setActive(true);
            employee.setCreatedFrom(Constants.EMPLOYEE_CREATED_FROM_ASSESSMENT);
            employee.setIsFromMultiEmployee(true);
            employee.setHasAccess(false);
            employee.setPositionId(positionId);
            employees.add(employee);
        }

        try {
            employeeServiceLocal.createEmployees(employees.toArray(new NewEmployee[]{}));
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Generic User", description = "Update existing generic User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/update_user", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object updateUser(@RequestBody UserGenericAddDTO dto) throws RestException {

        if (dto == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(dto.getFirst_name())) {
            throw new RestException("First name is required", "first_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(dto.getLast_name())) {
            throw new RestException("Last name is required", "last_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(dto.getEmail())) {
            if (!EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address " + dto.getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        Integer itemId = Optional.ofNullable(employeeManager.getEmployeeByEmail(dto.getEmail())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Employee with this email is not found", NOT_FOUND, HttpStatus.NOT_FOUND));

        ProfileItem employee = contactService.editProfile(itemId);

        if (employee != null) {
            employee.setFirstName(dto.getFirst_name());
            employee.setLastName(dto.getLast_name());
            employee.setPrimaryEmail(dto.getEmail());

            Optional.of(dto.isEssUser()).ifPresent(employee::setEss);

            Optional.ofNullable(dto.getDepartment()).ifPresent(employee::setDepartment);
            Optional.ofNullable(dto.getJobTitle()).ifPresent(employee::setJobTitle);

            Optional.ofNullable(dto.getGivenName()).ifPresent(employee::setMiddleName);
            Optional.ofNullable(dto.getSupervisor()).ifPresent(s -> {
                Integer reportsToId = employeeManager.getEmployeeByEmail(dto.getSupervisor());
                employee.setReportsToId(reportsToId);
            });

            Optional.ofNullable(dto.getDepartment()).ifPresent(d -> {
                List<EdsDepartment> department = departmentManager.getDepartmentByName(dto.getDepartment());
                if (department != null && !department.isEmpty()) {
                    employee.setPmDepartmentID(department.get(0).getObjectID());
                } else {
                    EdsDepartment newDepartment = new EdsDepartment();
                    newDepartment.setName(dto.getDepartment());
                    departmentManager.create(newDepartment);
                    employee.setPmDepartmentID(newDepartment.getObjectID());
                }
            });
            Optional.ofNullable(dto.getJobTitle()).ifPresent(j -> {
                EdsPosition position = positionManager.getByName(dto.getJobTitle());
                if (position != null) {
                    employee.setPositionId(position.getObjectID());
                } else {
                    EdsPosition newPosition = new EdsPosition();
                    newPosition.setName(dto.getJobTitle());
                    positionManager.create(newPosition);
                    employee.setPositionId(newPosition.getObjectID());
                }
            });
            contactService.updateProfile(employee);
        }
        return ResultTO.success();

    }

    @Operation(summary = "Patch Update Employee", description = "Patch Update Employee")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/employee", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Object patchUpdate(@RequestBody PatchRequestDto dto) throws RestException {
        Optional.ofNullable(employeeManager.get(dto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Employee with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        ProfileItem item = contactService.editProfile(dto.getId());
        List<CustomFieldRequest> cfs = new ArrayList<>();
        for (CustomFieldRequest column : dto.getColumns()) {
            if (column.getValue() != null) {
                switch (column.getAlias()) {
                    case "department" -> {
                        if (column.getValue() instanceof Integer) {
                            Optional.ofNullable(departmentManager.get((Integer) column.getValue())).ifPresent(d -> item.setPmDepartmentID(d.getObjectID()));
                        } else if (column.getValue() instanceof String) {
                            Optional.ofNullable(departmentManager.getDepartmentByName((String) column.getValue())).ifPresent(deps -> item.setPmDepartmentID(deps.get(0).getObjectID()));
                        }
                    }
                    case "position" -> {
                        if (column.getValue() instanceof Integer) {
                            Optional.ofNullable(positionManager.get((Integer) column.getValue())).ifPresent(d -> item.setPositionId(d.getObjectID()));
                        } else if (column.getValue() instanceof String) {
                            Optional.ofNullable(positionManager.getByName((String) column.getValue())).ifPresent(p -> item.setPositionId(p.getObjectID()));
                        }
                    }
                    case "empMode" -> {
                        if (column.getValue() instanceof Integer) {
                            Optional.ofNullable(referenceManager.get((Integer) column.getValue())).ifPresent(e -> item.setEmpModeId(e.getObjectID()));
                        } else if (column.getValue() instanceof String) {
                            Optional.ofNullable(referenceManager.getByName((String) column.getValue())).ifPresent(e -> item.setEmpModeId(e.getObjectID()));
                        }
                    }
                    default -> {
                        if (column.getValue() != null) {
                            cfs.add(new CustomFieldRequest(column.getAlias(), column.getValue()));
                        }
                    }
                }
            }
        }
        if (!cfs.isEmpty()) {
            EdsEmployee employee = employeeManager.get(dto.getId());
            ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.convertCustomFields(cfs, commonServiceLocal.getCompanyCustomFields(ViewName.Employee), employee.getCustomFields());
            if (customFieldItems != null) {
                item.setCustomFields(customFieldItems);
            }
        }
        contactService.updateProfile(item);
        return ResultTO.success();
    }

    @Operation(summary = "Get User Profile")
    @ApiResponses(value = @ApiResponse(responseCode = "200"))
    @RequestMapping(value = "/user_profile", method = RequestMethod.GET)
    public ResultTO<UserTO> getUserProfile() throws RestException {
        log.info("REST request to get current user profile");
        String accessToken = httpServletRequest.getHeader(ApiConstants.ACCESS_TOKEN);
        UserTO userProfile = userService.getUserProfile(accessToken);
        return ResultTO.success(userProfile);
    }

    @RequestMapping(value = "/department_users", method = RequestMethod.GET)
    public ResultTO<List<DepartmentDto>> getDepartmentUserList(@RequestParam(value = "searchText", required = false) String searchText,
                                                               @RequestParam(value = "start", required = false) Integer start,
                                                               @RequestParam(value = "limit", required = false) Integer limit) {
        log.info("REST request to get department users");
        List<DepartmentDto> departmentDtos = employeeServiceLocal.getDepartmentEmployees(searchText, start, limit);
        return ResultTO.success(departmentDtos);
    }

    @Operation(summary = "Get Experience from my.gov")
    @RequestMapping(value = "/insert_experience", method = RequestMethod.POST)
    public void insertExperience(@RequestParam("id") Integer id) throws RestException {
        EdsEmployee employee = employeeManager.get(id);
        if (employee == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Employee is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        RestTemplate restTemplate = new RestTemplate();
        ProfileItem item = contactService.editProfile(id);
        ExperienceRequestDto requestDto = new ExperienceRequestDto();
        requestDto.setPin(item.getNumberData().getNumberString());

        ExperienceResponseDto response = restTemplate.postForObject("https://api.agrobank.uz/egov/v1/experiences", getRequestEntity(requestDto), ExperienceResponseDto.class);
        if (response == null || response.getResult() == null || response.getResult().getData() == null || response.getResult().getData().getExperiences() == null) {
            return;
        }
        ArrayList<CustomTableRpc> tableItems = new ArrayList<>();
        for (ExperienceDto experience : response.getResult().getData().getExperiences()) {
            ArrayList<CompanyCustomFieldItem> cfs = commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, "ITEM_TABLE_4NxHFZacwb");
            for (CompanyCustomFieldItem cf : cfs) {
                switch (cf.getAliasName()) {
                    case "date_in" ->
                            cf.setFieldDateNonConvertedValue(StringUtils.isNotBlank(experience.getContractDate()) ? new DateNonConvertable(ServerUtils.parseDate(experience.getContractDate(), "yyyy-MM-dd")) : null);
                    case "resigned_date" ->
                            cf.setFieldDateNonConvertedValue(StringUtils.isNotBlank(experience.getEndDate()) ? new DateNonConvertable(ServerUtils.parseDate(experience.getEndDate(), "yyyy-MM-dd")) : null);
                    case "Организация" -> cf.setFieldStringValue(experience.getCompanyName());
                    case "Должность" -> cf.setFieldStringValue(experience.getPositionName());
                }
            }

            CustomTableRpc rpc = new CustomTableRpc();
            rpc.setUuid("ITEM_TABLE_4NxHFZacwb");
            rpc.setItemCustomFields(cfs);
            rpc.setSorder(tableItems.isEmpty() ? 1 : tableItems.get(tableItems.size() - 1).getSorder() + 1);
            tableItems.add(rpc);
        }
        if (item.getCustomTableItems() != null) {
            item.getCustomTableItems().put("ITEM_TABLE_4NxHFZacwb", tableItems);
        } else {
            item.setCustomTableItems(new HashMap<>(Collections.singletonMap("ITEM_TABLE_4NxHFZacwb", tableItems)));
        }
        contactService.updateProfile(item);
    }

    public <T> HttpEntity<T> getRequestEntity(T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

    @Operation(summary = "Get user list")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResultTO<ArrayList<EmployeeListItem>> getUserList(@RequestParam(value = "searchText", required = false) String searchText,
                                                             @RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                             @RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "roles", required = false) String roles) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(searchText);
        fp.setCurrentPage(Optional.ofNullable(currentPage).orElse(0));
        fp.setLimit(Optional.ofNullable(limit).orElse(50));
        if (roles != null) {
            fp.setRoles(roles);
        }
        ListResult<EmployeeListItem> employeeList = employeeServiceLocal.getEmployeeList(fp);
        return ResultTO.success(employeeList.getList());
    }

    @Operation(summary = "Deactivate current user")
    @DeleteMapping(path = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteMe() {
        log.info("REST request to deactivate user");
        employeeService.activateOrDisactivateEmployee(employeeManager.getUser().getObjectID(), false);
        return ResultTO.success();
    }

    @Operation(summary = "Get User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field will contain user information")})
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResultTO<UserTO> getUser(@PathVariable("id") Integer id) throws RestException {
        log.info("REST request to get user by id: {}", id);
        UserTO user = userService.getUserProfile(httpServletRequest.getHeader(ApiConstants.ACCESS_TOKEN), id);
        return ResultTO.success(user);
    }
}
