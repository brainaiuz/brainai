package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/internal")
public class ApiSessionControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiSessionControllerV2.class);

    @Autowired
    private SessionService sessionService;

    @Operation(hidden = true)
    @RequestMapping(value = "/session", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SessionDTO> obtainSession(@RequestBody AuthDetails authDetails, HttpServletRequest request, HttpServletResponse response) throws RestException{
        ServerSecurityContext.getInstance().setDatabase(authDetails.getDatabase());
        try {
            return ResponseEntity.ok(new SessionDTO(sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails)));
        } catch (IOException e) {
            log.error("Session obtain exception: ", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Session obtain exception", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private class SessionDTO extends ResponseData {
        String sessionID;

        public SessionDTO(String sessionID) {
            this.sessionID = sessionID;
        }

        public String getSessionID() {
            return sessionID;
        }
    }
}
