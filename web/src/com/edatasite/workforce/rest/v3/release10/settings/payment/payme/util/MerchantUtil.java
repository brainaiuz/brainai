package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.settings.payment.payme.EdsPaymeTransaction;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.DetailResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeAccount;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeItem;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CheckPerformTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CheckTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CreateTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.GetStatementResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MerchantUtil {
    private final CompanyCustomFieldsManager companyCFSettingsManager;
    private final ItemCFManager itemCFManager;

    public MerchantUtil(CompanyCustomFieldsManager companyCFSettingsManager, ItemCFManager itemCFManager) {
        this.companyCFSettingsManager = companyCFSettingsManager;
        this.itemCFManager = itemCFManager;
    }

    public CheckPerformTransactionResult getCheckPerformTransactionResult(Boolean isAllow, EdsInvoice invoice) {
        CheckPerformTransactionResult checkPerformTransactionResult = new CheckPerformTransactionResult();


        if (invoice != null && CollectionUtils.isNotEmpty(invoice.getInvoiceItems())) {
            EdsCompanyCustomFieldsSettings spic = companyCFSettingsManager.getByAliasName(ViewName.ProductServiceView.name(), "SPIC");
            EdsCompanyCustomFieldsSettings packageCode = companyCFSettingsManager.getByAliasName(ViewName.ProductServiceView.name(), "PACKAGE_CODE");
            List<PaymeItem> items = new ArrayList<>();
            for (EdsInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                PaymeItem item = new PaymeItem();
                item.setTitle(invoiceItem.getItem().getName());
                item.setPrice(invoiceItem.getUnitPrice().multiply(BigDecimal.valueOf(100)).longValue()); // Test price
                item.setCount(invoiceItem.getQty().intValue());
                if (invoiceItem.getItem().getCustomFields() != null) {
                    if (spic != null) {
                        Object spicValue = itemCFManager.getCustomFieldValue(invoiceItem.getItem().getCustomFields().getObjectID(), spic.getColumnCode());
                        if (spicValue != null) {
                            item.setCode(spicValue.toString()); // Test package code
                        }
                    }
                    if (packageCode != null) {
                        Object packageCodeValue = itemCFManager.getCustomFieldValue(invoiceItem.getItem().getCustomFields().getObjectID(), packageCode.getColumnCode());
                        if (packageCodeValue != null) {
                            item.setPackageCode(packageCodeValue.toString());
                        }
                    }
                }
                if (invoiceItem.getVat() != null) {
                    item.setVatPercent(invoiceItem.getVat().getTaxRate()); // Test VAT percent
                }
                item.setDiscount(invoiceItem.getDiscount());
                items.add(item);
            }


            DetailResult detailResult = new DetailResult();
            detailResult.setReceipt_type(0); // Test receipt type
            detailResult.setItems(items);
            checkPerformTransactionResult.setDetail(detailResult);
        }

        checkPerformTransactionResult.setAllow(isAllow);

        return checkPerformTransactionResult;
    }

    public CreateTransactionResult getCreateTransactionResult(EdsPaymeTransaction transaction) {
        CreateTransactionResult createTransactionResult = new CreateTransactionResult();
        createTransactionResult.setCreate_time(transaction.getCreateTime());
        createTransactionResult.setTransaction(transaction.getId().toString());
        createTransactionResult.setState(transaction.getState().getCode());

        return createTransactionResult;
    }

    public GetStatementResult getStatementResult(EdsPaymeTransaction transaction) {
        GetStatementResult getStatementResult = new GetStatementResult();
        getStatementResult.setId(transaction.getPaycomId());
        getStatementResult.setTime(transaction.getPaycomTime());
        if (transaction.getInvoice() != null) {
            getStatementResult.setAmount(transaction.getInvoice().getSubtotal().longValue());
            getStatementResult.setAccount(new PaymeAccount(transaction.getInvoice().getObjectID().longValue()));
        }
        getStatementResult.setCreateTime(transaction.getCreateTime());
        getStatementResult.setPerformTime(transaction.getPerformTime());
        getStatementResult.setCancelTime(transaction.getCancelTime());
        getStatementResult.setTransaction(transaction.getId().toString());

        if (transaction.getState() != null) {
            getStatementResult.setState(transaction.getState().getCode());
        }
        if (transaction.getReason() != null) {
            getStatementResult.setReason(transaction.getReason().getCode());
        }

        return getStatementResult;
    }

    public CheckTransactionResult getCheckTransactionResult(EdsPaymeTransaction transaction) {
        CheckTransactionResult checkTransactionResult = new CheckTransactionResult();
        checkTransactionResult.setCreate_time(transaction.getCreateTime());
        checkTransactionResult.setPerform_time(transaction.getPerformTime());
        checkTransactionResult.setCancel_time(transaction.getCancelTime());
        checkTransactionResult.setTransaction(transaction.getId().toString());
        if (transaction.getState() != null) {
            checkTransactionResult.setState(transaction.getState().getCode());
        }
        if (transaction.getReason() != null) {
            checkTransactionResult.setReason(transaction.getReason().getCode());
        }

        return checkTransactionResult;
    }
}
