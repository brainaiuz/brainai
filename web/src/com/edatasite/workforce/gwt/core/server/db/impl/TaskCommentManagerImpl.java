package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskComment;
import com.edatasite.workforce.gwt.core.server.db.TaskCommentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("taskCommentManager")
public class TaskCommentManagerImpl extends AttachmentSupportManager<EdsTaskComment> implements TaskCommentManager {

    public TaskCommentManagerImpl() {
        super(EdsTaskComment.class);
    }

    public List<EdsTaskComment> getComments(EdsTask task) {
        return find("select tc from EdsTaskComment tc where tc.task=? order by tc.creationDate desc", task);
    }

}
