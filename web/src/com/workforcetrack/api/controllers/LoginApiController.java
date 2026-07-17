package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.rest.base.exception.UserNotActivatedException;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.APISelectItemList;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.LoginApiPresenter;
import com.workforcetrack.mobile.rpc.login.MCompanyList;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;
import com.workforcetrack.mobile.services.LoginWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 15.05.12
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/login")
public class LoginApiController {

    private final RestServiceUtils restServiceUtils;

    private final LoginWebService loginWebService;

    private final HrmsServiceLocal hrmsServiceLocal;

    private final HttpServletRequest request;

    private final EmployeeService employeeService;

    private final RolePermissionService rolePermissionService;

    @Autowired
    public LoginApiController(RestServiceUtils restServiceUtils, LoginWebService loginWebService, HrmsServiceLocal hrmsServiceLocal, HttpServletRequest request, EmployeeService employeeService, RolePermissionService rolePermissionService) {
        this.restServiceUtils = restServiceUtils;
        this.loginWebService = loginWebService;
        this.hrmsServiceLocal = hrmsServiceLocal;
        this.request = request;
        this.employeeService = employeeService;
        this.rolePermissionService = rolePermissionService;
    }

    @RequestMapping(value = "/usercompanies", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest(checkSession = false)
    @ResponseBody
    public Object getUserCompanies(@RequestBody Map<String, Object> params, @RequestHeader Map<String, Object> header) throws BaseApiException {
        try {
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            String userAgent = (header != null && header.get("user-agent") != null)
                    ? (String) header.get("user-agent")
                    : APIConstants.UNDEFINED_USER_AGENT;

            String host = request.getServerName();
            MCompanyList companyList = loginWebService.getUserCompanies(username, password, userAgent, host);
            return new APISelectItemList(companyList);
        } catch (UserNotFoundException e) {
            throw ApiExceptions.USER_NOT_FOUND;
        } catch (IncorrectPasswordException e) {
            throw ApiExceptions.INCORRECT_AUTH_INFO;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (NullPointerException e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest(checkSession = false)
    @ResponseBody
    public Object login(@RequestBody Map<String, Object> params, @RequestHeader Map<String, Object> header) throws BaseApiException {
        try {
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            Integer companyID = (Integer) params.get("companyID");
            String userAgent = (header != null && header.get("user-agent") != null)
                    ? (String) header.get("user-agent")
                    : APIConstants.UNDEFINED_USER_AGENT;
            String deviceType = (String) params.get("deviceType");// Please look at the class com.edatasite.workforce.core.domain.enums.DeviceTypeEnum
            String deviceToken = (String) params.get("deviceToken");

            if (restServiceUtils.isEmptyOrNull(companyID)) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            String host = request.getServerName();
            boolean isMobileClient = userAgent.equalsIgnoreCase("Android") || userAgent.equalsIgnoreCase("iPhone");
            MUserCompanyDTO userCompanyDTO = isMobileClient
                    ? loginWebService.mobileLogin(username, password, userAgent, companyID, host)
                    : loginWebService.lightLogin(username, password, userAgent, companyID, host);

            if (userCompanyDTO == null || userCompanyDTO.getSessionID() == null || userCompanyDTO.getCompanyID() == null) {
                throw ApiExceptions.INCORRECT_COMPANY_ID;
            }

            if (!ServerUtils.isNullOrEmpty(deviceType) && !ServerUtils.isNullOrEmpty(deviceToken)) {
                loginWebService.setUserDeviceTypeAndToken(userCompanyDTO.getUserID(), deviceType, deviceToken);
            }

            String imageUrl = hrmsServiceLocal.getEmployeeImageURL(userCompanyDTO.getUserID());
            EmployeeViewItem employee = employeeService.getEmployee(userCompanyDTO.getUserID());
            LoginApiPresenter presenter = new LoginApiPresenter();
            Map<String, Object> resultMap = presenter.convertToMap(userCompanyDTO);
            resultMap.put(presenter.IMAGE_URL, imageUrl);
            resultMap.put(presenter.FIRST_NAME, employee.getFirstName());
            resultMap.put(presenter.LAST_NAME, employee.getLastName());
            resultMap.put(presenter.MIRDDLE_NAME, employee.getMiddleName());

            return resultMap;
        } catch (UserNotFoundException e) {
            throw ApiExceptions.USER_NOT_FOUND;
        } catch (IncorrectPasswordException e) {
            throw ApiExceptions.INCORRECT_AUTH_INFO;
        } catch (UserNotActivatedException e) {
            throw ApiExceptions.USER_NOT_ACTIVATED;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest()
    @ResponseBody
    public Object switchCompany(@RequestParam(value = "companyId", required = false) Integer companyId, @RequestHeader Map<String, Object> header) throws BaseApiException {
        try {
            String sessionId = request.getHeader("s");  //SESSION_ID
            String userAgent = (header != null && header.get("user-agent") != null)
                    ? (String) header.get("user-agent")
                    : APIConstants.UNDEFINED_USER_AGENT;
            String host = request.getServerName();
            MUserCompanyDTO userCompanyDTO;

            if (companyId != null && companyId != Integer.parseInt(sessionId.split("\\$")[1])) {
                userCompanyDTO = loginWebService.switchCompany(companyId, host, userAgent);
            } else {
                userCompanyDTO = loginWebService.getMainParams();
            }

            if (userCompanyDTO == null || sessionId == null || userCompanyDTO.getCompanyID() == null) {
                throw ApiExceptions.INCORRECT_COMPANY_ID;
            }

            String imageUrl = hrmsServiceLocal.getEmployeeImageURL(userCompanyDTO.getUserID());
            EmployeeViewItem employee = employeeService.getEmployee(userCompanyDTO.getUserID());
            APISelectItemList companyList = new APISelectItemList(loginWebService.getLoginedUserCompanies(employee.getUserName(), host));
            LoginApiPresenter presenter = new LoginApiPresenter();
            Map<String, Object> resultMap = presenter.convertToMap(userCompanyDTO);

            resultMap.put(BaseApiPresenter.IMAGE_URL, imageUrl);
            resultMap.put(BaseApiPresenter.FIRST_NAME, employee.getFirstName());
            resultMap.put(BaseApiPresenter.LAST_NAME, employee.getLastName());
            resultMap.put(BaseApiPresenter.MIRDDLE_NAME, employee.getMiddleName());
            resultMap.put(BaseApiPresenter.COMPANY_LIST, companyList);

            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/usercompanies1", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest(checkSession = false)
    @ResponseBody
    public Object getUserCompanies1(@RequestBody Map<String, Object> params, @RequestHeader Map<String, Object> headerParams) throws BaseApiException {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String userAgent = (String) params.get("userAgent");

        APISelectItemList resultList = null;
        try {
            MCompanyList companyList = loginWebService.getUserCompanies(username, password, userAgent, null);
            resultList = new APISelectItemList(companyList);
        } catch (UserNotFoundException e) {
            throw ApiExceptions.USER_NOT_FOUND;
        } catch (IncorrectPasswordException e) {
            throw ApiExceptions.INCORRECT_AUTH_INFO;
        }

        return resultList;
    }

    @RequestMapping(value = "/userpermissions", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest()
    @ResponseBody
    public Object getUserCorePermissions() throws BaseApiException {
        PermissionSettings permissionSettings = permissionSettings = rolePermissionService.getPermissionSettings(PermissionConstants.WORKSPACE_CONTEXT);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userID", permissionSettings.getUserID());
        result.put("roles", permissionSettings.getRoles());
        result.put("permissions", permissionSettings.getPermissions());

        return result;
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest()
    @ResponseBody
    public Object getUserPermissions(@RequestParam(value = "context", required = false, defaultValue = PermissionConstants.WORKSPACE_CONTEXT) String context) throws BaseApiException {
        PermissionSettings permissionSettings = permissionSettings = rolePermissionService.getPermissionSettings(context);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userID", permissionSettings.getUserID());
        result.put("roles", permissionSettings.getRoles());
        result.put("permissions", permissionSettings.getPermissions());

        return result;
    }
}
