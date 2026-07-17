package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.rbac.EdsAssessmentIndexRbac;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Nov 17, 2009
 * Time: 9:17:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AssessmentIndexRbacManager extends Manager<EdsAssessmentIndexRbac> {

    EdsAssessmentIndexRbac createAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission, EdsEmployeeAssessment employeeAssessment);

    EdsAssessmentIndexRbac updateAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission);

    EdsAssessmentIndexRbac updateAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission, EdsEmployeeAssessment employeeAssessment);

    EdsAssessmentIndexRbac getAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company);

    void removeAssessmentIndex(EdsAssessment assessment, EdsCompany company);

    void indexAssessment(EdsAssessment assessment, Set<EdsEmployeeAssessment> employeeAssessments, EdsCompany company);

    void indexAssessment(EdsAssessment assessment);

    List<EdsAssessmentIndexRbac> getAssessmentIndexes(EdsAssessment assessment, EdsCompany company);

}
