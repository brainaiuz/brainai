package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.service.ApiNoteService;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.note.NoteCreateTO;
import com.edatasite.workforce.rest.v3.release10.core.to.note.NoteTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/notes", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiNoteControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiNoteControllerV3.class);

    private final ApiNoteService apiNoteService;

    @Autowired
    public ApiNoteControllerV3(ApiNoteService apiNoteService) {
        this.apiNoteService = apiNoteService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Object> createNote(@Validated @RequestBody NoteCreateTO request) throws RestException {
        NoteTO note = apiNoteService.createNote(request);
        return ResultTO.success(note);
    }
}
