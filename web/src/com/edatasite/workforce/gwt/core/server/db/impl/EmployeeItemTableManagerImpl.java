package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;
import com.edatasite.workforce.gwt.core.server.db.EmployeeItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeItemTableManagerImpl extends BaseManager<EdsEmployeeCustomItemTable> implements EmployeeItemTableManager {

    public EmployeeItemTableManagerImpl() {
        super(EdsEmployeeCustomItemTable.class);
    }


    @Override
    public List<EdsEmployeeCustomItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsEmployeeCustomItemTable>) find("select t from EdsEmployeeCustomItemTable t where t.employee.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".employee_item_table cf where cf.uuid = '" + uuid + "'");
    }
}