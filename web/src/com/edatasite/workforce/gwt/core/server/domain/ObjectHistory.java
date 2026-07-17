package com.edatasite.workforce.gwt.core.server.domain;

import com.edatasite.workforce.core.domain.EdsUser;

import java.util.Date;

public interface ObjectHistory {
    void setLastUpdateTime(Date value);

    void setUpdater(EdsUser user);

    void setCreationTime(Date value);

    void setCreator(EdsUser value);

}
