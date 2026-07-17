package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class ApiHealthCheckControllerV2 extends BaseApiControllerV2 {

    @RequestMapping(value = "/health", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDTO> checkHealth() {
        try {
            return ResponseEntity.ok(new StatusDTO("UP"));
        } catch (Exception e) {
            return ResponseEntity.ok(new StatusDTO("DOWN"));
        }
    }

    class StatusDTO extends ResponseData {
        String status;

        StatusDTO(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
