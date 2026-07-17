package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCourseAttachment;
import com.edatasite.workforce.gwt.core.server.db.CourseAttachmentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Mar 12, 2009
 * Time: 2:22:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseAttachmentManager")
public class CourseAttachmentManagerImpl extends UploadManagerImpl<EdsCourseAttachment> implements
        CourseAttachmentManager {
    CourseAttachmentManagerImpl() {
        super(EdsCourseAttachment.class);
    }

    public List<EdsCourseAttachment> getCourseAttachmentsByID(Integer courseId) {
        return find("select ca from EdsCourseAttachment ca where ca.course.objectID=?", courseId);
    }
}
