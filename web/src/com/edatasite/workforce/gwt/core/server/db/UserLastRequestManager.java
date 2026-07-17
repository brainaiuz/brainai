package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUserLastRequest;

public interface UserLastRequestManager extends Manager<EdsUserLastRequest> {

    EdsUserLastRequest getLastDate(Integer userId);

}
