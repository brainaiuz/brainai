package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLessonAttachment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Mar 12, 2009
 * Time: 3:50:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LessonAttachmentManager extends Manager<EdsLessonAttachment> {
    List<EdsLessonAttachment> getLessonAttachmentsByID(Integer lessonId);
}
