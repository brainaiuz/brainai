package com.edatasite.workforce.gwt.core.server.db.impl.rbac.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderPolicy;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderPolicyManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Sherali
 * Date: 29.05.2010
 * Time: 12:58:21
 */
@Repository("folderPolicyManager")
public class FolderPolicyManagerImpl extends BaseManager<EdsFolderPolicy> implements FolderPolicyManager {
    public FolderPolicyManagerImpl() {
        super(EdsFolderPolicy.class);
    }

    @Override
    public EdsFolderPolicy getCompanyRelationPolicy(String relationshipCode) {
        return (EdsFolderPolicy) findSingle("SELECT tp FROM EdsFolderPolicy tp WHERE tp.entryType = ? AND tp.relation.code = ?",EdsFolderPolicy.BUILT_IN, relationshipCode);
    }

    @Override
    public List<EdsFolderPolicy> getCompanyIndirectRelationPolicies() {
        return (List<EdsFolderPolicy>) find("SELECT tp FROM EdsFolderPolicy tp WHERE tp.entryType = ? AND tp.relation.relationType = ? AND tp.trustee <> NULL",EdsFolderPolicy.BUILT_IN, EdsRelationship.INDIRECT);
    }

    public EdsFolderPolicy getFolderPolicyForTrustee(EdsCompany company, EdsTrustee trustee){
        return (EdsFolderPolicy) findSingle("SELECT tp FROM EdsFolderPolicy tp WHERE tp.entryType = ? AND tp.trustee = ?",EdsFolderPolicy.BUILT_IN, trustee);
    }
}