package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.rbac.EdsAssessmentIndexRbac;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.AssessmentIndexRbacManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * User: Abdulaziz
 * Date: Nov 17, 2009
 * Time: 9:28:01 PM
 */
@Repository("assessmentIndexRbacManager")
public class AssessmentIndexRbacManagerImpl extends BaseManager<EdsAssessmentIndexRbac> implements AssessmentIndexRbacManager {
    public AssessmentIndexRbacManagerImpl() {
        super(EdsAssessmentIndexRbac.class);
    }


    /**
     * Creates new Rbac index for given assessment
     *
     * @param assessment
     * @param user
     * @param company
     * @param permission
     * @param employeeAssessment
     * @return
     */
    public EdsAssessmentIndexRbac createAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission, EdsEmployeeAssessment employeeAssessment) {
        EdsAssessmentIndexRbac assessmentIndex = new EdsAssessmentIndexRbac();
        assessmentIndex.setAssessment(assessment);
//        assessmentIndex.setCompany(company);
        assessmentIndex.setUser(user);
        assessmentIndex.setPermission(permission);
        assessmentIndex.setEmployeeassessment(employeeAssessment);
        create(assessmentIndex);
        return assessmentIndex;

    }

    public EdsAssessmentIndexRbac updateAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission) {
        return updateAssessmentIndex(assessment, user, company, permission, null);
    }

    /**
     * It updates Assessment's Rbac index or creates new one if index does not exist
     *
     * @param assessment
     * @param user
     * @param company
     * @param permission
     * @param employeeAssessment
     * @return
     */
    public EdsAssessmentIndexRbac updateAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company, int permission, EdsEmployeeAssessment employeeAssessment) {
        EdsAssessmentIndexRbac assessmentIndex = getAssessmentIndex(assessment, user, company);
        if (assessmentIndex != null) {
            assessmentIndex.setPermission(permission);
            return assessmentIndex;
        } else {
            return createAssessmentIndex(assessment, user, company, permission, employeeAssessment);
        }
    }

    /**
     * Returns given assessment index for given user of given company
     *
     * @param assessment
     * @param user
     * @param company
     * @return
     */
    public EdsAssessmentIndexRbac getAssessmentIndex(EdsAssessment assessment, EdsUser user, EdsCompany company) {
        return (EdsAssessmentIndexRbac) findSingle("SELECT asi FROM EdsAssessmentIndexRbac asi WHERE asi.user = ? AND asi.assessment = ?", user, assessment);
    }

    /**
     * removes given assessment from RBAC index
     *
     * @param assessment
     * @param company
     */
    public void removeAssessmentIndex(EdsAssessment assessment, EdsCompany company) {
        List<EdsAssessmentIndexRbac> assessmentIndex = getAssessmentIndexes(assessment, company);
        for (EdsAssessmentIndexRbac asI : assessmentIndex) {
            delete(asI);
        }
    }

    public void indexAssessment(EdsAssessment assessment, Set<EdsEmployeeAssessment> employeeAssessments, EdsCompany company) {
        removeAssessmentIndex(assessment, company);
        for (EdsEmployeeAssessment empAssessment : employeeAssessments) {
            if (empAssessment.getCollaborator() != null) {
                updateAssessmentIndex(assessment, empAssessment.getCollaborator(), company, EdsAssessmentIndexRbac.READ, empAssessment);
            }
        }
        if (assessment.getInitiator() != null) {
            updateAssessmentIndex(assessment, assessment.getInitiator(), company, EdsAssessmentIndexRbac.DELETE);
        }
        if (assessment.getReviewer() != null) {
            updateAssessmentIndex(assessment, assessment.getReviewer(), company, EdsAssessmentIndexRbac.EDIT);
        }
        if (assessment.getKeyEmployeeAssessment() != null) {
            if (assessment.getKeyEmployeeAssessment().getEmployee() != null) {
                updateAssessmentIndex(assessment, assessment.getKeyEmployeeAssessment().getEmployee(), company, EdsAssessmentIndexRbac.EDIT, assessment.getKeyEmployeeAssessment());
            }
        }

    }

    /**
     * Assessment will be idnexed according to default permission policy
     * colloborators have permission to READ
     * Reviewers have permission to EDIT
     * Initiator has permission ot DELETE
     *
     * @param assessment
     */
    public void indexAssessment(EdsAssessment assessment) {
        if (assessment.getTemplate() != null && assessment.getInitiator().getCompany() != null) {
            EdsCompany company = assessment.getInitiator().getCompany();
            Set<EdsEmployeeAssessment> empAssessments = assessment.getEmployeeAssessments();
            indexAssessment(assessment, empAssessments, company);
        }
    }

    /**
     * returns all assessmentindexes
     *
     * @param assessment
     * @param company
     * @return
     */
    public List<EdsAssessmentIndexRbac> getAssessmentIndexes(EdsAssessment assessment, EdsCompany company) {
        return (List<EdsAssessmentIndexRbac>) find("SELECT asi FROM EdsAssessmentIndexRbac asi WHERE asi.assessment = ? AND asi.user IS NOT NULL", assessment);
    }

}
