package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.RelationshipManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jan 24, 2010
 * Time: 3:14:56 AM
 */
@Repository("relationshipManager")
public class RelationshipManagerImpl extends BaseManager<EdsRelationship> implements RelationshipManager {
    public RelationshipManagerImpl() {
        super(EdsRelationship.class);
    }

    public EdsRelationship getRelationship(String code) {
        return (EdsRelationship) findSingle("SELECT rel FROM EdsRelationship rel WHERE rel.code = ?", code);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<EdsRelationship> getChildRelations(String parentCode) {
        return find("SELECT rel FROM EdsRelationship rel WHERE rel.parent.code = ?", parentCode);
    }
}
