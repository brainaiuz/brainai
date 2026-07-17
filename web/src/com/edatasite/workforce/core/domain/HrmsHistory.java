package com.edatasite.workforce.core.domain;

import java.util.Date;

/**
 * User: ASUS
 * Date: 23.02.2016 17:11
 */
public interface HrmsHistory {
    EdsUser getUpdater();

    Integer getEntityID();

    Date getCreationTime();

    String getMessage();

    boolean isSuperUser();
}
