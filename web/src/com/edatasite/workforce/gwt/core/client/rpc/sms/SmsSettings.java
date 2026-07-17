package com.edatasite.workforce.gwt.core.client.rpc.sms;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/15/11
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class SmsSettings implements IsSerializable {
    public static final String NAME = "NAME";
    public static final String PROVIDER_NAME = "PROVIDER_NAME";

    public static final String CLICKATELL = "ClickATell";
    public static final String MVAAYOO = "MVaaYoo";
    public static final String SUNCELLULAR = "SunCellular";
    public static final String EZYTEXTPRO = "EzyTextPro";
    public static final String OURSMS = "Our SMS";
    public static final String ESKIZSMS = "Eskiz SMS";
    public static final String PLAY_MOBILE = "Play Mobile";
    public static final Integer SP_CLICKATELL = 1;
    public static final Integer SP_MVAAYOO = 2;
    public static final Integer SP_SUNCELLULAR = 3;
    public static final Integer SP_EZYTEXTPRO = 4;
    public static final Integer SP_OURSMS = 5;
    public static final Integer SP_ESKIZSMS = 6;
    public static final Integer SP_PLAY_MOBILE = 7;
    public static final HashMap<Integer, String[]> requiredFieldIDs = new HashMap<>();
    public static final SelectItem[] PROVIDERS = new SelectItem[]{
            new SelectItem(SP_CLICKATELL, CLICKATELL),
            new SelectItem(SP_MVAAYOO, MVAAYOO),
            new SelectItem(SP_SUNCELLULAR, SUNCELLULAR),
            new SelectItem(SP_EZYTEXTPRO, EZYTEXTPRO),
            new SelectItem(SP_OURSMS, OURSMS),
            new SelectItem(SP_ESKIZSMS, ESKIZSMS),
            new SelectItem(SP_PLAY_MOBILE, PLAY_MOBILE)
    };

    static {
        requiredFieldIDs.put(SP_CLICKATELL, new String[]{"username", "password", "apiid", "senderID", "clientID"});
        requiredFieldIDs.put(SP_MVAAYOO, new String[]{"username", "password", "senderID"});
        requiredFieldIDs.put(SP_SUNCELLULAR, new String[]{"username", "password"});
        requiredFieldIDs.put(SP_EZYTEXTPRO, new String[]{"apikey", "campaignID", "routeID", "senderID"});
        requiredFieldIDs.put(SP_OURSMS, new String[]{"username", "password", "senderID"});
        requiredFieldIDs.put(SP_ESKIZSMS, new String[]{"email", "password", "from"});
        requiredFieldIDs.put(SP_PLAY_MOBILE, new String[]{"url", "username", "password", "from"});
    }

    private Integer objectID;
    private Integer providerID;
    private String providerName;
    private String name;
    private String keyValues;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProviderID() {
        return providerID;
    }

    public void setProviderID(Integer providerID) {
        this.providerID = providerID;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(String keyValues) {
        this.keyValues = keyValues;
    }

    private HashMap<String, String> keyValuesMap;

    public HashMap<String, String> getKeyValuesAsMap() {
        keyValuesMap = keyValuesMap == null ? new HashMap<>() : keyValuesMap;
        if (keyValuesMap.size() == 0 && keyValues != null && !"".equals(keyValues)) {
            String[] keyValues = this.keyValues.split(";");
            if (keyValues != null && keyValues.length > 0) {
                for (String keyValue : keyValues) {
                    String key = keyValue.substring(0, keyValue.indexOf("="));
                    String value = keyValue.substring(keyValue.indexOf("=") + 1);
                    if (key != null && value != null && !"".equals(key)) {
                        keyValuesMap.put("<" + key.replaceAll("<|>", "") + ">", value);
                    }
                }
            }
        }
        return keyValuesMap;
    }

    public static String getProviderNameByID(Integer smsProviderID) {
        String result = null;
        if (SP_CLICKATELL.equals(smsProviderID)) {
            result = CLICKATELL;
        } else if (SP_MVAAYOO.equals(smsProviderID)) {
            result = MVAAYOO;
        } else if (SP_SUNCELLULAR.equals(smsProviderID)) {
            result = SUNCELLULAR;
        } else if (SP_EZYTEXTPRO.equals(smsProviderID)) {
            result = EZYTEXTPRO;
        } else if (SP_OURSMS.equals(smsProviderID)) {
            result = OURSMS;
        } else if (SP_ESKIZSMS.equals(smsProviderID)) {
            result = ESKIZSMS;
        } else if (SP_PLAY_MOBILE.equals(smsProviderID)) {
            result = PLAY_MOBILE;
        }
        return result;
    }
}
