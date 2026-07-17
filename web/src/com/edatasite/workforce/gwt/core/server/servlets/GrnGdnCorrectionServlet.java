package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.server.app.StockValidationService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
public class GrnGdnCorrectionServlet implements HttpRequestHandler {
    @Autowired
    ShippingDataManager shippingDataManager;
    @Autowired
    private StockValidationService stockValidationService;
    @Autowired
    @Qualifier("accountingService")
    protected AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private QuoteManager quoteManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServerSecurityContext.getInstance().setDatabase(request.getParameter("dbname"));
        ServerSecurityContext.getInstance().setCompanyId(request.getParameter("cid"));
        ServerSecurityContext.getInstance().setStaticUserID(Integer.valueOf(request.getParameter("uid")));
        String[] ids = request.getParameter("ids").split(",");

        for (String shIds : ids) {
            EdsShippingData shippingData = shippingDataManager.get(Integer.valueOf(shIds));
            if (shippingData == null || shippingData.isDeleted()) return;
            if (ShippingDataType.OUT.equals(shippingData.getShippingType())) {
                ArrayList<QuantityItem> itemsToValidate = new ArrayList<>();
                shippingData.getItems().forEach(shitem -> {
                    if (!shitem.isDeleted()) {
                        QuantityItem quantityItem = new QuantityItem();
                        quantityItem.setId(shitem.getItem().getObjectID());
                        quantityItem.setWarehouseID(shitem.getWarehouseId());
                        quantityItem.setQuantity(shitem.getApplyingQuantity());
                        itemsToValidate.add(quantityItem);
                    }
                });
                SelectItem[] eroritems = stockValidationService.validateStockAvailability(itemsToValidate.toArray(new QuantityItem[0]), shippingData.getObjectID(), StockOutFlow.FROM_GOODS_DELIVERY_NOTES, null);
                if (eroritems != null && eroritems.length > 0) {
                    StringBuilder itemNames = new StringBuilder();
                    for (int i = 0; i < eroritems.length; i++) {
                        if (i != 0) {
                            itemNames.append(", ");
                        }
                        itemNames.append("\"").append(eroritems[i].getName()).append("\"");
                    }
                    response.getWriter().println(shippingData + " GDN update failed, There are items not enough of " + itemNames + " in your warehouse");
                } else {
                    List<Integer> transactionIds = this.transactionManager.getTransactionIdsByShippings(Collections.singletonList(shippingData.getObjectID()));
                    if (!CollectionUtils.isEmpty(transactionIds)) {
                        this.itemStockManager.deleteItemStocksByTransactionIds(transactionIds);
                        transactionIds.forEach(tId -> transactionManager.deleteTransaction(tId));
                    }
                    EdsSaleQuote saleQuote = quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());
                    accountingServiceLocal.createTransactionForGoodsDelivered(saleQuote, shippingData);
                    response.getWriter().println(shippingData + " GDN update successfully ");
                }
            } else {
                EdsPurchaseOrder purchaseORder = quoteManager.getPurchaseOrderByID(shippingData.getQuote().getObjectID());
                this.accountingServiceLocal.createTransactionsForGoodsReceived(purchaseORder, shippingData, null);
            }
        }

        response.getWriter().println("Company -" + ServerSecurityContext.getInstance().getCompanyId());
        response.getWriter().println("DONE");
        response.setStatus(200);
    }
}
