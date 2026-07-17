package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCoursePrice;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 9/8/12
 * Time: 10:36 AM
 */
public interface CoursePriceManager extends Manager<EdsCoursePrice> {

    List<EdsCoursePrice> getCoursePrices(Integer courseID);

    EdsCoursePrice getCoursePriceByLocation(Integer courseID, Integer locationID);

    void deleteCoursePrices(Integer courseID);
}