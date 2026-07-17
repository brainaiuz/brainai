package com.edatasite.workforce.rest.v2.release10.core.internal.signup;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/signup", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiSignUpControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiSignUpControllerV2.class);

    @Autowired
    @Qualifier("signUpService")
    private SignUpServiceLocal signUpService;
    @Autowired
    private BackendServiceLocal backendService;
    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginService;

    @RequestMapping(value = "/new_sample_company", method = RequestMethod.POST)
    public CreatedCompany createNewSampleCompany(@RequestBody NewCompany company) {
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        Integer objectID = signUpService.getCompany();
        company.setCompanyId(objectID);

        try {
            backendService.getTemplateSchemaForID(objectID, TemplateSchema.TEMPLATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signUpService.createSampleCompany(company,TemplateSchema.TEMPLATE);
    }

    @RequestMapping(value = "/new_company", method = RequestMethod.POST)
    public CreatedCompany createNewCompany(@RequestBody NewCompany company) {
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        Integer objectID = signUpService.getCompany();
        company.setCompanyId(objectID);

        try {
            backendService.createSchemaByID(objectID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signUpService.createCompany(company);
    }

    @RequestMapping(value = "/free_trial_plan", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createFreeTrialUsagePlan(@RequestBody NewCompany company) {
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        ServerSecurityContext.getInstance().setCompanyId(company.getCompanyId());
        signUpService.createFreeTrialUsagePlan(
                company.getCompanyId(),
                company.isUk(),
                (company.getUsers() != null ? company.getUsers() : 4),
                company.getHost(),
                company.getPricingPackage());
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/register_google_services", method = RequestMethod.POST)
    public ResponseEntity<Boolean> registerGoogleServices(@RequestBody NewCompany company) {
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        ServerSecurityContext.getInstance().setCompanyId(company.getCompanyId());
        loginService.registrGoogleServices(company.getAdminEmail(), company.getGoogleAccessToken());
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/send_signup_notification", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendNotificationToUser(@RequestBody NewCompany company) {
        try {
            signUpService.sendCompanyRegistrationNotification(company);

        } catch (EdsDbException e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "/send_existing_company_notification", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendExistingCompanyNotification(@RequestBody StringBuffer stringBuffer, @RequestParam("message") String message) {
        try {
            signUpService.getParamsFromMarketPlace(stringBuffer, message);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }
}
