package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserContact;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 23.10.2008
 * Time: 16:10:02
 * To change this template use File | Settings | File Templates.
 */
public interface UserContactManager extends Manager<EdsUserContact> {

    List<EdsUserContact> getCompanyOneOffUsers();
    List<EdsUserContact> getCompanyOneOffUsers(EdsUser user);
}
