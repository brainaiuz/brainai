package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.AdjustmentItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ManualEntryDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ManualEntryLineItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.StockAdjustmentDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Normurod Buriev.
 * Date: 2/24/2021 7:34 PM
 */
@Service
public class ApiTransactionService implements ApiConstants {

    private static Logger log = LoggerFactory.getLogger(ApiTransactionService.class);

    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private ManualEntryService manualEntryService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ClientService clientService;

    public Object createStockAdjustment(StockAdjustmentDto dto) throws RestException {
        EdsAccount adjustmentAccount = null;

        if (!dto.getAccount().idIsNull()) {
            adjustmentAccount = accountingManager.get(dto.getAccount().getId());
        }
        if (adjustmentAccount == null && !dto.getAccount().codeIsBlank()) {
            adjustmentAccount = accountingManager.getAccountByCode(dto.getAccount().getCode());
        }
        if (adjustmentAccount == null) {
            throw new RestException(IN_VALID_DATA, "Adjustment Account is not found.", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (dto.getDate().compareTo(financialSettings.getConversionDate()) < 0) {
            throw new RestException(IN_VALID_DATA, "Date cannot be less than conversion date. Coversion date is " + ServerUtils.dateFormat(financialSettings.getConversionDate(), "yyyy MMM dd") + ".", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        if (financialSettings.getBlockBeforeDate() != null && dto.getDate().compareTo(financialSettings.getBlockBeforeDate()) < 0) {
            throw new RestException(IN_VALID_DATA, "Date cannot be less than Blocked Date. Blocked date is " + ServerUtils.dateFormat(financialSettings.getBlockBeforeDate(), "yyyy MMM dd") + ".", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        List<ProductItem> productItems = new ArrayList<>();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
        for (AdjustmentItemDto itemDto : dto.getItems()) {
            ItemDto productDto = itemDto.getProduct();
            EdsItem product = null;
            if (productDto.getId() != null) {
                product = itemManager.get(productDto.getId());
            }
            if (product == null && StringUtils.isNotBlank(productDto.getCode())) {
                product = itemManager.getItemByNumber(productDto.getCode());
            }
            if (product == null && StringUtils.isNotBlank(productDto.getName())) {
                product = itemManager.getItemByName(productDto.getName());
            }
            if (product == null) {
                continue;
            }
            EdsWarehouse warehouse = null;
            if (itemDto.getWarehouse() != null) {
                IdName warehouseDto = itemDto.getWarehouse();

                if (warehouseDto.getId() != null) {
                    warehouse = warehouseManager.get(warehouseDto.getId());
                }
                if (warehouse == null && StringUtils.isNotBlank(warehouseDto.getName())) {
                    warehouse = warehouseManager.getByName(warehouseDto.getName());
                }
                if (warehouse == null) {
                    continue;
                }
            } else {
                warehouse = defaultWarehouse;
            }

            BigDecimal availableQuantityInSystem = itemStockManager.getAvailableStock(product.getObjectID(), warehouse.getObjectID(), null);
            if (availableQuantityInSystem.compareTo(itemDto.getQuantityOnHand()) != 0) {
                BigDecimal adjustmentQuantity = itemDto.getQuantityOnHand().subtract(availableQuantityInSystem);

                ProductItem productItem = new ProductItem();
                productItem.setObjectId(product.getObjectID());
                productItem.setName(product.getName());
                productItem.setDescription(product.getDescription());
                productItem.setWarehouseId(warehouse.getObjectID());
                productItem.setWarehouseName(warehouse.getName());
                productItem.setCurrentQty(availableQuantityInSystem);

                if (adjustmentQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    productItem.setNewQty(adjustmentQuantity);
                    productItem.setUsedQty(BigDecimal.ZERO);
                } else {
                    productItem.setUsedQty(adjustmentQuantity.abs());
                    productItem.setNewQty(BigDecimal.ZERO);
                }
                productItem.setTotalQty(itemDto.getQuantityOnHand());
                productItems.add(productItem);
            }
        }

        if (!CollectionUtils.isEmpty(productItems)) {
            AdjustmentItem adjustmentItem = new AdjustmentItem();

            if (StringUtils.isNotBlank(dto.getNumber())) {
                adjustmentItem.setNumber(dto.getNumber());
            } else {
                BankTransferNumberData numberData = productServiceLocal.generateStockAdjustmentNumberFormat();
                adjustmentItem.setNumber(numberData.getTransferNumber());
                adjustmentItem.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
            }
            adjustmentItem.setDate(dto.getDate() != null ? new DateNonConvertable(dto.getDate()) : new DateNonConvertable());
            adjustmentItem.setAccount(adjustmentAccount.getAsSelectItem());
            adjustmentItem.setMemo(dto.getMemo());
            adjustmentItem.setProductItems(productItems.toArray(new ProductItem[]{}));
            adjustmentItem.setStatusCode(Constants.STOCK_ADJUSTMENT_APPROVED);

            return productServiceLocal.saveStockAdjustment(adjustmentItem);
        }
        return null;
    }

    public void deleteStockAdjustment(IdCode dto) throws RestException {
        EdsStockAdjustment adjustment = null;
        if (dto.getId() != null) {
            adjustment = stockAdjustmentManager.get(dto.getId());
        }
        if (adjustment == null && StringUtils.isNotBlank(dto.getCode())) {
            adjustment = stockAdjustmentManager.getByNumber(dto.getCode());
        }
        if (adjustment == null) {
            throw new RestException(IN_VALID_DATA, "Adjustment is not found by Id/Number.", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        productServiceLocal.deleteStockAdjustment(adjustment.getObjectID());
    }

    @Transactional
    public void createManualEntry(ManualEntryDto manualEntryDto) throws RestException {
        log.debug("Creating new manual entry from {}", manualEntryDto);
        if (manualEntryDto.getId() != null) {
            EdsManualJournal edsManualJournal = manualJournalManager.get(manualEntryDto.getId());
            if (edsManualJournal == null) {
                throw new RestException(ApiConstants.IN_VALID_DATA, "Manual Entry with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
        }
        ManualTransactionData manualTransactionData = manualEntryService.getManualJournalsData(manualEntryDto.getId());
        NewManualTransaction newManualTransaction = manualTransactionData.getManualTransactionItem();
        newManualTransaction.setNarration(manualEntryDto.getNarration());
        Optional.ofNullable(manualEntryDto.getNumber()).ifPresent(number -> newManualTransaction.setNumberData(new NumberData(number, null)));
        newManualTransaction.setDate(new DateNonConvertable(manualEntryDto.getDate()));
        newManualTransaction.setReference(manualEntryDto.getReference());
        if (newManualTransaction.getTransferNumberData() != null) {
            newManualTransaction.setIntNumber(Integer.valueOf(newManualTransaction.getTransferNumberData().getFourDigitNumber()));
        }
        if (manualEntryDto.getStatus() != null) {
            newManualTransaction.setStatus(manualEntryDto.getStatus());
        } else {
            newManualTransaction.setStatus(NewManualTransaction.DRAFT);
        }

        CurrencyItem[] currencyItems = currencyService.getCurrencies(true);
        CurrencyItem baseCurrency = Stream.of(currencyItems).filter(CurrencyItem::isCompanyCurrency).findAny().orElse(null);
        Optional<CurrencyItem> matchedCurrency = Stream.of(currencyItems).filter(c -> c.getName().equalsIgnoreCase(manualEntryDto.getCurrencyCode())).findAny();

        if (matchedCurrency.isPresent()) {
            newManualTransaction.setCurrency(matchedCurrency.get());
            if (manualEntryDto.getExchangeRate() != null) {
                newManualTransaction.setExchangeRate(manualEntryDto.getExchangeRate());
            } else {
                CurrencyLayerItem currencyLayerItem = currencyService.getExchangeRateDouble(baseCurrency.getName(), matchedCurrency.get().getName(), newManualTransaction.getDate() != null ? newManualTransaction.getDate().getDate() : new Date(), 0);
                newManualTransaction.setExchangeRate(BigDecimal.valueOf(currencyLayerItem.getRate()));
            }
        } else {
            newManualTransaction.setCurrency(baseCurrency);
            newManualTransaction.setExchangeRate(BigDecimal.ONE);
        }

        List<NewManualTransactionItem> newManualTransactionItems = new ArrayList<>();
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        for (ManualEntryLineItemDto manualEntryLineItemDto : manualEntryDto.getItems()) {
            NewManualTransactionItem newManualTransactionItem = new NewManualTransactionItem();
            EdsAccount account = null;
            if (manualEntryLineItemDto.getAccount().getId() != null) {
                account = accountingManager.get(manualEntryLineItemDto.getAccount().getId());
            } else if (manualEntryLineItemDto.getAccount().getCode() != null) {
                account = accountingManager.getAccountByCode(manualEntryLineItemDto.getAccount().getCode());
            }
            if (account == null) {
                throw new RestException(IN_VALID_DATA, "Account is not found.", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }
            newManualTransactionItem.setAccountItem(account.createAccountItem());
            if (manualEntryLineItemDto.getCredit() != null && manualEntryLineItemDto.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                newManualTransactionItem.setCredit(manualEntryLineItemDto.getCredit());
                totalCredit = totalCredit.add(manualEntryLineItemDto.getCredit()).setScale(5, BigDecimal.ROUND_HALF_UP);
            } else if (manualEntryLineItemDto.getDebit() != null && manualEntryLineItemDto.getDebit().compareTo(BigDecimal.ZERO) > 0) {
                newManualTransactionItem.setDebit(manualEntryLineItemDto.getDebit());
                totalDebit = totalDebit.add(manualEntryLineItemDto.getDebit()).setScale(5, BigDecimal.ROUND_HALF_UP);
            } else {
                throw new RestException(IN_VALID_DATA, "Both debit and credit cannot be null", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }
            newManualTransactionItem.setDescription(manualEntryLineItemDto.getDescription());
            if (manualEntryLineItemDto.getName() != null) {
                SelectItem selectItem = new SelectItem();
                if (manualEntryLineItemDto.getName().getId() != null) {
                    selectItem.setId(manualEntryLineItemDto.getName().getId());
                } else if (manualEntryLineItemDto.getName().getName() != null) {
                    EdsCrmAccount edsCrmAccount = crmAccountManager.getCrmAccountByName(manualEntryLineItemDto.getName().getName());
                    if (edsCrmAccount != null) {
                        selectItem.setId(edsCrmAccount.getObjectID());
                        selectItem.setName(edsCrmAccount.getName());
                    } else if (AccountingConstants.ACCOUNTS_PAYABLE_KEY.equals(newManualTransactionItem.getAccountItem().getAccountKey())) {
                        CrmAccountItem crmAccountItem = clientService.editAccount(null, Constants.SUPPLIER);
                        crmAccountItem.setName(manualEntryLineItemDto.getName().getName());
                        selectItem.setId(clientService.createSupplier(crmAccountItem, manualJournalManager.getUser().getObjectID()));
                    }
                }
                if (AccountingConstants.SALARY_PAYABLE.equals(newManualTransactionItem.getAccountItem().getAccountKey())) {
                    newManualTransactionItem.setEmployee(selectItem);
                } else {
                    newManualTransactionItem.setCustomerOrSupplier(selectItem);
                }
            }
            if (manualEntryLineItemDto.getBillTo() != null) {
                SelectItem selectItem = new SelectItem();
                if (manualEntryLineItemDto.getBillTo().getId() != null) {
                    selectItem.setId(manualEntryLineItemDto.getBillTo().getId());
                } else if (manualEntryLineItemDto.getBillTo().getName() != null) {
                    EdsCrmAccount edsCrmAccount = crmAccountManager.getCrmAccountByName(manualEntryLineItemDto.getBillTo().getName());
                    if (edsCrmAccount != null) {
                        selectItem.setId(edsCrmAccount.getObjectID());
                        selectItem.setName(edsCrmAccount.getName());
                    }
                }
                newManualTransactionItem.setClient(selectItem);
            }
            if (manualEntryLineItemDto.getProject() != null) {
                SelectItem selectItem = new SelectItem();
                if (manualEntryLineItemDto.getProject().getId() != null) {
                    selectItem.setId(manualEntryLineItemDto.getProject().getId());
                } else if (manualEntryLineItemDto.getProject().getCode() != null) {
                    EdsProject edsProject = projectManager.getProjectByNumber(manualEntryLineItemDto.getProject().getCode());
                    if (edsProject != null) {
                        selectItem.setId(edsProject.getObjectID());
                        selectItem.setName(edsProject.getName());
                    }
                }
                newManualTransactionItem.setProject(selectItem);
            }
            newManualTransactionItems.add(newManualTransactionItem);
        }
        if (totalCredit.compareTo(totalDebit) != 0) {
            throw new RestException(IN_VALID_DATA, "Total credit must be equal to total debit", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        newManualTransaction.setItems(newManualTransactionItems.toArray(new NewManualTransactionItem[]{}));
        manualEntryDto.setId(manualEntryService.saveManualJournal(newManualTransaction));
    }
}
