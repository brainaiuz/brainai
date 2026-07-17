package com.edatasite.workforce.rest.v3.release10.auth;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.server.app.CompanyService;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.controllers.signup.GeneralFreeSignUpController;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.TokenTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.auth.dto.AccountingInitTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.ActivateReponse;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyInitTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.LoginDTO;
import com.edatasite.workforce.rest.v3.release10.auth.service.ApiAuthService;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping(value = "/auth", headers = {ApiConstants.ACCESS_TOKEN})
public class ApiAuthControllerV3 extends GeneralFreeSignUpController {
    private static final Logger log = LoggerFactory.getLogger(ApiAuthControllerV3.class);

    private final ApiAuthService apiAuthService;
    private final HttpServletRequest servletRequest;
    private final UserManager userManager;
    private final LoginServiceLocal loginServiceLocal;
    private final CompanyService companyService;
    private final CommonService commonService;

    public ApiAuthControllerV3(ApiAuthService apiAuthService,
                               HttpServletRequest servletRequest,
                               UserManager userManager,
                               LoginServiceLocal loginServiceLocal,
                               CompanyService companyService,
                               CommonService commonService) {
        this.apiAuthService = apiAuthService;
        this.servletRequest = servletRequest;
        this.userManager = userManager;
        this.loginServiceLocal = loginServiceLocal;
        this.companyService = companyService;
        this.commonService = commonService;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<TokenTO> login(@Valid @RequestBody LoginDTO request) throws RestException {
        log.info("REST request to login: {} with server name: {}", request.getEmail(), servletRequest.getServerName());
        TokenTO login = apiAuthService.login(request, servletRequest.getServerName());
        return ResultTO.success(login);
    }

    @PostMapping(path = "/log_out", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> logOut() throws RestException {
        Boolean isSuccess = loginServiceLocal.logout();
        return ResultTO.success(isSuccess && deleteDeviceToken());
    }

    private boolean deleteDeviceToken() {
        EdsUser user = userManager.getUser();
        user.setDeviceToken(null);
        user.setMobileDeviceType(null);
        userManager.update(user);
        return true;
    }

    @PostMapping(path = "/company-register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> registerCompany(@RequestBody NewCompany newCompany,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ModelAndView mav = registerCompany(request, response, newCompany, TemplateSchema.getSchema(newCompany.getAdminFName(), request.getHeader("host")));
        var authDetails = (AuthDetails) mav.getModel().get("authDetails");
        String sessionID = sessionService.obtainSession(authDetails);
        return ResultTO.success(sessionID);
    }

    @PostMapping(value = {"/company-init"}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> initializeCompany(@RequestBody CompanyInitTO requestBody) throws Exception {
        companyService.gymCompanyInit(requestBody);
        return ResultTO.success(true);
    }

    @PostMapping(value = {"/accounting-init"}, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> initializeAccounting(@RequestBody AccountingInitTO accountingInitTO) throws Exception {
        companyService.gymAccountingInit(accountingInitTO);
        return ResultTO.success(true);
    }

    @PostMapping(path = "/reset-company", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<String> resetCompany() {
        log.info("REST request to reset company");
        var sessionId = commonService.resetCompany();
        return ResultTO.success(sessionId);
    }

    @PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<TokenTO> updateAccount(@RequestBody LoginDTO request) throws RestException {
        log.info("REST request to update account");
        AccountItem account = new AccountItem();
        account.setPassword(request.getPassword());
        loginService.updateAccount(account);
        return login(request);
    }

    @PostMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ActivateReponse> activate(HttpServletRequest request,
                                              @RequestParam(value = "key") String key,
                                              @RequestParam(value = "chpass", defaultValue = "false") boolean changePass) {
        log.info("REST request to activate account");
        ActivateReponse activate = apiAuthService.activate(request, key, changePass);
        return ResultTO.success(activate);
    }

    @GetMapping(value = "/list-companies", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<List<CompanyTO>> getCompanies() throws RestException {
        log.info("REST request to list companies");
        List<CompanyTO> companies = apiAuthService.getCompanies();
        return ResultTO.success(companies);
    }

}
