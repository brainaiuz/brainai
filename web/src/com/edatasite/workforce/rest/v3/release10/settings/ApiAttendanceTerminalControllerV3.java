package com.edatasite.workforce.rest.v3.release10.settings;

import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.settings.service.ApiAttendanceTerminalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Attendance Terminals", description = "Attendance Terminal Public API")
@RestController
@RequestMapping(path = "/attendance-terminal", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiAttendanceTerminalControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiAttendanceTerminalControllerV3.class);

    private final ApiAttendanceTerminalService apiAttendanceTerminalService;

    public ApiAttendanceTerminalControllerV3(ApiAttendanceTerminalService apiAttendanceTerminalService) {
        this.apiAttendanceTerminalService = apiAttendanceTerminalService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAttendanceTerminals(@RequestParam(value = "uuid", required = false) List<String> uuids,
                                                         @RequestParam(value = "status", required = false) String status) {
        log.info("REST request to get attendance terminals");
        List<AttendanceTerminal> attendanceTerminals = apiAttendanceTerminalService.getAttendanceTerminals(uuids, status);
        return ResponseEntity.ok(attendanceTerminals);
    }
}
