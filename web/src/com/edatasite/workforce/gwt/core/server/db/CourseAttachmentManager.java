package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCourseAttachment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Mar 12, 2009
 * Time: 2:21:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourseAttachmentManager extends Manager<EdsCourseAttachment> {
    List<EdsCourseAttachment> getCourseAttachmentsByID(Integer courseId);
}
