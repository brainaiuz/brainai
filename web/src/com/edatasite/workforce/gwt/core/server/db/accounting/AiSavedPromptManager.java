package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAiSavedPrompt;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface AiSavedPromptManager extends Manager<EdsAiSavedPrompt> {

    List<EdsAiSavedPrompt> getByUser(EdsUser user);
}
