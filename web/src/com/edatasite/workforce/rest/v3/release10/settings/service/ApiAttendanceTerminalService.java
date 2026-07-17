package com.edatasite.workforce.rest.v3.release10.settings.service;

import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiAttendanceTerminalService {
    private final AttendanceTerminalManager attendanceTerminalManager;

    public ApiAttendanceTerminalService(AttendanceTerminalManager attendanceTerminalManager) {
        this.attendanceTerminalManager = attendanceTerminalManager;
    }

    public List<AttendanceTerminal> getAttendanceTerminals(List<String> uuids, String status) {
        return attendanceTerminalManager.getAll(uuids, status)
                .stream()
                .map(t -> {
                    AttendanceTerminal dto = new AttendanceTerminal();
                    dto.setId(t.getObjectID());
                    dto.setCompanyUniqueID(t.getCompanyUniqueID());
                    dto.setCompanyBranchName(t.getCompanyBranchName());
                    dto.setDynamicStatus(t.getDynamicStatus());
                    dto.setLocationId(t.getLocationId());
                    dto.setExternalId(t.getExternalId());
                    if (t.getLocation() != null) {
                        dto.setLocation(t.getLocation().getAsSelectItem());
                        dto.setLatitude(t.getLocation().getLatitude());
                        dto.setLongitude(t.getLocation().getLongitude());
                        dto.setRadius(t.getLocation().getRadius());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
