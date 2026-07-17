package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsProjectBudget;
import com.edatasite.workforce.core.domain.EdsProjectBudgetItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.geoip.LookupService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.server.pojo.HolidayIndicator;
import com.edatasite.workforce.gwt.core.client.rpc.AnchorParam;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.FolderPermissionEnum;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz;
import com.edatasite.workforce.rest.base.enums.NameOrder;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

public class ServerUtils implements Constants {

    private static FinancialSettingsManager financialSettingsManager = (FinancialSettingsManager) ApplicationContextProvider.applicationContext.getBean("financialSettingsManager");

    private static final Logger log = LoggerFactory.getLogger(ServerUtils.class);
    private static final NumberFormat df = new DecimalFormat("#0.00");
    private static final NumberFormat df2 = new DecimalFormat("#0");

    public static Map<Integer, String> userRoles = new HashMap<>();

    static {
        userRoles.put(ADMIN, "Admin");
        userRoles.put(DR, "Director");
        userRoles.put(TL, "Department Leader");
        userRoles.put(HR, "HR Manager");
        userRoles.put(ACCOUNTANT, "Accountant");
        userRoles.put(ADMIN_LOCATION, "Admin (location)");
        userRoles.put(SALESMAN, "Sales Manager");
        userRoles.put(CUSTOMER_SERVICE_REPRESENTATIVE, "Customer Service Representative");
        userRoles.put(SALESPERSON, "Sales Person");
        userRoles.put(PM, "Project Manager");
        userRoles.put(MEM, "Member");
        userRoles.put(CALENDAR_EDITOR, "Calendar Editor");
        userRoles.put(CLIENT, "Client");
        userRoles.put(ONE_OFF, "One-off");

    }

    //CRM TYPES
    public static final int CRM_LEAD = 1;
    public static final int CRM_ACCOUNT = 2;
    public static final int CRM_CONTACT = 3;
    public static final int CRM_OPPORTUNITY = 4;
    public static final int CRM_CAMPAIGN = 5;
    public static final int CRM_EVENT = 7;
    public static final int CRM_CASE = 8;
    public static final int CRM_SOLUTION = 9;
    public static final int REFERENCE = 10;
    public static final int EDS_COUNTRY = 11;
    public static final int EDS_REGION = 12;
    public static final int EDS_JOBTITLE = 13;
    public static final int EDS_EMPLOYEE = 14;
    public static final int EDS_CONTACT_CATEGORY = 15;

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat shortMonthFormat = new SimpleDateFormat("MMM");//Dec,Yan


    public static String getName(EdsObject o) {
        return o == null ? "N/A" : o.getName();
    }

    public static Date getDate(Date date) {
        return date == null ? null : new Date(date.getTime());
    }

    public static BigDecimal getTotal(BigDecimal total) {
        return total == null ? new BigDecimal("0.00") : total;
    }


    public static String timeSpentToString(int spent) {
        int hours = spent / 60;
        int minutes = spent % 60;
        String hourStr = Integer.toString(hours);
        if (hourStr.length() < 2) {
            hourStr = "0" + hourStr;
        }
        String minutesStr = Integer.toString(minutes);
        if (minutesStr.length() < 2) {
            minutesStr = "0" + minutesStr;
        }
        return hourStr + ":" + minutesStr;
    }

    public static String millisecondToString(long millisecond) {
        long second = millisecond / 1000;
        long minutes = second / 60;
        long seconds = second % 60;

        String minutesStr = Long.toString(minutes);
        if (minutesStr.length() < 2) {
            minutesStr = "0" + minutes;
        }

        String secondStr = Long.toString(seconds);
        if (secondStr.length() < 2) {
            secondStr = "0" + seconds;
        }
        return minutesStr + ":" + secondStr + " sec";
    }

    public static SelectItem[] getAsSelectItem(List listOfObject, final int type) {
        if (listOfObject == null || listOfObject.size() == 0) {
            return new SelectItem[]{};
        }
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        int i;
        SelectItem[] result = new SelectItem[listOfObject.size()];
        i = 0;
        for (Object object : listOfObject) {
            Integer id = null;
            String name = null, code = null, description = null;

            boolean selected = false;
            if (object instanceof EdsObject) {
                id = ((EdsObject) object).getObjectID();
                name = ((EdsObject) object).getName();
                //this. is true for EDS_CONTACT_CATEGORY, CRM_ACCOUNT, CRM_CONTACT, CRM_OPPORTUNITY, CRM_CAMPAIGN, EDS_JOBTITLE
            }
            switch (type) {
                case CRM_EVENT -> {
                    EdsEvent event = (EdsEvent) object;
                    name = event.getSubject();
                }
                case CRM_CASE -> {
                    EdsCase crmCase = (EdsCase) object;
                    name = crmCase.getSubject();
                }
                case CRM_SOLUTION -> {
                    EdsSolution solution = (EdsSolution) object;
                    name = solution.getTitle();
                }
                case REFERENCE -> {
                    EdsReference reference = (EdsReference) object;
                    description = reference.getDescription();
                    name = reference.getName();
                    result[i] = new ReferenceItem(id, name, description, reference.getCssStyle(), reference.getAntonym());
                    result[i].setCode(reference.getCode());
                    result[i].setSelected(reference.isRequiredComment());
                    ((ReferenceItem) result[i]).setRelative(reference.getRelative());
                    ((ReferenceItem) result[i]).setCustomButton(reference.isCustomButton());
                    ((ReferenceItem) result[i]).setButtonLocation(reference.getButtonLocation());
                }
                case EDS_COUNTRY -> {
                    EdsCountry country = (EdsCountry) object;
                    if (ApplicationContextProvider.applicationContext != null) {
                        WfmResourceBundleMessageSource countryLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("countryLocalizer");
                        name = countryLocalizer.localize(country.getCode(), name);
                    }
                    description = country.getAlias();
                    code = country.getCode();
                }
                case EDS_REGION -> {
                    EdsRegion region = (EdsRegion) object;
                    description = region.getCountry().getObjectID().toString();
                    code = region.getCode();
                }
                case EDS_EMPLOYEE -> {
                    if (object instanceof EdsEmployee) {
                        EdsEmployee employee = (EdsEmployee) object;
                        name = employee.getFullName();
                        selected = user.getObjectID().equals(employee.getObjectID());
                    } else if (object instanceof EmployeeListItem) {
                        EmployeeListItem employee = ((EmployeeListItem) object);
                        name = employee.getFirstName() + " " + employee.getLastName();
                        selected = user.getObjectID().equals(employee.getObjectID());
                    }
                }
            }
            if (result[i] == null) {
                if (id != null) {
                    if (name != null) {
                        if (description != null) {
                            result[i] = new SelectItem(id, name, description, selected);
                        } else {
                            result[i] = new SelectItem(id, name, null, selected);
                        }
                        result[i].setCode(code);
                    } else {
                        result[i] = new SelectItem(id);
                    }
                }
            }
            i++;
        }
        return result;
    }

