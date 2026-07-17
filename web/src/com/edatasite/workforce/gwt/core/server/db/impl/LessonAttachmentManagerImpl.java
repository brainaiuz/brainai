package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLessonAttachment;
import com.edatasite.workforce.gwt.core.server.db.LessonAttachmentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Mar 12, 2009
 * Time: 3:50:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("lessonAttachmentManager")
public class LessonAttachmentManagerImpl extends UploadManagerImpl<EdsLessonAttachment> implements LessonAttachmentManager {
    public LessonAttachmentManagerImpl() {
        super(EdsLessonAttachment.class);
    }

    public List<EdsLessonAttachment> getLessonAttachmentsByID(Integer lessonId) {
        return find("select la from EdsLessonAttachment la where la.lesson.objectID=?", lessonId);
    }
}
