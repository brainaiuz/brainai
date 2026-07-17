package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;
import com.edatasite.workforce.gwt.core.server.db.CFItemTableSettingmanager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CFItemTableSettingManagerImpl extends BaseManager<EdsCFItemTableSetting> implements CFItemTableSettingmanager {

    public CFItemTableSettingManagerImpl() {
        super(EdsCFItemTableSetting.class);
    }


    @Override
    public EdsCFItemTableSetting findByName(String formID, String label) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cf.* from " + getCompanyId() + ".form_item_table_setting cf ");
        sql.append(" where cf.form_id='" + formID + "'");
        sql.append(" and lower(cf.name)='" + label.toLowerCase() + "'");

        return (EdsCFItemTableSetting) findNativeSingle(sql.toString(), EdsCFItemTableSetting.class);
    }

    @Override
    public List<EdsCFItemTableSetting> findByFormId(String formID) {
        return (List<EdsCFItemTableSetting>) find("select cf from EdsCFItemTableSetting cf where cf.customForm=?", formID);
    }

    @Override
    public EdsCFItemTableSetting findByUUID(String uuid) {
        return (EdsCFItemTableSetting) findSingle("select cf from EdsCFItemTableSetting cf where cf.uuid=?", uuid);
    }

    @Override
    public ArrayList<String> getNameByFormId(String formId, Integer companyId) {
        return (ArrayList<String>) findNative("select cf.name from " + "\"" + companyId + "\"." + "form_item_table_setting" + " cf where cf.form_id=?", formId);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".form_item_table_setting cf where cf.uuid = '" + uuid + "'");
    }
}
