package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBugComment;
import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.gwt.core.server.db.BugCommentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 27.08.2009
 * Time: 11:42:46
 * To change this template use File | Settings | File Templates.
 */
@Repository("bugCommentManager")
public class BugCommentManagerImpl extends AttachmentSupportManager<EdsBugComment> implements BugCommentManager {

    public BugCommentManagerImpl() {
        super(EdsBugComment.class);
    }


    public List<EdsBugComment> getBugComments(EdsBugReport bug) {
        return find("select bc from EdsBugComment bc where bc.bug = ? order by bc.creationDate desc", bug);
    }
}