    public static String transliterate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    public static Date getCountryDate(Date serverDate, String zoneID) throws RuntimeException {
        try {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            dateFormat1.setTimeZone(TimeZone.getTimeZone(zoneID));
            String s = dateFormat1.format(serverDate);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public static String randomstring() {
        return ServerUtils.randomstring(20, 30);
    }

    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte) rand('a', 'z');
        }
        return new String(b, 0);
    }

    public static int rand(int lo, int hi) {
        Random rn = new Random();
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0) {
            i = -i;
        }
        return lo + i;
    }

    public static String getDateAsString(Date date) {
        return getDateAsString(date, false);
    }

    public static String getDateAsString(Date date, boolean showTime) {
        SimpleDateFormat sdf;
        if (!showTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return sdf.format(date);
    }

    public static String getDateAndTimeFormatShort(Date date) {
        SimpleDateFormat dateAndTimeFormatShort = new SimpleDateFormat("MMM dd, HH:mm");
        return dateAndTimeFormatShort.format(date);
    }

    public static String getTimeFormatted(Date date, String... formats) {
        if (date == null) {
            return null;
        }
        String format = null;
        if (formats != null && formats.length > 0) {
            format = formats[0];
        }
        if (format == null) {
            format = "HH:mm";
        }
        SimpleDateFormat dateAndTimeFormatShort = new SimpleDateFormat(format);
        return dateAndTimeFormatShort.format(date);
    }

    public static String getDateShortFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        return dateFormat.format(date);
    }

    public static String getBankTransferDateNumber(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        return dateFormat.format(date);
    }

    public static AnchorParam parseAnchorParam(String string) {
        if (string == null || "".equals(string)) {
            return null;
        }
        String[] ray = string.split("/");
        String[] tokensArray = new String[ray.length - 1];
        System.arraycopy(ray, 1, tokensArray, 0, ray.length - 1);
        if (tokensArray.length == 0) {
            tokensArray = new String[]{""};
        }
        return new AnchorParam(ray[0], tokensArray);
    }

    public static String setUserSessionid(HttpServletRequest request) {
        String sessionId = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(SESSION_ID_COOKIE)) {
                    sessionId = cookie.getValue();
                    ServerSecurityContext.getInstance().setDummySessionId(sessionId);
                    break;
                }
            }
        }
        return sessionId;
    }

    public static Integer getMaxRoleID(String roles) {
        String[] roleArray = roles.split(",");
        List<Integer> rolle = new ArrayList<>();
        for (String aRoleArray : roleArray) {
            rolle.add(Integer.valueOf(aRoleArray));
        }
        List<Integer> roleList = getUserRolesSorted(rolle);
        if (roleList.size() > 0) {
            return roleList.get(0);
        }
        return null;
    }

    public static java.util.List<Integer> getUserRolesSorted(java.util.List<Integer> roles) {
        Integer[] sortRoles = new Integer[]{ADMIN, DR, HR, TIMESHEET_EDITOR, ACCOUNTANT, ADMIN_LOCATION, SALESMAN, CUSTOMER_SERVICE_REPRESENTATIVE,
                SALESPERSON, TL, PM, MEM, CALENDAR_EDITOR, CALENDAR_VIEWER, CLIENT, ONE_OFF};
        java.util.List<Integer> userRolesId = new ArrayList<>();
        for (Integer sortRole : sortRoles) {
            if (roles.contains(sortRole)) {
                userRolesId.add(sortRole);
            }
        }
        for (Integer role : roles) {
            if (!userRolesId.contains(role)) {
                userRolesId.add(role);
            }
        }
        return userRolesId;
    }

    /**
     * Return HTML code for currency
     *
     * @param currencyCODE - currency code
     * @return - html code
     */
    public static String getHTMLCODESForCurrency(String currencyCODE) {
        if ("USD".equals(currencyCODE)) {//currency code -- Dollar
            return "$";//symbol -- Dollar -- $
        } else if ("GBP".equals(currencyCODE)) {//currency code -- Pound Sterling
            return "£";//symbol -- Pound Sterling -- £
        } else if ("EUR".equals(currencyCODE)) {//currency code -- Euro
            return "€";//symbol -- Euro -- €
        } else if ("INR".equals(currencyCODE)) {//currency code -- Indian rupee
            return "₹";//symbol -- Indian rupee -- ₹
        }

        return "$";//default currency symbol -- Dollar -- $
    }

    public static void setEndOfTheDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
    }

    public static void setBeginningOfTheDay(Calendar calendar) {
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public static Date getStartDate(EdsCompany company, int PERIOD_TYPE, boolean registrationDate) {
        Calendar calendar = new GregorianCalendar(company.getTimeZone());
        calendar.setTime(company.getCompanyDate());
        if (PERIOD_YEAR_TO_DATE == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
        } else if (PERIOD_MONTH_TO_DATE == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, 1);
        } else if (PERIOD_WEEK_TO_DATE == PERIOD_TYPE) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else if (PERIOD_DAY_TO_DATE == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, new Date().getDate());
        } else if (PERIOD_PER_COMPANY_START_DATE == PERIOD_TYPE) {
            if (company.getCreationTime() == null) {
                calendar.set(Calendar.DATE, 1);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);

            } else {
                calendar.setTime(company.getCreationTime());
            }
        }

        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();
        Date regDate = company.getRegistrationDate();
        if (regDate != null && regDate.after(start) && registrationDate) { // This company have
            // resently signed up,
            // and start date should
            // be its reg date
            start = regDate;
            calendar.setTime(start);
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        return getCompanyDate(calendar.getTime(), company.getCountryZone().getZone().getZoneID());
    }

    public static Date getStartDate(EdsCompany company, Date date, boolean registrationDate) {

        Calendar calendar = new GregorianCalendar(company.getTimeZone());

        Date start = calendar.getTime();
        Date regDate = company.getRegistrationDate();
        if (regDate != null && regDate.after(start) && registrationDate) { // This company have
            // resently signed up,
            // and start date should
            // be its reg date
            start = regDate;
            calendar.setTime(start);
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else if (date != null) {
            calendar.setTime(date);
        } else {
            calendar.setTime(company.getCompanyDate());
        }

        return getCompanyDate(calendar.getTime(), company.getCountryZone().getZone().getZoneID());

    }

    /**
     * @param date
     * @return - beginning of the day "2017-03-14 00:00:00"
     */
    public static Date getStartDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return - ending of the day "2017-03-14 23:59:59"
     */
    public static Date getEndDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


    public static Date getEndDate(EdsCompany company, int PERIOD_TYPE) {
        Calendar calendar = new GregorianCalendar(company.getTimeZone());
        calendar.setTime(company.getCompanyDate());
        if (PERIOD_PER_YEAR == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, 31);
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        } else if (PERIOD_PER_MONTH == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } else if (PERIOD_PER_WEEK == PERIOD_TYPE) {
            calendar.set(Calendar.DATE, Calendar.SUNDAY);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return getCompanyDate(calendar.getTime(), company.getCountryZone().getZone().getZoneID());
    }

    public static Date getEndDate(EdsCompany company, Date date) {

        return getCompanyDate(date, company.getCountryZone().getZone().getZoneID());
    }

    public static Date getMonthStartDate(Date date) {
        Calendar monthStart = new GregorianCalendar();
        monthStart.setTime((Date) date.clone());
        monthStart.set(Calendar.DAY_OF_MONTH, 1);
        monthStart.set(Calendar.HOUR_OF_DAY, 0);
        monthStart.set(Calendar.MINUTE, 0);
        monthStart.set(Calendar.SECOND, 0);
        monthStart.set(Calendar.MILLISECOND, 0);

        return monthStart.getTime();
    }

    public static Date getMonthEndDate(Date date) {
        Calendar monthEnd = new GregorianCalendar();
        monthEnd.setTime((Date) date.clone());
        monthEnd.set(Calendar.DAY_OF_MONTH, 1);
        monthEnd.set(Calendar.HOUR_OF_DAY, 23);
        monthEnd.set(Calendar.MINUTE, 59);
        monthEnd.set(Calendar.SECOND, 59);
        monthEnd.set(Calendar.MILLISECOND, 999);
        monthEnd.set(Calendar.MONTH, monthEnd.get(Calendar.MONTH) + 1);
        monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.get(Calendar.DAY_OF_MONTH) - 1);

        return monthEnd.getTime();
    }

    /**
     * @param currentYear : sample 2010, or 2011, or 2020, etc.
     * @return date
     */
    public static Date getYearStartDate(int currentYear) {
        Calendar startYear = new GregorianCalendar();
        startYear.setTime(new Date());
        startYear.set(Calendar.YEAR, currentYear);
        startYear.set(Calendar.MONTH, Calendar.JANUARY);
        startYear.set(Calendar.DATE, 1);
        startYear.set(Calendar.HOUR_OF_DAY, 0);
        startYear.set(Calendar.MINUTE, 0);
        startYear.set(Calendar.SECOND, 0);
        startYear.set(Calendar.MILLISECOND, 0);
        return startYear.getTime();
    }

    /**
     * @param currentYear : sample 2010, or 2011, or 2020, etc.
     * @return date
     */
    public static Date getYearEndDate(int currentYear) {
        Calendar endYear = new GregorianCalendar();
        endYear.setTime(new Date());
        endYear.set(Calendar.YEAR, currentYear);
        endYear.set(Calendar.MONTH, Calendar.DECEMBER);
        endYear.set(Calendar.DATE, 31);
        endYear.set(Calendar.HOUR_OF_DAY, 23);
        endYear.set(Calendar.MINUTE, 59);
        endYear.set(Calendar.SECOND, 59);
        endYear.set(Calendar.MILLISECOND, 999);
        return endYear.getTime();
    }

    public static int countDays(Date start, Date end) {
        return (int) ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
    }

    public static String[] getReportPeriod() {
        String[] periods = new String[4];
        periods[0] = "Year to date";
        periods[1] = "Month to date";
        periods[2] = "Week to date";
        periods[3] = "Day to date";
        return periods;
    }

    public static Date getCompanyDate(Date d, String zoneID) throws RuntimeException {
        try {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            dateFormat1.setTimeZone(TimeZone.getTimeZone(zoneID));
            String s = dateFormat1.format(d);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public static Date getCompanyDate(Date d, EdsCompany company) throws RuntimeException {
        String zoneID = "";
        if (company != null && company.getCountryZone() != null && company.getCountryZone().getZone() != null && company.getCountryZone().getZone().getZoneID() != null) {
            zoneID = company.getCountryZone().getZone().getZoneID();
        } else {
            return d;
        }
        if (d == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            dateFormat1.setTimeZone(TimeZone.getTimeZone(zoneID));
            String s = dateFormat1.format(d);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public static int getCompanyWorkDay(Date start, Date end, Map<Integer, Integer> map) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int workDay = 1;
        while (true) {
            if (calendar.getTime().getDate() == end.getDate() && calendar.getTime().getMonth() == end.getMonth() && calendar.getTime().getYear() == end.getYear()) {
                if (map.containsKey(calendar.get(Calendar.DAY_OF_WEEK) - 1) && map.get(calendar.get(Calendar.DAY_OF_WEEK) - 1).equals(0)) {
                    workDay--;
                }
                break;
            }
            if (map.containsKey(calendar.get(Calendar.DAY_OF_WEEK) - 1) && !map.get(calendar.get(Calendar.DAY_OF_WEEK) - 1).equals(0)) {
                workDay++;
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return workDay;
    }


    public static String getCookieVal(String name, Cookie[] cookies) {
        String val = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    val = cookie.getValue();
                }
            }
        }
        return val;
    }

    private static final String HEADER_X_FORWARDED_FOR = "X-FORWARDED-FOR";

    public static String obtainClientIP(HttpServletRequest request) {
        String clientIP = null;
        clientIP = request.getRemoteAddr();
        String ip;
        if ((ip = request.getHeader(HEADER_X_FORWARDED_FOR)) != null) {
            clientIP = ip;
            int idx = clientIP.indexOf(',');
            if (idx > -1) {
                clientIP = clientIP.substring(0, idx);
            }
        }

        return clientIP;
    }


    public static String getPayPalLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return paypal_LINK_Live;
        } else {
            return paypal_LINK_Test;
        }
    }

    public static String getGoogleCheckoutLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return google_checkout_LINK_Live;
        } else {
            return google_checkout_LINK_Test;
        }
    }

    public static String getElavonLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return ELAVON_LINK_Live;
        } else {
            return ELAVON_LINK_Test;
        }
    }

    public static String getElavonXMLLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return ELAVON_XML_LINK_Live;
        } else {
            return ELAVON_XML_LINK_Test;
        }
    }

    public static String getPayMeDomain() {
        return PAYME_DOMAIN_LIVE;
    }

    public static String getPayMeDomainTest() {
        return PAYME_DOMAIN_TEST;
    }

    public static String getClickDomain() {
        return CLICK_DOMAIN_LIVE;
    }

    //only GMT timezone
    public static Date convertUserDateToServerDate(Date d, TimeZone tz) {
        try {
            TimeZone newTz = (TimeZone) tz.clone();
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            newTz.setRawOffset((-1) * newTz.getRawOffset());
            dateFormat1.setTimeZone(newTz);
            String s = dateFormat1.format(d);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    //only GMT timezone
    public static Date convertServerDateToUserDate(Date d, TimeZone tz) {
        try {
            TimeZone newTz = (TimeZone) tz.clone();
            SimpleDateFormat dateFormat1 = new SimpleDateFormat();
            dateFormat1.setTimeZone(newTz);
            String s = dateFormat1.format(d);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat();
            return dateFormat2.parse(s);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    // check ip

    public static String getClientCountryCodeByIP(HttpServletRequest request) {
        String countryCode = null;
        String clientIp = obtainClientIP(request);

//        clientIp="195.8.126.22"; // uk ip
        try {
            String sep = System.getProperty("file.separator");
            String directory = EdsContextParams.getGEOIP_REAL_PATH();

            String dbfile = directory + sep + "GeoIP.dat";

            LookupService cl = new LookupService(dbfile, LookupService.GEOIP_STANDARD);
            countryCode = cl.getCountry(clientIp).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryCode;
    }


    public static boolean dateEqual(Date firstDate, Date secondDate) {
        Calendar firstCalendar = new GregorianCalendar();
        firstCalendar.setTime(firstDate);

        Calendar secondCalendar = new GregorianCalendar();
        secondCalendar.setTime(secondDate);

        return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
                firstCalendar.get(Calendar.MONTH) == secondCalendar.get(Calendar.MONTH) &&
                firstCalendar.get(Calendar.DAY_OF_MONTH) == secondCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean dateEqualWithTimeZone(Date firstDate, Date secondDate, int offset) {


        Calendar firstCalendar = new GregorianCalendar();
        Calendar secondCalendar = new GregorianCalendar();

        Date date1 = new Date(firstDate.getTime() + offset);
        Date date2 = new Date(secondDate.getTime() + offset);

        firstCalendar.setTime(date1);
        secondCalendar.setTime(date2);

        return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
                firstCalendar.get(Calendar.MONTH) == secondCalendar.get(Calendar.MONTH) &&
                firstCalendar.get(Calendar.DAY_OF_MONTH) == secondCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String dateFormat(Date date, String pattern) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat dateFormat;
        if (ApplicationContextProvider.applicationContext != null) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            dateFormat = new SimpleDateFormat(pattern, commonLocalizer.initializeUserLocale());
        } else {
            dateFormat = new SimpleDateFormat(pattern);
        }
        return dateFormat.format(date);
    }

    public static String getShortDateFormat(EdsUser user) {
        String pattern = "MM/dd/yyyy";
        if (user != null && user.getCompany() != null) {
            EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
            if (companySettings != null) {
                pattern = companySettings.getShortDateFormat();
            }
        }
        return pattern;
    }

    public static String shortDateFormat(Date date, EdsUser user, boolean... isClientTime) {
        EdsCompany company = null;
        if (user != null && user.getCompany() != null) {
            company = user.getCompany();
        }
        return shortDateFormat(date, true, company, isClientTime);
    }

    public static String shortDateFormat(Date date, boolean shouldLocalize, EdsUser user, boolean... isClientTime) {
        EdsCompany company = null;
        if (user != null && user.getCompany() != null) {
            company = user.getCompany();
        }
        return shortDateFormat(date, shouldLocalize, company, isClientTime);
    }

    public static String shortDateFormat(Date date, EdsCompany company, boolean... isClientTime) {
        return shortDateFormat(date, true, company, isClientTime);
    }

    public static String shortDateFormat(Date date, boolean shouldLocalize, EdsCompany company, boolean... isClientTime) {
        if (date == null) {
            return "N/A";
        }
        String pattern = "MM/dd/yyyy";
        EdsCompanySettings companySettings = company != null ? company.getCompanySettings() : null;
        if (companySettings != null) {
            pattern = companySettings.getShortDateFormat();
        }
        SimpleDateFormat dateFormat;
        if (ApplicationContextProvider.applicationContext != null && shouldLocalize) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            dateFormat = new SimpleDateFormat(pattern, commonLocalizer.initializeUserLocale());
        } else {
            dateFormat = new SimpleDateFormat(pattern);
        }
        if (isClientTime != null && isClientTime.length > 0 && company != null) {
            if (isClientTime[0]) {
                return dateFormat.format(company.getCreator().getUserDate(date));
            }
        }
        return dateFormat.format(date);
    }

    public static String longDateFormat(Date date, EdsUser user, boolean... isClientTime) {
        if (date == null) {
            return "N/A";
        }
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        String shortDateFormat = "yyyy-MM-dd HH:mm:ss";
        if (companySettings != null) {
            shortDateFormat = companySettings.getLongDateFormat();

        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(shortDateFormat);
        if (ApplicationContextProvider.applicationContext != null) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            dateFormat = new SimpleDateFormat(shortDateFormat, commonLocalizer.initializeUserLocale());
        } else {
            dateFormat = new SimpleDateFormat(shortDateFormat);
        }
        if (isClientTime != null && isClientTime.length > 0) {
            if (isClientTime[0]) {
                return dateFormat.format(user.getUserDate(date));
            }
        }
        return dateFormat.format(date);
    }

    public static String longDateFormat(Date date, EdsCompany company, boolean... isClientTime) {
        if (date == null) {
            return "N/A";
        }
        EdsCompanySettings companySettings = company.getCompanySettings();
        String shortDateFormat = "yyyy-MM-dd HH:mm:ss";
        if (companySettings != null) {
            shortDateFormat = companySettings.getLongDateFormat();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(shortDateFormat);
        if (isClientTime != null && isClientTime.length > 0) {
            if (isClientTime[0]) {
                return dateFormat.format(company.getCreator().getUserDate(date));
            }
        }
        return dateFormat.format(date);
    }

    public static String dateDayFormat(Date date) {
        if (date == null) {
            return "N/A";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(date);
    }

    public static String dateMonthFormat(Date date) {
        if (date == null) {
            return "N/A";
        }
        String pattern = "MMMM";
        SimpleDateFormat dateFormat;
        if (ApplicationContextProvider.applicationContext != null) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            dateFormat = new SimpleDateFormat(pattern, commonLocalizer.initializeUserLocale());
        } else {
            dateFormat = new SimpleDateFormat(pattern);
        }
        return dateFormat.format(date);
    }

    public static String[] getPermissions(EdsTaskPermission tPermissions) {
        List<String> permission = new ArrayList<>();
        if (tPermissions.isAssigneeEdit()) {
            permission.add(TaskPermissionEnum.ASSIGNEE_EDIT.getCode());
        }
        if (tPermissions.isAssigneeStatusEdit()) {
            permission.add(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode());
        }
        if (tPermissions.isAssigneeView()) {
            permission.add(TaskPermissionEnum.ASSIGNEE_VIEW.getCode());
        }
        if (tPermissions.isDelete()) {
            permission.add(TaskPermissionEnum.DELETE.getCode());
        }
        if (tPermissions.isEdit()) {
            permission.add(TaskPermissionEnum.EDIT.getCode());
        }
        if (tPermissions.isFullControl()) {
            permission.add(TaskPermissionEnum.FULL_CONTROL.getCode());
        }
        if (tPermissions.isPermissionsEdit()) {
            permission.add(TaskPermissionEnum.PERMISSIONS_EDIT.getCode());
        }
        if (tPermissions.isStatusEdit()) {
            permission.add(TaskPermissionEnum.STATUS_EDIT.getCode());
        }
        if (tPermissions.isTimesheetEntryAdd()) {
            permission.add(TaskPermissionEnum.TIMESHEET_ENTRY_ADD.getCode());
        }
        if (tPermissions.isView()) {
            permission.add(TaskPermissionEnum.VIEW.getCode());
        }

        return permission.toArray(new String[]{});
    }

    public static String[] getPermissions(EdsDocumentPermission tPermissions) {
        List<String> permission = new ArrayList<>();
        if (tPermissions.hasRead()) {
            permission.add(FolderPermissionEnum.READ.getCode());
        }
        if (tPermissions.hasWrite()) {
            permission.add(FolderPermissionEnum.WRITE.getCode());
        }
        if (tPermissions.hasDelete()) {
            permission.add(FolderPermissionEnum.DELETE.getCode());
        }
        if (tPermissions.hasModifyACL()) {
            permission.add(FolderPermissionEnum.FULL_CONTROL.getCode());
        }
        return permission.toArray(new String[]{});
    }

    /**
     * Cancat array String
     *
     * @param data Array string
     * @return string data
     */
    public static String concatArray(String[] data) {
        StringBuilder text = new StringBuilder();
        for (int i = 1; i < data.length; i++) {
            text.append(data[i]);
        }
        return text.toString();
    }

    /**
     * @param data ="e,x,c,a,m,p,l,e" <br/> splitSymbol = ","
     * @return Array String
     */
    public static String[] splitStringToArray(String data, String splitSymbol) {
        if (data == null || "".equals(data)) {
            return new String[0];
        }
        return data.split(splitSymbol);
    }

    public static String convertToString(ArrayList<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Integer id : list) {
            sb.append(id).append(", ");
        }

        // Remove the last comma and space
        sb.setLength(sb.length() - 2);

        return sb.toString();
    }

    public static Date parseDate(String date, String... pattern) {
        if (date != null && !"".equals(date)) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
            if (pattern != null && pattern.length > 0 && !"".equals(pattern[0])) {
                format = new SimpleDateFormat(pattern[0]);
                if (pattern.length >= 2 && pattern[1] != null && !"".equals(pattern[1])) {
                    format = new SimpleDateFormat(pattern[0], new Locale(pattern[1]));
                }
            }
            try {
                return format.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date setDateHourMinutSecondZero(Date date) {
        if (date != null) {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            return date;
        }
        return null;
    }

    /**
     * @param date
     * @return year to format yyyy, sample 2011
     */
    public static int getYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(date));
    }

    /**
     * @param from
     * @param to
     * @return year to format yyyy, sample 2011
     */
    public static List<Integer> getYears(Date from, Date to) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Integer fromYear = Integer.parseInt(format.format(from));
        Integer toYear = Integer.parseInt(format.format(to));
        List<Integer> years = new ArrayList<>();
        if (fromYear <= toYear) {
            for (int i = fromYear; i <= toYear; i++) {
                years.add(i);
            }
        }
        return years;
    }

    /**
     * Sort SelectIteam Array
     */
    public static SelectItem[] sortSelectItem(SelectItem[] items) {
        Arrays.sort(items, (o1, o2) -> {
            if (o1.getName() == null) {
                return -1;
            }
            if (o2.getName() == null) {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        });
        return items;
    }

    /**
     * Sort SelectIteam Array
     */
    public static SelectItem[] sortSelectItemByDesc(SelectItem[] items) {
        Arrays.sort(items, (o1, o2) -> {
            if (o1.getDescription() == null) {
                return -1;
            }
            if (o2.getDescription() == null) {
                return -1;
            }
            return o1.getDescription().compareToIgnoreCase(o2.getDescription());
        });
        return items;
    }

    /**
     * Will return newly added Ids, and Removed Ids, also non-changed Ids. To be used where multi-selection
     * options used, such as Task Assignees, Event Assignees, etc... In order to compare previus state with new one
     *
     * @param newList - new state of selected Ids, will return newly selected Ids by reference
     * @param oldList - previous state of selected Ids, will return Ids that need to be removed due to new state by reference
     * @return intersect - non-changed part of Ids by reference
     */
    public static ArrayList<?> intersect(List<?> newList, List<?> oldList) {
        ArrayList<Object> intersect = new ArrayList<>();
        if (newList != null) {
            intersect.addAll(newList);
        }
        if (oldList != null) {
            intersect.retainAll(oldList);
        }
        if (newList != null) {
            newList.removeAll(intersect);
        }
        if (oldList != null) {
            oldList.removeAll(intersect);
        }
        return intersect;
    }

    public static String getAsCommoDelimited(List collection, String returnIfNull, String... delimitrs) {
        if (collection != null && collection.size() > 0 && collection.get(0) instanceof EdsObject) {
            return getAsCommaDelimited((List<EdsObject>) collection);
        }
        if (collection == null || collection.size() == 0) {
            return returnIfNull;
        }
        String delimitr = ",";
        if (delimitrs != null && delimitrs.length > 0) {
            delimitr = delimitrs[0];
        }
        StringBuilder ids = new StringBuilder();
        String delim = "";
        for (Object element : collection) {
            if (element != null && !"".equals(element.toString())) {
                ids.append(delim).append(element);
                delim = delimitr;
            }
        }
        return "".contentEquals(ids) ? returnIfNull : ids.toString();
    }

    private static String getAsCommaDelimited(List<EdsObject> edsObjects) {
        StringBuilder result = null;
        for (EdsObject edsObject : edsObjects) {
            if (result == null) {
                result = new StringBuilder("(" + edsObject.getObjectID());
            } else {
                result.append(",").append(edsObject.getObjectID());
            }
        }
        if (result != null) {
            result.append(")");
        }
        return result.toString();
    }

    public static Date getLastOneDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        return calendar.getTime();
    }

    /**
     * Returns new Date + add days
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return date == null ? null : new Date(date.getYear(), date.getMonth(), date.getDate() + days, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    public static EdsCurrency getCurrencyIDByCountry(EdsCountry edsCountry) {
        if (edsCountry != null && edsCountry.getCurrency() != null) {
            return edsCountry.getCurrency();
        }
        return null;
    }


    /**
     * convert String to XML Document and return content  xml document
     *
     * @param content
     * @return
     */
    public static Document convertStringToXMLDoc(String content) {
        try {
            //Create instance of DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //Get the DocumentBuilder
            DocumentBuilder parser = factory.newDocumentBuilder();

            Reader reader = new CharArrayReader(content.toCharArray());

            //Create DOM Document by layout content

            return parser.parse(new org.xml.sax.InputSource(reader));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    /**
     * convert XML Document To String content and return string content
     *
     * @param doc
     * @return
     */
    public static String convertXMLDocToString(Document doc) {

        String content = null;

        try {

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.METHOD, "xml");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            content = sw.toString();
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return content;
    }

    public static Date getServerDateByUserDate(Date serverDate, Date userDate) {
        return new Date(serverDate.getTime() - (userDate.getTime() - serverDate.getTime()));
    }

    public static String getCookiesAsStringForLog(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : request.getCookies()) {
            if (USER_NAME_COOKIE.equals(cookie.getName())
                    || SECTION_HTML.equals(cookie.getName())
                    || USER_AGENT.equals(cookie.getName())
                    || WEBAUTHTOKEN.equals(cookie.getName())) {
                sb.append(cookie.getName()).append(" =[ ").append(cookie.getValue()).append(" ];");
            }
        }
        return sb.toString();
    }

    public static Map<String, Integer> mapNameIDs(List<Object[]> objects) {
        Map<String, Integer> map = new HashMap<>();
        if (objects != null && objects.size() > 0) {
            objects.forEach(ob -> {
                if (ob.length == 2) {
                    String name = (String) ob[0];
                    Integer id = (Integer) ob[1];
                    if (!isNullOrEmpty(name)) {
                        map.put(name.trim(), id);
                    }
                }
            });
        }
        return map;
    }

    public static Map<String, Integer> listToMapCountryIDs(List<EdsCountry> countryList) {
        Map<String, Integer> sourceMap = new HashMap<>();
        for (EdsCountry country : countryList) {
            String aliases = country != null ? country.getAlias() : "";
            String name = country != null ? country.getName() : "";
            if (aliases != null && !"".equals(aliases)) {
                for (String alias : aliases.split(";")) {
                    if (alias != null && !"".equals(alias)) {
                        sourceMap.put(alias.toLowerCase(), country.getObjectID());
                    }
                }
            }
            if (name != null && !"".equals(name)) {
                sourceMap.put(name.toLowerCase(), country.getObjectID());
            }
        }
        return sourceMap;
    }

    public static Map<String, Integer> listToMapRegionIDs(List<EdsRegion> regionList) {
        Map<String, Integer> sourceMap = new HashMap<>();
        for (EdsRegion state : regionList) {
            String aliases = state != null ? state.getAlias() : "";
            String name = state != null ? state.getName() : "";
            if (aliases != null && !"".equals(aliases)) {
                for (String alias : aliases.split(";")) {
                    if (alias != null && !"".equals(alias)) {
                        sourceMap.put(alias.toLowerCase(), state.getObjectID());
                    }
                }
            }
            if (name != null && !"".equals(name)) {
                sourceMap.put(name.toLowerCase(), state.getObjectID());
            }
        }
        return sourceMap;
    }


    public static List<Integer> getStringAsList(String ids, String delimitr) {
        List<Integer> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            for (String item : ids.split(delimitr)) {
                if (!isNullOrEmpty(item)) {
                    Integer t;
                    try {
                        t = Integer.valueOf(item);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        continue;
                    }
                    list.add(t);
                }
            }
        }
        return list;
    }

    public static String checkForDeleted(String... fields) {
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(" and ");
            }
            String field = fields[i];
            field = field.trim();
            sql.append(" (" + field + " <> true or " + field + " is null) ");
        }
        return sql.toString();
    }

    /**
     * list must be type of List<Eds...>
     *
     * @param list
     * @return
     */
    public static <E extends EdsObject> Map<Integer, E> getListAsMapIntegerAndValue(List<E> list) {
        return EdsObject.getListAsMapIntegerAndValue(list);
    }

    public static <T extends Object> Set<T> listToSet(List<T> list) {
        if (list == null) {
            return null;
        }
        return new HashSet<>(list);
    }

    public static URL getWebServiceUrl(String wsdlUrl) {
        URL url = null;
        try {
            url = new URL(wsdlUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getSelectItemsAsCommaDelimeted(SelectItem[] selectItems, boolean onlySelected) {
        StringBuilder s = new StringBuilder();
        SelectItem[] selecteds = onlySelected ? SelectItem.getOnlySelecteds(selectItems) : selectItems;
        if (selectItems != null && selectItems.length > 0) {
            String delimitr = "";
            for (SelectItem selectItem : selecteds) {
                if (selectItem != null) {
                    s.append(delimitr + selectItem.getName());
                    delimitr = ", ";
                }
            }
        }
        return s.toString();
    }

    public static String getSelectItemIdAsCommaDelimeted(SelectItem[] selectItems) {
        StringBuilder s = new StringBuilder();
        if (selectItems != null && selectItems.length > 0) {
            String delimitr = "";
            for (SelectItem selectItem : selectItems) {
                if (selectItem != null) {
                    s.append(delimitr + selectItem.getId());
                    delimitr = ",";
                }
            }
        }
        return s.toString();
    }

    public static String collectionToCommaDelimitedString(Collection<?> collection) {
        return collectionToDelimitedString(collection, ", ");
    }

    public static String collectionToDelimitedString(Collection<?> collection, String delimiter) {
        if (collection == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String getSplitEmailContent(String contentType, String fileName) {
        if (contentType == null || "".equals(contentType)) {
            return "";
        }
        try {
            return contentType.replace("; name=" + fileName, "").toLowerCase();
        } catch (Exception e) {
//            e.printStackTrace();
            int index = contentType.indexOf("; name=");
            index = Math.max(index, 0);
            return contentType.substring(0, index);
        }
    }

    public static Calendar convertDateIntoCalendar(Date dateToConvert) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToConvert);
        return calendar;
    }

    public static Integer getDateDiff(Calendar d1, Calendar d2, int calUnit) {
        int sign = 1;
        if (d1.after(d2)) {    // make sure d1 < d2, else swap them
            Calendar temp = d1;
            d1 = d2;
            d2 = temp;
            sign = -1;
        }
        if (calUnit == Calendar.WEEK_OF_YEAR) {
            d1.set(Calendar.DAY_OF_WEEK, d1.getFirstDayOfWeek());
            d2.set(Calendar.DAY_OF_WEEK, d2.getFirstDayOfWeek());
        }
        for (int i = 1; ; i++) {
            d1.add(calUnit, 1);   // add one day, week, year, etc.
            if (d1.getTime().after(d2.getTime())) {
                return (i - 1) * sign;
            }
        }
    }

    public static String getTimeSpentHM(Integer timeSpent) {
        String timeSpentHM = "00:00";
        if (timeSpent == null || timeSpent.equals(0)) {
            return timeSpentHM;
        }
        timeSpentHM = "";
        if (timeSpent / 60 < 9) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpent % 60 < 9) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpent % 60;
        return timeSpentHM;
    }

    /**
     * Parse GWT payload to extract SessionID
     *
     * @param payload GWT RPC Payload
     * @return Session ID
     */
    public static String parsePayloadGetSessionID(String payload) {
        if (payload != null && payload.contains("#") && payload.contains("|")) {
            return payload.substring(payload.indexOf("#") + 1, payload.indexOf("|", payload.indexOf("#")));
        }
        return null;
    }

    public static void removeCookie(String name, HttpServletResponse response) {
        removeCookie(name, "/auth", response);
    }

    public static void removeCookie(String name, String path, HttpServletResponse response) {
        if (name != null) {
            Cookie cook = new Cookie(name, null);
            cook.setMaxAge(0);
            cook.setPath(path);
            response.addCookie(cook);
        }
    }

    public static Locale getUserLocale() {
        if (ApplicationContextProvider.applicationContext != null) {
            WfmResourceBundleMessageSource referenceWfmMessageSource = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
            return referenceWfmMessageSource.initializeUserLocale();
        }
        Locale locale = EdsContextParams.getDefaultLocale(EdsContextParams.getHostname());
        return locale != null ? locale : Locale.ENGLISH;
    }

    public static void fillHostParameters(HttpServletRequest request) {
        EdsHostBasedSetting settings = EdsContextParams.getHostSetting(request.getServerName());
        String logoImage = settings.getLogoImage() != null ? settings.getLogoImage() : "";
        String productName = settings.getProductName() != null ? settings.getProductName() : "";
        String hostName = settings.getHostname() != null ? settings.getHostname() : "";
        String skype = settings.getSkype() != null ? settings.getSkype() : "";
        String email = settings.getEmail() != null ? settings.getEmail() : "";
        String supportPhone = settings.getPhone() != null ? settings.getPhone() : "";
        String helpHost = settings.getHelpHost() != null ? settings.getHelpHost() : "";
        BigDecimal vatN = settings.getVAT() != null ? settings.getVAT() : new BigDecimal("0.20");//for UK based to KPI
        String defaultLocale = settings.getDefaultLocale() != null ? settings.getDefaultLocale().getLanguage() : Locale.ENGLISH.getLanguage();
        String defaultTheme = settings.getDefaultTheme() != null ? settings.getDefaultTheme() : "workforce";
        String defaultCurrencyCODE = settings.getCurrencyCODE() != null ? settings.getCurrencyCODE() : "USD";
        String productNameWithOutWhiteSpace = productName.replace(' ', '+');
        String fullHost = EdsContextParams.getFullHost() != null ? EdsContextParams.getFullHost() : "";
        String auth2ConsumerKey = EdsContextParams.getOauth2ConsumerKey() != null ? EdsContextParams.getOauth2ConsumerKey() : "";
        String auth2ConsumerSecret = EdsContextParams.getOauth2ConsumerSecret() != null ? EdsContextParams.getOauth2ConsumerSecret() : "";
        Boolean isCaptchaEnabled = EdsContextParams.isCaptchaEnabled(request.getServerName());
        Integer freeTrialDays = EdsContextParams.getFreeTrialDays(request.getServerName());
        String currencyCODE = EdsContextParams.getCurrencyCODE(request.getServerName());

        request.setAttribute("logoImage", logoImage);
        request.setAttribute("productName", productName);
        request.setAttribute("productNameLower", productNameWithOutWhiteSpace.toLowerCase());
        request.setAttribute("hostName", hostName);
        request.setAttribute("skype", skype);
        request.setAttribute("email", email);
        request.setAttribute("supportPhone", supportPhone);
        request.setAttribute("helpHost", helpHost);
        request.setAttribute("vatN", vatN);
        request.setAttribute("defaultLocale", defaultLocale);
        request.setAttribute("defaultTheme", defaultTheme);
        request.setAttribute("defaultCurrencyCODE", defaultCurrencyCODE);
        request.setAttribute("fullHost", fullHost);
        request.setAttribute("auth2ConsumerKey", auth2ConsumerKey);
        request.setAttribute("auth2ConsumerSecret", auth2ConsumerSecret);
        request.setAttribute("isCaptchaEnabled", isCaptchaEnabled);
        request.setAttribute("freeTrialDays", freeTrialDays);
        request.setAttribute("currencyCODE", currencyCODE);
    }

    public static String evaluateQuery(String query, ListingFilterParameter fp) {
        query = query.replace("\\$\\{position}", String.valueOf(fp.getPositionID() != null ? fp.getPositionID() : 0));
        return query;
    }

    public static String makeCamelCase(String text) {
        boolean toLower = true;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar == ' ') {
                toLower = false;
            } else if (toLower) {
                builder.append(Character.toLowerCase(currentChar));
            } else {
                builder.append(Character.toUpperCase(currentChar));
                toLower = true;
            }
        }
        return builder.toString();
    }

    public static String asListToString(List<String> list) {

        String concatString = "";
        if (list != null) {
            for (String contact : list) {
                if (!"".equals(concatString)) {
                    concatString += ", ";
                }
                if (ApplicationContextProvider.applicationContext != null) {
                    WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
                    concatString += commonLocalizer.localize(makeCamelCase(contact), contact);
                } else {
                    concatString += contact;
                }
            }
        }
        return concatString;
    }

    public static SelectItem[] asListToSelectItem(List<Integer> ids, List<String> names, List<String> numbers) {
        if (ids != null && !ids.isEmpty() && numbers != null) {
            SelectItem[] selectItems = new SelectItem[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                try {
                    selectItems[i] = new SelectItem(ids.get(i), names.get(i), numbers.get(i));
                } catch (Exception e) {
                    selectItems[i] = new SelectItem(ids.get(i), "No Name :" + ids.get(i), "No Number :" + ids.get(i));
                }
            }
            return selectItems;
        }
        return null;
    }

    public static SelectItem[] asListToSelectItem(List<Integer> ids, List<String> names) {
        if (ids != null && !ids.isEmpty()) {
            SelectItem[] selectItems = new SelectItem[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                selectItems[i] = new SelectItem(ids.get(i), names.get(i));
            }
            return selectItems;
        }
        return null;
    }

    public static String asListReferenceToString(List<? extends EdsObject> list) {
        StringBuffer referenceString = new StringBuffer();
        if (list != null && list.size() > 0) {
            list.sort(Comparator.comparing(EdsObject::getName));
            for (EdsObject reference : list) {
                if (referenceString.length() != 0) {
                    referenceString.append(", ");
                }
                referenceString.append(reference.getName());
            }
        }
        return referenceString.toString();
    }

    public static String integerListToString(List<Integer> list) {
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ids.append(list.get(i));
            if (i != list.size() - 1) {
                ids.append(", ");
            }
        }
        return ids.toString();

    }

    public static String contactToStringAttr(String _string, Integer attr) {
        if (!"".equals(_string)) {
            _string += ",";
        }
        _string += attr;
        return _string;
    }

    public static boolean equalsString(String str1, String str2) {
        if (str1 == null) {
            str1 = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        return str1.equals(str2);
    }

    public static boolean equalsInteger(Integer integer1, Integer integer2) {
        return integer1 == null && integer2 == null || !(integer1 == null || integer2 == null) && integer1 == integer2;
    }

    public static boolean equalsBoolean(Boolean boolean1, Boolean boolean2) {
        return boolean1 == null && boolean2 == null || !(boolean1 == null || boolean2 == null) && boolean1.equals(boolean2);
    }

    public static boolean equalsDouble(Double double1, Double double2) {
        return double1 == null && double2 == null || !(double1 == null || double2 == null) && Double.compare(double1, double2) == 0;
    }

    public static boolean equalsDoubleCustom(Double double1, Double double2) {
        if (double1 == null) {
            double1 = 0.d;
        }
        if (double2 == null) {
            double2 = 0.d;
        }
        return Double.compare(double1, double2) == 0;
    }

    public static boolean equalsFloat(Float float1, Float float2) {
        return float1 == null && float2 == null || !(float1 == null || float2 == null) && Float.compare(float1, float2) == 0;
    }

    public static Float decimalPrecision(Float value, Integer precision) {
        if (value != null && precision != null) {
            Float percent = 0f;
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_EVEN); // Number 6 is RoundingMode.HALF_EVEN. is manually set to avoid excessive import
            percent = bigDecimal.floatValue();
            return percent;
        } else {
            return 0f;
        }
    }

    public static float round(float value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static double roundAvoid(double value, int places) {
        return Math.round(value * 100) / 100.0;
    }

    public static Double decimalPrecision(Double value, Integer precision) {
        if (value != null && precision != null) {
            BigDecimal bigDecimal = new BigDecimal(value);
            bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_EVEN);
            return bigDecimal.doubleValue();
        } else {
            return 0d;
        }
    }

    public static BigDecimal decimalPrecision(BigDecimal bigDecimal, Integer precision) {
        if (bigDecimal != null && precision != null) {
            bigDecimal = bigDecimal.setScale(precision, RoundingMode.HALF_EVEN);
            return bigDecimal;
        }
        return null;
    }

    public static boolean equalsDate(Date date1, Date date2) {
        return date1 == null && date2 == null || !(date1 == null || date2 == null) && DateUtils.isSameDay(date1, date2);
    }

    public static boolean equalsReference(EdsReference ref1, EdsReference ref2) {
        return ref1 == null && ref2 == null || !(ref1 == null || ref2 == null) && ref1.equals(ref2);
    }

    public static boolean equalsListReference(List<? extends EdsObject> list1, List<? extends EdsObject> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        return new HashSet<>(list1).containsAll(list2) && new HashSet<>(list2).containsAll(list1);
    }

    public static boolean equalsIntegerList(ArrayList<? extends Integer> list1, List<? extends Integer> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        return list1.containsAll(list2) && new HashSet<>(list2).containsAll(list1);
    }

    public static boolean equalsEdsObject(EdsObject ref1, EdsObject ref2) {
        return ref1 == null && ref2 == null || !(ref1 == null || ref2 == null) && ref1.equals(ref2);
    }

    public static boolean equalsBigDecimal(BigDecimal ref1, BigDecimal ref2) {
        return ref1 == null && ref2 == null || !(ref1 == null || ref2 == null) && ref1.equals(ref2) || (ref1 != null && ref2 != null && ref1.compareTo(ref2) == 0);
    }

    public static Integer getDailyAverageTimeslotMinutes(Set<EdsTimeSlotItem> timeSlotItems) {
        int businessDayCounter = 0;
        int workMinutes = 0;
        for (EdsTimeSlotItem timeSlotItem : timeSlotItems) {
            if (!timeSlotItem.getStartTime().equals(timeSlotItem.getEndTime())) {
                businessDayCounter++;
                workMinutes += ((timeSlotItem.getEndTime() - timeSlotItem.getStartTime()) - (timeSlotItem.getLunchEnd() - timeSlotItem.getLunchStart()) - (timeSlotItem.getCoffeeEnd() - timeSlotItem.getCoffeeStart()));
            }
        }
        if (businessDayCounter == 0) {
            return 0;
        }
        return (int) Math.round(((double) workMinutes / (double) businessDayCounter));
    }

    /**
     * This method is deprecated. Pls user two methods below. It's enough to check with a code.
     * Because the code is unique in db. Checking with a context is not necessary !
     *
     * @param code
     * @return
     */

    public static boolean hasPermission(String code) {
        if (ApplicationContextProvider.applicationContext != null) {
            RolePermissionServiceLocal rolePermissionService = (RolePermissionServiceLocal) ApplicationContextProvider.applicationContext.getBean("rolePermissionService");
            return rolePermissionService.hasPermission(code);
        }
        return false;
    }

    public static boolean hasReportingPermission(String code, EdsUser user) {
        if (ApplicationContextProvider.applicationContext != null) {
            RolePermissionServiceLocal rolePermissionService = (RolePermissionServiceLocal) ApplicationContextProvider.applicationContext.getBean("rolePermissionService");
            return rolePermissionService.hasReportingPermission(code, user);
        }
        return false;
    }

    public static boolean hasPermission(String code, EdsUser user) {
        if (ApplicationContextProvider.applicationContext != null) {
            RolePermissionServiceLocal rolePermissionService = (RolePermissionServiceLocal) ApplicationContextProvider.applicationContext.getBean("rolePermissionService");
            return rolePermissionService.hasPermission(code, user);
        }
        return false;
    }

    public static HashSet<String> getMainMenuPermissions() {

        if (ApplicationContextProvider.applicationContext != null) {
            RolePermissionService rps = StaticContextAccessor.getBean(RolePermissionService.class);
            return rps.getMainMenuPermissions();
        }

        return new HashSet<>();
    }

    public static String refactorStr(String value) {
        return (value != null && !"".equals(value)) ? value : "";

    }

    public static String refactorNA(String value) {
        return (value != null && !"".equals(value)) ? value : "N/A";
    }

    public static String refactorPhone(String value) {
        if (value != null && !"".equals(value)) {
            String[] phoneNumbers = value.split("\\|");
            StringBuilder number = new StringBuilder("+");
            for (String phoneNumber : phoneNumbers) {
                number.append(phoneNumber);
            }
            if (phoneNumbers.length == 0) {
                return "N/A";
            } else {
                return number.toString();
            }

        } else {
            return "N/A";
        }
    }

    public static String normalizeFileName(String fileName) {
        if (fileName != null) {
            return fileName
                    .replace(" ", "_")
                    .replace(",", "")
                    .replace("/", "")
                    .replace("\"", "")
                    .replace(":", "")
                    .replaceAll("[|]", "")
                    .replaceAll("[.]", "")
                    .replaceAll("[?]", "")
                    .replaceAll("[*]", "");
        }
        return null;
    }

    public static String normalizeFileNameT(String fileName) {
        if (fileName != null) {
            return fileName
                    .replace(" ", "_")
                    .replace(",", "_")
                    .replace("/", "")
                    .replace("/", "")
                    .replace("\"", "")
                    .replace(":", "")
                    .replaceAll("[|]", "")
                    .replaceAll("[?]", "")
                    .replaceAll("[*]", "");
        }
        return null;
    }

    public static ArrayList removeDuplicates(ArrayList list) {
        HashSet set = new HashSet(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static void kpiLog(Logger log, KpiLog kpiLog, String message) {
        if (!Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_logaudit_enabled"))) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (!kpiLog.getFromMobile()) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                if (request != null && StringUtils.isEmpty(kpiLog.getUserAgent())) {
                    kpiLog.setUserAgent(request.getHeader("user-agent"));
                }
                if (request != null && StringUtils.isEmpty(kpiLog.getIp())) {
                    kpiLog.setIp(ServerUtils.obtainClientIP(request));
                }
                if (request != null) {
                    kpiLog.setModuleName(getCookieVal(SECTION_HTML, request.getCookies()));
                }
                if (request != null) {
                    kpiLog.setSessionID(SecurityContext.getInstance().getSessionId());
                }
            } catch (IllegalStateException e) {
//                System.out.println(e.getMessage());
            }
        }
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        if (user != null) {
            kpiLog.setUserId(user.getObjectID());
            kpiLog.setUsername(user.getUserName());
            kpiLog.setCompanyName(EdsContextParams.getCompanyName());
        } else {
            kpiLog.setCompanyName(EdsContextParams.getCompanyName());
        }

        if (StringUtils.isNotBlank(ServerSecurityContext.getInstance().getCompanyId())) {
            kpiLog.setCompanyId(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        }

        kpiLog.setCompanyType(ServerSecurityContext.getInstance().getDatabase());
        String host = EdsContextParams.getHostname();
        if (!StringUtils.isEmpty(host)) {
            putMDC("host", host);
        }
        if (SecurityContext.getCompanyID() != null) {
            putMDC("company_id", SecurityContext.getCompanyID());
        }
        if (!StringUtils.isEmpty(kpiLog.getCompanyName())) {
            putMDC("company_name", kpiLog.getCompanyName());
        }
        if (!StringUtils.isEmpty(kpiLog.getCompanyType())) {
            putMDC("company_type", kpiLog.getCompanyType());
        }
        if (!StringUtils.isEmpty(kpiLog.getModuleName())) {
            putMDC("modulename", kpiLog.getModuleName());
        }
        if (kpiLog.getUserId() != null) {
            putMDC("user_id", kpiLog.getUserId());
        }
        if (!StringUtils.isEmpty(kpiLog.getUsername())) {
            putMDC("username", kpiLog.getUsername());
        }
        if (!StringUtils.isEmpty(kpiLog.getUserRole())) {
            putMDC("user_role", kpiLog.getUserRole());
        }
        if (!StringUtils.isEmpty(kpiLog.getEntityType())) {
            putMDC("entity_type", kpiLog.getEntityType());
        }
        if (kpiLog.getEntityId() != null) {
            putMDC("entity_id", kpiLog.getEntityId());
        }
        if (!StringUtils.isEmpty(kpiLog.getEntityName())) {
            putMDC("entity_name", kpiLog.getEntityName());
        }
        if (kpiLog.getActionType() != null) {
            putMDC("action_type", kpiLog.getActionType());
        }
        if (!StringUtils.isEmpty(kpiLog.getCountryName())) {
            putMDC("country_name", kpiLog.getCountryName());
        }
        if (!StringUtils.isEmpty(kpiLog.getIp())) {
            putMDC("ip", kpiLog.getIp());
        }
        if (!StringUtils.isEmpty(kpiLog.getUserAgent())) {
            putMDC("user_agent", kpiLog.getUserAgent());
        }
        if (!StringUtils.isEmpty(kpiLog.getAppVersion())) {
            putMDC("app_version", kpiLog.getAppVersion());
        }
        if (!StringUtils.isEmpty(kpiLog.getOsVersion())) {
            putMDC("os_version", kpiLog.getOsVersion());
        }
        if (!StringUtils.isEmpty(kpiLog.getDeviceName())) {
            putMDC("device_name", kpiLog.getDeviceName());
        }
        if (!StringUtils.isEmpty(kpiLog.getSessionID())) {
            putMDC("sessionid", kpiLog.getSessionID());
        }

        log.error(message);

        ThreadContext.clearAll();
        long logTime = System.currentTimeMillis() - currentTimeMillis;
        if (logTime > 500) {
            System.out.println("LogTime: ---" + (System.currentTimeMillis() - currentTimeMillis) + " ms ---");
        }
    }

    private static void putMDC(String key, Object value) {
        if (value != null) {
            String val = value.toString();
            val = val.replace("'", "''");
            ThreadContext.put(key, val);
        } else {
            ThreadContext.put(key, "");
        }
    }


    public static String groupTheREs(String subject, int... res) {
        if (subject == null || "".equals(subject)) {
            return "";
        }
        String simpleRe = "\\sRe: |Re: ";
        String countedRe = "\\sRe\\[\\d+]: |Re\\[\\d+]: ";
        int reCount = 0;
        if (res != null && res.length > 0) {
            reCount = res[0];
        }
        int counter = 0;
        do {
            if (subject.matches(simpleRe + ".*")) {
                subject = subject.replaceFirst(simpleRe, "");
                reCount++;
            } else if (subject.matches(countedRe + ".*")) {
                Integer count = null;
                if (subject.contains("Re[")) {
                    try {
                        count = Integer.parseInt((subject.substring(subject.indexOf("Re[") + 3, subject.indexOf("]", subject.indexOf("Re[")))).trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (count != null && !"".equals(count)) {
                        reCount += count;
                    } else {
                        reCount++;
                    }
                    subject = subject.replaceFirst(countedRe, "");
                }
            }
            if (counter++ > 50) {
                break;
            }
        } while (subject.matches(simpleRe + ".*") || subject.matches(countedRe + ".*"));
        if (reCount == 0) {
            return subject;
        }
        subject = reCount == 1 ? "Re: " + subject : "Re[" + reCount + "]: " + subject;
        return subject;
    }

    public static String getInputStreamAsString(InputStream is, String charSet) {
        if (is == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        try {
            if (charSet != null) {
                charSet = charSet.replace("\"", "");
                IOUtils.copy(is, writer, charSet.toUpperCase().contains("UTF-8") ? "UTF-8" : charSet.toUpperCase());
            } else {
                IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            try {
                IOUtils.copy(is, writer, StandardCharsets.UTF_8);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static DecimalFormat getDecimalFormat(Integer scale) {
        if (scale == null) {
            scale = 2;
        }
        if (scale == 0) {
            return new DecimalFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < scale; i++) {
                s = s.concat("0");
            }
            return new DecimalFormat(",##0" + s);
        }
    }

    public static String extractCharsetFromContentType(String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("charset=")) {
            return contentType.substring(contentType.indexOf("charset=") + 8);
        }
        return null;
    }

    public static int getDayCount(Date startDate, Date endDate) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(startDate);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(endDate);
        cal1.set(Calendar.HOUR, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return (360 * (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) + 30 * (cal2.get(Calendar.MONTH) -
                cal1.get(Calendar.MONTH)) + cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH));
    }

    public static int getDayCountInCalendar(Date startDate, Date endDate) {
        if (startDate != null && endDate != null && startDate.before(endDate)) {
            return getDaysCount(startDate, endDate, 1);
        }
        return 0;
    }

    private static Integer getDaysCount(Date startDate, Date endDate, Integer dayCount) {
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        endDate.setHours(0);
        endDate.setMinutes(0);
        endDate.setSeconds(0);
        if (startDate.equals(endDate)) {
            return dayCount;
        } else {
            dayCount++;
            startDate.setDate(startDate.getDate() + 1);
            return getDaysCount(startDate, endDate, dayCount);
        }
    }

    public static boolean isCrm() {
        if (RequestContextHolder.getRequestAttributes() != null && RequestContextHolder.currentRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return request != null && request.getQueryString() != null && request.getQueryString().contains("Crm.html");
        }
        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * This method validates the captcha entered by user from Recaptcha web service
     *
     * @param request
     * @param response
     * @return whether the captcha valid or not
     */

    public static boolean validateCaptcha(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {

        if (!EdsContextParams.isCaptchaEnabled(request.getServerName())) {
            return true;
        }
        String remoteAddr = ServerUtils.obtainClientIP(request);

        if (request.getParameter("g-recaptcha-response") != null && !"".equals(request.getParameter("g-recaptcha-response"))) {
            String resp = "true";
            try {
                URL url = new URL("https://www.google.com/recaptcha/api/siteverify");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                // Send post request
                String postParams = "secret=" + EdsContextParams.getHostSetting(request.getServerName()).getRecaptchaPrivateKey() + "&response=" + request.getParameter("g-recaptcha-response") + "&remoteip=" + remoteAddr;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + postParams);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                StringBuilder responseBuffer = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }

                in.close();

                resp = responseBuffer.toString();
                System.out.println("RESPONSE RECAPTCHA = " + resp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resp.contains("\"success\": true");
        }
        String capchallenge = request.getParameter("recaptcha_challenge_field");
        String capresponse = request.getParameter("recaptcha_response_field");
        if (capchallenge == null || capresponse == null || "".equals(capchallenge)) {
            return false;
        }

        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

        reCaptcha.setPrivateKey(EdsContextParams.getHostSetting(request.getServerName()).getRecaptchaPrivateKey());
        ReCaptchaResponse reCaptchaResponse;
        try {
            reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, capchallenge, capresponse);

        } catch (Exception ex) {
            return true;
        }

        return reCaptchaResponse.isValid();
    }

    private static final SimpleDateFormat fpDateParseFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");

    public static Date parseFilterParameterDate(String dateAsString) {
        try {
            return StringUtils.isNotBlank(dateAsString) ? fpDateParseFormat.parse(dateAsString) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatFilterDate(Date date) {
        return date != null ? fpDateParseFormat.format(date) : "";
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static WfmResourceBundleMessageSource referenceLocalizer;

    public static WfmResourceBundleMessageSource initReferenceLocalizer() {
        if (referenceLocalizer == null) {
            referenceLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
        }
        return referenceLocalizer;
    }

    public static String parseURLResponse(URL url) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String res = null;
        while ((inputLine = in.readLine()) != null) {
            res = inputLine;
//            System.out.println(inputLine);
        }
        in.close();
        return res;

    }

    public static String getCookie(HttpServletRequest request, String key) {
        if (request.getCookies() != null) {
            for (int i = 0; i < request.getCookies().length; i++) {
                if (request.getCookies()[i].getName().equals(key)) {
                    return request.getCookies()[i].getValue();
                }
            }
        }
        return null;
    }

    public static String getWebURL(UserCompanyDTO companyDTO) {
        StringBuilder url = new StringBuilder("/auth/authentication?");

        url.append(C_ID).append("=").append(EncryptionHelper.encryptURL(companyDTO.getCompanyID().toString()));
        url.append("&").append(U_ID).append("=").append(EncryptionHelper.encryptURL(companyDTO.getUserID().toString()));
        url.append("&").append(S_ID).append("=").append(EncryptionHelper.encryptURL(companyDTO.getServiceID()));
//        url.append("&").append(D_ID).append("=").append(companyDTO.getClusterDbName());
        return url.toString();
    }

    public static boolean isResourceFile(String url) {
        return url.toLowerCase().endsWith(".ico")
                || url.endsWith(".gif")
                || url.endsWith(".doc")
                || url.endsWith(".csv")
                || url.endsWith(".xlsx")
                || url.endsWith(".xls")
                || url.endsWith(".jpg")
                || url.endsWith(".pdf")
                || url.endsWith(".png")
                || url.endsWith(".swf")
                || url.endsWith(".css")
                || url.endsWith(".woff")
                || url.endsWith(".svg")
                || url.endsWith(".ttf")
                || url.endsWith(".eot")
                || url.endsWith(".js")
                || url.endsWith(".txt")
                || url.endsWith(".html")
                || url.startsWith("/rpc")
                || url.startsWith("//rpc")
                || url.startsWith("/ckeditor")
                || url.startsWith("/wfpLogin")
                || url.endsWith("/documents")
                || url.endsWith("/servlet.gupld")
                || url.contains("wfpRegistration")
                || url.contains("/wfp/tempalates")
                || url.contains("/common/paywithelavonsc")
                || url.contains("customentityentry")
                || url.toLowerCase().contains("/printpage")
                || url.contains("/wfpaccount")
                || url.contains("/action/createwfpattachmentshandler")
                || url.contains("wfplogin")
                || url.contains("wfpforgotpassword")
                || url.contains("sitemap")
                || url.contains("urlcounter")
                || url.contains("analizeeventlog")
                || url.contains("wfpcaptcha");
    }

    public static String getString(HashMap<String, Object> map, String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        return (String) map.get(key);
    }

    public static int getDayOfWeek(Date tempDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static Long dateToLong(Date date) {
        if (date == null) {
            return 0L;
        } else {
            return date.getTime();
        }
    }

    public static Integer timeToMinutes(String time) {
        if (time == null) {
            return 0;
        }

        try {
            String[] chunks = time.split(":");
            return (Integer.valueOf(chunks[0]) * 60) + Integer.valueOf(chunks[1]);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    public static ArrayList<Date> convertToDates(List<Calendar> calendarList) {
        ArrayList<Date> dates = new ArrayList<>();
        if (calendarList != null && calendarList.size() > 0) {
            for (Calendar calendar : calendarList) {
                dates.add(calendar.getTime());
            }
        }
        return dates;
    }

    public static String formatFromTo(Date from, Date to, boolean isAllDay, EdsUser user) {
        try {
            String fromDate = isAllDay ? shortDateFormat(from, user) : longDateFormat(from, user);
            String toDate = isAllDay ? shortDateFormat(to, user) : longDateFormat(to, user);
            if (DateUtils.isSameDay(from, to)) {
                if (isAllDay) {
                    toDate = "";
                } else {
                    EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
                    String longFormat = "yyyy-MM-dd HH:mm:ss";
                    if (companySettings != null) {
                        longFormat = companySettings.getLongDateFormat();

                    }
                    String format = longFormat.contains("[") ? "[HH:mm]" : "HH:mm";
                    SimpleDateFormat timeFormat = new SimpleDateFormat(format);
                    toDate = timeFormat.format(to);
                }
            }
            return toDate != null && toDate.trim().length() > 0 ? (fromDate + " - " + toDate) : fromDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Return the given size in a humanly readable form, using SI units to denote
     * size information, e.g. 1 KB = 1000 B (bytes).
     *
     * @param size in bytes
     * @return the size in human readable string
     */
    public static String getSizeAsString(Long size) {
        if (size == null || size == 0) {
            return "0 B";
        }
        if (size < 1024) {
            return size + " B";
        }
        if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        }
        if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    private static String getSize(Long size, Double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat format = new DecimalFormat("######.#");
        return format.format(res);
    }

    public static Date getDayStartTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDayEndTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * @param input
     * @param result
     * @return
     */
    public static ArrayList<CompanyCustomFieldItem> mergeCustomFields(ArrayList<CompanyCustomFieldItem> input, ArrayList<CompanyCustomFieldItem> result) {
        for (CompanyCustomFieldItem inputcf : input) {
            for (CompanyCustomFieldItem resultcf : result) {
                if (inputcf.getAliasName().equals(resultcf.getAliasName()) && inputcf.getUiType().equals(resultcf.getUiType())) {
                    if (UI_TYPE_DATEPICKER.equals(inputcf.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(inputcf.getUiType())) {
                        resultcf.setFieldDateNonConvertedValue(inputcf.getFieldDateNonConvertedValue());
//                        resultcf.setFieldDateValue(inputcf.getFieldDateValue());
                    } else if (DATA_TYPE_NUMBER.equals(inputcf.getDataType())) {
                        resultcf.setFieldStringValue(inputcf.getFieldStringValue() != null ? inputcf.getFieldStringValue() : "0.0");
                    } else if (UI_TYPE_LOOKUP.equals(inputcf.getUiType())) {
                        if (resultcf.getLookUpTypeEnum().equals(inputcf.getLookUpTypeEnum())) {
                            resultcf.setFieldStringValue(inputcf.getFieldStringValue());
                            resultcf.setSelectedId(inputcf.getSelectedId());
                            resultcf.setItem(inputcf.getItem());
                        }
                    } else {
                        resultcf.setFieldStringValue(inputcf.getFieldStringValue());
                        resultcf.setSelectedId(inputcf.getSelectedId());
                        resultcf.setItem(inputcf.getItem());
                    }
                }
            }
        }
        return result;
    }

    /**
     * Receives String as parameter and return
     *
     * @param label
     * @return
     */
    public static String getStringAsAttribute(String label) {
        if (label != null) {
            return "${" + label + "}";
        }
        return null;
    }

    public static int getSystemCalculationScale() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        return financialSettings != null && financialSettings.getAccountingCalculationScale() != null ? financialSettings.getAccountingCalculationScale() : EdsFinancialSettings.SYSTEM_CALCULATION_SCALE;
    }

    public static int getSystemPriceScale() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        return financialSettings != null && financialSettings.getProductPriceScale() != null ? financialSettings.getProductPriceScale() : EdsFinancialSettings.SYSTEM_CALCULATION_SCALE;
    }

    public static Calendar getDateAsCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date != null ? date : new Date());
        return cal;
    }

    public static EdsProjectBudgetItem getEdsProjectBudgetItem(EdsProjectBudget projectBudget, EdsProjectBudgetItem budgetItem, BigDecimal amount, String type) {
        if (budgetItem == null) {
            budgetItem = new EdsProjectBudgetItem();
            budgetItem.setProjectBudget(projectBudget);
            budgetItem.setAmount(amount);
            budgetItem.setTotal(Boolean.FALSE);
            budgetItem.setType(type);
        }
        return budgetItem;
    }

    public static int getMonthDaysCountInYear(int month, int year) {
        if (month < 0 || month > 12) {
            return 0;
        }
        boolean kabisa = year > 100 && year % 100 == 0 ? year % 400 == 0 : year % 4 == 0;
        return switch (month) {
            case 0 -> 31;
            case 1 -> kabisa ? 29 : 28;
            case 2 -> 31;
            case 3 -> 30;
            case 4 -> 31;
            case 5 -> 30;
            case 6 -> 31;
            case 7 -> 31;
            case 8 -> 30;
            case 9 -> 31;
            case 10 -> 30;
            case 11 -> 31;
            default -> 30;
        };
    }

    public static Calendar getDateOfGiveYear(Date date, Integer year) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }

    public static List<String> getMonthYearList(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(startDate);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        int startYear = calendar.get(Calendar.YEAR);

        calendar.setTime(endDate);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endYear = calendar.get(Calendar.YEAR);

        List<String> list = new ArrayList<>();
        int diff = 12 * (endYear - startYear) + (endMonth - startMonth);
        if (diff == 0) {
            diff = 1;
        }
        for (int i = 0; i < diff; i++) {
            list.add(startMonth + "/" + startYear);

            startMonth++;
            if (startMonth == 13) {
                startMonth = 1;
                startYear++;
            }
        }
        return list;
    }

    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isUKCompany(EdsCompany company) {
        if (company == null || company.getCountryZone() == null || company.getCountryZone().getCountry() == null) {
            return false;
        }
        return "GB".equals(company.getCountryZone().getCountry().getCode());
    }

    public static boolean isUAECompany(EdsCompany company) {
        if (company == null || company.getCountryZone() == null || company.getCountryZone().getCountry() == null) {
            return false;
        }
        return Constants.AE.equals(company.getCountryZone().getCountry().getCode());
    }

    public static boolean isKSACompany(EdsCompany company) {
        if (company == null || company.getCountryZone() == null || company.getCountryZone().getCountry() == null) {
            return false;
        }
        return Constants.SA.equals(company.getCountryZone().getCountry().getCode());
    }

    public static boolean isArabicCompany(EdsCompany company) {
        if (company == null || company.getCountryZone() == null || company.getCountryZone().getCountry() == null) {
            return false;
        }
        return GCC_COUNTRIES.contains(company.getCountryZone().getCountry().getCode());
    }

    /**
     * Get calculation scale from financial settings.
     *
     * @return calculationScale
     */
    public static Integer getCalculationScale() {
        String key = ServerSecurityContext.getInstance().getCompanyId() + "_" + CacheConstants.CALCULATION_SCALE;
//        Map<String, Integer> map = ApplicationCache.getInstance().getMap(key);
        Integer calculationScale = null;
        /*if (map != null && map.get(key) != null) {
            calculationScale = map.get(key);
        }*/
        if (RedisClient.getKey(key) != null) {
            calculationScale = RedisClient.getKey(key, Integer.TYPE);
        }
        if (calculationScale != null) {
            return calculationScale;
        }
        if (ApplicationContextProvider.applicationContext != null) {
            FinancialSettingsManager financialSettingsManager = StaticContextAccessor.getBean(FinancialSettingsManager.class);
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null && financialSettings.getCalculationScale() != null) {
                calculationScale = financialSettings.getCalculationScale();
            }
        }

        return calculationScale != null ? calculationScale : 2;
    }

    public static boolean isSuperUser() {
        if (ServerSecurityContext.getInstance().getSessionId() == null) {
            return false;
        }
        if (ServerSecurityContext.getInstance().isSuperUser() != null) {
            return ServerSecurityContext.getInstance().isSuperUser();
        }
        if (ApplicationContextProvider.applicationContext != null) {
            UserSessionManager userSessionManager = StaticContextAccessor.getBean(UserSessionManager.class);
            EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
            if (userSession != null) {
                return userSession.isSuperUser();
            }
        }
        return false;
    }

    public static boolean hasNonSuperUserSession() {
        if (ApplicationContextProvider.applicationContext != null) {
            UserSessionManager userSessionManager = StaticContextAccessor.getBean(UserSessionManager.class);
            return userSessionManager.hasNonSuperUserSession(ServerSecurityContext.getInstance().getUserId());
        }
        return false;
    }

    public static int[] extractArrayFromHolidayIndicator(HolidayIndicator[] his) {
        int[] result = new int[his.length];
        for (int i = 0; i < his.length; i++) {
            result[i] = his[i].getIndicator();
        }
        return result;
    }

    public static String getCompanyCurrencyName() {
        String name = "USD";
        FinancialSettingsManager financialSettingsManager = StaticContextAccessor.getBean(FinancialSettingsManager.class);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            name = currency.getName();
        }
        return name;
    }

    public static String getFolderRelationType(int folderType) {
        return switch (folderType) {
            case F_SALE_INV -> RelationItem.TYPE_SALEINVOICE;
            case F_SALE_QUOTE -> RelationItem.TYPE_SALEQUOTE;
            case F_PUR_ORDER -> RelationItem.TYPE_PURCHASE_ORDER;
            case F_PUR_INV -> RelationItem.TYPE_PURCHASE_INVOICE;
            case F_MANUAL_TRANSACTION -> RelationItem.TYPE_MANUAL_JOURNAL;
            case F_EXP_DOC -> RelationItem.TYPE_EXPENSE_CLAIM;
            case F_BANK_TRANSFER -> RelationItem.TYPE_BANK_TRANSFER;
            case F_BATCH_PAYMENT -> RelationItem.TYPE_BATCH_PAYMENT;
            case F_PREPAYMENT -> RelationItem.TYPE_PRE_PAYMENT;
            case F_RFQ_1 -> RelationItem.TYPE_REQUEST_FOR_QUOTE;
            case F_RFP -> RelationItem.REQUEST_FOR_PURCHASE;
            default -> null;
        };
    }

    /**
     * Encrypt
     *
     * @param msg
     * @return enc
     */

    public static String encrypt(String msg) {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(GWT_DES_KEY);
        String enc = "";
        try {
            enc = cipher.encrypt(String.valueOf(msg));
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e1) {
            e1.printStackTrace();
        }

        return enc;
    }

    /**
     * Decrypt
     *
     * @param encryptedPassword
     * @return password
     */
    public static String decrypt(String encryptedPassword) {
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(Constants.GWT_DES_KEY);
        String password = "";
        try {
            password = cipher.decrypt(encryptedPassword);
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e) {
            log.error("Password decryption error: ", e);
        }

        return password;
    }

    public static String getReminderTimeAsString(Integer minutes) {
        if (minutes == null) {
            return null;
        }
        if (minutes < 60) {
            return minutes + " minutes";
        }
        return switch (minutes) {
            case 60 -> "1 hour";
            case 60 * 2 -> "2 hours";
            case 60 * 3 -> "3 hours";
            case 60 * 12 -> "12 hours";
            case 60 * 24 -> "1 day";
            case 60 * 24 * 2 -> "2 days";
            case 60 * 24 * 7 -> "1 week";
            case 60 * 24 * 15 -> "15 days";
            case 60 * 24 * 30 -> "1 month";
            case 60 * 24 * 45 -> "45 days";
            case 60 * 24 * 60 -> "2 months";
            default -> null;
        };
    }

    public static String getLeaveDayFormat(Double[] dd, int i) {
        if (dd == null) {
            return "";
        }
        //dd[0] - hour
        //dd[1] - day
        //dd[2] - paid
        //dd[3] - nonPaid

        Double day = dd[i];
        if (day != null && day != 0d) {
            /*if (day < 1) {
                Double hour = dd[0];
                String hourStr = dd[0] > 1 ? " Hours" : " Hour";
                if (hour < 1) {
                    hour = hour * 60;
                    hourStr = hour > 1 ? " Minutes" : " Minute";
                }
                return ((hour % 1) == 0 ? df2.format(hour) : df.format(hour)) + hourStr;
            } else {*/
//            String dayStr = day > 1 ? " Days" : " Day";
            return ((day % 1) == 0 ? df2.format(day) : df.format(day))/* + dayStr*/;
//            }
        }
        return "";
    }

    public static String getProductTypeName(Integer productType) {
        String typeName = "";
        if (productType != null) {
            if (productType.equals(AccountingConstants.INVENTORY_ITEM)) {
                typeName = AccountingConstants.INVENTORY_ITEM_STR;
            } else if (productType.equals(AccountingConstants.NON_INVENTORY_ITEM)) {
                typeName = AccountingConstants.NON_INVENTORY_ITEM_STR;
            } else if (productType.equals(AccountingConstants.ASSEMBLY_ITEM)) {
                typeName = AccountingConstants.ASSEMBLY_ITEM_STR;
            } else if (productType.equals(AccountingConstants.OTHER_CHARGE)) {
                typeName = AccountingConstants.OTHER_CHARGE_STR;
            } else if (productType.equals(AccountingConstants.SERVICE)) {
                typeName = AccountingConstants.SERVICE_STR;
            } else if (productType.equals(AccountingConstants.PRODUCT_KIT)) {
                typeName = AccountingConstants.PRODUCT_KIT_STR;
            } else if (productType.equals(AccountingConstants.RENTAL_ITEM)) {
                typeName = AccountingConstants.RENTAL_ITEM_STR;
            }
        }

        return typeName;
    }

    public static String convertToUzbDateFormat(String dateForm) {
        dateForm = dateForm.replace(",", " ,");
        String[] valueHere = dateForm.split(" ");
        Map<String, String> mapKaroche = new HashMap<>();
        mapKaroche.put("Jan", "Yan");
        mapKaroche.put("Feb", "Fev");
        mapKaroche.put("Mar", "Mar");
        mapKaroche.put("Apr", "Apr");
        mapKaroche.put("May", "May");
        mapKaroche.put("Jun", "Iyun");
        mapKaroche.put("Jul", "Iyul");
        mapKaroche.put("Aug", "Avg");
        mapKaroche.put("Sep", "Sen");
        mapKaroche.put("Oct", "Okt");
        mapKaroche.put("Nov", "Noya");
        mapKaroche.put("Dec", "Dek");
        mapKaroche.put("January", "Yanvar");
        mapKaroche.put("February", "Fevral");
        mapKaroche.put("March", "Mart");
        mapKaroche.put("April", "Aprel");
        mapKaroche.put("June", "Iyun");
        mapKaroche.put("July", "Iyul");
        mapKaroche.put("August", "Avgust");
        mapKaroche.put("September", "Sentyabr");
        mapKaroche.put("October", "Oktyabr");
        mapKaroche.put("November", "Noyabr");
        mapKaroche.put("December", "Dekabr");
        mapKaroche.put("Mon", "Dush");
        mapKaroche.put("Tue", "Sesh");
        mapKaroche.put("Wed", "Chor");
        mapKaroche.put("Thu", "Pay");
        mapKaroche.put("Fri", "Juma");
        mapKaroche.put("Sat", "Shan");
        mapKaroche.put("Sun", "Yak");
        int counter = 0;
        StringBuilder date = new StringBuilder();
        while (counter < valueHere.length) {
            if (mapKaroche.containsKey(valueHere[counter])) {
                date.append(" ").append(mapKaroche.get(valueHere[counter]));
            } else {
                date.append(" ").append(valueHere[counter]);
            }
            counter++;
        }
        date = new StringBuilder(date.toString().replace(" ,", ","));
        return date.toString();
    }

    public static String convertToRuDateFormat(String dateForm) {
        dateForm = dateForm.replace(",", " ,");
        String[] valueHere = dateForm.split(" ");
        Map<String, String> map = new HashMap<>();
        map.put("Jan", "Янв");
        map.put("Feb", "Фев");
        map.put("Mar", "Мар");
        map.put("Apr", "Апр");
        map.put("May", "Май");
        map.put("Jun", "Июн");
        map.put("Jul", "Июл");
        map.put("Aug", "Авг");
        map.put("Sep", "Сен");
        map.put("Oct", "Окт");
        map.put("Nov", "Ноя");
        map.put("Dec", "Дек");
        map.put("January", "Январь");
        map.put("February", "Февраль");
        map.put("March", "Март");
        map.put("April", "Апрель");
        map.put("June", "Июнь");
        map.put("July", "Июль");
        map.put("August", "Август");
        map.put("September", "Сентябрь");
        map.put("October", "Октябрь");
        map.put("November", "Ноябрь");
        map.put("December", "Декабрь");
        map.put("Mon", "Пн");
        map.put("Tue", "Вт");
        map.put("Wed", "Ср");
        map.put("Thu", "Чт");
        map.put("Fri", "Птн");
        map.put("Sat", "Сбт");
        map.put("Sun", "Вск");
        int counter = 0;
        StringBuilder date = new StringBuilder();
        while (counter < valueHere.length) {
            if (map.containsKey(valueHere[counter])) {
                date.append(" ").append(map.get(valueHere[counter]));
            } else {
                date.append(" ").append(valueHere[counter]);
            }
            counter++;
        }
        date = new StringBuilder(date.toString().replace(" ,", ","));
        return date.toString();
    }

    public static String convertDateFormatFromEngToUzb(String dateFormat) {
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            return convertToUzbDateFormat(dateFormat);
        }
        return dateFormat;
    }


    public static String convertMonthToInterfaceLanguage(String month) {
        String language = getUserLocale().getLanguage();
        if (month == null)
            return null;
        return switch (month) {
            case "january" -> language.equals("uz") ? "Yanvar" : language.equals("ru") ? "Январь" : language.equals("ar") ? "يناير" : "January";
            case "february" -> language.equals("uz") ? "Fevral" : language.equals("ru") ? "Февраль" : language.equals("ar") ? "فبراير" : "February";
            case "march" -> language.equals("uz") ? "Mart" : language.equals("ru") ? "Март" : language.equals("ar") ? "مارس" : "March";
            case "april" -> language.equals("uz") ? "Aprel" : language.equals("ru") ? "Апрель" : language.equals("ar") ? "إبريل" : "April";
            case "may" -> language.equals("uz") ? "May" : language.equals("ru") ? "Май" : language.equals("ar") ? "مايو" : "May";
            case "june" -> language.equals("uz") ? "Iyun" : language.equals("ru") ? "Июнь" : language.equals("ar") ? "يونيو" : "June";
            case "july" -> language.equals("uz") ? "Iyul" : language.equals("ru") ? "Июль" : language.equals("ar") ? "يوليو" : "July";
            case "august" -> language.equals("uz") ? "Avgust" : language.equals("ru") ? "Август" : language.equals("ar") ? "أغسطس" : "August";
            case "september" -> language.equals("uz") ? "Sentyabr" : language.equals("ru") ? "Сентябрь" : language.equals("ar") ? "سبتمبر" : "September";
            case "october" -> language.equals("uz") ? "Oktyabr" : language.equals("ru") ? "Октябрь" : language.equals("ar") ? "أكتوبر" : "October";
            case "november" -> language.equals("uz") ? "Noyabr" : language.equals("ru") ? "Ноябрь" : language.equals("ar") ? "نوفمبر" : "November";
            case "december" -> language.equals("uz") ? "Dekabr" : language.equals("ru") ? "Декабрь" : language.equals("ar") ? "ديسمبر" : "December";
            default -> null;
        };
    }


    public static String getMonthFromCompatibleIndex(Integer monthId) {
        return switch (monthId) {
            case 0 -> "january";
            case 1 -> "february";
            case 2 -> "march";
            case 3 -> "april";
            case 4 -> "may";
            case 5 -> "june";
            case 6 -> "july";
            case 7 -> "august";
            case 8 -> "september";
            case 9 -> "october";
            case 10 -> "november";
            case 11 -> "december";
            default -> String.valueOf(monthId);
        };
    }

    public static List<Integer> convertToIds(List<SelectItem> selectItems) {
        List<Integer> ids = new ArrayList<>();
        for (SelectItem selectItem : selectItems) {
            ids.add(selectItem.getId());
        }
        return ids;
    }

    public static String getAmountInWords(BigDecimal amount) {
        if (amount == null)
            return "";
        String amountInWords = getNumberToWordConverter().convert(amount);
        return !isNullOrEmpty(amountInWords) ? WordUtils.capitalizeFully(amountInWords) : "";
    }

    private static NumberToWord getNumberToWordConverter() {
        NumberToWord numberToWordConverter;
        switch (ServerUtils.getUserLocale().getLanguage()) {
            case "en":
                numberToWordConverter = new NumberToWord_en();
                break;
            case "uz":
                numberToWordConverter = new NumberToWord_uz();
                break;
            case "ar":
                numberToWordConverter = new NumberToWord_ar();
                break;
            default:
                numberToWordConverter = new NumberToWord_ru();
                break;
        }

        return numberToWordConverter;
    }


    public static NameOrder getNameFormat() {
        String key = ServerSecurityContext.getInstance().getCompanyId();
        NameOrder val = RedisClient.getKey(key, NameOrder.class);
        if (val == null) {
            NameOrder format = fetchNameFormatFromDatabase();
            RedisClient.setKey(key, format, NameOrder.class, 18000); // 5 hours
            val = format;
        }
        return val;
    }

    private static NameOrder fetchNameFormatFromDatabase() {
        CompanySystemSettingsManager settings = StaticContextAccessor.getBean(CompanySystemSettingsManager.class);
        String code = settings.getNameFormat();
        return NameOrder.fromCode(code);
    }

    public static void invalidateNameFormatCache() {
        String key = ServerSecurityContext.getInstance().getCompanyId();
        NameOrder format = fetchNameFormatFromDatabase();
        RedisClient.setKey(key, format.getCode(), 18000);
    }

    public static Map<String, String> extractParamsFromUrl(String url) {
        Map<String, String> paramMap = new HashMap<>();
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query != null && !query.isEmpty()) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=", 2); // 2 qismga bo‘linadi (agar "=" bo‘lmasa xatolik bo‘lmaydi)
                    String key = keyValue[0];
                    String value = keyValue.length > 1 ? keyValue[1] : "";
                    paramMap.put(key, value);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error while parsing URL: {}", url, e);
        }
        return paramMap;
    }
}
