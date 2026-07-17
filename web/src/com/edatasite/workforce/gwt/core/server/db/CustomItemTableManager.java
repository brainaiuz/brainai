package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;

import java.util.List;
import java.util.Map;

public interface CustomItemTableManager extends Manager<EdsCustomItemTable> {

    Map<Integer, EdsCustomItemTable> findAllByIds(List<Integer> ids);

    List<EdsCustomItemTable> findByUuid(Integer id, String uuid);

    void deleteItems(Integer itemObjectID);

    void deleteByUUID(String uuid);
}
