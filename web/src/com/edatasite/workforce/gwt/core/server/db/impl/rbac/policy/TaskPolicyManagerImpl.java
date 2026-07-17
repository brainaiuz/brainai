package com.edatasite.workforce.gwt.core.server.db.impl.rbac.policy;

import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.policy.EdsTaskPolicy;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.RelationshipManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.policy.TaskPolicyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Mar 4, 2010
 * Time: 4:54:16 PM
 */
@Repository("taskPolicyManager")
public class TaskPolicyManagerImpl extends BaseManager<EdsTaskPolicy> implements TaskPolicyManager {
    public TaskPolicyManagerImpl() {
        super(EdsTaskPolicy.class);
    }

    @Autowired
    private RelationshipManager relationshipManager;

    public EdsTaskPolicy getCompanyDirectRelationPolicy(String relationshipCode) {
        return (EdsTaskPolicy) findSingle("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.relation.relationType = ? AND tp.relation.code = ?", EdsTaskPolicy.BUILT_IN, EdsRelationship.DIRECT, relationshipCode);
    }

    public EdsTaskPolicy getCompanyRelationPolicy(String code) {
        return (EdsTaskPolicy) findSingle("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.relation.code = ?", EdsTaskPolicy.BUILT_IN,code);
    }

    public List<EdsTaskPolicy> getCompanyIndirectRelationPolicies() {
        return (List<EdsTaskPolicy>) find("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.relation.relationType = ? AND tp.trustee <> NULL",EdsTaskPolicy.BUILT_IN, EdsRelationship.INDIRECT);
    }

    public List<EdsTaskPolicy> getCompanyPolicies() {
        return (List<EdsTaskPolicy>) find("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ?",EdsTaskPolicy.BUILT_IN);
    }

    public List<EdsTaskPolicy> getCompanyPolicyIndirect() {
        return (List<EdsTaskPolicy>) find("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.trustee IS NOT NULL ",EdsTaskPolicy.BUILT_IN);
    }

    public List<EdsTaskPolicy> getCompanyPolicyDirect() {
        return (List<EdsTaskPolicy>) find("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.trustee IS NULL",EdsTaskPolicy.BUILT_IN);
    }

    public EdsTaskPolicy getTaskPolicyByTrustee(EdsTrustee trustee) {
        return (EdsTaskPolicy) findSingle("SELECT tp FROM EdsTaskPolicy tp WHERE tp.entryType = ? AND tp.trustee=?", EdsTaskPolicy.BUILT_IN, trustee);
    }
}
