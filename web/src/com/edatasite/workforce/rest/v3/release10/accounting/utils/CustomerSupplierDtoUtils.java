package com.edatasite.workforce.rest.v3.release10.accounting.utils;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BankDetailsDto;
import com.edatasite.workforce.rest.v3.release10.accounting.request.CustomerSupplierRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Normurod Buriev.
 * Date: 1/11/2021 8:59 AM
 */
@Component
public class CustomerSupplierDtoUtils {
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private BankAccountManager bankAccountManager;

    public CrmAccountItem wrapGeneralInfoRequestToModel(CustomerSupplierRequest request) {
        CrmAccountItem customer = new CrmAccountItem();
        customer = wrapToModel(request, customer);

        if (StringUtils.isNotBlank(request.getBankAccount())) {
            EdsAccount account = accountingManager.getAccountByName(request.getBankAccount());
            if (account != null) {
                EdsBankAccount bankAccount = bankAccountManager.getBankAccountByAccountID(account.getObjectID());
                if (bankAccount != null) {
                    customer.setBankAccountId(bankAccount.getObjectID());
                }
            }
        }
        if (request.getBalance() != null) {
            customer.setBalanceAmount(request.getBalance().doubleValue());
        }
        if (request.getBalanceAsOfDate() != null) {
            customer.setBalanceDate(new DateNonConvertable(request.getBalanceAsOfDate()));
        }
        customer.setCreditLimit(request.getCreditLimit());
        return customer;
    }

