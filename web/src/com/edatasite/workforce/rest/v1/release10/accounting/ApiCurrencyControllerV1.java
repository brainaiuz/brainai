package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.CurrencyTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by dilshod madrahimov on 3/24/15.
 */
@Tag(name = "Currency", description = "Currency API")
@RestController
@RequestMapping(value = "/currency", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCurrencyControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList() {
        CurrencyItem[] currencyItems = currencyServiceLocal.getCurrencies();
        ArrayList<CurrencyTO> result = new ArrayList<>();
        for (CurrencyItem item : currencyItems) {
            result.add(new CurrencyTO(item));

        }
        return successResponse(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        CurrencyItem currencyItem = currencyServiceLocal.getCurrency(id);
        if (currencyItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        return successResponse(new CurrencyTO(currencyItem));
    }

    @RequestMapping(value = "/base", method = RequestMethod.GET)
    public Object getBaseCurrency() {
        CurrencyItem currencyItem = currencyServiceLocal.getCompanyBaseCurrency();
        if (currencyItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(new CurrencyTO(currencyItem));
    }

    @RequestMapping(value = "/exchangeRate", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getExchangeRate(@RequestBody MListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (filterParameter.getDate() == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (filterParameter.getFrom() != null && filterParameter.getTo() != null) {
            return successResponse(currencyServiceLocal.getExchangeRateDouble(filterParameter.getFrom(), filterParameter.getTo(), WrapUtils.longToDate(filterParameter.getDate()), 0).getRate());
        }
        if (filterParameter.getCurrencyId() != null) {
            return successResponse(currencyServiceLocal.getCurrencyRateByDate(filterParameter.getCurrencyId(), new DateNonConvertable(WrapUtils.longToDate(filterParameter.getDate()))).getExchangeRate());
        }
        return errorResponse();
    }
}
