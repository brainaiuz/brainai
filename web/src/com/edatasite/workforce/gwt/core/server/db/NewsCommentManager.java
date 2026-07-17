package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNewsComment;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 24, 2009
 * Time: 11:44:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NewsCommentManager extends Manager<EdsNewsComment> {
    List<EdsNewsComment> getComments(Integer newsId);

    Integer getCommentsCount(Integer newsId);

    EdsNewsComment getRatedView(EdsUser user, EdsNews news);

    List<EdsNewsComment> getNewsComments(EdsNews news);
}
