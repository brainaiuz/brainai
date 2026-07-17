package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * User: kotabek
 * Date: 04.01.13 14:42
 */
public class ServerAntibotUtils implements Constants {

    public static boolean validateCaptcha(String antibot) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String kaptchaExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

        return antibot == null || kaptchaExpected == null || "".equals(antibot) || "".equals(kaptchaExpected) || !antibot.split("\\|")[0].equalsIgnoreCase(kaptchaExpected);
    }
}
