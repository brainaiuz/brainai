package com.edatasite.workforce.gwt.core.server.app;

import java.util.Date;

/**
 * Created by Hayot on 10/13/2014.
 */
public interface CaptchaServiceLocal {
    void create(String txt, Date expire);
}
