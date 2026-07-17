package com.edatasite.workforce.rest.v2.release10.settings;

import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.invoice.server.app.ItemTableSettingsServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldListTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by Akhror on 1/8/2021.
 */

@Tag(name = "Item table", description = "Item Table API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiItemTableControllerV2 extends BaseApiControllerV2 {

    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingService;

    @Operation(summary = "Item Table", description = "Retrieves data on item table columns")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of item table columns")})
    @RequestMapping(value = "/itemTable/{section}", method = RequestMethod.GET)
    public Object getColumns(@Pattern(regexp = "SALE_INVOICE_ITEM|SALE_ORDER_ITEM|SALE_QUOTE_ITEM|CREDIT_NOTE_ITEM|DEBIT_NOTE_ITEM|PURCHASE_INVOICE_ITEM|PURCHASE_ORDER_ITEM|OPPORTUNITY_SUB_ITEM|BILL_OF_MATERIALS_ITEM|EXPENSE_CLAIM_ITEM|RFQ_ITEM|RFP_ITEM|CLIENT_ITEM|SUPPLIER_ITEM",
            message = "Section must be one of these, SALE_INVOICE_ITEM, SALE_ORDER_ITEM, SALE_QUOTE_ITEM, CREDIT_NOTE_ITEM, DEBIT_NOTE_ITEM, PURCHASE_INVOICE_ITEM, PURCHASE_ORDER_ITEM, OPPORTUNITY_SUB_ITEM, BILL_OF_MATERIALS_ITEM, EXPENSE_CLAIM_ITEM, RFQ_ITEM, RFP_ITEM, CLIENT_ITEM, SUPPLIER_ITEM")
                             @PathVariable("section") String section) {
        List<CustomFieldListTO> columns = itemTableSettingService.getColumnConfigsForAPI(ItemTableEnum.valueOf(section));
        return ResultTO.success(columns);
    }

}
