package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;

import java.util.Map;

public class Property {

    private PropertyItem item;

    public Property() {
    }

    public Property(String objectCode) {
        item = Utils.getProperTy(objectCode);
    }

    public String getShort(String defaultValue) {
        if (item != null && !isEmpty(item.getShortcut())) {
            return item.getShortcut();
        }
        return defaultValue;
    }

    public String getShortForNumber(String defaultValue) {
        if (item != null && !isEmpty(item.getShortcut())) {
            return item.getShortcut() + " #";
        }
        return defaultValue;
    }

    public String getShort(String s, String defaultValue) {
        if (item != null && !isEmpty(item.getShortcut())) {
            return format(s, item.getShortcut());
        }
        return format(s, defaultValue);
    }

    public String getSingular(String defaultValue) {
        if (item != null && !isEmpty(item.getSingular())) {
            return item.getSingular();
        }
        return defaultValue;
    }

    public String getSingular(String s, String defaultValue) {
        if (item != null && !isEmpty(item.getSingular())) {
            return format(s, item.getSingular());
        }
        return format(s, defaultValue);
    }

    public String getPlural(String defaultValue) {
        if (item != null && !isEmpty(item.getPlural())) {
            return item.getPlural();
        }
        return defaultValue;
    }

    public String getPlural(String s, String defaultValue) {
        if (item != null && !isEmpty(item.getPlural())) {
            return format(s, item.getPlural());
        }
        return format(s, defaultValue);
    }

    private String format(String format, final String... args) {
        String retVal = format;
        for (final String current : args) {
            retVal = retVal.replaceFirst("[%][s]", current);
        }
        return retVal;
    }

    private boolean isEmpty(String s) {
        if (s != null && s.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public static String get(String objectCode, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        if (item != null && item.getSingular() != null) {
            return item.getSingular();
        }
        return defaultValue;
    }

    public static String getSingularWithLocalizedName(String objectCode, String defaultValue) {

        PropertyItem item = Utils.getProperTy(objectCode);
        String language = Utils.getUserLanguage();
        if (item != null && item.getSingular() != null) {
            CustomFormLocalization name = item.getlName();
            String return_name = "";
            if (name != null) {
                if ("ru".equals(language)) {
                    return_name = name.getRussianName();
                } else if ("en".equals(language)) {
                    return_name = name.getEnglishName();
                } else if ("uz".equals(language)) {
                    return_name = name.getUzbekName();
                } else if ("ar".equals(language)) {
                    return_name = name.getArabicName();
                } else {
                    return defaultValue;
                }
                return return_name.isEmpty() ? item.getSingular() : return_name;
            }
            return item.getSingular();
        }
        return defaultValue;
    }

    public static String getPluralWithObjectCode(String objectCode, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        if (item != null && item.getPlural() != null) {
            return item.getPlural();
        }

        return defaultValue;
    }

    public static String getPluralWithObjectCodeWithReplace(String objectCode, String s, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        String value = defaultValue;
        if (item != null && item.getPlural() != null) {
            value = item.getPlural();
        }
        return s.replaceFirst("[%][s]", value);
    }

    //will be used for formatted strings like add %s
    public static String get(String objectCode, String s, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        String value = defaultValue;
        if (item != null && item.getSingular() != null) {
            value = item.getSingular();
        }
        return s.replaceFirst("[%][s]", value);
    }

    //will be used for formatted strings like add %s
    public static String getShortName(String objectCode, String s, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        String value = defaultValue;
        if (item != null && item.getShortcut() != null) {
            value = item.getShortcut();
        }

        return s.replaceFirst("[%][s]", value);
    }

    public static String getShortName(String objectCode, String defaultValue) {
        PropertyItem item = Utils.getProperTy(objectCode);
        if (item != null && item.getShortcut() != null) {
            return item.getShortcut();
        }

        return defaultValue;
    }

    public static String findByFormId(String formID) {
        if (formID == null || formID.length() == 0) {
            return "";
        }
        for (Map.Entry map : Utils.properties.entrySet()) {
            PropertyItem propertyItem = (PropertyItem) map.getValue();
            if (formID.equals(propertyItem.getFormID())) {
                return propertyItem.getSingular();
            }
        }
        return "";
    }
}
