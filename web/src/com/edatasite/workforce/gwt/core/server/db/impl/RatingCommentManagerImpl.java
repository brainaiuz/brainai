package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsRatingComment;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillCommentItem;
import com.edatasite.workforce.gwt.core.server.db.RatingCommentManager;
import org.springframework.stereotype.Repository;

/**
 * User: Ilhombek
 * Date: 12/12/12
 * Time: 2:40 PM
 */
@Repository("ratingCommentManager")
public class RatingCommentManagerImpl extends BaseManager<EdsRatingComment> implements RatingCommentManager {

    public RatingCommentManagerImpl() {
        super(EdsRatingComment.class);
    }

    @Override
    public EdsRatingComment getCreateAndGetRatingComment(SkillCommentItem commentItem) {
        EdsRatingComment ratingComment = new EdsRatingComment();
        ratingComment.setReviewerComment(commentItem.getReviewerComment());
        ratingComment.setEmployeeComment(commentItem.getEmployeeComment());
        ratingComment.setType(commentItem.isTypeSkill() ? EdsRatingComment.RATING_COMMENT_TYPE_SKILL : EdsRatingComment.RATING_COMMENT_TYPE_GOAL);

        create(ratingComment);
        return ratingComment;
    }
}