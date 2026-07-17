package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CourseManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Feb 24, 2009
 * Time: 4:46:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseManager")
public class CourseManagerImpl extends BaseManager<EdsCourse> implements CourseManager {
    public CourseManagerImpl() {
        super(EdsCourse.class);
    }

    public List<EdsCourse> getAllCourses() {
        return find("from EdsCourse ec");
    }

    public List<EdsCourse> getMyCourses(Integer employeeID) {
        return find("select cr.course from EdsCourseRegistration cr where cr.employee.objectID=?", employeeID);
    }

    public EdsCourse getCourseById(Integer courseId) {
        return (EdsCourse) findSingle("from EdsCourse ec where (ec.deleted=false or ec.deleted is null)" +
                " and ec.objectID=?", courseId);
    }

    @Override
    public List<EdsCourse> getCourseList(String Ids) {
        return find("select ec from EdsCourse ec where (ec.deleted=false or ec.deleted is null) and ec.category.objectID in " + Ids);
    }

    public List<EdsCourse> searchCourses(String searchKey) {
        searchKey = "%" + searchKey + "%";
        return find("from EdsCourse ec where (ec.deleted=false or ec.deleted is null)" +
                " and ec.name like ?", searchKey);
    }

    public boolean isMyCourse(Integer courseId) {
        Long i = (Long) findSingle("select count(*)  from EdsCourseRegistration cr where cr.course.objectID=? " +
                "and cr.employee.objectID=?", courseId, getUser().getObjectID());
        return i != 0;
    }

    public List<EdsCourse> list(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder("select c from EdsCourse c ");
        sql.append("left join c.subject subject ");
        sql.append("where c.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(c.number) like '").append(fp.getSqlSearchKey()).append("' or");
            sql.append(" lower(c.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CourseItem.NUMBER.equals(fp.getSortField())) {
                sql.append("c.number");
            } else if (CourseItem.NAME.equals(fp.getSortField())) {
                sql.append("c.name");
            } else if (CourseItem.VALIDITY.equals(fp.getSortField())) {
                sql.append("c.validity");
            } else if (CourseItem.DURATION.equals(fp.getSortField())) {
                sql.append("c.duration");
            } else if (CourseItem.SUBJECT.equals(fp.getSortField())) {
                sql.append("subject.name");
            } else {
                sql.append(" c.lastUpdateTime desc");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" c.id  desc");

        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public Integer getCourseTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(c.objectID) from EdsCourse c ");
        sql.append("left join c.subject subject ");
        sql.append("where c.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(c.number) like '").append(fp.getSqlSearchKey()).append("' or");
            sql.append(" lower(c.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    public List<Integer> getInstructorCoursesIds(Integer instructorID) {
        List<Integer> selectedCoursesIds = new ArrayList<>();
        if (instructorID != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ic.course_id FROM ").append(getCompanyId()).append(".course_instructor ic \n");
            sql.append("WHERE ic.instructor_id = ").append(instructorID);
            selectedCoursesIds = (List<Integer>) findNative(sql.toString());
        }

        return selectedCoursesIds;
    }

    public void deleteInstructorInCourses(Integer instructorID) {
        if (instructorID != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(getCompanyId()).append(".course_instructor ic \n");
            sql.append("WHERE ic.instructor_id = ").append(instructorID);
            updateNative(sql.toString());
        }
    }

    public Integer getCourseLastIntNumber() {
        return (Integer) findSingle("select v.intNumber from EdsCourse v order by v.intNumber desc");
    }

    @Override
    public SelectItem[] getCoursesAsSelectItems(ListingFilterParameter filterParameter) {
        List<EdsCourse> courses = list(filterParameter);
        if (courses != null && courses.size() > 0) {
            List<SelectItem> courseList = new ArrayList<>();
            for (EdsCourse course : courses) {
                courseList.add(course.getAsSelectItem());
            }

            return courseList.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    public List<EdsProductCategory> getCourseProductCategorisList(String caurseIds) {
        return find("SELECT DISTINCT c.category FROM EdsCourse c WHERE c.objectID IN (" + caurseIds + ")");
    }

    @Override
    public List<EdsCourse> getByProductIdsCourseList(String productCategoriesIds) {
        return find("SELECT c FROM EdsCourse c WHERE c.category.objectID IN (" + productCategoriesIds + ")");
    }

    @Override
    public Map<Integer, ArrayList<String>> getPreRequisiteCourseNameMap(String courseIds) {
        List<Object[]> preRequisiteCourseObjects = findNative("SELECT cp.course_id,i.name FROM " + getCompanyId() + ".course_preRequisites cp " +
                " INNER JOIN " + getCompanyId() + ".course c  ON c.id=cp.preRequisite_id " +
                " INNER JOIN " + getCompanyId() + ".item i ON i.id=c.id " +
                " WHERE cp.course_id IN (" + courseIds + ")" +
                " GROUP BY cp.course_id,i.name ");
        Map<Integer, ArrayList<String>> preRequisiteMap = new HashMap<>();
        for (Object[] objects : preRequisiteCourseObjects) {
            Integer courseId = (Integer) objects[0];
            String preRequisteCourseName = (String) objects[1];
            if (!preRequisiteMap.containsKey(courseId)) {
                preRequisiteMap.put(courseId, new ArrayList<>());
            }
            preRequisiteMap.get(courseId).add(preRequisteCourseName);
        }
        return preRequisiteMap;
    }

    @Override
    public Map<Integer, String> getCourseOtherPrerequisite(String courseIds) {
        List<Object[]> courseOtherPreRequisite = findNative("select c.id,c.otherPreRequisites from " + getCompanyId() + ".course c where c.id in (" + courseIds + ")");
        Map<Integer, String> courseOtherPreRequisiteMap = new HashMap<>();
        for (Object[] objects : courseOtherPreRequisite) {
            Integer courseId = (Integer) objects[0];
            String otherPreRequisite = (String) objects[1];
            courseOtherPreRequisiteMap.put(courseId, otherPreRequisite);
        }
        return courseOtherPreRequisiteMap;
    }
}
