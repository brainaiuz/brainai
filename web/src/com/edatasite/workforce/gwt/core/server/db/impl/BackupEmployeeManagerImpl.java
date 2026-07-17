package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBackupEmployee;
import com.edatasite.workforce.gwt.core.server.db.BackupEmployeeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("backupEmployeeManager")
public class BackupEmployeeManagerImpl extends BaseManager<EdsBackupEmployee> implements BackupEmployeeManager {
    public BackupEmployeeManagerImpl() {
        super(EdsBackupEmployee.class);
    }


    @Override
    public List<EdsBackupEmployee> getBackupEmployeesBySickRequestId(Integer id) {
        return (List<EdsBackupEmployee>) find("select b from EdsBackupEmployee b where b.sickRequest = " + id);
    }

    @Override
    public void deleteBySickRequestId(Integer sickRequestId) {
        update("delete from EdsBackupEmployee b where b.sickRequest =  " + sickRequestId);
    }

    @Override
    public List<EdsBackupEmployee> getChildrensByParentId(Integer id) {
        return (List<EdsBackupEmployee>) find("select b from EdsBackupEmployee b where parentid = " + id);
    }

    @Override
    public void deleteByBackupsEmployeeId(Integer id) {
        update("delete from EdsBackupEmployee b where b.backupsEmployees =  " + id);
    }

}
