package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.RestHookManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 29, 2019
 * Time: 4:54:22 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Repository("restHookManager")
public class RestHookManagerImpl extends BaseManager<EdsRestHook> implements RestHookManager, Constants {

    public RestHookManagerImpl() {
        super(EdsRestHook.class);
    }

    @Override
    public List<EdsRestHook> getAllRestHooks() {
        return slaveEntityManager.createQuery("select rh from EdsRestHook rh where (rh.deleted is null or rh.deleted<>true)",
                EdsRestHook.class).getResultList();
    }

    @Override
    public List<EdsRestHook> getByEventName(String eventName) {
        return slaveEntityManager.createQuery("select rh from EdsRestHook rh where (rh.deleted is null or rh.deleted<>true) AND rh.eventName=:EVENT",
                EdsRestHook.class).setParameter("EVENT", eventName).getResultList();
    }

}