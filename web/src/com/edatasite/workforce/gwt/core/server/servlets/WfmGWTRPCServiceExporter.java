package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.security.XSSFilter;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.gwtwidgets.server.spring.GWTRPCServiceExporter;
import org.gwtwidgets.server.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import java.util.UUID;

public class WfmGWTRPCServiceExporter extends GWTRPCServiceExporter {

    static final Logger log = LoggerFactory.getLogger(WfmGWTRPCServiceExporter.class);

    private XSSFilter xssFilter;

    public WfmGWTRPCServiceExporter(XSSFilter xssFilter) {
        super();
        this.xssFilter = xssFilter;
    }

    @Override
    protected void doUnexpectedFailure(Throwable e) {

        if (e instanceof ServletException || e instanceof IllegalArgumentException) {//For handling @RPCServletUtils.checkContentTypeIgnoreCase and @RPC.decodeRequest
            try {
                ServletUtils.getResponse().sendRedirect("/index.html");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } else {
            super.doUnexpectedFailure(e);
        }
    }

    @Override
    protected void onBeforeRequestDeserialized(String serializedRequest) {
        super.onBeforeRequestDeserialized(serializedRequest);
        ServerSecurityContext.getInstance().setStartDate(new Date().getTime());
        ServerSecurityContext.getInstance().setUniqueID(UUID.randomUUID().toString());
    }

    /**
     * Overridden from {@link RemoteServiceServlet} and invoked by the servlet
     * code.
     */
    @Override
    public String processCall(String payload) throws SerializationException {
        RPCRequest rpcRequest;
        try {
            //This supposed to prevent XSRF attack
            setShouldCheckPermutationStrongName(true);
            checkPermutationStrongName();

            //Clean request payload from XSS atack
            if(xssFilter!=null) {
                payload = xssFilter.stripXSS(payload);
            }
            rpcRequest = RPC.decodeRequest(payload, null, this);

            //Support RemoteServiceObfuscateTypeNames option
            setSerializationFlags(rpcRequest.getFlags());
        } catch (IncompatibleRemoteServiceException e) {
            log.error("User with SID = {} is informed about old version of app, section:{}", ServerUtils.parsePayloadGetSessionID(payload), service, e.getCause());
            return encodeResponseForFailure(null, e, null, null);
        }
        String sessionId = getSessionId();
        try {

            Method targetMethod = getMethodToInvoke(rpcRequest.getMethod());
            Object[] targetParameters = rpcRequest.getParameters();

            if (sessionId != null) {
                ServerSecurityContext.getInstance().setSessionId(sessionId);
            }
            log.info("Request = {}[ {} ] {}", ServerSecurityContext.getInstance().getUniqueID(), rpcRequest, ServerUtils.getCookiesAsStringForLog(getThreadLocalRequest()));
            if (SecurityContext.getCompanyID() != null && (SecurityContext.getInstance().getDatabase() == null || "".equals(SecurityContext.getInstance().getDatabase()))) {
                GlobalAuthJdbcSpringManager globaFacelAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
                SecurityContext.getInstance().setDatabase(globaFacelAuthJdbcSpringManager.getCompanyDatabaseName(SecurityContext.getCompanyID()));
            }

            try {
                return invokeMethodOnService(service, targetMethod, targetParameters, rpcRequest);
            } catch (IllegalArgumentException e) {
                return handleIllegalArgumentException(e, service, targetMethod, rpcRequest);
            } catch (IllegalAccessException e) {
                return handleIllegalAccessException(e, service, targetMethod, rpcRequest);
            } catch (InvocationTargetException e) {
                return handleInvocationTargetException(e, service, targetMethod, targetParameters, rpcRequest);
            } catch (UndeclaredThrowableException e) {
                return handleUndeclaredThrowableException(e, service, targetMethod, rpcRequest);
            } catch (Exception e) {
                return handleServiceException(e, service, targetMethod, rpcRequest);
            }
        } catch (IncompatibleRemoteServiceException e) {
            return handleIncompatibleRemoteServiceException(new IncompatibleRemoteServiceException(
                    "\nSessionID = " + EncryptionHelper.encodeURL(sessionId) + "\n" +
                            "Section = " + service.toString() + "\n" + e.getMessage(), e.getCause()));
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return handleExporterProcessingException(e);
        }
    }

    @Override
    protected void onAfterResponseSerialized(String serializedResponse) {
        super.onAfterResponseSerialized(serializedResponse);
        long elapsedTime = System.currentTimeMillis() - ServerSecurityContext.getInstance().getStartDate();
        log.info("Response = " + ServerSecurityContext.getInstance().getUniqueID() + " Elapsed time = " + elapsedTime + "ms");
    }

    private String getSessionId() {
        Cookie[] cookies = ServletUtils.getRequest().getCookies();

        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if(ServletUtils.getRequest().getHeader("referer") != null && ServletUtils.getRequest().getHeader("referer").contains("WebForms.html")){
                    if (cooky.getName().equals(Constants.SESSION_ID_COOKIE+"_webform")) {
                        return cooky.getValue();
                    }
                } else {
                    if (cooky.getName().equals(Constants.SESSION_ID)) {
                        return cooky.getValue();
                    }
                }
            }
        }
        return null;
    }


}
