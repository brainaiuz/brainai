package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BankAccountDto;
import com.edatasite.workforce.rest.v3.release10.accounting.request.DailyCurrencyRateRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Tag(name = "Accounting utility Resources", description = "Contains all the accounting utilities as like tax-list and more")
@RestController
@RequestMapping(value = "/accounting", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class AccountingApiResource {

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;

    @Operation(summary = "Currency List", description = "Retrieve all used currencies of the Company")
    @RequestMapping(value = "/currency-list", method = RequestMethod.GET)
    public ResponseEntity<List<ItemDto>> getCurrencyList() {
        CurrencyItem[] currencyItems = currencyServiceLocal.getCurrencies();
        ArrayList<ItemDto> result = new ArrayList<>();
        Stream.of(currencyItems).forEach(currency -> result.add(new ItemDto(currency.getId(), currency.getName(), currency.getCode())));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Company Tax List", description = "Retrieve all the tax list of the Company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of tax_id,  tax_name"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/tax-list", method = RequestMethod.GET)
    public ResponseEntity<List<ItemDto>> getTaxList() {
        TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(new ListingFilterParameter());

        ArrayList<ItemDto> result = new ArrayList<>();

        FinancialSettingsManager companyManager = StaticContextAccessor.getBean(FinancialSettingsManager.class);
        int scale = 0;
        if (companyManager.getFinancialSettings() != null) {
            scale = companyManager.getFinancialSettings().getTaxRateScale();
        }

        for (TaxItem ti : taxItems) {
            ItemDto dto = new ItemDto(ti.getId(), ti.getName());
            if (ti.getTaxPercent() != null) {
                dto.setValue(ti.getTaxPercent().setScale(scale, RoundingMode.HALF_UP));
            }
            result.add(dto);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Company Bank Account List", description = "Retrieve all the bank accounts of the Company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of bank_id,  bank_name"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/bank-accounts", method = RequestMethod.GET)
    public ResponseEntity<List<BankAccountDto>> getBankAccounts() {
        ArrayList<BankAccountDto> banks = accountingServiceLocal.getBankAccountList();

        return new ResponseEntity<>(banks, HttpStatus.OK);
    }

    @Operation(summary = "Save daily exchange rate of a given currency", description = "Save daily exchange rate of a given currency")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have exchange rate of currency object by date"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/save/daily-exchange-rate", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CurrencyListItem> saveCurrencyDailyRate(@RequestBody @Valid DailyCurrencyRateRequest request) throws RestException {
        CurrencyItem[] currencyItems = currencyServiceLocal.getCurrencies();
        Optional<CurrencyItem> optCurrency = Stream.of(currencyItems).filter(item -> item.getName().equalsIgnoreCase(request.getCurrency())).findAny();
        if (optCurrency.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, MessageFormat.format("{0} is not recognized in the system.", request.getCurrency()), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        CurrencyListItem item = new CurrencyListItem();
        item.setCurrency(optCurrency.get());
        item.setDate(new DateNonConvertable(request.getDate()));
        item.setExchangeRate(request.getExchangeRate());
        currencyServiceLocal.createOrUpdateCurrency(item);
        return ResultTO.success(currencyServiceLocal.getCurrencyRateByDate(item.getCurrency().getId(), item.getDate()));
    }
}
