package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseItemsListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.tax.TaxTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 12/7/2017.
 */

@Tag(name = "Tax", description = "Tax API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTaxControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiTaxControllerV2.class);
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;

    @Operation(summary = "Get Expense Item Tax List", description = "Retrieves list of taxes related to expense item")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of taxes")})
    @RequestMapping(value = "/expenses/taxes", method = RequestMethod.GET)
    public Object getExpenseTaxes() throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setInvoiceType(Constants.PAYABLE);
        return getBaseTaxList(filterParameter);
    }

    private Object getBaseTaxList(ListingFilterParameter filterParameter) throws RestException {
        filterParameter.setInvoiceType(Constants.PAYABLE);
        try {
            TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(filterParameter);
            ArrayList<TaxTO> taxList = new ArrayList<>();
            for (TaxItem taxItem : taxItems) {
                taxList.add(new TaxTO(taxItem.getId(), taxItem.getName(), taxItem.getTaxPercent()));
            }
            return successResponse(new ResponseItemsListData<>(taxList));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
