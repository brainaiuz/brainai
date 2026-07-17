package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGrade;
import com.edatasite.workforce.gwt.core.server.db.GradeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:41:43 AM
 */
@Repository("gradeManager")
public class GradeManagerImpl extends BaseManager<EdsGrade> implements GradeManager {
    public GradeManagerImpl() {
        super(EdsGrade.class);
    }

    public List<EdsGrade> getGradeListByCompany(Integer companyID) {
        String companyId = "\"" + companyID + "\"";
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct g.* from ").append(companyId).append(".grade as g ");
        sql.append(" where ");
        sql.append(" g.deleted<>true ");
        sql.append(" order by g.gradecode asc");
        return findNative(sql.toString(), EdsGrade.class);
    }

    public void deleteGrade(EdsGrade grade) {
        update("update EdsGrade g set g.deleted = true  where g=? and g.deleted<>true", grade);
    }
}