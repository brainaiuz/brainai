package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickList;
import com.edatasite.workforce.gwt.invoice.client.rpc.PickListItem;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.accounting.ApiSalesOrderControllerV2;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.accounting.PickListDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.accounting.PickListItemDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created By : Dilsh0d Madrahimov on 9/30/2019 12:01 PM
 */
@Tag(name = "Picklist Api", description = "Picklist Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiPickListControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiSalesOrderControllerV2.class);

    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    private WarehouseManager warehouseManager;


    @Operation(summary = "Get Packlist")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Packlist data")})
    @RequestMapping(value = "/packlist/{sales_order_id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "sales_order_id") Integer sales_order_id) throws RestException {

        if (sales_order_id == null || sales_order_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "sales order id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsPickList edsPickList = pickListManager.getPickListBySaleQuoteID(sales_order_id);
        if (edsPickList == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "There is no any packlist with sales order: " + sales_order_id, NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        PickList pickList = edsPickList.getData(warehouseManager.getDefaultWarehouse());

        PickListDTO pickListDTO = new PickListDTO();
        pickListDTO.setId(edsPickList.getObjectID());

        ArrayList<PickListItemDTO> pickListItemDTOList = new ArrayList<>();
        for (PickListItem pickListItem : pickList.getItems()) {
            PickListItemDTO pickListItemDTO = new PickListItemDTO();
            pickListItemDTO.setId(pickListItem.getObjectID());
            pickListItemDTO.setProduct_id(pickListItem.getItemID());
            pickListItemDTO.setProduct_name(pickListItem.getItemName());
            pickListItemDTO.setProduct_number(pickListItem.getItemNumber());
            pickListItemDTO.setShipping_qty(BigDecimal.ZERO);
            pickListItemDTO.setReference("");
            pickListItemDTOList.add(pickListItemDTO);
        }
        pickListDTO.setItems(pickListItemDTOList);

        return successResponse(pickListDTO);
    }


    @Operation(summary = "Add Packlist")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Packlist data")})
    @RequestMapping(value = "/packlist", method = RequestMethod.POST)
    public Object add(@Valid @RequestBody PickListDTO pickListDTO) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (pickListDTO.getId() == null || pickListDTO.getId() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Packlist id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(pickListDTO.getShip_date())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Shipping date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Date shipDate;
        try {
            shipDate = longDateTimezoneFormat.parse(pickListDTO.getShip_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid shipping date format", "Invalid shipping date format. Acceptable format for shipping date is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (pickListDTO.getItems() == null || pickListDTO.getItems().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Packlist item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        LinkedHashMap<Integer, PickListItemDTO> itemsMap = new LinkedHashMap<>();

        for (PickListItemDTO pickListItemDTO : pickListDTO.getItems()) {
            if (pickListItemDTO.getId() == null || pickListItemDTO.getId() <= 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            /*if ( (pickListItemDTO.getProduct_id() == null || pickListItemDTO.getProduct_id() <= 0) && StringUtils.isBlank(pickListItemDTO.getProduct_number()) ) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "product_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }*/
            if (pickListItemDTO.getShipping_qty() == null || pickListItemDTO.getShipping_qty().compareTo(BigDecimal.ZERO) == 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Shipping qty is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            itemsMap.put(pickListItemDTO.getId(), pickListItemDTO);
        }

        PickList pickListData = quoteServiceLocal.getPickList(pickListDTO.getId());
        if (pickListData == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Picklist with " + pickListDTO.getId() + " id not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        pickListData.setShipDate(new DateNonConvertable(shipDate));
        pickListData.setGdnNumber(pickListData.getGdnNumberData().getTransferNumber());
        pickListData.setCarrierAccountID(pickListDTO.getCarrier_account_id());
        pickListData.setShippingLabel(pickListDTO.getShipping_label());

        ArrayList<PickListItem> items = new ArrayList<>();
        for (PickListItem pickListItem : pickListData.getItems()) {
            PickListItemDTO pickListItemDTO = itemsMap.get(pickListItem.getObjectID());
            if (pickListItemDTO == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "There is no picklist item with id: " + pickListItem.getObjectID(), REQUIRED, HttpStatus.BAD_REQUEST);
            }

            BigDecimal qty = pickListItem.getQty().subtract(pickListItem.getShippedQty());
            BigDecimal shippingQty = pickListItemDTO.getShipping_qty();
            BigDecimal rq = qty.subtract(shippingQty);
            if (rq.compareTo(BigDecimal.ZERO) < 0) {
                throw new RestException("You cannot enter shipping quantity more than ordered quantity", "You cannot enter shipping quantity more than ordered quantity.", CONFLICT, HttpStatus.CONFLICT);
            }

            pickListItem.setShipped(pickListItemDTO.getShipping_qty());
            pickListItem.setReference(pickListItemDTO.getReference());
            items.add(pickListItem);
        }
        pickListData.setItems(items.toArray(new PickListItem[0]));

        if (fullShipped(itemsMap, pickListData)) {
            pickListData.setStatus(Constants.SHIPPED);
        } else {
            pickListData.setStatus(Constants.PARTIAL_SHIPPED);
        }

        boolean isSaved = quoteServiceLocal.updatePickList(pickListData);
        if (!isSaved) {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new IdDTO(pickListDTO.getId()));
    }

    private boolean fullShipped(LinkedHashMap<Integer, PickListItemDTO> itemsMap, PickList pickListData) {
        for (PickListItem pickListItem : pickListData.getItems()) {
            PickListItemDTO pickListItemDTO = itemsMap.get(pickListItem.getObjectID());

            BigDecimal qty = pickListItem.getQty().subtract(pickListItem.getShippedQty());
            BigDecimal shippingQty = pickListItemDTO.getShipping_qty();
            BigDecimal rq = qty.subtract(shippingQty);

            if (rq.compareTo(BigDecimal.ZERO) > 0) {
                return false;
            }
        }
        return true;
    }
}
