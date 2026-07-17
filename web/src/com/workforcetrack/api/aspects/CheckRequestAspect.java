package com.workforcetrack.api.aspects;

import com.edatasite.shared.components.EncryptionHelper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 02.05.12
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
@Aspect
public class CheckRequestAspect implements APIConstants {

    private static final Logger log = LoggerFactory.getLogger(CheckRequestAspect.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RestServiceUtils restServiceUtils;


    @Around("@annotation(com.workforcetrack.api.aspects.CheckRequest) && @annotation(checkRequest)")
    public Object invoke(ProceedingJoinPoint pjp, CheckRequest checkRequest) throws Throwable {
        Map<String, Object> map = new HashMap<>();//the result map
        Object result;
        Date start = new Date();
        try {
            loggingRequest(pjp);
            if (request.getHeader("sign") != null && request.getMethod().equals("POST")) {
                StringBuilder signatureStringBuilder = new StringBuilder();
                calculateSignature(pjp, signatureStringBuilder);
                String userAgent = request.getHeader("user-agent");
                if (request.getHeader("sign") != null) {
                    String signature = request.getHeader("sign");
                    String generatedSignature = signatureStringBuilder.toString();
                    verifySignature(userAgent, signature, generatedSignature);
                }
            }
            //perform session check
            if (checkRequest.checkSession()) {
                restServiceUtils.checkAndSetAPISessionID(request.getHeader("s"));
            }
            //Actual method execution after all checks are passed
            result = pjp.proceed();
            //Set method result
            map.put(API_RESULT, result);
        } catch (BaseApiException e) {
            //re.printStackTrace();
            log.error(e.getMessage());
            map.put(API_ERROR, getErrorMap(e));
        }
        loggingResponse(start);
        ServerSecurityContext.getInstance().setSessionId(null);
        return map;
    }

    private Map<String, Object> getErrorMap(BaseApiException ex) {
        if (ex != null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("code", ex.getCode());
            errorMap.put("message", ex.getMessage());
            return errorMap;
        }
        return null;
    }

    private void verifySignature(String clientName, String sendedSignature, String generatedSignature) throws BaseApiException {
        if (clientName == null || "".equals(clientName.trim())) {
            throw ApiExceptions.CLIENT_NAME_EMPTY;
        }
        if (sendedSignature == null || "".equals(sendedSignature.trim())) {
            throw ApiExceptions.SIGNATURE_WRONG;
        }
        String clientSecretCode = EncryptionHelper.md5(EncryptionHelper.md5(clientName + API_SECRET_KEY) + API_SECRET_KEY);
        String validSignature = EncryptionHelper.md5(EncryptionHelper.md5(generatedSignature) + EncryptionHelper.md5(clientName) + EncryptionHelper.md5(clientSecretCode));
        if (!validSignature.equals(sendedSignature)) {
            throw ApiExceptions.SIGNATURE_WRONG;
        }
    }

    private void calculateSignature(ProceedingJoinPoint pjp, StringBuilder signatureStrBuilder) throws BaseApiException {
        if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
            try {
                Map<String, Object> paramsMap = (Map<String, Object>) pjp.getArgs()[0];
                if (paramsMap != null && !paramsMap.isEmpty()) {
                    appendKeyValue("", paramsMap, signatureStrBuilder);
                }
            } catch (Exception e) {
                throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
            }
        }
    }

    private void appendKeyValue(String paramName, Object paramValue, StringBuilder sb) {
        sb.append(paramName);
        if (paramValue instanceof Map) {
            TreeMap<String, Object> sortedParamNamesMap = new TreeMap<String, Object>((Map) paramValue);
            for (String key : sortedParamNamesMap.keySet()) {
                appendKeyValue(key, sortedParamNamesMap.get(key), sb);
            }
            sortedParamNamesMap.clear();
        } else if (paramValue instanceof List) {
            List listParams = (List) paramValue;
            if (listParams != null && listParams.size() > 0) {
                for (Object obj : listParams) {
                    appendKeyValue("", obj, sb);
                }
            }
        } else {
            sb.append(paramValue);
        }
    }

    private void loggingRequest(ProceedingJoinPoint pjp) {
        StringBuilder apiRequestInfo = new StringBuilder("=>=> APIRequest = [");
        ServerSecurityContext.getInstance().setUniqueID(UUID.randomUUID().toString());
        String userAgent = request.getHeader("user-agent");

        apiRequestInfo.append(ServerSecurityContext.getInstance().getUniqueID()).append("]; ");
        apiRequestInfo.append("Client: [").append(userAgent != null ? userAgent : APIConstants.UNDEFINED_USER_AGENT).append("]; ");
        apiRequestInfo.append("Method: [").append(pjp.toString()).append("]; ");
        apiRequestInfo.append("Params: [");
        if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
            Object obj = pjp.getArgs()[0];
            if (obj instanceof Map && !((Map) obj).isEmpty()) {
                Map<String, Object> paramsMap = (Map<String, Object>) obj;
                boolean isFirstArg = true;
                for (String paramName : paramsMap.keySet()) {
                    if (!isFirstArg) {
                        apiRequestInfo.append(",");
                    }
                    apiRequestInfo.append(paramName != null ? paramName : "null param:").append(":").append(paramsMap.get(paramName) != null ? paramsMap.get(paramName).toString() : "null");
                    isFirstArg = false;

                }
            }
        }
        apiRequestInfo.append("]; ");
        log.info(apiRequestInfo.toString());
    }

    private void loggingResponse(Date start) {
        Date end = new Date();
        long elapsedTime = end.getTime() - start.getTime();
        log.info("=>=> APIResponse = [" + ServerSecurityContext.getInstance().getUniqueID() + "]; Elapsed time = " + elapsedTime + "ms;");
    }
}
