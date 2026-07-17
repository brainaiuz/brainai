package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductRequestListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.AdjustmentItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.AdjustmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.StockTransferItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.StockTransferTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.TransferTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse.WarehouseListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductLocationDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anvar Akramov on 26/3/2018.
 */
@Tag(name = "Warehouse", description = "Warehouse API")
@RestController()
@RequestMapping(headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiWarehouseControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiWarehouseControllerV2.class);

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
//    @Autowired
//    private AccountingService accountingService;


    @Operation(summary = "Get Warehouse List", description = "Retrieves list of warehouses based on provided search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of warehouses."),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/warehouses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getWarehouseList(@RequestBody RequestListSearchData requestListData) throws RestException {
        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getStart().equals(requestListData.getLimit()) && requestListData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListData.getStart());
        filterParameter.setLimit(requestListData.getLimit());
        filterParameter.setSearchKey(requestListData.getSearch_text());

        try {
            ListResult<WarehouseItem> warehousesList = accountingServiceLocal.getWarehousesList(filterParameter);

            ArrayList<WarehouseListItemTO> warehouseListResult = new ArrayList<>();

            for (WarehouseItem item : warehousesList.getList()) {
                WarehouseListItemTO warehouse = new WarehouseListItemTO();
                warehouse.setId(item.getObjectID());
                warehouse.setName(item.getName());
                warehouseListResult.add(warehouse);
            }
            return successResponse(new ResponseResultListData<>(warehouseListResult, warehousesList.getTotal()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create Warehouse", description = "Create Warehouse")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/warehouse/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createWarehouse(@RequestBody WarehouseListItemTO warehouseListItemTO) throws RestException {

        if (StringUtils.isBlank(warehouseListItemTO.getName())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Warehouse name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        WarehouseItem newWarehouse = convertWarehouse(warehouseListItemTO);

        //CREATE Warehouse
        Integer id = accountingServiceLocal.saveWarehouse(newWarehouse);

        if (id != null && id > 0) {
            return successResponse(new WarehouseTO(id, warehouseListItemTO.getName()));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Warehouse", description = "Update Warehouse")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if updated or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/warehouse/update", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateWarehouse(@RequestBody WarehouseListItemTO warehouseListItemTO) throws RestException {

        if (warehouseListItemTO.getId() == null || warehouseListItemTO.getId() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Warehouse id invalid required", INVALID, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(warehouseListItemTO.getName())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Warehouse name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        WarehouseItem newWarehouse = convertWarehouse(warehouseListItemTO);
        newWarehouse.setObjectID(warehouseListItemTO.getId());

        //UPDATE Warehouse
        Integer id = accountingServiceLocal.saveWarehouse(newWarehouse);

        if (id != null && id > 0) {
            return successResponse(new WarehouseTO(id, warehouseListItemTO.getName()));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private WarehouseItem convertWarehouse(WarehouseListItemTO warehouseListItemTO) {
        WarehouseItem warehouseItem = new WarehouseItem();
        warehouseItem.setName(warehouseListItemTO.getName());
        warehouseItem.setNotes(warehouseListItemTO.getNotes());
        warehouseItem.setContactname(warehouseListItemTO.getContactname());
        warehouseItem.setPhone(warehouseListItemTO.getPhone());
        warehouseItem.setEmail(warehouseListItemTO.getEmail());
        warehouseItem.setAddress(warehouseListItemTO.getAddress());
        return warehouseItem;
    }

    @Operation(summary = "Get Warehouse Details", description = "Retrieves warehouse details based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have warehouse details."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "404", description = "Warehouse with provided id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/warehouse/{id}", method = RequestMethod.GET)
    public Object getWarehouse(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        WarehouseItem warehouse = accountingServiceLocal.getWarehouse(id);
        if (warehouse == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Warehouse with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        WarehouseListItemTO warehouseListItemTO = new WarehouseListItemTO();
        warehouseListItemTO.setId(warehouse.getObjectID());
        warehouseListItemTO.setName(warehouse.getName());
        warehouseListItemTO.setNotes(warehouse.getNotes());
        warehouseListItemTO.setContactname(warehouse.getContactname());
        warehouseListItemTO.setPhone(warehouse.getPhone());
        warehouseListItemTO.setEmail(warehouse.getEmail());
        warehouseListItemTO.setAddress(warehouse.getAddress());

        return successResponse(warehouseListItemTO);
    }

    @Operation(summary = "Delete Warehouse", description = "Deletes a warehouse based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with related error code and message."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/warehouse/{id}", method = RequestMethod.DELETE)
    public Object deleteWarehouse(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Warehouse id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        boolean isDeleted;
        try {
            isDeleted = accountingServiceLocal.deleteWarehouse(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (isDeleted) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException("You can not delete the warehouse which has other locations", "You can not delete the warehouse which has other locations", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Create Stock Adjustment", description = "Create Stock Adjustment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_adjustment/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createStockAdjustment(@RequestBody AdjustmentTO adjustmentTO) throws RestException {

        if (StringUtils.isBlank(adjustmentTO.getNumber())) {
            throw new RestException("Number is required.", "Number is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (adjustmentTO.getAccount() == null) {
            throw new RestException("Adjustment Account is required.", "Adjustment Account is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (adjustmentTO.getAdjustment_items() == null && adjustmentTO.getAdjustment_items().isEmpty()) {
            throw new RestException("Adjustment Items empty.", "Adjustment Items empty.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        try {
            longDateTimezoneFormat.parse(adjustmentTO.getDate());
        } catch (ParseException e) {
//            log.error("", e);
            throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        AdjustmentItem adjustmentItem = convertStockAdjustment(adjustmentTO);

        //CREATE StockAdjustment
        TestRPC createResult = productServiceLocal.saveStockAdjustment(adjustmentItem);

        if (createResult != null && createResult.getId() != null && createResult.getId() > 0) {
            return successResponse(new AdjustmentTO(createResult.getId()));
        } else {
            if (createResult != null) {
                if (MessageCommand.isNumberExists.equals(createResult.getMessageCommand())) {
                    throw new RestException("This Number already used", "This Number already used", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                } else if (MessageCommand.hasOutTransactions.equals(createResult.getMessageCommand())) {
                    throw new RestException("This Number already used", MessageCommand.hasOutTransactions.name(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Stock Adjustment", description = "Update Stock Adjustment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_adjustment/update", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateStockAdjustment(@RequestBody AdjustmentTO adjustmentTO) throws RestException {
        //Update is same as create but with id
        return createStockAdjustment(adjustmentTO);
    }

    @Operation(summary = "Get Stock Adjustment Details", description = "Retrieves Stock Adjustment details based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have warehouse details."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "404", description = "Stock Adjustment with provided id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_adjustment/{id}", method = RequestMethod.GET)
    public Object getStockAdjustment(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        AdjustmentItem adjustmentItem = productServiceLocal.getStockAdjustmentData(id);
        if (adjustmentItem == null) {
            throw new RestException("Stock Adjustment could not be found", "Stock Adjustment with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        AdjustmentTO adjustmentTO = new AdjustmentTO(adjustmentItem.getObjectID());
        adjustmentTO.setNumber(adjustmentItem.getNumber());
        if (adjustmentItem.getDate() != null) {
            adjustmentTO.setDate(longDateTimezoneFormat.format(adjustmentItem.getDate().getDate()));
        }
        adjustmentTO.setMemo(adjustmentItem.getMemo());
        adjustmentTO.setInt_number(adjustmentItem.getIntNumber());
        adjustmentTO.setRfpIds(adjustmentTO.getRfpIds());
        if (adjustmentItem.getAccount() != null) {
            adjustmentTO.setAccount(new IdNameTO(adjustmentItem.getAccount().getId(), adjustmentItem.getAccount().getName()));
        }
        if (adjustmentItem.getProductItems() != null) {

            List<AdjustmentItemTO> items = Arrays.stream(adjustmentItem.getProductItems()).map(item -> {
                AdjustmentItemTO adjustmentItemTO = new AdjustmentItemTO();
                adjustmentItemTO.setLine_item_id(item.getLineItemID());
                adjustmentItemTO.setProduct_id(item.getObjectId());
                adjustmentItemTO.setName(item.getName());
                adjustmentItemTO.setDescription(item.getDescription());
                if (item.getWarehouseId() != null) {
                    adjustmentItemTO.setWarehouse(new WarehouseTO(item.getWarehouseId(), item.getWarehouseName()));
                }
                adjustmentItemTO.setCurrent_qty(item.getCurrentQty());
                adjustmentItemTO.setUsed_qty(item.getUsedQty());
                adjustmentItemTO.setNew_qty(item.getNewQty());
                adjustmentItemTO.setTotal_qty(item.getTotalQty());
                if (item.getProjectID() != null) {
                    adjustmentItemTO.setProject(new IdNameTO(item.getProjectID(), item.getProjectName()));
                }
                return adjustmentItemTO;
            }).collect(Collectors.toList());

            adjustmentTO.setAdjustment_items((ArrayList) items);
        }

        return successResponse(adjustmentTO);
    }

    @Operation(summary = "Delete Stock Adjustment", description = "Deletes a Stock Adjustment based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with related error code and message."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_adjustment/{id}", method = RequestMethod.DELETE)
    public Object deleteStockAdjustment(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Stock Adjustment id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer objId;
        try {
            objId = productServiceLocal.deleteStockAdjustment(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (objId != null) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get warehouses by product ID")
    @GetMapping("/{productId}/warehouses")
    public ResultTO<List<ProductLocationDto>> getProductWarehouses(@PathVariable Integer productId) {
        List<ProductLocationDto> productLocations = accountingServiceLocal.getWarehousesByProductId(productId);
        return ResultTO.success(productLocations);
    }

    private AdjustmentItem convertStockAdjustment(AdjustmentTO adjustmentTO) {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        AdjustmentItem result = new AdjustmentItem();
        result.setObjectID(adjustmentTO.getId());
        result.setNumber(adjustmentTO.getNumber());
        try {
            result.setDate(new DateNonConvertable(longDateTimezoneFormat.parse(adjustmentTO.getDate())));
        } catch (ParseException e) {
            log.error("", e);
        }
        result.setMemo(adjustmentTO.getMemo());
        result.setIntNumber(adjustmentTO.getInt_number());
        result.setRfpIds(adjustmentTO.getRfpIds());
        if (adjustmentTO.getAccount() != null) {
            result.setAccount(new SelectItem(adjustmentTO.getAccount().getId(), adjustmentTO.getAccount().getName()));
        }
        if (adjustmentTO.getAdjustment_items() != null) {
            List<ProductItem> productItems = adjustmentTO.getAdjustment_items().stream().map(adjustmentItemTO -> {
                ProductItem productItem = new ProductItem();
                productItem.setLineItemID(adjustmentItemTO.getLine_item_id());
                productItem.setObjectId(adjustmentItemTO.getProduct_id());
                productItem.setName(adjustmentItemTO.getName());
                productItem.setDescription(adjustmentItemTO.getDescription());
                if (adjustmentItemTO.getWarehouse() != null) {
                    productItem.setWarehouseId(adjustmentItemTO.getWarehouse().getWarehouse_id());
                    productItem.setWarehouseName(adjustmentItemTO.getWarehouse().getWarehouse_name());
                }
                productItem.setCurrentQty(adjustmentItemTO.getCurrent_qty());
                productItem.setUsedQty(adjustmentItemTO.getUsed_qty());
                productItem.setNewQty(adjustmentItemTO.getNew_qty());
                productItem.setTotalQty(adjustmentItemTO.getTotal_qty());
                if (adjustmentItemTO.getProject() != null) {
                    productItem.setProjectID(adjustmentItemTO.getProject().getId());
                    productItem.setProjectName(adjustmentItemTO.getProject().getName());
                }
                return productItem;
            }).toList();

            result.setProductItems(productItems.toArray(new ProductItem[0]));
        }
        return result;
    }

    @Operation(summary = "Create Stock Transfer", description = "Create Stock Transfer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_transfer/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createStockTransfer(@RequestBody StockTransferTO stockTransferTO) throws RestException {
        stockTransferTO.setId(null);
        return saveStrockTransfer(stockTransferTO);
    }

    @Operation(summary = "Update Stock Transfer", description = "Update Stock Transfer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have true if created or false if error occured."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_transfer/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateStockTransfer(@RequestBody StockTransferTO stockTransferTO) throws RestException {
        if (stockTransferTO.getId() == null || stockTransferTO.getId() <= 0) {
            throw new RestException("id is required.", "id is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return saveStrockTransfer(stockTransferTO);
    }

    @Operation(summary = "Get Stock Transfer Details", description = "Retrieves Stock Transfer details based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Transfer details."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "404", description = "Warehouse with provided id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_transfer/{id}", method = RequestMethod.GET)
    public Object getStockTransfer(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        StockTransferItem stockTransferItem = productServiceLocal.getStockTransfer(id);
        if (stockTransferItem == null) {
            throw new RestException("Stock Transfer could not be found", "Stock Transfer with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        StockTransferTO stockTransferTO = new StockTransferTO(stockTransferItem.getObjectId());
        stockTransferTO.setName(stockTransferItem.getTransferName());
        stockTransferTO.setNumber(stockTransferItem.getNumber());
        if (stockTransferItem.getDate() != null) {
            stockTransferTO.setDate(longDateTimezoneFormat.format(stockTransferItem.getDate().getDate()));
        }

        if (stockTransferItem.getAdjustmentItemList() != null) {

            List<StockTransferItemTO> transferItems = stockTransferItem.getAdjustmentItemList().stream().map(item -> getStockTransferItemTO(longDateTimezoneFormat, item)).collect(Collectors.toList());

            stockTransferTO.setTransfer_items((ArrayList) transferItems);
        }

        return successResponse(stockTransferTO);
    }

    @Operation(summary = "Delete Stock Transfer", description = "Deletes a Stock Transfer based on provided id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with related error code and message."),
            @ApiResponse(responseCode = "400", description = "id is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_transfer/{id}", method = RequestMethod.DELETE)
    public Object deleteStockTransfer(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Stock Transfer id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer objId;
        try {
            objId = productServiceLocal.deleteStockTransfer(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (objId != null) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Stock Transfers List", description = "Retrieves Stock Transfers List")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Orders details "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/stock_transfer", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody ProductRequestListTO requestListSearchData) throws RestException {

        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListSearchData.getStart());
        filterParameter.setLimit(requestListSearchData.getLimit());
        filterParameter.setSearchKey(requestListSearchData.getSearch_text());
        filterParameter.setWarehouseID(requestListSearchData.getWarehouse_id());

        ListResult<StockTransferItem> stockTransferList;
        try {
            stockTransferList = productServiceLocal.getStockTransferList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<StockTransferTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (StockTransferItem st : stockTransferList.getList()) {

            StockTransferItem stockTransferItem = productServiceLocal.getStockTransfer(st.getObjectId());

            StockTransferTO stockTransferTO = new StockTransferTO(stockTransferItem.getObjectId());
            stockTransferTO.setName(stockTransferItem.getTransferName());
            stockTransferTO.setNumber(stockTransferItem.getNumber());
            if (stockTransferItem.getDate() != null) {
                stockTransferTO.setDate(longDateTimezoneFormat.format(stockTransferItem.getDate().getDate()));
            }

            if (stockTransferItem.getAdjustmentItemList() != null) {

                List<StockTransferItemTO> transferItems = stockTransferItem.getAdjustmentItemList().stream().map(item -> getStockTransferItemTO(longDateTimezoneFormat, item)).collect(Collectors.toList());

                stockTransferTO.setTransfer_items((ArrayList) transferItems);
            }

            resultList.add(stockTransferTO);
        }
        ResponseResultListData<StockTransferTO> resultListData = new ResponseResultListData<>();
        resultListData.setList(resultList);
        resultListData.setTotal(stockTransferList.getTotal());
        return successResponse(resultListData);
    }

    private StockTransferItemTO getStockTransferItemTO(SimpleDateFormat longDateTimezoneFormat, AdjustmentItem item) {
        StockTransferItemTO stockTransferItemTO = new StockTransferItemTO();
        stockTransferItemTO.setId(item.getObjectID());
        if (item.getDate() != null) {
            stockTransferItemTO.setDate(longDateTimezoneFormat.format(item.getDate().getDate()));
        }
        if (item.getProductItems() != null && item.getProductItems().length >= 2) {

            TransferTO from = new TransferTO();
            from.setLine_item_id(item.getProductItems()[0].getLineItemID());
            if (item.getProduct() != null) {
                from.setProduct_id(item.getProduct().getId());
                from.setProductname(item.getProduct().getName());
            }
            from.setAccount_id(item.getProductItems()[0].getAccountID());
            stockTransferItemTO.setTransfer_qty(item.getProductItems()[0].getUsedQty());
            if (item.getProductItems()[0].getWarehouseId() != null) {
                from.setWarehouse(new WarehouseTO(item.getProductItems()[0].getWarehouseId(), item.getProductItems()[0].getWarehouseName()));
            }
            stockTransferItemTO.setFrom(from);

            TransferTO to = new TransferTO();
            to.setLine_item_id(item.getProductItems()[1].getLineItemID());
//            to.setProduct_id(item.getProductItems()[1].getObjectId());
            if (item.getProduct() != null) {
                to.setProduct_id(item.getProduct().getId());
                to.setProductname(item.getProduct().getName());
            }
            to.setAccount_id(item.getProductItems()[1].getAccountID());
//                    stockTransferItemTO.setTransfer_qty(item.getProductItems()[1].getNewQty());
            if (item.getProductItems()[1].getWarehouseId() != null) {
                to.setWarehouse(new WarehouseTO(item.getProductItems()[1].getWarehouseId(), item.getProductItems()[1].getWarehouseName()));
            }
            stockTransferItemTO.setTo(to);
        }
        return stockTransferItemTO;
    }


    public Object saveStrockTransfer(StockTransferTO stockTransferTO) throws RestException {
        if (stockTransferTO.getTransfer_items() == null && stockTransferTO.getTransfer_items().isEmpty()) {
            throw new RestException("Transfer Items empty.", "Transfer Items empty.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(stockTransferTO.getName())) {
            throw new RestException("Name is required.", "Name is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        try {
            longDateTimezoneFormat.parse(stockTransferTO.getDate());
        } catch (ParseException e) {
            throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //Server Side Validation
        List<QuantityItem> itemsToValidate = stockTransferTO.getTransfer_items().stream().map(item -> {
            QuantityItem quantityItem = new QuantityItem();
            if (item.getFrom() != null) {
                if (item.getFrom().getWarehouse() != null) {
                    quantityItem.setWarehouseID(item.getFrom().getWarehouse().getWarehouse_id());
                }
                if (item.getFrom().getProduct_id() != null) {
                    quantityItem.setId(item.getFrom().getProduct_id());
                }
                if (item.getTransfer_qty() != null) {
                    quantityItem.setQuantity(item.getTransfer_qty());
                } else {
                    quantityItem.setQuantity(BigDecimal.ZERO);
                }
            }
            return quantityItem;
        }).toList();

        SelectItem[] errors = invoiceServiceLocal.validateStockAvailability(itemsToValidate.toArray(new QuantityItem[0]), stockTransferTO.getId(), StockOutFlow.FROM_GOODS_DELIVERY_NOTES, null);

        if (errors != null && errors.length > 0) {
            StringBuilder itemNames = new StringBuilder();
            for (int i = 0; i < errors.length; i++) {
                if (i != 0) {
                    itemNames.append(", ");
                }
                itemNames.append("\"").append(errors[i].getName()).append("\"");
            }
            throw new RestException(itemNames.toString(), itemNames.toString(), INVALID, HttpStatus.NOT_ACCEPTABLE);
        }

        StockTransferItem stockTransfer = convertStockTransfer(stockTransferTO);

        //CREATE StockAdjustment
        Integer transferId = productServiceLocal.saveStockTransfer(stockTransfer);

        if (transferId != null && transferId > 0) {
            return successResponse(new AdjustmentTO(transferId));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private StockTransferItem convertStockTransfer(StockTransferTO stockTransferTO) throws RestException {
        StockTransferItem result = new StockTransferItem();
        if (stockTransferTO != null) {

            result.setObjectId(stockTransferTO.getId());
            result.setTransferName(stockTransferTO.getName());

            if (StringUtils.isNotBlank(stockTransferTO.getDate())) {
                SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
                try {
                    result.setDate(new DateNonConvertable(longDateTimezoneFormat.parse(stockTransferTO.getDate())));
                } catch (ParseException e) {
                    log.error("", e);
                }
            }
            if (stockTransferTO.getTransfer_items() != null) {

                List<AdjustmentItem> transferItems = stockTransferTO.getTransfer_items().stream().map(item -> {

                    AdjustmentItem adjustmentItem = new AdjustmentItem();
                    adjustmentItem.setObjectID(item.getId());
                    adjustmentItem.setStockTransfer(true);
                    adjustmentItem.setDate(new DateNonConvertable());

                    ProductItem[] productItems = new ProductItem[2];
                    productItems[0] = new ProductItem();
                    productItems[1] = new ProductItem();

                    if (item.getFrom() != null) {
                        if (item.getFrom().getWarehouse() != null) {
                            adjustmentItem.setFromWarehouseID(item.getFrom().getWarehouse().getWarehouse_id());
                            productItems[0].setWarehouseId(item.getFrom().getWarehouse().getWarehouse_id());
                        }
                        productItems[0].setLineItemID(item.getFrom().getLine_item_id());
                        productItems[0].setObjectId(item.getFrom().getProduct_id());
                        productItems[0].setAccountID(item.getFrom().getAccount_id());
                        productItems[0].setUsedQty(item.getTransfer_qty());
                        productItems[0].setNewQty(BigDecimal.ZERO);
                        productItems[0].setBatchItems(item.getFrom().getBatchItems());
                    }
                    if (item.getTo() != null) {
                        if (item.getTo().getWarehouse() != null) {
                            adjustmentItem.setToWarehouseID(item.getTo().getWarehouse().getWarehouse_id());
                            productItems[1].setWarehouseId(item.getTo().getWarehouse().getWarehouse_id());
                        }
                        productItems[1].setLineItemID(item.getTo().getLine_item_id());
                        productItems[1].setObjectId(item.getTo().getProduct_id());
                        productItems[1].setAccountID(item.getTo().getAccount_id());
                        productItems[1].setUsedQty(BigDecimal.ZERO);
                        productItems[1].setNewQty(item.getTransfer_qty());
                        productItems[1].setBatchItems(item.getTo().getBatchItems());
                    }

                    adjustmentItem.setProductItems(productItems);

                    return adjustmentItem;
                }).collect(Collectors.toList());

                result.setAdjustmentItemList((ArrayList<AdjustmentItem>) transferItems);
            }
        }
        return result;
    }

}
