package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsBotActivation;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.db.BotActivationManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;

@Tag(name = "Kpi Helper Bot Activation")
@RestController
@RequestMapping(value = "/helper")
public class ApiBotActivationControllerV3 extends BaseLoginController {

    private final BotActivationManager botActivationManager;
    private final CompanyManager companyManager;

    public ApiBotActivationControllerV3(BotActivationManager botActivationManager, CompanyManager companyManager) {
        this.botActivationManager = botActivationManager;
        this.companyManager = companyManager;
    }

    @PostMapping(path = "/key", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Map<String, Integer>> getCompanyByKey(@RequestBody String key) throws RestException {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_PAID);
        EdsBotActivation activation = botActivationManager.getByKey(key);
        if (activation == null) {
            ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
            activation = botActivationManager.getByKey(key);
        }
        if (activation == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Key is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (activation.getCompanyId() == null || activation.getSessionId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Key is expired, please refresh", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        Map<String, Integer> companyMap = new HashMap<>();
        EdsCompany edsCompany = companyManager.get(activation.getCompanyId());
        companyMap.put(edsCompany.getName(), edsCompany.getObjectID());
        return ResultTO.success(companyMap);
    }

    @PostMapping(path = "/session", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> getSessionId(@RequestBody IdNameTO request) {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_PAID);
        EdsBotActivation activation = botActivationManager.getByKey(request.getName());
        if (activation == null) {
            ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
            activation = botActivationManager.getByKey(request.getName());
        }
        return ResultTO.success(activation.getSessionId());
    }
}
