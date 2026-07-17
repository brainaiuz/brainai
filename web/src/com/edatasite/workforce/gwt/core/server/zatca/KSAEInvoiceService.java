package com.edatasite.workforce.gwt.core.server.zatca;


import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.zatca.constants.InvoiceTypeCodeEnum;
import com.edatasite.workforce.gwt.core.server.zatca.exception.ExceptionConstants;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.*;
import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;
import com.edatasite.workforce.gwt.core.server.zatca.utils.ZatcaUtils;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.edatasite.workforce.gwt.core.server.zatca.utils.ZatcaUtils.DEFAULT_SCALE_NUMBER;

@Service
public class KSAEInvoiceService {
    private final ArrayList<String> exceptionNames = new ArrayList<>();
    private static final String ZERO_VALUE_HASH = "NWZlY2ViNjZmZmM4NmYzOGQ5NTI3ODZjNmQ2OTZjNzljMmRiYzIzOWRkNGU5MWI0NjcyOWQ3M2EyN2ZiNTdlOQ==";
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoiceServiceLocal invoiceService;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private CompanyManager companyManager;

    public InvoiceRequest generateInvoiceDtoItem(Integer invoiceID, String xmlType) throws ZatcaException {
        exceptionNames.clear();
        InvoiceTypeCodeEnum type = InvoiceTypeCodeEnum.valueOf(xmlType);
        if (type == null) {
            throw new ZatcaException(ExceptionConstants.XML_TYPE.getErrorCode(), ExceptionConstants.XML_TYPE.getMessageCode());
        }
        EdsInvoice saleInvoice = invoiceManager.get(invoiceID);
        EdsCrmAccount supplier = saleInvoice.getClientOrSupplier();

        EdsCrmAccount customer = null;
        if (InvoiceTypeCodeEnum.TAX_INVOICE.equals(type) || InvoiceTypeCodeEnum.CREDIT_NOTE.equals(type)) {
            customer = ((EdsSaleInvoice) saleInvoice).getClient();
        }
        // in our case supplier will be current company need to create supplier item from current company
        FinancialSettingsItem financialSettingsItem = accountingService.getCompanyFinancialSettings();
        String currencyCode = "SAR";
        if (saleInvoice.getCurrency() != null && saleInvoice.getCurrency().getName() != null) {
            currencyCode = saleInvoice.getCurrency().getName();
        }
        InvoiceRequest itemDto = new InvoiceRequest();
        itemDto.setId(saleInvoice.getObjectID());
        itemDto.setInvoiceNumber(saleInvoice.getNumber());
        itemDto.setCreditNote(saleInvoice.isCreditNote());
        itemDto.setUuid(UUID.nameUUIDFromBytes(String.valueOf(saleInvoice.getObjectID()).getBytes()));
        itemDto.setPreviousInvoiceHash(generatePreviousInoviceHash());
        /// need to add invoice type code by type invoice, note
        itemDto.setIssueDateAndTime(saleInvoice.getInvoiceDate());
        itemDto.setCurrencyCode(currencyCode);
        itemDto.setDocumentCurrencyCode(currencyCode);
        itemDto.setTaxCurrencyCode(currencyCode);
        itemDto.setSupplier(createSupplierParty(financialSettingsItem));
        if (customer != null) {
            if (customer != null) {
                Integer customerAddressId = customer.getPrimaryBillingAddressId();
                if (customerAddressId == null && customer.getAddresses().size() > 0) {
                    customerAddressId = customer.getAddresses().get(0).getObjectID();
                }
                itemDto.setCustomer(createCustomerParty(customerAddressId, customer.getTrn(), customer.getRegistrationNumber()));
            }
        }
        if (saleInvoice.isCreditNote()) {
            Boolean hasError = false;
            if (!ServerUtils.isNullOrEmpty(saleInvoice.getNoteReason())) {
                itemDto.setNoteReason(saleInvoice.getNoteReason());
            } else {
                hasError = true;
            }

            if (!ServerUtils.isNullOrEmpty(saleInvoice.getNumber())) {
                itemDto.setBillingReferenceId(saleInvoice.getNumber());
            } else {
                hasError = true;
            }
            if (saleInvoice.getNotePaymentCode() != null) {
                itemDto.setPaymentMeansCode(String.valueOf(saleInvoice.getNotePaymentCode()));
            } else {
                hasError = true;
            }
            if (hasError) {
                throw new ZatcaException("Please enter note required fields");
            }
        }
        if (saleInvoice.getInvoiceDate() != null) {
            itemDto.setActualDeliveryDate(saleInvoice.getInvoiceDate());
        }
//        itemDto.setPaymentMeansCode("97");
        itemDto.setTaxTotalList(getTaxTotalList(saleInvoice));
        itemDto.setLegalManetaryTotalItem(calculateAndSetLegalMonetoryTotal(saleInvoice));
        itemDto.setInvoiceLineItems(getInvoiceLineItems(saleInvoice));
        //todo discounts

        return itemDto;
    }

