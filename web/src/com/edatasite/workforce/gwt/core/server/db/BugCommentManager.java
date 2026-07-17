package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBugComment;
import com.edatasite.workforce.core.domain.EdsBugReport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 27.08.2009
 * Time: 11:41:31
 * To change this template use File | Settings | File Templates.
 */
public interface BugCommentManager extends Manager<EdsBugComment> {
    List<EdsBugComment> getBugComments(EdsBugReport bug);
}
