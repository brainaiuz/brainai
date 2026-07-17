package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 5/13/15 8:02 PM
 */
public class ManualEntryTO implements IsSerializable {
    Integer id;
    String number;
    String narration;
    Long date;
    Long endDate;
    BigDecimal totalDebit;
    BigDecimal totalCredit;
    SelectItemTO status;
    String reference;
    SelectItemTO type;
    String repeat;
    Long nextCreationDate;
    BigDecimal exchangeRate;
    SelectItemTO currency;
    SelectItemTO baseCurrency;
    ArrayList<ManualEntryItemTO> items;

    public ManualEntryTO() {
    }

    public ManualEntryTO(ManualJournalListItem item) {
        this.id = item.getObjectId();
        this.number = item.getNumber();
        this.narration = item.getNarration();
        this.totalDebit = item.getDebit();
        this.totalCredit = item.getCredit();
        this.date = WrapUtils.dateToLong(item.getDate().getNonConvertedDate());
        this.status = new SelectItemTO(item.getStatus());
        this.reference = item.getReferenceNumber();
        this.endDate = WrapUtils.dateToLong(item.getEndDate());
        this.nextCreationDate = WrapUtils.dateToLong(item.getNextCreationDate());
        this.repeat = item.getRepeats();
    }

    public ManualEntryTO(NewManualTransaction item) {
        this.id = item.getObjectId();
        this.number = item.getNumber();
        this.narration = item.getNarration();
        this.date = WrapUtils.dateToLong(item.getDate().getNonConvertedDate());
        this.status = new SelectItemTO(item.getStatus());
        this.reference = item.getReference();
        this.totalCredit = item.getCreditTotal();
        this.totalDebit = item.getDebitTotal();
        this.exchangeRate = item.getExchangeRate();
        this.currency = item.getCurrency() == null ? null : new SelectItemTO(item.getCurrency().getId(), item.getCurrency().getName(), item.getCurrency().getSymbol(), "");
        this.baseCurrency = new SelectItemTO(item.getBaseCurrency().getId(), item.getBaseCurrency().getName(), item.getBaseCurrency().getSymbol(), "");
        for (NewManualTransactionItem lineItem : item.getItems()) {
            this.getItems().add(new ManualEntryItemTO(lineItem));
        }
    }

    public NewManualTransaction wrap(ManualEntryTO manualEntryTO) {
        NewManualTransaction item = new NewManualTransaction();
        item.setObjectId(manualEntryTO.getId());
        item.setNumber(manualEntryTO.getNumber());
        item.setNarration(manualEntryTO.getNarration());
        item.setReference(manualEntryTO.getReference());
        item.setExchangeRate(manualEntryTO.getExchangeRate());
        item.setCreditTotal(manualEntryTO.getTotalCredit());
        item.setDebitTotal(manualEntryTO.getTotalDebit());
        item.setDate(new DateNonConvertable(WrapUtils.longToDate(manualEntryTO.getDate())));
        if (manualEntryTO.getCurrency() != null) {
            item.setCurrency(new CurrencyItem(manualEntryTO.getCurrency().getId(), manualEntryTO.getCurrency().getName(), manualEntryTO.getCurrency().getCode()));
        }
        item.setBaseCurrency(new CurrencyItem(manualEntryTO.getBaseCurrency().getId(), manualEntryTO.getBaseCurrency().getName(), manualEntryTO.getBaseCurrency().getCode()));
        NewManualTransactionItem[] manualTransactionItems = new NewManualTransactionItem[manualEntryTO.getItems().size()];
        int i = 0;
        for (ManualEntryItemTO lineItem : manualEntryTO.getItems()) {
            manualTransactionItems[i] = lineItem.wrap(lineItem);
            i++;
        }
        item.setItems(manualTransactionItems);
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Long getNextCreationDate() {
        return nextCreationDate;
    }

    public void setNextCreationDate(Long nextCreationDate) {
        this.nextCreationDate = nextCreationDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public SelectItemTO getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItemTO currency) {
        this.currency = currency;
    }

    public SelectItemTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(SelectItemTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public ArrayList<ManualEntryItemTO> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(ArrayList<ManualEntryItemTO> items) {
        this.items = items;
    }
}
