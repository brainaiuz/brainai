package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGrade;

import java.util.List;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 10:39:41 AM
 */
public interface GradeManager extends Manager<EdsGrade> {

    List<EdsGrade> getGradeListByCompany(Integer companyId);

    void deleteGrade(EdsGrade grade);
}