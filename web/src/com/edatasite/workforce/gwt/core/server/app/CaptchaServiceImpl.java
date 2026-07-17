package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCaptcha;
import com.edatasite.workforce.gwt.core.server.db.CaptchaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 6:13:41 PM To
 * change this template use File | Settings | File Templates.
 */

@Transactional
@Service("captchaService")
public class CaptchaServiceImpl implements CaptchaServiceLocal {

    @Autowired
    private CaptchaManager captchaManager;

    @Override
    public void create(String txt, Date expire) {
        captchaManager.create(new EdsCaptcha(txt, expire));
    }
}
