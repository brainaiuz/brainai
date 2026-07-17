package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/crm")
public class ApiCrmLeadControllerV2 {

    @Autowired
    private CrmServiceLocal crmService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @RequestMapping(value = "/lead_from_signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createLeadFromSignUp(@RequestBody NewCompany company) {
        try {
            SecurityContext.getInstance().setCompanyId(company.getCompanyId());
            SecurityContext.getInstance().setDatabase(EdsContextParams.getLeadSignUpCompany() != null ? globalAuthJdbcSpringManager.getCompanyClusterType(EdsContextParams.getLeadSignUpCompany()) : TenantContextHolder.PAID_DB);
            crmService.createActualLeadFromSignUpper(NewCompany.toString(company));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(true);
    }
}
