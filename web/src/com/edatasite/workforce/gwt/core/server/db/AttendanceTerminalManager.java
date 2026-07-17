package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;

import java.util.List;

public interface AttendanceTerminalManager extends Manager<EdsAttendanceTerminal> {
    List<EdsAttendanceTerminal> getAll();

    List<EdsAttendanceTerminal> getAll(List<String> uuids, String status);

    List<EdsAttendanceTerminal> getAll(String searchKey);
}
