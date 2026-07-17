package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.enums.UITypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class SwitchLayoutHandler implements HttpRequestHandler, Constants {

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    private String DEFAULT_SECTION = Constants.DEFAULT_SECTION;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switchLayout(request, response);
        return;
    }

    public boolean crossLogin(HttpServletRequest request, HttpServletResponse response, AuthInfoItem authInfoItem) throws IOException {

        String hostName = globalAuthJdbcSpringManager.getClassicUIHost(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));

        if (StringUtils.isEmpty(hostName)) {
            return false;
        }

        if (BaseLoginController.FROM_BASIC_LOGIN.equals(authInfoItem.getAuthType()) && (StringUtils.isBlank(authInfoItem.getUsername()) || StringUtils.isBlank(authInfoItem.getPassword()))) {
            return false;
        } else if (BaseLoginController.FROM_FEDERATED_LOGIN.equals(authInfoItem.getAuthType()) && (StringUtils.isBlank(authInfoItem.getSocialNetworkId()) || StringUtils.isBlank(authInfoItem.getEmail()))) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        String url = "https://" + hostName + "/crossLogin?token="+token;

        RedisClient.setKey(token, authInfoItem, AuthInfoItem.class, 120/*Life time is 2 mins.*/);
        response.sendRedirect(url);

        System.out.println("TOKEN: " + token);
        System.out.println("URL: " + url);
        System.out.println("TOKEN from Redis: " + RedisClient.getKey(token));
        return true;
    }

    public boolean switchLayout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hostName = null;

        //## VALIDATION PART
        {
            if (StringUtils.isEmpty(ServerSecurityContext.getInstance().getSessionId())) {
                return false;
            }
            hostName = globalAuthJdbcSpringManager.getClassicUIHost(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));

            if (StringUtils.isEmpty(hostName)) {
                return false;
            }
        }

        Map<String, String> cookiesMap = BaseLoginController.getCookies(request);
        String workingSection = cookiesMap.get(Constants.SECTION_HTML);

        String url = "https://" + hostName + "/";
        url += StringUtils.isNotBlank(workingSection) ? workingSection : (DEFAULT_SECTION + ".html");

        String token = UUID.randomUUID().toString();

        url += "?token=" + token + "&switchlayout=true";

        //## SET CLASSIC KPI LAYOUT FOR THIS USER
        {
            String username = globalAuthJdbcSpringManager.getUsername(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()), ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID());

            System.out.println("USER_NAME: " + username);
            //set current layout
            globalAuthJdbcSpringManager.setCurrentUIToUser(UITypeEnum.CLASSIC_KPI, username);
            //include the domain to access switched layout
            globalAuthJdbcSpringManager.setDomainToUIAccess(hostName, username);
        }

        RedisClient.setKey(token, ServerSecurityContext.getInstance().getSessionId(), 120);
        response.sendRedirect(url);

        System.out.println("TOKEN: " + token);
        System.out.println("URL: " + url);
        System.out.println("TOKEN from Redis: " + RedisClient.getKey(token));
        return true;
    }
}
