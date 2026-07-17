package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTable;

import java.util.List;

public interface PlacementItemTableManager extends Manager<EdsPlacementItemTable> {
    List<EdsPlacementItemTable> findByUuid(Integer id, String uuid);

    List<EdsPlacementItemTable> getByPlacementId(Integer id);

    void deleteByUUID(String uuid);
}
