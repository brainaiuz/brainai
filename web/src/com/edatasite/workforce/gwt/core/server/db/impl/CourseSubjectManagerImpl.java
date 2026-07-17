package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSubject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CourseSubjectManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseSubjectItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 26.12.12
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseSubjectManager")
public class CourseSubjectManagerImpl extends BaseManager<EdsCourseSubject> implements CourseSubjectManager {

    public CourseSubjectManagerImpl() {
        super(EdsCourseSubject.class);
    }

    @Override
    public List<EdsCourseSubject> getParentCourseSubject(Integer objectId) {
        return find("SELECT c FROM EdsCourseSubject c WHERE (c.deleted is false OR c.deleted is null) AND c.parent is null " + (objectId != null ? "  AND c.objectID != " + objectId + " " : ""));
    }

    @Override
    public List<EdsCourseSubject> list(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder("select c from EdsCourseSubject c ");
        sql.append("where c.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(c.name) like '").append(fp.getSqlSearchKey()).append("') or");
            sql.append(" (lower(c.description) like '").append(fp.getSqlSearchKey()).append("')");
        }
        sql.append(" order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CourseSubjectItem.NAME.equals(fp.getSortField())) {
                sql.append("c.name");
            } else if (CourseSubjectItem.DESCRIPTION.equals(fp.getSortField())) {
                sql.append("c.description");
            } else if (CourseSubjectItem.PARENT.equals(fp.getSortField())) {
                sql.append("c.parent");
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

    @Override
    public Integer getCourseSubjectTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(c.objectID) from EdsCourseSubject c ");
        sql.append("where c.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(c.name) like '").append(fp.getSqlSearchKey()).append("') or");
            sql.append(" (lower(c.description) like '").append(fp.getSqlSearchKey()).append("')");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public Integer getCountParent(EdsCourseSubject parent) {
        return ((Long) findSingle("SELECT count(c.objectID) FROM EdsCourseSubject c WHERE c.parent = ? and c.deleted <> true", parent)).intValue();
    }

    @Override
    public void deleteChild(EdsCourseSubject child) {
        updateNative("UPDATE " + getCompanyId() + ".coursesubject SET deleted = true where id = " + child.getObjectID());
    }
}
