/*
package com.edatasite.workforce.rest.base.aspects;


import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

*/
/**
 * User: iabdullo
 * Date: 10.11.14 20:50
 *//*

@Aspect
public class CheckPermissionAspect extends BaseApiControllerV1 {

    @Autowired
    private UserManager userManager;

    @Around("@annotation(com.edatasite.workforce.rest.aspects.CheckPermission) && @annotation(checkPermission)")
    public Object invoke(ProceedingJoinPoint pjp, CheckPermission checkPermission) throws Throwable {
        Object result = null;
        try {
            StringBuilder ids = new StringBuilder("");
            if (checkPermission.permissions().length > 0) {
                for (Object element : checkPermission.permissions()) {
                    if (ids.toString().equals("")) {
                        ids.append(element.toString());
                        continue;
                    }
                    ids.append("','").append(element.toString());
                }
                EdsUser user = userManager.getUser();
                Boolean hasPermission = false;
                if (user == null && ServerSecurityContext.getInstance().getStaticUserID() != null) {
                    user = userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
                }
                if (user != null && user.getRoleIds().contains(EdsRole.ADMIN)) {
                    //TODO Amin uchun Module permissionlar uida hal qilinadi.
//                    hasPermission = userManager.hasAdminPermission(ids.toString(), user.getCompany().getObjectID());
                    hasPermission = true;
                } else if (user != null) {
                    hasPermission = userManager.hasUserPermission(user.getObjectID(), ids.toString());
                }
                if (hasPermission) {
                    result = pjp.proceed();
                } else {
                    result = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return errorResponse("Permission denied!", "You do not have specific permission to access this url", 403);
        }
        if (result == null) {
            return errorResponse("Permission denied!", "You do not have specific permission to access this url", 403);
        }
        return result;
    }
}
*/
