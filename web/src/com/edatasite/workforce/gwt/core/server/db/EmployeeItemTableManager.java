package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;

import java.util.List;

public interface EmployeeItemTableManager extends Manager<EdsEmployeeCustomItemTable> {

    List<EdsEmployeeCustomItemTable> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);

}