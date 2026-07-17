package com.edatasite.workforce.gwt.core.server.db.rbac.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderPolicy;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Sherali
 * Date: 29.05.2010
 * Time: 12:55:48
 */
public interface FolderPolicyManager extends Manager<EdsFolderPolicy> {

    EdsFolderPolicy getCompanyRelationPolicy(String relationshipCode);

    List<EdsFolderPolicy> getCompanyIndirectRelationPolicies();

    EdsFolderPolicy getFolderPolicyForTrustee(EdsCompany company, EdsTrustee trustee);
}