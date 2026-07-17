package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 24, 2009
 * Time: 4:43:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourseManager extends Manager<EdsCourse> {
    List<EdsCourse> getAllCourses();

    List<EdsCourse> getMyCourses(Integer employeeID);

    EdsCourse getCourseById(Integer courseId);

    List<EdsCourse> getCourseList(String Ids);

    List<EdsCourse> searchCourses(String searchKey);

    boolean isMyCourse(Integer courseId);

    List<EdsCourse> list(ListingFilterParameter filterParameter);

    Integer getCourseTotalCount(ListingFilterParameter fp);

    List<Integer> getInstructorCoursesIds(Integer instructorID);

    void deleteInstructorInCourses(Integer instructorID);

    Integer getCourseLastIntNumber();

    SelectItem[] getCoursesAsSelectItems(ListingFilterParameter filterParameter);

    List<EdsProductCategory> getCourseProductCategorisList(String caurseIds);

    List<EdsCourse> getByProductIdsCourseList(String productCategoriesIds);

    Map<Integer, ArrayList<String>> getPreRequisiteCourseNameMap(String courseIds);

    Map<Integer, String> getCourseOtherPrerequisite(String courseIds);
}
