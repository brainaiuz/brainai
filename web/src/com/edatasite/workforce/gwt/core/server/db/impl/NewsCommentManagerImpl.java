package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNewsComment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.NewsCommentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 24, 2009
 * Time: 11:45:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("newsCommentManager")
public class NewsCommentManagerImpl extends BaseManager<EdsNewsComment> implements NewsCommentManager {
    public NewsCommentManagerImpl() {
        super(EdsNewsComment.class);
    }

    public List<EdsNewsComment> getComments(Integer newsId) {
        return find("from EdsNewsComment where news.objectID=? order by date", newsId);
    }

    public Integer getCommentsCount(Integer newsId) {
        Long l = (Long) findSingle("select count(objectID) from EdsNewsComment where news.objectID=?", newsId);
        return l != null ? l.intValue() : 0;
    }

    public EdsNewsComment getRatedView(EdsUser user, EdsNews news) {
        return (EdsNewsComment) findSingle("from EdsNewsComment nnv where nnv.user = ? and " +
                "nnv.news = ? and nnv.commented <> true", user, news);
    }

    @SuppressWarnings("unchecked")
    public List<EdsNewsComment> getNewsComments(EdsNews news) {
        return find("from EdsNewsComment nnv where nnv.news = ? and nnv.commented = true", news);
    }
}
