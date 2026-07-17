package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAiSavedPrompt;
import com.edatasite.workforce.gwt.core.server.db.accounting.AiSavedPromptManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("aiSavedPromptManager")
public class AiSavedPromptManagerImpl extends BaseManager<EdsAiSavedPrompt> implements AiSavedPromptManager {

    public AiSavedPromptManagerImpl() {
        super(EdsAiSavedPrompt.class);
    }

    @Override
    public List<EdsAiSavedPrompt> getByUser(EdsUser user) {
        return find(
                "SELECT p FROM EdsAiSavedPrompt p WHERE p.user = ? AND (p.deleted IS NULL OR p.deleted <> true) ORDER BY p.objectID DESC",
                user
        );
    }
}
