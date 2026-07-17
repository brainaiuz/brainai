package com.workforcetrack.api.presenter;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.workforcetrack.api.base.RestServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 18.03.13
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PosApiPresenter {
    public static final String OBJECT_ID = "objectID";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CLIENT_ID = "clientID";
    public static final String CURRENCY_ID = "currencyID";
    public static final String EXCHANGE_RATE = "exchangeRate";
    public static final String PO_NUMBER = "poNumber";
    public static final String SUBTOTAL = "subtotal";

    public static final String INVOICE_DATE = "invoiceDate";
    public static final String ACCOUNT_ID = "accountID";
    public static final String QUANTITY = "quantity";
    public static final String TAX_ID = "taxID";
    public static final String UNIT_PRICE = "unitPrice";
    public static final String ITEMS = "items";

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RestServiceUtils restServiceUtils;

    public NewInvoice getPosInvoiceItem(Map<String, Object> map) throws ParseException{
        NewInvoice newInvoice = new NewInvoice();
        if (map != null && !map.isEmpty()) {
            newInvoice.setID(map.get(OBJECT_ID) != null ? (Integer) map.get(OBJECT_ID) : 0);
            if (map.get(INVOICE_DATE) != null) {
                Date invoiceDate = restServiceUtils.getDate((String) map.get(INVOICE_DATE));
                newInvoice.setInvoiceDate(new DateNonConvertable(invoiceDate));
            }
            newInvoice.setDueDate(newInvoice.getInvoiceDate());
            List<Map<String, Object>> invoiceItems = (List<Map<String, Object>>) map.get(ITEMS);

            BigDecimal subTotal = new BigDecimal(BigInteger.ZERO);
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalDiscount = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalInInvoiceCurrency = new BigDecimal(BigInteger.ZERO);
            BigDecimal totalTaxes = new BigDecimal(BigInteger.ZERO);
            BigDecimal exchangeRate = new BigDecimal(BigInteger.ZERO);

            if (invoiceItems != null && invoiceItems.size() > 0) {
                NewInvoiceItem newInvoiceItem = null;
                List<NewInvoiceItem> newInvoiceItems = new ArrayList<>();
                for (Map<String, Object> newInvoiceItemMap : invoiceItems) {
                    newInvoiceItem = new NewInvoiceItem();
					if (newInvoiceItemMap.get(ACCOUNT_ID) != null) {
						newInvoiceItem.setAccountID((Integer)newInvoiceItemMap.get(ACCOUNT_ID));
					} else {
                        AccountItem accountItem = invoiceService.getDefaultAccountItem(null, Constants.RECEIVABLE);
                        if (accountItem != null) {
                            newInvoiceItem.setAccountID(accountItem.getId());
                        }
                    }
					newInvoiceItem.setID((Integer)newInvoiceItemMap.get(OBJECT_ID));
                    newInvoiceItem.setUnitPrice(RestServiceUtils.convertToBigDecimal(newInvoiceItemMap.get(UNIT_PRICE)));
                    newInvoiceItem.setQuantity(RestServiceUtils.convertToBigDecimal(newInvoiceItemMap.get(QUANTITY)));
                    newInvoiceItem.setNet(newInvoiceItem.getUnitPrice().multiply(newInvoiceItem.getQuantity()));
                    newInvoiceItem.setTaxAmount(BigDecimal.ZERO);
                    newInvoiceItem.setDiscountAmount(BigDecimal.ZERO);
                    newInvoiceItem.setDiscountPercent(BigDecimal.ZERO);
                    newInvoiceItems.add(newInvoiceItem);

                    subTotal = subTotal.add(newInvoiceItem.getNet());
                }
                newInvoice.setItems(newInvoiceItems.toArray(new NewInvoiceItem[]{}));
            }

            newInvoice.setSubtotal(subTotal);
            newInvoice.setTotal(subTotal);
            newInvoice.setTotalInInvoiceCurrency(subTotal);
            newInvoice.setTotalDiscount(BigDecimal.ZERO);
            newInvoice.setTotalTaxes(BigDecimal.ZERO);
            newInvoice.setExchageRate(BigDecimal.ONE);
            newInvoice.setBaseCurrency(expenseService.getBaseCurrency());
            newInvoice.setCurrencyID(newInvoice.getBaseCurrency().getId());

            EdsCompany company = companyManager.getUser().getCompany();
            newInvoice.setInvoiceNumber(invoiceCircularResolver.getInvoiceNumberData(company, null).getInvoiceNumber());
            //NEED TO SET CLIENT ID AND IT ADDRESS
            if (map.get(CLIENT_ID) != null) {
                ClientSupplierAddressData addressData = clientService.getAddressData((Integer) map.get(CLIENT_ID), true, Address.EntityType.CrmAccount);
                if (addressData.getPrimaryBillAddressID() != null) {
                    newInvoice.setBillAddressID(addressData.getPrimaryBillAddressID());
                } else {
                    newInvoice.setBillAddressID(addressData.getBillAddresses().length > 0 ? addressData.getBillAddresses()[0].getId() : null);
                }
            }

            newInvoice.setTaxCalculationType(2);
            newInvoice.setStatusCode(Constants.DRAFT);
            newInvoice.setType(Constants.RECEIVABLE);
        }
        return newInvoice;
    }
}
