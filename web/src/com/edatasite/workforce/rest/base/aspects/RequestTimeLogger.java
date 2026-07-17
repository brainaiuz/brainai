package com.edatasite.workforce.rest.base.aspects;


import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Sherali on 1/26/2016.
 * Project web
 */
@SuppressWarnings("unused")
@Component
@Aspect
public class RequestTimeLogger {
    private static final Logger logger = LoggerFactory.getLogger(RequestTimeLogger.class);
    private static String START = "start";
    private static String LAST = "last";
    private static String UUID_P = "uuid_p";
    private static String SESSION_ID = "session_id";
    private static String REQUEST_URI = "requestUri";
    private static Random r = new Random();

    private static ThreadLocal<Map<String, Object>> contextTL = ThreadLocal.withInitial(HashMap::new);

    public static void begin(HttpServletRequest request) {
        contextTL.get().put(REQUEST_URI, request.getRequestURI());
        String uniqueID = UUID.randomUUID().toString();
        long time = System.currentTimeMillis();
        contextTL.get().put(START, time);
        contextTL.get().put(LAST, time);
        contextTL.get().put(UUID_P, uniqueID);
        contextTL.get().put(SESSION_ID, ServerUtils.getCookiesAsStringForLog(request));

        logger.info("Request ={}[ {} ] ", uniqueID, contextTL.get().get(REQUEST_URI));
    }

    public static void end() {
        logger.info("Response = " + contextTL.get().get(UUID_P) + "[ " + contextTL.get().get(REQUEST_URI) + " ] " + contextTL.get().get(SESSION_ID) + " Elapsed time = " + (System.currentTimeMillis() - (Long) contextTL.get().get(START)) + "ms");
//        logger.info(String.format("%6s ms       end       %s", System.currentTimeMillis() - (Long) contextTL.get().get(START), contextTL.get().get(REQUEST_URI)));
        contextTL.get().clear();
    }

    public boolean isEnable() {
        return contextTL.get().get(START) != null;
    }

    @Pointcut("execution(* com.edatasite.workforce.gwt.core.server.servlets.pdf.*PDFHandler.*(..)) || execution(* com.edatasite.workforce.gwt.core.server.commons.*ExcelHandler.*(..)))")
    private void controllerServiceDaoMethod() {
    }

    @Before("controllerServiceDaoMethod()")
    public void beforeControllerServiceDao(JoinPoint joinPoint) {
        if (isEnable()) {
            // It is used to calculate how much time spent in one method
            long time = System.currentTimeMillis();
            contextTL.get().put(joinPoint.getSignature().toLongString(), time);
            // Last time points record
            contextTL.get().put(LAST, time);

            /*You can always turn on the printer into the methods of time
            logger.debug(String.format("%6s ms %9s  -->  %s", time - (Long) contextTL.get().get(START),
                    "", joinPoint.getSignature().toShortString()));*/
        }
    }

    @After("controllerServiceDaoMethod()")
    public void afterControllerServiceDao(JoinPoint joinPoint) {
        if (isEnable()) {
            // Time spent in the method
            long time = System.currentTimeMillis();
            long methodBeginTime = (Long) contextTL.get().get(joinPoint.getSignature().toLongString());
            long methodElapsed = time - methodBeginTime;
            // Last time points record
            contextTL.get().put(LAST, time);

            logger.info(String.format("%6s ms %6s ms  <--  %s", time - (Long) contextTL.get().get(START),
                    methodElapsed, joinPoint.getSignature().toShortString()));
        }
    }
}
