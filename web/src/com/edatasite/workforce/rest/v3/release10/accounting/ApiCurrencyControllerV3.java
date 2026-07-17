package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Tag(name = "Currency", description = "Currency Public API")
@RestController
@RequestMapping(value = "/currency")
public class ApiCurrencyControllerV3 {

    private final CurrencyService currencyService;
    private final CurrencyManager currencyManager;

    public ApiCurrencyControllerV3(CurrencyService currencyService,
                                   CurrencyManager currencyManager) {
        this.currencyService = currencyService;
        this.currencyManager = currencyManager;
    }

    @Operation(summary = "Get base currency")
    @GetMapping(value = "/base", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<CurrencyTO> getBaseCurrency() throws RestException {
        return Optional.ofNullable(currencyService.getCompanyBaseCurrency())
                .map(CurrencyTO::new)
                .map(ResultTO::success)
                .orElseThrow(() -> new RestException("Currency is not found", "Currency is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = {ApiConstants.ACCESS_TOKEN})
    public ResultTO<List<IdName>> getAllCurrencies(@RequestParam(value = "searchKey", required = false) String searchKey) throws RestException {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(searchKey);
        List<IdName> currencies = currencyManager.getAllCurrency(fp).stream()
                .map(c -> {
                    IdName idName = new IdName(c.getObjectID(), c.getName());
                    idName.addProperty("fullName", c.getFullName());
                    idName.addProperty("symbol", c.getSymbol());
                    return idName;
                })
                .toList();
        return ResultTO.success(currencies);
    }
}
