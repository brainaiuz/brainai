package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUserMessage;
import com.edatasite.workforce.gwt.core.server.db.UserMessageManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Azazello on 7/27/2017.
 */
@Repository("userMessageManager")
public class UserMessageManagerImpl extends BaseManager<EdsUserMessage> implements UserMessageManager {
    public UserMessageManagerImpl() {
        super(EdsUserMessage.class);
    }
}
