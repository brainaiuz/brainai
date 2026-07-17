package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBackupEmployee;

import java.util.List;

public interface BackupEmployeeManager extends Manager<EdsBackupEmployee> {

    List<EdsBackupEmployee> getBackupEmployeesBySickRequestId(Integer id);

    void deleteBySickRequestId(Integer sickRequestId);

    List<EdsBackupEmployee> getChildrensByParentId(Integer id);

    void deleteByBackupsEmployeeId(Integer id);

}
