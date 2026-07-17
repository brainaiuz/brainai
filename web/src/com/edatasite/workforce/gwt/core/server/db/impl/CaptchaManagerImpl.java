package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCaptcha;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CaptchaManager;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * Created by Hayot on 10/13/2014.
 */
@Repository("captchaManager")
public class CaptchaManagerImpl extends BaseManager<EdsCaptcha> implements CaptchaManager {

    public CaptchaManagerImpl() {
        super(EdsCaptcha.class);
    }

    @Override
    public boolean validateCaptcha(String antibot) {
        if (antibot == null || "".equals(antibot)) {
            return false;
        }
        if (antibot.contains(Constants.NO_CAPTCHA_USED)) {
            return true;
        }
        if (antibot.contains("|")) {
            String[] as = antibot.split("\\|");
            StringBuilder antibotBuilder = new StringBuilder("'" + antibot + "'");
            for (String a : as) {
                antibotBuilder.append(",'").append(a.toLowerCase()).append("'");
            }
            antibot = antibotBuilder.toString();
        } else {
            antibot = "'" + antibot + "'";
        }
        Object result = findNativeSingle("select * from " + getPublic() + ".captcha where txt in (" + antibot.toLowerCase() + ")");
        try {
            updateNative("delete from " + getPublic() + ".captcha where txt in (" + antibot.toLowerCase() + ")");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result != null;
    }
}
