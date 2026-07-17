
package com.edatasite.workforce.rest.aspects;


import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ACCESS_DENIED;

/**
 * User: iabdullo
 * Date: 10.11.14 20:50
 */

@Aspect
public class CheckPermissionAspect {

    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;


    @Around("@annotation(com.edatasite.workforce.rest.aspects.CheckPermission) && @annotation(checkPermission)")
    public Object invoke(ProceedingJoinPoint pjp, CheckPermission checkPermission) throws Throwable {
        Object result = null;
        try {
            if (checkPermission.permissions().length > 0) {
                EdsUser user = userManager.getUser();
                boolean hasPermission = false;
                if (user == null && ServerSecurityContext.getInstance().getStaticUserID() != null) {
                    user = userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
                }
                if (user != null && user.getRoleCODEs().contains(EdsRole.ADMIN_CODE)) {
                    hasPermission = true;
                } else if (user != null) {
                    for (String permissionCode : checkPermission.permissions()) {
                        hasPermission = ServerUtils.hasPermission(permissionCode);
                        if (!hasPermission) {
                            break;
                        }
                    }
                }
                if (hasPermission) {
                    result = pjp.proceed();
                }
            }
        } catch (Throwable ignored) {
        }
        if (result == null) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }
        return result;
    }
}

