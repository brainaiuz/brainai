package com.edatasite.workforce.gwt.core.server.db.rbac.policy;

import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.policy.EdsTaskPolicy;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Mar 4, 2010
 * Time: 4:28:49 PM
 */
public interface TaskPolicyManager extends Manager<EdsTaskPolicy> {
    List<EdsTaskPolicy> getCompanyIndirectRelationPolicies();
//    public EdsTaskPolicy getCompanyDirectRelationPolicy(EdsCompany company, EdsRelationship relationship);

    EdsTaskPolicy getCompanyDirectRelationPolicy(String relationshipCode);

    EdsTaskPolicy getCompanyRelationPolicy(String code);

//    public List<EdsTaskPolicy> getSystemBuiltinPolicies();

    List<EdsTaskPolicy> getCompanyPolicies();

    List<EdsTaskPolicy> getCompanyPolicyIndirect();

    List<EdsTaskPolicy> getCompanyPolicyDirect();

    EdsTaskPolicy getTaskPolicyByTrustee(EdsTrustee trustee);
}
