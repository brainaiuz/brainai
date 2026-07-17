package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.QueriesListDTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Tag(name = "Scripts", description = "Query Scripts Api ")
@RestController
@RequestMapping(value = "/run-scripts", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiRunSciptsControllerV3 extends BaseApiControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiRunSciptsControllerV3.class);

    @Autowired
    private StatusServiceLocal statusServiceLocal;
    private final ExecutorService executor = Executors.newFixedThreadPool(20);


    @Operation(summary = "Query Scripts Api ")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Query Scripts"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<String> createCase(@RequestBody QueriesListDTO queries) {
        log.info("REST request to run script: {}", queries);
        try {
            return ResultTO.success(statusServiceLocal.runScripts(queries));
        } catch (Exception e) {
            return ResultTO.failure(e.toString(), INVALID);
        }
    }

    @Operation(summary = "Query Scripts Api", hidden = true)
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Query Scripts"))
    @RequestMapping(path = "/async", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<?> runScriptAsync(@RequestBody QueriesListDTO queries) {
        log.info("REST request to run asyn script: {}", queries);

        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        executor.execute(() -> {
            try {
                if (queries.getDelay() != null && queries.getDelay() < 5000) {
                    Thread.sleep(queries.getDelay());
                }
                ServerSecurityContext.getInstance().setCompanyId(companyId);
                statusServiceLocal.runScripts(queries);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return ResultTO.success();
    }
}
