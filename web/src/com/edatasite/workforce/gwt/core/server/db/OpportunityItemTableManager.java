package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsOpportunityCustomItemTable;

import java.util.List;

public interface OpportunityItemTableManager extends Manager<EdsOpportunityCustomItemTable> {

    List<EdsOpportunityCustomItemTable> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);

}