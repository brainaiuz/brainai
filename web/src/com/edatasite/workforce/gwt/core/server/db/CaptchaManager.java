package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCaptcha;

/**
 * Created by Hayot on 10/13/2014.
 */
public interface CaptchaManager extends Manager<EdsCaptcha> {

    boolean validateCaptcha(String antibot);
}
