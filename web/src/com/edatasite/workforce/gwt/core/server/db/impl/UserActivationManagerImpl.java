package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserActivation;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserActivationManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 25.02.2009
 * Time: 12:22:40
 * To change this template use File | Settings | File Templates.
 */
@Repository("userActivationManager")
public class UserActivationManagerImpl extends BaseManager<EdsUserActivation> implements UserActivationManager, Constants {

    public UserActivationManagerImpl() {
        super(EdsUserActivation.class);
    }


    public List<EdsUser> getPandingUsers() {
        Map<String, Object> map = new HashMap<>();
        map.put("pending", EMPLOYEE_STATUS_PENDING);
        return find(" select m from EdsUser m where m.accountStatus.code=:pending  and ( m not in (select ua.user from EdsUserActivation ua))");
    }

    public List<EdsUserActivation> getNonMailSentUsers(Date date) {
        return find("select ua from EdsUserActivation ua where ua.sentCount = 0 and ua.lastSentDate<?", date);
    }


}
