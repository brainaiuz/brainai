package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsProjectCustomItemTable;
import com.edatasite.workforce.gwt.core.server.db.ProjectItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectItemTableManagerImpl extends BaseManager<EdsProjectCustomItemTable> implements ProjectItemTableManager {

    public ProjectItemTableManagerImpl() {
        super(EdsProjectCustomItemTable.class);
    }


    @Override
    public List<EdsProjectCustomItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsProjectCustomItemTable>) find("select t from EdsProjectCustomItemTable t where t.project.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".project_item_table cf where cf.uuid = '" + uuid + "'");
    }
}