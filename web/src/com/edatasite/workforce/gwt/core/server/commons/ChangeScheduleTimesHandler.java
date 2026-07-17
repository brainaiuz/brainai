package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 2/13/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeScheduleTimesHandler implements HttpRequestHandler {
    @Autowired
    private TCServiceLocal tcServiceLocal;

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ServerSecurityContext.getInstance().setSessionId(getCookie(httpServletRequest, "SESSION_ID"));

        int listLimit = 200;
        int listIndex = 1;

        boolean processStatus = true;
        do {
            int listStart = (listIndex - 1) * listLimit;

            processStatus = tcServiceLocal.reGenerateScheduledCourseTimes(listStart, listLimit);

            listIndex++;
            System.out.println("listIndex = " + listIndex);

        } while (processStatus);

        System.out.print("Successfully DONE.");
    }

    private String getCookie(HttpServletRequest request, String key) {
        if (request.getCookies() != null) {
            for (int i = 0; i < request.getCookies().length; i++) {
                if (request.getCookies()[i].getName().equals(key)) {
                    return request.getCookies()[i].getValue();
                }
            }
        }
        return null;
    }
}
