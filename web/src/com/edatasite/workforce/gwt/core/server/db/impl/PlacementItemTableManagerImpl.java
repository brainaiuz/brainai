package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTable;
import com.edatasite.workforce.gwt.core.server.db.PlacementItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlacementItemTableManagerImpl extends BaseManager<EdsPlacementItemTable> implements PlacementItemTableManager {


    public PlacementItemTableManagerImpl() {
        super(EdsPlacementItemTable.class);
    }

    @Override
    public List<EdsPlacementItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsPlacementItemTable>) find("select t from EdsPlacementItemTable t where t.placement.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public List<EdsPlacementItemTable> getByPlacementId(Integer id) {
        return (List<EdsPlacementItemTable>) find("select t from EdsPlacementItemTable t where t.placement.objectID=?", id);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".placementitemtable cf where cf.uuid = '" + uuid + "'");
    }
}
