package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.signup.client.rpc.ActivationLink;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.internal.signup.ApiSignUpControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiAccountControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiSignUpControllerV2.class);

    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public AccountItem getAccount(@RequestParam("sessionId") String sessionId) {
        SecurityContext.getInstance().setSessionId(sessionId);
        return loginService.getAccount();
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public void updateAccount(@RequestParam("sessionId") String sessionId, @RequestBody AccountItem account) {
        SecurityContext.getInstance().setSessionId(sessionId);
        loginService.updateAccount(account);
    }

    @RequestMapping(value = "/account/default_section", method = RequestMethod.POST)
    public ResponseEntity<AccountDTO> getDefaultSection(@RequestParam("sessionId") String sessionId) {
        SecurityContext.getInstance().setSessionId(sessionId);
        EdsUserEmailSettings settings = sessionService.getUserSettings();
        return ResponseEntity.ok(new AccountDTO(settings.getStartPage()));
    }

    @RequestMapping(value = "/account/active", method = RequestMethod.GET)
    public Boolean activeAccount(@RequestParam("uid") Integer userId, @RequestParam("cid") Integer companyId, @RequestParam("did") String dataBase) {
        SecurityContext.getInstance().setDatabase(dataBase);
        SecurityContext.getInstance().setCompanyId(companyId);
        return loginService.isActiveAccount(userId, companyId);
    }

    @RequestMapping(value = "/account/activation_link", method = RequestMethod.GET)
    public ActivationLink getActivationLink(@RequestParam("key") String key, @RequestParam("cid") Integer companyId) {
        String db = companyId != null ? globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId) : Constants.DATABASE_FREE;
        ServerSecurityContext.getInstance().setDatabase(db);
        return loginService.getActivationLink(key);
    }

    @RequestMapping(value = "/account/activation_link/{id}", method = RequestMethod.DELETE)
    public void deleteActivationLink(@PathVariable("id") Integer id, @RequestParam("cid") Integer companyId) {
        String db = companyId != null ? globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId) : Constants.DATABASE_FREE;
        ServerSecurityContext.getInstance().setDatabase(db);
        loginService.deleteActivationLink(id);
    }

    private class AccountDTO extends ResponseData {
        String defaultPage;

        public AccountDTO(String defaultPage) {
            this.defaultPage = defaultPage;
        }

        public String getDefaultPage() {
            return defaultPage;
        }
    }
}