    public CrmAccountItem wrapGeneralInfoRequestToModel(CustomerSupplierRequest request, EdsCrmAccount crmAccount) {
        CrmAccountItem crmAccountItem = crmAccount.getRPC(null, false);

        if (StringUtils.isNotBlank(request.getName())) {
            crmAccountItem.setName(request.getName());
        }
        if (StringUtils.isNotBlank(request.getNumber())) {
            crmAccountItem.setNumber(request.getNumber());
        }
        if (StringUtils.isNotBlank(request.getBankAccount())) {
            EdsAccount account = accountingManager.getAccountByName(request.getBankAccount());
            if (account != null) {
                EdsBankAccount bankAccount = bankAccountManager.getBankAccountByAccountID(account.getObjectID());
                if (bankAccount != null) {
                    crmAccountItem.setBankAccountId(bankAccount.getObjectID());
                }
            }
        }
        if (request.getPaymentMethod() != null) {
            Optional.ofNullable(paymentMethodManager.getByName(request.getPaymentMethod().getName())).ifPresent(edsPaymentMethod -> {
                crmAccountItem.setPaymentMethodId(edsPaymentMethod.getObjectID());
                crmAccountItem.setPaymentMethod(edsPaymentMethod.getName());
            });
            Optional.ofNullable(paymentMethodManager.get(request.getPaymentMethod().getId())).ifPresent(edsPaymentMethod -> {
                crmAccountItem.setPaymentMethodId(edsPaymentMethod.getObjectID());
                crmAccountItem.setPaymentMethod(edsPaymentMethod.getName());
            });
        }
        if (StringUtils.isNotBlank(request.getIndustry())) {
            EdsReference industry = referenceManager.findReference("_COMPANY_WORKAREA", request.getIndustry());

            if (industry == null) {
                industry = referenceManager.findByParentCodeAndName("_COMPANY_WORKAREA", request.getIndustry());
            }
            if (industry != null) {
                crmAccountItem.setIndustryID(industry.getObjectID());
                crmAccountItem.setIndustry(industry.getName());
            }
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            crmAccountItem.setEmail(request.getEmail());
        }
        Optional.ofNullable(request.getPhone()).ifPresent(phone -> crmAccountItem.setPhone(phone));
        Optional.ofNullable(request.getWebsite()).ifPresent(websiteUrl -> crmAccountItem.setWebsite(websiteUrl));
        Optional.ofNullable(request.getCurrency()).ifPresent(currency -> {
            CurrencyItem[] currencyItems = currencyService.getCurrencies(true);
            Optional<CurrencyItem> matchedCurrency = Stream.of(currencyItems).filter(c -> c.getName().equalsIgnoreCase(request.getCurrency())).findAny();
            matchedCurrency.ifPresent(currencyItem -> applyCurrencyItem(currencyItem, crmAccountItem));
        });
        Optional.ofNullable(request.getFax()).ifPresent(fax -> crmAccountItem.setFax(fax));
        Optional.ofNullable(request.getCreditLimit()).ifPresent(creditLimit -> crmAccountItem.setCreditLimit(creditLimit));
        if (request.getTerms() != null) {
            Optional.ofNullable(request.getTerms().getName()).ifPresent(termsName -> Optional.ofNullable(invoiceTermsManager.getTermsByName(termsName)).ifPresent(edsInvoiceTerms -> crmAccountItem.setTermsItem(edsInvoiceTerms.getAsSelectItem())));
            Optional.ofNullable(request.getTerms().getId()).ifPresent(termsId -> Optional.ofNullable(invoiceTermsManager.get(termsId)).ifPresent(edsInvoiceTerms -> crmAccountItem.setTermsItem(edsInvoiceTerms.getAsSelectItem())));
        }
        if (!CollectionUtils.isEmpty(request.getBillingAddresses())) {
            List<Address> addresses = getAddressList(request.getBillingAddresses(), null);
            crmAccountItem.setBillAddresses(addresses.toArray(new Address[]{}));
        }
        if (!CollectionUtils.isEmpty(request.getShippingAddresses())) {
            List<Address> addresses = getAddressList(request.getShippingAddresses(), null);
            crmAccountItem.setMailAddresses(addresses.toArray(new Address[]{}));
        }
        if (request.getBankDetails() != null) {
            BankDetailsDto bankDetails = request.getBankDetails();
            Optional.ofNullable(bankDetails.getBankName()).ifPresent(bankName -> crmAccountItem.setBankName(bankName));
            Optional.ofNullable(bankDetails.getAccountName()).ifPresent(accountName -> crmAccountItem.setAccountName(accountName));
            Optional.ofNullable(bankDetails.getAccountNumber()).ifPresent(accountNumber -> crmAccountItem.setAccountNo(accountNumber));
            Optional.ofNullable(bankDetails.getSwiftCode()).ifPresent(swift -> crmAccountItem.setSwiftCode(swift));
            Optional.ofNullable(bankDetails.getSortCode()).ifPresent(sort -> crmAccountItem.setSortCode(sort));
            Optional.ofNullable(bankDetails.getIban()).ifPresent(iban -> crmAccountItem.setIbanCode(iban));
            Optional.ofNullable(bankDetails.getBranch()).ifPresent(branch -> crmAccountItem.setBranch(branch));
            Optional.ofNullable(bankDetails.getBankAddress()).ifPresent(bankAddress -> crmAccountItem.setBankAddress(bankAddress));
        }
        if (!CollectionUtils.isEmpty(request.getCustomFields())) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.convertCustomFields(request.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount), crmAccount.getCustomFields());
            if (!CollectionUtils.isEmpty(customFieldItems)) {
                crmAccountItem.setCustomFields(customFieldItems);
            }
        }else {
            crmAccountItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(crmAccount.getCustomFields(),
                    commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount)));
        }

        if (request.getAccountTypes() != null && request.getAccountTypes().size() > 0){
            SelectItem[] selectItems= request.getAccountTypes()
                    .stream()
                    .map(x -> new SelectItem(x.getId(),x.getName(),x.getCode(),true)).collect(Collectors.toList()).toArray(new SelectItem[request.getAccountTypes().size()]);
            crmAccountItem.setAccountTypes(selectItems);
        }
        return crmAccountItem;
    }

    public CrmAccountItem wrapSupplierRequestToModel(CustomerSupplierRequest request) {
        CrmAccountItem supplier = new CrmAccountItem();
        supplier = wrapToModel(request, supplier);

        if (request.getBalance() != null) {
            supplier.setSupplierBalance(request.getBalance().doubleValue());
        }
        if (request.getBalanceAsOfDate() != null) {
            supplier.setSupplierBalanceDate(new DateNonConvertable(request.getBalanceAsOfDate()));
        }
        if (request.getBankDetails() != null) {
            BankDetailsDto bankDetails = request.getBankDetails();
            supplier.setBankName(bankDetails.getBankName());
            supplier.setAccountName(bankDetails.getAccountName());
            supplier.setAccountNo(bankDetails.getAccountNumber());
            supplier.setSwiftCode(bankDetails.getSwiftCode());
            supplier.setSortCode(bankDetails.getSortCode());
            supplier.setIbanCode(bankDetails.getIban());
            supplier.setBranch(bankDetails.getBranch());
            supplier.setBankAddress(bankDetails.getBranch());
        }
        supplier.setCreditLimit(request.getCreditLimit());
        return supplier;
    }

    CrmAccountItem wrapToModel(CustomerSupplierRequest request, final CrmAccountItem crmAccountItem) {
        if (!CollectionUtils.isEmpty(request.getOwners())) {
            List<SelectItem> ownerList = new ArrayList<>();
            request.getOwners().stream().filter(own -> own.getId() != null || StringUtils.isNotBlank(own.getCode())).forEach(own -> {
                SelectItem owner = new SelectItem();
                if (own.getId() != null) {
                    Optional.ofNullable(employeeManager.get(own.getId())).ifPresent(emp -> {
                        owner.setId(emp.getObjectID());
                        owner.setName(emp.getFullName());
                    });
                }
                if (owner.getId() == null && StringUtils.isNotBlank(own.getCode())) {
                    Optional.ofNullable(employeeManager.getEmployeeByNumber(own.getCode())).ifPresent(emp -> {
                        owner.setId(emp.getObjectID());
                        owner.setName(emp.getFullName());
                    });
                }
                if (owner.getId() != null) {
                    ownerList.add(owner);
                }
            });
            if (!CollectionUtils.isEmpty(ownerList)) {
                crmAccountItem.setOwnerItems(ownerList.toArray(new SelectItem[]{}));
            }
        }
        crmAccountItem.setObjectKey(request.getObjectKey());
        crmAccountItem.setName(request.getName());
        crmAccountItem.setNumber(request.getNumber());

        if (StringUtils.isNotBlank(request.getIndustry())) {
            EdsReference industry = referenceManager.findReference("_COMPANY_WORKAREA", request.getIndustry());

            if (industry == null) {
                industry = referenceManager.findByParentCodeAndName("_COMPANY_WORKAREA", request.getIndustry());
            }
            if (industry != null) {
                crmAccountItem.setIndustryID(industry.getObjectID());
                crmAccountItem.setIndustry(industry.getName());
            }
        }
        crmAccountItem.setEmail(request.getEmail());
        crmAccountItem.setPhone(request.getPhone());
        crmAccountItem.setWebsite(request.getWebsite());
        crmAccountItem.setFax(request.getFax());

        if (!CollectionUtils.isEmpty(request.getBillingAddresses())) {
            List<Address> addresses = getAddressList(request.getBillingAddresses(), null);
            crmAccountItem.setBillAddresses(addresses.toArray(new Address[]{}));
        } else {
            crmAccountItem.setBillAddresses(null);
        }
        if (!CollectionUtils.isEmpty(request.getShippingAddresses())) {
            List<Address> addresses = getAddressList(request.getShippingAddresses(), null);
            crmAccountItem.setMailAddresses(addresses.toArray(new Address[]{}));
        } else {
            crmAccountItem.setMailAddresses(null);
        }
        if (!CollectionUtils.isEmpty(request.getNotes())) {
            crmAccountItem.setNotes(getNoteList(request.getNotes(), null));
        } else {
            crmAccountItem.setNotes(null);
        }
        crmAccountItem.setVatNumber(request.getVatNumber());

        CurrencyItem baseCurrency = currencyService.getBaseCurrency();
        if (StringUtils.isBlank(request.getCurrency())) {
            applyCurrencyItem(baseCurrency, crmAccountItem);
        } else {
            CurrencyItem[] currencyItems = currencyService.getCurrencies(true);
            Optional<CurrencyItem> matchedCurrency = Stream.of(currencyItems).filter(c -> c.getName().equalsIgnoreCase(request.getCurrency())).findAny();
            if (matchedCurrency.isPresent()) {
                applyCurrencyItem(matchedCurrency.get(), crmAccountItem);
            } else {
                applyCurrencyItem(baseCurrency, crmAccountItem);
            }
        }
        if (request.getTax() != null) {
            Optional.ofNullable(request.getTax().getName()).ifPresent(taxName -> Optional.ofNullable(vatManager.getVatByName(taxName)).ifPresent(edsVat -> crmAccountItem.setVat(edsVat.createTaxItem())));
            Optional.ofNullable(request.getTax().getId()).ifPresent(taxId -> Optional.ofNullable(vatManager.get(taxId)).ifPresent(edsVat -> crmAccountItem.setVat(edsVat.createTaxItem())));
        } else {
            crmAccountItem.setVat(null);
        }
        if (request.getTerms() != null) {
            Optional.ofNullable(request.getTerms().getName()).ifPresent(termsName -> Optional.ofNullable(invoiceTermsManager.getTermsByName(termsName)).ifPresent(edsInvoiceTerms -> crmAccountItem.setTermsItem(edsInvoiceTerms.getAsSelectItem())));
            Optional.ofNullable(request.getTerms().getId()).ifPresent(termsId -> Optional.ofNullable(invoiceTermsManager.get(termsId)).ifPresent(edsInvoiceTerms -> crmAccountItem.setTermsItem(edsInvoiceTerms.getAsSelectItem())));
        } else {
            crmAccountItem.setTermsItem(null);
        }
        if (request.getPaymentMethod() != null) {
            Optional.ofNullable(paymentMethodManager.getByName(request.getPaymentMethod().getName())).ifPresent(edsPaymentMethod -> {
                crmAccountItem.setPaymentMethodId(edsPaymentMethod.getObjectID());
                crmAccountItem.setPaymentMethod(edsPaymentMethod.getName());
            });
            Optional.ofNullable(paymentMethodManager.get(request.getPaymentMethod().getId())).ifPresent(edsPaymentMethod -> {
                crmAccountItem.setPaymentMethodId(edsPaymentMethod.getObjectID());
                crmAccountItem.setPaymentMethod(edsPaymentMethod.getName());
            });
        } else {
            crmAccountItem.setPaymentMethodId(null);
            crmAccountItem.setPaymentMethod(null);
        }
        if (!CollectionUtils.isEmpty(request.getCustomFields())) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.convertCustomFields(request.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount), null);
            crmAccountItem.setCustomFields(customFieldItems);
        } else {
            crmAccountItem.setCustomFields(null);
        }
        return crmAccountItem;
    }

    List<Address> getAddressList(List<AddressDto> addressDtoList, Integer entityId) {
        ArrayList<Address> addresses = new ArrayList<>();
        for (AddressDto addressDto : addressDtoList) {
            Optional.ofNullable(addressDto.getCountry()).ifPresent(countryName -> {
                EdsCountry country = countryManager.getCountryByName(countryName);
                if (country != null) {
                    addressDto.setCountryId(country.getObjectID());
                    addressDto.setCountryCode(country.getCode());
                }
            });
            Optional.ofNullable(addressDto.getState()).ifPresent(state -> {
                EdsRegion region = regionManager.getRegionByName(state);
                if (region != null) {
                    addressDto.setStateId(region.getObjectID());
                }
            });
            addressDto.setEntityId(entityId);
            addressDto.setEntityType(EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            addresses.add(ConvertUtils.toEntity(addressDto));
        }
        return addresses;
    }

    ArrayList<HistoryListItem> getNoteList(List<NoteDto> noteDtoList, Integer entityId) {
        ArrayList<HistoryListItem> notes = new ArrayList<>();
        for (NoteDto noteDto : noteDtoList) {
            noteDto.setEntityId(entityId);
            notes.add(ConvertUtils.toEntity(noteDto, employeeManager.getUser().getName()));
        }
        return notes;
    }


    void applyCurrencyItem(CurrencyItem currencyItem, CrmAccountItem crmAccountItem) {
        crmAccountItem.setCurrencyId(currencyItem.getId());
        crmAccountItem.setCurrency(currencyItem.getName());
    }
}
