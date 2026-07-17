package com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.invoice.client.rpc.MultiQuoteConvertItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.common.base.Stopwatch;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Normurod Buriev.
 * Date: 6/8/2020 4:31 PM
 */
public class MultiQuoteConverterUtils {
    static final int THRESHOLD = 500;

    static InvoiceServiceLocal invoiceService;

    static {
        invoiceService = (InvoiceServiceLocal) ApplicationContextProvider.applicationContext.getBean("invoiceService");
    }

    public static List<NewInvoiceItem> getItems(ArrayList<Integer> quoteIds, MultiQuoteConvertItem convertItem) throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newFixedThreadPool(Constants.DEFAULT_THREAD_COUNT_FOR_MULTI_PROCESS);
        CompletionService<List<NewInvoiceItem>> service = new ExecutorCompletionService(exec);

        int pageCount = quoteIds.size() / THRESHOLD + (quoteIds.size() % THRESHOLD > 0 ? 1 : 0);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < pageCount; i++) {
            int offset = i * THRESHOLD;
            int toIndex = i * THRESHOLD + ((i == pageCount - 1) ? quoteIds.size() % THRESHOLD : THRESHOLD);

            service.submit(new QuoteItemsCallableTask(quoteIds.subList(offset, toIndex), convertItem, invoiceService, ServerSecurityContext.getInstance().getSessionId()));
        }
        stopwatch.elapsed(TimeUnit.MILLISECONDS);
        stopwatch.stop();
        System.out.println("Submitted all task to threads: " + stopwatch);

//        AccountItem defaultAccount = invoiceService.getDefaultAccountItem(Constants.SALE_INVOICE, Constants.RECEIVABLE);

        List<NewInvoiceItem> items = new ArrayList<>();
        Map<Integer, NewInvoiceItem> itemsMap = new HashMap<>();


        stopwatch.start();
        for (int i = 0; i < pageCount; i++) {
            Future<List<NewInvoiceItem>> task = service.take();

            if (!convertItem.isGroupByItem()) {
                items.addAll(task.get());
            } else {
                List<NewInvoiceItem> results = task.get();
                results.forEach(item -> {
                    Integer key = generateHashKey(item, convertItem.getGroupingFields());

                    AccountItem chartOfAccountFromProductAndServiceOrDefaultAccount = invoiceService.getChartOfAccountFromProductAndService(Constants.SALE_INVOICE, Constants.RECEIVABLE, item.getItemID());
                    if (itemsMap.get(key) == null) {
                        item.setQuoteItemId(null);
                        item.setDescription(null);

                        if (!convertItem.getGroupingFields().contains(QIGroupingField.PRICE)) {
                            item.setUnitPrice(item.getUnitPrice().multiply(item.getQuantity()));
                            item.setQuantity(BigDecimal.ONE);
                        }
                        if (!convertItem.getGroupingFields().contains(QIGroupingField.ACCOUNT)) {
                            item.setAccountItem(chartOfAccountFromProductAndServiceOrDefaultAccount);
                        }
                        if (!convertItem.getGroupingFields().contains(QIGroupingField.TAX)) {
                            item.setTaxItem(null);
                        }
                        if (!convertItem.getGroupingFields().contains(QIGroupingField.DEPARTMENT)) {
                            item.setDepartmentItem(null);
                        }
                        itemsMap.put(key, item);
                    } else {
                        NewInvoiceItem value = itemsMap.get(key);
                        if (!convertItem.getGroupingFields().contains(QIGroupingField.PRICE)) {
                            BigDecimal unitPrice = item.getUnitPrice().multiply(item.getQuantity());
                            value.setUnitPrice(value.getUnitPrice().add(unitPrice));
                        } else {
                            value.setQuantity(value.getQuantity().add(item.getQuantity()));
                        }
                    }
                });
            }
        }
        stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("All threads done their job: " + stopwatch);

        if (convertItem.isGroupByItem()) {
            items.addAll(itemsMap.values());
        }
        exec.shutdown();
        items.sort(Comparator.comparing(NewInvoiceItem::getFullItemName));
        return items;
    }

    static Integer generateHashKey(NewInvoiceItem item, List<QIGroupingField> groupingFields) {
        StringBuilder keyBuilder = new StringBuilder();
        if (groupingFields.contains(QIGroupingField.ITEM)) {
            keyBuilder.append(QIGroupingField.ITEM.name() + ":")
                    .append(item.getItemID() != null ? item.getItemID().toString() : "")
                    .append(item.getFullItemName());
        }

        if (groupingFields.contains(QIGroupingField.PRICE)) {
            keyBuilder.append("|").append(QIGroupingField.PRICE.name() + ":").append(item.getUnitPrice());
        }

        if (groupingFields.contains(QIGroupingField.ACCOUNT) && item.getAccountItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.ACCOUNT.name() + ":").append(item.getAccountItem().getId());
        }

        if (groupingFields.contains(QIGroupingField.TAX) && item.getTaxItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.TAX.name() + ":").append(item.getTaxItem().getId());
        }

        if (groupingFields.contains(QIGroupingField.DEPARTMENT) && item.getDepartmentItem() != null) {
            keyBuilder.append("|").append(QIGroupingField.DEPARTMENT.name() + ":").append(item.getDepartmentItem().getId());
        }

        return keyBuilder.toString().hashCode();
    }
}
