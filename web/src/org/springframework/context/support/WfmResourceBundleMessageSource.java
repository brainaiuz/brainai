package org.springframework.context.support;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public class WfmResourceBundleMessageSource extends ReloadableResourceBundleMessageSource implements WfmMessageSource {

    @Autowired
    private LocaleManager localeManager;

    public String localizeWithParam(String code, Object... arguments) {
        return getMessage(code, arguments, initializeUserLocale());
    }

    public String localizeAccounting(String message) {
        Locale locale = getCurrentuserLocale();
        return getMessage(message, null, locale);
    }

    @Override
    public String localizeAccounting(String message, String defaultMessage) {
        Locale locale = getCurrentuserLocale();
        return getMessage(message, null, defaultMessage, locale);
    }

    public String localizeWithParamAccounting(String code, Object... arguments) {
        Locale locale = getCurrentuserLocale();
        return getMessage(code, arguments, locale);
    }

    private Locale getCurrentuserLocale() {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        Locale locale = Locale.ENGLISH;
        if (user != null && user.getCompany() != null && user.getCompany().getLocale() != null) {
            EdsLocale userLocale = localeManager.getLocaleBylanguageCode(user.getCompany().getLocale());
            if (userLocale != null && userLocale.getCountry() != null) {
                locale = new Locale(userLocale.getLanguageCode(), userLocale.getCountry());
            } else {
                locale = new Locale(user.getCompany().getLocale());
            }
        }
        return locale;
    }

    @Deprecated
    public String localize(String message) {
        return localize(message, message);
    }

    public String localize(String codeMessage, String defaultMessage, Locale locale) {
        return getMessage(codeMessage, null, defaultMessage, locale);
    }

    public String localize(String codeMessage, String defaultText) {
        Locale userLocale = initializeUserLocale();
        // Below code has been removed as method shouldn't return defaultText in some columns(Assignee Status, Overall Status)
//        if (userLocale != null && userLocale.getLanguage() != null
//                && (userLocale.getLanguage().equalsIgnoreCase("en"))
//            && defaultText != null) {
//            return defaultText;
//        }
        return getMessage(codeMessage, null, defaultText, userLocale);
    }

    public String localizeRef(EdsReference reference) {
        return reference != null ? localize(reference.getCode(), reference.getName()) : null;
    }

    public String localizeRef(EdsLeaveReason reference) {
        return reference != null ? localize(reference.getCode(), reference.getName()) : null;
    }

    public Locale initializeUserLocale() {
        Locale userLocale = ServerSecurityContext.getInstance().getUserLocale();
        String userCorrentLocale = "";
        String correntUser = "";
        String userSettingLocale = "";
        String hostBasedDefaultLocale = "";
        if (userLocale == null) {
            userLocale = Locale.ENGLISH;
            userLocale = EdsContextParams.getDefaultLocale(EdsContextParams.getHostname());
            hostBasedDefaultLocale = userLocale.toString();
            EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
            if (user != null) {
                correntUser = user.getUserName();
                UserEmailSettingsManager userEmailSettingsManager = (UserEmailSettingsManager) ApplicationContextProvider.applicationContext.getBean("userEmailSettingsManager");
                EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
                if (userSettings != null && userSettings.getInternationalization() != null) {

                    if (userSettings.getInternationalization().contains("_")) {
                        String[] lns = userSettings.getInternationalization().split("_");
                        userLocale = new Locale(lns[0], lns[1]);
                    } else {
                        userLocale = new Locale(userSettings.getInternationalization());
                    }
                    userSettingLocale = userLocale.toString();
                }
            }
            ServerSecurityContext.getInstance().setUserLocale(userLocale);
        } else {
            userCorrentLocale = userLocale.toString();
        }

//        StringBuilder tempBuilder = new StringBuilder();
//        tempBuilder.append("\n              CORRENT_USER: ").append(correntUser);
//        tempBuilder.append("\n       USER_CORRENT_LOCALE: ").append(userCorrentLocale);
//        tempBuilder.append("\n      USER_SETTINGS_LOCALE: ").append(userSettingLocale);
//        tempBuilder.append("\n HOST_BASED_DEFAULT_LOCALE: ").append(hostBasedDefaultLocale);
//        tempBuilder.append("\n       SENT_MESSAGE_LOCALE: ").append(userLocale);
//        tempBuilder.append("\n _____________________________________________________");
//
//        String tempText = tempBuilder.toString();

        return userLocale;
    }
}
