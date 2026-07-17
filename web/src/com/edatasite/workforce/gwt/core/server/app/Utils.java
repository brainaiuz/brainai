package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static String getName(EdsObject o) {
        return o == null ? "N/A" : o.getName();
    }

    public static Date getDate(Date date) {
        return date == null ? null : new Date(date.getTime());
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

    public static String invertColor(String hexTripletColor) {
        String color = hexTripletColor;
        Integer _colur = Integer.parseInt(color, 16);          // convert to integer
        _colur = 0xFFFFFF ^ _colur;             // invert three bytes
        color = Integer.toHexString(_colur);          // convert to hex
        return color;
    }

    public static String formatDate(Date date, EdsCompany company) {
        String lang = ServerUtils.getUserLocale().getLanguage();
        Format formatter;
        if (company.getCompanySettings() != null) {
            formatter = new SimpleDateFormat(company.getCompanySettings().getLongDateFormat(), new Locale(lang));
        } else {
            formatter = new SimpleDateFormat("dd-MMM-yyyy", new Locale(lang));
        }
        return formatter.format(date);
    }

    public static String formatDouble(double doubleValue) {

        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        return decimalFormat.format(doubleValue);
    }

    public static String formatDecimal(BigDecimal decimalValue) {

        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        return decimalFormat.format(decimalValue.doubleValue());
    }

    public static String formatExtendedDecimal(BigDecimal decimalValue) {
        DecimalFormat decimalFormat = new DecimalFormat(",##0.0000");
        return decimalFormat.format(decimalValue.doubleValue());
    }

    public static Integer parseMinutes(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (minutes == null || minutes.equals("")) {

            return 0;
        }
        String[] parts = new String[2];
        int h = 0;
        int m = 0;
        int qw = 0;
        char[] splitters = new char[]{':', '.', ',', ' '};

        char splitter = 0;
        for (char splitter1 : splitters) {
            if (minutes.indexOf(splitter1) != -1) {
                splitter = splitter1;
                qw++;
                if (qw == 2) {
                    h = 0;
                    return 0;
                }
            }
        }
        try {
            if (splitter != 0) {
                minutes = minutes.replace(splitter, ':');
                splitter = ':';
                int k = 0;
                for (int i = 0; i < minutes.length(); i++) {
                    if (":,. ".indexOf(minutes.charAt(i)) != -1) {
                        k++;
                        if (k > 1 || i == 0 || i == (minutes.length() - 1)) { // ex->   :45 || 5: || (9:2: || 9::2)
                            m = Integer.parseInt(minutes); // for exeption
                        }
                    }
                }
                parts = minutes.split(String.valueOf(splitter));
                if (parts.length > 2) {
                    m = Integer.parseInt(minutes); // for exeption
                }

            } else if (minutes.contains("h") || minutes.contains("m")) {

                int minIndex = minutes.indexOf("m");
                int hourIndex = minutes.indexOf("h");

                if (hourIndex != -1 && minIndex != -1) {
                    int r = 0;
                    for (int i = 0; i < minutes.length(); i++) {
                        if ("hm".indexOf(minutes.charAt(i)) != -1) {  //ex-> 2hh8m
                            r++;
                            if (r > 2) {
                                m = Integer.parseInt(minutes); // for exeption
                            }
                        }
                    }
                    if ((hourIndex < minIndex) && hourIndex != 0) {
                        h = Integer.parseInt(minutes.substring(0, hourIndex));
                        m = Integer.parseInt(minutes.substring(hourIndex + 1, minIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                } else if (hourIndex != -1) {
                    if (hourIndex != 0 && ((hourIndex + 1) == minutes.length())) {
                        h = Integer.parseInt(minutes.substring(0, hourIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                } else {
                    if (minIndex != 0 && ((minIndex + 1) == minutes.length())) {
                        m = Integer.parseInt(minutes.substring(0, minIndex));
                    } else {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                }
            } else {
                try {
                    h = Integer.parseInt(minutes);
                } catch (NumberFormatException exc) {
                    h = 0;
                    return 0;
                }
            }

            if (parts[0] != null && parts[1] != null) {

                for (int i = 0; i < parts[0].length(); i++) {
                    if ("0123456789".indexOf(parts[0].charAt(i)) == -1) {
                        m = Integer.parseInt(minutes); // for exeption
                    }
                }

                h = Integer.parseInt(parts[0]);

                for (int i = 0; i < parts[1].length(); i++) {
                    if ("0123456789".indexOf(parts[1].charAt(i)) == -1) {
                        m = Integer.parseInt(minutes);   // for exeption
                    }
                }
                m = Integer.parseInt(parts[1]);

                if (m >= 60) {
                    h += m / 60;
                    m = m % 60;
                }
            }

        } catch (StringIndexOutOfBoundsException | NumberFormatException exc) {
            h = 0;

            return 0;
        }
        return h * 60 + m;
    }


    /*Phone Number Formatting */
    public static String formatPhoneNumber(String phoneNumber, boolean... returnNaIfNull) {
        if (phoneNumber == null) {
            if (returnNaIfNull != null && returnNaIfNull.length > 0 && returnNaIfNull[0]) {
                return "N/A";
            } else {
                return "";
            }
        }
        if (phoneNumber != null && phoneNumber.contains("|")) {
            String[] codes = phoneNumber.split("\\|");
            if (codes.length == 3) {
                return phoneStringToInt(codes[0], codes[1], codes[2]);
            } else {
                return phoneStringToInt(phoneNumber);
            }
        }
        return phoneNumber;
    }

    public static String cleanPhoneNumber(String phone) {

        if (phone == null || "".equals(phone)) {
            return null;
        }
        phone = phone.replace("(", "");
        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");
        phone = phone.replace(" ", "");
        phone = phone.replace(":", "");
        phone = phone.replace(";", "");
        phone = phone.replace(",", "");
        phone = phone.replace(".", "");
        phone = phone.replace("|", "");
        phone = phone.replaceAll("||", "");
        phone = phone.replace("+", "");

        return phone;
    }

    public static boolean isWikiVipworkspace(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("wiki.vipworkspace.com");
    }

    public static boolean isFromGenesisGift(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("genesis-gifts.com") || request.getRequestURL().toString().contains("genesis");
    }

    public static boolean isOrient(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("orient");
    }
    public static boolean isNewKpi(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("new.kpi");
    }

    public static boolean isUpshot(HttpServletRequest request) {
        return (request.getRequestURL().toString().contains("upshot") ||
                request.getRequestURL().toString().contains("erp.com") ||
                request.getRequestURL().toString().contains("erp.ae")) && !request.getRequestURL().toString().contains("passionerp.com");
    }

    public static boolean isTexnopark(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("texnopark");
    }

    public static boolean isYuborUz(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("yubor");
    }

    public static boolean isArtel(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("artel");
    }

    public static boolean isODP(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("omandatapark.com");
    }

    public static boolean isActivira(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("activira.com")
                || request.getRequestURL().toString().contains("passionerp.com")
                || request.getRequestURL().toString().contains("vipworkspace")
                || request.getRequestURL().toString().contains("tjilo")
                || request.getRequestURL().toString().contains("ebmconsultant.com")
                || request.getRequestURL().toString().contains("kpi.developmentlogix.com")
                || request.getRequestURL().toString().contains("gh.kpi")
                || request.getRequestURL().toString().contains("namangan.kpi");
    }

    public static boolean isBat(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("bat.kpi");
    }

    public static boolean isAkfa(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("akfa.kpi");
    }

    public static boolean isCar24(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("car24.kpi");
    }

    public static boolean isUzAuto(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("uzauto.kpi");
    }

    public static boolean isMerit(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("merit.kpi");
    }

    public static boolean isGtl(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("hrm.uzgtl");
    }

    public static boolean isNestle(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("nestle.kpi");
    }

    public static boolean isFolioOne(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("apps.folioone");
    }

    public static boolean isTenderGPT(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("srmgpt");
    }
    public static boolean isGym(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("gym.kpi");
    }

    public static boolean isBrain(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("brainbm") || request.getRequestURL().toString().contains("brain");
    }

    public static boolean isHotel(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("hotel");
    }

    public static boolean isTextileFinds(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("textilefinds");
    }

    public static boolean isPraaktis(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("praaktisgo");
    }
    public static boolean isCspace(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("cspace");
    }

    public static boolean isZeta(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("zeta");
    }

    public static boolean isAgroBank(HttpServletRequest request) {
        return request.getRequestURL().toString().contains("hrm.agrobank") || request.getRequestURL().toString().contains("t-hrm.agrobank") || request.getRequestURL().toString().contains("t-hrm.agro-net") || request.getRequestURL().toString().contains("172.16.10.70");
    }


    public static String joinStrArray(String[] strings, String joinBy) {
        if (joinBy == null)
            joinBy = ",";

        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        if (strings != null) {
            for (String string : strings) {
                if (isFirst) {
                    result.append(string);
                    isFirst = false;
                } else {
                    result.append(joinBy).append(string);
                }
            }
        }

        return result.toString();
    }

    public static String joinStrList(List<String> strings, String joinBy) {
        if (joinBy == null)
            joinBy = ",";

        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        if (strings != null && strings.size() > 0) {
            for (String string : strings) {
                if (isFirst) {
                    result.append(string);
                    isFirst = false;
                } else {
                    result.append(joinBy).append(string);
                }
            }
        }

        return result.toString();
    }

    public static String joinIntList(List<Integer> strings, String joinBy) {
        if (joinBy == null) {
            joinBy = ",";
        }

        StringBuilder result = new StringBuilder();
        boolean isFirst = true;

        if (strings != null && strings.size() > 0) {
            for (Integer string : strings) {
                if (isFirst) {
                    result.append(string);
                    isFirst = false;
                } else {
                    result.append(joinBy).append(string);
                }
            }
        }
        if ("".contentEquals(result)) {
            result.append("-1");
        }
        return result.toString();
    }


    private static String phoneStringToInt(String phoneNumber) {
        return phoneStringToInt(null, null, phoneNumber);
    }

    private static String phoneStringToInt(String code1, String code2, String phoneNumber) {
        String areaCode = null, countryCode = null, phone = null;
        if (code1 != null && !"".equals(code1)) {
            areaCode = code1;
        }
        if (code2 != null && !"".equals(code2)) {
            countryCode = code2;
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            if (!phoneNumber.contains("|")) {
                phone = phoneNumber;
            } else {
                return phoneStringToInt(phoneNumber.split("\\|"));
            }
        }
        return (areaCode != null ? "+" + areaCode + " " : "") + (countryCode != null ? countryCode + " " : "") + (phone == null ? "" : phone);
    }

    private static String phoneStringToInt(String[] phoneNumber) {
        if (phoneNumber.length == 3) {
            return phoneStringToInt(phoneNumber[0], phoneNumber[1], phoneNumber[2]);
        } else {
            StringBuilder number = new StringBuilder();
            for (String n : phoneNumber) {
                if (n != null)
                    number.append(n + " ");
            }
            return number.toString();
        }
    }

    public static String getIntegerListAsString(ArrayList<Integer> ids) {
        if (ids != null) {
            return getIntegerListAsString(ids.toArray(new Integer[]{}));
        } else {
            return null;
        }
    }

    public static String getIntegerListAsString(Integer[] ids) {
        StringBuilder idString = new StringBuilder();
        boolean isFirst = true;
        if (ids != null) {
            for (Integer id : ids) {
                if (isFirst) {
                    idString.append(id.toString());
                    isFirst = false;
                } else {
                    idString.append(",").append(id.toString());
                }
            }
        }
        return idString.toString();
    }

    public static String getStringEachValueWithParentheses(String value) {
        String[] array = value.split(",");
        StringBuilder sb = new StringBuilder();
        for (String anArray : array) {
            sb.append("'").append(anArray).append("',");
        }
        return sb.substring(0, sb.toString().length() - 1);
    }

    public static String determineContentType(String contentType, String fileName) {
        return contentType;
    }


    public static String refactor(Object obj, boolean isBold) {
        String s = null;
        if (obj != null) {
            if (obj instanceof Date) {
                s = ServerUtils.dateFormat(((Date) obj), "MM/dd/yyyy");
            } else {
                s = obj.toString();
            }
        }
        if (s != null && !"".equals(s)) {
            if (isBold) {
                return "<b>" + s + "</b>";
            } else {
                return s;
            }
        }
        return "";
    }

    public static String refactor(Object obj) {
        return refactor(obj, true);
    }

    public static boolean isRTL(String string){
        Boolean fromRightToLeft = false ;
        char[] chars = string != null ? string.toCharArray() : new char[0];
        for(char c: chars){
            if(c >= 0x5D0 && c <= 0x6ff){
                fromRightToLeft = true;
                break;
            }
        }
        return fromRightToLeft;
    }

    /**
     * convert hex coded Color to RGB values
     * something like
     * @param hexCode
     * @return
     */
    public static Color hexToRGB(String hexCode) {
        Integer[] result = new Integer[3];
        if(hexCode != null && !"".equals(hexCode) && hexCode.length() == 6) {
            int index = 0;
            while (index < hexCode.length()) {
                result[index/2] = hex2decimal((hexCode.substring(index, Math.min(index + 2,hexCode.length()))));
                index += 2;
            }
            return new Color(result[0], result[1], result[2]);
        } else {
            return null;
        }
    }
    public static int[] convertHexToRGB(String hexCode) {
        int[] result = new int[3];
        if(hexCode != null && !"".equals(hexCode)) {
            int index = 0;
            while (index < hexCode.length()) {
                result[index/2] = hex2decimal((hexCode.substring(index, Math.min(index + 2,hexCode.length()))));
                index += 2;
            }
            return result;
        } else {
            return null;
        }
    }
    private static Integer hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        Integer val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    /**
     * Check obj if it's null and if the size of the collections and maps
     * @param obj
     * @return boolean
     */
    public static boolean isOk(Object obj) {
        if (obj == null){
            return false;
        }
        if(obj instanceof Collection){
            return ((Collection)obj).size() > 0;
        }
        if(obj instanceof Map){
            return ((Map)obj).size() > 0;
        }
        if(obj instanceof Boolean){
            return (Boolean) obj;
        }
        return true;
    }

}
