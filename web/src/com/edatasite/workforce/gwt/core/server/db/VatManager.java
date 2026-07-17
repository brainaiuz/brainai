package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsTaxComponent;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.EdsVatTemplate;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.List;

public interface VatManager extends Manager<EdsVat> {

    String REDUCED = "Reduced";
    String STANDARD = "Standard";
    String ZERORATED = "Zero Rated";
    BigDecimal REDUCED_PERCENT = new BigDecimal("5.00");
    BigDecimal STANDARD_PERCENT = new BigDecimal("15.00");
    BigDecimal ZERORATED_PERCENT = AccountingConstants.ZERO;

    List<EdsVat> getCompanyVats(EdsCompany company, ListingFilterParameter fp);

//    List<EdsVat> getCompanyCountryVats(EdsCompany company);

    EdsVat getVat(Integer objectID);

    Boolean duplicate(String vatName, BigDecimal vatRate);

    EdsVat getVatByName(String vatName);

    EdsVat getVatByKey(TaxKeyEnum key);

    List<EdsVatTemplate> getCountryVatTemplates();

    List<EdsTaxComponent> getTaxComponents(Integer taxID);

    void deleteTaxComponents(Integer taxID);

    boolean isTaxUsed(Integer vatID);

    Integer getCompanyVatsCount(EdsCompany company, ListingFilterParameter filterParametrs);

    Integer getTaxRatesListCount();

    EdsVat getNimbleTax(BigDecimal percent);

    EdsVat getTaxForCustomInvoiceImport(BigDecimal percent);

    EdsVat getTaxByNameAndPercent(String name, BigDecimal percent);

    EdsVat getDefaultVat();

    boolean existsDefaultTax();

    void removeDefaultTax();
}