    private String generatePreviousInoviceHash() {
        String pih = invoiceManager.getPreviouseInoiceHash();
        if (!ServerUtils.isNullOrEmpty(pih) && !"null".equalsIgnoreCase(pih)) {
            return pih;
        }
        return ZERO_VALUE_HASH;
    }

    private ArrayList<InvoiceLineItem> getInvoiceLineItems(EdsInvoice saleInvoice) {
        ArrayList<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        if (saleInvoice.getInvoiceItems() != null && saleInvoice.getInvoiceItems().size() > 0) {
            saleInvoice.getInvoiceItems().forEach(item -> invoiceLineItems.add(getInvoiceLineItemRpc(item, saleInvoice.getTaxCalculationType())));
        }

        return invoiceLineItems;
    }

    private InvoiceLineItem getInvoiceLineItemRpc(EdsInvoiceItem invoiceItem, Integer taxCalculationType) {
        InvoiceLineItem lineItem = new InvoiceLineItem();
        lineItem.setInvoicedQuantity(ZatcaUtils.getFormattedValue(invoiceItem.getQty(), DEFAULT_SCALE_NUMBER));
        lineItem.setLineItemId(invoiceItem.getObjectID().toString());
        lineItem.setLineExtensionAmount(ZatcaUtils.getFormattedValue(invoiceItem.getNet(), DEFAULT_SCALE_NUMBER));
        lineItem.setPriceAmount(ZatcaUtils.getFormattedValue(invoiceItem.getUnitPrice(), DEFAULT_SCALE_NUMBER));
        lineItem.setTaxAmount(ZatcaUtils.getFormattedValue(invoiceItem.getTaxAmount(), DEFAULT_SCALE_NUMBER));
        BigDecimal roundingAmount = BigDecimal.ZERO;
        if (invoiceItem.getNet() != null) {
            roundingAmount = roundingAmount.add(invoiceItem.getNet());
        }
        if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType) && invoiceItem.getTaxAmount() != null) {
            roundingAmount = roundingAmount.add(invoiceItem.getTaxAmount());
        }
        lineItem.setRoundingAmount(ZatcaUtils.getFormattedValue(roundingAmount, DEFAULT_SCALE_NUMBER));

        LineItemTo itemTo = new LineItemTo();
        itemTo.setItemName(invoiceItem.getItem().getName());
        BigDecimal rate = ZatcaUtils.getTaxRateValue(invoiceItem.getVat().getEffectiveTaxRate());

        TaxCategory taxCategory = new TaxCategory();
        taxCategory.setCategoryID(ZatcaUtils.getTaxRateCodeByPercent(rate));
        taxCategory.setPercent(rate);
        itemTo.setClassifiedTaxCategory(taxCategory);

        lineItem.setItem(itemTo);
        return lineItem;
    }

    private ArrayList<TaxTotalItem> getTaxTotalList(EdsInvoice saleInvoice) {
        ArrayList<TaxTotalItem> taxTotalList = new ArrayList<>();
        TaxTotalItem taxTotalItem = new TaxTotalItem();
        BigDecimal taxTotal = BigDecimal.ZERO;
        Map<BigDecimal, TaxSubTotalItem> taxSubtotalMap = new HashMap<>();
        for (EdsInvoiceItem item : saleInvoice.getInvoiceItems()) {
            if (item.getVat() != null) {
                BigDecimal taxPercent = BigDecimal.valueOf(item.getVat().getEffectiveTaxRate());
                TaxSubTotalItem subTotalItem = taxSubtotalMap.getOrDefault(taxPercent, new TaxSubTotalItem());

                BigDecimal subtotalTaxAmount = item.getNet().multiply(taxPercent).divide(BigDecimal.valueOf(100), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                subTotalItem.setTaxAmount(subTotalItem.getTaxAmount().add(subtotalTaxAmount));
                subTotalItem.setTaxableAmount(subTotalItem.getTaxableAmount().add(item.getNet().setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));

                if (subTotalItem.getTaxCategory() == null) {
                    TaxCategory taxCategory = new TaxCategory();
                    taxCategory.setCategoryID(ZatcaUtils.getTaxRateCodeByPercent(taxPercent));
                    taxCategory.setPercent(taxPercent);
                    subTotalItem.setTaxCategory(taxCategory);
                }

                taxSubtotalMap.putIfAbsent(taxPercent, subTotalItem);

                taxTotal = taxTotal.add(subtotalTaxAmount);
            }
        }
        taxTotalItem.setTaxAmount(taxTotal);
        if (!taxSubtotalMap.isEmpty()) {
            taxTotalItem.setSubTotalItems(new ArrayList<>(taxSubtotalMap.values()));
        }
        taxTotalList.add(taxTotalItem);

        TaxTotalItem taxTotalItem2 = new TaxTotalItem();
        taxTotalItem2.setTaxAmount(taxTotal);
        taxTotalList.add(taxTotalItem2);

        return taxTotalList;
    }

    private LegalManetaryTotalItem calculateAndSetLegalMonetoryTotal(EdsInvoice saleInvoice) {
        BigDecimal lineExtensionAmount = BigDecimal.ZERO;
        for (EdsInvoiceItem item : saleInvoice.getInvoiceItems()) {
            if (item.getNet() != null) {
                lineExtensionAmount = lineExtensionAmount.add(item.getNet());
            }
        }
        BigDecimal lineExtensionAmountVal = ZatcaUtils.getFormattedValue(lineExtensionAmount, DEFAULT_SCALE_NUMBER);
        BigDecimal taxInclusiveAmount = ZatcaUtils.getFormattedValue(saleInvoice.getTotal(), DEFAULT_SCALE_NUMBER);
        BigDecimal taxExclusiveAmount = ZatcaUtils.getFormattedValue(saleInvoice.getSubtotal(), DEFAULT_SCALE_NUMBER);
        BigDecimal allowanceTotalAmount = ZatcaUtils.getFormattedValue(saleInvoice.getDiscountAmount(), DEFAULT_SCALE_NUMBER);
        BigDecimal payableAmount = ZatcaUtils.getFormattedValue(saleInvoice.getDueAmount(), DEFAULT_SCALE_NUMBER);
        LegalManetaryTotalItem legalManetaryTotalItem = new LegalManetaryTotalItem();
        legalManetaryTotalItem.setAllowanceTotalAmount(allowanceTotalAmount);
        legalManetaryTotalItem.setLineExtensionAmount(lineExtensionAmountVal);
        legalManetaryTotalItem.setTaxInclusiveAmount(taxInclusiveAmount);
        legalManetaryTotalItem.setTaxExclusiveAmount(taxExclusiveAmount);
        legalManetaryTotalItem.setPayableAmount(payableAmount);

        return legalManetaryTotalItem;
    }


    private SupplierAndCustomerParty createSupplierParty(FinancialSettingsItem financialSettingsItem) throws ZatcaException {
        EdsCompany company = companyManager.getCompany(SecurityContext.getCompanyID());
        SupplierAndCustomerParty supplirOrCustomer = new SupplierAndCustomerParty();
        EdsAddress address = company.getBillingAddress();
        String countryCode = company.getCountry().getCode() == null ? "SA" : company.getCountry().getCode();
        String currencyCode = company.getCurrency() != null ? company.getCurrency().getName() : "SAR";
        supplirOrCustomer.setIdentificationID("4030379626");
        PostalAddressItem postalAddressItem = new PostalAddressItem();
        postalAddressItem.setCityName(address.getCity());
        postalAddressItem.setCountryIdentificationCode(countryCode); //address.getCountryCode()

        if (!ServerUtils.isNullOrEmpty(address.getZipCode())) {
            postalAddressItem.setPostalZone(address.getZipCode());
        } else {
            throw new ZatcaException(ExceptionConstants.ZIP_CODE.getMessageCode());
        }
        if (!ServerUtils.isNullOrEmpty(address.getAddress())) {
            postalAddressItem.setStreetName(address.getAddress());
        } else {
            throw new ZatcaException(ExceptionConstants.STREET_NAME.getMessageCode());
        }
        supplirOrCustomer.setPostalAddressItem(postalAddressItem);
        if ("SA".equals(countryCode)) {
            if (!ServerUtils.isNullOrEmpty(address.getPlotIdentification()) && address.getPlotIdentification().length() == 4) {
                postalAddressItem.setPlotIdentification(address.getPlotIdentification());
            } else {
                throw new ZatcaException(ExceptionConstants.PLOT_IDENTIFICATION.getMessageCode());
            }
            if (!ServerUtils.isNullOrEmpty(address.getBuildingNumber()) && address.getBuildingNumber().length() == 4) {
                postalAddressItem.setBuildingName(address.getBuildingNumber());
            } else {
                throw new ZatcaException(ExceptionConstants.BUILDING_NUMBER.getMessageCode());
            }
            if (!ServerUtils.isNullOrEmpty(address.getCitySubdivisionName())) {
                postalAddressItem.setCitySubdivision(address.getCitySubdivisionName());
            } else {
                throw new ZatcaException(ExceptionConstants.CITY_SUBDIVISION_NAME.getMessageCode());
            }
            if (address.getState() != null && !ServerUtils.isNullOrEmpty(address.getStateName())) {
                postalAddressItem.setCountrySubentity(address.getStateName());
            } else {
                throw new ZatcaException(ExceptionConstants.COUNTRY_SUBENTITY.getMessageCode());
            }
        }
        supplirOrCustomer.setPostalAddressItem(postalAddressItem);
        supplirOrCustomer.setRegistrationName(company.getName());
        supplirOrCustomer.setCompanyID(financialSettingsItem.getTaxIdNumber());
        return supplirOrCustomer;
    }

    private SupplierAndCustomerParty createCustomerParty(Integer billigAdressId, String trnNumber, String registrationName) throws ZatcaException {
        SupplierAndCustomerParty supplirOrCustomer = new SupplierAndCustomerParty();
        EdsAddress address = addressManager.get(billigAdressId);
        if (address == null) {
            return supplirOrCustomer;
        }
        String countryCode = address.getCountry() != null ? address.getCountry().getCode() : "SA";
        supplirOrCustomer.setIdentificationID(trnNumber);

        PostalAddressItem postalAddressItem = new PostalAddressItem();
        postalAddressItem.setCityName(address.getCity());
        postalAddressItem.setCountryIdentificationCode(countryCode); //address.getCountryCode()
        if (!ServerUtils.isNullOrEmpty(address.getZipCode())) {
            postalAddressItem.setPostalZone(address.getZipCode());
        } else {
            throw new ZatcaException(ExceptionConstants.ZIP_CODE.getMessageCode());
        }
        if (!ServerUtils.isNullOrEmpty(address.getAddress())) {
            postalAddressItem.setStreetName(address.getAddress());
        } else {
            throw new ZatcaException(ExceptionConstants.STREET_NAME.getMessageCode());
        }
        if ("SA".equals(countryCode)) {
            if (!ServerUtils.isNullOrEmpty(address.getPlotIdentification()) && address.getPlotIdentification().length() == 4) {
                /// need to 4 digit value
                postalAddressItem.setPlotIdentification(address.getPlotIdentification());
            } else {
                throw new ZatcaException(ExceptionConstants.PLOT_IDENTIFICATION.getMessageCode());
            }
            if (!ServerUtils.isNullOrEmpty(address.getBuildingNumber()) && address.getBuildingNumber().length() == 4) {
                postalAddressItem.setBuildingName(address.getBuildingNumber());
            } else {
                throw new ZatcaException(ExceptionConstants.BUILDING_NUMBER.getMessageCode());
            }
            if (!ServerUtils.isNullOrEmpty(address.getCitySubdivisionName())) {
                postalAddressItem.setCitySubdivision(address.getCitySubdivisionName());
            } else {
                throw new ZatcaException(ExceptionConstants.CITY_SUBDIVISION_NAME.getMessageCode());

            }
            if (!ServerUtils.isNullOrEmpty(address.getStateName())) {
                postalAddressItem.setCountrySubentity(address.getStateName());
            } else {
                throw new ZatcaException(ExceptionConstants.STATE_NAME.getMessageCode());
            }
        }
        supplirOrCustomer.setPostalAddressItem(postalAddressItem);
//        supplirOrCustomer.setCompanyID(trnNumber);
        supplirOrCustomer.setRegistrationName(registrationName != null && !registrationName.isEmpty() ? registrationName : "AL KAWTHAR MARKETS");
        return supplirOrCustomer;
    }
}
