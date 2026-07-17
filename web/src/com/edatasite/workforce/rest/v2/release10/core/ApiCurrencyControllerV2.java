package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.CurrencyInfoResultTO;
import com.edatasite.workforce.rest.base.to.CurrencyInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ProductCurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Tag(name = "Currency", description = "Currency API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCurrencyControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCurrencyControllerV2.class);
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;

    @Operation(summary = "Get Main Currency", description = "Retrieves base currency of the company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have a base currency of the current company"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/payments/main_currency", method = RequestMethod.GET)
    public Object getMainCurrency() throws RestException {
        CurrencyItem currencyItem = currencyServiceLocal.getCompanyBaseCurrency();
        if (currencyItem != null) {
            return successResponse(new ProductCurrencyTO(currencyItem.getId(), currencyItem.getName(), BigDecimal.ONE));
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Company Currencies", description = "Retrieves list of company currencies")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have currency_id, name and exchange rate"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/payments/cash_advance/currencies", method = RequestMethod.GET)
    public Object getCompanyCurrencies(@RequestParam(value = "requester_id", required = false) Integer requester_id) throws RestException {
        EdsEmployee employee = null;
        if (requester_id != null) {
            employee = employeeManager.get(requester_id);
            if (employee == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Employee with id " + requester_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
        if (employee == null) {
            EdsUser user = userManager.getUser();
            if (!user.isEmployee()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, user.getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
            }
            employee = (EdsEmployee) user;
        }

        Integer roundingExchangeRate = (financialSettingsManager.getFinancialSettings() != null && financialSettingsManager.getFinancialSettings().getRoundingExchRate() != null) ? financialSettingsManager.getFinancialSettings().getRoundingExchRate() : 4;
        CurrencyItem[] currencyItems = currencyServiceLocal.getEmployeeCurrencies(employee.getObjectID(), true);

        ArrayList<CurrencyListTO> currencyListResult = new ArrayList<>();
        for (CurrencyItem currencyItem : currencyItems) {
            CurrencyListItem exchangeRateItem = currencyServiceLocal.getCurrencyRateByDate(currencyItem.getId(), new DateNonConvertable(new Date()));
            currencyListResult.add(new CurrencyListTO(currencyItem.getId(), currencyItem.getName(), BigDecimal.valueOf(exchangeRateItem.getExchangeRate()).setScale(roundingExchangeRate, RoundingMode.HALF_UP)));
        }

        return successResponse(new CurrencyListResultTO(currencyListResult));

    }

    @Operation(summary = "Get Currency Info", description = "Retrieves main currency of the company and list of currencies")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have main currency of the company and list of currencies"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/{main_entity_name}/currency_info", method = RequestMethod.GET)
    public Object getCurrencyInfo(@PathVariable(value = "main_entity_name") String main_entity_name) throws RestException {
        if (main_entity_name == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        Integer roundingExchangeRate = (financialSettingsManager.getFinancialSettings() != null && financialSettingsManager.getFinancialSettings().getRoundingExchRate() != null) ? financialSettingsManager.getFinancialSettings().getRoundingExchRate() : 4;
        CurrencyInfoResultTO currencyInfoResultTO = new CurrencyInfoResultTO();
        if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(main_entity_name) || EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(main_entity_name) || EntityTypeEnum.TASKS.name().equalsIgnoreCase(main_entity_name) ||
                EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(main_entity_name) || EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(main_entity_name)) {


            CurrencyItem currencyItem = currencyServiceLocal.getCompanyBaseCurrency();
            if (currencyItem != null) {
                currencyInfoResultTO.setCompany_currency(new CurrencyInfoTO(currencyItem.getId(), currencyItem.getName(), BigDecimal.ONE));
            }
            ArrayList<CurrencyInfoTO> currencies = new ArrayList<>();
            List<EdsCurrency> currencyList = exchangeCurrencyManager.getCurrencyList();
            if (currencyList != null && currencyList.size() > 0) {
                currencyList.forEach(edsCurrency -> {
                    CurrencyListItem exchangeRateItem = currencyServiceLocal.getCurrencyRateByDate(edsCurrency.getObjectID(), new DateNonConvertable(new Date()));
                    CurrencyInfoTO currency = new CurrencyInfoTO();
                    currency.setId(edsCurrency.getObjectID());
                    currency.setCode(edsCurrency.getName());
                    currency.setExchange_rate(BigDecimal.valueOf(exchangeRateItem.getExchangeRate()).setScale(roundingExchangeRate, RoundingMode.HALF_UP));
                    currencies.add(currency);
                });
            }
            if (currencyItem != null) {
                currencies.add(new CurrencyInfoTO(currencyItem.getId(), currencyItem.getName(), BigDecimal.ONE));
            }
            currencyInfoResultTO.setCurrencies(currencies);
        }
        return successResponse(currencyInfoResultTO);
    }

    /*private ArrayList<CurrencyListTO> getCurrencyListBase() throws RestException {
        try {
            //If exchange rate is too long, set exchange rate floating point, It's by default 4
            Integer roundingExchangeRate = (financialSettingsManager.getFinancialSettings() != null && financialSettingsManager.getFinancialSettings().getRoundingExchRate() != null) ? financialSettingsManager.getFinancialSettings().getRoundingExchRate() : 4;
            ListResult<CurrencyListItem> currencyList = currencyServiceLocal.getCurrencyRateList(new DateNonConvertable(new Date()));
            ArrayList<CurrencyListTO> currencyListResult = new ArrayList<>();
            if (currencyList != null && currencyList.getList().size() > 0) {
                for (CurrencyListItem item : currencyList.getList()) {
                    currencyListResult.add(new CurrencyListTO(item.getCurrency().getId(), item.getCurrency().getName(), new BigDecimal(item.getExchangeRate()).setScale(roundingExchangeRate, BigDecimal.ROUND_HALF_UP)));
                }
            }
            return currencyListResult;
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}
