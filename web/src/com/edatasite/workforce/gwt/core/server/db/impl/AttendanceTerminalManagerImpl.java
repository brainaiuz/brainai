package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository("attendanceTerminalManager")
public class AttendanceTerminalManagerImpl extends BaseManager<EdsAttendanceTerminal> implements AttendanceTerminalManager {
    public AttendanceTerminalManagerImpl() {
        super(EdsAttendanceTerminal.class);
    }

    @Override
    public List<EdsAttendanceTerminal> getAll() {
        return find("from EdsAttendanceTerminal");
    }

    @Override
    public List<EdsAttendanceTerminal> getAll(String searchKey) {
        if (searchKey == null || searchKey.trim().isEmpty()) {
            return getAll();
        }
        String like = "%" + searchKey.trim().toLowerCase() + "%";
        return find("from EdsAttendanceTerminal t where lower(t.companyUniqueID) like ? or lower(t.companyBranchName) like ?", like, like);
    }

    @Override
    public List<EdsAttendanceTerminal> getAll(List<String> uuids, String status) {
        String sql = "select at from EdsAttendanceTerminal at";
        sql += " left join at.location l ";
        sql += " where 1 = 1";
        if (uuids != null && !uuids.isEmpty()) {
            sql += " and at.companyUniqueID in (" + uuids.stream().map(uuid -> "'" + uuid + "'").collect(Collectors.joining(",")) + ")";
        }
        if (status != null) {
            sql += " and (at.status is null or at.status = '" + status + "')";
        }
        return find(sql);
    }
}
