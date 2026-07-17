package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUserLastRequest;
import com.edatasite.workforce.gwt.core.server.db.UserLastRequestManager;
import org.springframework.stereotype.Repository;

@Repository("userLastRequestManager")
public class UserLastRequestImpl extends BaseManager<EdsUserLastRequest> implements UserLastRequestManager {
    public UserLastRequestImpl() {
        super(EdsUserLastRequest.class);
    }

    @Override
    public EdsUserLastRequest getLastDate(Integer userId) {
        return (EdsUserLastRequest) findSingle("select t from EdsUserLastRequest t where t.employee.objectID = ?  order by t.requestDate desc", userId);
    }
}
