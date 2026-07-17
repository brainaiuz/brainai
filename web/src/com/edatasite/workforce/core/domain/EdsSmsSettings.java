package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.sms.ClickATellSmsProvider;
import com.edatasite.shared.sms.EskizSMSProvider;
import com.edatasite.shared.sms.EzyTextProSmsProvider;
import com.edatasite.shared.sms.MvaayooSmsProvider;
import com.edatasite.shared.sms.OurSMSProvider;
import com.edatasite.shared.sms.PlayMobileSmsProvider;
import com.edatasite.shared.sms.SmsProvider;
import com.edatasite.shared.sms.SunCellularSmsProvider;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/14/11
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "smsSettings")
public class EdsSmsSettings extends EdsObject {
    /**
     * We need Sender Collector. We call sender from this collector by companyID. (Clickatell Sender only)
     */
    public final static Map<Integer, SmsProvider> senderCollector = new HashMap<>();
    public static final String XML = "XML";
    public static final String URL = "URL";
    public static final String SERVER = "SERVER";
    public static final String RESPONSE_SUCCESS = "RESPONSE_SUCCESS";

    public static final String USERNAME = "<username>";
    public static final String EMAIL = "<email>";
    public static final String PASSWORD = "<password>";
    public static final String PHONE_NUMBER = "<phone>";
    public static final String FROM = "<from>";
    public static final String MESSAGE = "<message>";
    public static final String SENDER_ID = "<senderID>";
    public static final String CLIENT_ID = "<clientID>";
    public static final String API_ID = "<apiid>";
    public static final String API_KEY = "<apikey>";
    public static final String CAMPAIGN_ID = "<campaignID>";
    public static final String ROUTE_ID = "<routeID>";
    public static final String BASE_URL = "<url>";
    public static final String CONTENT_LENGTH = "CONTENT_LENGTH";
    //--SmsProviders.
    public static final Integer SP_CLICKATELL = SmsSettings.SP_CLICKATELL;
    public static final Integer SP_MVAAYOO = SmsSettings.SP_MVAAYOO;
    public static final Integer SP_SUNCELLULAR = SmsSettings.SP_SUNCELLULAR;
    public static final Integer SP_EZYTEXTPRO = SmsSettings.SP_EZYTEXTPRO;
    public static final Integer SP_OURSMS = SmsSettings.SP_OURSMS;
    public static final Integer SP_ESKIZSMS = SmsSettings.SP_ESKIZSMS;
    public static final Integer SP_PLAY_MOBILE = SmsSettings.SP_PLAY_MOBILE;

    public static final String URL_SEND_CLICKATELL = Constants.URL_SMS_CLICKATELL;
    public static final String URL_SEND_CLICKATELL_FOR_USA = Constants.URL_SMS_CLICKATELL_FOR_USA;
    public static final String URL_SEND_MVAAYOO = Constants.URL_SMS_MVAAYOO;
    public static final String URL_SEND_SUNCELLULAR = Constants.URL_SUNCELLULAR;
    public static final String URL_SEND_EZYTEXTPRO = Constants.URL_EZYTEXTPRO;
    public static final String URL_SEND_OURSMS = Constants.URL_OURSMS;
    public static final String URL_LOGIN_SUNCELLULAR = "http://mcpro1.sun-solutions.ph/mc/login.aspx?user=<username>&pass=<password>";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "smsProviderId")
    private Integer smsProviderID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyID")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCompany company;

    @Column(name = "keyValues")
    @Type(type = "text")
    private String keyValues;
