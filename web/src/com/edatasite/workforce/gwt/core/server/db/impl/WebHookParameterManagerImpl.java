package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWebHookParameter;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;
import com.edatasite.workforce.gwt.core.server.db.WebHookParameterManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 13.01.2022
 */
@Repository("webHookParameterManager")
public class WebHookParameterManagerImpl extends BaseManager<EdsWebHookParameter> implements WebHookParameterManager {
    public WebHookParameterManagerImpl() {
        super(EdsWebHookParameter.class);
    }

    @Override
    public void deleteAllParametersByWebHook(EdsWorkflowWebHook webHook) {
        update("delete from EdsWebHookParameter p where p.webHook = ? or p.webHookBody = ? ", webHook, webHook.getBody());
    }
}
