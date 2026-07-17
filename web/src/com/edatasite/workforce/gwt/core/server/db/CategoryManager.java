package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCategory;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 24, 2009
 * Time: 3:50:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryManager extends Manager<EdsCategory> {
    List<EdsCategory> getAllCategories();

    List<EdsCategory> getCategoryList();

    List<EdsCategory> getSubCategories(Integer categoryId);

    List<EdsCourse> getCourses(Integer categoryId);

    List<EdsCourse> getCourses(Integer categoryId, Integer employeeId);

    EdsCategory getCategoryById(Integer categoryId);
}
