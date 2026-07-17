package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWebHookParameter;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;

/**
 * User : Akhror
 * Date : 13.01.2022
 */
public interface WebHookParameterManager extends Manager<EdsWebHookParameter> {
    void deleteAllParametersByWebHook(EdsWorkflowWebHook webHook);
}
