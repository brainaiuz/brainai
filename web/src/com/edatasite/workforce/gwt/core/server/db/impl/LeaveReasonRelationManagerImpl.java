package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLeaveReasonRelation;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonRelationManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hurshid on 12/15/2018
 */
@Repository("leaveReasonRelationManager")
public class LeaveReasonRelationManagerImpl extends BaseManager<EdsLeaveReasonRelation> implements LeaveReasonRelationManager {

    public LeaveReasonRelationManagerImpl() {
        super(EdsLeaveReasonRelation.class);
    }

    @Override
    public void deleteRelations(String reasonCode) {
        update("delete from EdsLeaveReasonRelation where reasonCode = ?", reasonCode);
    }

    @Override
    public List<EdsLeaveReasonRelation> getRelations(String reasonCode) {
        return findByNamedParams("select t from EdsLeaveReasonRelation t where t.reasonCode = :reasonCode", preparing(new Entry("reasonCode", reasonCode)));
    }

    @Override
    public Map<String, List<EdsLeaveReasonRelation>> getRelationsAsMap() {

        String query = "select lr.* ,0 as clazz_ from " + getCompanyId() + ".leave_reason_relation  lr " +
                "inner join " + getCompanyId() + ".leave_reason l on l.code=lr.reason_code " +
                "where l.isActive is true and l.deleted is false";
        List<EdsLeaveReasonRelation> list = findNative(query, EdsLeaveReasonRelation.class);

        Map<String, List<EdsLeaveReasonRelation>> relations = new HashMap<>();

        list.stream()
                .filter(x -> x.getReasonCode() != null)
                .forEach(x -> relations.computeIfAbsent(x.getReasonCode(), v -> new ArrayList<>()).add(x));

        return relations;
    }
}
