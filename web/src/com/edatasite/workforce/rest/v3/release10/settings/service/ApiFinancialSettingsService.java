package com.edatasite.workforce.rest.v3.release10.settings.service;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.rest.base.to.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.settings.dto.FinancialSettingsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiFinancialSettingsService {
    private final FinancialSettingsManager financialSettingsManager;
    private final CurrencyService currencyService;



    @Autowired
    public ApiFinancialSettingsService(FinancialSettingsManager financialSettingsManager,
                                       CurrencyService currencyService) {
        this.financialSettingsManager = financialSettingsManager;
        this.currencyService = currencyService;
    }

    public FinancialSettingsDTO getFinancialSettings() throws RestException{
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null) {
            FinancialSettingsDTO financialSettingsDTO = new FinancialSettingsDTO();
            CurrencyItem currencyItem = currencyService.getCompanyBaseCurrency();
            if (currencyItem != null) {
                financialSettingsDTO.setBaseCurrency(new CurrencyTO(currencyItem));
            }
            financialSettingsDTO.setCalculationScale(financialSettings.getCalculationScale());
            financialSettingsDTO.setRoundingExchRate(financialSettings.getRoundingExchRate());
            financialSettingsDTO.setConversionDate(financialSettings.getConversionDate());
            financialSettingsDTO.setFinancialYearEnd(financialSettings.getFinancialYearEnd());
            financialSettingsDTO.setEnableIT(financialSettings.enableIT());
            return financialSettingsDTO;
        }
        throw new RestException(GENERAL_ERROR_MESSAGE, "Financial settings not found",NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
