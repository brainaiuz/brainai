package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsProjectCustomItemTable;

import java.util.List;

public interface ProjectItemTableManager extends Manager<EdsProjectCustomItemTable> {
    List<EdsProjectCustomItemTable> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);
}
