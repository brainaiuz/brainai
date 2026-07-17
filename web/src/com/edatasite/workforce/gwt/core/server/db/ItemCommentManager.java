package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsItemComment;

import java.util.List;

public interface ItemCommentManager extends Manager<EdsItemComment> {
    List<EdsItemComment> getComments(Integer productId);
}