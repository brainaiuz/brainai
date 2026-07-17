package com.workforcetrack.api.base;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 06.02.12
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class RestServiceUtils implements RestServiceConstants {

    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String JSON_DATE_FORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CustomObjectMapper jacksonObjectMapper;

    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;

    public void checkAndSetAPISessionID(String sessionID) throws BaseApiException {
        if (sessionID == null || sessionID.trim().equals("")) {
            throw ApiExceptions.SESSION_EMPTY;
        }
        if (!sessionID.matches(Constants.SESSION_REGEX)) {
            throw ApiExceptions.SESSION_WRONG;
        }
        ServerSecurityContext.getInstance().setSessionId(sessionID);
        EdsUserSession edsUserSession = userSessionManager.getUserSession(sessionID);
        if (edsUserSession == null) {
            throw ApiExceptions.SESSION_WRONG;
        }

        // Getting sessionLength from company setting, or setting default sessionID live time
        long lastRequestTime = edsUserSession.getLastAccessTime().getTime();
        int sessionLength = APIConstants.SESSION_LIVE_TIME_15_MINUTE;
        Integer companyID = userSessionManager.getUser().getCompany().getObjectID();
        EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(companyID);
        if (settings != null && settings.getSessionLength() != null) {
            try {
                sessionLength = Integer.valueOf(settings.getSessionLength());
            } catch (NumberFormatException ex) {
                //throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
            }
        }
        // Verifying for expiration & set lastAccessTime
        if (System.currentTimeMillis() < lastRequestTime + sessionLength * APIConstants.MILLISECONDS_IN_HOUR) {
            edsUserSession.setLastAccessTime(new Date(System.currentTimeMillis()));
        } else {
            throw ApiExceptions.SESSION_EXPIRED;
        }
    }

    public Map<String, Object> getObjectValuesMap(Object obj, List<String> fieldNameList) {
        if (obj != null && fieldNameList != null && fieldNameList.size() > 0) {
            Map<String, Object> valuesMap = new HashMap<>();
            for (String fieldName : fieldNameList) {
                try {
                    Method method = new PropertyDescriptor(fieldName, obj.getClass()).getReadMethod();
                    valuesMap.put(fieldName, method.invoke(obj));
                } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            return valuesMap;
        }
        return null;
    }

    public Object getObjectValuesMap(Object newObj, Object obj, List<String> fieldNameList) {
        if (obj != null && fieldNameList != null && fieldNameList.size() > 0) {
            for (String fieldName : fieldNameList) {
                try {
                    Method method = new PropertyDescriptor(fieldName, newObj.getClass()).getReadMethod();
                    Field t = obj.getClass().getDeclaredField(fieldName);
                    t.setAccessible(true);
                    t.set(obj, method.invoke(newObj));
                } catch (IntrospectionException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return obj;
        }
        return null;
    }

    public ListingFilterParameter getListingFilterParameter(Map<String, Object> paramsMap, boolean isBriefly) {
        if (paramsMap == null || paramsMap.isEmpty()) {
            return null;
        }
        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setStart(paramsMap.get(START) != null ? (Integer) paramsMap.get(START) : START_DEFAULT);
        lfp.setLimit(paramsMap.get(LIMIT) != null ? ((Integer) paramsMap.get(LIMIT) > LIMIT_MAX ? LIMIT_MAX : (Integer) paramsMap.get(LIMIT)) : START_DEFAULT);
        lfp.setSearchKey((String) paramsMap.get(SEARCH_KEY));
        lfp.setStartDate(getDateFull((String) paramsMap.get(START_DATE)));
        lfp.setEndDate(getDateFull((String) paramsMap.get(END_DATE)));
        if (!isBriefly) {
            if (paramsMap.get(FILTER) != null) {
                Map<String, Object> filter = (Map<String, Object>) paramsMap.get(FILTER);
            }
        }
        return lfp;
    }

    public MFilterParametrs getMFilterParameter(Map<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.isEmpty()) {
            return null;
        }
        MFilterParametrs fp = new MFilterParametrs();
        fp.setStart(paramsMap.get(START) != null ? (Integer) paramsMap.get(START) : START_DEFAULT);
        fp.setLimit(paramsMap.get(LIMIT) != null ? ((Integer) paramsMap.get(LIMIT) > LIMIT_MAX ? LIMIT_MAX : (Integer) paramsMap.get(LIMIT)) : START_DEFAULT);
        fp.setSearchKey((String) paramsMap.get(SEARCH_KEY));

        //OTHER PARAMETERS
        fp.setStartDate(getDateFull((String) paramsMap.get(START_DATE)));
        fp.setEndDate(getDateFull((String) paramsMap.get(END_DATE)));
        fp.setType(getNotZeroValue((Integer) paramsMap.get(TYPE)));
        if (paramsMap.get(FILTER) != null) {
            Map<String, Object> filter = (Map<String, Object>) paramsMap.get(FILTER);
        }
        return fp;
    }

    public Integer getNotZeroValue(Integer value) {
        if (value != null && !value.equals(0)) {
            return value;
        }
        return null;
    }

    public boolean isEmptyOrNull(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Integer) {
            return obj.equals(0);
        }

        if (obj instanceof String) {
            return obj.equals("");
        }
        return false;
    }

    public boolean isEmptyOrNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            } else if (obj instanceof Integer) {
                return obj.equals(0);
            } else if (obj instanceof String) {
                return obj.equals("");
            }
        }

        return false;
    }

    public Integer getCompanyID(String appName) {
        CompanyDomain companyDomain = globalAuthJdbcSpringManager.getCompanyDomain(appName);
        return companyDomain.getCompanyID();
    }

    public boolean isIgnoreField(String columnName, String... ignoreFields) {
        boolean isIgnoreField = false;
        if (ignoreFields != null && ignoreFields.length > 0) {
            for (String field : ignoreFields) {
                if (field != null && field.equals(columnName)) {
                    isIgnoreField = true;
                    break;
                }
            }
        }
        return isIgnoreField;
    }

    public Date getDate(String dateStr) {
        if (dateStr != null && !"".equals(dateStr.trim())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
        }
        return null;
    }

    public Date getDateFull(String dateStr) {
        if (dateStr != null && !"".equals(dateStr.trim())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(JSON_DATE_FORMAT_FULL);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
        }
        return null;
    }

    public String getDateString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);
            return dateFormat.format(date);
        }
        return null;
    }

    public String getDateFullString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(JSON_DATE_FORMAT_FULL);
            return dateFormat.format(date);
        }
        return null;
    }

    public Map<String, Object> getMapFromJson(String jsonStr) throws Exception {
        if (jsonStr != null && !"".equals(jsonStr.trim())) {
            return jacksonObjectMapper.readValue(jsonStr, Map.class);
        }
        return null;
    }

    public String getJsonFromObject(Object obj) throws Exception {
        if (obj != null) {
            StringWriter writer = new StringWriter();
            jacksonObjectMapper.writeValue(writer, obj);
            return writer.toString();
        }
        return null;
    }

    public String getRequestParamInfo(BufferedReader reader) throws IOException, Exception {
        StringBuilder spResponse = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            spResponse.append(line);
        }
        //reader.close();
        reader.reset();
        Map<String, Object> map = getMapFromJson(spResponse.toString());
        if (map != null && !map.isEmpty()) {
            StringBuilder resultStrBuilder = new StringBuilder("; RequestParams : [");
            for (String key : map.keySet()) {
                Object obj = map.get(key);
                resultStrBuilder.append(key).append(":").append(obj != null ? obj.toString() : "null");
            }
            resultStrBuilder.append(" ];");
            return resultStrBuilder.toString();
        }
        return "";
    }

    public static String cleanPhoneNumber(String phone) {

        if (phone == null || "".equals(phone)) {
            return null;
        }
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        phone = phone.replace("|", "");
        phone = phone.replaceAll("||", "");
        phone = phone.replace("+", "");
        phone = "+" + phone;

        return phone;
    }

    public static Double convertToDouble(Object d) {
        Double result = 0.0;
        if (d != null) {
            if (d instanceof Float) {
                Float f = (Float) d;
                result = Double.valueOf(f);
            } else if (d instanceof Integer) {
                result = ((Integer) d) * 1.0;
            } else {
                result = (Double) d;
            }
        }
        return result;
    }

    public static BigDecimal convertToBigDecimal(Object decimal) {
        BigDecimal result = null;
        if (decimal != null) {
            if (decimal instanceof Float) {
                result = new BigDecimal((Float) decimal);
            } else if (decimal instanceof Double) {
                result = new BigDecimal((Double) decimal);
            } else if (decimal instanceof BigDecimal) {
                result = (BigDecimal) decimal;
            } else if (decimal instanceof String) {
                result = new BigDecimal((String) decimal);
            }
        }
        return result;
    }
}


