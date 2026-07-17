package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.EventDto;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

/**
 * User: Abror Abdukadirov
 * Date: 12.02.2020 19:30
 */
@Tag(name = "Activity", description = "Activity Public API")
@RestController
@RequestMapping(value = "/event", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiActivityControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiActivityControllerV3.class);

    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ApiActivityService apiActivityService;
    @Autowired
    private EventManager eventManager;

    @RequestMapping(
            value = "/next-activities",
            method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}
    )
    public Object getNextEventList() throws RestException {
        try {
            return successResponse(crmServiceLocal.getNextEventList());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get event list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Events"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<EventDto>> getEvents(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.EventsListPanel);

        return ResultTO.success(apiActivityService.getEventList(fp));
    }

    @Operation(summary = "Get existing event by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "product"))
    @RequestMapping(path = "/{eventId}", method = RequestMethod.GET)
    public ResultTO<EventDto> getEventById(@PathVariable final Integer eventId) throws RestException {

        return ResultTO.success(apiActivityService.getEventById(eventId));
    }

    @Operation(summary = "Create new event")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Event"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<EventDto> createEvent(@Validated @RequestBody EventDto event) throws RestException {
        if (event.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        apiActivityService.save(event, true);
        return ResultTO.success(event);
    }

    @Operation(summary = "Edit an event")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Event"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<EventDto> updateEvent(@Validated @RequestBody EventDto event) throws RestException {
        if (event.getId() == null || event.getId() < 1) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        apiActivityService.save(event, false);
        return ResultTO.success(event);
    }

    @Operation(summary = "Delete existing event by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Event"))
    @RequestMapping(path = "/{eventId}", method = RequestMethod.DELETE)
    public Object deleteEvent(@PathVariable final Integer eventId) throws RestException {
        Optional.ofNullable(eventManager.get(eventId)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(eventId);
        crmServiceLocal.deleteEvent(ids);
        return ResultTO.success();
    }

    @Operation(summary = "Patch Update existing event")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Event"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO patchUpdateEvent(@RequestBody EventDto event) throws RestException {
        if (event.getId() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        apiActivityService.savePatch(event);
        return ResultTO.success();
    }
}
