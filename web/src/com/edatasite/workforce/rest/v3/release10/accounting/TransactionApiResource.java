package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ManualEntryDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.StockAdjustmentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiTransactionService;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Normurod Buriev.
 * Date: 2/24/2021 7:08 PM
 */
@Tag(name = "Transaction Api Resource", description = "Here is a transaction api resouce that contains STOCK-ADJUSTMENT/STOCK-TRANSACTION/BANK-RECEIPT/BANK-PAYMENT... transactions")
@RestController
@RequestMapping(value = "/transaction", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class TransactionApiResource implements ApiConstants {

    @Autowired
    private ApiTransactionService transactionService;

    @Operation(summary = "Create a stock adjustment transaction")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Stock Adjustment"))
    @RequestMapping(path = "/stock-adjustment", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createStockAdjustment(@Validated @RequestBody StockAdjustmentDto request) throws RestException {
        if (request.getAccount().getId() == null && request.getAccount().getCode() == null) {
            throw new RestException(IN_VALID_DATA, "Adjustment Account Id/Code is required.", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(transactionService.createStockAdjustment(request));
    }

    @Operation(summary = "Delete a stock adjustment transaction")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Stock Adjustment"))
    @RequestMapping(path = "/stock-adjustment", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> deleteStockAdjustment(@RequestBody IdCode request) throws RestException {
        transactionService.deleteStockAdjustment(request);
        return ResponseEntity.ok("Success");
    }

    @Operation(summary = "Create manual entry")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(path = "/manual-entry", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ManualEntryDto> createManualEntry(@Validated @RequestBody ManualEntryDto request) throws RestException {
        if (request.getId() != null) {
            throw new RestException(IN_VALID_DATA, "Manual Entry Id is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        transactionService.createManualEntry(request);
        return ResultTO.success(request);
    }

    @Operation(summary = "Update manual entry")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(path = "/manual-entry", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ManualEntryDto> updateManualEntry(@Validated @RequestBody ManualEntryDto request) throws RestException {
        if (request.getId() == null) {
            throw new RestException(IN_VALID_DATA, "Manual Entry Id is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        transactionService.createManualEntry(request);
        return ResultTO.success(request);
    }

    @Operation(summary = "Create COGS transaction")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "COGS"))
    @RequestMapping(path = "/cogs-transaction", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<?> createCOGSTransaction(@Validated @RequestBody ManualEntryDto request) throws RestException {
        if (request.getId() != null) {
            throw new RestException(IN_VALID_DATA, "Manual Entry Id is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        transactionService.createManualEntry(request);
        return ResultTO.success();
    }
}