//  Syntax = ${username}=hayot.rahimov;${password}=secretCode

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @Override
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * providerRequirements agar type = url bulsa bu yerda url qanday yuborilishi mumkin shular yoziladi. agar type= xml bulsa unda bu xml content buladi. replacement(keyValues...) bilan replace qilib chiqish kerak buladi.
     * masalan
     * type = url;
     * providerRequirments="http://api.clickatell.com/http/sendmsg?user=${username}&password=${password}&api_id=${apiid}&to=${phone}&text=${message}"
     * keyValues="${username}=hayot.rahimov;${password}=secretCode"..
     *
     * @return
     */
    public String getURL() {
        if (SP_CLICKATELL.equals(getSmsProviderID())) {
            return URL_SEND_CLICKATELL;
        } else if (SP_MVAAYOO.equals(getSmsProviderID())) {
            return URL_SEND_MVAAYOO;
        } else if (SP_SUNCELLULAR.equals(getSmsProviderID())) {
            return URL_SEND_SUNCELLULAR;
        } else if (SP_EZYTEXTPRO.equals(getSmsProviderID())) {
            return URL_SEND_EZYTEXTPRO;
        } else if (SP_OURSMS.equals(getSmsProviderID())) {
            return URL_SEND_OURSMS;
        }
        return null;
    }

    public Integer getSmsProviderID() {
        return smsProviderID;
    }

    public void setSmsProviderID(Integer smsProviderID) {
        this.smsProviderID = smsProviderID;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }

    public String getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(String keyValues) {
        this.keyValues = keyValues;
    }

    @Transient
    private Map<String, String> keyValuesMap = new HashMap<>();

    public Map<String, String> getParametrMap() {
        if (keyValuesMap.size() == 0 && getKeyValues() != null && !"".equals(getKeyValues())) {
            String[] keyValues = getKeyValues().split(";");
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

    public String getParametr(String key) {
        return getParametrMap().get(key);
    }

    public SmsSettings getRPC() {
        SmsSettings item = new SmsSettings();
        item.setObjectID(getObjectID());
        item.setName(getName());
        item.setProviderID(getSmsProviderID());
        item.setProviderName(SmsSettings.getProviderNameByID(getSmsProviderID()));
        item.setKeyValues(getKeyValues());
        return item;
    }

    public SmsProvider initProvider(Map<String, String> replacements) {
        replacements = replacements == null ? new HashMap<>() : replacements;
        replacements.putAll(getParametrMap());
        if (SP_CLICKATELL.equals(smsProviderID)) {
            return new ClickATellSmsProvider(this, replacements);
        } else if (SP_MVAAYOO.equals(smsProviderID)) {
            return new MvaayooSmsProvider(this, replacements);
        } else if (SP_SUNCELLULAR.equals(smsProviderID)) {
            return new SunCellularSmsProvider(this, replacements);
        } else if (SP_EZYTEXTPRO.equals(smsProviderID)) {
            return new EzyTextProSmsProvider(this, replacements);
        } else if (SP_OURSMS.equals(smsProviderID)) {
            return new OurSMSProvider(this, replacements);
        } else if (SP_ESKIZSMS.equals(smsProviderID)) {
            return new EskizSMSProvider(this, replacements);
        } else if (SP_PLAY_MOBILE.equals(smsProviderID)) {
            return new PlayMobileSmsProvider(this, replacements);
        }
        return new SmsProvider() {
            @Override
            protected boolean sendXMLRequest() {
                return false;
            }

            @Override
            protected boolean sendURLRequest() {
                return false;
            }
        };
    }

    public static String normalize(String source, Integer smsProviderID) {
        if (source != null) {
            source = source.replace("!", "%21").replace("@", "%40");
            source = source.replace("#", "%23").replace("$", "%24");
            source = source.replace("%", "%25").replace("^", "%5E");
            source = source.replace("&", "%26").replace("(", "%28");
            source = source.replace(")", "%29").replace("~", "%7E");
            source = source.replace("`", "%60");
            if (SmsSettings.SP_CLICKATELL.equals(smsProviderID)) {
                source = source.replace("\n", "+").replace("\n", "+");
                source = source.replace(" ", "+").replace("\n", "+");
            } else {
                source = source.replace("\n", "%20").replace("\n", "%20");
                source = source.replace(" ", "%20").replace("\n", "%20");
            }
        }
        return source == null ? "" : source;
    }

    public static String evaluateTemplate(Map<String, String> values, String urlSmsClickatell) {
        urlSmsClickatell = urlSmsClickatell == null ? "" : urlSmsClickatell;
        if (values != null && values.size() > 0) {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null && !"".equals(entry.getValue())) {
                    String key = entry.getKey().matches("^<.*>$") ? entry.getKey() : "<" + entry.getKey() + ">";
                    urlSmsClickatell = urlSmsClickatell.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        return urlSmsClickatell.replaceAll("(<.*>)", "");
    }
}
