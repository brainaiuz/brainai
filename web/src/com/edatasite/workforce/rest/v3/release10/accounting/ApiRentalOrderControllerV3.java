package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalOrderDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalOrderRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiRentalOrderService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental Orders", description = "Collection of public APIs for Rental Orders")
@RestController
@RequestMapping(value = "/rentalOrder", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.ALL_VALUE})
public class ApiRentalOrderControllerV3 implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiRentalOrderControllerV3.class);

    private final ApiRentalOrderService apiRentalOrderService;

    public ApiRentalOrderControllerV3(ApiRentalOrderService apiRentalOrderService) {
        this.apiRentalOrderService = apiRentalOrderService;
    }

    // get a list
    @Operation(summary = "Get rental order list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<RentalOrderDto>> getList(@RequestBody ListParamsDTO params) {
        log.info("REST request to get rental orders list");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.RentalOrdersListPanel);
        return ResultTO.success(apiRentalOrderService.getRentalOrderList(fp));
    }

    // get an available product list
    @Operation(summary = "Get an available product list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
    @RequestMapping(value = "/products", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<ItemTO>> getProducts(@RequestBody ItemTO params) throws RestException {
        log.info("REST request to get an available product list");
        return ResultTO.success(apiRentalOrderService.getAvailableProducts(params));
    }

    // get by id
    @Operation(summary = "Get existing rental order by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResultTO<RentalOrderDto> get(@PathVariable final Integer id) throws RestException {
        log.info("REST request to get rental order by id: {}", id);
        return ResultTO.success(apiRentalOrderService.getById(id));
    }

    // delete by id
    @Operation(summary = "Delete existing rental order by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable final Integer id) {
        log.info("REST request to delete product by id: {}", id);
        apiRentalOrderService.deleteRentalOrder(id);
        return ResultTO.success();
    }

    // create
    @Operation(summary = "Create new rental order")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<RentalOrderDto> create(@Validated(RentalOrderRequest.Create.class) @RequestBody RentalOrderRequest req) throws RestException {
        log.info("REST request to create rental order");
        return ResultTO.success(apiRentalOrderService.save(req));
    }

    // update
//    @Operation(summary = "Update rental order")
//    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "RentalOrders"))
//    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//    public ResultTO<RentalOrderDto> updateRentalOrder(@Validated(RentalOrderRequest.Update.class) @RequestBody RentalOrderRequest req) throws RestException {
//        log.info("REST request to update rental order");
//        for (RentalItemRequest item : req.getRentalItems()) {
//            if (!item.getFrom().before(item.getTo()) && !item.getFrom().equals(item.getTo())) {
//                throw new RestException("Invalid time range", "from must be before to", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
//            }
//        }
//        return ResultTO.success(apiRentalOrderService.update(req));
//    }

}
