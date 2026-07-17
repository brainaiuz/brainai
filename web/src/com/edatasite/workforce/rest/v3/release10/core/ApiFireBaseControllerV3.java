package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.service.ApiFirebaseService;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Tag(name = "Firebase", description = "Firebase API")
@RestController
@RequestMapping(value = "/api/firebase", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiFireBaseControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiFireBaseControllerV3.class);

    private final ApiFirebaseService apiFirebaseService;

    @Autowired
    public ApiFireBaseControllerV3(ApiFirebaseService apiFirebaseService) {
        this.apiFirebaseService = apiFirebaseService;
    }

    @Operation(summary = "Upload Admin SDK JSON File")
    @RequestMapping(value = "/upload-credentials", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> saveCredentialFromJson(@RequestParam("file") MultipartFile file) throws RestException {
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Attachment file is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            apiFirebaseService.saveCredentialFromJson(file);
            return ResultTO.success("Credentials successfully saved");
        } catch (Exception exception) {
            throw new RestException(GENERAL_ERROR_MESSAGE, exception.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Generate Assertion Token And Access Token")
    @PostMapping(path = "/generate-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Map<String, String>> generateAssertionToken() throws RestException {
        log.info("REST request to generate assertion Token and Access Token");
        try {
            return ResultTO.success(apiFirebaseService.generateAccessToken());
        } catch (Exception exception) {
            throw new RestException(GENERAL_ERROR_MESSAGE, exception.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/send-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendNotification(@RequestParam("id") Integer id,
                                                 @RequestParam("action") String action,
                                                 @RequestParam(value = "column", required = false) String column) throws Exception {
        log.info("REST request to send notification");
        Map<String, String> authKeys = apiFirebaseService.generateAccessToken();
        apiFirebaseService.sendNotification(id, action, column, authKeys);
        return ResponseEntity.ok().build();
    }
}
