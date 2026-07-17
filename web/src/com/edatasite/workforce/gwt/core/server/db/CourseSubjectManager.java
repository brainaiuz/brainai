package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSubject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 26.12.12
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */
public interface CourseSubjectManager extends Manager<EdsCourseSubject> {
    List<EdsCourseSubject> getParentCourseSubject(Integer objectId);

    List<EdsCourseSubject> list(ListingFilterParameter filterParameter);

    Integer getCourseSubjectTotalCount(ListingFilterParameter filterParameter);

    Integer getCountParent(EdsCourseSubject parent);

    void deleteChild(EdsCourseSubject child);
}
