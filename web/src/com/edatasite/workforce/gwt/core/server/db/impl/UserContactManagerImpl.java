package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserContact;
import com.edatasite.workforce.gwt.core.server.db.UserContactManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 23.10.2008
 * Time: 16:12:52
 * To change this template use File | Settings | File Templates.
 */
@Repository("userContactManager")
public class UserContactManagerImpl extends BaseManager<EdsUserContact> implements UserContactManager {

    public UserContactManagerImpl() {
        super(EdsUserContact.class);
    }

    public List<EdsUserContact> getCompanyOneOffUsers() {
        return find("FROM EdsUserContact WHERE deleted<>true");
    }

    public List<EdsUserContact> getCompanyOneOffUsers(EdsUser user) {
        return find("FROM EdsUserContact uc WHERE uc.deleted<>true and uc.creator=?", user);
    }
}
