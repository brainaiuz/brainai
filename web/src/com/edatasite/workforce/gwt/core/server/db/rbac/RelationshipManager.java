package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jan 24, 2010
 * Time: 3:13:13 AM
 */
public interface RelationshipManager extends Manager<EdsRelationship> {
    EdsRelationship getRelationship(String code);

    List<EdsRelationship> getChildRelations(String parentCode);
}
