package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;

import java.util.ArrayList;
import java.util.List;

public interface CFItemTableSettingmanager extends Manager<EdsCFItemTableSetting> {

    EdsCFItemTableSetting findByName(String formID, String label);

    List<EdsCFItemTableSetting> findByFormId(String formID);

    EdsCFItemTableSetting findByUUID(String uuid);

    ArrayList<String> getNameByFormId(String formId, Integer companyId);

    void deleteByUUID(String uuid);
}
