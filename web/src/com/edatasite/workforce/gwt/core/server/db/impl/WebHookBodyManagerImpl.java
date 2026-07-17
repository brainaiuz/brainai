package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWebHookBody;
import com.edatasite.workforce.gwt.core.server.db.WebHookBodyManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 14.01.2022
 */
@Repository("webHookBodyManager")
public class WebHookBodyManagerImpl extends BaseManager<EdsWebHookBody> implements WebHookBodyManager {
    public WebHookBodyManagerImpl() {
        super(EdsWebHookBody.class);
    }
}
