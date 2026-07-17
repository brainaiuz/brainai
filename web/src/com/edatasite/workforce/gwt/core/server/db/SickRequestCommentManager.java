package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSickRequestComment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 25, 2009
 * Time: 2:59:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SickRequestCommentManager extends Manager<EdsSickRequestComment> {

    List<EdsSickRequestComment> getComments(Integer sickRequestId);
}
