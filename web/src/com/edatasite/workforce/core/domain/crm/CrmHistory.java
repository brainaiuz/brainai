package com.edatasite.workforce.core.domain.crm;

import com.edatasite.workforce.core.domain.EdsUser;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 9/12/12
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmHistory {
    EdsUser getUpdater();

    Integer getEntityID();

    Date getCreationTime();

    String getMessage();

    boolean isSuperUser();
}
