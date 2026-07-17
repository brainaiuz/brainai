package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCoursePrice;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CoursePriceManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 9/8/12
 * Time: 10:35 AM
 */
@Repository("coursePriceManager")
public class CoursePriceManagerImpl extends BaseManager<EdsCoursePrice> implements CoursePriceManager {

    public CoursePriceManagerImpl() {
        super(EdsCoursePrice.class);
    }

    public List<EdsCoursePrice> getCoursePrices(Integer courseID) {
        return find("SELECT cp FROM EdsCoursePrice cp WHERE cp.deleted<>true AND cp.course.objectID=?", courseID);
    }

    @Override
    public EdsCoursePrice getCoursePriceByLocation(Integer courseID, Integer locationID) {
        return (EdsCoursePrice) findSingle("SElECT cp FROM EdsCoursePrice cp WHERE cp.course.objectID = ? and cp.location.objectID = ? and " + ServerUtils.checkForDeleted("cp.deleted"), courseID, locationID);
    }

    public void deleteCoursePrices(Integer courseID) {
        update("UPDATE EdsCoursePrice cp SET cp.deleted = true WHERE cp.deleted<>true AND cp.course.objectID=?", courseID);
    }
}