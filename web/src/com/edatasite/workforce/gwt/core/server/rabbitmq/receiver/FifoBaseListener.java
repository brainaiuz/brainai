package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsStockTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSServices;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.InvoiceItemManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentItemManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public abstract class FifoBaseListener extends BaseAmqpListener<FifoItem> {
    protected static final Logger log = LoggerFactory.getLogger(FifoBaseListener.class);
    @Autowired
    protected COGSServices cogsServices;
    @Autowired
    protected StockCalcManager stockCalcManager;
    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected ItemManager itemManager;
    @Autowired
    protected StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    protected AccountingServiceLocal accountingServiceLocal;
    @Autowired
    protected XSync<String> stringXSync;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected InvoiceItemManager invoiceItemManager;
    @Autowired
    protected SolrManager solrManager;

    protected static final int PAGE_SIZE = 50;

    @Override
    protected DataMQ<FifoItem> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<FifoItem>>() {
        }.getType());
    }

    @Override
    protected void receiveMessage(FifoItem fifoItem) {
        stringXSync.execute(getSynchronizedKey(fifoItem), () -> {
            try {
                log.info("============================FIFO Rabbit Consumer: {}", this.getTransactionType());
                log.info("COMPANY_ID: {}, TRANSACTION_ID: {}, PRODUCT_ID: {}, QUANTITY: {}, WAREHOUSE: {}, TRANSACTION_ITEM_ID:{}", ServerSecurityContext.getInstance().getCompanyId(), fifoItem.getTransactionId(), fifoItem.getProductId(), fifoItem.getQuantity(), fifoItem.getWarehouserId(), fifoItem.getTransactionItemId());

                if (isValid(fifoItem)) {
                    doAction(fifoItem);
                }
            } catch (Exception e) {
                log.warn("EXCEIPTION ITEM: {}", fifoItem);
                log.info("FIFO EXCEIPTION: {}", e);
            }
        });
    }

    protected abstract void doAction(FifoItem fifoItem);

    protected abstract String getTransactionType();

    protected void reCreateGocsTransactions(List<FifoItem> outStocks) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_FIFO)) {
            return;
        }
        if (!CollectionUtils.isEmpty(outStocks)) {
            for (FifoItem item : outStocks) {
                BigDecimal COGS = cogsServices.getService().createCogsTransaction(item);

                EdsTransaction transaction = transactionManager.get(item.getTransactionId());

                if (transaction instanceof EdsStockTransferTransaction) {
                    correctTransferInStock(item, (EdsStockTransferTransaction) transaction, COGS);
                }
            }
        }
    }

    protected void correctTransferInStock(FifoItem item, EdsStockTransferTransaction transaction, BigDecimal COGS) {
        if (item.getTransactionItemId() == null) {
            ServerSecurityContext.getInstance().setStaticUserID(transaction.getPostedBy() != null ? transaction.getPostedBy().getObjectID() : null);
            accountingServiceLocal.createTransactionForStockTransfer(((EdsStockTransferTransaction) transaction).getStockTransfer());
            ServerSecurityContext.getInstance().setStaticUserID(null);
        } else {
            EdsAdjustmentItem fromItem = stockAdjustmentItemManager.get(item.getTransactionItemId());
            EdsAdjustmentItem toItem = fromItem.getAdjustment().getAdjustmentItemList().get(1);

            EdsItem product = itemManager.get(item.getProductId());
            cogsServices.getService().applyStockTransferToItemStock(product, COGS, toItem.getNewQty(), transaction, toItem.getWarehouse().getObjectID(), toItem.getObjectID(), true);
        }
    }

    protected String getSynchronizedKey(FifoItem item) {
        String key = ServerSecurityContext.getInstance().getCompanyId() + "_" + item.getProductId();

        if (item.getWarehouserId() != null) {
            key += "_" + item.getWarehouserId();
        }
        return key;
    }

    boolean isValid(FifoItem fifoItem) {
        return true;
    }
}
