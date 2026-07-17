package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.RoleTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/user")
public class ApiMyUserControllerV2 extends BaseApiControllerV2 {

    @RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleTO> getAccount(@RequestParam("sessionId") String sessionId) {
        SecurityContext.getInstance().setSessionId(sessionId);
        //Get user
        EdsUser user = userManager.getUser();
        //Get User roles
        return getUserRoles(user);
    }

    private List<RoleTO> getUserRoles(EdsUser user) {
        if (user!=null && !user.getRoles().isEmpty()) {
            return user.getRoles().stream().map(role-> new RoleTO(role.getCode(), role.getName(), role.getSystem())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
