package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.gwt.core.server.db.CustomItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomItemTableManagerImpl extends BaseManager<EdsCustomItemTable> implements CustomItemTableManager {

    public CustomItemTableManagerImpl() {
        super(EdsCustomItemTable.class);
    }

    @Override
    public Map<Integer, EdsCustomItemTable> findAllByIds(List<Integer> ids) {
        List<EdsCustomItemTable> tables = get(ids);
        if (tables == null || tables.size() == 0) {
            return new HashMap<>();
        }
        return tables.stream()
                .collect(Collectors.toMap(EdsCustomItemTable::getObjectID, v -> v, (o, n) -> o));
    }

    @Override
    public List<EdsCustomItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsCustomItemTable>) find("select t from EdsCustomItemTable t where t.formItem.objectID=? and t.uuid=? order by sorder ", id, uuid);
    }

    @Override
    public void deleteItems(Integer itemObjectID) {
        update("DELETE FROM EdsCustomItemTable it WHERE it.formItem.objectID = ?", itemObjectID);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".custom_item_table cf where cf.uuid = '" + uuid + "'");
    }
}
