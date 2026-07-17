package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCategory;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.gwt.core.server.db.CategoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 24, 2009
 * Time: 3:51:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("eCategoryManager")
public class ECategoryManagerImpl extends BaseManager<EdsCategory> implements CategoryManager {
    public ECategoryManagerImpl() {
        super(EdsCategory.class);
    }

    public List<EdsCategory> getCategoryList() {
        return find("from EdsCategory ec where  (ec.deleted=false or ec.deleted is null)" +
                " and ec.parent.objectID=null");
    }

    public List<EdsCategory> getAllCategories() {
        return find("from EdsCategory ec where  (ec.deleted=false or ec.deleted is null)");
    }

    public List<EdsCategory> getSubCategories(Integer categoryId) {
        return find("from EdsCategory ec where (ec.deleted=false or ec.deleted is null)" +
                " and ec.parent.objectID=?", categoryId);
    }

    public List<EdsCourse> getCourses(Integer categoryId) {
        return find("from EdsCourse ec where (ec.deleted=false or ec.deleted is null) " +
                " and ec.category.objectID = ?", categoryId);
    }

    public List<EdsCourse> getCourses(Integer categoryId, Integer employeeId) {
        return find("select course from EdsCourseRegistration cr where " +
                " cr.course.category.objectID=? and cr.employee.objectID=?", categoryId, employeeId);
    }

    public EdsCategory getCategoryById(Integer categoryId) {
        return (EdsCategory) findSingle("from EdsCategory ec where (ec.deleted=false or ec.deleted is null) " +
                " and ec.objectID=?", categoryId);
    }
}
