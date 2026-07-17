package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskComment;

import java.util.List;

public interface TaskCommentManager extends Manager<EdsTaskComment> {
    List<EdsTaskComment> getComments(EdsTask task);
}
