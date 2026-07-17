package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.assessment.EdsRatingComment;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;

/**
 * User: Ilhombek
 * Date: 12/12/12
 * Time: 2:42 PM
 */
public interface RatingCommentManager extends Manager<EdsRatingComment> {

    EdsRatingComment getCreateAndGetRatingComment(SkillCommentItem commentItem);
}