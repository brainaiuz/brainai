package com.edatasite.workforce.rest.v3.release10.accounting.utils;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelPP;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelPPManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BaseInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InvoiceDtoUtils implements ApiConstants, AccountingConstants {

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    private PriceLevelPPManager priceLevelPPManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    static {
        typeOfSales = Arrays.asList(SALES_INVOICE, SALES_ORDER, SALES_QUOTE, CREDIT_NOTE);
    }

    private static final List<String> typeOfSales;

    @Autowired
    private InvoiceService invoiceService;

    public NewInvoice wrapToInvoiceModel(InvoiceDto dto, String transactionType) throws RestException {
        NewInvoice invoice = wrapToModel(dto, transactionType);

        if (dto.getOrder() != null) {
            IdCode order = dto.getOrder();
            EdsQuote edsQuote = null;
            if (order.getId() != null) {
                edsQuote = quoteManager.get(order.getId());
            }
            if (edsQuote == null && StringUtils.isNotBlank(order.getObjectKey())) {
                edsQuote = quoteManager.getByObjectKey(order.getObjectKey());
            }
            if (edsQuote == null && !order.codeIsBlank()) {
                List edsQuotes = typeOfSales.contains(transactionType) ? quoteManager.getQuoteByNumber(order.getCode().trim()) : quoteManager.getPurchaseOrderByNumber(order.getCode().trim(), null);

                if (!edsQuotes.isEmpty()) {
                    edsQuote = (EdsQuote) edsQuotes.get(0);
                }
            }
            if (edsQuote != null) {
                if (!dto.isExistingObject() && Constants.INVOICED.equals(edsQuote.getStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "Provided quote/order is already converted to invoice!", INVALID, HttpStatus.BAD_REQUEST);
                }
                invoice.setConvertedItemID(edsQuote.getObjectID());

                if (typeOfSales.contains(transactionType)) {
                    invoice.setQuoteNumber(edsQuote.getNumber());
                } else {
                    invoice.setPoNumber(edsQuote.getNumber());
                }
            } else {
                if (typeOfSales.contains(transactionType)) {
                    invoice.setQuoteNumber(order.getCode());
                } else {
                    invoice.setPoNumber(order.getCode());
                }
            }
        }
        if (dto.getAccountsReceivable() != null) {
            IdCode accountsReceivable = dto.getAccountsReceivable();
            EdsAccount edsAccount = null;

            if (accountsReceivable.getId() != null) {
                edsAccount = accountingManager.get(accountsReceivable.getId());
            } else if (StringUtils.isNotBlank(accountsReceivable.getCode())) {
                edsAccount = accountingManager.getAccountByCode(accountsReceivable.getCode().trim());
            }
            if (edsAccount != null) {
                invoice.setAccountsReceivablePayable(edsAccount.createAccountItem());
            }
        }
        if (!CollectionUtils.isEmpty(dto.getPayments())) {
            invoice.setStatusCode(InvoiceStatusEnum.APPROVE.name());
        }
        return invoice;
    }

    public NewInvoice wrapToModel(BaseInvoiceDto dto, String transactionType) {
        int financeCalcScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();
        NewInvoice model = new NewInvoice();
        model.setObjectKey(dto.getObjectKey());
        model.setBookkeep(true);
        model.setType(typeOfSales.contains(transactionType) ? Constants.RECEIVABLE : Constants.PAYABLE);

        EdsCrmAccount customer = null;
        ItemDto customerDto = dto.getCustomer();
        if (customerDto.getId() != null) {
            customer = crmAccountManager.get(customerDto.getId());
        } else if (StringUtils.isNotBlank(customerDto.getCode())) {
            customer = crmAccountManager.getCrmAccountByNumber(customerDto.getCode());
        } else if (StringUtils.isNotBlank(customerDto.getName())) {
            customer = crmAccountManager.getCrmAccountByName(customerDto.getName());
        }

        if (customer != null) {
            model.setClientID(customer.getObjectID());

            Optional.ofNullable(customer.getVat()).ifPresent(vat -> {
                TaxItem customerDefaultVat = vat.createTaxItem();
                dto.getItems().stream().filter(item -> item.getTaxItem() == null).forEach(item -> {
                    item.setTaxItem(new ItemDto(customerDefaultVat.getId(), customerDefaultVat.getName(), customerDefaultVat.getCode()));
                });
            });
        }

        if (StringUtils.isNotBlank(dto.getNumber())) {
            model.setInvoiceNumber(dto.getNumber());
        } else {
            model.setNumberData(getAutoGeneratedNumber(transactionType));
            if (model.getNumberData() != null) {
                model.setInvoiceNumber(model.getNumberData().getInvoiceNumber());
                model.setFourDigitNumber(model.getNumberData().getFourDigitNumber());
            }
        }

        switch (transactionType) {
            case SALES_ORDER -> {
                model.setStatusCode(Constants.SALE_ORDER);
                model.setSalesOrder(true);
            }
            case SALES_QUOTE, PURCHASE_ORDER ->
                    model.setStatusCode(StringUtils.isNotBlank(dto.getStatus()) ? dto.getStatus() : InvoiceStatusEnum.DRAFT.name());
            default -> {
                String status = InvoiceStatusEnum.getStatus(dto.getStatus());
                model.setStatusCode(status != null ? status : InvoiceStatusEnum.DRAFT.name());
            }
        }

        if (dto.getDate() == null) {
            model.setInvoiceDate(new DateNonConvertable(ServerUtils.getCompanyDate(new Date(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())))));
        } else {
            model.setInvoiceDate(new DateNonConvertable(dto.getDate()));
        }
        if (StringUtils.isNotBlank(dto.getDueDateType()) && dto.getDueDateType().equals("TERMS") && dto.getTerms() != null) {
            EdsInvoiceTerms terms = null;
            if (dto.getTerms().getId() != null) {
                terms = invoiceTermsManager.get(dto.getTerms().getId());
            }
            if (terms == null && StringUtils.isNotBlank(dto.getTerms().getName())) {
                terms = invoiceTermsManager.getTermsByName(dto.getTerms().getName());
            }
            model.setDueDate(new DateNonConvertable(ServerUtils.addDays(model.getInvoiceDate().getDate(), terms.getDays())));
            model.setInvoiceTermsItem(terms.getAsRPC());
        } else if (dto.getDueDate() != null) {
            model.setDueDate(new DateNonConvertable(dto.getDueDate()));
        } else if (customer != null && customer.getTerms() != null) {
            model.setDueDate(new DateNonConvertable(ServerUtils.addDays(model.getInvoiceDate().getDate(), customer.getTerms().getDays())));
            model.setInvoiceTermsItem(customer.getTerms().getAsRPC());
        } else {
            model.setDueDate(new DateNonConvertable(ServerUtils.getCompanyDate(new Date(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())))));
        }

        if (dto.getApprover() != null && !dto.getApprover().isEmpty()) {
            ApproverItemMini approver = new ApproverItemMini();
            List<EdsApprover> approvers = approverManager.list("saleinvoice", null);
            if (approvers != null && !approvers.isEmpty()){
                approver.setClonedFrom(approvers.get(0).getObjectID());
            }
            approver.setExactEmployee(new SelectItem(dto.getApprover().get(0).getId()));
            model.setApprovers(new ArrayList<>(Collections.singletonList(approver)));
        }
        if (StringUtils.isNotBlank(dto.getCurrencyCode())) {
            initModelCurrency(dto, model);
        } else {
            CurrencyItem currencyItem = currencyService.getBaseCurrency();
            model.setCurrencyID(currencyItem.getId());
            model.setExchageRate(BigDecimal.ONE);
        }
        model.setReference(dto.getReference());
        model.setTaxCalculationType(getTaxCalcTypeAsInteger(dto.getTaxCalcType()));

        if (dto.getPriceLevel() != null) {
            ItemDto priceLevel = dto.getPriceLevel();

            if (priceLevel.getId() != null) {
                Optional.ofNullable(priceLevelManager.get(priceLevel.getId())).ifPresent(pl -> {
                    pl.getClients().stream().filter(c -> c.getObjectID().equals(model.getClientID())).findAny().ifPresent(c -> model.setPriceLevel(pl.getRPC()));
                });
            } else if (StringUtils.isNotBlank(priceLevel.getName())) {
                Optional.ofNullable(priceLevelManager.getPriceLevelByName(priceLevel.getName().trim())).ifPresent(pl -> {
                    pl.getClients().stream().filter(c -> c.getObjectID().equals(model.getClientID())).findAny().ifPresent(c -> model.setPriceLevel(pl.getRPC()));
                });
            }
        }
        if (dto.getProject() != null) {
            IdCode project = dto.getProject();

            if (project.getId() != null) {
                Optional.ofNullable(projectManager.get(project.getId())).ifPresent(p -> model.setRelatedProject(p.getAsSelectItem()));
            } else if (StringUtils.isNotBlank(project.getCode())) {
                Optional.ofNullable(projectManager.getProjectByNumber(project.getCode())).ifPresent(p -> model.setRelatedProject(p.getAsSelectItem()));
            }
        }

        if (dto.getBankAccount() != null) {
            model.setBankAccount(new SelectItem(dto.getBankAccount().getId()));
        }

        if (dto.getItems() != null && !CollectionUtils.isEmpty(dto.getItems())) {
            if (model.getPriceLevel() != null) {
                dto.setItems(dto.getItems().stream().peek(itm -> itm.getProperties().put(LineItemDto.PRICE_LEVEL, model.getPriceLevel())).collect(Collectors.toList()));
            }
            //initialize items of the Object{SI,SO,SQ,PI,PO}
            model.setItems(getModelItems(dto.getItems(), transactionType, model.getTaxCalculationType()));

            //calculate subtotal
            model.setSubtotal(Stream.of(model.getItems())
                    .peek(item -> item.setNet(item.getNet().divide(model.getExchageRate(), ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP)))
                    .reduce(BigDecimal.ZERO, (substotal, item) -> {
                        return substotal.add(item.getUnitPrice().multiply(item.getQuantity()).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
                    }, BigDecimal::add));
            model.setSubtotal(model.getSubtotal().setScale(financeCalcScale, BigDecimal.ROUND_HALF_UP));

            //calculate total in invoice currency
            model.setTotalInInvoiceCurrency(Stream.of(model.getItems()).reduce(BigDecimal.ZERO, (total, item) -> {
                BigDecimal itemTotal = item.getUnitPrice().multiply(item.getQuantity()).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);

                if (item.getDiscountPercent() != null && item.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal discountAmount = itemTotal.multiply(item.getDiscountPercent()).divide(HUNDRED, ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP);
                    itemTotal = itemTotal.subtract(discountAmount);
                }
                if (TAX_CALCULATION_EXCLUSIVE.equals(model.getTaxCalculationType()) && item.getTaxItem() != null && item.getTaxAmount() != null) {
                    itemTotal = itemTotal.add(item.getTaxAmount());
                }
                total = total.add(itemTotal);
                return total;
            }, BigDecimal::add));
            model.setTotalInInvoiceCurrency(model.getTotalInInvoiceCurrency().setScale(financeCalcScale, BigDecimal.ROUND_HALF_UP));

            //calculate discount total;
            model.setTotalDiscount(Stream.of(model.getItems()).reduce(BigDecimal.ZERO, (totalDiscount, item) -> {
                BigDecimal itemTotal = item.getUnitPrice().multiply(item.getQuantity()).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);

                if (item.getDiscountPercent() != null && item.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal discountAmount = itemTotal.multiply(item.getDiscountPercent()).divide(HUNDRED, ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP);
                    totalDiscount = totalDiscount.add(discountAmount);
                }
                return totalDiscount;
            }, BigDecimal::add));
            model.setTotalDiscount(model.getTotalDiscount().setScale(financeCalcScale, BigDecimal.ROUND_HALF_UP));

            initialize_total_taxes:
            {
                //calculate tax total
                model.setTotalTaxesInInvoiceCurrency(Stream.of(model.getItems()).reduce(BigDecimal.ZERO, (totalTax, item) -> {
                    return totalTax.add(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO);
                }, BigDecimal::add));
                model.setTotalTaxesInInvoiceCurrency(model.getTotalTaxesInInvoiceCurrency().setScale(financeCalcScale, BigDecimal.ROUND_HALF_UP));

                Map<Integer, TotalTaxItem> totalTaxMap = new HashMap<>();
                Stream.of(model.getItems()).filter(item -> item.getTaxItem() != null && item.getTaxAmount() != null).forEach(item -> {
                    TotalTaxItem taxItem = totalTaxMap.getOrDefault(item.getTaxItem().getId(), new TotalTaxItem());
                    taxItem.setTaxItem(item.getTaxItem());
                    taxItem.setTaxAmount(taxItem.getTaxAmount() != null ? taxItem.getTaxAmount().add(item.getTaxAmount()) : BigDecimal.ZERO);
                });
                if (!totalTaxMap.isEmpty()) {
                    model.setTotalTaxItems(totalTaxMap.values().toArray(new TotalTaxItem[]{}));
                }
            }
        } else if (InvoiceStatusEnum.DRAFT.name().equalsIgnoreCase(model.getStatusCode()) && CollectionUtils.isEmpty(dto.getItems())) {
            model.setItems(new NewInvoiceItem[0]);
            model.setTotalInInvoiceCurrency(BigDecimal.ZERO);
            model.setSubtotal(BigDecimal.ZERO);
        }
        model.setTotal(model.getTotalInInvoiceCurrency().divide(model.getExchageRate(), ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP));
        Optional.ofNullable(model.getTotalTaxesInInvoiceCurrency()).ifPresent(tt -> {
            model.setTotalTaxes(tt.divide(model.getExchageRate(), ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP));
        });

        initialize_custom_fiels:
        {
            Map<String, CompanyCustomFieldItem> customFieldsMap = getCustomFields(transactionType).stream().collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (oldOne, newOne) -> oldOne));
            Map<String, CustomFieldRequest> customFieldsFromDtoMap = CollectionUtils.isEmpty(dto.getCustomFields()) ? null : dto.getCustomFields().stream().filter(cf -> cf.getValue() != null).collect(Collectors.toMap(CustomFieldRequest::getAlias, Function.identity(), (oldOne, newOne) -> oldOne));
            ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
            customFieldsMap.keySet().forEach(alise -> {
                CompanyCustomFieldItem customFieldItem = customFieldsMap.get(alise);

                if (!CollectionUtils.isEmpty(customFieldsFromDtoMap)) {
                    CustomFieldRequest cfRequest = customFieldsFromDtoMap.get(alise);

                    if (cfRequest != null) {
                        CompanyCustomFieldItem appliedField = CustomFieldsUtils.applyCustomFieldValue(customFieldItem, cfRequest.getValue());
                        // Ensure the field value is properly set for saving
                        if (appliedField != null) {
                            if (StringUtils.isNotBlank(appliedField.getFieldStringValue())) {
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getSelectedId() != null) {
                                // For lookup fields, ensure the field value is set from selectedId
                                appliedField.setFieldStringValue(String.valueOf(appliedField.getSelectedId()));
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getFieldDateNonConvertedValue() != null) {
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getSelectItems() != null && !appliedField.getSelectItems().isEmpty()) {
                                customFieldItems.add(appliedField);
                            }
                        }
                    }
                } else {//this one is from old logic
                    String fieldValue = dto.getStringValue(alise);

                    if (StringUtils.isNotBlank(fieldValue)) {
                        customFieldItem.setFieldStringValue(fieldValue);
                        customFieldItems.add(customFieldItem);
                    }
                }
            });

            if (!CollectionUtils.isEmpty(customFieldItems)) {
                model.setCustomFieldItems(customFieldItems);
            }
        }

        return model;
    }

    void initModelCurrency(BaseInvoiceDto dto, NewInvoice model) {
        CurrencyItem[] currencyItems = currencyService.getCurrencies(true);
        CurrencyItem baseCurrency = Stream.of(currencyItems).filter(c -> c.isCompanyCurrency()).findAny().orElse(null);
        Optional<CurrencyItem> matchedCurrency = Stream.of(currencyItems).filter(c -> c.getName().equalsIgnoreCase(dto.getCurrencyCode())).findAny();

        if (matchedCurrency.isPresent()) {
            model.setCurrencyID(matchedCurrency.get().getId());
            if (dto.getExchangeRate() != null) {
                model.setExchageRate(dto.getExchangeRate());
            } else {
                CurrencyLayerItem currencyLayerItem = currencyService.getExchangeRateDouble(baseCurrency.getName(), matchedCurrency.get().getName(), model.getInvoiceDate() != null ? model.getInvoiceDate().getDate() : new Date(), 0);
                model.setExchageRate(BigDecimal.valueOf(currencyLayerItem.getRate()));
            }
        } else {
            model.setCurrencyID(baseCurrency != null ? baseCurrency.getId() : null);
            model.setExchageRate(BigDecimal.ONE);
        }
    }

    NewInvoiceItem[] getModelItems(List<LineItemDto> items, String transactionType, Integer taxCalcType) {
        //List of model items
        List<NewInvoiceItem> lineItems = new ArrayList<>();
        Map<String, CompanyCustomFieldItem> customFieldsMap = getLineItemCustomFields(transactionType).stream().collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (oldOne, newOne) -> oldOne));

        if (items.size() > 10) {
            items.parallelStream().forEach(dto -> {
                lineItems.add(wrapToModelLineItem(dto, customFieldsMap, transactionType, taxCalcType));
            });
        } else {
            items.forEach(dto -> {
                lineItems.add(wrapToModelLineItem(dto, customFieldsMap, transactionType, taxCalcType));
            });
        }
        return lineItems.toArray(new NewInvoiceItem[]{});
    }

   public InvoiceNumberData getAutoGeneratedNumber(String transactionType) {
        return switch (transactionType) {
            case SALES_ORDER -> invoiceCircularResolver.getQuoteOrderNumberData(Constants.SALE_ORDER);
            case SALES_QUOTE -> invoiceCircularResolver.getQuoteOrderNumberData(Constants.SALE_QUOTE);
            case SALES_INVOICE -> invoiceCircularResolver.getInvoiceNumberData(null, null);
            case PURCHASE_INVOICE -> invoiceCircularResolver.getPurchaseInvoiceNumberData(false);
            case PURCHASE_ORDER -> invoiceCircularResolver.getQuoteOrderNumberData(Constants.PURCHASE_ORDER);
            case CREDIT_NOTE -> invoiceService.generateNewNumberData(Constants.CREDIT_NOTE, new DateNonConvertable());
            default -> null;
        };
    }

    /**
     * Retrieves Line Item's custom fields by its type(Sales Order, Sale Invoice, ...etc)
     *
     * @param type
     * @return
     */
    public List<CompanyCustomFieldItem> getLineItemCustomFields(String transactionType) {
        return switch (transactionType) {
            case SALES_QUOTE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleQuoteItem));
            case SALES_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleOrderItem));
            case SALES_INVOICE, CREDIT_NOTE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleInvoiceItem));
            case PURCHASE_INVOICE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.PurchaseInvoiceItem));
            case PURCHASE_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.PurchaseOrderItem));
            default -> null;
        };
    }

    /**
     * Retrieves Object(SI,SO,PI..)'s custom fields by its type(Sales Order, Sale Invoice, ...etc)
     *
     * @param type
     * @return
     */
    public List<CompanyCustomFieldItem> getCustomFields(@NotNull String transactionType) {
        return switch (transactionType) {
            case SALES_QUOTE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleQuote));
            case SALES_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleOrder));
            case SALES_INVOICE, CREDIT_NOTE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.SaleInvoice));
            case PURCHASE_INVOICE ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.PurchaseInvoice));
            case PURCHASE_ORDER ->
                    CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.PurchaseOrder));
            default -> null;
        };
    }

    /**
     * Wrapping LineItemDto to NewInvoiceItem and return it
     *
     * @param dto
     * @param customFieldsMap
     * @param type
     * @return
     */
    NewInvoiceItem wrapToModelLineItem(LineItemDto dto, Map<String, CompanyCustomFieldItem> customFieldsMap, String transactionType, Integer taxCalcType) {
        NewInvoiceItem lineItem = new NewInvoiceItem();
        BigDecimal unitPrice = dto.getUnitPrice();
        PriceLevelItem priceLevelItem = (PriceLevelItem) dto.getProperties().get(LineItemDto.PRICE_LEVEL);

        initialize_product:
        {
            if (dto.getProduct() != null) {
                ItemDto productDto = dto.getProduct();

                if (productDto.getId() != null) {
                    Optional.ofNullable(itemManager.get(productDto.getId())).ifPresent(p -> setItemDetails(dto, lineItem, p, priceLevelItem, transactionType));
                }
                if (lineItem.getItemID() == null && StringUtils.isNotBlank(productDto.getCode())) {
                    Optional.ofNullable(itemManager.getItemByNumber(productDto.getCode())).ifPresent(p -> setItemDetails(dto, lineItem, p, priceLevelItem, transactionType));
                }
                if (lineItem.getItemID() == null && StringUtils.isNotBlank(productDto.getName())) {
                    Optional.ofNullable(itemManager.getItemByName(productDto.getName())).ifPresent(p -> setItemDetails(dto, lineItem, p, priceLevelItem, transactionType));
                }
                if (lineItem.getItemID() == null) {
                    lineItem.setItemName(productDto.getName());
                }
            }
        }

        lineItem.setDescription(dto.getDescription());
        lineItem.setQuantity(dto.getQuantity() != null && dto.getQuantity().compareTo(BigDecimal.ZERO) != 0 ? dto.getQuantity() : BigDecimal.ONE);
        if (priceLevelItem == null) {
            lineItem.setUnitPrice(unitPrice != null ? unitPrice : lineItem.getUnitPrice() != null ? lineItem.getUnitPrice() : BigDecimal.ZERO);
        }

        if (dto.getUnitMeasurement() != null && dto.getUnitMeasurement().getId() != null) {
            lineItem.setMeasurement(new SelectItem(dto.getUnitMeasurement().getId()));
        }

        lineItem.setDiscountPercent(dto.getDiscount());
        lineItem.setDiscountItemStaticType(Constants.ONE_OFF_DISCOUNT);

        initialize_net:
        {
            lineItem.setNet(lineItem.getUnitPrice().multiply(lineItem.getQuantity()));
            if (dto.getDiscount() != null && dto.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discountAmount = lineItem.getNet().multiply(dto.getDiscount()).divide(HUNDRED, ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP);
                lineItem.setNet(lineItem.getNet().subtract(discountAmount));
            }
        }


        initialize_chart_of_account:
        {
            if (StringUtils.isNotBlank(dto.getAccountCode())) {
                Optional.ofNullable(accountingManager.getAccountByCode(dto.getAccountCode())).ifPresent(account -> {
                    lineItem.setAccountID(account.getObjectID());
                });
            }
            if (lineItem.getAccountID() == null) {
                EdsAccount systemDefaultAccount = accountingManager.getAccountTypeWithMinCode(typeOfSales.contains(transactionType) ? EdsAccountType.SALES : EdsAccountType.COST_OF_SALES);

                if (systemDefaultAccount != null) {
                    lineItem.setAccountID(systemDefaultAccount.getObjectID());
                }
            }
        }

        initialize_tax:
        {
            if (NO_TAX_CALCULATION.equals(taxCalcType)) {
                lineItem.setTaxItem(null);
                lineItem.setTotalAmount(null);
            } else if (dto.getTaxItem() != null) {
                ItemDto taxDto = dto.getTaxItem();

                if (taxDto.getId() != null) {
                    Optional.ofNullable(vatManager.get(taxDto.getId())).ifPresent(tax -> {
                        lineItem.setTaxItem(tax.createTaxItem());
                    });
                }
                if (lineItem.getTaxItem() == null && StringUtils.isNotBlank(taxDto.getName())) {
                    Optional.ofNullable(vatManager.getVatByName(taxDto.getName())).ifPresent(tax -> lineItem.setTaxItem(tax.createTaxItem()));
                }
                String strTaxAmount = taxDto.getValueByKey("amount");
                if (StringUtils.isNotBlank(strTaxAmount) && lineItem.getTaxItem() != null) {
                    strTaxAmount = strTaxAmount.replaceAll("\\s", "").replace(",", "");
                    lineItem.setTaxAmount(new BigDecimal(strTaxAmount));
                } else if (lineItem.getTaxItem() != null) {
                    TaxItem taxItem = lineItem.getTaxItem();
                    BigDecimal discount = lineItem.getDiscountPercent() == null ? BigDecimal.ONE : BigDecimal.ONE.subtract(lineItem.getDiscountPercent().divide(HUNDRED, 2, RoundingMode.HALF_UP));
                    BigDecimal net = lineItem.getQuantity().multiply(lineItem.getUnitPrice()).multiply(discount);
                    BigDecimal taxAmount = net.multiply(taxItem.getEffectiveTaxPercent())
                            .divide(TAX_CALCULATION_INCLUSIVE.equals(taxCalcType) ? HUNDRED.add(taxItem.getEffectiveTaxPercent()) : HUNDRED,
                                    ServerUtils.getCalculationScale(),
                                    RoundingMode.HALF_UP);
                    lineItem.setTaxAmount(taxAmount);
                }
            }
        }

        initialize_department:
        {
            if (dto.getDepartment() != null) {
                ItemDto departmentDto = dto.getDepartment();

                if (departmentDto.getId() != null) {
                    Optional.ofNullable(departmentManager.get(departmentDto.getId())).ifPresent(d -> {
                        lineItem.setDepartmentItem(d.getAsSelectItem());
                    });
                }
                if (lineItem.getDepartmentItem() == null && StringUtils.isNotBlank(departmentDto.getName())) {
                    List<EdsDepartment> departments = departmentManager.getDepartmentByName(departmentDto.getName().trim());
                    if (!departments.isEmpty()) {
                        lineItem.setDepartmentItem(departments.get(0).getAsSelectItem());
                    }
                }
            }
        }

        initialize_warehouse:
        {
            if (dto.getWarehouseId() != null) {
                EdsWarehouse wr = warehouseManager.get(dto.getWarehouseId());
                Optional.ofNullable(wr).ifPresent(d -> {
                    lineItem.setWarehouse(new SelectItem(wr.getObjectID(), wr.getName()));
                });
            }
        }

        initialize_project:
        if (dto.getProject() != null) {
            IdCode project = dto.getProject();

            if (project.getId() != null) {
                Optional.ofNullable(projectManager.get(project.getId())).ifPresent(p -> lineItem.setProject(p.getAsSelectItem()));
            } else if (StringUtils.isNotBlank(project.getCode())) {
                Optional.ofNullable(projectManager.getProjectByNumber(project.getCode())).ifPresent(p -> lineItem.setProject(p.getAsSelectItem()));
            }
        }

        initialize_custom_fiels:
        {
            Map<String, CustomFieldRequest> customFieldsFromDtoMap = CollectionUtils.isEmpty(dto.getCustomFields()) ? null : dto.getCustomFields().stream().filter(cf -> cf.getValue() != null).collect(Collectors.toMap(CustomFieldRequest::getAlias, Function.identity(), (oldOne, newOne) -> oldOne));
            ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
            customFieldsMap.keySet().forEach(alise -> {
                CompanyCustomFieldItem customFieldItem = customFieldsMap.get(alise).cloneObject();
                if (!CollectionUtils.isEmpty(customFieldsFromDtoMap)) {
                    CustomFieldRequest cfRequest = customFieldsFromDtoMap.get(alise);

                    if (cfRequest != null) {
                        CompanyCustomFieldItem appliedField = CustomFieldsUtils.applyCustomFieldValue(customFieldItem, cfRequest.getValue());
                        // Ensure the field value is properly set for saving
                        if (appliedField != null) {
                            if (StringUtils.isNotBlank(appliedField.getFieldStringValue())) {
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getSelectedId() != null) {
                                // For lookup fields, ensure the field value is set from selectedId
                                appliedField.setFieldStringValue(String.valueOf(appliedField.getSelectedId()));
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getFieldDateNonConvertedValue() != null) {
                                customFieldItems.add(appliedField);
                            } else if (appliedField.getSelectItems() != null && !appliedField.getSelectItems().isEmpty()) {
                                customFieldItems.add(appliedField);
                            }
                        }
                    }
                } else {
                    String fieldValue = dto.getStringValue(alise);

                    if (StringUtils.isNotBlank(fieldValue)) {
                        customFieldItem.setFieldStringValue(fieldValue);
                        customFieldItems.add(customFieldItem);
                    }
                }
            });

            if (!CollectionUtils.isEmpty(customFieldItems)) {
                lineItem.setCustomFieldItems(customFieldItems);
            }
        }

        return lineItem;
    }

    void setItemDetails(LineItemDto dto, NewInvoiceItem lineItem, EdsItem p, PriceLevelItem priceLevelItem, String transactionType) {
        lineItem.setItemID(p.getObjectID());
        lineItem.setItemType(p.getType());
        lineItem.setItemName(p.getName());
        setItemPrice(lineItem, p, transactionType, priceLevelItem);

        if (dto.getTaxItem() == null && p.getVat() != null) {
            TaxItem productDefaultVat = p.getVat().createTaxItem();
            dto.setTaxItem(new ItemDto(productDefaultVat.getId(), productDefaultVat.getName(), productDefaultVat.getCode()));
        }
    }

    void setItemPrice(NewInvoiceItem lineItem, EdsItem item, String transactionType, PriceLevelItem priceLevelItem) {
        if (typeOfSales.contains(transactionType)) {
            if (priceLevelItem != null && item.getSellingPrice() != null) {
                if (Constants.FIXED_PERCENTAGE.equals(priceLevelItem.getType())) {
                    BigDecimal effectiveRate = priceLevelItem.getPLCase() != Constants.DECREASE ? HUNDRED.add(BigDecimal.valueOf(priceLevelItem.getPercent())) : HUNDRED.subtract(BigDecimal.valueOf(priceLevelItem.getPercent()));
                    effectiveRate = effectiveRate.divide(HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
                    lineItem.setUnitPrice(item.getSellingPrice().multiply(effectiveRate));
                } else if (Constants.PER_PRODUCT.equals(priceLevelItem.getType())) {
                    EdsPriceLevelPP priceLevelPP = priceLevelPPManager.getByPriceLevelIdAndProductId(priceLevelItem.getId(), item.getObjectID());
                    if (priceLevelPP != null) {
                        lineItem.setUnitPrice(priceLevelPP.getCustomPrice() != null ? BigDecimal.valueOf(priceLevelPP.getCustomPrice()) : null);
                    } else {
                        lineItem.setUnitPrice(item.getSellingPrice());
                    }
                }
            } else {
                lineItem.setUnitPrice(item.getSellingPrice());
            }
        } else {
            if (priceLevelItem != null && item.getSellingPrice() != null) {
                if (Constants.FIXED_PERCENTAGE.equals(priceLevelItem.getType())) {
                    BigDecimal effectiveRate = priceLevelItem.getPLCase() != Constants.DECREASE ? HUNDRED.add(BigDecimal.valueOf(priceLevelItem.getPercent())) : HUNDRED.subtract(BigDecimal.valueOf(priceLevelItem.getPercent()));
                    effectiveRate = effectiveRate.divide(HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
                    lineItem.setUnitPrice(item.getUnitPrice().multiply(effectiveRate));
                } else if (Constants.PER_PRODUCT.equals(priceLevelItem.getType())) {
                    EdsPriceLevelPP priceLevelPP = priceLevelPPManager.getByPriceLevelIdAndProductId(priceLevelItem.getId(), item.getObjectID());
                    if (priceLevelPP != null) {
                        lineItem.setUnitPrice(priceLevelPP.getCustomPrice() != null ? BigDecimal.valueOf(priceLevelPP.getCustomPrice()) : null);
                    } else {
                        lineItem.setUnitPrice(item.getUnitPrice());
                    }
                }
            } else {
                lineItem.setUnitPrice(item.getUnitPrice());
            }
        }
    }

    Integer getTaxCalcTypeAsInteger(String taxCalcType) {
        if (StringUtils.isBlank(taxCalcType)) {
            return TAX_CALCULATION_EXCLUSIVE;
        }
        TaxTypeEnum type = TaxTypeEnum.valueOf(taxCalcType);

        return type != null ? type.getId() : TAX_CALCULATION_EXCLUSIVE;
    }
}
