package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ShiftSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("shiftSettingsManager")
public class ShiftSettingsManagerImpl extends BaseManager<EdsShiftSettings> implements ShiftSettingsManager {
    public ShiftSettingsManagerImpl() {
        super(EdsShiftSettings.class);
    }

    public List<EdsShiftSettings> getShiftSettings(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select sh from EdsShiftSettings sh where sh.deleted<>true ");
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(sh.name) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("' or ");
            sql.append(" lower(sh.description) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("') ");
        }
        if (fp != null && fp.isLookUp()) {
            sql.append(" and sh.shortName is not null ");
        }
        sql.append(" ORDER BY ");
        if (fp != null && TimeslotItem.NAME.equals(fp.getSortField())) {
            sql.append("name");
        } else if (fp != null && TimeslotItem.DESCRIPTION.equals(fp.getSortField())) {
            sql.append("description");
        } else {
            sql.append("name");
        }
        if (fp != null && !fp.isAscending()) {
            sql.append(" DESC ");
        }
        return findByNamedParams(sql.toString(), params);
    }
}
