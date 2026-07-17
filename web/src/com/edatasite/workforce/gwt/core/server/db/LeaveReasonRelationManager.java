package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLeaveReasonRelation;

import java.util.List;
import java.util.Map;

/**
 * @author Hurshid on 12/15/2018
 */
public interface LeaveReasonRelationManager extends Manager<EdsLeaveReasonRelation> {

    void deleteRelations(String reasonCode);

    List<EdsLeaveReasonRelation> getRelations(String reasonCode);

    Map<String, List<EdsLeaveReasonRelation>> getRelationsAsMap();
}
